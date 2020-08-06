package android.taobao.atlas.runtime;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Looper;
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.bundleInfo.BundleListing;
import android.taobao.atlas.framework.Atlas;
import android.taobao.atlas.framework.BundleClassLoader;
import android.taobao.atlas.framework.BundleImpl;
import android.taobao.atlas.hack.AtlasHacks;
import android.taobao.atlas.profile.AtlasProfile;
import android.taobao.atlas.util.StringUtils;
import android.taobao.atlas.util.log.impl.AtlasMonitor;
import android.util.Log;
import com.bftv.fui.constantplugin.Constant;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.SynchronousBundleListener;

public class BundleLifecycleHandler implements SynchronousBundleListener {
    @SuppressLint({"NewApi"})
    public void bundleChanged(BundleEvent event) {
        switch (event.getType()) {
            case 0:
                loaded(event.getBundle());
                return;
            case 1:
                installed(event.getBundle());
                return;
            case 2:
                if (isLewaOS()) {
                    if (Looper.myLooper() == null) {
                        Looper.prepare();
                    }
                    started(event.getBundle());
                    return;
                }
                started(event.getBundle());
                return;
            case 4:
                stopped(event.getBundle());
                return;
            case 8:
                updated(event.getBundle());
                return;
            case 16:
                uninstalled(event.getBundle());
                return;
            default:
                return;
        }
    }

    private void loaded(Bundle bundle) {
        BundleImpl b = (BundleImpl) bundle;
        try {
            DelegateResources.addBundleResources(b.getArchive().getArchiveFile().getAbsolutePath(), b.getArchive().getCurrentRevision().getDebugPatchFilePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void installed(Bundle bundle) {
    }

    private void updated(Bundle bundle) {
    }

    private void uninstalled(Bundle bundle) {
    }

    private void started(Bundle bundle) {
        BundleImpl b = (BundleImpl) bundle;
        if (b.getClassLoader() == null || ((b.getClassLoader() instanceof BundleClassLoader) && !((BundleClassLoader) b.getClassLoader()).validateClasses())) {
            Log.e("BundleLifeCycle", "validateClass fail,bundle can't be started :" + b);
            List<Bundle> bundles = Atlas.getInstance().getBundles();
            Map<String, Object> detail = new HashMap<>();
            detail.put("bundle", bundle);
            detail.put("bundles", bundles);
            AtlasMonitor.getInstance().report(AtlasMonitor.VALIDATE_CLASSES, detail, new Exception());
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        BundleListing.BundleInfo info = AtlasBundleInfoManager.instance().getBundleInfo(b.getLocation());
        if (info != null) {
            String appClassName = info.getApplicationName();
            if (StringUtils.isNotEmpty(appClassName)) {
                try {
                    AtlasProfile ap = AtlasProfile.profile(appClassName);
                    ap.start();
                    newApplication(appClassName, b.getClassLoader()).onCreate();
                    Log.e("BundleLifeCycle", "start finish" + appClassName + Constant.NLP_CACHE_TYPE + ap.stop() + Constant.NLP_CACHE_TYPE + Thread.currentThread().toString());
                    ((BundleImpl) bundle).setActive();
                } catch (ApplicationInitException e) {
                    if (b.getArchive() == null || !b.getArchive().isDexOpted()) {
                        Log.e("BundleLifeCycle", "started application crash | " + (Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId()));
                        return;
                    }
                    throw new RuntimeException(e);
                }
            } else {
                ((BundleImpl) bundle).setActive();
                Log.e("BundleLifeCycle", "started with no application");
            }
        }
    }

    protected static Application newApplication(String applicationClassName, ClassLoader cl) throws ApplicationInitException {
        try {
            Class<?> applicationClass = cl.loadClass(applicationClassName);
            if (applicationClass == null) {
                throw new ApplicationInitException(String.format("can not find class: %s", new Object[]{applicationClassName}));
            }
            Application app = (Application) applicationClass.newInstance();
            AtlasHacks.Application_attach.invoke(app, RuntimeVariables.androidApplication);
            return app;
        } catch (ClassNotFoundException e) {
            throw new ApplicationInitException((Throwable) e);
        } catch (IllegalAccessException e2) {
            throw new ApplicationInitException((Throwable) e2);
        } catch (InvocationTargetException e3) {
            throw new ApplicationInitException((Throwable) e3);
        } catch (InstantiationException e4) {
            throw new ApplicationInitException((Throwable) e4);
        }
    }

    private void stopped(Bundle bundle) {
    }

    public void handleLowMemory() {
    }

    private boolean isLewaOS() {
        try {
            return StringUtils.isNotEmpty((String) Class.forName("android.os.SystemProperties").getDeclaredMethod("get", new Class[]{String.class}).invoke((Object) null, new Object[]{"ro.lewa.version"}));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
