package net.ixbob.thepit.util;

import java.util.function.Supplier;

public class SingletonUtil {
    public static <T> Supplier<T> createSingletonLazy(Supplier<T> instanceSupplier) {
        return new Supplier<>() {
            private volatile T instance;

            @Override
            public T get() {
                if (instance == null) {
                    synchronized (this) {
                        if (instance == null) {
                            instance = instanceSupplier.get();
                        }
                    }
                }
                return instance;
            }
        };
    }

    public static <T> Supplier<T> createSingletonEager(Supplier<T> instanceSupplier) {
        T instance = instanceSupplier.get();
        return () -> instance;
    }
}
