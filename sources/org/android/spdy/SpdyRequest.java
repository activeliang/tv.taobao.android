package org.android.spdy;

import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import anet.channel.util.HttpConstant;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.ju.track.constants.Constants;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public final class SpdyRequest {
    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";
    private int connectionTimeoutMs = 0;
    private String domain;
    private Map<String, String> extHead;
    private String host;
    private String method;
    private int port;
    private RequestPriority priority;
    private String proxyIp = Constants.PARAM_OUTER_SPM_NONE;
    private int proxyPort = 0;
    private int requestTimeoutMs = 0;
    private int retryTimes = 0;
    private URL url;

    public SpdyRequest(URL url2, String host2, int port2, String proxyIp2, int proxyPort2, String method2, RequestPriority priority2, int requestTimeoutMs2, int connectionTimeoutMs2, int retryTimes2) {
        this.url = url2;
        this.domain = "";
        this.host = host2;
        this.port = port2;
        if (!(proxyIp2 == null || proxyPort2 == 0)) {
            this.proxyIp = proxyIp2;
            this.proxyPort = proxyPort2;
        }
        this.method = method2;
        this.extHead = new HashMap(5);
        this.priority = priority2;
        if (priority2 == null) {
            this.priority = RequestPriority.DEFAULT_PRIORITY;
        }
        this.requestTimeoutMs = requestTimeoutMs2;
        this.connectionTimeoutMs = connectionTimeoutMs2;
        this.retryTimes = retryTimes2;
    }

    public SpdyRequest(URL url2, String host2, int port2, String method2, RequestPriority priority2) {
        this.url = url2;
        this.domain = "";
        this.host = host2;
        this.port = port2;
        this.method = method2;
        this.extHead = new HashMap(5);
        this.priority = priority2;
        if (priority2 == null) {
            this.priority = RequestPriority.DEFAULT_PRIORITY;
        }
    }

    public SpdyRequest(URL url2, String method2, RequestPriority priority2) {
        this.url = url2;
        this.domain = "";
        this.host = url2.getHost();
        this.port = url2.getPort();
        if (this.port < 0) {
            this.port = url2.getDefaultPort();
        }
        this.method = method2;
        this.extHead = new HashMap(5);
        this.priority = priority2;
        if (priority2 == null) {
            this.priority = RequestPriority.DEFAULT_PRIORITY;
        }
    }

    public SpdyRequest(URL url2, String method2, RequestPriority priority2, int requestTimeoutMs2, int connectionTimeoutMs2) {
        this.url = url2;
        this.domain = "";
        this.host = url2.getHost();
        this.port = url2.getPort();
        if (this.port < 0) {
            this.port = url2.getDefaultPort();
        }
        this.method = method2;
        this.extHead = new HashMap(5);
        this.priority = priority2;
        if (priority2 == null) {
            this.priority = RequestPriority.DEFAULT_PRIORITY;
        }
        this.requestTimeoutMs = requestTimeoutMs2;
        this.connectionTimeoutMs = connectionTimeoutMs2;
    }

    public SpdyRequest(URL url2, String method2) {
        this.url = url2;
        this.domain = "";
        this.host = url2.getHost();
        this.port = url2.getPort();
        if (this.port < 0) {
            this.port = url2.getDefaultPort();
        }
        this.method = method2;
        this.extHead = new HashMap(5);
        this.priority = RequestPriority.DEFAULT_PRIORITY;
    }

    public SpdyRequest(URL url2, String domain2, String host2, int port2, String proxyIp2, int proxyPort2, String method2, RequestPriority priority2, int requestTimeoutMs2, int connectionTimeoutMs2, int retryTimes2) {
        this.url = url2;
        this.domain = domain2;
        this.host = host2;
        this.port = port2;
        if (!(proxyIp2 == null || proxyPort2 == 0)) {
            this.proxyIp = proxyIp2;
            this.proxyPort = proxyPort2;
        }
        this.method = method2;
        this.extHead = new HashMap(5);
        this.priority = priority2;
        if (priority2 == null) {
            this.priority = RequestPriority.DEFAULT_PRIORITY;
        }
        this.requestTimeoutMs = requestTimeoutMs2;
        this.connectionTimeoutMs = connectionTimeoutMs2;
        this.retryTimes = retryTimes2;
    }

    public SpdyRequest(URL url2, String domain2, String host2, int port2, String method2, RequestPriority priority2) {
        this.url = url2;
        this.domain = domain2;
        this.host = host2;
        this.port = port2;
        this.method = method2;
        this.extHead = new HashMap(5);
        this.priority = priority2;
        if (priority2 == null) {
            this.priority = RequestPriority.DEFAULT_PRIORITY;
        }
    }

    public SpdyRequest(URL url2, String domain2, String method2, RequestPriority priority2) {
        this.url = url2;
        this.domain = domain2;
        this.host = url2.getHost();
        this.port = url2.getPort();
        if (this.port < 0) {
            this.port = url2.getDefaultPort();
        }
        this.method = method2;
        this.extHead = new HashMap(5);
        this.priority = priority2;
        if (priority2 == null) {
            this.priority = RequestPriority.DEFAULT_PRIORITY;
        }
    }

    public SpdyRequest(URL url2, String domain2, String method2, RequestPriority priority2, int requestTimeoutMs2, int connectionTimeoutMs2) {
        this.url = url2;
        this.domain = domain2;
        this.host = url2.getHost();
        this.port = url2.getPort();
        if (this.port < 0) {
            this.port = url2.getDefaultPort();
        }
        this.method = method2;
        this.extHead = new HashMap(5);
        this.priority = priority2;
        if (priority2 == null) {
            this.priority = RequestPriority.DEFAULT_PRIORITY;
        }
        this.requestTimeoutMs = requestTimeoutMs2;
        this.connectionTimeoutMs = connectionTimeoutMs2;
    }

    public SpdyRequest(URL url2, String domain2, String method2) {
        this.url = url2;
        this.domain = domain2;
        this.host = url2.getHost();
        this.port = url2.getPort();
        if (this.port < 0) {
            this.port = url2.getDefaultPort();
        }
        this.method = method2;
        this.extHead = new HashMap(5);
        this.priority = RequestPriority.DEFAULT_PRIORITY;
    }

    public void addHeader(String key, String value) {
        this.extHead.put(key, value);
    }

    public void addHeaders(Map<String, String> headers) {
        this.extHead.putAll(headers);
    }

    /* access modifiers changed from: package-private */
    public URL getUrl() {
        return this.url;
    }

    /* access modifiers changed from: package-private */
    public String getMethod() {
        return this.method;
    }

    /* access modifiers changed from: package-private */
    public int getPriority() {
        return this.priority.getPriorityInt();
    }

    private String getPath() {
        StringBuilder path = new StringBuilder();
        path.append(this.url.getPath());
        if (this.url.getQuery() != null) {
            path.append(WVUtils.URL_DATA_CHAR).append(this.url.getQuery());
        }
        if (this.url.getRef() != null) {
            path.append(Constant.INTENT_JSON_MARK).append(this.url.getRef());
        }
        return path.toString();
    }

    /* access modifiers changed from: package-private */
    public Map<String, String> getHeaders() {
        Map<String, String> map = new HashMap<>(5);
        map.put(":path", getPath());
        map.put(":method", this.method);
        map.put(":version", "HTTP/1.1");
        map.put(":host", this.url.getAuthority());
        map.put(":scheme", this.url.getProtocol());
        if (this.extHead != null && this.extHead.size() > 0) {
            map.putAll(this.extHead);
        }
        return map;
    }

    /* access modifiers changed from: package-private */
    public String getUrlPath() {
        StringBuilder path = new StringBuilder();
        path.append(this.url.getProtocol()).append(HttpConstant.SCHEME_SPLIT).append(this.url.getAuthority()).append(getPath());
        return path.toString();
    }

    /* access modifiers changed from: package-private */
    public String getHost() {
        return this.host;
    }

    /* access modifiers changed from: package-private */
    public int getPort() {
        if (this.port < 0) {
            return 80;
        }
        return this.port;
    }

    /* access modifiers changed from: package-private */
    public String getProxyIp() {
        return this.proxyIp;
    }

    /* access modifiers changed from: package-private */
    public int getProxyPort() {
        return this.proxyPort;
    }

    public void setDomain(String domain2) {
        this.domain = domain2;
    }

    /* access modifiers changed from: package-private */
    public String getDomain() {
        return this.domain;
    }

    public String getAuthority() {
        return this.host + SymbolExpUtil.SYMBOL_COLON + Integer.toString(this.port) + WVNativeCallbackUtil.SEPERATER + this.proxyIp + SymbolExpUtil.SYMBOL_COLON + this.proxyPort;
    }

    public int getRequestTimeoutMs() {
        return this.requestTimeoutMs;
    }

    public int getConnectionTimeoutMs() {
        return this.connectionTimeoutMs;
    }

    public int getRetryTimes() {
        return this.retryTimes;
    }
}
