package com.tanksoffline.application.utils;

@FunctionalInterface
public interface Factory<T> {
    T createItem();

    class SingletonFactory<T> implements Factory<T> {
        protected T instance;
        protected final Factory<T> localFactory;

        public SingletonFactory(Factory<T> localFactory) {
            this.localFactory = localFactory;
        }

        @Override
        public T createItem() {
            synchronized (this) {
                if (instance == null) {
                    instance = localFactory.createItem();
                }
            }
            return instance;
        }
    }
}
