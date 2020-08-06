package com.alibaba.analytics.core.device;

import android.content.Context;
import com.alibaba.analytics.utils.StringUtils;

public class HardConfig {
    private PersistentConfiguration commonPersistentConfigWR = null;
    private String configDir = null;
    private Context context = null;
    private String resourceIdentifier = null;

    public HardConfig(Context context2, String resourceIdentifier2, String configDir2) {
        this.context = context2;
        this.resourceIdentifier = resourceIdentifier2;
        this.configDir = configDir2;
    }

    public void release() {
        this.commonPersistentConfigWR = null;
    }

    public static PersistentConfiguration getDevicePersistentConfig(Context aContext) {
        if (aContext == null) {
            return null;
        }
        return new PersistentConfiguration(aContext, Constants.GLOBAL_PERSISTENT_CONFIG_DIR, "Alvin3", false, true);
    }

    public static PersistentConfiguration getNewDevicePersistentConfig(Context aContext) {
        if (aContext == null) {
            return null;
        }
        return new PersistentConfiguration(aContext, Constants.GLOBAL_PERSISTENT_CONFIG_DIR, "UTCommon", false, true);
    }

    public PersistentConfiguration getCommonPersistentConfig() {
        PersistentConfiguration pcTmp = null;
        if (this.commonPersistentConfigWR != null) {
            pcTmp = this.commonPersistentConfigWR;
        }
        if (pcTmp != null) {
            return pcTmp;
        }
        if (this.context == null || StringUtils.isEmpty(this.configDir)) {
            return null;
        }
        PersistentConfiguration commonPersistentConfig = new PersistentConfiguration(this.context, this.configDir, "UTCommon", false, false);
        this.commonPersistentConfigWR = commonPersistentConfig;
        return commonPersistentConfig;
    }
}
