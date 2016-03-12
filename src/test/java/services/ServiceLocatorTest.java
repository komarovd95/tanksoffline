package services;

import com.tanksoffline.services.ApplicationServiceLocatorConfiguration;
import com.tanksoffline.services.HibernateDataService;
import com.tanksoffline.services.core.DataService;
import com.tanksoffline.services.core.ServiceLocator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServiceLocatorTest {
    private ServiceLocator locator;

    @Before
    public void setUp() throws Exception {
        ServiceLocator.bind(new ApplicationServiceLocatorConfiguration());
        locator = ServiceLocator.getInstance();
    }

    @Test
    public void testGetService() throws Exception {
        assertNotNull(locator.getService(HibernateDataService.class));
        assertEquals(locator.getService(DataService.class).getClass(), HibernateDataService.class);
        assertEquals(locator.getService(HibernateDataService.class), locator.getService(DataService.class));
    }
}