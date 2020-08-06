package mtopsdk.network.domain;

import android.text.TextUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.network.util.NetworkUtils;

public final class Request {
    public final String api;
    public final String appKey;
    public final String authCode;
    public final int bizId;
    public final RequestBody body;
    public final int connectTimeoutMills;
    public final int env;
    public final Map<String, String> headers;
    public final String method;
    public final int readTimeoutMills;
    public final Object reqContext;
    public final int retryTimes;
    public final String seqNo;
    public final String url;

    public interface Environment {
        public static final int DAILY = 2;
        public static final int ONLINE = 0;
        public static final int PRE = 1;

        @Retention(RetentionPolicy.SOURCE)
        public @interface Definition {
        }
    }

    private Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
        this.body = builder.body;
        this.seqNo = builder.seqNo;
        this.connectTimeoutMills = builder.connectTimeoutMills;
        this.readTimeoutMills = builder.readTimeoutMills;
        this.retryTimes = builder.retryTimes;
        this.bizId = builder.bizId;
        this.appKey = builder.appKey;
        this.authCode = builder.authCode;
        this.env = builder.env;
        this.reqContext = builder.reqContext;
        this.api = builder.api;
    }

    public Builder newBuilder() {
        return new Builder();
    }

    public String header(String name) {
        return this.headers.get(name);
    }

    public void setHeader(String name, String value) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(value)) {
            this.headers.put(name, value);
        }
    }

    public boolean isHttps() {
        if (this.url != null) {
            return this.url.startsWith("https");
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(128);
        builder.append("Request{ url=").append(this.url);
        builder.append(", method=").append(this.method);
        builder.append(", appKey=").append(this.appKey);
        builder.append(", authCode=").append(this.authCode);
        builder.append(", headers=").append(this.headers);
        builder.append(", body=").append(this.body);
        builder.append(", seqNo=").append(this.seqNo);
        builder.append(", connectTimeoutMills=").append(this.connectTimeoutMills);
        builder.append(", readTimeoutMills=").append(this.readTimeoutMills);
        builder.append(", retryTimes=").append(this.retryTimes);
        builder.append(", bizId=").append(this.bizId);
        builder.append(", env=").append(this.env);
        builder.append(", reqContext=").append(this.reqContext);
        builder.append(", api=").append(this.api);
        builder.append("}");
        return builder.toString();
    }

    public static class Builder {
        String api;
        String appKey;
        String authCode;
        int bizId;
        RequestBody body;
        int connectTimeoutMills;
        int env;
        Map<String, String> headers;
        String method;
        int readTimeoutMills;
        Object reqContext;
        int retryTimes;
        String seqNo;
        String url;

        public Builder() {
            this.connectTimeoutMills = 15000;
            this.readTimeoutMills = 15000;
            this.method = "GET";
            this.headers = new HashMap();
        }

        private Builder(Request request) {
            this.connectTimeoutMills = 15000;
            this.readTimeoutMills = 15000;
            this.url = request.url;
            this.method = request.method;
            this.body = request.body;
            this.headers = request.headers;
            this.seqNo = request.seqNo;
            this.connectTimeoutMills = request.connectTimeoutMills;
            this.readTimeoutMills = request.readTimeoutMills;
            this.retryTimes = request.retryTimes;
            this.bizId = request.bizId;
            this.appKey = request.appKey;
            this.authCode = request.authCode;
            this.reqContext = request.reqContext;
            this.api = request.api;
        }

        public Builder url(String url2) {
            if (url2 == null) {
                throw new IllegalArgumentException("url == null");
            }
            this.url = url2;
            return this;
        }

        public Builder setHeader(String name, String value) {
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(value)) {
                this.headers.put(name, value);
            }
            return this;
        }

        public Builder removeHeader(String name) {
            this.headers.remove(name);
            return this;
        }

        public Builder headers(Map<String, String> headers2) {
            if (headers2 != null) {
                this.headers = headers2;
            }
            return this;
        }

        public Builder seqNo(String seqNo2) {
            this.seqNo = seqNo2;
            return this;
        }

        public Builder connectTimeout(int connectTimeoutMills2) {
            if (connectTimeoutMills2 > 0) {
                this.connectTimeoutMills = connectTimeoutMills2;
            }
            return this;
        }

        public Builder readTimeout(int readTimeoutMills2) {
            if (readTimeoutMills2 > 0) {
                this.readTimeoutMills = readTimeoutMills2;
            }
            return this;
        }

        public Builder retryTimes(int retryTimes2) {
            this.retryTimes = retryTimes2;
            return this;
        }

        public Builder bizId(int bizId2) {
            this.bizId = bizId2;
            return this;
        }

        public Builder appKey(String appKey2) {
            this.appKey = appKey2;
            return this;
        }

        public Builder authCode(String authCode2) {
            this.authCode = authCode2;
            return this;
        }

        public Builder env(int env2) {
            this.env = env2;
            return this;
        }

        public Builder reqContext(Object reqContext2) {
            this.reqContext = reqContext2;
            return this;
        }

        public Builder api(String api2) {
            this.api = api2;
            return this;
        }

        public Builder post(RequestBody body2) {
            return method("POST", body2);
        }

        public Builder method(String method2, RequestBody body2) {
            if (method2 == null || method2.length() == 0) {
                throw new IllegalArgumentException("method == null || method.length() == 0");
            } else if (body2 != null || !NetworkUtils.requiresRequestBody(method2)) {
                this.method = method2;
                this.body = body2;
                return this;
            } else {
                throw new IllegalArgumentException("method " + method2 + " must have a request body.");
            }
        }

        public Request build() {
            if (this.url != null) {
                return new Request(this);
            }
            throw new IllegalStateException("url == null");
        }
    }
}
