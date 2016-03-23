package com.tanksoffline.core.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProxyFactory<T> implements Factory<T> {
    private InvocationHandler handler;
    private Class<?> instanceClass;

    public ProxyFactory(Class<T> instanceClass, InvocationHandler handler) {
        this.instanceClass = instanceClass;
        this.handler = handler;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T create() {
        List<Class<?>> interfaces = getAllInterfaces(instanceClass);
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(),
                interfaces.toArray(new Class[interfaces.size()]), handler);
    }

    private List<Class<?>> getAllInterfaces(Class<?> c) {
        Set<Class<?>> interfaces = new HashSet<>();

        if (c != null) {
            if (c.isInterface()) {
                interfaces.add(c);
            } else {
                interfaces.addAll(getAllInterfaces(c.getSuperclass()));
            }

            for (Class<?> i : c.getInterfaces()) {
                interfaces.add(i);
                interfaces.addAll(getAllInterfaces(i));
            }
        }
        return new ArrayList<>(interfaces);
    }
}
