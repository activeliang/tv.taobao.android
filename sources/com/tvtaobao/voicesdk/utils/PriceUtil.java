package com.tvtaobao.voicesdk.utils;

import java.text.DecimalFormat;

public class PriceUtil {
    static DecimalFormat df = new DecimalFormat("0.##");

    public static String getPrice(int price) {
        return df.format((double) (((float) price) / 100.0f));
    }
}
