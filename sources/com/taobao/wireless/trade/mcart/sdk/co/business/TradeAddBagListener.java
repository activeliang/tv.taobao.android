package com.taobao.wireless.trade.mcart.sdk.co.business;

import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.MtopResponse;

public abstract class TradeAddBagListener extends AbstractCartRemoteBaseListener {
    public abstract void onSuccessExt(int i, MtopResponse mtopResponse, BaseOutDo baseOutDo, Object obj);

    public TradeAddBagListener(CartFrom cartFrom) {
        super(cartFrom);
    }

    public void onSuccess(int requestType, MtopResponse response, BaseOutDo pojo, Object context) {
        super.onSuccess(requestType, response, pojo, context);
        onSuccessExt(requestType, response, pojo, context);
    }
}
