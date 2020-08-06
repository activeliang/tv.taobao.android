package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.common.OSSHeaders;
import com.taobao.wireless.trade.mcart.sdk.utils.StringUtils;

public class ItemPay {
    private JSONObject data;

    public ItemPay(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public long getNow() {
        return this.data.getLongValue("now");
    }

    public String getNowTitle() {
        return this.data.getString("nowTitle");
    }

    public String getNowTitleAppend() {
        return this.data.getString("nowTitleAppend");
    }

    public String getTotalNowPriceTitle() {
        return this.data.getString("totalTitle");
    }

    public long getTotalNowPrice() {
        return this.data.getLongValue("total");
    }

    public Long getAfterPromPrice() {
        return this.data.getLong("afterPromPrice");
    }

    public void setAfterPromPrice(Long price) {
        this.data.put("afterPromPrice", (Object) price);
    }

    public long getOrigin() {
        return this.data.getLongValue(OSSHeaders.ORIGIN);
    }

    public String getOriginTitle() {
        return this.data.getString("originTitle");
    }

    public String getCurrencySymbol() {
        String currencySymbol = this.data.getString("currencySymbol");
        return StringUtils.isBlank(currencySymbol) ? "￥" : currencySymbol;
    }

    public int getCurrencyUnitFactor() {
        int currencyUnitFactor = this.data.getIntValue("currencyUnitFactor");
        if (currencyUnitFactor == 0) {
            return 100;
        }
        return currencyUnitFactor;
    }

    public String toString() {
        return super.toString() + " - ItemPay [now =" + getNow() + ",nowTitle=" + getNowTitle() + ",totalNowPriceTitle=" + getTotalNowPriceTitle() + ",totalNowPrice=" + getTotalNowPrice() + ",origin=" + getOrigin() + ",originTitle=" + getOriginTitle() + "]";
    }
}
