package android.taobao.windvane.config;

import android.taobao.windvane.config.WVConfigManager;

public class WVUpdatingInfo {
    private WVConfigManager.WVConfigUpdateFromType fromType;
    private String version;

    WVUpdatingInfo(WVConfigManager.WVConfigUpdateFromType fromType2, String version2) {
        this.fromType = fromType2;
        this.version = version2;
    }

    public WVConfigManager.WVConfigUpdateFromType getFromType() {
        return this.fromType;
    }

    public String getVersion() {
        return this.version;
    }
}
