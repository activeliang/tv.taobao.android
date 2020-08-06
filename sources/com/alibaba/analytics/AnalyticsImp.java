package com.alibaba.analytics;

import android.app.Application;
import android.os.RemoteException;
import com.alibaba.analytics.IAnalytics;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.DebugPluginSwitch;
import com.alibaba.analytics.core.config.SystemConfigMgr;
import com.alibaba.analytics.core.logbuilder.SessionTimeAndIndexMgr;
import com.alibaba.analytics.core.selfmonitor.SelfChecker;
import com.alibaba.analytics.core.sync.UploadMgr;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.appmonitor.delegate.AppMonitorDelegate;
import com.alibaba.appmonitor.delegate.TransactionDelegate;
import com.alibaba.appmonitor.event.EventType;
import com.alibaba.mtl.appmonitor.Transaction;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.ut.mini.UTAnalyticsDelegate;
import java.util.Map;

public class AnalyticsImp extends IAnalytics.Stub {
    private static Application mApplication;

    public AnalyticsImp(Application application) {
        mApplication = application;
    }

    public void initUT() throws RemoteException {
        Logger.d("start..", new Object[0]);
        Variables.getInstance().init(mApplication);
        Logger.d("end..", new Object[0]);
    }

    public static Application getApplication() {
        return mApplication;
    }

    public void updateUserAccount(String aUsernick, String aUserid, String openid) throws RemoteException {
        try {
            UTAnalyticsDelegate.getInstance().updateUserAccount(aUsernick, aUserid, openid);
        } catch (VerifyError error) {
            Logger.e((String) null, error, new Object[0]);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void setAppVersion(String appVersion) throws RemoteException {
        try {
            UTAnalyticsDelegate.getInstance().setAppVersion(appVersion);
        } catch (VerifyError error) {
            Logger.e((String) null, error, new Object[0]);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void setChannel(String channel) throws RemoteException {
        try {
            UTAnalyticsDelegate.getInstance().setChannel(channel);
        } catch (VerifyError error) {
            Logger.e((String) null, error, new Object[0]);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void updateSessionProperties(Map aMap) throws RemoteException {
        try {
            UTAnalyticsDelegate.getInstance().updateSessionProperties(aMap);
        } catch (VerifyError error) {
            Logger.e((String) null, error, new Object[0]);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void setSessionProperties(Map aMap) throws RemoteException {
        try {
            UTAnalyticsDelegate.getInstance().setSessionProperties(aMap);
        } catch (VerifyError error) {
            Logger.e((String) null, error, new Object[0]);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void turnOnDebug() throws RemoteException {
        try {
            UTAnalyticsDelegate.getInstance().turnOnDebug();
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void transferLog(Map aLogMap) throws RemoteException {
        Logger.d();
        try {
            if (!Variables.getInstance().isInit()) {
                Variables.getInstance().init(mApplication);
            }
            UTAnalyticsDelegate.getInstance().transferLog(aLogMap);
        } catch (VerifyError error) {
            Logger.e((String) null, error, new Object[0]);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void dispatchLocalHits() throws RemoteException {
        try {
            UploadMgr.getInstance().dispatchHits();
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void saveCacheDataToLocal() throws RemoteException {
        try {
            UTAnalyticsDelegate.getInstance().saveCacheDataToLocal();
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void init() throws RemoteException {
        try {
            initUT();
        } catch (Throwable e) {
            Logger.e((String) null, e, new Object[0]);
        }
    }

    public void destroy() throws RemoteException {
        try {
            AppMonitorDelegate.destroy();
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void triggerUpload() throws RemoteException {
        try {
            AppMonitorDelegate.triggerUpload();
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void setSampling(int sampling) throws RemoteException {
        try {
            AppMonitorDelegate.setSampling(sampling);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void enableLog(boolean open) throws RemoteException {
        try {
            AppMonitorDelegate.enableLog(open);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public String selfCheck(String value) throws RemoteException {
        try {
            return SelfChecker.getInstance().check("selfcheck", value);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
            return null;
        }
    }

    public void setStatisticsInterval2(int event, int statisticsInterval) throws RemoteException {
        try {
            AppMonitorDelegate.setStatisticsInterval(getEventType(event), statisticsInterval);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void setRequestAuthInfo(boolean isSecurity, boolean isEncode, String appkey, String secret) throws RemoteException {
        try {
            AppMonitorDelegate.setRequestAuthInfo(isSecurity, isEncode, appkey, secret);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void turnOnRealTimeDebug(Map params) throws RemoteException {
        try {
            Variables.getInstance().turnOnRealTimeDebug(params);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void turnOffRealTimeDebug() throws RemoteException {
        try {
            Variables.getInstance().turnOffRealTimeDebug();
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void counter_setStatisticsInterval(int statisticsInterval) throws RemoteException {
        try {
            AppMonitorDelegate.Counter.setStatisticsInterval(statisticsInterval);
        } catch (VerifyError error) {
            Logger.e((String) null, error, new Object[0]);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void counter_setSampling(int sampling) throws RemoteException {
        try {
            AppMonitorDelegate.Counter.setSampling(sampling);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public boolean counter_checkSampled(String module, String monitorPoint) throws RemoteException {
        try {
            return AppMonitorDelegate.Counter.checkSampled(module, monitorPoint);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
            return false;
        }
    }

    public void counter_commit1(String module, String monitorPoint, double value) throws RemoteException {
        try {
            AppMonitorDelegate.Counter.commit(module, monitorPoint, value);
        } catch (VerifyError error) {
            Logger.e((String) null, error, new Object[0]);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void counter_commit2(String module, String monitorPoint, String arg, double value) throws RemoteException {
        try {
            AppMonitorDelegate.Counter.commit(module, monitorPoint, arg, value);
        } catch (VerifyError error) {
            Logger.e((String) null, error, new Object[0]);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void alarm_setStatisticsInterval(int statisticsInterval) throws RemoteException {
        try {
            AppMonitorDelegate.Alarm.setStatisticsInterval(statisticsInterval);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void alarm_setSampling(int sampling) throws RemoteException {
        try {
            AppMonitorDelegate.Alarm.setSampling(sampling);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public boolean alarm_checkSampled(String module, String monitorPoint) throws RemoteException {
        try {
            return AppMonitorDelegate.Alarm.checkSampled(module, monitorPoint);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
            return false;
        }
    }

    public void alarm_commitSuccess1(String module, String monitorPoint) throws RemoteException {
        try {
            AppMonitorDelegate.Alarm.commitSuccess(module, monitorPoint);
        } catch (Throwable error) {
            Logger.e((String) null, error, new Object[0]);
        }
    }

    public void alarm_commitSuccess2(String module, String monitorPoint, String arg) throws RemoteException {
        try {
            AppMonitorDelegate.Alarm.commitSuccess(module, monitorPoint, arg);
        } catch (Throwable error) {
            Logger.e((String) null, error, new Object[0]);
        }
    }

    public void alarm_commitFail1(String module, String monitorPoint, String errorCode, String errorMsg) throws RemoteException {
        try {
            AppMonitorDelegate.Alarm.commitFail(module, monitorPoint, errorCode, errorMsg);
        } catch (Throwable error) {
            Logger.e((String) null, error, new Object[0]);
        }
    }

    public void alarm_commitFail2(String module, String monitorPoint, String arg, String errorCode, String errorMsg) throws RemoteException {
        try {
            AppMonitorDelegate.Alarm.commitFail(module, monitorPoint, arg, errorCode, errorMsg);
        } catch (Throwable error) {
            Logger.e((String) null, error, new Object[0]);
        }
    }

    public void offlinecounter_setStatisticsInterval(int statisticsInterval) throws RemoteException {
        try {
            AppMonitorDelegate.OffLineCounter.setStatisticsInterval(statisticsInterval);
        } catch (Throwable error) {
            Logger.e((String) null, error, new Object[0]);
        }
    }

    public void offlinecounter_setSampling(int sampling) throws RemoteException {
        AppMonitorDelegate.OffLineCounter.setSampling(sampling);
    }

    public boolean offlinecounter_checkSampled(String module, String monitorPoint) throws RemoteException {
        return AppMonitorDelegate.OffLineCounter.checkSampled(module, monitorPoint);
    }

    public void offlinecounter_commit(String module, String monitorPoint, double value) throws RemoteException {
        AppMonitorDelegate.OffLineCounter.commit(module, monitorPoint, value);
    }

    public void setStatisticsInterval1(int statisticsInterval) throws RemoteException {
        try {
            AppMonitorDelegate.setStatisticsInterval(statisticsInterval);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void register1(String module, String monitorPoint, MeasureSet measures) throws RemoteException {
        try {
            AppMonitorDelegate.register(module, monitorPoint, measures);
        } catch (Throwable error) {
            Logger.e((String) null, error, new Object[0]);
        }
    }

    public void register2(String module, String monitorPoint, MeasureSet measures, boolean isCommitDetail) throws RemoteException {
        try {
            AppMonitorDelegate.register(module, monitorPoint, measures, isCommitDetail);
        } catch (Throwable error) {
            Logger.e((String) null, error, new Object[0]);
        }
    }

    public void register3(String module, String monitorPoint, MeasureSet measures, DimensionSet dimensions) throws RemoteException {
        try {
            AppMonitorDelegate.register(module, monitorPoint, measures, dimensions);
        } catch (Throwable error) {
            Logger.e((String) null, error, new Object[0]);
        }
    }

    public void register4(String module, String monitorPoint, MeasureSet measures, DimensionSet dimensions, boolean isCommitDetail) throws RemoteException {
        try {
            AppMonitorDelegate.register(module, monitorPoint, measures, dimensions, isCommitDetail);
        } catch (Throwable error) {
            Logger.e((String) null, error, new Object[0]);
        }
    }

    public void stat_begin(String module, String monitorPoint, String measureName) throws RemoteException {
        try {
            AppMonitorDelegate.Stat.begin(module, monitorPoint, measureName);
        } catch (Throwable error) {
            Logger.e((String) null, error, new Object[0]);
        }
    }

    public void stat_end(String module, String monitorPoint, String measureName) throws RemoteException {
        try {
            AppMonitorDelegate.Stat.end(module, monitorPoint, measureName);
        } catch (Throwable error) {
            Logger.e((String) null, error, new Object[0]);
        }
    }

    public void stat_setStatisticsInterval(int statisticsInterval) throws RemoteException {
        try {
            AppMonitorDelegate.Stat.setStatisticsInterval(statisticsInterval);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public void stat_setSampling(int sampling) throws RemoteException {
        try {
            AppMonitorDelegate.Stat.setSampling(sampling);
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    public boolean stat_checkSampled(String module, String monitorPoint) throws RemoteException {
        return AppMonitorDelegate.Stat.checkSampled(module, monitorPoint);
    }

    public void stat_commit1(String module, String monitorPoint, double value) throws RemoteException {
        try {
            AppMonitorDelegate.Stat.commit(module, monitorPoint, value);
        } catch (Throwable error) {
            Logger.e((String) null, error, new Object[0]);
        }
    }

    public void stat_commit2(String module, String monitorPoint, DimensionValueSet dimensionValues, double value) throws RemoteException {
        try {
            AppMonitorDelegate.Stat.commit(module, monitorPoint, dimensionValues, value);
        } catch (Throwable error) {
            Logger.e((String) null, error, new Object[0]);
        }
    }

    public void stat_commit3(String module, String monitorPoint, DimensionValueSet dimensionValues, MeasureValueSet measureValues) throws RemoteException {
        try {
            AppMonitorDelegate.Stat.commit(module, monitorPoint, dimensionValues, measureValues);
        } catch (Throwable error) {
            Logger.e((String) null, error, new Object[0]);
        }
    }

    private EventType getEventType(int eventId) {
        return EventType.getEventType(eventId);
    }

    public void transaction_begin(Transaction transaction, String measureName) throws RemoteException {
        try {
            TransactionDelegate.begin(transaction, measureName);
        } catch (Throwable error) {
            Logger.e((String) null, error, new Object[0]);
        }
    }

    public void transaction_end(Transaction transaction, String measureName) throws RemoteException {
        try {
            TransactionDelegate.end(transaction, measureName);
        } catch (Throwable error) {
            Logger.e((String) null, error, new Object[0]);
        }
    }

    public void updateMeasure(String module, String monitorPoint, String name, double min, double max, double defaultValue) throws RemoteException {
        AppMonitorDelegate.updateMeasure(module, monitorPoint, name, min, max, defaultValue);
    }

    public String getValue(String aKey) throws RemoteException {
        try {
            if (DebugPluginSwitch.KEY.equals(aKey)) {
                return SystemConfigMgr.getInstance().get(aKey);
            }
            if ("tpk_md5".equals(aKey)) {
                return Variables.getInstance().getTpkMD5();
            }
            if ("tpk_string".equals(aKey)) {
                return Variables.getInstance().getTPKString();
            }
            if ("session_timestamp".equals(aKey)) {
                return "" + SessionTimeAndIndexMgr.getInstance().getSessionTimestamp();
            }
            if ("autoExposure".equalsIgnoreCase(aKey)) {
                return SystemConfigMgr.getInstance().get(aKey);
            }
            return null;
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
            return null;
        }
    }
}
