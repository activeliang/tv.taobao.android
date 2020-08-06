package org.osgi.framework;

import java.util.EventListener;

public interface FrameworkListener extends EventListener {
    void frameworkEvent(FrameworkEvent frameworkEvent);
}
