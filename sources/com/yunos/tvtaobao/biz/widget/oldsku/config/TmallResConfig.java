package com.yunos.tvtaobao.biz.widget.oldsku.config;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.yunos.tvtaobao.biz.widget.oldsku.config.IResConfig;
import com.yunos.tvtaobao.businessview.R;

public class TmallResConfig extends AbstractResConfig {
    public TmallResConfig(Context context) {
        super(context);
    }

    public IResConfig.GoodsType getGoodsType() {
        return IResConfig.GoodsType.TMALL;
    }

    public int getColor() {
        return this.mContext.getResources().getColor(R.color.ytm_tmall_red);
    }

    public Drawable getQRCodeIcon() {
        return this.mContext.getResources().getDrawable(R.drawable.tradelink_qr_code_icon_tmail);
    }

    public Context getContext() {
        return this.mContext;
    }
}
