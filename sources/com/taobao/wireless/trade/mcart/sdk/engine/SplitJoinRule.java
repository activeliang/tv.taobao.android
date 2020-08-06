package com.taobao.wireless.trade.mcart.sdk.engine;

import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;
import java.util.List;

public interface SplitJoinRule {
    List<Component> execute(List<Component> list, CartFrom cartFrom);
}
