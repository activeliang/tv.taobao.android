package com.taobao.alimama.net.pojo.response;

import mtopsdk.mtop.domain.BaseOutDo;

public class P4pCpsInfoResponse extends BaseOutDo {
    private static final long serialVersionUID = -3873455972294398291L;
    private Object location;

    public Object getData() {
        return this.location;
    }

    public void setData(Object obj) {
        this.location = obj;
    }
}
