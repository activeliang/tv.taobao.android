package android.taobao.atlas.runtime;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.taobao.atlas.hack.AndroidHack;
import android.taobao.atlas.hack.AtlasHacks;
import android.taobao.atlas.util.ApkUtils;
import android.taobao.atlas.util.log.impl.AtlasMonitor;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.TypedValue;
import java.io.File;
import java.io.FileInputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class DelegateResources extends Resources {
    public static final int APK_RES = 1;
    public static final int BUNDLE_RES = 0;
    /* access modifiers changed from: private */
    public static AssetManagerProcessor sAssetManagerProcessor;
    /* access modifiers changed from: private */
    public static String sAssetsPatchDir = null;
    /* access modifiers changed from: private */
    public static final ArrayList<String> sFailedAsssetPath = new ArrayList<>();
    /* access modifiers changed from: private */
    public static String sKernalPathPath = null;
    private static ResourceIdFetcher sResourcesFetcher;
    private static ResourcesProcessor sResourcesProcessor;
    private static ColorStateList walkroundStateList;
    private final HashMap<String, Resources> bundleResourceWalkRound = new HashMap<>();

    public DelegateResources(AssetManager assets, Resources res) {
        super(assets, res.getDisplayMetrics(), res.getConfiguration());
    }

    public XmlResourceParser getLayout(int id) throws Resources.NotFoundException {
        XmlResourceParser parser;
        XmlResourceParser result = null;
        Resources.NotFoundException exception = null;
        try {
            result = super.getLayout(id);
        } catch (Resources.NotFoundException e) {
            exception = e;
        }
        if (result != null || exception == null) {
            return result;
        }
        TypedValue value = new TypedValue();
        if (this == RuntimeVariables.delegateResources) {
        }
        Log.e("DelegateResources", "compare:" + (this == RuntimeVariables.delegateResources));
        getValue(id, value, true);
        int assetCookie = value.assetCookie;
        Log.e("DelegateResources", String.format("ID: %s|cookie: %s|string: %s", new Object[]{Integer.valueOf(id), Integer.valueOf(assetCookie), value.string}));
        try {
            String assetsPath = getGetCookieName(getAssets(), assetCookie);
            Log.e("DelegateResources", "target Path: " + assetsPath);
            if (!new File(assetsPath).exists()) {
                Log.e("DelegateResources", "target Path is not exist");
            }
            Resources res = getBackupResources(assetsPath);
            if (!(res == null || (parser = res.getLayout(id)) == null)) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("walkroundgetLayout", assetsPath);
                AtlasMonitor.getInstance().report(AtlasMonitor.WALKROUND_GETLAYOUT, detail, exception);
                return parser;
            }
        } catch (Throwable th) {
        }
        throw exception;
    }

    public Drawable getDrawable(int id, Resources.Theme theme) throws Resources.NotFoundException {
        Drawable result = null;
        Resources.NotFoundException exception = null;
        try {
            result = super.getDrawable(id, theme);
        } catch (Resources.NotFoundException e) {
            exception = e;
        }
        if (result != null || exception == null) {
            return result;
        }
        TypedValue value = new TypedValue();
        getValue(id, value, true);
        try {
            Resources res = getBackupResources(getGetCookieName(getAssets(), value.assetCookie));
            if (res != null) {
                return res.getDrawable(id, theme);
            }
        } catch (Throwable e2) {
            e2.printStackTrace();
        }
        throw exception;
    }

    private Resources getBackupResources(String assetsPath) {
        if (TextUtils.isEmpty(assetsPath)) {
            return null;
        }
        try {
            Resources res = this.bundleResourceWalkRound.get(assetsPath);
            if (res != null) {
                return res;
            }
            synchronized (assetsPath) {
                try {
                    Resources res2 = this.bundleResourceWalkRound.get(assetsPath);
                    if (res2 == null) {
                        AssetManager newAssetManager = AssetManager.class.newInstance();
                        File walkroundDir = new File(RuntimeVariables.androidApplication.getFilesDir(), "storage/res_backup");
                        if (!walkroundDir.exists()) {
                            walkroundDir.mkdirs();
                        }
                        File walkroundBackupAsset = new File(walkroundDir, new File(assetsPath).getName() + ".backup.zip");
                        if (!walkroundBackupAsset.exists() || walkroundBackupAsset.length() != new File(assetsPath).length()) {
                            if (walkroundBackupAsset.exists()) {
                                walkroundBackupAsset.delete();
                            }
                            ApkUtils.copyInputStreamToFile(new FileInputStream(assetsPath), walkroundBackupAsset);
                        }
                        AtlasHacks.AssetManager_addAssetPath.invoke(newAssetManager, walkroundBackupAsset.getAbsolutePath());
                        Resources res3 = new Resources(newAssetManager, getDisplayMetrics(), getConfiguration());
                        try {
                            this.bundleResourceWalkRound.put(assetsPath, res3);
                            res2 = res3;
                        } catch (Throwable th) {
                            th = th;
                            Resources resources = res3;
                            throw th;
                        }
                    }
                    return res2;
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void reset() {
        sKernalPathPath = null;
        sAssetsPatchDir = null;
    }

    public static void addBundleResources(String assetPath, String debugPath) throws Exception {
        synchronized (DelegateResources.class) {
            if (debugPath != null) {
                if (!findResByAssetIndexDescending()) {
                    updateResources(RuntimeVariables.delegateResources, debugPath, 0);
                }
            }
            updateResources(RuntimeVariables.delegateResources, assetPath, 0);
            if (debugPath != null && findResByAssetIndexDescending()) {
                updateResources(RuntimeVariables.delegateResources, debugPath, 0);
            }
        }
    }

    public static void addApkpatchResources(String assetPath) throws Exception {
        AtlasHacks.defineAndVerify();
        sKernalPathPath = assetPath;
        synchronized (DelegateResources.class) {
            updateResources(RuntimeVariables.delegateResources, assetPath, 1);
        }
    }

    public static String getCurrentAssetpathStr(AssetManager manager) {
        if (sAssetManagerProcessor != null) {
            return sAssetManagerProcessor.getCurrentAssetPathStr(manager);
        }
        return "";
    }

    public static List<String> getCurrentAssetPath(AssetManager manager) {
        if (sAssetManagerProcessor == null) {
            return new ArrayList();
        }
        AssetManagerProcessor assetManagerProcessor = sAssetManagerProcessor;
        return AssetManagerProcessor.getAssetPath(manager);
    }

    public static boolean checkAsset(String assetPath) {
        if (assetPath == null || sFailedAsssetPath.contains(assetPath)) {
            return false;
        }
        return true;
    }

    private static void updateResources(Resources res, String assetPath, int assertType) throws Exception {
        if (sAssetManagerProcessor == null) {
            sAssetManagerProcessor = new AssetManagerProcessor();
        }
        AssetManager updatedAssetManager = sAssetManagerProcessor.updateAssetManager(res.getAssets(), assetPath, assertType);
        if (sResourcesProcessor == null) {
            sResourcesProcessor = getResourceProcessor();
        }
        sResourcesProcessor.updateResources(updatedAssetManager);
        if (sResourcesFetcher == null) {
            sResourcesFetcher = new ResourceIdFetcher();
        }
    }

    private static ResourcesProcessor getResourceProcessor() {
        if (RuntimeVariables.delegateResources.getClass().getName().equals("android.content.res.MiuiResources")) {
            return new MiuiResourcesProcessor();
        }
        return new ResourcesProcessor();
    }

    public int getIdentifier(String name, String defType, String defPackage) {
        int id = super.getIdentifier(name, defType, defPackage);
        if (id == 0 && sResourcesFetcher != null) {
            return sResourcesFetcher.getIdentifier(name, defType, defPackage);
        }
        return id;
    }

    /* access modifiers changed from: private */
    public static boolean findResByAssetIndexDescending() {
        if (Build.VERSION.SDK_INT > 20) {
            return false;
        }
        return true;
    }

    private static class AssetManagerProcessor {
        private static HashMap<String, Boolean> sDefaultAssetPathList;
        private static String sWebviewPath = null;
        private LinkedHashMap<String, Boolean> assetPathCache;
        private boolean hasCreatedAssetsManager;
        private LinkedHashMap<String, Boolean> preAssetPathCache;

        static {
            try {
                ArrayList<String> defaultPaths = getAssetPath(AssetManager.class.newInstance());
                if (defaultPaths != null && defaultPaths.size() > 0) {
                    sDefaultAssetPathList = new HashMap<>();
                    Iterator<String> it = defaultPaths.iterator();
                    while (it.hasNext()) {
                        sDefaultAssetPathList.put(it.next(), Boolean.FALSE);
                    }
                }
                if (sDefaultAssetPathList == null) {
                    sDefaultAssetPathList = new HashMap<>(0);
                }
            } catch (Throwable th) {
                if (sDefaultAssetPathList == null) {
                    sDefaultAssetPathList = new HashMap<>(0);
                }
                throw th;
            }
        }

        public AssetManagerProcessor() {
            this.assetPathCache = null;
            this.preAssetPathCache = null;
            this.hasCreatedAssetsManager = false;
            this.assetPathCache = new LinkedHashMap<>();
            this.preAssetPathCache = new LinkedHashMap<>();
            this.assetPathCache.put(RuntimeVariables.androidApplication.getApplicationInfo().sourceDir, Boolean.FALSE);
        }

        public AssetManager updateAssetManager(AssetManager manager, String newAssetPath, int assetType) throws Exception {
            AssetManager targetManager;
            if (assetType == 0) {
                if (supportExpandAssetManager()) {
                    try {
                        targetManager = updateAssetManagerWithAppend(manager, newAssetPath, 0);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        Log.e("DelegateResources", "walkround to createNewAssetmanager");
                        targetManager = createNewAssetManager(manager, newAssetPath, true, 0);
                    }
                } else {
                    targetManager = createNewAssetManager(manager, newAssetPath, true, 0);
                }
                updateAssetPathList(newAssetPath, true);
                return targetManager;
            }
            File newAssetsDir = new File(new File(newAssetPath).getParent(), "newAssets");
            if (newAssetsDir.exists() && new File(newAssetsDir, "assets").exists()) {
                String unused = DelegateResources.sAssetsPatchDir = newAssetsDir.getAbsolutePath();
            }
            if (supportExpandAssetManager()) {
                if (DelegateResources.findResByAssetIndexDescending()) {
                    AssetManager targetManager2 = updateAssetManagerWithAppend(manager, newAssetPath, assetType);
                    updateAssetPathList(newAssetPath, true);
                    return targetManager2;
                }
                AssetManager targetManager3 = createNewAssetManager(manager, newAssetPath, false, assetType);
                updateAssetPathList(newAssetPath, false);
                return targetManager3;
            } else if (DelegateResources.findResByAssetIndexDescending()) {
                AssetManager targetManager4 = createNewAssetManager(manager, newAssetPath, true, assetType);
                updateAssetPathList(newAssetPath, true);
                return targetManager4;
            } else {
                AssetManager targetManager5 = createNewAssetManager(manager, newAssetPath, false, assetType);
                updateAssetPathList(newAssetPath, false);
                return targetManager5;
            }
        }

        public List<String> getCurrentAssetPath(AssetManager target) {
            if (DelegateResources.sAssetManagerProcessor == null) {
                return new ArrayList();
            }
            AssetManagerProcessor unused = DelegateResources.sAssetManagerProcessor;
            return getAssetPath(target);
        }

        public String getCurrentAssetPathStr(AssetManager target) {
            if (target == null) {
                return "";
            }
            List<String> currentList = getAssetPath(target);
            StringBuffer sb = new StringBuffer();
            sb.append("newDelegateResources [");
            if (currentList != null) {
                for (String path : currentList) {
                    sb.append(path).append(",");
                }
            }
            sb.append("]");
            return sb.toString();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:42:0x00f2, code lost:
            throw new java.lang.RuntimeException("no valid addassetpathnative method");
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private android.content.res.AssetManager updateAssetManagerWithAppend(android.content.res.AssetManager r19, java.lang.String r20, int r21) throws java.lang.Exception {
            /*
                r18 = this;
                monitor-enter(r19)
                r11 = 1
                r0 = r21
                if (r0 != r11) goto L_0x0024
                r11 = 0
                r0 = r18
                r1 = r19
                r2 = r20
                r0.appendAssetPath(r1, r2, r11)     // Catch:{ all -> 0x0035 }
                java.lang.String r11 = android.taobao.atlas.runtime.DelegateResources.sAssetsPatchDir     // Catch:{ all -> 0x0035 }
                if (r11 == 0) goto L_0x0022
                java.lang.String r11 = android.taobao.atlas.runtime.DelegateResources.sAssetsPatchDir     // Catch:{ all -> 0x0035 }
                r12 = 0
                r0 = r18
                r1 = r19
                r0.appendAssetPath(r1, r11, r12)     // Catch:{ all -> 0x0035 }
            L_0x0022:
                monitor-exit(r19)     // Catch:{ all -> 0x0035 }
                return r19
            L_0x0024:
                int r11 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x0035 }
                r12 = 28
                if (r11 < r12) goto L_0x0038
                r11 = 0
                r0 = r18
                r1 = r19
                r2 = r20
                r0.appendAssetPath(r1, r2, r11)     // Catch:{ all -> 0x0035 }
                goto L_0x0022
            L_0x0035:
                r11 = move-exception
                monitor-exit(r19)     // Catch:{ all -> 0x0035 }
                throw r11
            L_0x0038:
                r9 = 2
                r3 = 0
            L_0x003a:
                int r9 = r9 + -1
                android.taobao.atlas.hack.Hack$HackedMethod r11 = android.taobao.atlas.hack.AtlasHacks.AssetManager_addAssetPathNative     // Catch:{ all -> 0x0035 }
                if (r11 == 0) goto L_0x0097
                android.taobao.atlas.hack.Hack$HackedMethod r11 = android.taobao.atlas.hack.AtlasHacks.AssetManager_addAssetPathNative     // Catch:{ all -> 0x0035 }
                java.lang.reflect.Method r11 = r11.getMethod()     // Catch:{ all -> 0x0035 }
                if (r11 == 0) goto L_0x0097
                android.taobao.atlas.hack.Hack$HackedMethod r11 = android.taobao.atlas.hack.AtlasHacks.AssetManager_addAssetPathNative     // Catch:{ all -> 0x0035 }
                r12 = 1
                java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x0035 }
                r13 = 0
                r12[r13] = r20     // Catch:{ all -> 0x0035 }
                r0 = r19
                java.lang.Object r11 = r11.invoke(r0, r12)     // Catch:{ all -> 0x0035 }
                java.lang.Integer r11 = (java.lang.Integer) r11     // Catch:{ all -> 0x0035 }
                int r3 = r11.intValue()     // Catch:{ all -> 0x0035 }
            L_0x005c:
                if (r3 <= 0) goto L_0x00f3
            L_0x005e:
                if (r3 <= 0) goto L_0x012c
                android.taobao.atlas.hack.Hack$HackedField<android.content.res.AssetManager, java.lang.Object> r11 = android.taobao.atlas.hack.AtlasHacks.AssetManager_mStringBlocks     // Catch:{ all -> 0x0035 }
                r0 = r19
                java.lang.Object r11 = r11.get(r0)     // Catch:{ all -> 0x0035 }
                java.lang.Object[] r11 = (java.lang.Object[]) r11     // Catch:{ all -> 0x0035 }
                r0 = r11
                java.lang.Object[] r0 = (java.lang.Object[]) r0     // Catch:{ all -> 0x0035 }
                r6 = r0
                int r10 = r6.length     // Catch:{ all -> 0x0035 }
                android.taobao.atlas.hack.Hack$HackedMethod r11 = android.taobao.atlas.hack.AtlasHacks.AssetManager_getStringBlockCount     // Catch:{ all -> 0x0035 }
                r12 = 0
                java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x0035 }
                r0 = r19
                java.lang.Object r11 = r11.invoke(r0, r12)     // Catch:{ all -> 0x0035 }
                java.lang.Integer r11 = (java.lang.Integer) r11     // Catch:{ all -> 0x0035 }
                int r8 = r11.intValue()     // Catch:{ all -> 0x0035 }
                android.taobao.atlas.hack.Hack$HackedClass<java.lang.Object> r11 = android.taobao.atlas.hack.AtlasHacks.StringBlock     // Catch:{ all -> 0x0035 }
                java.lang.Class r11 = r11.getmClass()     // Catch:{ all -> 0x0035 }
                java.lang.Object r7 = java.lang.reflect.Array.newInstance(r11, r8)     // Catch:{ all -> 0x0035 }
                r5 = 0
            L_0x008b:
                if (r5 >= r8) goto L_0x0123
                if (r5 >= r10) goto L_0x00f7
                r11 = r6[r5]     // Catch:{ all -> 0x0035 }
                java.lang.reflect.Array.set(r7, r5, r11)     // Catch:{ all -> 0x0035 }
            L_0x0094:
                int r5 = r5 + 1
                goto L_0x008b
            L_0x0097:
                android.taobao.atlas.hack.Hack$HackedMethod r11 = android.taobao.atlas.hack.AtlasHacks.AssetManager_addAssetPathNative24     // Catch:{ all -> 0x0035 }
                if (r11 == 0) goto L_0x00c0
                android.taobao.atlas.hack.Hack$HackedMethod r11 = android.taobao.atlas.hack.AtlasHacks.AssetManager_addAssetPathNative24     // Catch:{ all -> 0x0035 }
                java.lang.reflect.Method r11 = r11.getMethod()     // Catch:{ all -> 0x0035 }
                if (r11 == 0) goto L_0x00c0
                android.taobao.atlas.hack.Hack$HackedMethod r11 = android.taobao.atlas.hack.AtlasHacks.AssetManager_addAssetPathNative24     // Catch:{ all -> 0x0035 }
                r12 = 2
                java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x0035 }
                r13 = 0
                r12[r13] = r20     // Catch:{ all -> 0x0035 }
                r13 = 1
                r14 = 0
                java.lang.Boolean r14 = java.lang.Boolean.valueOf(r14)     // Catch:{ all -> 0x0035 }
                r12[r13] = r14     // Catch:{ all -> 0x0035 }
                r0 = r19
                java.lang.Object r11 = r11.invoke(r0, r12)     // Catch:{ all -> 0x0035 }
                java.lang.Integer r11 = (java.lang.Integer) r11     // Catch:{ all -> 0x0035 }
                int r3 = r11.intValue()     // Catch:{ all -> 0x0035 }
                goto L_0x005c
            L_0x00c0:
                android.taobao.atlas.hack.Hack$HackedMethod r11 = android.taobao.atlas.hack.AtlasHacks.AssetManager_addAssetPathNativeSamSung     // Catch:{ all -> 0x0035 }
                if (r11 == 0) goto L_0x00ea
                android.taobao.atlas.hack.Hack$HackedMethod r11 = android.taobao.atlas.hack.AtlasHacks.AssetManager_addAssetPathNativeSamSung     // Catch:{ all -> 0x0035 }
                java.lang.reflect.Method r11 = r11.getMethod()     // Catch:{ all -> 0x0035 }
                if (r11 == 0) goto L_0x00ea
                android.taobao.atlas.hack.Hack$HackedMethod r11 = android.taobao.atlas.hack.AtlasHacks.AssetManager_addAssetPathNativeSamSung     // Catch:{ all -> 0x0035 }
                r12 = 2
                java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x0035 }
                r13 = 0
                r12[r13] = r20     // Catch:{ all -> 0x0035 }
                r13 = 1
                r14 = 0
                java.lang.Integer r14 = java.lang.Integer.valueOf(r14)     // Catch:{ all -> 0x0035 }
                r12[r13] = r14     // Catch:{ all -> 0x0035 }
                r0 = r19
                java.lang.Object r11 = r11.invoke(r0, r12)     // Catch:{ all -> 0x0035 }
                java.lang.Integer r11 = (java.lang.Integer) r11     // Catch:{ all -> 0x0035 }
                int r3 = r11.intValue()     // Catch:{ all -> 0x0035 }
                goto L_0x005c
            L_0x00ea:
                java.lang.RuntimeException r11 = new java.lang.RuntimeException     // Catch:{ all -> 0x0035 }
                java.lang.String r12 = "no valid addassetpathnative method"
                r11.<init>(r12)     // Catch:{ all -> 0x0035 }
                throw r11     // Catch:{ all -> 0x0035 }
            L_0x00f3:
                if (r9 > 0) goto L_0x003a
                goto L_0x005e
            L_0x00f7:
                android.taobao.atlas.hack.Hack$HackedConstructor r11 = android.taobao.atlas.hack.AtlasHacks.StringBlock_constructor     // Catch:{ all -> 0x0035 }
                r12 = 2
                java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x0035 }
                r13 = 0
                android.taobao.atlas.hack.Hack$HackedMethod r14 = android.taobao.atlas.hack.AtlasHacks.AssetManager_getNativeStringBlock     // Catch:{ all -> 0x0035 }
                r15 = 1
                java.lang.Object[] r15 = new java.lang.Object[r15]     // Catch:{ all -> 0x0035 }
                r16 = 0
                java.lang.Integer r17 = java.lang.Integer.valueOf(r5)     // Catch:{ all -> 0x0035 }
                r15[r16] = r17     // Catch:{ all -> 0x0035 }
                r0 = r19
                java.lang.Object r14 = r14.invoke(r0, r15)     // Catch:{ all -> 0x0035 }
                r12[r13] = r14     // Catch:{ all -> 0x0035 }
                r13 = 1
                r14 = 1
                java.lang.Boolean r14 = java.lang.Boolean.valueOf(r14)     // Catch:{ all -> 0x0035 }
                r12[r13] = r14     // Catch:{ all -> 0x0035 }
                java.lang.Object r11 = r11.getInstance(r12)     // Catch:{ all -> 0x0035 }
                java.lang.reflect.Array.set(r7, r5, r11)     // Catch:{ all -> 0x0035 }
                goto L_0x0094
            L_0x0123:
                android.taobao.atlas.hack.Hack$HackedField<android.content.res.AssetManager, java.lang.Object> r11 = android.taobao.atlas.hack.AtlasHacks.AssetManager_mStringBlocks     // Catch:{ all -> 0x0035 }
                r0 = r19
                r11.set(r0, r7)     // Catch:{ all -> 0x0035 }
                goto L_0x0022
            L_0x012c:
                java.util.ArrayList r11 = android.taobao.atlas.runtime.DelegateResources.sFailedAsssetPath     // Catch:{ all -> 0x0035 }
                r0 = r20
                r11.add(r0)     // Catch:{ all -> 0x0035 }
                java.util.HashMap r4 = new java.util.HashMap     // Catch:{ all -> 0x0035 }
                r4.<init>()     // Catch:{ all -> 0x0035 }
                java.lang.String r11 = "appendAssetPath"
                r0 = r20
                r4.put(r11, r0)     // Catch:{ all -> 0x0035 }
                android.taobao.atlas.util.log.impl.AtlasMonitor r11 = android.taobao.atlas.util.log.impl.AtlasMonitor.getInstance()     // Catch:{ all -> 0x0035 }
                java.lang.String r12 = "container_append_assetpath_fail"
                java.lang.RuntimeException r13 = new java.lang.RuntimeException     // Catch:{ all -> 0x0035 }
                r13.<init>()     // Catch:{ all -> 0x0035 }
                r11.report(r12, r4, r13)     // Catch:{ all -> 0x0035 }
                goto L_0x0022
            */
            throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.runtime.DelegateResources.AssetManagerProcessor.updateAssetManagerWithAppend(android.content.res.AssetManager, java.lang.String, int):android.content.res.AssetManager");
        }

        private AssetManager createNewAssetManager(AssetManager srcManager, String newAssetPath, boolean append, int type) throws Exception {
            AssetManager newAssetManager = AssetManager.class.newInstance();
            List<String> runtimeAdditionalAssets = new ArrayList<>();
            for (String currentPath : getAssetPath(srcManager)) {
                if (!sDefaultAssetPathList.containsKey(currentPath) && !this.assetPathCache.containsKey(currentPath) && !this.preAssetPathCache.containsKey(currentPath) && !currentPath.equals(newAssetPath)) {
                    if (currentPath.toLowerCase().contains("webview") || currentPath.toLowerCase().contains("chrome")) {
                        runtimeAdditionalAssets.add(currentPath);
                    }
                }
            }
            if (Build.VERSION.SDK_INT >= 24) {
                if (TextUtils.isEmpty(sWebviewPath)) {
                    try {
                        PackageInfo info = (PackageInfo) Class.forName("android.webkit.WebViewFactory").getDeclaredMethod("getLoadedPackageInfo", new Class[0]).invoke((Object) null, new Object[0]);
                        if (!(info == null || info.applicationInfo == null)) {
                            sWebviewPath = info.applicationInfo.sourceDir;
                        }
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(sWebviewPath) && !runtimeAdditionalAssets.contains(sWebviewPath)) {
                    Log.e("DelegateResource", "special webviewPath: " + sWebviewPath);
                    runtimeAdditionalAssets.add(sWebviewPath);
                }
            }
            DelegateResources.sFailedAsssetPath.clear();
            if (!append) {
                appendAssetPath(newAssetManager, newAssetPath, false);
            }
            if (this.preAssetPathCache != null) {
                if (this.preAssetPathCache.size() == 1) {
                    appendAssetPath(newAssetManager, (String) this.preAssetPathCache.entrySet().iterator().next().getKey(), false);
                } else {
                    ListIterator<Map.Entry<String, Boolean>> i = new ArrayList(this.preAssetPathCache.entrySet()).listIterator(this.preAssetPathCache.size());
                    while (i.hasPrevious()) {
                        appendAssetPath(newAssetManager, i.previous().getKey(), false);
                    }
                }
            }
            if (this.assetPathCache != null) {
                for (Map.Entry<String, Boolean> entry : this.assetPathCache.entrySet()) {
                    if (!sDefaultAssetPathList.containsKey(entry.getKey())) {
                        appendAssetPath(newAssetManager, entry.getKey(), false);
                    }
                }
            }
            if (append) {
                appendAssetPath(newAssetManager, newAssetPath, false);
            }
            if (!runtimeAdditionalAssets.isEmpty()) {
                for (String additional : runtimeAdditionalAssets) {
                    if (Build.VERSION.SDK_INT < 24) {
                        appendAssetPath(newAssetManager, additional, false);
                    } else {
                        appendAssetPath(newAssetManager, additional, true);
                    }
                }
            }
            if (DelegateResources.sAssetsPatchDir != null) {
                appendAssetPath(newAssetManager, DelegateResources.sAssetsPatchDir, false);
            }
            return newAssetManager;
        }

        private synchronized boolean supportExpandAssetManager() {
            boolean z = true;
            synchronized (this) {
                if (!this.hasCreatedAssetsManager || Build.VERSION.SDK_INT <= 20 || Build.BRAND.equalsIgnoreCase("sony") || Build.BRAND.equalsIgnoreCase("semc") || (Build.BRAND.equalsIgnoreCase("xiaomi") && Build.MODEL.toLowerCase().startsWith("mibox"))) {
                    this.hasCreatedAssetsManager = true;
                    z = false;
                }
            }
            return z;
        }

        private boolean supportAddAssetPathNative() {
            if (AtlasHacks.AssetManager_addAssetPathNative == null && AtlasHacks.AssetManager_addAssetPathNative24 == null && AtlasHacks.AssetManager_addAssetPathNativeSamSung == null) {
                return false;
            }
            return true;
        }

        private void updateAssetPathList(String newAssetPath, boolean append) {
            if (append) {
                this.assetPathCache.put(newAssetPath, Boolean.FALSE);
            } else {
                this.preAssetPathCache.put(newAssetPath, Boolean.FALSE);
            }
            if (DelegateResources.sKernalPathPath == null) {
                return;
            }
            if (DelegateResources.sFailedAsssetPath.contains(DelegateResources.sKernalPathPath)) {
                throw new RuntimeException("maindex arsc inject fail");
            } else if (DelegateResources.sAssetsPatchDir != null && DelegateResources.sFailedAsssetPath.contains(DelegateResources.sAssetsPatchDir)) {
                throw new RuntimeException("maindex assets inject fail");
            }
        }

        private boolean appendAssetPath(AssetManager asset, String path, boolean shared) throws Exception {
            boolean result = false;
            int cookie = addAssetPathInternal(asset, path, shared);
            if (cookie == 0) {
                for (int i = 0; i < 3 && (cookie = addAssetPathInternal(asset, path, shared)) == 0; i++) {
                }
            }
            if (cookie == 0) {
                DelegateResources.sFailedAsssetPath.add(path);
            } else {
                result = true;
            }
            if (!result) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("appendAssetPath", path);
                AtlasMonitor.getInstance().report(AtlasMonitor.CONTAINER_APPEND_ASSETPATH_FAIL, detail, new RuntimeException());
            }
            return result;
        }

        private int addAssetPathInternal(AssetManager asset, String path, boolean shared) throws Exception {
            if (shared) {
                return ((Integer) AtlasHacks.AssetManager_addAssetPathAsSharedLibrary.invoke(asset, path)).intValue();
            }
            return ((Integer) AtlasHacks.AssetManager_addAssetPath.invoke(asset, path)).intValue();
        }

        public static ArrayList<String> getAssetPath(AssetManager manager) {
            ArrayList<String> assetPaths = new ArrayList<>();
            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    Method method = AssetManager.class.getDeclaredMethod("getApkAssets", new Class[0]);
                    method.setAccessible(true);
                    for (Object apkAsset : (Object[]) method.invoke(manager, new Object[0])) {
                        String assetsPath = (String) apkAsset.getClass().getMethod("getAssetPath", new Class[0]).invoke(apkAsset, new Object[0]);
                        if (sDefaultAssetPathList == null || (!TextUtils.isEmpty(assetsPath) && !sDefaultAssetPathList.containsKey(assetsPath))) {
                            assetPaths.add(assetsPath);
                        }
                    }
                } else {
                    Method method2 = manager.getClass().getDeclaredMethod("getStringBlockCount", new Class[0]);
                    method2.setAccessible(true);
                    int assetsPathCount = ((Integer) method2.invoke(manager, new Object[0])).intValue();
                    for (int x = 0; x < assetsPathCount; x++) {
                        String assetsPath2 = DelegateResources.getGetCookieName(manager, x + 1);
                        if (sDefaultAssetPathList == null || (!TextUtils.isEmpty(assetsPath2) && !sDefaultAssetPathList.containsKey(assetsPath2))) {
                            assetPaths.add(assetsPath2);
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return assetPaths;
        }
    }

    public static String getGetCookieName(AssetManager manager, int i) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (Build.VERSION.SDK_INT >= 28) {
            Method method = AssetManager.class.getDeclaredMethod("getApkAssets", new Class[0]);
            method.setAccessible(true);
            Object apkAsset = ((Object[]) method.invoke(manager, new Object[0]))[i - 1];
            return (String) apkAsset.getClass().getMethod("getAssetPath", new Class[0]).invoke(apkAsset, new Object[0]);
        }
        return (String) AssetManager.class.getMethod("getCookieName", new Class[]{Integer.TYPE}).invoke(manager, new Object[]{Integer.valueOf(i)});
    }

    public static class ResourcesProcessor {
        public void updateResources(AssetManager assetManager) throws Exception {
            if (RuntimeVariables.delegateResources.getAssets() != assetManager || RuntimeVariables.delegateResources == null || !(RuntimeVariables.delegateResources instanceof DelegateResources)) {
                RuntimeVariables.delegateResources = createNewResources(assetManager);
                DelegateResources.walkroundActionMenuTextColor(RuntimeVariables.delegateResources);
                AndroidHack.injectResources(RuntimeVariables.androidApplication, RuntimeVariables.delegateResources);
            }
        }

        /* access modifiers changed from: package-private */
        public Resources createNewResources(AssetManager manager) throws Exception {
            return new DelegateResources(manager, RuntimeVariables.delegateResources);
        }
    }

    public static class MiuiResourcesProcessor extends ResourcesProcessor {
        /* access modifiers changed from: package-private */
        public Resources createNewResources(AssetManager manager) throws Exception {
            if (Build.VERSION.SDK_INT > 20) {
                return super.createNewResources(manager);
            }
            Constructor<?> miuiCons = Class.forName("android.content.res.MiuiResources").getDeclaredConstructor(new Class[]{AssetManager.class, DisplayMetrics.class, Configuration.class});
            miuiCons.setAccessible(true);
            return (Resources) miuiCons.newInstance(new Object[]{manager, RuntimeVariables.delegateResources.getDisplayMetrics(), RuntimeVariables.delegateResources.getConfiguration()});
        }
    }

    public static void walkroundActionMenuTextColor(Resources res) {
        try {
            if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT <= 19) {
                if (walkroundStateList == null) {
                    walkroundStateList = ColorStateList.valueOf(Color.rgb(0, 0, 0));
                }
                Field mColorStateListCacheField = AndroidHack.findField(res, "mColorStateListCache");
                mColorStateListCacheField.setAccessible(true);
                ((LongSparseArray) mColorStateListCacheField.get(res)).put(-2164195198L, new WeakReference(walkroundStateList));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
