package org.osgi.framework;

import java.util.EventListener;

public interface BundleListener extends EventListener {
    void bundleChanged(BundleEvent bundleEvent);
}
