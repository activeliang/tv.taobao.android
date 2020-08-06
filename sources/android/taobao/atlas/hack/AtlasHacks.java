package android.taobao.atlas.hack;

import android.app.Application;
import android.app.ContentProviderHolder;
import android.app.IActivityManager;
import android.app.Instrumentation;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.IContentProvider;
import android.content.pm.ApplicationInfo;
import android.content.pm.ProviderInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.IBinder;
import android.taobao.atlas.hack.Hack;
import android.util.Log;
import android.view.ContextThemeWrapper;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.yunos.tv.alitvasrsdk.CommonData;
import dalvik.system.DexClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AtlasHacks extends Hack.HackDeclaration implements Hack.AssertionFailureHandler {
    public static Hack.HackedClass<Object> ActivityManager;
    public static Hack.HackedClass<Object> ActivityManagerNative;
    public static Hack.HackedField<Object, Object> ActivityManagerNative_gDefault;
    public static Hack.HackedField<Object, Object> ActivityManager_IActivityManagerSingleton;
    public static Hack.HackedClass<Object> ActivityThread;
    public static Hack.HackedClass<Object> ActivityThread$AppBindData;
    public static Hack.HackedField<Object, List<ProviderInfo>> ActivityThread$AppBindData_providers;
    public static Hack.HackedMethod ActivityThread_currentActivityThread;
    public static Hack.HackedMethod ActivityThread_installContentProviders;
    public static Hack.HackedMethod ActivityThread_installProvider;
    public static Hack.HackedField<Object, ArrayList<Application>> ActivityThread_mAllApplications;
    public static Hack.HackedField<Object, Object> ActivityThread_mBoundApplication;
    public static Hack.HackedField<Object, Application> ActivityThread_mInitialApplication;
    public static Hack.HackedField<Object, Instrumentation> ActivityThread_mInstrumentation;
    public static Hack.HackedField<Object, Map<String, Object>> ActivityThread_mPackages;
    public static Hack.HackedField<Object, Object> ActivityThread_sPackageManager;
    public static Hack.HackedClass<Application> Application;
    public static Hack.HackedClass<Object> ApplicationLoaders;
    public static Hack.HackedMethod ApplicationLoaders_getDefault;
    public static Hack.HackedField<Object, Map<String, ClassLoader>> ApplicationLoaders_mLoaders;
    public static Hack.HackedMethod Application_attach;
    public static Hack.HackedClass<AssetManager> AssetManager;
    public static Hack.HackedMethod AssetManager_addAssetPath;
    public static Hack.HackedMethod AssetManager_addAssetPathAsSharedLibrary;
    public static Hack.HackedMethod AssetManager_addAssetPathNative;
    public static Hack.HackedMethod AssetManager_addAssetPathNative24;
    public static Hack.HackedMethod AssetManager_addAssetPathNativeSamSung;
    public static Hack.HackedMethod AssetManager_ensureStringBlocks;
    public static Hack.HackedMethod AssetManager_getNativeStringBlock;
    public static Hack.HackedMethod AssetManager_getResourceIdentifier;
    public static Hack.HackedMethod AssetManager_getStringBlockCount;
    public static Hack.HackedField<AssetManager, Object> AssetManager_mStringBlocks;
    public static Hack.HackedClass<ClassLoader> ClassLoader;
    public static Hack.HackedMethod ClassLoader_findLibrary;
    public static Hack.HackedClass<Object> ContextImpl;
    public static Hack.HackedField<Object, Object> ContextImpl_mPackageInfo;
    public static Hack.HackedField<Object, Resources> ContextImpl_mResources;
    public static Hack.HackedField<Object, Resources.Theme> ContextImpl_mTheme;
    public static Hack.HackedMethod ContextImpl_setOuterContext;
    public static Hack.HackedClass<ContextThemeWrapper> ContextThemeWrapper;
    public static Hack.HackedField<ContextThemeWrapper, Context> ContextThemeWrapper_mBase;
    public static Hack.HackedField<ContextThemeWrapper, Resources> ContextThemeWrapper_mResources;
    public static Hack.HackedField<ContextThemeWrapper, Resources.Theme> ContextThemeWrapper_mTheme;
    public static Hack.HackedClass<ContextWrapper> ContextWrapper;
    public static Hack.HackedField<ContextWrapper, Context> ContextWrapper_mBase;
    public static Hack.HackedClass<DexClassLoader> DexClassLoader;
    public static Hack.HackedMethod DexClassLoader_findClass;
    public static ArrayList<Hack.HackedMethod> GeneratePackageInfoList = new ArrayList<>();
    public static ArrayList<Hack.HackedMethod> GetPackageInfoList = new ArrayList<>();
    public static Hack.HackedClass<Object> IPackageManager;
    public static Hack.HackedClass<Object> LexFile;
    public static Hack.HackedMethod LexFile_close;
    public static Hack.HackedMethod LexFile_loadClass;
    public static Hack.HackedMethod LexFile_loadLex;
    public static Hack.HackedClass<Object> LoadedApk;
    public static Hack.HackedField<Object, String> LoadedApk_mAppDir;
    public static Hack.HackedField<Object, Application> LoadedApk_mApplication;
    public static Hack.HackedField<Object, ClassLoader> LoadedApk_mBaseClassLoader;
    public static Hack.HackedField<Object, ClassLoader> LoadedApk_mClassLoader;
    public static Hack.HackedField<Object, String> LoadedApk_mResDir;
    public static Hack.HackedField<Object, Resources> LoadedApk_mResources;
    public static Hack.HackedClass<Object> PackageParser;
    public static Hack.HackedClass<Object> PackageParser$Activity;
    public static Hack.HackedClass<Object> PackageParser$ActivityIntentInfo;
    public static Hack.HackedField<Object, Object> PackageParser$ActivityIntentInfo_activity;
    public static Hack.HackedClass<Object> PackageParser$Component;
    public static Hack.HackedMethod PackageParser$Component_getComponentName;
    public static Hack.HackedField<Object, ArrayList<Object>> PackageParser$Component_intents;
    public static Hack.HackedClass<Object> PackageParser$Package;
    public static Hack.HackedField<Object, ArrayList<Object>> PackageParser$Package_activities;
    public static Hack.HackedField<Object, ApplicationInfo> PackageParser$Package_applicationInfo;
    public static Hack.HackedField<Object, String> PackageParser$Package_packageName;
    public static Hack.HackedField<Object, ArrayList<Object>> PackageParser$Package_providers;
    public static Hack.HackedField<Object, ArrayList<Object>> PackageParser$Package_receivers;
    public static Hack.HackedField<Object, ArrayList<Object>> PackageParser$Package_services;
    public static Hack.HackedClass<Object> PackageParser$Provider;
    public static Hack.HackedClass<Object> PackageParser$ProviderIntentInfo;
    public static Hack.HackedField<Object, Object> PackageParser$ProviderIntentInfo_provider;
    public static Hack.HackedField<Object, ProviderInfo> PackageParser$Provider_info;
    public static Hack.HackedClass<Object> PackageParser$Service;
    public static Hack.HackedClass<Object> PackageParser$ServiceIntentInfo;
    public static Hack.HackedField<Object, Object> PackageParser$ServiceIntentInfo_service;
    public static Hack.HackedConstructor PackageParser_constructor;
    public static Hack.HackedMethod PackageParser_generatePackageInfo;
    public static Hack.HackedMethod PackageParser_parsePackage;
    public static Hack.HackedClass<Resources> Resources;
    public static Hack.HackedClass<Service> Service;
    public static Hack.HackedMethod Service_attach;
    public static Hack.HackedClass<Object> Singleton;
    public static Hack.HackedField<Object, Object> Singleton_mInstance;
    public static Hack.HackedClass<Object> StringBlock;
    public static Hack.HackedConstructor StringBlock_constructor;
    public static boolean sIsIgnoreFailure = false;
    public static boolean sIsReflectAvailable = false;
    public static boolean sIsReflectChecked = false;
    private AssertionArrayException mExceptionArray = null;

    public static boolean defineAndVerify() {
        if (sIsReflectChecked) {
            return sIsReflectAvailable;
        }
        AtlasHacks atlasHacks = new AtlasHacks();
        try {
            Hack.setAssertionFailureHandler(atlasHacks);
            if (Build.VERSION.SDK_INT == 11) {
                atlasHacks.onAssertionFailure(new Hack.HackDeclaration.HackAssertionException("Hack Assertion Failed: Android OS Version 11"));
            }
            allClasses();
            allConstructors();
            allFields();
            allMethods();
            if (atlasHacks.mExceptionArray != null) {
                sIsReflectAvailable = false;
            } else {
                sIsReflectAvailable = true;
            }
        } catch (Hack.HackDeclaration.HackAssertionException e) {
            sIsReflectAvailable = false;
            e.printStackTrace();
        } finally {
            Hack.setAssertionFailureHandler((Hack.AssertionFailureHandler) null);
            sIsReflectChecked = true;
        }
        return sIsReflectAvailable;
    }

    public static void allClasses() throws Hack.HackDeclaration.HackAssertionException {
        if (Build.VERSION.SDK_INT <= 8) {
            LoadedApk = Hack.into("android.app.ActivityThread$PackageInfo");
        } else {
            LoadedApk = Hack.into("android.app.LoadedApk");
        }
        ActivityThread = Hack.into("android.app.ActivityThread");
        Resources = Hack.into(Resources.class);
        Application = Hack.into(Application.class);
        AssetManager = Hack.into(AssetManager.class);
        IPackageManager = Hack.into("android.content.pm.IPackageManager");
        Service = Hack.into(Service.class);
        ContextImpl = Hack.into("android.app.ContextImpl");
        ContextThemeWrapper = Hack.into(ContextThemeWrapper.class);
        ContextWrapper = Hack.into("android.content.ContextWrapper");
        sIsIgnoreFailure = true;
        ClassLoader = Hack.into(ClassLoader.class);
        DexClassLoader = Hack.into(DexClassLoader.class);
        LexFile = Hack.into("dalvik.system.LexFile");
        PackageParser$Component = Hack.into("android.content.pm.PackageParser$Component");
        PackageParser$Activity = Hack.into("android.content.pm.PackageParser$Activity");
        PackageParser$Service = Hack.into("android.content.pm.PackageParser$Service");
        PackageParser$Provider = Hack.into("android.content.pm.PackageParser$Provider");
        PackageParser = Hack.into("android.content.pm.PackageParser");
        PackageParser$Package = Hack.into("android.content.pm.PackageParser$Package");
        PackageParser$ActivityIntentInfo = Hack.into("android.content.pm.PackageParser$ActivityIntentInfo");
        PackageParser$ServiceIntentInfo = Hack.into("android.content.pm.PackageParser$ServiceIntentInfo");
        PackageParser$ProviderIntentInfo = Hack.into("android.content.pm.PackageParser$ProviderIntentInfo");
        ActivityManagerNative = Hack.into("android.app.ActivityManagerNative");
        Singleton = Hack.into("android.util.Singleton");
        ActivityThread$AppBindData = Hack.into("android.app.ActivityThread$AppBindData");
        ActivityManager = Hack.into("android.app.ActivityManager");
        StringBlock = Hack.into("android.content.res.StringBlock");
        ApplicationLoaders = Hack.into("android.app.ApplicationLoaders");
        sIsIgnoreFailure = false;
    }

    public static void allFields() throws Hack.HackDeclaration.HackAssertionException {
        ActivityThread_mInstrumentation = ActivityThread.field("mInstrumentation").ofType(Instrumentation.class);
        ActivityThread_mAllApplications = ActivityThread.field("mAllApplications").ofGenericType(ArrayList.class);
        ActivityThread_mInitialApplication = ActivityThread.field("mInitialApplication").ofType(Application.class);
        ActivityThread_mPackages = ActivityThread.field("mPackages").ofGenericType(Map.class);
        ActivityThread_sPackageManager = ActivityThread.staticField("sPackageManager").ofType(IPackageManager.getmClass());
        LoadedApk_mApplication = LoadedApk.field("mApplication").ofType(Application.class);
        LoadedApk_mResources = LoadedApk.field("mResources").ofType(Resources.class);
        LoadedApk_mResDir = LoadedApk.field("mResDir").ofType(String.class);
        LoadedApk_mClassLoader = LoadedApk.field("mClassLoader").ofType(ClassLoader.class);
        LoadedApk_mBaseClassLoader = LoadedApk.field("mBaseClassLoader").ofType(ClassLoader.class);
        LoadedApk_mAppDir = LoadedApk.field("mAppDir").ofType(String.class);
        ContextImpl_mResources = ContextImpl.field("mResources").ofType(Resources.class);
        ContextImpl_mTheme = ContextImpl.field("mTheme").ofType(Resources.Theme.class);
        sIsIgnoreFailure = true;
        ContextThemeWrapper_mBase = ContextThemeWrapper.field("mBase").ofType(Context.class);
        sIsIgnoreFailure = false;
        ContextThemeWrapper_mTheme = ContextThemeWrapper.field("mTheme").ofType(Resources.Theme.class);
        try {
            if (Build.VERSION.SDK_INT >= 17 && ContextThemeWrapper.getmClass().getDeclaredField("mResources") != null) {
                ContextThemeWrapper_mResources = ContextThemeWrapper.field("mResources").ofType(Resources.class);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        ContextWrapper_mBase = ContextWrapper.field("mBase").ofType(Context.class);
        PackageParser$Component_intents = PackageParser$Component.field("intents").ofGenericType(ArrayList.class);
        PackageParser$Package_activities = PackageParser$Package.field("activities").ofGenericType(ArrayList.class);
        PackageParser$Package_services = PackageParser$Package.field("services").ofGenericType(ArrayList.class);
        PackageParser$Package_receivers = PackageParser$Package.field("receivers").ofGenericType(ArrayList.class);
        PackageParser$Package_providers = PackageParser$Package.field("providers").ofGenericType(ArrayList.class);
        PackageParser$Package_applicationInfo = PackageParser$Package.field("applicationInfo").ofType(ApplicationInfo.class);
        PackageParser$Package_packageName = PackageParser$Package.field(CommonData.KEY_PACKAGE_NAME).ofGenericType(String.class);
        PackageParser$ActivityIntentInfo_activity = PackageParser$ActivityIntentInfo.field(TuwenConstants.MODEL_LIST_KEY.ACTIVITY).ofType(PackageParser$Activity.getmClass());
        PackageParser$ServiceIntentInfo_service = PackageParser$ServiceIntentInfo.field("service").ofType(PackageParser$Service.getmClass());
        PackageParser$ProviderIntentInfo_provider = PackageParser$ProviderIntentInfo.field("provider").ofType(PackageParser$Provider.getmClass());
        PackageParser$Provider_info = PackageParser$Provider.field("info").ofType(ProviderInfo.class);
        if (Build.VERSION.SDK_INT > 25 || (Build.VERSION.SDK_INT == 25 && Build.VERSION.PREVIEW_SDK_INT > 0)) {
            ActivityManager_IActivityManagerSingleton = ActivityManager.staticField("IActivityManagerSingleton");
        } else {
            ActivityManagerNative_gDefault = ActivityManagerNative.staticField("gDefault");
        }
        Singleton_mInstance = Singleton.field("mInstance");
        ActivityThread$AppBindData_providers = ActivityThread$AppBindData.field("providers").ofGenericType(List.class);
        ActivityThread_mBoundApplication = ActivityThread.field("mBoundApplication");
        ContextImpl_mPackageInfo = ContextImpl.field("mPackageInfo");
        AssetManager_mStringBlocks = AssetManager.field("mStringBlocks");
        ApplicationLoaders_mLoaders = ApplicationLoaders.field("mLoaders").ofGenericType(Map.class);
    }

    public static void allMethods() throws Hack.HackDeclaration.HackAssertionException {
        ActivityThread_currentActivityThread = ActivityThread.method("currentActivityThread", new Class[0]);
        AssetManager_addAssetPath = AssetManager.method("addAssetPath", String.class);
        if (Build.VERSION.SDK_INT >= 24) {
            AssetManager_addAssetPathAsSharedLibrary = AssetManager.method("addAssetPathAsSharedLibrary", String.class);
        }
        Application_attach = Application.method("attach", Context.class);
        PackageParser$Component_getComponentName = PackageParser$Component.method("getComponentName", new Class[0]);
        ClassLoader_findLibrary = ClassLoader.method("findLibrary", String.class);
        ContextImpl_setOuterContext = ContextImpl.method("setOuterContext", Context.class);
        if (!(LexFile == null || LexFile.getmClass() == null)) {
            LexFile_loadLex = LexFile.method("loadLex", String.class, Integer.TYPE);
            LexFile_loadClass = LexFile.method("loadClass", String.class, ClassLoader.class);
            LexFile_close = LexFile.method("close", new Class[0]);
            DexClassLoader_findClass = DexClassLoader.method("findClass", String.class);
        }
        try {
            if (Build.VERSION.SDK_INT > 20) {
                AssetManager_getResourceIdentifier = AssetManager.method("getResourceIdentifier", String.class, String.class, String.class);
                AssetManager_ensureStringBlocks = AssetManager.method("ensureStringBlocks", new Class[0]);
            }
        } catch (Throwable th) {
        }
        ActivityThread_installContentProviders = ActivityThread.method("installContentProviders", Context.class, List.class);
        try {
            if (Build.VERSION.SDK_INT > 25 || (Build.VERSION.SDK_INT == 25 && Build.VERSION.PREVIEW_SDK_INT > 0)) {
                ActivityThread_installProvider = ActivityThread.method("installProvider", Context.class, ContentProviderHolder.class, ProviderInfo.class, Boolean.TYPE, Boolean.TYPE, Boolean.TYPE);
                Service_attach = Service.method("attach", Context.class, ActivityThread.getmClass(), String.class, IBinder.class, Application.getmClass(), Object.class);
                AssetManager_addAssetPathNative = AssetManager.method("addAssetPathNative", String.class);
                if (AssetManager_addAssetPathNative == null || AssetManager_addAssetPathNative.getMethod() == null) {
                    AssetManager_addAssetPathNative24 = AssetManager.method("addAssetPathNative", String.class, Boolean.TYPE);
                }
                if ((AssetManager_addAssetPathNative == null || AssetManager_addAssetPathNative.getMethod() == null) && (AssetManager_addAssetPathNative24 == null || AssetManager_addAssetPathNative24.getMethod() == null)) {
                    AssetManager_addAssetPathNativeSamSung = AssetManager.method("addAssetPathNative", String.class, Integer.TYPE);
                }
                AssetManager_getStringBlockCount = AssetManager.method("getStringBlockCount", new Class[0]);
                AssetManager_getNativeStringBlock = AssetManager.method("getNativeStringBlock", Integer.TYPE);
                ApplicationLoaders_getDefault = ApplicationLoaders.method("getDefault", new Class[0]);
            }
            if (Build.VERSION.SDK_INT == 14) {
                ActivityThread_installProvider = ActivityThread.method("installProvider", Context.class, IContentProvider.class, ProviderInfo.class, Boolean.TYPE);
            } else if (Build.VERSION.SDK_INT == 15) {
                ActivityThread_installProvider = ActivityThread.method("installProvider", Context.class, IContentProvider.class, ProviderInfo.class, Boolean.TYPE, Boolean.TYPE);
            } else {
                ActivityThread_installProvider = ActivityThread.method("installProvider", Context.class, IActivityManager.ContentProviderHolder.class, ProviderInfo.class, Boolean.TYPE, Boolean.TYPE, Boolean.TYPE);
            }
            Service_attach = Service.method("attach", Context.class, ActivityThread.getmClass(), String.class, IBinder.class, Application.getmClass(), Object.class);
            AssetManager_addAssetPathNative = AssetManager.method("addAssetPathNative", String.class);
            AssetManager_addAssetPathNative24 = AssetManager.method("addAssetPathNative", String.class, Boolean.TYPE);
            AssetManager_addAssetPathNativeSamSung = AssetManager.method("addAssetPathNative", String.class, Integer.TYPE);
            AssetManager_getStringBlockCount = AssetManager.method("getStringBlockCount", new Class[0]);
            AssetManager_getNativeStringBlock = AssetManager.method("getNativeStringBlock", Integer.TYPE);
            ApplicationLoaders_getDefault = ApplicationLoaders.method("getDefault", new Class[0]);
        } catch (Throwable t) {
            Log.w("AtlasHacks", "Error getting ActivityThread_installProvider", t);
        }
    }

    public static void allConstructors() throws Hack.HackDeclaration.HackAssertionException {
        if (Build.VERSION.SDK_INT <= 20) {
            PackageParser_constructor = PackageParser.constructor(String.class);
        } else {
            PackageParser_constructor = PackageParser.constructor(new Class[0]);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            StringBlock_constructor = StringBlock.constructor(Long.TYPE, Boolean.TYPE);
            return;
        }
        StringBlock_constructor = StringBlock.constructor(Integer.TYPE, Boolean.TYPE);
    }

    public boolean onAssertionFailure(Hack.HackDeclaration.HackAssertionException failure) {
        if (!sIsIgnoreFailure) {
            if (this.mExceptionArray == null) {
                this.mExceptionArray = new AssertionArrayException("atlas hack assert failed");
            }
            this.mExceptionArray.addException(failure);
        }
        return true;
    }
}
