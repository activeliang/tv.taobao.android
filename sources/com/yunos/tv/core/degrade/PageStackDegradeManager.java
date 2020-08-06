package com.yunos.tv.core.degrade;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tv.core.BuildConfig;
import com.yunos.tv.core.aqm.ActivityQueueManager;
import com.yunos.tv.core.common.DeviceJudge;
import com.yunos.tv.core.common.SharePreferences;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;

public class PageStackDegradeManager {
    /* access modifiers changed from: private */
    public static String TAG = PageStackDegradeManager.class.getSimpleName();
    private static PageStackDegradeManager ourInstance = null;
    private ConfigParser channelConfigParser;
    private Runnable checkTask = new Runnable() {
        public void run() {
            ArrayList<WeakReference<Activity>> arrayList = ActivityQueueManager.getInstance().getActList();
            if (arrayList.size() > PageStackDegradeManager.this.limited_activity_list_size) {
                ZpLogger.i(PageStackDegradeManager.TAG, "[checkTask] -- begin.");
                int finishCount = 0;
                int mastKeepCount = 0;
                for (int i = 0; i < arrayList.size(); i++) {
                    WeakReference<Activity> wrAct = arrayList.get(i);
                    if (wrAct.get() == null) {
                        finishCount++;
                    } else if (((Activity) wrAct.get()).isFinishing()) {
                        finishCount++;
                    } else if (ActivityQueueManager.isTvTaoHome((Activity) wrAct.get())) {
                        mastKeepCount++;
                    } else {
                        if (ActivityQueueManager.isKeepLast((Activity) wrAct.get())) {
                            boolean isLast = true;
                            for (int j = i + 1; j < arrayList.size(); j++) {
                                WeakReference<Activity> wrAct2 = arrayList.get(j);
                                if (!(wrAct2 == null || wrAct2.get() == null || ((Activity) wrAct2.get()).getClass() != ((Activity) wrAct.get()).getClass())) {
                                    isLast = false;
                                }
                            }
                            if (isLast) {
                                mastKeepCount++;
                            }
                        }
                        if (arrayList.size() - finishCount <= PageStackDegradeManager.this.limited_activity_list_size + mastKeepCount) {
                            break;
                        }
                        ZpLogger.i(PageStackDegradeManager.TAG, "[checkTask] finish name=" + ((Activity) wrAct.get()).getClass().getName() + Constant.NLP_CACHE_TYPE + ((Activity) wrAct.get()).hashCode());
                        ((Activity) wrAct.get()).finish();
                        finishCount++;
                        if (arrayList.size() - finishCount <= PageStackDegradeManager.this.limited_activity_list_size) {
                            break;
                        }
                    }
                }
                ZpLogger.i(PageStackDegradeManager.TAG, "[checkTask] -- end. finishCount=" + finishCount);
            }
        }
    };
    private ConfigParser configParser;
    private Handler handler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public int limited_activity_list_size = Integer.MAX_VALUE;
    private int svrCfgValue;
    private int userCfgValue;

    public static PageStackDegradeManager getInstance() {
        if (ourInstance == null) {
            synchronized (PageStackDegradeManager.class) {
                if (ourInstance == null) {
                    ourInstance = new PageStackDegradeManager();
                }
            }
        }
        return ourInstance;
    }

    private PageStackDegradeManager() {
        int flag = 0;
        this.svrCfgValue = SharePreferences.getInt("low_memory_page_stack_degrade", -1).intValue();
        this.userCfgValue = SharePreferences.getInt("low_memory_page_stack_degrade_by_user", -1).intValue();
        this.channelConfigParser = ChannelDegradeManager.getInstance().getChanelPageStackDegrade();
        flag = this.svrCfgValue != -1 ? this.svrCfgValue : flag;
        if (this.userCfgValue != -1) {
            flag = this.userCfgValue;
            this.channelConfigParser = null;
        }
        if (this.svrCfgValue == -1 && this.userCfgValue == -1) {
            flag = BuildConfig.low_memory_page_stack_degrade.intValue();
        }
        this.configParser = new ConfigParser(flag);
        if (DeviceJudge.isMemTypeLow()) {
            this.limited_activity_list_size = 4;
            if (this.channelConfigParser != null) {
                if (this.channelConfigParser.shouldDoDegrade()) {
                    this.limited_activity_list_size = ((Integer) this.configParser.level.second).intValue();
                }
            } else if (this.configParser.shouldDoDegrade()) {
                this.limited_activity_list_size = ((Integer) this.configParser.level.second).intValue();
            }
        } else if (DeviceJudge.isMemTypeMedium()) {
            this.limited_activity_list_size = 9;
        } else if (DeviceJudge.isMemTypeHigh()) {
            this.limited_activity_list_size = 16;
        } else {
            this.limited_activity_list_size = Integer.MAX_VALUE;
        }
        ZpLogger.i(TAG, toString());
    }

    public String toString() {
        return "PageStackDegradeManager{configParser=" + this.configParser + ", limited_activity_list_size=" + this.limited_activity_list_size + ", svrCfgValue=" + this.svrCfgValue + ", userCfgValue=" + this.userCfgValue + '}';
    }

    public void check() {
        ZpLogger.i(TAG, "[check]");
        this.handler.removeCallbacks(this.checkTask);
        this.handler.postDelayed(this.checkTask, 300);
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

    public static class ConfigParser {
        public static final int FLAG_DEFAULT = 0;
        public static final int FLAG_LEVEL1 = 1;
        public static final int FLAG_LEVEL2 = 2;
        private int flag = 0;
        /* access modifiers changed from: private */
        public Pair<Integer, Integer> level = null;
        final Pair<Integer, Integer>[] levelDefs = {new Pair<>(1, 3), new Pair<>(2, 2)};

        ConfigParser(int predefinedLevel) {
            this.flag = predefinedLevel;
            for (int i = 0; i < this.levelDefs.length; i++) {
                if (((Integer) this.levelDefs[i].first).intValue() == this.flag) {
                    this.level = this.levelDefs[i];
                    return;
                }
            }
        }

        /* access modifiers changed from: private */
        public boolean shouldDoDegrade() {
            if (this.level != null) {
                return true;
            }
            return false;
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

        public boolean isLevel1() {
            if (this.flag == 1) {
                return true;
            }
            return false;
        }

        public boolean isLevel2() {
            if (this.flag == 2) {
                return true;
            }
            return false;
        }

        public String toString() {
            return "ConfigParser{levelDefs=" + Arrays.toString(this.levelDefs) + ", level=" + this.level + ", flag=" + this.flag + '}';
        }
    }
}
