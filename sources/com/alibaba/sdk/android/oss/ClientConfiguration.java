package com.alibaba.sdk.android.oss;

import android.taobao.windvane.jsbridge.api.WVFile;
import anet.channel.util.HttpConstant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientConfiguration {
    private static final int DEFAULT_MAX_RETRIES = 2;
    private boolean checkCRC64 = false;
    private int connectionTimeout = 60000;
    private List<String> customCnameExcludeList = new ArrayList();
    private boolean httpDnsEnable = true;
    private String mUserAgentMark;
    private int maxConcurrentRequest = 5;
    private int maxErrorRetry = 2;
    private long max_log_size = WVFile.FILE_MAX_SIZE;
    private String proxyHost;
    private int proxyPort;
    private int socketTimeout = 60000;

    public static ClientConfiguration getDefaultConf() {
        return new ClientConfiguration();
    }

    public int getMaxConcurrentRequest() {
        return this.maxConcurrentRequest;
    }

    public void setMaxConcurrentRequest(int maxConcurrentRequest2) {
        this.maxConcurrentRequest = maxConcurrentRequest2;
    }

    public int getSocketTimeout() {
        return this.socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout2) {
        this.socketTimeout = socketTimeout2;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout2) {
        this.connectionTimeout = connectionTimeout2;
    }

    public long getMaxLogSize() {
        return this.max_log_size;
    }

    public void setMaxLogSize(long max_log_size2) {
        this.max_log_size = max_log_size2;
    }

    public int getMaxErrorRetry() {
        return this.maxErrorRetry;
    }

    public void setMaxErrorRetry(int maxErrorRetry2) {
        this.maxErrorRetry = maxErrorRetry2;
    }

    public List<String> getCustomCnameExcludeList() {
        return Collections.unmodifiableList(this.customCnameExcludeList);
    }

    public void setCustomCnameExcludeList(List<String> customCnameExcludeList2) {
        if (customCnameExcludeList2 == null || customCnameExcludeList2.size() == 0) {
            throw new IllegalArgumentException("cname exclude list should not be null.");
        }
        this.customCnameExcludeList.clear();
        for (String host : customCnameExcludeList2) {
            if (host.contains(HttpConstant.SCHEME_SPLIT)) {
                this.customCnameExcludeList.add(host.substring(host.indexOf(HttpConstant.SCHEME_SPLIT) + 3));
            } else {
                this.customCnameExcludeList.add(host);
            }
        }
    }

    public String getProxyHost() {
        return this.proxyHost;
    }

    public void setProxyHost(String proxyHost2) {
        this.proxyHost = proxyHost2;
    }

    public int getProxyPort() {
        return this.proxyPort;
    }

    public void setProxyPort(int proxyPort2) {
        this.proxyPort = proxyPort2;
    }

    public String getCustomUserMark() {
        return this.mUserAgentMark;
    }

    public void setUserAgentMark(String mark) {
        this.mUserAgentMark = mark;
    }

    public boolean isHttpDnsEnable() {
        return this.httpDnsEnable;
    }

    public void setHttpDnsEnable(boolean httpdnsEnable) {
        this.httpDnsEnable = httpdnsEnable;
    }

    public boolean isCheckCRC64() {
        return this.checkCRC64;
    }

    public void setCheckCRC64(boolean checkCRC642) {
        this.checkCRC64 = checkCRC642;
    }
}
