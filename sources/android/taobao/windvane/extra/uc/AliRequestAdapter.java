package android.taobao.windvane.extra.uc;

import android.taobao.windvane.urlintercept.WVURLInterceptService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.text.TextUtils;
import anetwork.channel.Request;
import anetwork.channel.Response;
import anetwork.channel.entity.RequestImpl;
import com.uc.webview.export.internal.interfaces.EventHandler;
import com.uc.webview.export.internal.interfaces.IRequest;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AliRequestAdapter implements IRequest {
    String TAG = "alinetwork";
    Request mAliRequest;
    private final Object mClientResource = new Object();
    private EventHandler mEventHandler;
    Future<Response> mFutureResponse;
    private Map<String, String> mHeaders;
    private boolean mIsUCProxy;
    private int mLoadType;
    private String mMethod = "GET";
    private int mRequestType;
    private Map<String, String> mUCHeaders;
    private Map<String, byte[]> mUploadDataMap;
    private Map<String, String> mUploadFileMap;
    private long mUploadFileTotalLen;
    private String mUrl;

    AliRequestAdapter(Request request, EventHandler handler) {
        this.mEventHandler = handler;
    }

    AliRequestAdapter(EventHandler handler, String url, String method, boolean isUCProxyReq, Map<String, String> headers, Map<String, String> ucHeaders, Map<String, String> uploadFileMap, Map<String, byte[]> uploadDataMap, long uploadFileTotalLen, int requestType, int loadType) {
        this.mEventHandler = handler;
        this.mUrl = url;
        this.mMethod = method;
        this.mIsUCProxy = isUCProxyReq;
        this.mHeaders = headers;
        this.mUCHeaders = ucHeaders;
        this.mUploadFileMap = uploadFileMap;
        this.mUploadDataMap = uploadDataMap;
        this.mUploadFileTotalLen = uploadFileTotalLen;
        this.mRequestType = requestType;
        this.mLoadType = loadType;
        this.mAliRequest = formatAliRequest();
    }

    private Request formatAliRequest() {
        return formatAliRequest(this.mUrl, this.mMethod, this.mIsUCProxy, this.mHeaders, this.mUCHeaders, this.mUploadFileMap, this.mUploadDataMap, this.mUploadFileTotalLen, this.mRequestType, this.mLoadType);
    }

    private Request formatAliRequest(String url, String method, boolean isUCProxyReq, Map<String, String> headers, Map<String, String> map, Map<String, String> map2, Map<String, byte[]> map3, long uploadFileTotalLen, int requestType, int loadType) {
        if (WVURLInterceptService.getWVABTestHandler() != null && WVUrlUtil.shouldTryABTest(url)) {
            String abTestUrl = WVURLInterceptService.getWVABTestHandler().toABTestUrl(url);
            if (!TextUtils.isEmpty(abTestUrl) && !abTestUrl.equals(url)) {
                TaoLog.i("AliNetworkDelegate", url + " abTestUrl to : " + abTestUrl);
                url = abTestUrl;
            }
        }
        try {
            Request requestImpl = new RequestImpl(url);
            try {
                requestImpl.setFollowRedirects(false);
                requestImpl.setBizId(98);
                requestImpl.setCookieEnabled(WVUCWebView.isNeedCookie(url));
                requestImpl.setMethod(method);
                if (headers != null) {
                    UCNetworkDelegate.getInstance().onSendRequest(headers, url);
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        if (TaoLog.getLogStatus()) {
                            TaoLog.d(this.TAG, "AliRequestAdapter from uc header key=" + key + ",value=" + value);
                        }
                        requestImpl.addHeader(key, value);
                    }
                }
                Request request = requestImpl;
                return requestImpl;
            } catch (Exception e) {
                e = e;
                Request request2 = requestImpl;
                TaoLog.e(this.TAG, " AliRequestAdapter formatAliRequest Exception" + e.getMessage());
                return null;
            }
        } catch (Exception e2) {
            e = e2;
            TaoLog.e(this.TAG, " AliRequestAdapter formatAliRequest Exception" + e.getMessage());
            return null;
        }
    }

    public void setFutureResponse(Future<Response> futureResponse) {
        this.mFutureResponse = futureResponse;
    }

    /* access modifiers changed from: package-private */
    public void complete() {
        if (this.mEventHandler.isSynchronous()) {
            synchronized (this.mClientResource) {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(this.TAG, "AliRequestAdapter complete will notify");
                }
                this.mClientResource.notifyAll();
            }
        }
    }

    public void cancel() {
        try {
            if (!(!TaoLog.getLogStatus() || this.mFutureResponse == null || this.mFutureResponse.get() == null)) {
                TaoLog.d(this.TAG, "AliRequestAdapter cancel desc url=" + this.mFutureResponse.get().getDesc());
            }
            complete();
        } catch (InterruptedException e) {
            e.printStackTrace();
            TaoLog.d(this.TAG, "AliRequestAdapter cancel =" + e.getMessage());
        } catch (ExecutionException e2) {
            e2.printStackTrace();
            TaoLog.d(this.TAG, "AliRequestAdapter cancel =" + e2.getMessage());
        }
        if (this.mFutureResponse != null) {
            this.mFutureResponse.cancel(true);
        }
    }

    public void setEventHandler(EventHandler h) {
        this.mEventHandler = h;
    }

    public EventHandler getEventHandler() {
        return this.mEventHandler;
    }

    public int getRequestType() {
        return this.mRequestType;
    }

    public Map<String, String> getHeaders() {
        return this.mHeaders;
    }

    public Map<String, String> getUCHeaders() {
        return this.mUCHeaders;
    }

    public void handleSslErrorResponse(boolean proceed) {
    }

    public void waitUntilComplete(int timeout) {
        if (this.mEventHandler.isSynchronous()) {
            synchronized (this.mClientResource) {
                try {
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d(this.TAG, "AliRequestAdapter waitUntilComplete timeout=" + timeout + ",url=" + this.mUrl);
                    }
                    this.mClientResource.wait((long) timeout);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public Request getAliRequest() {
        return this.mAliRequest;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public String getMethod() {
        return this.mMethod;
    }

    public boolean getIsUCProxy() {
        return this.mIsUCProxy;
    }

    public Map<String, String> getUploadFileMap() {
        return this.mUploadFileMap;
    }

    public Map<String, byte[]> getUploadDataMap() {
        return this.mUploadDataMap;
    }

    public long getUploadFileTotalLen() {
        return this.mUploadFileTotalLen;
    }

    public int getLoadtype() {
        return this.mLoadType;
    }
}
