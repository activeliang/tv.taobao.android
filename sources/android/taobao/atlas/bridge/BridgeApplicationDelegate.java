package android.taobao.atlas.bridge;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.taobao.atlas.framework.Atlas;
import android.taobao.atlas.framework.Framework;
import android.taobao.atlas.hack.AndroidHack;
import android.taobao.atlas.hack.AtlasHacks;
import android.taobao.atlas.runtime.AtlasPreLauncher;
import android.taobao.atlas.runtime.PackageManagerDelegate;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.atlas.runtime.newcomponent.AdditionalActivityManagerProxy;
import android.taobao.atlas.startup.KernalVersionManager;
import android.taobao.atlas.util.AtlasCrashManager;
import android.taobao.atlas.util.SoLoader;
import android.taobao.atlas.util.log.IAlarmer;
import android.taobao.atlas.util.log.IMonitor;
import android.taobao.atlas.util.log.impl.AtlasAlarmer;
import android.taobao.atlas.util.log.impl.AtlasMonitor;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BridgeApplicationDelegate {
    private String mApkPath;
    private List<ProviderInfo> mBoundApplication_provider;
    private String mCurrentProcessname;
    private long mInstalledVersionCode;
    private String mInstalledVersionName;
    private boolean mIsUpdated;
    private long mLastUpdateTime;
    private Application mRawApplication;
    private Application mRealApplication;
    private String mRealApplicationName;
    private Object mdexLoadBooster;

    public BridgeApplicationDelegate(Application rawApplication, String processname, String installedVersion, long versioncode, long lastupdatetime, String apkPath, boolean isUpdated, Object dexLoadBooster) {
        this.mRawApplication = rawApplication;
        this.mCurrentProcessname = processname;
        this.mInstalledVersionName = installedVersion;
        this.mInstalledVersionCode = versioncode;
        this.mLastUpdateTime = lastupdatetime;
        this.mIsUpdated = isUpdated;
        this.mApkPath = apkPath;
        this.mdexLoadBooster = dexLoadBooster;
        PackageManagerDelegate.delegatepackageManager(rawApplication.getBaseContext());
    }

    public void attachBaseContext() {
        AtlasPreLauncher launcher;
        AtlasHacks.defineAndVerify();
        RuntimeVariables.androidApplication = this.mRawApplication;
        RuntimeVariables.originalResources = this.mRawApplication.getResources();
        RuntimeVariables.sCurrentProcessName = this.mCurrentProcessname;
        RuntimeVariables.sInstalledVersionCode = this.mInstalledVersionCode;
        RuntimeVariables.sAppLastUpdateTime = this.mLastUpdateTime;
        RuntimeVariables.sApkPath = this.mApkPath;
        RuntimeVariables.delegateResources = this.mRawApplication.getResources();
        RuntimeVariables.sDexLoadBooster = this.mdexLoadBooster;
        Log.e("BridgeApplication", "length =" + new File(this.mRawApplication.getApplicationInfo().sourceDir).length());
        if (!Build.MANUFACTURER.equalsIgnoreCase("vivo") || Build.VERSION.SDK_INT != 23) {
            try {
                RuntimeVariables.sDexLoadBooster.getClass().getDeclaredMethod("setVerificationEnabled", new Class[]{Boolean.TYPE}).invoke(RuntimeVariables.sDexLoadBooster, new Object[]{false});
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(this.mInstalledVersionName)) {
            RuntimeVariables.sInstalledVersionName = this.mInstalledVersionName;
        }
        AtlasCrashManager.forceStopAppWhenCrashed();
        System.out.print(SoLoader.class.getName());
        try {
            String preLaunchStr = (String) RuntimeVariables.getFrameworkProperty("preLaunch");
            if (!TextUtils.isEmpty(preLaunchStr) && (launcher = (AtlasPreLauncher) Class.forName(preLaunchStr).newInstance()) != null) {
                launcher.initBeforeAtlas(this.mRawApplication.getBaseContext());
            }
            try {
                ApplicationInfo appInfo = this.mRawApplication.getPackageManager().getApplicationInfo(this.mRawApplication.getPackageName(), 128);
                this.mRealApplicationName = appInfo.metaData.getString("REAL_APPLICATION");
                if (appInfo.metaData.getBoolean("multidex_enable")) {
                    MultiDex.install(this.mRawApplication);
                }
                this.mRealApplicationName = TextUtils.isEmpty(this.mRealApplicationName) ? "android.app.Application" : this.mRealApplicationName;
                if (this.mRealApplicationName.startsWith(".")) {
                    this.mRealApplicationName = this.mRawApplication.getPackageName() + this.mRealApplicationName;
                }
                RuntimeVariables.sRealApplicationName = this.mRealApplicationName;
                try {
                    Atlas.getInstance().init(this.mRawApplication, this.mIsUpdated);
                } catch (Throwable th) {
                    Framework.deleteDirectory(new File(this.mRawApplication.getFilesDir(), "storage"));
                    KernalVersionManager.instance().removeBaseLineInfo();
                    Process.killProcess(Process.myPid());
                }
                try {
                    Class BuildConfig = Class.forName(this.mRawApplication.getPackageName() + ".BuildConfig");
                    Field launchTimeField = BuildConfig.getDeclaredField("launchTime");
                    launchTimeField.setAccessible(true);
                    launchTimeField.set(BuildConfig, Long.valueOf(System.currentTimeMillis()));
                } catch (Throwable th2) {
                }
                try {
                    Object mBoundApplication = AtlasHacks.ActivityThread_mBoundApplication.get(AndroidHack.getActivityThread());
                    this.mBoundApplication_provider = AtlasHacks.ActivityThread$AppBindData_providers.get(mBoundApplication);
                    if (this.mBoundApplication_provider != null && this.mBoundApplication_provider.size() > 0) {
                        AtlasHacks.ActivityThread$AppBindData_providers.set(mBoundApplication, (Object) null);
                    } else if (Build.VERSION.SDK_INT >= 24 && this.mCurrentProcessname != null && this.mCurrentProcessname.equals(this.mRawApplication.getPackageName())) {
                        this.mBoundApplication_provider = new ArrayList();
                        ProviderInfo providerInfo = this.mRawApplication.getPackageManager().resolveContentProvider(this.mRawApplication.getPackageName() + ".update.provider", 0);
                        if (providerInfo != null) {
                            providerInfo.exported = false;
                            providerInfo.grantUriPermissions = true;
                            this.mBoundApplication_provider.add(providerInfo);
                        }
                    }
                } catch (Exception e2) {
                    if (e2 instanceof InvocationTargetException) {
                        throw new RuntimeException(((InvocationTargetException) e2).getTargetException());
                    }
                    throw new RuntimeException(e2);
                }
            } catch (PackageManager.NameNotFoundException e3) {
                throw new RuntimeException(e3);
            }
        } finally {
            RuntimeException runtimeException = new RuntimeException(e3);
        }
    }

    public void onCreate() {
        try {
            AdditionalActivityManagerProxy.get().startRegisterReceivers(RuntimeVariables.androidApplication);
            this.mRealApplication = (Application) this.mRawApplication.getBaseContext().getClassLoader().loadClass(this.mRealApplicationName).newInstance();
            Object activityThread = AndroidHack.getActivityThread();
            AtlasHacks.ContextImpl_setOuterContext.invoke(this.mRawApplication.getBaseContext(), this.mRealApplication);
            AtlasHacks.LoadedApk_mApplication.set(AtlasHacks.ContextImpl_mPackageInfo.get(this.mRawApplication.getBaseContext()), this.mRealApplication);
            AtlasHacks.ActivityThread_mInitialApplication.set(activityThread, this.mRealApplication);
            List<Application> allApplications = AtlasHacks.ActivityThread_mAllApplications.get(activityThread);
            for (int i = 0; i < allApplications.size(); i++) {
                if (allApplications.get(i) == this.mRawApplication) {
                    allApplications.set(i, this.mRealApplication);
                }
            }
            RuntimeVariables.androidApplication = this.mRealApplication;
            this.mRealApplication.registerComponentCallbacks(new ComponentCallbacks() {
                public void onConfigurationChanged(Configuration newConfig) {
                    DisplayMetrics newMetrics = new DisplayMetrics();
                    if (RuntimeVariables.delegateResources != null && RuntimeVariables.androidApplication != null) {
                        WindowManager manager = (WindowManager) RuntimeVariables.androidApplication.getSystemService("window");
                        if (manager == null || manager.getDefaultDisplay() == null) {
                            Log.e("BridgeApplication", "get windowmanager service failed");
                            return;
                        }
                        manager.getDefaultDisplay().getMetrics(newMetrics);
                        RuntimeVariables.delegateResources.updateConfiguration(newConfig, newMetrics);
                        Class<Resources> cls = Resources.class;
                        try {
                            Method method = cls.getDeclaredMethod("updateSystemConfiguration", new Class[]{Configuration.class, DisplayMetrics.class, Class.forName("android.content.res.CompatibilityInfo")});
                            method.setAccessible(true);
                            method.invoke(RuntimeVariables.delegateResources, new Object[]{newConfig, newMetrics, null});
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }

                public void onLowMemory() {
                }
            });
            AtlasHacks.Application_attach.invoke(this.mRealApplication, this.mRawApplication.getBaseContext());
            if (this.mBoundApplication_provider != null && this.mBoundApplication_provider.size() > 0) {
                AtlasHacks.ActivityThread$AppBindData_providers.set(AtlasHacks.ActivityThread_mBoundApplication.get(activityThread), this.mBoundApplication_provider);
                AtlasHacks.ActivityThread_installContentProviders.invoke(activityThread, this.mRealApplication, this.mBoundApplication_provider);
            }
            if (this.mRealApplication instanceof IMonitor) {
                AtlasMonitor.getInstance();
                AtlasMonitor.setExternalMonitor((IMonitor) this.mRealApplication);
            }
            if (this.mRealApplication instanceof IAlarmer) {
                AtlasAlarmer.getInstance();
                AtlasAlarmer.setExternalAlarmer((IAlarmer) this.mRealApplication);
            }
            Atlas.getInstance().startup(this.mRealApplication, this.mIsUpdated);
            this.mRealApplication.onCreate();
        } catch (Throwable e) {
            if (e instanceof InvocationTargetException) {
                throw new RuntimeException(((InvocationTargetException) e).getTargetException());
            }
            throw new RuntimeException(e);
        }
    }
}
