package android.taobao.atlas.runtime;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.taobao.atlas.hack.Interception;
import java.util.ArrayList;
import java.util.List;

public class ActivityManagerDelegate extends Interception.InterceptionHandler {
    public static final String TAG = "ActivityManagrHook";
    static ActivityInfo[] receiverInfos;
    public static List sIntentHaveProessed = new ArrayList();

    /* JADX WARNING: Code restructure failed: missing block: B:108:?, code lost:
        return 0;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:3:0x0013, B:68:0x0215] */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object invoke(java.lang.Object r23, java.lang.reflect.Method r24, java.lang.Object[] r25) throws java.lang.Throwable {
        /*
            r22 = this;
            java.lang.String r13 = r24.getName()
            java.lang.String r18 = r24.getName()
            java.lang.String r19 = "startService"
            boolean r18 = r18.equals(r19)
            if (r18 == 0) goto L_0x0087
            r18 = 1
            r12 = r25[r18]     // Catch:{ Throwable -> 0x02cb }
            android.content.Intent r12 = (android.content.Intent) r12     // Catch:{ Throwable -> 0x02cb }
            java.util.List r18 = sIntentHaveProessed     // Catch:{ Throwable -> 0x02cb }
            r0 = r18
            boolean r18 = r0.contains(r12)     // Catch:{ Throwable -> 0x02cb }
            if (r18 == 0) goto L_0x002d
            java.util.List r18 = sIntentHaveProessed     // Catch:{ Throwable -> 0x02cb }
            r0 = r18
            r0.remove(r12)     // Catch:{ Throwable -> 0x02cb }
            java.lang.Object r18 = super.invoke(r23, r24, r25)     // Catch:{ Throwable -> 0x02cb }
        L_0x002c:
            return r18
        L_0x002d:
            r18 = 1
            r0 = r18
            android.taobao.atlas.runtime.ServiceDetector$DetectResult r14 = android.taobao.atlas.runtime.ServiceDetector.prepareServiceBundle(r12, r0)     // Catch:{ Throwable -> 0x02cb }
            int r0 = r14.resultCode     // Catch:{ Throwable -> 0x02cb }
            r18 = r0
            r19 = 1
            r0 = r18
            r1 = r19
            if (r0 != r1) goto L_0x0063
            java.lang.String r18 = "atlas_external_service"
            r19 = 0
            r0 = r18
            r1 = r19
            boolean r18 = r12.getBooleanExtra(r0, r1)     // Catch:{ Throwable -> 0x02cb }
            if (r18 == 0) goto L_0x005e
            android.taobao.atlas.runtime.newcomponent.AdditionalActivityManagerProxy r18 = android.taobao.atlas.runtime.newcomponent.AdditionalActivityManagerProxy.get()     // Catch:{ Throwable -> 0x02cb }
            r0 = r18
            r0.startService(r12)     // Catch:{ Throwable -> 0x02cb }
        L_0x0059:
            java.lang.Object r18 = super.invoke(r23, r24, r25)
            goto L_0x002c
        L_0x005e:
            java.lang.Object r18 = super.invoke(r23, r24, r25)     // Catch:{ Throwable -> 0x02cb }
            goto L_0x002c
        L_0x0063:
            int r0 = r14.resultCode     // Catch:{ Throwable -> 0x02cb }
            r18 = r0
            r19 = -1
            r0 = r18
            r1 = r19
            if (r0 != r1) goto L_0x0072
            r18 = 0
            goto L_0x002c
        L_0x0072:
            android.taobao.atlas.runtime.ActivityManagerDelegate$1 r18 = new android.taobao.atlas.runtime.ActivityManagerDelegate$1     // Catch:{ Throwable -> 0x02cb }
            r0 = r18
            r1 = r22
            r2 = r24
            r3 = r25
            r0.<init>(r12, r2, r3)     // Catch:{ Throwable -> 0x02cb }
            r0 = r18
            r14.setResultListener(r0)     // Catch:{ Throwable -> 0x02cb }
            r18 = 0
            goto L_0x002c
        L_0x0087:
            java.lang.String r18 = r24.getName()
            java.lang.String r19 = "bindService"
            boolean r18 = r18.equals(r19)
            if (r18 == 0) goto L_0x0130
            r18 = 2
            r12 = r25[r18]     // Catch:{ Throwable -> 0x02c8 }
            android.content.Intent r12 = (android.content.Intent) r12     // Catch:{ Throwable -> 0x02c8 }
            java.util.List r18 = sIntentHaveProessed     // Catch:{ Throwable -> 0x02c8 }
            r0 = r18
            boolean r18 = r0.contains(r12)     // Catch:{ Throwable -> 0x02c8 }
            if (r18 == 0) goto L_0x00b1
            java.util.List r18 = sIntentHaveProessed     // Catch:{ Throwable -> 0x02c8 }
            r0 = r18
            r0.remove(r12)     // Catch:{ Throwable -> 0x02c8 }
            java.lang.Object r18 = super.invoke(r23, r24, r25)     // Catch:{ Throwable -> 0x02c8 }
            goto L_0x002c
        L_0x00b1:
            r18 = 1
            r0 = r18
            android.taobao.atlas.runtime.ServiceDetector$DetectResult r14 = android.taobao.atlas.runtime.ServiceDetector.prepareServiceBundle(r12, r0)     // Catch:{ Throwable -> 0x02c8 }
            int r0 = r14.resultCode     // Catch:{ Throwable -> 0x02c8 }
            r18 = r0
            r19 = 1
            r0 = r18
            r1 = r19
            if (r0 != r1) goto L_0x0102
            java.lang.String r18 = "atlas_external_service"
            r19 = 0
            r0 = r18
            r1 = r19
            boolean r18 = r12.getBooleanExtra(r0, r1)     // Catch:{ Throwable -> 0x02c8 }
            if (r18 == 0) goto L_0x00fc
            android.taobao.atlas.runtime.newcomponent.AdditionalActivityManagerProxy r21 = android.taobao.atlas.runtime.newcomponent.AdditionalActivityManagerProxy.get()     // Catch:{ Throwable -> 0x02c8 }
            r18 = 1
            r18 = r25[r18]     // Catch:{ Throwable -> 0x02c8 }
            android.os.IBinder r18 = (android.os.IBinder) r18     // Catch:{ Throwable -> 0x02c8 }
            r19 = 3
            r19 = r25[r19]     // Catch:{ Throwable -> 0x02c8 }
            java.lang.String r19 = (java.lang.String) r19     // Catch:{ Throwable -> 0x02c8 }
            r20 = 4
            r20 = r25[r20]     // Catch:{ Throwable -> 0x02c8 }
            android.app.IServiceConnection r20 = (android.app.IServiceConnection) r20     // Catch:{ Throwable -> 0x02c8 }
            r0 = r21
            r1 = r18
            r2 = r19
            r3 = r20
            int r18 = r0.bindService(r1, r12, r2, r3)     // Catch:{ Throwable -> 0x02c8 }
            java.lang.Integer r18 = java.lang.Integer.valueOf(r18)     // Catch:{ Throwable -> 0x02c8 }
            goto L_0x002c
        L_0x00fc:
            java.lang.Object r18 = super.invoke(r23, r24, r25)     // Catch:{ Throwable -> 0x02c8 }
            goto L_0x002c
        L_0x0102:
            int r0 = r14.resultCode     // Catch:{ Throwable -> 0x02c8 }
            r18 = r0
            r19 = -1
            r0 = r18
            r1 = r19
            if (r0 != r1) goto L_0x0116
            r18 = 0
            java.lang.Integer r18 = java.lang.Integer.valueOf(r18)     // Catch:{ Throwable -> 0x02c8 }
            goto L_0x002c
        L_0x0116:
            android.taobao.atlas.runtime.ActivityManagerDelegate$2 r18 = new android.taobao.atlas.runtime.ActivityManagerDelegate$2     // Catch:{ Throwable -> 0x02c8 }
            r0 = r18
            r1 = r22
            r2 = r25
            r3 = r24
            r0.<init>(r12, r2, r3)     // Catch:{ Throwable -> 0x02c8 }
            r0 = r18
            r14.setResultListener(r0)     // Catch:{ Throwable -> 0x02c8 }
            r18 = 1
            java.lang.Integer r18 = java.lang.Integer.valueOf(r18)     // Catch:{ Throwable -> 0x02c8 }
            goto L_0x002c
        L_0x0130:
            java.lang.String r18 = r24.getName()
            java.lang.String r19 = "stopService"
            boolean r18 = r18.equals(r19)
            if (r18 == 0) goto L_0x0157
            r18 = 1
            r12 = r25[r18]
            android.content.Intent r12 = (android.content.Intent) r12
            android.taobao.atlas.runtime.newcomponent.AdditionalActivityManagerProxy r18 = android.taobao.atlas.runtime.newcomponent.AdditionalActivityManagerProxy.get()
            r0 = r18
            boolean r18 = r0.stopService(r12)
            if (r18 == 0) goto L_0x0059
            r18 = 1
            java.lang.Boolean r18 = java.lang.Boolean.valueOf(r18)
            goto L_0x002c
        L_0x0157:
            java.lang.String r18 = r24.getName()
            java.lang.String r19 = "unbindService"
            boolean r18 = r18.equals(r19)
            if (r18 == 0) goto L_0x017e
            r18 = 0
            r15 = r25[r18]
            android.app.IServiceConnection r15 = (android.app.IServiceConnection) r15
            android.taobao.atlas.runtime.newcomponent.AdditionalActivityManagerProxy r18 = android.taobao.atlas.runtime.newcomponent.AdditionalActivityManagerProxy.get()
            r0 = r18
            boolean r18 = r0.unbindService(r15)
            if (r18 == 0) goto L_0x0059
            r18 = 1
            java.lang.Boolean r18 = java.lang.Boolean.valueOf(r18)
            goto L_0x002c
        L_0x017e:
            java.lang.String r18 = r24.getName()
            java.lang.String r19 = "broadcastIntent"
            boolean r18 = r18.equals(r19)
            if (r18 == 0) goto L_0x0269
            r18 = 1
            r12 = r25[r18]     // Catch:{ Throwable -> 0x0214 }
            android.content.Intent r12 = (android.content.Intent) r12     // Catch:{ Throwable -> 0x0214 }
            long r16 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0214 }
            android.app.Application r18 = android.taobao.atlas.runtime.RuntimeVariables.androidApplication     // Catch:{ Throwable -> 0x0214 }
            android.content.pm.PackageManager r18 = r18.getPackageManager()     // Catch:{ Throwable -> 0x0214 }
            r19 = 0
            r0 = r18
            r1 = r19
            java.util.List r11 = r0.queryBroadcastReceivers(r12, r1)     // Catch:{ Throwable -> 0x0214 }
            if (r11 == 0) goto L_0x0215
            int r18 = r11.size()     // Catch:{ Throwable -> 0x0214 }
            if (r18 <= 0) goto L_0x0215
            java.util.ArrayList r6 = new java.util.ArrayList     // Catch:{ Throwable -> 0x0214 }
            r6.<init>()     // Catch:{ Throwable -> 0x0214 }
            java.util.Iterator r18 = r11.iterator()     // Catch:{ Throwable -> 0x0214 }
        L_0x01b6:
            boolean r19 = r18.hasNext()     // Catch:{ Throwable -> 0x0214 }
            if (r19 == 0) goto L_0x021b
            java.lang.Object r10 = r18.next()     // Catch:{ Throwable -> 0x0214 }
            android.content.pm.ResolveInfo r10 = (android.content.pm.ResolveInfo) r10     // Catch:{ Throwable -> 0x0214 }
            android.content.pm.ActivityInfo r0 = r10.activityInfo     // Catch:{ Throwable -> 0x0214 }
            r19 = r0
            r0 = r19
            java.lang.String r0 = r0.packageName     // Catch:{ Throwable -> 0x0214 }
            r19 = r0
            android.app.Application r20 = android.taobao.atlas.runtime.RuntimeVariables.androidApplication     // Catch:{ Throwable -> 0x0214 }
            java.lang.String r20 = r20.getPackageName()     // Catch:{ Throwable -> 0x0214 }
            boolean r19 = r19.equals(r20)     // Catch:{ Throwable -> 0x0214 }
            if (r19 == 0) goto L_0x01b6
            android.content.pm.ActivityInfo r0 = r10.activityInfo     // Catch:{ Throwable -> 0x0214 }
            r19 = r0
            r0 = r19
            java.lang.String r0 = r0.name     // Catch:{ Throwable -> 0x0214 }
            r19 = r0
            r0 = r22
            r1 = r19
            boolean r19 = r0.isstaticReceiver(r1)     // Catch:{ Throwable -> 0x0214 }
            if (r19 == 0) goto L_0x01b6
            android.taobao.atlas.bundleInfo.AtlasBundleInfoManager r19 = android.taobao.atlas.bundleInfo.AtlasBundleInfoManager.instance()     // Catch:{ Throwable -> 0x0214 }
            android.content.pm.ActivityInfo r0 = r10.activityInfo     // Catch:{ Throwable -> 0x0214 }
            r20 = r0
            r0 = r20
            java.lang.String r0 = r0.name     // Catch:{ Throwable -> 0x0214 }
            r20 = r0
            java.lang.String r5 = r19.getBundleForComponet(r20)     // Catch:{ Throwable -> 0x0214 }
            boolean r19 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x0214 }
            if (r19 != 0) goto L_0x01b6
            android.taobao.atlas.framework.Atlas r19 = android.taobao.atlas.framework.Atlas.getInstance()     // Catch:{ Throwable -> 0x0214 }
            r0 = r19
            org.osgi.framework.Bundle r19 = r0.getBundle(r5)     // Catch:{ Throwable -> 0x0214 }
            if (r19 != 0) goto L_0x01b6
            r6.add(r5)     // Catch:{ Throwable -> 0x0214 }
            goto L_0x01b6
        L_0x0214:
            r18 = move-exception
        L_0x0215:
            java.lang.Object r18 = super.invoke(r23, r24, r25)     // Catch:{ Throwable -> 0x0260 }
            goto L_0x002c
        L_0x021b:
            int r18 = r6.size()     // Catch:{ Throwable -> 0x0214 }
            if (r18 <= 0) goto L_0x024b
            android.taobao.atlas.runtime.ActivityManagerDelegate$3 r8 = new android.taobao.atlas.runtime.ActivityManagerDelegate$3     // Catch:{ Throwable -> 0x0214 }
            r0 = r22
            r1 = r24
            r2 = r25
            r8.<init>(r1, r2)     // Catch:{ Throwable -> 0x0214 }
            int r18 = r6.size()     // Catch:{ Throwable -> 0x0214 }
            r0 = r18
            java.lang.String[] r0 = new java.lang.String[r0]     // Catch:{ Throwable -> 0x0214 }
            r18 = r0
            r0 = r18
            java.lang.Object[] r18 = r6.toArray(r0)     // Catch:{ Throwable -> 0x0214 }
            java.lang.String[] r18 = (java.lang.String[]) r18     // Catch:{ Throwable -> 0x0214 }
            r0 = r18
            android.taobao.atlas.runtime.BundleUtil.checkBundleArrayStateAsync(r0, r8, r8)     // Catch:{ Throwable -> 0x0214 }
            r18 = 0
            java.lang.Integer r18 = java.lang.Integer.valueOf(r18)     // Catch:{ Throwable -> 0x0214 }
            goto L_0x002c
        L_0x024b:
            java.lang.Object r18 = r22.delegatee()     // Catch:{ Throwable -> 0x0214 }
            r0 = r24
            r1 = r18
            r2 = r25
            r0.invoke(r1, r2)     // Catch:{ Throwable -> 0x0214 }
            r18 = 0
            java.lang.Integer r18 = java.lang.Integer.valueOf(r18)     // Catch:{ Throwable -> 0x0214 }
            goto L_0x002c
        L_0x0260:
            r7 = move-exception
            r18 = 0
            java.lang.Integer r18 = java.lang.Integer.valueOf(r18)
            goto L_0x002c
        L_0x0269:
            java.lang.String r18 = "startActivity"
            r0 = r18
            boolean r18 = r13.equals(r0)
            if (r18 == 0) goto L_0x029f
            android.app.Instrumentation r9 = android.taobao.atlas.hack.AndroidHack.getInstrumentation()
            if (r9 == 0) goto L_0x0059
            java.lang.Class r18 = r9.getClass()
            java.lang.String r18 = r18.getName()
            java.lang.String r19 = "com.lbe.security.service"
            boolean r18 = r18.contains(r19)
            if (r18 == 0) goto L_0x0059
            android.taobao.atlas.runtime.InstrumentationHook r18 = new android.taobao.atlas.runtime.InstrumentationHook
            android.app.Instrumentation r19 = android.taobao.atlas.hack.AndroidHack.getInstrumentation()
            android.app.Application r20 = android.taobao.atlas.runtime.RuntimeVariables.androidApplication
            android.content.Context r20 = r20.getBaseContext()
            r18.<init>(r19, r20)
            android.taobao.atlas.hack.AndroidHack.injectInstrumentationHook(r18)
            goto L_0x0059
        L_0x029f:
            java.lang.String r18 = "getContentProvider"
            r0 = r18
            boolean r18 = r13.equals(r0)
            if (r18 == 0) goto L_0x0059
            r18 = 1
            r4 = r25[r18]
            java.lang.String r4 = (java.lang.String) r4
            android.taobao.atlas.runtime.newcomponent.AdditionalPackageManager r18 = android.taobao.atlas.runtime.newcomponent.AdditionalPackageManager.getInstance()
            r0 = r18
            android.content.pm.ProviderInfo r10 = r0.resolveContentProvider(r4)
            if (r10 == 0) goto L_0x0059
            android.taobao.atlas.runtime.newcomponent.AdditionalActivityManagerProxy r18 = android.taobao.atlas.runtime.newcomponent.AdditionalActivityManagerProxy.get()
            r0 = r18
            java.lang.Object r18 = r0.getContentProvider(r10)
            goto L_0x002c
        L_0x02c8:
            r18 = move-exception
            goto L_0x0059
        L_0x02cb:
            r18 = move-exception
            goto L_0x0059
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.runtime.ActivityManagerDelegate.invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[]):java.lang.Object");
    }

    private boolean isstaticReceiver(String name) {
        PackageInfo info;
        try {
            if (receiverInfos == null && (info = RuntimeVariables.androidApplication.getPackageManager().getPackageInfo(RuntimeVariables.androidApplication.getPackageName(), 2)) != null && info.receivers != null && info.receivers.length > 0) {
                receiverInfos = info.receivers;
            }
            if (receiverInfos != null) {
                for (ActivityInfo info2 : receiverInfos) {
                    if (info2 != null && info2.name != null && info2.name.equals(name)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return true;
        }
    }
}
