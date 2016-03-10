package tanks.app.services;

import javax.persistence.*;

public class DatabaseService implements Service {
    private EntityManagerFactory emf;
    private EntityManager em;

    DatabaseService() {
        emf = Persistence.createEntityManagerFactory("TanksTest");
        em = emf.createEntityManager();
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public void shutdown() {
        em.close();
        emf.close();
    }
}
