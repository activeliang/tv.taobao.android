package org.osgi.framework;

public class FrameworkEvent {
    public static final int ERROR = 2;
    public static final int PACKAGES_REFRESHED = 4;
    public static final int STARTED = 1;
    public static final int STARTLEVEL_CHANGED = 8;
    public int state;

    public FrameworkEvent(int state2) {
        this.state = state2;
    }

    public int getType() {
        return this.state;
    }
}
