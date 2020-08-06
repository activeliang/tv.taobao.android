package anet.channel.strategy;

import android.text.TextUtils;
import anet.channel.strategy.utils.SerialLruCache;
import anet.channel.strategy.utils.Utils;
import java.io.Serializable;

class SafeAislesMap implements Serializable {
    public static final String NO_RESULT = "No_Result";
    private static final long serialVersionUID = -7798500032935529499L;
    private transient StrategyInfoHolder holder = null;
    private SerialLruCache<String, String> schemeMap = null;

    SafeAislesMap() {
    }

    /* access modifiers changed from: package-private */
    public void setHolder(StrategyInfoHolder holder2) {
        this.holder = holder2;
    }

    /* access modifiers changed from: package-private */
    public void checkInit() {
        if (this.schemeMap == null) {
            this.schemeMap = new SerialLruCache<>(128);
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00b7, code lost:
        if (anet.channel.util.ALog.isPrintLog(1) == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00b9, code lost:
        anet.channel.util.ALog.d("awcn.SafeAislesMap", toString(), (java.lang.String) null, new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void update(anet.channel.strategy.StrategyResultParser.HttpDnsResponse r12) {
        /*
            r11 = this;
            anet.channel.strategy.StrategyResultParser$DnsInfo[] r7 = r12.dnsInfo
            if (r7 != 0) goto L_0x0005
        L_0x0004:
            return
        L_0x0005:
            anet.channel.strategy.utils.SerialLruCache<java.lang.String, java.lang.String> r8 = r11.schemeMap
            monitor-enter(r8)
            r0 = 0
            r5 = 0
            r1 = r0
        L_0x000b:
            anet.channel.strategy.StrategyResultParser$DnsInfo[] r7 = r12.dnsInfo     // Catch:{ all -> 0x00a1 }
            int r7 = r7.length     // Catch:{ all -> 0x00a1 }
            if (r5 >= r7) goto L_0x006d
            anet.channel.strategy.StrategyResultParser$DnsInfo[] r7 = r12.dnsInfo     // Catch:{ all -> 0x00a1 }
            r3 = r7[r5]     // Catch:{ all -> 0x00a1 }
            boolean r7 = r3.clear     // Catch:{ all -> 0x00a1 }
            if (r7 == 0) goto L_0x0024
            anet.channel.strategy.utils.SerialLruCache<java.lang.String, java.lang.String> r7 = r11.schemeMap     // Catch:{ all -> 0x00a1 }
            java.lang.String r9 = r3.host     // Catch:{ all -> 0x00a1 }
            r7.remove(r9)     // Catch:{ all -> 0x00a1 }
            r0 = r1
        L_0x0020:
            int r5 = r5 + 1
            r1 = r0
            goto L_0x000b
        L_0x0024:
            boolean r7 = r3.notModified     // Catch:{ all -> 0x00a1 }
            if (r7 == 0) goto L_0x002a
            r0 = r1
            goto L_0x0020
        L_0x002a:
            java.lang.String r7 = r3.cname     // Catch:{ all -> 0x00a1 }
            if (r7 == 0) goto L_0x0040
            if (r1 != 0) goto L_0x00c9
            java.util.TreeMap r0 = new java.util.TreeMap     // Catch:{ all -> 0x00a1 }
            r0.<init>()     // Catch:{ all -> 0x00a1 }
        L_0x0035:
            java.lang.String r7 = r3.host     // Catch:{ all -> 0x003d }
            java.lang.String r9 = r3.cname     // Catch:{ all -> 0x003d }
            r0.put(r7, r9)     // Catch:{ all -> 0x003d }
            goto L_0x0020
        L_0x003d:
            r7 = move-exception
        L_0x003e:
            monitor-exit(r8)     // Catch:{ all -> 0x003d }
            throw r7
        L_0x0040:
            java.lang.String r7 = "http"
            java.lang.String r9 = r3.safeAisles     // Catch:{ all -> 0x00a1 }
            boolean r7 = r7.equalsIgnoreCase(r9)     // Catch:{ all -> 0x00a1 }
            if (r7 != 0) goto L_0x0062
            java.lang.String r7 = "https"
            java.lang.String r9 = r3.safeAisles     // Catch:{ all -> 0x00a1 }
            boolean r7 = r7.equalsIgnoreCase(r9)     // Catch:{ all -> 0x00a1 }
            if (r7 != 0) goto L_0x0062
            anet.channel.strategy.utils.SerialLruCache<java.lang.String, java.lang.String> r7 = r11.schemeMap     // Catch:{ all -> 0x00a1 }
            java.lang.String r9 = r3.host     // Catch:{ all -> 0x00a1 }
            java.lang.String r10 = "No_Result"
            r7.put(r9, r10)     // Catch:{ all -> 0x00a1 }
            r0 = r1
            goto L_0x0020
        L_0x0062:
            anet.channel.strategy.utils.SerialLruCache<java.lang.String, java.lang.String> r7 = r11.schemeMap     // Catch:{ all -> 0x00a1 }
            java.lang.String r9 = r3.host     // Catch:{ all -> 0x00a1 }
            java.lang.String r10 = r3.safeAisles     // Catch:{ all -> 0x00a1 }
            r7.put(r9, r10)     // Catch:{ all -> 0x00a1 }
            r0 = r1
            goto L_0x0020
        L_0x006d:
            if (r1 == 0) goto L_0x00b1
            java.util.Set r7 = r1.entrySet()     // Catch:{ all -> 0x00a1 }
            java.util.Iterator r6 = r7.iterator()     // Catch:{ all -> 0x00a1 }
        L_0x0077:
            boolean r7 = r6.hasNext()     // Catch:{ all -> 0x00a1 }
            if (r7 == 0) goto L_0x00b1
            java.lang.Object r4 = r6.next()     // Catch:{ all -> 0x00a1 }
            java.util.Map$Entry r4 = (java.util.Map.Entry) r4     // Catch:{ all -> 0x00a1 }
            java.lang.Object r2 = r4.getValue()     // Catch:{ all -> 0x00a1 }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ all -> 0x00a1 }
            anet.channel.strategy.utils.SerialLruCache<java.lang.String, java.lang.String> r7 = r11.schemeMap     // Catch:{ all -> 0x00a1 }
            boolean r7 = r7.containsKey(r2)     // Catch:{ all -> 0x00a1 }
            if (r7 == 0) goto L_0x00a4
            anet.channel.strategy.utils.SerialLruCache<java.lang.String, java.lang.String> r7 = r11.schemeMap     // Catch:{ all -> 0x00a1 }
            java.lang.Object r9 = r4.getKey()     // Catch:{ all -> 0x00a1 }
            anet.channel.strategy.utils.SerialLruCache<java.lang.String, java.lang.String> r10 = r11.schemeMap     // Catch:{ all -> 0x00a1 }
            java.lang.Object r10 = r10.get(r2)     // Catch:{ all -> 0x00a1 }
            r7.put(r9, r10)     // Catch:{ all -> 0x00a1 }
            goto L_0x0077
        L_0x00a1:
            r7 = move-exception
            r0 = r1
            goto L_0x003e
        L_0x00a4:
            anet.channel.strategy.utils.SerialLruCache<java.lang.String, java.lang.String> r7 = r11.schemeMap     // Catch:{ all -> 0x00a1 }
            java.lang.Object r9 = r4.getKey()     // Catch:{ all -> 0x00a1 }
            java.lang.String r10 = "No_Result"
            r7.put(r9, r10)     // Catch:{ all -> 0x00a1 }
            goto L_0x0077
        L_0x00b1:
            monitor-exit(r8)     // Catch:{ all -> 0x00a1 }
            r7 = 1
            boolean r7 = anet.channel.util.ALog.isPrintLog(r7)
            if (r7 == 0) goto L_0x0004
            java.lang.String r7 = "awcn.SafeAislesMap"
            java.lang.String r8 = r11.toString()
            r9 = 0
            r10 = 0
            java.lang.Object[] r10 = new java.lang.Object[r10]
            anet.channel.util.ALog.d(r7, r8, r9, r10)
            goto L_0x0004
        L_0x00c9:
            r0 = r1
            goto L_0x0035
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.strategy.SafeAislesMap.update(anet.channel.strategy.StrategyResultParser$HttpDnsResponse):void");
    }

    /* access modifiers changed from: package-private */
    public String getSafeAislesByHost(String host) {
        String scheme;
        if (TextUtils.isEmpty(host) || !Utils.checkHostValidAndNotIp(host)) {
            return null;
        }
        synchronized (this.schemeMap) {
            scheme = (String) this.schemeMap.get(host);
            if (scheme == null) {
                this.schemeMap.put(host, NO_RESULT);
            }
        }
        if (scheme == null) {
            this.holder.getCurrStrategyTable().sendAmdcRequest(host, false);
            return scheme;
        } else if (NO_RESULT.equals(scheme)) {
            return null;
        } else {
            return scheme;
        }
    }

    public String toString() {
        String str;
        synchronized (this.schemeMap) {
            str = "SchemeMap: " + this.schemeMap.toString();
        }
        return str;
    }
}
