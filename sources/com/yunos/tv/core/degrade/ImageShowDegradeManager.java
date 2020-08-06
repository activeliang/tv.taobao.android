package com.yunos.tv.core.degrade;

import com.yunos.tv.core.BuildConfig;
import com.yunos.tv.core.common.DeviceJudge;
import com.yunos.tv.core.common.SharePreferences;
import com.zhiping.dev.android.logger.ZpLogger;

public class ImageShowDegradeManager {
    private static final String TAG = ImageShowDegradeManager.class.getSimpleName();
    private static ImageShowDegradeManager ourInstance;
    private ConfigParser channelConfigParser = ChannelDegradeManager.getInstance().getChanelImageShowDegrade();
    private ConfigParser configParser;
    private int svrCfgValue = SharePreferences.getInt("low_memory_img_show_degrade", -1).intValue();
    private int userCfgValue = SharePreferences.getInt("low_memory_img_show_degrade_by_user", -1).intValue();

    public static ImageShowDegradeManager getInstance() {
        if (ourInstance == null) {
            synchronized (ImageShowDegradeManager.class) {
                if (ourInstance == null) {
                    ourInstance = new ImageShowDegradeManager();
                }
            }
        }
        return ourInstance;
    }

    private ImageShowDegradeManager() {
        int flag = 0;
        flag = this.svrCfgValue != -1 ? this.svrCfgValue : flag;
        if (this.userCfgValue != -1) {
            flag = this.userCfgValue;
            this.channelConfigParser = null;
        }
        if (this.svrCfgValue == -1 && this.userCfgValue == -1) {
            flag = BuildConfig.low_memory_img_show_degrade.intValue();
        }
        this.configParser = new ConfigParser(flag);
        ZpLogger.i(TAG, toString());
    }

    public boolean isImageDegrade() {
        if (this.channelConfigParser != null) {
            return this.channelConfigParser.isImgDegrade();
        }
        if (!this.configParser.isImgDegrade() || !DeviceJudge.isMemTypeLow()) {
            return false;
        }
        return true;
    }

    public boolean isImageLoaderDegrade() {
        if (this.channelConfigParser != null) {
            return this.channelConfigParser.isImgLoaderDegrade();
        }
        if (!this.configParser.isImgLoaderDegrade() || !DeviceJudge.isMemTypeLow()) {
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
        return "ImageShowDegradeManager{configParser=" + this.configParser + ", svrCfgValue=" + this.svrCfgValue + ", userCfgValue=" + this.userCfgValue + '}';
    }

    public static class ConfigParser {
        public static final int FLAG_DEFAULT = 0;
        public static final int FLAG_IMG_DEGRADE = 1;
        public static final int FLAG_IMG_LOADER_DEGRADE = 2;
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

        public boolean isImgDegrade() {
            if ((this.flag & 1) == 1) {
                return true;
            }
            return false;
        }

        public boolean isImgLoaderDegrade() {
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
