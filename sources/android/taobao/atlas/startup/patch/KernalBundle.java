package android.taobao.atlas.startup.patch;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.taobao.atlas.startup.KernalVersionManager;
import android.taobao.atlas.startup.NClassLoader;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.taobao.atlas.dexmerge.MergeConstants;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipFile;

public class KernalBundle {
    public static String KERNAL_BUNDLE_NAME = MergeConstants.MAIN_DEX;
    static List<String> blackList = Arrays.asList(new String[]{"/Plugin/", "/virtual/"});
    static Class[] constructorArgs1 = {File.class, Boolean.TYPE, File.class, DexFile.class};
    static Class[] constructorArgs2 = {File.class, File.class, DexFile.class};
    static Class[] constructorArgs3 = {File.class, ZipFile.class, DexFile.class};
    static Class[] constructorArgsO = {DexFile.class, File.class};
    public static KernalBundle kernalBundle = null;
    public static boolean nativeLibPatched;
    public static boolean patchWithApk = false;
    private Class FrameworkPropertiesClazz;
    KernalBundleArchive archive;
    File bundleDir;
    private NClassLoader replaceClassLoader;

    public static boolean checkloadKernalBundle(Application application, String currentProcessName) {
        File updateDir = new File(KernalConstants.baseContext.getFilesDir(), "storage");
        File dexPatchDir = updateDir;
        if (!TextUtils.isEmpty(KernalVersionManager.instance().DEXPATCH_STORAGE_LOCATION)) {
            dexPatchDir = new File(KernalVersionManager.instance().DEXPATCH_STORAGE_LOCATION);
        }
        if (!TextUtils.isEmpty(KernalVersionManager.instance().CURRENT_STORAGE_LOCATION)) {
            updateDir = new File(KernalVersionManager.instance().CURRENT_STORAGE_LOCATION);
        }
        File kernalUpdateDir = new File(updateDir, KERNAL_BUNDLE_NAME);
        File kernalDexPatchDir = new File(dexPatchDir, KERNAL_BUNDLE_NAME);
        if (!kernalUpdateDir.exists() && !kernalDexPatchDir.exists()) {
            return false;
        }
        try {
            kernalBundle = new KernalBundle(kernalUpdateDir, kernalDexPatchDir, KernalVersionManager.instance().getBaseBundleVersion(KERNAL_BUNDLE_NAME), currentProcessName);
            if (isInBlackList(kernalBundle)) {
                throw new IOException(kernalBundle.getArchive().getArchiveFile().getAbsolutePath());
            }
            kernalBundle.patchKernalDex(application);
            kernalBundle.patchKernalResource(application);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            kernalBundle = null;
            deleteDirectory(kernalUpdateDir);
            if (!kernalDexPatchDir.exists()) {
                return false;
            }
            deleteDirectory(kernalDexPatchDir);
            return false;
        }
    }

    private static boolean isInBlackList(KernalBundle kernalBundle2) {
        if (!(kernalBundle2 == null || kernalBundle2.getArchive() == null || kernalBundle2.getArchive().getArchiveFile() == null)) {
            for (String s : blackList) {
                if (kernalBundle2.getArchive().getArchiveFile().getAbsolutePath().contains(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkLoadKernalDebugPatch(Application application) {
        if (Build.VERSION.SDK_INT < 21) {
            return false;
        }
        boolean loadKernalPatch = false;
        try {
            if (!((application.getApplicationInfo().flags & 2) != 0)) {
                return false;
            }
            File file = new File(new File(KernalConstants.baseContext.getExternalFilesDir("debug_storage"), KERNAL_BUNDLE_NAME), "patch.zip");
            if (!file.exists()) {
                return false;
            }
            loadKernalPatch = true;
            KernalBundle bundle = new KernalBundle();
            File internalDebugBundleDir = new File(new File(application.getFilesDir(), "debug_storage"), KERNAL_BUNDLE_NAME);
            internalDebugBundleDir.mkdirs();
            DexFile patchDexFile = KernalConstants.dexBooster.loadDex(KernalConstants.baseContext, file.getAbsolutePath(), new File(internalDebugBundleDir, "patch.dex").getAbsolutePath(), 0, true);
            if (bundle.needReplaceClassLoader(application)) {
                NClassLoader.replacePathClassLoader(KernalConstants.baseContext, KernalBundle.class.getClassLoader(), new NClassLoader(".", KernalBundle.class.getClassLoader().getParent()));
            }
            bundle.installKernalBundle(KernalConstants.baseContext.getClassLoader(), file, new DexFile[]{patchDexFile}, (File) null, true);
            bundle.prepareRuntimeVariables(application);
            Class DelegateResourcesClazz = application.getClassLoader().loadClass("android.taobao.atlas.runtime.DelegateResources");
            DelegateResourcesClazz.getDeclaredMethod("addApkpatchResources", new Class[]{String.class}).invoke(DelegateResourcesClazz, new Object[]{file.getAbsolutePath()});
            Toast.makeText(KernalConstants.baseContext, "当前处于DEBUG调试状态，不支持动态更新，清除数据可恢复", 1).show();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } catch (Throwable th) {
            return loadKernalPatch;
        }
    }

    public static boolean hasKernalPatch() {
        return KernalVersionManager.instance().isUpdated(KERNAL_BUNDLE_NAME) || KernalVersionManager.instance().isDexPatched(KERNAL_BUNDLE_NAME);
    }

    public static boolean hasNativeLibPatch(Context base) {
        try {
            if (new File(base.getFilesDir(), String.format("nativeLib-%s", new Object[]{base.getPackageManager().getPackageInfo(base.getPackageName(), 0).versionName})).exists()) {
                if (new File(new File(base.getFilesDir(), String.format("nativeLib-%s", new Object[]{base.getPackageManager().getPackageInfo(base.getPackageName(), 0).versionName})), "mark").exists()) {
                    return true;
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void clear() {
        File kernalDir = new File(new File(KernalConstants.baseContext.getFilesDir(), "storage"), KERNAL_BUNDLE_NAME);
        if (kernalDir.exists()) {
            deleteDirectory(kernalDir);
        }
    }

    private KernalBundle() {
    }

    public KernalBundle(File bundleDir2, File file, String version, long dexPatchVersion) throws Exception {
        this.bundleDir = bundleDir2;
        try {
            this.archive = new KernalBundleArchive(bundleDir2, file, version, dexPatchVersion);
        } catch (IOException e) {
            e.printStackTrace();
            if (bundleDir2.exists()) {
                deleteDirectory(bundleDir2);
            }
            throw new IOException("install kernal Bundlele fail ", e);
        }
    }

    public KernalBundle(File updateDir, File dexPatchDir, String version, String process) throws Exception {
        long dexPatchVersion = KernalVersionManager.instance().getDexPatchBundleVersion(KERNAL_BUNDLE_NAME);
        if (dexPatchVersion > 0) {
            try {
                this.bundleDir = dexPatchDir;
                this.archive = new KernalBundleArchive(KernalConstants.baseContext, dexPatchDir, "", dexPatchVersion, process);
            } catch (Throwable th) {
                this.bundleDir = updateDir;
                this.archive = new KernalBundleArchive(KernalConstants.baseContext, this.bundleDir, makeMainDexUniqueTag(KernalVersionManager.instance().currentVersionName(), version), dexPatchVersion, process);
            }
        } else {
            this.bundleDir = updateDir;
            this.archive = new KernalBundleArchive(KernalConstants.baseContext, this.bundleDir, makeMainDexUniqueTag(KernalVersionManager.instance().currentVersionName(), version), dexPatchVersion, process);
        }
    }

    public static void patchNativeLib(Context base) {
        try {
            File dir = new File(base.getFilesDir(), String.format("nativeLib-%s", new Object[]{base.getPackageManager().getPackageInfo(base.getPackageName(), 0).versionName}));
            ClassLoader loader = KernalBundle.class.getClassLoader();
            patchLibrary(findField(loader, "pathList").get(loader), dir);
            nativeLibPatched = true;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private String makeMainDexUniqueTag(String appVersion, String maindexTag) {
        return maindexTag.startsWith(appVersion) ? maindexTag : appVersion + "_" + maindexTag;
    }

    public void patchKernalDex(Application application) throws Exception {
        int newFrameworkPropertiesDexIndex;
        DexFile[] dexFile = this.archive.getOdexFile();
        if ((dexFile != null && dexFile.length > 0) || this.archive.getLibraryDirectory().exists()) {
            boolean needReplaceClassLoader = needReplaceClassLoader(application);
            boolean dexPatch = dexFile[dexFile.length - 1].getName().contains("dexpatch/");
            if (dexPatch) {
                newFrameworkPropertiesDexIndex = 0;
            } else {
                newFrameworkPropertiesDexIndex = dexFile.length - 1;
            }
            patchWithApk = dexPatch && (TextUtils.isEmpty(KernalVersionManager.instance().currentVersionName()) || KernalVersionManager.instance().currentVersionName().equals(KernalConstants.INSTALLED_VERSIONNAME));
            if (!needReplaceClassLoader) {
                this.FrameworkPropertiesClazz = this.archive.getOdexFile()[newFrameworkPropertiesDexIndex].loadClass("android.taobao.atlas.framework.FrameworkProperties", application.getClassLoader());
            } else if (patchWithApk) {
                this.FrameworkPropertiesClazz = this.archive.getOdexFile()[newFrameworkPropertiesDexIndex].loadClass("android.taobao.atlas.framework.FrameworkProperties", application.getClassLoader());
                this.replaceClassLoader = new NClassLoader(".", KernalBundle.class.getClassLoader().getParent());
            } else {
                this.replaceClassLoader = new NClassLoader(".", KernalBundle.class.getClassLoader().getParent());
                this.FrameworkPropertiesClazz = this.archive.getOdexFile()[newFrameworkPropertiesDexIndex].loadClass("android.taobao.atlas.framework.FrameworkProperties", this.replaceClassLoader);
            }
            if (this.FrameworkPropertiesClazz == null) {
                this.FrameworkPropertiesClazz = Class.forName("android.taobao.atlas.framework.FrameworkProperties");
            }
            if (this.FrameworkPropertiesClazz != null || !isDeubgMode()) {
                Field versionField = this.FrameworkPropertiesClazz.getDeclaredField("version");
                versionField.setAccessible(true);
                if (!KernalVersionManager.instance().currentVersionName().equals((String) versionField.get(this.FrameworkPropertiesClazz.newInstance()))) {
                    if (isDeubgMode()) {
                        Log.e("KernalBundle", "main dex is not match, awo test?");
                    } else {
                        throw new RuntimeException("maindex version is not mismatch");
                    }
                }
                if (needReplaceClassLoader) {
                    try {
                        NClassLoader.replacePathClassLoader(KernalConstants.baseContext, KernalBundle.class.getClassLoader(), this.replaceClassLoader);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                installKernalBundle(KernalConstants.baseContext.getClassLoader(), this.archive.getArchiveFile(), this.archive.getOdexFile(), this.archive.getLibraryDirectory());
                prepareRuntimeVariables(application);
                return;
            }
            Log.e("KernalBundle", "main dex is not match, library awo test?");
        }
    }

    public boolean needReplaceClassLoader(Application application) {
        if (Build.VERSION.SDK_INT < 24) {
            return false;
        }
        ClassLoader loader = getClass().getClassLoader();
        while (!loader.getClass().getName().equals(PathClassLoader.class.getName())) {
            loader = loader.getParent();
            if (loader == null) {
                return false;
            }
        }
        return true;
    }

    public void prepareRuntimeVariables(Application application) {
        try {
            Class RuntimeVariablesClass = application.getClassLoader().loadClass("android.taobao.atlas.runtime.RuntimeVariables");
            RuntimeVariablesClass.getDeclaredField("sRawClassLoader").set(RuntimeVariablesClass, KernalBundle.class.getClassLoader());
            if (this.FrameworkPropertiesClazz != null) {
                RuntimeVariablesClass.getDeclaredField("FrameworkPropertiesClazz").set(RuntimeVariablesClass, this.FrameworkPropertiesClazz);
            } else if (!isDeubgMode()) {
                throw new RuntimeException("FrameworkPropertiesClazz find error,will be rollback!");
            }
            RuntimeVariablesClass.getDeclaredField("sCurrentProcessName").set(RuntimeVariablesClass, KernalConstants.PROCESS);
            RuntimeVariablesClass.getDeclaredField("androidApplication").set(RuntimeVariablesClass, application);
            RuntimeVariablesClass.getDeclaredField("delegateResources").set(RuntimeVariablesClass, KernalConstants.baseContext.getResources());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void patchKernalResource(Application application) throws Exception {
        if (!patchWithApk) {
            Class DelegateResourcesClazz = application.getClassLoader().loadClass("android.taobao.atlas.runtime.DelegateResources");
            DelegateResourcesClazz.getDeclaredMethod("addApkpatchResources", new Class[]{String.class}).invoke(DelegateResourcesClazz, new Object[]{this.archive.getArchiveFile().getAbsolutePath()});
        }
    }

    public int getState() {
        return 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0049 A[SYNTHETIC, Splitter:B:19:0x0049] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0052 A[SYNTHETIC, Splitter:B:24:0x0052] */
    /* JADX WARNING: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void extract(java.lang.String r10, java.lang.String r11, java.io.File r12) {
        /*
            r6 = 0
            java.util.zip.ZipFile r7 = new java.util.zip.ZipFile     // Catch:{ Exception -> 0x0043 }
            java.io.File r8 = new java.io.File     // Catch:{ Exception -> 0x0043 }
            r8.<init>(r10)     // Catch:{ Exception -> 0x0043 }
            r7.<init>(r8)     // Catch:{ Exception -> 0x0043 }
            java.util.zip.ZipEntry r3 = r7.getEntry(r11)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            java.io.BufferedOutputStream r1 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            java.io.FileOutputStream r8 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            r8.<init>(r12)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            r1.<init>(r8)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            java.io.BufferedInputStream r0 = new java.io.BufferedInputStream     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            java.io.InputStream r8 = r7.getInputStream(r3)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            r0.<init>(r8)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            r8 = 600(0x258, float:8.41E-43)
            byte[] r4 = new byte[r8]     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            int r5 = r0.read(r4)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
        L_0x002a:
            r8 = -1
            if (r5 == r8) goto L_0x0036
            r8 = 0
            r1.write(r4, r8, r5)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            int r5 = r0.read(r4)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            goto L_0x002a
        L_0x0036:
            r1.close()     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            if (r7 == 0) goto L_0x003e
            r7.close()     // Catch:{ Throwable -> 0x0040 }
        L_0x003e:
            r6 = r7
        L_0x003f:
            return
        L_0x0040:
            r8 = move-exception
            r6 = r7
            goto L_0x003f
        L_0x0043:
            r2 = move-exception
        L_0x0044:
            r2.printStackTrace()     // Catch:{ all -> 0x004f }
            if (r6 == 0) goto L_0x003f
            r6.close()     // Catch:{ Throwable -> 0x004d }
            goto L_0x003f
        L_0x004d:
            r8 = move-exception
            goto L_0x003f
        L_0x004f:
            r8 = move-exception
        L_0x0050:
            if (r6 == 0) goto L_0x0055
            r6.close()     // Catch:{ Throwable -> 0x0056 }
        L_0x0055:
            throw r8
        L_0x0056:
            r9 = move-exception
            goto L_0x0055
        L_0x0058:
            r8 = move-exception
            r6 = r7
            goto L_0x0050
        L_0x005b:
            r2 = move-exception
            r6 = r7
            goto L_0x0044
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.startup.patch.KernalBundle.extract(java.lang.String, java.lang.String, java.io.File):void");
    }

    private static Field findField(Object instance, String name) throws NoSuchFieldException {
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

    private static void expandFieldArray(Object instance, String fieldName, Object[] extraElement) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field jlrField = findField(instance, fieldName);
        Object[] original = (Object[]) jlrField.get(instance);
        Object[] combined = (Object[]) Array.newInstance(original.getClass().getComponentType(), original.length + extraElement.length);
        for (int i = 0; i < extraElement.length; i++) {
            combined[i] = extraElement[i];
        }
        System.arraycopy(original, 0, combined, extraElement.length, original.length);
        jlrField.set(instance, combined);
    }

    private static void expandFieldList(Object instance, String fieldName, Object extraElement) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        ((List) findField(instance, fieldName).get(instance)).add(0, extraElement);
    }

    private static boolean replaceElement(Object instance, String fieldName, Object[] replaceElement) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Object[] original = (Object[]) findField(instance, fieldName).get(instance);
        int x = 0;
        while (x < original.length) {
            File apkFile = findDexRawFile(original[x]);
            if (apkFile == null || apkFile.getAbsolutePath() == null || !apkFile.getAbsolutePath().contains(KernalConstants.baseContext.getPackageName())) {
                x++;
            } else {
                original[x] = replaceElement;
                return true;
            }
        }
        return false;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v5, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.io.File} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v8, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: java.io.File} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.io.File findDexRawFile(java.lang.Object r6) {
        /*
            r3 = 0
            r1 = 0
            int r4 = android.os.Build.VERSION.SDK_INT
            r5 = 25
            if (r4 < r5) goto L_0x0022
            java.lang.Class r4 = r6.getClass()     // Catch:{ Throwable -> 0x0021 }
            java.lang.String r5 = "path"
            java.lang.reflect.Field r3 = r4.getDeclaredField(r5)     // Catch:{ Throwable -> 0x0021 }
            r4 = 1
            r3.setAccessible(r4)     // Catch:{ Throwable -> 0x0021 }
            java.lang.Object r4 = r3.get(r6)     // Catch:{ Throwable -> 0x0021 }
            r0 = r4
            java.io.File r0 = (java.io.File) r0     // Catch:{ Throwable -> 0x0021 }
            r1 = r0
            r2 = r1
        L_0x0020:
            return r2
        L_0x0021:
            r4 = move-exception
        L_0x0022:
            java.lang.Class r4 = r6.getClass()     // Catch:{ Throwable -> 0x0056 }
            java.lang.String r5 = "file"
            java.lang.reflect.Field r3 = r4.getDeclaredField(r5)     // Catch:{ Throwable -> 0x0056 }
            r4 = 1
            r3.setAccessible(r4)     // Catch:{ Throwable -> 0x0056 }
            java.lang.Object r4 = r3.get(r6)     // Catch:{ Throwable -> 0x0056 }
            r0 = r4
            java.io.File r0 = (java.io.File) r0     // Catch:{ Throwable -> 0x0056 }
            r1 = r0
        L_0x0039:
            if (r3 != 0) goto L_0x0052
            java.lang.Class r4 = r6.getClass()     // Catch:{ Throwable -> 0x0054 }
            java.lang.String r5 = "zip"
            java.lang.reflect.Field r3 = r4.getDeclaredField(r5)     // Catch:{ Throwable -> 0x0054 }
            r4 = 1
            r3.setAccessible(r4)     // Catch:{ Throwable -> 0x0054 }
            java.lang.Object r4 = r3.get(r6)     // Catch:{ Throwable -> 0x0054 }
            r0 = r4
            java.io.File r0 = (java.io.File) r0     // Catch:{ Throwable -> 0x0054 }
            r1 = r0
        L_0x0052:
            r2 = r1
            goto L_0x0020
        L_0x0054:
            r4 = move-exception
            goto L_0x0052
        L_0x0056:
            r4 = move-exception
            goto L_0x0039
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.startup.patch.KernalBundle.findDexRawFile(java.lang.Object):java.io.File");
    }

    public boolean installKernalBundle(ClassLoader updateClassLoader, File archiveFile, DexFile[] odexFiles, File libraryDirectory) throws IOException, NoSuchFieldException, IllegalAccessException {
        return installKernalBundle(updateClassLoader, archiveFile, odexFiles, libraryDirectory, false);
    }

    public boolean installKernalBundle(ClassLoader updateClassLoader, File archiveFile, DexFile[] odexFiles, File libraryDirectory, boolean vmSafeMode) throws IOException, NoSuchFieldException, IllegalAccessException {
        ClassLoader loader = updateClassLoader;
        try {
            Object dexPathList = findField(loader, "pathList").get(loader);
            if (odexFiles != null) {
                Object[] element = makeDexElement(archiveFile, odexFiles);
                if (element == null || element.length == 0) {
                    throw new IOException("makeDexElement failed");
                }
                expandFieldArray(dexPathList, "dexElements", element);
            }
            patchLibrary(dexPathList, libraryDirectory);
            return true;
        } catch (Exception e) {
            throw new IOException("install kernal fail", e);
        }
    }

    private boolean removeOrignalElement(Object instance, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Object[] original = (Object[]) findField(instance, fieldName).get(instance);
        int x = 0;
        while (true) {
            if (x >= original.length) {
                break;
            }
            Object element = original[x];
            Field dexFileField = element.getClass().getDeclaredField("dexFile");
            dexFileField.setAccessible(true);
            File apkFile = findDexRawFile(element);
            if (apkFile != null && apkFile.getAbsolutePath() != null && apkFile.getAbsolutePath().contains(KernalConstants.baseContext.getPackageName())) {
                dexFileField.set(element, (Object) null);
                break;
            }
            x++;
        }
        return true;
    }

    private static void patchLibrary(Object dexPathList, File libraryDirectory) throws NoSuchFieldException, IllegalAccessException, IOException {
        if (libraryDirectory != null && !TextUtils.isEmpty(libraryDirectory.getAbsolutePath()) && libraryDirectory.exists()) {
            if (Build.VERSION.SDK_INT < 23) {
                expandFieldArray(dexPathList, "nativeLibraryDirectories", new Object[]{libraryDirectory});
            } else {
                expandFieldList(dexPathList, "nativeLibraryDirectories", libraryDirectory);
            }
            if (Build.VERSION.SDK_INT >= 23) {
                expandFieldArray(dexPathList, "nativeLibraryPathElements", new Object[]{makeNativeLibraryElement(libraryDirectory)});
            }
        }
    }

    private void quietClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable th) {
            }
        }
    }

    public static Object[] makeDexElement(File file, DexFile[] dex) throws Exception {
        Object[] objects = new Object[dex.length];
        int i = 0;
        while (i < dex.length) {
            try {
                Class Element = Class.forName("dalvik.system.DexPathList$Element");
                if (Build.VERSION.SDK_INT > 25 || (Build.VERSION.SDK_INT == 25 && Build.VERSION.PREVIEW_SDK_INT > 0)) {
                    objects[i] = getElementConstructor(Element, constructorArgsO).newInstance(new Object[]{dex[i], file});
                } else {
                    File apkFile = new File(KernalConstants.baseContext.getApplicationInfo().sourceDir);
                    Constructor cons = getElementConstructor(Element, constructorArgs1);
                    if (cons != null) {
                        objects[i] = cons.newInstance(new Object[]{apkFile, false, apkFile, dex[i]});
                    } else {
                        Constructor cons2 = getElementConstructor(Element, constructorArgs2);
                        if (cons2 != null) {
                            objects[i] = cons2.newInstance(new Object[]{apkFile, apkFile, dex[i]});
                        } else {
                            Constructor cons3 = getElementConstructor(Element, constructorArgs3);
                            if (cons3 != null) {
                                objects[i] = cons3.newInstance(new Object[]{apkFile, null, dex[i]});
                            }
                        }
                    }
                }
                i++;
            } catch (Exception e) {
                throw new RuntimeException("make DexElement fail", e);
            }
        }
        return objects;
    }

    public static Object makeNativeLibraryElement(File dir) throws IOException {
        if (Build.VERSION.SDK_INT > 25 || (Build.VERSION.SDK_INT == 25 && Build.VERSION.PREVIEW_SDK_INT > 0)) {
            try {
                Constructor constructor = Class.forName("dalvik.system.DexPathList$NativeLibraryElement").getDeclaredConstructor(new Class[]{File.class});
                constructor.setAccessible(true);
                return constructor.newInstance(new Object[]{dir});
            } catch (Exception e) {
                throw new IOException("make nativeElement fail", e);
            }
        } else {
            try {
                Constructor cons = getElementConstructor(Class.forName("dalvik.system.DexPathList$Element"), constructorArgs1);
                if (cons != null) {
                    return cons.newInstance(new Object[]{dir, true, null, null});
                }
                throw new IOException("make nativeElement fail | error constructor");
            } catch (Exception e2) {
                throw new IOException("make nativeElement fail", e2);
            }
        }
    }

    private static Constructor getElementConstructor(Class element, Class... args) {
        try {
            return element.getDeclaredConstructor(args);
        } catch (Throwable th) {
            Log.w("KernalBundleImpl", "can not create element by args" + args);
            return null;
        }
    }

    public KernalBundleArchive getArchive() {
        return this.archive;
    }

    public File getRevisionDir() {
        return getArchive().getRevisionDir();
    }

    public File getRevisionZip() {
        return getArchive().getArchiveFile();
    }

    public static boolean shouldSyncUpdateInThisProcess(String process) {
        String processName = process;
        if (processName == null || (!processName.equals(KernalConstants.baseContext.getPackageName()) && !processName.toLowerCase().contains(":safemode"))) {
            return false;
        }
        return true;
    }

    public static void deleteDirectory(File path) {
        File[] files = path.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
            path.delete();
        }
    }

    public boolean isDeubgMode() {
        boolean DEBUG;
        try {
            if ((KernalConstants.baseContext.getApplicationInfo().flags & 2) != 0) {
                DEBUG = true;
            } else {
                DEBUG = false;
            }
            if (!DEBUG && !KernalConstants.baseContext.getSharedPreferences("dynamic_test", 0).getBoolean("dynamic_test_key", false)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
