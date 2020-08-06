package com.yunos.tvtaobao.biz.widget.newsku.presenter;

import com.yunos.tvtaobao.biz.base.IPresenter;
import com.yunos.tvtaobao.biz.request.bo.TBDetailResultV6;

public interface ITvTaoSkuPresenter extends IPresenter {
    void doDetailData(TBDetailResultV6 tBDetailResultV6);

    void doDetailRequest(String str, String str2);

    void setDefaultSku(String str);
}
