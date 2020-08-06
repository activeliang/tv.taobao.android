package com.yunos.tvtaobao.biz.request.bo;

public class ElemeOauthBo {
    private String actionType;
    private String code;
    private String message;
    private ElemeOauthReturnValueBo returnValue;

    public String getActionType() {
        return this.actionType;
    }

    public void setActionType(String actionType2) {
        this.actionType = actionType2;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code2) {
        this.code = code2;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

    public ElemeOauthReturnValueBo getReturnValue() {
        return this.returnValue;
    }

    public void setReturnValue(ElemeOauthReturnValueBo returnValue2) {
        this.returnValue = returnValue2;
    }
}
