package com.uc.webview.export.internal.setup;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Pair;
import android.webkit.ValueCallback;
import com.uc.webview.export.annotations.Reflection;
import com.uc.webview.export.internal.setup.UCAsyncTask;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

/* compiled from: ProGuard */
public class UCAsyncTask<RETURN_TYPE extends UCAsyncTask, CALLBACK_TYPE extends UCAsyncTask> implements Runnable {
    protected static final String EVENT_COST = "cost";
    protected static final String EVENT_DIE = "die";
    protected static final String EVENT_EXCEPTION = "exception";
    protected static final String EVENT_GONE = "gone";
    protected static final String EVENT_PAUSE = "pause";
    protected static final String EVENT_PROGRESS = "progress";
    protected static final String EVENT_RESUME = "resume";
    protected static final String EVENT_START = "start";
    protected static final String EVENT_STOP = "stop";
    protected static final String EVENT_SUCCESS = "success";
    public static final int cleanThread = 10008;
    public static final int clearSubTasks = 10012;
    public static final int getBlankString = 10011;
    public static final int getCallback = 10007;
    public static final int getCosts = 10006;
    public static final int getEvent = 10009;
    public static final int getParent = 10005;
    public static final int getParentCnt = 10010;
    public static final int getPercent = 10017;
    public static final int getPriority = 10013;
    public static final int getRootTask = 10015;
    public static final int getTaskCount = 10014;
    public static final int inThread = 10016;
    public static final int isPaused = 10018;
    /* access modifiers changed from: private */
    public static final Boolean m = false;
    public static final int post = 10004;
    public static final int setCallbacks = 10002;
    public static final int setParent = 10001;
    public static final int setPriority = 10003;
    /* access modifiers changed from: private */
    public UCAsyncTask a;
    /* access modifiers changed from: private */
    public LinkedList<UCAsyncTask> b;
    private int c;
    /* access modifiers changed from: private */
    public final Integer d;
    /* access modifiers changed from: private */
    public boolean e;
    /* access modifiers changed from: private */
    public boolean f;
    /* access modifiers changed from: private */
    public final ap g;
    private HandlerThread h;
    /* access modifiers changed from: private */
    public Handler i;
    private String j;
    /* access modifiers changed from: private */
    public long k;
    private Runnable l;
    protected HashMap<String, ValueCallback<CALLBACK_TYPE>> mCallbacks;
    protected UCSetupException mException;
    protected boolean mHasStarted;
    protected int mPercent;
    /* access modifiers changed from: private */
    public Vector<Pair<String, Pair<Long, Long>>> n;

    static /* synthetic */ int e(UCAsyncTask uCAsyncTask) {
        int i2 = uCAsyncTask.c + 1;
        uCAsyncTask.c = i2;
        return i2;
    }

    public UCAsyncTask(Integer num) {
        Vector<Pair<String, Pair<Long, Long>>> vector;
        this.c = 0;
        this.e = false;
        this.f = false;
        this.g = new ap();
        this.k = 0;
        this.mHasStarted = false;
        if (m.booleanValue()) {
            vector = new Vector<>();
        } else {
            vector = null;
        }
        this.n = vector;
        this.d = num;
    }

    public UCAsyncTask(Runnable runnable) {
        this((Integer) 0);
        this.l = runnable;
    }

    public UCAsyncTask(UCAsyncTask uCAsyncTask) {
        this((Runnable) null);
        invoke(10001, uCAsyncTask);
    }

    public final RETURN_TYPE invoke(int i2, Object... objArr) {
        invokeO(i2, objArr);
        return this;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00db, code lost:
        if (r5.invokeO(10005, new java.lang.Object[0]) == null) goto L_0x00e7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00dd, code lost:
        r5 = (com.uc.webview.export.internal.setup.UCAsyncTask) r5.invokeO(10005, new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:?, code lost:
        return r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object invokeO(int r6, java.lang.Object... r7) {
        /*
            r5 = this;
            r0 = 1
            r4 = 10005(0x2715, float:1.402E-41)
            r2 = 0
            r1 = 0
            switch(r6) {
                case 10001: goto L_0x000a;
                case 10002: goto L_0x0011;
                case 10003: goto L_0x0018;
                case 10004: goto L_0x0024;
                case 10005: goto L_0x0063;
                case 10006: goto L_0x0066;
                case 10007: goto L_0x0069;
                case 10008: goto L_0x004d;
                case 10009: goto L_0x007c;
                case 10010: goto L_0x007f;
                case 10011: goto L_0x009e;
                case 10012: goto L_0x0059;
                case 10013: goto L_0x00bd;
                case 10014: goto L_0x00c1;
                case 10015: goto L_0x00d5;
                case 10016: goto L_0x00ea;
                case 10017: goto L_0x00fa;
                case 10018: goto L_0x0102;
                default: goto L_0x0008;
            }
        L_0x0008:
            r0 = r2
        L_0x0009:
            return r0
        L_0x000a:
            r0 = r7[r1]
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = (com.uc.webview.export.internal.setup.UCAsyncTask) r0
            r5.a = r0
            goto L_0x0008
        L_0x0011:
            r0 = r7[r1]
            java.util.HashMap r0 = (java.util.HashMap) r0
            r5.mCallbacks = r0
            goto L_0x0008
        L_0x0018:
            r0 = r7[r1]
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            android.os.Process.setThreadPriority(r0)
            goto L_0x0008
        L_0x0024:
            r0 = r7[r1]
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = (com.uc.webview.export.internal.setup.UCAsyncTask) r0
            com.uc.webview.export.internal.setup.UCAsyncTask r1 = r0.a
            if (r1 == r5) goto L_0x0035
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.String r1 = "Please use \"new UCAsyncTask(parentTask).start()\" instead of \"post(new UCAsyncTask())\" to add sub task."
            r0.<init>(r1)
            throw r0
        L_0x0035:
            java.lang.Integer r1 = r5.d
            monitor-enter(r1)
            java.util.LinkedList<com.uc.webview.export.internal.setup.UCAsyncTask> r3 = r5.b     // Catch:{ all -> 0x004a }
            if (r3 != 0) goto L_0x0043
            java.util.LinkedList r3 = new java.util.LinkedList     // Catch:{ all -> 0x004a }
            r3.<init>()     // Catch:{ all -> 0x004a }
            r5.b = r3     // Catch:{ all -> 0x004a }
        L_0x0043:
            java.util.LinkedList<com.uc.webview.export.internal.setup.UCAsyncTask> r3 = r5.b     // Catch:{ all -> 0x004a }
            r3.add(r0)     // Catch:{ all -> 0x004a }
            monitor-exit(r1)     // Catch:{ all -> 0x004a }
            goto L_0x0008
        L_0x004a:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x004a }
            throw r0
        L_0x004d:
            r5.b = r2
            r5.i = r2
            android.os.HandlerThread r0 = r5.h
            r0.quit()
            r5.h = r2
            goto L_0x0008
        L_0x0059:
            java.util.LinkedList<com.uc.webview.export.internal.setup.UCAsyncTask> r0 = r5.b
            if (r0 == 0) goto L_0x0008
            java.util.LinkedList<com.uc.webview.export.internal.setup.UCAsyncTask> r0 = r5.b
            r0.clear()
            goto L_0x0008
        L_0x0063:
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r5.a
            goto L_0x0009
        L_0x0066:
            java.util.Vector<android.util.Pair<java.lang.String, android.util.Pair<java.lang.Long, java.lang.Long>>> r0 = r5.n
            goto L_0x0009
        L_0x0069:
            r0 = r7[r1]
            java.lang.String r0 = (java.lang.String) r0
            java.util.HashMap<java.lang.String, android.webkit.ValueCallback<CALLBACK_TYPE>> r1 = r5.mCallbacks
            if (r1 != 0) goto L_0x0073
            r0 = r2
            goto L_0x0009
        L_0x0073:
            java.util.HashMap<java.lang.String, android.webkit.ValueCallback<CALLBACK_TYPE>> r1 = r5.mCallbacks
            java.lang.Object r0 = r1.get(r0)
            android.webkit.ValueCallback r0 = (android.webkit.ValueCallback) r0
            goto L_0x0009
        L_0x007c:
            java.lang.String r0 = r5.j
            goto L_0x0009
        L_0x007f:
            java.lang.Object[] r0 = new java.lang.Object[r1]
            java.lang.Object r0 = r5.invokeO(r4, r0)
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = (com.uc.webview.export.internal.setup.UCAsyncTask) r0
            r3 = r0
            r0 = r1
        L_0x0089:
            if (r3 == 0) goto L_0x0098
            int r2 = r0 + 1
            java.lang.Object[] r0 = new java.lang.Object[r1]
            java.lang.Object r0 = r3.invokeO(r4, r0)
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = (com.uc.webview.export.internal.setup.UCAsyncTask) r0
            r3 = r0
            r0 = r2
            goto L_0x0089
        L_0x0098:
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            goto L_0x0009
        L_0x009e:
            r0 = r7[r1]
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
        L_0x00ab:
            int r1 = r0 + -1
            if (r0 <= 0) goto L_0x00b7
            java.lang.String r0 = "    "
            r2.append(r0)
            r0 = r1
            goto L_0x00ab
        L_0x00b7:
            java.lang.String r0 = r2.toString()
            goto L_0x0009
        L_0x00bd:
            java.lang.Integer r0 = r5.d
            goto L_0x0009
        L_0x00c1:
            java.util.LinkedList<com.uc.webview.export.internal.setup.UCAsyncTask> r1 = r5.b
            if (r1 != 0) goto L_0x00cb
        L_0x00c5:
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            goto L_0x0009
        L_0x00cb:
            java.util.LinkedList<com.uc.webview.export.internal.setup.UCAsyncTask> r0 = r5.b
            int r0 = r0.size()
            int r1 = r5.c
            int r0 = r0 + r1
            goto L_0x00c5
        L_0x00d5:
            java.lang.Object[] r0 = new java.lang.Object[r1]
            java.lang.Object r0 = r5.invokeO(r4, r0)
            if (r0 == 0) goto L_0x00e7
            java.lang.Object[] r0 = new java.lang.Object[r1]
            java.lang.Object r0 = r5.invokeO(r4, r0)
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = (com.uc.webview.export.internal.setup.UCAsyncTask) r0
            r5 = r0
            goto L_0x00d5
        L_0x00e7:
            r0 = r5
            goto L_0x0009
        L_0x00ea:
            java.lang.Thread r2 = java.lang.Thread.currentThread()
            android.os.HandlerThread r3 = r5.h
            if (r2 != r3) goto L_0x00f8
        L_0x00f2:
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            goto L_0x0009
        L_0x00f8:
            r0 = r1
            goto L_0x00f2
        L_0x00fa:
            int r0 = r5.mPercent
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            goto L_0x0009
        L_0x0102:
            r0 = 10015(0x271f, float:1.4034E-41)
            java.lang.Object[] r1 = new java.lang.Object[r1]
            java.lang.Object r0 = r5.invokeO(r0, r1)
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = (com.uc.webview.export.internal.setup.UCAsyncTask) r0
            com.uc.webview.export.internal.setup.ap r1 = r0.g
            monitor-enter(r1)
            com.uc.webview.export.internal.setup.ap r0 = r0.g     // Catch:{ all -> 0x011a }
            boolean r0 = r0.b     // Catch:{ all -> 0x011a }
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)     // Catch:{ all -> 0x011a }
            monitor-exit(r1)     // Catch:{ all -> 0x011a }
            goto L_0x0009
        L_0x011a:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x011a }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.UCAsyncTask.invokeO(int, java.lang.Object[]):java.lang.Object");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x00d4 A[Catch:{ Throwable -> 0x0169 }] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x00ef A[SYNTHETIC, Splitter:B:18:0x00ef] */
    /* JADX WARNING: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void callback(java.lang.String r12) {
        /*
            r11 = this;
            r10 = 1
            r9 = 0
            r11.j = r12
            com.uc.webview.export.internal.setup.UCSetupException r3 = r11.getException()     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r0 = "stat"
            r0.equals(r12)     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r0 = "cost"
            boolean r0 = r0.equals(r12)     // Catch:{ Throwable -> 0x0169 }
            if (r0 == 0) goto L_0x00f3
            java.lang.Boolean r0 = m     // Catch:{ Throwable -> 0x0169 }
            boolean r0 = r0.booleanValue()     // Catch:{ Throwable -> 0x0169 }
            if (r0 == 0) goto L_0x00b6
            java.util.Vector<android.util.Pair<java.lang.String, android.util.Pair<java.lang.Long, java.lang.Long>>> r0 = r11.n     // Catch:{ Throwable -> 0x0169 }
            java.lang.Object r0 = r0.lastElement()     // Catch:{ Throwable -> 0x0169 }
            android.util.Pair r0 = (android.util.Pair) r0     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r2 = "UCAsyncTask"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r1 = "callback: "
            r4.<init>(r1)     // Catch:{ Throwable -> 0x0169 }
            r5 = 10011(0x271b, float:1.4028E-41)
            r1 = 1
            java.lang.Object[] r6 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x0169 }
            r7 = 0
            r1 = 10010(0x271a, float:1.4027E-41)
            r8 = 0
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ Throwable -> 0x0169 }
            java.lang.Object r1 = r11.invokeO(r1, r8)     // Catch:{ Throwable -> 0x0169 }
            java.lang.Integer r1 = (java.lang.Integer) r1     // Catch:{ Throwable -> 0x0169 }
            r6[r7] = r1     // Catch:{ Throwable -> 0x0169 }
            java.lang.Object r1 = r11.invokeO(r5, r6)     // Catch:{ Throwable -> 0x0169 }
            java.lang.StringBuilder r1 = r4.append(r1)     // Catch:{ Throwable -> 0x0169 }
            java.lang.Class r4 = r11.getClass()     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r4 = r4.getSimpleName()     // Catch:{ Throwable -> 0x0169 }
            java.lang.StringBuilder r1 = r1.append(r4)     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r4 = "."
            java.lang.StringBuilder r1 = r1.append(r4)     // Catch:{ Throwable -> 0x0169 }
            java.lang.StringBuilder r1 = r1.append(r12)     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r4 = " cost:"
            java.lang.StringBuilder r4 = r1.append(r4)     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r5 = "%5s"
            r1 = 1
            java.lang.Object[] r6 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x0169 }
            r7 = 0
            java.lang.Object r1 = r0.second     // Catch:{ Throwable -> 0x0169 }
            android.util.Pair r1 = (android.util.Pair) r1     // Catch:{ Throwable -> 0x0169 }
            java.lang.Object r1 = r1.first     // Catch:{ Throwable -> 0x0169 }
            r6[r7] = r1     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r1 = java.lang.String.format(r5, r6)     // Catch:{ Throwable -> 0x0169 }
            java.lang.StringBuilder r1 = r4.append(r1)     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r4 = " cost_cpu:"
            java.lang.StringBuilder r4 = r1.append(r4)     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r5 = "%5s"
            r1 = 1
            java.lang.Object[] r6 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x0169 }
            r7 = 0
            java.lang.Object r1 = r0.second     // Catch:{ Throwable -> 0x0169 }
            android.util.Pair r1 = (android.util.Pair) r1     // Catch:{ Throwable -> 0x0169 }
            java.lang.Object r1 = r1.second     // Catch:{ Throwable -> 0x0169 }
            r6[r7] = r1     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r1 = java.lang.String.format(r5, r6)     // Catch:{ Throwable -> 0x0169 }
            java.lang.StringBuilder r1 = r4.append(r1)     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r4 = " task:"
            java.lang.StringBuilder r1 = r1.append(r4)     // Catch:{ Throwable -> 0x0169 }
            java.lang.Object r0 = r0.first     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Throwable -> 0x0169 }
        L_0x00ab:
            java.lang.StringBuilder r0 = r1.append(r0)     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x0169 }
            com.uc.webview.export.internal.utility.Log.d(r2, r0)     // Catch:{ Throwable -> 0x0169 }
        L_0x00b6:
            java.lang.String r0 = "exception"
            boolean r0 = r0.equals(r12)     // Catch:{ Throwable -> 0x0169 }
            if (r0 == 0) goto L_0x00e1
            boolean r0 = r11 instanceof com.uc.webview.export.internal.setup.UCSetupTask     // Catch:{ Throwable -> 0x0169 }
            if (r0 == 0) goto L_0x00e1
            if (r3 == 0) goto L_0x00e1
            java.lang.String r0 = "UCAsyncTask"
            java.lang.String r1 = "callback: exception: "
            com.uc.webview.export.internal.utility.Log.w(r0, r1, r3)     // Catch:{ Throwable -> 0x0169 }
            java.lang.Throwable r0 = r3.getRootCause()     // Catch:{ Throwable -> 0x0169 }
            if (r3 == r0) goto L_0x00e1
            java.lang.String r0 = "UCAsyncTask"
            java.lang.String r1 = "callback: rootCause: "
            java.lang.Throwable r2 = r3.getRootCause()     // Catch:{ Throwable -> 0x0169 }
            com.uc.webview.export.internal.utility.Log.w(r0, r1, r2)     // Catch:{ Throwable -> 0x0169 }
        L_0x00e1:
            r0 = 10007(0x2717, float:1.4023E-41)
            java.lang.Object[] r1 = new java.lang.Object[r10]
            r1[r9] = r12
            java.lang.Object r0 = r11.invokeO(r0, r1)
            android.webkit.ValueCallback r0 = (android.webkit.ValueCallback) r0
            if (r0 == 0) goto L_0x00f2
            r0.onReceiveValue(r11)     // Catch:{ Throwable -> 0x0167 }
        L_0x00f2:
            return
        L_0x00f3:
            java.lang.String r2 = "UCAsyncTask"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r0 = "callback: "
            r1.<init>(r0)     // Catch:{ Throwable -> 0x0169 }
            r4 = 10011(0x271b, float:1.4028E-41)
            r0 = 1
            java.lang.Object[] r5 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x0169 }
            r6 = 0
            r0 = 10010(0x271a, float:1.4027E-41)
            r7 = 0
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ Throwable -> 0x0169 }
            java.lang.Object r0 = r11.invokeO(r0, r7)     // Catch:{ Throwable -> 0x0169 }
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Throwable -> 0x0169 }
            r5[r6] = r0     // Catch:{ Throwable -> 0x0169 }
            java.lang.Object r0 = r11.invokeO(r4, r5)     // Catch:{ Throwable -> 0x0169 }
            java.lang.StringBuilder r0 = r1.append(r0)     // Catch:{ Throwable -> 0x0169 }
            java.lang.Class r1 = r11.getClass()     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r1 = r1.getSimpleName()     // Catch:{ Throwable -> 0x0169 }
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r1 = "."
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x0169 }
            java.lang.StringBuilder r0 = r0.append(r12)     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r1 = " "
            java.lang.StringBuilder r1 = r0.append(r1)     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r0 = "progress"
            boolean r0 = r0.equals(r12)     // Catch:{ Throwable -> 0x0169 }
            if (r0 == 0) goto L_0x015e
            r0 = 10017(0x2721, float:1.4037E-41)
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0169 }
            java.lang.Object r0 = r11.invokeO(r0, r4)     // Catch:{ Throwable -> 0x0169 }
        L_0x0149:
            java.lang.StringBuilder r1 = r1.append(r0)     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r0 = "exception"
            boolean r0 = r0.equals(r12)     // Catch:{ Throwable -> 0x0169 }
            if (r0 == 0) goto L_0x0162
            if (r3 == 0) goto L_0x0162
            java.lang.String r0 = r3.toString()     // Catch:{ Throwable -> 0x0169 }
            goto L_0x00ab
        L_0x015e:
            java.lang.String r0 = ""
            goto L_0x0149
        L_0x0162:
            java.lang.String r0 = ""
            goto L_0x00ab
        L_0x0167:
            r0 = move-exception
            goto L_0x00f2
        L_0x0169:
            r0 = move-exception
            goto L_0x00e1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.UCAsyncTask.callback(java.lang.String):void");
    }

    public RETURN_TYPE start() {
        synchronized (this.d) {
            if (!this.mHasStarted || (this.a == null && this.h == null)) {
                this.mHasStarted = true;
                if (this.a != null) {
                    this.a.invoke(10004, this);
                } else {
                    aq aqVar = new aq(this, UCAsyncTask.class.getSimpleName(), this.d.intValue());
                    this.h = aqVar;
                    aqVar.start();
                }
            }
        }
        return this;
    }

    public final RETURN_TYPE start(long j2) {
        this.k = j2;
        return start();
    }

    public RETURN_TYPE stop() {
        synchronized (this.g) {
            resume();
            this.f = true;
        }
        return this;
    }

    public RETURN_TYPE pause() {
        UCAsyncTask uCAsyncTask = (UCAsyncTask) invokeO(getRootTask, new Object[0]);
        synchronized (uCAsyncTask.g) {
            if (!uCAsyncTask.g.b) {
                uCAsyncTask.e = true;
            }
        }
        return this;
    }

    public RETURN_TYPE resume() {
        UCAsyncTask uCAsyncTask = (UCAsyncTask) invokeO(getRootTask, new Object[0]);
        synchronized (uCAsyncTask.g) {
            uCAsyncTask.e = false;
            if (uCAsyncTask.g.b) {
                ap apVar = uCAsyncTask.g;
                synchronized (apVar) {
                    apVar.a = new Pair<>(0, (Object) null);
                    try {
                        apVar.notify();
                    } catch (Exception e2) {
                    }
                }
            }
        }
        return this;
    }

    public void run() {
        if (this.l != null) {
            this.l.run();
        }
    }

    public RETURN_TYPE onEvent(String str, ValueCallback<CALLBACK_TYPE> valueCallback) {
        if (this.mCallbacks == null) {
            this.mCallbacks = new HashMap<>();
        }
        this.mCallbacks.put(str, valueCallback);
        return this;
    }

    @Reflection
    public UCSetupException getException() {
        return this.mException;
    }

    public void setException(UCSetupException uCSetupException) {
        this.mException = uCSetupException;
    }

    /* compiled from: ProGuard */
    public class b<CB_TYPE extends UCAsyncTask<CB_TYPE, CB_TYPE>> implements ValueCallback<CB_TYPE> {
        public b() {
        }

        public final /* synthetic */ void onReceiveValue(Object obj) {
            UCAsyncTask.this.setException(((UCAsyncTask) obj).getException());
        }
    }

    /* compiled from: ProGuard */
    public class c<CB_TYPE extends UCAsyncTask<CB_TYPE, CB_TYPE>> implements ValueCallback<CB_TYPE> {
        public c() {
        }

        public final /* synthetic */ void onReceiveValue(Object obj) {
            UCAsyncTask.this.stop();
        }
    }

    /* compiled from: ProGuard */
    public class a<CB_TYPE extends UCAsyncTask<CB_TYPE, CB_TYPE>> implements ValueCallback<CB_TYPE> {
        public a() {
        }

        public final /* synthetic */ void onReceiveValue(Object obj) {
            UCAsyncTask.this.callback((String) ((UCAsyncTask) obj).invokeO(10009, new Object[0]));
        }
    }
}
