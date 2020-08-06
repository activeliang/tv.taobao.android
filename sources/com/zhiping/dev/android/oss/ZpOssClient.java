package com.zhiping.dev.android.oss;

import com.zhiping.dev.android.oss.IClient;

public class ZpOssClient {
    private static final ZpOssClient ourInstance = new ZpOssClient();
    private AliyunOssClient aliyunOssClient;

    public static synchronized ZpOssClient getInstance() {
        ZpOssClient zpOssClient;
        synchronized (ZpOssClient.class) {
            zpOssClient = ourInstance;
        }
        return zpOssClient;
    }

    private ZpOssClient() {
    }

    public synchronized IClient getAliyunOssClient(IClient.IConfig config) {
        if (this.aliyunOssClient == null) {
            this.aliyunOssClient = new AliyunOssClient(config);
        } else if (config != null) {
            this.aliyunOssClient.applyConfig(config);
        }
        return this.aliyunOssClient;
    }
}
