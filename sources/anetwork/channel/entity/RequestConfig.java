package anetwork.channel.entity;

import anet.channel.request.Request;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpUrl;
import anet.channel.util.Utils;
import anetwork.channel.Header;
import anetwork.channel.Param;
import anetwork.channel.aidl.ParcelableRequest;
import anetwork.channel.config.NetworkConfigCenter;
import anetwork.channel.util.RequestConstant;
import anetwork.channel.util.SeqGen;
import java.util.HashMap;
import java.util.Map;

public class RequestConfig {
    private static final int DFT_CONNECT_TIMEOUT = 15000;
    private static final int DFT_READ_TIMEOUT = 15000;
    private static final int MAX_RETRY_TIMES = 3;
    private static final String TAG = "anet.RequestConfig";
    private Request awcnRequest = null;
    private int connectTimeout = 0;
    private int mCurrentRetryTimes = 0;
    private int mRedirectTimes = 0;
    private int maxRetryTime = 0;
    private int readTimeout = 0;
    private final ParcelableRequest request;
    private RequestStatistic rs = null;
    private final String seqNo;
    private final int type;

    public RequestConfig(ParcelableRequest request2, int type2) {
        if (request2 == null) {
            throw new IllegalArgumentException("request is null");
        }
        this.request = request2;
        this.type = type2;
        this.seqNo = SeqGen.createSeqNo(request2.getSeqNo(), type2 == 0 ? "HTTP" : "DGRD");
        this.connectTimeout = request2.getConnectTimeout();
        if (this.connectTimeout <= 0) {
            this.connectTimeout = (int) (Utils.getNetworkTimeFactor() * 15000.0f);
        }
        this.readTimeout = request2.getReadTimeout();
        if (this.readTimeout <= 0) {
            this.readTimeout = (int) (Utils.getNetworkTimeFactor() * 15000.0f);
        }
        this.maxRetryTime = request2.getRetryTime();
        if (this.maxRetryTime < 0 || this.maxRetryTime > 3) {
            this.maxRetryTime = 2;
        }
        HttpUrl httpUrl = initHttpUrl();
        this.rs = new RequestStatistic(httpUrl.host(), String.valueOf(request2.getBizId()));
        this.rs.url = httpUrl.simpleUrlString();
        this.awcnRequest = buildRequest(httpUrl);
    }

    public Request getAwcnRequest() {
        return this.awcnRequest;
    }

    public void setAwcnRequest(Request request2) {
        this.awcnRequest = request2;
    }

    private HttpUrl initHttpUrl() {
        HttpUrl httpUrl = HttpUrl.parse(this.request.getURL());
        if (httpUrl == null) {
            throw new IllegalArgumentException("url is invalid. url=" + this.request.getURL());
        }
        if (!NetworkConfigCenter.isSSLEnabled()) {
            httpUrl.downgradeSchemeAndLock();
        } else if ("false".equalsIgnoreCase(this.request.getExtProperty(RequestConstant.ENABLE_SCHEME_REPLACE))) {
            httpUrl.lockScheme();
        }
        return httpUrl;
    }

    private Request buildRequest(HttpUrl httpUrl) {
        Request.Builder builder = new Request.Builder().setUrl(httpUrl).setMethod(this.request.getMethod()).setBody(this.request.getBodyEntry()).setReadTimeout(getReadTimeout()).setConnectTimeout(getConnectTimeout()).setRedirectEnable(this.request.getFollowRedirects()).setRedirectTimes(this.mRedirectTimes).setBizId(this.request.getBizId()).setSeq(getSeqNo()).setRequestStatistic(this.rs);
        if (this.request.getParams() != null) {
            for (Param param : this.request.getParams()) {
                builder.addParam(param.getKey(), param.getValue());
            }
        }
        if (this.request.getCharset() != null) {
            builder.setCharset(this.request.getCharset());
        }
        builder.setHeaders(initHeaders(httpUrl));
        return builder.build();
    }

    public RequestStatistic getStatistic() {
        return this.rs;
    }

    public int getCurrentRetryTimes() {
        return this.mCurrentRetryTimes;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public int getWaitTimeout() {
        return this.readTimeout * (this.maxRetryTime + 1);
    }

    public String getSeqNo() {
        return this.seqNo;
    }

    public int getRequestType() {
        return this.type;
    }

    public String getRequestProperty(String key) {
        return this.request.getExtProperty(key);
    }

    public boolean isAllowRetry() {
        return this.mCurrentRetryTimes < this.maxRetryTime;
    }

    public boolean isHttpSessionEnable() {
        return NetworkConfigCenter.isHttpSessionEnable() && !"false".equalsIgnoreCase(this.request.getExtProperty(RequestConstant.ENABLE_HTTP_DNS)) && (NetworkConfigCenter.isAllowHttpIpRetry() || getCurrentRetryTimes() == 0);
    }

    public HttpUrl getHttpUrl() {
        return this.awcnRequest.getHttpUrl();
    }

    public String getUrlString() {
        return this.awcnRequest.getUrlString();
    }

    public Map<String, String> getHeaders() {
        return this.awcnRequest.getHeaders();
    }

    private Map<String, String> initHeaders(HttpUrl httpUrl) {
        boolean removeHost = true;
        if (anet.channel.strategy.utils.Utils.isIPV4Address(httpUrl.host())) {
            removeHost = false;
        }
        HashMap<String, String> headers = new HashMap<>();
        if (this.request.getHeaders() != null) {
            for (Header header : this.request.getHeaders()) {
                String name = header.getName();
                if (!"Host".equalsIgnoreCase(name) && !":host".equalsIgnoreCase(name)) {
                    boolean isRequestKeepCustomCookie = "true".equalsIgnoreCase(this.request.getExtProperty(RequestConstant.KEEP_CUSTOM_COOKIE));
                    if (!HttpConstant.COOKIE.equalsIgnoreCase(header.getName()) || isRequestKeepCustomCookie) {
                        headers.put(name, header.getValue());
                    }
                } else if (!removeHost) {
                    headers.put("Host", header.getValue());
                }
            }
        }
        return headers;
    }

    public boolean isRequestCookieEnabled() {
        return !"false".equalsIgnoreCase(this.request.getExtProperty(RequestConstant.ENABLE_COOKIE));
    }

    public void retryRequest() {
        this.mCurrentRetryTimes++;
        this.rs.retryTimes = this.mCurrentRetryTimes;
    }

    public void redirectToUrl(HttpUrl httpUrl) {
        this.mRedirectTimes++;
        this.rs = new RequestStatistic(httpUrl.host(), String.valueOf(this.request.getBizId()));
        this.rs.url = httpUrl.simpleUrlString();
        this.awcnRequest = buildRequest(httpUrl);
    }
}
