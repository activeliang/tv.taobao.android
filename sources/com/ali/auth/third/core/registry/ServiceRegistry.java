package com.ali.auth.third.core.registry;

import java.util.Map;

public interface ServiceRegistry {
    <T> T getService(Class<T> cls, Map<String, String> map);

    <T> T[] getServices(Class<T> cls, Map<String, String> map);

    ServiceRegistration registerService(Class<?>[] clsArr, Object obj, Map<String, String> map);

    Object unregisterService(ServiceRegistration serviceRegistration);
}
