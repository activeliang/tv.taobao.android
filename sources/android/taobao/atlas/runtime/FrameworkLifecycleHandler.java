package android.taobao.atlas.runtime;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.taobao.atlas.framework.Framework;
import android.taobao.atlas.runtime.newcomponent.AdditionalPackageManager;
import android.taobao.atlas.util.StringUtils;
import android.taobao.atlas.versionInfo.BaselineInfoManager;
import com.taobao.atlas.dexmerge.MergeConstants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;

public class FrameworkLifecycleHandler implements FrameworkListener {
    public void frameworkEvent(FrameworkEvent event) {
        switch (event.getType()) {
            case 0:
                starting();
                return;
            case 1:
                started();
                return;
            default:
                return;
        }
    }

    private void starting() {
        if (!RuntimeVariables.safeMode) {
            if (BaselineInfoManager.instance().isUpdated(MergeConstants.MAIN_DEX)) {
                AdditionalPackageManager.getInstance();
            }
            long time = System.currentTimeMillis();
            Bundle metaData = null;
            try {
                metaData = RuntimeVariables.androidApplication.getPackageManager().getApplicationInfo(RuntimeVariables.androidApplication.getPackageName(), 128).metaData;
            } catch (PackageManager.NameNotFoundException e1) {
                e1.printStackTrace();
            }
            if (metaData != null) {
                String strApps = metaData.getString("application");
                if (StringUtils.isNotEmpty(strApps)) {
                    String[] appClassNames = StringUtils.split(strApps, ",");
                    if (appClassNames == null || appClassNames.length == 0) {
                        appClassNames = new String[]{strApps};
                    }
                    for (String appClassName : appClassNames) {
                        try {
                            BundleLifecycleHandler.newApplication(appClassName, Framework.getSystemClassLoader()).onCreate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            long currentTimeMillis = System.currentTimeMillis() - time;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0026, code lost:
        r0 = (java.lang.String) android.taobao.atlas.runtime.RuntimeVariables.getFrameworkProperty("autoStartBundles");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void started() {
        /*
            r6 = this;
            android.app.Application r1 = android.taobao.atlas.runtime.RuntimeVariables.androidApplication
            android.taobao.atlas.runtime.ActivityLifeCycleObserver r2 = new android.taobao.atlas.runtime.ActivityLifeCycleObserver
            r2.<init>()
            r1.registerActivityLifecycleCallbacks(r2)
            android.app.Application r1 = android.taobao.atlas.runtime.RuntimeVariables.androidApplication
            android.taobao.atlas.runtime.FrameworkLifecycleHandler$1 r2 = new android.taobao.atlas.runtime.FrameworkLifecycleHandler$1
            r2.<init>()
            r1.registerComponentCallbacks(r2)
            android.app.Application r1 = android.taobao.atlas.runtime.RuntimeVariables.androidApplication
            java.lang.String r1 = android.taobao.atlas.runtime.RuntimeVariables.getProcessName(r1)
            android.app.Application r2 = android.taobao.atlas.runtime.RuntimeVariables.androidApplication
            java.lang.String r2 = r2.getPackageName()
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x0044
            java.lang.String r1 = "autoStartBundles"
            java.lang.Object r0 = android.taobao.atlas.runtime.RuntimeVariables.getFrameworkProperty(r1)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 == 0) goto L_0x0044
            android.os.Handler r1 = new android.os.Handler
            android.os.Looper r2 = android.os.Looper.getMainLooper()
            r1.<init>(r2)
            android.taobao.atlas.runtime.FrameworkLifecycleHandler$2 r2 = new android.taobao.atlas.runtime.FrameworkLifecycleHandler$2
            r2.<init>(r0)
            r4 = 4000(0xfa0, double:1.9763E-320)
            r1.postDelayed(r2, r4)
        L_0x0044:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.runtime.FrameworkLifecycleHandler.started():void");
    }
}
