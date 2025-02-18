package net.ixbob.thepit.util;

import java.util.function.Supplier;

public class SingletonUtil {
    public static <T> Supplier<T> createSingleton(Supplier<T> instanceSupplier) {
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
}
