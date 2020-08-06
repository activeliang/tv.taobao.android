package android.taobao.atlas.framework;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.framework.bundlestorage.BundleArchive;
import android.taobao.atlas.patch.AtlasHotPatchManager;
import android.taobao.atlas.runtime.ClassNotFoundInterceptorCallback;
import android.taobao.atlas.runtime.LowDiskException;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.atlas.util.AtlasFileLock;
import android.taobao.atlas.util.BundleLock;
import android.taobao.atlas.util.FileUtils;
import android.taobao.atlas.util.log.impl.AtlasMonitor;
import android.taobao.atlas.versionInfo.BaselineInfoManager;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.taobao.atlas.dexmerge.MergeConstants;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;

public final class Framework {
    private static String BASEDIR = null;
    public static boolean DEBUG = false;
    static boolean DEBUG_BUNDLES = false;
    public static final String DEPRECATED_MARK = "deprecated";
    public static final String STORAGE_LOCATION = (BASEDIR + File.separatorChar + "storage" + File.separatorChar);
    static List<BundleListener> bundleListeners = new ArrayList();
    private static boolean bundleUpdated = false;
    public static Map<String, Bundle> bundles = new ConcurrentHashMap();
    private static ClassNotFoundInterceptorCallback classNotFoundCallback;
    static String containerVersion = "";
    static List<FrameworkListener> frameworkListeners = new ArrayList();
    static HashMap<String, Integer> installingBundles = new HashMap<>();
    static List<BundleListener> syncBundleListeners = new ArrayList();
    static ClassLoader systemClassLoader;
    public static boolean updateHappend = false;

    static {
        boolean z = false;
        DEBUG = false;
        File fileDir = RuntimeVariables.androidApplication.getFilesDir();
        if (fileDir == null || !fileDir.exists()) {
            fileDir = RuntimeVariables.androidApplication.getFilesDir();
        }
        BASEDIR = fileDir.getAbsolutePath();
        try {
            if ((RuntimeVariables.androidApplication.getApplicationInfo().flags & 2) != 0) {
                z = true;
            }
            DEBUG = z;
        } catch (Exception e) {
            DEBUG = true;
        }
    }

    private Framework() {
    }

    static void startup(boolean updated) throws BundleException {
        AtlasBundleInfoManager.instance().getBundleInfo();
        AtlasHotPatchManager.getInstance();
        notifyFrameworkListeners(0, (Bundle) null, (Throwable) null);
        notifyFrameworkListeners(1, (Bundle) null, (Throwable) null);
    }

    public static ClassLoader getSystemClassLoader() {
        return systemClassLoader;
    }

    public static List<Bundle> getBundles() {
        List<Bundle> res = new ArrayList<>(bundles.size());
        synchronized (bundles) {
            res.addAll(bundles.values());
        }
        return res;
    }

    public static synchronized Bundle getBundle(String location) {
        Bundle bundle;
        synchronized (Framework.class) {
            if (location == null) {
                bundle = null;
            } else {
                bundle = bundles.get(location);
            }
        }
        return bundle;
    }

    public static void deleteDirectory(File path) {
        File[] files = path.listFiles();
        if (files != null) {
            Log.e(RequestParameters.SUBRESOURCE_DELETE, path.getAbsolutePath());
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

    static BundleImpl installNewBundle(String location, InputStream in) throws BundleException {
        File bundleDir = null;
        try {
            BundleLock.WriteLock(location);
            installingBundles.put(location, 0);
            bundleDir = createBundleStorage(location);
            if (!bundleDir.exists()) {
                bundleDir.mkdirs();
            }
            AtlasFileLock.getInstance().LockExclusive(bundleDir);
            BundleImpl cached = (BundleImpl) getBundle(location);
            if (cached != null) {
                installingBundles.remove(location);
                BundleLock.WriteUnLock(location);
                if (bundleDir == null) {
                    return cached;
                }
                AtlasFileLock.getInstance().unLock(bundleDir);
                return cached;
            }
            BundleImpl bundle = new BundleImpl(bundleDir, location, in, (File) null, AtlasBundleInfoManager.instance().getBundleInfo(location).getUnique_tag(), true, -1);
            installingBundles.remove(location);
            BundleLock.WriteUnLock(location);
            if (bundleDir != null) {
                AtlasFileLock.getInstance().unLock(bundleDir);
            }
            return bundle;
        } catch (IOException e) {
            new BundleException("Failed to install bundle." + FileUtils.getAvailableDisk(), e);
            if (bundleDir != null && !updateHappend) {
                deleteDirectory(bundleDir);
            }
            if (FileUtils.getUsableSpace(Environment.getDataDirectory()) < ((long) LowDiskException.thredshold)) {
                throw new LowDiskException(FileUtils.getAvailableDisk(), e);
            }
            throw new BundleException("Failed to install bundle.", e);
        } catch (BundleException e2) {
            BundleException e1 = new BundleException("Failed to install bundle." + FileUtils.getAvailableDisk(), e2);
            if (bundleDir != null && !updateHappend) {
                deleteDirectory(bundleDir);
            }
            throw e1;
        } catch (Throwable th) {
            installingBundles.remove(location);
            BundleLock.WriteUnLock(location);
            if (bundleDir != null) {
                AtlasFileLock.getInstance().unLock(bundleDir);
            }
            throw th;
        }
    }

    static BundleImpl installNewBundle(String location, File file) throws BundleException {
        File bundleDir = null;
        try {
            BundleLock.WriteLock(location);
            installingBundles.put(location, 0);
            bundleDir = createBundleStorage(location);
            if (!bundleDir.exists()) {
                bundleDir.mkdirs();
            }
            AtlasFileLock.getInstance().LockExclusive(bundleDir);
            BundleImpl cached = (BundleImpl) getBundle(location);
            if (cached != null) {
                installingBundles.remove(location);
                BundleLock.WriteUnLock(location);
                if (bundleDir == null) {
                    return cached;
                }
                AtlasFileLock.getInstance().unLock(bundleDir);
                return cached;
            }
            Log.e("BundleInstaller", "real install " + location);
            BundleImpl bundle = new BundleImpl(bundleDir, location, (InputStream) null, file, AtlasBundleInfoManager.instance().getBundleInfo(location).getUnique_tag(), true, -1);
            installingBundles.remove(location);
            BundleLock.WriteUnLock(location);
            if (bundleDir != null) {
                AtlasFileLock.getInstance().unLock(bundleDir);
            }
            return bundle;
        } catch (IOException e) {
            new BundleException("Failed to install bundle." + FileUtils.getAvailableDisk() + ", location=" + location, e);
            if (bundleDir != null && !updateHappend) {
                deleteDirectory(bundleDir);
            }
            if (FileUtils.getUsableSpace(Environment.getDataDirectory()) < ((long) LowDiskException.thredshold)) {
                throw new LowDiskException(FileUtils.getAvailableDisk(), e);
            }
            throw new BundleException("Failed to install bundle." + FileUtils.getAvailableDisk() + ", location=" + location, e);
        } catch (BundleException e2) {
            BundleException e1 = new BundleException("Failed to install bundle." + FileUtils.getAvailableDisk() + ", location=" + location, e2);
            if (bundleDir != null && !updateHappend) {
                deleteDirectory(bundleDir);
            }
            throw e1;
        } catch (Throwable th) {
            installingBundles.remove(location);
            BundleLock.WriteUnLock(location);
            if (bundleDir != null) {
                AtlasFileLock.getInstance().unLock(bundleDir);
            }
            throw th;
        }
    }

    public static BundleImpl restoreFromExistedBundle(String location) {
        String[] temp;
        BundleImpl bundle = null;
        String bundleUniqueTag = AtlasBundleInfoManager.instance().getBundleInfo(location).getUnique_tag();
        long dexPatchVersion = BaselineInfoManager.instance().getDexPatchBundleVersion(location);
        File bundleDir = null;
        File dexPatchDir = null;
        File internalTmpDir = new File(STORAGE_LOCATION, location + File.separator + bundleUniqueTag);
        if (!internalTmpDir.exists() || !new File(internalTmpDir, "meta").exists()) {
            File[] externalStorages = getExternalFilesDirs(RuntimeVariables.androidApplication, "storage");
            if (externalStorages != null && externalStorages.length > 0) {
                int length = externalStorages.length;
                int i = 0;
                while (true) {
                    if (i < length) {
                        File tmpDir = externalStorages[i];
                        if (tmpDir != null && new File(tmpDir, location + File.separator + bundleUniqueTag).exists() && getStorageState(tmpDir).equals("mounted")) {
                            bundleDir = new File(tmpDir, location);
                            break;
                        }
                        i++;
                    } else {
                        break;
                    }
                }
            }
        } else {
            bundleDir = new File(STORAGE_LOCATION, location);
        }
        if (dexPatchVersion > 0) {
            if (new File(STORAGE_LOCATION, location + File.separator + "dexpatch/" + dexPatchVersion).exists()) {
                dexPatchDir = new File(STORAGE_LOCATION, location);
            } else {
                File[] externalStorages2 = getExternalFilesDirs(RuntimeVariables.androidApplication, "storage");
                if (externalStorages2 != null && externalStorages2.length > 0) {
                    int length2 = externalStorages2.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 < length2) {
                            File tmpDir2 = externalStorages2[i2];
                            if (tmpDir2 != null && getStorageState(tmpDir2).equals("mounted") && new File(tmpDir2, location + File.separator + "dexpatch/" + dexPatchVersion).exists()) {
                                bundleDir = new File(tmpDir2, location);
                                break;
                            }
                            i2++;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        if (!(bundleDir == null || !bundleDir.exists() || (temp = bundleDir.list()) == null)) {
            int length3 = temp.length;
            for (int i3 = 0; i3 < length3; i3++) {
                Log.e("Framework", "content: " + temp[i3]);
            }
        }
        Log.e("Framework", "restoreExisted: " + location + "| " + bundleUniqueTag + "| " + bundleDir);
        if ((bundleDir == null || !new File(bundleDir, bundleUniqueTag).exists()) && BaselineInfoManager.instance().isUpdated(location) && getCurProcessName().equals(RuntimeVariables.androidApplication.getPackageName())) {
            Map<String, Object> detail = new HashMap<>();
            detail.put("Bundle", location);
            AtlasMonitor.getInstance().report(AtlasMonitor.DD_BUNDLE_MISMATCH, detail, new RuntimeException(bundleUniqueTag + " is not existed"));
            BaselineInfoManager.instance().rollbackHardly();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                public void run() {
                    Process.killProcess(Process.myPid());
                }
            }, 1000);
        }
        if (installingBundles.containsKey(location)) {
            return null;
        }
        if ((bundleDir == null || !bundleDir.exists()) && (dexPatchDir == null || !dexPatchDir.exists())) {
            return null;
        }
        try {
            BundleContext bcontext = new BundleContext();
            bcontext.bundle_tag = bundleUniqueTag;
            bcontext.location = location;
            bcontext.bundleDir = bundleDir;
            bcontext.dexPatchDir = dexPatchDir;
            BundleImpl bundle2 = new BundleImpl(bcontext);
            if (bundle2 != null) {
                try {
                    bundle2.optDexFile();
                } catch (Exception e) {
                    e = e;
                    bundle = bundle2;
                    if ((e instanceof BundleArchive.MisMatchException) && bundleDir.exists()) {
                        bundle = null;
                    }
                    Log.e("Framework", "restore bundle failed" + location, e);
                    return bundle;
                }
            }
            return bundle2;
        } catch (Exception e2) {
            e = e2;
            bundle = null;
            Log.e("Framework", "restore bundle failed" + location, e);
            return bundle;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:67:0x015f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void update(boolean r23, java.lang.String[] r24, java.io.File[] r25, java.lang.String[] r26, long[] r27, java.lang.String r28, boolean r29) throws org.osgi.framework.BundleException {
        /*
            if (r24 == 0) goto L_0x000c
            if (r25 == 0) goto L_0x000c
            r0 = r24
            int r3 = r0.length
            r0 = r25
            int r5 = r0.length
            if (r3 == r5) goto L_0x0015
        L_0x000c:
            java.lang.IllegalArgumentException r3 = new java.lang.IllegalArgumentException
            java.lang.String r5 = "locations and files must not be null and must be same length"
            r3.<init>(r5)
            throw r3
        L_0x0015:
            r2 = 0
            java.util.HashMap r21 = new java.util.HashMap
            r21.<init>()
            java.io.File r22 = new java.io.File
            java.lang.String r3 = STORAGE_LOCATION
            r0 = r22
            r0.<init>(r3)
            if (r29 == 0) goto L_0x005f
            r22 = 0
            android.app.Application r3 = android.taobao.atlas.runtime.RuntimeVariables.androidApplication
            java.lang.String r5 = "storage"
            java.io.File[] r17 = getExternalFilesDirs(r3, r5)
            if (r17 == 0) goto L_0x005f
            r0 = r17
            int r3 = r0.length
            if (r3 <= 0) goto L_0x005f
            r0 = r17
            int r5 = r0.length
            r3 = 0
        L_0x003c:
            if (r3 >= r5) goto L_0x005f
            r16 = r17[r3]
            if (r16 == 0) goto L_0x005c
            java.lang.String r6 = getStorageState(r16)
            java.lang.String r7 = "mounted"
            boolean r6 = r6.equals(r7)
            if (r6 == 0) goto L_0x005c
            long r6 = r16.getUsableSpace()
            r8 = 52428800(0x3200000, double:2.5903269E-316)
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 <= 0) goto L_0x005c
            r22 = r16
        L_0x005c:
            int r3 = r3 + 1
            goto L_0x003c
        L_0x005f:
            if (r22 != 0) goto L_0x006a
            org.osgi.framework.BundleException r3 = new org.osgi.framework.BundleException
            java.lang.String r5 = "no enough space"
            r3.<init>((java.lang.String) r5)
            throw r3
        L_0x006a:
            r3 = 1
            updateHappend = r3
            r18 = 0
        L_0x006f:
            r0 = r24
            int r3 = r0.length
            r0 = r18
            if (r0 >= r3) goto L_0x02d8
            if (r23 == 0) goto L_0x0086
            r3 = r26[r18]
            java.lang.String r5 = "-1"
            boolean r3 = r3.equals(r5)
            if (r3 == 0) goto L_0x0086
        L_0x0083:
            int r18 = r18 + 1
            goto L_0x006f
        L_0x0086:
            if (r23 != 0) goto L_0x009b
            r6 = r27[r18]
            r8 = -1
            int r3 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r3 != 0) goto L_0x009b
            r3 = r24[r18]
            java.lang.String r5 = "-1"
            r0 = r21
            r0.put(r3, r5)
            goto L_0x0083
        L_0x009b:
            r3 = r24[r18]
            if (r3 == 0) goto L_0x0083
            r3 = r25[r18]
            if (r3 == 0) goto L_0x0083
            r12 = 0
            r3 = r24[r18]     // Catch:{ Exception -> 0x033e, all -> 0x033a }
            boolean r3 = isKernalBundle(r3)     // Catch:{ Exception -> 0x033e, all -> 0x033a }
            if (r3 == 0) goto L_0x016c
            r3 = r24[r18]     // Catch:{ Exception -> 0x033e, all -> 0x033a }
            android.taobao.atlas.util.BundleLock.WriteLock(r3)     // Catch:{ Exception -> 0x033e, all -> 0x033a }
            java.lang.ClassLoader r3 = android.taobao.atlas.runtime.RuntimeVariables.getRawClassLoader()     // Catch:{ Exception -> 0x033e, all -> 0x033a }
            java.lang.String r5 = "android.taobao.atlas.startup.patch.KernalBundle"
            java.lang.Class r2 = r3.loadClass(r5)     // Catch:{ Exception -> 0x033e, all -> 0x033a }
            java.io.File r4 = new java.io.File     // Catch:{ Exception -> 0x033e, all -> 0x033a }
            java.lang.String r3 = "com.taobao.maindex"
            r0 = r22
            r4.<init>(r0, r3)     // Catch:{ Exception -> 0x033e, all -> 0x033a }
            boolean r3 = r4.exists()     // Catch:{ Exception -> 0x0150 }
            if (r3 != 0) goto L_0x00cf
            r4.mkdirs()     // Catch:{ Exception -> 0x0150 }
        L_0x00cf:
            android.taobao.atlas.util.AtlasFileLock r3 = android.taobao.atlas.util.AtlasFileLock.getInstance()     // Catch:{ Exception -> 0x0150 }
            r3.LockExclusive(r4)     // Catch:{ Exception -> 0x0150 }
            r3 = 4
            java.lang.Class[] r3 = new java.lang.Class[r3]     // Catch:{ Exception -> 0x0150 }
            r5 = 0
            java.lang.Class<java.io.File> r6 = java.io.File.class
            r3[r5] = r6     // Catch:{ Exception -> 0x0150 }
            r5 = 1
            java.lang.Class<java.io.File> r6 = java.io.File.class
            r3[r5] = r6     // Catch:{ Exception -> 0x0150 }
            r5 = 2
            java.lang.Class<java.lang.String> r6 = java.lang.String.class
            r3[r5] = r6     // Catch:{ Exception -> 0x0150 }
            r5 = 3
            java.lang.Class r6 = java.lang.Long.TYPE     // Catch:{ Exception -> 0x0150 }
            r3[r5] = r6     // Catch:{ Exception -> 0x0150 }
            java.lang.reflect.Constructor r14 = r2.getDeclaredConstructor(r3)     // Catch:{ Exception -> 0x0150 }
            r3 = 1
            r14.setAccessible(r3)     // Catch:{ Exception -> 0x0150 }
            if (r23 == 0) goto L_0x0134
            r3 = 4
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x0150 }
            r5 = 0
            r3[r5] = r4     // Catch:{ Exception -> 0x0150 }
            r5 = 1
            r6 = r25[r18]     // Catch:{ Exception -> 0x0150 }
            r3[r5] = r6     // Catch:{ Exception -> 0x0150 }
            r5 = 2
            r6 = r26[r18]     // Catch:{ Exception -> 0x0150 }
            r0 = r28
            java.lang.String r6 = makeMainDexUniqueTag(r0, r6)     // Catch:{ Exception -> 0x0150 }
            r3[r5] = r6     // Catch:{ Exception -> 0x0150 }
            r5 = 3
            r6 = -1
            java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch:{ Exception -> 0x0150 }
            r3[r5] = r6     // Catch:{ Exception -> 0x0150 }
            r14.newInstance(r3)     // Catch:{ Exception -> 0x0150 }
        L_0x0119:
            if (r23 == 0) goto L_0x02b9
            r3 = r24[r18]     // Catch:{ Exception -> 0x0150 }
            r5 = r26[r18]     // Catch:{ Exception -> 0x0150 }
            r0 = r21
            r0.put(r3, r5)     // Catch:{ Exception -> 0x0150 }
        L_0x0124:
            if (r4 == 0) goto L_0x012d
            android.taobao.atlas.util.AtlasFileLock r3 = android.taobao.atlas.util.AtlasFileLock.getInstance()
            r3.unLock(r4)
        L_0x012d:
            r3 = r24[r18]
            android.taobao.atlas.util.BundleLock.WriteUnLock(r3)
            goto L_0x0083
        L_0x0134:
            r3 = 4
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x0150 }
            r5 = 0
            r3[r5] = r4     // Catch:{ Exception -> 0x0150 }
            r5 = 1
            r6 = r25[r18]     // Catch:{ Exception -> 0x0150 }
            r3[r5] = r6     // Catch:{ Exception -> 0x0150 }
            r5 = 2
            r6 = 0
            r3[r5] = r6     // Catch:{ Exception -> 0x0150 }
            r5 = 3
            r6 = r27[r18]     // Catch:{ Exception -> 0x0150 }
            java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch:{ Exception -> 0x0150 }
            r3[r5] = r6     // Catch:{ Exception -> 0x0150 }
            r14.newInstance(r3)     // Catch:{ Exception -> 0x0150 }
            goto L_0x0119
        L_0x0150:
            r15 = move-exception
        L_0x0151:
            if (r23 == 0) goto L_0x02c8
            org.osgi.framework.BundleException r3 = new org.osgi.framework.BundleException     // Catch:{ all -> 0x015c }
            java.lang.String r5 = "failed to installOrUpdate bundles "
            r3.<init>(r5, r15)     // Catch:{ all -> 0x015c }
            throw r3     // Catch:{ all -> 0x015c }
        L_0x015c:
            r3 = move-exception
        L_0x015d:
            if (r4 == 0) goto L_0x0166
            android.taobao.atlas.util.AtlasFileLock r5 = android.taobao.atlas.util.AtlasFileLock.getInstance()
            r5.unLock(r4)
        L_0x0166:
            r5 = r24[r18]
            android.taobao.atlas.util.BundleLock.WriteUnLock(r5)
            throw r3
        L_0x016c:
            java.io.File r4 = new java.io.File     // Catch:{ Exception -> 0x033e, all -> 0x033a }
            r3 = r24[r18]     // Catch:{ Exception -> 0x033e, all -> 0x033a }
            r0 = r22
            r4.<init>(r0, r3)     // Catch:{ Exception -> 0x033e, all -> 0x033a }
            boolean r3 = r4.exists()     // Catch:{ Exception -> 0x0150 }
            if (r3 != 0) goto L_0x017e
            r4.mkdirs()     // Catch:{ Exception -> 0x0150 }
        L_0x017e:
            if (r23 == 0) goto L_0x029d
            android.taobao.atlas.bundleInfo.AtlasBundleInfoManager r3 = android.taobao.atlas.bundleInfo.AtlasBundleInfoManager.instance()     // Catch:{ Exception -> 0x0150 }
            r5 = r24[r18]     // Catch:{ Exception -> 0x0150 }
            android.taobao.atlas.bundleInfo.BundleListing$BundleInfo r19 = r3.getBundleInfo(r5)     // Catch:{ Exception -> 0x0150 }
            if (r19 == 0) goto L_0x01ea
            java.lang.String r3 = r19.getUnique_tag()     // Catch:{ Exception -> 0x0150 }
            r5 = r26[r18]     // Catch:{ Exception -> 0x0150 }
            boolean r3 = r3.equals(r5)     // Catch:{ Exception -> 0x0150 }
            if (r3 == 0) goto L_0x01ea
            java.lang.String r3 = "Framework"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0150 }
            r5.<init>()     // Catch:{ Exception -> 0x0150 }
            r6 = r24[r18]     // Catch:{ Exception -> 0x0150 }
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x0150 }
            java.lang.String r6 = " unitTag is same as before,it maybe a mistake"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x0150 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x0150 }
            android.util.Log.e(r3, r5)     // Catch:{ Exception -> 0x0150 }
            boolean r3 = isDeubgMode()     // Catch:{ Exception -> 0x0150 }
            if (r3 == 0) goto L_0x01da
            android.app.Application r3 = android.taobao.atlas.runtime.RuntimeVariables.androidApplication     // Catch:{ Exception -> 0x0150 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0150 }
            r5.<init>()     // Catch:{ Exception -> 0x0150 }
            r6 = r24[r18]     // Catch:{ Exception -> 0x0150 }
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x0150 }
            java.lang.String r6 = " unitTag is same as before,it maybe a mistake"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x0150 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x0150 }
            r6 = 1
            android.widget.Toast r3 = android.widget.Toast.makeText(r3, r5, r6)     // Catch:{ Exception -> 0x0150 }
            r3.show()     // Catch:{ Exception -> 0x0150 }
        L_0x01da:
            if (r4 == 0) goto L_0x01e3
            android.taobao.atlas.util.AtlasFileLock r3 = android.taobao.atlas.util.AtlasFileLock.getInstance()
            r3.unLock(r4)
        L_0x01e3:
            r3 = r24[r18]
            android.taobao.atlas.util.BundleLock.WriteUnLock(r3)
            goto L_0x0083
        L_0x01ea:
            r3 = r25[r18]     // Catch:{ Exception -> 0x0150 }
            java.lang.String r3 = r3.getName()     // Catch:{ Exception -> 0x0150 }
            java.lang.String r5 = "inherit"
            boolean r3 = r3.equals(r5)     // Catch:{ Exception -> 0x0150 }
            if (r3 == 0) goto L_0x0280
            r3 = 1
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch:{ Exception -> 0x0150 }
            r5 = 0
            r6 = r24[r18]     // Catch:{ Exception -> 0x0150 }
            r3[r5] = r6     // Catch:{ Exception -> 0x0150 }
            android.taobao.atlas.runtime.BundleUtil.checkBundleStateSync(r3)     // Catch:{ Exception -> 0x0150 }
            r3 = r24[r18]     // Catch:{ Exception -> 0x0150 }
            android.taobao.atlas.util.BundleLock.WriteLock(r3)     // Catch:{ Exception -> 0x0150 }
            android.taobao.atlas.util.AtlasFileLock r3 = android.taobao.atlas.util.AtlasFileLock.getInstance()     // Catch:{ Exception -> 0x0150 }
            r3.LockExclusive(r4)     // Catch:{ Exception -> 0x0150 }
            r3 = r24[r18]     // Catch:{ Exception -> 0x0150 }
            android.taobao.atlas.bundleInfo.AtlasBundleInfoManager r5 = android.taobao.atlas.bundleInfo.AtlasBundleInfoManager.instance()     // Catch:{ Exception -> 0x0150 }
            r6 = r24[r18]     // Catch:{ Exception -> 0x0150 }
            android.taobao.atlas.bundleInfo.BundleListing$BundleInfo r5 = r5.getBundleInfo(r6)     // Catch:{ Exception -> 0x0150 }
            java.lang.String r5 = r5.getUnique_tag()     // Catch:{ Exception -> 0x0150 }
            java.io.File r13 = getInstalledBundle(r3, r5)     // Catch:{ Exception -> 0x0150 }
            if (r13 == 0) goto L_0x022c
            boolean r3 = r13.exists()     // Catch:{ Exception -> 0x0150 }
            if (r3 != 0) goto L_0x0235
        L_0x022c:
            java.io.IOException r3 = new java.io.IOException     // Catch:{ Exception -> 0x0150 }
            java.lang.String r5 = "can not find source bundle : new bundle is inherit"
            r3.<init>(r5)     // Catch:{ Exception -> 0x0150 }
            throw r3     // Catch:{ Exception -> 0x0150 }
        L_0x0235:
            java.lang.String r3 = r13.getAbsolutePath()     // Catch:{ Exception -> 0x0150 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0150 }
            r5.<init>()     // Catch:{ Exception -> 0x0150 }
            java.lang.String r6 = "storage/"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x0150 }
            r6 = r24[r18]     // Catch:{ Exception -> 0x0150 }
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x0150 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x0150 }
            boolean r3 = r3.contains(r5)     // Catch:{ Exception -> 0x0150 }
            if (r3 == 0) goto L_0x026b
            java.io.File r20 = r13.getParentFile()     // Catch:{ Exception -> 0x0150 }
        L_0x0259:
            java.io.File r3 = new java.io.File     // Catch:{ Exception -> 0x0150 }
            java.lang.String r5 = r20.getParent()     // Catch:{ Exception -> 0x0150 }
            r6 = r26[r18]     // Catch:{ Exception -> 0x0150 }
            r3.<init>(r5, r6)     // Catch:{ Exception -> 0x0150 }
            r0 = r20
            android.taobao.atlas.util.ApkUtils.copyDirectory(r0, r3)     // Catch:{ Exception -> 0x0150 }
            goto L_0x0119
        L_0x026b:
            r3 = r24[r18]     // Catch:{ Exception -> 0x0150 }
            org.osgi.framework.Bundle r3 = getBundle(r3)     // Catch:{ Exception -> 0x0150 }
            android.taobao.atlas.framework.BundleImpl r3 = (android.taobao.atlas.framework.BundleImpl) r3     // Catch:{ Exception -> 0x0150 }
            android.taobao.atlas.framework.bundlestorage.BundleArchive r3 = r3.getArchive()     // Catch:{ Exception -> 0x0150 }
            android.taobao.atlas.framework.bundlestorage.BundleArchiveRevision r3 = r3.getCurrentRevision()     // Catch:{ Exception -> 0x0150 }
            java.io.File r20 = r3.getRevisionDir()     // Catch:{ Exception -> 0x0150 }
            goto L_0x0259
        L_0x0280:
            r3 = r24[r18]     // Catch:{ Exception -> 0x0150 }
            android.taobao.atlas.util.BundleLock.WriteLock(r3)     // Catch:{ Exception -> 0x0150 }
            android.taobao.atlas.util.AtlasFileLock r3 = android.taobao.atlas.util.AtlasFileLock.getInstance()     // Catch:{ Exception -> 0x0150 }
            r3.LockExclusive(r4)     // Catch:{ Exception -> 0x0150 }
            android.taobao.atlas.framework.BundleImpl r3 = new android.taobao.atlas.framework.BundleImpl     // Catch:{ Exception -> 0x0150 }
            r5 = r24[r18]     // Catch:{ Exception -> 0x0150 }
            r6 = 0
            r7 = r25[r18]     // Catch:{ Exception -> 0x0150 }
            r8 = r26[r18]     // Catch:{ Exception -> 0x0150 }
            r9 = 0
            r10 = -1
            r3.<init>(r4, r5, r6, r7, r8, r9, r10)     // Catch:{ Exception -> 0x0150 }
            goto L_0x0119
        L_0x029d:
            r3 = r24[r18]     // Catch:{ Exception -> 0x0150 }
            android.taobao.atlas.util.BundleLock.WriteLock(r3)     // Catch:{ Exception -> 0x0150 }
            android.taobao.atlas.util.AtlasFileLock r3 = android.taobao.atlas.util.AtlasFileLock.getInstance()     // Catch:{ Exception -> 0x0150 }
            r3.LockExclusive(r4)     // Catch:{ Exception -> 0x0150 }
            android.taobao.atlas.framework.BundleImpl r3 = new android.taobao.atlas.framework.BundleImpl     // Catch:{ Exception -> 0x0150 }
            r5 = r24[r18]     // Catch:{ Exception -> 0x0150 }
            r6 = 0
            r7 = r25[r18]     // Catch:{ Exception -> 0x0150 }
            r8 = 0
            r9 = 0
            r10 = r27[r18]     // Catch:{ Exception -> 0x0150 }
            r3.<init>(r4, r5, r6, r7, r8, r9, r10)     // Catch:{ Exception -> 0x0150 }
            goto L_0x0119
        L_0x02b9:
            r3 = r24[r18]     // Catch:{ Exception -> 0x0150 }
            r6 = r27[r18]     // Catch:{ Exception -> 0x0150 }
            java.lang.String r5 = java.lang.Long.toString(r6)     // Catch:{ Exception -> 0x0150 }
            r0 = r21
            r0.put(r3, r5)     // Catch:{ Exception -> 0x0150 }
            goto L_0x0124
        L_0x02c8:
            if (r4 == 0) goto L_0x02d1
            android.taobao.atlas.util.AtlasFileLock r3 = android.taobao.atlas.util.AtlasFileLock.getInstance()
            r3.unLock(r4)
        L_0x02d1:
            r3 = r24[r18]
            android.taobao.atlas.util.BundleLock.WriteUnLock(r3)
            goto L_0x0083
        L_0x02d8:
            if (r23 == 0) goto L_0x0316
            r3 = 1
            bundleUpdated = r3
            android.taobao.atlas.versionInfo.BaselineInfoManager r5 = android.taobao.atlas.versionInfo.BaselineInfoManager.instance()     // Catch:{ IOException -> 0x030c }
            if (r29 == 0) goto L_0x0308
            java.lang.String r3 = r22.getAbsolutePath()     // Catch:{ IOException -> 0x030c }
        L_0x02e7:
            r0 = r28
            r1 = r21
            r5.saveBaselineInfo(r0, r1, r3)     // Catch:{ IOException -> 0x030c }
            java.lang.String r3 = "installedVersionWhenUpdated"
            java.lang.String r5 = containerVersion
            android.taobao.atlas.util.WrapperUtil.appendLog(r3, r5)
            java.lang.String r3 = "VersionWhenUpdated"
            android.app.Application r5 = android.taobao.atlas.runtime.RuntimeVariables.androidApplication
            android.content.pm.PackageInfo r5 = android.taobao.atlas.util.WrapperUtil.getPackageInfo(r5)
            java.lang.String r5 = r5.versionName
            android.taobao.atlas.util.WrapperUtil.appendLog(r3, r5)
            android.taobao.atlas.runtime.InstrumentationHook.notifyAppUpdated()
        L_0x0307:
            return
        L_0x0308:
            java.lang.String r3 = ""
            goto L_0x02e7
        L_0x030c:
            r15 = move-exception
            org.osgi.framework.BundleException r3 = new org.osgi.framework.BundleException
            java.lang.String r5 = "save baseline info fail"
            r3.<init>((java.lang.String) r5)
            throw r3
        L_0x0316:
            int r3 = r21.size()
            if (r3 <= 0) goto L_0x0307
            android.taobao.atlas.versionInfo.BaselineInfoManager r5 = android.taobao.atlas.versionInfo.BaselineInfoManager.instance()     // Catch:{ IOException -> 0x032c }
            if (r29 == 0) goto L_0x0336
            java.lang.String r3 = r22.getAbsolutePath()     // Catch:{ IOException -> 0x032c }
        L_0x0326:
            r0 = r21
            r5.saveDexPathInfo(r0, r3)     // Catch:{ IOException -> 0x032c }
            goto L_0x0307
        L_0x032c:
            r15 = move-exception
            org.osgi.framework.BundleException r3 = new org.osgi.framework.BundleException
            java.lang.String r5 = "save dexpatch info fail"
            r3.<init>((java.lang.String) r5)
            throw r3
        L_0x0336:
            java.lang.String r3 = ""
            goto L_0x0326
        L_0x033a:
            r3 = move-exception
            r4 = r12
            goto L_0x015d
        L_0x033e:
            r15 = move-exception
            r4 = r12
            goto L_0x0151
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.framework.Framework.update(boolean, java.lang.String[], java.io.File[], java.lang.String[], long[], java.lang.String, boolean):void");
    }

    private static String makeMainDexUniqueTag(String appVersion, String maindexTag) {
        return maindexTag.startsWith(appVersion) ? maindexTag : appVersion + "_" + maindexTag;
    }

    public static void rollback() {
        BaselineInfoManager.instance().rollback();
    }

    static boolean isKernalBundle(String location) {
        if (TextUtils.isEmpty(location)) {
            return false;
        }
        return location.equals(MergeConstants.MAIN_DEX);
    }

    static void notifyBundleListeners(int state, Bundle bundle) {
        BundleListener[] asyncs;
        if (!syncBundleListeners.isEmpty() || !bundleListeners.isEmpty()) {
            BundleEvent event = new BundleEvent(state, bundle);
            BundleListener[] syncs = (BundleListener[]) syncBundleListeners.toArray(new BundleListener[syncBundleListeners.size()]);
            for (BundleListener bundleChanged : syncs) {
                bundleChanged.bundleChanged(event);
            }
            if (!bundleListeners.isEmpty()) {
                synchronized (bundleListeners) {
                    asyncs = (BundleListener[]) bundleListeners.toArray(new BundleListener[bundleListeners.size()]);
                }
                for (BundleListener bundleChanged2 : asyncs) {
                    bundleChanged2.bundleChanged(event);
                }
            }
        }
    }

    public static void addFrameworkListener(FrameworkListener listener) {
        frameworkListeners.add(listener);
    }

    public static void removeFrameworkListener(FrameworkListener listener) {
        frameworkListeners.remove(listener);
    }

    static void addBundleListener(BundleListener listener) {
        if (listener == null) {
            Log.e("Framework", "the listener must not be null", new Exception());
        }
        synchronized (bundleListeners) {
            bundleListeners.add(listener);
        }
    }

    static void removeBundleListener(BundleListener listener) {
        synchronized (bundleListeners) {
            bundleListeners.remove(listener);
        }
    }

    static void notifyFrameworkListeners(int state, Bundle bundle, Throwable throwable) {
        if (!frameworkListeners.isEmpty()) {
            FrameworkEvent event = new FrameworkEvent(state);
            FrameworkListener[] listeners = (FrameworkListener[]) frameworkListeners.toArray(new FrameworkListener[frameworkListeners.size()]);
            for (FrameworkListener listener : listeners) {
                listener.frameworkEvent(event);
            }
        }
    }

    public static ClassNotFoundInterceptorCallback getClassNotFoundCallback() {
        return classNotFoundCallback;
    }

    public static void setClassNotFoundCallback(ClassNotFoundInterceptorCallback classNotFoundCallback2) {
        classNotFoundCallback = classNotFoundCallback2;
    }

    public static File getInstalledBundle(String location, String bundleUniqueId) {
        File internalBundleStorage = new File(STORAGE_LOCATION, location + File.separator + bundleUniqueId);
        if (!internalBundleStorage.exists() || (!isKernalBundle(location) && !new File(internalBundleStorage, "meta").exists())) {
            File[] externalStorages = getExternalFilesDirs(RuntimeVariables.androidApplication, "storage");
            if (externalStorages != null && externalStorages.length > 0) {
                for (File tmpDir : externalStorages) {
                    if (tmpDir != null && getStorageState(tmpDir).equals("mounted") && new File(tmpDir, location + File.separator + bundleUniqueId).exists()) {
                        File file = getInstalledBundleInternal(location, bundleUniqueId, new File(tmpDir, location));
                        if (file.exists()) {
                            return file;
                        }
                    }
                }
            }
        } else {
            File file2 = getInstalledBundleInternal(location, bundleUniqueId, new File(STORAGE_LOCATION, location));
            if (file2 != null) {
                return file2;
            }
        }
        return null;
    }

    private static File getInstalledBundleInternal(String bundleName, String bundleUniqueId, File bundleDir) {
        File file;
        if (bundleDir != null && bundleDir.exists()) {
            if (isKernalBundle(bundleName)) {
                File file2 = new File(bundleDir, makeMainDexUniqueTag(BaselineInfoManager.instance().currentVersionName(), bundleUniqueId) + File.separator + "com_taobao_maindex.zip");
                if (file2.exists()) {
                    return file2;
                }
            } else {
                try {
                    BundleArchive archive = new BundleArchive(bundleName, bundleDir, bundleUniqueId, 0);
                    if (!(archive == null || (file = archive.getArchiveFile()) == null || !file.exists())) {
                        return file;
                    }
                } catch (Throwable th) {
                    return null;
                }
            }
        }
        return null;
    }

    private static File createBundleStorage(String bundleName) {
        if (new File(STORAGE_LOCATION).getUsableSpace() > 10485760) {
            return new File(STORAGE_LOCATION, bundleName);
        }
        File externalStorageDir = null;
        File[] externalStorages = getExternalFilesDirs(RuntimeVariables.androidApplication, "storage");
        if (externalStorages != null && externalStorages.length > 0) {
            int length = externalStorages.length;
            int i = 0;
            while (true) {
                if (i < length) {
                    File tmpDir = externalStorages[i];
                    if (tmpDir != null && getStorageState(tmpDir).equals("mounted") && tmpDir.getUsableSpace() > 20971520) {
                        externalStorageDir = tmpDir;
                        break;
                    }
                    i++;
                } else {
                    break;
                }
            }
        }
        if (externalStorageDir != null) {
            return new File(externalStorageDir, bundleName);
        }
        return new File(STORAGE_LOCATION, bundleName);
    }

    public static File[] getExternalFilesDirs(Context context, String type) {
        if (Build.VERSION.SDK_INT >= 19) {
            return context.getExternalFilesDirs(type);
        }
        return new File[]{context.getExternalFilesDir(type)};
    }

    public static String getStorageState(File path) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Environment.getStorageState(path);
        }
        try {
            if (path.getCanonicalPath().startsWith(Environment.getExternalStorageDirectory().getCanonicalPath())) {
                return Environment.getExternalStorageState();
            }
        } catch (IOException e) {
        }
        return "unknown";
    }

    public static String getCurProcessName() {
        return RuntimeVariables.sCurrentProcessName;
    }

    public static boolean isUpdated() {
        return bundleUpdated;
    }

    public static boolean isDeubgMode() {
        return DEBUG;
    }
}
