package com.yunos.tv.core.degrade;

import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.degrade.ImageShowDegradeManager;
import com.yunos.tv.core.degrade.PageShowDegradeManager;
import com.yunos.tv.core.degrade.PageStackDegradeManager;
import com.yunos.tv.core.degrade.WidgetDegradeManager;
import com.yunos.tvtaobao.payment.BuildConfig;
import com.zhiping.dev.android.logger.ZpLogger;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChannelDegradeManager {
    private static final String TAG = ChannelDegradeManager.class.getSimpleName();
    private static ChannelDegradeManager instance;
    private JSONArray channelDegradeConfigs;

    private ChannelDegradeManager() {
        String jsonStr = SharePreferences.getString("channelDegradeConfigs", (String) null);
        if (jsonStr != null) {
            try {
                this.channelDegradeConfigs = new JSONArray(jsonStr);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static ChannelDegradeManager getInstance() {
        if (instance == null) {
            synchronized (ChannelDegradeManager.class) {
                if (instance == null) {
                    instance = new ChannelDegradeManager();
                }
            }
        }
        return instance;
    }

    public PageShowDegradeManager.ConfigParser getChanelPageShowDegrade() {
        try {
            ZpLogger.i(TAG, ".getChanelPageShowDegrade");
            if (this.channelDegradeConfigs != null) {
                int i = 0;
                while (true) {
                    if (i >= this.channelDegradeConfigs.length()) {
                        break;
                    }
                    JSONObject item = this.channelDegradeConfigs.optJSONObject(i);
                    if (item == null || !BuildConfig.CHANNELID.equals(item.optString("channelId"))) {
                        i++;
                    } else {
                        int flag = item.optInt("page_show_degrade", -1);
                        if (flag != -1) {
                            ZpLogger.i(TAG, ".getChanelPageShowDegrade rtn :" + flag);
                            return new PageShowDegradeManager.ConfigParser(flag);
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        ZpLogger.i(TAG, ".getChanelPageShowDegrade rtn null");
        return null;
    }

    public ImageShowDegradeManager.ConfigParser getChanelImageShowDegrade() {
        try {
            ZpLogger.i(TAG, ".getChanelImageShowDegrade");
            if (this.channelDegradeConfigs != null) {
                int i = 0;
                while (true) {
                    if (i >= this.channelDegradeConfigs.length()) {
                        break;
                    }
                    JSONObject item = this.channelDegradeConfigs.optJSONObject(i);
                    if (item == null || !BuildConfig.CHANNELID.equals(item.optString("channelId"))) {
                        i++;
                    } else {
                        int flag = item.optInt("img_show_degrade", -1);
                        if (flag != -1) {
                            ZpLogger.i(TAG, ".getChanelImageShowDegrade rtn :" + flag);
                            return new ImageShowDegradeManager.ConfigParser(flag);
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        ZpLogger.i(TAG, ".getChanelImageShowDegrade rtn null");
        return null;
    }

    public PageStackDegradeManager.ConfigParser getChanelPageStackDegrade() {
        try {
            ZpLogger.i(TAG, ".getChanelPageStackDegrade");
            if (this.channelDegradeConfigs != null) {
                int i = 0;
                while (true) {
                    if (i >= this.channelDegradeConfigs.length()) {
                        break;
                    }
                    JSONObject item = this.channelDegradeConfigs.optJSONObject(i);
                    if (item == null || !BuildConfig.CHANNELID.equals(item.optString("channelId"))) {
                        i++;
                    } else {
                        int flag = item.optInt("page_stack_degrade", -1);
                        if (flag != -1) {
                            ZpLogger.i(TAG, ".getChanelPageStackDegrade rtn :" + flag);
                            return new PageStackDegradeManager.ConfigParser(flag);
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        ZpLogger.i(TAG, ".getChanelPageStackDegrade rtn null");
        return null;
    }

    public WidgetDegradeManager.ConfigParser getChanelWidgetDegrade() {
        try {
            ZpLogger.i(TAG, ".getChanelWidgetDegrade");
            if (this.channelDegradeConfigs != null) {
                int i = 0;
                while (true) {
                    if (i >= this.channelDegradeConfigs.length()) {
                        break;
                    }
                    JSONObject item = this.channelDegradeConfigs.optJSONObject(i);
                    if (item == null || !BuildConfig.CHANNELID.equals(item.optString("channelId"))) {
                        i++;
                    } else {
                        int flag = item.optInt("widget_degrade", -1);
                        if (flag != -1) {
                            ZpLogger.i(TAG, ".getChanelWidgetDegrade rtn :" + flag);
                            return new WidgetDegradeManager.ConfigParser(flag);
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        ZpLogger.i(TAG, ".getChanelWidgetDegrade rtn null");
        return null;
    }
}
