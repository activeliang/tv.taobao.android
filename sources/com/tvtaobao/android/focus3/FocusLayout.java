package com.tvtaobao.android.focus3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import java.util.ArrayList;

class FocusLayout extends FrameLayout {
    private static int FLAG_LOCK_FOCUSROUTINE = 1;
    private static final String TAG = FocusLayout.class.getSimpleName();
    public static final String VIEW_TREE_TAG = (TAG + "_view_tree_tag");
    private ClickHappenListener clickHappenListener;
    private DispatchKeyEventInterrupter dispatchKeyEventInterrupter;
    private DrawCallBack drawCallBack;
    private int flag;
    private FocusSearchCallBack focusSearchCallBack;
    private Rect myGlobalVisibleRect;
    private View oldFocus;
    private PreKeyEventDispatch preKeyEventDispatch;
    private RequestFocusCallBack requestFocusCallBack;
    private ViewLifeCircleListener viewLifeCircleListener;

    public interface ClickHappenListener {
        void onClickHappen();
    }

    public interface DispatchKeyEventInterrupter {
        boolean interruptDispatch(KeyEvent keyEvent);
    }

    public interface DrawCallBack {
        void afterDraw(Canvas canvas);

        void beforeDraw(Canvas canvas);
    }

    public interface FocusSearchCallBack {
        View focusSearch(View view, int i, View view2);
    }

    public interface PreKeyEventDispatch {
        void onPreKeyEventDispatch(KeyEvent keyEvent);
    }

    public enum RefreshReason {
        byKeyEvent,
        byAnimateUpdate
    }

    public interface RequestFocusCallBack {
        boolean requestFocus(int i, Rect rect);
    }

    public interface ViewLifeCircleListener {
        void onAttach();

        void onDetach();
    }

    public static FocusLayout inject(Window window) {
        View xmlLayout;
        if (window == null) {
            return null;
        }
        try {
            if (window.getDecorView() == null) {
                return null;
            }
            View contentView = window.getDecorView().findViewById(16908290);
            if (!(contentView instanceof ViewGroup) || ((ViewGroup) contentView).getChildCount() <= 0 || (xmlLayout = ((ViewGroup) contentView).getChildAt(0)) == null || (xmlLayout.getParent() instanceof FocusLayout)) {
                return null;
            }
            ViewGroup.LayoutParams lp = xmlLayout.getLayoutParams();
            FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(-1, -1);
            FocusLayout rtn = new FocusLayout(xmlLayout.getContext());
            try {
                if (contentView instanceof ViewGroup) {
                    ((ViewGroup) contentView).removeView(xmlLayout);
                    rtn.addView(xmlLayout, lp2);
                    ((ViewGroup) contentView).addView(rtn, 0, lp);
                }
                return rtn;
            } catch (Throwable th) {
                e = th;
                FocusLayout focusLayout = rtn;
                e.printStackTrace();
                return null;
            }
        } catch (Throwable th2) {
            e = th2;
            e.printStackTrace();
            return null;
        }
    }

    public FocusLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public FocusLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.oldFocus = null;
        this.flag = 0;
        init();
    }

    private void init() {
        setTag(VIEW_TREE_TAG);
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        Focus3Logger.i(TAG, ".dispatchDraw");
        if (this.drawCallBack != null) {
            this.drawCallBack.beforeDraw(canvas);
        }
        super.dispatchDraw(canvas);
        if (this.drawCallBack != null) {
            this.drawCallBack.afterDraw(canvas);
        }
        Focus3Logger.i(TAG, ".dispatchDraw done flag = " + Integer.toHexString(this.flag));
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean rtn;
        Focus3Logger.i(TAG, ".interruptDispatch " + event);
        if (this.dispatchKeyEventInterrupter == null || !this.dispatchKeyEventInterrupter.interruptDispatch(event)) {
            if (this.preKeyEventDispatch != null) {
                try {
                    this.preKeyEventDispatch.onPreKeyEventDispatch(event);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            rtn = callSupperDispatchKeyEvent(event);
            if (rtn && Focus3Util.isConfirmKey(event.getKeyCode()) && event.getAction() == 1 && this.clickHappenListener != null) {
                try {
                    this.clickHappenListener.onClickHappen();
                } catch (Throwable e2) {
                    e2.printStackTrace();
                }
            }
        } else {
            Focus3Logger.i(TAG, ".interruptDispatch eat by dispatchKeyEventInterrupter !");
            rtn = true;
        }
        refresh(RefreshReason.byKeyEvent);
        Focus3Logger.i(TAG, ".interruptDispatch done :" + rtn);
        return rtn;
    }

    public boolean callSupperDispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        Focus3Logger.i(TAG, ".onAttachedToWindow ");
        super.onAttachedToWindow();
        if (this.viewLifeCircleListener != null) {
            this.viewLifeCircleListener.onAttach();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        Focus3Logger.i(TAG, ".onDetachedFromWindow ");
        super.onDetachedFromWindow();
        if (this.viewLifeCircleListener != null) {
            this.viewLifeCircleListener.onDetach();
        }
    }

    public void invalidate() {
        Focus3Logger.i(TAG, ".invalidate called ");
        super.invalidate();
    }

    public View focusSearch(View focused, int direction) {
        View rtn;
        Focus3Logger.i(TAG, ".focusSearch 4 " + Focus3Util.getString(focused));
        if (!isFocusRoutineLocked()) {
            View rtn1 = super.focusSearch(focused, direction);
            Focus3Logger.i(TAG, ".focusSearch androidResult: " + Focus3Util.getString(rtn1));
            rtn = rtn1;
            if (!(this.focusSearchCallBack == null || (rtn = this.focusSearchCallBack.focusSearch(focused, direction, rtn1)) == null || Focus3Util.isBinA(this, rtn))) {
                rtn = rtn1;
                Focus3Logger.i(TAG, ".focusSearch rule ignored cause view has removed");
            }
        } else {
            rtn = this;
        }
        Focus3Logger.i(TAG, ".focusSearch done:" + Focus3Util.getString(rtn));
        return rtn;
    }

    public void focusableViewAvailable(View v) {
        Focus3Logger.i(TAG, ".focusableViewAvailable " + Focus3Util.getString(v));
        super.focusableViewAvailable(v);
        Focus3Logger.i(TAG, ".focusableViewAvailable done ");
    }

    public boolean dispatchUnhandledMove(View focused, int direction) {
        Focus3Logger.i(TAG, ".dispatchUnhandledMove " + Focus3Util.getString(focused) + " " + direction);
        boolean rtn = super.dispatchUnhandledMove(focused, direction);
        Focus3Logger.i(TAG, ".dispatchUnhandledMove done " + rtn);
        return rtn;
    }

    public void clearChildFocus(View child) {
        Focus3Logger.i(TAG, ".clearChildFocus " + Focus3Util.getString(child));
        super.clearChildFocus(child);
        Focus3Logger.i(TAG, ".clearChildFocus done ");
    }

    public void requestChildFocus(View child, View focused) {
        Focus3Logger.i(TAG, ".requestChildFocus " + Focus3Util.getString(child) + " " + Focus3Util.getString(focused));
        super.requestChildFocus(child, focused);
        Focus3Logger.i(TAG, ".requestChildFocus done ");
    }

    public void dispatchWindowFocusChanged(boolean hasFocus) {
        Focus3Logger.i(TAG, ".dispatchWindowFocusChanged " + hasFocus);
        super.dispatchWindowFocusChanged(hasFocus);
        Focus3Logger.i(TAG, ".dispatchWindowFocusChanged done");
    }

    /* access modifiers changed from: protected */
    public boolean dispatchGenericFocusedEvent(MotionEvent event) {
        Focus3Logger.i(TAG, ".dispatchGenericFocusedEvent ");
        boolean rtn = super.dispatchGenericFocusedEvent(event);
        Focus3Logger.i(TAG, ".dispatchGenericFocusedEvent done " + rtn);
        return rtn;
    }

    /* access modifiers changed from: protected */
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        Focus3Logger.i(TAG, ".onRequestFocusInDescendants ");
        boolean rtn = super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
        Focus3Logger.i(TAG, ".onRequestFocusInDescendants done " + rtn);
        return rtn;
    }

    public boolean restoreDefaultFocus() {
        Focus3Logger.i(TAG, ".restoreDefaultFocus ");
        boolean rtn = super.restoreDefaultFocus();
        Focus3Logger.i(TAG, ".restoreDefaultFocus done " + rtn);
        return rtn;
    }

    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        Focus3Logger.i(TAG, ".requestFocus " + direction + " " + Focus3Util.getString(previouslyFocusedRect));
        boolean rtn = false;
        if (this.requestFocusCallBack != null) {
            try {
                rtn = this.requestFocusCallBack.requestFocus(direction, previouslyFocusedRect);
                Focus3Logger.i(TAG, ".requestFocus requestFocusCallBack handle rlt=" + rtn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!rtn) {
            rtn = super.requestFocus(direction, previouslyFocusedRect);
        }
        Focus3Logger.i(TAG, ".requestFocus done " + rtn);
        return rtn;
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        Integer num = null;
        Focus3Logger.i(TAG, ".addFocusables enter direction=" + direction + " views.size=" + (views != null ? Integer.valueOf(views.size()) : null));
        if (isFocusRoutineLocked()) {
            Focus3Logger.i(TAG, ".addFocusables ignored cause isFocusRoutineLocked=true");
        } else {
            super.addFocusables(views, direction, focusableMode);
        }
        String str = TAG;
        StringBuilder append = new StringBuilder().append(".addFocusables leave direction=").append(direction).append(" views.size=");
        if (views != null) {
            num = Integer.valueOf(views.size());
        }
        Focus3Logger.i(str, append.append(num).toString());
    }

    public void lockFocusRoutine() {
        this.flag |= FLAG_LOCK_FOCUSROUTINE;
    }

    public void unlockFocusRoutine() {
        this.flag &= FLAG_LOCK_FOCUSROUTINE ^ -1;
    }

    public boolean isFocusRoutineLocked() {
        if ((this.flag & FLAG_LOCK_FOCUSROUTINE) == FLAG_LOCK_FOCUSROUTINE) {
            return true;
        }
        return false;
    }

    public void release() {
        this.drawCallBack = null;
        this.preKeyEventDispatch = null;
        this.viewLifeCircleListener = null;
    }

    public Rect getMyGlobalVisibleRect(boolean forceUpdate) {
        try {
            if (this.myGlobalVisibleRect == null) {
                this.myGlobalVisibleRect = new Rect();
                getGlobalVisibleRect(this.myGlobalVisibleRect);
            }
            if (forceUpdate) {
                getGlobalVisibleRect(this.myGlobalVisibleRect);
            }
            return this.myGlobalVisibleRect;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean offsetGlobalVisibleRectToMyCoords(Rect globalVisibleRect) {
        try {
            Rect mRect = getMyGlobalVisibleRect(false);
            if (globalVisibleRect == null) {
                return false;
            }
            globalVisibleRect.offset(mRect.left * -1, mRect.top * -1);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public PreKeyEventDispatch getPreKeyEventDispatch() {
        return this.preKeyEventDispatch;
    }

    public void setPreKeyEventDispatch(PreKeyEventDispatch preKeyEventDispatch2) {
        this.preKeyEventDispatch = preKeyEventDispatch2;
    }

    public DrawCallBack getDrawCallBack() {
        return this.drawCallBack;
    }

    public void setDrawCallBack(DrawCallBack drawCallBack2) {
        this.drawCallBack = drawCallBack2;
    }

    public void setViewLifeCircleListener(ViewLifeCircleListener viewLifeCircleListener2) {
        this.viewLifeCircleListener = viewLifeCircleListener2;
    }

    public ClickHappenListener getClickHappenListener() {
        return this.clickHappenListener;
    }

    public void setClickHappenListener(ClickHappenListener clickHappenListener2) {
        this.clickHappenListener = clickHappenListener2;
    }

    public FocusSearchCallBack getFocusSearchCallBack() {
        return this.focusSearchCallBack;
    }

    public void setFocusSearchCallBack(FocusSearchCallBack focusSearchCallBack2) {
        this.focusSearchCallBack = focusSearchCallBack2;
    }

    public RequestFocusCallBack getRequestFocusCallBack() {
        return this.requestFocusCallBack;
    }

    public void setRequestFocusCallBack(RequestFocusCallBack requestFocusCallBack2) {
        this.requestFocusCallBack = requestFocusCallBack2;
    }

    public DispatchKeyEventInterrupter getDispatchKeyEventInterrupter() {
        return this.dispatchKeyEventInterrupter;
    }

    public void setDispatchKeyEventInterrupter(DispatchKeyEventInterrupter dispatchKeyEventInterrupter2) {
        this.dispatchKeyEventInterrupter = dispatchKeyEventInterrupter2;
    }

    public void refresh(RefreshReason refreshReason) {
        View focusView;
        if (refreshReason != null) {
            Focus3Logger.i(TAG, ".refresh by " + refreshReason);
            try {
                if (refreshReason == RefreshReason.byAnimateUpdate) {
                    postInvalidate();
                } else if (refreshReason == RefreshReason.byKeyEvent && (focusView = findFocus()) != this.oldFocus) {
                    this.oldFocus = focusView;
                    Focus3Util.callInvalidateFromChildToRoot((View) null, focusView);
                    Focus3Logger.i(TAG, ".refresh by " + Focus3Util.getString(focusView));
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            Focus3Logger.i(TAG, ".refresh done flag=" + Integer.toHexString(this.flag));
        }
    }
}
