package com.taobao.taobaoavsdk.widget.extra;

import com.alibaba.ut.abtest.UTABTest;
import com.alibaba.ut.abtest.Variation;
import com.alibaba.ut.abtest.VariationSet;
import com.taobao.adapter.ABTestAdapter;

public class TaobaoABTestAdapter implements ABTestAdapter {
    public String getBucket(String component, String module) {
        Variation variation;
        try {
            VariationSet variationSet = UTABTest.activate(component, module);
            if (!(variationSet == null || variationSet.size() <= 0 || (variation = variationSet.getVariation("bucket")) == null)) {
                return variation.getValueAsString("bucket");
            }
        } catch (Throwable th) {
        }
        return "";
    }
}
