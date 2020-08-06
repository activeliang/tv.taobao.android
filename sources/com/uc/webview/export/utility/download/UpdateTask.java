package com.uc.webview.export.utility.download;

import android.content.Context;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.util.Pair;
import android.webkit.ValueCallback;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.setup.UCMPackageInfo;
import com.uc.webview.export.internal.utility.c;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import java.io.File;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.codec.language.Soundex;

@Api
/* compiled from: ProGuard */
public class UpdateTask {
    public static final String START_FLG_FILE_NAME = "299772b0fd1634653ae3c31f366de3f8";
    public static final String STOP_FLG_FILE_NAME = "2e67cdbeb4ec133dcc8204d930aa7145";
    private static final ConcurrentHashMap<Integer, Integer> a = new ConcurrentHashMap<>();
    private final String[] b = new String[3];
    /* access modifiers changed from: private */
    public final long[] c = new long[6];
    private final ValueCallback<UpdateTask>[] d = new ValueCallback[10];
    /* access modifiers changed from: private */
    public final Object[] e = new Object[3];
    /* access modifiers changed from: private */
    public int f = 0;
    /* access modifiers changed from: private */
    public String g = "core.jar";
    /* access modifiers changed from: private */
    public ValueCallback<Object[]> h;

    static /* synthetic */ void a(File file, boolean z) {
        if (z) {
            new File(file, STOP_FLG_FILE_NAME).createNewFile();
        } else {
            new File(file, START_FLG_FILE_NAME).createNewFile();
        }
    }

    static /* synthetic */ int f(UpdateTask updateTask) {
        int i = updateTask.f + 10;
        updateTask.f = i;
        return i;
    }

    public UpdateTask(Context context, String str, String str2, String str3, ValueCallback<Object[]> valueCallback, Long l, Long l2) {
        l = l == null ? 60000L : l;
        l2 = l2 == null ? 604800000L : l2;
        int hashCode = str.hashCode();
        synchronized (a) {
            if (a.containsKey(Integer.valueOf(hashCode))) {
                throw new RuntimeException("Duplicate task.");
            }
            a.put(Integer.valueOf(hashCode), Integer.valueOf(hashCode));
        }
        String valueOf = hashCode >= 0 ? String.valueOf(hashCode) : String.valueOf(hashCode).replace(Soundex.SILENT_MARKER, '_');
        IWaStat.WaStat.stat(IWaStat.UCM);
        this.h = valueCallback;
        this.c[0] = (long) hashCode;
        this.c[4] = l.longValue();
        this.c[5] = l2.longValue();
        this.b[0] = str;
        this.b[1] = str2;
        this.b[2] = valueOf;
        this.e[0] = context;
        this.e[2] = new DownloadTask(context, str, valueCallback);
        this.g = str3;
    }

    public static final File getUCPlayerRoot(Context context) {
        if (d.r != null) {
            return new File(d.r);
        }
        return (File) UCMPackageInfo.invoke(10001, context, "ucplayer");
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        try {
            synchronized (a) {
                a.remove(Long.valueOf(this.c[0]));
            }
        } catch (Throwable th) {
        }
    }

    public UpdateTask onEvent(String str, ValueCallback<UpdateTask> valueCallback) {
        if (str.equals(BlitzServiceUtils.CSUCCESS)) {
            this.d[0] = valueCallback;
        } else if (str.equals("failed")) {
            this.d[1] = valueCallback;
        } else if (str.equals("recovered")) {
            this.d[2] = valueCallback;
        } else if (str.equals("progress")) {
            this.d[3] = valueCallback;
        } else if (str.equals("exception")) {
            this.d[4] = valueCallback;
        } else if (str.equals("check")) {
            this.d[5] = valueCallback;
        } else if (str.equals("exists")) {
            this.d[6] = valueCallback;
        } else if (str.equals("beginDownload")) {
            this.d[7] = valueCallback;
        } else if (str.equals("beginUnZip")) {
            this.d[8] = valueCallback;
        } else if (str.equals("unzipSuccess")) {
            this.d[9] = valueCallback;
        } else {
            throw new RuntimeException("The given event:" + str + " is unknown.");
        }
        return this;
    }

    public final File getUpdateDir() {
        if (this.c[1] <= 0) {
            Pair<Long, Long> a2 = c.a(this.b[0], (URL) null);
            this.c[1] = ((Long) a2.first).longValue();
            this.c[2] = ((Long) a2.second).longValue();
        }
        return new File(this.b[1] + WVNativeCallbackUtil.SEPERATER + this.b[2] + WVNativeCallbackUtil.SEPERATER + (String.valueOf(this.c[1]) + "_" + this.c[2]));
    }

    public Throwable getException() {
        return (Throwable) this.e[1];
    }

    public UpdateTask start() {
        ValueCallback<UpdateTask> valueCallback = this.d[0];
        ValueCallback<UpdateTask> valueCallback2 = this.d[1];
        ValueCallback<UpdateTask> valueCallback3 = this.d[2];
        ValueCallback<UpdateTask> valueCallback4 = this.d[3];
        ValueCallback<UpdateTask> valueCallback5 = this.d[4];
        ValueCallback<UpdateTask> valueCallback6 = this.d[5];
        ValueCallback<UpdateTask> valueCallback7 = this.d[6];
        ValueCallback<UpdateTask> valueCallback8 = this.d[7];
        ValueCallback<UpdateTask> valueCallback9 = this.d[8];
        ValueCallback<UpdateTask> valueCallback10 = this.d[9];
        String str = this.b[0];
        DownloadTask downloadTask = (DownloadTask) this.e[2];
        this.f = 0;
        d dVar = new d(this, valueCallback4, str, downloadTask, valueCallback2, valueCallback9, valueCallback10, valueCallback, valueCallback5);
        downloadTask.onEvent("check", new n(this, valueCallback6)).onEvent(BlitzServiceUtils.CSUCCESS, new m(this, dVar)).onEvent("exists", new l(this, dVar)).onEvent("failed", new k(this, valueCallback2)).onEvent("progress", new j(this, valueCallback4)).onEvent("exception", new h(this, valueCallback5)).onEvent("header", new g(this, valueCallback7, valueCallback3, valueCallback4, valueCallback5)).onEvent("beginDownload", new f(this, valueCallback8)).start();
        return this;
    }

    public UpdateTask stop() {
        ((DownloadTask) this.e[2]).stop();
        return this;
    }

    public UpdateTask delete() {
        ((DownloadTask) this.e[2]).stopWith(new e(this));
        return this;
    }

    public static boolean isFinished(File file, String str) {
        return file.exists() && new File(file, str).exists() && ((!new File(file, START_FLG_FILE_NAME).exists() && !new File(file, "c34d62af061f389f7e4c9f0e835f7a54").exists()) || new File(file, STOP_FLG_FILE_NAME).exists() || new File(file, "95b70b3ec9f6407a92becf890996088d").exists());
    }

    public static File getUpdateRoot(Context context) {
        return (File) UCMPackageInfo.invoke(10002, context);
    }
}
