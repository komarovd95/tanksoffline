package tanks.services;

import tanks.utils.persistence.DomainObject;
import tanks.utils.persistence.ResultList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Service
public class DatabaseService implements DataService {
    private EntityManagerFactory emf;
    private EntityManager em;

    DatabaseService() {
        startUp();
    }

    @Override
    public void startUp() {
        emf = Persistence.createEntityManagerFactory("TanksTest");
        em = emf.createEntityManager();
    }

    @Override
    public void shutDown() {
        em.close();
        emf.close();
    }

    @Override
    public <T> T save(T item) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        em.persist(item);
        em.getTransaction().commit();
        return item;
    }

    @Override
    public <T> T remove(T item) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        em.remove(item);
        em.getTransaction().commit();
        return item;
    }

    public <T> T update(T item) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        T merged = em.merge(item);
        em.getTransaction().commit();
        return merged;
    }

    @Override
    public <T> T refresh(T item) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        em.refresh(item);
        em.getTransaction().commit();
        return item;
    }

    @Override
    public <T> T find(Class<T> itemClass, Object id) {
        return em.find(itemClass, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T fetch(T item, String... fetchedFields) {
        Class<T> tClass = (Class<T>) item.getClass();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);
        Root<T> rootEntry = cq.from(tClass);
        for (String willBeFetched : fetchedFields) {
            rootEntry.fetch(willBeFetched);
        }
        CriteriaQuery<T> id = cq.select(rootEntry).where(cb.equal(rootEntry.get("id"), DomainObject.getAttribute(item, "id")));
        TypedQuery<T> idQuery = em.createQuery(id);
        return idQuery.getSingleResult();
    }

    @Override
    public <T> ResultList<T> findAll(Class<T> itemClass) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(itemClass);
        Root<T> rootEntry = cq.from(itemClass);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = em.createQuery(all);
        return new ResultList<>(allQuery.getResultList());
    }
}
