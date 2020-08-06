package com.yunos.tv.alitvasr.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.util.Log;
import com.yunos.tv.alitvasrsdk.CommonData;
import java.util.Map;

public abstract class AbstractClientManager<T extends IInterface> implements ServiceConnection {
    public static final long BIND_SERVICE_CHECK_TIMEOUT = 100;
    public static final long BIND_SERVICE_TIMEOUT = 2000;
    private String mAction;
    protected Context mContext;
    private Map<Integer, IInterface> mInterfaces;
    private volatile boolean mIsBinding = false;
    private final Object mLock = new Object();
    private String mPakcageName;
    private volatile T mService;
    private final Object mSyn = new Object();

    /* access modifiers changed from: protected */
    public abstract T asInterface(IBinder iBinder);

    public AbstractClientManager(Context context, String action, String packageName) {
        this.mContext = context.getApplicationContext();
        this.mAction = action;
        this.mPakcageName = packageName;
        bindService();
    }

    /* access modifiers changed from: protected */
    public T getService() {
        T service = this.mService;
        if (service == null || !service.asBinder().isBinderAlive()) {
            checkService();
        }
        return service;
    }

    private void notifyLock() {
        try {
            this.mLock.notifyAll();
        } catch (Throwable th) {
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void bindService() {
        /*
            r12 = this;
            r10 = 100
            java.lang.Object r6 = r12.mLock
            monitor-enter(r6)
            T r2 = r12.mService     // Catch:{ all -> 0x00a8 }
            if (r2 == 0) goto L_0x0015
            android.os.IBinder r3 = r2.asBinder()     // Catch:{ all -> 0x00a8 }
            boolean r3 = r3.isBinderAlive()     // Catch:{ all -> 0x00a8 }
            if (r3 == 0) goto L_0x0015
            monitor-exit(r6)     // Catch:{ all -> 0x00a8 }
        L_0x0014:
            return
        L_0x0015:
            r3 = 0
            r12.mService = r3     // Catch:{ all -> 0x00a8 }
            r3 = 1
            r12.mIsBinding = r3     // Catch:{ all -> 0x00a8 }
            android.content.Intent r1 = new android.content.Intent     // Catch:{ Throwable -> 0x00ab }
            r1.<init>()     // Catch:{ Throwable -> 0x00ab }
            java.lang.String r3 = r12.mAction     // Catch:{ Throwable -> 0x00ab }
            r1.setAction(r3)     // Catch:{ Throwable -> 0x00ab }
            java.lang.String r3 = r12.mPakcageName     // Catch:{ Throwable -> 0x00ab }
            boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x00ab }
            if (r3 != 0) goto L_0x0032
            java.lang.String r3 = r12.mPakcageName     // Catch:{ Throwable -> 0x00ab }
            r1.setPackage(r3)     // Catch:{ Throwable -> 0x00ab }
        L_0x0032:
            android.content.Context r3 = r12.mContext     // Catch:{ Throwable -> 0x00ab }
            r7 = 1
            boolean r3 = r3.bindService(r1, r12, r7)     // Catch:{ Throwable -> 0x00ab }
            if (r3 == 0) goto L_0x0079
            java.lang.String r3 = "AliTVASRSdk"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00ab }
            r7.<init>()     // Catch:{ Throwable -> 0x00ab }
            java.lang.String r8 = "bindService starting: action = "
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Throwable -> 0x00ab }
            java.lang.String r8 = r12.mAction     // Catch:{ Throwable -> 0x00ab }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Throwable -> 0x00ab }
            java.lang.String r8 = " packageName = "
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Throwable -> 0x00ab }
            java.lang.String r8 = r12.mPakcageName     // Catch:{ Throwable -> 0x00ab }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Throwable -> 0x00ab }
            java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x00ab }
            android.util.Log.d(r3, r7)     // Catch:{ Throwable -> 0x00ab }
            r4 = 2000(0x7d0, double:9.88E-321)
        L_0x0066:
            r8 = 0
            int r3 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r3 < 0) goto L_0x00a2
            T r3 = r12.mService     // Catch:{ Throwable -> 0x00ab }
            if (r3 != 0) goto L_0x00a2
            java.lang.Object r3 = r12.mLock     // Catch:{ Throwable -> 0x00ab }
            r8 = 100
            r3.wait(r8)     // Catch:{ Throwable -> 0x00ab }
            long r4 = r4 - r10
            goto L_0x0066
        L_0x0079:
            java.lang.String r3 = "AliTVASRSdk"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00ab }
            r7.<init>()     // Catch:{ Throwable -> 0x00ab }
            java.lang.String r8 = "bindService error: action = "
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Throwable -> 0x00ab }
            java.lang.String r8 = r12.mAction     // Catch:{ Throwable -> 0x00ab }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Throwable -> 0x00ab }
            java.lang.String r8 = " packageName = "
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Throwable -> 0x00ab }
            java.lang.String r8 = r12.mPakcageName     // Catch:{ Throwable -> 0x00ab }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Throwable -> 0x00ab }
            java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x00ab }
            android.util.Log.e(r3, r7)     // Catch:{ Throwable -> 0x00ab }
        L_0x00a2:
            r3 = 0
            r12.mIsBinding = r3     // Catch:{ all -> 0x00a8 }
        L_0x00a5:
            monitor-exit(r6)     // Catch:{ all -> 0x00a8 }
            goto L_0x0014
        L_0x00a8:
            r3 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x00a8 }
            throw r3
        L_0x00ab:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x00b3 }
            r3 = 0
            r12.mIsBinding = r3     // Catch:{ all -> 0x00a8 }
            goto L_0x00a5
        L_0x00b3:
            r3 = move-exception
            r7 = 0
            r12.mIsBinding = r7     // Catch:{ all -> 0x00a8 }
            throw r3     // Catch:{ all -> 0x00a8 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.alitvasr.sdk.AbstractClientManager.bindService():void");
    }

    private void checkService() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            bindService();
        } else if (!this.mIsBinding) {
            new Thread(new Runnable() {
                public void run() {
                    AbstractClientManager.this.bindService();
                }
            }).start();
        }
    }

    public void onServiceConnected(ComponentName name, IBinder service) {
        this.mService = asInterface(service);
        Log.d(CommonData.TAG, "onServiceConnected: action = " + this.mAction + " packageName = " + this.mPakcageName);
        notifyLock();
    }

    public void onServiceDisconnected(ComponentName name) {
        this.mService = null;
        Log.e(CommonData.TAG, "onServiceDisconnected: action = " + this.mAction + " packageName = " + this.mPakcageName);
        checkService();
    }

    public void onBindingDied(ComponentName name) {
        Log.e(CommonData.TAG, "onBindingDied: action = " + this.mAction + " packageName = " + this.mPakcageName);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0044, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0045, code lost:
        r3.printStackTrace();
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <I extends android.os.IInterface> I getInterface(int r8) {
        /*
            r7 = this;
            android.os.IInterface r2 = r7.getService()
            r1 = 0
            if (r2 == 0) goto L_0x0043
            java.lang.Object r5 = r7.mSyn
            monitor-enter(r5)
            java.util.Map<java.lang.Integer, android.os.IInterface> r4 = r7.mInterfaces     // Catch:{ all -> 0x0049 }
            if (r4 == 0) goto L_0x001c
            java.util.Map<java.lang.Integer, android.os.IInterface> r4 = r7.mInterfaces     // Catch:{ all -> 0x0049 }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r8)     // Catch:{ all -> 0x0049 }
            java.lang.Object r4 = r4.get(r6)     // Catch:{ all -> 0x0049 }
            r0 = r4
            android.os.IInterface r0 = (android.os.IInterface) r0     // Catch:{ all -> 0x0049 }
            r1 = r0
        L_0x001c:
            if (r1 == 0) goto L_0x0028
            android.os.IBinder r4 = r1.asBinder()     // Catch:{ all -> 0x0049 }
            boolean r4 = r4.isBinderAlive()     // Catch:{ all -> 0x0049 }
            if (r4 != 0) goto L_0x0042
        L_0x0028:
            android.os.IInterface r1 = r7.getBinder(r2, r8)     // Catch:{ Throwable -> 0x0044 }
            if (r1 == 0) goto L_0x0042
            java.util.Map<java.lang.Integer, android.os.IInterface> r4 = r7.mInterfaces     // Catch:{ Throwable -> 0x0044 }
            if (r4 != 0) goto L_0x0039
            java.util.HashMap r4 = new java.util.HashMap     // Catch:{ Throwable -> 0x0044 }
            r4.<init>()     // Catch:{ Throwable -> 0x0044 }
            r7.mInterfaces = r4     // Catch:{ Throwable -> 0x0044 }
        L_0x0039:
            java.util.Map<java.lang.Integer, android.os.IInterface> r4 = r7.mInterfaces     // Catch:{ Throwable -> 0x0044 }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r8)     // Catch:{ Throwable -> 0x0044 }
            r4.put(r6, r1)     // Catch:{ Throwable -> 0x0044 }
        L_0x0042:
            monitor-exit(r5)     // Catch:{ all -> 0x0049 }
        L_0x0043:
            return r1
        L_0x0044:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x0049 }
            goto L_0x0042
        L_0x0049:
            r4 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0049 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.alitvasr.sdk.AbstractClientManager.getInterface(int):android.os.IInterface");
    }

    /* access modifiers changed from: protected */
    public IInterface getBinder(T t, int type) throws Throwable {
        return null;
    }
}
