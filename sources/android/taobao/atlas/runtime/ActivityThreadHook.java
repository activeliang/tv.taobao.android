package android.taobao.atlas.runtime;

import android.app.IActivityManager;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.framework.Atlas;
import android.taobao.atlas.framework.BundleImpl;
import android.taobao.atlas.framework.Framework;
import android.taobao.atlas.hack.AndroidHack;
import android.taobao.atlas.hack.AtlasHacks;
import android.taobao.atlas.runtime.newcomponent.activity.ActivityBridge;
import android.taobao.atlas.util.log.impl.AtlasMonitor;
import android.text.TextUtils;
import android.util.Log;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityThreadHook implements Handler.Callback {
    private final Object mActivityThread;
    private final Handler mActivityThreadHandler;
    private List<Message> sDelayServiceMessageList = null;

    public ActivityThreadHook(Object activityThread, Handler h) {
        this.mActivityThread = activityThread;
        this.mActivityThreadHandler = h;
    }

    public void ensureLoadedApk() throws Exception {
        Object loadedapk = AndroidHack.getLoadedApk(RuntimeVariables.androidApplication, this.mActivityThread, RuntimeVariables.androidApplication.getPackageName());
        if (loadedapk == null) {
            ActivityTaskMgr.getInstance().clearActivityStack();
            Process.killProcess(Process.myPid());
            return;
        }
        ClassLoader classLoader = AtlasHacks.LoadedApk_mClassLoader.get(loadedapk);
        if (!(classLoader instanceof DelegateClassLoader)) {
            AtlasHacks.LoadedApk_mClassLoader.set(loadedapk, RuntimeVariables.delegateClassLoader);
            AtlasHacks.LoadedApk_mResources.set(loadedapk, RuntimeVariables.delegateResources);
            Map<String, Object> detail = new HashMap<>();
            detail.put("classLoader", classLoader.getClass());
            AtlasMonitor.getInstance().report(AtlasMonitor.CONTAINER_LOADEDAPK_CHANGE, detail, new RuntimeException("classloader change"));
        }
    }

    public boolean handleMessage(Message msg) {
        String appVersion;
        try {
            ensureLoadedApk();
            if (msg.what == 100) {
                ActivityBridge.processActivityIntentIfNeed(msg.obj);
            } else if (msg.what == 112) {
                ActivityBridge.handleNewIntent(msg.obj);
            }
            this.mActivityThreadHandler.handleMessage(msg);
            ensureLoadedApk();
            return true;
        } catch (Throwable th) {
        }
        String error = getStackTrace(e);
        if (error != null && error.contains("RemoteServiceException") && !Framework.isDeubgMode()) {
            return true;
        }
        if (appVersion == null) {
            appVersion = "";
        }
        if ((e instanceof ClassNotFoundException) || e.toString().contains("ClassNotFoundException")) {
            if (msg.what == 113 || msg.what == 114) {
                handleCreateServiceException(msg, e);
                return true;
            }
            Object loadedapk = AndroidHack.getLoadedApk(RuntimeVariables.androidApplication, this.mActivityThread, RuntimeVariables.androidApplication.getPackageName());
            if (loadedapk == null) {
                throw new RuntimeException(appVersion + "avalialbeSpace = " + "" + "rootSize = " + 0 + " filesSize = " + 0 + " databasesSize =  " + 0 + " prefSize =" + 0 + "loadedapk is null");
            }
            ClassLoader pathClassLoader = AtlasHacks.LoadedApk_mClassLoader.get(loadedapk);
            if (!(pathClassLoader instanceof DelegateClassLoader)) {
                throw new RuntimeException(appVersion + "avalialbeSpace = " + "" + "rootSize = " + 0 + " filesSize = " + 0 + " databasesSize =  " + 0 + " prefSize =" + 0 + "wrong classloader in loadedapk---" + pathClassLoader.getClass().getName(), e);
            }
            throw new RuntimeException(appVersion + "avalialbeSpace = " + "" + "rootSize = " + 0 + " filesSize = " + 0 + " databasesSize =  " + 0 + " prefSize =" + 0 + "From Atlas:classNotFound ---", e);
        } else if (!e.toString().contains("android.content.res.Resources") || e.toString().contains("OutOfMemoryError")) {
            throw new RuntimeException(appVersion, e);
        } else {
            Object loadedapk2 = AndroidHack.getLoadedApk(RuntimeVariables.androidApplication, this.mActivityThread, RuntimeVariables.androidApplication.getPackageName());
            if (loadedapk2 == null) {
                throw new RuntimeException(appVersion + "loadedapk is null", e);
            } else if (!(AtlasHacks.LoadedApk_mResources.get(loadedapk2) instanceof DelegateResources)) {
                throw new RuntimeException(appVersion + "Not DelegateResources type, " + "DelegateResources paths are: " + DelegateResources.getCurrentAssetpathStr(RuntimeVariables.androidApplication.getAssets()), e);
            } else {
                throw new RuntimeException(appVersion + "DelegateResources paths are: " + DelegateResources.getCurrentAssetpathStr(RuntimeVariables.androidApplication.getAssets()), e);
            }
        }
    }

    private void handleCreateServiceException(Message msg, Throwable e) {
        Object gDefault;
        if (msg.what == 114) {
            try {
                if (Build.VERSION.SDK_INT > 25 || (Build.VERSION.SDK_INT == 25 && Build.VERSION.PREVIEW_SDK_INT > 0)) {
                    gDefault = AtlasHacks.ActivityManager_IActivityManagerSingleton.get(AtlasHacks.ActivityManager.getmClass());
                } else {
                    gDefault = AtlasHacks.ActivityManagerNative_gDefault.get(AtlasHacks.ActivityManagerNative.getmClass());
                }
                Field token_field = Class.forName("android.app.ActivityThread$CreateServiceData").getDeclaredField("token");
                token_field.setAccessible(true);
                ((IActivityManager) AtlasHacks.Singleton_mInstance.get(gDefault)).serviceDoneExecuting((IBinder) token_field.get(msg.obj), 0, 0, 0);
            } catch (Throwable t) {
                Log.w("ActivityThreadHook", "Error serviceDoneExecuting  t: " + t, t);
                throw new RuntimeException(t);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0027 A[SYNTHETIC, Splitter:B:16:0x0027] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x002c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getStackTrace(java.lang.Throwable r6) {
        /*
            r2 = 0
            r0 = 0
            java.io.StringWriter r3 = new java.io.StringWriter     // Catch:{ all -> 0x0024 }
            r3.<init>()     // Catch:{ all -> 0x0024 }
            java.io.PrintWriter r1 = new java.io.PrintWriter     // Catch:{ all -> 0x0034 }
            r1.<init>(r3)     // Catch:{ all -> 0x0034 }
            r6.printStackTrace(r1)     // Catch:{ all -> 0x0037 }
            r1.flush()     // Catch:{ all -> 0x0037 }
            r3.flush()     // Catch:{ all -> 0x0037 }
            if (r3 == 0) goto L_0x001a
            r3.close()     // Catch:{ IOException -> 0x0030 }
        L_0x001a:
            if (r1 == 0) goto L_0x001f
            r1.close()
        L_0x001f:
            java.lang.String r4 = r3.toString()
            return r4
        L_0x0024:
            r4 = move-exception
        L_0x0025:
            if (r2 == 0) goto L_0x002a
            r2.close()     // Catch:{ IOException -> 0x0032 }
        L_0x002a:
            if (r0 == 0) goto L_0x002f
            r0.close()
        L_0x002f:
            throw r4
        L_0x0030:
            r4 = move-exception
            goto L_0x001a
        L_0x0032:
            r5 = move-exception
            goto L_0x002a
        L_0x0034:
            r4 = move-exception
            r2 = r3
            goto L_0x0025
        L_0x0037:
            r4 = move-exception
            r0 = r1
            r2 = r3
            goto L_0x0025
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.runtime.ActivityThreadHook.getStackTrace(java.lang.Throwable):java.lang.String");
    }

    private void handleService(Message message) throws Exception {
        Field info_field = Class.forName("android.app.ActivityThread$CreateServiceData").getDeclaredField("info");
        info_field.setAccessible(true);
        String bundleName = AtlasBundleInfoManager.instance().getBundleForComponet(((ServiceInfo) info_field.get(message.obj)).name);
        if (!TextUtils.isEmpty(bundleName)) {
            BundleImpl impl = (BundleImpl) Atlas.getInstance().getBundle(bundleName);
            if (impl == null || !impl.checkValidate()) {
                if (this.sDelayServiceMessageList == null) {
                    this.sDelayServiceMessageList = new ArrayList();
                    this.sDelayServiceMessageList.add(Message.obtain(message));
                }
                BundleUtil.checkBundleStateAsync(bundleName, new Runnable() {
                    public void run() {
                        try {
                            ActivityThreadHook.this.executeDelayMsg();
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, (Runnable) null);
                return;
            }
            this.mActivityThreadHandler.handleMessage(message);
            if (this.sDelayServiceMessageList != null) {
                this.sDelayServiceMessageList.remove(message);
            }
            executeDelayMsg();
            return;
        }
        this.mActivityThreadHandler.handleMessage(message);
        if (this.sDelayServiceMessageList != null) {
            this.sDelayServiceMessageList.remove(message);
        }
        executeDelayMsg();
    }

    /* access modifiers changed from: private */
    public void executeDelayMsg() throws Exception {
        if (this.sDelayServiceMessageList != null) {
            int size = this.sDelayServiceMessageList.size();
            for (int x = 0; x < size; x++) {
                Message msg = this.sDelayServiceMessageList.get(0);
                if (msg.what == 114) {
                    handleService(msg);
                    return;
                }
                this.sDelayServiceMessageList.remove(msg);
                this.mActivityThreadHandler.handleMessage(msg);
                msg.recycle();
            }
            this.sDelayServiceMessageList = null;
        }
    }
}
