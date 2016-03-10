package tanks.app.services;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Services {
    private static final Logger logger = Logger.getLogger("Services");
    private static Services service;

    private Map<Class<?>, Service> serviceMap;

    private Services() {
        serviceMap = new HashMap<>();
        initServices();
    }

    public static void load() {
        service = new Services();
    }

    private void initServices() {
        try {
            serviceMap.put(ApplicationService.class, new ApplicationService());
            serviceMap.put(DatabaseService.class, new DatabaseService());
        } catch (Throwable t) {
            logger.log(Level.SEVERE, t.getMessage());
            throw t;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Service> T getService(Class<T> serviceClass) {
        Service s = service.serviceMap.get(serviceClass);
        if (s == null) {
            throw new RuntimeException("Couldn't look up a service " + serviceClass.getName());
        }
        return (T) s;
    }
}
