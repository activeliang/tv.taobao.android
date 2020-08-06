package mtopsdk.network.impl;

import android.content.Context;
import android.os.Handler;
import anetwork.channel.Network;
import anetwork.channel.degrade.DegradableNetwork;
import anetwork.channel.http.HttpNetwork;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.MockResponse;
import mtopsdk.mtop.global.SwitchConfig;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import mtopsdk.network.AbstractCallImpl;
import mtopsdk.network.NetworkCallback;
import mtopsdk.network.domain.NetworkStats;
import mtopsdk.network.domain.Request;
import mtopsdk.network.domain.Response;
import mtopsdk.network.util.ANetworkConverter;

public class ANetworkCallImpl extends AbstractCallImpl {
    private static final String TAG = "mtopsdk.ANetworkCallImpl";
    static volatile Network mDegradalbeNetwork;
    static volatile Network mHttpNetwork;
    Network mNetwork;

    public ANetworkCallImpl(Request mRequest, Context context) {
        super(mRequest, context);
        if (!SwitchConfig.getInstance().isGlobalSpdySwitchOpen()) {
            if (mHttpNetwork == null) {
                mHttpNetwork = new HttpNetwork(this.mContext);
            }
            this.mNetwork = mHttpNetwork;
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, this.seqNo, "mNetwork=HttpNetwork in ANetworkCallImpl");
                return;
            }
            return;
        }
        if (mDegradalbeNetwork == null) {
            mDegradalbeNetwork = new DegradableNetwork(this.mContext);
        }
        this.mNetwork = mDegradalbeNetwork;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, this.seqNo, "mNetwork=DegradableNetwork in ANetworkCallImpl");
        }
    }

    public Response execute() throws Exception {
        Request request = request();
        int responseCode = 0;
        String responseMessage = null;
        Map<String, List<String>> responseHeaders = null;
        byte[] responseByteData = null;
        NetworkStats networkStats = null;
        MockResponse mockResponse = null;
        if (isDebugApk && isOpenMock && (mockResponse = getMockResponse(request.api)) != null) {
            responseCode = mockResponse.statusCode;
            responseHeaders = mockResponse.headers;
            responseByteData = mockResponse.byteData;
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, this.seqNo, "[execute]get MockResponse succeed.mockResponse=" + mockResponse);
            }
        }
        if (mockResponse == null) {
            anetwork.channel.Response resp = this.mNetwork.syncSend(ANetworkConverter.convertRequest(request), request.reqContext);
            responseCode = resp.getStatusCode();
            responseMessage = resp.getDesc();
            responseHeaders = resp.getConnHeadFields();
            responseByteData = resp.getBytedata();
            networkStats = ANetworkConverter.convertNetworkStats(resp.getStatisticData());
        }
        return buildResponse(request, responseCode, responseMessage, responseHeaders, responseByteData, networkStats);
    }

    public void enqueue(final NetworkCallback callback) {
        Request request = request();
        MockResponse mockResponse = null;
        if (isDebugApk && isOpenMock && (mockResponse = getMockResponse(request.api)) != null) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, this.seqNo, "[enqueue]get MockResponse succeed.mockResponse=" + mockResponse);
            }
            final Response response = buildResponse(request, mockResponse.statusCode, (String) null, mockResponse.headers, mockResponse.byteData, (NetworkStats) null);
            MtopSDKThreadPoolExecutorFactory.submitCallbackTask(this.seqNo != null ? this.seqNo.hashCode() : hashCode(), new Runnable() {
                public void run() {
                    try {
                        callback.onResponse(ANetworkCallImpl.this, response);
                    } catch (Exception e) {
                        TBSdkLog.e(ANetworkCallImpl.TAG, ANetworkCallImpl.this.seqNo, "[enqueue]call NetworkCallback.onResponse error.", e);
                    }
                }
            });
        } else if (mockResponse == null) {
            this.future = this.mNetwork.asyncSend(ANetworkConverter.convertRequest(request), request.reqContext, (Handler) null, new NetworkListenerAdapter(this, callback, request.seqNo));
        }
    }
}
