package com.tvtaobao.android.focus3;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.tvtaobao.android.focus3.FocusAssist;

class FocusNode {
    private static final int FLAG_NOT_AVAILABLE = 1;
    private static final String TAG = FocusNode.class.getSimpleName();
    Drawable d;
    FocusAssist fa;
    FocusAssist.FocusListener fl;
    private int flag1 = 0;
    long focusInCount = 0;
    long focusOutCount = 0;
    Rect fpRect = new Rect();
    View v;

    private FocusNode() {
    }

    FocusNode(FocusAssist focusAssist, View view, FocusAssist.FocusListener focusListener, Drawable drawable, Rect focusPadding) {
        this.fa = focusAssist;
        this.v = view;
        this.fl = focusListener;
        this.d = drawable;
        if (focusPadding != null) {
            this.fpRect = focusPadding;
        } else {
            this.fpRect.set(6, 6, 6, 6);
        }
    }

    /* access modifiers changed from: package-private */
    public void doFocusChangedDispatch(View oldFocus, View newFocus) {
        Focus3Logger.i(TAG, ".doFocusChangedDispatch");
        boolean newFocusState = Focus3Util.isBinA(this.v, newFocus);
        boolean oldFocusState = Focus3Util.isBinA(this.v, oldFocus);
        if (newFocusState && !oldFocusState) {
            this.focusInCount++;
            if (this.fl != null) {
                this.fl.onFocusIn(this.v, oldFocus, newFocus);
            }
        } else if (!newFocusState && oldFocusState) {
            this.focusOutCount++;
            if (this.fl != null) {
                this.fl.onFocusOut(this.v, oldFocus, newFocus);
            }
        } else if (!newFocusState && !oldFocusState && this.focusInCount != this.focusOutCount && this.fl != null) {
            this.fl.onFocusOut(this.v, oldFocus, newFocus);
        }
        Focus3Logger.i(TAG, ".doFocusChangedDispatch done");
    }

    public boolean isHasFocus() {
        if (this.v != null) {
            return this.v.hasFocus();
        }
        return false;
    }

    public boolean isNodeAvailable() {
        Focus3Logger.i(TAG, ".isNodeAvailable");
        boolean rtn = doRealNodeAvailableJudge();
        Focus3Logger.i(TAG, ".isNodeAvailable done");
        return rtn;
    }

    private boolean doRealNodeAvailableJudge() {
        if ((this.flag1 & 1) == 1) {
            return false;
        }
        if (!(this.fa == null || this.fa.getFocusLayout() == null)) {
            if (Focus3Util.isBinA(this.fa.getFocusLayout(), this.v)) {
                this.flag1 &= -2;
            } else {
                this.flag1 |= 1;
            }
        }
        if ((this.flag1 & 1) != 1) {
            return true;
        }
        return false;
    }

    public void doFocusStateDraw(Canvas canvas) {
        if (canvas != null && isHasFocus() && this.fa != null) {
            Drawable drawable = this.d;
            FocusLayout focusLayout = this.fa.getFocusLayout();
            View mv = this.v;
            if (drawable != null && focusLayout != null && mv != null) {
                Rect rect = new Rect();
                if (mv.getGlobalVisibleRect(rect) && focusLayout.offsetGlobalVisibleRectToMyCoords(rect)) {
                    float w = ((float) rect.width()) * mv.getScaleX();
                    float h = ((float) rect.height()) * mv.getScaleY();
                    float wD = (w - ((float) rect.width())) / 2.0f;
                    float hD = (h - ((float) rect.height())) / 2.0f;
                    if (this.fpRect != null) {
                        rect.left = (int) (((float) rect.left) - wD);
                        rect.right = (int) (((float) rect.right) + wD);
                        rect.top = (int) (((float) rect.top) - hD);
                        rect.bottom = (int) (((float) rect.bottom) + hD);
                    }
                    if (this.fpRect != null) {
                        rect.left -= this.fpRect.left;
                        rect.right += this.fpRect.right;
                        rect.top -= this.fpRect.top;
                        rect.bottom += this.fpRect.bottom;
                    }
                    drawable.setBounds(rect);
                    drawable.draw(canvas);
                }
            }
        }
    }
}
