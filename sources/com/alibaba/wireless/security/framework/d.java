package com.alibaba.wireless.security.framework;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.alibaba.wireless.security.SecExceptionCode;
import com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge;
import com.alibaba.wireless.security.framework.utils.c;
import com.alibaba.wireless.security.framework.utils.f;
import com.alibaba.wireless.security.open.SecException;
import com.taobao.atlas.dexmerge.MergeConstants;
import com.taobao.atlas.dexmerge.dx.io.Opcodes;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import mtopsdk.common.util.SymbolExpUtil;

public class d implements ISGPluginManager {
    private static String[] n = {"armeabi", "armeabi-v7a", "x86", "arm64-v8a", "x86_64"};
    private static String o = null;
    private static volatile boolean p = true;
    HashMap<Class, Object> a = new HashMap<>();
    c b = null;
    /* access modifiers changed from: private */
    public Context c;
    private final HashMap<String, c> d = new HashMap<>();
    private IRouterComponent e = null;
    private boolean f = true;
    private String g = null;
    private String h = null;
    private b i = null;
    private boolean j = false;
    private File k = null;
    private File l = null;
    private File m = null;

    private static class a {
        File a;
        File b;
        File c;
        boolean d;

        public a(File file, File file2, File file3, boolean z) {
            this.a = file;
            this.b = file2;
            this.c = file3;
            this.d = z;
        }
    }

    private int a(String str, String str2) {
        String[] split = str.split("\\.");
        String[] split2 = str2.split("\\.");
        int length = split.length < split2.length ? split.length : split2.length;
        for (int i2 = 0; i2 < length; i2++) {
            int parseInt = Integer.parseInt(split[i2]);
            int parseInt2 = Integer.parseInt(split2[i2]);
            if (parseInt > parseInt2) {
                return 1;
            }
            if (parseInt < parseInt2) {
                return -1;
            }
        }
        return 0;
    }

    private PackageInfo a(String str, a aVar, String str2) throws SecException {
        PackageInfo packageInfo;
        try {
            packageInfo = this.c.getPackageManager().getPackageArchiveInfo(aVar.a.getAbsolutePath(), Opcodes.LONG_TO_FLOAT);
        } catch (Throwable th) {
            a(100043, 199, "getPackageArchiveInfo failed", "" + th, c(aVar.a.getAbsolutePath() + ""), aVar.c != null ? c(aVar.c.getAbsolutePath()) : "", str2);
            if (aVar.a.exists() && !b(aVar.a)) {
                aVar.a.delete();
            }
            packageInfo = null;
        }
        if (packageInfo != null) {
            return packageInfo;
        }
        a(100043, 199, "packageInfo == null", str + "[" + str2 + "]", c(aVar.a.getAbsolutePath()), aVar.c != null ? c(aVar.c.getAbsolutePath()) : "", c());
        throw new SecException(199);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:140:0x0596, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:141:0x0597, code lost:
        r12 = r4;
        r13 = new java.io.File(r11 + java.io.File.separator + r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x05ba, code lost:
        if (r12.getErrorCode() == 103) goto L_0x05bc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:143:0x05bc, code lost:
        r8 = r12.toString();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:144:0x05cc, code lost:
        if (r31.length() == 0) goto L_0x05ce;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:145:0x05ce, code lost:
        r9 = r24;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:147:0x05d4, code lost:
        if (r29.c != null) goto L_0x05d6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:148:0x05d6, code lost:
        r10 = "src:" + c(r29.c.getAbsolutePath());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:149:0x05f8, code lost:
        a(100043, 103, "", r8, r9, r10, c(r13.getAbsolutePath()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:153:0x060b, code lost:
        if (r29.d == false) goto L_0x060d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:154:0x060d, code lost:
        r27.b.a();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:156:0x0618, code lost:
        if (r13.exists() != false) goto L_0x061a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:157:0x061a, code lost:
        r13.delete();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:161:0x0621, code lost:
        if (r29.d == false) goto L_0x0623;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:162:0x0623, code lost:
        r27.b.b();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:163:0x062a, code lost:
        throw r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:164:0x062b, code lost:
        r9 = r31 + "->" + r24;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:165:0x0648, code lost:
        r10 = "zipfile:" + c(r29.a.getAbsolutePath());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:171:0x067a, code lost:
        a(100043, 199, "native exception occurred", r12.toString(), "soName=" + r9 + ", authCode=" + r27.h + ", errorCode=" + r12.getErrorCode(), c(r29.a.getAbsolutePath()), c(r13.getAbsolutePath()));
     */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x0596 A[Catch:{ InstantiationException -> 0x06da, IllegalAccessException -> 0x058c, SecException -> 0x0596, all -> 0x066b, SecException -> 0x0290, all -> 0x015a }, ExcHandler: SecException (r4v38 'e' com.alibaba.wireless.security.open.SecException A[CUSTOM_DECLARE, Catch:{  }]), Splitter:B:89:0x049b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.alibaba.wireless.security.framework.c a(java.lang.String r28, com.alibaba.wireless.security.framework.d.a r29, android.content.Context r30, java.lang.String r31) throws com.alibaba.wireless.security.open.SecException {
        /*
            r27 = this;
            r0 = r29
            java.io.File r4 = r0.a
            java.lang.String r10 = r4.getAbsolutePath()
            r0 = r29
            java.io.File r4 = r0.b
            java.lang.String r11 = r4.getAbsolutePath()
            r19 = 0
            r7 = 0
            r6 = 0
            r4 = 0
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r4)
            r4 = 0
            r8 = 0
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r8)
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.StringBuilder r8 = r8.append(r11)
            java.lang.String r12 = java.io.File.separator
            java.lang.StringBuilder r8 = r8.append(r12)
            r0 = r28
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.String r12 = "pkgInfo.config"
            java.lang.StringBuilder r8 = r8.append(r12)
            java.lang.String r8 = r8.toString()
            com.alibaba.wireless.security.framework.a r17 = new com.alibaba.wireless.security.framework.a
            r0 = r17
            r0.<init>(r8)
            r8 = 0
            boolean r12 = r17.a()     // Catch:{ all -> 0x015a }
            if (r12 == 0) goto L_0x06e2
            org.json.JSONObject r12 = r17.b()     // Catch:{ JSONException -> 0x0160, Exception -> 0x016b }
            java.lang.String r13 = "version"
            java.lang.String r7 = r12.getString(r13)     // Catch:{ JSONException -> 0x0160, Exception -> 0x016b }
            java.lang.String r13 = "dependencies"
            java.lang.String r6 = r12.getString(r13)     // Catch:{ JSONException -> 0x0160, Exception -> 0x016b }
            java.lang.String r13 = "hasso"
            java.lang.String r13 = r12.getString(r13)     // Catch:{ JSONException -> 0x0160, Exception -> 0x016b }
            boolean r13 = java.lang.Boolean.parseBoolean(r13)     // Catch:{ JSONException -> 0x0160, Exception -> 0x016b }
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r13)     // Catch:{ JSONException -> 0x0160, Exception -> 0x016b }
            java.lang.String r13 = "pluginclass"
            java.lang.String r4 = r12.getString(r13)     // Catch:{ JSONException -> 0x0160, Exception -> 0x016b }
            java.lang.String r13 = "thirdpartyso"
            boolean r12 = r12.getBoolean(r13)     // Catch:{ JSONException -> 0x0160, Exception -> 0x016b }
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r12)     // Catch:{ JSONException -> 0x0160, Exception -> 0x016b }
            r8 = 1
            r26 = r8
            r8 = r6
            r6 = r5
            r5 = r4
            r4 = r9
            r9 = r26
        L_0x0089:
            if (r9 != 0) goto L_0x00da
            r0 = r27
            r1 = r28
            r2 = r29
            r3 = r31
            android.content.pm.PackageInfo r9 = r0.a((java.lang.String) r1, (com.alibaba.wireless.security.framework.d.a) r2, (java.lang.String) r3)     // Catch:{ all -> 0x015a }
            java.lang.String r7 = r9.versionName     // Catch:{ all -> 0x015a }
            android.content.pm.ApplicationInfo r4 = r9.applicationInfo     // Catch:{ all -> 0x015a }
            android.os.Bundle r4 = r4.metaData     // Catch:{ all -> 0x015a }
            java.lang.String r5 = "dependencies"
            java.lang.String r8 = r4.getString(r5)     // Catch:{ all -> 0x015a }
            android.content.pm.ApplicationInfo r4 = r9.applicationInfo     // Catch:{ all -> 0x015a }
            android.os.Bundle r4 = r4.metaData     // Catch:{ all -> 0x015a }
            java.lang.String r5 = "hasso"
            r6 = 0
            boolean r4 = r4.getBoolean(r5, r6)     // Catch:{ all -> 0x015a }
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r4)     // Catch:{ all -> 0x015a }
            android.content.pm.ApplicationInfo r4 = r9.applicationInfo     // Catch:{ all -> 0x015a }
            android.os.Bundle r4 = r4.metaData     // Catch:{ all -> 0x015a }
            java.lang.String r5 = "pluginclass"
            java.lang.String r5 = r4.getString(r5)     // Catch:{ all -> 0x015a }
            android.content.pm.ApplicationInfo r4 = r9.applicationInfo     // Catch:{ all -> 0x015a }
            android.os.Bundle r4 = r4.metaData     // Catch:{ all -> 0x015a }
            java.lang.String r12 = "thirdpartyso"
            r13 = 0
            boolean r4 = r4.getBoolean(r12, r13)     // Catch:{ all -> 0x015a }
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r4)     // Catch:{ all -> 0x015a }
            r0 = r17
            r0.a = r9     // Catch:{ all -> 0x015a }
            r0 = r17
            r1 = r28
            r0.a(r9, r1)     // Catch:{ all -> 0x015a }
        L_0x00da:
            r25 = r7
            r7 = r6
            r6 = r4
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            r0 = r28
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = "("
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            r0 = r25
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = ")"
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            java.lang.String r24 = r4.toString()     // Catch:{ all -> 0x015a }
            int r4 = r31.length()     // Catch:{ all -> 0x015a }
            if (r4 != 0) goto L_0x0176
            r4 = r24
        L_0x0109:
            r9 = 0
            r0 = r27
            boolean r4 = r0.a((java.lang.String) r8, (java.lang.String) r4, (boolean) r9)     // Catch:{ all -> 0x015a }
            if (r4 != 0) goto L_0x01d6
            r5 = 100043(0x186cb, float:1.4019E-40)
            r6 = 199(0xc7, float:2.79E-43)
            java.lang.String r7 = "loadRequirements failed"
            int r4 = r31.length()     // Catch:{ all -> 0x015a }
            if (r4 != 0) goto L_0x0194
            r9 = r24
        L_0x0122:
            r0 = r29
            java.io.File r4 = r0.c     // Catch:{ all -> 0x015a }
            if (r4 == 0) goto L_0x01b2
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            java.lang.String r10 = "src:"
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            r0 = r29
            java.io.File r10 = r0.c     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r10.getAbsolutePath()     // Catch:{ all -> 0x015a }
            r0 = r27
            java.lang.String r10 = r0.c((java.lang.String) r10)     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r4.toString()     // Catch:{ all -> 0x015a }
        L_0x014a:
            java.lang.String r11 = ""
            r4 = r27
            r4.a(r5, r6, r7, r8, r9, r10, r11)     // Catch:{ all -> 0x015a }
            com.alibaba.wireless.security.open.SecException r4 = new com.alibaba.wireless.security.open.SecException     // Catch:{ all -> 0x015a }
            r5 = 199(0xc7, float:2.79E-43)
            r4.<init>(r5)     // Catch:{ all -> 0x015a }
            throw r4     // Catch:{ all -> 0x015a }
        L_0x015a:
            r4 = move-exception
            r6 = 0
            com.alibaba.wireless.security.framework.SGPluginExtras.slot = r6
            throw r4
        L_0x0160:
            r12 = move-exception
            r26 = r8
            r8 = r6
            r6 = r5
            r5 = r4
            r4 = r9
            r9 = r26
            goto L_0x0089
        L_0x016b:
            r12 = move-exception
            r26 = r8
            r8 = r6
            r6 = r5
            r5 = r4
            r4 = r9
            r9 = r26
            goto L_0x0089
        L_0x0176:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            r0 = r31
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = "->"
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            r0 = r24
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x015a }
            goto L_0x0109
        L_0x0194:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            r0 = r31
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = "->"
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            r0 = r24
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = r4.toString()     // Catch:{ all -> 0x015a }
            goto L_0x0122
        L_0x01b2:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            java.lang.String r10 = "zipfile:"
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            r0 = r29
            java.io.File r10 = r0.a     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r10.getAbsolutePath()     // Catch:{ all -> 0x015a }
            r0 = r27
            java.lang.String r10 = r0.c((java.lang.String) r10)     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r4.toString()     // Catch:{ all -> 0x015a }
            goto L_0x014a
        L_0x01d6:
            r0 = r27
            r1 = r28
            r2 = r25
            r0.b((java.lang.String) r1, (java.lang.String) r2)     // Catch:{ SecException -> 0x0290 }
            java.lang.String r13 = ""
            java.lang.String r22 = ""
            boolean r4 = r7.booleanValue()     // Catch:{ all -> 0x015a }
            if (r4 == 0) goto L_0x0354
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            java.lang.String r9 = "libsg"
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            r0 = r28
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = "so-"
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            r0 = r25
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = ".so"
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            java.lang.String r13 = r4.toString()     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            java.lang.String r9 = "sg"
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            r0 = r28
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = "so-"
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            r0 = r25
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r22 = r4.toString()     // Catch:{ all -> 0x015a }
            r0 = r29
            java.io.File r12 = r0.c     // Catch:{ all -> 0x015a }
            r0 = r29
            boolean r14 = r0.d     // Catch:{ all -> 0x015a }
            r9 = r27
            boolean r4 = r9.a((java.lang.String) r10, (java.lang.String) r11, (java.io.File) r12, (java.lang.String) r13, (boolean) r14)     // Catch:{ all -> 0x015a }
            if (r4 != 0) goto L_0x0354
            r5 = 100043(0x186cb, float:1.4019E-40)
            r6 = 107(0x6b, float:1.5E-43)
            java.lang.String r7 = ""
            int r4 = r31.length()     // Catch:{ all -> 0x015a }
            if (r4 != 0) goto L_0x0312
            r9 = r24
        L_0x0258:
            r0 = r29
            java.io.File r4 = r0.c     // Catch:{ all -> 0x015a }
            if (r4 == 0) goto L_0x0330
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            java.lang.String r10 = "src:"
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            r0 = r29
            java.io.File r10 = r0.c     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r10.getAbsolutePath()     // Catch:{ all -> 0x015a }
            r0 = r27
            java.lang.String r10 = r0.c((java.lang.String) r10)     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r4.toString()     // Catch:{ all -> 0x015a }
        L_0x0280:
            java.lang.String r11 = ""
            r4 = r27
            r4.a(r5, r6, r7, r8, r9, r10, r11)     // Catch:{ all -> 0x015a }
            com.alibaba.wireless.security.open.SecException r4 = new com.alibaba.wireless.security.open.SecException     // Catch:{ all -> 0x015a }
            r5 = 107(0x6b, float:1.5E-43)
            r4.<init>(r5)     // Catch:{ all -> 0x015a }
            throw r4     // Catch:{ all -> 0x015a }
        L_0x0290:
            r12 = move-exception
            r5 = 100043(0x186cb, float:1.4019E-40)
            r6 = 199(0xc7, float:2.79E-43)
            java.lang.String r7 = "isMeetReverseDependencies failed"
            int r4 = r31.length()     // Catch:{ all -> 0x015a }
            if (r4 != 0) goto L_0x02d2
            r9 = r24
        L_0x02a1:
            r0 = r29
            java.io.File r4 = r0.c     // Catch:{ all -> 0x015a }
            if (r4 == 0) goto L_0x02ef
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            java.lang.String r10 = "src:"
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            r0 = r29
            java.io.File r10 = r0.c     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r10.getAbsolutePath()     // Catch:{ all -> 0x015a }
            r0 = r27
            java.lang.String r10 = r0.c((java.lang.String) r10)     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r4.toString()     // Catch:{ all -> 0x015a }
        L_0x02c9:
            java.lang.String r11 = ""
            r4 = r27
            r4.a(r5, r6, r7, r8, r9, r10, r11)     // Catch:{ all -> 0x015a }
            throw r12     // Catch:{ all -> 0x015a }
        L_0x02d2:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            r0 = r31
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = "->"
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            r0 = r24
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = r4.toString()     // Catch:{ all -> 0x015a }
            goto L_0x02a1
        L_0x02ef:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            java.lang.String r10 = "zipfile:"
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            r0 = r29
            java.io.File r10 = r0.a     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r10.getAbsolutePath()     // Catch:{ all -> 0x015a }
            r0 = r27
            java.lang.String r10 = r0.c((java.lang.String) r10)     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r4.toString()     // Catch:{ all -> 0x015a }
            goto L_0x02c9
        L_0x0312:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            r0 = r31
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = "->"
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            r0 = r24
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = r4.toString()     // Catch:{ all -> 0x015a }
            goto L_0x0258
        L_0x0330:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            java.lang.String r10 = "zipfile:"
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            r0 = r29
            java.io.File r10 = r0.a     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r10.getAbsolutePath()     // Catch:{ all -> 0x015a }
            r0 = r27
            java.lang.String r10 = r0.c((java.lang.String) r10)     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r4.toString()     // Catch:{ all -> 0x015a }
            goto L_0x0280
        L_0x0354:
            r9 = r13
            if (r5 != 0) goto L_0x03df
            r5 = 100043(0x186cb, float:1.4019E-40)
            r6 = 199(0xc7, float:2.79E-43)
            java.lang.String r7 = "miss pluginclass"
            int r4 = r31.length()     // Catch:{ all -> 0x015a }
            if (r4 != 0) goto L_0x039f
            r9 = r24
        L_0x0367:
            r0 = r29
            java.io.File r4 = r0.c     // Catch:{ all -> 0x015a }
            if (r4 == 0) goto L_0x03bc
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            java.lang.String r10 = "src:"
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            r0 = r29
            java.io.File r10 = r0.c     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r10.getAbsolutePath()     // Catch:{ all -> 0x015a }
            r0 = r27
            java.lang.String r10 = r0.c((java.lang.String) r10)     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r4.toString()     // Catch:{ all -> 0x015a }
        L_0x038f:
            java.lang.String r11 = ""
            r4 = r27
            r4.a(r5, r6, r7, r8, r9, r10, r11)     // Catch:{ all -> 0x015a }
            com.alibaba.wireless.security.open.SecException r4 = new com.alibaba.wireless.security.open.SecException     // Catch:{ all -> 0x015a }
            r5 = 199(0xc7, float:2.79E-43)
            r4.<init>(r5)     // Catch:{ all -> 0x015a }
            throw r4     // Catch:{ all -> 0x015a }
        L_0x039f:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            r0 = r31
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = "->"
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            r0 = r24
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = r4.toString()     // Catch:{ all -> 0x015a }
            goto L_0x0367
        L_0x03bc:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            java.lang.String r10 = "zipfile:"
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            r0 = r29
            java.io.File r10 = r0.a     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r10.getAbsolutePath()     // Catch:{ all -> 0x015a }
            r0 = r27
            java.lang.String r10 = r0.c((java.lang.String) r10)     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r4.toString()     // Catch:{ all -> 0x015a }
            goto L_0x038f
        L_0x03df:
            java.lang.String r12 = r5.trim()     // Catch:{ all -> 0x015a }
            r0 = r29
            boolean r4 = r0.d     // Catch:{ all -> 0x015a }
            r0 = r27
            dalvik.system.DexClassLoader r16 = r0.b(r10, r11, r4)     // Catch:{ all -> 0x015a }
            r0 = r27
            r1 = r16
            java.lang.Class r4 = r0.a((java.lang.ClassLoader) r1, (java.lang.String) r12)     // Catch:{ all -> 0x015a }
            if (r4 != 0) goto L_0x049b
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            java.lang.String r5 = "load "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r12)     // Catch:{ all -> 0x015a }
            java.lang.String r5 = " failed from plugin "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x015a }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x015a }
            com.alibaba.wireless.security.framework.utils.a.b(r4)     // Catch:{ all -> 0x015a }
            r5 = 100043(0x186cb, float:1.4019E-40)
            r6 = 199(0xc7, float:2.79E-43)
            java.lang.String r7 = "clazz == null"
            int r4 = r31.length()     // Catch:{ all -> 0x015a }
            if (r4 != 0) goto L_0x045b
            r9 = r24
        L_0x0425:
            r0 = r29
            java.io.File r4 = r0.c     // Catch:{ all -> 0x015a }
            if (r4 == 0) goto L_0x0478
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            java.lang.String r10 = "src:"
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            r0 = r29
            java.io.File r10 = r0.c     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r10.getAbsolutePath()     // Catch:{ all -> 0x015a }
            r0 = r27
            java.lang.String r10 = r0.c((java.lang.String) r10)     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r4.toString()     // Catch:{ all -> 0x015a }
        L_0x044d:
            r4 = r27
            r11 = r12
            r4.a(r5, r6, r7, r8, r9, r10, r11)     // Catch:{ all -> 0x015a }
            com.alibaba.wireless.security.open.SecException r4 = new com.alibaba.wireless.security.open.SecException     // Catch:{ all -> 0x015a }
            r5 = 199(0xc7, float:2.79E-43)
            r4.<init>(r5)     // Catch:{ all -> 0x015a }
            throw r4     // Catch:{ all -> 0x015a }
        L_0x045b:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            r0 = r31
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = "->"
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            r0 = r24
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = r4.toString()     // Catch:{ all -> 0x015a }
            goto L_0x0425
        L_0x0478:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            java.lang.String r10 = "zipfile:"
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            r0 = r29
            java.io.File r10 = r0.a     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r10.getAbsolutePath()     // Catch:{ all -> 0x015a }
            r0 = r27
            java.lang.String r10 = r0.c((java.lang.String) r10)     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r4.toString()     // Catch:{ all -> 0x015a }
            goto L_0x044d
        L_0x049b:
            java.lang.Object r18 = r4.newInstance()     // Catch:{ InstantiationException -> 0x06da, IllegalAccessException -> 0x058c, SecException -> 0x0596 }
            com.alibaba.wireless.security.open.initialize.ISecurityGuardPlugin r18 = (com.alibaba.wireless.security.open.initialize.ISecurityGuardPlugin) r18     // Catch:{ InstantiationException -> 0x06da, IllegalAccessException -> 0x058c, SecException -> 0x0596 }
            com.alibaba.wireless.security.framework.c r12 = new com.alibaba.wireless.security.framework.c     // Catch:{ InstantiationException -> 0x06da, IllegalAccessException -> 0x058c, SecException -> 0x0596 }
            r13 = r10
            r14 = r27
            r15 = r28
            r12.<init>(r13, r14, r15, r16, r17, r18)     // Catch:{ InstantiationException -> 0x06da, IllegalAccessException -> 0x058c, SecException -> 0x0596 }
            java.lang.String r4 = r27.getMainPluginName()     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r0 = r28
            boolean r4 = r0.equalsIgnoreCase(r4)     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            if (r4 == 0) goto L_0x056d
            r4 = 0
            r0 = r27
            boolean r5 = r0.f     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            if (r5 == 0) goto L_0x04bf
            r4 = 1
        L_0x04bf:
            r0 = r27
            boolean r5 = r0.j     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            if (r5 == 0) goto L_0x04c7
            r4 = r4 | 2
        L_0x04c7:
            r0 = r27
            java.lang.String r5 = r0.g     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            if (r5 == 0) goto L_0x04d9
            r0 = r27
            java.lang.String r5 = r0.g     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            int r5 = r5.length()     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            if (r5 <= 0) goto L_0x04d9
            r4 = r4 | 4
        L_0x04d9:
            r0 = r27
            android.content.Context r5 = r0.c     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            boolean r5 = com.alibaba.wireless.security.framework.utils.f.a(r5)     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            if (r5 == 0) goto L_0x04e5
            r4 = r4 | 8
        L_0x04e5:
            r0 = r27
            android.content.Context r5 = r0.c     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            boolean r5 = com.alibaba.wireless.security.framework.utils.f.b(r5)     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            if (r5 == 0) goto L_0x06df
            r4 = r4 | 16
            r5 = r4
        L_0x04f2:
            java.lang.String r4 = ""
            r0 = r27
            com.alibaba.wireless.security.framework.b r4 = r0.i     // Catch:{ Exception -> 0x0568 }
            if (r4 == 0) goto L_0x0564
            r0 = r27
            com.alibaba.wireless.security.framework.b r4 = r0.i     // Catch:{ Exception -> 0x0568 }
            org.json.JSONObject r4 = r4.a()     // Catch:{ Exception -> 0x0568 }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x0568 }
        L_0x0507:
            r20 = 0
            r8 = 4
            java.lang.Object[] r0 = new java.lang.Object[r8]     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r23 = r0
            r8 = 0
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r23[r8] = r5     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r5 = 1
            r23[r5] = r4     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r4 = 2
            r0 = r27
            java.io.File r5 = r0.l     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            java.lang.String r5 = r5.getAbsolutePath()     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r23[r4] = r5     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r4 = 3
            r0 = r27
            java.lang.String r5 = r0.h     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r23[r4] = r5     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r19 = r30
            r21 = r12
            com.alibaba.wireless.security.framework.IRouterComponent r4 = r18.onPluginLoaded(r19, r20, r21, r22, r23)     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r0 = r27
            r0.e = r4     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
        L_0x0536:
            boolean r4 = r7.booleanValue()     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            if (r4 == 0) goto L_0x055f
            boolean r4 = r6.booleanValue()     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            if (r4 != 0) goto L_0x055f
            r0 = r16
            r1 = r22
            java.lang.String r4 = com.alibaba.wireless.security.framework.utils.f.a(r0, r1)     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r0 = r27
            com.alibaba.wireless.security.framework.IRouterComponent r5 = r0.e     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r6 = 10102(0x2776, float:1.4156E-41)
            r7 = 3
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r8 = 0
            r7[r8] = r28     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r8 = 1
            r7[r8] = r25     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r8 = 2
            r7[r8] = r4     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r5.doCommand(r6, r7)     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
        L_0x055f:
            r4 = 0
            com.alibaba.wireless.security.framework.SGPluginExtras.slot = r4
            return r12
        L_0x0564:
            java.lang.String r4 = ""
            goto L_0x0507
        L_0x0568:
            r4 = move-exception
            java.lang.String r4 = ""
            goto L_0x0507
        L_0x056d:
            r4 = 0
            com.alibaba.wireless.security.framework.SGPluginExtras.slot = r4     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r0 = r27
            com.alibaba.wireless.security.framework.IRouterComponent r0 = r0.e     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r20 = r0
            r4 = 0
            java.lang.Object[] r0 = new java.lang.Object[r4]     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            r23 = r0
            r19 = r30
            r21 = r12
            r18.onPluginLoaded(r19, r20, r21, r22, r23)     // Catch:{ InstantiationException -> 0x0584, IllegalAccessException -> 0x06d7, SecException -> 0x0596 }
            goto L_0x0536
        L_0x0584:
            r4 = move-exception
        L_0x0585:
            java.lang.String r5 = ""
            com.alibaba.wireless.security.framework.utils.a.a(r5, r4)     // Catch:{ all -> 0x015a }
            goto L_0x055f
        L_0x058c:
            r4 = move-exception
            r12 = r19
        L_0x058f:
            java.lang.String r5 = ""
            com.alibaba.wireless.security.framework.utils.a.a(r5, r4)     // Catch:{ all -> 0x015a }
            goto L_0x055f
        L_0x0596:
            r4 = move-exception
            r12 = r4
            java.io.File r13 = new java.io.File     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r11)     // Catch:{ all -> 0x015a }
            java.lang.String r5 = java.io.File.separator     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x015a }
            r13.<init>(r4)     // Catch:{ all -> 0x015a }
            int r4 = r12.getErrorCode()     // Catch:{ all -> 0x015a }
            r5 = 103(0x67, float:1.44E-43)
            if (r4 != r5) goto L_0x067a
            r5 = 100043(0x186cb, float:1.4019E-40)
            r6 = 103(0x67, float:1.44E-43)
            java.lang.String r7 = ""
            java.lang.String r8 = r12.toString()     // Catch:{ all -> 0x015a }
            int r4 = r31.length()     // Catch:{ all -> 0x015a }
            if (r4 != 0) goto L_0x062b
            r9 = r24
        L_0x05d0:
            r0 = r29
            java.io.File r4 = r0.c     // Catch:{ all -> 0x015a }
            if (r4 == 0) goto L_0x0648
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            java.lang.String r10 = "src:"
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            r0 = r29
            java.io.File r10 = r0.c     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r10.getAbsolutePath()     // Catch:{ all -> 0x015a }
            r0 = r27
            java.lang.String r10 = r0.c((java.lang.String) r10)     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r4.toString()     // Catch:{ all -> 0x015a }
        L_0x05f8:
            java.lang.String r4 = r13.getAbsolutePath()     // Catch:{ all -> 0x015a }
            r0 = r27
            java.lang.String r11 = r0.c((java.lang.String) r4)     // Catch:{ all -> 0x015a }
            r4 = r27
            r4.a(r5, r6, r7, r8, r9, r10, r11)     // Catch:{ all -> 0x015a }
            r0 = r29
            boolean r4 = r0.d     // Catch:{ all -> 0x066b }
            if (r4 != 0) goto L_0x0614
            r0 = r27
            com.alibaba.wireless.security.framework.utils.c r4 = r0.b     // Catch:{ all -> 0x066b }
            r4.a()     // Catch:{ all -> 0x066b }
        L_0x0614:
            boolean r4 = r13.exists()     // Catch:{ all -> 0x066b }
            if (r4 == 0) goto L_0x061d
            r13.delete()     // Catch:{ all -> 0x066b }
        L_0x061d:
            r0 = r29
            boolean r4 = r0.d     // Catch:{ all -> 0x015a }
            if (r4 != 0) goto L_0x062a
            r0 = r27
            com.alibaba.wireless.security.framework.utils.c r4 = r0.b     // Catch:{ all -> 0x015a }
            r4.b()     // Catch:{ all -> 0x015a }
        L_0x062a:
            throw r12     // Catch:{ all -> 0x015a }
        L_0x062b:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            r0 = r31
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = "->"
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            r0 = r24
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = r4.toString()     // Catch:{ all -> 0x015a }
            goto L_0x05d0
        L_0x0648:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            java.lang.String r10 = "zipfile:"
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            r0 = r29
            java.io.File r10 = r0.a     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r10.getAbsolutePath()     // Catch:{ all -> 0x015a }
            r0 = r27
            java.lang.String r10 = r0.c((java.lang.String) r10)     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            java.lang.String r10 = r4.toString()     // Catch:{ all -> 0x015a }
            goto L_0x05f8
        L_0x066b:
            r4 = move-exception
            r0 = r29
            boolean r5 = r0.d     // Catch:{ all -> 0x015a }
            if (r5 != 0) goto L_0x0679
            r0 = r27
            com.alibaba.wireless.security.framework.utils.c r5 = r0.b     // Catch:{ all -> 0x015a }
            r5.b()     // Catch:{ all -> 0x015a }
        L_0x0679:
            throw r4     // Catch:{ all -> 0x015a }
        L_0x067a:
            r5 = 100043(0x186cb, float:1.4019E-40)
            r6 = 199(0xc7, float:2.79E-43)
            java.lang.String r7 = "native exception occurred"
            java.lang.String r8 = r12.toString()     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x015a }
            r4.<init>()     // Catch:{ all -> 0x015a }
            java.lang.String r10 = "soName="
            java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = ", authCode="
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            r0 = r27
            java.lang.String r9 = r0.h     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = ", errorCode="
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            int r9 = r12.getErrorCode()     // Catch:{ all -> 0x015a }
            java.lang.StringBuilder r4 = r4.append(r9)     // Catch:{ all -> 0x015a }
            java.lang.String r9 = r4.toString()     // Catch:{ all -> 0x015a }
            r0 = r29
            java.io.File r4 = r0.a     // Catch:{ all -> 0x015a }
            java.lang.String r4 = r4.getAbsolutePath()     // Catch:{ all -> 0x015a }
            r0 = r27
            java.lang.String r10 = r0.c((java.lang.String) r4)     // Catch:{ all -> 0x015a }
            java.lang.String r4 = r13.getAbsolutePath()     // Catch:{ all -> 0x015a }
            r0 = r27
            java.lang.String r11 = r0.c((java.lang.String) r4)     // Catch:{ all -> 0x015a }
            r4 = r27
            r4.a(r5, r6, r7, r8, r9, r10, r11)     // Catch:{ all -> 0x015a }
            goto L_0x062a
        L_0x06d7:
            r4 = move-exception
            goto L_0x058f
        L_0x06da:
            r4 = move-exception
            r12 = r19
            goto L_0x0585
        L_0x06df:
            r5 = r4
            goto L_0x04f2
        L_0x06e2:
            r26 = r8
            r8 = r6
            r6 = r5
            r5 = r4
            r4 = r9
            r9 = r26
            goto L_0x0089
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.framework.d.a(java.lang.String, com.alibaba.wireless.security.framework.d$a, android.content.Context, java.lang.String):com.alibaba.wireless.security.framework.c");
    }

    private File a(Context context) throws SecException {
        if (context == null) {
            a(100038, 116, "", "", "", "", "");
            throw new SecException(116);
        }
        String str = null;
        if (context != null) {
            try {
                String str2 = context.getApplicationInfo().sourceDir;
                if (str2 != null) {
                    File file = new File(str2);
                    if (file.exists()) {
                        str = (file.lastModified() / 1000) + "";
                    }
                }
            } catch (Exception e2) {
                Exception exc = e2;
                a(100038, SecExceptionCode.SEC_ERROR_INIT_SOURCE_DIR_NOT_EXISTED, "", "" + exc, "", "", "");
                throw new SecException((Throwable) exc, (int) SecExceptionCode.SEC_ERROR_INIT_SOURCE_DIR_NOT_EXISTED);
            }
        }
        if (str == null) {
            throw new SecException(SecExceptionCode.SEC_ERROR_INIT_SOURCE_DIR_NOT_EXISTED);
        }
        this.l = context.getDir("SGLib", 0);
        if (this.l == null || !this.l.exists()) {
            a(100038, 109, "", "" + this.l, "", "", "");
            throw new SecException(109);
        }
        File file2 = new File(this.l.getAbsolutePath(), "app_" + str);
        if (!file2.exists()) {
            file2.mkdirs();
            if (!file2.exists()) {
                file2.mkdirs();
            }
        }
        if (p && file2.exists()) {
            p = false;
            a(this.l, "app_" + str);
        }
        if (file2.exists()) {
            return file2;
        }
        a(100038, 114, "", "", "", "", "");
        throw new SecException(114);
    }

    private File a(Context context, b bVar) {
        if (f.a(context) || bVar == null || bVar.b() == 0 || bVar.c() == null || bVar.c().length() <= 0) {
            return null;
        }
        File file = new File(this.k.getAbsolutePath() + File.separator + "libs" + File.separator + bVar.b() + File.separator + bVar.c());
        if (!file.exists()) {
            return null;
        }
        return file;
    }

    private File a(String str) {
        String a2;
        if (this.g == null && (a2 = f.a(d.class.getClassLoader(), "sg" + str)) != null && a2.length() > 0) {
            return new File(a2);
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0086 A[Catch:{ all -> 0x00b5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x009b A[SYNTHETIC, Splitter:B:30:0x009b] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00ab A[SYNTHETIC, Splitter:B:37:0x00ab] */
    /* JADX WARNING: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.io.File a(java.lang.String r11, java.io.File r12) {
        /*
            r10 = this;
            r8 = 0
            android.content.Context r0 = r10.c     // Catch:{ Exception -> 0x000d }
            android.content.pm.ApplicationInfo r0 = r0.getApplicationInfo()     // Catch:{ Exception -> 0x000d }
            java.lang.String r0 = r0.sourceDir     // Catch:{ Exception -> 0x000d }
            r4 = r0
        L_0x000a:
            if (r4 != 0) goto L_0x0010
        L_0x000c:
            return r8
        L_0x000d:
            r0 = move-exception
            r4 = r8
            goto L_0x000a
        L_0x0010:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "libsg"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.StringBuilder r0 = r0.append(r11)
            java.lang.String r1 = ".so"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r2 = r0.toString()
            java.lang.String r0 = "Plugin not existed in the application library path, maybe installed in x86 phone, or the armeabi-v7a existed"
            com.alibaba.wireless.security.framework.utils.a.b(r0)     // Catch:{ IOException -> 0x0078, all -> 0x00a7 }
            java.util.zip.ZipFile r1 = new java.util.zip.ZipFile     // Catch:{ IOException -> 0x0078, all -> 0x00a7 }
            r1.<init>(r4)     // Catch:{ IOException -> 0x0078, all -> 0x00a7 }
            java.lang.String[] r3 = n     // Catch:{ IOException -> 0x00b8, all -> 0x00b3 }
            int r5 = r3.length     // Catch:{ IOException -> 0x00b8, all -> 0x00b3 }
            r0 = 0
        L_0x003a:
            if (r0 >= r5) goto L_0x00bb
            r6 = r3[r0]     // Catch:{ IOException -> 0x00b8, all -> 0x00b3 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00b8, all -> 0x00b3 }
            r7.<init>()     // Catch:{ IOException -> 0x00b8, all -> 0x00b3 }
            java.lang.String r9 = "lib"
            java.lang.StringBuilder r7 = r7.append(r9)     // Catch:{ IOException -> 0x00b8, all -> 0x00b3 }
            java.lang.String r9 = java.io.File.separator     // Catch:{ IOException -> 0x00b8, all -> 0x00b3 }
            java.lang.StringBuilder r7 = r7.append(r9)     // Catch:{ IOException -> 0x00b8, all -> 0x00b3 }
            java.lang.StringBuilder r7 = r7.append(r6)     // Catch:{ IOException -> 0x00b8, all -> 0x00b3 }
            java.lang.String r9 = java.io.File.separator     // Catch:{ IOException -> 0x00b8, all -> 0x00b3 }
            java.lang.StringBuilder r7 = r7.append(r9)     // Catch:{ IOException -> 0x00b8, all -> 0x00b3 }
            java.lang.StringBuilder r7 = r7.append(r2)     // Catch:{ IOException -> 0x00b8, all -> 0x00b3 }
            java.lang.String r7 = r7.toString()     // Catch:{ IOException -> 0x00b8, all -> 0x00b3 }
            java.util.zip.ZipEntry r9 = r1.getEntry(r7)     // Catch:{ IOException -> 0x00b8, all -> 0x00b3 }
            if (r9 == 0) goto L_0x0075
            o = r6     // Catch:{ IOException -> 0x00b8, all -> 0x00b3 }
            java.io.File r0 = r10.a((java.lang.String) r11, (java.io.File) r12, (java.util.zip.ZipFile) r1, (java.lang.String) r7)     // Catch:{ IOException -> 0x00b8, all -> 0x00b3 }
        L_0x006e:
            if (r1 == 0) goto L_0x0073
            r1.close()     // Catch:{ Exception -> 0x00af }
        L_0x0073:
            r8 = r0
            goto L_0x000c
        L_0x0075:
            int r0 = r0 + 1
            goto L_0x003a
        L_0x0078:
            r0 = move-exception
            r9 = r8
        L_0x007a:
            java.lang.String r1 = "getPluginFile throws exception"
            com.alibaba.wireless.security.framework.utils.a.a(r1, r0)     // Catch:{ all -> 0x00b5 }
            r1 = 100047(0x186cf, float:1.40196E-40)
            r2 = 3
            if (r0 == 0) goto L_0x00a3
            java.lang.String r3 = r0.toString()     // Catch:{ all -> 0x00b5 }
        L_0x008a:
            java.lang.String r5 = r10.c((java.lang.String) r4)     // Catch:{ all -> 0x00b5 }
            java.lang.String r6 = ""
            java.lang.String r7 = ""
            r0 = r10
            r4 = r11
            r0.a(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ all -> 0x00b5 }
            if (r9 == 0) goto L_0x000c
            r9.close()     // Catch:{ Exception -> 0x00a0 }
            goto L_0x000c
        L_0x00a0:
            r0 = move-exception
            goto L_0x000c
        L_0x00a3:
            java.lang.String r3 = ""
            goto L_0x008a
        L_0x00a7:
            r0 = move-exception
            r1 = r8
        L_0x00a9:
            if (r1 == 0) goto L_0x00ae
            r1.close()     // Catch:{ Exception -> 0x00b1 }
        L_0x00ae:
            throw r0
        L_0x00af:
            r1 = move-exception
            goto L_0x0073
        L_0x00b1:
            r1 = move-exception
            goto L_0x00ae
        L_0x00b3:
            r0 = move-exception
            goto L_0x00a9
        L_0x00b5:
            r0 = move-exception
            r1 = r9
            goto L_0x00a9
        L_0x00b8:
            r0 = move-exception
            r9 = r1
            goto L_0x007a
        L_0x00bb:
            r0 = r8
            goto L_0x006e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.framework.d.a(java.lang.String, java.io.File):java.io.File");
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0051 A[SYNTHETIC, Splitter:B:31:0x0051] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.io.File a(java.lang.String r11, java.io.File r12, java.lang.String r13, java.lang.String r14) {
        /*
            r10 = this;
            r8 = 0
            if (r13 == 0) goto L_0x0005
            if (r14 != 0) goto L_0x0007
        L_0x0005:
            r0 = r8
        L_0x0006:
            return r0
        L_0x0007:
            java.lang.String r0 = "Plugin not existed in the application library path, maybe installed in x86 phone, or the armeabi-v7a existed"
            com.alibaba.wireless.security.framework.utils.a.b(r0)     // Catch:{ IOException -> 0x001e, all -> 0x004d }
            java.util.zip.ZipFile r1 = new java.util.zip.ZipFile     // Catch:{ IOException -> 0x001e, all -> 0x004d }
            r1.<init>(r13)     // Catch:{ IOException -> 0x001e, all -> 0x004d }
            java.io.File r0 = r10.a((java.lang.String) r11, (java.io.File) r12, (java.util.zip.ZipFile) r1, (java.lang.String) r14)     // Catch:{ IOException -> 0x005c, all -> 0x0057 }
            if (r1 == 0) goto L_0x0006
            r1.close()     // Catch:{ Exception -> 0x001c }
            goto L_0x0006
        L_0x001c:
            r1 = move-exception
            goto L_0x0006
        L_0x001e:
            r0 = move-exception
            r9 = r8
        L_0x0020:
            java.lang.String r1 = "getPluginFile throws exception"
            com.alibaba.wireless.security.framework.utils.a.a(r1, r0)     // Catch:{ all -> 0x005a }
            r1 = 100047(0x186cf, float:1.40196E-40)
            r2 = 3
            if (r0 == 0) goto L_0x0046
            java.lang.String r3 = r0.toString()     // Catch:{ all -> 0x005a }
        L_0x0030:
            java.lang.String r5 = r10.c((java.lang.String) r13)     // Catch:{ all -> 0x005a }
            java.lang.String r6 = ""
            java.lang.String r7 = ""
            r0 = r10
            r4 = r11
            r0.a(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ all -> 0x005a }
            if (r9 == 0) goto L_0x0044
            r9.close()     // Catch:{ Exception -> 0x004a }
        L_0x0044:
            r0 = r8
            goto L_0x0006
        L_0x0046:
            java.lang.String r3 = ""
            goto L_0x0030
        L_0x004a:
            r0 = move-exception
            r0 = r8
            goto L_0x0006
        L_0x004d:
            r0 = move-exception
            r9 = r8
        L_0x004f:
            if (r9 == 0) goto L_0x0054
            r9.close()     // Catch:{ Exception -> 0x0055 }
        L_0x0054:
            throw r0
        L_0x0055:
            r1 = move-exception
            goto L_0x0054
        L_0x0057:
            r0 = move-exception
            r9 = r1
            goto L_0x004f
        L_0x005a:
            r0 = move-exception
            goto L_0x004f
        L_0x005c:
            r0 = move-exception
            r9 = r1
            goto L_0x0020
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.framework.d.a(java.lang.String, java.io.File, java.lang.String, java.lang.String):java.io.File");
    }

    private File a(String str, File file, ZipFile zipFile, String str2) throws IOException {
        ZipEntry entry;
        if (zipFile == null || str2 == null || (entry = zipFile.getEntry(str2)) == null) {
            return null;
        }
        com.alibaba.wireless.security.framework.utils.a.b("Plugin existed  in " + entry.getName());
        File file2 = new File(file, "libsg" + str + "_inner" + entry.getTime() + ".zip");
        if ((!file2.exists() || file2.length() != entry.getSize()) && !f.a(zipFile, entry, file2)) {
            com.alibaba.wireless.security.framework.utils.a.b("Extract failed!!");
            return null;
        }
        com.alibaba.wireless.security.framework.utils.a.b("Extract success");
        return file2;
    }

    private Class<?> a(ClassLoader classLoader, String str) {
        Class<?> cls;
        long currentTimeMillis = System.currentTimeMillis();
        try {
            cls = Class.forName(str, true, classLoader);
        } catch (Throwable th) {
            a(100042, 199, "Class.forName failed", "" + th, str, classLoader.toString(), "");
            cls = null;
        }
        com.alibaba.wireless.security.framework.utils.a.b("    loadClassFromClassLoader( " + str + " ) used time: " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
        return cls;
    }

    private String a(Class<?> cls) {
        InterfacePluginInfo interfacePluginInfo = (InterfacePluginInfo) cls.getAnnotation(InterfacePluginInfo.class);
        if (interfacePluginInfo == null) {
            return null;
        }
        return interfacePluginInfo.pluginName();
    }

    private void a() throws SecException {
        this.k = a(this.c);
        if (this.k != null) {
            this.b = new c(this.c, this.k + File.separator + "lock.lock");
            this.i = b();
            this.m = a(this.c, this.i);
        }
    }

    private void a(int i2, int i3, String str, String str2, String str3, String str4, String str5) {
        UserTrackMethodJniBridge.addUtRecord("" + i2, i3, 0, com.alibaba.wireless.security.open.initialize.c.a(), 0, str, str2, str3, str4, str5);
    }

    /* access modifiers changed from: private */
    public void a(File file) {
        String[] list;
        if (file.isDirectory() && (list = file.list()) != null) {
            for (String file2 : list) {
                a(new File(file, file2));
            }
        }
        file.delete();
    }

    private void a(final File file, final String str) {
        new Thread(new Runnable() {
            public void run() {
                File filesDir;
                File[] listFiles;
                File[] listFiles2;
                try {
                    if (file != null && file.isDirectory() && (listFiles2 = file.listFiles()) != null && listFiles2.length > 0) {
                        for (File file : listFiles2) {
                            if (file.isDirectory() && file.getName().startsWith("app_") && !file.getName().equals(str)) {
                                d.this.a(file);
                            } else if (file.getName().startsWith("libsg")) {
                                file.delete();
                            }
                        }
                    }
                    if (d.this.c != null && (filesDir = d.this.c.getFilesDir()) != null && filesDir.isDirectory() && (listFiles = filesDir.listFiles()) != null && listFiles.length > 0) {
                        for (File file2 : listFiles) {
                            if (file2.getName().startsWith("libsecuritysdk")) {
                                file2.delete();
                            }
                        }
                    }
                } catch (Throwable th) {
                }
            }
        }).start();
    }

    /* JADX WARNING: Removed duplicated region for block: B:45:0x00ad A[Catch:{ all -> 0x0126 }] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00d3 A[SYNTHETIC, Splitter:B:49:0x00d3] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00d8 A[SYNTHETIC, Splitter:B:52:0x00d8] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00df  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00f8 A[SYNTHETIC, Splitter:B:63:0x00f8] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00fd A[SYNTHETIC, Splitter:B:66:0x00fd] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean a(java.io.File r12, java.io.File r13) {
        /*
            r11 = this;
            r1 = 0
            r2 = 0
            r6 = 0
            java.io.File r10 = new java.io.File
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = r13.getAbsolutePath()
            java.lang.StringBuilder r0 = r0.append(r3)
            java.lang.String r3 = ".tmp."
            java.lang.StringBuilder r0 = r0.append(r3)
            int r3 = android.os.Process.myPid()
            java.lang.StringBuilder r0 = r0.append(r3)
            java.lang.String r0 = r0.toString()
            r10.<init>(r0)
            boolean r0 = r10.exists()     // Catch:{ Exception -> 0x009e, all -> 0x00f3 }
            if (r0 == 0) goto L_0x0031
            r10.delete()     // Catch:{ Exception -> 0x009e, all -> 0x00f3 }
        L_0x0031:
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Exception -> 0x009e, all -> 0x00f3 }
            r0.<init>(r12)     // Catch:{ Exception -> 0x009e, all -> 0x00f3 }
            java.nio.channels.FileChannel r1 = r0.getChannel()     // Catch:{ Exception -> 0x009e, all -> 0x00f3 }
            java.io.FileOutputStream r0 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0128, all -> 0x0114 }
            r0.<init>(r10)     // Catch:{ Exception -> 0x0128, all -> 0x0114 }
            java.nio.channels.FileChannel r0 = r0.getChannel()     // Catch:{ Exception -> 0x0128, all -> 0x0114 }
            r2 = 0
            long r4 = r1.size()     // Catch:{ Exception -> 0x012d, all -> 0x0118 }
            r0.transferFrom(r1, r2, r4)     // Catch:{ Exception -> 0x012d, all -> 0x0118 }
            r1.close()     // Catch:{ Exception -> 0x012d, all -> 0x0118 }
            r2 = 0
            r0.close()     // Catch:{ Exception -> 0x0133, all -> 0x011d }
            r1 = 0
            long r4 = r10.length()     // Catch:{ Exception -> 0x0139, all -> 0x0122 }
            long r8 = r12.length()     // Catch:{ Exception -> 0x0139, all -> 0x0122 }
            int r0 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r0 != 0) goto L_0x013e
            boolean r0 = r13.exists()     // Catch:{ Exception -> 0x0139, all -> 0x0122 }
            if (r0 == 0) goto L_0x0089
            long r4 = r13.length()     // Catch:{ Exception -> 0x0139, all -> 0x0122 }
            long r6 = r12.length()     // Catch:{ Exception -> 0x0139, all -> 0x0122 }
            int r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r0 != 0) goto L_0x0081
            r0 = 1
        L_0x0073:
            if (r2 == 0) goto L_0x0078
            r2.close()     // Catch:{ Exception -> 0x008e }
        L_0x0078:
            if (r1 == 0) goto L_0x007d
            r1.close()     // Catch:{ Exception -> 0x0096 }
        L_0x007d:
            r10.delete()
        L_0x0080:
            return r0
        L_0x0081:
            r13.delete()     // Catch:{ Exception -> 0x0139, all -> 0x0122 }
            boolean r0 = r10.renameTo(r13)     // Catch:{ Exception -> 0x0139, all -> 0x0122 }
            goto L_0x0073
        L_0x0089:
            boolean r0 = r10.renameTo(r13)     // Catch:{ Exception -> 0x0139, all -> 0x0122 }
            goto L_0x0073
        L_0x008e:
            r2 = move-exception
            java.lang.String r3 = ""
            com.alibaba.wireless.security.framework.utils.a.a(r3, r2)
            goto L_0x0078
        L_0x0096:
            r1 = move-exception
            java.lang.String r2 = ""
            com.alibaba.wireless.security.framework.utils.a.a(r2, r1)
            goto L_0x007d
        L_0x009e:
            r0 = move-exception
            r8 = r2
            r9 = r1
        L_0x00a1:
            java.lang.String r1 = ""
            com.alibaba.wireless.security.framework.utils.a.a(r1, r0)     // Catch:{ all -> 0x0126 }
            r1 = 100047(0x186cf, float:1.40196E-40)
            r2 = 2
            if (r0 == 0) goto L_0x00df
            java.lang.String r3 = r0.toString()     // Catch:{ all -> 0x0126 }
        L_0x00b1:
            java.lang.String r0 = r12.getAbsolutePath()     // Catch:{ all -> 0x0126 }
            java.lang.String r4 = r11.c((java.lang.String) r0)     // Catch:{ all -> 0x0126 }
            java.lang.String r0 = r13.getAbsolutePath()     // Catch:{ all -> 0x0126 }
            java.lang.String r5 = r11.c((java.lang.String) r0)     // Catch:{ all -> 0x0126 }
            java.lang.String r0 = r10.getAbsolutePath()     // Catch:{ all -> 0x0126 }
            java.lang.String r6 = r11.c((java.lang.String) r0)     // Catch:{ all -> 0x0126 }
            java.lang.String r7 = ""
            r0 = r11
            r0.a(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ all -> 0x0126 }
            r0 = 0
            if (r9 == 0) goto L_0x00d6
            r9.close()     // Catch:{ Exception -> 0x00e3 }
        L_0x00d6:
            if (r8 == 0) goto L_0x00db
            r8.close()     // Catch:{ Exception -> 0x00eb }
        L_0x00db:
            r10.delete()
            goto L_0x0080
        L_0x00df:
            java.lang.String r3 = ""
            goto L_0x00b1
        L_0x00e3:
            r1 = move-exception
            java.lang.String r2 = ""
            com.alibaba.wireless.security.framework.utils.a.a(r2, r1)
            goto L_0x00d6
        L_0x00eb:
            r1 = move-exception
            java.lang.String r2 = ""
            com.alibaba.wireless.security.framework.utils.a.a(r2, r1)
            goto L_0x00db
        L_0x00f3:
            r0 = move-exception
            r8 = r2
            r9 = r1
        L_0x00f6:
            if (r9 == 0) goto L_0x00fb
            r9.close()     // Catch:{ Exception -> 0x0104 }
        L_0x00fb:
            if (r8 == 0) goto L_0x0100
            r8.close()     // Catch:{ Exception -> 0x010c }
        L_0x0100:
            r10.delete()
            throw r0
        L_0x0104:
            r1 = move-exception
            java.lang.String r2 = ""
            com.alibaba.wireless.security.framework.utils.a.a(r2, r1)
            goto L_0x00fb
        L_0x010c:
            r1 = move-exception
            java.lang.String r2 = ""
            com.alibaba.wireless.security.framework.utils.a.a(r2, r1)
            goto L_0x0100
        L_0x0114:
            r0 = move-exception
            r8 = r2
            r9 = r1
            goto L_0x00f6
        L_0x0118:
            r2 = move-exception
            r8 = r0
            r9 = r1
            r0 = r2
            goto L_0x00f6
        L_0x011d:
            r1 = move-exception
            r8 = r0
            r9 = r2
            r0 = r1
            goto L_0x00f6
        L_0x0122:
            r0 = move-exception
            r8 = r1
            r9 = r2
            goto L_0x00f6
        L_0x0126:
            r0 = move-exception
            goto L_0x00f6
        L_0x0128:
            r0 = move-exception
            r8 = r2
            r9 = r1
            goto L_0x00a1
        L_0x012d:
            r2 = move-exception
            r8 = r0
            r9 = r1
            r0 = r2
            goto L_0x00a1
        L_0x0133:
            r1 = move-exception
            r8 = r0
            r9 = r2
            r0 = r1
            goto L_0x00a1
        L_0x0139:
            r0 = move-exception
            r8 = r1
            r9 = r2
            goto L_0x00a1
        L_0x013e:
            r0 = r6
            goto L_0x0073
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.framework.d.a(java.io.File, java.io.File):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:0x0126, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:101:0x0127, code lost:
        r8 = r0;
        r9 = r3;
        r0 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x010b, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x010c, code lost:
        r2 = r3;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x010b A[ExcHandler: all (th java.lang.Throwable), Splitter:B:22:0x0038] */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x010f A[SYNTHETIC, Splitter:B:85:0x010f] */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0116 A[SYNTHETIC, Splitter:B:88:0x0116] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized boolean a(java.lang.String r11, java.lang.String r12, java.io.File r13, java.lang.String r14, boolean r15) {
        /*
            r10 = this;
            monitor-enter(r10)
            android.content.Context r0 = r10.c     // Catch:{ all -> 0x011a }
            boolean r0 = com.alibaba.wireless.security.framework.utils.f.a(r0)     // Catch:{ all -> 0x011a }
            if (r0 == 0) goto L_0x0012
            int r0 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x011a }
            r1 = 19
            if (r0 <= r1) goto L_0x0012
            r0 = 1
        L_0x0010:
            monitor-exit(r10)
            return r0
        L_0x0012:
            java.lang.String r0 = r10.g     // Catch:{ all -> 0x011a }
            if (r0 == 0) goto L_0x0018
            r0 = 1
            goto L_0x0010
        L_0x0018:
            if (r13 == 0) goto L_0x002f
            java.io.File r0 = new java.io.File     // Catch:{ all -> 0x011a }
            java.lang.String r1 = r13.getParent()     // Catch:{ all -> 0x011a }
            r0.<init>(r1, r14)     // Catch:{ all -> 0x011a }
            boolean r0 = r0.exists()     // Catch:{ all -> 0x011a }
            if (r0 == 0) goto L_0x002b
            r0 = 1
            goto L_0x0010
        L_0x002b:
            java.lang.String r11 = r13.getAbsolutePath()     // Catch:{ all -> 0x011a }
        L_0x002f:
            if (r15 != 0) goto L_0x0036
            com.alibaba.wireless.security.framework.utils.c r0 = r10.b     // Catch:{ all -> 0x011a }
            r0.a()     // Catch:{ all -> 0x011a }
        L_0x0036:
            r3 = 0
            r2 = 0
            java.io.File r1 = new java.io.File     // Catch:{ IOException -> 0x00bb, all -> 0x010b }
            r1.<init>(r12, r14)     // Catch:{ IOException -> 0x00bb, all -> 0x010b }
            boolean r0 = r1.exists()     // Catch:{ IOException -> 0x0126, all -> 0x010b }
            if (r0 == 0) goto L_0x0053
            r0 = 1
            if (r15 != 0) goto L_0x004b
            com.alibaba.wireless.security.framework.utils.c r1 = r10.b     // Catch:{ all -> 0x011a }
            r1.b()     // Catch:{ all -> 0x011a }
        L_0x004b:
            if (r3 == 0) goto L_0x0010
            r3.close()     // Catch:{ IOException -> 0x0051 }
            goto L_0x0010
        L_0x0051:
            r1 = move-exception
            goto L_0x0010
        L_0x0053:
            java.util.zip.ZipFile r2 = new java.util.zip.ZipFile     // Catch:{ IOException -> 0x0126, all -> 0x010b }
            r2.<init>(r11)     // Catch:{ IOException -> 0x0126, all -> 0x010b }
            java.lang.String[] r3 = n     // Catch:{ IOException -> 0x012b, all -> 0x0121 }
            int r4 = r3.length     // Catch:{ IOException -> 0x012b, all -> 0x0121 }
            r0 = 0
        L_0x005c:
            if (r0 >= r4) goto L_0x00ac
            r5 = r3[r0]     // Catch:{ IOException -> 0x012b, all -> 0x0121 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x012b, all -> 0x0121 }
            r6.<init>()     // Catch:{ IOException -> 0x012b, all -> 0x0121 }
            java.lang.String r7 = "lib"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ IOException -> 0x012b, all -> 0x0121 }
            java.lang.String r7 = java.io.File.separator     // Catch:{ IOException -> 0x012b, all -> 0x0121 }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ IOException -> 0x012b, all -> 0x0121 }
            java.lang.StringBuilder r5 = r6.append(r5)     // Catch:{ IOException -> 0x012b, all -> 0x0121 }
            java.lang.String r6 = java.io.File.separator     // Catch:{ IOException -> 0x012b, all -> 0x0121 }
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ IOException -> 0x012b, all -> 0x0121 }
            java.lang.StringBuilder r5 = r5.append(r14)     // Catch:{ IOException -> 0x012b, all -> 0x0121 }
            java.lang.String r5 = r5.toString()     // Catch:{ IOException -> 0x012b, all -> 0x0121 }
            java.util.zip.ZipEntry r5 = r2.getEntry(r5)     // Catch:{ IOException -> 0x012b, all -> 0x0121 }
            if (r5 == 0) goto L_0x0094
            long r6 = r5.getSize()     // Catch:{ IOException -> 0x012b, all -> 0x0121 }
            r8 = 0
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 != 0) goto L_0x0097
        L_0x0094:
            int r0 = r0 + 1
            goto L_0x005c
        L_0x0097:
            boolean r0 = com.alibaba.wireless.security.framework.utils.f.a((java.util.zip.ZipFile) r2, (java.util.zip.ZipEntry) r5, (java.io.File) r1)     // Catch:{ IOException -> 0x012b, all -> 0x0121 }
            if (r15 != 0) goto L_0x00a2
            com.alibaba.wireless.security.framework.utils.c r1 = r10.b     // Catch:{ all -> 0x011a }
            r1.b()     // Catch:{ all -> 0x011a }
        L_0x00a2:
            if (r2 == 0) goto L_0x0010
            r2.close()     // Catch:{ IOException -> 0x00a9 }
            goto L_0x0010
        L_0x00a9:
            r1 = move-exception
            goto L_0x0010
        L_0x00ac:
            if (r15 != 0) goto L_0x00b3
            com.alibaba.wireless.security.framework.utils.c r0 = r10.b     // Catch:{ all -> 0x011a }
            r0.b()     // Catch:{ all -> 0x011a }
        L_0x00b3:
            if (r2 == 0) goto L_0x00b8
            r2.close()     // Catch:{ IOException -> 0x011d }
        L_0x00b8:
            r0 = 0
            goto L_0x0010
        L_0x00bb:
            r0 = move-exception
            r8 = r0
            r9 = r3
            r0 = r2
        L_0x00bf:
            r1 = 100039(0x186c7, float:1.40184E-40)
            r2 = 107(0x6b, float:1.5E-43)
            if (r8 == 0) goto L_0x00ff
            java.lang.String r3 = r8.toString()     // Catch:{ all -> 0x0123 }
        L_0x00ca:
            java.lang.String r4 = r10.c()     // Catch:{ all -> 0x0123 }
            java.lang.String r5 = r10.c((java.lang.String) r11)     // Catch:{ all -> 0x0123 }
            if (r0 == 0) goto L_0x0103
            java.lang.String r0 = r0.getAbsolutePath()     // Catch:{ all -> 0x0123 }
            java.lang.String r6 = r10.c((java.lang.String) r0)     // Catch:{ all -> 0x0123 }
        L_0x00dc:
            if (r13 == 0) goto L_0x0107
            java.lang.String r0 = r13.getAbsolutePath()     // Catch:{ all -> 0x0123 }
            java.lang.String r7 = r10.c((java.lang.String) r0)     // Catch:{ all -> 0x0123 }
        L_0x00e6:
            r0 = r10
            r0.a(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ all -> 0x0123 }
            java.lang.String r0 = ""
            com.alibaba.wireless.security.framework.utils.a.a(r0, r8)     // Catch:{ all -> 0x0123 }
            if (r15 != 0) goto L_0x00f7
            com.alibaba.wireless.security.framework.utils.c r0 = r10.b     // Catch:{ all -> 0x011a }
            r0.b()     // Catch:{ all -> 0x011a }
        L_0x00f7:
            if (r9 == 0) goto L_0x00b8
            r9.close()     // Catch:{ IOException -> 0x00fd }
            goto L_0x00b8
        L_0x00fd:
            r0 = move-exception
            goto L_0x00b8
        L_0x00ff:
            java.lang.String r3 = ""
            goto L_0x00ca
        L_0x0103:
            java.lang.String r6 = ""
            goto L_0x00dc
        L_0x0107:
            java.lang.String r7 = ""
            goto L_0x00e6
        L_0x010b:
            r0 = move-exception
            r2 = r3
        L_0x010d:
            if (r15 != 0) goto L_0x0114
            com.alibaba.wireless.security.framework.utils.c r1 = r10.b     // Catch:{ all -> 0x011a }
            r1.b()     // Catch:{ all -> 0x011a }
        L_0x0114:
            if (r2 == 0) goto L_0x0119
            r2.close()     // Catch:{ IOException -> 0x011f }
        L_0x0119:
            throw r0     // Catch:{ all -> 0x011a }
        L_0x011a:
            r0 = move-exception
            monitor-exit(r10)
            throw r0
        L_0x011d:
            r0 = move-exception
            goto L_0x00b8
        L_0x011f:
            r1 = move-exception
            goto L_0x0119
        L_0x0121:
            r0 = move-exception
            goto L_0x010d
        L_0x0123:
            r0 = move-exception
            r2 = r9
            goto L_0x010d
        L_0x0126:
            r0 = move-exception
            r8 = r0
            r9 = r3
            r0 = r1
            goto L_0x00bf
        L_0x012b:
            r0 = move-exception
            r8 = r0
            r9 = r2
            r0 = r1
            goto L_0x00bf
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.framework.d.a(java.lang.String, java.lang.String, java.io.File, java.lang.String, boolean):boolean");
    }

    /* access modifiers changed from: private */
    public boolean a(String str, String str2, boolean z) throws SecException {
        int i2;
        ISGPluginInfo iSGPluginInfo;
        Throwable th;
        int indexOf;
        int indexOf2;
        if (str.trim().length() == 0) {
            return true;
        }
        String[] split = str.split(SymbolExpUtil.SYMBOL_SEMICOLON);
        int i3 = 0;
        while (true) {
            i2 = i3;
            if (i2 >= split.length) {
                return true;
            }
            String trim = split[i2].trim();
            if (trim.length() != 0) {
                String[] split2 = trim.split(SymbolExpUtil.SYMBOL_COLON);
                if (split2.length != 2) {
                    a(100040, 199, "nameVersionPair.length != 2", trim, str2, "" + z, "" + i2);
                    throw new SecException(199);
                }
                int indexOf3 = str2.indexOf(split2[0]);
                if (indexOf3 >= 0) {
                    indexOf = str2.indexOf("(", indexOf3);
                    indexOf2 = str2.indexOf(")", indexOf3);
                    if (indexOf < 0 || indexOf2 < 0 || indexOf > indexOf2) {
                        a(100040, 199, "indexLeftP < 0 || indexRightP < 0 || indexLeftP > indexRightP", "" + indexOf, "" + indexOf2, "", "" + i2);
                    } else {
                        String substring = str2.substring(indexOf + 1, indexOf2);
                        if (a(substring, split2[1]) < 0) {
                            String str3 = "version " + substring + " of " + split2[0] + " is not meet the requirement: " + split2[1];
                            String str4 = str2.length() != 0 ? str3 + ", depended by: " + str2 : str3;
                            if (!z) {
                                a(100040, 113, "versionCompare(parentPluginVersion, nameVersionPair[1]) < 0", substring, split2[0], split2[1], "" + i2);
                            }
                            throw new SecException(str4, 113);
                        }
                    }
                } else {
                    ISGPluginInfo iSGPluginInfo2 = this.d.get(split2[0]);
                    if (iSGPluginInfo2 == null) {
                        try {
                            iSGPluginInfo = d(split2[0], str2, !z);
                            th = null;
                        } catch (Throwable th2) {
                            Throwable th3 = th2;
                            iSGPluginInfo = iSGPluginInfo2;
                            th = th3;
                        }
                        if (iSGPluginInfo != null) {
                            iSGPluginInfo2 = iSGPluginInfo;
                        } else if (!z) {
                            if (th instanceof SecException) {
                                throw ((SecException) th);
                            }
                            a(100040, 199, "throwable in loadPluginInner", "" + th, str, str2, "" + i2);
                            throw new SecException(str2, 199);
                        }
                    }
                    if (a(iSGPluginInfo2.getVersion(), split2[1]) < 0) {
                        String str5 = "version " + iSGPluginInfo2.getVersion() + " of " + split2[0] + " is not meet the requirement: " + split2[1];
                        String str6 = str2.length() != 0 ? str5 + ", depended by: " + str2 : str5;
                        if (!z) {
                            a(100040, 113, "versionCompare(dependPlugin.getVersion(), nameVersionPair[1]) < 0", iSGPluginInfo2.getVersion(), split2[0], split2[1], "" + i2);
                        }
                        throw new SecException(str6, 113);
                    }
                }
            }
            i3 = i2 + 1;
        }
        a(100040, 199, "indexLeftP < 0 || indexRightP < 0 || indexLeftP > indexRightP", "" + indexOf, "" + indexOf2, "", "" + i2);
        throw new SecException(199);
    }

    private b b() {
        File file = new File(this.k, "update.config");
        File file2 = new File(this.k, "init.config");
        if (this.j) {
            b a2 = b.a(file);
            if (a2 == null) {
                return b.a(file2);
            }
            try {
                this.b.a();
                file2.delete();
                file.renameTo(file2);
                return a2;
            } finally {
                this.b.b();
            }
        } else {
            try {
                this.b.a();
                return b.a(file2);
            } finally {
                this.b.b();
            }
        }
    }

    private DexClassLoader b(String str, String str2, boolean z) {
        if (!z) {
            try {
                this.b.a();
            } catch (Throwable th) {
                if (!z) {
                    this.b.b();
                }
                throw th;
            }
        }
        String str3 = this.c.getApplicationInfo().nativeLibraryDir + File.pathSeparator + str2;
        if (this.g != null) {
            str3 = str3 + File.pathSeparator + this.g;
            com.alibaba.wireless.security.framework.utils.a.b("add path to native classloader " + str3);
        }
        if (o != null) {
            str3 = str3 + File.pathSeparator + this.c.getApplicationInfo().sourceDir + "!/lib/" + o;
        }
        DexClassLoader dexClassLoader = new DexClassLoader(str, str2, str3, d.class.getClassLoader());
        if (!z) {
            this.b.b();
        }
        return dexClassLoader;
    }

    private File b(String str) {
        String str2 = this.g;
        if (str2 == null) {
            try {
                str2 = this.c.getApplicationInfo().nativeLibraryDir;
            } catch (Exception e2) {
            }
        }
        if (str2 != null && str2.length() > 0) {
            File file = new File(str2 + File.separator + "libsg" + str + MergeConstants.SO_SUFFIX);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

    private boolean b(File file) {
        try {
            if (!file.getParentFile().getCanonicalPath().equals(file.getCanonicalFile().getParentFile().getCanonicalPath())) {
                return true;
            }
            return !file.getName().equals(file.getCanonicalFile().getName());
        } catch (Exception e2) {
            com.alibaba.wireless.security.framework.utils.a.a("", e2);
            a(100047, 0, e2 != null ? e2.toString() : "", file.getAbsolutePath(), "", "", "");
            return false;
        }
    }

    private boolean b(File file, File file2) {
        Method method;
        Object obj = null;
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                method = Class.forName("android.system.Os").getDeclaredMethod(RequestParameters.X_OSS_SYMLINK, new Class[]{String.class, String.class});
            } else {
                Field declaredField = Class.forName("libcore.io.Libcore").getDeclaredField("os");
                declaredField.setAccessible(true);
                obj = declaredField.get((Object) null);
                method = obj.getClass().getMethod(RequestParameters.X_OSS_SYMLINK, new Class[]{String.class, String.class});
            }
            method.invoke(obj, new Object[]{file.getAbsolutePath(), file2.getAbsolutePath()});
            return true;
        } catch (Exception e2) {
            com.alibaba.wireless.security.framework.utils.a.a("create symbolic link( " + file2.getAbsolutePath() + " -> " + file.getAbsolutePath() + " ) failed", e2);
            a(100047, 1, e2 != null ? e2.toString() : "", file.getAbsolutePath(), file2.getAbsolutePath(), "" + Build.VERSION.SDK_INT, "");
            return false;
        }
    }

    private boolean b(String str, String str2) throws SecException {
        for (Map.Entry next : this.d.entrySet()) {
            String str3 = (String) next.getKey();
            c cVar = (c) next.getValue();
            String a2 = cVar.a("reversedependencies");
            if (a2 != null) {
                String[] split = a2.split(SymbolExpUtil.SYMBOL_SEMICOLON);
                for (int i2 = 0; i2 < split.length; i2++) {
                    String trim = split[i2].trim();
                    if (trim.length() != 0) {
                        String[] split2 = trim.split(SymbolExpUtil.SYMBOL_COLON);
                        if (split2.length != 2) {
                            a(100041, 199, "nameVersionPair.length != 2", str + "(" + str2 + ")", str3 + "(" + cVar.getVersion() + ")", c(cVar.getPluginPath()), a2);
                            throw new SecException(199);
                        } else if (split2[0].equalsIgnoreCase(str) && a(str2, split2[1]) < 0) {
                            String str4 = "plugin " + str + "(" + str2 + ") is not meet the reverse dependency of " + str3 + "(" + cVar.getVersion() + "): " + split2[0] + "(" + split2[1] + ")";
                            a(100041, 117, str4, d.class.getClassLoader().toString(), c(cVar.getPluginPath()), a2, "" + i2);
                            throw new SecException(str4, 117);
                        }
                    }
                }
                continue;
            }
        }
        return true;
    }

    private a c(String str, String str2, boolean z) throws SecException {
        File file;
        boolean z2;
        File file2;
        a aVar;
        String absolutePath;
        File file3 = null;
        File file4 = null;
        boolean z3 = false;
        if (this.m != null) {
            file3 = new File(this.m, "libsg" + str + MergeConstants.SO_SUFFIX);
            if (file3.exists()) {
                file4 = d(this.m);
                z3 = file4 != this.m;
            } else {
                file3 = null;
            }
        }
        if (file4 == null) {
            File d2 = d(this.k);
            file = d2;
            z2 = d2 != this.k;
        } else {
            file = file4;
            z2 = z3;
        }
        if (!z2) {
            this.b.a();
        }
        if (file3 == null) {
            try {
                file2 = a(str);
                if (!c(file2)) {
                    file2 = null;
                }
            } catch (Throwable th) {
                if (!z2) {
                    this.b.b();
                }
                throw th;
            }
        } else {
            file2 = file3;
        }
        if (file2 != null && (absolutePath = file2.getAbsolutePath()) != null && absolutePath.contains("!/")) {
            String[] split = absolutePath.split("!/", 2);
            String str3 = split[0];
            String str4 = split[1];
            String[] strArr = n;
            int length = strArr.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                String str5 = strArr[i2];
                if (("lib" + File.separator + str5 + File.separator + "libsg" + str + MergeConstants.SO_SUFFIX).equals(str4)) {
                    o = str5;
                    file2 = a(str, file, str3, str4);
                    break;
                }
                i2++;
            }
        }
        if (file2 == null) {
            file2 = b(str);
        }
        File a2 = (file2 != null || !z) ? file2 : a(str, file);
        if (a2 == null) {
            aVar = null;
            if (!z2) {
                this.b.b();
            }
        } else {
            if (a2.getAbsolutePath().endsWith(".zip")) {
                aVar = new a(a2, file, (File) null, z2);
            } else if (f.a()) {
                aVar = new a(a2, file, (File) null, z2);
            } else {
                File file5 = new File(file, "libsg" + str + "_" + a2.lastModified() + ".zip");
                if (!file5.exists() || !b(file5) || c(file5, a2)) {
                    file5.delete();
                    if (b(a2, file5)) {
                        aVar = new a(file5, file, a2, z2);
                    } else {
                        File file6 = new File(file, "libsg" + str + "_cp" + a2.lastModified() + ".zip");
                        aVar = ((!file6.exists() || file6.length() != a2.length()) && !a(a2, file6)) ? null : new a(file6, file, a2, z2);
                    }
                } else {
                    aVar = new a(file5, file, a2, z2);
                }
            }
            if (!z2) {
                this.b.b();
            }
        }
        return aVar;
    }

    private String c() {
        StringBuilder sb = new StringBuilder();
        File file = this.k;
        if (file == null || !file.exists() || !file.isDirectory()) {
            sb.append("not exists!");
        } else {
            try {
                sb.append("[");
                File[] listFiles = file.listFiles();
                int i2 = 0;
                while (listFiles != null && i2 < listFiles.length) {
                    File file2 = listFiles[i2];
                    if (file2.getName().startsWith("libsg") && (file2.getName().endsWith("zip") || file2.getName().endsWith(MergeConstants.SO_SUFFIX))) {
                        sb.append(file2.getName());
                        sb.append("(");
                        sb.append(b(file2) + " , ");
                        sb.append(file2.length());
                        sb.append(") , ");
                    }
                    i2++;
                }
                sb.append("]");
            } catch (Throwable th) {
            }
        }
        return sb.toString();
    }

    private String c(String str) {
        if (str == null || str.length() <= 0) {
            return "";
        }
        File file = new File(str);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        if (b(file)) {
            sb.append("->");
            try {
                sb.append(file.getCanonicalPath());
            } catch (Exception e2) {
            }
        }
        sb.append('[');
        sb.append("exists:" + file.exists() + ",");
        sb.append("size:" + file.length() + ",");
        sb.append("canRead:" + file.canRead() + ",");
        sb.append("canWrite:" + file.canWrite() + ",");
        sb.append("totalSpace:" + file.getTotalSpace() + ",");
        sb.append("freeSpace:" + file.getFreeSpace() + ",");
        sb.append(']');
        return sb.toString();
    }

    private boolean c(File file) {
        if (file == null) {
            return false;
        }
        try {
            String absolutePath = file.getAbsolutePath();
            if (absolutePath == null || absolutePath.length() <= 0) {
                return false;
            }
            return f.a(this.c) || !absolutePath.startsWith("/system/");
        } catch (Exception e2) {
            return false;
        }
    }

    private boolean c(File file, File file2) {
        try {
            return file.getCanonicalPath().equals(file2.getCanonicalPath());
        } catch (Exception e2) {
            com.alibaba.wireless.security.framework.utils.a.a("", e2);
            a(100046, 0, e2 != null ? e2.toString() : "", file.getAbsolutePath(), file2.getAbsolutePath(), "", "");
            return false;
        }
    }

    private synchronized ISGPluginInfo d(String str, String str2, boolean z) throws SecException {
        c cVar;
        cVar = this.d.get(str);
        if (cVar == null) {
            if (this.k == null || !this.k.exists()) {
                a();
            }
            a c2 = c(str, str2, z);
            if (c2 != null && c2.a != null && c2.a.exists()) {
                cVar = a(str, c2, this.c, str2);
                if (cVar == null) {
                    a(100044, 110, "", str, str2, c2.c != null ? "src:" + c(c2.c.getAbsolutePath()) : "zipfile:" + c(c2.a.getAbsolutePath()), "");
                    throw new SecException(str, 111);
                }
                this.d.put(str, cVar);
                String str3 = str + "(" + cVar.getVersion() + ")";
                final String a2 = cVar.a("weakdependencies");
                final String str4 = str2.length() == 0 ? str3 : str2 + "->" + str3;
                Looper myLooper = Looper.myLooper();
                if (myLooper == null || myLooper == Looper.getMainLooper()) {
                    com.alibaba.wireless.security.framework.utils.a.a("looper of current thread is null, try to create a new thread with a looper");
                    HandlerThread handlerThread = new HandlerThread("SGBackgroud");
                    handlerThread.start();
                    myLooper = handlerThread.getLooper();
                }
                new Handler(myLooper).postDelayed(new Runnable() {
                    public void run() {
                        try {
                            boolean unused = d.this.a(a2, str4, true);
                        } catch (SecException e) {
                            com.alibaba.wireless.security.framework.utils.a.a("load weak dependency", e);
                        }
                    }
                }, 60000);
            } else if (z) {
                String str5 = "plugin " + str + " not existed";
                String str6 = str2.length() != 0 ? str5 + ", depended by " + str2 : str5;
                a(100044, 110, "", str, str2, "", "");
                throw new SecException(str6, 110);
            } else {
                cVar = null;
            }
        }
        return cVar;
    }

    private File d(File file) {
        if (!file.exists() || !file.isDirectory() || !this.j) {
            return file;
        }
        File file2 = new File(file, "main");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        return file2.exists() ? file2 : file;
    }

    public void a(Context context, String str, String str2, boolean z, Object... objArr) {
        if (context.getApplicationContext() != null) {
            context = context.getApplicationContext();
        }
        this.c = context;
        this.h = str;
        this.j = f.c(this.c);
        this.f = z;
        UserTrackMethodJniBridge.init(this.c);
        if (str2 != null && !str2.isEmpty()) {
            this.g = str2;
        }
        try {
            a();
        } catch (SecException e2) {
        }
    }

    public synchronized <T> T getInterface(Class<T> cls) throws SecException {
        T cast;
        Object obj = this.a.get(cls);
        if (obj != null) {
            cast = cls.cast(obj);
        } else {
            String str = null;
            if (this.i != null && ((str = this.i.d()) == null || str.length() == 0)) {
                str = this.i.a(cls.getName());
            }
            if (str == null || str.length() == 0) {
                str = a((Class<?>) cls);
            }
            if (str == null || str.length() == 0) {
                throw new SecException(150);
            }
            ISGPluginInfo pluginInfo = getPluginInfo(str);
            if (pluginInfo == null) {
                throw new SecException(110);
            }
            T t = pluginInfo.getSGPluginEntry().getInterface(cls);
            if (t == null) {
                a(100045, 112, "", cls.getName(), str + "(" + pluginInfo.getVersion() + ")", c(pluginInfo.getPluginPath()), "");
                throw new SecException(112);
            }
            this.a.put(cls, t);
            cast = cls.cast(t);
        }
        return cast;
    }

    public String getMainPluginName() {
        return "main";
    }

    public ISGPluginInfo getPluginInfo(String str) throws SecException {
        return d(str, "", true);
    }

    public IRouterComponent getRouter() {
        return this.e;
    }
}
