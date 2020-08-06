package android.taobao.windvane.connect;

import android.net.Uri;
import java.util.Map;

public class HttpRequest {
    public static final String DEFAULT_HTTPS_ERROR_EXPIRED = "EXPIRED";
    public static final String DEFAULT_HTTPS_ERROR_INVALID = "INVALID";
    public static final String DEFAULT_HTTPS_ERROR_NONE = "NONE";
    public static final int DEFAULT_MAX_LENGTH = 5242880;
    public static final int DEFAULT_MAX_REDIRECT_TIMES = 5;
    private int connectTimeout = 5000;
    private Map<String, String> headers = null;
    private String httpsVerifyError = "NONE";
    private boolean isRedirect = true;
    private String method = "GET";
    private byte[] postData;
    private int readTimeout = 5000;
    private int retryTime = 1;
    private Uri uri;

    public HttpRequest(String url) {
        if (url == null) {
            throw new NullPointerException("HttpRequest init error, url is null.");
        }
        this.uri = Uri.parse(url);
    }

    public Uri getUri() {
        return this.uri;
    }

    public void setUri(Uri uri2) {
        if (uri2 != null) {
            this.uri = uri2;
        }
    }

    public byte[] getPostData() {
        return this.postData;
    }

    public void setPostData(byte[] postData2) {
        this.postData = postData2;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method2) {
        this.method = method2;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, String> headers2) {
        this.headers = headers2;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout2) {
        this.connectTimeout = connectTimeout2;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(int readTimeout2) {
        this.readTimeout = readTimeout2;
    }

    public boolean isRedirect() {
        return this.isRedirect;
    }

    public void setRedirect(boolean isRedirect2) {
        this.isRedirect = isRedirect2;
    }

    public int getRetryTime() {
        return this.retryTime;
    }

    public void setRetryTime(int retryTime2) {
        this.retryTime = retryTime2;
    }

    public String getHttpsVerifyError() {
        return this.httpsVerifyError;
    }

    public void setHttpsVerifyError(String httpsVerifyError2) {
        this.httpsVerifyError = httpsVerifyError2;
    }
}
