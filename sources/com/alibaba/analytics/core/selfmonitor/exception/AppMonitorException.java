package com.alibaba.analytics.core.selfmonitor.exception;

public class AppMonitorException extends RuntimeException {
    public AppMonitorException() {
    }

    public AppMonitorException(String message) {
        super(message);
    }

    public AppMonitorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppMonitorException(Throwable cause) {
        super(cause);
    }
}
