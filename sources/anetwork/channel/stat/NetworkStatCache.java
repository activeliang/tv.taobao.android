package anetwork.channel.stat;

import anet.channel.util.StringUtils;
import anetwork.channel.statist.StatisticData;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

class NetworkStatCache implements INetworkStat {
    private static final int MAX_SIZE = 100;
    private static final String RESET_STAT = "{\"oneWayTime\" : 0, \"totalSize\" : 0}";
    private Map<String, String> lruCache;

    private static class holder {
        public static NetworkStatCache instance = new NetworkStatCache();

        private holder() {
        }
    }

    public static NetworkStatCache getInstance() {
        return holder.instance;
    }

    private NetworkStatCache() {
        this.lruCache = Collections.synchronizedMap(new LinkedHashMap<String, String>() {
            /* access modifiers changed from: protected */
            public boolean removeEldestEntry(Map.Entry<String, String> entry) {
                return size() > 100;
            }
        });
    }

    public void put(String key, StatisticData data) {
        if (!StringUtils.isBlank(key)) {
            StringBuilder sb = new StringBuilder(48);
            sb.append("{\"oneWayTime\" : ").append(data.oneWayTime_ANet).append(", \"totalSize\" : ").append(data.totalSize).append("}");
            this.lruCache.put(key, sb.toString());
        }
    }

    public void reset(String key) {
        if (this.lruCache.containsKey(key)) {
            this.lruCache.put(key, RESET_STAT);
        }
    }

    public String get(String key) {
        return this.lruCache.get(key);
    }
}
