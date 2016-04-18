package services;

import com.tanksoffline.core.utils.Factory;
import com.tanksoffline.application.configuration.ApplicationServiceLocatorConfiguration;
import com.tanksoffline.core.services.Service;
import com.tanksoffline.core.services.configuration.ServiceLocatorConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class ServiceLocatorConfigurationTest {
    private ServiceLocatorConfiguration slc;

    @Before
    public void setUp() {
        slc = new ApplicationServiceLocatorConfiguration();
    }

    @Test
    public void testConfigureServices() {
        Map<Class<? extends Service>, Service> serviceMap = slc.configure();
        assertEquals(4, serviceMap.size());
    }

    @Test
    public void testConfigureFactories() {
        Map<Class<? extends Service>, Factory<? extends Service>> factoryMap = slc.configureFactories();
        assertEquals(4, factoryMap.size());
    }

}