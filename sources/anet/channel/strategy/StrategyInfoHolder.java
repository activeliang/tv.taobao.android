package anet.channel.strategy;

import android.text.TextUtils;
import anet.channel.statist.StrategyStatObject;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.StrategyResultParser;
import anet.channel.strategy.dispatch.AmdcRuntimeInfo;
import anet.channel.strategy.utils.AmdcThreadPoolExecutor;
import anet.channel.strategy.utils.SerialLruCache;
import anet.channel.util.StringUtils;
import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import mtopsdk.common.util.SymbolExpUtil;

class StrategyInfoHolder implements NetworkStatusHelper.INetworkStatusChangeListener {
    static final String HOST_UNIT_MAP = "Config$unit";
    static final int MAX_TABLE_NUM_IN_MEM = 3;
    static final String SCHEME_MAP = "Config$scheme";
    private static final String TAG = "awcn.StrategyInfoHolder";
    HostUnitMap hostUnitMap = null;
    private final Set<String> loadingFiles = new HashSet();
    final LocalDnsStrategyTable localDnsStrategyTable = new LocalDnsStrategyTable();
    SafeAislesMap schemeMap = null;
    Map<String, StrategyTable> strategyTableMap = new LruStrategyMap();
    private volatile String uniqueId = "";
    private final StrategyTable unknownStrategyTable = new StrategyTable("Unknown");

    public static StrategyInfoHolder newInstance() {
        return new StrategyInfoHolder();
    }

    private StrategyInfoHolder() {
        try {
            init();
            restore();
        } catch (Throwable th) {
        } finally {
            checkInit();
        }
    }

    /* access modifiers changed from: package-private */
    public void clear() {
        NetworkStatusHelper.removeStatusChangeListener(this);
    }

    private void init() {
        NetworkStatusHelper.addStatusChangeListener(this);
        this.uniqueId = getUniqueId(NetworkStatusHelper.getStatus());
    }

    private void checkInit() {
        for (Map.Entry<String, StrategyTable> entry : this.strategyTableMap.entrySet()) {
            entry.getValue().checkInit();
        }
        if (this.schemeMap == null) {
            this.schemeMap = new SafeAislesMap();
        }
        this.schemeMap.checkInit();
        this.schemeMap.setHolder(this);
        if (this.hostUnitMap == null) {
            this.hostUnitMap = new HostUnitMap();
        }
        this.hostUnitMap.checkInit();
    }

    private void restore() {
        final String currentFilename = this.uniqueId;
        if (!TextUtils.isEmpty(currentFilename)) {
            loadFile(currentFilename, true);
        }
        this.schemeMap = (SafeAislesMap) StrategySerializeHelper.restore(SCHEME_MAP, (StrategyStatObject) null);
        this.hostUnitMap = (HostUnitMap) StrategySerializeHelper.restore(HOST_UNIT_MAP, (StrategyStatObject) null);
        AmdcThreadPoolExecutor.submitTask(new Runnable() {
            public void run() {
                try {
                    File[] files = StrategySerializeHelper.getSortedFiles();
                    if (files != null) {
                        int fileToLoad = 0;
                        for (int i = 0; i < files.length && fileToLoad < 2; i++) {
                            File file = files[i];
                            if (!file.isDirectory()) {
                                String filename = file.getName();
                                if (!filename.equals(currentFilename) && !filename.startsWith("Config")) {
                                    StrategyInfoHolder.this.loadFile(filename, false);
                                    fileToLoad++;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001c, code lost:
        r0 = (anet.channel.strategy.StrategyTable) anet.channel.strategy.StrategySerializeHelper.restore(r7, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0022, code lost:
        if (r0 == null) goto L_0x0032;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0024, code lost:
        r0.checkInit();
        r3 = r6.strategyTableMap;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0029, code lost:
        monitor-enter(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r6.strategyTableMap.put(r0.uniqueId, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0031, code lost:
        monitor-exit(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0032, code lost:
        r3 = r6.loadingFiles;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0034, code lost:
        monitor-enter(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        r6.loadingFiles.remove(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003a, code lost:
        monitor-exit(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x003b, code lost:
        if (r8 == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x003d, code lost:
        if (r0 == null) goto L_0x0040;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x003f, code lost:
        r2 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0040, code lost:
        r1.isSucceed = r2;
        anet.channel.appmonitor.AppMonitor.getInstance().commitStat(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0012, code lost:
        r1 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0013, code lost:
        if (r8 == false) goto L_0x001c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0015, code lost:
        r1 = new anet.channel.statist.StrategyStatObject(0);
        r1.readStrategyFileId = r7;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void loadFile(java.lang.String r7, boolean r8) {
        /*
            r6 = this;
            r2 = 0
            java.util.Set<java.lang.String> r3 = r6.loadingFiles
            monitor-enter(r3)
            java.util.Set<java.lang.String> r4 = r6.loadingFiles     // Catch:{ all -> 0x004c }
            boolean r4 = r4.contains(r7)     // Catch:{ all -> 0x004c }
            if (r4 != 0) goto L_0x004a
            java.util.Set<java.lang.String> r4 = r6.loadingFiles     // Catch:{ all -> 0x004c }
            r4.add(r7)     // Catch:{ all -> 0x004c }
            monitor-exit(r3)     // Catch:{ all -> 0x004c }
            r1 = 0
            if (r8 == 0) goto L_0x001c
            anet.channel.statist.StrategyStatObject r1 = new anet.channel.statist.StrategyStatObject
            r1.<init>(r2)
            r1.readStrategyFileId = r7
        L_0x001c:
            java.lang.Object r0 = anet.channel.strategy.StrategySerializeHelper.restore(r7, r1)
            anet.channel.strategy.StrategyTable r0 = (anet.channel.strategy.StrategyTable) r0
            if (r0 == 0) goto L_0x0032
            r0.checkInit()
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyTable> r3 = r6.strategyTableMap
            monitor-enter(r3)
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyTable> r4 = r6.strategyTableMap     // Catch:{ all -> 0x004f }
            java.lang.String r5 = r0.uniqueId     // Catch:{ all -> 0x004f }
            r4.put(r5, r0)     // Catch:{ all -> 0x004f }
            monitor-exit(r3)     // Catch:{ all -> 0x004f }
        L_0x0032:
            java.util.Set<java.lang.String> r3 = r6.loadingFiles
            monitor-enter(r3)
            java.util.Set<java.lang.String> r4 = r6.loadingFiles     // Catch:{ all -> 0x0052 }
            r4.remove(r7)     // Catch:{ all -> 0x0052 }
            monitor-exit(r3)     // Catch:{ all -> 0x0052 }
            if (r8 == 0) goto L_0x0049
            if (r0 == 0) goto L_0x0040
            r2 = 1
        L_0x0040:
            r1.isSucceed = r2
            anet.channel.appmonitor.IAppMonitor r2 = anet.channel.appmonitor.AppMonitor.getInstance()
            r2.commitStat(r1)
        L_0x0049:
            return
        L_0x004a:
            monitor-exit(r3)     // Catch:{ all -> 0x004c }
            goto L_0x0049
        L_0x004c:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x004c }
            throw r2
        L_0x004f:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x004f }
            throw r2
        L_0x0052:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0052 }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.strategy.StrategyInfoHolder.loadFile(java.lang.String, boolean):void");
    }

    /* access modifiers changed from: package-private */
    public void saveData() {
        synchronized (this) {
            for (StrategyTable st : this.strategyTableMap.values()) {
                if (st.isChanged) {
                    StrategyStatObject sso = new StrategyStatObject(1);
                    sso.writeStrategyFileId = st.uniqueId;
                    StrategySerializeHelper.persist(st, st.uniqueId, sso);
                    st.isChanged = false;
                }
            }
            StrategySerializeHelper.persist(this.schemeMap, SCHEME_MAP, (StrategyStatObject) null);
            if (!this.hostUnitMap.isEmpty()) {
                StrategySerializeHelper.persist(this.hostUnitMap, HOST_UNIT_MAP, (StrategyStatObject) null);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public StrategyTable getCurrStrategyTable() {
        StrategyTable ret = this.unknownStrategyTable;
        String id = this.uniqueId;
        if (!TextUtils.isEmpty(id)) {
            synchronized (this.strategyTableMap) {
                try {
                    StrategyTable st = this.strategyTableMap.get(id);
                    if (st != null) {
                        ret = st;
                    } else {
                        StrategyTable ret2 = new StrategyTable(id);
                        try {
                            this.strategyTableMap.put(id, ret2);
                            ret = ret2;
                        } catch (Throwable th) {
                            th = th;
                            StrategyTable strategyTable = ret2;
                            throw th;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        }
        return ret;
    }

    private String getUniqueId(NetworkStatusHelper.NetworkStatus networkStatus) {
        if (networkStatus.isWifi()) {
            String bssid = StringUtils.md5ToHex(NetworkStatusHelper.getWifiBSSID());
            if (TextUtils.isEmpty(bssid)) {
                bssid = "";
            }
            return "WIFI$" + bssid;
        } else if (networkStatus.isMobile()) {
            return networkStatus.getType() + SymbolExpUtil.SYMBOL_DOLLAR + NetworkStatusHelper.getApn();
        } else {
            return "";
        }
    }

    /* access modifiers changed from: package-private */
    public void update(StrategyResultParser.HttpDnsResponse response) {
        if (response.fcLevel != 0) {
            AmdcRuntimeInfo.updateAmdcLimit(response.fcLevel, response.fcTime);
        }
        StrategyTable st = getCurrStrategyTable();
        st.update(response);
        st.isChanged = true;
        this.schemeMap.update(response);
        this.hostUnitMap.update(response);
    }

    public void onNetworkStatusChanged(NetworkStatusHelper.NetworkStatus networkStatus) {
        this.uniqueId = getUniqueId(networkStatus);
        final String id = this.uniqueId;
        if (!TextUtils.isEmpty(id)) {
            synchronized (this.strategyTableMap) {
                if (!this.strategyTableMap.containsKey(id)) {
                    AmdcThreadPoolExecutor.submitTask(new Runnable() {
                        public void run() {
                            StrategyInfoHolder.this.loadFile(id, true);
                        }
                    });
                }
            }
        }
    }

    private static class LruStrategyMap extends SerialLruCache<String, StrategyTable> {
        private static final long serialVersionUID = 1866478394612290927L;

        public LruStrategyMap() {
            super(3);
        }

        /* access modifiers changed from: protected */
        public boolean entryRemoved(final Map.Entry<String, StrategyTable> eldest) {
            AmdcThreadPoolExecutor.submitTask(new Runnable() {
                public void run() {
                    StrategyTable st = (StrategyTable) eldest.getValue();
                    if (st.isChanged) {
                        StrategyStatObject sso = new StrategyStatObject(1);
                        sso.writeStrategyFileId = st.uniqueId;
                        StrategySerializeHelper.persist((Serializable) eldest.getValue(), st.uniqueId, sso);
                        st.isChanged = false;
                    }
                }
            });
            return true;
        }
    }
}
