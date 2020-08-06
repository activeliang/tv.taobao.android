package anet.channel.statist;

import anet.channel.util.ErrorConstant;

@Monitor(module = "networkPrefer", monitorPoint = "NetworkError")
public class ExceptionStatistic extends StatObject {
    @Dimension
    public String bizId;
    @Dimension
    public String errorMsg;
    @Dimension
    public String exceptionStack;
    @Dimension
    public String exceptionType;
    @Dimension
    public String host;
    @Dimension
    public String ip;
    @Dimension
    public boolean isDNS;
    @Dimension
    public boolean isProxy;
    @Dimension
    public boolean isSSL;
    @Dimension
    public String netType;
    @Dimension
    public int port;
    @Dimension
    public String protocolType;
    @Dimension
    public String proxyType;
    @Dimension
    public int resultCode;
    @Dimension
    public String url;

    public ExceptionStatistic() {
    }

    public ExceptionStatistic(int errorCode, String errorMsg2, String type) {
        this.resultCode = errorCode;
        this.errorMsg = errorMsg2 == null ? ErrorConstant.getErrMsg(errorCode) : errorMsg2;
        this.exceptionType = type;
    }

    public ExceptionStatistic(int errorCode, String errorMsg2, RequestStatistic rs, Throwable e) {
        this.exceptionType = "nw";
        this.resultCode = errorCode;
        this.errorMsg = errorMsg2 == null ? ErrorConstant.getErrMsg(errorCode) : errorMsg2;
        this.exceptionStack = e != null ? e.toString() : "";
        if (rs != null) {
            this.host = rs.host;
            this.ip = rs.ip;
            this.port = rs.port;
            this.isSSL = rs.isSSL;
            this.isProxy = rs.isProxy;
            this.proxyType = String.valueOf(rs.proxyType);
            this.netType = rs.netType;
            this.isDNS = rs.isDNS;
            this.protocolType = String.valueOf(rs.protocolType);
            this.bizId = rs.bizId;
        }
    }
}
