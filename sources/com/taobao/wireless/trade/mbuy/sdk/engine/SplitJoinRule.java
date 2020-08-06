package com.taobao.wireless.trade.mbuy.sdk.engine;

import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import java.util.List;

public interface SplitJoinRule {
    List<Component> execute(List<Component> list);
}
