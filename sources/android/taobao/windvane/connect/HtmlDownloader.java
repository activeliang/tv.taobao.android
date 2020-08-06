package android.taobao.windvane.connect;

import android.taobao.windvane.util.CommonUtils;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVConstants;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class HtmlDownloader implements Runnable {
    private byte[] data;
    private WebListener listener;
    private Map<String, String> requestHeader;
    private int token;
    private String url;

    public HtmlDownloader(String ua, WebListener listener2, Map<String, String> requestHeader2, String url2, int token2, boolean enableCache) {
        this.token = token2;
        this.listener = listener2;
        if (requestHeader2 != null) {
            this.requestHeader = new HashMap(requestHeader2);
        }
        this.url = url2;
    }

    public void run() {
        HttpRequest request = new HttpRequest(this.url);
        request.setHeaders(this.requestHeader);
        request.setRedirect(false);
        HttpResponse result = new HttpConnector().syncConnect(request);
        this.data = result.getData();
        synchronized (this) {
            if (this.listener != null) {
                Map<String, String> responseHeader = result.getHeaders();
                responseHeader.put("url", this.url);
                responseHeader.put(HttpConnector.RESPONSE_CODE, result.getHttpCode() + "");
                responseHeader.put(WVConstants.HTTPSVERIFYERROR, request.getHttpsVerifyError());
                String charset = responseHeader.get("content-type");
                TaoLog.d("HtmlDownloader", "http charset:" + charset);
                String charset2 = CommonUtils.parseCharset(charset);
                if (charset2 == null) {
                    charset2 = "utf-8";
                    TaoLog.d("HtmlDownloader", "default charset:" + charset2);
                }
                responseHeader.put(WVConstants.CHARSET, charset2);
                this.listener.callback(this.data, responseHeader, this.token);
                this.requestHeader = null;
                this.listener = null;
            }
        }
    }

    public synchronized void setListener(WebListener listener2) {
        this.listener = listener2;
    }

    public void cancel() {
    }

    public void clear() {
        if (this.requestHeader != null) {
            this.requestHeader.clear();
        }
    }
}
