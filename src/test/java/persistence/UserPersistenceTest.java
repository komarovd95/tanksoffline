package persistence;

import com.tanksoffline.application.data.users.User;
import com.tanksoffline.application.configuration.ApplicationServiceLocatorConfiguration;
import com.tanksoffline.application.services.HibernateDataService;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserPersistenceTest {
    private DataService dataService;
    private User user;

    @Before
    public void setUp() throws Exception {
        ServiceLocator.bind(new ApplicationServiceLocatorConfiguration());
        dataService = ServiceLocator.getInstance().getService(HibernateDataService.class);
        dataService.start();

        user = new User("Dave", "pass123");
    }

    @After
    public void tearDown() throws Exception {
        dataService.shutdown();
    }

    @Test(expected = ConstraintViolationException.class)
    public void testLoginUniqueConstraint() throws Throwable {
        try {
            dataService.save(user);
            dataService.save(new User("Dave", "123pass"));
        } catch (Throwable t) {
            throw t.getCause();
        }
    }

    @Test(expected = DataException.class)
    public void testLoginLengthConstraint() throws Throwable {
        try {
            dataService.save(new User("David Mark SpiderManJ", "pass123")); // string length == 21
        } catch (Throwable t) {
            throw t.getCause();
        }
    }
}