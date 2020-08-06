package anet.channel.strategy;

import anet.channel.strategy.StrategyResultParser;
import anet.channel.util.ALog;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

class StrategyList implements Serializable {
    private static final String TAG = "awcn.StrategyList";
    private static final long serialVersionUID = -258058881561327174L;
    private boolean containsStaticIp = false;
    private transient Comparator<IPConnStrategy> defaultComparator = null;
    /* access modifiers changed from: private */
    public Map<Integer, ConnHistoryItem> historyItemMap = new HashMap();
    private List<IPConnStrategy> strategyList = new ArrayList();

    private interface Predicate<T> {
        boolean apply(T t);
    }

    public StrategyList() {
    }

    StrategyList(List<IPConnStrategy> list) {
        this.strategyList = list;
    }

    public void checkInit() {
        if (this.strategyList == null) {
            this.strategyList = new ArrayList();
        }
        if (this.historyItemMap == null) {
            this.historyItemMap = new HashMap();
        }
        Iterator<Map.Entry<Integer, ConnHistoryItem>> itr = this.historyItemMap.entrySet().iterator();
        while (itr.hasNext()) {
            if (((ConnHistoryItem) itr.next().getValue()).isExpire()) {
                itr.remove();
            }
        }
        for (IPConnStrategy ipStrategy : this.strategyList) {
            if (!this.historyItemMap.containsKey(Integer.valueOf(ipStrategy.getUniqueId()))) {
                this.historyItemMap.put(Integer.valueOf(ipStrategy.getUniqueId()), new ConnHistoryItem());
            }
        }
        Collections.sort(this.strategyList, getDefaultComparator());
    }

    public String toString() {
        return this.strategyList.toString();
    }

    public List<IConnStrategy> getStrategyList() {
        if (this.strategyList.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<IConnStrategy> ret = null;
        for (IPConnStrategy ipConnStrategy : this.strategyList) {
            if (this.historyItemMap.get(Integer.valueOf(ipConnStrategy.getUniqueId())).shouldBan()) {
                ALog.i(TAG, "strategy ban!", (String) null, "strategy", ipConnStrategy);
            } else {
                if (ret == null) {
                    ret = new ArrayList<>();
                }
                ret.add(ipConnStrategy);
            }
        }
        return ret == null ? Collections.EMPTY_LIST : ret;
    }

    public void update(StrategyResultParser.DnsInfo dnsInfo) {
        for (IPConnStrategy ipConnStrategy : this.strategyList) {
            ipConnStrategy.isToRemove = true;
        }
        for (int i = 0; i < dnsInfo.aisleses.length; i++) {
            for (String handleUpdate : dnsInfo.ips) {
                handleUpdate(handleUpdate, 1, dnsInfo.aisleses[i]);
            }
            if (dnsInfo.sips != null) {
                this.containsStaticIp = true;
                for (String handleUpdate2 : dnsInfo.sips) {
                    handleUpdate(handleUpdate2, 0, dnsInfo.aisleses[i]);
                }
            } else {
                this.containsStaticIp = false;
            }
        }
        ListIterator<IPConnStrategy> it = this.strategyList.listIterator();
        while (it.hasNext()) {
            if (it.next().isToRemove) {
                it.remove();
            }
        }
        Collections.sort(this.strategyList, getDefaultComparator());
    }

    private void handleUpdate(final String ip, int ipType, final StrategyResultParser.Aisles aisles) {
        final ConnProtocol protocol = ConnProtocol.valueOf(aisles);
        int pos = find(this.strategyList, new Predicate<IPConnStrategy>() {
            public boolean apply(IPConnStrategy t) {
                return t.getPort() == aisles.port && t.getIp().equals(ip) && t.protocol.equals(protocol);
            }
        });
        if (pos != -1) {
            IPConnStrategy ips = this.strategyList.get(pos);
            ips.cto = aisles.cto;
            ips.rto = aisles.rto;
            ips.heartbeat = aisles.heartbeat;
            ips.ipType = ipType;
            ips.ipSource = 0;
            ips.isToRemove = false;
            return;
        }
        IPConnStrategy ips2 = IPConnStrategy.create(ip, aisles);
        if (ips2 != null) {
            ips2.ipType = ipType;
            ips2.ipSource = 0;
            if (!this.historyItemMap.containsKey(Integer.valueOf(ips2.getUniqueId()))) {
                this.historyItemMap.put(Integer.valueOf(ips2.getUniqueId()), new ConnHistoryItem());
            }
            this.strategyList.add(ips2);
        }
    }

    public boolean shouldRefresh() {
        for (IPConnStrategy ipConnStrategy : this.strategyList) {
            if ((!this.containsStaticIp || ipConnStrategy.ipType == 0) && !this.historyItemMap.get(Integer.valueOf(ipConnStrategy.getUniqueId())).latestFail()) {
                return false;
            }
        }
        return true;
    }

    public void notifyConnEvent(IConnStrategy connStrategy, ConnEvent connEvent) {
        if ((connStrategy instanceof IPConnStrategy) && this.strategyList.indexOf(connStrategy) != -1) {
            this.historyItemMap.get(Integer.valueOf(((IPConnStrategy) connStrategy).getUniqueId())).update(connEvent.isSuccess);
            Collections.sort(this.strategyList, this.defaultComparator);
        }
    }

    private Comparator getDefaultComparator() {
        if (this.defaultComparator == null) {
            this.defaultComparator = new Comparator<IPConnStrategy>() {
                public int compare(IPConnStrategy t1, IPConnStrategy t2) {
                    int c1 = ((ConnHistoryItem) StrategyList.this.historyItemMap.get(Integer.valueOf(t1.getUniqueId()))).countFail();
                    int c2 = ((ConnHistoryItem) StrategyList.this.historyItemMap.get(Integer.valueOf(t2.getUniqueId()))).countFail();
                    int p1 = c1 + t1.ipType;
                    int p2 = c2 + t2.ipType;
                    if (p1 != p2) {
                        return p1 - p2;
                    }
                    if (c1 != c2) {
                        return c1 - c2;
                    }
                    return t1.protocol.isHttp - t2.protocol.isHttp;
                }
            };
        }
        return this.defaultComparator;
    }

    private static <T> int find(Collection<T> collection, Predicate<T> predicate) {
        if (collection == null) {
            return -1;
        }
        int pos = 0;
        Iterator i$ = collection.iterator();
        while (i$.hasNext() && !predicate.apply(i$.next())) {
            pos++;
        }
        if (pos == collection.size()) {
            pos = -1;
        }
        return pos;
    }
}
