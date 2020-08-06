package android.taobao.atlas.runtime;

import android.content.ComponentName;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.framework.Atlas;
import android.taobao.atlas.framework.BundleImpl;
import android.taobao.atlas.framework.Framework;
import android.taobao.atlas.framework.MbundleImpl;
import android.taobao.atlas.util.BundleLock;
import android.taobao.atlas.util.FileUtils;
import android.taobao.atlas.util.log.impl.AtlasMonitor;
import android.util.Log;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import mtopsdk.common.util.SymbolExpUtil;
import org.osgi.framework.Bundle;

public class DelegateClassLoader extends PathClassLoader {
    private ReadWriteLock mReadWirteLock = new ReentrantReadWriteLock();

    public DelegateClassLoader(ClassLoader cl) {
        super(".", cl);
    }

    public void addDexPath(String dexPath) {
        try {
            Method addDexMethod = findMethod(getParent(), "addDexPath", new Class[]{String.class});
            addDexMethod.setAccessible(true);
            addDexMethod.invoke(getParent(), new Object[]{dexPath});
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public Class<?> loadClass(String className) throws ClassNotFoundException {
        String location = AtlasBundleInfoManager.instance().getBundleForComponet(className);
        if (location == null) {
            return super.loadClass(className);
        }
        installMbundleWithDependency(location);
        return super.loadClass(className);
    }

    public void installMbundleWithDependency(String location) {
        if (AtlasBundleInfoManager.instance().isMbundle(location)) {
            for (String bundle : AtlasBundleInfoManager.instance().getBundleInfo(location).getTotalDependency()) {
                if (!(bundle == null || AtlasBundleInfoManager.instance().getBundleInfo(bundle) == null)) {
                    if (!AtlasBundleInfoManager.instance().getBundleInfo(bundle).isMBundle()) {
                        Map<String, Object> detailMap = new HashMap<>();
                        detailMap.put(BaseConfig.INTENT_KEY_SOURCE, location);
                        detailMap.put("dependency", bundle);
                        detailMap.put("method", "installMbundleWithDependency()");
                        AtlasMonitor.getInstance().report(AtlasMonitor.BUNDLE_DEPENDENCY_ERROR, detailMap, new IllegalArgumentException());
                        Log.e("Atlas", location + " Mbundle can not has dependency bundle--> " + bundle);
                    }
                    installMbundle(bundle);
                }
            }
        }
    }

    public void installMbundle(String location) {
        try {
            BundleLock.WriteLock(location);
            if (Atlas.getInstance().getBundle(location) == null) {
                new MbundleImpl(location).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BundleLock.WriteUnLock(location);
        }
    }

    /* access modifiers changed from: protected */
    public Class<?> findClass(String className) throws ClassNotFoundException {
        if (Thread.currentThread().getId() != Looper.getMainLooper().getThread().getId()) {
            BundleUtil.checkBundleStateSyncOnChildThread(className);
        } else {
            BundleUtil.checkBundleStateSyncOnUIThread(className);
        }
        Class<?> clazz = loadFromInstalledBundles(className, true);
        if (clazz != null) {
            return clazz;
        }
        ComponentName comp = new ComponentName(RuntimeVariables.androidApplication.getPackageName(), className);
        if (isProvider(comp)) {
            return Atlas.class.getClassLoader().loadClass("android.taobao.atlas.util.FakeProvider");
        }
        if (isReceiver(comp)) {
            return Atlas.class.getClassLoader().loadClass("android.taobao.atlas.util.FakeReceiver");
        }
        throw new ClassNotFoundException("Can't find class " + className + printExceptionInfo());
    }

    private String printExceptionInfo() {
        StringBuilder sb = new StringBuilder("installed bundles: ");
        List<Bundle> bundles = Framework.getBundles();
        if (bundles != null && !bundles.isEmpty()) {
            for (Bundle b : Framework.getBundles()) {
                if (b.getLocation().contains("com.ut")) {
                    sb.append(b.getLocation().toUpperCase());
                } else {
                    sb.append(b.getLocation());
                }
                sb.append(SymbolExpUtil.SYMBOL_COLON);
            }
        }
        return sb.toString();
    }

    static Class<?> loadFromInstalledBundles(String className, boolean safe) throws ClassNotFoundException {
        ClassLoader classloader;
        String str;
        int i = 0;
        Class<?> clazz = null;
        List<Bundle> bundles = Framework.getBundles();
        BundleImpl bundle = (BundleImpl) Atlas.getInstance().getBundle(AtlasBundleInfoManager.instance().getBundleForComponet(className));
        if (bundle != null) {
            if (!Framework.isDeubgMode()) {
                bundle.optDexFile();
            }
            bundle.startBundle();
            ClassLoader classloader2 = bundle.getClassLoader();
            if (classloader2 != null) {
                try {
                    clazz = classloader2.loadClass(className);
                    if (clazz != null && bundle.checkValidate()) {
                        return clazz;
                    }
                } catch (ClassNotFoundException e) {
                }
            }
            if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
                Throwable ex = new Throwable();
                ex.fillInStackTrace();
                Log.e("MainThreadFindClass", String.format("can not findClass %s from %s in UI thread ", new Object[]{className, bundle}));
                ex.printStackTrace();
            }
            if (safe) {
                ComponentName component = new ComponentName(RuntimeVariables.androidApplication.getPackageName(), className);
                if (isProvider(component)) {
                    return Atlas.class.getClassLoader().loadClass("android.taobao.atlas.util.FakeProvider");
                }
                if (isReceiver(component)) {
                    return Atlas.class.getClassLoader().loadClass("android.taobao.atlas.util.FakeReceiver");
                }
                StringBuilder append = new StringBuilder().append("Can't find class ").append(className).append(" in BundleClassLoader: ").append(bundle.getLocation()).append(" [");
                if (bundles != null) {
                    i = bundles.size();
                }
                StringBuilder append2 = append.append(i).append("]");
                if (classloader2 == null) {
                    str = "classloader is null";
                } else {
                    str = "classloader not null";
                }
                throw new ClassNotFoundException(append2.append(str).append(" packageversion ").append(getPackageVersion()).append(FileUtils.getAvailableDisk()).toString());
            }
        }
        if (bundles != null && !bundles.isEmpty()) {
            Iterator<Bundle> it = Framework.getBundles().iterator();
            while (it.hasNext()) {
                BundleImpl bundle2 = (BundleImpl) it.next();
                if (bundle2.getArchive().isDexOpted() && (classloader = bundle2.getClassLoader()) != null) {
                    try {
                        clazz = classloader.loadClass(className);
                        if (clazz != null && bundle2.checkValidate()) {
                            return clazz;
                        }
                    } catch (ClassNotFoundException e2) {
                    }
                }
            }
        }
        return clazz;
    }

    private static int getPackageVersion() {
        PackageInfo packageInfo;
        try {
            packageInfo = RuntimeVariables.androidApplication.getPackageManager().getPackageInfo(RuntimeVariables.androidApplication.getPackageName(), 0);
        } catch (Exception e) {
            packageInfo = new PackageInfo();
        }
        return packageInfo.versionCode;
    }

    private static boolean isReceiver(ComponentName component) {
        try {
            if (RuntimeVariables.androidApplication.getPackageManager().getReceiverInfo(component, 2) != null) {
                return true;
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    private static boolean isProvider(ComponentName component) {
        try {
            if (RuntimeVariables.androidApplication.getPackageManager().getProviderInfo(component, 8) != null) {
                return true;
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    public static Method findMethod(Object instance, String methodName, Class[] args) throws NoSuchMethodException {
        Class cls = instance.getClass();
        while (cls != null) {
            try {
                Method method = cls.getDeclaredMethod(methodName, args);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                return method;
            } catch (NoSuchMethodException e) {
                cls = cls.getSuperclass();
            }
        }
        throw new NoSuchMethodException("Field " + methodName + " not found in " + instance.getClass());
    }
}
