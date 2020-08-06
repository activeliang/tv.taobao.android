package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.taobao.wireless.trade.mbuy.sdk.co.misc.Option;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class InstallmentOption extends Option {
    private BuyEngine engine;

    public InstallmentOption(JSONObject data, BuyEngine engine2) {
        super(data);
        this.engine = engine2;
    }

    public int getNum() {
        return this.data.getIntValue("num");
    }

    public String getTitle() {
        return this.data.getString("display");
    }

    public String getSubtitle() {
        return this.data.getString("subtitle");
    }

    public String getTip() {
        return this.data.getString("tip");
    }

    public String getCurrencySymbol() {
        return this.engine.getCurrencySymbol();
    }

    public double getPoundage() {
        return this.data.getDoubleValue("poundage");
    }

    public String getPoundageText() {
        double poundage = getPoundage();
        if (poundage <= ClientTraceData.b.f47a) {
            return "0.00";
        }
        return String.format("%.2f", new Object[]{Double.valueOf(poundage)});
    }
}
