package tanks.utils.di;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

public class DIContainer {
    private Map<String, Object> singletonsPool;
    private Map<String, List<Object>> prototypesPool;
    private Set<Constructor> injectorConstructors;
    private Set<Method> injectorMethods;

    public DIContainer() {
        singletonsPool = new HashMap<>();
        prototypesPool = new HashMap<>();
        init();
    }

    private void init() {
        Reflections reflections = new Reflections("tanks");

        injectorConstructors = reflections.getConstructorsAnnotatedWith(Injector.class);
        injectorMethods = reflections.getMethodsAnnotatedWith(Injector.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T inject(String qualifier) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!singletonsPool.containsKey(qualifier)) {
            Object[] args;
            Constructor constructorInjector = getInjectorConstructor(qualifier);
            if (constructorInjector != null) {
                args = getArgs(constructorInjector);
                Object newInstance = constructorInjector.newInstance(args);
                singletonsPool.put(qualifier, newInstance);
                return (T) newInstance;
            } else {
                Method methodInjector = getInjectorMethod(qualifier);
                if (methodInjector != null) {
                    int modifiers = methodInjector.getModifiers();
                    if (Modifier.isStatic(modifiers)) {
                        args = getArgs(methodInjector);
                        Object newInstance = methodInjector.invoke(null, args);
                        singletonsPool.put(qualifier, newInstance);
                        return (T) newInstance;
                    } else {
                        throw new Error("@Injector method " + methodInjector.getName() + " must be a static modified");
                    }
                } else {
                    throw new Error("No @Injector's found. Can't inject an injection (" + qualifier + ")");
                }
            }
        } else {
            return (T) singletonsPool.get(qualifier);
        }
    }

    private Method getInjectorMethod(String qualifier) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        String s = inject("Hello");
        return null;
    }

    private Constructor getInjectorConstructor(String qualifier) {
        List<Constructor> cs = injectorConstructors.stream().filter(c -> {
            Injector injectorAnnotation = c.getDeclaredAnnotation(Injector.class);
            return injectorAnnotation != null && qualifier.equals(injectorAnnotation.name());
        }).collect(Collectors.toList());
        if (cs.size() == 1) {
            return cs.get(0);
        } else {
            Constructor injector = null;
            for (Constructor c : cs) {
                if (c.getParameters().length == 0) {
                    injector = c;
                    break;
                }
            }
            return injector;
        }
    }

    private Object[] getArgs(Executable exec) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<Object> args = new ArrayList<>();
        for (Parameter p : exec.getParameters()) {
            Inject injectAnnotation = p.getAnnotation(Inject.class);
            if (injectAnnotation != null) {
                if ("".equals(injectAnnotation.name())) {
                    args.add(inject(p.getType().getName()));
                } else {
                    args.add(inject(injectAnnotation.name()));
                }
            }
        }
        return args.toArray(new Object[args.size()]);
    }
}
