package anet.channel.entity;

import android.text.TextUtils;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.strategy.ConnProtocol;
import java.util.HashMap;
import java.util.Map;

public class ConnType {
    public static ConnType HTTP = new ConnType("http");
    public static final String HTTP2 = "http2";
    public static ConnType HTTPS = new ConnType("https");
    public static final String PK_ACS = "acs";
    public static final String PK_CDN = "cdn";
    public static final String PK_OPEN = "open";
    public static final String RTT_0 = "0rtt";
    public static final String RTT_1 = "1rtt";
    public static final String SPDY = "spdy";
    private static Map<ConnProtocol, ConnType> connTypeMap = new HashMap();
    private String name = "";
    private String publicKey;
    private int spdyProtocol;

    public enum TypeLevel {
        SPDY,
        HTTP
    }

    private ConnType(String name2) {
        this.name = name2;
    }

    public static ConnType valueOf(ConnProtocol connProtocol) {
        if (connProtocol == null) {
            return null;
        }
        if ("http".equalsIgnoreCase(connProtocol.protocol)) {
            return HTTP;
        }
        if ("https".equalsIgnoreCase(connProtocol.protocol)) {
            return HTTPS;
        }
        synchronized (connTypeMap) {
            if (connTypeMap.containsKey(connProtocol)) {
                ConnType connType = connTypeMap.get(connProtocol);
                return connType;
            }
            ConnType connType2 = new ConnType(connProtocol.toString());
            connType2.publicKey = connProtocol.publicKey;
            if (HTTP2.equalsIgnoreCase(connProtocol.protocol)) {
                connType2.spdyProtocol |= 8;
            } else if (SPDY.equalsIgnoreCase(connProtocol.protocol)) {
                connType2.spdyProtocol |= 2;
            }
            if (connType2.spdyProtocol == 0) {
                return null;
            }
            if (!TextUtils.isEmpty(connProtocol.publicKey)) {
                connType2.spdyProtocol |= 128;
                if (RTT_1.equalsIgnoreCase(connProtocol.rtt)) {
                    connType2.spdyProtocol |= 8192;
                } else if (!RTT_0.equalsIgnoreCase(connProtocol.rtt)) {
                    return null;
                } else {
                    connType2.spdyProtocol |= 4096;
                }
                if (connProtocol.l7) {
                    connType2.spdyProtocol |= 16384;
                }
            }
            connTypeMap.put(connProtocol, connType2);
            return connType2;
        }
    }

    public int getTnetConType() {
        return this.spdyProtocol;
    }

    public int getTnetPublicKey(boolean secOff) {
        if (PK_CDN.equals(this.publicKey)) {
            return secOff ? 1 : 1;
        }
        if (GlobalAppRuntimeInfo.getEnv() == ENV.TEST) {
            return 0;
        }
        if (PK_OPEN.equals(this.publicKey)) {
            return secOff ? 11 : 10;
        }
        if (PK_ACS.equals(this.publicKey)) {
            return secOff ? 4 : 3;
        }
        return -1;
    }

    public boolean isHttpType() {
        return equals(HTTP) || equals(HTTPS);
    }

    public boolean isSSL() {
        return equals(HTTPS) || (this.spdyProtocol & 128) != 0;
    }

    public String toString() {
        return this.name;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof ConnType)) {
            return false;
        }
        if (this == o || this.name.equals(((ConnType) o).name)) {
            return true;
        }
        return false;
    }

    public TypeLevel getTypeLevel() {
        if (isHttpType()) {
            return TypeLevel.HTTP;
        }
        return TypeLevel.SPDY;
    }

    private int getPriority() {
        if (isHttpType()) {
            return 1;
        }
        if ((this.spdyProtocol & 8) == 0) {
            return 0;
        }
        return -1;
    }

    public static int compare(ConnType type1, ConnType type2) {
        return type1.getPriority() - type2.getPriority();
    }
}
