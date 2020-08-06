package anet.channel.strategy;

import android.text.TextUtils;
import anet.channel.strategy.StrategyResultParser;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ConnProtocol implements Serializable {
    public static final ConnProtocol HTTP = valueOf("http", (String) null, (String) null, false);
    public static final ConnProtocol HTTPS = valueOf("https", (String) null, (String) null, false);
    private static Map<String, ConnProtocol> protocolMap = new HashMap();
    private static final long serialVersionUID = -3523201990674557001L;
    final int isHttp;
    public final boolean l7;
    public final String name;
    public final String protocol;
    public final String publicKey;
    public final String rtt;

    public static ConnProtocol valueOf(StrategyResultParser.Aisles aisles) {
        if (aisles == null) {
            return null;
        }
        return valueOf(aisles.protocol, aisles.rtt, aisles.publicKey, aisles.l7Encrity);
    }

    public static ConnProtocol valueOf(String protocol2, String rtt2, String publicKey2, boolean l72) {
        if (TextUtils.isEmpty(protocol2)) {
            return null;
        }
        String name2 = buildName(protocol2, rtt2, publicKey2, l72);
        synchronized (protocolMap) {
            if (protocolMap.containsKey(name2)) {
                ConnProtocol connProtocol = protocolMap.get(name2);
                return connProtocol;
            }
            ConnProtocol connProtocol2 = new ConnProtocol(name2, protocol2, rtt2, publicKey2, l72);
            protocolMap.put(name2, connProtocol2);
            return connProtocol2;
        }
    }

    private ConnProtocol(String name2, String protocol2, String rtt2, String publicKey2, boolean l72) {
        this.name = name2;
        this.protocol = protocol2;
        this.rtt = rtt2;
        this.publicKey = publicKey2;
        this.l7 = l72;
        this.isHttp = ("http".equalsIgnoreCase(protocol2) || "https".equalsIgnoreCase(protocol2)) ? 1 : 0;
    }

    private static String buildName(String protocol2, String rtt2, String publicKey2, boolean l72) {
        if (TextUtils.isEmpty(publicKey2)) {
            return protocol2;
        }
        StringBuilder builder = new StringBuilder(18);
        builder.append(protocol2);
        if (!TextUtils.isEmpty(rtt2)) {
            builder.append("_").append(rtt2);
        } else {
            builder.append("_0rtt");
        }
        builder.append("_");
        builder.append(publicKey2);
        if (l72) {
            builder.append("_l7");
        }
        return builder.toString();
    }

    public String toString() {
        return this.name;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof ConnProtocol)) {
            return false;
        }
        return this.name.equals(((ConnProtocol) o).name);
    }

    public int hashCode() {
        int result = this.protocol.hashCode() + 527;
        if (this.rtt != null) {
            result = (result * 31) + this.rtt.hashCode();
        }
        if (this.publicKey != null) {
            result = (result * 31) + this.publicKey.hashCode();
        }
        return (result * 31) + (this.l7 ? 1 : 0);
    }
}
