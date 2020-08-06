package com.taobao.atlas.update.util;

import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.framework.Framework;
import android.util.Pair;
import com.taobao.atlas.update.model.UpdateInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.osgi.framework.BundleException;

public class PatchInstaller {
    private Map<String, Pair<String, UpdateInfo.Item>> mergeOutputs;
    private UpdateInfo updateInfo;

    public PatchInstaller(Map<String, Pair<String, UpdateInfo.Item>> mergeOutputs2, UpdateInfo updateInfo2) {
        this.mergeOutputs = mergeOutputs2;
        this.updateInfo = updateInfo2;
    }

    public void install() throws BundleException {
        if (this.mergeOutputs.isEmpty()) {
            throw new BundleException("merge bundles is empty");
        }
        List<String> bundleNameList = new ArrayList<>();
        List<File> bundleFilePathList = new ArrayList<>();
        ArrayList arrayList = new ArrayList();
        List<Long> dexPatchVersions = new ArrayList<>();
        for (Map.Entry entry : this.mergeOutputs.entrySet()) {
            bundleNameList.add((String) entry.getKey());
            Pair<String, UpdateInfo.Item> bundlePair = (Pair) entry.getValue();
            if (!((UpdateInfo.Item) bundlePair.second).reset) {
                File bundleFile = new File((String) bundlePair.first);
                bundleFilePathList.add(bundleFile);
                if (!bundleFile.exists()) {
                    throw new BundleException("bundle input is wrong : " + bundleFilePathList);
                } else if (!this.updateInfo.dexPatch) {
                    arrayList.add(((UpdateInfo.Item) bundlePair.second).unitTag);
                } else {
                    dexPatchVersions.add(Long.valueOf(((UpdateInfo.Item) bundlePair.second).dexpatchVersion));
                }
            } else if (!this.updateInfo.dexPatch) {
                arrayList.add("-1");
                bundleFilePathList.add(new File("reset"));
            } else {
                dexPatchVersions.add(-1L);
                bundleFilePathList.add(new File("reset"));
            }
        }
        for (UpdateInfo.Item bundle : this.updateInfo.updateBundles) {
            if (!bundleNameList.contains(bundle.name) && AtlasBundleInfoManager.instance().isInternalBundle(bundle.name)) {
                if (bundle.inherit) {
                    bundleNameList.add(bundle.name);
                    bundleFilePathList.add(new File("inherit"));
                    if (!this.updateInfo.dexPatch) {
                        arrayList.add(bundle.unitTag);
                    } else {
                        dexPatchVersions.add(Long.valueOf(bundle.dexpatchVersion));
                    }
                } else {
                    throw new BundleException("bundle  " + bundle.name + " is error");
                }
            }
        }
        String[] bundleNameArray = (String[]) bundleNameList.toArray(new String[bundleNameList.size()]);
        File[] bundleFilePathArray = (File[]) bundleFilePathList.toArray(new File[bundleFilePathList.size()]);
        String[] updateVersionArray = (String[]) arrayList.toArray(new String[arrayList.size()]);
        long[] dexPatchVersionArray = new long[dexPatchVersions.size()];
        for (int x = 0; x < dexPatchVersions.size(); x++) {
            dexPatchVersionArray[x] = dexPatchVersions.get(x).longValue();
        }
        Framework.update(!this.updateInfo.dexPatch, bundleNameArray, bundleFilePathArray, updateVersionArray, dexPatchVersionArray, this.updateInfo.updateVersion, this.updateInfo.lowDisk);
    }
}
