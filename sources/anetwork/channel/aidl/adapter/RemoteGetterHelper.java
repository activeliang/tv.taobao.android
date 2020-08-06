package anetwork.channel.aidl.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import anet.channel.util.ALog;
import anetwork.channel.aidl.IRemoteNetworkGetter;
import anetwork.channel.aidl.NetworkService;
import java.util.concurrent.CountDownLatch;

public class RemoteGetterHelper {
    private static final String TAG = "anet.RemoteGetter";
    /* access modifiers changed from: private */
    public static volatile boolean bBindFailed = false;
    /* access modifiers changed from: private */
    public static volatile boolean bBinding = false;
    private static ServiceConnection conn = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            if (ALog.isPrintLog(2)) {
                ALog.i(RemoteGetterHelper.TAG, "ANet_Service Disconnected", (String) null, new Object[0]);
            }
            IRemoteNetworkGetter unused = RemoteGetterHelper.mGetter = null;
            boolean unused2 = RemoteGetterHelper.bBinding = false;
            if (RemoteGetterHelper.mServiceBindLock != null) {
                RemoteGetterHelper.mServiceBindLock.countDown();
            }
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            if (ALog.isPrintLog(2)) {
                ALog.i(RemoteGetterHelper.TAG, "[onServiceConnected]ANet_Service start success. ANet run with service mode", (String) null, new Object[0]);
            }
            synchronized (RemoteGetterHelper.class) {
                IRemoteNetworkGetter unused = RemoteGetterHelper.mGetter = IRemoteNetworkGetter.Stub.asInterface(service);
                if (RemoteGetterHelper.mServiceBindLock != null) {
                    RemoteGetterHelper.mServiceBindLock.countDown();
                }
            }
            boolean unused2 = RemoteGetterHelper.bBindFailed = false;
            boolean unused3 = RemoteGetterHelper.bBinding = false;
        }
    };
    private static Handler handler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public static volatile IRemoteNetworkGetter mGetter;
    /* access modifiers changed from: private */
    public static volatile CountDownLatch mServiceBindLock = null;

    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        anet.channel.util.ALog.i(TAG, "[initRemoteGetterAndWait]begin to wait 5s", (java.lang.String) null, new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0051, code lost:
        if (mServiceBindLock.await(5, java.util.concurrent.TimeUnit.SECONDS) == false) goto L_0x0061;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0053, code lost:
        anet.channel.util.ALog.i(TAG, "mServiceBindLock count down to 0", (java.lang.String) null, new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0061, code lost:
        anet.channel.util.ALog.i(TAG, "mServiceBindLock wait timeout", (java.lang.String) null, new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void initRemoteGetterAndWait(android.content.Context r7, boolean r8) {
        /*
            r6 = 0
            r5 = 0
            anetwork.channel.aidl.IRemoteNetworkGetter r1 = mGetter
            if (r1 == 0) goto L_0x0007
        L_0x0006:
            return
        L_0x0007:
            boolean r1 = bBindFailed
            if (r1 != 0) goto L_0x0006
            asyncBindService(r7)
            boolean r1 = bBindFailed
            if (r1 != 0) goto L_0x0006
            if (r8 == 0) goto L_0x0006
            java.lang.Class<anetwork.channel.aidl.adapter.RemoteGetterHelper> r2 = anetwork.channel.aidl.adapter.RemoteGetterHelper.class
            monitor-enter(r2)     // Catch:{ InterruptedException -> 0x0020 }
            anetwork.channel.aidl.IRemoteNetworkGetter r1 = mGetter     // Catch:{ all -> 0x001d }
            if (r1 == 0) goto L_0x002d
            monitor-exit(r2)     // Catch:{ all -> 0x001d }
            goto L_0x0006
        L_0x001d:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x001d }
            throw r1     // Catch:{ InterruptedException -> 0x0020 }
        L_0x0020:
            r0 = move-exception
            java.lang.String r1 = "anet.RemoteGetter"
            java.lang.String r2 = "mServiceBindLock wait interrupt"
            java.lang.Object[] r3 = new java.lang.Object[r5]
            anet.channel.util.ALog.e(r1, r2, r6, r3)
            goto L_0x0006
        L_0x002d:
            java.util.concurrent.CountDownLatch r1 = mServiceBindLock     // Catch:{ all -> 0x001d }
            if (r1 != 0) goto L_0x0039
            java.util.concurrent.CountDownLatch r1 = new java.util.concurrent.CountDownLatch     // Catch:{ all -> 0x001d }
            r3 = 1
            r1.<init>(r3)     // Catch:{ all -> 0x001d }
            mServiceBindLock = r1     // Catch:{ all -> 0x001d }
        L_0x0039:
            monitor-exit(r2)     // Catch:{ all -> 0x001d }
            java.lang.String r1 = "anet.RemoteGetter"
            java.lang.String r2 = "[initRemoteGetterAndWait]begin to wait 5s"
            r3 = 0
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ InterruptedException -> 0x0020 }
            anet.channel.util.ALog.i(r1, r2, r3, r4)     // Catch:{ InterruptedException -> 0x0020 }
            java.util.concurrent.CountDownLatch r1 = mServiceBindLock     // Catch:{ InterruptedException -> 0x0020 }
            r2 = 5
            java.util.concurrent.TimeUnit r4 = java.util.concurrent.TimeUnit.SECONDS     // Catch:{ InterruptedException -> 0x0020 }
            boolean r1 = r1.await(r2, r4)     // Catch:{ InterruptedException -> 0x0020 }
            if (r1 == 0) goto L_0x0061
            java.lang.String r1 = "anet.RemoteGetter"
            java.lang.String r2 = "mServiceBindLock count down to 0"
            r3 = 0
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ InterruptedException -> 0x0020 }
            anet.channel.util.ALog.i(r1, r2, r3, r4)     // Catch:{ InterruptedException -> 0x0020 }
            goto L_0x0006
        L_0x0061:
            java.lang.String r1 = "anet.RemoteGetter"
            java.lang.String r2 = "mServiceBindLock wait timeout"
            r3 = 0
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ InterruptedException -> 0x0020 }
            anet.channel.util.ALog.i(r1, r2, r3, r4)     // Catch:{ InterruptedException -> 0x0020 }
            goto L_0x0006
        */
        throw new UnsupportedOperationException("Method not decompiled: anetwork.channel.aidl.adapter.RemoteGetterHelper.initRemoteGetterAndWait(android.content.Context, boolean):void");
    }

    public static IRemoteNetworkGetter getRemoteGetter() {
        return mGetter;
    }

    private static void asyncBindService(Context mContext) {
        boolean z = true;
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "[asyncBindService] mContext:" + mContext + " bBindFailed:" + bBindFailed + " bBinding:" + bBinding, (String) null, new Object[0]);
        }
        if (mContext != null && !bBindFailed && !bBinding) {
            bBinding = true;
            Intent intent = new Intent(mContext, NetworkService.class);
            intent.setAction(IRemoteNetworkGetter.class.getName());
            intent.addCategory("android.intent.category.DEFAULT");
            if (mContext.bindService(intent, conn, 1)) {
                z = false;
            }
            bBindFailed = z;
            if (bBindFailed) {
                bBinding = false;
                ALog.w(TAG, "[asyncBindService]ANet_Service start not success. ANet run with local mode!", (String) null, new Object[0]);
            }
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (RemoteGetterHelper.bBinding) {
                        boolean unused = RemoteGetterHelper.bBinding = false;
                        ALog.w(RemoteGetterHelper.TAG, "binding service timeout. reset status!", (String) null, new Object[0]);
                    }
                }
            }, 10000);
        }
    }
}
