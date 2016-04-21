package persistence;

import com.tanksoffline.application.entities.UserEntity;
import com.tanksoffline.application.configuration.ApplicationServiceLocatorConfiguration;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserEntityPersistenceTest {
//    private DataService dataService;
//    private UserEntity userEntity;
//
//    @Before
//    public void setUp() throws Exception {
//        ServiceLocator.bind(new ApplicationServiceLocatorConfiguration());
//        dataService = ServiceLocator.getInstance().getService(HibernateDataService.class);
//        dataService.start();
//
//        userEntity = new UserEntity("Dave", "pass123");
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        dataService.shutdown();
//    }
//
//    @Test(expected = ConstraintViolationException.class)
//    public void testLoginUniqueConstraint() throws Throwable {
//        try {
//            dataService.save(userEntity);
//            dataService.save(new UserEntity("Dave", "123pass"));
//        } catch (Throwable t) {
//            throw t.getCause();
//        }
//    }
//
//    @Test(expected = DataException.class)
//    public void testLoginLengthConstraint() throws Throwable {
//        try {
//            dataService.save(new UserEntity("David Mark SpiderManJ", "pass123")); // string length == 21
//        } catch (Throwable t) {
//            throw t.getCause();
//        }
//    }
}