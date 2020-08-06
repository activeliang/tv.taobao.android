package com.yunos.tv.tvsdk.exception;

import java.lang.Thread;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private OnCrashInterface onCrashInterface;

    public interface OnCrashInterface {
        void onCrash(String str);
    }

    public CrashHandler(OnCrashInterface onCrashInterface2) {
        this.onCrashInterface = onCrashInterface2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0033 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x004f A[SYNTHETIC, Splitter:B:29:0x004f] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0055 A[Catch:{ Exception -> 0x005a }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0062 A[SYNTHETIC, Splitter:B:38:0x0062] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0068 A[Catch:{ Exception -> 0x006d }] */
    /* JADX WARNING: Removed duplicated region for block: B:58:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void uncaughtException(java.lang.Thread r10, java.lang.Throwable r11) {
        /*
            r9 = this;
            r0 = 0
            r5 = 0
            r3 = 0
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0049 }
            r1.<init>()     // Catch:{ Exception -> 0x0049 }
            java.io.PrintStream r6 = new java.io.PrintStream     // Catch:{ Exception -> 0x0079, all -> 0x0072 }
            r6.<init>(r1)     // Catch:{ Exception -> 0x0079, all -> 0x0072 }
            r11.printStackTrace(r6)     // Catch:{ Exception -> 0x007c, all -> 0x0075 }
            java.lang.String r7 = "CrashHandler"
            java.lang.String r8 = "CrashHandler"
            android.util.Log.e(r7, r8, r11)     // Catch:{ Exception -> 0x007c, all -> 0x0075 }
            java.lang.String r4 = new java.lang.String     // Catch:{ Exception -> 0x007c, all -> 0x0075 }
            byte[] r7 = r1.toByteArray()     // Catch:{ Exception -> 0x007c, all -> 0x0075 }
            r4.<init>(r7)     // Catch:{ Exception -> 0x007c, all -> 0x0075 }
            if (r6 == 0) goto L_0x0084
            r6.close()     // Catch:{ Exception -> 0x0041 }
            r5 = 0
        L_0x0028:
            if (r1 == 0) goto L_0x0082
            r1.close()     // Catch:{ Exception -> 0x0080 }
            r0 = 0
        L_0x002e:
            r3 = r4
        L_0x002f:
            com.yunos.tv.tvsdk.exception.CrashHandler$OnCrashInterface r7 = r9.onCrashInterface
            if (r7 == 0) goto L_0x0040
            if (r3 == 0) goto L_0x0040
            boolean r7 = android.text.TextUtils.isEmpty(r3)
            if (r7 != 0) goto L_0x0040
            com.yunos.tv.tvsdk.exception.CrashHandler$OnCrashInterface r7 = r9.onCrashInterface
            r7.onCrash(r3)
        L_0x0040:
            return
        L_0x0041:
            r2 = move-exception
            r5 = r6
        L_0x0043:
            r2.printStackTrace()
            r3 = r4
            r0 = r1
            goto L_0x002f
        L_0x0049:
            r2 = move-exception
        L_0x004a:
            r2.printStackTrace()     // Catch:{ all -> 0x005f }
            if (r5 == 0) goto L_0x0053
            r5.close()     // Catch:{ Exception -> 0x005a }
            r5 = 0
        L_0x0053:
            if (r0 == 0) goto L_0x002f
            r0.close()     // Catch:{ Exception -> 0x005a }
            r0 = 0
            goto L_0x002f
        L_0x005a:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x002f
        L_0x005f:
            r7 = move-exception
        L_0x0060:
            if (r5 == 0) goto L_0x0066
            r5.close()     // Catch:{ Exception -> 0x006d }
            r5 = 0
        L_0x0066:
            if (r0 == 0) goto L_0x006c
            r0.close()     // Catch:{ Exception -> 0x006d }
            r0 = 0
        L_0x006c:
            throw r7
        L_0x006d:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x006c
        L_0x0072:
            r7 = move-exception
            r0 = r1
            goto L_0x0060
        L_0x0075:
            r7 = move-exception
            r5 = r6
            r0 = r1
            goto L_0x0060
        L_0x0079:
            r2 = move-exception
            r0 = r1
            goto L_0x004a
        L_0x007c:
            r2 = move-exception
            r5 = r6
            r0 = r1
            goto L_0x004a
        L_0x0080:
            r2 = move-exception
            goto L_0x0043
        L_0x0082:
            r0 = r1
            goto L_0x002e
        L_0x0084:
            r5 = r6
            goto L_0x0028
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.tvsdk.exception.CrashHandler.uncaughtException(java.lang.Thread, java.lang.Throwable):void");
    }
}
