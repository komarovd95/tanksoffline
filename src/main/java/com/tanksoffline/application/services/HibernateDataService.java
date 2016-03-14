package com.tanksoffline.application.services;

import com.tanksoffline.core.data.ActiveRecord;
import com.tanksoffline.core.services.DataService;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class HibernateDataService implements DataService {
    private SessionFactory sessionFactory;
    private Session session;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T save(T item) {
        return UnitOfWork.doWork(session, () -> (T) session.save(item));
    }

    @Override
    public <T> T remove(T item) {
        UnitOfWork.doWork(session, () -> session.delete(item));
        return item;
    }

    @Override
    public <T> T update(T item) {
        UnitOfWork.doWork(session, () -> session.update(item));
        return item;
    }

    @Override
    public <T> T refresh(T item) {
        UnitOfWork.doWork(session, () -> session.refresh(item));
        return item;
    }

    @Override
    public <T> T find(Class<T> itemClass, Object id) {
        return UnitOfWork.doWork(session, () -> session.get(itemClass, (Serializable) id));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T fetch(T item, String... fetchedFields) {
        Criteria criteria = session.createCriteria(item.getClass());
        for (String field : fetchedFields) {
            criteria = criteria.setFetchMode(field, FetchMode.JOIN);
        }
        criteria.add(Restrictions.idEq(((ActiveRecord) item).getId()));
        return (T) criteria.uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> where(Class<T> itemClass, Map<String, Object> params) {
        Criteria criteria = session.createCriteria(itemClass);
        criteria.add(Restrictions.allEq(params));
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> where(Class<T> itemClass, String paramName, Object paramValue) {
        Criteria criteria = session.createCriteria(itemClass);
        criteria.add(Restrictions.eq(paramName, paramValue));
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> where(String query, Object... params) {
        Query q = session.createQuery(query);
        for (int i = 0; i < params.length; i++) {
            q.setParameter(i, params[i]);
        }
        return q.list();
    }

    @Override
    public void start() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
        session = sessionFactory.openSession();
    }

    @Override
    public void shutdown() {
        session.close();
        sessionFactory.close();
    }




    static class UnitOfWork {
        static void doWork(Session session, Runnable work) {
            doWork(session, () -> {
                work.run();
                return null;
            });
        }

        static <T> T doWork(Session session, Callable<T> work) {
            Transaction tx = null;
            T item;
            try {
                tx = session.beginTransaction();
                item = work.call();
                tx.commit();
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new RuntimeException(e);
            }
            return item;
        }
    }
}
