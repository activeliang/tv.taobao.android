package android.taobao.atlas.framework;

import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.framework.bundlestorage.BundleArchive;
import android.taobao.atlas.runtime.DelegateResources;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.atlas.util.log.impl.AtlasMonitor;
import android.taobao.atlas.versionInfo.BaselineInfoManager;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;

public class BundleImpl implements Bundle {
    BundleArchive archive;
    File bundleDir;
    BundleClassLoader classloader;
    boolean disabled = false;
    final String location;
    volatile int state;

    BundleImpl(File bundleDir2, String location2, InputStream stream, File file, String unique_tag, boolean installForCurrentVersion, long dexPatchVersion) throws BundleException, IOException {
        this.location = location2;
        this.bundleDir = bundleDir2;
        if (installForCurrentVersion) {
            Framework.notifyBundleListeners(BundleEvent.BEFORE_INSTALL, this);
        }
        if (stream != null) {
            this.archive = new BundleArchive(location2, bundleDir2, stream, unique_tag, dexPatchVersion);
        } else if (file != null) {
            this.archive = new BundleArchive(location2, bundleDir2, file, unique_tag, dexPatchVersion);
        }
        this.state = 2;
        if (installForCurrentVersion) {
            resolveBundle();
            Framework.bundles.put(location2, this);
            Framework.notifyBundleListeners(1, this);
        }
    }

    BundleImpl(BundleContext bcontext) throws Exception {
        long start = System.currentTimeMillis();
        this.location = bcontext.location;
        long dexPatchVersion = BaselineInfoManager.instance().getDexPatchBundleVersion(this.location);
        Framework.notifyBundleListeners(BundleEvent.BEFORE_INSTALL, this);
        this.state = 2;
        if (dexPatchVersion > 0) {
            try {
                this.bundleDir = bcontext.dexPatchDir;
                this.archive = new BundleArchive(this.location, this.bundleDir, bcontext.bundle_tag, dexPatchVersion);
            } catch (Throwable th) {
                try {
                    this.bundleDir = bcontext.bundleDir;
                    this.archive = new BundleArchive(this.location, this.bundleDir, bcontext.bundle_tag, -1);
                } catch (Exception e) {
                    e.printStackTrace();
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("BundleImpl", "BundleImpl create failed!");
                    if (e instanceof BundleArchive.MisMatchException) {
                        this.archive = null;
                        BaselineInfoManager.instance().rollbackHardly();
                        AtlasMonitor.getInstance().report(AtlasMonitor.DD_BUNDLE_MISMATCH, detail, e);
                        throw e;
                    }
                    AtlasMonitor.getInstance().report(AtlasMonitor.DD_BUNDLE_RESOLVEFAIL, detail, e);
                    throw new BundleException("Could not load bundle " + this.location, e.getCause());
                }
            }
        } else {
            this.bundleDir = bcontext.bundleDir;
            this.archive = new BundleArchive(this.location, this.bundleDir, bcontext.bundle_tag, -1);
        }
        resolveBundle();
        Framework.bundles.put(this.location, this);
        Framework.notifyBundleListeners(1, this);
        if (Framework.DEBUG_BUNDLES) {
            Log.i("Framework", " Bundle " + toString() + " loaded. " + (System.currentTimeMillis() - start) + " ms");
        }
    }

    public BundleImpl(String location2) {
        this.location = location2;
        this.state = 4;
    }

    private synchronized void resolveBundle() throws BundleException {
        if (this.archive == null) {
            throw new BundleException("Not a valid bundle: " + this.location);
        } else if (this.state != 4) {
            if (this.classloader == null) {
                List<String> dependencies = AtlasBundleInfoManager.instance().getDependencyForBundle(this.location);
                String nativeLibDir = getArchive().getCurrentRevision().mappingInternalDirectory().getAbsolutePath() + "/lib" + SymbolExpUtil.SYMBOL_COLON + RuntimeVariables.androidApplication.getApplicationInfo().nativeLibraryDir + SymbolExpUtil.SYMBOL_COLON + System.getProperty("java.library.path");
                if (dependencies != null) {
                    for (String str : dependencies) {
                        BundleImpl impl = (BundleImpl) Atlas.getInstance().getBundle(str);
                        if (impl != null && !AtlasBundleInfoManager.instance().isMbundle(str)) {
                            nativeLibDir = (nativeLibDir + SymbolExpUtil.SYMBOL_COLON) + new File(impl.getArchive().getCurrentRevision().mappingInternalDirectory(), "lib");
                        }
                    }
                }
                this.classloader = new BundleClassLoader(this, dependencies, nativeLibDir);
            }
            this.state = 4;
            Framework.notifyBundleListeners(0, this);
        }
    }

    public long getBundleId() {
        return 0;
    }

    public String getLocation() {
        return this.location;
    }

    public BundleArchive getArchive() {
        return this.archive;
    }

    public ClassLoader getClassLoader() {
        return this.classloader;
    }

    public URL getResource(String name) {
        if (this.state != 1) {
            return this.classloader.getResource(name);
        }
        throw new IllegalStateException("Bundle " + toString() + " has been uninstalled");
    }

    public int getState() {
        return this.state;
    }

    public void start() throws BundleException {
        startBundle();
    }

    public void stop() throws BundleException {
        throw new IllegalStateException("Cannot stop bundle now");
    }

    public void startBundle() {
        if (!checkIsActive()) {
            startBundleLocked();
        }
    }

    public synchronized void startBundleLocked() {
        if (!checkIsActive()) {
            if (!checkActing()) {
                this.state = 8;
                Framework.notifyBundleListeners(BundleEvent.BEFORE_STARTED, this);
                Framework.notifyBundleListeners(2, this);
                if (Framework.DEBUG_BUNDLES) {
                    Log.i("Framework", "Bundle " + toString() + " started.");
                }
            }
        }
    }

    private boolean checkActing() {
        if (this.state == 8) {
            return true;
        }
        return false;
    }

    private boolean checkIsActive() {
        if (this.state == 1) {
            throw new IllegalStateException("Cannot start uninstalled bundle " + toString());
        } else if (this.state == 32) {
            return true;
        } else {
            if (this.state != 2) {
                return false;
            }
            throw new RuntimeException("can not start bundle which is not resolved");
        }
    }

    public void setActive() {
        this.state = 32;
    }

    public boolean checkValidate() {
        if (this.classloader != null) {
            long currentTimeMillis = System.currentTimeMillis();
            if (this.classloader.validateClasses() && checkResources()) {
                return true;
            }
            return false;
        }
        return true;
    }

    public boolean checkResources() {
        BundleImpl dependencyBundle;
        if (!DelegateResources.checkAsset(getArchive().getArchiveFile().getAbsolutePath())) {
            return false;
        }
        for (String bundleName : AtlasBundleInfoManager.instance().getBundleInfo(this.location).getTotalDependency()) {
            if (!AtlasBundleInfoManager.instance().isMbundle(bundleName) && ((dependencyBundle = (BundleImpl) Atlas.getInstance().getBundle(bundleName)) == null || dependencyBundle.getArchive() == null || !DelegateResources.checkAsset(dependencyBundle.getArchive().getArchiveFile().getAbsolutePath()))) {
                return false;
            }
        }
        return true;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public synchronized void uninstall() throws BundleException {
        if (this.state == 1) {
            throw new IllegalStateException("Bundle " + toString() + " is already uninstalled.");
        }
        if (this.state == 32) {
            try {
                stop();
            } catch (Throwable t) {
                Framework.notifyFrameworkListeners(2, this, t);
            }
        }
        this.state = 1;
        this.classloader.cleanup(true);
        this.classloader = null;
        Framework.bundles.remove(getLocation());
        Framework.notifyBundleListeners(16, this);
        return;
    }

    public void optDexFile() {
        getArchive().optDexFile();
    }

    public String toString() {
        return this.location;
    }
}
