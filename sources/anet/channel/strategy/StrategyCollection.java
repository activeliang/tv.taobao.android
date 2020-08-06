package anet.channel.strategy;

import android.text.TextUtils;
import anet.channel.strategy.StrategyResultParser;
import anet.channel.strategy.dispatch.DispatchConstants;
import anet.channel.util.ALog;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

class StrategyCollection implements Serializable {
    private static final long serialVersionUID = 1454976454894208229L;
    volatile String cname = null;
    volatile String etag = null;
    String host;
    boolean isFixed = false;
    private transient long lastAmdcRequestSend = 0;
    StrategyList strategyList = null;
    volatile long ttl = 0;

    public StrategyCollection() {
    }

    protected StrategyCollection(String host2) {
        this.host = host2;
        this.isFixed = DispatchConstants.isAmdcServerDomain(host2);
    }

    public void checkInit() {
        if (this.strategyList != null) {
            this.strategyList.checkInit();
        }
    }

    public synchronized List<IConnStrategy> queryStrategyList() {
        List<IConnStrategy> strategyList2;
        if (this.strategyList == null) {
            strategyList2 = Collections.EMPTY_LIST;
        } else {
            strategyList2 = this.strategyList.getStrategyList();
        }
        return strategyList2;
    }

    public synchronized void notifyConnEvent(IConnStrategy connStrategy, ConnEvent connEvent) {
        if (this.strategyList != null) {
            this.strategyList.notifyConnEvent(connStrategy, connEvent);
            if (!connEvent.isSuccess && this.strategyList.shouldRefresh()) {
                long now = System.currentTimeMillis();
                if (now - this.lastAmdcRequestSend > 60000) {
                    StrategyCenter.getInstance().forceRefreshStrategy(this.host);
                    this.lastAmdcRequestSend = now;
                }
            }
        }
    }

    public String getHostWithEtag() {
        if (!TextUtils.isEmpty(this.etag)) {
            return this.host + ':' + this.etag;
        }
        return this.host;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > this.ttl;
    }

    public synchronized void update(StrategyResultParser.DnsInfo dnsInfo) {
        this.ttl = System.currentTimeMillis() + (((long) dnsInfo.ttl) * 1000);
        if (!dnsInfo.host.equalsIgnoreCase(this.host)) {
            ALog.e("StrategyCollection", "update error!", (String) null, "host", this.host, "dnsInfo.host", dnsInfo.host);
        } else if (!dnsInfo.notModified) {
            this.cname = dnsInfo.cname;
            this.etag = dnsInfo.etag;
            if (dnsInfo.ips == null || dnsInfo.ips.length == 0 || dnsInfo.aisleses == null || dnsInfo.aisleses.length == 0) {
                this.strategyList = null;
            } else {
                if (this.strategyList == null) {
                    this.strategyList = new StrategyList();
                }
                this.strategyList.update(dnsInfo);
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append("\nStrategyList = ").append(this.ttl);
        if (this.strategyList != null) {
            sb.append(this.strategyList.toString());
        } else if (this.cname != null) {
            sb.append('[').append(this.host).append("=>").append(this.cname).append(']');
        } else {
            sb.append("[]");
        }
        return sb.toString();
    }
}
