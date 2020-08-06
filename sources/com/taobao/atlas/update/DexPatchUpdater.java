package com.taobao.atlas.update;

import android.taobao.atlas.patch.AtlasHotPatchManager;
import android.taobao.atlas.util.IOUtil;
import android.taobao.atlas.versionInfo.BaselineInfoManager;
import android.util.Pair;
import com.taobao.atlas.dexmerge.MergeCallback;
import com.taobao.atlas.dexmerge.MergeConstants;
import com.taobao.atlas.update.AtlasUpdater;
import com.taobao.atlas.update.model.UpdateInfo;
import com.taobao.atlas.update.util.PatchCleaner;
import com.taobao.atlas.update.util.PatchInstaller;
import com.taobao.atlas.update.util.PatchMerger;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipFile;

public class DexPatchUpdater {
    public static void installHotPatch(String updateVersion, List<UpdateInfo.Item> updateList, File patchFile, AtlasUpdater.IDexpatchMonitor monitor) throws Exception {
        String entryName;
        if (updateList != null && !updateList.isEmpty()) {
            ZipFile patchZip = null;
            try {
                ZipFile patchZip2 = new ZipFile(patchFile);
                try {
                    HashMap hashMap = new HashMap(updateList.size());
                    for (UpdateInfo.Item bundle : updateList) {
                        if (MergeConstants.MAIN_DEX.equals(bundle.name)) {
                            entryName = "hot.dex";
                        } else {
                            entryName = String.format("%s%s/hot.dex", new Object[]{"lib", bundle.name.replace(".", "_")});
                        }
                        HashMap hashMap2 = hashMap;
                        hashMap2.put(bundle.name, new Pair(Long.valueOf(bundle.dexpatchVersion), patchZip2.getInputStream(patchZip2.getEntry(entryName))));
                    }
                    AtlasHotPatchManager.getInstance().installHotFixPatch(updateVersion, hashMap);
                    IOUtil.quietClose(patchZip2);
                    if (monitor != null) {
                        Map<String, Long> hotPatchInstall = AtlasHotPatchManager.getInstance().getAllInstallPatch();
                        if (hotPatchInstall == null) {
                            hotPatchInstall = new HashMap<>(1);
                        }
                        for (UpdateInfo.Item item : updateList) {
                            monitor.install(hotPatchInstall.containsKey(item.name), item.name, item.dexpatchVersion, "");
                        }
                    }
                } catch (IOException e) {
                    e = e;
                    patchZip = patchZip2;
                    try {
                        e.printStackTrace();
                        throw e;
                    } catch (Throwable th) {
                        th = th;
                        IOUtil.quietClose(patchZip);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    patchZip = patchZip2;
                    IOUtil.quietClose(patchZip);
                    throw th;
                }
            } catch (IOException e2) {
                e = e2;
                e.printStackTrace();
                throw e;
            }
        }
    }

    public static void installColdPatch(UpdateInfo updateInfo, File patchFile, AtlasUpdater.IDexpatchMonitor monitor) throws Exception {
        if (updateInfo.updateBundles != null && !updateInfo.updateBundles.isEmpty()) {
            try {
                PatchMerger patchMerger = new PatchMerger(updateInfo, patchFile, (MergeCallback) null);
                try {
                    patchMerger.merge();
                    List<UpdateInfo.Item> result = new ArrayList<>();
                    for (int i = 0; i < updateInfo.updateBundles.size(); i++) {
                        UpdateInfo.Item item = updateInfo.updateBundles.get(i);
                        if (patchMerger.mergeOutputs.containsKey(item.name)) {
                            boolean succeed = new File((String) patchMerger.mergeOutputs.get(item.name).first).exists();
                            if (item.reset) {
                                succeed = true;
                            }
                            if (succeed) {
                                result.add(item);
                            }
                            if (monitor != null) {
                                monitor.merge(succeed, item.name, item.dexpatchVersion, "");
                            }
                        }
                    }
                    updateInfo.updateBundles = result;
                    new PatchInstaller(patchMerger.mergeOutputs, updateInfo).install();
                    PatchCleaner.clearUpdatePath(updateInfo.workDir.getAbsolutePath());
                    ConcurrentHashMap<String, Long> installList = BaselineInfoManager.instance().getDexPatchBundles();
                    if (installList != null && installList.size() != 0 && monitor != null) {
                        for (UpdateInfo.Item item2 : updateInfo.updateBundles) {
                            boolean succeed2 = installList.containsKey(item2.name) && item2.dexpatchVersion == installList.get(item2.name).longValue();
                            if (item2.reset) {
                                monitor.install(true, item2.name, item2.dexpatchVersion, "");
                            } else {
                                monitor.install(succeed2, item2.name, item2.dexpatchVersion, "");
                            }
                        }
                    }
                } catch (Exception e) {
                    e = e;
                    PatchMerger patchMerger2 = patchMerger;
                    try {
                        e.printStackTrace();
                        throw e;
                    } catch (Throwable th) {
                        th = th;
                        PatchCleaner.clearUpdatePath(updateInfo.workDir.getAbsolutePath());
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    PatchMerger patchMerger3 = patchMerger;
                    PatchCleaner.clearUpdatePath(updateInfo.workDir.getAbsolutePath());
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                throw e;
            }
        }
    }

    public static List<UpdateInfo.Item> filterNeedHotPatchList(List<UpdateInfo.Item> hotPatchList) {
        Long version;
        List<UpdateInfo.Item> resultList = new ArrayList<>(hotPatchList.size());
        Map<String, Long> installMap = AtlasHotPatchManager.getInstance().getAllInstallPatch();
        if (installMap == null) {
            installMap = new HashMap<>(1);
        }
        for (UpdateInfo.Item item : hotPatchList) {
            if (item.patchType == 2 && ((version = installMap.get(item.name)) == null || item.dexpatchVersion > version.longValue())) {
                resultList.add(UpdateInfo.Item.makeCopy(item));
            }
        }
        return resultList;
    }

    public static List<UpdateInfo.Item> filterNeedColdPatchList(List<UpdateInfo.Item> coldPatchList) {
        List<UpdateInfo.Item> resultList = new ArrayList<>(coldPatchList.size());
        Map<String, Long> installMap = BaselineInfoManager.instance().getDexPatchBundles();
        if (installMap == null) {
            installMap = new HashMap<>(1);
        }
        for (UpdateInfo.Item item : coldPatchList) {
            if (item.patchType == 1) {
                Long version = installMap.get(item.name);
                if (item.reset) {
                    if (!(version == null || version.longValue() == -1)) {
                        resultList.add(UpdateInfo.Item.makeCopy(item));
                    }
                } else if (version == null || item.dexpatchVersion > version.longValue()) {
                    resultList.add(UpdateInfo.Item.makeCopy(item));
                }
            }
        }
        return resultList;
    }
}
