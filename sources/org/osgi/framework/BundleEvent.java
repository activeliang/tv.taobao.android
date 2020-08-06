package org.osgi.framework;

import java.util.EventObject;

public class BundleEvent extends EventObject {
    public static final int BEFORE_INSTALL = 10086;
    public static final int BEFORE_STARTED = 10087;
    public static final int INSTALLED = 1;
    public static final int STARTED = 2;
    public static final int STOPPED = 4;
    public static final int UNINSTALLED = 16;
    public static final int UPDATED = 8;
    private transient Bundle bundle;
    private transient int type;

    public BundleEvent(int type2, Bundle bundle2) {
        super(bundle2);
        this.bundle = bundle2;
        this.type = type2;
    }

    public Bundle getBundle() {
        return this.bundle;
    }

    public int getType() {
        return this.type;
    }
}
