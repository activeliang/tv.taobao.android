package com.yunos.tvtaobao.biz.widget.oldsku.config;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.yunos.tvtaobao.biz.widget.oldsku.config.IResConfig;
import com.yunos.tvtaobao.businessview.R;

public class TaobaoResConfig extends AbstractResConfig {
    public TaobaoResConfig(Context context) {
        super(context);
    }

    public IResConfig.GoodsType getGoodsType() {
        return IResConfig.GoodsType.TAOBAO;
    }

    public int getColor() {
        return this.mContext.getResources().getColor(R.color.ytm_orange);
    }

    public Drawable getQRCodeIcon() {
        return this.mContext.getResources().getDrawable(R.drawable.tradelink_qr_code_icon_taobao);
    }

    public Context getContext() {
        return this.mContext;
    }
}
