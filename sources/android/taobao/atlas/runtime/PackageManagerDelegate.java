package android.taobao.atlas.runtime;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ParceledListSlice;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.taobao.atlas.runtime.newcomponent.AdditionalPackageManager;
import android.taobao.atlas.versionInfo.BaselineInfoManager;
import android.text.TextUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class PackageManagerDelegate {
    /* access modifiers changed from: private */
    public static Context mBaseContext;
    private static PackageManagerProxyhandler mPackageManagerProxyhandler;
    private static Object mProxyPm;

    public static void delegatepackageManager(Context context) {
        try {
            mBaseContext = context;
            PackageManager manager = mBaseContext.getPackageManager();
            if (mProxyPm == null) {
                Field field = Class.forName("android.app.ApplicationPackageManager").getDeclaredField("mPM");
                field.setAccessible(true);
                Object rawPm = field.get(manager);
                Class IPackageManagerClass = Class.forName("android.content.pm.IPackageManager");
                if (rawPm != null) {
                    if (mPackageManagerProxyhandler == null) {
                        mPackageManagerProxyhandler = new PackageManagerProxyhandler(rawPm);
                    }
                    mProxyPm = Proxy.newProxyInstance(mBaseContext.getClassLoader(), new Class[]{IPackageManagerClass}, mPackageManagerProxyhandler);
                }
                field.set(manager, mProxyPm);
            }
        } catch (Throwable th) {
        }
    }

    public static class PackageManagerProxyhandler implements InvocationHandler {
        private Object mPm;

        public PackageManagerProxyhandler(Object pm) {
            this.mPm = pm;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object info;
            try {
                Object object = method.invoke(this.mPm, args);
                if (method.getName().equals("getPackageInfo") && args[0] != null && args[0].equals(PackageManagerDelegate.mBaseContext.getPackageName())) {
                    PackageInfo info2 = (PackageInfo) object;
                    if (info2 == null) {
                        throw new RuntimeException("can not get packageInfo");
                    }
                    String baselineVersion = BaselineInfoManager.instance().currentVersionName();
                    if (TextUtils.isEmpty(baselineVersion)) {
                        return object;
                    }
                    info2.versionName = baselineVersion;
                    return info2;
                } else if (method.getName().equals("queryIntentActivities")) {
                    List<ResolveInfo> info3 = AdditionalPackageManager.getInstance().queryIntentActivities(args[0]);
                    if (info3 != null) {
                        return Build.VERSION.SDK_INT >= 24 ? new ParceledListSlice(info3) : info3;
                    }
                    return object;
                } else if (method.getName().equals("getActivityInfo")) {
                    Object info4 = (ActivityInfo) AdditionalPackageManager.getInstance().getNewComponentInfo(args[0], ActivityInfo.class);
                    if (info4 != null) {
                        return info4;
                    }
                    return object;
                } else if (method.getName().equals("resolveIntent")) {
                    List<ResolveInfo> info5 = AdditionalPackageManager.getInstance().queryIntentActivities(args[0]);
                    if (info5 != null) {
                        return info5.get(0);
                    }
                    return object;
                } else if (method.getName().equals("queryIntentServices")) {
                    List<ResolveInfo> info6 = AdditionalPackageManager.getInstance().queryIntentService(args[0]);
                    if (info6 != null) {
                        return Build.VERSION.SDK_INT >= 24 ? new ParceledListSlice(info6) : info6;
                    }
                    return object;
                } else if (method.getName().equals("getServiceInfo")) {
                    Object info7 = (ServiceInfo) AdditionalPackageManager.getInstance().getNewComponentInfo(args[0], ServiceInfo.class);
                    if (info7 != null) {
                        return info7;
                    }
                    return object;
                } else if (method.getName().equals("resolveService")) {
                    List<ResolveInfo> info8 = AdditionalPackageManager.getInstance().queryIntentService(args[0]);
                    if (info8 != null) {
                        return info8.get(0);
                    }
                    return object;
                } else if (method.getName().equals("getReceiverInfo")) {
                    Object info9 = AdditionalPackageManager.getInstance().getReceiverInfo(args[0]);
                    if (info9 != null) {
                        return info9;
                    }
                    return object;
                } else if (method.getName().equals("queryBroadcastReceivers")) {
                    List<ResolveInfo> info10 = AdditionalPackageManager.getInstance().queryIntentReceivers(args[0]);
                    if (info10 == null) {
                        return object;
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        return Boolean.valueOf(((ParceledListSlice) object).getList().addAll(info10));
                    }
                    return Boolean.valueOf(info10.addAll((List) object));
                } else if (method.getName().equals("queryContentProviders")) {
                    List<ProviderInfo> infos = AdditionalPackageManager.getInstance().queryContentProviders(args[0]);
                    if (infos == null) {
                        return object;
                    }
                    if (Build.VERSION.SDK_INT < 23) {
                        infos.addAll((List) object);
                        return infos;
                    }
                    ((ParceledListSlice) object).getList().addAll(infos);
                    return object;
                } else if (!method.getName().equals("getProviderInfo") || (info = (ProviderInfo) AdditionalPackageManager.getInstance().getNewComponentInfo(args[0], ProviderInfo.class)) == null) {
                    return object;
                } else {
                    return info;
                }
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    }
}
