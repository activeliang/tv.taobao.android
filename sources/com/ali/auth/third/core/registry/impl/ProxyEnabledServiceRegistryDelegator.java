package com.ali.auth.third.core.registry.impl;

import com.ali.auth.third.core.message.Message;
import com.ali.auth.third.core.model.Constants;
import com.ali.auth.third.core.registry.ServiceRegistration;
import com.ali.auth.third.core.registry.ServiceRegistry;
import com.ali.auth.third.core.trace.SDKLogger;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class ProxyEnabledServiceRegistryDelegator implements ServiceRegistry {
    /* access modifiers changed from: private */
    public ServiceRegistry delegator;

    public ProxyEnabledServiceRegistryDelegator(ServiceRegistry delegator2) {
        this.delegator = delegator2;
    }

    public ServiceRegistration registerService(Class<?>[] types, Object instance, Map<String, String> properties) {
        return this.delegator.registerService(types, instance, properties);
    }

    public <T> T getService(final Class<T> type, final Map<String, String> filterProperties) {
        T service = this.delegator.getService(type, filterProperties);
        if (service != null || filterProperties == null) {
            return service;
        }
        String str = filterProperties.get(Constants.ISV_SCOPE_FLAG);
        if (!type.isInterface()) {
            return service;
        }
        return type.cast(Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{type}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object serviceInstance = ProxyEnabledServiceRegistryDelegator.this.delegator.getService(type, filterProperties);
                if (serviceInstance != null) {
                    return method.invoke(serviceInstance, args);
                }
                Object[] objArr = new Object[2];
                objArr[0] = type.getName();
                objArr[1] = filterProperties != null ? filterProperties.toString() : "";
                SDKLogger.e("kernel", Message.create(17, objArr).toString());
                return null;
            }
        }));
    }

    public <T> T[] getServices(Class<T> type, Map<String, String> filterProperties) {
        return this.delegator.getServices(type, filterProperties);
    }

    public Object unregisterService(ServiceRegistration registration) {
        return this.delegator.unregisterService(registration);
    }
}
