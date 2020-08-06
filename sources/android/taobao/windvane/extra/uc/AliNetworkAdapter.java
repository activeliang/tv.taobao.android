package android.taobao.windvane.extra.uc;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.taobao.windvane.monitor.AppMonitorUtil;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.taobao.windvane.util.WVUrlUtil;
import android.text.TextUtils;
import anetwork.channel.IBodyHandler;
import anetwork.channel.Network;
import anetwork.channel.NetworkCallBack;
import anetwork.channel.NetworkEvent;
import anetwork.channel.Request;
import anetwork.channel.Response;
import anetwork.channel.anet.ANetwork;
import anetwork.channel.degrade.DegradableNetwork;
import anetwork.channel.http.HttpNetwork;
import com.uc.webview.export.internal.interfaces.EventHandler;
import com.uc.webview.export.internal.interfaces.INetwork;
import com.uc.webview.export.internal.interfaces.IRequest;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.TBSdkLog;
import org.apache.http.ParseException;

public class AliNetworkAdapter implements INetwork {
    public static final int NETWORK_WORKING_MODE_ASYNC = 1;
    public static final int NETWORK_WORKING_MODE_SYNC = 0;
    /* access modifiers changed from: private */
    public int BUFFER_SIZE;
    String LOGTAG;
    private Network mAliNetwork;
    /* access modifiers changed from: private */
    public Context mContext;
    private int mNetworkType;
    private int mWorkingMode;
    public HashSet<EventHandler> mainRequest;

    public AliNetworkAdapter(Context context) {
        this(context, 2);
    }

    public AliNetworkAdapter(Context context, int type) {
        this.LOGTAG = "AliNetwork";
        this.mNetworkType = -1;
        this.mWorkingMode = 1;
        this.BUFFER_SIZE = 1024;
        this.mainRequest = new HashSet<>();
        this.mContext = context;
        this.mNetworkType = type;
        switch (this.mNetworkType) {
            case 0:
                this.mAliNetwork = new HttpNetwork(context);
                return;
            case 1:
                this.mAliNetwork = new ANetwork(context);
                return;
            case 2:
                TBSdkLog.setPrintLog(TaoLog.getLogStatus());
                this.mAliNetwork = new DegradableNetwork(context);
                return;
            default:
                return;
        }
    }

    public boolean requestURL(EventHandler eventhandler, String url, String method, boolean isUCProxyReq, Map<String, String> headers, Map<String, String> ucHeaders, Map<String, String> uploadFileMap, Map<String, byte[]> uploadDataMap, long uploadFileTotalLen, int requestType, int loadType) {
        TaoLog.d(this.LOGTAG, "requestURL:" + url + " isUCProxyReq:" + isUCProxyReq + " requestType:" + requestType);
        AliRequestAdapter requestAdatper = new AliRequestAdapter(eventhandler, Escape.tryDecodeUrl(url), method, isUCProxyReq, headers, ucHeaders, uploadFileMap, uploadDataMap, uploadFileTotalLen, requestType, loadType);
        setRequestBodyHandler(requestAdatper.getAliRequest(), requestAdatper);
        eventhandler.setRequest(requestAdatper);
        return sendRequestInternal(requestAdatper);
    }

    public static boolean willLog(EventHandler ev) {
        int requestType = ev.getResourceType();
        return requestType == 0 || requestType == 14 || ev.isSynchronous();
    }

    private boolean sendRequestInternal(AliRequestAdapter adapterRequest) {
        Request req = adapterRequest.getAliRequest();
        EventHandler handler = adapterRequest.getEventHandler();
        if (TaoLog.getLogStatus()) {
            TaoLog.d(this.LOGTAG, "requestURL eventId=" + adapterRequest.getEventHandler().hashCode() + ", url=" + adapterRequest.getUrl() + ",isSync=" + handler.isSynchronous());
        }
        if (this.mWorkingMode == 0) {
            Response response = this.mAliNetwork.syncSend(req, (Object) null);
            Throwable exception = response.getError();
            if (exception != null) {
                handler.error(getErrorFromException(exception), exception.toString());
            } else {
                int status = response.getStatusCode();
                handler.status(0, 0, status, "");
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(this.LOGTAG, "status code=" + status);
                }
                handler.headers(response.getConnHeadFields());
                byte[] data = response.getBytedata();
                if (data != null) {
                    handler.data(data, data.length);
                }
                handler.endData();
            }
        } else if (this.mWorkingMode == 1) {
            AliNetCallback callback = new AliNetCallback();
            callback.setEventHandler(handler);
            callback.setURL(adapterRequest.getUrl());
            callback.setRequest(adapterRequest);
            adapterRequest.setFutureResponse(this.mAliNetwork.asyncSend(req, (Object) null, (Handler) null, callback));
        }
        return true;
    }

    protected class AliNetCallback implements NetworkCallBack.FinishListener, NetworkCallBack.ResponseCodeListener, NetworkCallBack.ProgressListener {
        EventHandler mEventHandler;
        IRequest mReq;
        String mUrl;

        protected AliNetCallback() {
        }

        public void setEventHandler(EventHandler handler) {
            this.mEventHandler = handler;
        }

        public void setURL(String url) {
            this.mUrl = url;
        }

        public void setRequest(IRequest req) {
            this.mReq = req;
        }

        public void onDataReceived(NetworkEvent.ProgressEvent event, Object context) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(AliNetworkAdapter.this.LOGTAG, "[onDataReceived] event:" + event + "event.getSize():" + event.getSize() + "{data:" + new String(event.getBytedata()) + "}");
            }
            this.mEventHandler.data(event.getBytedata(), event.getSize());
        }

        public boolean onResponseCode(int code, Map<String, List<String>> aliheader, Object context) {
            List<String> valuelist;
            if (TaoLog.getLogStatus()) {
                TaoLog.d(AliNetworkAdapter.this.LOGTAG, "status code=" + code + ",event=" + this.mEventHandler.hashCode() + ",url=" + this.mUrl);
                for (Map.Entry<String, List<String>> entry : aliheader.entrySet()) {
                    String key = entry.getKey();
                    if (!(key == null || (valuelist = entry.getValue()) == null)) {
                        TaoLog.d(AliNetworkAdapter.this.LOGTAG, "header key=" + key + ",value=" + valuelist.toString());
                    }
                }
            }
            try {
                String url = WVUrlUtil.removeScheme(WVUrlUtil.removeQueryParam(this.mUrl));
                Map<String, String> rHeader = this.mReq.getHeaders();
                String referer = "";
                if (rHeader != null) {
                    referer = rHeader.get("Referer");
                }
                String isHTML = "0";
                if (url != null && (url.endsWith(".htm") || url.endsWith(".html") || url.endsWith(WVNativeCallbackUtil.SEPERATER))) {
                    isHTML = "1";
                }
                if ((code < 200 || code > 304) && code != 307) {
                    AppMonitorUtil.commitStatusCode(this.mUrl, referer, String.valueOf(code), isHTML);
                    this.mEventHandler.status(0, 0, code, "");
                    this.mEventHandler.headers(aliheader);
                    return false;
                }
                if (code == 302) {
                    String location = "";
                    if (aliheader != null) {
                        location = (String) aliheader.get("Location").get(0);
                        if (!TextUtils.isEmpty(location)) {
                            location = WVUrlUtil.removeScheme(WVUrlUtil.removeQueryParam(location));
                        }
                    }
                    if (!TextUtils.isEmpty(location)) {
                        if (location.equals("//err.tmall.com/error1.html") || location.equals("//err.taobao.com/error1.html")) {
                            AppMonitorUtil.commitStatusCode(this.mUrl, referer, String.valueOf(404), isHTML);
                        } else if (location.equals("//err.tmall.com/error2.html")) {
                            AppMonitorUtil.commitStatusCode(this.mUrl, referer, String.valueOf(500), isHTML);
                        }
                    }
                }
                this.mEventHandler.status(0, 0, code, "");
                this.mEventHandler.headers(aliheader);
                return false;
            } catch (Throwable e) {
                TaoLog.e(AliNetworkAdapter.this.LOGTAG, "AppMonitorUtil.commitStatusCode error : " + e.getMessage());
            }
        }

        public void onFinished(NetworkEvent.FinishEvent event, Object context) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(AliNetworkAdapter.this.LOGTAG, "[onFinished] event:" + event);
                TaoLog.d(AliNetworkAdapter.this.LOGTAG, "onFinished code = " + event.getHttpCode() + ", url = " + this.mUrl);
            }
            AliRequestAdapter req = (AliRequestAdapter) this.mReq;
            int code = event.getHttpCode();
            UCNetworkDelegate.getInstance().onFinish(code, this.mUrl);
            if (code < 0) {
                if (TaoLog.getLogStatus()) {
                    TaoLog.e(AliNetworkAdapter.this.LOGTAG, "error code=" + code + ",desc=" + event.getDesc() + ",url=" + this.mUrl);
                }
                this.mEventHandler.error(code, event.getDesc());
                req.complete();
                return;
            }
            if (TaoLog.getLogStatus()) {
                TaoLog.d(AliNetworkAdapter.this.LOGTAG, "endData");
            }
            this.mEventHandler.endData();
            req.complete();
        }
    }

    private void setRequestBodyHandler(Request aliRequest, AliRequestAdapter request) {
        if (request.getUploadFileTotalLen() != 0) {
            final Map<String, String> fileMap = request.getUploadFileMap();
            final Map<String, byte[]> dataMap = request.getUploadDataMap();
            final int totalFileNum = fileMap.size() + dataMap.size();
            aliRequest.setBodyHandler(new IBodyHandler() {
                byte[] buffer = new byte[AliNetworkAdapter.this.BUFFER_SIZE];
                int curFilenum = 0;
                byte[] dataValue = null;
                String fileNameValue = null;
                boolean hasInitilized = false;
                InputStream instream = null;
                boolean isCompleted = false;
                int postedLen = 0;

                public void initStream() {
                    try {
                        this.curFilenum = 0;
                        while (this.curFilenum < totalFileNum) {
                            this.fileNameValue = (String) fileMap.get(String.valueOf(this.curFilenum));
                            this.dataValue = (byte[]) dataMap.get(String.valueOf(this.curFilenum));
                            if (TaoLog.getLogStatus() && this.dataValue != null) {
                                TaoLog.d(AliNetworkAdapter.this.LOGTAG, "len =" + this.dataValue.length + ",datavalue=" + new String(this.dataValue, 0, this.dataValue.length));
                            }
                            if (this.fileNameValue == null) {
                                this.instream = new ByteArrayInputStream(this.dataValue);
                            } else if (this.fileNameValue.toLowerCase().startsWith("content://")) {
                                this.instream = AliNetworkAdapter.this.mContext.getContentResolver().openInputStream(Uri.parse(this.fileNameValue));
                            } else {
                                this.instream = new FileInputStream(this.fileNameValue);
                            }
                            this.curFilenum++;
                        }
                    } catch (Exception e) {
                    }
                }

                public int read(byte[] netBuffer) {
                    if (!this.hasInitilized) {
                        initStream();
                        this.hasInitilized = true;
                    }
                    int dataLen = 0;
                    try {
                        dataLen = this.instream.read(this.buffer, 0, AliNetworkAdapter.this.BUFFER_SIZE > netBuffer.length ? netBuffer.length : AliNetworkAdapter.this.BUFFER_SIZE);
                        if (dataLen != -1) {
                            if (TaoLog.getLogStatus()) {
                                TaoLog.d(AliNetworkAdapter.this.LOGTAG, "current read len=" + dataLen + ",posted=" + this.postedLen + ",data" + new String(this.buffer, 0, dataLen));
                            }
                            System.arraycopy(this.buffer, 0, netBuffer, 0, dataLen);
                            this.postedLen += dataLen;
                            return dataLen;
                        }
                        if (TaoLog.getLogStatus()) {
                            TaoLog.d(AliNetworkAdapter.this.LOGTAG, "read error or finish len=" + dataLen);
                        }
                        this.isCompleted = true;
                        this.instream.close();
                        return 0;
                    } catch (Exception e) {
                        TaoLog.e(AliNetworkAdapter.this.LOGTAG, "read exception" + e.getMessage());
                    }
                }

                public boolean isCompleted() {
                    if (!this.isCompleted) {
                        return this.isCompleted;
                    }
                    this.isCompleted = false;
                    try {
                        this.instream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
        }
    }

    public int getErrorFromException(Throwable exception) {
        if (exception instanceof ParseException) {
            return -43;
        }
        if (exception instanceof SocketTimeoutException) {
            return -46;
        }
        if (exception instanceof SocketException) {
            return -47;
        }
        if (exception instanceof IOException) {
            return -44;
        }
        if (exception instanceof IllegalStateException) {
            return -45;
        }
        if (exception instanceof UnknownHostException) {
            return -2;
        }
        return -99;
    }

    public void cancelPrefetchLoad() {
    }

    public void clearUserSslPrefTable() {
    }

    public IRequest formatRequest(EventHandler handler, String url, String method, boolean isUCProxyReq, Map<String, String> headers, Map<String, String> ucHeaders, Map<String, String> uploadFileMap, Map<String, byte[]> uploadDataMap, long uploadFileTotalLen, int requestType, int loadType) {
        AliRequestAdapter requestAdatper = new AliRequestAdapter(handler, Escape.tryDecodeUrl(url), method, isUCProxyReq, headers, ucHeaders, uploadFileMap, uploadDataMap, uploadFileTotalLen, requestType, loadType);
        setRequestBodyHandler(requestAdatper.getAliRequest(), requestAdatper);
        handler.setRequest(requestAdatper);
        handler.setResourceType(requestType);
        return requestAdatper;
    }

    public boolean sendRequest(IRequest request) {
        return sendRequestInternal((AliRequestAdapter) request);
    }

    public String getVersion() {
        return "1.0";
    }

    public int getNetworkType() {
        return 1;
    }
}
