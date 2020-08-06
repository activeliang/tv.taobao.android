package com.taobao.taobaoavsdk.cache.library;

public class InterruptedProxyCacheException extends ProxyCacheException {
    public InterruptedProxyCacheException(String message) {
        super(message);
    }

    public InterruptedProxyCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public InterruptedProxyCacheException(Throwable cause) {
        super(cause);
    }
}
