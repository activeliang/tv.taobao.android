package com.yunos.tvtaobao.homebundle.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.animation.Interpolator;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusHListView;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.Params;
import com.yunos.tvtaobao.tvsdk.widget.interpolator.AccelerateDecelerateFrameInterpolator;

public class DetainMentListView extends FocusHListView {
    private Params mParamsFocus = new Params(1.05f, 1.05f, 5, (Interpolator) null, true, 10, new AccelerateDecelerateFrameInterpolator());

    public DetainMentListView(Context context) {
        super(context);
    }

    public DetainMentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DetainMentListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Params getParams() {
        if (this.mParamsFocus != null) {
            return this.mParamsFocus;
        }
        throw new IllegalArgumentException("The params is null, you must call setScaleParams before it's running");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 19) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 19) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public boolean preOnKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 19) {
            return true;
        }
        return super.preOnKeyDown(keyCode, event);
    }
}
