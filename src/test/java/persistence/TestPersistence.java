package persistence;

import com.tanksoffline.application.models.UserActiveModel;
import com.tanksoffline.application.services.JpaDataService;
import com.tanksoffline.core.services.DataService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestPersistence {
//    public static void main(String[] args) throws InterruptedException {
////        <properties>
////        <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect" />
////        <property name="hibernate.connection.url" value="jdbc:h2:mem:test" />
////        <property name="hibernate.driver" value="org.h2.Driver" />
////        <property name="hibernate.username" value="sa" />
////        <property name="hibernate.password" value="" />
////        <property name="hibernate.hbm2ddl.auto" value="create-drop" />
////        </properties>
//        Map<String, String> properties = new HashMap<>();
//        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//        properties.put("hibernate.connection.url", "jdbc:h2:mem:test");
//        properties.put("hibernate.driver", "org.h2.Driver");
//        properties.put("hibernate.username", "sa");
//        properties.put("hibernate.password", "");
//        properties.put("hibernate.hbm2ddl.auto", "create-drop");
//        properties.put("hibernate.show_sql", "true");
////        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test", properties);
////        EntityManager em = emf.createEntityManager();
//
//        DataService dataService = new JpaDataService("test", () -> properties);
//        dataService.start();
//
//        TestEntity entity = new TestEntity();
//        entity.name = "Henry";
//        System.out.println(entity.getId());
//        System.out.println(entity.getVersion());
////        em.getTransaction().begin();
////        em.persist(entity);
////        em.getTransaction().commit();
//        dataService.save(entity);
//        System.out.println(entity.getId());
//        System.out.println(entity.getCreatedAt() + " " + entity.getUpdatedAt());
//        System.out.println(entity.getVersion());
//
//        Thread.sleep(2000);
////        em.getTransaction().begin();
//        entity.name = "pi";
//        dataService.update(entity);
////        em.getTransaction().commit();
//        System.out.println(entity.getCreatedAt() + " " + entity.getUpdatedAt());
//        System.out.println(entity.getVersion());
////        InheritedEntity entity1 = new InheritedEntity();
////        em.getTransaction().begin();
////        em.persist(entity1);
////        em.getTransaction().commit();
//
//        TestEntity entity1 = new TestEntity();
//        entity1.name = "John";
//        dataService.save(entity1);
//
//        List<TestEntity> testEntities = dataService.findAll(TestEntity.class);
//
//        UserActiveModel uam = new UserActiveModel();
//
//        dataService.shutdown();
//    }
}
