package com.yunos.tvtaobao.biz.request.bo;

public class ElemeRecommandBo {
    private String code;
    private ElemeRecommandReturnValueBo returnValue;

    public ElemeRecommandReturnValueBo getReturnValue() {
        return this.returnValue;
    }

    public void setReturnValue(ElemeRecommandReturnValueBo returnValue2) {
        this.returnValue = returnValue2;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code2) {
        this.code = code2;
    }
}
