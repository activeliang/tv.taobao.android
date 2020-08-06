package com.yunos.tv.core.degrade;

import com.yunos.tv.core.BuildConfig;
import com.yunos.tv.core.common.DeviceJudge;
import com.yunos.tv.core.common.SharePreferences;
import com.zhiping.dev.android.logger.ZpLogger;

public class WidgetDegradeManager {
    private static final String TAG = WidgetDegradeManager.class.getSimpleName();
    private static WidgetDegradeManager ourInstance = null;
    private ConfigParser channelConfigParser = ChannelDegradeManager.getInstance().getChanelWidgetDegrade();
    private ConfigParser configParser;
    private int svrCfgValue = SharePreferences.getInt("low_memory_widget_degrade", -1).intValue();
    private int userCfgValue = SharePreferences.getInt("low_memory_widget_degrade_by_user", -1).intValue();

    public static WidgetDegradeManager getInstance() {
        if (ourInstance == null) {
            synchronized (WidgetDegradeManager.class) {
                if (ourInstance == null) {
                    ourInstance = new WidgetDegradeManager();
                }
            }
        }
        return ourInstance;
    }

    private WidgetDegradeManager() {
        int flag = 0;
        flag = this.svrCfgValue != -1 ? this.svrCfgValue : flag;
        if (this.userCfgValue != -1) {
            flag = this.userCfgValue;
            this.channelConfigParser = null;
        }
        if (this.svrCfgValue == -1 && this.userCfgValue == -1) {
            flag = BuildConfig.low_memory_widget_degrade.intValue();
        }
        this.configParser = new ConfigParser(flag);
        ZpLogger.i(TAG, toString());
    }

    public ConfigParser getConfigParser() {
        if (this.channelConfigParser != null) {
            return this.channelConfigParser;
        }
        return this.configParser;
    }

    public int getSvrCfgValue() {
        return this.svrCfgValue;
    }

    public int getUserCfgValue() {
        return this.userCfgValue;
    }

    public String toString() {
        return "WidgetDegradeManager{configParser=" + this.configParser + ", svrCfgValue=" + this.svrCfgValue + ", userCfgValue=" + this.userCfgValue + '}';
    }

    public boolean isRoundImageViewDegrade() {
        if (this.channelConfigParser != null) {
            return this.channelConfigParser.isRoundImgViewDegrade();
        }
        if (!this.configParser.isRoundImgViewDegrade() || !DeviceJudge.isMemTypeLow()) {
            return false;
        }
        return true;
    }

    public static class ConfigParser {
        public static final int FLAG_DEFAULT = 0;
        public static final int FLAG_RoundImageView_DEGRADE = 1;
        private int flag = 0;

        ConfigParser(int predefinedFlag) {
            this.flag = predefinedFlag;
        }

        public int getFlag() {
            return this.flag;
        }

        public boolean isDefault() {
            if (this.flag == 0) {
                return true;
            }
            return false;
        }

        public boolean isRoundImgViewDegrade() {
            if ((this.flag & 1) == 1) {
                return true;
            }
            return false;
        }

        public String toString() {
            return "ConfigParser{flag=" + Integer.toBinaryString(this.flag) + '}';
        }
    }
}
