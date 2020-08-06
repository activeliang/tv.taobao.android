package android.taobao.atlas.startup;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Process;
import android.taobao.atlas.startup.patch.KernalBundle;
import android.taobao.atlas.startup.patch.KernalConstants;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class AtlasBridgeApplication extends Application {
    public Object mBridgeApplicationDelegate;

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (!isApplicationNormalCreate(base)) {
            Process.killProcess(Process.myPid());
        }
        System.setProperty("BOOT_TIME", System.currentTimeMillis() + "");
        boolean isUpdated = isUpdated(getBaseContext());
        KernalConstants.baseContext = getBaseContext();
        KernalConstants.APK_PATH = getBaseContext().getApplicationInfo().sourceDir;
        KernalConstants.RAW_APPLICATION_NAME = getClass().getName();
        DexLoadBooster dexBooster = new DexLoadBooster();
        dexBooster.init(getBaseContext());
        KernalConstants.dexBooster = dexBooster;
        boolean isMainProcess = getBaseContext().getPackageName().equals(KernalConstants.PROCESS);
        if (isUpdated) {
            if (!isMainProcess) {
                Process.killProcess(Process.myPid());
            }
            File storageDir = new File(getFilesDir(), "storage");
            File bundleBaseline = new File(getFilesDir(), "bundleBaseline");
            deleteDirectory(storageDir);
            KernalVersionManager.instance().removeBaseLineInfo();
            if (storageDir.exists() || bundleBaseline.exists()) {
                Process.killProcess(Process.myPid());
            }
            storePackageVersion(base);
            KernalVersionManager.instance().init();
            System.setProperty("APK_INSTALLED", "true");
        } else if (!KernalConstants.PROCESS.contains(":dex2oat")) {
            KernalVersionManager.instance().init();
            if (!KernalBundle.checkLoadKernalDebugPatch(this)) {
                if (KernalBundle.hasKernalPatch()) {
                    if (!KernalBundle.checkloadKernalBundle(this, KernalConstants.PROCESS)) {
                        if (isMainProcess) {
                            KernalVersionManager.instance().rollbackHardly();
                        }
                        Process.killProcess(Process.myPid());
                    }
                } else if (isMainProcess) {
                    KernalBundle.clear();
                }
            }
        } else {
            return;
        }
        if (KernalBundle.hasNativeLibPatch(this)) {
            KernalBundle.patchNativeLib(this);
        }
        try {
            Class BaselineInfoManagerClazz = getBaseContext().getClassLoader().loadClass("android.taobao.atlas.versionInfo.BaselineInfoManager");
            Object instance = BaselineInfoManagerClazz.getDeclaredMethod("instance", new Class[0]).invoke(BaselineInfoManagerClazz, new Object[0]);
            Field mVersionManager = BaselineInfoManagerClazz.getDeclaredField("mVersionManager");
            mVersionManager.setAccessible(true);
            mVersionManager.set(instance, KernalVersionManager.instance());
            Class BridgeApplicationDelegateClazz = getBaseContext().getClassLoader().loadClass("android.taobao.atlas.bridge.BridgeApplicationDelegate");
            this.mBridgeApplicationDelegate = BridgeApplicationDelegateClazz.getConstructor(new Class[]{Application.class, String.class, String.class, Long.TYPE, Long.TYPE, String.class, Boolean.TYPE, Object.class}).newInstance(new Object[]{this, KernalConstants.PROCESS, KernalConstants.INSTALLED_VERSIONNAME, Long.valueOf(KernalConstants.INSTALLED_VERSIONCODE), Long.valueOf(KernalConstants.LASTUPDATETIME), KernalConstants.APK_PATH, Boolean.valueOf(isUpdated), KernalConstants.dexBooster});
            BridgeApplicationDelegateClazz.getDeclaredMethod("attachBaseContext", new Class[0]).invoke(this.mBridgeApplicationDelegate, new Object[0]);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public Resources getResources() {
        Resources rs = super.getResources();
        if (rs == null) {
            try {
                rs = getPackageManager().getResourcesForApplication(getApplicationInfo());
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        return rs;
    }

    public void onCreate() {
        super.onCreate();
        if (!KernalConstants.PROCESS.contains(":dex2oat")) {
            try {
                this.mBridgeApplicationDelegate.getClass().getDeclaredMethod("onCreate", new Class[0]).invoke(this.mBridgeApplicationDelegate, new Object[0]);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getTargetException());
            } catch (NoSuchMethodException e2) {
                throw new RuntimeException(e2);
            } catch (IllegalAccessException e3) {
                throw new RuntimeException(e3);
            }
        }
    }

    private boolean isApplicationNormalCreate(Context base) {
        StackTraceElement[] stackElements = new Throwable().getStackTrace();
        if (stackElements != null) {
            for (StackTraceElement className : stackElements) {
                if (className.getClassName().equalsIgnoreCase("android.taobao.atlas.runtime.InstrumentationHook")) {
                    return false;
                }
            }
        }
        ApplicationInfo info = base.getApplicationInfo();
        if (info == null) {
            return false;
        }
        String apkPath = info.sourceDir;
        String nativeLibDir = info.nativeLibraryDir;
        if (apkPath == null || !new File(apkPath).exists()) {
            checkShowErrorNotification("InvalidApkPath");
            return false;
        }
        if (nativeLibDir == null || !new File(nativeLibDir).exists()) {
            Log.e("AtlasBridgeApplication", "can not find nativeLibDir : " + nativeLibDir);
        }
        int pid = Process.myPid();
        try {
            for (ActivityManager.RunningAppProcessInfo appProcess : ((ActivityManager) base.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    KernalConstants.PROCESS = appProcess.processName;
                }
            }
        } catch (Exception e) {
        }
        if (!TextUtils.isEmpty(KernalConstants.PROCESS)) {
            return true;
        }
        Log.e("AtlasBridgeApplication", "getProcess failed");
        return false;
    }

    @TargetApi(16)
    private void checkShowErrorNotification(String errorInfo) {
        SharedPreferences preferences = getSharedPreferences("err_log", 0);
        int errorCount = preferences.getInt(errorInfo, 0);
        if (errorCount >= 3) {
            ((NotificationManager) getSystemService("notification")).notify(123, new Notification.Builder(this).setSmallIcon(getResources().getIdentifier("icon", "drawable", getPackageName())).setContentTitle("提示").setAutoCancel(true).setContentText("应用安装不完整,请您卸载重新安装!").build());
            return;
        }
        preferences.edit().putInt(errorInfo, errorCount + 1).commit();
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x00aa A[SYNTHETIC, Splitter:B:34:0x00aa] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00b3 A[SYNTHETIC, Splitter:B:39:0x00b3] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isUpdated(android.content.Context r15) {
        /*
            r14 = this;
            r4 = 0
            android.content.pm.PackageManager r5 = r15.getPackageManager()     // Catch:{ Exception -> 0x0096 }
            java.lang.String r12 = r15.getPackageName()     // Catch:{ Exception -> 0x0096 }
            r13 = 0
            android.content.pm.PackageInfo r4 = r5.getPackageInfo(r12, r13)     // Catch:{ Exception -> 0x0096 }
        L_0x000e:
            java.lang.String r12 = r4.versionName
            android.taobao.atlas.startup.patch.KernalConstants.INSTALLED_VERSIONNAME = r12
            int r12 = r4.versionCode
            long r12 = (long) r12
            android.taobao.atlas.startup.patch.KernalConstants.INSTALLED_VERSIONCODE = r12
            long r12 = r4.lastUpdateTime
            android.taobao.atlas.startup.patch.KernalConstants.LASTUPDATETIME = r12
            java.lang.String r12 = android.taobao.atlas.startup.patch.KernalConstants.INSTALLED_VERSIONNAME
            boolean r12 = android.text.TextUtils.isEmpty(r12)
            if (r12 == 0) goto L_0x0033
            java.lang.String r12 = "AtlasBridgeApplication"
            java.lang.String r13 = "version name is empty "
            android.util.Log.e(r12, r13)
            int r12 = android.os.Process.myPid()
            android.os.Process.killProcess(r12)
        L_0x0033:
            java.io.File r3 = new java.io.File
            java.io.File r12 = r15.getFilesDir()
            java.lang.String r13 = "storage/version_meta"
            r3.<init>(r12, r13)
            boolean r12 = r3.exists()
            if (r12 == 0) goto L_0x00a5
            r1 = 0
            java.io.DataInputStream r2 = new java.io.DataInputStream     // Catch:{ Throwable -> 0x00a7, all -> 0x00b0 }
            java.io.FileInputStream r12 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x00a7, all -> 0x00b0 }
            r12.<init>(r3)     // Catch:{ Throwable -> 0x00a7, all -> 0x00b0 }
            r2.<init>(r12)     // Catch:{ Throwable -> 0x00a7, all -> 0x00b0 }
            java.lang.String r7 = r2.readUTF()     // Catch:{ Throwable -> 0x00c0, all -> 0x00bd }
            long r10 = r2.readLong()     // Catch:{ Throwable -> 0x00c0, all -> 0x00bd }
            long r8 = r2.readLong()     // Catch:{ Throwable -> 0x00c0, all -> 0x00bd }
            java.lang.String r6 = r2.readUTF()     // Catch:{ Throwable -> 0x00c0, all -> 0x00bd }
            java.lang.String r12 = "APP_VERSION_TAG"
            java.lang.String r13 = android.taobao.atlas.startup.patch.KernalConstants.INSTALLED_VERSIONNAME     // Catch:{ Throwable -> 0x00c0, all -> 0x00bd }
            java.lang.System.setProperty(r12, r13)     // Catch:{ Throwable -> 0x00c0, all -> 0x00bd }
            int r12 = r4.versionCode     // Catch:{ Throwable -> 0x00c0, all -> 0x00bd }
            long r12 = (long) r12     // Catch:{ Throwable -> 0x00c0, all -> 0x00bd }
            int r12 = (r12 > r10 ? 1 : (r12 == r10 ? 0 : -1))
            if (r12 != 0) goto L_0x00a0
            java.lang.String r12 = r4.versionName     // Catch:{ Throwable -> 0x00c0, all -> 0x00bd }
            boolean r12 = android.text.TextUtils.equals(r12, r7)     // Catch:{ Throwable -> 0x00c0, all -> 0x00bd }
            if (r12 == 0) goto L_0x00a0
            long r12 = r4.lastUpdateTime     // Catch:{ Throwable -> 0x00c0, all -> 0x00bd }
            int r12 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1))
            if (r12 != 0) goto L_0x00a0
            android.content.pm.ApplicationInfo r12 = r15.getApplicationInfo()     // Catch:{ Throwable -> 0x00c0, all -> 0x00bd }
            java.lang.String r12 = r12.sourceDir     // Catch:{ Throwable -> 0x00c0, all -> 0x00bd }
            boolean r12 = r12.equals(r6)     // Catch:{ Throwable -> 0x00c0, all -> 0x00bd }
            if (r12 == 0) goto L_0x00a0
            boolean r12 = r14.needRollback()     // Catch:{ Throwable -> 0x00c0, all -> 0x00bd }
            if (r12 != 0) goto L_0x00a0
            r12 = 0
            if (r2 == 0) goto L_0x0095
            r2.close()     // Catch:{ Throwable -> 0x00b7 }
        L_0x0095:
            return r12
        L_0x0096:
            r0 = move-exception
            int r12 = android.os.Process.myPid()
            android.os.Process.killProcess(r12)
            goto L_0x000e
        L_0x00a0:
            if (r2 == 0) goto L_0x00a5
            r2.close()     // Catch:{ Throwable -> 0x00b9 }
        L_0x00a5:
            r12 = 1
            goto L_0x0095
        L_0x00a7:
            r12 = move-exception
        L_0x00a8:
            if (r1 == 0) goto L_0x00a5
            r1.close()     // Catch:{ Throwable -> 0x00ae }
            goto L_0x00a5
        L_0x00ae:
            r12 = move-exception
            goto L_0x00a5
        L_0x00b0:
            r12 = move-exception
        L_0x00b1:
            if (r1 == 0) goto L_0x00b6
            r1.close()     // Catch:{ Throwable -> 0x00bb }
        L_0x00b6:
            throw r12
        L_0x00b7:
            r13 = move-exception
            goto L_0x0095
        L_0x00b9:
            r12 = move-exception
            goto L_0x00a5
        L_0x00bb:
            r13 = move-exception
            goto L_0x00b6
        L_0x00bd:
            r12 = move-exception
            r1 = r2
            goto L_0x00b1
        L_0x00c0:
            r12 = move-exception
            r1 = r2
            goto L_0x00a8
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.startup.AtlasBridgeApplication.isUpdated(android.content.Context):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x004f A[SYNTHETIC, Splitter:B:17:0x004f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void storePackageVersion(android.content.Context r9) {
        /*
            r8 = this;
            java.io.File r1 = new java.io.File
            java.io.File r5 = r9.getFilesDir()
            java.lang.String r6 = "storage/version_meta"
            r1.<init>(r5, r6)
            r3 = 0
            java.io.File r5 = r1.getParentFile()     // Catch:{ IOException -> 0x0045 }
            boolean r5 = r5.exists()     // Catch:{ IOException -> 0x0045 }
            if (r5 != 0) goto L_0x001e
            java.io.File r5 = r1.getParentFile()     // Catch:{ IOException -> 0x0045 }
            r5.mkdirs()     // Catch:{ IOException -> 0x0045 }
        L_0x001e:
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x0045 }
            r2.<init>(r1)     // Catch:{ IOException -> 0x0045 }
            java.io.DataOutputStream r4 = new java.io.DataOutputStream     // Catch:{ IOException -> 0x0045 }
            r4.<init>(r2)     // Catch:{ IOException -> 0x0045 }
            java.lang.String r5 = android.taobao.atlas.startup.patch.KernalConstants.INSTALLED_VERSIONNAME     // Catch:{ IOException -> 0x005a, all -> 0x0057 }
            r4.writeUTF(r5)     // Catch:{ IOException -> 0x005a, all -> 0x0057 }
            long r6 = android.taobao.atlas.startup.patch.KernalConstants.INSTALLED_VERSIONCODE     // Catch:{ IOException -> 0x005a, all -> 0x0057 }
            r4.writeLong(r6)     // Catch:{ IOException -> 0x005a, all -> 0x0057 }
            long r6 = android.taobao.atlas.startup.patch.KernalConstants.LASTUPDATETIME     // Catch:{ IOException -> 0x005a, all -> 0x0057 }
            r4.writeLong(r6)     // Catch:{ IOException -> 0x005a, all -> 0x0057 }
            java.lang.String r5 = android.taobao.atlas.startup.patch.KernalConstants.APK_PATH     // Catch:{ IOException -> 0x005a, all -> 0x0057 }
            r4.writeUTF(r5)     // Catch:{ IOException -> 0x005a, all -> 0x0057 }
            r4.flush()     // Catch:{ IOException -> 0x005a, all -> 0x0057 }
            if (r4 == 0) goto L_0x0044
            r4.close()     // Catch:{ Throwable -> 0x0053 }
        L_0x0044:
            return
        L_0x0045:
            r0 = move-exception
        L_0x0046:
            java.lang.RuntimeException r5 = new java.lang.RuntimeException     // Catch:{ all -> 0x004c }
            r5.<init>(r0)     // Catch:{ all -> 0x004c }
            throw r5     // Catch:{ all -> 0x004c }
        L_0x004c:
            r5 = move-exception
        L_0x004d:
            if (r3 == 0) goto L_0x0052
            r3.close()     // Catch:{ Throwable -> 0x0055 }
        L_0x0052:
            throw r5
        L_0x0053:
            r5 = move-exception
            goto L_0x0044
        L_0x0055:
            r6 = move-exception
            goto L_0x0052
        L_0x0057:
            r5 = move-exception
            r3 = r4
            goto L_0x004d
        L_0x005a:
            r0 = move-exception
            r3 = r4
            goto L_0x0046
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.startup.AtlasBridgeApplication.storePackageVersion(android.content.Context):void");
    }

    public boolean needRollback() {
        File baseLineDir = new File(getBaseContext().getFilesDir().getAbsolutePath() + File.separatorChar + "bundleBaseline");
        if (!baseLineDir.exists() || !new File(baseLineDir, "deprecated_mark").exists()) {
            return false;
        }
        return true;
    }

    public void deleteDirectory(File path) {
        File[] files;
        if (path.exists() && (files = path.listFiles()) != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
            path.delete();
        }
    }
}
