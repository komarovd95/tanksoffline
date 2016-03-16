package services;

import com.tanksoffline.application.configuration.ApplicationServiceLocatorConfiguration;
import com.tanksoffline.application.data.users.User;
import com.tanksoffline.application.services.HibernateDataService;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ProxyHDS {

    @Test
    public void testProxy() {
        ServiceLocator.bind(new ApplicationServiceLocatorConfiguration());
        DataService dataService = ServiceLocator.getInstance().getService(HibernateDataService.class);
        dataService.start();
        dataService.save(new User("Dave", "pass123"));
        assertNotNull(dataService);
    }
}
