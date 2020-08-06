package com.taobao.wireless.trade.mcart.sdk.engine;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.co.ComponentTag;
import com.taobao.wireless.trade.mcart.sdk.co.biz.CartStructure;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ComponentCollectInfo;
import com.taobao.wireless.trade.mcart.sdk.co.biz.FindEntrenceRules;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ItemComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ShopComponent;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;
import com.taobao.wireless.trade.mcart.sdk.utils.CartResult;
import java.util.List;

public interface CommonProtocol {
    boolean allowClearCache();

    String buyCartIds();

    CartResult checkSubmitItems();

    void cleanData();

    void free();

    List<Component> getAllCartComponents();

    List<ItemComponent> getAllCheckedValidAndPreBuyItemComponents();

    List<ItemComponent> getAllCheckedValidItemComponents();

    List<ItemComponent> getAllItemComponentOfBundleByItemComponent(ItemComponent itemComponent);

    List<ItemComponent> getAllValidItemComponents();

    String getCannotAdd2FavoritesTipsOfPreSell();

    CartFrom getCartFrom();

    CartStructure getCartStructureData();

    int getCheckMax();

    ComponentCollectInfo getComponentCollectInfoByBundleId(ShopComponent shopComponent);

    String getDeleteTipsOfContentPreSell();

    String getDeleteTipsOfContentPriorityBuy();

    String getDeleteTipsOfTitlePreSell();

    String getDeleteTipsOfTitlePriorityBuy();

    String getFavorTipsOfContentPriorityBuy();

    String getFavorTipsOfTitlePriorityBuy();

    FindEntrenceRules getFindEntrenceRules();

    String getInvalidItemRecommendUrl();

    List<ItemComponent> getItemComponentIdsByBundleId(String str);

    List<ItemComponent> getItemComponentIdsByOrderId(String str);

    Integer getPageNO();

    String getTsmHomeUrl();

    boolean isEndPage();

    boolean isPreLoadOpen();

    boolean isRemoteCheck();

    boolean isSettlementAlone();

    CartResult orderByH5Check();

    CartResult orderBySpecialNativeDomainOrH5();

    List<Component> parseByStructure(JSONObject jSONObject);

    void refreshCheckAllComponentCheckStatus();

    void refreshComponentInfoWithoutCheckStatus();

    void registerParseCallback(String str, ParseProtocol parseProtocol);

    void registerSplitJoinRule(ComponentTag componentTag, SplitJoinRule splitJoinRule);

    void registerSubmitCallback(SubmitProtocol submitProtocol);

    void removeAllParseCallback();

    void removeAllSubmitCallback();

    void removeSplitJoinRule(ComponentTag componentTag);

    void unregisterParseCallback(String str);

    void unregisterSubmitCallback(SubmitProtocol submitProtocol);
}
