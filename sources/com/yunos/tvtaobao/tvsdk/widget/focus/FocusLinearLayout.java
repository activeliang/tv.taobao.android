package com.yunos.tvtaobao.tvsdk.widget.focus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import com.yunos.tvtaobao.tvsdk.widget.FocusFinder;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.AnimateWhenGainFocusListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.DeepListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.FocusStateListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemSelectedListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.Params;
import com.yunos.tvtaobao.tvsdk.widget.interpolator.AccelerateDecelerateFrameInterpolator;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FocusLinearLayout extends LinearLayout implements DeepListener, ItemListener {
    protected static final boolean DEBUG = false;
    protected static final String TAG = "FocusLinearLayout";
    boolean mAimateWhenGainFocusFromDown = true;
    boolean mAimateWhenGainFocusFromLeft = true;
    boolean mAimateWhenGainFocusFromRight = true;
    boolean mAimateWhenGainFocusFromUp = true;
    private boolean mAutoSearchFocus = true;
    boolean mClearDataDetachedFromWindow = true;
    DeepListener mDeep = null;
    boolean mDeepFocus = false;
    ViewGroup mFindRootView;
    View mFirstSelectedView;
    boolean mFocusBackground = false;
    private FocusFinder mFocusFinder;
    protected FocusRectParams mFocusRectparams = new FocusRectParams();
    FocusStateListener mFocusStateListener = null;
    protected int mIndex = -1;
    boolean mIsAnimate = true;
    DeepListener mLastDeep = null;
    private View mLastSelectedView = null;
    boolean mLayouted = false;
    boolean mNeedFocused = true;
    protected boolean mNeedInit = true;
    boolean mNeedInitNode = true;
    boolean mNeedReset = false;
    int mNextDirection;
    View mNextFocus = null;
    protected Map<View, NodeInfo> mNodeMap = new HashMap();
    private boolean mOnFocused = false;
    OnItemClickListener mOnItemClickListener;
    private ItemSelectedListener mOnItemSelectedListener = null;
    protected Params mParams = new Params(1.05f, 1.05f, 10, (Interpolator) null, true, 20, new AccelerateDecelerateFrameInterpolator());
    private boolean mUpdateIndexBySelectView;

    public interface OnItemClickListener {
        void onItemClick(ViewGroup viewGroup, View view);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public FocusLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        boolean z = true;
        this.mUpdateIndexBySelectView = this.mIndex >= 0 ? false : z;
        this.mFirstSelectedView = null;
        initLayout(context);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public FocusLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        boolean z = true;
        this.mUpdateIndexBySelectView = this.mIndex >= 0 ? false : z;
        this.mFirstSelectedView = null;
        initLayout(context);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public FocusLinearLayout(Context context) {
        super(context);
        boolean z = true;
        this.mUpdateIndexBySelectView = this.mIndex >= 0 ? false : z;
        this.mFirstSelectedView = null;
        initLayout(context);
    }

    public void setAnimateWhenGainFocus(boolean fromleft, boolean fromUp, boolean fromRight, boolean fromDown) {
        this.mAimateWhenGainFocusFromLeft = fromleft;
        this.mAimateWhenGainFocusFromUp = fromUp;
        this.mAimateWhenGainFocusFromRight = fromRight;
        this.mAimateWhenGainFocusFromDown = fromDown;
    }

    public void setClearDataDetachedFromWindowEnable(boolean enable) {
        this.mClearDataDetachedFromWindow = enable;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mOnFocused = false;
        if (this.mClearDataDetachedFromWindow) {
            this.mLayouted = false;
            this.mNeedInitNode = true;
            if (this.mNodeMap != null) {
                this.mNodeMap.clear();
            }
            if (this.mFocusFinder != null) {
                this.mFocusFinder.clearFocusables();
            }
        }
    }

    public void setFocusBackground(boolean back) {
        this.mFocusBackground = back;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    public void setOnItemSelectedListener(ItemSelectedListener listener) {
        this.mOnItemSelectedListener = listener;
    }

    private void performItemClick() {
        if (this.mDeep != null) {
            this.mDeep.onItemClick();
        } else if (this.mOnItemClickListener != null) {
            this.mOnItemClickListener.onItemClick(this, getSelectedView());
        }
    }

    private void performItemSelect(View v, boolean isSelected, boolean isLocal) {
        if (!isLocal && this.mDeep != null) {
            this.mDeep.onItemSelected(isSelected);
        } else if (v != null) {
            v.setSelected(isSelected);
            if (this.mOnItemSelectedListener != null) {
                this.mOnItemSelectedListener.onItemSelected(v, this.mIndex, isSelected, this);
            }
        }
    }

    public void setAutoSearchFocus(boolean autoSearchFocus) {
        this.mAutoSearchFocus = autoSearchFocus;
    }

    public void setOnFocusStateListener(FocusStateListener l) {
        this.mFocusStateListener = l;
    }

    private void initLayout(Context conext) {
        this.mFocusFinder = new FocusFinder();
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        forceInitNode();
        setNeedInitNode(true);
    }

    public boolean isNeedFocusItem() {
        return this.mNeedFocused;
    }

    public void release() {
        this.mNodeMap.clear();
    }

    public void clearFocusedIndex() {
        this.mIndex = -1;
    }

    public void setFocusedIndex(int index) {
        this.mIndex = index;
    }

    public int getFocusedIndex() {
        return this.mIndex;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.mDeep != null && this.mDeep.canDeep() && this.mDeep.hasDeepFocus()) {
            return this.mDeep.onKeyUp(keyCode, event);
        }
        if ((23 != keyCode && 66 != keyCode && keyCode != 160) || getSelectedView() == null) {
            return super.onKeyUp(keyCode, event);
        }
        if (isPressed()) {
            setPressed(false);
            performItemClick();
            if (getSelectedView() != null) {
                getSelectedView().performClick();
            }
        }
        return true;
    }

    public void setSelected(boolean selected) {
        if (!selected) {
            this.mNeedFocused = true;
        }
        super.setSelected(selected);
    }

    private void performSelected(View selectedView, boolean isSelect, boolean isLocal) {
        if (selectedView != null) {
            selectedView.setSelected(isSelect);
            performItemSelect(selectedView, isSelect, isLocal);
            View.OnFocusChangeListener listener = selectedView.getOnFocusChangeListener();
            if (listener != null) {
                listener.onFocusChange(selectedView, isSelect);
            }
        }
    }

    private boolean performDeepAction(DeepListener deep, boolean focus, int direction, Rect previouslyFocusedRect) {
        if (deep == null || !deep.hasDeepFocus()) {
            return false;
        }
        deep.onFocusDeeped(focus, direction, previouslyFocusedRect);
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ZpLogger.d(TAG, "onKeyDown keyCode = " + keyCode + ", mDeep = " + this.mDeep);
        if (this.mDeep != null && this.mDeep.canDeep() && this.mDeep.hasDeepFocus() && this.mDeep.onKeyDown(keyCode, event)) {
            reset();
            return true;
        } else if (keyCode == 23 || keyCode == 66 || keyCode == 160) {
            setPressed(true);
            return true;
        } else {
            int direction = 130;
            switch (keyCode) {
                case 19:
                    direction = 33;
                    break;
                case 20:
                    direction = 130;
                    break;
                case 21:
                    direction = 17;
                    break;
                case 22:
                    direction = 66;
                    break;
            }
            if (this.mNextFocus != null) {
                if (this.mDeep != null) {
                    this.mLastDeep = this.mDeep;
                    this.mDeep = null;
                }
                if (this.mNextFocus instanceof DeepListener) {
                    this.mDeep = (DeepListener) this.mNextFocus;
                    if (!this.mDeep.canDeep()) {
                        this.mDeep = null;
                    }
                }
            }
            if (this.mNextFocus == null || !this.mNodeMap.containsKey(this.mNextFocus) || !this.mNextFocus.isFocusable()) {
                return super.onKeyDown(keyCode, event);
            }
            this.mIsAnimate = true;
            if (this.mDeep == null || !this.mDeep.canDeep()) {
                performDeepAction(this.mLastDeep, false, direction, (Rect) null);
                this.mLastSelectedView = getSelectedView();
                performSelected(this.mLastSelectedView, false, false);
                NodeInfo info = this.mNodeMap.get(this.mNextFocus);
                this.mIndex = info.index;
                switch (keyCode) {
                    case 19:
                        info.fromDown = this.mLastSelectedView;
                        if (this.mNextFocus instanceof AnimateWhenGainFocusListener) {
                            this.mIsAnimate = ((AnimateWhenGainFocusListener) this.mNextFocus).fromBottom();
                            break;
                        }
                        break;
                    case 20:
                        info.fromUp = this.mLastSelectedView;
                        if (this.mNextFocus instanceof AnimateWhenGainFocusListener) {
                            this.mIsAnimate = ((AnimateWhenGainFocusListener) this.mNextFocus).fromTop();
                            break;
                        }
                        break;
                    case 21:
                        info.fromRight = this.mLastSelectedView;
                        if (this.mNextFocus instanceof AnimateWhenGainFocusListener) {
                            this.mIsAnimate = ((AnimateWhenGainFocusListener) this.mNextFocus).fromRight();
                            break;
                        }
                        break;
                    case 22:
                        info.fromLeft = this.mLastSelectedView;
                        if (this.mNextFocus instanceof AnimateWhenGainFocusListener) {
                            this.mIsAnimate = ((AnimateWhenGainFocusListener) this.mNextFocus).fromLeft();
                            break;
                        }
                        break;
                }
                this.mLastDeep = null;
                performSelected(getSelectedView(), true, false);
                reset();
                playSoundEffect(SoundEffectConstants.getContantForFocusDirection(this.mNextDirection));
                return true;
            }
            if (!this.mDeep.hasDeepFocus()) {
                Rect previouslyFocusedRect = getFocusedRect(getSelectedView(), this.mNextFocus);
                if (performDeepAction(this.mLastDeep, false, direction, (Rect) null)) {
                    this.mLastDeep = null;
                }
                this.mDeep.onFocusDeeped(true, this.mNextDirection, previouslyFocusedRect);
                this.mLastSelectedView = getSelectedView();
                performSelected(this.mLastSelectedView, false, true);
                this.mIndex = this.mNodeMap.get(this.mNextFocus).index;
                performSelected(getSelectedView(), true, true);
                reset();
            }
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void reset() {
        if (!hasFocusChild()) {
            Rect r = new Rect();
            getFocusedRect(r);
            this.mFocusRectparams.set(r, 0.5f, 0.5f);
        } else if (getSelectedView() != null) {
            if (this.mDeep != null) {
                this.mFocusRectparams.set(this.mDeep.getFocusParams());
            } else {
                if (this.mIndex == -1 && getChildCount() > 0) {
                    this.mIndex = 0;
                }
                ItemListener item = (ItemListener) getSelectedView();
                if (item != null) {
                    this.mFocusRectparams.set(item.getFocusParams());
                }
            }
            int left = getSelectedView().getLeft();
            int scrollX = getScrollX();
            offsetDescendantRectToMyCoords(getSelectedView(), this.mFocusRectparams.focusRect());
        }
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect, ViewGroup findRoot) {
        this.mFindRootView = findRoot;
        onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        this.mFindRootView = null;
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        ZpLogger.d(TAG, "onFocusChanged");
        this.mOnFocused = gainFocus;
        if (getOnFocusChangeListener() != null) {
            getOnFocusChangeListener().onFocusChange(this, gainFocus);
        }
        doFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    /* access modifiers changed from: protected */
    public void doFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (gainFocus) {
            if (!hasFocusChild()) {
                this.mNeedReset = true;
                return;
            }
            this.mNeedFocused = false;
            if (this.mAutoSearchFocus && previouslyFocusedRect != null) {
                if (this.mFindRootView == null) {
                    this.mFindRootView = this;
                }
                View v = this.mFocusFinder.findNextFocusFromRect(this.mFindRootView, previouslyFocusedRect, direction);
                if (this.mNodeMap.containsKey(v)) {
                    this.mIndex = this.mNodeMap.get(v).index;
                } else if (this.mIndex < 0) {
                    this.mIndex = getFocusableItemIndex();
                }
            } else if (this.mIndex < 0) {
                this.mIndex = getFocusableItemIndex();
            }
            boolean isNeedReset = true;
            ZpLogger.i(TAG, "FocusLinearLayout.doFocusChanged.getSelectedView = " + getSelectedView());
            if (getSelectedView() instanceof DeepListener) {
                this.mDeep = (DeepListener) getSelectedView();
                if (this.mDeep.canDeep()) {
                    Rect rect = null;
                    if (previouslyFocusedRect != null) {
                        rect = new Rect(previouslyFocusedRect);
                        offsetRectIntoDescendantCoords((View) this.mDeep, rect);
                    }
                    this.mDeep.onFocusDeeped(gainFocus, direction, rect);
                    reset();
                    isNeedReset = false;
                }
            }
            if (!this.mLayouted) {
                this.mNeedReset = true;
            } else {
                if (isNeedReset) {
                    reset();
                }
                performItemSelect(getSelectedView(), gainFocus, true);
            }
        } else if (this.mDeep != null && this.mDeep.canDeep()) {
            this.mDeep.onFocusDeeped(gainFocus, direction, (Rect) null);
        } else if (this.mLayouted) {
            performItemSelect(getSelectedView(), gainFocus, true);
        } else {
            this.mNeedReset = true;
        }
        this.mIsAnimate = checkAnimate(direction);
        invalidate();
    }

    private boolean checkAnimate(int direction) {
        switch (direction) {
            case 17:
                if (!this.mAimateWhenGainFocusFromRight) {
                    return false;
                }
                return true;
            case 33:
                if (!this.mAimateWhenGainFocusFromDown) {
                    return false;
                }
                return true;
            case 66:
                if (!this.mAimateWhenGainFocusFromLeft) {
                    return false;
                }
                return true;
            case 130:
                return this.mAimateWhenGainFocusFromUp;
            default:
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
        View item = getSelectedView();
        if (!isFocused() || item == null || item == null || item == this) {
            super.getFocusedRect(r);
            return;
        }
        item.getFocusedRect(r);
        offsetDescendantRectToMyCoords(item, r);
    }

    public void setSelectedView(View v, int direction) {
        if (!this.mNodeMap.containsKey(v)) {
            throw new IllegalArgumentException("Parent does't contain this view");
        }
        this.mIsAnimate = false;
        View lastSelectedView = getSelectedView();
        this.mIndex = this.mNodeMap.get(v).index;
        this.mNextFocus = v;
        Rect previouslyFocusedRect = null;
        if (this.mDeep != null) {
            previouslyFocusedRect = new Rect();
            previouslyFocusedRect.set(this.mDeep.getFocusParams().focusRect());
            offsetDescendantRectToMyCoords(lastSelectedView, previouslyFocusedRect);
            this.mDeep.onFocusDeeped(false, 0, (Rect) null);
            this.mLastDeep = this.mDeep;
            this.mDeep = null;
        }
        if (v instanceof DeepListener) {
            this.mDeep = (DeepListener) v;
            if (!this.mDeep.canDeep()) {
                this.mDeep = null;
                return;
            }
            offsetRectIntoDescendantCoords(v, previouslyFocusedRect);
            this.mDeep.onFocusDeeped(true, direction, previouslyFocusedRect);
        }
    }

    public View getSelectedView() {
        if (!hasFocusChild()) {
            return null;
        }
        return getChildAt(this.mIndex);
    }

    public boolean isLayouted() {
        return this.mLayouted;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        initNode();
        this.mLayouted = true;
        reset();
        if (this.mNeedReset && hasFocusChild()) {
            performItemSelect(getSelectedView(), isFocused(), true);
            this.mNeedReset = false;
        }
    }

    public void setFirstSelectedView(View v) {
        this.mFirstSelectedView = v;
    }

    /* access modifiers changed from: protected */
    public void initNode() {
        if (this.mNeedInitNode) {
            this.mFocusFinder.clearFocusables();
            this.mFocusFinder.initFocusables(this);
            this.mNodeMap.clear();
            for (int index = 0; index < getChildCount(); index++) {
                View child = getChildAt(index);
                if (child.isFocusable() && (child instanceof ItemListener)) {
                    if (this.mFirstSelectedView == child && isUpdateIndexBySelectView()) {
                        this.mIndex = index;
                        setNeedUpdateIndexBySelectView(false);
                    }
                    if (!this.mNodeMap.containsKey(child)) {
                        NodeInfo info = new NodeInfo();
                        info.index = index;
                        this.mNodeMap.put(child, info);
                    }
                }
            }
            this.mNeedInitNode = false;
        }
    }

    public void forceInitNode() {
        this.mNeedInitNode = true;
        initNode();
    }

    public void notifyLayoutChanged() {
        ZpLogger.d(TAG, "notifyLayoutChanged");
        this.mNeedInitNode = true;
        requestLayout();
    }

    public void requestLayout() {
        super.requestLayout();
        this.mLayouted = false;
    }

    /* access modifiers changed from: protected */
    public int getChildDrawingOrder(int childCount, int i) {
        int selectedIndex = this.mIndex;
        if (selectedIndex >= 0 && i >= selectedIndex && i >= selectedIndex) {
            return ((childCount - 1) - i) + selectedIndex;
        }
        return i;
    }

    public boolean isUpdateIndexBySelectView() {
        return this.mUpdateIndexBySelectView;
    }

    public void setNeedUpdateIndexBySelectView(boolean needResetIndex) {
        this.mUpdateIndexBySelectView = needResetIndex;
    }

    class NodeInfo {
        public View fromDown;
        public View fromLeft;
        public View fromRight;
        public View fromUp;
        public int index;

        NodeInfo() {
        }
    }

    public FocusRectParams getFocusParams() {
        View v = getSelectedView();
        if (!isFocusOrSlected() || v == null) {
            Rect r = new Rect();
            getFocusedRect(r);
            this.mFocusRectparams.set(r, 0.5f, 0.5f);
            return this.mFocusRectparams;
        }
        if (this.mFocusRectparams == null || isScrolling()) {
            reset();
        }
        return this.mFocusRectparams;
    }

    public boolean canDraw() {
        if (this.mDeep != null) {
            return this.mDeep.canDraw();
        }
        return getSelectedView() != null || this.mOnFocused || isSelected();
    }

    public boolean isAnimate() {
        if (this.mDeep != null) {
            return this.mDeep.isAnimate();
        }
        return this.mIsAnimate;
    }

    /* Debug info: failed to restart local var, previous not found, register: 5 */
    public ItemListener getItem() {
        View v = getSelectedView();
        if (!isFocusOrSlected() || v == null) {
            return this;
        }
        ItemListener item = (ItemListener) v;
        DeepListener deep = this.mDeep;
        DeepListener lastDeep = this.mLastDeep;
        if (deep != null && deep.hasDeepFocus()) {
            item = deep.getItem();
        } else if (lastDeep != null && lastDeep.hasDeepFocus()) {
            item = lastDeep.getItem();
        }
        if (item == null) {
            return this;
        }
        return item;
    }

    public boolean isScrolling() {
        if (this.mDeep != null) {
            return this.mDeep.isScrolling();
        }
        return false;
    }

    public Params getParams() {
        if (this.mDeep != null) {
            return this.mDeep.getParams();
        }
        if (this.mParams != null) {
            return this.mParams;
        }
        throw new IllegalArgumentException("The params is null, you must call setScaleParams before it's running");
    }

    public boolean preOnKeyDown(int keyCode, KeyEvent event) {
        View nextFocus;
        ZpLogger.d(TAG, "preOnKeyDown keyCode = " + keyCode);
        if (this.mDeep != null && this.mDeep.preOnKeyDown(keyCode, event)) {
            return true;
        }
        View selectedView = getSelectedView();
        NodeInfo nodeInfo = this.mNodeMap.get(selectedView);
        switch (keyCode) {
            case 19:
                this.mNextDirection = 33;
                if (nodeInfo != null && nodeInfo.fromUp != null && nodeInfo.fromUp.isFocusable()) {
                    nextFocus = nodeInfo.fromUp;
                    break;
                } else {
                    nextFocus = this.mFocusFinder.findNextFocus(this, selectedView, this.mNextDirection);
                    break;
                }
                break;
            case 20:
                this.mNextDirection = 130;
                if (nodeInfo != null && nodeInfo.fromDown != null && nodeInfo.fromDown.isFocusable()) {
                    nextFocus = nodeInfo.fromDown;
                    break;
                } else {
                    nextFocus = this.mFocusFinder.findNextFocus(this, selectedView, this.mNextDirection);
                    break;
                }
                break;
            case 21:
                this.mNextDirection = 17;
                if (nodeInfo != null && nodeInfo.fromLeft != null && nodeInfo.fromLeft.isFocusable()) {
                    nextFocus = nodeInfo.fromLeft;
                    break;
                } else {
                    nextFocus = this.mFocusFinder.findNextFocus(this, selectedView, this.mNextDirection);
                    break;
                }
            case 22:
                this.mNextDirection = 66;
                if (nodeInfo != null && nodeInfo.fromRight != null && nodeInfo.fromRight.isFocusable()) {
                    nextFocus = nodeInfo.fromRight;
                    break;
                } else {
                    nextFocus = this.mFocusFinder.findNextFocus(this, selectedView, this.mNextDirection);
                    break;
                }
                break;
            case 23:
            case 66:
            case 160:
                return true;
            default:
                return false;
        }
        this.mNextFocus = nextFocus;
        if (nextFocus == null) {
            return false;
        }
        return true;
    }

    private Rect getFocusedRect(View from, View to) {
        if (from == null || to == null) {
            return null;
        }
        Rect rFrom = new Rect();
        from.getFocusedRect(rFrom);
        Rect rTo = new Rect();
        to.getFocusedRect(rTo);
        offsetDescendantRectToMyCoords(from, rFrom);
        offsetDescendantRectToMyCoords(to, rTo);
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

    public boolean hasDeepFocus() {
        return this.mDeepFocus;
    }

    public boolean canDeep() {
        if (!hasFocusChild()) {
            return false;
        }
        return true;
    }

    public void onFocusDeeped(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        this.mDeepFocus = gainFocus;
        onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public boolean isScale() {
        return false;
    }

    public int getItemWidth() {
        View v = getSelectedView();
        if (!isFocusOrSlected() || v == null) {
            return getWidth();
        }
        return v.getWidth();
    }

    public int getItemHeight() {
        View v = getSelectedView();
        if (!isFocusOrSlected() || v == null) {
            return getHeight();
        }
        return v.getHeight();
    }

    public Rect getManualPadding() {
        return null;
    }

    public void onFocusStart() {
        if (this.mDeep != null) {
            this.mDeep.onFocusStart();
        } else if (this.mFocusStateListener != null) {
            this.mFocusStateListener.onFocusStart(getSelectedView(), this);
        }
    }

    public void onFocusFinished() {
        if (this.mDeep != null) {
            this.mDeep.onFocusFinished();
        } else if (this.mFocusStateListener != null) {
            this.mFocusStateListener.onFocusFinished(getSelectedView(), this);
        }
    }

    public void onItemSelected(boolean selected) {
        performItemSelect(getSelectedView(), selected, false);
    }

    public void onItemClick() {
        performClick();
    }

    private int getFocusableItemIndex() {
        int childCount = getChildCount();
        int result = -1;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.isFocusable() && childView.getVisibility() == 0 && (childView instanceof ItemListener)) {
                if (childView == this.mFirstSelectedView) {
                    return i;
                }
                if (result == -1) {
                    result = i;
                }
            }
        }
        return result;
    }

    public boolean isFocusBackground() {
        if (this.mDeep != null) {
            return this.mDeep.isFocusBackground();
        }
        return this.mFocusBackground;
    }

    public void drawBeforeFocus(Canvas canvas) {
    }

    public void drawAfterFocus(Canvas canvas) {
    }

    public boolean isFinished() {
        return true;
    }

    public boolean hasFocusChild() {
        return this.mNodeMap != null && !this.mNodeMap.isEmpty();
    }

    public void setNeedInitNode(boolean needInitNode) {
        this.mNeedInitNode = needInitNode;
    }

    public boolean isNeedInitNode() {
        return this.mNeedInitNode;
    }

    public Map<View, NodeInfo> getNodeMap() {
        return this.mNodeMap;
    }

    private boolean isFocusOrSlected() {
        return isFocused() || isSelected();
    }

    public boolean isFocused() {
        return super.isFocused() || hasFocus() || hasDeepFocus() || this.mOnFocused;
    }

    public Rect getClipFocusRect() {
        if (this.mDeep == null || !isFocusOrSlected() || !isScrolling()) {
            return null;
        }
        Rect clipFocusRect = new Rect();
        Rect deepRect = this.mDeep.getClipFocusRect();
        if (deepRect == null) {
            return clipFocusRect;
        }
        clipFocusRect.set(deepRect);
        offsetDescendantRectToMyCoords((View) this.mDeep, clipFocusRect);
        return clipFocusRect;
    }
}
