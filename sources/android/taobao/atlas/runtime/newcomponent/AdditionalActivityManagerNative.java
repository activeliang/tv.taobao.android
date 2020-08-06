package android.taobao.atlas.runtime.newcomponent;

import android.app.IServiceConnection;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.ServiceInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.atlas.runtime.newcomponent.activity.ActivityBridge;
import android.taobao.atlas.runtime.newcomponent.service.IDelegateBinder;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class AdditionalActivityManagerNative {
    private static ConcurrentHashMap<String, AdditionalActivityManagerNative> sBridges = new ConcurrentHashMap<>();
    private static Handler sServicehandler = new Handler(shandlerThread.getLooper());
    private static HandlerThread shandlerThread = new HandlerThread("atlas_service_manager");
    HashMap<Intent, IServiceConnection> mActiveServiceInfo = new HashMap<>();
    /* access modifiers changed from: private */
    public IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
        public void binderDied() {
            Log.w("ServiceBridge", "DelegateService " + AdditionalActivityManagerNative.this.mRemoteDelegate + " died unexpectedly");
            AdditionalActivityManagerNative.this.mRemoteDelegate.asBinder().unlinkToDeath(AdditionalActivityManagerNative.this.mBinderPoolDeathRecipient, 0);
            IDelegateBinder unused = AdditionalActivityManagerNative.this.mRemoteDelegate = null;
            AdditionalActivityManagerNative.this.connectDelegateService(AdditionalActivityManagerNative.this.processName);
        }
    };
    /* access modifiers changed from: private */
    public CountDownLatch mCountDownLatch;
    private ServiceConnection mDelegateConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            IDelegateBinder unused = AdditionalActivityManagerNative.this.mRemoteDelegate = IDelegateBinder.Stub.asInterface(service);
            try {
                AdditionalActivityManagerNative.this.mRemoteDelegate.asBinder().linkToDeath(AdditionalActivityManagerNative.this.mBinderPoolDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            AdditionalActivityManagerNative.this.recoverActiveServie(AdditionalActivityManagerNative.this.mRemoteDelegate);
            AdditionalActivityManagerNative.this.mCountDownLatch.countDown();
        }
    };
    /* access modifiers changed from: private */
    public IDelegateBinder mRemoteDelegate;
    /* access modifiers changed from: private */
    public String processName;
    private Intent targetIntent;

    static {
        shandlerThread.start();
    }

    private AdditionalActivityManagerNative(String processname) {
        this.processName = processname;
    }

    /* access modifiers changed from: private */
    public static AdditionalActivityManagerNative obtain(String processName2) {
        if (sBridges.get(processName2) == null) {
            synchronized (AdditionalActivityManagerNative.class) {
                if (sBridges.get(processName2) == null) {
                    sBridges.put(processName2, new AdditionalActivityManagerNative(processName2));
                }
            }
        }
        return sBridges.get(processName2);
    }

    public IDelegateBinder getRemoteDelegate() {
        if (this.mRemoteDelegate != null && this.mRemoteDelegate.asBinder().isBinderAlive()) {
            return this.mRemoteDelegate;
        }
        connectDelegateService(this.processName);
        return this.mRemoteDelegate;
    }

    /* access modifiers changed from: private */
    public synchronized void connectDelegateService(String processName2) {
        if (this.mRemoteDelegate == null || !this.mRemoteDelegate.asBinder().isBinderAlive()) {
            this.mCountDownLatch = new CountDownLatch(1);
            if (this.targetIntent == null) {
                Intent service = new Intent();
                service.setClassName(RuntimeVariables.androidApplication, BridgeUtil.getBridgeName(2, processName2));
                this.targetIntent = service;
            }
            RuntimeVariables.androidApplication.bindService(this.targetIntent, this.mDelegateConnection, 1);
            try {
                this.mCountDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    public static ComponentName startService(final Intent service) {
        final ServiceInfo info = (ServiceInfo) AdditionalPackageManager.getInstance().getNewComponentInfo(service.getComponent(), ServiceInfo.class);
        if (info == null) {
            Log.e("ServiceBridge", "can't find startservice | serviceinfo for intent: " + service.toString());
            return null;
        }
        sServicehandler.post(new Runnable() {
            public void run() {
                try {
                    AdditionalActivityManagerNative.obtain(info.processName).getRemoteDelegate().startService(service, info);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        return service.getComponent();
    }

    public static boolean stopService(final Intent service) {
        final ServiceInfo info = (ServiceInfo) AdditionalPackageManager.getInstance().getNewComponentInfo(service.getComponent(), ServiceInfo.class);
        if (info == null) {
            Log.e("ServiceBridge", "can't stopService | serviceinfo for intent: " + service.toString());
            return false;
        }
        sServicehandler.post(new Runnable() {
            public void run() {
                try {
                    AdditionalActivityManagerNative.obtain(info.processName).getRemoteDelegate().stopService(service);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    public static int bindService(final IBinder token, final Intent service, String resolvedType, final IServiceConnection connection) {
        final ServiceInfo info = (ServiceInfo) AdditionalPackageManager.getInstance().getNewComponentInfo(service.getComponent(), ServiceInfo.class);
        if (info == null) {
            Log.e("ServiceBridge", "can't bindService | serviceinfo for intent: " + service.toString());
            return 0;
        }
        sServicehandler.post(new Runnable() {
            public void run() {
                try {
                    String processName = info.processName;
                    AdditionalActivityManagerNative.obtain(processName).mActiveServiceInfo.put(service, connection);
                    AdditionalActivityManagerNative.obtain(processName).getRemoteDelegate().bindService(service, token, connection);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        return 1;
    }

    public static boolean unbindService(final IServiceConnection conn) {
        String tmp = null;
        for (Map.Entry<String, AdditionalActivityManagerNative> entry : sBridges.entrySet()) {
            if (entry.getValue().mActiveServiceInfo.containsValue(conn)) {
                tmp = entry.getKey();
            }
        }
        final String processOfRemoteService = tmp;
        if (processOfRemoteService == null) {
            return false;
        }
        sServicehandler.post(new Runnable() {
            public void run() {
                try {
                    AdditionalActivityManagerNative.obtain(processOfRemoteService).mRemoteDelegate.unbindService(conn);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    public static void handleActivityStack(final Intent intent, final ActivityInfo info, final ActivityBridge.OnIntentPreparedObserver observer) {
        sServicehandler.post(new Runnable() {
            public void run() {
                try {
                    Intent result = AdditionalActivityManagerNative.obtain(info.processName).getRemoteDelegate().handleActivityStack(intent, info);
                    if (observer != null) {
                        observer.onPrepared(result);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void notifyonReceived(final Intent intent, final ActivityInfo info) {
        sServicehandler.post(new Runnable() {
            public void run() {
                try {
                    AdditionalActivityManagerNative.obtain(info.processName).getRemoteDelegate().handleReceiver(intent, info);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void recoverActiveServie(IDelegateBinder delegate) {
        if (this.mActiveServiceInfo.size() > 0) {
            for (Map.Entry<Intent, IServiceConnection> entry : this.mActiveServiceInfo.entrySet()) {
                Intent intent = entry.getKey();
                IServiceConnection connection = entry.getValue();
                if (connection != null) {
                    try {
                        delegate.bindService(intent, (IBinder) null, connection);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        delegate.startService(intent, (ServiceInfo) null);
                    } catch (RemoteException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }
}
