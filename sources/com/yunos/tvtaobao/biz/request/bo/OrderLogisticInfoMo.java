package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class OrderLogisticInfoMo implements Serializable {
    private static final long serialVersionUID = -3000507618972262600L;
    private String message;
    private String time;

    public String getTime() {
        return this.time;
    }

    public void setTime(String time2) {
        this.time = time2;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }
}
