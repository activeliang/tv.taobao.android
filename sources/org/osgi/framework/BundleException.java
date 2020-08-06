package org.osgi.framework;

public class BundleException extends Exception {
    private transient Throwable throwable;

    public BundleException(Throwable throwable2) {
        super(throwable2);
        this.throwable = throwable2;
    }

    public BundleException(String msg, Throwable throwable2) {
        super(msg, throwable2);
        this.throwable = throwable2;
    }

    public BundleException(String msg) {
        super(msg);
        this.throwable = null;
    }
}
