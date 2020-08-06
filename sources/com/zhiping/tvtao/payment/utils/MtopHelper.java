package com.zhiping.tvtao.payment.utils;

import android.content.Context;
import android.util.Log;
import anetwork.channel.util.RequestConstant;
import com.alibaba.baichuan.android.trade.adapter.mtop.AlibcMtop;
import com.alibaba.baichuan.android.trade.adapter.mtop.NetworkRequest;
import com.alibaba.baichuan.android.trade.adapter.mtop.NetworkResponse;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.zhiping.tvtao.payment.AlipayManager;
import com.zhiping.tvtao.payment.alipay.request.base.BaseMtopRequest;
import com.zhiping.tvtao.payment.alipay.request.base.MtopResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.xstate.util.XStateConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class MtopHelper {
    private static boolean isThird = false;
    static BlockingDeque<Runnable> requestQueue = new LinkedBlockingDeque();
    private static Context sAppContext;
    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2, 300, TimeUnit.SECONDS, requestQueue);

    public interface MtopListener {
        void onFinish(BaseMtopRequest baseMtopRequest, MtopResponse mtopResponse);
    }

    public static void init(Context context, boolean isThird2) {
        if (context != null) {
            sAppContext = context.getApplicationContext();
        }
        isThird = isThird2;
        if (isThird2) {
            AlibcMtop.getInstance().init();
        } else {
            Mtop.instance(sAppContext);
        }
    }

    public static MtopResponse baseRequest(BaseMtopRequest request) {
        Map<String, String> extraParams;
        Map<String, String> extraParams2;
        if (isThird) {
            NetworkRequest networkRequest = new NetworkRequest();
            networkRequest.apiName = request.getApi();
            networkRequest.apiVersion = request.getApiVersion();
            networkRequest.isPost = request.isPost();
            networkRequest.needLogin = request.isNeedLogin();
            networkRequest.needWua = request.isNeedWua();
            networkRequest.needAuth = request.isNeedAuth();
            networkRequest.needCache = request.isNeedCache();
            if (request.getTimeOut() > 0) {
                networkRequest.timeOut = request.getTimeOut();
            }
            if (request.isNeedUmt()) {
                if (networkRequest.paramMap == null) {
                    networkRequest.paramMap = new HashMap();
                }
                try {
                    networkRequest.paramMap.put(XStateConstants.KEY_UMID_TOKEN, SecurityGuardManager.getInstance(sAppContext).getUMIDComp().getSecurityToken());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Map<String, String> params = request.getParams();
            if (params != null) {
                if (networkRequest.paramMap == null) {
                    networkRequest.paramMap = new HashMap();
                }
                networkRequest.paramMap.putAll(params);
            }
            if (!(AlipayManager.getBizInfoProvider() == null || (extraParams2 = AlipayManager.getBizInfoProvider().extraParamsForMtop(request.getApi(), request.getApiVersion())) == null)) {
                if (networkRequest.paramMap == null) {
                    networkRequest.paramMap = new HashMap();
                }
                networkRequest.paramMap.putAll(extraParams2);
            }
            NetworkResponse response = AlibcMtop.getInstance().sendRequest(networkRequest);
            MtopResponse mtopResponse = new MtopResponse();
            mtopResponse.setErrorCode(response.errorCode);
            mtopResponse.setErrorMsg(response.errorMsg);
            try {
                mtopResponse.setJsonData(new JSONObject(response.jsonData));
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
            mtopResponse.setHttpCode(response.httpCode);
            mtopResponse.setSuccess(response.isSuccess);
            return mtopResponse;
        }
        try {
            MtopRequest mtopRequest = new MtopRequest();
            mtopRequest.setApiName(request.getApi());
            mtopRequest.setVersion(request.getApiVersion());
            mtopRequest.dataParams = request.getParams();
            if (request.isNeedUmt()) {
                if (mtopRequest.dataParams == null) {
                    mtopRequest.dataParams = new HashMap();
                }
                try {
                    mtopRequest.dataParams.put(XStateConstants.KEY_UMID_TOKEN, SecurityGuardManager.getInstance(sAppContext).getUMIDComp().getSecurityToken());
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
            if (!(AlipayManager.getBizInfoProvider() == null || (extraParams = AlipayManager.getBizInfoProvider().extraParamsForMtop(request.getApi(), request.getApiVersion())) == null)) {
                if (mtopRequest.dataParams == null) {
                    mtopRequest.dataParams = new HashMap();
                }
                mtopRequest.dataParams.putAll(extraParams);
            }
            mtopRequest.setNeedEcode(request.isNeedEcode());
            mtopRequest.setNeedSession(request.isNeedLogin());
            if (mtopRequest.dataParams != null) {
                JSONObject jsonData = new JSONObject();
                for (Object key : mtopRequest.dataParams.keySet()) {
                    jsonData.put("" + key, "" + mtopRequest.dataParams.get(key));
                }
                mtopRequest.setData(jsonData.toString());
            }
            mtopsdk.mtop.domain.MtopResponse response2 = Mtop.instance((Context) null).build(mtopRequest, (String) null).syncRequest();
            Log.d(RequestConstant.ENV_TEST, response2.getRetMsg());
            MtopResponse mtopResponse2 = new MtopResponse();
            mtopResponse2.setSuccess(response2.isApiSuccess());
            mtopResponse2.setHttpCode(response2.getResponseCode() + "");
            mtopResponse2.setJsonData(response2.getDataJsonObject());
            mtopResponse2.setErrorMsg(response2.getRetMsg());
            mtopResponse2.setErrorCode(response2.getRetCode());
            return mtopResponse2;
        } catch (Exception e4) {
            e4.printStackTrace();
            return null;
        }
    }

    public static void asycSendRequest(BaseMtopRequest request, MtopListener listener) {
        threadPoolExecutor.execute(new RequestJob(request, listener));
    }

    static class RequestJob implements Runnable {
        MtopListener mtopListener;
        BaseMtopRequest mtopRequest;

        public RequestJob(BaseMtopRequest request, MtopListener listener) {
            this.mtopRequest = request;
            this.mtopListener = listener;
        }

        public void run() {
            MtopResponse mtopResponse = MtopHelper.baseRequest(this.mtopRequest);
            if (this.mtopListener != null) {
                this.mtopListener.onFinish(this.mtopRequest, mtopResponse);
            }
        }
    }

    public static void dispose() {
    }
}
