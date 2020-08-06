package com.taobao.wireless.trade.mcart.sdk.engine;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.co.ComponentTag;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ActionType;
import com.taobao.wireless.trade.mcart.sdk.co.biz.FoldingBarComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.GroupComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ItemComponent;
import com.taobao.wireless.trade.mcart.sdk.co.service.RequestType;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CartSubmitModule {
    private CartFrom cartFrom = CartFrom.DEFAULT_CLIENT;
    private final Set<SubmitProtocol> submitProtocols = new HashSet();

    private CartSubmitModule() {
    }

    public CartSubmitModule(CartFrom cartFrom2) {
        this.cartFrom = cartFrom2 == null ? CartFrom.DEFAULT_CLIENT : cartFrom2;
    }

    public void registerSubmitCallback(SubmitProtocol submitProtocol) {
        this.submitProtocols.add(submitProtocol);
    }

    public void unregisterSubmitCallback(SubmitProtocol submitProtocol) {
        this.submitProtocols.remove(submitProtocol);
    }

    public void removeAllSubmitCallbacks() {
        this.submitProtocols.clear();
    }

    public void expandExparams(Map<String, String> map, RequestType type) {
        if (!this.submitProtocols.isEmpty()) {
            for (SubmitProtocol entry : this.submitProtocols) {
                entry.setExParamsMap(map, type);
            }
        }
    }

    public JSONObject generatePageData() {
        JSONObject submitData = null;
        CartEngineContext context = CartEngineForMtop.getInstance(this.cartFrom).getContext();
        if (!(context == null || context.getPageMeta() == null)) {
            submitData = new JSONObject();
            JSONObject operate = new JSONObject();
            operate.put("query", (Object) new JSONArray());
            submitData.put("operate", (Object) operate);
            JSONObject structure = new JSONObject();
            structure.put("structure", (Object) context.getStructure());
            submitData.put("hierarchy", (Object) structure);
            submitData.put("pageMeta", (Object) context.getPageMeta());
            if (!this.submitProtocols.isEmpty()) {
                for (SubmitProtocol entry : this.submitProtocols) {
                    entry.setSubmitData(submitData, RequestType.Query_Carts);
                }
            }
        }
        return submitData;
    }

    public JSONObject generateAsyncRequestData(List<Component> actionKeys, ActionType actionType, boolean needAllCheckedComponents) {
        if (CartEngine.getInstance(this.cartFrom).getContext() == null) {
            return null;
        }
        List<ItemComponent> datas = null;
        if (needAllCheckedComponents) {
            datas = CartEngine.getInstance(this.cartFrom).getAllCheckedValidItemComponents();
        }
        try {
            return format(datas, actionKeys, actionType);
        } catch (Throwable th) {
            return null;
        }
    }

    private JSONObject format(List<ItemComponent> collections, List<Component> actionKeys, ActionType actionType) {
        JSONObject data = new JSONObject();
        JSONArray actionKeylist = new JSONArray();
        if (actionKeys != null && actionKeys.size() > 0) {
            for (Object actionObj : actionKeys) {
                if (actionObj != null) {
                    Component actionTmp = (Component) actionObj;
                    if ((ComponentTag.getComponentTagByDesc(actionTmp.getTag()) == ComponentTag.ITEM && (actionTmp instanceof ItemComponent)) || (ComponentTag.getComponentTagByDesc(actionTmp.getTag()) == ComponentTag.FOLDINGBAR && (actionTmp instanceof FoldingBarComponent))) {
                        actionKeylist.add(actionTmp.getComponentId());
                        data.put(actionTmp.getComponentId(), (Object) actionTmp.convertToSubmitData());
                        switch (actionType) {
                            case UPDATE_QUANTITY:
                                addFoldingBarComponentByItem(data, actionTmp);
                                addItemComponentUnderBundle(data, actionTmp);
                                break;
                            case UPDATE_SKU:
                                addFoldingBarComponentByItem(data, actionTmp);
                                addItemComponentUnderBundle(data, actionTmp);
                                break;
                            case DELETE:
                                addFoldingBarComponentByItem(data, actionTmp);
                                addItemComponentUnderBundle(data, actionTmp);
                                break;
                            case ADD_FAVORITE:
                                addFoldingBarComponentByItem(data, actionTmp);
                                addItemComponentUnderBundle(data, actionTmp);
                                break;
                            case CHECK:
                                addFoldingBarComponentByItem(data, actionTmp);
                                addItemComponentUnderBundle(data, actionTmp);
                                break;
                            case UNCHECK:
                                addFoldingBarComponentByItem(data, actionTmp);
                                addItemComponentUnderBundle(data, actionTmp);
                                break;
                        }
                    }
                }
            }
        }
        if (collections != null && !collections.isEmpty()) {
            for (ItemComponent component : collections) {
                data.put(component.getComponentId(), (Object) component.convertToSubmitData());
            }
        }
        JSONObject operate = new JSONObject();
        switch (actionType) {
            case UPDATE_QUANTITY:
                operate.put(UpdatePreference.UT_CLICK_UPDATE, (Object) actionKeylist);
                break;
            case UPDATE_SKU:
                operate.put("updateItemSku", (Object) actionKeylist);
                break;
            case DELETE:
                operate.put("deleteSome", (Object) actionKeylist);
                break;
            case ADD_FAVORITE:
                operate.put("addFavor", (Object) actionKeylist);
                break;
            case CHECK:
                operate.put("check", (Object) actionKeylist);
                break;
            case UNCHECK:
                operate.put("check", (Object) actionKeylist);
                break;
            case DELETE_INVALID:
                operate.put("deleteInvalid", (Object) actionKeylist);
                break;
            case UNFOLD_SHOP:
                operate.put("queryFoldedItem", (Object) actionKeylist);
                break;
        }
        CartEngineContext context = CartEngine.getInstance(this.cartFrom).getContext();
        JSONObject submitData = new JSONObject();
        submitData.put("data", (Object) data);
        submitData.put("operate", (Object) operate);
        JSONObject structure = new JSONObject();
        structure.put("structure", (Object) context.getStructure());
        submitData.put("hierarchy", (Object) structure);
        submitData.put("pageMeta", (Object) context.getPageMeta());
        if (!this.submitProtocols.isEmpty()) {
            for (SubmitProtocol entry : this.submitProtocols) {
                entry.setSubmitData(submitData, RequestType.getByActionType(actionType));
            }
        }
        return submitData;
    }

    private void addItemComponentUnderBundle(JSONObject data, Component actionTmp) {
        Component bundleComponent;
        List<ItemComponent> itemComponents;
        Component itemParent = actionTmp.getParent();
        if (itemParent != null && ComponentTag.getComponentTagByDesc(itemParent.getTag()) == ComponentTag.GROUP && (itemParent instanceof GroupComponent) && (bundleComponent = itemParent.getParent()) != null && (itemComponents = CartEngine.getInstance(this.cartFrom).getItemComponentIdsByBundleId(bundleComponent.getComponentId())) != null && itemComponents.size() > 0) {
            for (ItemComponent itemComponentTmp : itemComponents) {
                if (!(itemComponentTmp == null || itemComponentTmp.getComponentId() == null || itemComponentTmp.getComponentId().equals(actionTmp.getComponentId()))) {
                    data.put(itemComponentTmp.getComponentId(), (Object) itemComponentTmp.convertToSubmitData());
                }
            }
        }
    }

    private void addFoldingBarComponentByItem(JSONObject data, Component actionTmp) {
        FoldingBarComponent fbc;
        if (ComponentTag.getComponentTagByDesc(actionTmp.getTag()) == ComponentTag.ITEM && (actionTmp instanceof ItemComponent) && (fbc = CartEngine.getInstance(this.cartFrom).getFoldingBarComponentByItemComponent((ItemComponent) actionTmp)) != null && data != null && !data.containsKey(fbc.getComponentId())) {
            data.put(fbc.getComponentId(), (Object) fbc.convertToSubmitData());
        }
    }

    public JSONObject generateFinalSubmitData() {
        Map<String, Component> index;
        CartEngineContext context = CartEngine.getInstance(this.cartFrom).getContext();
        if (context == null || (index = context.getIndex()) == null || index.size() == 0) {
            return null;
        }
        JSONArray cartIdlist = new JSONArray();
        for (Map.Entry<String, Component> entry : index.entrySet()) {
            Component obj = entry.getValue();
            if (obj != null) {
                Component component = obj;
                if (ComponentTag.getComponentTagByDesc(component.getTag()) == ComponentTag.ITEM && (component instanceof ItemComponent)) {
                    ItemComponent itemComponent = (ItemComponent) component;
                    if (itemComponent.isChecked() && itemComponent.isValid()) {
                        cartIdlist.add(itemComponent.getCartId());
                    }
                }
            }
        }
        JSONObject submitData = new JSONObject();
        submitData.put("cartIds", (Object) cartIdlist);
        return submitData;
    }
}
