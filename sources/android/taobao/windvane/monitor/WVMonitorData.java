package android.taobao.windvane.monitor;

import android.net.Uri;
import android.taobao.windvane.monitor.WVPerformanceMonitorInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class WVMonitorData {
    public extra args = new extra();
    public long init = 0;
    public boolean isInit = false;
    public String performanceInfo = "";
    public long startTime = 0;
    public stat stat = new stat();
    public String url;
    public int wvAppMonitor = 1;

    public HashMap<String, Object> toJsonStringMap() {
        HashMap<String, Object> res = new HashMap<>();
        res.put("url", this.url);
        res.put("loadTime", Long.valueOf(this.stat.onLoad));
        res.put("isFinish", Integer.valueOf(this.stat.finish));
        res.put("firstByte", Long.valueOf(this.stat.firstByteTime));
        res.put("domLoad", Long.valueOf(this.stat.onDomLoad));
        res.put("fromType", Integer.valueOf(this.stat.fromType));
        res.put("matchCost", Long.valueOf(this.stat.matchCost));
        res.put("statusCode", Integer.valueOf(this.args.statusCode));
        res.put("packageappversion", this.stat.packageAppVersion);
        res.put("packageAppName", this.stat.packageAppName);
        res.put("verifyCacheSize", Integer.valueOf(this.stat.verifyCacheSize));
        res.put("via", this.args.via);
        res.put("verifyError", Integer.valueOf(this.stat.verifyError));
        res.put("verifyResTime", Long.valueOf(this.stat.verifyResTime));
        res.put("verifyTime", Long.valueOf(this.stat.verifyTime));
        res.put("allVerifyTime", Long.valueOf(this.stat.allVerifyTime));
        if (this.args.netStat != null) {
            res.put("netStat", toUtArray(this.args.netStat));
        }
        if (!this.args.resStat.isEmpty() && ((int) Math.ceil((Math.random() * 100.0d) + 0.5d)) <= WVMonitorConfigManager.getInstance().config.stat.resSample) {
            ArrayList<Map<String, String>> arr = new ArrayList<>();
            for (Map.Entry<String, resStat> entry : this.args.resStat.entrySet()) {
                if (entry.getValue().end - entry.getValue().start > WVMonitorConfigManager.getInstance().config.stat.resTime) {
                    Map<String, String> map = entry.getValue().toUtMap();
                    String url2 = entry.getKey();
                    Uri uri = Uri.parse(url2);
                    if (uri != null && uri.isHierarchical()) {
                        map.put("url", url2);
                        arr.add(map);
                    }
                }
            }
            res.put("resStat", arr);
        }
        return res;
    }

    public String[] toJsonStringDict() {
        List<String> res = new ArrayList<>();
        res.add("fromType=" + this.stat.fromType);
        res.add("PackageApp-Seq=" + this.stat.appSeq);
        res.add("PackageApp-Version=" + this.stat.packageAppVersion);
        res.add("PackageApp-Name=" + this.stat.packageAppName);
        res.add("domLoad=" + this.stat.onDomLoad);
        if (((int) Math.ceil((Math.random() * 100.0d) + 0.5d)) <= WVMonitorConfigManager.getInstance().config.stat.resSample && !this.args.resStat.isEmpty()) {
            JSONArray arr = new JSONArray();
            for (Map.Entry<String, resStat> entry : this.args.resStat.entrySet()) {
                if (entry.getValue().end - entry.getValue().start >= WVMonitorConfigManager.getInstance().config.stat.resTime) {
                    Map<String, String> map = entry.getValue().toUtMap();
                    map.put("url", entry.getKey());
                    arr.put(new JSONObject(map));
                }
            }
            res.add("resStat=" + arr.toString());
        }
        return (String[]) res.toArray(new String[0]);
    }

    public static resStat createNewResStatInstance() {
        return new resStat();
    }

    public class stat {
        public long allVerifyTime = 0;
        public String appSeq = "";
        public int finish = 0;
        public long firstByteTime = 0;
        public int fromType = 1;
        public long matchCost = -1;
        public long onDomLoad = 0;
        public long onLoad = 0;
        public String packageAppName = "";
        public String packageAppVersion = "";
        public int verifyCacheSize = 0;
        public int verifyError = 0;
        public long verifyResTime = 0;
        public long verifyTime = 0;

        public stat() {
        }
    }

    public static class resStat {
        public long end;
        public int fromType;
        public WVPerformanceMonitorInterface.NetStat netStat;
        public long start;
        public int statusCode;
        public int verifyError = 0;
        public long verifyResTime = 0;
        public long verifyTime = 0;
        public String via;

        public Map<String, String> toUtMap() {
            Map<String, String> map;
            if (this.netStat == null) {
                map = new HashMap<>();
            } else {
                map = WVMonitorData.toUtMap(this.netStat);
            }
            if (this.statusCode > 0) {
                map.put("statusCode", String.valueOf(this.statusCode));
            }
            if (this.via != null) {
                map.put("via", this.via);
            }
            if (this.start > 0) {
                map.put("start", String.valueOf(this.start));
            }
            if (this.end > 0) {
                map.put("end", String.valueOf(this.end));
            }
            map.put("fromType", String.valueOf(this.fromType));
            map.put("verifyError", String.valueOf(this.verifyError));
            map.put("verifyResTime", String.valueOf(this.verifyResTime));
            map.put("verifyTime", String.valueOf(this.verifyTime));
            return map;
        }
    }

    public class extra {
        public WVPerformanceMonitorInterface.NetStat netStat = null;
        public Map<String, resStat> resStat = new ConcurrentHashMap();
        public Map<String, Long> selfDefine = new ConcurrentHashMap();
        public int statusCode;
        public String via;

        public extra() {
        }
    }

    public static Map<String, String> toUtMap(WVPerformanceMonitorInterface.NetStat stat2) {
        Map<String, String> map = new HashMap<>();
        map.put("net_dnsTime", String.valueOf(stat2.dnsTime));
        map.put("net_isDNSTimeout", String.valueOf(stat2.isDNSTimeout));
        map.put("net_oneWayTime", String.valueOf(stat2.oneWayTime));
        map.put("net_tcpLinkDate", String.valueOf(stat2.tcpLinkDate));
        map.put("net_waitTime", String.valueOf(stat2.waitTime));
        map.put("net_postBodyTime", String.valueOf(stat2.postBodyTime));
        map.put("net_firstDataTime", String.valueOf(stat2.firstDataTime));
        map.put("net_serverRT", String.valueOf(stat2.serverRT));
        map.put("net_totalSize", String.valueOf(stat2.totalSize));
        map.put("net_recDataTime", String.valueOf(stat2.recDataTime));
        map.put("net_isSSL", String.valueOf(stat2.isSSL));
        map.put("net_dataSpeed", String.valueOf(stat2.dataSpeed));
        map.put("net_spdy", String.valueOf(stat2.spdy));
        return map;
    }

    public static ArrayList<String> toUtArray(WVPerformanceMonitorInterface.NetStat stat2) {
        ArrayList<String> arr = new ArrayList<>();
        for (Map.Entry<String, String> entry : toUtMap(stat2).entrySet()) {
            arr.add(entry.getKey() + "=" + entry.getValue());
        }
        return arr;
    }
}
