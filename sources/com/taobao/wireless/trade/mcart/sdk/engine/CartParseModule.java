package com.taobao.wireless.trade.mcart.sdk.engine;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.co.ComponentFactory;
import com.taobao.wireless.trade.mcart.sdk.co.ComponentTag;
import com.taobao.wireless.trade.mcart.sdk.co.biz.BundleComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.CartStructure;
import com.taobao.wireless.trade.mcart.sdk.co.biz.FoldingBarComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.GroupComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ItemComponent;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;
import com.yunos.tvtaobao.biz.focus_impl.FocusRoot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CartParseModule {
    private CartFrom cartFrom = CartFrom.DEFAULT_CLIENT;
    private final Map<String, ParseProtocol> parseProtocols = new HashMap();
    private Map<ComponentTag, SplitJoinRule> rules = new HashMap();

    public void registerParseCallback(String key, ParseProtocol parseProtocol) {
        this.parseProtocols.put(key, parseProtocol);
    }

    public void unregisterParseCallback(String key) {
        this.parseProtocols.remove(key);
    }

    public void removeAllParseCallbacks() {
        this.parseProtocols.clear();
    }

    private CartParseModule() {
    }

    public CartParseModule(CartFrom cartFrom2) {
        this.cartFrom = cartFrom2 == null ? CartFrom.DEFAULT_CLIENT : cartFrom2;
    }

    public void registerSplitJoinRule(ComponentTag componentTag, SplitJoinRule rule) {
        this.rules.put(componentTag, rule);
    }

    public void removeSplitJoinRule(ComponentTag componentTag) {
        if (this.rules.containsKey(componentTag)) {
            this.rules.remove(componentTag);
        }
    }

    public List<Component> parseFoldingBarData(JSONObject origin) {
        if (origin == null) {
            return null;
        }
        List<Component> output = null;
        CartEngineContext context = CartEngine.getInstance(this.cartFrom).getContext();
        synchronized (this) {
            if (context != null) {
                if (context.getOutput() != null && context.getOutput().size() >= 0) {
                    output = parseAsyncResponseJson(origin);
                }
            }
        }
        if (output != null) {
            context = CartEngine.getInstance(this.cartFrom).getContext();
            context.setOutput(output);
        }
        JSONObject controlParas = origin.getJSONObject("controlParas");
        if (controlParas == null) {
            return output;
        }
        context.setControlParas(controlParas);
        return output;
    }

    public List<Component> parse(JSONObject origin) {
        List<Component> output;
        if (origin == null) {
            return null;
        }
        CartEngineContext context = CartEngine.getInstance(this.cartFrom).getContext();
        synchronized (this) {
            if (context != null) {
                if (context.getOutput() != null && context.getOutput().size() >= 0) {
                    output = parseAsyncResponseJson(origin);
                }
            }
            output = parseFirstResponseJson(origin);
        }
        CartEngineContext context2 = CartEngine.getInstance(this.cartFrom).getContext();
        context2.setOutput(output);
        JSONObject pageMeta = origin.getJSONObject("pageMeta");
        if (pageMeta != null) {
            context2.setPageMeta(pageMeta);
        }
        JSONObject feature = origin.getJSONObject("feature");
        if (feature != null) {
            context2.setFeature(feature);
        }
        JSONObject controlParas = origin.getJSONObject("controlParas");
        if (controlParas != null) {
            context2.setControlParas(controlParas);
        }
        JSONObject excludes = origin.getJSONObject("excludes");
        if (excludes != null) {
            context2.setExcludes(excludes);
        }
        if (this.parseProtocols.isEmpty()) {
            return output;
        }
        for (Map.Entry<String, ParseProtocol> entry : this.parseProtocols.entrySet()) {
            String key = entry.getKey();
            ParseProtocol parseProtocol = entry.getValue();
            JSONObject value = origin.getJSONObject(key);
            if (parseProtocol != null) {
                parseProtocol.parse(key, value);
            }
        }
        return output;
    }

    private List<Component> parseFirstResponseJson(JSONObject origin) {
        if (origin == null) {
            return null;
        }
        CartEngine engine = CartEngine.getInstance(this.cartFrom);
        JSONObject data = origin.getJSONObject("data");
        JSONObject hierarchy = origin.getJSONObject("hierarchy");
        if (data == null || hierarchy == null) {
            return null;
        }
        CartEngineContext context = engine.getContext();
        context.setData(data);
        context.setHierarchy(hierarchy);
        context.setStructure(hierarchy.getJSONObject("structure"));
        String rootComponentKey = hierarchy.getString(FocusRoot.TAG_FLAG_FOR_ROOT);
        if (rootComponentKey == null) {
            return null;
        }
        List<Component> output = resolve(rootComponentKey, (Component) null);
        context.setOutput(output);
        return output;
    }

    private List<Component> parseAsyncResponseJson(JSONObject origin) {
        JSONObject data;
        CartEngineContext context = CartEngine.getInstance(this.cartFrom).getContext();
        if (context == null) {
            return null;
        }
        List<Component> output = context.getOutput();
        Map<String, Component> index = context.getIndex();
        JSONObject oldData = context.getData();
        if (origin == null || index == null || oldData == null || (data = origin.getJSONObject("data")) == null) {
            return output;
        }
        JSONObject hierarchy = origin.getJSONObject("hierarchy");
        JSONObject structure = null;
        String rootComponentKey = null;
        if (hierarchy != null) {
            structure = hierarchy.getJSONObject("structure");
            rootComponentKey = hierarchy.getString(FocusRoot.TAG_FLAG_FOR_ROOT);
        }
        if (structure == null || rootComponentKey == null) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                JSONObject componentData = (JSONObject) entry.getValue();
                Component component = index.get(entry.getKey());
                if (component != null) {
                    try {
                        component.reload(componentData);
                    } catch (Throwable th) {
                    }
                }
            }
            return output;
        }
        context.setHierarchy(hierarchy);
        context.setStructure(structure);
        for (Map.Entry<String, Object> entry2 : data.entrySet()) {
            String componentKey = entry2.getKey();
            JSONObject componentData2 = (JSONObject) entry2.getValue();
            Component component2 = index.get(componentKey);
            if (component2 == null) {
                try {
                    oldData.put(componentKey, (Object) componentData2);
                } catch (Throwable th2) {
                }
            } else {
                try {
                    component2.reload(componentData2);
                } catch (Throwable th3) {
                }
            }
        }
        HashSet<String> validComponentKeys = new HashSet<>();
        List<Component> outputMerge = resolveAsync(rootComponentKey, (Component) null, validComponentKeys);
        context.setOutput(outputMerge);
        List<Component> output2 = outputMerge;
        if (validComponentKeys == null || validComponentKeys.size() <= 0) {
            return output2;
        }
        Iterator<Map.Entry<String, Object>> itData = oldData.entrySet().iterator();
        while (itData.hasNext()) {
            if (!validComponentKeys.contains(itData.next().getKey())) {
                itData.remove();
            }
        }
        Iterator<Map.Entry<String, Component>> itIndex = index.entrySet().iterator();
        while (itIndex.hasNext()) {
            if (!validComponentKeys.contains(itIndex.next().getKey())) {
                itIndex.remove();
            }
        }
        return output2;
    }

    private void refreshGroupPromotion(JSONObject component, Map<String, Component> index) {
        String ruleId = component.getString("ruleId");
        JSONObject promotion = component.getJSONObject("groupPromotion");
        if (!TextUtils.isEmpty(ruleId) && promotion != null) {
            for (Map.Entry<String, Component> entry : index.entrySet()) {
                Component co = entry.getValue();
                if (entry.getValue() instanceof GroupComponent) {
                    GroupComponent groupComponent = (GroupComponent) co;
                    if (ruleId.equals(groupComponent.getRuleId())) {
                        groupComponent.updatePromotion(promotion);
                    }
                }
            }
        }
    }

    private List<Component> resolveAsync(String componentKey, Component parent, HashSet<String> validComponentKeys) {
        List<Component> subComponent;
        CartEngineContext context = CartEngine.getInstance(this.cartFrom).getContext();
        if (componentKey == null || context == null) {
            return null;
        }
        List<Component> list = new ArrayList<>();
        JSONObject data = context.getData();
        JSONObject structure = context.getStructure();
        Map<String, Component> index = context.getIndex();
        JSONObject componentData = data.getJSONObject(componentKey);
        Component current = null;
        try {
            current = index.get(componentKey);
            if (current == null) {
                current = ComponentFactory.make(componentData, this.cartFrom);
            }
        } catch (Exception e) {
        }
        if (current != null) {
            current.setParent(parent);
            list.add(current);
            if (index != null && !index.containsKey(componentKey)) {
                index.put(componentKey, current);
            }
            validComponentKeys.add(componentKey);
        }
        JSONArray subComponentKeyArray = structure.getJSONArray(componentKey);
        if (subComponentKeyArray != null) {
            Iterator<Object> it = subComponentKeyArray.iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                if (!(obj == null || (subComponent = resolveAsync((String) obj, current, validComponentKeys)) == null)) {
                    list.addAll(subComponent);
                }
            }
        }
        if (current == null) {
            return list;
        }
        SplitJoinRule r = this.rules.get(ComponentTag.getComponentTagByDesc(current.getTag()));
        if (r != null) {
            return r.execute(list, this.cartFrom);
        }
        return list;
    }

    private List<Component> resolve(String componentKey, Component parent) {
        List<Component> subComponent;
        CartEngineContext context = CartEngine.getInstance(this.cartFrom).getContext();
        if (componentKey == null || context == null) {
            return null;
        }
        List<Component> list = new ArrayList<>();
        JSONObject data = context.getData();
        JSONObject structure = context.getStructure();
        Map<String, Component> index = context.getIndex();
        Component current = null;
        try {
            current = ComponentFactory.make(data.getJSONObject(componentKey), this.cartFrom);
        } catch (Exception e) {
        }
        if (current != null) {
            current.setParent(parent);
            list.add(current);
            index.put(componentKey, current);
        }
        JSONArray subComponentKeyArray = structure.getJSONArray(componentKey);
        if (subComponentKeyArray != null) {
            Iterator<Object> it = subComponentKeyArray.iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                if (!(obj == null || (subComponent = resolve((String) obj, current)) == null)) {
                    list.addAll(subComponent);
                }
            }
        }
        if (current == null) {
            return list;
        }
        SplitJoinRule r = this.rules.get(ComponentTag.getComponentTagByDesc(current.getTag()));
        if (r != null) {
            return r.execute(list, this.cartFrom);
        }
        return list;
    }

    public CartStructure generateCartStructure() {
        List<Component> output;
        CartStructure cartStructure = null;
        CartEngineContext context = CartEngine.getInstance(this.cartFrom).getContext();
        if (!(context == null || (output = context.getOutput()) == null)) {
            cartStructure = new CartStructure();
            for (Component obj : output) {
                if (obj != null) {
                    if (ComponentTag.getComponentTagByDesc(obj.getTag()) == ComponentTag.ALL_ITEM || ComponentTag.getComponentTagByDesc(obj.getTag()) == ComponentTag.LABEL || ComponentTag.getComponentTagByDesc(obj.getTag()) == ComponentTag.BANNER || ComponentTag.getComponentTagByDesc(obj.getTag()) == ComponentTag.PROMOTIONBAR) {
                        cartStructure.getHeader().add(obj);
                    } else if (ComponentTag.getComponentTagByDesc(obj.getTag()) == ComponentTag.FOOTER) {
                        cartStructure.getFooter().add(obj);
                    } else {
                        cartStructure.getBody().add(obj);
                    }
                }
            }
        }
        return cartStructure;
    }

    public List<ItemComponent> getItemComponentsByBundleId(String bundleId) {
        Component bundleComponent;
        String subComponentKey;
        Component component;
        List<ItemComponent> itemComponentsByOrderId;
        List<ItemComponent> itemComponents = null;
        CartEngineContext context = CartEngine.getInstance(this.cartFrom).getContext();
        if (!(context == null || bundleId == null)) {
            JSONObject structure = context.getStructure();
            Map<String, Component> index = context.getIndex();
            if (!(structure == null || index == null || (bundleComponent = index.get(bundleId)) == null || ComponentTag.getComponentTagByDesc(bundleComponent.getTag()) != ComponentTag.BUNDLE)) {
                itemComponents = new ArrayList<>();
                JSONArray subComponentKeyArray = structure.getJSONArray(bundleId);
                if (subComponentKeyArray != null) {
                    Iterator<Object> it = subComponentKeyArray.iterator();
                    while (it.hasNext()) {
                        Object obj = it.next();
                        if (!(obj == null || (subComponentKey = (String) obj) == null || (component = index.get(subComponentKey)) == null || ComponentTag.getComponentTagByDesc(component.getTag()) != ComponentTag.GROUP || (itemComponentsByOrderId = getItemComponentsByOrderId(component.getComponentId())) == null)) {
                            itemComponents.addAll(itemComponentsByOrderId);
                        }
                    }
                }
            }
        }
        return itemComponents;
    }

    public List<ItemComponent> getItemComponentsByOrderId(String orderId) {
        Component orderComponent;
        String subComponentKey;
        Component component;
        List<ItemComponent> itemComponents = null;
        CartEngineContext context = CartEngine.getInstance(this.cartFrom).getContext();
        if (!(context == null || orderId == null)) {
            JSONObject structure = context.getStructure();
            Map<String, Component> index = context.getIndex();
            if (!(structure == null || index == null || (orderComponent = index.get(orderId)) == null || ComponentTag.getComponentTagByDesc(orderComponent.getTag()) != ComponentTag.GROUP)) {
                itemComponents = new ArrayList<>();
                JSONArray subComponentKeyArray = structure.getJSONArray(orderId);
                if (subComponentKeyArray != null) {
                    Iterator<Object> it = subComponentKeyArray.iterator();
                    while (it.hasNext()) {
                        Object obj = it.next();
                        if (!(obj == null || (subComponentKey = (String) obj) == null || (component = index.get(subComponentKey)) == null || ComponentTag.getComponentTagByDesc(component.getTag()) != ComponentTag.ITEM || !(component instanceof ItemComponent))) {
                            itemComponents.add((ItemComponent) component);
                        }
                    }
                }
            }
        }
        return itemComponents;
    }

    public List<ItemComponent> getAllValidItemComponents() {
        Map<String, Component> index;
        List<ItemComponent> itemComponents = null;
        CartEngineContext context = CartEngine.getInstance(this.cartFrom).getContext();
        if (!(context == null || (index = context.getIndex()) == null || index.size() == 0)) {
            itemComponents = new ArrayList<>();
            for (Map.Entry<String, Component> entry : index.entrySet()) {
                Component obj = entry.getValue();
                if (obj != null && ComponentTag.getComponentTagByDesc(obj.getTag()) == ComponentTag.ITEM && (obj instanceof ItemComponent)) {
                    ItemComponent itemComponentTmp = (ItemComponent) obj;
                    if (itemComponentTmp.isValid()) {
                        itemComponents.add(itemComponentTmp);
                    }
                }
            }
        }
        return itemComponents;
    }

    public FoldingBarComponent getFoldingBarComponentByItemComponent(ItemComponent itemComponent) {
        CartEngineContext context = CartEngine.getInstance(this.cartFrom).getContext();
        if (context == null) {
            return null;
        }
        Map<String, Component> index = context.getIndex();
        if (index == null || index.size() == 0) {
            return null;
        }
        if (itemComponent != null) {
            BundleComponent itemBundle = null;
            Component itemParent = itemComponent.getParent();
            int i = 0;
            while (true) {
                if (i < 10) {
                    if (itemParent == null || !(itemParent instanceof BundleComponent)) {
                        if (itemParent == null) {
                            break;
                        }
                        itemParent = itemParent.getParent();
                        i++;
                    } else {
                        itemBundle = (BundleComponent) itemParent;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (itemBundle != null) {
                for (Map.Entry<String, Component> entry : index.entrySet()) {
                    Component obj = entry.getValue();
                    if (obj instanceof FoldingBarComponent) {
                        Component fbcParent = obj.getParent();
                        int i2 = 0;
                        while (i2 < 10) {
                            if (fbcParent == null || !(fbcParent instanceof BundleComponent) || fbcParent != itemBundle) {
                                if (fbcParent == null) {
                                    break;
                                }
                                fbcParent = fbcParent.getParent();
                                i2++;
                            } else {
                                return (FoldingBarComponent) obj;
                            }
                        }
                        continue;
                    }
                }
            }
        }
        return null;
    }

    public List<ItemComponent> getAllCheckedValidItemComponents() {
        Map<String, Component> index;
        List<ItemComponent> itemComponents = null;
        CartEngineContext context = CartEngine.getInstance(this.cartFrom).getContext();
        if (!(context == null || (index = context.getIndex()) == null || index.size() == 0)) {
            itemComponents = new ArrayList<>();
            for (Map.Entry<String, Component> entry : index.entrySet()) {
                Component obj = entry.getValue();
                if (obj != null && ComponentTag.getComponentTagByDesc(obj.getTag()) == ComponentTag.ITEM && (obj instanceof ItemComponent)) {
                    ItemComponent itemComponentTmp = (ItemComponent) obj;
                    if (itemComponentTmp.isChecked() && itemComponentTmp.isValid()) {
                        itemComponents.add((ItemComponent) obj);
                    }
                }
            }
        }
        return itemComponents;
    }

    public List<ItemComponent> getAllCheckedValidAndPreBuyItemComponents() {
        Map<String, Component> index;
        List<ItemComponent> itemComponents = null;
        CartEngineContext context = CartEngine.getInstance(this.cartFrom).getContext();
        if (!(context == null || (index = context.getIndex()) == null || index.size() == 0)) {
            itemComponents = new ArrayList<>();
            for (Map.Entry<String, Component> entry : index.entrySet()) {
                Component obj = entry.getValue();
                if (obj != null && ComponentTag.getComponentTagByDesc(obj.getTag()) == ComponentTag.ITEM && (obj instanceof ItemComponent)) {
                    ItemComponent itemComponentTmp = (ItemComponent) obj;
                    if (itemComponentTmp.isChecked() && (itemComponentTmp.isValid() || itemComponentTmp.isPreBuyItem())) {
                        itemComponents.add((ItemComponent) obj);
                    }
                }
            }
        }
        return itemComponents;
    }
}
