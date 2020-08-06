package com.yunos.tvtaobao.biz.request.bo;

import com.yunos.tv.blitz.service.BlitzServiceUtils;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class TakeOutOrderCancelData implements Serializable {
    private static final long serialVersionUID = -610484326142980001L;
    private String bizOrderId;
    private String codeType;
    private int codeValue;
    private int errorCode;
    private String errorMsg;
    private boolean success;

    public String getCodeType() {
        return this.codeType;
    }

    public void setCodeType(String codeType2) {
        this.codeType = codeType2;
    }

    public int getCodeValue() {
        return this.codeValue;
    }

    public void setCodeValue(int codeValue2) {
        this.codeValue = codeValue2;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success2) {
        this.success = success2;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode2) {
        this.errorCode = errorCode2;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg2) {
        this.errorMsg = errorMsg2;
    }

    public String getBizOrderId() {
        return this.bizOrderId;
    }

    public void setBizOrderId(String bizOrderId2) {
        this.bizOrderId = bizOrderId2;
    }

    public static TakeOutOrderCancelData resolverFromMtop(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        TakeOutOrderCancelData orderListData = new TakeOutOrderCancelData();
        if (obj == null) {
            return orderListData;
        }
        orderListData.setCodeValue(obj.optInt("codeValue", 0));
        orderListData.setErrorCode(obj.optInt("errorCode"));
        orderListData.setBizOrderId(obj.optString("bizOrderId"));
        orderListData.setCodeType(obj.optString("codeType"));
        orderListData.setErrorMsg(obj.optString("errorMsg"));
        orderListData.setSuccess(obj.optBoolean(BlitzServiceUtils.CSUCCESS, false));
        return orderListData;
    }
}
