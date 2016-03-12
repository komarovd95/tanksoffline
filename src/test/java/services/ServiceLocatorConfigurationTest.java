package services;

import com.tanksoffline.application.utils.Factory;
import com.tanksoffline.services.ApplicationServiceLocatorConfiguration;
import com.tanksoffline.services.HibernateDataService;
import com.tanksoffline.services.core.DataService;
import com.tanksoffline.services.core.Service;
import com.tanksoffline.services.core.ServiceLocatorConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ServiceLocatorConfigurationTest {
    private ServiceLocatorConfiguration slc;
    private Map<Class<?>, Service> expectedServiceMap;
    private Map<Class<?>, Factory<? extends Service>> expectedFactoryMap;

    @Before
    public void setUp() {
        slc = new ApplicationServiceLocatorConfiguration();

        expectedServiceMap = new HashMap<>();
        expectedServiceMap.put(DataService.class, null);
        expectedServiceMap.put(HibernateDataService.class, null);

        expectedFactoryMap = new HashMap<>();

        Factory<DataService> mockServiceFactory = () -> null;
        expectedFactoryMap.put(DataService.class, mockServiceFactory);
        expectedFactoryMap.put(HibernateDataService.class, mockServiceFactory);
    }

    @Test
    public void testConfigureServices() {
        Map<Class<?>, Service> serviceMap = slc.configureServices();
        assertEquals(expectedServiceMap, serviceMap);
    }

    @Test
    public void testConfigureFactories() {
        Map<Class<?>, Factory<? extends Service>> factoryMap = slc.configureFactories();
        assertEquals(expectedFactoryMap.keySet(), factoryMap.keySet());
    }

}