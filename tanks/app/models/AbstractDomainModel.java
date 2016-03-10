package tanks.app.models;

import tanks.app.services.DatabaseService;
import tanks.app.services.Services;
import tanks.app.views.View;
import tanks.utils.persistence.DomainObject;
import tanks.utils.persistence.ResultList;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class AbstractDomainModel<T extends DomainObject> implements DomainModel<T> {
    protected static final Logger logger = Logger.getLogger("DomainModel");
    protected static final EntityManager em = Services.getService(DatabaseService.class).getEntityManager();
    protected Class<T> entityClass;
    protected List<View> views;

    @SuppressWarnings("unchecked")
    public AbstractDomainModel() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) type.getActualTypeArguments()[0];
        this.views = new ArrayList<>();
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public T find(Object id) {
        return em.find(entityClass, id);
    }

    @Override
    public T save(T t) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        em.persist(t);
        em.getTransaction().commit();
        return t;
    }

    @Override
    public T update(T t) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        T merged = em.merge(t);
        em.getTransaction().commit();
        return merged;
    }

    @Override
    public List<T> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rootEntry = cq.from(entityClass);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }

    @Override
    public ResultList<T> where(Map<String, Object> params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rootEntry = cq.from(entityClass);
        List<javax.persistence.criteria.Predicate> collect = params.entrySet().stream()
                .map(entry -> cb.equal(rootEntry.get(entry.getKey()), entry.getValue())).collect(Collectors.toList());
        javax.persistence.criteria.Predicate[] predicates = collect.toArray(new javax.persistence.criteria.Predicate[collect.size()]);
        CriteriaQuery<T> where = cq.select(rootEntry).where(cb.and(predicates));
        TypedQuery<T> whereQuery = em.createQuery(where);
        return new ResultList<>(whereQuery.getResultList());
    }

    @Override
    public ResultList<T> where(String param, Object value) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rootEntry = cq.from(entityClass);
        CriteriaQuery<T> where = cq.select(rootEntry).where(cb.equal(rootEntry.get(param), value));
        TypedQuery<T> whereQuery = em.createQuery(where);
        return new ResultList<>(whereQuery.getResultList());
    }

    @Override
    public void addObserver(View view) {
        views.add(view);
    }

    @Override
    public void fireUpdate() {
        for (View v : views) {
            v.fireUpdate(this);
        }
    }

    @Override
    public void fireError() {
        for (View v : views) {
            v.fireError(this);
        }
    }

    protected static <E extends DomainObject> E fetch(E t, Class<E> tClass, String... fetchFields) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> cq = cb.createQuery(tClass);
        Root<E> rootEntry = cq.from(tClass);
        for (String willBeFetched : fetchFields) {
            rootEntry.fetch(willBeFetched);
        }
        CriteriaQuery<E> id = cq.select(rootEntry).where(cb.equal(rootEntry.get("id"), t.getId()));
        TypedQuery<E> idQuery = em.createQuery(id);
        return idQuery.getSingleResult();
    }
}
