package com.taobao.wireless.trade.mcart.sdk.engine;

import android.content.Context;
import anetwork.network.cache.Cache;
import com.alibaba.fastjson.JSONObject;
import com.taobao.tao.remotebusiness.IRemoteBaseListener;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.co.ComponentTag;
import com.taobao.wireless.trade.mcart.sdk.co.biz.CartQueryType;
import com.taobao.wireless.trade.mcart.sdk.co.biz.CartStructure;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ComponentCollectInfo;
import com.taobao.wireless.trade.mcart.sdk.co.biz.FindEntrenceRules;
import com.taobao.wireless.trade.mcart.sdk.co.biz.GroupChargeTotalData;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ItemComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ShopComponent;
import com.taobao.wireless.trade.mcart.sdk.co.service.CartParam;
import com.taobao.wireless.trade.mcart.sdk.co.service.CartService;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;
import com.taobao.wireless.trade.mcart.sdk.protocol.RequestDebug;
import com.taobao.wireless.trade.mcart.sdk.utils.CartResult;
import com.taobao.wireless.trade.mcart.sdk.utils.McartConstants;
import java.util.HashMap;
import java.util.List;
import mtopsdk.common.util.StringUtils;
import mtopsdk.mtop.cache.CacheManagerImpl;
import mtopsdk.mtop.intf.Mtop;

public class CartEngineForMtop implements CartProtocol {
    private static volatile HashMap<CartFrom, CartEngineForMtop> instances = new HashMap<>();
    private CartEngine cartEngine;
    private CartFrom cartFrom = CartFrom.DEFAULT_CLIENT;
    private CartService cartService;
    private String eTagOfCache = null;
    private boolean isACDSClosed = true;

    public String geteTagOfCache() {
        return this.eTagOfCache;
    }

    public void seteTagOfCache(String eTagOfCache2) {
        this.eTagOfCache = eTagOfCache2;
    }

    private CartEngineForMtop() {
    }

    private CartEngineForMtop(CartFrom cartFrom2) {
        this.cartFrom = cartFrom2;
        this.cartEngine = CartEngine.getInstance(cartFrom2);
        this.cartService = new CartService(cartFrom2);
    }

    public static CartEngineForMtop getInstance(CartFrom cartFrom2) {
        if (cartFrom2 == null) {
            cartFrom2 = CartFrom.DEFAULT_CLIENT;
        }
        if (!instances.containsKey(cartFrom2)) {
            synchronized (CartEngineForMtop.class) {
                if (!instances.containsKey(cartFrom2)) {
                    instances.put(cartFrom2, new CartEngineForMtop(cartFrom2));
                }
            }
        }
        return instances.get(cartFrom2);
    }

    public void setRequestDebug(RequestDebug requestDebug) {
        if (this.cartService != null) {
            this.cartService.setRequestDebug(requestDebug);
        }
    }

    public void registerSplitJoinRule(ComponentTag componentTag, SplitJoinRule rule) {
        this.cartEngine.registerSplitJoinRule(componentTag, rule);
    }

    public void registerParseCallback(String key, ParseProtocol parseProtocol) {
        this.cartEngine.registerParseCallback(key, parseProtocol);
    }

    public void unregisterParseCallback(String key) {
        this.cartEngine.unregisterParseCallback(key);
    }

    public void removeAllParseCallback() {
        this.cartEngine.removeAllParseCallback();
    }

    public void registerSubmitCallback(SubmitProtocol submitProtocol) {
        this.cartEngine.registerSubmitCallback(submitProtocol);
    }

    public void unregisterSubmitCallback(SubmitProtocol submitProtocol) {
        this.cartEngine.unregisterSubmitCallback(submitProtocol);
    }

    public void removeAllSubmitCallback() {
        this.cartEngine.removeAllSubmitCallback();
    }

    public void free() {
        if (instances.containsKey(this.cartFrom)) {
            synchronized (CartEngineForMtop.class) {
                if (instances.containsKey(this.cartFrom)) {
                    this.cartEngine.free();
                    instances.remove(this.cartFrom);
                }
            }
        }
    }

    public void cleanData() {
        if (instances.containsKey(this.cartFrom)) {
            synchronized (CartEngineForMtop.class) {
                if (instances.containsKey(this.cartFrom)) {
                    this.cartEngine.cleanData();
                }
            }
        }
    }

    public List<ItemComponent> getItemComponentIdsByBundleId(String bundleId) {
        return this.cartEngine.getItemComponentIdsByBundleId(bundleId);
    }

    public List<ItemComponent> getItemComponentIdsByOrderId(String orderId) {
        return this.cartEngine.getItemComponentIdsByOrderId(orderId);
    }

    public List<ItemComponent> getAllValidItemComponents() {
        return this.cartEngine.getAllValidItemComponents();
    }

    public void updateCartSKUs(CartQueryType type, List<Component> updateComponents, IRemoteBaseListener listener, Context context, String ttid, String cartFrom2, int bizID, String divisionCode, boolean needAllCheckedComponents) {
        this.cartService.updateCartSKUs(type, updateComponents, listener, context, ttid, cartFrom2, bizID, divisionCode, needAllCheckedComponents);
    }

    public void updateCartQuantities(CartQueryType type, List<Component> updateComponents, IRemoteBaseListener listener, Context context, String ttid, String cartFrom2, int bizID, String divisionCode, boolean needAllCheckedComponents) {
        this.cartService.updateCartQuantities(type, updateComponents, listener, context, ttid, cartFrom2, bizID, divisionCode, needAllCheckedComponents);
    }

    public void addFavorites(CartQueryType type, List<Component> addComponents, IRemoteBaseListener listener, Context context, String ttid, String cartFrom2, int bizID, boolean needAllCheckedComponents) {
        this.cartService.addFavorites(type, addComponents, listener, context, ttid, cartFrom2, bizID, needAllCheckedComponents);
    }

    public void addBag(String itemId, String skuId, long quantity, String exParams, IRemoteBaseListener listener, Context context, String ttid, String cartFrom2, int bizID) {
        this.cartService.addBag(itemId, skuId, quantity, exParams, listener, context, ttid, cartFrom2, bizID);
    }

    public void deleteCarts(CartQueryType type, List<Component> deleteComponents, IRemoteBaseListener listener, Context context, String ttid, String cartFrom2, int bizID, String divisionCode, boolean needAllCheckedComponents) {
        this.cartService.deleteCarts(type, deleteComponents, listener, context, ttid, cartFrom2, bizID, divisionCode, needAllCheckedComponents);
    }

    public void deleteInvalidItemCarts(CartQueryType type, List<Component> deleteComponents, IRemoteBaseListener listener, Context context, String ttid, String cartFrom2, int bizID, String divisionCode, boolean needAllCheckedComponents) {
        this.cartService.deleteInvalidItemCarts(type, deleteComponents, listener, context, ttid, cartFrom2, bizID, divisionCode, needAllCheckedComponents);
    }

    public void checkCartItems(CartQueryType type, List<Component> checkComponents, IRemoteBaseListener listener, Context context, String ttid, String cartFrom2, int bizID, String divisionCode, boolean needAllCheckedComponents) {
        this.cartService.checkCartItems(type, checkComponents, listener, context, ttid, cartFrom2, bizID, divisionCode, needAllCheckedComponents);
    }

    public void getRecommendItems(Long appId, HashMap<String, String> params, IRemoteBaseListener listener, Context context, String ttid, int bizID) {
        this.cartService.getRecommendItems(appId, params, listener, context, ttid, bizID);
    }

    public int getCheckMax() {
        return this.cartEngine.getCheckMax();
    }

    public String buyCartIds() {
        return this.cartEngine.buyCartIds();
    }

    public CartStructure getCartStructureData() {
        return this.cartEngine.getCartStructureData();
    }

    public List<Component> getAllCartComponents() {
        return this.cartEngine.getAllCartComponents();
    }

    public CartEngineContext getContext() {
        return this.cartEngine.getContext();
    }

    public CartParseModule getParseModule() {
        return this.cartEngine.getParseModule();
    }

    public CartSubmitModule getSubmitModule() {
        return this.cartEngine.getSubmitModule();
    }

    public GroupChargeTotalData getGroupCommitData() {
        return this.cartEngine.getGroupCommitData();
    }

    public List<ItemComponent> getAllCheckedValidItemComponents() {
        return this.cartEngine.getAllCheckedValidItemComponents();
    }

    public List<ItemComponent> getAllCheckedValidAndPreBuyItemComponents() {
        return this.cartEngine.getAllCheckedValidAndPreBuyItemComponents();
    }

    public boolean removeAllCartQueryCache(Context ctx) {
        return Mtop.instance(Mtop.Id.INNER, ctx).removeCacheBlock(new CacheManagerImpl((Cache) null).getBlockName(StringUtils.concatStr2LowerCase(McartConstants.QUERYBAG_API_NAME, McartConstants.QUERYBAG_API_VERSION)));
    }

    public CartResult checkSubmitItems() {
        return this.cartEngine.checkSubmitItems();
    }

    public boolean isEndPage() {
        return this.cartEngine.isEndPage();
    }

    public boolean isPreLoadOpen() {
        return this.cartEngine.isPreLoadOpen();
    }

    public boolean isSettlementAlone() {
        return this.cartEngine.isSettlementAlone();
    }

    public FindEntrenceRules getFindEntrenceRules() {
        return this.cartEngine.getFindEntrenceRules();
    }

    public CartResult orderByH5Check() {
        return this.cartEngine.orderByH5Check();
    }

    public CartResult orderByH5Check(List<ItemComponent> itemComponents) {
        return this.cartEngine.orderByH5Check(itemComponents);
    }

    public String getInvalidItemRecommendUrl() {
        return this.cartEngine.getInvalidItemRecommendUrl();
    }

    public List<Component> parseByStructure(JSONObject origin) {
        return this.cartEngine.parseByStructure(origin);
    }

    public List<ItemComponent> getAllItemComponentOfBundleByItemComponent(ItemComponent itemComponent) {
        return this.cartEngine.getAllItemComponentOfBundleByItemComponent(itemComponent);
    }

    public boolean allowClearCache() {
        return this.cartEngine.allowClearCache();
    }

    public void refreshComponentInfoWithoutCheckStatus() {
        this.cartEngine.refreshComponentInfoWithoutCheckStatus();
    }

    public void refreshCheckAllComponentCheckStatus() {
        this.cartEngine.refreshCheckAllComponentCheckStatus();
    }

    public CartResult orderBySpecialNativeDomainOrH5(List<ItemComponent> itemComponents) {
        return this.cartEngine.orderBySpecialNativeDomainOrH5(itemComponents);
    }

    public CartResult orderBySpecialNativeDomainOrH5() {
        return this.cartEngine.orderBySpecialNativeDomainOrH5();
    }

    public boolean isACDSClosed() {
        return this.isACDSClosed;
    }

    public void setACDSClosed(boolean isACDSClosed2) {
        this.isACDSClosed = isACDSClosed2;
    }

    public Integer getPageNO() {
        return this.cartEngine.getPageNO();
    }

    public boolean isRemoteCheck() {
        return this.cartEngine.isRemoteCheck();
    }

    public void queryCartsWithParam(CartQueryType type, CartParam param, IRemoteBaseListener listener, Context context, String ttid, int bizID, String divisionCode, boolean queryCartNextByPost) {
        this.cartService.queryCartWithParam(type, param, listener, context, ttid, bizID, divisionCode, queryCartNextByPost);
    }

    public void unfoldShop(List<Component> foldingBarComponents, IRemoteBaseListener listener, Context context, String ttid, String cartFrom2, int bizID, String divisionCode, boolean needAllCheckedComponents) {
        this.cartService.unfoldShop(foldingBarComponents, listener, context, ttid, cartFrom2, bizID, divisionCode, needAllCheckedComponents);
    }

    public CartFrom getCartFrom() {
        return this.cartEngine.getCartFrom();
    }

    public ComponentCollectInfo getComponentCollectInfoByBundleId(ShopComponent shopComponent) {
        return this.cartEngine.getComponentCollectInfoByBundleId(shopComponent);
    }

    public String getTsmHomeUrl() {
        return this.cartEngine.getTsmHomeUrl();
    }

    public void removeSplitJoinRule(ComponentTag componentTag) {
        this.cartEngine.removeSplitJoinRule(componentTag);
    }

    public String getDeleteTipsOfTitlePriorityBuy() {
        return this.cartEngine.getDeleteTipsOfTitlePriorityBuy();
    }

    public String getDeleteTipsOfContentPriorityBuy() {
        return this.cartEngine.getDeleteTipsOfContentPriorityBuy();
    }

    public String getDeleteTipsOfTitlePreSell() {
        return this.cartEngine.getDeleteTipsOfTitlePreSell();
    }

    public String getDeleteTipsOfContentPreSell() {
        return this.cartEngine.getDeleteTipsOfContentPreSell();
    }

    public String getCannotAdd2FavoritesTipsOfPreSell() {
        return this.cartEngine.getCannotAdd2FavoritesTipsOfPreSell();
    }

    public String getFavorTipsOfTitlePriorityBuy() {
        return this.cartEngine.getFavorTipsOfTitlePriorityBuy();
    }

    public String getFavorTipsOfContentPriorityBuy() {
        return this.cartEngine.getFavorTipsOfContentPriorityBuy();
    }
}
