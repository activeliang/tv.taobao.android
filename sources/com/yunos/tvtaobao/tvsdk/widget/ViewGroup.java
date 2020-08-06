package com.yunos.tvtaobao.tvsdk.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.animation.Interpolator;
import com.yunos.tvtaobao.tvsdk.widget.focus.action.IFocusAction;
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

public abstract class ViewGroup extends android.view.ViewGroup implements IFocusAction, ItemListener {
    protected static final boolean DEBUG = true;
    private final String TAG = getClass().getSimpleName();
    private boolean mAimateWhenGainFocusFromDown = true;
    private boolean mAimateWhenGainFocusFromLeft = true;
    private boolean mAimateWhenGainFocusFromRight = true;
    private boolean mAimateWhenGainFocusFromUp = true;
    private boolean mAutoSearchFocus = true;
    boolean mClearDataDetachedFromWindow = true;
    protected DeepListener mDeep = null;
    private boolean mDeepFocus = false;
    android.view.ViewGroup mFindRootView;
    View mFirstSelectedView = null;
    boolean mFocusBackground = false;
    private FocusFinder mFocusFinder;
    protected FocusRectParams mFocusRectparams = new FocusRectParams();
    private FocusStateListener mFocusStateListener = null;
    protected int mIndex = -1;
    private boolean mIsAnimate = true;
    private ItemSelectedListener mItemSelectedListener;
    protected DeepListener mLastDeep = null;
    View mLastSelectedView = null;
    boolean mLayouted = false;
    boolean mNeedFocused = true;
    protected boolean mNeedInit = true;
    boolean mNeedInitNode = true;
    boolean mNeedReset = false;
    protected int mNextDirection;
    protected View mNextFocus = null;
    protected Map<View, NodeInfo> mNodeMap = new HashMap();
    private boolean mOnFocused = false;
    private OnItemClickListener mOnItemClickListener;
    protected Params mParams = new Params(1.1f, 1.1f, 10, (Interpolator) null, true, 20, new AccelerateDecelerateFrameInterpolator());
    private boolean mUpdateIndexBySelectView;

    public interface OnItemClickListener {
        void onItemClick(android.view.ViewGroup viewGroup, View view);
    }

    public class NodeInfo {
        public View fromDown;
        public View fromLeft;
        public View fromRight;
        public View fromUp;
        public int index;

        public NodeInfo() {
        }
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ViewGroup(Context context) {
        super(context);
        boolean z = true;
        this.mUpdateIndexBySelectView = this.mIndex >= 0 ? false : z;
        initFocusFinder();
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        boolean z = true;
        this.mUpdateIndexBySelectView = this.mIndex >= 0 ? false : z;
        initFocusFinder();
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        boolean z = true;
        this.mUpdateIndexBySelectView = this.mIndex >= 0 ? false : z;
        initFocusFinder();
    }

    public boolean isUpdateIndexBySelectView() {
        return this.mUpdateIndexBySelectView;
    }

    public void setNeedUpdateIndexBySelectView(boolean needResetIndex) {
        this.mUpdateIndexBySelectView = needResetIndex;
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (views == null || !isFocusable()) {
            return;
        }
        if ((focusableMode & 1) != 1 || !isInTouchMode() || isFocusableInTouchMode()) {
            views.add(this);
        }
    }

    /* access modifiers changed from: protected */
    public void afterLayout(boolean changed, int l, int t, int r, int b) {
        boolean z;
        initNode();
        this.mLayouted = true;
        reset();
        if (this.mNeedReset && hasFocusChild()) {
            View selectedView = getSelectedView();
            if (hasFocus() || hasDeepFocus()) {
                z = true;
            } else {
                z = false;
            }
            performItemSelect(selectedView, z, true);
            this.mNeedReset = false;
        }
    }

    public boolean hasFocusChild() {
        return this.mNodeMap != null && !this.mNodeMap.isEmpty();
    }

    public boolean canDeep() {
        if (!hasFocusChild()) {
            return false;
        }
        return true;
    }

    public boolean canDraw() {
        if ((this.mDeep == null || !this.mDeep.canDraw()) && getSelectedView() == null && !this.mOnFocused && !isSelected()) {
            return false;
        }
        return true;
    }

    public boolean checkAnimate(int direction) {
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

    public void clearFocusedIndex() {
        this.mIndex = -1;
    }

    public void clearSelectedView() {
        View selectedView = getSelectedView();
        if (selectedView != null) {
            selectedView.setSelected(false);
            performItemSelect(selectedView, false, false);
            View.OnFocusChangeListener listener = selectedView.getOnFocusChangeListener();
            if (listener != null) {
                listener.onFocusChange(selectedView, false);
            }
        }
    }

    public void drawAfterFocus(Canvas canvas) {
    }

    public void drawBeforeFocus(Canvas canvas) {
    }

    public void forceInitNode() {
        this.mNeedInitNode = true;
        initNode();
    }

    /* access modifiers changed from: protected */
    public boolean getAnimateByKeyCode(AnimateWhenGainFocusListener animateListener, int keyCode) {
        switch (keyCode) {
            case 19:
                return animateListener.fromBottom();
            case 20:
                return animateListener.fromTop();
            case 21:
                return animateListener.fromRight();
            case 22:
                return animateListener.fromLeft();
            default:
                return false;
        }
    }

    /* access modifiers changed from: protected */
    public int getChildDrawingOrder(int childCount, int i) {
        int selectedIndex = this.mIndex;
        if (selectedIndex >= 0 && i >= selectedIndex && i >= selectedIndex) {
            return ((childCount - 1) - i) + selectedIndex;
        }
        return i;
    }

    public DeepListener getDeep() {
        return this.mDeep;
    }

    public int getDirectionByKeyCode(int keyCode) {
        switch (keyCode) {
            case 19:
                return 33;
            case 20:
                return 130;
            case 21:
                return 17;
            case 22:
                return 66;
            default:
                ZpLogger.w(this.TAG, "direction is default value : View.FOCUS_DOWN");
                return 130;
        }
    }

    /* access modifiers changed from: protected */
    public int getFocusableItemIndex() {
        int childCount = getChildCount();
        int i = 0;
        while (i < childCount) {
            View childView = getChildAt(i);
            if (!childView.isFocusable() || childView.getVisibility() != 0 || !(childView instanceof ItemListener)) {
                i++;
            } else {
                if (childView == this.mFirstSelectedView) {
                }
                return i;
            }
        }
        return -1;
    }

    public void getFocusedRect(Rect r) {
        View item = getSelectedView();
        if ((hasFocus() || hasDeepFocus()) && item != null) {
            item.getFocusedRect(r);
            offsetDescendantRectToMyCoords(item, r);
            return;
        }
        super.getFocusedRect(r);
    }

    /* access modifiers changed from: protected */
    public Rect getFocusedRect(View from, View to) {
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

    public FocusFinder getFocusFinder() {
        return this.mFocusFinder;
    }

    public FocusRectParams getFocusParams() {
        if (this.mFocusRectparams == null || isScrolling()) {
            reset();
        }
        return this.mFocusRectparams;
    }

    public FocusRectParams getFocusRectParams() {
        return this.mFocusRectparams;
    }

    public FocusStateListener getFocusStateListener() {
        return this.mFocusStateListener;
    }

    /* Debug info: failed to restart local var, previous not found, register: 1 */
    public ItemListener getItem() {
        if (!hasDeepFocus()) {
            return this;
        }
        if (this.mDeep != null && this.mDeep.hasDeepFocus()) {
            return this.mDeep.getItem();
        }
        if (this.mLastDeep != null) {
            return this.mLastDeep.getItem();
        }
        return (ItemListener) getSelectedView();
    }

    public int getItemHeight() {
        if (!hasFocusChild() || getSelectedView() == null) {
            return getHeight();
        }
        return getSelectedView().getHeight();
    }

    public ItemSelectedListener getOnItemSelectedListener() {
        return this.mItemSelectedListener;
    }

    public int getItemWidth() {
        if (!hasFocusChild() || getSelectedView() == null) {
            return getWidth();
        }
        return getSelectedView().getWidth();
    }

    public DeepListener getLastDeep() {
        return this.mLastDeep;
    }

    public View getLastSelectedView() {
        return this.mLastSelectedView;
    }

    public Rect getManualPadding() {
        return null;
    }

    public View getNextFocus() {
        return this.mNextFocus;
    }

    public OnItemClickListener getOnItemClickListener() {
        return this.mOnItemClickListener;
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

    public View getSelectedView() {
        if (!hasFocusChild()) {
            return null;
        }
        if (this.mIndex < 0) {
            this.mIndex = getFocusableItemIndex();
        }
        return getChildAt(this.mIndex);
    }

    public boolean hasDeepFocus() {
        return this.mDeepFocus;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        forceInitNode();
        setNeedInitNode(true);
    }

    private void initFocusFinder() {
        this.mFocusFinder = new FocusFinder();
        this.mFocusFinder.clearFocusables();
    }

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

    public void setNeedInitNode(boolean initNode) {
        this.mNeedInitNode = initNode;
    }

    public boolean isNeedInitNode() {
        return this.mNeedInitNode;
    }

    public boolean isAnimate() {
        if (this.mDeep != null) {
            return this.mDeep.isAnimate();
        }
        return this.mIsAnimate;
    }

    /* access modifiers changed from: package-private */
    public boolean isDirectionKeyCode(int keyCode) {
        return isHorizontalKeyCode(keyCode) || isVerticalKeyCode(keyCode);
    }

    /* access modifiers changed from: package-private */
    public boolean isEnterKeyCode(int keyCode) {
        return keyCode == 23 || keyCode == 66 || keyCode == 160;
    }

    public boolean isFinished() {
        return true;
    }

    public boolean isFocusBackground() {
        if (this.mDeep != null) {
            return this.mDeep.isFocusBackground();
        }
        return this.mFocusBackground;
    }

    /* access modifiers changed from: package-private */
    public boolean isHorizontalKeyCode(int keyCode) {
        return keyCode == 21 || keyCode == 22;
    }

    /* access modifiers changed from: package-private */
    public boolean isItemClickSelf() {
        return this.mDeep == null;
    }

    /* access modifiers changed from: package-private */
    public boolean isItemSelectSelf() {
        return this.mDeep == null;
    }

    public boolean isNeedFocusItem() {
        return this.mNeedFocused;
    }

    public boolean isLayouted() {
        return this.mLayouted;
    }

    /* access modifiers changed from: package-private */
    public boolean isNeedOnFocusSelf() {
        return this.mDeep == null;
    }

    public boolean isScale() {
        return false;
    }

    public boolean isScrolling() {
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isSelfChildren(View view) {
        return indexOfChild(view) >= 0;
    }

    /* access modifiers changed from: package-private */
    public boolean isVerticalKeyCode(int keyCode) {
        return keyCode == 19 || keyCode == 20;
    }

    public void notifyLayoutChanged() {
        ZpLogger.d(this.TAG, "notifyLayoutChanged");
        this.mNeedInitNode = true;
        requestLayout();
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

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        this.mOnFocused = gainFocus;
        ZpLogger.d(this.TAG, "onFocusChanged");
        if (getOnFocusChangeListener() != null) {
            getOnFocusChangeListener().onFocusChange(this, gainFocus);
        }
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
            boolean isDeep = false;
            if (getSelectedView() instanceof DeepListener) {
                this.mDeep = (DeepListener) getSelectedView();
                if (this.mDeep.canDeep()) {
                    isDeep = true;
                    Rect rect = new Rect(previouslyFocusedRect);
                    offsetRectIntoDescendantCoords((View) this.mDeep, rect);
                    this.mDeep.onFocusDeeped(gainFocus, direction, rect);
                    reset();
                }
            }
            if (!isDeep) {
                if (!this.mLayouted) {
                    this.mNeedReset = true;
                } else {
                    reset();
                    performItemSelect(getSelectedView(), gainFocus, true);
                }
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

    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect, android.view.ViewGroup findRoot) {
        this.mFindRootView = findRoot;
        onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        this.mFindRootView = null;
    }

    public void onFocusDeeped(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        this.mDeepFocus = gainFocus;
        onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public void onFocusFinished() {
        if (this.mDeep != null) {
            this.mDeep.onFocusFinished();
        } else if (this.mFocusStateListener != null) {
            this.mFocusStateListener.onFocusFinished(getSelectedView(), this);
        }
    }

    public void onFocusStart() {
        if (this.mDeep != null) {
            this.mDeep.onFocusStart();
        } else if (this.mFocusStateListener != null) {
            this.mFocusStateListener.onFocusStart(getSelectedView(), this);
        }
    }

    public void onItemClick() {
        performClick();
    }

    public void onItemSelected(boolean selected) {
        performItemSelect(getSelectedView(), selected, false);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ZpLogger.d(this.TAG, "onKeyDown keyCode = " + keyCode);
        if (keyCode == 23 || keyCode == 66 || keyCode == 160) {
            setPressed(true);
            return true;
        } else if (this.mDeep == null || !this.mDeep.canDeep() || !this.mDeep.hasDeepFocus() || !this.mDeep.onKeyDown(keyCode, event)) {
            int direction = 0;
            if (isDirectionKeyCode(keyCode)) {
                direction = getDirectionByKeyCode(keyCode);
            }
            if (this.mNextFocus == null || !this.mNodeMap.containsKey(this.mNextFocus) || !this.mNextFocus.isFocusable()) {
                return super.onKeyDown(keyCode, event);
            }
            this.mIsAnimate = true;
            if (this.mDeep == null || !this.mDeep.canDeep()) {
                if (this.mLastDeep != null && this.mLastDeep.hasDeepFocus()) {
                    this.mLastDeep.onFocusDeeped(false, direction, (Rect) null);
                }
                this.mLastSelectedView = getSelectedView();
                if (this.mLastSelectedView != null) {
                    this.mLastSelectedView.setSelected(false);
                    performItemSelect(this.mLastSelectedView, false, false);
                    View.OnFocusChangeListener listener = this.mLastSelectedView.getOnFocusChangeListener();
                    if (listener != null) {
                        listener.onFocusChange(this.mLastSelectedView, false);
                    }
                }
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
                View selectedView = getSelectedView();
                if (selectedView != null) {
                    selectedView.setSelected(true);
                    performItemSelect(selectedView, true, false);
                    View.OnFocusChangeListener listener2 = selectedView.getOnFocusChangeListener();
                    if (listener2 != null) {
                        listener2.onFocusChange(selectedView, true);
                    }
                }
                reset();
                playSoundEffect(SoundEffectConstants.getContantForFocusDirection(this.mNextDirection));
                return true;
            }
            if (!this.mDeep.hasDeepFocus()) {
                Rect previouslyFocusedRect = getFocusedRect(getSelectedView(), this.mNextFocus);
                if (this.mLastDeep != null && this.mLastDeep.hasDeepFocus()) {
                    this.mLastDeep.onFocusDeeped(false, direction, (Rect) null);
                    this.mLastDeep = null;
                }
                this.mDeep.onFocusDeeped(true, this.mNextDirection, previouslyFocusedRect);
                this.mIndex = this.mNodeMap.get(this.mNextFocus).index;
                reset();
            }
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
            return true;
        } else {
            reset();
            return true;
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        ZpLogger.d(this.TAG, "onKeyUp keyCode = " + keyCode);
        if (this.mDeep != null && this.mDeep.canDeep() && this.mDeep.hasDeepFocus()) {
            return this.mDeep.onKeyUp(keyCode, event);
        }
        if ((23 != keyCode && 66 != keyCode && keyCode != 160) || getSelectedView() == null) {
            return super.onKeyUp(keyCode, event);
        }
        if (isPressed()) {
            setPressed(false);
            performItemClick();
            getSelectedView().performClick();
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void performItemClick() {
        if (this.mDeep != null) {
            this.mDeep.onItemClick();
        } else if (this.mOnItemClickListener != null) {
            this.mOnItemClickListener.onItemClick(this, getSelectedView());
        }
    }

    /* access modifiers changed from: package-private */
    public void performItemSelect(View v, boolean isSelected, boolean isLocal) {
        if (!isLocal && this.mDeep != null) {
            this.mDeep.onItemSelected(isSelected);
        } else if (v != null) {
            v.setSelected(isSelected);
            if (this.mItemSelectedListener != null) {
                this.mItemSelectedListener.onItemSelected(v, this.mIndex, isSelected, this);
            }
        }
    }

    public boolean preOnKeyDown(int keyCode, KeyEvent event) {
        View nextFocus;
        ZpLogger.d(this.TAG, "preOnKeyDown keyCode = " + keyCode);
        if (this.mDeep != null && this.mDeep.preOnKeyDown(keyCode, event)) {
            return true;
        }
        View selectedView = getSelectedView();
        NodeInfo nodeInfo = this.mNodeMap.get(selectedView);
        switch (keyCode) {
            case 19:
                this.mNextDirection = 33;
                if (nodeInfo != null && nodeInfo.fromUp != null && nodeInfo.fromUp.isFocusable() && !nodeInfo.fromUp.equals(selectedView)) {
                    nextFocus = nodeInfo.fromUp;
                    break;
                } else {
                    nextFocus = this.mFocusFinder.findNextFocus(this, selectedView, this.mNextDirection);
                    break;
                }
                break;
            case 20:
                this.mNextDirection = 130;
                if (nodeInfo != null && nodeInfo.fromDown != null && nodeInfo.fromDown.isFocusable() && !nodeInfo.fromDown.equals(selectedView)) {
                    nextFocus = nodeInfo.fromDown;
                    break;
                } else {
                    nextFocus = this.mFocusFinder.findNextFocus(this, selectedView, this.mNextDirection);
                    break;
                }
            case 21:
                this.mNextDirection = 17;
                if (nodeInfo != null && nodeInfo.fromLeft != null && nodeInfo.fromLeft.isFocusable() && !nodeInfo.fromLeft.equals(selectedView)) {
                    nextFocus = nodeInfo.fromLeft;
                    break;
                } else {
                    nextFocus = this.mFocusFinder.findNextFocus(this, selectedView, this.mNextDirection);
                    break;
                }
            case 22:
                this.mNextDirection = 66;
                if (nodeInfo != null && nodeInfo.fromRight != null && nodeInfo.fromRight.isFocusable() && !nodeInfo.fromRight.equals(selectedView)) {
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
        if (this.mDeep != null) {
            this.mLastDeep = this.mDeep;
            this.mDeep = null;
        }
        if (!(nextFocus instanceof DeepListener)) {
            return true;
        }
        this.mDeep = (DeepListener) nextFocus;
        if (this.mDeep.canDeep()) {
            return true;
        }
        this.mDeep = null;
        return true;
    }

    public void release() {
        this.mNodeMap.clear();
    }

    public void requestLayout() {
        super.requestLayout();
        this.mLayouted = false;
    }

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
            offsetDescendantRectToMyCoords(getSelectedView(), this.mFocusRectparams.focusRect());
        }
    }

    public void setAnimateWhenGainFocus(boolean fromleft, boolean fromUp, boolean fromRight, boolean fromDown) {
        this.mAimateWhenGainFocusFromLeft = fromleft;
        this.mAimateWhenGainFocusFromUp = fromUp;
        this.mAimateWhenGainFocusFromRight = fromRight;
        this.mAimateWhenGainFocusFromDown = fromDown;
    }

    public void setAutoSearchFocus(boolean autoSearchFocus) {
        this.mAutoSearchFocus = autoSearchFocus;
    }

    public void setClearDataDetachedFromWindowEnable(boolean enable) {
        this.mClearDataDetachedFromWindow = enable;
    }

    public void setDeep(DeepListener deep) {
        this.mDeep = deep;
    }

    public void setDeepFocus(boolean deepFocus) {
        this.mDeepFocus = deepFocus;
    }

    public void setFirstSelectedView(View v) {
        this.mFirstSelectedView = v;
    }

    public void setFocusBackground(boolean focusBg) {
        this.mFocusBackground = focusBg;
    }

    public void setFocusRectParams(FocusRectParams params) {
        if (params == null) {
            ZpLogger.w(this.TAG, "AbstractViewGroupFocus setFocusRectParams 'params' is null.");
        } else {
            this.mFocusRectparams.set(params);
        }
    }

    public void setLastDeep(DeepListener lastDeep) {
        this.mLastDeep = lastDeep;
    }

    public void setLastSelectedView(View lastSelectedView) {
        this.mLastSelectedView = lastSelectedView;
    }

    public void setNextFocus(View nextFocus) {
        this.mNextFocus = nextFocus;
    }

    public void setOnFocusStateListener(FocusStateListener focusStateListener) {
        this.mFocusStateListener = focusStateListener;
    }

    public void setOnItemClickListener(OnItemClickListener lis) {
        this.mOnItemClickListener = lis;
    }

    public void setOnItemSelectedListener(ItemSelectedListener lis) {
        this.mItemSelectedListener = lis;
    }

    public void setParams(Params params) {
        if (params == null) {
            throw new NullPointerException("AbstractViewGroupFocus setParams params is null.");
        }
        this.mParams = params;
    }

    public void setSelected(boolean selected) {
        if (!selected) {
            this.mNeedFocused = true;
        }
        super.setSelected(selected);
    }

    public void setSelectedView(View v) {
        if (!this.mNodeMap.containsKey(v)) {
            throw new IllegalArgumentException("Parent does't contain this view");
        }
    }

    public Map<View, NodeInfo> getNodeMap() {
        return this.mNodeMap;
    }
}
