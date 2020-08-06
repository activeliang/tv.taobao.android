package anet.channel.strategy;

import android.text.TextUtils;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.dispatch.AmdcRuntimeInfo;
import anet.channel.strategy.dispatch.HttpDispatcher;
import anet.channel.strategy.utils.SerialLruCache;
import anet.channel.util.ALog;
import anet.channel.util.AppLifecycle;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import mtopsdk.common.util.SymbolExpUtil;

class StrategyTable implements Serializable {
    private static final int COLD_HOST_CAPABILITY = 40;
    private static final long FRESH_TTL = 30000;
    private static final int HOT_HOST_CAPABILITY = 40;
    private static final String TAG = "awcn.StrategyTable";
    private static final long serialVersionUID = 6044722613437834958L;
    protected volatile String clientIp;
    private transient Map<String, StrategyCollection> coldStrategyMap;
    private volatile transient int configVersion;
    private HotHostLruCache hotStrategyMap;
    protected transient boolean isChanged = false;
    private Set<String> successIpSet;
    protected String uniqueId;

    private static class HotHostLruCache extends SerialLruCache<String, StrategyCollection> {
        private static final long serialVersionUID = -4001655685948369525L;

        public HotHostLruCache(int cacheSize) {
            super(cacheSize);
        }

        /* access modifiers changed from: protected */
        public boolean entryRemoved(Map.Entry<String, StrategyCollection> eldest) {
            if (!eldest.getValue().isFixed) {
                return true;
            }
            Iterator<Map.Entry<String, StrategyCollection>> iterator = entrySet().iterator();
            while (true) {
                if (iterator.hasNext()) {
                    if (!((StrategyCollection) iterator.next().getValue()).isFixed) {
                        iterator.remove();
                        break;
                    }
                } else {
                    break;
                }
            }
            return false;
        }
    }

    protected StrategyTable(String uniqueId2) {
        this.uniqueId = uniqueId2;
        checkInit();
    }

    private void initStrategy() {
        if (HttpDispatcher.getInstance().isInitHostsChanged(this.uniqueId)) {
            for (String host : HttpDispatcher.getInstance().getInitHosts()) {
                this.hotStrategyMap.put(host, new StrategyCollection(host));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void checkInit() {
        if (this.hotStrategyMap == null) {
            this.hotStrategyMap = new HotHostLruCache(40);
            initStrategy();
        }
        for (StrategyCollection sc : this.hotStrategyMap.values()) {
            sc.checkInit();
        }
        if (this.coldStrategyMap == null) {
            this.coldStrategyMap = new SerialLruCache(40);
        }
        if (this.successIpSet == null) {
            this.successIpSet = new TreeSet();
        }
        this.configVersion = GlobalAppRuntimeInfo.isTargetProcess() ? 0 : -1;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0053, code lost:
        if (r3 == false) goto L_0x002e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0055, code lost:
        sendAmdcRequest(r7, false);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<anet.channel.strategy.IConnStrategy> queryByHost(java.lang.String r7) {
        /*
            r6 = this;
            boolean r4 = android.text.TextUtils.isEmpty(r7)
            if (r4 != 0) goto L_0x000c
            boolean r4 = anet.channel.strategy.utils.Utils.checkHostValidAndNotIp(r7)
            if (r4 != 0) goto L_0x000f
        L_0x000c:
            java.util.List r4 = java.util.Collections.EMPTY_LIST
        L_0x000e:
            return r4
        L_0x000f:
            r6.checkInitHost()
            r1 = 0
            anet.channel.strategy.StrategyTable$HotHostLruCache r5 = r6.hotStrategyMap
            monitor-enter(r5)
            anet.channel.strategy.StrategyTable$HotHostLruCache r4 = r6.hotStrategyMap     // Catch:{ all -> 0x0033 }
            java.lang.Object r4 = r4.get(r7)     // Catch:{ all -> 0x0033 }
            r0 = r4
            anet.channel.strategy.StrategyCollection r0 = (anet.channel.strategy.StrategyCollection) r0     // Catch:{ all -> 0x0033 }
            r1 = r0
            monitor-exit(r5)     // Catch:{ all -> 0x0033 }
            if (r1 == 0) goto L_0x0036
            boolean r4 = r1.isExpired()
            if (r4 == 0) goto L_0x002e
            anet.channel.strategy.StrategyTable$HotHostLruCache r4 = r6.hotStrategyMap
            r6.sendAmdcRequest(r4)
        L_0x002e:
            java.util.List r4 = r1.queryStrategyList()
            goto L_0x000e
        L_0x0033:
            r4 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0033 }
            throw r4
        L_0x0036:
            r3 = 0
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyCollection> r5 = r6.coldStrategyMap
            monitor-enter(r5)
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyCollection> r4 = r6.coldStrategyMap     // Catch:{ all -> 0x005a }
            java.lang.Object r4 = r4.get(r7)     // Catch:{ all -> 0x005a }
            r0 = r4
            anet.channel.strategy.StrategyCollection r0 = (anet.channel.strategy.StrategyCollection) r0     // Catch:{ all -> 0x005a }
            r1 = r0
            if (r1 != 0) goto L_0x0052
            anet.channel.strategy.StrategyCollection r2 = new anet.channel.strategy.StrategyCollection     // Catch:{ all -> 0x005a }
            r2.<init>(r7)     // Catch:{ all -> 0x005a }
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyCollection> r4 = r6.coldStrategyMap     // Catch:{ all -> 0x005d }
            r4.put(r7, r2)     // Catch:{ all -> 0x005d }
            r3 = 1
            r1 = r2
        L_0x0052:
            monitor-exit(r5)     // Catch:{ all -> 0x005a }
            if (r3 == 0) goto L_0x002e
            r4 = 0
            r6.sendAmdcRequest(r7, r4)
            goto L_0x002e
        L_0x005a:
            r4 = move-exception
        L_0x005b:
            monitor-exit(r5)     // Catch:{ all -> 0x005a }
            throw r4
        L_0x005d:
            r4 = move-exception
            r1 = r2
            goto L_0x005b
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.strategy.StrategyTable.queryByHost(java.lang.String):java.util.List");
    }

    public String getCnameByHost(String host) {
        StrategyCollection sc;
        String str;
        if (TextUtils.isEmpty(host)) {
            return null;
        }
        synchronized (this.hotStrategyMap) {
            sc = (StrategyCollection) this.hotStrategyMap.get(host);
        }
        if (sc == null) {
            synchronized (this.coldStrategyMap) {
                sc = this.coldStrategyMap.get(host);
            }
        } else if (sc.isExpired()) {
            sendAmdcRequest(this.hotStrategyMap);
        }
        if (sc != null) {
            str = sc.cname;
        } else {
            str = null;
        }
        return str;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void update(anet.channel.strategy.StrategyResultParser.HttpDnsResponse r15) {
        /*
            r14 = this;
            java.lang.String r8 = "awcn.StrategyTable"
            java.lang.String r9 = "update strategyTable with httpDns response"
            r10 = 0
            r11 = 0
            java.lang.Object[] r11 = new java.lang.Object[r11]
            anet.channel.util.ALog.i(r8, r9, r10, r11)
            java.lang.String r8 = r15.clientIp     // Catch:{ Throwable -> 0x004b }
            r14.clientIp = r8     // Catch:{ Throwable -> 0x004b }
            int r8 = r15.configVersion     // Catch:{ Throwable -> 0x004b }
            r14.configVersion = r8     // Catch:{ Throwable -> 0x004b }
            anet.channel.strategy.StrategyResultParser$DnsInfo[] r1 = r15.dnsInfo     // Catch:{ Throwable -> 0x004b }
            if (r1 != 0) goto L_0x001a
        L_0x0019:
            return
        L_0x001a:
            anet.channel.strategy.StrategyTable$HotHostLruCache r9 = r14.hotStrategyMap     // Catch:{ Throwable -> 0x004b }
            monitor-enter(r9)     // Catch:{ Throwable -> 0x004b }
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyCollection> r10 = r14.coldStrategyMap     // Catch:{ all -> 0x0048 }
            monitor-enter(r10)     // Catch:{ all -> 0x0048 }
            r4 = 0
        L_0x0021:
            int r8 = r1.length     // Catch:{ all -> 0x0045 }
            if (r4 >= r8) goto L_0x012c
            r0 = r1[r4]     // Catch:{ all -> 0x0045 }
            if (r0 == 0) goto L_0x002c
            java.lang.String r8 = r0.host     // Catch:{ all -> 0x0045 }
            if (r8 != 0) goto L_0x002f
        L_0x002c:
            int r4 = r4 + 1
            goto L_0x0021
        L_0x002f:
            boolean r8 = r0.clear     // Catch:{ all -> 0x0045 }
            if (r8 == 0) goto L_0x00cf
            anet.channel.strategy.StrategyTable$HotHostLruCache r8 = r14.hotStrategyMap     // Catch:{ all -> 0x0045 }
            java.lang.String r11 = r0.host     // Catch:{ all -> 0x0045 }
            java.lang.Object r8 = r8.remove(r11)     // Catch:{ all -> 0x0045 }
            if (r8 != 0) goto L_0x002c
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyCollection> r8 = r14.coldStrategyMap     // Catch:{ all -> 0x0045 }
            java.lang.String r11 = r0.host     // Catch:{ all -> 0x0045 }
            r8.remove(r11)     // Catch:{ all -> 0x0045 }
            goto L_0x002c
        L_0x0045:
            r8 = move-exception
            monitor-exit(r10)     // Catch:{ all -> 0x0045 }
            throw r8     // Catch:{ all -> 0x0048 }
        L_0x0048:
            r8 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x0048 }
            throw r8     // Catch:{ Throwable -> 0x004b }
        L_0x004b:
            r2 = move-exception
            java.lang.String r8 = "awcn.StrategyTable"
            java.lang.String r9 = "fail to update strategyTable"
            r10 = 0
            r11 = 0
            java.lang.Object[] r11 = new java.lang.Object[r11]
            anet.channel.util.ALog.e(r8, r9, r10, r2, r11)
        L_0x0059:
            r8 = 1
            boolean r8 = anet.channel.util.ALog.isPrintLog(r8)
            if (r8 == 0) goto L_0x0019
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r9 = "uniqueId : "
            r8.<init>(r9)
            java.lang.String r9 = r14.uniqueId
            java.lang.StringBuilder r6 = r8.append(r9)
            java.lang.String r8 = "\n-------------------------hot domains:------------------------------------"
            r6.append(r8)
            java.lang.String r8 = "awcn.StrategyTable"
            java.lang.String r9 = r6.toString()
            r10 = 0
            r11 = 0
            java.lang.Object[] r11 = new java.lang.Object[r11]
            anet.channel.util.ALog.d(r8, r9, r10, r11)
            anet.channel.strategy.StrategyTable$HotHostLruCache r9 = r14.hotStrategyMap
            monitor-enter(r9)
            anet.channel.strategy.StrategyTable$HotHostLruCache r8 = r14.hotStrategyMap     // Catch:{ all -> 0x00cc }
            java.util.Set r8 = r8.entrySet()     // Catch:{ all -> 0x00cc }
            java.util.Iterator r5 = r8.iterator()     // Catch:{ all -> 0x00cc }
        L_0x008f:
            boolean r8 = r5.hasNext()     // Catch:{ all -> 0x00cc }
            if (r8 == 0) goto L_0x0130
            java.lang.Object r3 = r5.next()     // Catch:{ all -> 0x00cc }
            java.util.Map$Entry r3 = (java.util.Map.Entry) r3     // Catch:{ all -> 0x00cc }
            r8 = 0
            r6.setLength(r8)     // Catch:{ all -> 0x00cc }
            java.lang.Object r8 = r3.getKey()     // Catch:{ all -> 0x00cc }
            java.lang.String r8 = (java.lang.String) r8     // Catch:{ all -> 0x00cc }
            java.lang.StringBuilder r8 = r6.append(r8)     // Catch:{ all -> 0x00cc }
            java.lang.String r10 = " = "
            java.lang.StringBuilder r10 = r8.append(r10)     // Catch:{ all -> 0x00cc }
            java.lang.Object r8 = r3.getValue()     // Catch:{ all -> 0x00cc }
            anet.channel.strategy.StrategyCollection r8 = (anet.channel.strategy.StrategyCollection) r8     // Catch:{ all -> 0x00cc }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x00cc }
            r10.append(r8)     // Catch:{ all -> 0x00cc }
            java.lang.String r8 = "awcn.StrategyTable"
            java.lang.String r10 = r6.toString()     // Catch:{ all -> 0x00cc }
            r11 = 0
            r12 = 0
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x00cc }
            anet.channel.util.ALog.d(r8, r10, r11, r12)     // Catch:{ all -> 0x00cc }
            goto L_0x008f
        L_0x00cc:
            r8 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x00cc }
            throw r8
        L_0x00cf:
            anet.channel.strategy.StrategyTable$HotHostLruCache r8 = r14.hotStrategyMap     // Catch:{ all -> 0x0045 }
            java.lang.String r11 = r0.host     // Catch:{ all -> 0x0045 }
            java.lang.Object r7 = r8.get(r11)     // Catch:{ all -> 0x0045 }
            anet.channel.strategy.StrategyCollection r7 = (anet.channel.strategy.StrategyCollection) r7     // Catch:{ all -> 0x0045 }
            if (r7 == 0) goto L_0x00f4
            int r8 = r0.isHot     // Catch:{ all -> 0x0045 }
            r11 = 1
            if (r8 == r11) goto L_0x00ef
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyCollection> r8 = r14.coldStrategyMap     // Catch:{ all -> 0x0045 }
            java.lang.String r11 = r0.host     // Catch:{ all -> 0x0045 }
            anet.channel.strategy.StrategyTable$HotHostLruCache r12 = r14.hotStrategyMap     // Catch:{ all -> 0x0045 }
            java.lang.String r13 = r0.host     // Catch:{ all -> 0x0045 }
            java.lang.Object r12 = r12.remove(r13)     // Catch:{ all -> 0x0045 }
            r8.put(r11, r12)     // Catch:{ all -> 0x0045 }
        L_0x00ef:
            r7.update(r0)     // Catch:{ all -> 0x0045 }
            goto L_0x002c
        L_0x00f4:
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyCollection> r8 = r14.coldStrategyMap     // Catch:{ all -> 0x0045 }
            java.lang.String r11 = r0.host     // Catch:{ all -> 0x0045 }
            java.lang.Object r7 = r8.get(r11)     // Catch:{ all -> 0x0045 }
            anet.channel.strategy.StrategyCollection r7 = (anet.channel.strategy.StrategyCollection) r7     // Catch:{ all -> 0x0045 }
            if (r7 == 0) goto L_0x0115
            int r8 = r0.isHot     // Catch:{ all -> 0x0045 }
            r11 = 1
            if (r8 != r11) goto L_0x00ef
            anet.channel.strategy.StrategyTable$HotHostLruCache r8 = r14.hotStrategyMap     // Catch:{ all -> 0x0045 }
            java.lang.String r11 = r0.host     // Catch:{ all -> 0x0045 }
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyCollection> r12 = r14.coldStrategyMap     // Catch:{ all -> 0x0045 }
            java.lang.String r13 = r0.host     // Catch:{ all -> 0x0045 }
            java.lang.Object r12 = r12.remove(r13)     // Catch:{ all -> 0x0045 }
            r8.put(r11, r12)     // Catch:{ all -> 0x0045 }
            goto L_0x00ef
        L_0x0115:
            anet.channel.strategy.StrategyCollection r7 = new anet.channel.strategy.StrategyCollection     // Catch:{ all -> 0x0045 }
            java.lang.String r8 = r0.host     // Catch:{ all -> 0x0045 }
            r7.<init>(r8)     // Catch:{ all -> 0x0045 }
            int r8 = r0.isHot     // Catch:{ all -> 0x0045 }
            r11 = 1
            if (r8 != r11) goto L_0x0129
            anet.channel.strategy.StrategyTable$HotHostLruCache r8 = r14.hotStrategyMap     // Catch:{ all -> 0x0045 }
        L_0x0123:
            java.lang.String r11 = r0.host     // Catch:{ all -> 0x0045 }
            r8.put(r11, r7)     // Catch:{ all -> 0x0045 }
            goto L_0x00ef
        L_0x0129:
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyCollection> r8 = r14.coldStrategyMap     // Catch:{ all -> 0x0045 }
            goto L_0x0123
        L_0x012c:
            monitor-exit(r10)     // Catch:{ all -> 0x0045 }
            monitor-exit(r9)     // Catch:{ all -> 0x0048 }
            goto L_0x0059
        L_0x0130:
            monitor-exit(r9)     // Catch:{ all -> 0x00cc }
            r8 = 0
            r6.setLength(r8)
            java.lang.String r8 = "\n-------------------------cold domains:------------------------------------"
            r6.append(r8)
            java.lang.String r8 = "awcn.StrategyTable"
            java.lang.String r9 = r6.toString()
            r10 = 0
            r11 = 0
            java.lang.Object[] r11 = new java.lang.Object[r11]
            anet.channel.util.ALog.d(r8, r9, r10, r11)
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyCollection> r9 = r14.coldStrategyMap
            monitor-enter(r9)
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyCollection> r8 = r14.coldStrategyMap     // Catch:{ all -> 0x0193 }
            java.util.Set r8 = r8.entrySet()     // Catch:{ all -> 0x0193 }
            java.util.Iterator r5 = r8.iterator()     // Catch:{ all -> 0x0193 }
        L_0x0156:
            boolean r8 = r5.hasNext()     // Catch:{ all -> 0x0193 }
            if (r8 == 0) goto L_0x0196
            java.lang.Object r3 = r5.next()     // Catch:{ all -> 0x0193 }
            java.util.Map$Entry r3 = (java.util.Map.Entry) r3     // Catch:{ all -> 0x0193 }
            r8 = 0
            r6.setLength(r8)     // Catch:{ all -> 0x0193 }
            java.lang.Object r8 = r3.getKey()     // Catch:{ all -> 0x0193 }
            java.lang.String r8 = (java.lang.String) r8     // Catch:{ all -> 0x0193 }
            java.lang.StringBuilder r8 = r6.append(r8)     // Catch:{ all -> 0x0193 }
            java.lang.String r10 = " = "
            java.lang.StringBuilder r10 = r8.append(r10)     // Catch:{ all -> 0x0193 }
            java.lang.Object r8 = r3.getValue()     // Catch:{ all -> 0x0193 }
            anet.channel.strategy.StrategyCollection r8 = (anet.channel.strategy.StrategyCollection) r8     // Catch:{ all -> 0x0193 }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x0193 }
            r10.append(r8)     // Catch:{ all -> 0x0193 }
            java.lang.String r8 = "awcn.StrategyTable"
            java.lang.String r10 = r6.toString()     // Catch:{ all -> 0x0193 }
            r11 = 0
            r12 = 0
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x0193 }
            anet.channel.util.ALog.d(r8, r10, r11, r12)     // Catch:{ all -> 0x0193 }
            goto L_0x0156
        L_0x0193:
            r8 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x0193 }
            throw r8
        L_0x0196:
            monitor-exit(r9)     // Catch:{ all -> 0x0193 }
            goto L_0x0019
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.strategy.StrategyTable.update(anet.channel.strategy.StrategyResultParser$HttpDnsResponse):void");
    }

    private Set<String> getNeedUpdateHost(Map<String, StrategyCollection> map) {
        Set<String> hostToUpdate = new HashSet<>();
        long currTime = System.currentTimeMillis();
        boolean collectAll = map == this.hotStrategyMap;
        for (StrategyCollection sc : map.values()) {
            if (collectAll || currTime >= sc.ttl) {
                hostToUpdate.add(sc.getHostWithEtag());
                sc.ttl = FRESH_TTL + currTime;
            }
        }
        return hostToUpdate;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0085, code lost:
        if (r4.isExpired() != false) goto L_0x0087;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void sendAmdcRequest(java.lang.String r11, boolean r12) {
        /*
            r10 = this;
            boolean r6 = anet.channel.GlobalAppRuntimeInfo.isAppBackground()
            if (r6 == 0) goto L_0x000e
            long r6 = anet.channel.util.AppLifecycle.lastEnterBackgroundTime
            r8 = 0
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 > 0) goto L_0x001a
        L_0x000e:
            boolean r6 = android.text.TextUtils.isEmpty(r11)
            if (r6 != 0) goto L_0x001a
            boolean r6 = anet.channel.status.NetworkStatusHelper.isConnected()
            if (r6 != 0) goto L_0x001b
        L_0x001a:
            return
        L_0x001b:
            int r3 = anet.channel.strategy.dispatch.AmdcRuntimeInfo.getAmdcLimitLevel()
            r6 = 3
            if (r3 == r6) goto L_0x001a
            java.util.Set r2 = java.util.Collections.EMPTY_SET
            r6 = 2
            if (r3 != r6) goto L_0x003d
            java.util.HashSet r2 = new java.util.HashSet
            r2.<init>()
            r2.add(r11)
        L_0x002f:
            anet.channel.strategy.dispatch.HttpDispatcher r6 = anet.channel.strategy.dispatch.HttpDispatcher.getInstance()
            java.lang.String r7 = r10.buildPreIpString()
            int r8 = r10.configVersion
            r6.sendAmdcRequest(r2, r7, r8)
            goto L_0x001a
        L_0x003d:
            r4 = 0
            anet.channel.strategy.StrategyTable$HotHostLruCache r7 = r10.hotStrategyMap
            monitor-enter(r7)
            anet.channel.strategy.StrategyTable$HotHostLruCache r6 = r10.hotStrategyMap     // Catch:{ all -> 0x0099 }
            java.lang.Object r6 = r6.get(r11)     // Catch:{ all -> 0x0099 }
            r0 = r6
            anet.channel.strategy.StrategyCollection r0 = (anet.channel.strategy.StrategyCollection) r0     // Catch:{ all -> 0x0099 }
            r4 = r0
            if (r4 == 0) goto L_0x0062
            if (r12 != 0) goto L_0x0055
            boolean r6 = r4.isExpired()     // Catch:{ all -> 0x0099 }
            if (r6 == 0) goto L_0x0062
        L_0x0055:
            anet.channel.strategy.StrategyTable$HotHostLruCache r6 = r10.hotStrategyMap     // Catch:{ all -> 0x0099 }
            java.util.Set r2 = r10.getNeedUpdateHost(r6)     // Catch:{ all -> 0x0099 }
            java.lang.String r6 = r4.getHostWithEtag()     // Catch:{ all -> 0x0099 }
            r2.add(r6)     // Catch:{ all -> 0x0099 }
        L_0x0062:
            monitor-exit(r7)     // Catch:{ all -> 0x0099 }
            if (r4 != 0) goto L_0x002f
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyCollection> r7 = r10.coldStrategyMap
            monitor-enter(r7)
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyCollection> r6 = r10.coldStrategyMap     // Catch:{ all -> 0x0096 }
            java.lang.Object r6 = r6.get(r11)     // Catch:{ all -> 0x0096 }
            r0 = r6
            anet.channel.strategy.StrategyCollection r0 = (anet.channel.strategy.StrategyCollection) r0     // Catch:{ all -> 0x0096 }
            r4 = r0
            if (r4 != 0) goto L_0x007f
            anet.channel.strategy.StrategyCollection r5 = new anet.channel.strategy.StrategyCollection     // Catch:{ all -> 0x0096 }
            r5.<init>(r11)     // Catch:{ all -> 0x0096 }
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyCollection> r6 = r10.coldStrategyMap     // Catch:{ all -> 0x009c }
            r6.put(r11, r5)     // Catch:{ all -> 0x009c }
            r4 = r5
        L_0x007f:
            if (r12 != 0) goto L_0x0087
            boolean r6 = r4.isExpired()     // Catch:{ all -> 0x0096 }
            if (r6 == 0) goto L_0x0094
        L_0x0087:
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyCollection> r6 = r10.coldStrategyMap     // Catch:{ all -> 0x0096 }
            java.util.Set r2 = r10.getNeedUpdateHost(r6)     // Catch:{ all -> 0x0096 }
            java.lang.String r6 = r4.getHostWithEtag()     // Catch:{ all -> 0x0096 }
            r2.add(r6)     // Catch:{ all -> 0x0096 }
        L_0x0094:
            monitor-exit(r7)     // Catch:{ all -> 0x0096 }
            goto L_0x002f
        L_0x0096:
            r6 = move-exception
        L_0x0097:
            monitor-exit(r7)     // Catch:{ all -> 0x0096 }
            throw r6
        L_0x0099:
            r6 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x0099 }
            throw r6
        L_0x009c:
            r6 = move-exception
            r4 = r5
            goto L_0x0097
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.strategy.StrategyTable.sendAmdcRequest(java.lang.String, boolean):void");
    }

    /* access modifiers changed from: protected */
    public void sendAmdcRequest(Map<String, StrategyCollection> map) {
        Set<String> hostSet;
        if ((!GlobalAppRuntimeInfo.isAppBackground() || AppLifecycle.lastEnterBackgroundTime <= 0) && AmdcRuntimeInfo.getAmdcLimitLevel() <= 0 && NetworkStatusHelper.isConnected()) {
            synchronized (map) {
                hostSet = getNeedUpdateHost(map);
            }
            if (!hostSet.isEmpty()) {
                HttpDispatcher.getInstance().sendAmdcRequest(hostSet, buildPreIpString(), this.configVersion);
            }
        }
    }

    private void checkInitHost() {
        try {
            if (HttpDispatcher.getInstance().isInitHostsChanged(this.uniqueId)) {
                boolean bSend = false;
                synchronized (this.hotStrategyMap) {
                    synchronized (this.coldStrategyMap) {
                        for (String host : HttpDispatcher.getInstance().getInitHosts()) {
                            if (!this.hotStrategyMap.containsKey(host) && !this.coldStrategyMap.containsKey(host)) {
                                this.coldStrategyMap.put(host, new StrategyCollection(host));
                                bSend = true;
                            }
                        }
                    }
                }
                if (bSend) {
                    sendAmdcRequest(this.coldStrategyMap);
                }
            }
        } catch (Exception e) {
            ALog.e(TAG, "checkInitHost failed", (String) null, e, new Object[0]);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyConnEvent(String host, IConnStrategy connStrategy, ConnEvent connEvent) {
        StrategyCollection sc;
        if (ALog.isPrintLog(1)) {
            ALog.d(TAG, "[notifyConnEvent]", (String) null, "Host", host, "IConnStrategy", connStrategy, "ConnEvent", connEvent);
        }
        handlePreIp(connEvent, connStrategy.getIp());
        synchronized (this.hotStrategyMap) {
            synchronized (this.coldStrategyMap) {
                sc = (StrategyCollection) this.hotStrategyMap.get(host);
                if (sc == null) {
                    sc = this.coldStrategyMap.get(host);
                }
            }
        }
        if (sc != null) {
            sc.notifyConnEvent(connStrategy, connEvent);
        }
    }

    private void handlePreIp(ConnEvent connEvent, String ip) {
        if (!TextUtils.isEmpty(ip)) {
            synchronized (this.successIpSet) {
                if (connEvent.isSuccess) {
                    this.successIpSet.add(ip);
                } else {
                    this.successIpSet.remove(ip);
                }
            }
        }
    }

    private String buildPreIpString() {
        StringBuilder preIp = new StringBuilder();
        synchronized (this.successIpSet) {
            for (String ip : this.successIpSet) {
                preIp.append(ip).append(SymbolExpUtil.SYMBOL_SEMICOLON);
            }
        }
        if (preIp.length() > 0) {
            preIp.deleteCharAt(preIp.length() - 1);
        }
        return preIp.toString();
    }
}
