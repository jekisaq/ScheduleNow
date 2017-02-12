package ru.jeki.schedulenow.services;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.NoSuchElementException;

public class Services {
    private static Map<Class, Object> classToServices = Maps.newHashMap();

    public static <T> void addService(T serviceInstance) {
        classToServices.put(serviceInstance.getClass(), serviceInstance);
    }

    public static <T> T getService(Class<T> serviceClass) {
        if (!classToServices.containsKey(serviceClass)) {
            throw new NoSuchElementException("There's no such service that was required.");
        }

        return serviceClass.cast(classToServices.get(serviceClass));
    }
}
