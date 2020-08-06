package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.yunos.tvtaobao.tvsdk.widget.FocusFinder;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;

public class InnerFocusLayout extends RelativeLayout implements ItemListener {
    private final String TAG = "InnerFocusLayout";
    private InnerFocusFinder mFocusFinder;
    protected FocusRectParams mFocusRectparams;
    private View mNextFocus;
    private OnInnerItemSelectedListener mOnInnerItemSelectedListener;
    private View mSelectedView;

    public interface OnInnerItemSelectedListener {
        void onInnerItemSelected(View view, boolean z, View view2);
    }

    public InnerFocusLayout(Context context) {
        super(context);
        initLayout(context);
    }

    public InnerFocusLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    public InnerFocusLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context);
    }

    public void setOnInnerItemSelectedListener(OnInnerItemSelectedListener listener) {
        this.mOnInnerItemSelectedListener = listener;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (23 != keyCode && 66 != keyCode && keyCode != 160) {
            return super.onKeyUp(keyCode, event);
        }
        if (isPressed()) {
            setPressed(false);
            if (this.mSelectedView != null) {
                this.mSelectedView.performClick();
            }
        }
        return true;
    }

    public void setNextFocusSelected() {
        if (this.mNextFocus != null && this.mNextFocus.isFocusable()) {
            performItemSelect(this.mSelectedView, false);
            this.mSelectedView = this.mNextFocus;
            this.mNextFocus = null;
            performItemSelect(this.mSelectedView, true);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 23 || keyCode == 66 || keyCode == 160) {
            setPressed(true);
            return true;
        } else if (this.mNextFocus == null || !this.mNextFocus.isFocusable()) {
            return super.onKeyDown(keyCode, event);
        } else {
            performItemSelect(this.mSelectedView, false);
            this.mSelectedView = this.mNextFocus;
            this.mNextFocus = null;
            performItemSelect(this.mSelectedView, true);
            return true;
        }
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (views == null || !isFocusable()) {
            return;
        }
        if ((focusableMode & 1) != 1 || !isInTouchMode() || isFocusableInTouchMode()) {
            views.add(this);
        }
    }

    public void getFocusedRect(Rect r) {
        View item = this.mSelectedView;
        if (!hasFocus() || item == null) {
            super.getFocusedRect(r);
            return;
        }
        item.getFocusedRect(r);
        offsetDescendantRectToMyCoords(item, r);
    }

    /* access modifiers changed from: protected */
    public View getFirstFocusView() {
        return null;
    }

    public boolean findFirstFocus(int keyCode) {
        Rect preRect = new Rect();
        View nextFocus = getFirstFocusView();
        if (nextFocus == null) {
            switch (keyCode) {
                case 19:
                    preRect.set(0, getHeight(), getWidth(), getHeight() + 1);
                    nextFocus = this.mFocusFinder.findNextFocusFromRect(this, preRect, 33);
                    break;
                case 20:
                    preRect.set(0, -1, getWidth(), 0);
                    nextFocus = this.mFocusFinder.findNextFocusFromRect(this, preRect, 130);
                    break;
                case 21:
                    preRect.set(getWidth(), 0, getWidth() + 1, getHeight());
                    nextFocus = this.mFocusFinder.findNextFocusFromRect(this, preRect, 17);
                    break;
                case 22:
                    preRect.set(-1, 0, 0, getHeight());
                    nextFocus = this.mFocusFinder.findNextFocusFromRect(this, preRect, 66);
                    break;
                default:
                    return false;
            }
        }
        this.mNextFocus = nextFocus;
        if (nextFocus != null) {
            return true;
        }
        ZpLogger.w("InnerFocusLayout", "findFirstFocus can not find the new focused");
        return false;
    }

    public boolean findNextFocus(int keyCode, KeyEvent event) {
        View nextFocus;
        ZpLogger.d("InnerFocusLayout", "preOnKeyDown keyCode = " + keyCode);
        View selectedView = this.mSelectedView;
        switch (keyCode) {
            case 19:
                nextFocus = this.mFocusFinder.findNextFocus(this, selectedView, 33);
                break;
            case 20:
                nextFocus = this.mFocusFinder.findNextFocus(this, selectedView, 130);
                break;
            case 21:
                nextFocus = this.mFocusFinder.findNextFocus(this, selectedView, 17);
                break;
            case 22:
                nextFocus = this.mFocusFinder.findNextFocus(this, selectedView, 66);
                break;
            default:
                return false;
        }
        this.mNextFocus = nextFocus;
        if (nextFocus != null) {
            return true;
        }
        ZpLogger.w("InnerFocusLayout", "findNextFocus can not find the new focused");
        return false;
    }

    public boolean isChangedInnerKey(int keyCode) {
        if (keyCode == 19) {
            return true;
        }
        return false;
    }

    public boolean isScale() {
        return false;
    }

    public FocusRectParams getFocusParams() {
        Rect r = new Rect();
        getFocusedRect(r);
        this.mFocusRectparams.set(r, 0.5f, 0.5f);
        return this.mFocusRectparams;
    }

    public int getItemWidth() {
        return getWidth();
    }

    public int getItemHeight() {
        return getHeight();
    }

    public Rect getManualPadding() {
        return null;
    }

    public void drawBeforeFocus(Canvas canvas) {
    }

    public void drawAfterFocus(Canvas canvas) {
    }

    public boolean isFinished() {
        return true;
    }

    public void clearItemSelected() {
        performItemSelect(this.mSelectedView, false);
        this.mSelectedView = null;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mFocusFinder != null) {
            this.mFocusFinder.clearFocusables();
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        initNode();
    }

    /* access modifiers changed from: protected */
    public void initNode() {
        this.mFocusFinder.clearFocusables();
        this.mFocusFinder.initFocusables(this);
    }

    private void initLayout(Context conext) {
        this.mFocusFinder = new InnerFocusFinder();
        this.mFocusRectparams = new FocusRectParams();
    }

    private void performItemSelect(View selectedView, boolean selected) {
        if (selectedView != null) {
            selectedView.setSelected(selected);
            if (this.mOnInnerItemSelectedListener != null) {
                this.mOnInnerItemSelectedListener.onInnerItemSelected(selectedView, selected, this);
            }
        }
    }

    private class InnerFocusFinder extends FocusFinder {
        private InnerFocusFinder() {
        }

        public void initFocusables(ViewGroup root) {
            for (int index = 0; index < root.getChildCount(); index++) {
                View child = root.getChildAt(index);
                if ((child instanceof ViewGroup) && !child.isFocusable() && child.getVisibility() == 0) {
                    initFocusables((ViewGroup) child);
                } else if (child.isFocusable() && child.getVisibility() == 0) {
                    addFocusable(child);
                }
            }
        }
    }
}
