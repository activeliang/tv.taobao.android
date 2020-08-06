package com.taobao.atlas.update.util;

import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.framework.Atlas;
import android.taobao.atlas.framework.BundleImpl;
import android.taobao.atlas.framework.Framework;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.atlas.util.ApkUtils;
import android.taobao.atlas.util.IOUtil;
import android.taobao.atlas.util.WrapperUtil;
import android.taobao.atlas.versionInfo.BaselineInfoManager;
import android.text.TextUtils;
import android.util.Pair;
import com.taobao.atlas.dexmerge.DexMergeClient;
import com.taobao.atlas.dexmerge.MergeCallback;
import com.taobao.atlas.dexmerge.MergeConstants;
import com.taobao.atlas.dexmerge.MergeObject;
import com.taobao.atlas.update.exception.MergeException;
import com.taobao.atlas.update.model.UpdateInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PatchMerger {
    private static int BUFFEREDSIZE = 1024;
    private static String MAIN_DEX = MergeConstants.MAIN_DEX;
    private static boolean supportMerge = true;
    private ZipFile apkZip;
    private boolean lowDisk = false;
    private MergeCallback mergeCallback;
    public Map<String, Pair<String, UpdateInfo.Item>> mergeOutputs = new HashMap();
    private File patchFile;
    private UpdateInfo updateInfo;

    public PatchMerger(UpdateInfo updateInfo2, File patchFile2, MergeCallback mergeCallback2) throws MergeException {
        this.updateInfo = updateInfo2;
        this.patchFile = patchFile2;
        this.mergeCallback = mergeCallback2;
        if (updateInfo2.lowDisk) {
            this.lowDisk = true;
        } else {
            this.lowDisk = false;
        }
        try {
            this.apkZip = new ZipFile(RuntimeVariables.androidApplication.getApplicationInfo().sourceDir);
        } catch (IOException e) {
            throw new MergeException((Throwable) e);
        }
    }

    public void merge() throws MergeException, IOException {
        try {
            File outputDirectory = this.updateInfo.workDir;
            File oringnalDir = new File(outputDirectory.getParentFile(), "orignal_" + this.updateInfo.baseVersion);
            if (!outputDirectory.exists()) {
                outputDirectory.mkdirs();
            }
            if (!oringnalDir.exists()) {
                oringnalDir.mkdirs();
            }
            ZipFile zipFile = new ZipFile(this.patchFile);
            Pair<File, String>[] updateBundles = new Pair[this.updateInfo.updateBundles.size()];
            ArrayList arrayList = new ArrayList();
            for (int x = 0; x < this.updateInfo.updateBundles.size(); x++) {
                UpdateInfo.Item item = this.updateInfo.updateBundles.get(x);
                if (!item.inherit) {
                    String bundleName = item.name;
                    String entryName = String.format("%s%s", new Object[]{"lib", bundleName.replace(".", "_")}) + MergeConstants.SO_SUFFIX;
                    ZipEntry entry = zipFile.getEntry(entryName);
                    if (zipFile.getEntry(entryName) != null && !supportMerge(bundleName)) {
                        File file = new File(outputDirectory, entryName);
                        IOUtil.copyStream(zipFile.getInputStream(entry), new FileOutputStream(file));
                        this.mergeOutputs.put(bundleName, new Pair(file.getAbsolutePath(), item));
                    } else if (item.reset) {
                        this.mergeOutputs.put(bundleName, new Pair("", item));
                    } else {
                        File originalBundle = findOriginalBundleFile(bundleName, oringnalDir.getAbsolutePath(), item);
                        if (originalBundle != null && originalBundle.exists()) {
                            updateBundles[x] = new Pair<>(originalBundle, bundleName);
                            File file2 = new File(outputDirectory, "lib" + bundleName.replace(".", "_") + MergeConstants.SO_SUFFIX);
                            if (file2.exists()) {
                                file2.delete();
                            }
                            arrayList.add(new MergeObject(((File) updateBundles[x].first).getAbsolutePath(), (String) updateBundles[x].second, file2.getAbsolutePath()));
                            this.mergeOutputs.put(bundleName, new Pair(file2.getAbsolutePath(), item));
                        }
                    }
                }
            }
            if (arrayList.size() > 0) {
                DexMergeClient dexMergeClient = getMergeClient();
                boolean mergeFinish = dexMergeClient.dexMerge(this.patchFile.getAbsolutePath(), arrayList, true);
                dexMergeClient.unPrepare();
                if (!this.updateInfo.dexPatch && !mergeFinish) {
                    throw new MergeException("merge failed!");
                }
            }
            if (this.apkZip != null) {
                try {
                    this.apkZip.close();
                } catch (Throwable th) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MergeException("merge failed!");
        } catch (Throwable th2) {
            if (this.apkZip != null) {
                try {
                    this.apkZip.close();
                } catch (Throwable th3) {
                }
            }
            throw th2;
        }
    }

    public File findOriginalBundleFile(String bundleName, String bundleDirIfNeedCreate, UpdateInfo.Item item) throws IOException {
        if ("com.taobao.dynamic.test".equals(bundleName)) {
            return getOriginalBundleFromApk(bundleName, bundleDirIfNeedCreate);
        }
        if (bundleName.equals(MAIN_DEX)) {
            if (!this.updateInfo.dexPatch) {
                return new File(RuntimeVariables.androidApplication.getApplicationInfo().sourceDir);
            }
            String maindexVersion = BaselineInfoManager.instance().getBaseBundleVersion(MergeConstants.MAIN_DEX);
            if (WrapperUtil.getPackageInfo(RuntimeVariables.androidApplication).versionName.equals(this.updateInfo.baseVersion)) {
                if (!TextUtils.isEmpty(maindexVersion)) {
                    File old = Framework.getInstalledBundle(MergeConstants.MAIN_DEX, maindexVersion);
                    if (old.exists()) {
                        return old;
                    }
                } else if (this.updateInfo.baseVersion.equals(RuntimeVariables.sInstalledVersionName)) {
                    return new File(RuntimeVariables.androidApplication.getApplicationInfo().sourceDir);
                }
            }
            throw new IllegalStateException("src version can not be null");
        } else if (TextUtils.isEmpty(item.srcUnitTag)) {
            throw new IllegalStateException("src version can not be null");
        } else {
            File oldBundle = null;
            BundleImpl impl = (BundleImpl) Atlas.getInstance().getBundle(bundleName);
            if (impl == null || BaselineInfoManager.instance().isDexPatched(bundleName)) {
                oldBundle = Framework.getInstalledBundle(bundleName, item.srcUnitTag);
            } else if (!impl.getArchive().getCurrentRevision().getRevisionDir().getAbsolutePath().contains("dexpatch/") && AtlasBundleInfoManager.instance().getBundleInfo(bundleName).getUnique_tag().equals(item.srcUnitTag)) {
                oldBundle = impl.getArchive().getArchiveFile();
            }
            if (oldBundle == null && AtlasBundleInfoManager.instance().getBundleInfo(bundleName).getUnique_tag().equals(item.srcUnitTag) && !BaselineInfoManager.instance().isUpdated(bundleName)) {
                oldBundle = getOriginalBundleFromApk(bundleName, bundleDirIfNeedCreate);
            }
            if (oldBundle != null || !AtlasBundleInfoManager.instance().isInternalBundle(bundleName)) {
                return oldBundle;
            }
            throw new IOException("can not find valid src bundle of " + bundleName);
        }
    }

    private File getOriginalBundleFromApk(String bundleName, String bundleDirIfNeedCreate) throws IOException {
        String oldBundleFileName = String.format("lib%s.so", new Object[]{bundleName.replace(".", "_")});
        File oldBundle = new File(new File(RuntimeVariables.androidApplication.getFilesDir().getParentFile(), "lib"), oldBundleFileName);
        if (!oldBundle.exists()) {
            oldBundle = new File(RuntimeVariables.androidApplication.getApplicationInfo().nativeLibraryDir, oldBundleFileName);
        }
        if (oldBundle.exists()) {
            return oldBundle;
        }
        InputStream oldBundleStream = null;
        try {
            oldBundleStream = RuntimeVariables.originalResources.getAssets().open(oldBundleFileName);
        } catch (Throwable th) {
        }
        if (oldBundleStream == null) {
            if (this.apkZip == null) {
                this.apkZip = new ZipFile(RuntimeVariables.androidApplication.getApplicationInfo().sourceDir);
            }
            String entryName = String.format("lib/armeabi/%s", new Object[]{oldBundleFileName});
            if (this.apkZip.getEntry(entryName) != null) {
                oldBundleStream = this.apkZip.getInputStream(this.apkZip.getEntry(entryName));
            }
        }
        if (oldBundleStream != null) {
            oldBundle = new File(bundleDirIfNeedCreate, oldBundleFileName);
            ApkUtils.copyInputStreamToFile(oldBundleStream, oldBundle);
        }
        return oldBundle;
    }

    private DexMergeClient getMergeClient() {
        DexMergeClient mergeClient = new DexMergeClient(this.mergeCallback);
        if (mergeClient.prepare()) {
            return mergeClient;
        }
        throw new RuntimeException("prepare client error");
    }

    private boolean supportMerge(String bundleName) {
        return (this.lowDisk || supportMerge) && bundleName.equals(MAIN_DEX);
    }
}
