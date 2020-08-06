package anet.channel.util;

import android.content.Context;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.statist.StrategyStatObject;
import java.io.File;
import java.io.Serializable;

public class SerializeHelper {
    private static final String TAG = "awcn.SerializeHelper";
    private static File cacheDir = null;

    public static File getCacheFiles(String cacheFile) {
        Context context;
        if (cacheDir == null && (context = GlobalAppRuntimeInfo.getContext()) != null) {
            cacheDir = context.getCacheDir();
        }
        return new File(cacheDir, cacheFile);
    }

    public static synchronized void persist(Serializable s, File toFile) {
        synchronized (SerializeHelper.class) {
            persist(s, toFile, (StrategyStatObject) null);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0066 A[SYNTHETIC, Splitter:B:25:0x0066] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x007f  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0107 A[Catch:{ all -> 0x0119 }] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0111 A[SYNTHETIC, Splitter:B:53:0x0111] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x011c A[SYNTHETIC, Splitter:B:58:0x011c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void persist(java.io.Serializable r20, java.io.File r21, anet.channel.statist.StrategyStatObject r22) {
        /*
            java.lang.Class<anet.channel.util.SerializeHelper> r13 = anet.channel.util.SerializeHelper.class
            monitor-enter(r13)
            if (r20 == 0) goto L_0x0007
            if (r21 != 0) goto L_0x001d
        L_0x0007:
            java.lang.String r12 = "awcn.SerializeHelper"
            java.lang.String r14 = "persist fail. Invalid parameter"
            r15 = 0
            r16 = 0
            r0 = r16
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x00da }
            r16 = r0
            r0 = r16
            anet.channel.util.ALog.e(r12, r14, r15, r0)     // Catch:{ all -> 0x00da }
        L_0x001b:
            monitor-exit(r13)
            return
        L_0x001d:
            long r10 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00da }
            r3 = 0
            r4 = 0
            r6 = 0
            r9 = 0
            java.util.UUID r12 = java.util.UUID.randomUUID()     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r12 = r12.toString()     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r14 = "-"
            java.lang.String r15 = ""
            java.lang.String r12 = r12.replace(r14, r15)     // Catch:{ Exception -> 0x00e1 }
            java.io.File r3 = getCacheFiles(r12)     // Catch:{ Exception -> 0x00e1 }
            r3.createNewFile()     // Catch:{ Exception -> 0x00e1 }
            r12 = 1
            r3.setReadable(r12)     // Catch:{ Exception -> 0x00e1 }
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00e1 }
            r5.<init>(r3)     // Catch:{ Exception -> 0x00e1 }
            java.io.ObjectOutputStream r7 = new java.io.ObjectOutputStream     // Catch:{ Exception -> 0x015b, all -> 0x0154 }
            java.io.BufferedOutputStream r12 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x015b, all -> 0x0154 }
            r12.<init>(r5)     // Catch:{ Exception -> 0x015b, all -> 0x0154 }
            r7.<init>(r12)     // Catch:{ Exception -> 0x015b, all -> 0x0154 }
            r0 = r20
            r7.writeObject(r0)     // Catch:{ Exception -> 0x015e, all -> 0x0157 }
            r7.flush()     // Catch:{ Exception -> 0x015e, all -> 0x0157 }
            r7.close()     // Catch:{ Exception -> 0x015e, all -> 0x0157 }
            r9 = 1
            if (r5 == 0) goto L_0x0162
            r5.close()     // Catch:{ IOException -> 0x00dd }
            r6 = r7
            r4 = r5
        L_0x0064:
            if (r22 == 0) goto L_0x007d
            java.lang.String r12 = java.lang.String.valueOf(r3)     // Catch:{ all -> 0x00da }
            r0 = r22
            r0.writeTempFilePath = r12     // Catch:{ all -> 0x00da }
            java.lang.String r12 = java.lang.String.valueOf(r21)     // Catch:{ all -> 0x00da }
            r0 = r22
            r0.writeStrategyFilePath = r12     // Catch:{ all -> 0x00da }
            if (r9 == 0) goto L_0x0120
            r12 = 1
        L_0x0079:
            r0 = r22
            r0.isTempWriteSucceed = r12     // Catch:{ all -> 0x00da }
        L_0x007d:
            if (r9 == 0) goto L_0x001b
            r0 = r21
            boolean r8 = r3.renameTo(r0)     // Catch:{ all -> 0x00da }
            if (r8 == 0) goto L_0x0123
            java.lang.String r12 = "awcn.SerializeHelper"
            java.lang.String r14 = "persist end."
            r15 = 0
            r16 = 4
            r0 = r16
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x00da }
            r16 = r0
            r17 = 0
            java.lang.String r18 = "file"
            r16[r17] = r18     // Catch:{ all -> 0x00da }
            r17 = 1
            java.io.File r18 = r21.getAbsoluteFile()     // Catch:{ all -> 0x00da }
            r16[r17] = r18     // Catch:{ all -> 0x00da }
            r17 = 2
            java.lang.String r18 = "cost"
            r16[r17] = r18     // Catch:{ all -> 0x00da }
            r17 = 3
            long r18 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00da }
            long r18 = r18 - r10
            java.lang.Long r18 = java.lang.Long.valueOf(r18)     // Catch:{ all -> 0x00da }
            r16[r17] = r18     // Catch:{ all -> 0x00da }
            r0 = r16
            anet.channel.util.ALog.i(r12, r14, r15, r0)     // Catch:{ all -> 0x00da }
        L_0x00bf:
            if (r22 == 0) goto L_0x001b
            if (r8 == 0) goto L_0x014c
            r12 = 1
        L_0x00c4:
            r0 = r22
            r0.isRenameSucceed = r12     // Catch:{ all -> 0x00da }
            if (r8 == 0) goto L_0x014f
            r12 = 1
        L_0x00cb:
            r0 = r22
            r0.isSucceed = r12     // Catch:{ all -> 0x00da }
            anet.channel.appmonitor.IAppMonitor r12 = anet.channel.appmonitor.AppMonitor.getInstance()     // Catch:{ all -> 0x00da }
            r0 = r22
            r12.commitStat(r0)     // Catch:{ all -> 0x00da }
            goto L_0x001b
        L_0x00da:
            r12 = move-exception
            monitor-exit(r13)
            throw r12
        L_0x00dd:
            r12 = move-exception
            r6 = r7
            r4 = r5
            goto L_0x0064
        L_0x00e1:
            r2 = move-exception
        L_0x00e2:
            java.lang.String r12 = "awcn.SerializeHelper"
            java.lang.String r14 = "persist fail. "
            r15 = 0
            r16 = 2
            r0 = r16
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x0119 }
            r16 = r0
            r17 = 0
            java.lang.String r18 = "file"
            r16[r17] = r18     // Catch:{ all -> 0x0119 }
            r17 = 1
            java.lang.String r18 = r21.getName()     // Catch:{ all -> 0x0119 }
            r16[r17] = r18     // Catch:{ all -> 0x0119 }
            r0 = r16
            anet.channel.util.ALog.e(r12, r14, r15, r2, r0)     // Catch:{ all -> 0x0119 }
            if (r22 == 0) goto L_0x010f
            java.lang.String r12 = "SerializeHelper.persist()"
            r0 = r22
            r0.appendErrorTrace(r12, r2)     // Catch:{ all -> 0x0119 }
        L_0x010f:
            if (r4 == 0) goto L_0x0064
            r4.close()     // Catch:{ IOException -> 0x0116 }
            goto L_0x0064
        L_0x0116:
            r12 = move-exception
            goto L_0x0064
        L_0x0119:
            r12 = move-exception
        L_0x011a:
            if (r4 == 0) goto L_0x011f
            r4.close()     // Catch:{ IOException -> 0x0152 }
        L_0x011f:
            throw r12     // Catch:{ all -> 0x00da }
        L_0x0120:
            r12 = 0
            goto L_0x0079
        L_0x0123:
            java.lang.String r12 = "awcn.SerializeHelper"
            java.lang.String r14 = "rename failed."
            r15 = 0
            r16 = 0
            r0 = r16
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x00da }
            r16 = r0
            r0 = r16
            anet.channel.util.ALog.e(r12, r14, r15, r0)     // Catch:{ all -> 0x00da }
            anet.channel.appmonitor.IAppMonitor r12 = anet.channel.appmonitor.AppMonitor.getInstance()     // Catch:{ all -> 0x00da }
            anet.channel.statist.ExceptionStatistic r14 = new anet.channel.statist.ExceptionStatistic     // Catch:{ all -> 0x00da }
            r15 = -106(0xffffffffffffff96, float:NaN)
            r16 = 0
            java.lang.String r17 = "rt"
            r14.<init>(r15, r16, r17)     // Catch:{ all -> 0x00da }
            r12.commitStat(r14)     // Catch:{ all -> 0x00da }
            goto L_0x00bf
        L_0x014c:
            r12 = 0
            goto L_0x00c4
        L_0x014f:
            r12 = 0
            goto L_0x00cb
        L_0x0152:
            r14 = move-exception
            goto L_0x011f
        L_0x0154:
            r12 = move-exception
            r4 = r5
            goto L_0x011a
        L_0x0157:
            r12 = move-exception
            r6 = r7
            r4 = r5
            goto L_0x011a
        L_0x015b:
            r2 = move-exception
            r4 = r5
            goto L_0x00e2
        L_0x015e:
            r2 = move-exception
            r6 = r7
            r4 = r5
            goto L_0x00e2
        L_0x0162:
            r6 = r7
            r4 = r5
            goto L_0x0064
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.util.SerializeHelper.persist(java.io.Serializable, java.io.File, anet.channel.statist.StrategyStatObject):void");
    }

    public static synchronized <T> T restore(File file) {
        T restore;
        synchronized (SerializeHelper.class) {
            restore = restore(file, (StrategyStatObject) null);
        }
        return restore;
    }

    /* JADX WARNING: Removed duplicated region for block: B:48:0x007c A[Catch:{ all -> 0x009c }] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x008b A[Catch:{ all -> 0x009c }] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0093 A[SYNTHETIC, Splitter:B:52:0x0093] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x009f A[SYNTHETIC, Splitter:B:59:0x009f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized <T> T restore(java.io.File r14, anet.channel.statist.StrategyStatObject r15) {
        /*
            java.lang.Class<anet.channel.util.SerializeHelper> r8 = anet.channel.util.SerializeHelper.class
            monitor-enter(r8)
            r1 = 0
            r3 = 0
            r5 = 0
            if (r15 == 0) goto L_0x000e
            java.lang.String r7 = java.lang.String.valueOf(r14)     // Catch:{ all -> 0x0042 }
            r15.readStrategyFilePath = r7     // Catch:{ all -> 0x0042 }
        L_0x000e:
            boolean r7 = r14.exists()     // Catch:{ Throwable -> 0x0074 }
            if (r7 != 0) goto L_0x0045
            r7 = 3
            boolean r7 = anet.channel.util.ALog.isPrintLog(r7)     // Catch:{ Throwable -> 0x0074 }
            if (r7 == 0) goto L_0x0035
            java.lang.String r7 = "awcn.SerializeHelper"
            java.lang.String r9 = "file not exist."
            r10 = 0
            r11 = 2
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ Throwable -> 0x0074 }
            r12 = 0
            java.lang.String r13 = "file"
            r11[r12] = r13     // Catch:{ Throwable -> 0x0074 }
            r12 = 1
            java.lang.String r13 = r14.getName()     // Catch:{ Throwable -> 0x0074 }
            r11[r12] = r13     // Catch:{ Throwable -> 0x0074 }
            anet.channel.util.ALog.w(r7, r9, r10, r11)     // Catch:{ Throwable -> 0x0074 }
        L_0x0035:
            r5 = 0
            if (r1 == 0) goto L_0x003b
            r1.close()     // Catch:{ IOException -> 0x003d }
        L_0x003b:
            monitor-exit(r8)
            return r5
        L_0x003d:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x0042 }
            goto L_0x003b
        L_0x0042:
            r7 = move-exception
        L_0x0043:
            monitor-exit(r8)
            throw r7
        L_0x0045:
            if (r15 == 0) goto L_0x004a
            r7 = 1
            r15.isFileExists = r7     // Catch:{ Throwable -> 0x0074 }
        L_0x004a:
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x0074 }
            r2.<init>(r14)     // Catch:{ Throwable -> 0x0074 }
            java.io.ObjectInputStream r4 = new java.io.ObjectInputStream     // Catch:{ Throwable -> 0x00af, all -> 0x00a8 }
            java.io.BufferedInputStream r7 = new java.io.BufferedInputStream     // Catch:{ Throwable -> 0x00af, all -> 0x00a8 }
            r7.<init>(r2)     // Catch:{ Throwable -> 0x00af, all -> 0x00a8 }
            r4.<init>(r7)     // Catch:{ Throwable -> 0x00af, all -> 0x00a8 }
            java.lang.Object r5 = r4.readObject()     // Catch:{ Throwable -> 0x00b2, all -> 0x00ab }
            r4.close()     // Catch:{ Throwable -> 0x00b2, all -> 0x00ab }
            if (r15 == 0) goto L_0x0065
            r7 = 1
            r15.isReadObjectSucceed = r7     // Catch:{ Throwable -> 0x00b2, all -> 0x00ab }
        L_0x0065:
            if (r2 == 0) goto L_0x00ba
            r2.close()     // Catch:{ IOException -> 0x006d }
            r3 = r4
            r1 = r2
            goto L_0x003b
        L_0x006d:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x00b6 }
            r3 = r4
            r1 = r2
            goto L_0x003b
        L_0x0074:
            r6 = move-exception
        L_0x0075:
            r7 = 3
            boolean r7 = anet.channel.util.ALog.isPrintLog(r7)     // Catch:{ all -> 0x009c }
            if (r7 == 0) goto L_0x0089
            java.lang.String r7 = "awcn.SerializeHelper"
            java.lang.String r9 = "restore file fail."
            r10 = 0
            r11 = 0
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ all -> 0x009c }
            anet.channel.util.ALog.w(r7, r9, r10, r6, r11)     // Catch:{ all -> 0x009c }
        L_0x0089:
            if (r15 == 0) goto L_0x0091
            java.lang.String r7 = "SerializeHelper.restore()"
            r15.appendErrorTrace(r7, r6)     // Catch:{ all -> 0x009c }
        L_0x0091:
            if (r1 == 0) goto L_0x003b
            r1.close()     // Catch:{ IOException -> 0x0097 }
            goto L_0x003b
        L_0x0097:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x0042 }
            goto L_0x003b
        L_0x009c:
            r7 = move-exception
        L_0x009d:
            if (r1 == 0) goto L_0x00a2
            r1.close()     // Catch:{ IOException -> 0x00a3 }
        L_0x00a2:
            throw r7     // Catch:{ all -> 0x0042 }
        L_0x00a3:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x0042 }
            goto L_0x00a2
        L_0x00a8:
            r7 = move-exception
            r1 = r2
            goto L_0x009d
        L_0x00ab:
            r7 = move-exception
            r3 = r4
            r1 = r2
            goto L_0x009d
        L_0x00af:
            r6 = move-exception
            r1 = r2
            goto L_0x0075
        L_0x00b2:
            r6 = move-exception
            r3 = r4
            r1 = r2
            goto L_0x0075
        L_0x00b6:
            r7 = move-exception
            r3 = r4
            r1 = r2
            goto L_0x0043
        L_0x00ba:
            r3 = r4
            r1 = r2
            goto L_0x003b
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.util.SerializeHelper.restore(java.io.File, anet.channel.statist.StrategyStatObject):java.lang.Object");
    }
}
