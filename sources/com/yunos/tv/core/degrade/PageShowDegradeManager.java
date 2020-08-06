package com.yunos.tv.core.degrade;

import com.yunos.tv.core.BuildConfig;
import com.yunos.tv.core.common.DeviceJudge;
import com.yunos.tv.core.common.SharePreferences;
import com.zhiping.dev.android.logger.ZpLogger;

public class PageShowDegradeManager {
    private static final String TAG = PageShowDegradeManager.class.getSimpleName();
    private static PageShowDegradeManager ourInstance = null;
    private ConfigParser channelConfigParser = ChannelDegradeManager.getInstance().getChanelPageShowDegrade();
    private ConfigParser configParser;
    private int svrCfgValue = SharePreferences.getInt("low_memory_page_show_degrade", -1).intValue();
    private int userCfgValue = SharePreferences.getInt("low_memory_page_show_degrade_by_user", -1).intValue();

    public static PageShowDegradeManager getInstance() {
        if (ourInstance == null) {
            synchronized (PageShowDegradeManager.class) {
                if (ourInstance == null) {
                    ourInstance = new PageShowDegradeManager();
                }
            }
        }
        return ourInstance;
    }

    private PageShowDegradeManager() {
        int flag = 0;
        flag = this.svrCfgValue != -1 ? this.svrCfgValue : flag;
        if (this.userCfgValue != -1) {
            flag = this.userCfgValue;
            this.channelConfigParser = null;
        }
        if (this.svrCfgValue == -1 && this.userCfgValue == -1) {
            flag = BuildConfig.low_memory_page_show_degrade.intValue();
        }
        this.configParser = new ConfigParser(flag);
        ZpLogger.i(TAG, toString());
    }

    public boolean shouldDegradeGoodDetail() {
        if (this.channelConfigParser != null) {
            return this.channelConfigParser.isGoodDetailDegrade();
        }
        if (!this.configParser.isGoodDetailDegrade() || !DeviceJudge.isMemTypeLow()) {
            return false;
        }
        return true;
    }

    public boolean shouldDegradeBigImgBg() {
        if (this.channelConfigParser != null) {
            return this.channelConfigParser.isBigImgBgDegrade();
        }
        if (!this.configParser.isBigImgBgDegrade() || !DeviceJudge.isMemTypeLow()) {
            return false;
        }
        return true;
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
        return "PageShowDegradeManager{configParser=" + this.configParser + ", svrCfgValue=" + this.svrCfgValue + ", userCfgValue=" + this.userCfgValue + '}';
    }

    public static class ConfigParser {
        public static final int FLAG_BIG_IMG_BG_DEGRADE = 2;
        public static final int FLAG_DEFAULT = 0;
        public static final int FLAG_GOOD_DETAIL_DEGRADE = 1;
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

        public boolean isGoodDetailDegrade() {
            if ((this.flag & 1) == 1) {
                return true;
            }
            return false;
        }

        public boolean isBigImgBgDegrade() {
            if ((this.flag & 2) == 2) {
                return true;
            }
            return false;
        }

        public String toString() {
            return "ConfigParser{flag=" + Integer.toBinaryString(this.flag) + '}';
        }
    }
}
