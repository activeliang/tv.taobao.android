package com.taobao.wireless.trade.mbuy.sdk.engine;

import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.co.ComponentStatus;
import com.taobao.wireless.trade.mbuy.sdk.co.ComponentType;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.CascadeComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuyValidateModule {
    protected BuyEngine engine;

    public BuyValidateModule(BuyEngine engine2) {
        this.engine = engine2;
    }

    public ValidateResult execute() {
        ValidateResult result = new ValidateResult();
        result.setValid(true);
        BuyEngineContext context = this.engine.getContext();
        if (context == null) {
            return result;
        }
        List<String> disableCascadeTargets = getDisableCascadeTargets();
        Map<String, Component> index = context.getIndex();
        if (index == null) {
            return result;
        }
        for (Component component : index.values()) {
            if (component.getStatus() != ComponentStatus.HIDDEN && !disableCascadeTargets.contains(component.getKey())) {
                ValidateResult r = component.validate();
                if (!r.isValid()) {
                    r.setInvalidComponent(component);
                    return r;
                }
            }
        }
        return result;
    }

    private List<String> getDisableCascadeTargets() {
        BuyEngineContext context = this.engine.getContext();
        List<String> list = new ArrayList<>();
        if (context.getIndex() != null) {
            for (Component component : context.getIndex().values()) {
                if (component.getType() == ComponentType.CASCADE) {
                    CascadeComponent cc = (CascadeComponent) component;
                    if (!cc.isExpand()) {
                        for (Component c : cc.getTargets()) {
                            list.add(c.getKey());
                        }
                    }
                }
            }
        }
        return list;
    }
}
