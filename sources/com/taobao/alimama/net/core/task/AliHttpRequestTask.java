package com.taobao.alimama.net.core.task;

import com.taobao.alimama.net.core.state.NetRequestRetryPolicy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AliHttpRequestTask extends AbsNetRequestTask {
    private static final String RESPONSE_SUCCESS_CODE = "200";
    private int connectTimeout;
    private Map<String, String> extraHeaders;
    private Map<String, String> fields;
    private boolean isRedirect;
    private int readTimeout;
    private int retryTimes;

    public static class Builder {
        private static final int SECOND = 1000;
        /* access modifiers changed from: private */
        public int connectTimeout;
        /* access modifiers changed from: private */
        public Map<String, String> extraHeaders;
        /* access modifiers changed from: private */
        public Map<String, String> fields;
        /* access modifiers changed from: private */
        public int innerRetryTimes;
        /* access modifiers changed from: private */
        public boolean isRedirect;
        /* access modifiers changed from: private */
        public int readTimeout;
        /* access modifiers changed from: private */
        public final NetRequestRetryPolicy retryPolicy;
        /* access modifiers changed from: private */
        public final String url;

        public Builder(String str) {
            this(str, (NetRequestRetryPolicy) null);
        }

        public Builder(String str, NetRequestRetryPolicy netRequestRetryPolicy) {
            this.isRedirect = true;
            this.innerRetryTimes = 3;
            this.readTimeout = 30000;
            this.connectTimeout = 30000;
            this.url = str;
            this.retryPolicy = netRequestRetryPolicy;
        }

        public Builder addHeader(String str, String str2) {
            if (this.extraHeaders == null) {
                this.extraHeaders = new HashMap(4);
            }
            this.extraHeaders.put(str, str2);
            return this;
        }

        public AliHttpRequestTask build() {
            return new AliHttpRequestTask(this);
        }

        public Builder putField(String str, String str2) {
            if (this.fields == null) {
                this.fields = new HashMap(5);
            }
            this.fields.put(str, str2);
            return this;
        }

        public Builder setConnectTimeout(int i) {
            this.connectTimeout = i;
            return this;
        }

        public Builder setFollowRedirect(boolean z) {
            this.isRedirect = z;
            return this;
        }

        public Builder setInnerRetryTimes(int i) {
            this.innerRetryTimes = i;
            return this;
        }

        public Builder setReadTimeout(int i) {
            this.readTimeout = i;
            return this;
        }
    }

    public AliHttpRequestTask(Builder builder) {
        super(builder.url, builder.retryPolicy);
        this.isRedirect = builder.isRedirect;
        this.retryTimes = builder.innerRetryTimes;
        this.readTimeout = builder.readTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.fields = builder.fields;
        this.extraHeaders = Collections.unmodifiableMap(builder.extraHeaders);
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public Map<String, String> getExtraHeaders() {
        return this.extraHeaders;
    }

    public Map<String, String> getFields() {
        return this.fields;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public int getRetryTimes() {
        return this.retryTimes;
    }

    public boolean isRedirect() {
        return this.isRedirect;
    }

    public boolean isRequestSuccess(String str) {
        return RESPONSE_SUCCESS_CODE.equals(str);
    }

    public boolean isRequestSystemError(String str) {
        return str.startsWith("-");
    }
}
