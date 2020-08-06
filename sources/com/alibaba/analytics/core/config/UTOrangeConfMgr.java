package com.alibaba.analytics.core.config;

import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.TaskExecutor;
import com.taobao.orange.OrangeConfig;
import com.taobao.orange.OrangeConfigListenerV1;
import java.util.Map;

public class UTOrangeConfMgr extends UTBaseConfMgr {
    /* access modifiers changed from: private */
    public static final String[] ORANGE_CONFIGS = {"ut_sample", "ut_stream", "ut_bussiness", "utap_system", "ap_alarm", "ap_counter", "ap_stat", "ut_realtime"};
    public static final SelfMonitorEventDispather mMonitor = new SelfMonitorEventDispather();

    public void requestOnlineConfig() {
        try {
            OrangeConfig.getInstance().init(Variables.getInstance().getContext());
            TaskExecutor.getInstance().submit(new OrangeGetConfigsRunnable());
            OrangeConfig.getInstance().registerListener(ORANGE_CONFIGS, (OrangeConfigListenerV1) new OrangeConfigListenerV1() {
                public void onConfigUpdate(String aGroupname, boolean aIsCached) {
                    Logger.d((String) null, "aGroupname", aGroupname, "aIsCached", Boolean.valueOf(aIsCached));
                    Map<String, String> configs = OrangeConfig.getInstance().getConfigs(aGroupname);
                    if (configs != null) {
                        UTOrangeConfMgr.super.updateAndDispatch(aGroupname, configs);
                        UTBaseConfMgr.sendConfigTimeStamp("2");
                    }
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    final class OrangeGetConfigsRunnable implements Runnable {
        private final int[] mSleepIntervalList = {1, 1, 2, 2, 4, 4, 8, 8};

        OrangeGetConfigsRunnable() {
        }

        public void run() {
            String[] lOrangeConfigs = UTOrangeConfMgr.ORANGE_CONFIGS;
            UTOrangeConfMgr.super.init();
            UTOrangeConfMgr.super.dispatchLocalCacheConfigs();
            UTBaseConfMgr.sendConfigTimeStamp("0");
            int lSuccessCount = 0;
            int lSleepIntervalIndex = 0;
            do {
                for (int i = 0; i < lOrangeConfigs.length; i++) {
                    if (!(lOrangeConfigs[i] == null || OrangeConfig.getInstance().getConfigs(UTOrangeConfMgr.ORANGE_CONFIGS[i]) == null)) {
                        lOrangeConfigs[i] = null;
                        lSuccessCount++;
                    }
                }
                if (lSuccessCount == lOrangeConfigs.length) {
                    break;
                }
                try {
                    Thread.sleep((long) (this.mSleepIntervalList[lSleepIntervalIndex] * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lSleepIntervalIndex++;
            } while (lSleepIntervalIndex <= this.mSleepIntervalList.length);
            for (String lOrangeConfig : lOrangeConfigs) {
                if (lOrangeConfig != null) {
                    UTOrangeConfMgr.super.deleteDBConfigEntity(lOrangeConfig);
                }
            }
        }
    }
}
