package com.alibaba.motu.crashreporter;

import android.content.Context;
import com.alibaba.motu.crashreporter.CatcherManager;
import com.alibaba.motu.crashreporter.Options;
import com.alibaba.motu.crashreporter.Propertys;
import com.alibaba.motu.crashreporter.utils.StringUtils;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.ut.mini.crashhandler.UTCrashHandlerWapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class MotuCrashReporter {
    static final MotuCrashReporter INSTANCE = new MotuCrashReporter();
    static List dataListenerList = new ArrayList();
    static List listenerList = new ArrayList();
    static List senderListenerList = new ArrayList();
    AtomicBoolean enabling = new AtomicBoolean(false);
    CrashReporter mCrashReporter = CrashReporter.getInstance();

    public static MotuCrashReporter getInstance() {
        return INSTANCE;
    }

    public void setCrashCaughtListener(final UTCrashHandlerWapper handlerWapper) {
        this.mCrashReporter.addUncaughtExceptionLinster(new CatcherManager.UncaughtExceptionLinster() {
            public boolean originalEquals(Object o) {
                if (handlerWapper == null || o == null) {
                    return false;
                }
                return handlerWapper.equals(o);
            }

            public Map<String, Object> onUncaughtException(Thread thread, Throwable throwable) {
                return handlerWapper.onCrashCaught(thread, throwable);
            }
        });
    }

    public void setCrashCaughtListener(final IUTCrashCaughtListener listener) {
        this.mCrashReporter.addUncaughtExceptionLinster(new CatcherManager.UncaughtExceptionLinster() {
            public boolean originalEquals(Object o) {
                if (listener == null || o == null) {
                    return false;
                }
                return listener.equals(o);
            }

            public Map<String, Object> onUncaughtException(Thread thread, Throwable throwable) {
                return listener.onCrashCaught(thread, throwable);
            }
        });
    }

    public void setCrashReportDataListener(ICrashReportDataListener crashReportDataListener) {
    }

    public void addCrashReportSendListener(ICrashReportSendListener sendListener) {
        this.mCrashReporter.addSendLinster(sendListener);
    }

    public void removeCrashReportSendListener(ICrashReportSendListener sendListener) {
        this.mCrashReporter.removeSendLinster(sendListener);
    }

    public boolean isTaobaoApplication(Context context) {
        return StringUtils.isNotBlank(this.mCrashReporter.mProcessName) && this.mCrashReporter.mProcessName.startsWith("com.taobao.taobao");
    }

    @Deprecated
    public boolean enable(Context context, String appKey, String appVersion, String channel, String userNick, ReporterConfigure configure) {
        String appId = "";
        if (StringUtils.isBlank(appKey) || "12278902".equals(appKey) || "21646297".equals(appKey)) {
            appKey = "21646297";
            appId = "12278902@android";
        }
        return enable(context, appId, appKey, appVersion, channel, userNick, (ReporterConfigure) null);
    }

    public boolean enable(Context context, String appId, String appKey, String appVersion, String channel, String userNick, ReporterConfigure configure) {
        if (this.enabling.compareAndSet(false, true)) {
            try {
                Configuration configuration = new Configuration();
                if (configure != null) {
                    configuration.add(new Options.Option(Configuration.enableUncaughtExceptionIgnore, Boolean.valueOf(configure.enableUncaughtExceptionIgnore)));
                    configuration.add(new Options.Option(Configuration.enableExternalLinster, Boolean.valueOf(configure.enableExternalLinster)));
                    configuration.add(new Options.Option(Configuration.enableFinalizeFake, Boolean.valueOf(configure.enableFinalizeFake)));
                    configuration.add(new Options.Option(Configuration.enableUIProcessSafeGuard, Boolean.valueOf(configure.enableUIProcessSafeGuard)));
                }
                this.mCrashReporter.initialize(context, appId, appKey, appVersion, channel, configuration);
                this.mCrashReporter.enable();
                setUserNick(userNick);
                return true;
            } catch (Exception e) {
                LogUtil.e(IWaStat.KEY_ENABLE, e);
            }
        }
        return false;
    }

    public void setAppVersion(String appVersion) {
        this.mCrashReporter.refreshAppVersion(appVersion);
    }

    public void setExtraInfo(String extra) {
    }

    public void setUserNick(String userNick) {
        if (!StringUtils.isBlank(userNick)) {
            this.mCrashReporter.setProperty(new Propertys.Property(Constants.USERNICK, userNick));
        }
    }

    public void setTTid(String channel) {
        if (!StringUtils.isBlank(channel)) {
            this.mCrashReporter.setProperty(new Propertys.Property(Constants.CHANNEL, channel));
        }
    }

    public List getMyListenerList() {
        return this.mCrashReporter.getAllUncaughtExceptionLinster();
    }

    public List getMyDataListenerList() {
        return dataListenerList;
    }

    public List getMySenderListenerList() {
        return senderListenerList;
    }

    public String getStrExtraInfo() {
        return "";
    }

    public void setCrashReporterState(int iType) {
    }

    public int getCrashReporterState() {
        return 0;
    }
}
