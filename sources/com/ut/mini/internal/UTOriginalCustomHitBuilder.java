package com.ut.mini.internal;

import com.alibaba.analytics.utils.StringUtils;
import com.ut.mini.UTHitBuilders;
import java.util.Map;

public class UTOriginalCustomHitBuilder extends UTHitBuilders.UTHitBuilder {
    public UTOriginalCustomHitBuilder(String aPage, int aEventId, String aArg1, String aArg2, String aArg3, Map<String, String> aHitMap) {
        if (!StringUtils.isEmpty(aPage)) {
            super.setProperty(UTHitBuilders.UTHitBuilder.FIELD_PAGE, aPage);
        }
        super.setProperty(UTHitBuilders.UTHitBuilder.FIELD_EVENT_ID, "" + aEventId);
        if (!StringUtils.isEmpty(aArg1)) {
            super.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG1, aArg1);
        }
        if (!StringUtils.isEmpty(aArg2)) {
            super.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG2, aArg2);
        }
        if (!StringUtils.isEmpty(aArg3)) {
            super.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG3, aArg3);
        }
        super.setProperties(aHitMap);
    }
}
