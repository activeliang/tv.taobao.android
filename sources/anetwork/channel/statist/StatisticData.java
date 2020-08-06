package anetwork.channel.statist;

import anet.channel.statist.RequestStatistic;
import anet.channel.util.StringUtils;
import java.io.Serializable;

public class StatisticData implements Serializable, Cloneable {
    private static final long serialVersionUID = -3538602124202475612L;
    @Deprecated
    public String api_v = "";
    public long cacheTime = 0;
    public String connectionType = "";
    public long dataSpeed = 0;
    @Deprecated
    public int dnsTime = 0;
    public long firstDataTime = 0;
    public String host = "";
    public String ip_port = "";
    @Deprecated
    public int isDNSTimeout = 0;
    public boolean isRequestSuccess = false;
    public boolean isSSL = false;
    @Deprecated
    public boolean isSpdy = false;
    public String netStatSum;
    @Deprecated
    public long netTime = 0;
    @Deprecated
    public long oneWayTime_AEngine = 0;
    public long oneWayTime_ANet = 0;
    @Deprecated
    public long oneWayTime_Jni = 0;
    public long postBodyTime = 0;
    public long recDataTime = 0;
    @Deprecated
    public long receiveDataTime = 0;
    @Deprecated
    public int redirectTime;
    @Deprecated
    public long responseBodySize = 0;
    public int resultCode = 0;
    public int retryTime;
    public long rtt = 0;
    public long sendBeforeTime = 0;
    public long sendSize = 0;
    public long serverRT = 0;
    @Deprecated
    public long spdyWaitTime = 0;
    @Deprecated
    public long tcpConnTime = 0;
    @Deprecated
    public long tcpLinkDate = 0;
    @Deprecated
    public String timeoutType = "";
    public long totalSize = 0;

    public void filledBy(RequestStatistic rs) {
        if (rs != null) {
            this.resultCode = rs.statusCode;
            this.connectionType = rs.protocolType;
            this.isRequestSuccess = rs.ret == 1;
            this.host = rs.host;
            if (!(rs.ip == null || rs.port == 0)) {
                this.ip_port = String.format("%s:%d", new Object[]{rs.ip, Integer.valueOf(rs.port)});
            }
            this.retryTime = rs.retryTimes;
            this.isSSL = rs.isSSL;
            this.oneWayTime_ANet = rs.oneWayTime;
            this.cacheTime = rs.cacheTime;
            this.firstDataTime = rs.firstDataTime;
            this.sendBeforeTime = rs.sendBeforeTime;
            this.recDataTime = rs.recDataTime;
            this.sendSize = rs.sendDataSize;
            this.totalSize = rs.recDataSize;
            this.serverRT = rs.serverRT;
            this.dataSpeed = this.recDataTime != 0 ? this.totalSize / this.recDataTime : this.totalSize;
        }
    }

    public String sumNetStat() {
        StringBuilder sumBuilder = new StringBuilder(128);
        sumBuilder.append("isSuccess=").append(this.isRequestSuccess);
        sumBuilder.append(",host=").append(this.host);
        sumBuilder.append(",resultCode=").append(this.resultCode);
        sumBuilder.append(",connType=").append(this.connectionType);
        sumBuilder.append(",oneWayTime_ANet=").append(this.oneWayTime_ANet);
        sumBuilder.append(",ip_port=").append(this.ip_port);
        sumBuilder.append(",isSSL=").append(this.isSSL);
        sumBuilder.append(",cacheTime=").append(this.cacheTime);
        sumBuilder.append(",sendBeforeTime=").append(this.sendBeforeTime);
        sumBuilder.append(",postBodyTime=").append(this.postBodyTime);
        sumBuilder.append(",firstDataTime=").append(this.firstDataTime);
        sumBuilder.append(",recDataTime=").append(this.recDataTime);
        sumBuilder.append(",serverRT=").append(this.serverRT);
        sumBuilder.append(",rtt=").append(this.rtt);
        sumBuilder.append(",sendSize=").append(this.sendSize);
        sumBuilder.append(",totalSize=").append(this.totalSize);
        sumBuilder.append(",dataSpeed=").append(this.dataSpeed);
        sumBuilder.append(",retryTime=").append(this.retryTime);
        return sumBuilder.toString();
    }

    public String toString() {
        if (StringUtils.isBlank(this.netStatSum)) {
            this.netStatSum = sumNetStat();
        }
        return "StatisticData [" + this.netStatSum + "]";
    }
}
