package android.taobao.atlas.runtime.newcomponent.service;

import android.app.IServiceConnection;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.taobao.atlas.runtime.newcomponent.activity.ActivityBridge;
import android.taobao.atlas.runtime.newcomponent.receiver.ReceiverBridge;
import android.taobao.atlas.runtime.newcomponent.service.IDelegateBinder;
import android.taobao.atlas.util.log.impl.AtlasMonitor;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BaseDelegateService extends Service {
    private static final String TAG = "BaseDelegateService";
    private static Method sConnectedMethod;
    private static boolean sConnectedMethodFetched;
    private ServiceDispatcherImpl dispatcher = new ServiceDispatcherImpl();

    public void onCreate() {
        super.onCreate();
    }

    public IBinder onBind(Intent intent) {
        return this.dispatcher;
    }

    public static class ServiceDispatcherImpl extends IDelegateBinder.Stub {
        /* access modifiers changed from: private */
        public HashMap<AdditionalServiceRecord, Service> mActivateServices = new HashMap<>();
        private Handler mMainHandler = new Handler(Looper.getMainLooper());

        public IBinder startService(final Intent serviceIntent, ServiceInfo info) throws RemoteException {
            Log.e(BaseDelegateService.TAG, "startService");
            this.mMainHandler.post(new Runnable() {
                public void run() {
                    AdditionalServiceRecord record = ServiceDispatcherImpl.this.retriveServiceByComponent(serviceIntent.getComponent());
                    if (record == null) {
                        record = ServiceDispatcherImpl.this.handleCreateService(serviceIntent.getComponent());
                    }
                    record.calledStart = true;
                    if (record != null) {
                        ServiceDispatcherImpl.this.handleServiceArgs(serviceIntent, record);
                    }
                }
            });
            return null;
        }

        public IBinder bindService(final Intent serviceIntent, IBinder activityToken, final IServiceConnection conn) throws RemoteException {
            Log.e(BaseDelegateService.TAG, "bindService");
            this.mMainHandler.post(new Runnable() {
                public void run() {
                    AdditionalServiceRecord record = ServiceDispatcherImpl.this.retriveServiceByComponent(serviceIntent.getComponent());
                    if (record == null) {
                        record = ServiceDispatcherImpl.this.handleCreateService(serviceIntent.getComponent());
                    }
                    if (record != null) {
                        IBinder binder = ((Service) ServiceDispatcherImpl.this.mActivateServices.get(record)).onBind(serviceIntent);
                        if (!record.activeConnections.contains(conn)) {
                            record.activeConnections.add(conn);
                        }
                        try {
                            BaseDelegateService.connected(conn, record.component, binder, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Map<String, Object> detail = new HashMap<>();
                            detail.put("serviceIntent", serviceIntent);
                            AtlasMonitor.getInstance().report(AtlasMonitor.NEWCOMPONENT_SERVICE, detail, e);
                        }
                    }
                }
            });
            return null;
        }

        public boolean unbindService(IServiceConnection conn) throws RemoteException {
            Log.e(BaseDelegateService.TAG, "unbindService");
            Iterator iter = this.mActivateServices.entrySet().iterator();
            AdditionalServiceRecord record = null;
            while (true) {
                if (!iter.hasNext()) {
                    break;
                }
                AdditionalServiceRecord tmpRecord = iter.next().getKey();
                if (tmpRecord.activeConnections.contains(conn)) {
                    record = tmpRecord;
                    break;
                }
            }
            if (record != null) {
                record.activeConnections.remove(conn);
                if (record.activeConnections.size() == 0 && (!record.calledStart || (record.calledStart && record.delayStop))) {
                    this.mActivateServices.remove(record).onDestroy();
                    return true;
                }
            }
            return false;
        }

        public int stopService(Intent serviceIntent) throws RemoteException {
            Log.e(BaseDelegateService.TAG, "stopService");
            AdditionalServiceRecord record = retriveServiceByComponent(serviceIntent.getComponent());
            if (record == null) {
                return 0;
            }
            if (record.activeConnections.size() > 0) {
                record.delayStop = true;
                return 0;
            }
            this.mActivateServices.get(record).onDestroy();
            return 0;
        }

        public Intent handleActivityStack(Intent intent, ActivityInfo info) throws RemoteException {
            ActivityBridge.handleActivityStack(info, intent);
            return intent;
        }

        public void handleReceiver(final Intent intent, final ActivityInfo info) throws RemoteException {
            this.mMainHandler.post(new Runnable() {
                public void run() {
                    ReceiverBridge.postOnReceived(intent, info);
                }
            });
        }

        /* access modifiers changed from: private */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public android.taobao.atlas.runtime.newcomponent.service.BaseDelegateService.AdditionalServiceRecord handleCreateService(android.content.ComponentName r19) {
            /*
                r18 = this;
                android.taobao.atlas.runtime.DelegateClassLoader r13 = android.taobao.atlas.runtime.RuntimeVariables.delegateClassLoader     // Catch:{ Throwable -> 0x0127 }
                java.lang.String r14 = r19.getClassName()     // Catch:{ Throwable -> 0x0127 }
                java.lang.Class r12 = r13.loadClass(r14)     // Catch:{ Throwable -> 0x0127 }
                java.lang.Object r11 = r12.newInstance()     // Catch:{ Throwable -> 0x0127 }
                android.app.Service r11 = (android.app.Service) r11     // Catch:{ Throwable -> 0x0127 }
                r5 = 0
                java.lang.Object r3 = android.taobao.atlas.hack.AndroidHack.getActivityThread()     // Catch:{ Throwable -> 0x0127 }
                android.app.Application r13 = android.taobao.atlas.runtime.RuntimeVariables.androidApplication     // Catch:{ Throwable -> 0x0127 }
                android.app.Application r14 = android.taobao.atlas.runtime.RuntimeVariables.androidApplication     // Catch:{ Throwable -> 0x0127 }
                java.lang.String r14 = r14.getPackageName()     // Catch:{ Throwable -> 0x0127 }
                java.lang.Object r9 = android.taobao.atlas.hack.AndroidHack.getLoadedApk(r13, r3, r14)     // Catch:{ Throwable -> 0x0127 }
                android.taobao.atlas.hack.Hack$HackedClass<java.lang.Object> r13 = android.taobao.atlas.hack.AtlasHacks.ContextImpl     // Catch:{ Throwable -> 0x00c7 }
                java.lang.String r14 = "createAppContext"
                r15 = 2
                java.lang.Class[] r15 = new java.lang.Class[r15]     // Catch:{ Throwable -> 0x00c7 }
                r16 = 0
                android.taobao.atlas.hack.Hack$HackedClass<java.lang.Object> r17 = android.taobao.atlas.hack.AtlasHacks.ActivityThread     // Catch:{ Throwable -> 0x00c7 }
                java.lang.Class r17 = r17.getmClass()     // Catch:{ Throwable -> 0x00c7 }
                r15[r16] = r17     // Catch:{ Throwable -> 0x00c7 }
                r16 = 1
                android.taobao.atlas.hack.Hack$HackedClass<java.lang.Object> r17 = android.taobao.atlas.hack.AtlasHacks.LoadedApk     // Catch:{ Throwable -> 0x00c7 }
                java.lang.Class r17 = r17.getmClass()     // Catch:{ Throwable -> 0x00c7 }
                r15[r16] = r17     // Catch:{ Throwable -> 0x00c7 }
                android.taobao.atlas.hack.Hack$HackedMethod r1 = r13.method(r14, r15)     // Catch:{ Throwable -> 0x00c7 }
                java.lang.reflect.Method r13 = r1.getMethod()     // Catch:{ Throwable -> 0x00c7 }
                if (r13 == 0) goto L_0x005a
                android.taobao.atlas.hack.Hack$HackedClass<java.lang.Object> r13 = android.taobao.atlas.hack.AtlasHacks.ContextImpl     // Catch:{ Throwable -> 0x00c7 }
                java.lang.Class r13 = r13.getmClass()     // Catch:{ Throwable -> 0x00c7 }
                r14 = 2
                java.lang.Object[] r14 = new java.lang.Object[r14]     // Catch:{ Throwable -> 0x00c7 }
                r15 = 0
                r14[r15] = r3     // Catch:{ Throwable -> 0x00c7 }
                r15 = 1
                r14[r15] = r9     // Catch:{ Throwable -> 0x00c7 }
                java.lang.Object r5 = r1.invoke(r13, r14)     // Catch:{ Throwable -> 0x00c7 }
            L_0x005a:
                r13 = r5
            L_0x005b:
                r7 = 0
                int r14 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x0127 }
                r15 = 25
                if (r14 > r15) goto L_0x006c
                int r14 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x0127 }
                r15 = 25
                if (r14 != r15) goto L_0x0119
                int r14 = android.os.Build.VERSION.PREVIEW_SDK_INT     // Catch:{ Throwable -> 0x0127 }
                if (r14 <= 0) goto L_0x0119
            L_0x006c:
                android.taobao.atlas.hack.Hack$HackedField<java.lang.Object, java.lang.Object> r14 = android.taobao.atlas.hack.AtlasHacks.ActivityManager_IActivityManagerSingleton     // Catch:{ Throwable -> 0x0127 }
                android.taobao.atlas.hack.Hack$HackedClass<java.lang.Object> r15 = android.taobao.atlas.hack.AtlasHacks.ActivityManager     // Catch:{ Throwable -> 0x0127 }
                java.lang.Class r15 = r15.getmClass()     // Catch:{ Throwable -> 0x0127 }
                java.lang.Object r7 = r14.get(r15)     // Catch:{ Throwable -> 0x0127 }
            L_0x0078:
                android.taobao.atlas.hack.Hack$HackedField<java.lang.Object, java.lang.Object> r14 = android.taobao.atlas.hack.AtlasHacks.Singleton_mInstance     // Catch:{ Throwable -> 0x0127 }
                java.lang.Object r7 = r14.get(r7)     // Catch:{ Throwable -> 0x0127 }
                android.taobao.atlas.hack.Hack$HackedMethod r14 = android.taobao.atlas.hack.AtlasHacks.ContextImpl_setOuterContext     // Catch:{ Throwable -> 0x0127 }
                r15 = 1
                java.lang.Object[] r15 = new java.lang.Object[r15]     // Catch:{ Throwable -> 0x0127 }
                r16 = 0
                r15[r16] = r11     // Catch:{ Throwable -> 0x0127 }
                r14.invoke(r13, r15)     // Catch:{ Throwable -> 0x0127 }
                android.taobao.atlas.runtime.ContextImplHook r8 = new android.taobao.atlas.runtime.ContextImplHook     // Catch:{ Throwable -> 0x0127 }
                android.content.Context r13 = (android.content.Context) r13     // Catch:{ Throwable -> 0x0127 }
                java.lang.ClassLoader r14 = r12.getClassLoader()     // Catch:{ Throwable -> 0x0127 }
                r8.<init>(r13, r14)     // Catch:{ Throwable -> 0x0127 }
                android.taobao.atlas.runtime.newcomponent.service.BaseDelegateService$AdditionalServiceRecord r10 = new android.taobao.atlas.runtime.newcomponent.service.BaseDelegateService$AdditionalServiceRecord     // Catch:{ Throwable -> 0x0127 }
                r0 = r19
                r10.<init>(r0)     // Catch:{ Throwable -> 0x0127 }
                android.taobao.atlas.hack.Hack$HackedMethod r13 = android.taobao.atlas.hack.AtlasHacks.Service_attach     // Catch:{ Throwable -> 0x0127 }
                r14 = 6
                java.lang.Object[] r14 = new java.lang.Object[r14]     // Catch:{ Throwable -> 0x0127 }
                r15 = 0
                r14[r15] = r8     // Catch:{ Throwable -> 0x0127 }
                r15 = 1
                r14[r15] = r3     // Catch:{ Throwable -> 0x0127 }
                r15 = 2
                java.lang.String r16 = r12.getName()     // Catch:{ Throwable -> 0x0127 }
                r14[r15] = r16     // Catch:{ Throwable -> 0x0127 }
                r15 = 3
                r14[r15] = r10     // Catch:{ Throwable -> 0x0127 }
                r15 = 4
                android.app.Application r16 = android.taobao.atlas.runtime.RuntimeVariables.androidApplication     // Catch:{ Throwable -> 0x0127 }
                r14[r15] = r16     // Catch:{ Throwable -> 0x0127 }
                r15 = 5
                r14[r15] = r7     // Catch:{ Throwable -> 0x0127 }
                r13.invoke(r11, r14)     // Catch:{ Throwable -> 0x0127 }
                r11.onCreate()     // Catch:{ Throwable -> 0x0127 }
                r0 = r18
                java.util.HashMap<android.taobao.atlas.runtime.newcomponent.service.BaseDelegateService$AdditionalServiceRecord, android.app.Service> r13 = r0.mActivateServices     // Catch:{ Throwable -> 0x0127 }
                r13.put(r10, r11)     // Catch:{ Throwable -> 0x0127 }
            L_0x00c6:
                return r10
            L_0x00c7:
                r6 = move-exception
                android.taobao.atlas.hack.Hack$HackedClass<java.lang.Object> r13 = android.taobao.atlas.hack.AtlasHacks.ContextImpl     // Catch:{ Throwable -> 0x0127 }
                java.lang.String r14 = "init"
                r15 = 3
                java.lang.Class[] r15 = new java.lang.Class[r15]     // Catch:{ Throwable -> 0x0127 }
                r16 = 0
                android.taobao.atlas.hack.Hack$HackedClass<java.lang.Object> r17 = android.taobao.atlas.hack.AtlasHacks.LoadedApk     // Catch:{ Throwable -> 0x0127 }
                java.lang.Class r17 = r17.getmClass()     // Catch:{ Throwable -> 0x0127 }
                r15[r16] = r17     // Catch:{ Throwable -> 0x0127 }
                r16 = 1
                java.lang.Class<android.os.IBinder> r17 = android.os.IBinder.class
                r15[r16] = r17     // Catch:{ Throwable -> 0x0127 }
                r16 = 2
                android.taobao.atlas.hack.Hack$HackedClass<java.lang.Object> r17 = android.taobao.atlas.hack.AtlasHacks.ActivityThread     // Catch:{ Throwable -> 0x0127 }
                java.lang.Class r17 = r17.getmClass()     // Catch:{ Throwable -> 0x0127 }
                r15[r16] = r17     // Catch:{ Throwable -> 0x0127 }
                android.taobao.atlas.hack.Hack$HackedMethod r2 = r13.method(r14, r15)     // Catch:{ Throwable -> 0x0127 }
                android.taobao.atlas.hack.Hack$HackedClass<java.lang.Object> r13 = android.taobao.atlas.hack.AtlasHacks.ContextImpl     // Catch:{ Throwable -> 0x0127 }
                java.lang.Class r13 = r13.getmClass()     // Catch:{ Throwable -> 0x0127 }
                r14 = 0
                java.lang.Class[] r14 = new java.lang.Class[r14]     // Catch:{ Throwable -> 0x0127 }
                java.lang.reflect.Constructor r4 = r13.getDeclaredConstructor(r14)     // Catch:{ Throwable -> 0x0127 }
                r13 = 1
                r4.setAccessible(r13)     // Catch:{ Throwable -> 0x0127 }
                r13 = 0
                java.lang.Object[] r13 = new java.lang.Object[r13]     // Catch:{ Throwable -> 0x0127 }
                java.lang.Object r5 = r4.newInstance(r13)     // Catch:{ Throwable -> 0x0127 }
                r13 = 3
                java.lang.Object[] r13 = new java.lang.Object[r13]     // Catch:{ Throwable -> 0x0127 }
                r14 = 0
                r13[r14] = r9     // Catch:{ Throwable -> 0x0127 }
                r14 = 1
                r15 = 0
                r13[r14] = r15     // Catch:{ Throwable -> 0x0127 }
                r14 = 2
                r13[r14] = r3     // Catch:{ Throwable -> 0x0127 }
                r2.invoke(r5, r13)     // Catch:{ Throwable -> 0x0127 }
                r13 = r5
                goto L_0x005b
            L_0x0119:
                android.taobao.atlas.hack.Hack$HackedField<java.lang.Object, java.lang.Object> r14 = android.taobao.atlas.hack.AtlasHacks.ActivityManagerNative_gDefault     // Catch:{ Throwable -> 0x0127 }
                android.taobao.atlas.hack.Hack$HackedClass<java.lang.Object> r15 = android.taobao.atlas.hack.AtlasHacks.ActivityManagerNative     // Catch:{ Throwable -> 0x0127 }
                java.lang.Class r15 = r15.getmClass()     // Catch:{ Throwable -> 0x0127 }
                java.lang.Object r7 = r14.get(r15)     // Catch:{ Throwable -> 0x0127 }
                goto L_0x0078
            L_0x0127:
                r6 = move-exception
                r6.printStackTrace()
                r10 = 0
                goto L_0x00c6
            */
            throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.runtime.newcomponent.service.BaseDelegateService.ServiceDispatcherImpl.handleCreateService(android.content.ComponentName):android.taobao.atlas.runtime.newcomponent.service.BaseDelegateService$AdditionalServiceRecord");
        }

        /* access modifiers changed from: private */
        public void handleServiceArgs(Intent serviceIntent, IBinder token) {
            Service service = this.mActivateServices.get(token);
            if (service != null) {
                if (serviceIntent != null) {
                    serviceIntent.setExtrasClassLoader(service.getClassLoader());
                }
                service.onStartCommand(serviceIntent, 2, 0);
            }
        }

        /* access modifiers changed from: private */
        public AdditionalServiceRecord retriveServiceByComponent(ComponentName component) {
            for (Map.Entry<AdditionalServiceRecord, Service> entry : this.mActivateServices.entrySet()) {
                AdditionalServiceRecord tmpRecord = entry.getKey();
                if (tmpRecord.component.equals(component)) {
                    return tmpRecord;
                }
            }
            return null;
        }
    }

    public static class AdditionalServiceRecord extends Binder {
        public final ArrayList<IServiceConnection> activeConnections = new ArrayList<>();
        public boolean calledStart = false;
        final ComponentName component;
        public boolean delayStop = false;

        public AdditionalServiceRecord(ComponentName componentName) {
            this.component = componentName;
        }
    }

    public static void connected(IServiceConnection iServiceConnection, ComponentName name, IBinder service, boolean dead) {
        fetchConnectedMethod();
        if (sConnectedMethod != null) {
            try {
                if (Build.VERSION.SDK_INT >= 26) {
                    sConnectedMethod.invoke(iServiceConnection, new Object[]{name, service, Boolean.valueOf(dead)});
                    return;
                }
                sConnectedMethod.invoke(iServiceConnection, new Object[]{name, service});
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e2) {
                throw new RuntimeException(e2.getCause());
            }
        }
    }

    private static void fetchConnectedMethod() {
        if (!sConnectedMethodFetched) {
            try {
                if (Build.VERSION.SDK_INT >= 26) {
                    sConnectedMethod = IServiceConnection.class.getDeclaredMethod("connected", new Class[]{ComponentName.class, IBinder.class, Boolean.TYPE});
                } else {
                    sConnectedMethod = IServiceConnection.class.getDeclaredMethod("connected", new Class[]{ComponentName.class, IBinder.class});
                }
                sConnectedMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                Log.i(TAG, "Failed to retrieve connected method", e);
            }
            sConnectedMethodFetched = true;
        }
    }
}
