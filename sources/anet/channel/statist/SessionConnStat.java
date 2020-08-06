package anet.channel.statist;

import anet.channel.Session;
import anet.channel.status.NetworkStatusHelper;

@Monitor(module = "networkPrefer", monitorPoint = "conn_stat")
public class SessionConnStat extends StatObject {
    @Dimension
    public float accuracy = -1.0f;
    @Measure(max = 60000.0d)
    public long authTime = 0;
    @Dimension
    public String bssid = NetworkStatusHelper.getWifiBSSID();
    @Dimension
    public String errorCode;
    @Dimension
    public String errorMsg;
    @Dimension
    public StringBuilder errorTrace;
    @Dimension
    public String host;
    @Dimension
    public String ip;
    @Dimension
    public int ipRefer = 0;
    @Dimension
    public int ipType = 1;
    public volatile boolean isCommited = false;
    @Dimension
    public int isProxy = 0;
    @Dimension
    public double lat = 90000.0d;
    @Dimension
    public double lng = 90000.0d;
    @Dimension
    public String mnc;
    @Dimension
    public String netType = NetworkStatusHelper.getNetworkSubType();
    @Dimension
    public int port;
    @Dimension
    public String protocolType;
    @Dimension
    public int ret;
    @Dimension
    public int retryTimes;
    @Dimension
    public int roaming;
    public volatile long start = 0;
    public volatile long startConnect = 0;
    @Measure(max = 60000.0d)
    public long totalTime = 0;
    @Dimension
    public String unit;

    public SessionConnStat() {
        int i = 1;
        this.roaming = !NetworkStatusHelper.isRoaming() ? 0 : i;
        this.mnc = NetworkStatusHelper.getSimOp();
    }

    public void syncValueFromSession(Session session) {
        SessionStatistic ss = session.mSessionStat;
        this.ip = ss.ip;
        this.port = ss.port;
        this.ipRefer = ss.ipRefer;
        this.ipType = ss.ipType;
        this.protocolType = ss.conntype;
        this.host = ss.host;
        this.isProxy = ss.isProxy;
        this.authTime = ss.authTime;
        this.unit = session.getUnit();
        if (this.unit == null && this.ipRefer == 1) {
            this.unit = "LocalDNS";
        }
    }

    public boolean beforeCommit() {
        if (this.isCommited) {
            return false;
        }
        this.isCommited = true;
        return true;
    }

    public void appendErrorTrace(int errorCode2) {
        if (this.errorTrace == null) {
            this.errorTrace = new StringBuilder();
        }
        if (this.errorTrace.length() > 0) {
            this.errorTrace.append(",");
        }
        this.errorTrace.append(errorCode2).append("=").append(System.currentTimeMillis() - this.startConnect);
    }
}
