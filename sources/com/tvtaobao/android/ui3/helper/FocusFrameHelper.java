package com.tvtaobao.android.ui3.helper;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.tvtaobao.android.ui3.R;
import com.tvtaobao.android.ui3.helper.DrawableHelper;

public class FocusFrameHelper {
    private View anchor;
    private int dp10;
    private Drawable focusDrawable;
    private int framePadding = 6;
    private DrawableHelper.DrawableA rectDrawable;
    private DrawableHelper.DrawableA roundDrawable;

    public FocusFrameHelper(View view) {
        this.dp10 = view.getContext().getResources().getDimensionPixelSize(R.dimen.values_dp_10);
        this.framePadding = view.getContext().getResources().getDimensionPixelSize(R.dimen.values_dp_4);
        this.roundDrawable = DrawableHelper.mkStrokeRoundRectDrawable((float) this.dp10);
        this.rectDrawable = DrawableHelper.mkStrokeRectDrawable();
        attachView(view);
    }

    public void attachView(View view) {
        this.anchor = view;
        this.anchor.setPadding(this.framePadding, this.framePadding, this.framePadding, this.framePadding);
        this.anchor.postInvalidate();
    }

    public void drawFocusFrame(Canvas canvas) {
        if (this.focusDrawable != null) {
            this.focusDrawable.setBounds(0, 0, this.anchor.getWidth(), this.anchor.getHeight());
            this.focusDrawable.draw(canvas);
        }
    }

    public void setFocusDrawable(Drawable drawable) {
        this.focusDrawable = drawable;
        this.anchor.postInvalidate();
    }

    public DrawableHelper.DrawableA getRoundDrawable() {
        return this.roundDrawable;
    }

    public void setRoundDrawable(DrawableHelper.DrawableA roundDrawable2) {
        this.roundDrawable = roundDrawable2;
        this.anchor.postInvalidate();
    }

    public DrawableHelper.DrawableA getRectDrawable() {
        return this.rectDrawable;
    }

    public void setFramePadding(int framePadding2) {
        this.framePadding = framePadding2;
        attachView(this.anchor);
    }
}
