package com.tanksoffline.services;

import com.tanksoffline.data.core.ActiveRecord;
import com.tanksoffline.services.core.DataService;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
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
    public void start() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
        session = sessionFactory.openSession();
    }

    @Override
    public void restart() {
        shutdown();
        start();
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
