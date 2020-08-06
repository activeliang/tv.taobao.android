package com.taobao.atlas.update;

import android.content.Context;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.atlas.dexmerge.MergeCallback;
import com.taobao.atlas.update.exception.MergeException;
import com.taobao.atlas.update.model.UpdateInfo;
import com.taobao.atlas.update.util.PatchCleaner;
import com.taobao.atlas.update.util.PatchInstaller;
import com.taobao.atlas.update.util.PatchMerger;
import java.io.File;
import java.io.IOException;
import org.osgi.framework.BundleException;

public class AtlasUpdater {

    public interface IDexpatchMonitor {
        void install(boolean z, String str, long j, String str2);

        void merge(boolean z, String str, long j, String str2);
    }

    public static void update(UpdateInfo updateInfo, File patchFile) throws MergeException, BundleException {
        if (updateInfo != null && !updateInfo.dexPatch) {
            PatchMerger patchMerger = new PatchMerger(updateInfo, patchFile, new MergeCallback() {
                public void onMergeResult(boolean result, String bundleName) {
                    if (result) {
                        Log.d("[dexmerge]", "merge bundle " + bundleName + " success ");
                    } else {
                        Log.e("[dexmerge]", "merge bundle " + bundleName + " fail ");
                    }
                }
            });
            try {
                patchMerger.merge();
            } catch (IOException e) {
                e.printStackTrace();
            }
            new PatchInstaller(patchMerger.mergeOutputs, updateInfo).install();
            PatchCleaner.clearUpdatePath(updateInfo.workDir.getAbsolutePath());
        }
    }

    public static void dexpatchUpdate(Context context, UpdateInfo updateInfo, File patchFile, IDexpatchMonitor coldMonitor, boolean enableHot, IDexpatchMonitor hotMonitor) throws Exception {
        if (updateInfo != null && updateInfo.dexPatch) {
            String versionName = null;
            try {
                Context c = RuntimeVariables.androidApplication;
                versionName = c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionName;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(versionName) && versionName.equals(updateInfo.baseVersion)) {
                if (enableHot) {
                    DexPatchUpdater.installHotPatch(updateInfo.updateVersion, DexPatchUpdater.filterNeedHotPatchList(UpdateBundleDivider.dividePatchInfo(updateInfo.updateBundles, 2)), patchFile, hotMonitor);
                }
                updateInfo.updateBundles = DexPatchUpdater.filterNeedColdPatchList(UpdateBundleDivider.dividePatchInfo(updateInfo.updateBundles, 1));
                DexPatchUpdater.installColdPatch(updateInfo, patchFile, coldMonitor);
            }
        }
    }

    public static void dexpatchUpdate(Context context, UpdateInfo updateInfo, File patchFile, IDexpatchMonitor monitor) throws Exception {
        dexpatchUpdate(context, updateInfo, patchFile, monitor, false, (IDexpatchMonitor) null);
    }
}
