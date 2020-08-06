package com.yunos.tvtaobao.biz.focus_impl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.internal.view.SupportMenu;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tvtaobao.biz.focus_impl.FocusNode;
import com.zhiping.dev.android.logger.ZpLogger;

public class FocusArea extends FrameLayout implements FocusNode.Binder {
    private FocusNode focusNode;
    Paint paint;

    public FocusArea(@NonNull Context context) {
        this(context, (AttributeSet) null);
    }

    public FocusArea(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusArea(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.focusNode = new FocusNode(this);
        this.paint = null;
        setWillNotDraw(false);
    }

    public boolean isInEditMode() {
        return true;
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility != 0) {
            this.focusNode.setNodeFocusable(false);
        } else {
            this.focusNode.setNodeFocusable(true);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        this.focusNode.onFocusDraw(canvas);
        if (this.paint == null) {
            this.paint = new Paint();
            this.paint.setColor(SupportMenu.CATEGORY_MASK);
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() != 19 && event.getKeyCode() != 20 && event.getKeyCode() != 21 && event.getKeyCode() != 22) {
            return super.dispatchKeyEvent(event);
        }
        ZpLogger.d(getClass().getSimpleName(), Constant.NLP_CACHE_TYPE + hashCode() + " dispatchKeyEvent " + event.getKeyCode());
        return false;
    }

    public void invalidate(Rect dirty) {
        super.invalidate(dirty);
    }

    public void invalidate() {
        super.invalidate();
    }

    public View getView() {
        return this;
    }

    public FocusNode getNode() {
        return this.focusNode;
    }

    public void setFocusConsumer(FocusConsumer focusConsumer) {
        this.focusNode.setFocusConsumer(focusConsumer);
    }
}
