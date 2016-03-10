package tanks.services;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Services {
    private static class ServicesHolder {
        private Map<Class<?>, Object> services;

        public ServicesHolder() {
            services = new HashMap<>();
        }

        @SuppressWarnings("unchecked")
        public <T> T getService(Class<T> c) {
            return (T) services.get(c);
        }

        public <T> void registerService(Class<T> c, Object service) {
            services.put(c, service);
        }

        public boolean contains(Class<?> c) {
            return services.containsKey(c);
        }
    }

    private static final Logger logger = Logger.getLogger(Services.class.getName());
    private static ServicesHolder servicesHolder;
    private static Reflections reflections;

    static {
        servicesHolder = new ServicesHolder();

        reflections = new Reflections("tanks");
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);

        for (Class<?> c : services) {
            Service serviceAnnotation = c.getAnnotation(Service.class);
            Object service = (serviceAnnotation.value() == LoadType.INITIAL) ? createService(c) : null;
            servicesHolder.registerService(c, service);
            for (Class<?> i : c.getInterfaces()) {
                if (i.isAnnotationPresent(Provider.class)) {
                    servicesHolder.registerService(i, service);
                }
            }
        }
    }

    private static Object createService(Class<?> serviceClass) {
        try {
            return serviceClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new RuntimeException("Cannot create service " + serviceClass.getSimpleName());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getService(Class<T> c) {
        if (servicesHolder.contains(c)) {
            Object service = servicesHolder.getService(c);
            if (service == null) {
                if (c.isAnnotationPresent(Provider.class)) {
                    Set<Class<? extends T>> services = reflections.getSubTypesOf(c);
                    for (Class<?> s : services) {
                        if (s.isAnnotationPresent(Service.class)) {
                            service = createService(s);
                            break;
                        }
                    }
                } else {
                    service = createService(c);
                }
                servicesHolder.registerService(c, service);
            }
            return (T) service;
        } else {
            throw new RuntimeException("Cannot lookup service " + c.getSimpleName());
        }
    }
}
