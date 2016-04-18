package com.tanksoffline.application.services;

import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.configuration.ServiceConfiguration;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class JpaDataService implements DataService {
    private ServiceConfiguration<String, String> configuration;
    private String unitName;
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    public JpaDataService(String unitName, ServiceConfiguration<String, String> configuration) {
        this.configuration = configuration;
        this.unitName = unitName;
    }

    @Override
    public <T> T save(T item) {
        return doWithTransaction(em -> {
            em.persist(item);
            return item;
        });
    }

    @Override
    public <T> T remove(T item) {
        return doWithTransaction(em -> {
            em.remove(item);
            return item;
        });
    }

    @Override
    public <T> T update(T item) {
        return doWithTransaction(em -> em.merge(item));
    }

    @Override
    public <T> T refresh(T item) {
        return doWithTransaction(em -> {
            em.refresh(item);
            return item;
        });
    }

    @Override
    public <T> T findById(Class<T> itemClass, Object id) {
        return doWithTransaction(em -> em.find(itemClass, id));
    }

    @Override
    public <T> List<T> findAll(Class<T> itemClass) {
        return doWithTransaction(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> findAllQuery = cb.createQuery(itemClass);
            Root<T> root = findAllQuery.from(itemClass);
            findAllQuery.select(root);
            TypedQuery<T> query = em.createQuery(findAllQuery);
            return query.getResultList();
        });
    }

    @Override
    public <T> List<T> findBy(Class<T> itemClass, Map<String, Object> args) {
        return doWithTransaction(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> findByQuery = cb.createQuery(itemClass);
            Root<T> root = findByQuery.from(itemClass);
            List<Predicate> restrictions = new ArrayList<>();
            args.forEach((param, value) -> restrictions.add(cb.equal(root.get(param), value)));
            findByQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
            TypedQuery<T> query = em.createQuery(findByQuery);
            return query.getResultList();
        });
    }

    @Override
    public <T> List<T> findBy(Class<T> itemClass, String paramName, Object paramValue) {
        return findBy(itemClass, Collections.singletonMap(paramName, paramValue));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findBy(String query, Object... args) {
        return doWithTransaction(em -> {
            Query jpaQuery = em.createQuery(query);
            for (int i = 0; i < args.length; i++) {
                jpaQuery.setParameter(i, args[i]);
            }
            return (List<T>) jpaQuery.getResultList();
        });
    }

    @Override
    public <T> T fetch(T item, String... fetchedFields) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void start() {
        entityManagerFactory = Persistence.createEntityManagerFactory(unitName, configuration.configure());
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public void shutdown() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
        if (entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    private <T> T doWithTransaction(Function<EntityManager, T> work) {
        try {
            entityManager.getTransaction().begin();
            T result = work.apply(entityManager);
            entityManager.getTransaction().commit();
            return result;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException(e);
        }
    }
}
