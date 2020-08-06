package com.yunos.tv.tao.speech.client.domain.result.multisearch.trade;

import com.tvtaobao.voicesdk.bean.LogisticsDo;
import java.io.Serializable;
import java.util.List;

public class TradeLogisticsResultDo implements Serializable {
    private String code;
    private String end;
    private String errorCode;
    private List<LogisticsDo> model;

    public void setCode(String code2) {
        this.code = code2;
    }

    public String getCode() {
        return this.code;
    }

    public void setEnd(String end2) {
        this.end = end2;
    }

    public String getEnd() {
        return this.end;
    }

    public void setModel(List<LogisticsDo> model2) {
        this.model = model2;
    }

    public List<LogisticsDo> getModel() {
        return this.model;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode2) {
        this.errorCode = errorCode2;
    }
}
