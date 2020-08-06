package com.yunos.tvtaobao.tvsdk.widget.dialog;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import com.yunos.tv.aliTvSdk.R;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusButton;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.Params;

public class AlertDialogFocusButton extends FocusButton {
    private boolean mIsDeepFocus = false;
    private int mTextUnSelectedColor = 0;

    public AlertDialogFocusButton(Context context) {
        super(context);
        init(context);
    }

    public AlertDialogFocusButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AlertDialogFocusButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public boolean isFocusBackground() {
        return true;
    }

    public boolean isScale() {
        return false;
    }

    private void init(Context context) {
        this.mParams = new Params(1.0f, 1.0f, 5, (Interpolator) null, true, 5, new DecelerateInterpolator());
        this.mTextUnSelectedColor = context.getResources().getColor(R.color.tui_text_color_white_50_alpha);
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            setTextColor(-1);
        } else {
            setTextColor(this.mTextUnSelectedColor);
        }
    }
}
