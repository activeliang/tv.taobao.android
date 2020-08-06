package android.taobao.atlas.framework.bundlestorage;

import android.app.PreVerifier;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.framework.Framework;
import android.taobao.atlas.hack.AtlasHacks;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.atlas.util.ApkUtils;
import android.taobao.atlas.util.AtlasFileLock;
import android.taobao.atlas.util.IOUtil;
import android.taobao.atlas.util.StringUtils;
import android.taobao.atlas.util.log.impl.AtlasMonitor;
import android.taobao.windvane.jsbridge.api.WVFile;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.atlas.dexmerge.MergeConstants;
import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class BundleArchiveRevision {
    static final String BUNDLE_FILE_NAME = "bundle.zip";
    static final String BUNDLE_LEX_FILE = "bundle.lex";
    static final String BUNDLE_ODEX_FILE = "bundle.dex";
    static final String FILE_PROTOCOL = "file:";
    static final String REFERENCE_PROTOCOL = "reference:";
    private final File bundleFile;
    private ClassLoader dexClassLoader;
    private DexFile dexFile;
    private boolean externalStorage = false;
    private volatile boolean isDexOptDone = false;
    private final String location;
    private Manifest manifest;
    private DexFile patchDexFileForDebug;
    private final File revisionDir;
    private final String revisionLocation;
    private ZipFile zipFile;

    BundleArchiveRevision(String location2, File revisionDir2, InputStream inputStream) throws IOException {
        if (Boolean.FALSE.booleanValue()) {
            String.valueOf(PreVerifier.class);
        }
        this.revisionDir = revisionDir2;
        this.location = location2;
        if (!this.revisionDir.exists()) {
            this.revisionDir.mkdirs();
        }
        if (revisionDir2.getAbsolutePath().startsWith(RuntimeVariables.androidApplication.getFilesDir().getAbsolutePath())) {
            this.externalStorage = false;
        } else {
            this.externalStorage = true;
        }
        this.revisionLocation = FILE_PROTOCOL;
        this.bundleFile = new File(revisionDir2, BUNDLE_FILE_NAME);
        ApkUtils.copyInputStreamToFile(inputStream, this.bundleFile);
        installSoLib(this.bundleFile);
        updateMetadata();
    }

    BundleArchiveRevision(String location2, File revisionDir2, File file) throws IOException {
        this.revisionDir = revisionDir2;
        this.location = location2;
        if (!this.revisionDir.exists()) {
            this.revisionDir.mkdirs();
        }
        if (revisionDir2.getAbsolutePath().startsWith(RuntimeVariables.androidApplication.getFilesDir().getAbsolutePath())) {
            this.externalStorage = false;
        } else {
            this.externalStorage = true;
        }
        if (shouldCopyInstallFile(file)) {
            if (isSameDriver(revisionDir2, file)) {
                this.revisionLocation = FILE_PROTOCOL;
                this.bundleFile = new File(revisionDir2, BUNDLE_FILE_NAME);
                if (!file.renameTo(this.bundleFile)) {
                    ApkUtils.copyInputStreamToFile(new FileInputStream(file), this.bundleFile);
                }
            } else {
                this.revisionLocation = FILE_PROTOCOL;
                this.bundleFile = new File(revisionDir2, BUNDLE_FILE_NAME);
                ApkUtils.copyInputStreamToFile(new FileInputStream(file), this.bundleFile);
            }
            installSoLib(this.bundleFile);
        } else {
            this.revisionLocation = REFERENCE_PROTOCOL + file.getAbsolutePath();
            this.bundleFile = file;
            installSoLib(file);
        }
        updateMetadata();
    }

    BundleArchiveRevision(String location2, File revisionDir2) throws IOException {
        this.location = location2;
        if (revisionDir2.getAbsolutePath().startsWith(RuntimeVariables.androidApplication.getFilesDir().getAbsolutePath())) {
            this.externalStorage = false;
        } else {
            this.externalStorage = true;
        }
        File metafile = new File(revisionDir2, "meta");
        if (metafile.exists()) {
            DataInputStream in = new DataInputStream(new FileInputStream(metafile));
            this.revisionLocation = in.readUTF();
            IOUtil.quietClose((Closeable) in);
            this.revisionDir = revisionDir2;
            if (StringUtils.startWith(this.revisionLocation, REFERENCE_PROTOCOL)) {
                this.bundleFile = new File(StringUtils.substringAfter(this.revisionLocation, REFERENCE_PROTOCOL));
            } else {
                this.bundleFile = new File(revisionDir2, BUNDLE_FILE_NAME);
            }
        } else {
            throw new IOException("Could not find meta file in " + revisionDir2.getAbsolutePath());
        }
    }

    private boolean shouldCopyInstallFile(File bundleFile2) throws IOException {
        if (bundleFile2 == null) {
            throw new IOException("bundle file not exists");
        }
        if (!bundleFile2.getAbsolutePath().startsWith(new File(RuntimeVariables.androidApplication.getFilesDir().getParentFile(), "lib").getAbsolutePath()) || ((AtlasHacks.LexFile != null && AtlasHacks.LexFile.getmClass() != null) || (Build.HARDWARE.toLowerCase().contains("mt65") && bundleFile2.getName().endsWith(MergeConstants.SO_SUFFIX)))) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void updateMetadata() throws IOException {
        File file = new File(this.revisionDir, "meta");
        DataOutputStream out = null;
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            DataOutputStream out2 = new DataOutputStream(new FileOutputStream(file));
            try {
                out2.writeUTF(this.revisionLocation);
                out2.flush();
                IOUtil.quietClose((Closeable) out2);
            } catch (IOException e) {
                e = e;
                out = out2;
                try {
                    throw new IOException("Could not save meta data ", e);
                } catch (Throwable th) {
                    th = th;
                    IOUtil.quietClose((Closeable) out);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                out = out2;
                IOUtil.quietClose((Closeable) out);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            throw new IOException("Could not save meta data ", e);
        }
    }

    public File getRevisionDir() {
        return this.revisionDir;
    }

    public File getRevisionFile() {
        return this.bundleFile;
    }

    /* JADX WARNING: Removed duplicated region for block: B:3:0x003e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.io.File mappingDebugInternalDirectory() {
        /*
            r8 = this;
            java.io.File r0 = new java.io.File
            android.app.Application r2 = android.taobao.atlas.runtime.RuntimeVariables.androidApplication
            java.io.File r2 = r2.getFilesDir()
            java.lang.String r3 = "storage/%s/%s"
            r4 = 2
            java.lang.Object[] r4 = new java.lang.Object[r4]
            r5 = 0
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = r8.location
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r7 = "_debug_internal"
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            r4[r5] = r6
            r5 = 1
            java.io.File r6 = r8.revisionDir
            java.lang.String r6 = r6.getName()
            r4[r5] = r6
            java.lang.String r3 = java.lang.String.format(r3, r4)
            r0.<init>(r2, r3)
            r1 = 2
        L_0x0038:
            boolean r2 = r0.exists()
            if (r2 != 0) goto L_0x0041
            r0.mkdirs()
        L_0x0041:
            boolean r2 = r0.exists()
            if (r2 == 0) goto L_0x006a
        L_0x0047:
            boolean r2 = r0.exists()
            if (r2 != 0) goto L_0x0069
            java.lang.String r2 = "BundleArchiveRevision"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "create internal LibDir Failed : "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = r8.location
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            android.util.Log.e(r2, r3)
        L_0x0069:
            return r0
        L_0x006a:
            int r1 = r1 + -1
            if (r1 > 0) goto L_0x0038
            goto L_0x0047
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.framework.bundlestorage.BundleArchiveRevision.mappingDebugInternalDirectory():java.io.File");
    }

    /* JADX WARNING: Removed duplicated region for block: B:5:0x0042  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.io.File mappingInternalDirectory() {
        /*
            r8 = this;
            boolean r2 = r8.externalStorage
            if (r2 == 0) goto L_0x0073
            java.io.File r0 = new java.io.File
            android.app.Application r2 = android.taobao.atlas.runtime.RuntimeVariables.androidApplication
            java.io.File r2 = r2.getFilesDir()
            java.lang.String r3 = "storage/%s/%s"
            r4 = 2
            java.lang.Object[] r4 = new java.lang.Object[r4]
            r5 = 0
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = r8.location
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r7 = "_internal"
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            r4[r5] = r6
            r5 = 1
            java.io.File r6 = r8.revisionDir
            java.lang.String r6 = r6.getName()
            r4[r5] = r6
            java.lang.String r3 = java.lang.String.format(r3, r4)
            r0.<init>(r2, r3)
            r1 = 2
        L_0x003c:
            boolean r2 = r0.exists()
            if (r2 != 0) goto L_0x0045
            r0.mkdirs()
        L_0x0045:
            boolean r2 = r0.exists()
            if (r2 == 0) goto L_0x006e
        L_0x004b:
            boolean r2 = r0.exists()
            if (r2 != 0) goto L_0x006d
            java.lang.String r2 = "BundleArchiveRevision"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "create internal LibDir Failed : "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = r8.location
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            android.util.Log.e(r2, r3)
        L_0x006d:
            return r0
        L_0x006e:
            int r1 = r1 + -1
            if (r1 > 0) goto L_0x003c
            goto L_0x004b
        L_0x0073:
            java.io.File r0 = r8.revisionDir
            goto L_0x006d
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.framework.bundlestorage.BundleArchiveRevision.mappingInternalDirectory():java.io.File");
    }

    /* JADX WARNING: Removed duplicated region for block: B:5:0x002e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.io.File mappingInternalDirectoryOld() {
        /*
            r7 = this;
            boolean r2 = r7.externalStorage
            if (r2 == 0) goto L_0x005f
            java.io.File r0 = new java.io.File
            android.app.Application r2 = android.taobao.atlas.runtime.RuntimeVariables.androidApplication
            java.io.File r2 = r2.getFilesDir()
            java.lang.String r3 = "storage/%s/%s"
            r4 = 2
            java.lang.Object[] r4 = new java.lang.Object[r4]
            r5 = 0
            java.lang.String r6 = r7.location
            r4[r5] = r6
            r5 = 1
            java.io.File r6 = r7.revisionDir
            java.lang.String r6 = r6.getName()
            r4[r5] = r6
            java.lang.String r3 = java.lang.String.format(r3, r4)
            r0.<init>(r2, r3)
            r1 = 2
        L_0x0028:
            boolean r2 = r0.exists()
            if (r2 != 0) goto L_0x0031
            r0.mkdirs()
        L_0x0031:
            boolean r2 = r0.exists()
            if (r2 == 0) goto L_0x005a
        L_0x0037:
            boolean r2 = r0.exists()
            if (r2 != 0) goto L_0x0059
            java.lang.String r2 = "BundleArchiveRevision"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "create internal LibDir Failed : "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = r7.location
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            android.util.Log.e(r2, r3)
        L_0x0059:
            return r0
        L_0x005a:
            int r1 = r1 + -1
            if (r1 > 0) goto L_0x0028
            goto L_0x0037
        L_0x005f:
            java.io.File r0 = r7.revisionDir
            goto L_0x0059
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.framework.bundlestorage.BundleArchiveRevision.mappingInternalDirectoryOld():java.io.File");
    }

    public File findDebugSoLibrary(String libraryName) {
        File patchFile = new File(new File(RuntimeVariables.androidApplication.getExternalFilesDir("debug_storage"), this.location), "patch.zip");
        if (patchFile.exists()) {
            File debugInternalDirectory = mappingDebugInternalDirectory();
            File file = new File(String.format("%s%s%s%s", new Object[]{debugInternalDirectory, File.separator, "lib", File.separator}), libraryName);
            if (file.exists() && file.isFile() && file.length() > 0 && file.lastModified() > patchFile.lastModified()) {
                return file;
            }
            ZipFile bundleZip = null;
            try {
                ZipFile bundleZip2 = new ZipFile(patchFile);
                String extractTag = "lib/armeabi";
                try {
                    if (Build.CPU_ABI.contains("x86") && bundleZip2.getEntry("lib/x86/") != null) {
                        extractTag = "lib/x86";
                    }
                    ZipEntry libEntry = bundleZip2.getEntry(extractTag + WVNativeCallbackUtil.SEPERATER + libraryName);
                    if (libEntry != null) {
                        extractEntry(bundleZip2, libEntry, debugInternalDirectory);
                    }
                    if (!file.exists() || !file.isFile()) {
                        IOUtil.quietClose(bundleZip2);
                        IOUtil.quietClose(bundleZip2);
                    } else {
                        IOUtil.quietClose(bundleZip2);
                        return file;
                    }
                } catch (Throwable th) {
                    th = th;
                    bundleZip = bundleZip2;
                    IOUtil.quietClose(bundleZip);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                IOUtil.quietClose(bundleZip);
                throw th;
            }
        }
        return null;
    }

    public File findSoLibrary(String libraryName) {
        File soFile;
        if (Framework.isDeubgMode() && (soFile = findDebugSoLibrary(libraryName)) != null && soFile.exists()) {
            return soFile;
        }
        File file = new File(String.format("%s%s%s%s", new Object[]{mappingInternalDirectory(), File.separator, "lib", File.separator}), libraryName);
        if (file.exists() && file.isFile() && file.length() > 0) {
            return file;
        }
        File file2 = new File(String.format("%s%s%s%s", new Object[]{mappingInternalDirectoryOld(), File.separator, "lib", File.separator}), libraryName);
        if (file2.exists() && file2.isFile() && file2.length() > 0) {
            return file2;
        }
        if (this.bundleFile != null) {
            ZipFile bundleZip = null;
            try {
                ZipFile bundleZip2 = new ZipFile(this.bundleFile);
                String extractTag = "lib/armeabi";
                try {
                    if (Build.CPU_ABI.contains("x86") && bundleZip2.getEntry("lib/x86/") != null) {
                        extractTag = "lib/x86";
                    }
                    ZipEntry libEntry = bundleZip2.getEntry(extractTag + WVNativeCallbackUtil.SEPERATER + libraryName);
                    if (libEntry != null) {
                        extractEntry(bundleZip2, libEntry, mappingInternalDirectory());
                    }
                    if (!file.exists() || !file.isFile()) {
                        IOUtil.quietClose(bundleZip2);
                        IOUtil.quietClose(bundleZip2);
                    } else {
                        IOUtil.quietClose(bundleZip2);
                        return file;
                    }
                } catch (Throwable th) {
                    th = th;
                    bundleZip = bundleZip2;
                    IOUtil.quietClose(bundleZip);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                IOUtil.quietClose(bundleZip);
                throw th;
            }
        }
        return null;
    }

    public boolean isDexOpted() {
        if (!this.isDexOptDone) {
            return false;
        }
        if (this.dexFile != null) {
            return true;
        }
        if (AtlasHacks.LexFile == null || AtlasHacks.LexFile.getmClass() == null) {
            File odexFile = new File(mappingInternalDirectory(), BUNDLE_ODEX_FILE);
            if (!odexFile.exists() || odexFile.length() <= 0) {
                return false;
            }
            return true;
        }
        File lexFile = new File(mappingInternalDirectory(), BUNDLE_LEX_FILE);
        if (!lexFile.exists() || lexFile.length() <= 0) {
            return false;
        }
        return true;
    }

    public void optDexFile() {
        if (!isDexOpted()) {
            optDexFileLocked();
        }
    }

    /* JADX INFO: finally extract failed */
    public synchronized void optDexFileLocked() {
        boolean interpretOnly = true;
        synchronized (this) {
            if (!isDexOpted()) {
                if (AtlasHacks.LexFile == null || AtlasHacks.LexFile.getmClass() == null) {
                    File odexFile = new File(mappingInternalDirectory(), BUNDLE_ODEX_FILE);
                    long START = System.currentTimeMillis();
                    try {
                        if (!AtlasFileLock.getInstance().LockExclusive(odexFile)) {
                            Log.e("Framework", "Failed to get file lock for " + this.bundleFile.getAbsolutePath());
                        }
                        if (this.dexFile == null) {
                            if (!this.externalStorage || (this.externalStorage && Build.VERSION.SDK_INT >= 21 && MultiDex.IS_VM_MULTIDEX_CAPABLE)) {
                                if (!this.externalStorage) {
                                    interpretOnly = false;
                                }
                                if (this.bundleFile.getUsableSpace() < WVFile.FILE_MAX_SIZE) {
                                    interpretOnly = true;
                                }
                                Log.e("BundleArchiveRevision", "interpretOnly = " + interpretOnly);
                                this.dexFile = (DexFile) RuntimeVariables.sDexLoadBooster.getClass().getDeclaredMethod("loadDex", new Class[]{Context.class, String.class, String.class, Integer.TYPE, Boolean.TYPE}).invoke(RuntimeVariables.sDexLoadBooster, new Object[]{RuntimeVariables.androidApplication, this.bundleFile.getAbsolutePath(), odexFile.getAbsolutePath(), 0, Boolean.valueOf(interpretOnly)});
                            } else {
                                this.dexFile = (DexFile) Class.forName("android.taobao.atlas.startup.DexFileCompat").getDeclaredMethod("loadDex", new Class[]{Context.class, String.class, String.class, Integer.TYPE}).invoke((Object) null, new Object[]{RuntimeVariables.androidApplication, this.bundleFile.getAbsolutePath(), odexFile.getAbsolutePath(), 0});
                            }
                        }
                        if (Framework.isDeubgMode()) {
                            optPatchDexFile();
                        }
                        this.isDexOptDone = true;
                        AtlasFileLock.getInstance().unLock(odexFile);
                    } catch (Exception e) {
                        if (odexFile.exists()) {
                            odexFile.delete();
                        }
                        Log.e("Framework", "Failed optDexFile '" + this.bundleFile.getAbsolutePath() + "' >>> ", e);
                        Map<String, Object> detail = new HashMap<>();
                        detail.put("optDexFile", this.bundleFile.getAbsolutePath());
                        AtlasMonitor.getInstance().report(AtlasMonitor.CONTAINER_DEXOPT_FAIL, detail, e);
                        AtlasFileLock.getInstance().unLock(odexFile);
                    } catch (Throwable th) {
                        AtlasFileLock.getInstance().unLock(odexFile);
                        throw th;
                    }
                    Log.e("Framework", "bundle archieve dexopt bundle " + this.bundleFile.getAbsolutePath() + " cost time = " + (System.currentTimeMillis() - START) + " ms");
                } else {
                    new DexClassLoader(this.bundleFile.getAbsolutePath(), mappingInternalDirectory().getAbsolutePath(), (String) null, ClassLoader.getSystemClassLoader());
                    this.isDexOptDone = true;
                }
            }
        }
    }

    public boolean checkDexValid(DexFile dexFile2) throws IOException {
        String applicationName = AtlasBundleInfoManager.instance().getBundleInfo(this.location).getApplicationName();
        if (!TextUtils.isEmpty(applicationName)) {
            Class clazz = null;
            try {
                clazz = getClass().getClassLoader().loadClass(applicationName);
            } catch (ClassNotFoundException e) {
            }
            if (clazz == null) {
                try {
                    dexFile2.loadClass(applicationName, new ClassLoader() {
                    });
                    return false;
                } catch (Throwable th) {
                    return false;
                }
            }
        }
        return true;
    }

    public static class DexLoadException extends RuntimeException {
        DexLoadException(String e) {
            super(e);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0068  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void installSoLib(java.io.File r11) throws java.io.IOException {
        /*
            r10 = this;
            r5 = 0
            java.util.zip.ZipFile r6 = new java.util.zip.ZipFile     // Catch:{ IOException -> 0x0082 }
            r6.<init>(r11)     // Catch:{ IOException -> 0x0082 }
            java.lang.String r4 = "lib/armeabi"
            java.lang.String r8 = android.os.Build.CPU_ABI     // Catch:{ IOException -> 0x0049, all -> 0x007f }
            java.lang.String r9 = "x86"
            boolean r8 = r8.contains(r9)     // Catch:{ IOException -> 0x0049, all -> 0x007f }
            if (r8 == 0) goto L_0x001d
            boolean r8 = android.taobao.atlas.framework.Framework.isDeubgMode()     // Catch:{ IOException -> 0x0049, all -> 0x007f }
            if (r8 == 0) goto L_0x006c
            java.lang.String r4 = "lib/x86"
        L_0x001d:
            java.util.Enumeration r2 = r6.entries()     // Catch:{ IOException -> 0x0049, all -> 0x007f }
        L_0x0021:
            boolean r8 = r2.hasMoreElements()     // Catch:{ IOException -> 0x0049, all -> 0x007f }
            if (r8 == 0) goto L_0x0079
            java.lang.Object r7 = r2.nextElement()     // Catch:{ IOException -> 0x0049, all -> 0x007f }
            java.util.zip.ZipEntry r7 = (java.util.zip.ZipEntry) r7     // Catch:{ IOException -> 0x0049, all -> 0x007f }
            java.lang.String r3 = r7.getName()     // Catch:{ IOException -> 0x0049, all -> 0x007f }
            java.lang.String r8 = "../"
            boolean r8 = r3.equalsIgnoreCase(r8)     // Catch:{ IOException -> 0x0049, all -> 0x007f }
            if (r8 != 0) goto L_0x0021
            int r8 = r3.indexOf(r4)     // Catch:{ IOException -> 0x0049, all -> 0x007f }
            r9 = -1
            if (r8 == r9) goto L_0x0021
            java.io.File r8 = r10.mappingInternalDirectory()     // Catch:{ IOException -> 0x0049, all -> 0x007f }
            r10.extractEntry(r6, r7, r8)     // Catch:{ IOException -> 0x0049, all -> 0x007f }
            goto L_0x0021
        L_0x0049:
            r1 = move-exception
            r5 = r6
        L_0x004b:
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ all -> 0x0065 }
            r0.<init>()     // Catch:{ all -> 0x0065 }
            java.lang.String r8 = "installSoLib"
            java.lang.String r9 = r11.getAbsolutePath()     // Catch:{ all -> 0x0065 }
            r0.put(r8, r9)     // Catch:{ all -> 0x0065 }
            android.taobao.atlas.util.log.impl.AtlasMonitor r8 = android.taobao.atlas.util.log.impl.AtlasMonitor.getInstance()     // Catch:{ all -> 0x0065 }
            java.lang.String r9 = "container_solib_unzip_fail"
            r8.report(r9, r0, r1)     // Catch:{ all -> 0x0065 }
            throw r1     // Catch:{ all -> 0x0065 }
        L_0x0065:
            r8 = move-exception
        L_0x0066:
            if (r5 == 0) goto L_0x006b
            r5.close()
        L_0x006b:
            throw r8
        L_0x006c:
            java.lang.String r8 = "lib/x86/"
            java.util.zip.ZipEntry r8 = r6.getEntry(r8)     // Catch:{ IOException -> 0x0049, all -> 0x007f }
            if (r8 == 0) goto L_0x001d
            java.lang.String r4 = "lib/x86"
            goto L_0x001d
        L_0x0079:
            if (r6 == 0) goto L_0x007e
            r6.close()
        L_0x007e:
            return
        L_0x007f:
            r8 = move-exception
            r5 = r6
            goto L_0x0066
        L_0x0082:
            r1 = move-exception
            goto L_0x004b
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.framework.bundlestorage.BundleArchiveRevision.installSoLib(java.io.File):void");
    }

    private void extractEntry(ZipFile zip, ZipEntry zipEntry, File directory) throws IOException {
        String entryName = zipEntry.getName();
        String targetPath = String.format("%s%s%s%s%s", new Object[]{directory, File.separator, "lib", File.separator, entryName.substring(entryName.lastIndexOf(File.separator) + 1, entryName.length())});
        if (zipEntry.isDirectory()) {
            File decompressDirFile = new File(targetPath);
            if (!decompressDirFile.exists()) {
                decompressDirFile.mkdirs();
                return;
            }
            return;
        }
        File fileDirFile = new File(targetPath.substring(0, targetPath.lastIndexOf(WVNativeCallbackUtil.SEPERATER)));
        if (!fileDirFile.exists()) {
            fileDirFile.mkdirs();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetPath));
        BufferedInputStream bi = new BufferedInputStream(zip.getInputStream(zipEntry));
        byte[] readContent = new byte[1024];
        for (int readCount = bi.read(readContent); readCount != -1; readCount = bi.read(readContent)) {
            bos.write(readContent, 0, readCount);
        }
        bos.close();
        bi.close();
    }

    /* access modifiers changed from: package-private */
    public Class<?> findClass(String className, ClassLoader cl) throws ClassNotFoundException {
        Class<?> clazz;
        try {
            if (AtlasHacks.LexFile == null || AtlasHacks.LexFile.getmClass() == null) {
                if (!isDexOpted()) {
                    optDexFile();
                }
                if (this.dexFile == null) {
                    optDexFile();
                }
                if (!Framework.isDeubgMode() || ((clazz = findPatchClass(className, cl)) == null && this.patchDexFileForDebug == null)) {
                    return this.dexFile.loadClass(className, cl);
                }
                return clazz;
            }
            if (this.dexClassLoader == null) {
                this.dexClassLoader = new DexClassLoader(this.bundleFile.getAbsolutePath(), this.revisionDir.getAbsolutePath(), new File(RuntimeVariables.androidApplication.getFilesDir().getParentFile(), "lib").getAbsolutePath(), cl) {
                    public String findLibrary(String name) {
                        String path = super.findLibrary(name);
                        if (!TextUtils.isEmpty(path)) {
                            return path;
                        }
                        File soFile = BundleArchiveRevision.this.findSoLibrary(System.mapLibraryName(name));
                        if (soFile != null && soFile.exists()) {
                            return soFile.getAbsolutePath();
                        }
                        try {
                            return (String) AtlasHacks.ClassLoader_findLibrary.invoke(Framework.getSystemClassLoader(), name);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }
            return (Class) AtlasHacks.DexClassLoader_findClass.invoke(this.dexClassLoader, className);
        } catch (IllegalArgumentException | InvocationTargetException e) {
            return null;
        } catch (Exception e2) {
            if (!(e2 instanceof ClassNotFoundException)) {
                if (e2 instanceof DexLoadException) {
                    throw ((DexLoadException) e2);
                }
                Log.e("Framework", "Exception while find class in archive revision: " + this.bundleFile.getAbsolutePath(), e2);
            }
            return null;
        }
    }

    private Class findPatchClass(String clazz, ClassLoader cl) {
        if (this.patchDexFileForDebug != null) {
            return this.patchDexFileForDebug.loadClass(clazz, cl);
        }
        return null;
    }

    private void optPatchDexFile() {
        File patchFile = new File(new File(RuntimeVariables.androidApplication.getExternalFilesDir("debug_storage"), this.location), "patch.zip");
        if (patchFile.exists()) {
            try {
                File internalDebugBundleDir = new File(new File(RuntimeVariables.androidApplication.getFilesDir(), "debug_storage"), this.location);
                internalDebugBundleDir.mkdirs();
                this.patchDexFileForDebug = (DexFile) RuntimeVariables.sDexLoadBooster.getClass().getDeclaredMethod("loadDex", new Class[]{Context.class, String.class, String.class, Integer.TYPE, Boolean.TYPE}).invoke(RuntimeVariables.sDexLoadBooster, new Object[]{RuntimeVariables.androidApplication, patchFile.getAbsolutePath(), new File(internalDebugBundleDir, "patch.dex").getAbsolutePath(), 0, true});
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getDebugPatchFilePath() {
        File patchFile = new File(new File(RuntimeVariables.androidApplication.getExternalFilesDir("debug_storage"), this.location), "patch.zip");
        if (patchFile.exists()) {
            return patchFile.getAbsolutePath();
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public List<URL> getResources(String resName) throws IOException {
        List<URL> results = new ArrayList<>();
        ensureZipFile();
        if (!(this.zipFile == null || this.zipFile.getEntry(resName) == null)) {
            try {
                results.add(new URL("jar:" + this.bundleFile.toURL() + "!/" + resName));
            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return results;
    }

    private boolean isSameDriver(File file1, File file2) {
        return StringUtils.equals(StringUtils.substringBetween(file1.getAbsolutePath(), WVNativeCallbackUtil.SEPERATER, WVNativeCallbackUtil.SEPERATER), StringUtils.substringBetween(file2.getAbsolutePath(), WVNativeCallbackUtil.SEPERATER, WVNativeCallbackUtil.SEPERATER));
    }

    private void ensureZipFile() throws IOException {
        if (this.zipFile == null) {
            this.zipFile = new ZipFile(this.bundleFile, 1);
        }
    }
}
