package com.alibaba.appmonitor.model;

import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.utils.ParseUtils;
import com.alibaba.appmonitor.pool.BalancedPool;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import java.util.Map;

public class UTDimensionValueSet extends DimensionValueSet {
    public Integer getEventId() {
        String eventIdStr;
        int eventId = 0;
        if (!(this.map == null || (eventIdStr = (String) this.map.get(LogField.EVENTID.toString())) == null)) {
            try {
                eventId = ParseUtils.parseInteger(eventIdStr);
            } catch (NumberFormatException e) {
            }
        }
        return Integer.valueOf(eventId);
    }

    public static UTDimensionValueSet create(Map<String, String> rawMsg) {
        return (UTDimensionValueSet) BalancedPool.getInstance().poll(UTDimensionValueSet.class, rawMsg);
    }

    public void clean() {
        super.clean();
    }

    public void fill(Object... params) {
        super.fill(params);
    }
}
