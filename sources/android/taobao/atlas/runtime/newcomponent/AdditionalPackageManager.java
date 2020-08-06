package android.taobao.atlas.runtime.newcomponent;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.taobao.atlas.hack.AtlasHacks;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.atlas.versionInfo.BaselineInfoManager;
import android.text.TextUtils;
import com.taobao.atlas.dexmerge.MergeConstants;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import mtopsdk.common.util.SymbolExpUtil;

public class AdditionalPackageManager {
    private static int retryCount = 2;
    private static AdditionalPackageManager sBundlePackagemanager;
    private AdditionalComponentIntentResolver mExternalActivity;
    private AdditionalComponentIntentResolver mExternalProviders;
    private AdditionalComponentIntentResolver mExternalReceivers;
    private AdditionalComponentIntentResolver mExternalServices;
    private final HashMap<String, Object> mProvidersByAuthority = new HashMap<>();
    private final HashMap<IntentFilter, String> mReceiverIntentFilters = new HashMap<>();

    public static synchronized AdditionalPackageManager getInstance() {
        AdditionalPackageManager additionalPackageManager;
        synchronized (AdditionalPackageManager.class) {
            if (sBundlePackagemanager == null) {
                sBundlePackagemanager = new AdditionalPackageManager();
            }
            additionalPackageManager = sBundlePackagemanager;
        }
        return additionalPackageManager;
    }

    public AdditionalPackageManager() {
        boolean needRetry;
        if (BaselineInfoManager.instance().isUpdated(MergeConstants.MAIN_DEX)) {
            do {
                try {
                    retryCount--;
                    needRetry = false;
                    Class KernalBundleClass = RuntimeVariables.getRawClassLoader().loadClass("android.taobao.atlas.startup.patch.KernalBundle");
                    Object kernalBundle = KernalBundleClass.getDeclaredField("kernalBundle").get(KernalBundleClass);
                    if (kernalBundle != null) {
                        Method method = KernalBundleClass.getDeclaredMethod("getRevisionZip", new Class[0]);
                        method.setAccessible(true);
                        File file = (File) method.invoke(kernalBundle, new Object[0]);
                        if (file != null) {
                            parseBundle(file.getAbsolutePath());
                        }
                    }
                } catch (Throwable e) {
                    needRetry = true;
                    e.printStackTrace();
                }
                if (retryCount <= 0) {
                    return;
                }
            } while (needRetry);
        }
    }

    private void parseBundle(String zipPath) throws InvocationTargetException {
        Object pkgParser;
        String str;
        if (Build.VERSION.SDK_INT <= 20) {
            pkgParser = AtlasHacks.PackageParser_constructor.getInstance(zipPath);
        } else {
            pkgParser = AtlasHacks.PackageParser_constructor.getInstance(new Object[0]);
        }
        try {
            AssetManager assmgr = AssetManager.class.newInstance();
            AtlasHacks.AssetManager_addAssetPath.invoke(assmgr, RuntimeVariables.androidApplication.getApplicationInfo().sourceDir);
            Object packageObj = parsePackage(pkgParser, new Resources(assmgr, RuntimeVariables.delegateResources.getDisplayMetrics(), RuntimeVariables.delegateResources.getConfiguration()), assmgr.openXmlResourceParser(((Integer) AtlasHacks.AssetManager_addAssetPath.invoke(assmgr, zipPath)).intValue(), "AndroidManifest.xml"));
            if (packageObj != null) {
                AtlasHacks.PackageParser$Package_packageName.set(packageObj, RuntimeVariables.androidApplication.getPackageName());
                ApplicationInfo info = AtlasHacks.PackageParser$Package_applicationInfo.get(packageObj);
                info.name = RuntimeVariables.androidApplication.getApplicationInfo().name;
                info.className = RuntimeVariables.androidApplication.getApplicationInfo().className;
                info.taskAffinity = RuntimeVariables.androidApplication.getApplicationInfo().taskAffinity;
                info.permission = RuntimeVariables.androidApplication.getApplicationInfo().permission;
                info.processName = RuntimeVariables.androidApplication.getApplicationInfo().processName;
                info.theme = RuntimeVariables.androidApplication.getApplicationInfo().theme;
                info.flags = RuntimeVariables.androidApplication.getApplicationInfo().flags;
                info.uiOptions = RuntimeVariables.androidApplication.getApplicationInfo().uiOptions;
                info.backupAgentName = RuntimeVariables.androidApplication.getApplicationInfo().backupAgentName;
                info.descriptionRes = RuntimeVariables.androidApplication.getApplicationInfo().descriptionRes;
                info.targetSdkVersion = RuntimeVariables.androidApplication.getApplicationInfo().targetSdkVersion;
                info.compatibleWidthLimitDp = RuntimeVariables.androidApplication.getApplicationInfo().compatibleWidthLimitDp;
                info.uid = RuntimeVariables.androidApplication.getApplicationInfo().uid;
                info.largestWidthLimitDp = RuntimeVariables.androidApplication.getApplicationInfo().largestWidthLimitDp;
                info.enabled = RuntimeVariables.androidApplication.getApplicationInfo().enabled;
                info.requiresSmallestWidthDp = RuntimeVariables.androidApplication.getApplicationInfo().requiresSmallestWidthDp;
                info.packageName = RuntimeVariables.androidApplication.getApplicationInfo().packageName;
                AdditionalComponentIntentResolver activityResolver = new AdditionalComponentIntentResolver(1);
                AdditionalComponentIntentResolver serviceResolver = new AdditionalComponentIntentResolver(2);
                AdditionalComponentIntentResolver additionalComponentIntentResolver = new AdditionalComponentIntentResolver(1);
                AdditionalComponentIntentResolver additionalComponentIntentResolver2 = new AdditionalComponentIntentResolver(3);
                ArrayList<Object> activityList = AtlasHacks.PackageParser$Package_activities.get(packageObj);
                ArrayList<Object> serviceList = AtlasHacks.PackageParser$Package_services.get(packageObj);
                ArrayList<Object> providerList = AtlasHacks.PackageParser$Package_providers.get(packageObj);
                ArrayList<Object> receiverList = AtlasHacks.PackageParser$Package_receivers.get(packageObj);
                if (activityList != null) {
                    Iterator<Object> it = activityList.iterator();
                    while (it.hasNext()) {
                        Object activity = it.next();
                        try {
                            ActivityInfo activityInfo = (ActivityInfo) activity.getClass().getField("info").get(activity);
                            if (activityInfo.targetActivity != null) {
                                activityInfo.taskAffinity = RuntimeVariables.androidApplication.getApplicationInfo().taskAffinity;
                            }
                            activityInfo.processName = TextUtils.isEmpty(activityInfo.processName) ? RuntimeVariables.androidApplication.getPackageName() : activityInfo.processName;
                            activityInfo.packageName = RuntimeVariables.androidApplication.getApplicationInfo().packageName;
                            activityResolver.addComponent(activity);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (activityResolver != null && activityResolver.mComponents.size() > 0) {
                        setNewActivityResolver(activityResolver);
                    }
                }
                if (serviceList != null) {
                    Iterator<Object> it2 = serviceList.iterator();
                    while (it2.hasNext()) {
                        Object service = it2.next();
                        try {
                            ServiceInfo serviceInfo = (ServiceInfo) service.getClass().getField("info").get(service);
                            serviceInfo.packageName = RuntimeVariables.androidApplication.getApplicationInfo().packageName;
                            serviceInfo.processName = serviceInfo.processName == null ? RuntimeVariables.androidApplication.getPackageName() : serviceInfo.processName;
                            serviceResolver.addComponent(service);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    setNewServiceResolver(serviceResolver);
                }
                if (providerList != null) {
                    Iterator<Object> it3 = providerList.iterator();
                    while (it3.hasNext()) {
                        Object next = it3.next();
                        Object provider = providerList.get(0);
                        try {
                            ProviderInfo providerInfo = AtlasHacks.PackageParser$Provider_info.get(provider);
                            if (providerInfo.processName == null) {
                                str = RuntimeVariables.androidApplication.getPackageName();
                            } else {
                                str = providerInfo.processName;
                            }
                            providerInfo.processName = str;
                            additionalComponentIntentResolver2.addComponent(provider);
                            if (providerInfo.authority != null) {
                                String[] names = providerInfo.authority.split(SymbolExpUtil.SYMBOL_SEMICOLON);
                                providerInfo.authority = null;
                                for (int j = 0; j < names.length; j++) {
                                    if (!this.mProvidersByAuthority.containsKey(names[j])) {
                                        this.mProvidersByAuthority.put(names[j], provider);
                                        if (providerInfo.authority == null) {
                                            providerInfo.authority = names[j];
                                        } else {
                                            providerInfo.authority += SymbolExpUtil.SYMBOL_SEMICOLON + names[j];
                                        }
                                    }
                                }
                            }
                        } catch (Throwable e3) {
                            e3.printStackTrace();
                        }
                    }
                    setNewProviderResolver(additionalComponentIntentResolver2);
                }
                if (receiverList != null) {
                    Iterator<Object> it4 = receiverList.iterator();
                    while (it4.hasNext()) {
                        Object activity2 = it4.next();
                        try {
                            ActivityInfo activityInfo2 = (ActivityInfo) activity2.getClass().getField("info").get(activity2);
                            additionalComponentIntentResolver.addComponent(activity2);
                            activityInfo2.processName = activityInfo2.processName == null ? RuntimeVariables.androidApplication.getPackageName() : activityInfo2.processName;
                            ArrayList<IntentFilter> intentFilters = (ArrayList) activity2.getClass().getSuperclass().getDeclaredField("intents").get(activity2);
                            if (intentFilters != null) {
                                Iterator<IntentFilter> it5 = intentFilters.iterator();
                                while (it5.hasNext()) {
                                    this.mReceiverIntentFilters.put(it5.next(), activityInfo2.name);
                                }
                            }
                        } catch (Exception e4) {
                            e4.printStackTrace();
                        }
                    }
                    if (additionalComponentIntentResolver != null && additionalComponentIntentResolver.mComponents.size() > 0) {
                        setNewReceiverResolver(additionalComponentIntentResolver);
                    }
                }
            }
        } catch (Exception e5) {
            e5.printStackTrace();
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.Object parsePackage(java.lang.Object r9, android.content.res.Resources r10, android.content.res.XmlResourceParser r11) {
        /*
            r4 = 1
            r0 = 0
            java.lang.String[] r3 = new java.lang.String[r4]
            int r4 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x0111 }
            r5 = 26
            if (r4 < r5) goto L_0x0051
            android.taobao.atlas.hack.Hack$HackedClass<java.lang.Object> r4 = android.taobao.atlas.hack.AtlasHacks.PackageParser     // Catch:{ Throwable -> 0x0111 }
            java.lang.String r5 = "parseBaseApk"
            r6 = 5
            java.lang.Class[] r6 = new java.lang.Class[r6]     // Catch:{ Throwable -> 0x0111 }
            r7 = 0
            java.lang.Class<java.lang.String> r8 = java.lang.String.class
            r6[r7] = r8     // Catch:{ Throwable -> 0x0111 }
            r7 = 1
            java.lang.Class<android.content.res.Resources> r8 = android.content.res.Resources.class
            r6[r7] = r8     // Catch:{ Throwable -> 0x0111 }
            r7 = 2
            java.lang.Class<android.content.res.XmlResourceParser> r8 = android.content.res.XmlResourceParser.class
            r6[r7] = r8     // Catch:{ Throwable -> 0x0111 }
            r7 = 3
            java.lang.Class r8 = java.lang.Integer.TYPE     // Catch:{ Throwable -> 0x0111 }
            r6[r7] = r8     // Catch:{ Throwable -> 0x0111 }
            r7 = 4
            java.lang.Class<java.lang.String[]> r8 = java.lang.String[].class
            r6[r7] = r8     // Catch:{ Throwable -> 0x0111 }
            android.taobao.atlas.hack.Hack$HackedMethod r0 = r4.method(r5, r6)     // Catch:{ Throwable -> 0x0111 }
            r4 = 5
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0111 }
            r5 = 0
            android.app.Application r6 = android.taobao.atlas.runtime.RuntimeVariables.androidApplication     // Catch:{ Throwable -> 0x0111 }
            java.lang.String r6 = r6.getPackageName()     // Catch:{ Throwable -> 0x0111 }
            r4[r5] = r6     // Catch:{ Throwable -> 0x0111 }
            r5 = 1
            r4[r5] = r10     // Catch:{ Throwable -> 0x0111 }
            r5 = 2
            r4[r5] = r11     // Catch:{ Throwable -> 0x0111 }
            r5 = 3
            r6 = 0
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Throwable -> 0x0111 }
            r4[r5] = r6     // Catch:{ Throwable -> 0x0111 }
            r5 = 4
            r4[r5] = r3     // Catch:{ Throwable -> 0x0111 }
            java.lang.Object r4 = r0.invoke(r9, r4)     // Catch:{ Throwable -> 0x0111 }
        L_0x0050:
            return r4
        L_0x0051:
            int r4 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x0111 }
            r5 = 20
            if (r4 <= r5) goto L_0x0090
            android.taobao.atlas.hack.Hack$HackedClass<java.lang.Object> r4 = android.taobao.atlas.hack.AtlasHacks.PackageParser     // Catch:{ Throwable -> 0x0111 }
            java.lang.String r5 = "parseBaseApk"
            r6 = 4
            java.lang.Class[] r6 = new java.lang.Class[r6]     // Catch:{ Throwable -> 0x0111 }
            r7 = 0
            java.lang.Class<android.content.res.Resources> r8 = android.content.res.Resources.class
            r6[r7] = r8     // Catch:{ Throwable -> 0x0111 }
            r7 = 1
            java.lang.Class<android.content.res.XmlResourceParser> r8 = android.content.res.XmlResourceParser.class
            r6[r7] = r8     // Catch:{ Throwable -> 0x0111 }
            r7 = 2
            java.lang.Class r8 = java.lang.Integer.TYPE     // Catch:{ Throwable -> 0x0111 }
            r6[r7] = r8     // Catch:{ Throwable -> 0x0111 }
            r7 = 3
            java.lang.Class<java.lang.String[]> r8 = java.lang.String[].class
            r6[r7] = r8     // Catch:{ Throwable -> 0x0111 }
            android.taobao.atlas.hack.Hack$HackedMethod r0 = r4.method(r5, r6)     // Catch:{ Throwable -> 0x0111 }
            r4 = 4
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0111 }
            r5 = 0
            r4[r5] = r10     // Catch:{ Throwable -> 0x0111 }
            r5 = 1
            r4[r5] = r11     // Catch:{ Throwable -> 0x0111 }
            r5 = 2
            r6 = 0
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Throwable -> 0x0111 }
            r4[r5] = r6     // Catch:{ Throwable -> 0x0111 }
            r5 = 3
            r4[r5] = r3     // Catch:{ Throwable -> 0x0111 }
            java.lang.Object r4 = r0.invoke(r9, r4)     // Catch:{ Throwable -> 0x0111 }
            goto L_0x0050
        L_0x0090:
            android.taobao.atlas.hack.Hack$HackedClass<java.lang.Object> r4 = android.taobao.atlas.hack.AtlasHacks.PackageParser     // Catch:{ HackAssertionException -> 0x00c9 }
            java.lang.String r5 = "parsePackage"
            r6 = 4
            java.lang.Class[] r6 = new java.lang.Class[r6]     // Catch:{ HackAssertionException -> 0x00c9 }
            r7 = 0
            java.lang.Class<android.content.res.Resources> r8 = android.content.res.Resources.class
            r6[r7] = r8     // Catch:{ HackAssertionException -> 0x00c9 }
            r7 = 1
            java.lang.Class<android.content.res.XmlResourceParser> r8 = android.content.res.XmlResourceParser.class
            r6[r7] = r8     // Catch:{ HackAssertionException -> 0x00c9 }
            r7 = 2
            java.lang.Class r8 = java.lang.Integer.TYPE     // Catch:{ HackAssertionException -> 0x00c9 }
            r6[r7] = r8     // Catch:{ HackAssertionException -> 0x00c9 }
            r7 = 3
            java.lang.Class<java.lang.String[]> r8 = java.lang.String[].class
            r6[r7] = r8     // Catch:{ HackAssertionException -> 0x00c9 }
            android.taobao.atlas.hack.Hack$HackedMethod r0 = r4.method(r5, r6)     // Catch:{ HackAssertionException -> 0x00c9 }
            r4 = 4
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ HackAssertionException -> 0x00c9 }
            r5 = 0
            r4[r5] = r10     // Catch:{ HackAssertionException -> 0x00c9 }
            r5 = 1
            r4[r5] = r11     // Catch:{ HackAssertionException -> 0x00c9 }
            r5 = 2
            r6 = 0
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ HackAssertionException -> 0x00c9 }
            r4[r5] = r6     // Catch:{ HackAssertionException -> 0x00c9 }
            r5 = 3
            r4[r5] = r3     // Catch:{ HackAssertionException -> 0x00c9 }
            java.lang.Object r4 = r0.invoke(r9, r4)     // Catch:{ HackAssertionException -> 0x00c9 }
            goto L_0x0050
        L_0x00c9:
            r2 = move-exception
            android.taobao.atlas.hack.Hack$HackedClass<java.lang.Object> r4 = android.taobao.atlas.hack.AtlasHacks.PackageParser     // Catch:{ Throwable -> 0x0111 }
            java.lang.String r5 = "parsePackage"
            r6 = 5
            java.lang.Class[] r6 = new java.lang.Class[r6]     // Catch:{ Throwable -> 0x0111 }
            r7 = 0
            java.lang.Class<android.content.res.Resources> r8 = android.content.res.Resources.class
            r6[r7] = r8     // Catch:{ Throwable -> 0x0111 }
            r7 = 1
            java.lang.Class<android.content.res.XmlResourceParser> r8 = android.content.res.XmlResourceParser.class
            r6[r7] = r8     // Catch:{ Throwable -> 0x0111 }
            r7 = 2
            java.lang.Class r8 = java.lang.Integer.TYPE     // Catch:{ Throwable -> 0x0111 }
            r6[r7] = r8     // Catch:{ Throwable -> 0x0111 }
            r7 = 3
            java.lang.Class r8 = java.lang.Boolean.TYPE     // Catch:{ Throwable -> 0x0111 }
            r6[r7] = r8     // Catch:{ Throwable -> 0x0111 }
            r7 = 4
            java.lang.Class<java.lang.String[]> r8 = java.lang.String[].class
            r6[r7] = r8     // Catch:{ Throwable -> 0x0111 }
            android.taobao.atlas.hack.Hack$HackedMethod r0 = r4.method(r5, r6)     // Catch:{ Throwable -> 0x0111 }
            r4 = 5
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0111 }
            r5 = 0
            r4[r5] = r10     // Catch:{ Throwable -> 0x0111 }
            r5 = 1
            r4[r5] = r11     // Catch:{ Throwable -> 0x0111 }
            r5 = 2
            r6 = 0
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Throwable -> 0x0111 }
            r4[r5] = r6     // Catch:{ Throwable -> 0x0111 }
            r5 = 3
            r6 = 0
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r6)     // Catch:{ Throwable -> 0x0111 }
            r4[r5] = r6     // Catch:{ Throwable -> 0x0111 }
            r5 = 4
            r4[r5] = r3     // Catch:{ Throwable -> 0x0111 }
            java.lang.Object r4 = r0.invoke(r9, r4)     // Catch:{ Throwable -> 0x0111 }
            goto L_0x0050
        L_0x0111:
            r2 = move-exception
            android.app.Application r4 = android.taobao.atlas.runtime.RuntimeVariables.androidApplication
            android.content.pm.ApplicationInfo r1 = r4.getApplicationInfo()
            int r4 = r1.flags
            r4 = r4 & 2
            if (r4 == 0) goto L_0x0124
            java.lang.RuntimeException r4 = new java.lang.RuntimeException
            r4.<init>(r2)
            throw r4
        L_0x0124:
            r4 = 0
            goto L_0x0050
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.runtime.newcomponent.AdditionalPackageManager.parsePackage(java.lang.Object, android.content.res.Resources, android.content.res.XmlResourceParser):java.lang.Object");
    }

    /* access modifiers changed from: package-private */
    public void setNewActivityResolver(AdditionalComponentIntentResolver resolver) {
        this.mExternalActivity = resolver;
    }

    /* access modifiers changed from: package-private */
    public void setNewServiceResolver(AdditionalComponentIntentResolver resolver) {
        this.mExternalServices = resolver;
    }

    /* access modifiers changed from: package-private */
    public void setNewReceiverResolver(AdditionalComponentIntentResolver resolver) {
        this.mExternalReceivers = resolver;
    }

    /* access modifiers changed from: package-private */
    public void setNewProviderResolver(AdditionalComponentIntentResolver resolver) {
        this.mExternalProviders = resolver;
    }

    private <T extends ComponentInfo> ResolveInfo queryIntentResolveInfo(Intent intent, Class<T> infoClass) {
        AdditionalComponentIntentResolver resolver;
        if (intent == null) {
            return null;
        }
        if (infoClass == ActivityInfo.class) {
            resolver = this.mExternalActivity;
        } else {
            resolver = this.mExternalServices;
        }
        if (resolver == null) {
            return null;
        }
        ComponentName comp = intent.getComponent();
        if (comp == null && Build.VERSION.SDK_INT >= 15 && intent.getSelector() != null) {
            intent = intent.getSelector();
            comp = intent.getComponent();
        }
        if (comp != null) {
            Object obj = resolver.mComponents.get(comp);
            if (obj == null) {
                return null;
            }
            try {
                ResolveInfo ri = new ExternalResolverInfo();
                if (resolver == this.mExternalActivity) {
                    ri.activityInfo = (ActivityInfo) obj.getClass().getField("info").get(obj);
                    return ri;
                }
                ri.serviceInfo = (ServiceInfo) obj.getClass().getField("info").get(obj);
                return ri;
            } catch (Exception e) {
                return null;
            }
        } else if (!TextUtils.isEmpty(intent.getPackage()) && !TextUtils.equals(intent.getPackage(), RuntimeVariables.androidApplication.getPackageName())) {
            return null;
        } else {
            List<ResolveInfo> list = resolver.queryIntent(intent, intent.resolveTypeIfNeeded(RuntimeVariables.androidApplication.getContentResolver()), false);
            if (list == null || list.size() <= 0) {
                return null;
            }
            return new ExternalResolverInfo(list.get(0));
        }
    }

    public ProviderInfo resolveContentProvider(String name) {
        Object provider = this.mProvidersByAuthority.get(name);
        if (provider == null) {
            return null;
        }
        ProviderInfo pi = new ProviderInfo(AtlasHacks.PackageParser$Provider_info.get(provider));
        try {
            pi.metaData = (Bundle) provider.getClass().getSuperclass().getDeclaredField("metaData").get(provider);
            return pi;
        } catch (Throwable e) {
            e.printStackTrace();
            return pi;
        }
    }

    public List<ResolveInfo> queryIntentActivities(Intent intent) {
        ResolveInfo info = getInstance().queryIntentResolveInfo(intent, ActivityInfo.class);
        if (info == null) {
            return null;
        }
        List<ResolveInfo> rf = new ArrayList<>(1);
        rf.add(info);
        return rf;
    }

    public List<ResolveInfo> queryIntentService(Intent intent) {
        ResolveInfo info = getInstance().queryIntentResolveInfo(intent, ServiceInfo.class);
        if (info == null) {
            return null;
        }
        List<ResolveInfo> rf = new ArrayList<>(1);
        rf.add(info);
        return rf;
    }

    public <T extends ComponentInfo> T getNewComponentInfo(ComponentName comonent, Class<T> ComponentInfoClass) {
        Object obj;
        AdditionalComponentIntentResolver resolver = null;
        if (ComponentInfoClass == ActivityInfo.class) {
            resolver = this.mExternalActivity;
        } else if (ComponentInfoClass == ServiceInfo.class) {
            resolver = this.mExternalServices;
        } else if (ComponentInfoClass == ProviderInfo.class) {
            resolver = this.mExternalProviders;
        }
        if (!(resolver == null || resolver.mComponents == null || (obj = resolver.mComponents.get(comonent)) == null)) {
            try {
                return (ComponentInfo) obj.getClass().getField("info").get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }

    public ActivityInfo getReceiverInfo(ComponentName component) {
        Object obj;
        if (!(this.mExternalReceivers == null || this.mExternalReceivers.mComponents == null || (obj = this.mExternalReceivers.mComponents.get(component)) == null)) {
            try {
                return (ActivityInfo) obj.getClass().getField("info").get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }

    public List<ResolveInfo> queryIntentReceivers(Intent intent) {
        if (intent == null) {
            return null;
        }
        if (this.mExternalReceivers == null) {
            return null;
        }
        ComponentName comp = intent.getComponent();
        if (comp == null && Build.VERSION.SDK_INT >= 15 && intent.getSelector() != null) {
            intent = intent.getSelector();
            comp = intent.getComponent();
        }
        if (comp != null) {
            Object obj = this.mExternalReceivers.mComponents.get(comp);
            if (obj == null) {
                return null;
            }
            try {
                ArrayList arrayList = new ArrayList(1);
                new ResolveInfo().activityInfo = (ActivityInfo) obj.getClass().getField("info").get(obj);
                return arrayList;
            } catch (Exception e) {
                return null;
            }
        } else if (TextUtils.isEmpty(intent.getPackage()) || TextUtils.equals(intent.getPackage(), RuntimeVariables.androidApplication.getPackageName())) {
            return this.mExternalReceivers.queryIntent(intent, intent.resolveTypeIfNeeded(RuntimeVariables.androidApplication.getContentResolver()), false);
        } else {
            return null;
        }
    }

    public List<ProviderInfo> queryContentProviders(String processName) {
        if (this.mExternalProviders == null || this.mExternalProviders.mComponents == null) {
            return null;
        }
        List<ProviderInfo> infos = new ArrayList<>();
        try {
            for (Object provider : this.mExternalProviders.mComponents.values()) {
                ProviderInfo info = (ProviderInfo) provider.getClass().getField("info").get(provider);
                info.processName = TextUtils.isEmpty(info.processName) ? RuntimeVariables.androidApplication.getPackageName() : info.processName;
                if (info != null && (processName == null || processName.equals(info.processName))) {
                    infos.add(info);
                }
            }
            return infos;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public IntentFilter getAdditionIntentFilter() {
        if (this.mReceiverIntentFilters == null) {
            return null;
        }
        IntentFilter allAdditionalFilter = new IntentFilter();
        try {
            Field mActionsField = IntentFilter.class.getDeclaredField("mActions");
            mActionsField.setAccessible(true);
            for (IntentFilter filter : this.mReceiverIntentFilters.keySet()) {
                ArrayList<String> mActions = (ArrayList) mActionsField.get(filter);
                if (mActions != null) {
                    Iterator<String> it = mActions.iterator();
                    while (it.hasNext()) {
                        allAdditionalFilter.addAction(it.next());
                    }
                }
            }
            return allAdditionalFilter;
        } catch (Throwable e) {
            e.printStackTrace();
            return allAdditionalFilter;
        }
    }

    public static class ExternalResolverInfo extends ResolveInfo {
        public ExternalResolverInfo() {
        }

        public ExternalResolverInfo(ResolveInfo info) {
            super(info);
        }
    }
}
