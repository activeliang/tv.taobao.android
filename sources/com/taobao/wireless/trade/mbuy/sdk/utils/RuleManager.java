package com.taobao.wireless.trade.mbuy.sdk.utils;

import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.co.ComponentTag;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.ShipDatePickerComponent;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyParseModule;
import com.taobao.wireless.trade.mbuy.sdk.engine.SplitJoinRule;
import java.util.ArrayList;
import java.util.List;

public class RuleManager {
    public static void registerShipRule(BuyParseModule parseModule) {
        parseModule.registerSplitJoinRule(ComponentTag.DELIVERY_METHOD, new SplitJoinRule() {
            public List<Component> execute(List<Component> input) {
                List<Component> output = new ArrayList<>(input.size() + 1);
                for (Component component : input) {
                    output.add(component);
                    if (ComponentTag.getComponentTagByDesc(component.getTag()) == ComponentTag.DELIVERY_METHOD) {
                        output.add(new ShipDatePickerComponent(component));
                    }
                }
                return output;
            }
        });
    }
}
