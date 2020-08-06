package mtopsdk.common.util;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IInterface;
import android.text.TextUtils;
import mtopsdk.common.util.TBSdkLog;

public abstract class AsyncServiceBinder<T extends IInterface> {
    static final String TAG = "mtopsdk.AsyncServiceBinder";
    private ServiceConnection conn = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            synchronized (AsyncServiceBinder.this.lock) {
                try {
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
                        if (TextUtils.isEmpty(AsyncServiceBinder.this.interfaceName)) {
                            AsyncServiceBinder.this.interfaceName = AsyncServiceBinder.this.interfaceCls.getSimpleName();
                        }
                        TBSdkLog.w(AsyncServiceBinder.TAG, "[onServiceDisconnected] Service disconnected called,interfaceName=" + AsyncServiceBinder.this.interfaceName);
                    }
                } catch (Exception e) {
                }
                AsyncServiceBinder.this.service = null;
                AsyncServiceBinder.this.mBinding = false;
            }
        }

        public void onServiceConnected(ComponentName name, IBinder s) {
            synchronized (AsyncServiceBinder.this.lock) {
                try {
                    if (TextUtils.isEmpty(AsyncServiceBinder.this.interfaceName)) {
                        AsyncServiceBinder.this.interfaceName = AsyncServiceBinder.this.interfaceCls.getSimpleName();
                    }
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.i(AsyncServiceBinder.TAG, "[onServiceConnected] Service connected called. interfaceName =" + AsyncServiceBinder.this.interfaceName);
                    }
                    for (Class<?> cls : AsyncServiceBinder.this.interfaceCls.getDeclaredClasses()) {
                        if (cls.getSimpleName().equals("Stub")) {
                            AsyncServiceBinder.this.service = (IInterface) cls.getDeclaredMethod("asInterface", new Class[]{IBinder.class}).invoke(cls, new Object[]{s});
                        }
                    }
                } catch (Exception e) {
                    AsyncServiceBinder.this.mBindFailed = true;
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
                        TBSdkLog.w(AsyncServiceBinder.TAG, "[onServiceConnected] Service bind failed. mBindFailed=" + AsyncServiceBinder.this.mBindFailed + ",interfaceName=" + AsyncServiceBinder.this.interfaceName);
                    }
                }
                if (AsyncServiceBinder.this.service != null) {
                    AsyncServiceBinder.this.mBindFailed = false;
                    AsyncServiceBinder.this.afterAsyncBind();
                }
                AsyncServiceBinder.this.mBinding = false;
            }
        }
    };
    Class<? extends IInterface> interfaceCls;
    String interfaceName;
    final byte[] lock = new byte[0];
    volatile boolean mBindFailed = false;
    volatile boolean mBinding = false;
    protected volatile T service = null;
    Class<? extends Service> serviceCls;

    /* access modifiers changed from: protected */
    public abstract void afterAsyncBind();

    public AsyncServiceBinder(Class<? extends IInterface> interfaceCls2, Class<? extends Service> serviceCls2) {
        this.interfaceCls = interfaceCls2;
        this.serviceCls = serviceCls2;
    }

    @TargetApi(4)
    public void asyncBind(Context context) {
        boolean z;
        if (this.service == null && context != null && !this.mBindFailed && !this.mBinding) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[asyncBind] mContext=" + context + ",mBindFailed= " + this.mBindFailed + ",mBinding=" + this.mBinding);
            }
            this.mBinding = true;
            try {
                if (TextUtils.isEmpty(this.interfaceName)) {
                    this.interfaceName = this.interfaceCls.getSimpleName();
                }
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, "[asyncBind]try to bind service for " + this.interfaceName);
                }
                Intent intent = new Intent(context.getApplicationContext(), this.serviceCls);
                intent.setAction(this.interfaceCls.getName());
                intent.setPackage(context.getPackageName());
                intent.addCategory("android.intent.category.DEFAULT");
                boolean ret = context.bindService(intent, this.conn, 1);
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, "[asyncBind]use intent bind service ret=" + ret + " for interfaceName=" + this.interfaceName);
                }
                if (!ret) {
                    z = true;
                } else {
                    z = false;
                }
                this.mBindFailed = z;
            } catch (Throwable e) {
                this.mBindFailed = true;
                TBSdkLog.e(TAG, "[asyncBind] use intent bind service failed. mBindFailed=" + this.mBindFailed + ",interfaceName = " + this.interfaceName, e);
            }
            if (this.mBindFailed) {
                this.mBinding = false;
            }
        }
    }

    public T getService() {
        return this.service;
    }
}
