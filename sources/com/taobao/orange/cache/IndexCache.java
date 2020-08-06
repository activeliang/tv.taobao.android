package com.taobao.orange.cache;

import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.OConstant;
import com.taobao.orange.model.IndexDO;
import com.taobao.orange.model.NameSpaceDO;
import com.taobao.orange.util.FileUtil;
import com.taobao.orange.util.OLog;
import com.taobao.orange.util.OrangeMonitor;
import com.taobao.orange.util.OrangeUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IndexCache {
    public static final String INDEX_STORE_NAME = "orange.index";
    private static final String TAG = "IndexCache";
    private volatile IndexDO mIndex = new IndexDO();

    public void load() {
        IndexDO indexDO = null;
        Object obj = FileUtil.restoreObject(INDEX_STORE_NAME);
        if (obj != null) {
            try {
                indexDO = (IndexDO) obj;
            } catch (Throwable t) {
                OLog.e(TAG, "load", t, new Object[0]);
                OrangeMonitor.commitCount(OConstant.MONITOR_PRIVATE_MODULE, OConstant.POINT_EXCEPTION, "loadIndex: " + t.getMessage(), 1.0d);
            }
        }
        if (indexDO == null || !indexDO.checkValid()) {
            FileUtil.clearCacheFile();
        } else {
            OLog.i(TAG, "load", "indexDO", OrangeUtils.formatIndexDO(indexDO));
            this.mIndex = indexDO;
        }
        updateOrangeHeader();
    }

    public List<String> cache(IndexDO indexDO) {
        Map<String, NameSpaceDO> oldNameSpaceDOMap = formatMergedNamespaceMap(this.mIndex.mergedNamespaces);
        Map<String, NameSpaceDO> newNameSpaceDOMap = formatMergedNamespaceMap(indexDO.mergedNamespaces);
        List<String> removeNamespaces = new ArrayList<>();
        removeNamespaces.addAll(oldNameSpaceDOMap.keySet());
        removeNamespaces.removeAll(newNameSpaceDOMap.keySet());
        for (Map.Entry<String, NameSpaceDO> entry : newNameSpaceDOMap.entrySet()) {
            NameSpaceDO oldDO = oldNameSpaceDOMap.get(entry.getKey());
            NameSpaceDO newDO = entry.getValue();
            if (oldDO == null) {
                newDO.hasChanged = true;
            } else {
                boolean hasChanged = !newDO.equals(oldDO);
                if (hasChanged && OLog.isPrintLog(2)) {
                    OLog.i(TAG, "cache", "compare change NameSpaceDO", OrangeUtils.formatNamespaceDO(newDO));
                }
                newDO.hasChanged = hasChanged;
            }
        }
        this.mIndex = indexDO;
        updateOrangeHeader();
        FileUtil.persistObject(this.mIndex, INDEX_STORE_NAME);
        return removeNamespaces;
    }

    private Map<String, NameSpaceDO> formatMergedNamespaceMap(List<NameSpaceDO> nameSpaceDOList) {
        Map<String, NameSpaceDO> nameSpaceDOMap = new HashMap<>();
        if (nameSpaceDOList != null && !nameSpaceDOList.isEmpty()) {
            for (NameSpaceDO nameSpaceDO : nameSpaceDOList) {
                nameSpaceDOMap.put(nameSpaceDO.name, nameSpaceDO);
            }
        }
        return nameSpaceDOMap;
    }

    private void updateOrangeHeader() {
        StringBuilder builder = new StringBuilder().append("appKey").append("=").append(GlobalOrange.appKey).append("&").append("appVersion").append("=").append(GlobalOrange.appVersion).append("&").append(OConstant.KEY_CLIENTAPPINDEXVERSION).append("=").append(getAppIndexVersion()).append("&").append(OConstant.KEY_CLIENTVERSIONINDEXVERSION).append("=").append(getVersionIndexVersion());
        OLog.i(TAG, "updateOrangeHeader", "reqOrangeHeader", builder.toString());
        GlobalOrange.reqOrangeHeader = builder.toString();
    }

    public IndexDO getIndex() {
        return this.mIndex;
    }

    public Set<NameSpaceDO> getAllNameSpaces() {
        Set<NameSpaceDO> results = new HashSet<>();
        results.addAll(this.mIndex.mergedNamespaces);
        return results;
    }

    public Set<NameSpaceDO> getUpdateNameSpaces(Set<String> localNamespaces) {
        Set<NameSpaceDO> results = new HashSet<>();
        for (NameSpaceDO spaceDO : this.mIndex.mergedNamespaces) {
            if (spaceDO.hasChanged) {
                if (NameSpaceDO.LEVEL_HIGH.equals(spaceDO.loadLevel)) {
                    results.add(spaceDO);
                } else if (localNamespaces != null && localNamespaces.contains(spaceDO.name)) {
                    results.add(spaceDO);
                }
            }
        }
        return results;
    }

    public NameSpaceDO getNameSpace(String namespace) {
        if (TextUtils.isEmpty(namespace)) {
            return null;
        }
        for (NameSpaceDO namespaceDO : this.mIndex.mergedNamespaces) {
            if (namespace.equals(namespaceDO.name)) {
                return namespaceDO;
            }
        }
        return null;
    }

    public String getCdnUrl() {
        if (TextUtils.isEmpty(this.mIndex.cdn)) {
            return null;
        }
        return GlobalOrange.schema + HttpConstant.SCHEME_SPLIT + this.mIndex.cdn;
    }

    public String getAppIndexVersion() {
        return this.mIndex.appIndexVersion == null ? "0" : this.mIndex.appIndexVersion;
    }

    public String getVersionIndexVersion() {
        return this.mIndex.versionIndexVersion == null ? "0" : this.mIndex.versionIndexVersion;
    }
}
