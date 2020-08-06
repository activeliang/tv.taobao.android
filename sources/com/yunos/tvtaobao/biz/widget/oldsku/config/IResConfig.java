package com.yunos.tvtaobao.biz.widget.oldsku.config;

import android.content.Context;
import android.graphics.drawable.Drawable;

public interface IResConfig {

    public enum GoodsType {
        TAOBAO,
        TMALL
    }

    int getColor();

    Context getContext();

    GoodsType getGoodsType();

    Drawable getQRCodeIcon();

    void setCanBuy(boolean z);

    void setGreenStatus(String str);
}
