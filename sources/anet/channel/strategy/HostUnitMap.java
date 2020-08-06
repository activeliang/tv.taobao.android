package anet.channel.strategy;

import android.text.TextUtils;
import anet.channel.strategy.StrategyResultParser;
import anet.channel.util.ALog;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class HostUnitMap implements Serializable {
    private static final long serialVersionUID = -8816995878876377954L;
    private Map<String, String> unitMap = null;

    HostUnitMap() {
    }

    /* access modifiers changed from: package-private */
    public void checkInit() {
        if (this.unitMap == null) {
            this.unitMap = new ConcurrentHashMap();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isEmpty() {
        return this.unitMap.isEmpty();
    }

    /* access modifiers changed from: package-private */
    public void update(StrategyResultParser.HttpDnsResponse response) {
        if (response.dnsInfo != null) {
            for (StrategyResultParser.DnsInfo dnsInfo : response.dnsInfo) {
                if (!TextUtils.isEmpty(dnsInfo.unit)) {
                    this.unitMap.put(dnsInfo.host, dnsInfo.unit);
                } else {
                    this.unitMap.remove(dnsInfo.host);
                }
            }
            if (ALog.isPrintLog(1)) {
                ALog.d("awcn.HostUnitMap", toString(), (String) null, new Object[0]);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public String getUnitByHost(String host) {
        if (TextUtils.isEmpty(host)) {
            return null;
        }
        return this.unitMap.get(host);
    }

    public String toString() {
        String str;
        synchronized (this.unitMap) {
            str = "HostUnitMap: " + this.unitMap.toString();
        }
        return str;
    }
}
