package com.yunos.tvtaobao.tvsdk.widget.focus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.DrawListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.FocusListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.PositionListener;
import com.zhiping.dev.android.logger.ZpLogger;

public class FocusPositionManager extends FrameLayout implements PositionListener {
    protected static boolean DEBUG = false;
    protected static String TAG = "FocusPositionManager";
    int f = 0;
    private View mFirstRequestChild;
    FocusRequestRunnable mFocusRequestRunnable = new FocusRequestRunnable();
    View mFocused;
    boolean mLayouted = false;
    PositionManager mPositionManager;
    long time = 0;

    public FocusPositionManager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FocusPositionManager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FocusPositionManager(Context context) {
        super(context);
        init();
    }

    public boolean isInEditMode() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mPositionManager != null) {
            this.mPositionManager.release();
        }
    }

    public View getFocused() {
        return this.mFocused;
    }

    private void init() {
        this.mPositionManager = PositionManager.createPositionManager(2, this);
    }

    public void setFocusMode(int focusMode) {
        if (this.mPositionManager != null) {
            this.mPositionManager.release();
            this.mPositionManager = null;
        }
        this.mPositionManager = PositionManager.createPositionManager(focusMode, this);
    }

    public void setSelector(DrawListener selector) {
        this.mPositionManager.setSelector(selector);
    }

    public void setConvertSelector(DrawListener convertSelector) {
        this.mPositionManager.setConvertSelector(convertSelector);
    }

    public void requestFocus(View v, int direction) {
        if (v == null) {
            throw new NullPointerException();
        } else if (!(v instanceof FocusListener)) {
            throw new IllegalArgumentException("The view you request focus must extend from FocusListener");
        } else {
            ZpLogger.d(TAG, TAG + ".requestFocus v = " + v + ", direction = " + direction);
            View rootFocus = findRootFocus(v);
            Rect previouslyFocusedRect = getFocusedRect(this.mFocused, rootFocus);
            ZpLogger.d(TAG, TAG + ".requestFocus rootFocus = " + rootFocus + ", mFocused = " + this.mFocused + ", previouslyFocusedRect = " + previouslyFocusedRect);
            if (!rootFocus.hasFocus()) {
                rootFocus.requestFocus(direction, previouslyFocusedRect);
            }
            ZpLogger.i(TAG, TAG + ".requestFocus.mPositionManager.stop()");
            this.mPositionManager.stop();
            if (this.mFocused != rootFocus) {
                this.mFocused = rootFocus;
                this.mPositionManager.reset((FocusListener) this.mFocused);
                return;
            }
            this.mPositionManager.reset();
        }
    }

    private View findRootFocus(View child) {
        View rootFocus = child;
        ViewParent temp = child.getParent();
        while (temp != this) {
            try {
                if (temp instanceof FocusListener) {
                    rootFocus = (View) temp;
                }
                temp = temp.getParent();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rootFocus;
    }

    public void forceDrawFocus() {
        if (this.mPositionManager != null) {
            this.mPositionManager.forceDrawFocus();
        }
    }

    public void focusShow() {
        ZpLogger.d(TAG, "focusShow");
        focusStart();
        invalidate();
    }

    public void focusHide() {
        ZpLogger.d(TAG, "focusHide");
        focusStop();
        invalidate();
    }

    public void focusPause() {
        ZpLogger.d(TAG, "focusPause");
        this.mPositionManager.focusPause();
    }

    public void focusStop() {
        ZpLogger.d(TAG, "focusStop");
        this.mPositionManager.focusStop();
    }

    public void focusStart() {
        ZpLogger.d(TAG, "focusStart");
        this.mPositionManager.focusStart();
    }

    public boolean IsFocusStarted() {
        return this.mPositionManager.isFocusStarted();
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        if (!this.mLayouted) {
            postInvalidateDelayed(30);
            super.dispatchDraw(canvas);
        } else if (this.mFocused == null) {
            super.dispatchDraw(canvas);
        } else {
            drawBackFocus(canvas);
            super.dispatchDraw(canvas);
            drawForeFocus(canvas);
            if (DEBUG) {
                if (System.currentTimeMillis() - this.time >= 1000) {
                    ZpLogger.d(TAG, "dispatchDraw f = " + this.f);
                    this.time = System.currentTimeMillis();
                    this.f = 0;
                }
                this.f++;
            }
        }
    }

    private void drawBackFocus(Canvas canvas) {
        if (this.mPositionManager.isFocusBackground()) {
            drawFocus(canvas);
        }
    }

    private void drawForeFocus(Canvas canvas) {
        if (!this.mPositionManager.isFocusBackground()) {
            drawFocus(canvas);
        }
    }

    private void drawFocus(Canvas canvas) {
        if (DEBUG) {
            ZpLogger.i(TAG, TAG + ".drawFocus.mFocused = " + this.mFocused + ".mLayouted = " + this.mLayouted);
        }
        if (this.mFocused != null && this.mLayouted) {
            this.mPositionManager.draw(canvas);
        } else if (!this.mLayouted) {
            postInvalidateDelayed(30);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (checkValidKey(event.getKeyCode()) && ((this.mFocused == null || !this.mFocused.hasFocus()) && this.mLayouted)) {
            ZpLogger.w(TAG, "dispatchKeyEvent mFocused is null, may be no focusbale view in your layout, or mouse switch to key");
            deliverFocus();
            invalidate();
            return true;
        } else if (!event.dispatch(this, (KeyEvent.DispatcherState) null, this)) {
            return false;
        } else {
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (DEBUG) {
            ZpLogger.d(TAG, "onFocusChanged: child count = " + getChildCount() + ", gainFocus = " + gainFocus);
        }
        if (gainFocus && this.mLayouted) {
            deliverFocus();
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (DEBUG) {
            ZpLogger.i(TAG, TAG + ".onLayout");
        }
        if (hasFocus() && !this.mLayouted && this.mFocused == null) {
            deliverFocus();
        }
        this.mLayouted = true;
    }

    private void deliverFocus() {
        if (this.mFocused == null) {
            this.mFocused = findFocusChild(this);
        }
        ZpLogger.d(TAG, "deliverFocus mFocused = " + this.mFocused);
        try {
            if (this.mFocused != null) {
                this.mFocused.requestFocus();
                this.mPositionManager.reset((FocusListener) this.mFocused);
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public void resetFocused() {
        if (this.mFocused == null) {
            this.mFocused = findFocusChild(this);
        }
        ZpLogger.d(TAG, "resetFocused mFocused = " + this.mFocused);
        if (this.mFocused != null) {
            if (!this.mFocused.hasFocus()) {
                this.mFocused.requestFocus();
            }
            this.mPositionManager.reset((FocusListener) this.mFocused);
        }
    }

    public void requestLayout() {
        super.requestLayout();
        this.mLayouted = false;
    }

    public void setFirstFocusChild(View firstRequestChild) {
        this.mFirstRequestChild = firstRequestChild;
    }

    private View findFocusChild(ViewGroup v) {
        View child;
        View result = null;
        for (int index = 0; index < v.getChildCount(); index++) {
            View child2 = v.getChildAt(index);
            if (child2 instanceof FocusListener) {
                if (child2.isFocusable() && child2.getVisibility() == 0) {
                    if (child2 == this.mFirstRequestChild) {
                        return child2;
                    }
                    if (result == null) {
                        result = child2;
                    }
                }
            } else if ((child2 instanceof ViewGroup) && (child = findFocusChild((ViewGroup) child2)) != null) {
                return child;
            }
        }
        return result;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.mFocused == null || !this.mFocused.onKeyUp(keyCode, event)) {
            return super.onKeyUp(keyCode, event);
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (DEBUG) {
            ZpLogger.d(TAG, "onKeyDown: keyCode = " + keyCode + ".mFocused = " + this.mFocused);
        }
        if (isNeedCheckValidKey(keyCode) && !checkValidKey(keyCode)) {
            return super.onKeyDown(keyCode, event);
        }
        if (!this.mPositionManager.canDrawNext()) {
            return true;
        }
        if (this.mPositionManager.preOnKeyDown(keyCode, event)) {
            boolean hr = false;
            if (!(keyCode == 23 || keyCode == 66 || keyCode == 160)) {
                ZpLogger.i(TAG, TAG + ".onKeyDown.mPositionManager.stop()");
                this.mPositionManager.stop();
                hr = true;
            }
            if (this.mPositionManager.onKeyDown(keyCode, event)) {
                if (hr) {
                    this.mPositionManager.reset();
                }
                return true;
            }
        }
        if (this.mFocused == null) {
            return true;
        }
        View focused = null;
        int direction = 0;
        switch (keyCode) {
            case 4:
            case 111:
                return super.onKeyDown(keyCode, event);
            case 19:
                direction = 33;
                focused = focusSearch(this.mFocused, 33);
                break;
            case 20:
                direction = 130;
                focused = focusSearch(this.mFocused, 130);
                break;
            case 21:
                direction = 17;
                focused = focusSearch(this.mFocused, 17);
                break;
            case 22:
                direction = 66;
                focused = focusSearch(this.mFocused, 66);
                break;
        }
        ZpLogger.i(TAG, "onKeyDown the new focused = " + focused + ", previous focused = " + this.mFocused);
        if (focused == null || focused == this) {
            ZpLogger.w(TAG, "onKeyDown can not find the new focus");
            return true;
        } else if (!checkFocus(focused, this.mFocused, direction)) {
            ZpLogger.i(TAG, "onKeyDown: checkFocus failed  new focus = " + focused + ", old focus = " + this.mFocused);
            this.mFocused = focused;
            return super.onKeyDown(keyCode, event);
        } else {
            if (focused instanceof FocusListener) {
                View lastFocused = this.mFocused;
                this.mFocused = focused;
                this.mPositionManager.stop();
                this.mFocused.requestFocus(direction, getFocusedRect(lastFocused, this.mFocused));
                this.mPositionManager.reset((FocusListener) this.mFocused);
            } else {
                ZpLogger.w(TAG, "onKeyDown the new focused is not instance of FocusListener");
            }
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean checkFocus(View newFocus, View oldFocus, int direction) {
        if (!isChild(this, newFocus)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean isChild(ViewGroup p, View newFocus) {
        for (int index = 0; index < p.getChildCount(); index++) {
            View v = p.getChildAt(index);
            if (v == newFocus) {
                return true;
            }
            if ((v instanceof ViewGroup) && isChild((ViewGroup) v, newFocus)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public Rect getFocusedRect(View from, View to) {
        if (from == null || to == null) {
            return null;
        }
        Rect rFrom = new Rect();
        from.getFocusedRect(rFrom);
        Rect rTo = new Rect();
        to.getFocusedRect(rTo);
        try {
            offsetDescendantRectToMyCoords(from, rFrom);
            offsetDescendantRectToMyCoords(to, rTo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int xDiff = rFrom.left - rTo.left;
        int yDiff = rFrom.top - rTo.top;
        int rWidth = rFrom.width();
        int rheight = rFrom.height();
        rFrom.left = xDiff;
        rFrom.right = rFrom.left + rWidth;
        rFrom.top = yDiff;
        rFrom.bottom = rFrom.top + rheight;
        return rFrom;
    }

    public void offsetDescendantRectToItsCoords(View descendant, Rect rect) {
        offsetDescendantRectToMyCoords(descendant, rect);
    }

    public void reset() {
        ZpLogger.i(TAG, TAG + ".reset.mFocused = " + this.mFocused);
        boolean isInvalidate = true;
        if (this.mFocused != null) {
            this.mPositionManager.stop();
            this.mPositionManager.reset();
        } else {
            this.mFocused = findFocusChild(this);
            if (this.mFocused != null) {
                this.mFocused.requestFocus();
                this.mPositionManager.reset((FocusListener) this.mFocused);
            } else {
                isInvalidate = false;
            }
        }
        if (isInvalidate) {
            invalidate();
        }
    }

    private class FocusRequestRunnable implements Runnable {
        View mView;

        public FocusRequestRunnable() {
        }

        public void start(View v) {
            this.mView = v;
            if (this.mView != null) {
                FocusPositionManager.this.removeCallbacks(this);
                FocusPositionManager.this.post(this);
            }
        }

        public void run() {
            this.mView.requestFocus();
            FocusPositionManager.this.invalidate();
        }
    }

    private boolean checkValidKey(int keyCode) {
        switch (keyCode) {
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 66:
            case 67:
                return true;
            default:
                return false;
        }
    }

    public PositionManager getPositionManager() {
        return this.mPositionManager;
    }

    public int getFocusFrameRate() {
        return this.mPositionManager.getFocusFrameRate();
    }

    public int getCurFocusFrame() {
        return this.mPositionManager.getCurFocusFrame();
    }

    /* access modifiers changed from: protected */
    public View getFocusedView() {
        return this.mFocused;
    }

    /* access modifiers changed from: protected */
    public boolean isNeedCheckValidKey(int keyCode) {
        return true;
    }

    public DrawListener getSelector() {
        return this.mPositionManager.getSelector();
    }

    public DrawListener setConvertSelector() {
        return this.mPositionManager.getConvertSelector();
    }
}
