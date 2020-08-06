package com.taobao.wireless.trade.mcart.sdk.engine;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.orange.model.NameSpaceDO;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.co.ComponentTag;
import com.taobao.wireless.trade.mcart.sdk.co.biz.CartStructure;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ComponentBizUtil;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ComponentCollectInfo;
import com.taobao.wireless.trade.mcart.sdk.co.biz.CountDown;
import com.taobao.wireless.trade.mcart.sdk.co.biz.Double11CountDownInfo;
import com.taobao.wireless.trade.mcart.sdk.co.biz.FindEntrenceRules;
import com.taobao.wireless.trade.mcart.sdk.co.biz.FoldingBarComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.GroupChargeData;
import com.taobao.wireless.trade.mcart.sdk.co.biz.GroupChargeTotalData;
import com.taobao.wireless.trade.mcart.sdk.co.biz.GroupChargeType;
import com.taobao.wireless.trade.mcart.sdk.co.biz.GroupComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ItemComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ShareTip;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ShopComponent;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;
import com.taobao.wireless.trade.mcart.sdk.utils.CartResult;
import com.taobao.wireless.trade.mcart.sdk.utils.McartConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CartEngine implements CommonProtocol {
    private static volatile HashMap<CartFrom, CartEngine> instances = new HashMap<>();
    private CartFrom cartFrom = CartFrom.DEFAULT_CLIENT;
    private CartEngineContext context = new CartEngineContext();
    private boolean isDouble11Mode = false;
    private LinkageDelegate linkageDelegate;
    private CartParseModule parseModule;
    private CartSubmitModule submitModule;

    public boolean isDouble11Mode() {
        return this.isDouble11Mode;
    }

    public void setIsDouble11Mode(boolean isDouble11Mode2) {
        this.isDouble11Mode = isDouble11Mode2;
    }

    public CartFrom getCartFrom() {
        return this.cartFrom;
    }

    private CartEngine() {
    }

    private CartEngine(CartFrom cartFrom2) {
        this.cartFrom = cartFrom2;
        this.parseModule = new CartParseModule(cartFrom2);
        this.submitModule = new CartSubmitModule(cartFrom2);
    }

    public static CartEngine getInstance(CartFrom cartFrom2) {
        if (cartFrom2 == null) {
            cartFrom2 = CartFrom.DEFAULT_CLIENT;
        }
        if (!instances.containsKey(cartFrom2)) {
            synchronized (CartEngine.class) {
                if (!instances.containsKey(cartFrom2)) {
                    instances.put(cartFrom2, new CartEngine(cartFrom2));
                }
            }
        }
        return instances.get(cartFrom2);
    }

    public void registerSplitJoinRule(ComponentTag componentTag, SplitJoinRule rule) {
        this.parseModule.registerSplitJoinRule(componentTag, rule);
    }

    public void registerParseCallback(String key, ParseProtocol parseProtocol) {
        this.parseModule.registerParseCallback(key, parseProtocol);
    }

    public void unregisterParseCallback(String key) {
        this.parseModule.unregisterParseCallback(key);
    }

    public void removeAllParseCallback() {
        this.parseModule.removeAllParseCallbacks();
    }

    public void registerSubmitCallback(SubmitProtocol submitProtocol) {
        this.submitModule.registerSubmitCallback(submitProtocol);
    }

    public void unregisterSubmitCallback(SubmitProtocol submitProtocol) {
        this.submitModule.unregisterSubmitCallback(submitProtocol);
    }

    public void removeAllSubmitCallback() {
        this.submitModule.removeAllSubmitCallbacks();
    }

    public void free() {
        if (instances.containsKey(this.cartFrom)) {
            synchronized (CartEngine.class) {
                if (instances.containsKey(this.cartFrom)) {
                    instances.remove(this.cartFrom);
                }
            }
        }
    }

    public void cleanData() {
        if (instances.containsKey(this.cartFrom)) {
            synchronized (CartEngine.class) {
                if (instances.containsKey(this.cartFrom)) {
                    this.context = new CartEngineContext();
                }
            }
        }
    }

    public List<ItemComponent> getItemComponentIdsByBundleId(String bundleId) {
        return this.parseModule.getItemComponentsByBundleId(bundleId);
    }

    public List<ItemComponent> getItemComponentIdsByOrderId(String orderId) {
        return this.parseModule.getItemComponentsByOrderId(orderId);
    }

    public List<ItemComponent> getAllValidItemComponents() {
        return this.parseModule.getAllValidItemComponents();
    }

    public FoldingBarComponent getFoldingBarComponentByItemComponent(ItemComponent itemComponent) {
        if (this.parseModule != null) {
            return this.parseModule.getFoldingBarComponentByItemComponent(itemComponent);
        }
        return null;
    }

    public int getCheckMax() {
        JSONObject pageMeta;
        CartEngineContext context2 = getContext();
        if (context2 == null || (pageMeta = context2.getPageMeta()) == null) {
            return 0;
        }
        return pageMeta.getIntValue("checkMax");
    }

    public String buyCartIds() {
        JSONArray cartIdsArray;
        JSONObject submitData = this.submitModule.generateFinalSubmitData();
        if (!(submitData == null || (cartIdsArray = submitData.getJSONArray("cartIds")) == null)) {
            StringBuffer cartIdsBuffer = new StringBuffer();
            Iterator<Object> it = cartIdsArray.iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                if (obj != null) {
                    cartIdsBuffer.append((String) obj).append(",");
                }
            }
            String cartIds = cartIdsBuffer.toString();
            int indexlast = cartIds.lastIndexOf(",");
            if (indexlast > 0) {
                return cartIds.substring(0, indexlast);
            }
        }
        return null;
    }

    public CartStructure getCartStructureData() {
        return this.parseModule.generateCartStructure();
    }

    public List<Component> getAllCartComponents() {
        if (getContext() != null) {
            return getContext().getOutput();
        }
        return null;
    }

    public CartEngineContext getContext() {
        if (this.context != null) {
            return this.context;
        }
        return null;
    }

    public CartParseModule getParseModule() {
        return this.parseModule;
    }

    public CartSubmitModule getSubmitModule() {
        return this.submitModule;
    }

    public List<ItemComponent> getAllCheckedValidItemComponents() {
        return this.parseModule.getAllCheckedValidItemComponents();
    }

    public List<ItemComponent> getAllCheckedValidAndPreBuyItemComponents() {
        return this.parseModule.getAllCheckedValidAndPreBuyItemComponents();
    }

    public CartResult checkSubmitItems() {
        List<ItemComponent> itemComponents;
        JSONArray innerValueArray;
        CartResult cartResult = new CartResult();
        CartEngineContext context2 = getContext();
        if (context2 != null && (itemComponents = this.parseModule.getAllCheckedValidItemComponents()) != null && itemComponents.size() > 1) {
            JSONObject excludes = context2.getExcludes();
            JSONArray global = null;
            JSONObject inner = null;
            JSONArray innerGlobal = null;
            if (excludes != null) {
                global = excludes.getJSONArray("global");
                inner = excludes.getJSONObject("inner");
                innerGlobal = excludes.getJSONArray("innerGlobal");
            }
            if (global != null || inner != null || innerGlobal != null) {
                boolean globalExist = false;
                boolean innerExist = false;
                boolean innerGlobalExist = false;
                HashSet<String> shopHosts = new HashSet<>();
                HashSet<String> shopInnerHosts = new HashSet<>();
                HashSet<String> shopInnerGlobalHosts = new HashSet<>();
                String globalExcludeHost = null;
                String innerExcludeHost = null;
                String innerGolbalExcludeHost = null;
                Iterator<ItemComponent> it = itemComponents.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    ItemComponent itemComponent = it.next();
                    if (itemComponent != null) {
                        String itemExclude = itemComponent.getExclude();
                        if (!(global == null || itemExclude == null)) {
                            shopHosts.add(itemExclude);
                            if (global.contains(itemExclude)) {
                                globalExist = true;
                                globalExcludeHost = itemExclude;
                            }
                        }
                        if (globalExist && shopHosts.size() > 1) {
                            constructFailCartResult(cartResult, context2, globalExcludeHost);
                            break;
                        }
                        String itemMutex = itemComponent.getMutex();
                        if (!(inner == null || itemExclude == null || itemMutex == null || !inner.containsKey(itemExclude) || (innerValueArray = inner.getJSONArray(itemExclude)) == null || !innerValueArray.contains(itemMutex))) {
                            innerExist = true;
                            shopInnerHosts.add(itemMutex);
                            innerExcludeHost = itemExclude;
                        }
                        if (innerExist && shopInnerHosts.size() > 1) {
                            constructFailCartResult(cartResult, context2, innerExcludeHost);
                            break;
                        }
                        if (!(innerGlobal == null || itemExclude == null || itemMutex == null || !innerGlobal.contains(itemExclude))) {
                            innerGlobalExist = true;
                            shopInnerGlobalHosts.add(itemMutex);
                            innerGolbalExcludeHost = itemExclude;
                        }
                        if (innerGlobalExist && shopInnerGlobalHosts.size() > 1) {
                            constructFailCartResult(cartResult, context2, innerGolbalExcludeHost);
                            break;
                        }
                    }
                }
            }
        }
        return cartResult;
    }

    private void constructFailCartResult(CartResult cartResult, CartEngineContext context2, String itemExclude) {
        cartResult.setSuccess(false);
        cartResult.setErrorCode(McartConstants.EXCLUDED_HOST);
    }

    public boolean isEndPage() {
        JSONObject pageMeta;
        CartEngineContext context2 = getContext();
        if (context2 == null || (pageMeta = context2.getPageMeta()) == null || !pageMeta.containsKey("isEndPage")) {
            return true;
        }
        return pageMeta.getBoolean("isEndPage").booleanValue();
    }

    public boolean isPreLoadOpen() {
        JSONObject controlParas;
        CartEngineContext context2 = getContext();
        if (context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("preLoadOpen")) {
            return false;
        }
        return controlParas.getBoolean("preLoadOpen").booleanValue();
    }

    public boolean isSettlementAlone() {
        JSONObject controlParas;
        CartEngineContext context2 = getContext();
        if (context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("isSettlementAlone")) {
            return false;
        }
        return controlParas.getBoolean("isSettlementAlone").booleanValue();
    }

    public FindEntrenceRules getFindEntrenceRules() {
        JSONObject controlParas;
        JSONObject findEntrenceRulesJS;
        CartEngineContext context2 = getContext();
        if (context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("recomm") || (findEntrenceRulesJS = controlParas.getJSONObject("recomm")) == null || findEntrenceRulesJS.getString("num") == null) {
            return null;
        }
        FindEntrenceRules findEntrenceRules = new FindEntrenceRules();
        findEntrenceRules.setFindRecommendItemNum(findEntrenceRulesJS.getIntValue("num"));
        return findEntrenceRules;
    }

    public CartResult orderByH5Check() {
        return orderByH5Check(getAllCheckedValidItemComponents());
    }

    public CartResult orderByH5Check(List<ItemComponent> itemComponents) {
        JSONObject controlParas;
        JSONObject orderByH5UrlsJS;
        String orderH5Url;
        CartResult cartResult = new CartResult();
        cartResult.setSuccess(false);
        HashSet<String> shopHosts = new HashSet<>();
        String hostName = "";
        if (itemComponents != null) {
            for (ItemComponent itemComponent : itemComponents) {
                if (!(itemComponent == null || itemComponent.getExclude() == null)) {
                    shopHosts.add(itemComponent.getExclude());
                    hostName = itemComponent.getExclude();
                }
            }
        }
        CartEngineContext context2 = getContext();
        if (!(context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("orderByH5Urls") || shopHosts.size() != 1 || (orderByH5UrlsJS = controlParas.getJSONObject("orderByH5Urls")) == null || !orderByH5UrlsJS.containsKey(hostName) || (orderH5Url = orderByH5UrlsJS.getString(hostName)) == null)) {
            cartResult.setSuccess(true);
            cartResult.setOrderH5Url(orderH5Url);
        }
        return cartResult;
    }

    public String getInvalidItemRecommendUrl() {
        JSONObject controlParas;
        CartEngineContext context2 = getContext();
        if (context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("invalidItemRecommendUrl")) {
            return null;
        }
        return controlParas.getString("invalidItemRecommendUrl");
    }

    public List<Component> parseByStructure(JSONObject origin) {
        if (origin == null) {
            return null;
        }
        try {
            return getParseModule().parse(origin);
        } catch (Throwable th) {
            return null;
        }
    }

    public List<ItemComponent> getAllItemComponentOfBundleByItemComponent(ItemComponent itemComponent) {
        return itemComponentsUnderBundle(itemComponent);
    }

    private List<ItemComponent> itemComponentsUnderBundle(ItemComponent itemComponent) {
        Component itemParent;
        Component bundleComponent;
        if (itemComponent == null || (itemParent = itemComponent.getParent()) == null || ComponentTag.getComponentTagByDesc(itemParent.getTag()) != ComponentTag.GROUP || !(itemParent instanceof GroupComponent) || (bundleComponent = itemParent.getParent()) == null) {
            return null;
        }
        return getItemComponentIdsByBundleId(bundleComponent.getComponentId());
    }

    public CartResult tmallFlowCheck() {
        CartResult cartResult = new CartResult();
        cartResult.setSuccess(false);
        List<ItemComponent> itemComponets = this.parseModule.getAllCheckedValidItemComponents();
        if (itemComponets != null) {
            Iterator<ItemComponent> it = itemComponets.iterator();
            while (true) {
                if (it.hasNext()) {
                    ItemComponent itemComponent = it.next();
                    if (itemComponent != null && itemComponent.getToBuy() != null && itemComponent.getToBuy().equalsIgnoreCase("noGray")) {
                        cartResult.setSuccess(true);
                        cartResult.setFlowType(itemComponent.getToBuy());
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return cartResult;
    }

    public void refreshComponentInfoWithoutCheckStatus() {
        ComponentBizUtil.refreshComponentInfoWithoutCheckStatus(this.cartFrom);
    }

    public void refreshCheckAllComponentCheckStatus() {
        ComponentBizUtil.refreshCheckAllComponentCheckStatus(this.cartFrom);
    }

    public boolean allowClearCache() {
        JSONObject controlParas;
        CartEngineContext context2 = getContext();
        if (context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("allowClearCache")) {
            return true;
        }
        return controlParas.getBoolean("allowClearCache").booleanValue();
    }

    public CartResult orderBySpecialNativeDomainOrH5() {
        return orderBySpecialNativeDomainOrH5(getAllCheckedValidItemComponents());
    }

    public CartResult orderBySpecialNativeDomainOrH5(List<ItemComponent> itemComponents) {
        JSONObject controlParas;
        String orderNative;
        String orderH5Url;
        CartResult cartResult = new CartResult();
        cartResult.setSuccess(false);
        HashSet<String> shopHosts = new HashSet<>();
        String hostName = "";
        if (itemComponents != null) {
            for (ItemComponent itemComponent : itemComponents) {
                if (!(itemComponent == null || itemComponent.getExclude() == null)) {
                    shopHosts.add(itemComponent.getExclude());
                    hostName = itemComponent.getExclude();
                }
            }
        }
        CartEngineContext context2 = getContext();
        if (!(context2 == null || (controlParas = context2.getControlParas()) == null || ((!controlParas.containsKey("orderByH5Urls") && !controlParas.containsKey("orderByNative")) || shopHosts.size() != 1))) {
            JSONObject orderByH5UrlsJS = controlParas.getJSONObject("orderByH5Urls");
            if (!(orderByH5UrlsJS == null || !orderByH5UrlsJS.containsKey(hostName) || (orderH5Url = orderByH5UrlsJS.getString(hostName)) == null)) {
                cartResult.setSuccess(true);
                cartResult.setOrderH5Url(orderH5Url);
            }
            JSONObject orderByNative = controlParas.getJSONObject("orderByNative");
            if (!(orderByNative == null || !orderByNative.containsKey(hostName) || (orderNative = orderByNative.getString(hostName)) == null)) {
                cartResult.setSuccess(true);
                cartResult.setOrderByNative(orderNative);
            }
        }
        return cartResult;
    }

    public Integer getPageNO() {
        JSONObject pageMeta;
        CartEngineContext context2 = getContext();
        if (context2 == null || (pageMeta = context2.getPageMeta()) == null || !pageMeta.containsKey("pageNo")) {
            return null;
        }
        return pageMeta.getInteger("pageNo");
    }

    public boolean isRemoteCheck() {
        JSONObject controlParas;
        CartEngineContext context2 = getContext();
        if (context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("remoteCheck")) {
            return false;
        }
        return controlParas.getBoolean("remoteCheck").booleanValue();
    }

    public ComponentCollectInfo getComponentCollectInfoByBundleId(ShopComponent shopComponent) {
        return ComponentBizUtil.getComponentCollectInfoByBundleId(shopComponent);
    }

    public String getTsmHomeUrl() {
        JSONObject controlParas;
        CartEngineContext context2 = getContext();
        if (context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("tsmHomeUrl")) {
            return null;
        }
        return controlParas.getString("tsmHomeUrl");
    }

    public void removeSplitJoinRule(ComponentTag componentTag) {
        this.parseModule.removeSplitJoinRule(componentTag);
    }

    public String getDeleteTipsOfTitlePriorityBuy() {
        JSONObject controlParas;
        String ret = null;
        CartEngineContext context2 = getContext();
        if (!(context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("priorityBuy"))) {
            ret = controlParas.getJSONObject("priorityBuy").getString("deleteTipsOfTitlePriorityBuy");
        }
        if (TextUtils.isEmpty(ret)) {
            return "确定删除抢先订商品吗？";
        }
        return ret;
    }

    public String getDeleteTipsOfContentPriorityBuy() {
        JSONObject controlParas;
        String ret = null;
        CartEngineContext context2 = getContext();
        if (!(context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("priorityBuy"))) {
            ret = controlParas.getJSONObject("priorityBuy").getString("deleteTipsOfContentPriorityBuy");
        }
        if (TextUtils.isEmpty(ret)) {
            return "删除后，已锁定的优先库存将释放";
        }
        return ret;
    }

    public String getDeleteTipsOfTitlePreSell() {
        JSONObject controlParas;
        String ret = null;
        CartEngineContext context2 = getContext();
        if (!(context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("preSell"))) {
            ret = controlParas.getJSONObject("preSell").getString("deleteTipsOfTitlePreSell");
        }
        if (TextUtils.isEmpty(ret)) {
            return "确定删除预售商品吗？";
        }
        return ret;
    }

    public String getDeleteTipsOfContentPreSell() {
        JSONObject controlParas;
        String ret = null;
        CartEngineContext context2 = getContext();
        if (!(context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("preSell"))) {
            ret = controlParas.getJSONObject("preSell").getString("deleteTipsOfContentPreSell");
        }
        if (TextUtils.isEmpty(ret)) {
            return "删除后预售宝贝可在待付款-预售定金页查看，在预售定金页您可再次加入购物车，其他宝贝合并下单享优惠。";
        }
        return ret;
    }

    public String getCannotAdd2FavoritesTipsOfPreSell() {
        JSONObject controlParas;
        String ret = null;
        CartEngineContext context2 = getContext();
        if (!(context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("preSell"))) {
            ret = controlParas.getJSONObject("preSell").getString("cannotAdd2FavoritesTipsOfPreSell");
        }
        if (TextUtils.isEmpty(ret)) {
            return "预售商品不能移入收藏夹";
        }
        return ret;
    }

    public String getFavorTipsOfTitlePriorityBuy() {
        JSONObject controlParas;
        String ret = null;
        CartEngineContext context2 = getContext();
        if (!(context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("priorityBuy"))) {
            ret = controlParas.getJSONObject("priorityBuy").getString("favorTipsOfTitlePriorityBuy");
        }
        if (TextUtils.isEmpty(ret)) {
            return "确定将抢先订宝贝移入收藏夹吗？";
        }
        return ret;
    }

    public String getFavorTipsOfContentPriorityBuy() {
        JSONObject controlParas;
        String ret = null;
        CartEngineContext context2 = getContext();
        if (!(context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("priorityBuy"))) {
            ret = controlParas.getJSONObject("priorityBuy").getString("favorTipsOfContentPriorityBuy");
        }
        if (TextUtils.isEmpty(ret)) {
            return "移入收藏夹后，商品将取消抢先订资格";
        }
        return ret;
    }

    public GroupChargeTotalData getGroupCommitData() {
        GroupChargeTotalData groupChargeTotalData = new GroupChargeTotalData();
        List<ItemComponent> checkedItems = getAllCheckedValidItemComponents();
        CartEngineContext context2 = getContext();
        if (context2 != null) {
            JSONObject excludes = context2.getExcludes();
            if (!(excludes == null || excludes.getJSONObject("tip") == null)) {
                groupChargeTotalData.setTitle(excludes.getJSONObject("tip").getString(NameSpaceDO.LEVEL_DEFAULT));
            }
            Map<String, List<ItemComponent>> groupData = new HashMap<>();
            HashSet<String> excludesHashSet = new HashSet<>();
            for (ItemComponent itemComponent : checkedItems) {
                String key = getKeyByExcludeAndMutex(excludes, itemComponent.getExclude(), itemComponent.getMutex());
                if (excludesHashSet.add(key)) {
                    List<ItemComponent> newItemList = new ArrayList<>();
                    newItemList.add(itemComponent);
                    groupData.put(key, newItemList);
                } else {
                    groupData.get(key).add(itemComponent);
                }
            }
            groupChargeTotalData.setGroupChargeDatas(getGroupChargeDataByGroupData(excludesHashSet, groupData));
        }
        return groupChargeTotalData;
    }

    private String getKeyByExcludeAndMutex(JSONObject excludes, String itemExclude, String itemMutex) {
        JSONArray innerValueArray;
        String defaultKey = GroupChargeType.BC.getCode();
        if (excludes == null) {
            return defaultKey;
        }
        JSONArray global = excludes.getJSONArray("global");
        JSONObject inner = excludes.getJSONObject("inner");
        JSONArray innerGlobal = excludes.getJSONArray("innerGlobal");
        if (global == null && inner == null && innerGlobal == null) {
            return defaultKey;
        }
        boolean globalExist = false;
        boolean innerExist = false;
        boolean innerGlobalExist = false;
        if (!(global == null || itemExclude == null || !global.contains(itemExclude))) {
            globalExist = true;
        }
        if (!(inner == null || itemExclude == null || itemMutex == null || !inner.containsKey(itemExclude) || (innerValueArray = inner.getJSONArray(itemExclude)) == null || !innerValueArray.contains(itemMutex))) {
            innerExist = true;
        }
        if (!(innerGlobal == null || itemExclude == null || itemMutex == null || !innerGlobal.contains(itemExclude))) {
            innerGlobalExist = true;
        }
        if (innerExist || innerGlobalExist) {
            return itemExclude + itemMutex;
        }
        if (globalExist) {
            return itemExclude;
        }
        return defaultKey;
    }

    private String hasCheckedOneSMShop(HashSet<String> excludesHashSet) {
        String smKey = null;
        int smShopCount = 0;
        if (excludesHashSet != null && !excludesHashSet.isEmpty()) {
            Iterator<String> it = excludesHashSet.iterator();
            while (it.hasNext()) {
                String key = it.next();
                if (key.startsWith(GroupChargeType.SM.getCode())) {
                    smShopCount++;
                    smKey = key;
                }
            }
        }
        if (smShopCount == 1) {
            return smKey;
        }
        return null;
    }

    private List<GroupChargeData> getGroupChargeDataByGroupData(HashSet<String> keys, Map<String, List<ItemComponent>> groupData) {
        List<GroupChargeData> groupChargeDataList = new ArrayList<>();
        String smKey = hasCheckedOneSMShop(keys);
        if (!(keys == null || smKey == null || !keys.contains(smKey))) {
            if (keys.contains(GroupChargeType.BC.getCode())) {
                groupData.get(GroupChargeType.BC.getCode()).addAll(groupData.get(smKey));
                keys.remove(smKey);
            }
        }
        if (!(keys == null || groupData == null || groupData.isEmpty())) {
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                GroupChargeData groupChargeData = new GroupChargeData();
                List<ItemComponent> itemComponentList = groupData.get(key);
                if (itemComponentList != null && !itemComponentList.isEmpty()) {
                    boolean fromServer = true;
                    long totalPrice = 0;
                    for (ItemComponent itemComponent : itemComponentList) {
                        Long p = null;
                        try {
                            p = itemComponent.getItemPay().getAfterPromPrice();
                        } catch (Exception e) {
                        }
                        if (p == null || !fromServer) {
                            fromServer = false;
                            p = Long.valueOf(itemComponent.getItemPay().getTotalNowPrice());
                        }
                        totalPrice += p.longValue();
                        groupChargeData.addShopComponent(ComponentBizUtil.getShopComponentByItemComponent(getInstance(this.cartFrom).getContext(), itemComponent));
                    }
                    groupChargeData.setQuantity(itemComponentList.size());
                    groupChargeData.setTotalPrice(totalPrice);
                    groupChargeData.setFromServer(fromServer);
                    groupChargeData.setItemComponents(itemComponentList);
                    GroupChargeType[] values = GroupChargeType.values();
                    int length = values.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            break;
                        }
                        GroupChargeType type = values[i];
                        if (key.startsWith(type.getCode())) {
                            groupChargeData.setGroupChargeType(type);
                            break;
                        }
                        i++;
                    }
                }
                groupChargeDataList.add(groupChargeData);
            }
        }
        Collections.sort(groupChargeDataList, new Comparator<GroupChargeData>() {
            public int compare(GroupChargeData o1, GroupChargeData o2) {
                return o1.getGroupChargeType().getPriority() - o2.getGroupChargeType().getPriority();
            }
        });
        return groupChargeDataList;
    }

    public String getMainMeetingUrl() {
        JSONObject controlParas;
        CartEngineContext context2 = getContext();
        if (context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("mainMeeting")) {
            return null;
        }
        return controlParas.getString("mainMeeting");
    }

    public Double11CountDownInfo getDouble11CounDownInfo() {
        Double11CountDownInfo double11CountDownInfo = new Double11CountDownInfo();
        List<CountDown> countDownList = new ArrayList<>();
        List<String> redWords = new ArrayList<>();
        CartEngineContext context2 = getContext();
        if (!(context2 == null || context2.getControlParas() == null)) {
            JSONObject controlParas = context2.getControlParas();
            if (controlParas.containsKey("countdown")) {
                JSONArray countdown = controlParas.getJSONArray("countdown");
                for (int i = 0; i < countdown.size(); i++) {
                    countDownList.add(new CountDown(countdown.getJSONObject(i)));
                }
            }
            if (controlParas.containsKey("redWords")) {
                JSONArray redWordsArray = controlParas.getJSONArray("redWords");
                for (int i2 = 0; i2 < redWordsArray.size(); i2++) {
                    redWords.add(redWordsArray.getString(i2));
                }
            }
            if (controlParas.containsKey("closeCountDown")) {
                double11CountDownInfo.setCloseCountDown(controlParas.getBooleanValue("closeCountDown"));
            }
            if (controlParas.containsKey("closeDouble11Model")) {
                double11CountDownInfo.setCloseDouble11Model(controlParas.getBooleanValue("closeDouble11Model"));
            }
        }
        double11CountDownInfo.setCountDownInfo(countDownList);
        double11CountDownInfo.setRedWords(redWords);
        return double11CountDownInfo;
    }

    public ShareTip getShareTip() {
        JSONObject controlParas;
        CartEngineContext context2 = getContext();
        if (context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("shareTip")) {
            return null;
        }
        return new ShareTip(controlParas.getJSONObject("shareTip"));
    }

    public String getItemRecommendUrl() {
        JSONObject controlParas;
        CartEngineContext context2 = getContext();
        if (context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("itemRecommendUrl")) {
            return null;
        }
        return controlParas.getString("itemRecommendUrl");
    }

    public String getItemRecommendType() {
        JSONObject controlParas;
        CartEngineContext context2 = getContext();
        if (context2 == null || (controlParas = context2.getControlParas()) == null || !controlParas.containsKey("itemRecommendType")) {
            return null;
        }
        return controlParas.getString("itemRecommendType");
    }

    public LinkageDelegate getLinkageDelegate() {
        return this.linkageDelegate;
    }

    public void setLinkageDelegate(LinkageDelegate linkageDelegate2) {
        this.linkageDelegate = linkageDelegate2;
    }

    public void removeLinkageDelegate(LinkageDelegate linkageDelegate2) {
        if (this.linkageDelegate == linkageDelegate2) {
            this.linkageDelegate = null;
        }
    }

    public void executRollBack() {
        RollbackProtocol rollbackProtocol;
        CartEngineContext context2 = getContext();
        if (context2 != null && (rollbackProtocol = context2.getRollbackProtocol()) != null) {
            rollbackProtocol.rollback();
        }
    }
}
