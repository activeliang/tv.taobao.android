package com.ali.user.open.mtop.rpc;

import android.text.TextUtils;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.config.ConfigManager;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.exception.RpcException;
import com.ali.user.open.core.model.LoginReturnData;
import com.ali.user.open.core.model.RpcRequest;
import com.ali.user.open.core.model.RpcRequestCallbackWithCode;
import com.ali.user.open.core.model.RpcResponse;
import com.ali.user.open.core.trace.SDKLogger;
import com.taobao.tao.remotebusiness.IRemoteBaseListener;
import com.taobao.tao.remotebusiness.MtopBusiness;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.intf.MtopBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public class MTOPWrapper {
    private static MTOPWrapper INSTANCE = null;
    private static final int MTOP_BIZ_CODE = 94;
    private static final String TAG = "login.MTOPWrapperImpl";

    public static synchronized MTOPWrapper getInstance() {
        MTOPWrapper mTOPWrapper;
        synchronized (MTOPWrapper.class) {
            if (INSTANCE == null) {
                INSTANCE = new MTOPWrapper();
            }
            mTOPWrapper = INSTANCE;
        }
        return mTOPWrapper;
    }

    public String post(RpcRequest request) {
        return post(request, LoginReturnData.class).toString();
    }

    public <V> RpcResponse<V> post(RpcRequest request, Class<V> targetClass) {
        return post(request, targetClass, (String) null);
    }

    public <V> RpcResponse<V> post(RpcRequest rpcRequest, Class<V> v, String userId) {
        MtopResponse response = null;
        try {
            MtopBuilder mtopBuilder = Mtop.instance(Mtop.Id.INNER, KernelContext.applicationContext).build(buildMtopRequest(rpcRequest), AliMemberSDK.ttid).reqMethod(MethodEnum.POST).setBizId(94).setConnectionTimeoutMilliSecond(10000);
            if (!TextUtils.isEmpty(userId)) {
                mtopBuilder.setReqUserId(userId);
            }
            mtopBuilder.retryTime(1);
            mtopBuilder.setCustomDomain(TextUtils.isEmpty(ConfigManager.getInstance().onlineDomain) ? "acs.m.taobao.com" : ConfigManager.getInstance().onlineDomain, TextUtils.isEmpty(ConfigManager.getInstance().preDomain) ? "acs.wapa.taobao.com" : ConfigManager.getInstance().preDomain, TextUtils.isEmpty(ConfigManager.getInstance().dailyDomain) ? "acs.waptest.taobao.com" : ConfigManager.getInstance().dailyDomain);
            response = mtopBuilder.syncRequest();
            SDKLogger.d(TAG, new StringBuilder().append("receive MtopResponse = ").append(response).toString() == null ? "  null" : response.toString());
        } catch (Exception e) {
            SDKLogger.e(TAG, "MtopResponse error", e);
            e.printStackTrace();
        }
        if (response != null) {
            return processMtopResponse(response, v);
        }
        SDKLogger.e(TAG, "MtopResponse response=null");
        return null;
    }

    private MtopRequest buildMtopRequest(RpcRequest rpcRequest) throws JSONException {
        MtopRequest mtopRequest = new MtopRequest();
        mtopRequest.setApiName(rpcRequest.target);
        mtopRequest.setVersion(rpcRequest.version);
        mtopRequest.setNeedEcode(rpcRequest.NEED_ECODE);
        mtopRequest.setNeedSession(rpcRequest.NEED_SESSION);
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < rpcRequest.paramNames.size(); i++) {
            if (rpcRequest.paramNames.get(i) != null) {
                jsonObject.put(rpcRequest.paramNames.get(i), rpcRequest.paramValues.get(i).toString());
            }
        }
        mtopRequest.setData(jsonObject.toString());
        return mtopRequest;
    }

    /* access modifiers changed from: private */
    public <V> RpcResponse<V> processMtopResponse(MtopResponse mtopResponse, Class<V> v) {
        if (mtopResponse != null && mtopResponse.isApiSuccess()) {
            return getBizRpcResponse(mtopResponse, v);
        }
        if (mtopResponse == null) {
            return null;
        }
        if (mtopResponse.isNetworkError()) {
            throw new RpcException((Integer) 7, mtopResponse.getRetMsg());
        } else if (mtopResponse.isApiLockedResult()) {
            throw new RpcException((Integer) 400, mtopResponse.getRetMsg());
        } else if (mtopResponse.is41XResult()) {
            throw new RpcException((Integer) 401, mtopResponse.getRetMsg());
        } else if (mtopResponse.isExpiredRequest()) {
            throw new RpcException((Integer) 402, mtopResponse.getRetMsg());
        } else if (mtopResponse.isIllegelSign()) {
            throw new RpcException((Integer) 403, mtopResponse.getRetMsg());
        } else if (mtopResponse.isSystemError()) {
            throw new RpcException((Integer) 406, mtopResponse.getRetMsg());
        } else if (mtopResponse.isSessionInvalid()) {
            throw new RpcException((Integer) 407, mtopResponse.getRetMsg());
        } else if (mtopResponse.isMtopServerError()) {
            throw new RpcException((Integer) 406, mtopResponse.getRetMsg());
        } else if (!mtopResponse.isMtopSdkError()) {
            return getBizRpcResponse(mtopResponse, v);
        } else {
            throw new RpcException((Integer) 406, mtopResponse.getRetMsg());
        }
    }

    private <V> RpcResponse<V> getBizRpcResponse(MtopResponse mtopResponse, Class<V> v) {
        try {
            return getRpcResponse(mtopResponse, v);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARNING: type inference failed for: r8v0, types: [java.lang.Class<V>, java.lang.Class] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private <V> com.ali.user.open.core.model.RpcResponse<V> getRpcResponse(mtopsdk.mtop.domain.MtopResponse r7, java.lang.Class<V> r8) throws org.json.JSONException {
        /*
            r6 = this;
            com.ali.user.open.core.model.RpcResponse r4 = new com.ali.user.open.core.model.RpcResponse
            r4.<init>()
            byte[] r3 = r7.getBytedata()
            if (r3 == 0) goto L_0x0065
            java.lang.String r2 = new java.lang.String
            r2.<init>(r3)
            org.json.JSONObject r1 = new org.json.JSONObject
            r1.<init>(r2)
            java.lang.String r5 = "data"
            org.json.JSONObject r0 = r1.optJSONObject(r5)
            if (r0 == 0) goto L_0x0065
            java.lang.String r5 = "code"
            int r5 = r0.optInt(r5)
            r4.code = r5
            java.lang.String r5 = "codeGroup"
            java.lang.String r5 = r0.optString(r5)
            r4.codeGroup = r5
            java.lang.String r5 = "message"
            java.lang.String r5 = r0.optString(r5)
            r4.message = r5
            java.lang.String r5 = "actionType"
            java.lang.String r5 = r0.optString(r5)
            r4.actionType = r5
            java.lang.String r5 = "success"
            boolean r5 = r0.optBoolean(r5)
            r4.success = r5
            java.lang.String r5 = "returnValue"
            java.lang.String r5 = r0.optString(r5)
            boolean r5 = android.text.TextUtils.isEmpty(r5)
            if (r5 != 0) goto L_0x0065
            java.lang.String r5 = "returnValue"
            java.lang.String r5 = r0.optString(r5)
            java.lang.Object r5 = com.ali.user.open.core.util.JSONUtils.parseStringValue(r5, r8)
            r4.returnValue = r5
        L_0x0065:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.open.mtop.rpc.MTOPWrapper.getRpcResponse(mtopsdk.mtop.domain.MtopResponse, java.lang.Class):com.ali.user.open.core.model.RpcResponse");
    }

    public <T> void remoteBusiness(RpcRequest request, final Class<T> resultType, final RpcRequestCallbackWithCode rpcRequestCallback) {
        if (request != null && rpcRequestCallback != null) {
            try {
                MtopBusiness business = MtopBusiness.build(Mtop.instance(Mtop.Id.INNER, KernelContext.applicationContext), buildMtopRequest(request), AliMemberSDK.ttid);
                business.setCustomDomain(TextUtils.isEmpty(ConfigManager.getInstance().onlineDomain) ? "acs.m.taobao.com" : ConfigManager.getInstance().onlineDomain, TextUtils.isEmpty(ConfigManager.getInstance().preDomain) ? "acs.wapa.taobao.com" : ConfigManager.getInstance().preDomain, TextUtils.isEmpty(ConfigManager.getInstance().dailyDomain) ? "acs.waptest.taobao.com" : ConfigManager.getInstance().dailyDomain);
                business.addListener((MtopListener) new IRemoteBaseListener() {
                    public void onSuccess(int requestType, MtopResponse response, BaseOutDo pojo, Object requestContext) {
                        rpcRequestCallback.onSuccess(MTOPWrapper.this.processMtopResponse(response, resultType));
                    }

                    public void onError(int requestType, MtopResponse response, Object requestContext) {
                        String errorCode = "-1";
                        if (response != null) {
                            errorCode = response.getRetCode();
                        }
                        try {
                            rpcRequestCallback.onError(errorCode, MTOPWrapper.this.processMtopResponse(response, resultType));
                        } catch (RpcException e) {
                            RpcResponse error = new RpcResponse();
                            error.code = e.getCode();
                            error.message = "亲，您的手机网络不太顺畅哦~";
                            rpcRequestCallback.onError(errorCode, error);
                        }
                    }

                    public void onSystemError(int requestType, MtopResponse response, Object requestContext) {
                        String errorCode = "-1";
                        if (response != null) {
                            errorCode = response.getRetCode();
                        }
                        try {
                            rpcRequestCallback.onSystemError(errorCode, MTOPWrapper.this.processMtopResponse(response, resultType));
                        } catch (RpcException e) {
                            RpcResponse error = new RpcResponse();
                            error.code = e.getCode();
                            error.message = "亲，您的手机网络不太顺畅哦~";
                            rpcRequestCallback.onSystemError(errorCode, error);
                        }
                    }
                });
                business.startRequest();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
