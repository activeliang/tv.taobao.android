package com.ut.mini.core;

import com.alibaba.analytics.core.LogProcessor;
import com.alibaba.analytics.core.config.UTSampleConfBiz;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorConfigMgr;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather;
import com.alibaba.analytics.utils.Logger;
import com.ut.mini.core.UTSendLogDelegate;
import java.util.Map;

public class UTLogTransferMain implements UTSendLogDelegate.ISendLogListener {
    private static UTLogTransferMain s_instance = new UTLogTransferMain();
    private Object mInitializeLockObj = new Object();
    private volatile boolean mIsInitialized = false;
    public SelfMonitorEventDispather mMonitor = new SelfMonitorEventDispather();
    private UTSendLogDelegate mSendLogDelegate = null;

    private UTLogTransferMain() {
    }

    public static UTLogTransferMain getInstance() {
        return s_instance;
    }

    private void _initialize() {
        if (!this.mIsInitialized) {
            synchronized (this.mInitializeLockObj) {
                if (!this.mIsInitialized) {
                    this.mSendLogDelegate = new UTSendLogDelegate();
                    this.mSendLogDelegate.setSendLogListener(this);
                    this.mSendLogDelegate.start();
                    this.mIsInitialized = true;
                }
            }
        }
    }

    public void transferLog(Map<String, String> aLogMap) {
        _initialize();
        if (aLogMap.containsKey("_sls")) {
            _transferLog(aLogMap);
        } else if (this.mSendLogDelegate != null) {
            this.mSendLogDelegate.send(aLogMap);
        }
    }

    private void _transferLog(Map<String, String> aLogMap) {
        if (aLogMap != null) {
            try {
                String eventId = aLogMap.get(LogField.EVENTID.toString());
                if (SelfMonitorConfigMgr.getInstance().isNeedMonitorForUT(eventId)) {
                    this.mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.INTERFACE, eventId, Double.valueOf(1.0d)));
                }
                if (UTSampleConfBiz.getInstance().isSampleSuccess(aLogMap)) {
                    LogProcessor.process(aLogMap);
                    return;
                }
                Logger.i("log discard", "aLogMap", aLogMap);
            } catch (Throwable e) {
                Logger.e((String) null, e, new Object[0]);
            }
        }
    }

    public void onLogArrived(Map<String, String> aLogMap) {
        if (aLogMap != null) {
            _transferLog(aLogMap);
        }
    }
}
