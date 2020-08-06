package com.taobao.wireless.trade.mbuy.sdk.engine;

import android.util.Log;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.co.ComponentFactory;
import com.taobao.wireless.trade.mbuy.sdk.co.ComponentStatus;
import com.taobao.wireless.trade.mbuy.sdk.co.ComponentTag;
import com.taobao.wireless.trade.mbuy.sdk.co.ComponentType;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.CascadeComponent;
import com.taobao.wireless.trade.mbuy.sdk.utils.RuleManager;
import com.yunos.tvtaobao.biz.focus_impl.FocusRoot;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BuyParseModule {
    private static final String TAG = BuyParseModule.class.getSimpleName();
    protected BuyEngine engine;
    private ExpandParseRule expandParseRule;
    private SplitJoinRule[] rules = new SplitJoinRule[ComponentTag.size()];

    public BuyParseModule(BuyEngine engine2) {
        this.engine = engine2;
    }

    public void registerSplitJoinRule(ComponentTag componentTag, SplitJoinRule rule) {
        if (componentTag != null && componentTag != ComponentTag.UNKNOWN) {
            this.rules[componentTag.index] = rule;
        }
    }

    public void registerInternalSplitJoinRule() {
        RuleManager.registerShipRule(this);
    }

    public void registerExpandParseRule(ExpandParseRule expandParseRule2) {
        this.expandParseRule = expandParseRule2;
    }

    public ExpandParseRule getExpandParseRule() {
        return this.expandParseRule;
    }

    public List<Component> parse(JSONObject origin) {
        List<Component> output = null;
        if (origin != null) {
            try {
                if (origin.getBooleanValue("reload")) {
                    output = parseFirstResponseJson(origin);
                } else {
                    output = parseAsyncResponseJson(origin);
                }
                this.engine.getContext().setOutput(output);
            } catch (Throwable th) {
            }
        }
        return output;
    }

    private List<Component> parseFirstResponseJson(JSONObject origin) {
        List<Component> output = null;
        if (origin != null) {
            this.engine.freeLinkageEngine();
            this.engine.initContext();
            BuyEngineContext context = this.engine.getContext();
            JSONObject data = origin.getJSONObject("data");
            JSONObject hierarchy = origin.getJSONObject("hierarchy");
            JSONObject linkage = origin.getJSONObject("linkage");
            if (!(data == null || hierarchy == null || linkage == null)) {
                context.setOrigin(origin);
                context.setData(data);
                context.setHierarchy(hierarchy);
                context.setLinkage(linkage);
                JSONObject structure = hierarchy.getJSONObject("structure");
                JSONObject common = linkage.getJSONObject("common");
                if (structure != null) {
                    context.setStructure(structure);
                    context.setCommon(common);
                    String rootComponentKey = hierarchy.getString(FocusRoot.TAG_FLAG_FOR_ROOT);
                    if (rootComponentKey != null && !rootComponentKey.isEmpty()) {
                        output = null;
                        try {
                            output = resolve(rootComponentKey, (Component) null);
                        } catch (Throwable th) {
                        }
                        if (output == null || output.isEmpty()) {
                            this.engine.profileModule.commitEvent(ProfilePoint.RECURSIVE_PARSE_ERROR, new String[0]);
                        } else {
                            buildOutputView(output);
                            this.engine.startLinkageEngine();
                        }
                    }
                }
            }
        }
        return output;
    }

    private List<Component> parseAsyncResponseJson(JSONObject origin) {
        JSONObject data;
        BuyEngineContext context = this.engine.getContext();
        List<Component> output = context.getOutput();
        Map<String, Component> index = context.getIndex();
        if (!(origin == null || index == null || (data = origin.getJSONObject("data")) == null)) {
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
            buildOutputView(output);
            Log.d(TAG, "Linkage Before Replacing: \n" + context.getLinkage());
            JSONObject linkage = origin.getJSONObject("linkage");
            if (linkage != null) {
                for (Map.Entry<String, Object> entry2 : linkage.entrySet()) {
                    if ("common".equals(entry2.getKey())) {
                        JSONObject common = (JSONObject) entry2.getValue();
                        if (common != null) {
                            JSONObject originalCommon = context.getCommon();
                            if (originalCommon != null) {
                                for (Map.Entry<String, Object> entryCommon : common.entrySet()) {
                                    originalCommon.put(entryCommon.getKey(), entryCommon.getValue());
                                }
                            } else {
                                context.setCommon(common);
                            }
                        }
                    } else {
                        context.getLinkage().put(entry2.getKey(), entry2.getValue());
                    }
                }
            }
            Log.d(TAG, "Linkage After Replacing: \n" + context.getLinkage());
            this.engine.getContext().setRollbackProtocol((RollbackProtocol) null);
            this.engine.startLinkageEngine();
        }
        return output;
    }

    public Component getComponentByTag(ComponentTag selfTag, ComponentTag parentTag) {
        return getComponentByTag(selfTag.desc, parentTag);
    }

    public Component getComponentByTag(String selfTag, ComponentTag parentTag) {
        BuyEngineContext context = this.engine.getContext();
        if (selfTag == null || context == null) {
            return null;
        }
        List<Component> output = context.getOutput();
        if (output == null) {
            return null;
        }
        boolean checkParent = (parentTag == null || parentTag == ComponentTag.UNKNOWN) ? false : true;
        for (Component target : output) {
            if (selfTag.equals(target.getTag())) {
                if (!checkParent) {
                    return target;
                }
                Component targetParent = target.getParent();
                if (targetParent != null) {
                    if (ComponentTag.getComponentTagByDesc(targetParent.getTag()) == parentTag) {
                    }
                    return target;
                }
            }
        }
        return null;
    }

    public Component getComponentByType(ComponentType type) {
        BuyEngineContext context = this.engine.getContext();
        if (type == null || context == null) {
            return null;
        }
        List<Component> output = context.getOutput();
        if (output == null) {
            return null;
        }
        for (Component target : output) {
            if (target.getType() == type) {
                return target;
            }
        }
        return null;
    }

    private List<Component> resolve(String componentKey, Component parent) {
        ComponentTag tag;
        SplitJoinRule r;
        if (componentKey == null || componentKey.isEmpty()) {
            return null;
        }
        BuyEngineContext context = this.engine.getContext();
        List<Component> list = new ArrayList<>();
        JSONObject data = context.getData();
        JSONObject structure = context.getStructure();
        Map<String, Component> index = context.getIndex();
        Component current = null;
        try {
            current = ComponentFactory.make(data.getJSONObject(componentKey), this.engine);
        } catch (Throwable th) {
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
                List<Component> subComponent = resolve((String) it.next(), current);
                if (subComponent != null) {
                    list.addAll(subComponent);
                }
            }
        }
        if (current == null || (tag = ComponentTag.getComponentTagByDesc(current.getTag())) == null || tag.equals(ComponentTag.UNKNOWN) || (r = this.rules[tag.index]) == null) {
            return list;
        }
        return r.execute(list);
    }

    private void buildOutputView(List<Component> list) {
        for (Component component : list) {
            if (component.getType() == ComponentType.CASCADE) {
                CascadeComponent cc = (CascadeComponent) component;
                for (Component t : cc.getTargets()) {
                    if (cc.isExpand()) {
                        t.setStatus(ComponentStatus.NORMAL);
                    } else {
                        t.setStatus(ComponentStatus.HIDDEN);
                    }
                }
            }
        }
    }
}
