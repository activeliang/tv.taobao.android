package com.tvtaobao.android.recyclerviews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

public class TVRecyclerViewA extends RecyclerView {
    public static final int FLAG_centerDoing = 1;
    /* access modifiers changed from: private */
    public static final String TAG = TVRecyclerViewA.class.getSimpleName();
    /* access modifiers changed from: private */
    public CenterStyle centerStyle;
    private int flagX;
    private FocusPosListener focusPosListener;
    private FocusSearchRule focusSearchRule;
    private KeyEventDispatchInterceptor keyEventDispatchInterceptor;
    private VCenterHelper vCenterHelper;

    public enum CenterStyle {
        centerFocusView,
        centerItemView
    }

    public interface ExtFocusListener {
        void onFocusOnPosition(int i);

        void onFocusSearch(View view, View view2, int i);
    }

    public interface FocusPosListener {
        void onFocusAtPos(int i);
    }

    public interface FocusSearchRule {
        View onFocusSearch(View view, int i, View view2);
    }

    public interface KeyEventDispatchInterceptor {
        boolean dispatchKeyEvent(KeyEvent keyEvent);
    }

    public TVRecyclerViewA(Context context) {
        this(context, (AttributeSet) null);
    }

    public TVRecyclerViewA(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TVRecyclerViewA(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.centerStyle = CenterStyle.centerFocusView;
        this.vCenterHelper = new VCenterHelper();
        this.flagX = 0;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (this.keyEventDispatchInterceptor == null || !this.keyEventDispatchInterceptor.dispatchKeyEvent(event)) {
            int keyCode = event.getKeyCode();
            if (19 == keyCode || 20 == keyCode || 21 == keyCode || 22 == keyCode) {
                if (hasFlag(1)) {
                    TVRVLogger.d(TAG, ".dispatchKeyEvent eat by isFocusCenterDoing " + event);
                    return true;
                } else if (isScrollOrLayoutDoing()) {
                    TVRVLogger.d(TAG, ".dispatchKeyEvent eat by state not ok  " + event);
                    return true;
                }
            }
            TVRVLogger.d(TAG, ".dispatchKeyEvent " + event);
            return super.dispatchKeyEvent(event);
        }
        TVRVLogger.d(TAG, ".dispatchKeyEvent eat by keyEventDispatchInterceptor " + event);
        return true;
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        TVRVLogger.d(TAG, ".requestChildFocus \n" + child + "\n" + focused);
        if (this.focusPosListener != null) {
            this.focusPosListener.onFocusAtPos(getChildAdapterPosition(getFocusedChild()));
        }
        this.vCenterHelper.centerFocus();
    }

    public View focusSearch(View focused, int direction) {
        View rtn = callRecyclerViewFocusSearch(focused, direction);
        View rtn2 = null;
        if (this.focusSearchRule != null) {
            rtn2 = this.focusSearchRule.onFocusSearch(focused, direction, rtn);
        }
        TVRVLogger.d(TAG, ".focusSearch \nnowFocusOn:" + TVRVUtil.getString(focused) + "\nandroidResult:" + TVRVUtil.getString(rtn) + "\nfocusSearchRule:" + TVRVUtil.getString(rtn2));
        if (rtn2 == null || rtn == rtn2) {
            return rtn;
        }
        return rtn2;
    }

    public View callRecyclerViewFocusSearch(View focused, int direction) {
        return super.focusSearch(focused, direction);
    }

    public boolean hasFlag(int flag) {
        if ((this.flagX & flag) == flag) {
            return true;
        }
        return false;
    }

    public void setFlag(int flag) {
        this.flagX |= flag;
    }

    public void clrFlag(int flag) {
        this.flagX &= flag ^ -1;
    }

    public boolean isScrollOrLayoutDoing() {
        try {
            RecyclerView.LayoutManager lm = getLayoutManager();
            if (lm != null) {
                boolean isSmoothScrolling = lm.isSmoothScrolling();
                boolean isComputingLayout = isComputingLayout();
                if (isSmoothScrolling || isComputingLayout) {
                    TVRVLogger.d(TAG, ".isScrollOrLayoutDoing rtn=true");
                    return true;
                }
                TVRVLogger.d(TAG, ".isScrollOrLayoutDoing rtn=false");
                return false;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        TVRVLogger.d(TAG, ".isScrollOrLayoutDoing rtn=false");
        return false;
    }

    public void centerPos(int pos) {
        this.vCenterHelper.centerPos(pos);
    }

    public CenterStyle getCenterStyle() {
        return this.centerStyle;
    }

    public void setCenterStyle(CenterStyle centerStyle2) {
        this.centerStyle = centerStyle2;
    }

    public FocusSearchRule getFocusSearchRule() {
        return this.focusSearchRule;
    }

    public void setFocusSearchRule(FocusSearchRule focusSearchRule2) {
        this.focusSearchRule = focusSearchRule2;
    }

    public FocusPosListener getFocusPosListener() {
        return this.focusPosListener;
    }

    public void setFocusPosListener(FocusPosListener focusPosListener2) {
        this.focusPosListener = focusPosListener2;
    }

    public KeyEventDispatchInterceptor getKeyEventDispatchInterceptor() {
        return this.keyEventDispatchInterceptor;
    }

    public void setKeyEventDispatchInterceptor(KeyEventDispatchInterceptor keyEventDispatchInterceptor2) {
        this.keyEventDispatchInterceptor = keyEventDispatchInterceptor2;
    }

    private class VCenterHelper {
        int centerPos;
        Runnable centerTask;
        int lstDy;

        private VCenterHelper() {
            this.lstDy = 0;
            this.centerPos = -1;
            this.centerTask = new Runnable() {
                public void run() {
                    View leafView;
                    try {
                        Rect rect1 = new Rect();
                        TVRecyclerViewA.this.getDrawingRect(rect1);
                        Rect rect2 = new Rect();
                        if (VCenterHelper.this.centerPos != -1) {
                            RecyclerView.ViewHolder vh = TVRecyclerViewA.this.findViewHolderForAdapterPosition(VCenterHelper.this.centerPos);
                            if (!(vh == null || vh.itemView == null)) {
                                vh.itemView.getDrawingRect(rect2);
                                TVRecyclerViewA.this.offsetDescendantRectToMyCoords(vh.itemView, rect2);
                            }
                        } else if (TVRecyclerViewA.this.centerStyle == CenterStyle.centerFocusView) {
                            View leafView2 = TVRecyclerViewA.this.findFocus();
                            if (leafView2 != null) {
                                leafView2.getDrawingRect(rect2);
                                TVRecyclerViewA.this.offsetDescendantRectToMyCoords(leafView2, rect2);
                            }
                        } else if (TVRecyclerViewA.this.centerStyle == CenterStyle.centerItemView && (leafView = TVRecyclerViewA.this.getFocusedChild()) != null) {
                            leafView.getDrawingRect(rect2);
                            TVRecyclerViewA.this.offsetDescendantRectToMyCoords(leafView, rect2);
                        }
                        if (rect1.width() <= 0 || rect1.height() <= 0 || rect2.width() <= 0 || rect2.height() <= 0) {
                            TVRecyclerViewA.this.clrFlag(1);
                            VCenterHelper.this.centerPos = -1;
                            return;
                        }
                        int dy = rect2.centerY() - rect1.centerY();
                        boolean scrollFinished = false;
                        if (dy == 0) {
                            scrollFinished = true;
                        } else {
                            if (dy == VCenterHelper.this.lstDy) {
                                scrollFinished = true;
                            }
                            VCenterHelper.this.lstDy = dy;
                        }
                        if (scrollFinished) {
                            TVRecyclerViewA.this.clrFlag(1);
                            VCenterHelper.this.centerPos = -1;
                            return;
                        }
                        TVRVLogger.d(TVRecyclerViewA.TAG, "vCenterHelper.doScroll dy = " + dy);
                        TVRecyclerViewA.this.smoothScrollBy(0, dy);
                        TVRecyclerViewA.this.postInvalidate();
                        TVRecyclerViewA.this.postDelayed(this, ValueAnimator.getFrameDelay());
                    } catch (Throwable th) {
                        TVRecyclerViewA.this.clrFlag(1);
                        VCenterHelper.this.centerPos = -1;
                    }
                }
            };
        }

        /* access modifiers changed from: private */
        public void centerFocus() {
            TVRecyclerViewA.this.setFlag(1);
            TVRecyclerViewA.this.removeCallbacks(this.centerTask);
            TVRecyclerViewA.this.post(this.centerTask);
        }

        /* access modifiers changed from: private */
        public void centerPos(int pos) {
            if (pos >= 0 && TVRecyclerViewA.this.getAdapter() != null && pos < TVRecyclerViewA.this.getAdapter().getItemCount()) {
                this.centerPos = pos;
                TVRecyclerViewA.this.setFlag(1);
                TVRecyclerViewA.this.removeCallbacks(this.centerTask);
                TVRecyclerViewA.this.post(this.centerTask);
            }
        }
    }

    private class EventNotifyHelper {
        /* access modifiers changed from: private */
        public ExtFocusListener extFocusListener;

        private EventNotifyHelper() {
        }

        private void doPositionNotify(final View view) {
            TVRecyclerViewA.this.post(new Runnable() {
                public void run() {
                    int pos;
                    try {
                        RecyclerView.ViewHolder vh = TVRecyclerViewA.this.findContainingViewHolder(view);
                        if (vh != null && (pos = vh.getAdapterPosition()) != -1 && EventNotifyHelper.this.extFocusListener != null) {
                            EventNotifyHelper.this.extFocusListener.onFocusOnPosition(pos);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        private void notifyFocusSearch(View rtn, View focused, int direction) {
            if (this.extFocusListener != null) {
                try {
                    this.extFocusListener.onFocusSearch(rtn, focused, direction);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
