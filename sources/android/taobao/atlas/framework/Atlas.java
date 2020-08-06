package android.taobao.atlas.framework;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.framework.BundleInstaller;
import android.taobao.atlas.hack.AndroidHack;
import android.taobao.atlas.hack.AssertionArrayException;
import android.taobao.atlas.hack.AtlasHacks;
import android.taobao.atlas.runtime.ActivityManagerDelegate;
import android.taobao.atlas.runtime.ActivityTaskMgr;
import android.taobao.atlas.runtime.BundleLifecycleHandler;
import android.taobao.atlas.runtime.ClassNotFoundInterceptorCallback;
import android.taobao.atlas.runtime.DelegateClassLoader;
import android.taobao.atlas.runtime.DelegateResources;
import android.taobao.atlas.runtime.FrameworkLifecycleHandler;
import android.taobao.atlas.runtime.InstrumentationHook;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.atlas.runtime.SecurityHandler;
import android.taobao.atlas.util.ApkUtils;
import android.taobao.atlas.util.WrapperUtil;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;

public class Atlas {
    public static boolean Downgrade_H5 = false;
    private static final Atlas INSTANCE = new Atlas();
    public static boolean isDebug;
    public static String sAPKSource;
    public static Set<String> sDisableBundle = null;
    private BundleLifecycleHandler bundleLifecycleHandler;
    private FrameworkLifecycleHandler frameworkLifecycleHandler;

    public interface BundleVerifier {
        boolean verifyBundle(String str);
    }

    public interface ExternalBundleInstallReminder {
        Dialog createReminderDialog(Activity activity, String str);
    }

    private Atlas() {
    }

    public static Atlas getInstance() {
        return INSTANCE;
    }

    public void init(Application application, boolean reset) throws AssertionArrayException, Exception {
        Object gDefault;
        if (application == null) {
            throw new RuntimeException("application is null,atlas init failed!");
        }
        sAPKSource = application.getApplicationInfo().sourceDir;
        RuntimeVariables.androidApplication = application;
        RuntimeVariables.delegateResources = application.getResources();
        DelegateResources.walkroundActionMenuTextColor(RuntimeVariables.delegateResources);
        Framework.containerVersion = RuntimeVariables.sInstalledVersionName;
        ClassLoader cl = Atlas.class.getClassLoader();
        Framework.systemClassLoader = cl;
        String packageName = application.getPackageName();
        DelegateClassLoader newClassLoader = new DelegateClassLoader(cl);
        RuntimeVariables.delegateClassLoader = newClassLoader;
        AndroidHack.injectClassLoader(packageName, newClassLoader);
        AndroidHack.injectInstrumentationHook(new InstrumentationHook(AndroidHack.getInstrumentation(), application.getBaseContext()));
        this.bundleLifecycleHandler = new BundleLifecycleHandler();
        Framework.syncBundleListeners.add(this.bundleLifecycleHandler);
        this.frameworkLifecycleHandler = new FrameworkLifecycleHandler();
        Framework.frameworkListeners.add(this.frameworkLifecycleHandler);
        try {
            ActivityManagerDelegate activityManagerProxy = new ActivityManagerDelegate();
            if (Build.VERSION.SDK_INT > 25 || (Build.VERSION.SDK_INT == 25 && Build.VERSION.PREVIEW_SDK_INT > 0)) {
                gDefault = AtlasHacks.ActivityManager_IActivityManagerSingleton.get(AtlasHacks.ActivityManager.getmClass());
            } else {
                gDefault = AtlasHacks.ActivityManagerNative_gDefault.get(AtlasHacks.ActivityManagerNative.getmClass());
            }
            AtlasHacks.Singleton_mInstance.hijack(gDefault, activityManagerProxy);
        } catch (Throwable th) {
        }
        AndroidHack.hackH();
    }

    public void startup(Application application, boolean isUpdated) {
        if (!RuntimeVariables.safeMode) {
            if (!WrapperUtil.isDebugMode(application) && ApkUtils.isRootSystem()) {
                getInstance().addBundleListener(new SecurityHandler());
            }
            try {
                Framework.startup(isUpdated);
                if (RuntimeVariables.sCurrentProcessName.equals(RuntimeVariables.androidApplication.getPackageName())) {
                    System.setProperty("BUNDLES_INSTALLED", "true");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void checkDownGradeToH5(Intent intent) {
        if (Downgrade_H5) {
            if (!(intent == null || intent.getComponent() == null)) {
                intent.setComponent((ComponentName) null);
            }
            String url = intent.getDataString();
            if (!TextUtils.isEmpty(url)) {
                if (url.contains(WVUtils.URL_DATA_CHAR)) {
                    url = url + "&hybrid=true";
                } else {
                    url = url + "?hybrid=true";
                }
            }
            intent.setData(Uri.parse(url));
            intent.addCategory("com.taobao.intent.category.HYBRID_UI");
        }
    }

    private void checkingThread(boolean strict) {
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId() && Framework.isDeubgMode()) {
            if (strict) {
                throw new RuntimeException("can not install bundle in ui thread");
            }
            Log.w("Atlas", "can not install bundle in ui thread");
        }
    }

    public void setIntentRedirectListener(InstrumentationHook.OnIntentRedirectListener listener) {
        InstrumentationHook.sOnIntentRedirectListener = listener;
    }

    @Deprecated
    public void installBundleWithDependency(String location) {
        if (TextUtils.isEmpty(location)) {
            Log.e("Atlas", "empty location");
        } else if (Framework.getBundle(location) == null) {
            checkingThread(false);
            BundleInstallerFetcher.obtainInstaller().installTransitivelySync(new String[]{location});
        }
    }

    @Deprecated
    public void installBundle(String location, InputStream input) throws BundleException {
        if (TextUtils.isEmpty(location)) {
            Log.e("Atlas", "empty location");
        }
        if (Framework.getBundle(location) == null) {
            checkingThread(false);
            BundleInstallerFetcher.obtainInstaller().installSync(new String[]{location}, new InputStream[]{input});
        }
    }

    @Deprecated
    public void installBundle(String location, File file) throws BundleException {
        if (TextUtils.isEmpty(location)) {
            Log.e("Atlas", "empty location");
        }
        if (Framework.getBundle(location) == null) {
            checkingThread(false);
            BundleInstallerFetcher.obtainInstaller().installSync(new String[]{location}, new File[]{file});
        }
    }

    @Deprecated
    public void uninstallBundle(String location) throws BundleException {
        Bundle bundle = Framework.getBundle(location);
        if (bundle != null) {
            BundleImpl b = (BundleImpl) bundle;
            try {
                File soFile = b.getArchive().getArchiveFile();
                if (soFile.canWrite()) {
                    soFile.delete();
                }
                File delDir = b.getArchive().getCurrentRevision().getRevisionDir();
                bundle.uninstall();
                if (delDir != null) {
                    Framework.deleteDirectory(delDir);
                }
            } catch (Exception e) {
            }
        } else {
            throw new BundleException("Could not uninstall bundle " + location + ", because could not find it");
        }
    }

    @Deprecated
    public Resources getDelegateResources() {
        return RuntimeVariables.delegateResources;
    }

    @Deprecated
    public ClassLoader getDelegateClassLoader() {
        return RuntimeVariables.delegateClassLoader;
    }

    public void installBundleTransitivelyAsync(String[] locations, BundleInstaller.InstallListener listener) {
        BundleInstallerFetcher.obtainInstaller().installTransitivelyAsync(locations, listener);
    }

    public void installIdleBundleTransitively(String location, BundleInstaller.InstallListener listener) {
        BundleInstaller.startIdleInstall(location, listener);
    }

    public void installDelayBundleTransitively(String location, BundleInstaller.InstallListener listener) {
        BundleInstaller.startDelayInstall(location, listener);
    }

    public Bundle getBundle(String location) {
        return Framework.getBundle(location);
    }

    public List<Bundle> getBundles() {
        return Framework.getBundles();
    }

    public ClassLoader getBundleClassLoader(String location) {
        Bundle bundle = Framework.getBundle(location);
        if (bundle != null) {
            return ((BundleImpl) bundle).getClassLoader();
        }
        return null;
    }

    public File getBundleFile(String location) {
        Bundle bundle = Framework.getBundle(location);
        if (bundle != null) {
            return ((BundleImpl) bundle).archive.getArchiveFile();
        }
        return null;
    }

    public void addBundleListener(BundleListener listener) {
        Framework.addBundleListener(listener);
    }

    public void removeBundleListener(BundleListener listener) {
        Framework.removeBundleListener(listener);
    }

    public void setClassNotFoundInterceptorCallback(ClassNotFoundInterceptorCallback callback) {
        Framework.setClassNotFoundCallback(callback);
    }

    @Deprecated
    public void requestRuntimeDependency(ClassLoader source, ClassLoader dependency, boolean resourceDependencyNeed) {
        new HashMap();
        if (source == getClass().getClassLoader() && (dependency instanceof BundleClassLoader)) {
            Log.e("Atlas", "PathClassLoader can not have bundle dependency,this method will be removed in next stage");
        } else if (source == getClass().getClassLoader() && dependency == getClass().getClassLoader()) {
            Log.e("Atlas", "PathClassLoader can not have runtime PathClassLoader dependency, this method will be removed in next stage");
        } else if (!(source instanceof BundleClassLoader) || dependency != getClass().getClassLoader()) {
            String dependencyLocation = ((BundleClassLoader) dependency).location;
            ((BundleClassLoader) source).addRuntimeDependency(dependencyLocation);
            if (resourceDependencyNeed) {
                ActivityTaskMgr.getInstance().updateBundleActivityResource(dependencyLocation);
            }
        }
    }

    @Deprecated
    public void requestRuntimeDependency(ClassLoader source, String dependencyBundle, boolean resourceDependencyNeed) throws BundleException {
        new HashMap();
        if (((BundleImpl) getInstance().getBundle(dependencyBundle)) == null) {
            checkingThread(true);
            BundleInstallerFetcher.obtainInstaller().installTransitivelySync(new String[]{dependencyBundle});
        }
        BundleImpl impl = (BundleImpl) getInstance().getBundle(dependencyBundle);
        if (impl == null) {
            throw new BundleException("failed install deppendencyBundle : " + dependencyBundle);
        } else if ((source instanceof BundleClassLoader) && (impl.getClassLoader() instanceof BundleClassLoader)) {
            requestRuntimeDependency(source, impl.getClassLoader(), resourceDependencyNeed);
        } else if ((source instanceof BundleClassLoader) && impl.getClassLoader() == Framework.getSystemClassLoader()) {
            ((BundleClassLoader) source).addRuntimeDependency(dependencyBundle);
        } else if (source == Framework.getSystemClassLoader() && (impl.getClassLoader() instanceof BundleClassLoader)) {
            Log.e("Atlas", " PathClassLoader can not have bundle dependency " + dependencyBundle);
        }
    }

    public void requestRuntimeDependency(String bundleName, String dependencyBundle, boolean resourceDependencyNeed) throws BundleException {
        new HashMap();
        BundleImpl dependecyBundleImpl = (BundleImpl) getInstance().getBundle(dependencyBundle);
        BundleImpl bundle = (BundleImpl) getInstance().getBundle(bundleName);
        if (dependecyBundleImpl == null || dependecyBundleImpl == null) {
            checkingThread(true);
            BundleInstallerFetcher.obtainInstaller().installTransitivelySync(new String[]{dependencyBundle, bundleName});
        }
        if (dependecyBundleImpl == null || bundle == null) {
            throw new BundleException("failed install deppendencyBundle : " + dependencyBundle);
        } else if ((bundle.getClassLoader() instanceof BundleClassLoader) && (dependecyBundleImpl.getClassLoader() instanceof BundleClassLoader)) {
            requestRuntimeDependency(bundle.getClassLoader(), dependecyBundleImpl.getClassLoader(), resourceDependencyNeed);
        } else if ((bundle.getClassLoader() instanceof BundleClassLoader) && dependecyBundleImpl.getClassLoader() == Framework.getSystemClassLoader()) {
            ((BundleClassLoader) bundle.getClassLoader()).addRuntimeDependency(dependencyBundle);
        } else if (bundle.getClassLoader() != Framework.getSystemClassLoader() || !(dependecyBundleImpl.getClassLoader() instanceof BundleClassLoader)) {
            AtlasBundleInfoManager.instance().getBundleInfo(bundleName).getTotalDependency().add(dependencyBundle);
        } else {
            Log.e("Atlas", "PathClassLoader can not have bundle dependency " + dependencyBundle);
        }
    }

    public void setExternalBundleInstallReminder(ExternalBundleInstallReminder reminder) {
        RuntimeVariables.sReminder = reminder;
    }

    public void setBundleSecurityChecker(BundleVerifier checker) {
        RuntimeVariables.sBundleVerifier = checker;
    }

    public static boolean isDisableBundle(String bundleName) {
        Set<String> disableBundle = sDisableBundle;
        if (disableBundle != null) {
            return disableBundle.contains(bundleName);
        }
        return false;
    }

    public void forceStopSelf() {
    }
}
