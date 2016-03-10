package tanks.utils.container.di;

import org.reflections.Reflections;
import tanks.utils.container.Container;
import tanks.utils.di.Inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class DIContainer implements Container {
    private Map<String, Object> singletonPool;
    private Map<String, Constructor<?>> prototypePool;
    private Map<String, Method> producersPool;

    public DIContainer() {
        singletonPool = new HashMap<>();
        prototypePool = new HashMap<>();
        producersPool = new HashMap<>();
    }

    protected static void bind(Class<?> rootClass) {

    }

    @Override
    public void configure() {

    }

    @Override
    public void init() {
        Reflections reflections = new Reflections("tanks");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Singleton.class);

        for (Class<?> singleton : classes) {
            Singleton singletonAnnotation = singleton.getAnnotation(Singleton.class);
            loadInjection(singletonPool, singleton, singletonAnnotation.value(), null);
        }

        classes = reflections.getTypesAnnotatedWith(Prototype.class);
        for (Class<?> prototype : classes) {
            Prototype prototypeAnnotation = prototype.getAnnotation(Prototype.class);
            loadInjection(prototypePool, prototype, prototypeAnnotation.value(), null);
        }

        Set<Method> methods = reflections.getMethodsAnnotatedWith(Produces.class);
        for (Method producer : methods) {
            Produces producesAnnotation = producer.getAnnotation(Produces.class);
            loadInjection(producersPool, producer.getReturnType(), producesAnnotation.value(), producer);
        }

        for (Map.Entry<String, Object> singleton : singletonPool.entrySet()) {
            if (singleton.getValue() == null) {
                String className = singleton.getKey().split(" ")[0];
                try {
                    Class<?> clazz = Class.forName(className);
                    Constructor<?> targetConstructor = null;
                    for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                        if (constructor.isAnnotationPresent(Inject.class)) {
                            targetConstructor = constructor;
                            break;
                        }
                    }

                    if (targetConstructor == null) {
                        
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private <T> void loadInjection(Map<String, ? super T> pool, Class<?> clazz, String name, T injection) {
        if ("".equals(name)) {
            pool.put(clazz.getName(), injection);
        } else {
            pool.put(clazz.getName() + " " + name, injection);
        }
    }
}
