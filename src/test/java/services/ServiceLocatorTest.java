package services;

import com.tanksoffline.application.configuration.ApplicationServiceLocatorConfiguration;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServiceLocatorTest {
//    private ServiceLocator locator;
//
//    @Before
//    public void setUp() throws Exception {
//        ServiceLocator.bind(new ApplicationServiceLocatorConfiguration());
//        locator = ServiceLocator.getInstance();
//    }
//
//    @Test
//    public void testGetService() throws Exception {
//        assertNotNull(locator.getService(HibernateDataService.class));
//        assertEquals(locator.getService(DataService.class).getClass(), HibernateDataService.class);
//        assertEquals(locator.getService(HibernateDataService.class), locator.getService(DataService.class));
//    }
//
//    @Test
//    public void testLoadServices() throws Exception {
//        locator.loadServices();
//        assertNotNull(locator.getService(HibernateDataService.class));
//        assertNotNull(locator.getService(DataService.class));
//    }
}