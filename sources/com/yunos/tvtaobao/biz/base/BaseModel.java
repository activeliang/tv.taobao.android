package com.yunos.tvtaobao.biz.base;

import com.yunos.tvtaobao.biz.request.BusinessRequest;

public class BaseModel implements IModel {
    public BusinessRequest mBusinessRequest = new BusinessRequest();

    public void onDestroy() {
        this.mBusinessRequest = null;
    }
}
