package com.taobao.alimama.net.pojo.response;

import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.IMTOPDataObject;

public class AlimamaTkQueryChanneleResponse extends BaseOutDo implements IMTOPDataObject {
    private Object data;

    public Object getData() {
        return this.data;
    }

    public void setData(Object obj) {
        this.data = obj;
    }
}
