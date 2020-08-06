package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusImageView;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.Params;
import com.yunos.tvtaobao.tvsdk.widget.interpolator.AccelerateDecelerateFrameInterpolator;

public class TbFocusImageView extends FocusImageView {
    protected Params mParams = new Params(1.05f, 1.05f, 10, (Interpolator) null, true, 20, new AccelerateDecelerateFrameInterpolator());

    public TbFocusImageView(Context arg0) {
        super(arg0);
    }

    public TbFocusImageView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public TbFocusImageView(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    public Params getParams() {
        if (this.mParams != null) {
            return this.mParams;
        }
        throw new IllegalArgumentException("The params is null, you must call setScaleParams before it's running");
    }

    public boolean isScale() {
        return true;
    }
}
