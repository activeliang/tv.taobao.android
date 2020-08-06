package com.uc.webview.export.cyclone;

import android.content.Context;
import android.util.Pair;
import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentLinkedQueue;

/* compiled from: ProGuard */
public class UCDex implements Runnable {
    private static boolean a = false;
    private static ConcurrentLinkedQueue<Pair<Integer, Object>> b = new ConcurrentLinkedQueue<>();
    private static boolean c = false;
    private static boolean d = true;
    private static Context e = null;
    private static Method f = null;
    private static Method g = null;
    /* access modifiers changed from: private */
    public static String h = DexFormat.DEX_IN_JAR_NAME;
    private static int i = 0;

    private static native int initArt();

    private static native int initDvm(int i2);

    private static native int openDexFile(String str, String str2, int i2);

    private static native int openDexFile(byte[] bArr);

    private static native int syncData(boolean z);

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v13, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int openDexFileUC(java.lang.String r11, java.lang.String r12, int r13) {
        /*
            r7 = 0
            r10 = 1
            r6 = 0
            java.lang.String r0 = r11.trim()
            int r1 = r0.length()     // Catch:{ Throwable -> 0x00ae }
            int r1 = r1 + -4
            java.lang.String r1 = r0.substring(r1)     // Catch:{ Throwable -> 0x00ae }
            java.lang.String r1 = r1.toLowerCase()     // Catch:{ Throwable -> 0x00ae }
            java.lang.String r2 = ".dex"
            boolean r1 = r1.endsWith(r2)     // Catch:{ Throwable -> 0x00ae }
            if (r1 != 0) goto L_0x0146
            java.io.File r2 = com.uc.webview.export.cyclone.UCCyclone.expectFile((java.lang.String) r0)     // Catch:{ Throwable -> 0x00ae }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00ae }
            r0.<init>()     // Catch:{ Throwable -> 0x00ae }
            java.lang.String r1 = com.uc.webview.export.cyclone.UCCyclone.a((java.io.File) r2)     // Catch:{ Throwable -> 0x00ae }
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x00ae }
            java.lang.String r1 = ".dex"
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x00ae }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x00ae }
            android.content.Context r1 = e     // Catch:{ Throwable -> 0x00ae }
            java.io.File r3 = com.uc.webview.export.cyclone.UCCyclone.a((android.content.Context) r1)     // Catch:{ Throwable -> 0x00ae }
            java.io.File r8 = new java.io.File     // Catch:{ Throwable -> 0x00ae }
            r8.<init>(r3, r0)     // Catch:{ Throwable -> 0x00ae }
            boolean r0 = r8.exists()     // Catch:{ Throwable -> 0x00ae }
            if (r0 != 0) goto L_0x0066
            com.uc.webview.export.cyclone.UCDex$1 r4 = new com.uc.webview.export.cyclone.UCDex$1     // Catch:{ Throwable -> 0x00ae }
            r4.<init>()     // Catch:{ Throwable -> 0x00ae }
            android.content.Context r0 = e     // Catch:{ Throwable -> 0x00ae }
            r1 = 0
            r5 = 0
            com.uc.webview.export.cyclone.UCCyclone.decompressIfNeeded(r0, r1, r2, r3, r4, r5)     // Catch:{ Throwable -> 0x00ae }
            java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x00ae }
            java.lang.String r1 = h     // Catch:{ Throwable -> 0x00ae }
            r0.<init>(r3, r1)     // Catch:{ Throwable -> 0x00ae }
            boolean r0 = r0.renameTo(r8)     // Catch:{ Throwable -> 0x00ae }
            if (r0 != 0) goto L_0x0066
            r0 = r6
        L_0x0065:
            return r0
        L_0x0066:
            java.lang.String r0 = r8.getAbsolutePath()     // Catch:{ Throwable -> 0x00ae }
            r2 = r0
        L_0x006b:
            java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x00ae }
            r0.<init>(r2)     // Catch:{ Throwable -> 0x00ae }
            long r4 = r0.length()     // Catch:{ Throwable -> 0x00ae }
            r8 = 2147483647(0x7fffffff, double:1.060997895E-314)
            int r1 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r1 >= 0) goto L_0x00c0
            long r4 = r0.length()     // Catch:{ Throwable -> 0x00ae }
            int r1 = (int) r4     // Catch:{ Throwable -> 0x00ae }
            byte[] r3 = new byte[r1]     // Catch:{ Throwable -> 0x00ae }
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x00ae }
            r4.<init>(r0)     // Catch:{ Throwable -> 0x00ae }
            int r0 = r4.read(r3)     // Catch:{ Throwable -> 0x00ae }
            if (r0 == r1) goto L_0x00c2
            java.lang.Exception r2 = new java.lang.Exception     // Catch:{ Throwable -> 0x00ae }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00ae }
            java.lang.String r4 = "Read length not match:"
            r3.<init>(r4)     // Catch:{ Throwable -> 0x00ae }
            java.lang.StringBuilder r0 = r3.append(r0)     // Catch:{ Throwable -> 0x00ae }
            java.lang.String r3 = "/"
            java.lang.StringBuilder r0 = r0.append(r3)     // Catch:{ Throwable -> 0x00ae }
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x00ae }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x00ae }
            r2.<init>(r0)     // Catch:{ Throwable -> 0x00ae }
            throw r2     // Catch:{ Throwable -> 0x00ae }
        L_0x00ae:
            r0 = move-exception
            boolean r1 = com.uc.webview.export.cyclone.UCCyclone.enableDebugLog
            if (r1 != 0) goto L_0x013a
            r1 = r7
        L_0x00b4:
            if (r1 == 0) goto L_0x00c0
            java.lang.String r2 = "UCDex.openDexFileUC: opt_dex error: "
            java.lang.Throwable[] r3 = new java.lang.Throwable[r10]
            r3[r6] = r0
            r1.print(r2, r3)
        L_0x00c0:
            r0 = r6
            goto L_0x0065
        L_0x00c2:
            java.lang.reflect.Method r0 = f     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            if (r0 != 0) goto L_0x00ce
            int r0 = openDexFile(r3)     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            com.uc.webview.export.cyclone.UCCyclone.close(r4)     // Catch:{ Throwable -> 0x00ae }
            goto L_0x0065
        L_0x00ce:
            java.lang.reflect.Method r0 = f     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            r1 = 1
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            r5 = 0
            r1[r5] = r3     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            java.lang.Object r0 = com.uc.webview.export.cyclone.UCCyclone.a((java.lang.reflect.Method) r0, (java.lang.Object[]) r1)     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            int r0 = r0.intValue()     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            boolean r1 = com.uc.webview.export.cyclone.UCCyclone.enableDebugLog     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            if (r1 != 0) goto L_0x0125
            r1 = r7
        L_0x00e5:
            if (r1 == 0) goto L_0x0108
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            java.lang.String r5 = "UCDex.openDexFileUC: "
            r3.<init>(r5)     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            java.lang.StringBuilder r2 = r3.append(r2)     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            java.lang.String r3 = " = "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            java.lang.StringBuilder r2 = r2.append(r0)     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            r3 = 0
            java.lang.Throwable[] r3 = new java.lang.Throwable[r3]     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            r1.print(r2, r3)     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
        L_0x0108:
            if (r0 != 0) goto L_0x0130
            java.lang.Exception r1 = new java.lang.Exception     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            java.lang.String r3 = "OpenDexFile:"
            r2.<init>(r3)     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            java.lang.StringBuilder r0 = r2.append(r0)     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            r1.<init>(r0)     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            throw r1     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
        L_0x0120:
            r0 = move-exception
            com.uc.webview.export.cyclone.UCCyclone.close(r4)     // Catch:{ Throwable -> 0x00ae }
            goto L_0x00c0
        L_0x0125:
            java.lang.String r1 = "d"
            java.lang.String r3 = "cyclone"
            com.uc.webview.export.cyclone.UCLogger r1 = com.uc.webview.export.cyclone.UCLogger.create(r1, r3)     // Catch:{ Exception -> 0x0120, all -> 0x0135 }
            goto L_0x00e5
        L_0x0130:
            com.uc.webview.export.cyclone.UCCyclone.close(r4)     // Catch:{ Throwable -> 0x00ae }
            goto L_0x0065
        L_0x0135:
            r0 = move-exception
            com.uc.webview.export.cyclone.UCCyclone.close(r4)     // Catch:{ Throwable -> 0x00ae }
            throw r0     // Catch:{ Throwable -> 0x00ae }
        L_0x013a:
            java.lang.String r1 = "e"
            java.lang.String r2 = "cyclone"
            com.uc.webview.export.cyclone.UCLogger r1 = com.uc.webview.export.cyclone.UCLogger.create(r1, r2)
            goto L_0x00b4
        L_0x0146:
            r2 = r0
            goto L_0x006b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.cyclone.UCDex.openDexFileUC(java.lang.String, java.lang.String, int):int");
    }

    private static int openDexFileUCSys(String str, String str2, int i2) {
        try {
            int openDexFile = openDexFile(str, str2, i2);
            UCLogger create = !UCCyclone.enableDebugLog ? null : UCLogger.create("d", "cyclone");
            if (create == null) {
                return openDexFile;
            }
            create.print("UCDex.openDexFileUCSys: unopt_dex = " + str, new Throwable[0]);
            return openDexFile;
        } catch (Exception e2) {
            return 0;
        }
    }

    public static DexClassLoader createDexClassLoader(Context context, Boolean bool, String str, String str2, String str3, ClassLoader classLoader) {
        ClassLoader classLoader2;
        if (classLoader == null) {
            try {
                classLoader2 = UCDex.class.getClassLoader();
            } catch (IOException e2) {
                throw new UCKnownException(6012, (Throwable) e2);
            }
        } else {
            classLoader2 = classLoader;
        }
        return (DexClassLoader) a("DSL", context, bool, str, str2, str3, classLoader2);
    }

    public static DexFile createDexFile(Context context, Boolean bool, String str, String str2, int i2) throws IOException {
        return (DexFile) a("DF", context, bool, str, str2, (String) null, (ClassLoader) null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:136:0x027c, code lost:
        r4 = r11;
        r6 = r5;
     */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0094  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0189 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x01ee A[SYNTHETIC, Splitter:B:87:0x01ee] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.Object a(java.lang.String r20, android.content.Context r21, java.lang.Boolean r22, java.lang.String r23, java.lang.String r24, java.lang.String r25, java.lang.ClassLoader r26) throws java.io.IOException {
        /*
            com.uc.webview.export.cyclone.UCElapseTime r16 = new com.uc.webview.export.cyclone.UCElapseTime
            r16.<init>()
            r14 = 0
            r12 = 1
            r4 = 0
            r11 = 0
            boolean r5 = d     // Catch:{ Throwable -> 0x0199 }
            if (r5 == 0) goto L_0x003f
            int r5 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x0199 }
            r6 = 14
            if (r5 < r6) goto L_0x003f
            int r5 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x0199 }
            r6 = 21
            if (r5 == r6) goto L_0x003f
            if (r22 != 0) goto L_0x0193
            java.lang.String r4 = ":"
            r0 = r23
            java.lang.String[] r5 = r0.split(r4)     // Catch:{ Throwable -> 0x0199 }
            int r6 = r5.length     // Catch:{ Throwable -> 0x0199 }
            r4 = 0
        L_0x0026:
            if (r4 >= r6) goto L_0x0190
            r7 = r5[r4]     // Catch:{ Throwable -> 0x0199 }
            r0 = r24
            java.io.File r7 = com.uc.webview.export.cyclone.UCCyclone.optimizedFileFor(r7, r0)     // Catch:{ Throwable -> 0x0199 }
            boolean r7 = r7.exists()     // Catch:{ Throwable -> 0x0199 }
            if (r7 != 0) goto L_0x018c
            r4 = 1
        L_0x0037:
            java.lang.Boolean r22 = java.lang.Boolean.valueOf(r4)     // Catch:{ Throwable -> 0x0199 }
            boolean r4 = r22.booleanValue()     // Catch:{ Throwable -> 0x0199 }
        L_0x003f:
            r15 = r4
        L_0x0040:
            if (r24 == 0) goto L_0x0064
            java.io.File r4 = new java.io.File
            r0 = r24
            r4.<init>(r0)
            boolean r5 = r4.exists()
            if (r5 != 0) goto L_0x0064
            java.lang.String r5 = r4.getAbsolutePath()
            java.io.File r6 = r21.getCacheDir()
            java.lang.String r6 = r6.getAbsolutePath()
            boolean r5 = r5.startsWith(r6)
            if (r5 == 0) goto L_0x0064
            com.uc.webview.export.cyclone.UCCyclone.expectCreateDirFile(r4)
        L_0x0064:
            if (r15 != 0) goto L_0x01b3
            java.lang.String r4 = "DSL"
            r0 = r20
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x01a5
            com.uc.webview.export.cyclone.UCLoader r4 = new com.uc.webview.export.cyclone.UCLoader
            r0 = r23
            r1 = r24
            r2 = r25
            r3 = r26
            r4.<init>(r0, r1, r2, r3)
            r5 = r12
            r6 = r4
            r4 = r11
        L_0x0081:
            java.lang.String r7 = "sdk_ucdexopt"
            r8 = 0
            com.uc.webview.export.cyclone.UCCyclone.a((java.lang.String) r7, (java.util.HashMap<java.lang.String, java.lang.String>) r8)
            long r12 = r16.getMilis()
            long r16 = r16.getMilisCpu()
            android.webkit.ValueCallback<android.util.Pair<java.lang.String, java.util.HashMap<java.lang.String, java.lang.String>>> r7 = com.uc.webview.export.cyclone.UCCyclone.statCallback
            if (r7 == 0) goto L_0x0187
            java.lang.String r7 = "/"
            r0 = r23
            int r7 = r0.lastIndexOf(r7)
            r8 = -1
            if (r7 != r8) goto L_0x02c5
            int r7 = r23.length()
            r8 = 7
            if (r7 >= r8) goto L_0x02b7
        L_0x00a7:
            java.lang.String r8 = ""
            java.lang.String r9 = ""
            java.lang.String r7 = ""
            if (r4 == 0) goto L_0x00ba
            int r10 = r4.errCode()     // Catch:{ Throwable -> 0x02cf }
            java.lang.String r7 = java.lang.String.valueOf(r10)     // Catch:{ Throwable -> 0x02cf }
        L_0x00ba:
            r10 = r7
        L_0x00bb:
            if (r4 == 0) goto L_0x0323
            java.lang.Throwable r7 = r4.getRootCause()     // Catch:{ Throwable -> 0x02fb }
            java.lang.Class r7 = r7.getClass()     // Catch:{ Throwable -> 0x02fb }
            java.lang.String r7 = r7.getSimpleName()     // Catch:{ Throwable -> 0x02fb }
        L_0x00c9:
            r8 = r7
        L_0x00ca:
            if (r4 == 0) goto L_0x0320
            java.lang.Throwable r7 = r4.getRootCause()     // Catch:{ Throwable -> 0x02f8 }
            java.lang.String r7 = r7.getMessage()     // Catch:{ Throwable -> 0x02f8 }
        L_0x00d4:
            r9 = r7
        L_0x00d5:
            java.lang.String r11 = "sdk_ucdexopt"
            com.uc.webview.export.cyclone.UCHashMap r7 = new com.uc.webview.export.cyclone.UCHashMap
            r7.<init>()
            java.lang.String r14 = "cnt"
            java.lang.String r18 = "1"
            r0 = r18
            com.uc.webview.export.cyclone.UCHashMap r14 = r7.set(r14, r0)
            java.lang.String r18 = "succ"
            if (r5 == 0) goto L_0x02d3
            java.lang.String r7 = "T"
        L_0x00f1:
            r0 = r18
            com.uc.webview.export.cyclone.UCHashMap r7 = r14.set(r0, r7)
            java.lang.String r14 = "task"
            r0 = r20
            com.uc.webview.export.cyclone.UCHashMap r14 = r7.set(r14, r0)
            java.lang.String r18 = "enable"
            if (r15 == 0) goto L_0x02d8
            java.lang.String r7 = "T"
        L_0x0108:
            r0 = r18
            com.uc.webview.export.cyclone.UCHashMap r14 = r14.set(r0, r7)
            java.lang.String r15 = "hook_succ"
            boolean r7 = c
            if (r7 == 0) goto L_0x02dd
            java.lang.String r7 = "T"
        L_0x0118:
            com.uc.webview.export.cyclone.UCHashMap r14 = r14.set(r15, r7)
            java.lang.String r15 = "run_expected"
            boolean r7 = d
            if (r7 == 0) goto L_0x02e2
            java.lang.String r7 = "T"
        L_0x0126:
            com.uc.webview.export.cyclone.UCHashMap r14 = r14.set(r15, r7)
            java.lang.String r15 = "frun"
            if (r22 != 0) goto L_0x02e7
            java.lang.String r7 = "null"
        L_0x0132:
            com.uc.webview.export.cyclone.UCHashMap r7 = r14.set(r15, r7)
            java.lang.String r14 = "data"
            int r15 = i
            java.lang.String r15 = java.lang.String.valueOf(r15)
            com.uc.webview.export.cyclone.UCHashMap r7 = r7.set(r14, r15)
            java.lang.String r14 = "sdk_int"
            int r15 = android.os.Build.VERSION.SDK_INT
            java.lang.String r15 = java.lang.String.valueOf(r15)
            com.uc.webview.export.cyclone.UCHashMap r7 = r7.set(r14, r15)
            java.lang.String r14 = "code"
            r0 = r23
            com.uc.webview.export.cyclone.UCHashMap r7 = r7.set(r14, r0)
            java.lang.String r14 = "cost_cpu"
            java.lang.String r15 = java.lang.String.valueOf(r16)
            com.uc.webview.export.cyclone.UCHashMap r7 = r7.set(r14, r15)
            java.lang.String r14 = "cost"
            java.lang.String r12 = java.lang.String.valueOf(r12)
            com.uc.webview.export.cyclone.UCHashMap r7 = r7.set(r14, r12)
            java.lang.String r12 = "err"
            com.uc.webview.export.cyclone.UCHashMap r7 = r7.set(r12, r10)
            java.lang.String r10 = "cls"
            com.uc.webview.export.cyclone.UCHashMap r7 = r7.set(r10, r8)
            java.lang.String r8 = "msg"
            com.uc.webview.export.cyclone.UCHashMap r7 = r7.set(r8, r9)
            com.uc.webview.export.cyclone.UCCyclone.a((java.lang.String) r11, (java.util.HashMap<java.lang.String, java.lang.String>) r7)
        L_0x0187:
            if (r5 != 0) goto L_0x02f7
            if (r4 == 0) goto L_0x02f7
            throw r4
        L_0x018c:
            int r4 = r4 + 1
            goto L_0x0026
        L_0x0190:
            r4 = 0
            goto L_0x0037
        L_0x0193:
            boolean r4 = r22.booleanValue()     // Catch:{ Throwable -> 0x0199 }
            goto L_0x0037
        L_0x0199:
            r4 = move-exception
            com.uc.webview.export.cyclone.UCKnownException r11 = new com.uc.webview.export.cyclone.UCKnownException
            r5 = 6011(0x177b, float:8.423E-42)
            r11.<init>((int) r5, (java.lang.Throwable) r4)
            r4 = 0
            r15 = r4
            goto L_0x0040
        L_0x01a5:
            r4 = 0
            r0 = r23
            r1 = r24
            dalvik.system.DexFile r4 = dalvik.system.DexFile.loadDex(r0, r1, r4)
            r5 = r12
            r6 = r4
            r4 = r11
            goto L_0x0081
        L_0x01b3:
            java.lang.Class<com.uc.webview.export.cyclone.UCDex> r17 = com.uc.webview.export.cyclone.UCDex.class
            monitor-enter(r17)     // Catch:{ Throwable -> 0x02fe }
            if (r15 == 0) goto L_0x01d2
            java.lang.String r4 = "DSL"
            r0 = r20
            boolean r4 = r4.equals(r0)     // Catch:{ Throwable -> 0x021c }
            if (r4 == 0) goto L_0x020e
            r5 = 1
            r6 = 1
            r4 = r21
            r7 = r23
            r8 = r24
            r9 = r25
            r10 = r26
            a((android.content.Context) r4, (boolean) r5, (boolean) r6, (java.lang.String) r7, (java.lang.String) r8, (java.lang.String) r9, (java.lang.ClassLoader) r10)     // Catch:{ Throwable -> 0x021c }
        L_0x01d2:
            java.lang.String r4 = "DSL"
            r0 = r20
            boolean r4 = r4.equals(r0)     // Catch:{ Throwable -> 0x0235 }
            if (r4 == 0) goto L_0x0229
            com.uc.webview.export.cyclone.UCLoader r4 = new com.uc.webview.export.cyclone.UCLoader     // Catch:{ Throwable -> 0x0235 }
            r0 = r23
            r1 = r24
            r2 = r25
            r3 = r26
            r4.<init>(r0, r1, r2, r3)     // Catch:{ Throwable -> 0x0235 }
            r13 = r11
            r11 = r4
        L_0x01ec:
            if (r15 == 0) goto L_0x0326
            boolean r4 = d     // Catch:{ all -> 0x0307 }
            if (r4 == 0) goto L_0x0326
            java.lang.String r4 = "DSL"
            r0 = r20
            boolean r4 = r4.equals(r0)     // Catch:{ Throwable -> 0x02a9 }
            if (r4 == 0) goto L_0x029b
            r5 = 0
            r6 = 1
            r7 = 0
            r8 = 0
            r9 = 0
            r10 = 0
            r4 = r21
            a((android.content.Context) r4, (boolean) r5, (boolean) r6, (java.lang.String) r7, (java.lang.String) r8, (java.lang.String) r9, (java.lang.ClassLoader) r10)     // Catch:{ Throwable -> 0x02a9 }
            r4 = r13
        L_0x0209:
            monitor-exit(r17)     // Catch:{ all -> 0x0314 }
            r5 = r12
            r6 = r11
            goto L_0x0081
        L_0x020e:
            r5 = 1
            r6 = 0
            r9 = 0
            r10 = 0
            r4 = r21
            r7 = r23
            r8 = r24
            a((android.content.Context) r4, (boolean) r5, (boolean) r6, (java.lang.String) r7, (java.lang.String) r8, (java.lang.String) r9, (java.lang.ClassLoader) r10)     // Catch:{ Throwable -> 0x021c }
            goto L_0x01d2
        L_0x021c:
            r4 = move-exception
            r5 = 0
            d = r5     // Catch:{ all -> 0x0303 }
            com.uc.webview.export.cyclone.UCKnownException r5 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ all -> 0x0303 }
            r6 = 6009(0x1779, float:8.42E-42)
            r5.<init>((int) r6, (java.lang.Throwable) r4)     // Catch:{ all -> 0x0303 }
            r11 = r5
            goto L_0x01d2
        L_0x0229:
            r4 = 0
            r0 = r23
            r1 = r24
            dalvik.system.DexFile r14 = dalvik.system.DexFile.loadDex(r0, r1, r4)     // Catch:{ Throwable -> 0x0235 }
            r13 = r11
            r11 = r14
            goto L_0x01ec
        L_0x0235:
            r4 = move-exception
            com.uc.webview.export.cyclone.UCKnownException r13 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ all -> 0x0303 }
            r5 = 6002(0x1772, float:8.41E-42)
            r13.<init>((int) r5, (java.lang.Throwable) r4)     // Catch:{ all -> 0x0303 }
            if (r15 == 0) goto L_0x0329
            boolean r4 = d     // Catch:{ all -> 0x0276 }
            if (r4 == 0) goto L_0x0329
            r4 = 0
            d = r4     // Catch:{ all -> 0x0276 }
            java.lang.String r4 = "DSL"
            r0 = r20
            boolean r4 = r4.equals(r0)     // Catch:{ Throwable -> 0x028d }
            if (r4 == 0) goto L_0x0281
            r5 = 0
            r6 = 1
            r7 = 0
            r8 = 0
            r9 = 0
            r10 = 0
            r4 = r21
            a((android.content.Context) r4, (boolean) r5, (boolean) r6, (java.lang.String) r7, (java.lang.String) r8, (java.lang.String) r9, (java.lang.ClassLoader) r10)     // Catch:{ Throwable -> 0x028d }
        L_0x025c:
            java.lang.String r4 = "DSL"
            r0 = r20
            boolean r4 = r4.equals(r0)     // Catch:{ all -> 0x0276 }
            if (r4 == 0) goto L_0x028f
            com.uc.webview.export.cyclone.UCLoader r11 = new com.uc.webview.export.cyclone.UCLoader     // Catch:{ all -> 0x0276 }
            r0 = r23
            r1 = r24
            r2 = r25
            r3 = r26
            r11.<init>(r0, r1, r2, r3)     // Catch:{ all -> 0x0276 }
            goto L_0x01ec
        L_0x0276:
            r4 = move-exception
            r11 = r13
            r5 = r14
        L_0x0279:
            monitor-exit(r17)     // Catch:{ all -> 0x031d }
            throw r4     // Catch:{ Throwable -> 0x027b }
        L_0x027b:
            r4 = move-exception
            r4 = r11
            r6 = r5
        L_0x027e:
            r5 = 0
            goto L_0x0081
        L_0x0281:
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            r10 = 0
            r4 = r21
            a((android.content.Context) r4, (boolean) r5, (boolean) r6, (java.lang.String) r7, (java.lang.String) r8, (java.lang.String) r9, (java.lang.ClassLoader) r10)     // Catch:{ Throwable -> 0x028d }
            goto L_0x025c
        L_0x028d:
            r4 = move-exception
            goto L_0x025c
        L_0x028f:
            r4 = 0
            r0 = r23
            r1 = r24
            dalvik.system.DexFile r14 = dalvik.system.DexFile.loadDex(r0, r1, r4)     // Catch:{ all -> 0x0276 }
            r11 = r14
            goto L_0x01ec
        L_0x029b:
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            r10 = 0
            r4 = r21
            a((android.content.Context) r4, (boolean) r5, (boolean) r6, (java.lang.String) r7, (java.lang.String) r8, (java.lang.String) r9, (java.lang.ClassLoader) r10)     // Catch:{ Throwable -> 0x02a9 }
            r4 = r13
            goto L_0x0209
        L_0x02a9:
            r4 = move-exception
            com.uc.webview.export.cyclone.UCKnownException r5 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ all -> 0x0307 }
            r6 = 6010(0x177a, float:8.422E-42)
            r5.<init>((int) r6, (java.lang.Throwable) r4)     // Catch:{ all -> 0x0307 }
            r4 = 0
            d = r4     // Catch:{ all -> 0x030c }
            r4 = r5
            goto L_0x0209
        L_0x02b7:
            int r7 = r23.length()
            int r7 = r7 + -7
            r0 = r23
            java.lang.String r23 = r0.substring(r7)
            goto L_0x00a7
        L_0x02c5:
            int r7 = r7 + 1
            r0 = r23
            java.lang.String r23 = r0.substring(r7)
            goto L_0x00a7
        L_0x02cf:
            r10 = move-exception
            r10 = r7
            goto L_0x00bb
        L_0x02d3:
            java.lang.String r7 = "F"
            goto L_0x00f1
        L_0x02d8:
            java.lang.String r7 = "F"
            goto L_0x0108
        L_0x02dd:
            java.lang.String r7 = "F"
            goto L_0x0118
        L_0x02e2:
            java.lang.String r7 = "F"
            goto L_0x0126
        L_0x02e7:
            boolean r7 = r22.booleanValue()
            if (r7 == 0) goto L_0x02f2
            java.lang.String r7 = "T"
            goto L_0x0132
        L_0x02f2:
            java.lang.String r7 = "F"
            goto L_0x0132
        L_0x02f7:
            return r6
        L_0x02f8:
            r7 = move-exception
            goto L_0x00d5
        L_0x02fb:
            r7 = move-exception
            goto L_0x00ca
        L_0x02fe:
            r4 = move-exception
            r4 = r11
            r6 = r14
            goto L_0x027e
        L_0x0303:
            r4 = move-exception
            r5 = r14
            goto L_0x0279
        L_0x0307:
            r4 = move-exception
            r5 = r11
            r11 = r13
            goto L_0x0279
        L_0x030c:
            r4 = move-exception
            r19 = r5
            r5 = r11
            r11 = r19
            goto L_0x0279
        L_0x0314:
            r5 = move-exception
            r19 = r5
            r5 = r11
            r11 = r4
            r4 = r19
            goto L_0x0279
        L_0x031d:
            r4 = move-exception
            goto L_0x0279
        L_0x0320:
            r7 = r9
            goto L_0x00d4
        L_0x0323:
            r7 = r8
            goto L_0x00c9
        L_0x0326:
            r4 = r13
            goto L_0x0209
        L_0x0329:
            r11 = r14
            goto L_0x01ec
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.cyclone.UCDex.a(java.lang.String, android.content.Context, java.lang.Boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.ClassLoader):java.lang.Object");
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0048  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x013f  */
    /* JADX WARNING: Removed duplicated region for block: B:87:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void a(android.content.Context r11, boolean r12, boolean r13, java.lang.String r14, java.lang.String r15, java.lang.String r16, java.lang.ClassLoader r17) {
        /*
            boolean r0 = a
            if (r0 != 0) goto L_0x00dc
            r0 = 1
            a = r0
            r0 = 0
            r2 = 999(0x3e7, float:1.4E-42)
            r3 = 999(0x3e7, float:1.4E-42)
            r1 = 999(0x3e7, float:1.4E-42)
            com.uc.webview.export.cyclone.UCSevenZip.a(r11)     // Catch:{ Throwable -> 0x00eb }
            int r4 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x00eb }
            r5 = 19
            if (r4 < r5) goto L_0x001b
            int r2 = initArt()     // Catch:{ Throwable -> 0x00e1 }
        L_0x001b:
            if (r2 == 0) goto L_0x01b7
            java.lang.Class<dalvik.system.DexFile> r4 = dalvik.system.DexFile.class
            java.lang.String r5 = "openDexFile"
            r6 = 1
            java.lang.Class[] r6 = new java.lang.Class[r6]     // Catch:{ Throwable -> 0x0106 }
            r7 = 0
            java.lang.Class<byte[]> r8 = byte[].class
            r6[r7] = r8     // Catch:{ Throwable -> 0x0106 }
            java.lang.reflect.Method r4 = r4.getDeclaredMethod(r5, r6)     // Catch:{ Throwable -> 0x0106 }
            f = r4     // Catch:{ Throwable -> 0x0106 }
            r4 = 0
            int r3 = initDvm(r4)     // Catch:{ Throwable -> 0x00f8 }
            r10 = r1
            r1 = r3
            r3 = r0
            r0 = r10
        L_0x0039:
            if (r2 == 0) goto L_0x003f
            if (r1 == 0) goto L_0x003f
            if (r0 != 0) goto L_0x0133
        L_0x003f:
            r4 = 1
        L_0x0040:
            c = r4     // Catch:{ Throwable -> 0x01ad }
            e = r11     // Catch:{ Throwable -> 0x01ad }
        L_0x0044:
            android.webkit.ValueCallback<android.util.Pair<java.lang.String, java.util.HashMap<java.lang.String, java.lang.String>>> r4 = com.uc.webview.export.cyclone.UCCyclone.statCallback
            if (r4 == 0) goto L_0x00dc
            java.lang.String r5 = java.lang.String.valueOf(r2)
            java.lang.String r6 = java.lang.String.valueOf(r1)
            java.lang.String r7 = java.lang.String.valueOf(r0)
            java.lang.String r1 = ""
            java.lang.String r2 = ""
            java.lang.String r0 = ""
            if (r3 == 0) goto L_0x0067
            int r4 = r3.errCode()     // Catch:{ Throwable -> 0x0136 }
            java.lang.String r0 = java.lang.String.valueOf(r4)     // Catch:{ Throwable -> 0x0136 }
        L_0x0067:
            r4 = r0
        L_0x0068:
            if (r3 == 0) goto L_0x01b4
            java.lang.Throwable r0 = r3.getRootCause()     // Catch:{ Throwable -> 0x01aa }
            java.lang.Class r0 = r0.getClass()     // Catch:{ Throwable -> 0x01aa }
            java.lang.String r0 = r0.getSimpleName()     // Catch:{ Throwable -> 0x01aa }
        L_0x0076:
            r1 = r0
        L_0x0077:
            if (r3 == 0) goto L_0x01b1
            java.lang.Throwable r0 = r3.getRootCause()     // Catch:{ Throwable -> 0x01a7 }
            java.lang.String r0 = r0.getMessage()     // Catch:{ Throwable -> 0x01a7 }
        L_0x0081:
            r2 = r0
        L_0x0082:
            java.lang.String r3 = "sdk_hookdex"
            com.uc.webview.export.cyclone.UCHashMap r0 = new com.uc.webview.export.cyclone.UCHashMap
            r0.<init>()
            java.lang.String r8 = "cnt"
            java.lang.String r9 = "1"
            com.uc.webview.export.cyclone.UCHashMap r8 = r0.set(r8, r9)
            java.lang.String r9 = "hook_succ"
            boolean r0 = c
            if (r0 == 0) goto L_0x013a
            java.lang.String r0 = "T"
        L_0x009e:
            com.uc.webview.export.cyclone.UCHashMap r0 = r8.set(r9, r0)
            java.lang.String r8 = "art"
            com.uc.webview.export.cyclone.UCHashMap r0 = r0.set(r8, r5)
            java.lang.String r5 = "dvm"
            com.uc.webview.export.cyclone.UCHashMap r0 = r0.set(r5, r6)
            java.lang.String r5 = "dvm2"
            com.uc.webview.export.cyclone.UCHashMap r0 = r0.set(r5, r7)
            java.lang.String r5 = "sdk_int"
            int r6 = android.os.Build.VERSION.SDK_INT
            java.lang.String r6 = java.lang.String.valueOf(r6)
            com.uc.webview.export.cyclone.UCHashMap r0 = r0.set(r5, r6)
            java.lang.String r5 = "err"
            com.uc.webview.export.cyclone.UCHashMap r0 = r0.set(r5, r4)
            java.lang.String r4 = "cls"
            com.uc.webview.export.cyclone.UCHashMap r0 = r0.set(r4, r1)
            java.lang.String r1 = "msg"
            com.uc.webview.export.cyclone.UCHashMap r0 = r0.set(r1, r2)
            com.uc.webview.export.cyclone.UCCyclone.a((java.lang.String) r3, (java.util.HashMap<java.lang.String, java.lang.String>) r0)
        L_0x00dc:
            boolean r0 = c
            if (r0 != 0) goto L_0x013f
        L_0x00e0:
            return
        L_0x00e1:
            r4 = move-exception
            com.uc.webview.export.cyclone.UCKnownException r0 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ Throwable -> 0x00eb }
            r5 = 6006(0x1776, float:8.416E-42)
            r0.<init>((int) r5, (java.lang.Throwable) r4)     // Catch:{ Throwable -> 0x00eb }
            goto L_0x001b
        L_0x00eb:
            r0 = move-exception
            r4 = r0
            r0 = r1
            r1 = r3
        L_0x00ef:
            com.uc.webview.export.cyclone.UCKnownException r3 = new com.uc.webview.export.cyclone.UCKnownException
            r5 = 6003(0x1773, float:8.412E-42)
            r3.<init>((int) r5, (java.lang.Throwable) r4)
            goto L_0x0044
        L_0x00f8:
            r4 = move-exception
            com.uc.webview.export.cyclone.UCKnownException r0 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ Throwable -> 0x0106 }
            r5 = 6007(0x1777, float:8.418E-42)
            r0.<init>((int) r5, (java.lang.Throwable) r4)     // Catch:{ Throwable -> 0x0106 }
            r10 = r1
            r1 = r3
            r3 = r0
            r0 = r10
            goto L_0x0039
        L_0x0106:
            r0 = move-exception
            com.uc.webview.export.cyclone.UCKnownException r4 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ Throwable -> 0x0126 }
            r5 = 6004(0x1774, float:8.413E-42)
            r4.<init>((int) r5, (java.lang.Throwable) r0)     // Catch:{ Throwable -> 0x0126 }
            int r0 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x0119 }
            int r1 = initDvm(r0)     // Catch:{ Throwable -> 0x0119 }
            r0 = r1
            r1 = r3
            r3 = r4
            goto L_0x0039
        L_0x0119:
            r0 = move-exception
            com.uc.webview.export.cyclone.UCKnownException r4 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ Throwable -> 0x0126 }
            r5 = 6007(0x1777, float:8.418E-42)
            r4.<init>((int) r5, (java.lang.Throwable) r0)     // Catch:{ Throwable -> 0x0126 }
            r0 = r1
            r1 = r3
            r3 = r4
            goto L_0x0039
        L_0x0126:
            r0 = move-exception
            com.uc.webview.export.cyclone.UCKnownException r4 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ Throwable -> 0x00eb }
            r5 = 6005(0x1775, float:8.415E-42)
            r4.<init>((int) r5, (java.lang.Throwable) r0)     // Catch:{ Throwable -> 0x00eb }
            r0 = r1
            r1 = r3
            r3 = r4
            goto L_0x0039
        L_0x0133:
            r4 = 0
            goto L_0x0040
        L_0x0136:
            r4 = move-exception
            r4 = r0
            goto L_0x0068
        L_0x013a:
            java.lang.String r0 = "F"
            goto L_0x009e
        L_0x013f:
            int r0 = syncData(r12)
            i = r0
            if (r0 == 0) goto L_0x0161
            com.uc.webview.export.cyclone.UCKnownException r0 = new com.uc.webview.export.cyclone.UCKnownException
            r1 = 6008(0x1778, float:8.419E-42)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "syncData:"
            r2.<init>(r3)
            int r3 = i
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r0.<init>((int) r1, (java.lang.String) r2)
            throw r0
        L_0x0161:
            if (r12 == 0) goto L_0x00e0
            if (r13 == 0) goto L_0x0185
            java.util.concurrent.ConcurrentLinkedQueue<android.util.Pair<java.lang.Integer, java.lang.Object>> r0 = b
            android.util.Pair r1 = new android.util.Pair
            r2 = 1
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r3 = 4
            java.lang.Object[] r3 = new java.lang.Object[r3]
            r4 = 0
            r3[r4] = r14
            r4 = 1
            r3[r4] = r15
            r4 = 2
            r3[r4] = r16
            r4 = 3
            r3[r4] = r17
            r1.<init>(r2, r3)
            r0.add(r1)
            goto L_0x00e0
        L_0x0185:
            java.util.concurrent.ConcurrentLinkedQueue<android.util.Pair<java.lang.Integer, java.lang.Object>> r0 = b
            android.util.Pair r1 = new android.util.Pair
            r2 = 2
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r3 = 3
            java.lang.Object[] r3 = new java.lang.Object[r3]
            r4 = 0
            r3[r4] = r14
            r4 = 1
            r3[r4] = r15
            r4 = 2
            r5 = 0
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r3[r4] = r5
            r1.<init>(r2, r3)
            r0.add(r1)
            goto L_0x00e0
        L_0x01a7:
            r0 = move-exception
            goto L_0x0082
        L_0x01aa:
            r0 = move-exception
            goto L_0x0077
        L_0x01ad:
            r3 = move-exception
            r4 = r3
            goto L_0x00ef
        L_0x01b1:
            r0 = r2
            goto L_0x0081
        L_0x01b4:
            r0 = r1
            goto L_0x0076
        L_0x01b7:
            r10 = r1
            r1 = r3
            r3 = r0
            r0 = r10
            goto L_0x0039
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.cyclone.UCDex.a(android.content.Context, boolean, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.ClassLoader):void");
    }

    public synchronized void run() {
        if (!b.isEmpty()) {
            synchronized (UCDex.class) {
                syncData(false);
            }
            for (Pair poll = b.poll(); poll != null; poll = b.poll()) {
                Object[] objArr = (Object[]) poll.second;
                if (((Integer) poll.first).intValue() == 1) {
                    new UCLoader((String) objArr[0], (String) objArr[1], (String) objArr[2], (ClassLoader) objArr[3]);
                } else {
                    try {
                        DexFile.loadDex((String) objArr[0], (String) objArr[1], ((Integer) objArr[2]).intValue());
                    } catch (Throwable th) {
                    }
                }
            }
        }
    }
}
