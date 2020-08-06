package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao;

import java.io.Serializable;

public class ApiStackBean implements Serializable {
    public String name;
    public String value;

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value2) {
        this.value = value2;
    }
}
