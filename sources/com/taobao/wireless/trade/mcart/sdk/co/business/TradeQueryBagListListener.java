package com.taobao.wireless.trade.mcart.sdk.co.business;

import com.alibaba.fastjson.JSONObject;
import com.taobao.tao.remotebusiness.IRemoteCacheListener;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ComponentBizUtil;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeQueryBagListResponse;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;
import com.taobao.wireless.trade.mcart.sdk.engine.CartEngineContext;
import com.taobao.wireless.trade.mcart.sdk.engine.CartEngineForMtop;
import com.taobao.wireless.trade.mcart.sdk.engine.CartParseModule;
import mtopsdk.mtop.common.MtopCacheEvent;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.MtopResponse;

public abstract class TradeQueryBagListListener extends AbstractCartRemoteBaseListener implements IRemoteCacheListener {
    public abstract void onCachedBefore(MtopCacheEvent mtopCacheEvent, BaseOutDo baseOutDo, Object obj);

    public abstract void onCachedExt(MtopCacheEvent mtopCacheEvent, BaseOutDo baseOutDo, Object obj, DataProcessResult dataProcessResult);

    public abstract void onSuccessBefore(int i, MtopResponse mtopResponse, BaseOutDo baseOutDo, Object obj);

    public abstract void onSuccessExt(int i, MtopResponse mtopResponse, BaseOutDo baseOutDo, Object obj, DataProcessResult dataProcessResult);

    public TradeQueryBagListListener(CartFrom cartFrom) {
        super(cartFrom);
    }

    public void onSuccess(int requestType, MtopResponse response, BaseOutDo pojo, Object context) {
        super.onSuccess(requestType, response, pojo, context);
        onSuccessBefore(requestType, response, pojo, context);
        onSuccessExt(requestType, response, pojo, context, dataProcess(pojo, response, false));
    }

    private DataProcessResult dataProcess(BaseOutDo pojo, MtopResponse response, boolean fromCache) {
        JSONObject data;
        JSONObject controlParasOfCache;
        long onSuccessStartTime = System.currentTimeMillis();
        DataProcessResult dataProcessResult = new DataProcessResult();
        if (!(pojo == null || !(pojo instanceof MtopTradeQueryBagListResponse) || (data = ((MtopTradeQueryBagListResponse) pojo).getData()) == null)) {
            CartParseModule parseModule = CartEngineForMtop.getInstance(this.cartFrom).getParseModule();
            long step2Time = System.currentTimeMillis();
            parseModule.parse(data);
            dataProcessResult.setParseModuleParseStructTime(System.currentTimeMillis() - step2Time);
            CartEngineContext cartEngineContext = CartEngineForMtop.getInstance(this.cartFrom).getContext();
            if (fromCache && cartEngineContext != null) {
                int pageNo = 0;
                JSONObject pageMeta = cartEngineContext.getPageMeta();
                if (pageMeta != null && pageMeta.containsKey("pageNo")) {
                    pageNo = pageMeta.getIntValue("pageNo");
                }
                if (pageNo == 1 && (controlParasOfCache = cartEngineContext.getControlParas()) != null && controlParasOfCache.containsKey("eTag")) {
                    CartEngineForMtop.getInstance(this.cartFrom).seteTagOfCache(controlParasOfCache.getString("eTag"));
                }
            }
        }
        refreshFooterComponentInfo();
        dataProcessResult.setSuccessTotalTime(System.currentTimeMillis() - onSuccessStartTime);
        return dataProcessResult;
    }

    public void refreshFooterComponentInfo() {
        ComponentBizUtil.refreshAllComponentChangeToCheckedStatus(this.cartFrom);
        ComponentBizUtil.refreshRealQuantityWeightAndSubmitComponentInfo(this.cartFrom);
    }

    public void onCached(MtopCacheEvent event, BaseOutDo pojo, Object context) {
        onCachedBefore(event, pojo, context);
        MtopResponse response = null;
        if (event != null) {
            response = event.getMtopResponse();
        }
        onCachedExt(event, pojo, context, dataProcess(pojo, response, true));
    }
}
