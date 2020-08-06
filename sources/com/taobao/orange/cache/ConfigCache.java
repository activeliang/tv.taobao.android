package com.taobao.orange.cache;

import android.text.TextUtils;
import com.taobao.orange.ConfigCenter;
import com.taobao.orange.OConstant;
import com.taobao.orange.model.ConfigDO;
import com.taobao.orange.model.NameSpaceDO;
import com.taobao.orange.util.FileUtil;
import com.taobao.orange.util.OLog;
import com.taobao.orange.util.OrangeMonitor;
import com.taobao.orange.util.OrangeUtils;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigCache {
    private static final String TAG = "ConfigCache";
    private Map<String, ConfigDO> mConfigMap = new ConcurrentHashMap();

    public Map<String, ConfigDO> getConfigMap() {
        return this.mConfigMap;
    }

    public Set<NameSpaceDO> load(Set<NameSpaceDO> namespaceDOList) {
        Set<NameSpaceDO> notMatchNamespaces = new HashSet<>();
        if (namespaceDOList == null || namespaceDOList.isEmpty()) {
            OLog.w(TAG, "load config cache empty", new Object[0]);
            return null;
        }
        for (NameSpaceDO namespaceDO : namespaceDOList) {
            ConfigDO configDO = restoreConfig(namespaceDO);
            if (configDO != null) {
                this.mConfigMap.put(configDO.name, configDO);
                ConfigCenter.getInstance().removeFail(configDO.name);
                ConfigCenter.getInstance().notifyListeners(configDO.name, configDO.getCurVersion(), true);
                if (OrangeUtils.parseLong(namespaceDO.version) > OrangeUtils.parseLong(configDO.version)) {
                    notMatchNamespaces.add(namespaceDO);
                }
            } else if (NameSpaceDO.LEVEL_HIGH.equals(namespaceDO.loadLevel)) {
                notMatchNamespaces.add(namespaceDO);
            }
        }
        return notMatchNamespaces;
    }

    private ConfigDO restoreConfig(NameSpaceDO namespaceDO) {
        ConfigDO configDO = null;
        Object obj = FileUtil.restoreObject(namespaceDO.name);
        if (obj != null) {
            try {
                configDO = (ConfigDO) obj;
            } catch (Throwable t) {
                OLog.e(TAG, "restoreConfig", t, new Object[0]);
                OrangeMonitor.commitCount(OConstant.MONITOR_PRIVATE_MODULE, OConstant.POINT_EXCEPTION, "restoreConfig: " + t.getMessage(), 1.0d);
            }
        }
        if (configDO == null || !configDO.checkValid()) {
            return null;
        }
        if (!OLog.isPrintLog(1)) {
            return configDO;
        }
        if (configDO.candidate == null) {
            OLog.d(TAG, "restoreConfig", configDO);
            return configDO;
        }
        OLog.d(TAG, "restoreAbConfig", configDO);
        return configDO;
    }

    public void cache(ConfigDO configDO) {
        this.mConfigMap.put(configDO.name, configDO);
        ConfigCenter.getInstance().notifyListeners(configDO.name, configDO.getCurVersion(), false);
        FileUtil.persistObject(configDO, configDO.name);
    }

    public void remove(String namespace) {
        if (!TextUtils.isEmpty(namespace)) {
            FileUtil.deleteConfigFile(namespace);
        }
    }

    public Map<String, String> getConfigs(String namespace) {
        ConfigDO config = this.mConfigMap.get(namespace);
        if (config != null) {
            return config.content;
        }
        return null;
    }
}
