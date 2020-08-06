package com.yunos.tvtaobao.biz.widget.oldsku.config;

import android.content.Context;

public abstract class AbstractResConfig implements IResConfig {
    protected boolean canBuy = true;
    protected boolean isGreenStatus = true;
    protected Context mContext;

    public AbstractResConfig(Context context) {
        this.mContext = context;
    }

    public void setCanBuy(boolean canBuy2) {
        this.canBuy = canBuy2;
    }

    public void setGreenStatus(String buyStatus) {
        if ("即将开始".equals(buyStatus)) {
            this.isGreenStatus = true;
        } else {
            this.isGreenStatus = false;
        }
    }
}
