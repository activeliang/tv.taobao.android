package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.os.Environment;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.webkit.ValueCallback;
import com.uc.webview.export.annotations.Reflection;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.cyclone.UCLoader;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.setup.UCSetupTask;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.ReflectionUtil;
import com.uc.webview.export.internal.utility.b;
import com.uc.webview.export.internal.utility.c;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/* compiled from: ProGuard */
public abstract class UCSetupTask<RETURN_TYPE extends UCSetupTask<RETURN_TYPE, CALLBACK_TYPE>, CALLBACK_TYPE extends UCSetupTask<RETURN_TYPE, CALLBACK_TYPE>> extends UCSubSetupTask<RETURN_TYPE, CALLBACK_TYPE> {
    protected static final String LEGACY_EVENT_INIT = "init";
    protected static final String LEGACY_EVENT_LOAD = "load";
    protected static final String LEGACY_EVENT_SETUP = "setup";
    protected static final String LEGACY_EVENT_SWITCH = "switch";
    private static UCMRunningInfo b;
    private static UCSetupTask c;
    private static UCAsyncTask d;
    private static int e;
    private static boolean f = false;
    protected static final List<UCSetupTask> sTotalSetupTasks = new ArrayList(2);
    private UCMRunningInfo a;
    /* access modifiers changed from: private */
    public String g = "";
    private a h;

    /* access modifiers changed from: protected */
    public a getSetupCrashImproverInst(String str) {
        if (this.h == null) {
            this.h = new a(str);
        }
        return this.h;
    }

    /* access modifiers changed from: protected */
    public final String getCrashCode() {
        return this.g;
    }

    protected static synchronized UCAsyncTask getRoot() {
        UCAsyncTask uCAsyncTask;
        synchronized (UCSetupTask.class) {
            if (d == null) {
                d = new ba(Integer.valueOf(e)).onEvent("start", new az()).onEvent("die", new ay());
            }
            uCAsyncTask = d;
        }
        return uCAsyncTask;
    }

    /* access modifiers changed from: protected */
    public final void setTotalLoadedUCM(UCMRunningInfo uCMRunningInfo) {
        b = uCMRunningInfo;
    }

    public static UCMRunningInfo getTotalLoadedUCM() {
        return b;
    }

    @Reflection
    public static Class<?> classForName(String str) {
        UCMRunningInfo totalLoadedUCM = getTotalLoadedUCM();
        ClassLoader classLoader = totalLoadedUCM == null ? null : totalLoadedUCM.classLoader;
        if (classLoader == null) {
            return Class.forName(str);
        }
        return Class.forName(str, true, classLoader);
    }

    public static boolean isSetupThread() {
        return ((Boolean) getRoot().invokeO(UCAsyncTask.inThread, new Object[0])).booleanValue();
    }

    public static void resumeAll() {
        synchronized (sTotalSetupTasks) {
            for (int i = 0; i < sTotalSetupTasks.size(); i++) {
                sTotalSetupTasks.get(i).resume();
            }
        }
    }

    public UCSetupTask() {
        synchronized (sTotalSetupTasks) {
            sTotalSetupTasks.add(this);
        }
    }

    public synchronized RETURN_TYPE start() {
        RETURN_TYPE return_type;
        if (invokeO(10005, new Object[0]) == null) {
            Integer num = (Integer) this.mOptions.get(UCCore.OPTION_SETUP_THREAD_PRIORITY);
            if (num != null) {
                setRootTaskPriority(num.intValue());
            }
            UCAsyncTask root = getRoot();
            invoke(10001, root);
            return_type = (UCSetupTask) super.start();
            root.start();
        } else {
            return_type = (UCSetupTask) super.start();
        }
        return return_type;
    }

    /* access modifiers changed from: protected */
    public void setupGlobalOnce(Context context) {
        int i;
        boolean z = false;
        if (!f) {
            f = true;
            if (!Log.sPrintLog) {
                try {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "uclogconfig.apk");
                    if (file.exists() && file.isFile() && file.length() <= 3000 && b.a(file.getAbsolutePath(), context, context, "com.UCMobile", (ValueCallback<Object[]>) null)) {
                        String str = (String) ReflectionUtil.invoke(new UCLoader(file.getAbsolutePath(), context.getCacheDir().getAbsolutePath(), (String) null, UCSetupTask.class.getClassLoader()).loadClass("com.uc.webview.uclogconfig.UCSDKLogConfig"), "getEnabledDate", (Class[]) null, (Object[]) null);
                        String format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
                        if (str != null && str.length() >= 8 && format != null && format.length() >= 8 && str.startsWith(format)) {
                            Log.sPrintLog = true;
                        }
                    }
                } catch (Throwable th) {
                }
            }
            UCCyclone.enableDebugLog = Log.sPrintLog;
            d.a((int) UCMPackageInfo.getKernelFileIfMultiCoreFromDir, context);
            Integer num = (Integer) this.mOptions.get(UCCore.OPTION_WEBVIEW_POLICY);
            if (num != null) {
                d.i = num.intValue();
            }
            Boolean bool = (Boolean) this.mOptions.get(UCCore.OPTION_USE_SDK_SETUP);
            if (bool != null) {
                d.k = bool.booleanValue();
            }
            Boolean a2 = c.a((HashMap<String, Object>) this.mOptions, UCCore.OPTION_MULTI_CORE_TYPE);
            if (a2 != null) {
                d.j = a2.booleanValue();
            }
            Boolean a3 = c.a((HashMap<String, Object>) this.mOptions, UCCore.OPTION_HARDWARE_ACCELERATED);
            if (a3 != null) {
                d.h = a3.booleanValue() ? 1 : 0;
            }
            Boolean a4 = c.a((HashMap<String, Object>) this.mOptions, UCCore.OPTION_VIDEO_HARDWARE_ACCELERATED);
            if (a4 != null) {
                Object[] objArr = new Object[1];
                if (a4.booleanValue()) {
                    i = 1;
                } else {
                    i = 0;
                }
                objArr[0] = Integer.valueOf(i);
                d.a(10043, objArr);
            }
            if (context.getPackageName().equals("com.ucsdk.cts") || !c.a((Boolean) this.mOptions.get(UCCore.OPTION_GRANT_ALL_BUILDS))) {
                z = true;
            }
            if (z) {
                d.p = true;
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void setLoadedUCM(UCMRunningInfo uCMRunningInfo) {
        this.a = uCMRunningInfo;
    }

    @Reflection
    public final UCMRunningInfo getLoadedUCM() {
        return this.a;
    }

    protected static void setRootTaskPriority(int i) {
        e = i;
    }

    public static UCSetupTask getDefault() {
        return c;
    }

    /* access modifiers changed from: protected */
    public void setDefault(UCSetupTask uCSetupTask) {
        c = uCSetupTask;
    }

    protected static void deleteSetupFiles(Context context, boolean z, boolean z2) {
        File file;
        File file2 = null;
        try {
            UCMRunningInfo totalLoadedUCM = getTotalLoadedUCM();
            try {
                UCMPackageInfo.invoke(UCMPackageInfo.deleteTempDecFiles, context);
            } catch (Throwable th) {
            }
            try {
                if (totalLoadedUCM.ucmPackageInfo == null || totalLoadedUCM.ucmPackageInfo.coreImplModule == null || totalLoadedUCM.ucmPackageInfo.coreImplModule.second == null) {
                    file = null;
                } else {
                    file = new File((String) totalLoadedUCM.ucmPackageInfo.coreImplModule.second);
                }
                UCCyclone.recursiveDelete((File) UCMPackageInfo.invoke(10004, context), true, file);
            } catch (Throwable th2) {
            }
            try {
                File file3 = (totalLoadedUCM.ucmPackageInfo == null || totalLoadedUCM.ucmPackageInfo.dataDir == null) ? null : new File(totalLoadedUCM.ucmPackageInfo.dataDir);
                try {
                    File file4 = (File) UCMPackageInfo.invoke(10003, context);
                    File[] listFiles = file4.listFiles();
                    if (listFiles != null && listFiles.length > 0 && (z || listFiles.length >= 2 || (file3 != null && file3.getAbsolutePath().startsWith(file4.getAbsolutePath())))) {
                        UCCyclone.recursiveDelete(file4, true, file3);
                    }
                } catch (Throwable th3) {
                }
                try {
                    File file5 = (File) UCMPackageInfo.invoke(10006, context);
                    if (file3 != null) {
                        file2 = file3.getParentFile().getParentFile();
                    }
                    UCCyclone.recursiveDelete(file5, true, file2);
                } catch (Throwable th4) {
                }
                try {
                    File file6 = (File) UCMPackageInfo.invoke(10002, context);
                    File[] listFiles2 = file6.listFiles();
                    if (listFiles2 != null && listFiles2.length > 0) {
                        if (z2 || listFiles2.length >= 2 || (file3 != null && file3.getAbsolutePath().startsWith(file6.getAbsolutePath()))) {
                            UCCyclone.recursiveDelete(file6, true, file3);
                        }
                    }
                } catch (Throwable th5) {
                }
            } catch (Throwable th6) {
            }
        } catch (Throwable th7) {
        }
    }

    /* compiled from: ProGuard */
    public class a {
        File a;
        File b;
        File c;
        public final ValueCallback<CALLBACK_TYPE> d = new bb(this);
        public final ValueCallback<CALLBACK_TYPE> e = new bc(this);

        static /* synthetic */ void a(a aVar) {
            UCSetupTask uCSetupTask;
            String str;
            boolean z = false;
            if (aVar.b.exists()) {
                if (!aVar.a.exists() || !aVar.c.exists()) {
                    z = true;
                } else {
                    if (System.currentTimeMillis() - Math.max(aVar.b.lastModified(), aVar.c.lastModified()) > ZipAppConstants.UPDATEGROUPID_AGE) {
                        try {
                            aVar.b.delete();
                        } catch (Throwable th) {
                        }
                        try {
                            aVar.a.delete();
                        } catch (Throwable th2) {
                        }
                    } else {
                        String unused = UCSetupTask.this.g = "2";
                        uCSetupTask = UCSetupTask.this;
                        str = "crash_repeat";
                        uCSetupTask.callback(str);
                    }
                }
            } else if (aVar.a.exists() && aVar.c.exists()) {
                z = true;
            }
            String unused2 = UCSetupTask.this.g = z ? "1" : "0";
            uCSetupTask = UCSetupTask.this;
            str = z ? "crash_seen" : "crash_none";
            uCSetupTask.callback(str);
        }

        a(String str) {
            if (this.a == null) {
                File file = (File) UCMPackageInfo.invoke(UCMPackageInfo.expectCreateDirFile2P, (File) UCMPackageInfo.invoke(10005, (Context) UCSetupTask.this.mOptions.get("CONTEXT")), (String) UCMPackageInfo.invoke(10012, str));
                this.a = new File(file, "b36ce8d879e33bc88f717f74617ea05a");
                this.b = new File(file, "bd89426940609c9ae14e5ae90827201b");
                this.c = new File(file, "51bfcd9dd2f1379936c4fbb3558a6e67");
            }
        }
    }
}
