package com.yunos.tvtaobao.biz.widget.newsku.presenter;

import com.yunos.tvtaobao.biz.base.IPresenter;
import com.yunos.tvtaobao.biz.request.bo.ItemListBean;

public interface ITakeOutSkuPresent extends IPresenter {
    void initSkuPropsList(ItemListBean itemListBean);

    void onChooseSkuComplete(int i);
}
