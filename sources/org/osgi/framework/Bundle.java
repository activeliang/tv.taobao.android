package org.osgi.framework;

import java.net.URL;

public interface Bundle {
    public static final int ACTIVE = 32;
    public static final int INSTALLED = 2;
    public static final int RESOLVED = 4;
    public static final int STARTING = 8;
    public static final int STOPPING = 16;
    public static final int UNINSTALLED = 1;

    long getBundleId();

    String getLocation();

    URL getResource(String str);

    int getState();

    void start() throws BundleException;

    void stop() throws BundleException;

    void uninstall() throws BundleException;
}
