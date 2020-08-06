package android.taobao.atlas.hack;

import android.app.Application;
import android.app.Instrumentation;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.taobao.atlas.framework.Atlas;
import android.taobao.atlas.framework.Framework;
import android.taobao.atlas.hack.Hack;
import android.taobao.atlas.runtime.ActivityTaskMgr;
import android.taobao.atlas.runtime.ActivityThreadHook;
import android.taobao.atlas.runtime.DelegateClassLoader;
import android.taobao.atlas.runtime.DelegateResources;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.util.ArrayMap;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AndroidHack {
    private static Object _mLoadedApk = null;
    /* access modifiers changed from: private */
    public static Object _sActivityThread = null;
    static Field sActiveResourcesField;
    static Field sAssetsField;
    static Class sResourcesManagerClazz;
    static Method sgetInstanceMethod;

    static {
        sActiveResourcesField = null;
        sResourcesManagerClazz = null;
        sgetInstanceMethod = null;
        sAssetsField = null;
        try {
            if (Build.VERSION.SDK_INT <= 18) {
                sActiveResourcesField = Class.forName("android.app.ActivityThread").getDeclaredField("mActiveResources");
                sActiveResourcesField.setAccessible(true);
                sAssetsField = Resources.class.getDeclaredField("mAssets");
                sAssetsField.setAccessible(true);
            } else if (Build.VERSION.SDK_INT < 24) {
                sResourcesManagerClazz = Class.forName("android.app.ResourcesManager");
                sActiveResourcesField = sResourcesManagerClazz.getDeclaredField("mActiveResources");
                sActiveResourcesField.setAccessible(true);
                sgetInstanceMethod = sResourcesManagerClazz.getDeclaredMethod("getInstance", new Class[0]);
                sgetInstanceMethod.setAccessible(true);
                sAssetsField = Resources.class.getDeclaredField("mAssets");
                sAssetsField.setAccessible(true);
            } else {
                sResourcesManagerClazz = Class.forName("android.app.ResourcesManager");
                sActiveResourcesField = sResourcesManagerClazz.getDeclaredField("mResourceReferences");
                sActiveResourcesField.setAccessible(true);
                sgetInstanceMethod = sResourcesManagerClazz.getDeclaredMethod("getInstance", new Class[0]);
                sgetInstanceMethod.setAccessible(true);
            }
        } catch (Throwable th) {
        }
    }

    public static Object getActivityThread() throws Exception {
        if (_sActivityThread == null) {
            if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
                _sActivityThread = AtlasHacks.ActivityThread_currentActivityThread.invoke((Object) null, new Object[0]);
            } else {
                Handler handler = new Handler(Looper.getMainLooper());
                synchronized (AtlasHacks.ActivityThread_currentActivityThread) {
                    handler.post(new ActvityThreadGetter());
                    AtlasHacks.ActivityThread_currentActivityThread.wait();
                }
            }
        }
        return _sActivityThread;
    }

    public static Handler hackH() throws Exception {
        Object activityThread = getActivityThread();
        if (activityThread == null) {
            throw new Exception("Failed to get ActivityThread.sCurrentActivityThread");
        }
        try {
            Handler handler = (Handler) AtlasHacks.ActivityThread.field("mH").ofType(Hack.into("android.app.ActivityThread$H").getmClass()).get(activityThread);
            Field field = Handler.class.getDeclaredField("mCallback");
            field.setAccessible(true);
            field.set(handler, new ActivityThreadHook(activityThread, handler));
            return null;
        } catch (Hack.HackDeclaration.HackAssertionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getLoadedApk(Application application, Object activityThread, String packageName) {
        if (_mLoadedApk != null) {
            return _mLoadedApk;
        }
        WeakReference<?> rf = (WeakReference) AtlasHacks.ActivityThread_mPackages.get(activityThread).get(packageName);
        if (rf == null || rf.get() == null) {
            return null;
        }
        _mLoadedApk = rf.get();
        return rf.get();
    }

    public static Object createNewLoadedApk(Application application, Object activityThread) {
        Method getCompatibilityInfo;
        try {
            ApplicationInfo info = RuntimeVariables.androidApplication.getPackageManager().getApplicationInfo(RuntimeVariables.androidApplication.getPackageName(), 1);
            String currentSource = info != null ? info.sourceDir : null;
            if (Atlas.sAPKSource == null || currentSource == null || !currentSource.equals(Atlas.sAPKSource)) {
                Log.e("AndroidHack", Atlas.sAPKSource + " | " + currentSource);
                ActivityTaskMgr.getInstance().clearActivityStack();
                Process.killProcess(Process.myPid());
                System.exit(0);
                return null;
            }
            ApplicationInfo ai = application.getPackageManager().getApplicationInfo(application.getPackageName(), 1152);
            PackageManager packageManager = application.getPackageManager();
            Resources mResources = application.getResources();
            if (mResources instanceof DelegateResources) {
                getCompatibilityInfo = mResources.getClass().getSuperclass().getDeclaredMethod("getCompatibilityInfo", new Class[0]);
            } else {
                getCompatibilityInfo = findMethod(mResources, "getCompatibilityInfo", new Class[0]);
            }
            getCompatibilityInfo.setAccessible(true);
            Class ComplatibilityInfoClass = Class.forName("android.content.res.CompatibilityInfo");
            Object compatibilityInfo = getCompatibilityInfo.invoke(application.getResources(), new Object[0]);
            Method getPackageInfoNoCheck = AtlasHacks.ActivityThread.getmClass().getDeclaredMethod("getPackageInfoNoCheck", new Class[]{ApplicationInfo.class, ComplatibilityInfoClass});
            getPackageInfoNoCheck.setAccessible(true);
            Object loadedApk = getPackageInfoNoCheck.invoke(activityThread, new Object[]{ai, compatibilityInfo});
            _mLoadedApk = loadedApk;
            Field mApplicationField = _mLoadedApk.getClass().getDeclaredField("mApplication");
            mApplicationField.setAccessible(true);
            mApplicationField.set(_mLoadedApk, RuntimeVariables.androidApplication);
            return loadedApk;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void injectClassLoader(String packageName, ClassLoader classLoader) throws Exception {
        try {
            for (Map.Entry<String, ClassLoader> e : AtlasHacks.ApplicationLoaders_mLoaders.get(AtlasHacks.ApplicationLoaders_getDefault.invoke((Object) null, new Object[0])).entrySet()) {
                if (e.getValue() == Framework.getSystemClassLoader()) {
                    e.setValue(classLoader);
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        Object activityThread = getActivityThread();
        if (activityThread == null) {
            throw new Exception("Failed to get ActivityThread.sCurrentActivityThread");
        }
        Object loadedApk = getLoadedApk(RuntimeVariables.androidApplication, activityThread, packageName);
        if (loadedApk == null) {
            loadedApk = createNewLoadedApk(RuntimeVariables.androidApplication, activityThread);
        }
        if (loadedApk == null) {
            throw new Exception("Failed to get ActivityThread.mLoadedApk");
        }
        AtlasHacks.LoadedApk_mClassLoader.set(loadedApk, classLoader);
    }

    public static void injectResources(Application application, Resources resources) throws Exception {
        Collection<WeakReference<Resources>> references;
        Object activityThread = getActivityThread();
        if (activityThread == null) {
            throw new Exception("Failed to get ActivityThread.sCurrentActivityThread");
        }
        Object loadedApk = getLoadedApk(application, activityThread, application.getPackageName());
        if (loadedApk == null) {
            loadedApk = createNewLoadedApk(application, activityThread);
            if (loadedApk == null) {
                throw new RuntimeException(" Failed to get ActivityThread.mLoadedApk");
            } else if (!(AtlasHacks.LoadedApk_mClassLoader.get(loadedApk) instanceof DelegateClassLoader)) {
                AtlasHacks.LoadedApk_mClassLoader.set(loadedApk, RuntimeVariables.delegateClassLoader);
            }
        }
        AtlasHacks.LoadedApk_mResources.set(loadedApk, resources);
        AtlasHacks.ContextImpl_mResources.set(application.getBaseContext(), resources);
        AtlasHacks.ContextImpl_mTheme.set(application.getBaseContext(), (Object) null);
        application.getBaseContext().getTheme();
        try {
            if (Build.VERSION.SDK_INT <= 18) {
                references = ((HashMap) sActiveResourcesField.get(activityThread)).values();
            } else if (Build.VERSION.SDK_INT < 24) {
                references = ((ArrayMap) sActiveResourcesField.get(sgetInstanceMethod.invoke(sResourcesManagerClazz, new Object[0]))).values();
            } else {
                references = (Collection) sActiveResourcesField.get(sgetInstanceMethod.invoke(sResourcesManagerClazz, new Object[0]));
            }
            for (WeakReference<Resources> wr : references) {
                Resources res = (Resources) wr.get();
                if (Build.VERSION.SDK_INT < 24) {
                    if (res != null) {
                        sAssetsField.set(res, resources.getAssets());
                    }
                } else if (res != null) {
                    Field resourcesImplField = Resources.class.getDeclaredField("mResourcesImpl");
                    resourcesImplField.setAccessible(true);
                    Object resourceImpl = resourcesImplField.get(res);
                    Field implAssets = findField(resourceImpl, "mAssets");
                    implAssets.setAccessible(true);
                    implAssets.set(resourceImpl, resources.getAssets());
                }
                if (Build.VERSION.SDK_INT > 19 && Build.VERSION.SDK_INT < 24) {
                    try {
                        Field typedArrayPoolField = findField(Resources.class, "mTypedArrayPool");
                        Object origTypedArrayPool = typedArrayPoolField.get(resources);
                        Field poolField = findField(origTypedArrayPool, "mPool");
                        Constructor<?> typedArrayConstructor = origTypedArrayPool.getClass().getConstructor(new Class[]{Integer.TYPE});
                        typedArrayConstructor.setAccessible(true);
                        typedArrayPoolField.set(resources, typedArrayConstructor.newInstance(new Object[]{Integer.valueOf(((Object[]) poolField.get(origTypedArrayPool)).length)}));
                    } catch (Throwable th) {
                    }
                }
                if (res != null) {
                    res.updateConfiguration(resources.getConfiguration(), resources.getDisplayMetrics());
                }
                Class TintContextWrapper = Class.forName("android.support.v7.widget.TintContextWrapper");
                Field tintWrapperField = TintContextWrapper.getDeclaredField("sCache");
                tintWrapperField.setAccessible(true);
                ArrayList<WeakReference<Object>> sCache = (ArrayList) tintWrapperField.get(TintContextWrapper);
                if (sCache != null) {
                    for (int n = 0; n < sCache.size(); n++) {
                        WeakReference<Object> wrappRef = sCache.get(n);
                        Object wrapper = wrappRef != null ? wrappRef.get() : null;
                        Field mTintResourcesField = TintContextWrapper.getDeclaredField("mResources");
                        mTintResourcesField.setAccessible(true);
                        Object obj = mTintResourcesField.get(wrapper);
                        findField(obj, "mResources").set(obj, resources);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static Field findField(Object instance, String name) throws NoSuchFieldException {
        Class cls = instance.getClass();
        while (cls != null) {
            try {
                Field field = cls.getDeclaredField(name);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field;
            } catch (NoSuchFieldException e) {
                cls = cls.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field " + name + " not found in " + instance.getClass());
    }

    public static Method findMethod(Object instance, String name, Class<?>... args) throws NoSuchMethodException {
        Class cls = instance.getClass();
        while (cls != null) {
            try {
                Method method = cls.getDeclaredMethod(name, args);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                return method;
            } catch (NoSuchMethodException e) {
                cls = cls.getSuperclass();
            }
        }
        throw new NoSuchMethodException("Method " + name + " not found in " + instance.getClass());
    }

    public static Instrumentation getInstrumentation() throws Exception {
        Object activityThread = getActivityThread();
        if (activityThread != null) {
            return AtlasHacks.ActivityThread_mInstrumentation.get(activityThread);
        }
        throw new Exception("Failed to get ActivityThread.sCurrentActivityThread");
    }

    public static void injectInstrumentationHook(Instrumentation instrumentation) throws Exception {
        Object activityThread = getActivityThread();
        if (activityThread == null) {
            throw new Exception("Failed to get ActivityThread.sCurrentActivityThread");
        }
        AtlasHacks.ActivityThread_mInstrumentation.set(activityThread, instrumentation);
    }

    static class ActvityThreadGetter implements Runnable {
        ActvityThreadGetter() {
        }

        public void run() {
            try {
                Object unused = AndroidHack._sActivityThread = AtlasHacks.ActivityThread_currentActivityThread.invoke(AtlasHacks.ActivityThread.getmClass(), new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            synchronized (AtlasHacks.ActivityThread_currentActivityThread) {
                AtlasHacks.ActivityThread_currentActivityThread.notify();
            }
        }
    }
}
