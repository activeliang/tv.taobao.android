package anet.channel.strategy;

import android.text.TextUtils;
import anet.channel.strategy.StrategyResultParser;
import java.io.Serializable;

class IPConnStrategy implements IConnStrategy, Serializable {
    public static final int SOURCE_AMDC = 0;
    public static final int SOURCE_CUSTOMIZED = 2;
    public static final int SOURCE_LOCAL_DNS = 1;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_STATIC_BANDWITDH = 0;
    private static final long serialVersionUID = -2492035520806142510L;
    public volatile int cto;
    public volatile int heartbeat;
    public final String ip;
    volatile int ipSource = 1;
    volatile int ipType = 1;
    transient boolean isToRemove;
    public final int port;
    public final ConnProtocol protocol;
    public volatile int retry;
    public volatile int rto;

    static IPConnStrategy create(String ip2, StrategyResultParser.Aisles aisles) {
        ConnProtocol protocol2 = ConnProtocol.valueOf(aisles);
        if (protocol2 == null) {
            return null;
        }
        return create(ip2, aisles.port, protocol2, aisles.cto, aisles.rto, aisles.retry, aisles.heartbeat);
    }

    static IPConnStrategy create(String ip2, int port2, ConnProtocol protocol2, int cto2, int rto2, int retry2, int heartbeat2) {
        if (TextUtils.isEmpty(ip2) || protocol2 == null || port2 <= 0) {
            return null;
        }
        return new IPConnStrategy(ip2, port2, protocol2, cto2, rto2, retry2, heartbeat2);
    }

    private IPConnStrategy(String ip2, int port2, ConnProtocol protocol2, int cto2, int rto2, int retry2, int heartbeat2) {
        this.ip = ip2;
        this.port = port2;
        this.protocol = protocol2;
        this.cto = cto2;
        this.rto = rto2;
        this.retry = retry2;
        this.heartbeat = heartbeat2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append('{').append(this.ip);
        if (this.ipType == 0) {
            sb.append("(*)");
        }
        sb.append(' ').append(this.port).append(' ').append(this.protocol).append('}');
        return sb.toString();
    }

    public String getIp() {
        return this.ip;
    }

    public int getIpType() {
        return this.ipType;
    }

    public int getIpSource() {
        return this.ipSource;
    }

    public int getPort() {
        return this.port;
    }

    public ConnProtocol getProtocol() {
        return this.protocol;
    }

    public int getConnectionTimeout() {
        return this.cto;
    }

    public int getReadTimeout() {
        return this.rto;
    }

    public int getRetryTimes() {
        return this.retry;
    }

    public int getHeartbeat() {
        return this.heartbeat;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof IPConnStrategy)) {
            return false;
        }
        IPConnStrategy ips = (IPConnStrategy) o;
        if (this.port != ips.port || !this.ip.equals(ips.ip) || !this.protocol.equals(ips.protocol)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return ((((this.ip.hashCode() + 527) * 31) + this.port) * 31) + this.protocol.hashCode();
    }

    public int getUniqueId() {
        return hashCode();
    }
}
