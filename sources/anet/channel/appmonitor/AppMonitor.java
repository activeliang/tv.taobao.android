package anet.channel.appmonitor;

import anet.channel.statist.AlarmObject;
import anet.channel.statist.CountObject;
import anet.channel.statist.StatObject;

public class AppMonitor {
    private static volatile IAppMonitor appMonitor = new Proxy((IAppMonitor) null);

    public static IAppMonitor getInstance() {
        return appMonitor;
    }

    public static void setInstance(IAppMonitor appMonitor2) {
        appMonitor = new Proxy(appMonitor2);
    }

    static class Proxy implements IAppMonitor {
        IAppMonitor appMonitor = null;

        Proxy(IAppMonitor appMonitor2) {
            this.appMonitor = appMonitor2;
        }

        @Deprecated
        public void register() {
        }

        @Deprecated
        public void register(Class<?> cls) {
        }

        public void commitStat(StatObject obj) {
            if (this.appMonitor != null) {
                this.appMonitor.commitStat(obj);
            }
        }

        public void commitAlarm(AlarmObject obj) {
            if (this.appMonitor != null) {
                this.appMonitor.commitAlarm(obj);
            }
        }

        public void commitCount(CountObject object) {
            if (this.appMonitor != null) {
                this.appMonitor.commitCount(object);
            }
        }
    }
}
