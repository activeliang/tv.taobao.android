package android.taobao.atlas.startup.patch.releaser;

import android.app.Application;
import android.os.Environment;
import android.os.Process;
import android.taobao.atlas.hack.Hack;
import android.taobao.atlas.startup.patch.KernalBundle;
import android.text.TextUtils;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatchDexProfile {
    static Hack.HackedMethod applicationLoaders_addPath = null;
    static Hack.HackedClass applicationLoaders_clazz;
    static Hack.HackedField applicationLoaders_gApplicationLoaders;
    private static PatchDexProfile mPatchDexProfile = null;
    static Hack.HackedClass systemProperties_clazz;
    static Hack.HackedMethod systemProperties_getBoolean;
    static Hack.HackedMethod systemProperties_set;
    static Hack.HackedField vmRuntime_THE_ONE;
    static Hack.HackedClass vmRuntime_clazz;
    static Hack.HackedMethod vmRuntime_disableJitCompilation;
    static Hack.HackedMethod vmRuntime_registerAppInfo = null;
    private Application mApp;

    static {
        vmRuntime_clazz = null;
        vmRuntime_THE_ONE = null;
        vmRuntime_disableJitCompilation = null;
        systemProperties_clazz = null;
        systemProperties_getBoolean = null;
        systemProperties_set = null;
        applicationLoaders_clazz = null;
        applicationLoaders_gApplicationLoaders = null;
        try {
            vmRuntime_clazz = Hack.into("dalvik.system.VMRuntime");
            vmRuntime_THE_ONE = vmRuntime_clazz.staticField("THE_ONE");
            vmRuntime_disableJitCompilation = vmRuntime_clazz.method("disableJitCompilation", new Class[0]);
            systemProperties_clazz = Hack.into("android.os.SystemProperties");
            systemProperties_getBoolean = systemProperties_clazz.staticMethod("getBoolean", String.class, Boolean.TYPE);
            systemProperties_set = systemProperties_clazz.staticMethod("set", String.class, String.class);
            applicationLoaders_clazz = Hack.into("android.app.ApplicationLoaders");
            applicationLoaders_gApplicationLoaders = applicationLoaders_clazz.staticField("gApplicationLoaders");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private PatchDexProfile(Application mApplication) {
        this.mApp = mApplication;
    }

    public static PatchDexProfile instance(Application mApp2) {
        if (mPatchDexProfile == null) {
            mPatchDexProfile = new PatchDexProfile(mApp2);
        }
        return mPatchDexProfile;
    }

    public void disableJitCompile() {
        try {
            if (vmRuntime_disableJitCompilation != null) {
                vmRuntime_disableJitCompilation.invoke(vmRuntime_THE_ONE.get(null), new Object[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupJitProfileSupport(String[] dexFiles) {
        try {
            if (!((Boolean) systemProperties_getBoolean.invoke((Object) null, "dalvik.vm.usejitprofiles", false)).booleanValue()) {
                return;
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (this.mApp.getApplicationInfo().uid == Process.myUid()) {
            List<String> codePaths = new ArrayList<>();
            if (dexFiles != null) {
                Collections.addAll(codePaths, dexFiles);
            }
            if (!codePaths.isEmpty()) {
                File profileFile = getPrimaryProfileFile(this.mApp.getPackageName());
                try {
                    vmRuntime_registerAppInfo.invoke((Object) null, profileFile.getPath(), codePaths.toArray(new String[codePaths.size()]));
                } catch (InvocationTargetException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public static void addPathes(List<String> addedPaths) {
        if (addedPaths != null) {
            try {
                if (addedPaths.size() > 0) {
                    String add = TextUtils.join(File.pathSeparator, addedPaths);
                    applicationLoaders_addPath.invoke(applicationLoaders_gApplicationLoaders.get(null), KernalBundle.class.getClassLoader(), add);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private File getPrimaryProfileFile(String packageName) {
        Class<Environment> cls = Environment.class;
        try {
            Method m = cls.getDeclaredMethod("getDataProfilesDePackageDirectory", new Class[]{Integer.TYPE, String.class});
            m.setAccessible(true);
            return new File((File) m.invoke((Object) null, new Object[]{Integer.valueOf(this.mApp.getApplicationInfo().uid / 100000), packageName}), "primary-patch.prof");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
