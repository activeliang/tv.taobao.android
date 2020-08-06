package anet.channel.strategy;

import anet.channel.strategy.dispatch.DispatchConstants;
import anet.channel.util.ALog;
import com.ali.user.open.tbauth.TbAuthConstants;
import mtopsdk.xstate.util.XStateConstants;
import org.json.JSONArray;
import org.json.JSONObject;

public class StrategyResultParser {
    public static HttpDnsResponse parse(JSONObject response) {
        try {
            return new HttpDnsResponse(response);
        } catch (Exception e) {
            ALog.e("StrategyResultParser", "Parse HttpDns response failed.", (String) null, e, "JSON Content", response.toString());
            return null;
        }
    }

    public static class Aisles {
        public final boolean auth;
        public final int cto;
        public final int heartbeat;
        public final boolean l7Encrity;
        public final int port;
        public final String protocol;
        public final String publicKey;
        public final int retry;
        public final int rto;
        public final String rtt;

        public Aisles(JSONObject jsonObject) {
            boolean z;
            boolean z2 = true;
            this.port = jsonObject.optInt("port");
            this.protocol = jsonObject.optString("protocol");
            this.cto = jsonObject.optInt("cto");
            this.rto = jsonObject.optInt("rto");
            this.retry = jsonObject.optInt("retry");
            this.heartbeat = jsonObject.optInt("heartbeat");
            this.rtt = jsonObject.optString("rtt", "");
            if (jsonObject.optInt("l7encript", 0) == 1) {
                z = true;
            } else {
                z = false;
            }
            this.l7Encrity = z;
            this.publicKey = jsonObject.optString("publickey");
            this.auth = jsonObject.optInt("auth", 0) != 1 ? false : z2;
        }

        public String toString() {
            return "{port=" + this.port + "protocol=" + this.protocol + "publickey=" + this.publicKey + "}";
        }
    }

    public static class DnsInfo {
        public final Aisles[] aisleses;
        public final boolean clear;
        public final String cname;
        public final boolean effectNow;
        public final String etag;
        public final String host;
        public final String[] ips;
        public final int isHot;
        public final boolean notModified;
        public final String safeAisles;
        public final String[] sips;
        public final int ttl;
        public final String unit;

        public DnsInfo(JSONObject jsonObject) {
            boolean z;
            boolean z2 = true;
            this.host = jsonObject.optString("host");
            this.ttl = jsonObject.optInt("ttl");
            this.safeAisles = jsonObject.optString("safeAisles");
            this.cname = jsonObject.optString("cname", (String) null);
            this.unit = jsonObject.optString("unit", (String) null);
            this.isHot = jsonObject.optInt("isHot");
            if (jsonObject.optInt("clear") == 1) {
                z = true;
            } else {
                z = false;
            }
            this.clear = z;
            this.etag = jsonObject.optString("etag");
            this.notModified = jsonObject.optInt("notModified") != 1 ? false : z2;
            this.effectNow = jsonObject.optBoolean("effectNow");
            JSONArray ipArray = jsonObject.optJSONArray("ips");
            if (ipArray != null) {
                int length = ipArray.length();
                this.ips = new String[length];
                for (int i = 0; i < length; i++) {
                    this.ips[i] = ipArray.optString(i);
                }
            } else {
                this.ips = null;
            }
            JSONArray sipArray = jsonObject.optJSONArray("sips");
            if (sipArray == null || sipArray.length() <= 0) {
                this.sips = null;
            } else {
                int length2 = sipArray.length();
                this.sips = new String[length2];
                for (int i2 = 0; i2 < length2; i2++) {
                    this.sips[i2] = sipArray.optString(i2);
                }
            }
            JSONArray aislesArray = jsonObject.optJSONArray("aisles");
            if (aislesArray != null) {
                int arrayLength = aislesArray.length();
                this.aisleses = new Aisles[arrayLength];
                for (int i3 = 0; i3 < arrayLength; i3++) {
                    this.aisleses[i3] = new Aisles(aislesArray.optJSONObject(i3));
                }
                return;
            }
            this.aisleses = null;
        }
    }

    public static class HttpDnsResponse {
        public final String clientIp;
        public final int configVersion;
        public final DnsInfo[] dnsInfo;
        public final int fcLevel;
        public final int fcTime;
        public final String userId;
        public final String utdid;

        public HttpDnsResponse(JSONObject jsonObject) {
            this.clientIp = jsonObject.optString(TbAuthConstants.IP);
            this.userId = jsonObject.optString(XStateConstants.KEY_UID, (String) null);
            this.utdid = jsonObject.optString("utdid", (String) null);
            this.configVersion = jsonObject.optInt(DispatchConstants.CONFIG_VERSION);
            this.fcLevel = jsonObject.optInt("fcl");
            this.fcTime = jsonObject.optInt("fct");
            JSONArray dnsArray = jsonObject.optJSONArray("dns");
            if (dnsArray != null) {
                int length = dnsArray.length();
                this.dnsInfo = new DnsInfo[length];
                for (int i = 0; i < length; i++) {
                    this.dnsInfo[i] = new DnsInfo(dnsArray.optJSONObject(i));
                }
                return;
            }
            this.dnsInfo = null;
        }
    }
}
