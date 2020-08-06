package com.yunos.tv.tao.speech.client.domain.result.multisearch.trade;

import com.tvtaobao.voicesdk.bean.BillModel;
import java.io.Serializable;

public class TradeBillResultDO implements Serializable {
    public static final String DATETIME_BEGIN_ERROR = "DATETIME_BEGIN_ERROR";
    public static final String DATETIME_RANGE_ERROR = "DATETIME_RANGE_ERROR";
    public static final String TYPE_ERROR_DATETIME_NOT_IDENTIFY = "DATETIME_NOT_IDENTIFY";
    public static final String TYPE_ERROR_EMPTY_DATA = "EMPTY_DATA";
    public static final String TYPE_ERROR_ILLEGAL_REQUEST = "ILLEGAL_REQUEST_PARAMETER";
    private String code;
    private String end;
    private String errorCode;
    private BillModel model;

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

    public void setModel(BillModel model2) {
        this.model = model2;
    }

    public BillModel getModel() {
        return this.model;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode2) {
        this.errorCode = errorCode2;
    }
}
