package services;

import com.tanksoffline.data.users.User;
import com.tanksoffline.services.ApplicationServiceLocatorConfiguration;
import com.tanksoffline.services.HibernateDataService;
import com.tanksoffline.services.core.DataService;
import com.tanksoffline.services.core.ServiceLocator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class DataServiceTest {
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
    public void tearDown() throws Exception{
        dataService.shutdown();
    }

    @Test
    public void testSave() throws Exception {
        dataService.save(user);
        assertEquals(1L, user.getId().longValue());
        assertEquals(user.getLogin(), dataService.find(User.class, 1L).getLogin());
    }

    @Test
    public void testRemove() throws Exception {
        dataService.save(user);
        dataService.remove(user);
        assertNull(dataService.find(User.class, 1L));
    }

    @Test
    public void testUpdate() throws Exception {
        dataService.save(user);
        user.setLogin("Mark");
        dataService.update(user);
        assertEquals(user.getLogin(), dataService.find(User.class, 1L).getLogin());
    }

    @Test
    public void testRefresh() throws Exception {
        dataService.save(user);
        user.setLogin("Mark");
        dataService.refresh(user);
        assertEquals(user.getLogin(), "Dave");
    }

    @Test
    public void testFind() throws Exception {
        dataService.save(user);
        assertNotNull(dataService.find(User.class, 1L));
        assertNull(dataService.find(User.class, 2L));
    }

    @Test
    public void testFetch() throws Exception {

    }
}