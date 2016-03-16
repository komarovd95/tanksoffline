package com.tanksoffline.application.services;

import com.tanksoffline.core.data.ActiveRecord;
import com.tanksoffline.core.services.DataService;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.util.List;
import java.util.Map;

public class HibernateDataService implements DataService {
    private SessionFactory sessionFactory;
    private Session session;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T save(T item) {
        return (T) session.save(item);
    }

    @Override
    public <T> T remove(T item) {
        session.delete(item);
        return item;
    }

    @Override
    public <T> T update(T item) {
        session.update(item);
        return item;
    }

    @Override
    public <T> T refresh(T item) {
        session.refresh(item);
        return item;
    }

    @Override
    public <T> T find(Class<T> itemClass, Object id) {
        return session.get(itemClass, (Serializable) id);
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
    }

    @Override
    public void shutdown() {
        sessionFactory.close();
    }

    public static InvocationHandler createTransactionHandler(HibernateDataService hds) {
        return (proxy, method, args) -> {
            if (args == null) args = new Object[0];

            String name = method.getName();
            if (!name.equals("start") && !name.equals("shutdown") && !name.equals("restart")) {
                Transaction tx = null;
                Object result;
                try {
                    hds.session = hds.sessionFactory.openSession();
                    tx = hds.session.beginTransaction();
                    result = method.invoke(hds, args);
                    tx.commit();
                } catch (Exception e) {
                    if (tx != null) {
                        tx.rollback();
                    }
                    throw new RuntimeException(e);
                } finally {
                    hds.session.close();
                }

                return result;
            } else {
                return method.invoke(hds, args);
            }
        };
    }
}
