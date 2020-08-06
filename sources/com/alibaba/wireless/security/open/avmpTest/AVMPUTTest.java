package com.alibaba.wireless.security.open.avmpTest;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.alibaba.wireless.security.SecExceptionCode;
import com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent;
import com.taobao.detail.domain.tuwen.TuwenConstants;

public class AVMPUTTest {
    public static final String TAG = "AVMPUTTest_T";
    private static IAVMPGenericComponent a = null;
    private static IAVMPGenericComponent.IAVMPGenericInstance b = null;

    private static synchronized void a(Context context) {
        synchronized (AVMPUTTest.class) {
            int i = 0;
            boolean z = false;
            int isForeground = isForeground(context);
            long currentTimeMillis = System.currentTimeMillis();
            try {
                if (b != null) {
                    a("AVMP instance has been initialized");
                } else {
                    a = (IAVMPGenericComponent) SecurityGuardManager.getInstance(context).getInterface(IAVMPGenericComponent.class);
                    if (a != null) {
                        a = (IAVMPGenericComponent) SecurityGuardManager.getInstance(context).getInterface(IAVMPGenericComponent.class);
                        b = a.createAVMPInstance("mwua", "sgcipher");
                        z = true;
                    }
                }
                UserTrackMethodJniBridge.addUtRecord("100086", 0, 1, "", System.currentTimeMillis() - currentTimeMillis, "", String.valueOf(z), String.valueOf(b != null), "" + isForeground + "" + isForeground(context), "");
            } catch (SecException e) {
                UserTrackMethodJniBridge.addUtRecord("100086", e.getErrorCode(), 1, "", System.currentTimeMillis() - currentTimeMillis, "", String.valueOf(false), String.valueOf(b != null), "" + isForeground + "" + isForeground(context), "");
            } catch (Exception e2) {
                i = SecExceptionCode.SEC_ERROR_GENERIC_AVMP_UNKNOWN_ERROR;
                UserTrackMethodJniBridge.addUtRecord("100086", SecExceptionCode.SEC_ERROR_GENERIC_AVMP_UNKNOWN_ERROR, 1, "", System.currentTimeMillis() - currentTimeMillis, e2.getMessage(), String.valueOf(false), String.valueOf(b != null), "" + isForeground + "" + isForeground(context), "");
            } catch (Throwable th) {
                Throwable th2 = th;
                UserTrackMethodJniBridge.addUtRecord("100086", i, 1, "", System.currentTimeMillis() - currentTimeMillis, "", String.valueOf(false), String.valueOf(b != null), "" + isForeground + "" + isForeground(context), "");
                throw th2;
            }
        }
    }

    private static void a(String str) {
        Log.d("AVMPUTTest", str);
    }

    public static byte[] avmpSign(IAVMPGenericComponent.IAVMPGenericInstance iAVMPGenericInstance, byte[] bArr, String str, int i) throws Exception {
        if (iAVMPGenericInstance == null) {
            return null;
        }
        return (byte[]) iAVMPGenericInstance.invokeAVMP("sign", new byte[0].getClass(), Integer.valueOf(i), str.getBytes(), Integer.valueOf(str.getBytes().length), null, bArr, 0);
    }

    public static byte[] avmpSign2(IAVMPGenericComponent.IAVMPGenericInstance iAVMPGenericInstance, String str, String str2) throws Exception {
        if (iAVMPGenericInstance == null) {
            return null;
        }
        return (byte[]) iAVMPGenericInstance.invokeAVMP2("sign_v2", new byte[0].getClass(), str2, str, 0);
    }

    public static void avmpTest(Context context, String str) {
        a("enter avmpTest");
        runAVMPSignSchedule(context.getApplicationContext(), str + "|startLVMTrack");
    }

    public static int isForeground(Context context) {
        int i;
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY);
            if (Build.VERSION.SDK_INT < 21) {
                return activityManager.getRunningTasks(1).get(0).topActivity.getPackageName().equals(context.getPackageName()) ? 1 : 0;
            }
            int i2 = 0;
            for (ActivityManager.RunningAppProcessInfo next : activityManager.getRunningAppProcesses()) {
                try {
                    if (next.importance == 100) {
                        String[] strArr = next.pkgList;
                        int length = strArr.length;
                        i = i2;
                        int i3 = 0;
                        while (i3 < length) {
                            try {
                                if (strArr[i3].equals(context.getPackageName())) {
                                    i = 1;
                                }
                                i3++;
                            } catch (Exception e) {
                                return i;
                            }
                        }
                    } else {
                        i = i2;
                    }
                    i2 = i;
                } catch (Exception e2) {
                    return i2;
                }
            }
            return i2;
        } catch (Exception e3) {
            return 0;
        }
    }

    public static void mySleep(long j) {
        try {
            Thread.sleep(j);
        } catch (Exception e) {
        }
    }

    public static void runAVMPSignSchedule(Context context, String str) {
        for (int i = 0; i < 15; i++) {
            a("enter runAVMPSignSchedule");
            runAVMPSignSchedule1(context, str, "ib00000010b4732dc6645e87a20900b653a94ef27d72696f99", "ib0001002026f1091f2042df0cae1af323ac6e80a661d4a169");
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:68:0x02e4=Splitter:B:68:0x02e4, B:48:0x0205=Splitter:B:48:0x0205, B:117:0x0576=Splitter:B:117:0x0576, B:100:0x049a=Splitter:B:100:0x049a, B:30:0x012b=Splitter:B:30:0x012b, B:81:0x03bb=Splitter:B:81:0x03bb, B:130:0x064d=Splitter:B:130:0x064d, B:16:0x003f=Splitter:B:16:0x003f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void runAVMPSignSchedule1(android.content.Context r15, java.lang.String r16, java.lang.String r17, java.lang.String r18) {
        /*
            java.lang.Class<com.alibaba.wireless.security.open.avmpTest.AVMPUTTest> r12 = com.alibaba.wireless.security.open.avmpTest.AVMPUTTest.class
            monitor-enter(r12)
            a((android.content.Context) r15)     // Catch:{ all -> 0x02bb }
            r0 = 3000(0xbb8, double:1.482E-320)
            mySleep(r0)     // Catch:{ all -> 0x02bb }
            r1 = 0
            java.lang.String r6 = ""
            r3 = 0
            r2 = 0
            int r7 = isForeground(r15)     // Catch:{ all -> 0x02bb }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x02bb }
            r0 = 4
            byte[] r0 = new byte[r0]     // Catch:{ SecException -> 0x01e7, Exception -> 0x02c4 }
            com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent$IAVMPGenericInstance r8 = b     // Catch:{ SecException -> 0x01e7, Exception -> 0x02c4 }
            java.lang.String r9 = "ib00000010b4732dc6645e87a20900b653a94ef27d72696f99"
            r10 = 0
            byte[] r3 = avmpSign(r8, r0, r9, r10)     // Catch:{ SecException -> 0x01e7, Exception -> 0x02c4 }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x02bb }
            long r4 = r8 - r4
            int r10 = isForeground(r15)     // Catch:{ all -> 0x02bb }
            r0 = 0
            java.lang.String r8 = new java.lang.String     // Catch:{ Exception -> 0x01e1 }
            r8.<init>(r3)     // Catch:{ Exception -> 0x01e1 }
            if (r8 == 0) goto L_0x003d
            int r0 = r8.length()     // Catch:{ Exception -> 0x01e1 }
            r2 = 1
        L_0x003d:
            r9 = r0
            r8 = r2
        L_0x003f:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r0.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = "enter test1: isForeground1="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r7)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " isForeground11="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " msg"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r6)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " callRes1"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r8)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " signLength="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r9)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " duration="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r4)     // Catch:{ all -> 0x02bb }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x02bb }
            a((java.lang.String) r0)     // Catch:{ all -> 0x02bb }
            java.lang.String r0 = "100087"
            r2 = 1
            java.lang.String r3 = ""
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r11.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r13 = ""
            java.lang.StringBuilder r11 = r11.append(r13)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r7 = r11.append(r7)     // Catch:{ all -> 0x02bb }
            java.lang.String r11 = ""
            java.lang.StringBuilder r7 = r7.append(r11)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r7 = r7.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.String r10 = "&"
            java.lang.StringBuilder r7 = r7.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r6 = r7.append(r6)     // Catch:{ all -> 0x02bb }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x02bb }
            java.lang.String r7 = "1"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r10.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r11 = ""
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x02bb }
            java.lang.String r8 = java.lang.String.valueOf(r8)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r8 = r10.append(r8)     // Catch:{ all -> 0x02bb }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r10.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r11 = ""
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r9 = r10.append(r9)     // Catch:{ all -> 0x02bb }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x02bb }
            java.lang.String r10 = ""
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r0, r1, r2, r3, r4, r6, r7, r8, r9, r10)     // Catch:{ all -> 0x02bb }
        L_0x00f3:
            r0 = 3000(0xbb8, double:1.482E-320)
            mySleep(r0)     // Catch:{ all -> 0x02bb }
            r1 = 0
            java.lang.String r6 = ""
            r3 = 0
            r2 = 0
            int r7 = isForeground(r15)     // Catch:{ all -> 0x02bb }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x02bb }
            com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent$IAVMPGenericInstance r0 = b     // Catch:{ SecException -> 0x047c, Exception -> 0x0556 }
            java.lang.String r8 = "ib00000010b4732dc6645e87a20900b653a94ef27d72696f99"
            java.lang.String r9 = "vs_config_0"
            byte[] r3 = avmpSign2(r0, r8, r9)     // Catch:{ SecException -> 0x047c, Exception -> 0x0556 }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x02bb }
            long r4 = r8 - r4
            int r10 = isForeground(r15)     // Catch:{ all -> 0x02bb }
            r0 = 0
            java.lang.String r8 = new java.lang.String     // Catch:{ Exception -> 0x0476 }
            r8.<init>(r3)     // Catch:{ Exception -> 0x0476 }
            if (r8 == 0) goto L_0x0129
            int r0 = r8.length()     // Catch:{ Exception -> 0x0476 }
            r2 = 1
        L_0x0129:
            r9 = r0
            r8 = r2
        L_0x012b:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r0.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = "enter test2: isForeground1="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r7)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " isForeground11="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " msg"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r6)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " callRes1"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r8)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " signLength="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r9)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " dration="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r4)     // Catch:{ all -> 0x02bb }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x02bb }
            a((java.lang.String) r0)     // Catch:{ all -> 0x02bb }
            java.lang.String r0 = "100087"
            r2 = 1
            java.lang.String r3 = ""
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r11.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r13 = ""
            java.lang.StringBuilder r11 = r11.append(r13)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r7 = r11.append(r7)     // Catch:{ all -> 0x02bb }
            java.lang.String r11 = ""
            java.lang.StringBuilder r7 = r7.append(r11)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r7 = r7.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.String r10 = "&"
            java.lang.StringBuilder r7 = r7.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r6 = r7.append(r6)     // Catch:{ all -> 0x02bb }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x02bb }
            java.lang.String r7 = "3"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r10.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r11 = ""
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x02bb }
            java.lang.String r8 = java.lang.String.valueOf(r8)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r8 = r10.append(r8)     // Catch:{ all -> 0x02bb }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r10.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r11 = ""
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r9 = r10.append(r9)     // Catch:{ all -> 0x02bb }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x02bb }
            java.lang.String r10 = ""
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r0, r1, r2, r3, r4, r6, r7, r8, r9, r10)     // Catch:{ all -> 0x02bb }
        L_0x01df:
            monitor-exit(r12)
            return
        L_0x01e1:
            r2 = move-exception
            r2 = 0
            r9 = r0
            r8 = r2
            goto L_0x003f
        L_0x01e7:
            r0 = move-exception
            int r1 = r0.getErrorCode()     // Catch:{ all -> 0x03a0 }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x02bb }
            long r4 = r8 - r4
            int r10 = isForeground(r15)     // Catch:{ all -> 0x02bb }
            r0 = 0
            java.lang.String r8 = new java.lang.String     // Catch:{ Exception -> 0x02be }
            r8.<init>(r3)     // Catch:{ Exception -> 0x02be }
            if (r8 == 0) goto L_0x0203
            int r0 = r8.length()     // Catch:{ Exception -> 0x02be }
            r2 = 1
        L_0x0203:
            r9 = r0
            r8 = r2
        L_0x0205:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r0.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = "enter test1: isForeground1="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r7)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " isForeground11="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " msg"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r6)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " callRes1"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r8)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " signLength="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r9)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " duration="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r4)     // Catch:{ all -> 0x02bb }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x02bb }
            a((java.lang.String) r0)     // Catch:{ all -> 0x02bb }
            java.lang.String r0 = "100087"
            r2 = 1
            java.lang.String r3 = ""
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r11.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r13 = ""
            java.lang.StringBuilder r11 = r11.append(r13)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r7 = r11.append(r7)     // Catch:{ all -> 0x02bb }
            java.lang.String r11 = ""
            java.lang.StringBuilder r7 = r7.append(r11)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r7 = r7.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.String r10 = "&"
            java.lang.StringBuilder r7 = r7.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r6 = r7.append(r6)     // Catch:{ all -> 0x02bb }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x02bb }
            java.lang.String r7 = "1"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r10.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r11 = ""
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x02bb }
            java.lang.String r8 = java.lang.String.valueOf(r8)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r8 = r10.append(r8)     // Catch:{ all -> 0x02bb }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r10.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r11 = ""
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r9 = r10.append(r9)     // Catch:{ all -> 0x02bb }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x02bb }
            java.lang.String r10 = ""
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r0, r1, r2, r3, r4, r6, r7, r8, r9, r10)     // Catch:{ all -> 0x02bb }
            goto L_0x00f3
        L_0x02bb:
            r0 = move-exception
            monitor-exit(r12)
            throw r0
        L_0x02be:
            r2 = move-exception
            r2 = 0
            r9 = r0
            r8 = r2
            goto L_0x0205
        L_0x02c4:
            r0 = move-exception
            r1 = 1999(0x7cf, float:2.801E-42)
            java.lang.String r6 = r0.getMessage()     // Catch:{ all -> 0x03a0 }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x02bb }
            long r4 = r8 - r4
            int r10 = isForeground(r15)     // Catch:{ all -> 0x02bb }
            r0 = 0
            java.lang.String r8 = new java.lang.String     // Catch:{ Exception -> 0x039a }
            r8.<init>(r3)     // Catch:{ Exception -> 0x039a }
            if (r8 == 0) goto L_0x02e2
            int r0 = r8.length()     // Catch:{ Exception -> 0x039a }
            r2 = 1
        L_0x02e2:
            r9 = r0
            r8 = r2
        L_0x02e4:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r0.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = "enter test1: isForeground1="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r7)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " isForeground11="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " msg"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r6)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " callRes1"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r8)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " signLength="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r9)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " duration="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r4)     // Catch:{ all -> 0x02bb }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x02bb }
            a((java.lang.String) r0)     // Catch:{ all -> 0x02bb }
            java.lang.String r0 = "100087"
            r2 = 1
            java.lang.String r3 = ""
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r11.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r13 = ""
            java.lang.StringBuilder r11 = r11.append(r13)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r7 = r11.append(r7)     // Catch:{ all -> 0x02bb }
            java.lang.String r11 = ""
            java.lang.StringBuilder r7 = r7.append(r11)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r7 = r7.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.String r10 = "&"
            java.lang.StringBuilder r7 = r7.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r6 = r7.append(r6)     // Catch:{ all -> 0x02bb }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x02bb }
            java.lang.String r7 = "1"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r10.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r11 = ""
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x02bb }
            java.lang.String r8 = java.lang.String.valueOf(r8)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r8 = r10.append(r8)     // Catch:{ all -> 0x02bb }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r10.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r11 = ""
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r9 = r10.append(r9)     // Catch:{ all -> 0x02bb }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x02bb }
            java.lang.String r10 = ""
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r0, r1, r2, r3, r4, r6, r7, r8, r9, r10)     // Catch:{ all -> 0x02bb }
            goto L_0x00f3
        L_0x039a:
            r2 = move-exception
            r2 = 0
            r9 = r0
            r8 = r2
            goto L_0x02e4
        L_0x03a0:
            r0 = move-exception
            r11 = r0
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x02bb }
            long r4 = r8 - r4
            int r10 = isForeground(r15)     // Catch:{ all -> 0x02bb }
            r0 = 0
            java.lang.String r8 = new java.lang.String     // Catch:{ Exception -> 0x0470 }
            r8.<init>(r3)     // Catch:{ Exception -> 0x0470 }
            if (r8 == 0) goto L_0x03b9
            int r0 = r8.length()     // Catch:{ Exception -> 0x0470 }
            r2 = 1
        L_0x03b9:
            r9 = r0
            r8 = r2
        L_0x03bb:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r0.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = "enter test1: isForeground1="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r7)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " isForeground11="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " msg"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r6)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " callRes1"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r8)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " signLength="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r9)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " duration="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r4)     // Catch:{ all -> 0x02bb }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x02bb }
            a((java.lang.String) r0)     // Catch:{ all -> 0x02bb }
            java.lang.String r0 = "100087"
            r2 = 1
            java.lang.String r3 = ""
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r13.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r14 = ""
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r7 = r13.append(r7)     // Catch:{ all -> 0x02bb }
            java.lang.String r13 = ""
            java.lang.StringBuilder r7 = r7.append(r13)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r7 = r7.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.String r10 = "&"
            java.lang.StringBuilder r7 = r7.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r6 = r7.append(r6)     // Catch:{ all -> 0x02bb }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x02bb }
            java.lang.String r7 = "1"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r10.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r13 = ""
            java.lang.StringBuilder r10 = r10.append(r13)     // Catch:{ all -> 0x02bb }
            java.lang.String r8 = java.lang.String.valueOf(r8)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r8 = r10.append(r8)     // Catch:{ all -> 0x02bb }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r10.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r13 = ""
            java.lang.StringBuilder r10 = r10.append(r13)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r9 = r10.append(r9)     // Catch:{ all -> 0x02bb }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x02bb }
            java.lang.String r10 = ""
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r0, r1, r2, r3, r4, r6, r7, r8, r9, r10)     // Catch:{ all -> 0x02bb }
            throw r11     // Catch:{ all -> 0x02bb }
        L_0x0470:
            r2 = move-exception
            r2 = 0
            r9 = r0
            r8 = r2
            goto L_0x03bb
        L_0x0476:
            r2 = move-exception
            r2 = 0
            r9 = r0
            r8 = r2
            goto L_0x012b
        L_0x047c:
            r0 = move-exception
            int r1 = r0.getErrorCode()     // Catch:{ all -> 0x0632 }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x02bb }
            long r4 = r8 - r4
            int r10 = isForeground(r15)     // Catch:{ all -> 0x02bb }
            r0 = 0
            java.lang.String r8 = new java.lang.String     // Catch:{ Exception -> 0x0550 }
            r8.<init>(r3)     // Catch:{ Exception -> 0x0550 }
            if (r8 == 0) goto L_0x0498
            int r0 = r8.length()     // Catch:{ Exception -> 0x0550 }
            r2 = 1
        L_0x0498:
            r9 = r0
            r8 = r2
        L_0x049a:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r0.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = "enter test2: isForeground1="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r7)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " isForeground11="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " msg"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r6)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " callRes1"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r8)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " signLength="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r9)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " dration="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r4)     // Catch:{ all -> 0x02bb }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x02bb }
            a((java.lang.String) r0)     // Catch:{ all -> 0x02bb }
            java.lang.String r0 = "100087"
            r2 = 1
            java.lang.String r3 = ""
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r11.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r13 = ""
            java.lang.StringBuilder r11 = r11.append(r13)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r7 = r11.append(r7)     // Catch:{ all -> 0x02bb }
            java.lang.String r11 = ""
            java.lang.StringBuilder r7 = r7.append(r11)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r7 = r7.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.String r10 = "&"
            java.lang.StringBuilder r7 = r7.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r6 = r7.append(r6)     // Catch:{ all -> 0x02bb }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x02bb }
            java.lang.String r7 = "3"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r10.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r11 = ""
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x02bb }
            java.lang.String r8 = java.lang.String.valueOf(r8)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r8 = r10.append(r8)     // Catch:{ all -> 0x02bb }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r10.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r11 = ""
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r9 = r10.append(r9)     // Catch:{ all -> 0x02bb }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x02bb }
            java.lang.String r10 = ""
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r0, r1, r2, r3, r4, r6, r7, r8, r9, r10)     // Catch:{ all -> 0x02bb }
            goto L_0x01df
        L_0x0550:
            r2 = move-exception
            r2 = 0
            r9 = r0
            r8 = r2
            goto L_0x049a
        L_0x0556:
            r0 = move-exception
            r1 = 1999(0x7cf, float:2.801E-42)
            java.lang.String r6 = r0.getMessage()     // Catch:{ all -> 0x0632 }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x02bb }
            long r4 = r8 - r4
            int r10 = isForeground(r15)     // Catch:{ all -> 0x02bb }
            r0 = 0
            java.lang.String r8 = new java.lang.String     // Catch:{ Exception -> 0x062c }
            r8.<init>(r3)     // Catch:{ Exception -> 0x062c }
            if (r8 == 0) goto L_0x0574
            int r0 = r8.length()     // Catch:{ Exception -> 0x062c }
            r2 = 1
        L_0x0574:
            r9 = r0
            r8 = r2
        L_0x0576:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r0.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = "enter test2: isForeground1="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r7)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " isForeground11="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " msg"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r6)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " callRes1"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r8)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " signLength="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r9)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " dration="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r4)     // Catch:{ all -> 0x02bb }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x02bb }
            a((java.lang.String) r0)     // Catch:{ all -> 0x02bb }
            java.lang.String r0 = "100087"
            r2 = 1
            java.lang.String r3 = ""
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r11.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r13 = ""
            java.lang.StringBuilder r11 = r11.append(r13)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r7 = r11.append(r7)     // Catch:{ all -> 0x02bb }
            java.lang.String r11 = ""
            java.lang.StringBuilder r7 = r7.append(r11)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r7 = r7.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.String r10 = "&"
            java.lang.StringBuilder r7 = r7.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r6 = r7.append(r6)     // Catch:{ all -> 0x02bb }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x02bb }
            java.lang.String r7 = "3"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r10.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r11 = ""
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x02bb }
            java.lang.String r8 = java.lang.String.valueOf(r8)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r8 = r10.append(r8)     // Catch:{ all -> 0x02bb }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r10.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r11 = ""
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r9 = r10.append(r9)     // Catch:{ all -> 0x02bb }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x02bb }
            java.lang.String r10 = ""
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r0, r1, r2, r3, r4, r6, r7, r8, r9, r10)     // Catch:{ all -> 0x02bb }
            goto L_0x01df
        L_0x062c:
            r2 = move-exception
            r2 = 0
            r9 = r0
            r8 = r2
            goto L_0x0576
        L_0x0632:
            r0 = move-exception
            r11 = r0
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x02bb }
            long r4 = r8 - r4
            int r10 = isForeground(r15)     // Catch:{ all -> 0x02bb }
            r0 = 0
            java.lang.String r8 = new java.lang.String     // Catch:{ Exception -> 0x0702 }
            r8.<init>(r3)     // Catch:{ Exception -> 0x0702 }
            if (r8 == 0) goto L_0x064b
            int r0 = r8.length()     // Catch:{ Exception -> 0x0702 }
            r2 = 1
        L_0x064b:
            r9 = r0
            r8 = r2
        L_0x064d:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r0.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = "enter test2: isForeground1="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r7)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " isForeground11="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " msg"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r6)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " callRes1"
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r8)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " signLength="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r9)     // Catch:{ all -> 0x02bb }
            java.lang.String r2 = " dration="
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r0 = r0.append(r4)     // Catch:{ all -> 0x02bb }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x02bb }
            a((java.lang.String) r0)     // Catch:{ all -> 0x02bb }
            java.lang.String r0 = "100087"
            r2 = 1
            java.lang.String r3 = ""
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r13.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r14 = ""
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r7 = r13.append(r7)     // Catch:{ all -> 0x02bb }
            java.lang.String r13 = ""
            java.lang.StringBuilder r7 = r7.append(r13)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r7 = r7.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.String r10 = "&"
            java.lang.StringBuilder r7 = r7.append(r10)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r6 = r7.append(r6)     // Catch:{ all -> 0x02bb }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x02bb }
            java.lang.String r7 = "3"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r10.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r13 = ""
            java.lang.StringBuilder r10 = r10.append(r13)     // Catch:{ all -> 0x02bb }
            java.lang.String r8 = java.lang.String.valueOf(r8)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r8 = r10.append(r8)     // Catch:{ all -> 0x02bb }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x02bb }
            r10.<init>()     // Catch:{ all -> 0x02bb }
            java.lang.String r13 = ""
            java.lang.StringBuilder r10 = r10.append(r13)     // Catch:{ all -> 0x02bb }
            java.lang.StringBuilder r9 = r10.append(r9)     // Catch:{ all -> 0x02bb }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x02bb }
            java.lang.String r10 = ""
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r0, r1, r2, r3, r4, r6, r7, r8, r9, r10)     // Catch:{ all -> 0x02bb }
            throw r11     // Catch:{ all -> 0x02bb }
        L_0x0702:
            r2 = move-exception
            r2 = 0
            r9 = r0
            r8 = r2
            goto L_0x064d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.open.avmpTest.AVMPUTTest.runAVMPSignSchedule1(android.content.Context, java.lang.String, java.lang.String, java.lang.String):void");
    }
}
