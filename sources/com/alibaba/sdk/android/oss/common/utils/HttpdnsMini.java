package com.alibaba.sdk.android.oss.common.utils;

import android.taobao.windvane.util.WVNativeCallbackUtil;
import com.alibaba.sdk.android.oss.common.OSSLog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONObject;

public class HttpdnsMini {
    private static final String ACCOUNT_ID = "181345";
    private static final int EMPTY_RESULT_HOST_TTL = 30;
    private static final int MAX_HOLD_HOST_NUM = 100;
    private static final int MAX_THREAD_NUM = 5;
    private static final int RESOLVE_TIMEOUT_IN_SEC = 10;
    private static final String SERVER_IP = "203.107.1.1";
    private static final String TAG = "HttpDnsMini";
    private static HttpdnsMini instance;
    /* access modifiers changed from: private */
    public ConcurrentMap<String, HostObject> hostManager = new ConcurrentHashMap();
    public boolean isHttp2Test = false;
    private ExecutorService pool = Executors.newFixedThreadPool(5);

    private HttpdnsMini() {
    }

    public static HttpdnsMini getInstance() {
        if (instance == null) {
            synchronized (HttpdnsMini.class) {
                if (instance == null) {
                    instance = new HttpdnsMini();
                }
            }
        }
        return instance;
    }

    public String getIpByHostAsync(String hostName) {
        if (this.isHttp2Test) {
            return "118.178.62.19";
        }
        HostObject host = (HostObject) this.hostManager.get(hostName);
        if (host == null || host.isExpired()) {
            OSSLog.logDebug("[httpdnsmini] - refresh host: " + hostName);
            this.pool.submit(new QueryHostTask(hostName));
        }
        if (host == null || !host.isStillAvailable()) {
            return null;
        }
        return host.getIp();
    }

    class HostObject {
        private String hostName;
        private String ip;
        private long queryTime;
        private long ttl;

        HostObject() {
        }

        public String toString() {
            return "[hostName=" + getHostName() + ", ip=" + this.ip + ", ttl=" + getTtl() + ", queryTime=" + this.queryTime + "]";
        }

        public boolean isExpired() {
            return getQueryTime() + this.ttl < System.currentTimeMillis() / 1000;
        }

        public boolean isStillAvailable() {
            return (getQueryTime() + this.ttl) + 600 > System.currentTimeMillis() / 1000;
        }

        public String getIp() {
            return this.ip;
        }

        public void setIp(String ip2) {
            this.ip = ip2;
        }

        public String getHostName() {
            return this.hostName;
        }

        public void setHostName(String hostName2) {
            this.hostName = hostName2;
        }

        public long getTtl() {
            return this.ttl;
        }

        public void setTtl(long ttl2) {
            this.ttl = ttl2;
        }

        public long getQueryTime() {
            return this.queryTime;
        }

        public void setQueryTime(long queryTime2) {
            this.queryTime = queryTime2;
        }
    }

    class QueryHostTask implements Callable<String> {
        private boolean hasRetryed = false;
        private String hostName;

        public QueryHostTask(String hostToQuery) {
            this.hostName = hostToQuery;
        }

        public String call() {
            String ip;
            String resolveUrl = "http://" + HttpdnsMini.SERVER_IP + WVNativeCallbackUtil.SEPERATER + HttpdnsMini.ACCOUNT_ID + "/d?host=" + this.hostName;
            InputStream in = null;
            OSSLog.logDebug("[httpdnsmini] - buildUrl: " + resolveUrl);
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(resolveUrl).openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                if (conn.getResponseCode() != 200) {
                    OSSLog.logError("[httpdnsmini] - responseCodeNot 200, but: " + conn.getResponseCode());
                } else {
                    in = conn.getInputStream();
                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        String line = streamReader.readLine();
                        if (line == null) {
                            break;
                        }
                        sb.append(line);
                    }
                    JSONObject json = new JSONObject(sb.toString());
                    String host = json.getString("host");
                    long ttl = json.getLong("ttl");
                    JSONArray ips = json.getJSONArray("ips");
                    OSSLog.logDebug("[httpdnsmini] - ips:" + ips.toString());
                    if (!(host == null || ips == null || ips.length() <= 0)) {
                        if (ttl == 0) {
                            ttl = 30;
                        }
                        HostObject hostObject = new HostObject();
                        if (ips == null) {
                            ip = null;
                        } else {
                            ip = ips.getString(0);
                        }
                        hostObject.setHostName(host);
                        hostObject.setTtl(ttl);
                        hostObject.setIp(ip);
                        hostObject.setQueryTime(System.currentTimeMillis() / 1000);
                        OSSLog.logDebug("[httpdnsmini] - resolve result:" + hostObject.toString());
                        if (HttpdnsMini.this.hostManager.size() < 100) {
                            HttpdnsMini.this.hostManager.put(this.hostName, hostObject);
                        }
                        if (in == null) {
                            return ip;
                        }
                        try {
                            in.close();
                            return ip;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return ip;
                        }
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (Exception e3) {
                if (OSSLog.isEnableLog()) {
                    e3.printStackTrace();
                    OSSLog.logThrowable2Local(e3);
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
            } catch (Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                }
                throw th;
            }
            if (this.hasRetryed) {
                return null;
            }
            this.hasRetryed = true;
            return call();
        }
    }
}
