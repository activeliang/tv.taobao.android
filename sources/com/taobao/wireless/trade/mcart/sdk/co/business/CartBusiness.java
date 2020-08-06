package com.taobao.wireless.trade.mcart.sdk.co.business;

import android.content.Context;
import com.taobao.tao.remotebusiness.IRemoteBaseListener;
import com.taobao.tao.remotebusiness.IRemoteListener;
import com.taobao.tao.remotebusiness.MtopBusiness;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeAddBagRequest;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeAddBagResponse;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeBagToFavorRequest;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeBagToFavorResponse;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeBatchDelBagResponse;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeCheckCartItemResponse;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeItemRecommendRequest;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeItemRecommendResponse;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeQueryBagListRequest;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeQueryBagListResponse;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeUnfoldShopRequest;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeUnfoldShopResponse;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeUpdateBagCountResponse;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeUpdateCartSkuResponse;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeUpdateRequest;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.intf.MtopUnitStrategy;

public class CartBusiness {
    private static final String TAG = "Cart.CartBusiness";
    private int retryTime = 1;

    public void queryBagList(MtopTradeQueryBagListRequest request, IRemoteBaseListener listener, Context context, String ttid, boolean refreshCache, int bizId, boolean queryCartNextByPost) {
        MtopBusiness business;
        if (queryCartNextByPost) {
            if (refreshCache) {
                business = getRemoteBusinessByGet(request, listener, context, ttid, bizId);
                business.setErrorNotifyNeedAfterCache(true);
            } else {
                business = getRemoteBusinessByPost(request, listener, context, ttid, bizId);
            }
        } else if (request.getP() == null || request.getDataMd5() != null) {
            business = getRemoteBusinessByGet(request, listener, context, ttid, bizId);
            business.setErrorNotifyNeedAfterCache(true);
        } else {
            business = getRemoteBusinessByPost(request, listener, context, ttid, bizId);
        }
        business.startRequest(MtopTradeQueryBagListResponse.class);
    }

    public void batchDelBag(MtopTradeUpdateRequest request, IRemoteBaseListener listener, Context context, String ttid, int bizId) {
        getRemoteBusinessByPost(request, listener, context, ttid, bizId).startRequest(MtopTradeBatchDelBagResponse.class);
    }

    public void unfoldShop(MtopTradeUnfoldShopRequest request, IRemoteBaseListener listener, Context context, String ttid, int bizId) {
        getRemoteBusinessByPost(request, listener, context, ttid, bizId).startRequest(MtopTradeUnfoldShopResponse.class);
    }

    public void bagToFavor(MtopTradeBagToFavorRequest request, IRemoteBaseListener listener, Context context, String ttid, int bizId) {
        getRemoteBusinessByPost(request, listener, context, ttid, bizId).startRequest(MtopTradeBagToFavorResponse.class);
    }

    public void addBag(MtopTradeAddBagRequest request, IRemoteBaseListener listener, Context context, String ttid, int bizId) {
        getRemoteBusinessByPost(request, listener, context, ttid, bizId).startRequest(MtopTradeAddBagResponse.class);
    }

    public void updateBagCount(MtopTradeUpdateRequest request, IRemoteBaseListener listener, Context context, String ttid, int bizId) {
        getRemoteBusinessByPost(request, listener, context, ttid, bizId).startRequest(MtopTradeUpdateBagCountResponse.class);
    }

    public void updateCartSku(MtopTradeUpdateRequest request, IRemoteBaseListener listener, Context context, String ttid, int bizId) {
        getRemoteBusinessByPost(request, listener, context, ttid, bizId).startRequest(MtopTradeUpdateCartSkuResponse.class);
    }

    public void checkCartItems(MtopTradeUpdateRequest request, IRemoteBaseListener listener, Context context, String ttid, int bizId) {
        getRemoteBusinessByPost(request, listener, context, ttid, bizId).startRequest(MtopTradeCheckCartItemResponse.class);
    }

    public void getRecommendItems(MtopTradeItemRecommendRequest request, IRemoteBaseListener listener, Context context, String ttid, int bizId) {
        getRemoteBusinessByGet(request, listener, context, ttid, bizId).startRequest(MtopTradeItemRecommendResponse.class);
    }

    private MtopBusiness getRemoteBusinessByPost(IMTOPDataObject request, IRemoteBaseListener listener, Context context, String ttid, int bizId) {
        return getRemoteBusinessByGet(request, listener, context, ttid, bizId).reqMethod(MethodEnum.POST).setUnitStrategy(MtopUnitStrategy.UNIT_TRADE);
    }

    private MtopBusiness getRemoteBusinessByGet(IMTOPDataObject request, IRemoteBaseListener listener, Context context, String ttid, int bizId) {
        return MtopBusiness.build(Mtop.instance(context), request, ttid).registerListener((IRemoteListener) listener).retryTime(this.retryTime).setBizId(bizId).setUnitStrategy(MtopUnitStrategy.UNIT_TRADE);
    }
}
