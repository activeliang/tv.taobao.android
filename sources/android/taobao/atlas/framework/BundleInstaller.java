package android.taobao.atlas.framework;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.MessageQueue;
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.bundleInfo.BundleListing;
import android.taobao.atlas.runtime.LowDiskException;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.atlas.util.ApkUtils;
import android.taobao.atlas.util.BundleLock;
import android.taobao.atlas.util.FileUtils;
import android.taobao.atlas.util.log.impl.AtlasMonitor;
import android.taobao.atlas.versionInfo.BaselineInfoManager;
import android.util.Log;
import android.util.Pair;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.osgi.framework.Bundle;

public class BundleInstaller implements Callable {
    private static Handler sBundleHandler;
    /* access modifiers changed from: private */
    public static List<Pair<String, InstallListener>> sDelayInstallBundles = new ArrayList();
    /* access modifiers changed from: private */
    public static MessageQueue.IdleHandler sIdleHandler;
    /* access modifiers changed from: private */
    public static List<Pair<String, InstallListener>> sIdleInstallBundles = new ArrayList();
    private File[] mBundleSourceFile;
    private InputStream[] mBundleSourceInputStream;
    /* access modifiers changed from: private */
    public InstallListener mListener;
    /* access modifiers changed from: private */
    public String[] mLocation;
    private File mTmpBundleSourceFile;
    private InputStream mTmpBundleSourceInputStream;
    private boolean mTransitive;

    public interface InstallListener {
        void onFinished();
    }

    static {
        HandlerThread handlerThread = new HandlerThread("bundle_installer");
        handlerThread.start();
        sBundleHandler = new Handler(handlerThread.getLooper());
    }

    public static synchronized void createIdleInstallerIfNeed() {
        synchronized (BundleInstaller.class) {
            if (sIdleHandler == null) {
                sIdleHandler = new MessageQueue.IdleHandler() {
                    public boolean queueIdle() {
                        if (BundleInstaller.sIdleInstallBundles.size() == 0 && BundleInstaller.sDelayInstallBundles.size() == 0) {
                            MessageQueue.IdleHandler unused = BundleInstaller.sIdleHandler = null;
                            return false;
                        }
                        Pair<String, InstallListener> bundleStruct = null;
                        if (BundleInstaller.sDelayInstallBundles.size() > 0) {
                            bundleStruct = (Pair) BundleInstaller.sDelayInstallBundles.remove(0);
                        } else if (BundleInstaller.sIdleInstallBundles.size() > 0) {
                            bundleStruct = (Pair) BundleInstaller.sIdleInstallBundles.remove(0);
                        }
                        if (bundleStruct != null) {
                            String bundleName = (String) bundleStruct.first;
                            InstallListener listener = (InstallListener) bundleStruct.second;
                            BundleImpl impl = (BundleImpl) Atlas.getInstance().getBundle(bundleName);
                            if (impl == null || !impl.checkValidate()) {
                                Log.d("BundleInstaller", "idle install bundle : " + bundleName);
                                BundleInstallerFetcher.obtainInstaller().installTransitivelyAsync(new String[]{bundleName}, listener);
                                return true;
                            }
                        }
                        return true;
                    }
                };
                Looper.myQueue().addIdleHandler(sIdleHandler);
            }
        }
    }

    public static void startIdleInstall(String location, InstallListener listener) {
        createIdleInstallerIfNeed();
        if (location != null) {
            BundleImpl impl = (BundleImpl) Atlas.getInstance().getBundle(location);
            if (impl == null || !impl.checkValidate()) {
                sIdleInstallBundles.add(new Pair(location, listener));
            }
        }
    }

    public static void startDelayInstall(String location, InstallListener listener) {
        createIdleInstallerIfNeed();
        if (location != null) {
            BundleImpl impl = (BundleImpl) Atlas.getInstance().getBundle(location);
            if (impl == null || !impl.checkValidate()) {
                sDelayInstallBundles.add(new Pair(location, listener));
            }
        }
    }

    BundleInstaller() {
    }

    /* access modifiers changed from: package-private */
    public void release() {
        this.mLocation = null;
        this.mBundleSourceInputStream = null;
        this.mBundleSourceFile = null;
        this.mListener = null;
        this.mTmpBundleSourceFile = null;
        this.mTmpBundleSourceInputStream = null;
        this.mTransitive = false;
    }

    public void installTransitivelyAsync(String[] location, InstallListener listener) {
        if (location != null) {
            release();
            this.mTransitive = true;
            this.mLocation = location;
            this.mListener = listener;
            try {
                installBundleInternal(false);
            } catch (Throwable e) {
                e.printStackTrace();
                if (this.mListener != null) {
                    this.mListener.onFinished();
                }
                BundleInstallerFetcher.recycle(this);
            }
        }
    }

    public void installTransitivelySync(String[] location) {
        if (location != null) {
            release();
            this.mTransitive = true;
            this.mLocation = location;
            try {
                installBundleInternal(true);
            } catch (Throwable e) {
                e.printStackTrace();
                BundleInstallerFetcher.recycle(this);
            }
        }
    }

    public void installSync(String[] location) {
        if (location != null) {
            release();
            this.mTransitive = false;
            this.mLocation = location;
            try {
                installBundleInternal(true);
            } catch (Throwable e) {
                e.printStackTrace();
                BundleInstallerFetcher.recycle(this);
            }
        }
    }

    public void installAsync(String[] location, InputStream[] input, InstallListener listener) {
        if (Thread.currentThread().getId() != Looper.getMainLooper().getThread().getId()) {
            throw new RuntimeException("method executeOnUIThread is only prepared for UI Thread");
        }
        release();
        this.mListener = listener;
        this.mBundleSourceInputStream = input;
        this.mLocation = location;
        try {
            checkSrc(location, input);
            installBundleInternal(false);
        } catch (Throwable e) {
            e.printStackTrace();
            if (this.mListener != null) {
                this.mListener.onFinished();
            }
            BundleInstallerFetcher.recycle(this);
        }
    }

    public void installAsync(String[] location, File[] input, InstallListener listener) {
        if (Thread.currentThread().getId() != Looper.getMainLooper().getThread().getId()) {
            throw new RuntimeException("method executeOnUIThread is only prepared for UI Thread");
        }
        release();
        this.mListener = listener;
        this.mBundleSourceFile = input;
        this.mLocation = location;
        try {
            checkSrc(location, input);
            installBundleInternal(false);
        } catch (Throwable e) {
            e.printStackTrace();
            BundleInstallerFetcher.recycle(this);
        }
    }

    public void installSync(String[] location, InputStream[] input) {
        try {
            release();
            this.mBundleSourceInputStream = input;
            this.mLocation = location;
            checkSrc(location, input);
            installBundleInternal(true);
        } catch (Throwable e) {
            e.printStackTrace();
            BundleInstallerFetcher.recycle(this);
        }
    }

    public void installSync(String[] location, File[] input) {
        try {
            release();
            this.mBundleSourceFile = input;
            this.mLocation = location;
            checkSrc(location, input);
            installBundleInternal(true);
        } catch (Throwable e) {
            e.printStackTrace();
            BundleInstallerFetcher.recycle(this);
        }
    }

    private void checkSrc(String[] location, Object[] input) {
        if (location == null || location.length == 0) {
            throw new RuntimeException("bundle name can not be empty");
        } else if (input == null || input.length == 0) {
            throw new RuntimeException("can not find raw bundle file");
        }
    }

    private void installBundleInternal(boolean sync) throws Exception {
        if (Thread.currentThread().getId() == sBundleHandler.getLooper().getThread().getId()) {
            Log.e("BundleInstaller", Arrays.toString(this.mLocation));
            call();
            if (this.mListener != null) {
                this.mListener.onFinished();
            }
            BundleInstallerFetcher.recycle(this);
        } else if (!sync) {
            deliveryTask(sync);
        } else if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            call();
            BundleInstallerFetcher.recycle(this);
        } else {
            synchronized (this) {
                deliveryTask(sync);
                Log.d("BundleInstaller", "call wait:" + this);
                wait(30000);
                BundleInstallerFetcher.recycle(this);
            }
        }
    }

    private void deliveryTask(boolean sync) {
        sBundleHandler.post(new Runnable() {
            /* JADX WARNING: Unknown top exception splitter block from list: {B:23:0x0058=Splitter:B:23:0x0058, B:48:0x00ab=Splitter:B:48:0x00ab, B:12:0x002e=Splitter:B:12:0x002e} */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r6 = this;
                    android.taobao.atlas.framework.BundleInstaller r2 = android.taobao.atlas.framework.BundleInstaller.this
                    monitor-enter(r2)
                    android.taobao.atlas.framework.BundleInstaller r1 = android.taobao.atlas.framework.BundleInstaller.this     // Catch:{ Throwable -> 0x005d }
                    java.lang.String[] r1 = r1.mLocation     // Catch:{ Throwable -> 0x005d }
                    if (r1 != 0) goto L_0x0030
                    android.taobao.atlas.framework.BundleInstaller r1 = android.taobao.atlas.framework.BundleInstaller.this     // Catch:{ all -> 0x005a }
                    r1.notify()     // Catch:{ all -> 0x005a }
                    android.taobao.atlas.framework.BundleInstaller r1 = android.taobao.atlas.framework.BundleInstaller.this     // Catch:{ all -> 0x005a }
                    android.taobao.atlas.framework.BundleInstaller$InstallListener r1 = r1.mListener     // Catch:{ all -> 0x005a }
                    if (r1 == 0) goto L_0x0029
                    android.os.Handler r1 = new android.os.Handler     // Catch:{ all -> 0x005a }
                    android.os.Looper r3 = android.os.Looper.getMainLooper()     // Catch:{ all -> 0x005a }
                    r1.<init>(r3)     // Catch:{ all -> 0x005a }
                    android.taobao.atlas.framework.BundleInstaller$2$1 r3 = new android.taobao.atlas.framework.BundleInstaller$2$1     // Catch:{ all -> 0x005a }
                    r3.<init>()     // Catch:{ all -> 0x005a }
                    r1.post(r3)     // Catch:{ all -> 0x005a }
                L_0x0029:
                    r4 = 1
                    java.lang.Thread.sleep(r4)     // Catch:{ InterruptedException -> 0x00ac }
                L_0x002e:
                    monitor-exit(r2)     // Catch:{ all -> 0x005a }
                L_0x002f:
                    return
                L_0x0030:
                    android.taobao.atlas.framework.BundleInstaller r1 = android.taobao.atlas.framework.BundleInstaller.this     // Catch:{ Throwable -> 0x005d }
                    r1.call()     // Catch:{ Throwable -> 0x005d }
                    android.taobao.atlas.framework.BundleInstaller r1 = android.taobao.atlas.framework.BundleInstaller.this     // Catch:{ all -> 0x005a }
                    r1.notify()     // Catch:{ all -> 0x005a }
                    android.taobao.atlas.framework.BundleInstaller r1 = android.taobao.atlas.framework.BundleInstaller.this     // Catch:{ all -> 0x005a }
                    android.taobao.atlas.framework.BundleInstaller$InstallListener r1 = r1.mListener     // Catch:{ all -> 0x005a }
                    if (r1 == 0) goto L_0x0053
                    android.os.Handler r1 = new android.os.Handler     // Catch:{ all -> 0x005a }
                    android.os.Looper r3 = android.os.Looper.getMainLooper()     // Catch:{ all -> 0x005a }
                    r1.<init>(r3)     // Catch:{ all -> 0x005a }
                    android.taobao.atlas.framework.BundleInstaller$2$1 r3 = new android.taobao.atlas.framework.BundleInstaller$2$1     // Catch:{ all -> 0x005a }
                    r3.<init>()     // Catch:{ all -> 0x005a }
                    r1.post(r3)     // Catch:{ all -> 0x005a }
                L_0x0053:
                    r4 = 1
                    java.lang.Thread.sleep(r4)     // Catch:{ InterruptedException -> 0x00ae }
                L_0x0058:
                    monitor-exit(r2)     // Catch:{ all -> 0x005a }
                    goto L_0x002f
                L_0x005a:
                    r1 = move-exception
                    monitor-exit(r2)     // Catch:{ all -> 0x005a }
                    throw r1
                L_0x005d:
                    r0 = move-exception
                    r0.printStackTrace()     // Catch:{ all -> 0x0087 }
                    android.taobao.atlas.framework.BundleInstaller r1 = android.taobao.atlas.framework.BundleInstaller.this     // Catch:{ all -> 0x005a }
                    r1.notify()     // Catch:{ all -> 0x005a }
                    android.taobao.atlas.framework.BundleInstaller r1 = android.taobao.atlas.framework.BundleInstaller.this     // Catch:{ all -> 0x005a }
                    android.taobao.atlas.framework.BundleInstaller$InstallListener r1 = r1.mListener     // Catch:{ all -> 0x005a }
                    if (r1 == 0) goto L_0x007f
                    android.os.Handler r1 = new android.os.Handler     // Catch:{ all -> 0x005a }
                    android.os.Looper r3 = android.os.Looper.getMainLooper()     // Catch:{ all -> 0x005a }
                    r1.<init>(r3)     // Catch:{ all -> 0x005a }
                    android.taobao.atlas.framework.BundleInstaller$2$1 r3 = new android.taobao.atlas.framework.BundleInstaller$2$1     // Catch:{ all -> 0x005a }
                    r3.<init>()     // Catch:{ all -> 0x005a }
                    r1.post(r3)     // Catch:{ all -> 0x005a }
                L_0x007f:
                    r4 = 1
                    java.lang.Thread.sleep(r4)     // Catch:{ InterruptedException -> 0x0085 }
                    goto L_0x0058
                L_0x0085:
                    r1 = move-exception
                    goto L_0x0058
                L_0x0087:
                    r1 = move-exception
                    android.taobao.atlas.framework.BundleInstaller r3 = android.taobao.atlas.framework.BundleInstaller.this     // Catch:{ all -> 0x005a }
                    r3.notify()     // Catch:{ all -> 0x005a }
                    android.taobao.atlas.framework.BundleInstaller r3 = android.taobao.atlas.framework.BundleInstaller.this     // Catch:{ all -> 0x005a }
                    android.taobao.atlas.framework.BundleInstaller$InstallListener r3 = r3.mListener     // Catch:{ all -> 0x005a }
                    if (r3 == 0) goto L_0x00a6
                    android.os.Handler r3 = new android.os.Handler     // Catch:{ all -> 0x005a }
                    android.os.Looper r4 = android.os.Looper.getMainLooper()     // Catch:{ all -> 0x005a }
                    r3.<init>(r4)     // Catch:{ all -> 0x005a }
                    android.taobao.atlas.framework.BundleInstaller$2$1 r4 = new android.taobao.atlas.framework.BundleInstaller$2$1     // Catch:{ all -> 0x005a }
                    r4.<init>()     // Catch:{ all -> 0x005a }
                    r3.post(r4)     // Catch:{ all -> 0x005a }
                L_0x00a6:
                    r4 = 1
                    java.lang.Thread.sleep(r4)     // Catch:{ InterruptedException -> 0x00b0 }
                L_0x00ab:
                    throw r1     // Catch:{ all -> 0x005a }
                L_0x00ac:
                    r1 = move-exception
                    goto L_0x002e
                L_0x00ae:
                    r1 = move-exception
                    goto L_0x0058
                L_0x00b0:
                    r3 = move-exception
                    goto L_0x00ab
                */
                throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.framework.BundleInstaller.AnonymousClass2.run():void");
            }
        });
    }

    public Bundle getInstalledBundle(String bundleName) {
        Bundle bundle = Framework.getBundle(bundleName);
        if (bundle == null) {
            try {
                BundleLock.WriteLock(bundleName);
                bundle = Framework.getBundle(bundleName);
                if (bundle == null) {
                    bundle = Framework.restoreFromExistedBundle(bundleName);
                }
                try {
                    BundleLock.WriteUnLock(bundleName);
                } catch (Throwable th) {
                }
            } catch (Throwable th2) {
            }
        }
        if (bundle == null && (BaselineInfoManager.instance().isDexPatched(bundleName) || BaselineInfoManager.instance().isUpdated(bundleName))) {
            Log.e("BundleInstaller", "restore existed bundle failed : " + bundleName);
        }
        return bundle;
        throw th;
    }

    public synchronized Void call() throws Exception {
        Bundle bundle;
        Bundle bundle2;
        try {
            if (!this.mTransitive) {
                for (int x = 0; x < this.mLocation.length; x++) {
                    if (AtlasBundleInfoManager.instance().isMbundle(this.mLocation[x])) {
                        if (Framework.getBundle(this.mLocation[x]) == null) {
                            RuntimeVariables.delegateClassLoader.installMbundle(this.mLocation[x]);
                        }
                    } else if (FileUtils.getUsableSpace(Environment.getDataDirectory()) < 5) {
                        throw new LowDiskException("no enough space");
                    } else if (this.mBundleSourceInputStream != null && this.mBundleSourceInputStream.length > x && this.mBundleSourceInputStream[x] != null) {
                        Bundle bundle3 = getInstalledBundle(this.mLocation[x]);
                        if (bundle3 == null) {
                            if (!BaselineInfoManager.instance().isDexPatched(this.mLocation[x]) && !BaselineInfoManager.instance().isUpdated(this.mLocation[x])) {
                                bundle3 = Framework.installNewBundle(this.mLocation[x], this.mBundleSourceInputStream[x]);
                            }
                        }
                        if (bundle3 != null) {
                            ((BundleImpl) bundle3).optDexFile();
                        }
                    } else if (this.mBundleSourceFile != null && this.mBundleSourceFile.length > x && this.mBundleSourceFile[x] != null) {
                        Bundle bundle4 = getInstalledBundle(this.mLocation[x]);
                        if (bundle4 == null) {
                            if (!BaselineInfoManager.instance().isDexPatched(this.mLocation[x]) && !BaselineInfoManager.instance().isUpdated(this.mLocation[x])) {
                                bundle4 = Framework.installNewBundle(this.mLocation[x], this.mBundleSourceFile[x]);
                            }
                        }
                        if (bundle4 != null) {
                            ((BundleImpl) bundle4).optDexFile();
                        }
                    } else if (getInstalledBundle(this.mLocation[x]) == null && AtlasBundleInfoManager.instance().isInternalBundle(this.mLocation[x]) && !BaselineInfoManager.instance().isDexPatched(this.mLocation[x]) && !BaselineInfoManager.instance().isUpdated(this.mLocation[x]) && (bundle2 = installBundleFromApk(this.mLocation[x])) != null) {
                        ((BundleImpl) bundle2).optDexFile();
                    }
                }
            } else {
                for (int x2 = 0; x2 < this.mLocation.length; x2++) {
                    List<String> bundlesForInstall = AtlasBundleInfoManager.instance().getBundleInfo(this.mLocation[x2]).getTotalDependency();
                    Log.e("BundleInstaller", this.mLocation[x2] + "-->" + bundlesForInstall.toString() + ", thread=" + Thread.currentThread());
                    for (String bundleName : bundlesForInstall) {
                        if (!AtlasBundleInfoManager.instance().isMbundle(bundleName)) {
                            Bundle bundle5 = getInstalledBundle(bundleName);
                            if (bundle5 == null) {
                                if (!BaselineInfoManager.instance().isDexPatched(bundleName) && !BaselineInfoManager.instance().isUpdated(bundleName)) {
                                    if (FileUtils.getUsableSpace(Environment.getDataDirectory()) < 5) {
                                        throw new LowDiskException("no enough space");
                                    } else if (AtlasBundleInfoManager.instance().isInternalBundle(bundleName) && (bundle = installBundleFromApk(bundleName)) != null) {
                                        ((BundleImpl) bundle).optDexFile();
                                    }
                                }
                            } else if (!(bundle5 == null || ((BundleImpl) bundle5).getArchive() == null || ((BundleImpl) bundle5).getArchive().isDexOpted())) {
                                ((BundleImpl) bundle5).optDexFile();
                            }
                        } else if (Framework.getBundle(bundleName) == null) {
                            RuntimeVariables.delegateClassLoader.installMbundle(bundleName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Map<String, Object> detail = new HashMap<>();
            detail.put("mLocation", this.mLocation);
            AtlasMonitor.getInstance().report(AtlasMonitor.INSTALL, detail, e);
            throw e;
        }
        return null;
    }

    public static synchronized void resolveInternalBundles() {
        synchronized (BundleInstaller.class) {
        }
    }

    private void findBundleSource(String location) throws IOException {
        ZipEntry entry;
        this.mTmpBundleSourceInputStream = null;
        this.mTmpBundleSourceFile = null;
        String dataDir = RuntimeVariables.androidApplication.getApplicationInfo().dataDir;
        String bundleFileName = String.format("lib%s.so", new Object[]{location.replace(".", "_")});
        File bundleFile = new File(String.format("%s/lib/%s", new Object[]{dataDir, bundleFileName}));
        if (!bundleFile.exists()) {
            bundleFile = new File(RuntimeVariables.androidApplication.getApplicationInfo().nativeLibraryDir, bundleFileName);
        }
        if (isBundleFileMatched(location, bundleFile, bundleFileName)) {
            this.mTmpBundleSourceFile = bundleFile;
            Log.e("BundleInstaller", "find valid bundle : " + bundleFile.getAbsolutePath());
            return;
        }
        try {
            this.mTmpBundleSourceInputStream = RuntimeVariables.originalResources.getAssets().open(bundleFileName);
        } catch (Throwable th) {
        }
        if (this.mTmpBundleSourceInputStream == null && ApkUtils.getApk() != null && (entry = ApkUtils.getApk().getEntry("lib/armeabi/" + bundleFileName)) != null) {
            this.mTmpBundleSourceInputStream = ApkUtils.getApk().getInputStream(entry);
        }
    }

    private boolean isBundleFileMatched(String location, File file, String bundleFileName) {
        if (!file.exists() || !AtlasBundleInfoManager.instance().isInternalBundle(location)) {
            return false;
        }
        BundleListing.BundleInfo info = AtlasBundleInfoManager.instance().getBundleInfo(location);
        if (info == null || info.getSize() <= 0 || info.getSize() == file.length()) {
            if (info == null && info.getSize() <= 0) {
                try {
                    ZipFile apkZip = ApkUtils.getApk();
                    ZipEntry entry = apkZip.getEntry("lib/armeabi/" + bundleFileName);
                    if (entry == null) {
                        entry = apkZip.getEntry("assets/" + bundleFileName);
                    }
                    if (entry == null || entry.getSize() == file.length()) {
                        return true;
                    }
                    return false;
                } catch (Throwable th) {
                }
            }
            return true;
        }
        Log.e("BundleInstaller", "wanted size: " + info.getSize() + "| realSize: " + file.length());
        return false;
    }

    private Bundle installBundleFromApk(String bundleName) throws Exception {
        findBundleSource(bundleName);
        if (this.mTmpBundleSourceFile != null) {
            return Framework.installNewBundle(bundleName, this.mTmpBundleSourceFile);
        }
        if (this.mTmpBundleSourceInputStream != null) {
            return Framework.installNewBundle(bundleName, this.mTmpBundleSourceInputStream);
        }
        IOException e = new IOException("can not find bundle source file");
        Map<String, Object> detail = new HashMap<>();
        detail.put("installBundleFromApk", bundleName);
        AtlasMonitor.getInstance().report(AtlasMonitor.CONTAINER_BUNDLE_SOURCE_MISMATCH, detail, e);
        throw e;
    }
}
