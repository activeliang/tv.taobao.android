package com.taobao.wireless.trade.mcart.sdk.co.business;

import android.text.TextUtils;
import com.taobao.tao.remotebusiness.IRemoteBaseListener;
import com.taobao.wireless.trade.mcart.sdk.co.service.CartMessage;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;
import com.taobao.wireless.trade.mcart.sdk.protocol.RequestDebug;
import com.taobao.wireless.trade.mcart.sdk.utils.CartLogProfiler;
import com.taobao.wireless.trade.mcart.sdk.utils.StringUtils;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.util.ErrorConstant;

public abstract class AbstractCartRemoteBaseListener implements IRemoteBaseListener {
    private static final String TAG = "Cart.AbstractCartRemoteBaseListener ";
    protected CartFrom cartFrom = CartFrom.DEFAULT_CLIENT;
    private RequestDebug requestDebug;

    public abstract void onErrorExt(int i, MtopResponse mtopResponse, Object obj, CartMessage cartMessage);

    public abstract void onSystemErrorExt(int i, MtopResponse mtopResponse, Object obj, CartMessage cartMessage);

    private AbstractCartRemoteBaseListener() {
    }

    public AbstractCartRemoteBaseListener(CartFrom cartFrom2) {
        this.cartFrom = cartFrom2 == null ? CartFrom.DEFAULT_CLIENT : cartFrom2;
    }

    public void setRequestDebug(RequestDebug requestDebug2) {
        this.requestDebug = requestDebug2;
    }

    public void onSuccess(int requestType, MtopResponse response, BaseOutDo pojo, Object context) {
        if (this.requestDebug != null) {
            this.requestDebug.onResponseBack(requestType, response, pojo);
        }
    }

    public void onError(int requestType, MtopResponse response, Object requestContext) {
        logE(response);
        CartMessage cartMessage = new CartMessage();
        if (response != null && !StringUtils.isBlank(response.getRetMsg())) {
            cartMessage.setErrorMessage(response.getRetMsg());
        }
        onErrorExt(requestType, response, requestContext, cartMessage);
    }

    public void onSystemError(int requestType, MtopResponse response, Object requestContext) {
        logE(response);
        String mtopErrorMsg = "";
        if (response != null) {
            mtopErrorMsg = response.getRetMsg();
        }
        CartMessage cartMessage = new CartMessage();
        if (TextUtils.isEmpty(mtopErrorMsg)) {
            mtopErrorMsg = "小二很忙，系统很累，请稍后重试";
            if (response != null && response.isApiLockedResult()) {
                mtopErrorMsg = ErrorConstant.MappingMsg.FLOW_LIMIT_MAPPING_MSG;
            }
        }
        cartMessage.setErrorMessage(mtopErrorMsg);
        onSystemErrorExt(requestType, response, requestContext, cartMessage);
    }

    private void logE(MtopResponse response) {
        if (response != null) {
            StringBuffer buffer = new StringBuffer();
            String api = response.getApi();
            if (!TextUtils.isEmpty(api)) {
                buffer.append("api:").append(api).append(",");
            }
            String retCode = response.getRetCode();
            if (!TextUtils.isEmpty(retCode)) {
                buffer.append("retCode:").append(retCode).append(",");
            }
            String retMsg = response.getRetMsg();
            if (!TextUtils.isEmpty(retMsg)) {
                buffer.append("retMsg:").append(retMsg).append(",");
            }
            buffer.append("responseCode:").append(response.getResponseCode()).append(",");
            String mappingCode = response.getMappingCode();
            if (!TextUtils.isEmpty(mappingCode)) {
                buffer.append("mappingCode:").append(mappingCode);
            }
            CartLogProfiler.e(TAG, buffer.toString());
        }
    }
}
