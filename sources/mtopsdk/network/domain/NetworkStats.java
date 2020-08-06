package mtopsdk.network.domain;

import java.io.Serializable;
import mtopsdk.common.util.StringUtils;

public class NetworkStats implements Serializable, Cloneable {
    private static final long serialVersionUID = -3538602124202475612L;
    public String connectionType = "";
    public long dataSpeed = 0;
    public long firstDataTime = 0;
    public String host = "";
    public String ip_port = "";
    public boolean isRequestSuccess = false;
    public boolean isSSL = false;
    public String netStatSum;
    public long oneWayTime_ANet = 0;
    public long recDataTime = 0;
    public long recvSize = 0;
    public int resultCode = 0;
    public int retryTimes;
    public long sendSize = 0;
    public long sendWaitTime = 0;
    public long serverRT = 0;

    public String sumNetStat() {
        StringBuilder sumBuilder = new StringBuilder(128);
        sumBuilder.append("oneWayTime_ANet=").append(this.oneWayTime_ANet);
        sumBuilder.append(",resultCode=").append(this.resultCode);
        sumBuilder.append(",isRequestSuccess=").append(this.isRequestSuccess);
        sumBuilder.append(",host=").append(this.host);
        sumBuilder.append(",ip_port=").append(this.ip_port);
        sumBuilder.append(",isSSL=").append(this.isSSL);
        sumBuilder.append(",connType=").append(this.connectionType);
        sumBuilder.append(",firstDataTime=").append(this.firstDataTime);
        sumBuilder.append(",recDataTime=").append(this.recDataTime);
        sumBuilder.append(",sendWaitTime=").append(this.sendWaitTime);
        sumBuilder.append(",serverRT=").append(this.serverRT);
        sumBuilder.append(",sendSize=").append(this.sendSize);
        sumBuilder.append(",recvSize=").append(this.recvSize);
        sumBuilder.append(",dataSpeed=").append(this.dataSpeed);
        sumBuilder.append(",retryTimes=").append(this.retryTimes);
        return sumBuilder.toString();
    }

    public String toString() {
        if (StringUtils.isBlank(this.netStatSum)) {
            this.netStatSum = sumNetStat();
        }
        StringBuilder str = new StringBuilder(128);
        str.append("NetworkStats [").append(this.netStatSum);
        str.append("]");
        return str.toString();
    }
}
