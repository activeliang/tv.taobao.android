package anetwork.channel.entity;

import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.text.TextUtils;
import anet.channel.request.BodyEntry;
import anet.channel.util.ALog;
import anetwork.channel.Header;
import anetwork.channel.IBodyHandler;
import anetwork.channel.Param;
import anetwork.channel.Request;
import anetwork.channel.util.RequestConstant;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestImpl implements Request {
    private static final String TAG = "anet.RequestImpl";
    private String bizId;
    private BodyEntry bodyEntry = null;
    private String charset = "utf-8";
    private int connectTimeout;
    private Map<String, String> extProperties;
    private List<Header> headers;
    private boolean isRedirect = true;
    private String method = "GET";
    private List<Param> params;
    private int readTimeout;
    private int retryTime = 2;
    private String seqNo;
    private URI uri;
    private URL url;

    public RequestImpl() {
    }

    @Deprecated
    public RequestImpl(URI uri2) {
        this.uri = uri2;
    }

    @Deprecated
    public RequestImpl(URL url2) {
        this.url = url2;
    }

    public RequestImpl(String urlStr) {
        if (urlStr != null) {
            try {
                if (urlStr.startsWith(WVUtils.URL_SEPARATOR)) {
                    urlStr = "http:" + urlStr;
                }
            } catch (MalformedURLException e) {
                ALog.w(TAG, "url MalformedURLException error:" + urlStr, (String) null, new Object[0]);
                return;
            }
        }
        this.url = new URL(urlStr);
    }

    @Deprecated
    public URI getURI() {
        return this.uri;
    }

    @Deprecated
    public void setUri(URI uri2) {
        this.uri = uri2;
    }

    public boolean getFollowRedirects() {
        return this.isRedirect;
    }

    public void setFollowRedirects(boolean isRedirect2) {
        this.isRedirect = isRedirect2;
    }

    public List<Header> getHeaders() {
        return this.headers;
    }

    public void setHeaders(List<Header> headers2) {
        this.headers = headers2;
    }

    public void addHeader(String name, String value) {
        if (name != null && value != null) {
            if (this.headers == null) {
                this.headers = new ArrayList();
            }
            this.headers.add(new BasicHeader(name, value));
        }
    }

    public void removeHeader(Header header) {
        if (this.headers != null) {
            this.headers.remove(header);
        }
    }

    public void setHeader(Header header) {
        if (header != null) {
            if (this.headers == null) {
                this.headers = new ArrayList();
            }
            int i = 0;
            int size = this.headers.size();
            while (true) {
                if (i >= size) {
                    break;
                }
                if (header.getName().equalsIgnoreCase(this.headers.get(i).getName())) {
                    this.headers.set(i, header);
                    break;
                }
                i++;
            }
            if (i < this.headers.size()) {
                this.headers.add(header);
            }
        }
    }

    public Header[] getHeaders(String name) {
        if (name == null) {
            return null;
        }
        ArrayList<Header> hs = new ArrayList<>();
        if (this.headers == null) {
            return null;
        }
        for (int i = 0; i < this.headers.size(); i++) {
            if (!(this.headers.get(i) == null || this.headers.get(i).getName() == null || !this.headers.get(i).getName().equalsIgnoreCase(name))) {
                hs.add(this.headers.get(i));
            }
        }
        if (hs.size() <= 0) {
            return null;
        }
        Header[] headerArray = new Header[hs.size()];
        hs.toArray(headerArray);
        return headerArray;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method2) {
        this.method = method2;
    }

    public int getRetryTime() {
        return this.retryTime;
    }

    public void setRetryTime(int retryTime2) {
        this.retryTime = retryTime2;
    }

    public String getCharset() {
        return this.charset;
    }

    public void setCharset(String charset2) {
        this.charset = charset2;
    }

    public List<Param> getParams() {
        return this.params;
    }

    public void setParams(List<Param> params2) {
        this.params = params2;
    }

    public BodyEntry getBodyEntry() {
        return this.bodyEntry;
    }

    public void setBodyEntry(BodyEntry bodyEntry2) {
        this.bodyEntry = bodyEntry2;
    }

    @Deprecated
    public IBodyHandler getBodyHandler() {
        return null;
    }

    public void setBodyHandler(IBodyHandler bodyHandler) {
        this.bodyEntry = new BodyHandlerEntry(bodyHandler);
    }

    public URL getURL() {
        return this.url;
    }

    public void setUrL(URL url2) {
        this.url = url2;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public void setConnectTimeout(int socketTimeout) {
        this.connectTimeout = socketTimeout;
    }

    public void setReadTimeout(int readTimeout2) {
        this.readTimeout = readTimeout2;
    }

    @Deprecated
    public void setBizId(int bizId2) {
        this.bizId = String.valueOf(bizId2);
    }

    public void setBizId(String bizId2) {
        this.bizId = bizId2;
    }

    public String getBizId() {
        return this.bizId;
    }

    public void setSeqNo(String seqNo2) {
        this.seqNo = seqNo2;
    }

    public String getSeqNo() {
        return this.seqNo;
    }

    @Deprecated
    public boolean isCookieEnabled() {
        return !"false".equals(getExtProperty(RequestConstant.ENABLE_COOKIE));
    }

    @Deprecated
    public void setCookieEnabled(boolean needCookie) {
        setExtProperty(RequestConstant.ENABLE_COOKIE, needCookie ? "true" : "false");
    }

    @Deprecated
    public void setProtocolModifiable(boolean bModifiable) {
        setExtProperty(RequestConstant.ENABLE_SCHEME_REPLACE, bModifiable ? "true" : "false");
    }

    @Deprecated
    public boolean isProtocolModifiable() {
        return !"false".equals(getExtProperty(RequestConstant.ENABLE_SCHEME_REPLACE));
    }

    public void setExtProperty(String key, String value) {
        if (!TextUtils.isEmpty(key)) {
            if (this.extProperties == null) {
                this.extProperties = new HashMap();
            }
            this.extProperties.put(key, value);
        }
    }

    public String getExtProperty(String key) {
        if (this.extProperties == null) {
            return null;
        }
        return this.extProperties.get(key);
    }

    public Map<String, String> getExtProperties() {
        return this.extProperties;
    }
}
