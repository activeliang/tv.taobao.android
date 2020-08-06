package com.alibaba.analytics.core.selfmonitor;

import com.alibaba.analytics.core.config.UTOrangeConfMgr;
import com.alibaba.analytics.core.store.LogStoreMgr;
import com.alibaba.analytics.core.sync.BizRequest;
import com.alibaba.analytics.core.sync.TnetUtil;
import com.alibaba.analytics.core.sync.UploadLogFromCache;
import com.alibaba.analytics.core.sync.UploadLogFromDB;
import com.alibaba.analytics.core.sync.UrlWrapper;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.appmonitor.delegate.AppMonitorDelegate;
import com.alibaba.appmonitor.event.EventType;
import com.ut.mini.core.UTLogTransferMain;

public class SelfMonitorHandle implements SelfMonitorEventListener {
    private static SelfMonitorHandle instance = new SelfMonitorHandle();

    public static SelfMonitorHandle getInstance() {
        return instance;
    }

    public void init() {
        try {
            AppMonitorDelegate.mMonitor.regiserListener(this);
        } catch (Throwable e) {
            Logger.e((String) null, e, new Object[0]);
        }
        try {
            UploadLogFromDB.getInstance().mMonitor.regiserListener(this);
        } catch (Throwable e2) {
            Logger.e((String) null, e2, new Object[0]);
        }
        try {
            UploadLogFromCache.getInstance().mMonitor.regiserListener(this);
        } catch (Throwable e3) {
            Logger.e((String) null, e3, new Object[0]);
        }
        try {
            UrlWrapper.mMonitor.regiserListener(this);
        } catch (Throwable e4) {
            Logger.e((String) null, e4, new Object[0]);
        }
        try {
            BizRequest.mMonitor.regiserListener(this);
        } catch (Throwable e5) {
            Logger.e((String) null, e5, new Object[0]);
        }
        try {
            LogStoreMgr.mMonitor.regiserListener(this);
        } catch (Throwable e6) {
            Logger.e((String) null, e6, new Object[0]);
        }
        try {
            UTLogTransferMain.getInstance().mMonitor.regiserListener(this);
        } catch (Throwable e7) {
            Logger.e((String) null, e7, new Object[0]);
        }
        try {
            UTOrangeConfMgr.mMonitor.regiserListener(this);
        } catch (Throwable e8) {
            Logger.e((String) null, e8, new Object[0]);
        }
        try {
            TnetUtil.mMonitor.regiserListener(this);
        } catch (Throwable e9) {
            Logger.e((String) null, e9, new Object[0]);
        }
        try {
            ConfigArrivedMonitor.mMonitor.regiserListener(this);
        } catch (Throwable e10) {
            Logger.e((String) null, e10, new Object[0]);
        }
    }

    @Deprecated
    public void handleEvent(SelfMonitorEvent event) {
    }

    public void onEvent(SelfMonitorEvent event) {
        if (event.type == EventType.COUNTER) {
            AppMonitorDelegate.Counter.commit(SelfMonitorEvent.module, event.monitorPoint, event.arg, event.value.doubleValue());
        } else if (event.type == EventType.STAT) {
            AppMonitorDelegate.Stat.commit(SelfMonitorEvent.module, event.monitorPoint, event.dvs, event.mvs);
        }
    }
}
