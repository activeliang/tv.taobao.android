package mtopsdk.network.impl;

import anetwork.channel.NetworkCallBack;
import anetwork.channel.NetworkEvent;
import anetwork.channel.aidl.ParcelableInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import mtopsdk.network.Call;
import mtopsdk.network.NetworkCallback;
import mtopsdk.network.domain.Response;
import mtopsdk.network.domain.ResponseBody;
import mtopsdk.network.util.ANetworkConverter;
import mtopsdk.network.util.NetworkUtils;

public class NetworkListenerAdapter implements NetworkCallBack.ResponseCodeListener, NetworkCallBack.InputStreamListener, NetworkCallBack.FinishListener {
    private static final String TAG = "mtopsdk.NetworkListenerAdapter";
    ByteArrayOutputStream bos = null;
    Call call;
    NetworkEvent.FinishEvent finishEvent = null;
    Map<String, List<String>> headers;
    boolean isNeedCallFinish = false;
    private volatile boolean isStreamReceived = false;
    NetworkCallback networkCallback;
    int resLength = 0;
    final String seqNo;
    int statusCode;

    public NetworkListenerAdapter(Call call2, NetworkCallback networkCallback2, String seqNo2) {
        this.call = call2;
        this.networkCallback = networkCallback2;
        this.seqNo = seqNo2;
    }

    public boolean onResponseCode(int code, Map<String, List<String>> header, Object context) {
        this.statusCode = code;
        this.headers = header;
        try {
            String contentLength = HeaderHandlerUtil.getSingleHeaderFieldByKey(this.headers, "content-length");
            if (StringUtils.isBlank(contentLength)) {
                contentLength = HeaderHandlerUtil.getSingleHeaderFieldByKey(this.headers, HttpHeaderConstant.X_BIN_LENGTH);
            }
            if (!StringUtils.isNotBlank(contentLength)) {
                return false;
            }
            this.resLength = Integer.parseInt(contentLength);
            return false;
        } catch (Exception e) {
            TBSdkLog.e(TAG, this.seqNo, "[onResponseCode]parse Response HeaderField ContentLength error ");
            return false;
        }
    }

    public void onFinished(NetworkEvent.FinishEvent event, Object context) {
        synchronized (this) {
            this.finishEvent = event;
            if (this.isNeedCallFinish || !this.isStreamReceived) {
                callFinish(event, context);
            }
        }
    }

    public void onInputStreamGet(final ParcelableInputStream inputStream, final Object context) {
        this.isStreamReceived = true;
        MtopSDKThreadPoolExecutorFactory.submitRequestTask(new Runnable() {
            public void run() {
                int total;
                try {
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                        TBSdkLog.d(NetworkListenerAdapter.TAG, NetworkListenerAdapter.this.seqNo, "[onInputStreamGet]start to read input stream");
                    }
                    if (inputStream.length() > 0) {
                        total = inputStream.length();
                    } else {
                        total = NetworkListenerAdapter.this.resLength;
                    }
                    NetworkListenerAdapter.this.bos = new ByteArrayOutputStream(total);
                    byte[] buff = new byte[4096];
                    while (true) {
                        int cnt = inputStream.read(buff);
                        if (cnt == -1) {
                            break;
                        }
                        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                            TBSdkLog.d(NetworkListenerAdapter.TAG, NetworkListenerAdapter.this.seqNo, "[onInputStreamGet]data chunk content: " + new String(buff, 0, cnt));
                        }
                        NetworkListenerAdapter.this.bos.write(buff, 0, cnt);
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Exception e) {
                        }
                    }
                    NetworkUtils.closeQuietly(NetworkListenerAdapter.this.bos);
                } catch (Exception e2) {
                    TBSdkLog.e(NetworkListenerAdapter.TAG, NetworkListenerAdapter.this.seqNo, "[onInputStreamGet]Read data from inputstream failed.", e2);
                    NetworkListenerAdapter.this.bos = null;
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Exception e3) {
                        }
                    }
                    NetworkUtils.closeQuietly(NetworkListenerAdapter.this.bos);
                } catch (Throwable th) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Exception e4) {
                        }
                    }
                    NetworkUtils.closeQuietly(NetworkListenerAdapter.this.bos);
                    throw th;
                }
                synchronized (NetworkListenerAdapter.this) {
                    if (NetworkListenerAdapter.this.finishEvent != null) {
                        NetworkListenerAdapter.this.callFinish(NetworkListenerAdapter.this.finishEvent, context);
                    } else {
                        NetworkListenerAdapter.this.isNeedCallFinish = true;
                    }
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void callFinish(final NetworkEvent.FinishEvent finishEvent2, final Object context) {
        MtopSDKThreadPoolExecutorFactory.submitCallbackTask(this.seqNo != null ? this.seqNo.hashCode() : hashCode(), new Runnable() {
            public void run() {
                try {
                    long start = System.currentTimeMillis();
                    NetworkListenerAdapter.this.onFinishTask(finishEvent2, context);
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                        TBSdkLog.d(NetworkListenerAdapter.TAG, NetworkListenerAdapter.this.seqNo, "[callFinish] execute onFinishTask time[ms] " + (System.currentTimeMillis() - start));
                    }
                } catch (Exception e) {
                    TBSdkLog.e(NetworkListenerAdapter.TAG, NetworkListenerAdapter.this.seqNo, "[callFinish]execute onFinishTask error.", e);
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void onFinishTask(NetworkEvent.FinishEvent event, Object context) {
        if (this.networkCallback == null) {
            TBSdkLog.e(TAG, this.seqNo, "[onFinishTask]networkCallback is null");
            return;
        }
        byte[] byteArray = null;
        if (this.bos != null) {
            byteArray = this.bos.toByteArray();
        }
        final byte[] byteData = byteArray;
        ResponseBody responseBody = new ResponseBody() {
            public String contentType() {
                return HeaderHandlerUtil.getSingleHeaderFieldByKey(NetworkListenerAdapter.this.headers, "Content-Type");
            }

            public long contentLength() throws IOException {
                if (byteData != null) {
                    return (long) byteData.length;
                }
                return 0;
            }

            public InputStream byteStream() {
                return null;
            }

            public byte[] getBytes() throws IOException {
                return byteData;
            }
        };
        this.networkCallback.onResponse(this.call, new Response.Builder().request(this.call.request()).code(event.getHttpCode()).message(event.getDesc()).headers(this.headers).body(responseBody).stat(ANetworkConverter.convertNetworkStats(event.getStatisticData())).build());
    }
}
