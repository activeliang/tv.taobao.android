package anet.channel.statist;

import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.entity.ConnType;
import anet.channel.status.NetworkStatusHelper;

@Monitor(module = "networkPrefer", monitorPoint = "network", sampleRate = 1000)
public class RequestStatistic extends StatObject {
    @Dimension
    public float accuracy = -1.0f;
    @Dimension
    public volatile String bizId;
    @Dimension
    public volatile String bssid = null;
    @Measure
    public volatile long cacheTime = 0;
    @Measure
    public volatile long connWaitTime = 0;
    @Dimension
    public volatile String contentEncoding = null;
    @Dimension
    public volatile int degraded = 0;
    @Dimension
    public volatile StringBuilder errorTrace = null;
    @Measure
    public volatile long firstDataTime = 0;
    @Dimension
    public volatile String host;
    @Dimension
    public volatile String ip;
    @Dimension
    public volatile int ipRefer = 0;
    @Dimension
    public volatile int ipType = 1;
    @Dimension
    public volatile String isBg = "";
    @Dimension
    public volatile boolean isDNS = false;
    @Dimension
    public volatile boolean isProxy;
    @Dimension
    public volatile boolean isSSL;
    @Dimension
    public double lat = 90000.0d;
    @Dimension
    public double lng = 90000.0d;
    @Dimension
    public String mnc = "0";
    @Dimension(name = "errorMsg")
    public volatile String msg = "";
    @Dimension
    public volatile String netType = "";
    @Measure(max = 60000.0d)
    public volatile long oneWayTime = 0;
    @Dimension
    public volatile int port;
    @Measure
    public volatile long processTime = 0;
    @Dimension
    public volatile String protocolType;
    @Dimension
    public volatile String proxyType = "";
    @Measure
    public volatile long recDataSize = 0;
    @Measure
    public volatile long recDataTime = 0;
    @Measure
    public volatile long reqBodyDeflateSize = 0;
    @Measure
    public volatile long reqBodyInflateSize = 0;
    @Measure
    public volatile long reqHeadDeflateSize = 0;
    @Measure
    public volatile long reqHeadInflateSize = 0;
    public volatile long requestStart = 0;
    @Dimension
    public volatile int ret;
    @Dimension
    public volatile int retryTimes;
    @Dimension
    public int roaming = 0;
    @Measure
    public volatile long rspBodyDeflateSize = 0;
    @Measure
    public volatile long rspBodyInflateSize = 0;
    @Measure
    public volatile long rspHeadDeflateSize = 0;
    @Measure
    public volatile long rspHeadInflateSize = 0;
    public volatile long rspStart = 0;
    @Measure
    public volatile long sendBeforeTime = 0;
    @Measure
    public volatile long sendDataSize = 0;
    @Measure
    public volatile long sendDataTime = 0;
    public volatile long sendEnd = 0;
    public volatile long sendStart = 0;
    @Measure
    public volatile long serverRT = 0;
    public volatile boolean spdyRequestSend = false;
    public volatile long start = 0;
    @Dimension(name = "errorCode")
    public volatile int statusCode = 0;
    @Dimension
    public String unit;
    @Dimension(name = "URL")
    public volatile String url;
    @Deprecated
    public volatile long waitingTime = 0;

    public RequestStatistic(String host2, String bizId2) {
        int i = 1;
        this.host = host2;
        this.proxyType = NetworkStatusHelper.getProxyType();
        this.isProxy = !this.proxyType.isEmpty();
        this.netType = NetworkStatusHelper.getNetworkSubType();
        this.bssid = NetworkStatusHelper.getWifiBSSID();
        this.isBg = GlobalAppRuntimeInfo.isAppBackground() ? "bg" : "fg";
        this.roaming = !NetworkStatusHelper.isRoaming() ? 0 : i;
        this.mnc = NetworkStatusHelper.getSimOp();
        this.bizId = bizId2;
    }

    public void setConnType(ConnType connType) {
        this.isSSL = connType.isSSL();
        this.protocolType = connType.toString();
    }

    public void setIPAndPort(String ip2, int port2) {
        this.ip = ip2;
        this.port = port2;
        if (ip2 != null && port2 != 0) {
            this.isDNS = true;
        }
    }

    public void appendErrorTrace(int errorCode) {
        if (this.errorTrace == null) {
            this.errorTrace = new StringBuilder();
        }
        if (this.errorTrace.length() != 0) {
            this.errorTrace.append(",");
        }
        this.errorTrace.append(errorCode).append("=").append(System.currentTimeMillis() - this.requestStart);
    }
}
