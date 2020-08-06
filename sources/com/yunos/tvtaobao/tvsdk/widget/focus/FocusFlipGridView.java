package com.yunos.tvtaobao.tvsdk.widget.focus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import com.alibaba.wireless.security.SecExceptionCode;
import com.uc.webview.export.extension.UCCore;
import com.yunos.tvtaobao.tvsdk.utils.SystemProUtils;
import com.yunos.tvtaobao.tvsdk.widget.AbsBaseListView;
import com.yunos.tvtaobao.tvsdk.widget.FlipGridView;
import com.yunos.tvtaobao.tvsdk.widget.GridView;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.DeepListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.FocusListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemSelectedListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.Params;
import com.yunos.tvtaobao.tvsdk.widget.interpolator.AccelerateDecelerateFrameInterpolator;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;

public class FocusFlipGridView extends FlipGridView implements FocusListener, DeepListener, ItemListener {
    private static final String TAG = "FocusFlipGridView";
    private final boolean DEBUG = false;
    private final boolean DYNAMIC_ADD_CHILD_VIEW = true;
    private boolean forceResetFocusParams = false;
    private boolean mAimateWhenGainFocusFromDown = true;
    private boolean mAimateWhenGainFocusFromLeft = true;
    private boolean mAimateWhenGainFocusFromRight = true;
    private boolean mAimateWhenGainFocusFromUp = true;
    private RectF mAlphaRectF;
    /* access modifiers changed from: private */
    public boolean mAnimAlpha = true;
    /* access modifiers changed from: private */
    public int mAnimAlphaValue = 255;
    private boolean mCenterFocus = true;
    protected Rect mClipFocusRect = new Rect();
    boolean mDeepFocus = false;
    private int mDistance = -1;
    private boolean mFirstAnimDone = true;
    private FocusRectParams mFocusRectparams = new FocusRectParams();
    private boolean mGainFocus;
    private boolean mIsAnimate = true;
    private boolean mIsFirstLayout = true;
    private ItemSelectedListener mItemSelectedListener;
    private boolean mNeedAutoSearchFocused = true;
    private boolean mNeedResetParam = false;
    /* access modifiers changed from: private */
    public OnFocusFlipGridViewListener mOnFocusFlipGridViewListener;
    private int mOnKeyDirection = 130;
    private OutAnimationRunnable mOutAnimationRunnable;
    protected Params mParams = new Params(1.1f, 1.1f, 10, (Interpolator) null, true, 20, new AccelerateDecelerateFrameInterpolator());
    private Rect mPreFocusRect = new Rect();

    public interface OnFocusFlipGridViewListener {
        void onLayoutDone(boolean z);

        void onOutAnimationDone();

        void onReachGridViewBottom();

        void onReachGridViewTop();
    }

    public FocusFlipGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FocusFlipGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FocusFlipGridView(Context context) {
        super(context);
        init();
    }

    public void setOnItemSelectedListener(ItemSelectedListener listener) {
        this.mItemSelectedListener = listener;
    }

    public void setAnimateWhenGainFocus(boolean fromleft, boolean fromUp, boolean fromRight, boolean fromDown) {
        this.mAimateWhenGainFocusFromLeft = fromleft;
        this.mAimateWhenGainFocusFromUp = fromUp;
        this.mAimateWhenGainFocusFromRight = fromRight;
        this.mAimateWhenGainFocusFromDown = fromDown;
    }

    public void initFocused() {
        setNeedAutoSearchFocused(true);
        if (getHeaderViewsCount() > 0) {
            for (int i = 0; i < getHeaderViewsCount(); i++) {
                View view = ((AbsBaseListView.FixedViewInfo) this.mHeaderViewInfos.get(i)).view;
                if (view instanceof FocusRelativeLayout) {
                    FocusRelativeLayout headerView = (FocusRelativeLayout) view;
                    headerView.notifyLayoutChanged();
                    headerView.clearFocusedIndex();
                }
            }
        }
    }

    public void setNeedAutoSearchFocused(boolean b) {
        this.mNeedAutoSearchFocused = b;
    }

    public void setSelection(int position) {
        if (isFlipFinished() && getChildCount() > 0 && !this.mIsFirstLayout) {
            View preSelectedView = getSelectedView();
            int preSelectedPos = getSelectedItemPosition();
            setSelectedPositionInt(position);
            setNextSelectedPositionInt(position);
            this.mLayoutMode = 9;
            lockFlipAnimOnceLayout();
            this.mNeedLayout = true;
            this.mNeedAutoSearchFocused = false;
            this.mNeedResetParam = true;
            layoutChildren();
            if (isFocused()) {
                checkSelected(preSelectedView, preSelectedPos);
                return;
            }
            View currSelectedView = getSelectedView();
            int currSelectedPos = getSelectedItemPosition();
            if (preSelectedPos != currSelectedPos) {
                if (currSelectedView != null) {
                    currSelectedView.setSelected(true);
                }
                if (this.mItemSelectedListener != null) {
                    this.mItemSelectedListener.onItemSelected(currSelectedView, currSelectedPos, true, this);
                }
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int selectedPos;
        View focusedView;
        FocusRectParams rectParms;
        int nextSelectedPosition;
        if (getAdapter() == null || getChildCount() <= 0) {
            return false;
        }
        View preSelectedView = getSelectedView();
        int preSelectedPos = getSelectedItemPosition();
        this.mIsAnimate = true;
        if (!hasFocus()) {
            ZpLogger.i(TAG, "requestFocus for touch event to onKeyUp");
            requestFocus();
        }
        switch (keyCode) {
            case 19:
                this.mOnKeyDirection = 33;
                break;
            case 20:
                this.mOnKeyDirection = 130;
                break;
            case 21:
                this.mOnKeyDirection = 17;
                break;
            case 22:
                this.mOnKeyDirection = 66;
                break;
            default:
                this.mOnKeyDirection = 130;
                break;
        }
        int selectedPos2 = getSelectedItemPosition();
        if (selectedPos2 < getHeaderViewsCount()) {
            View view = getSelectedView();
            if (view instanceof FocusRelativeLayout) {
                FocusRelativeLayout headerView = (FocusRelativeLayout) view;
                boolean headerViewRet = headerView.onKeyDown(keyCode, event);
                if (headerViewRet) {
                    this.mNeedResetParam = true;
                    layoutResetParam();
                    checkSelected(preSelectedView, preSelectedPos);
                    return headerViewRet;
                }
                headerView.clearSelectedView();
            }
        }
        if (!isFlipFinished()) {
            switch (keyCode) {
                case 19:
                    if (!lockKeyEvent(19)) {
                        int currselectedPos = getSelectedItemPosition();
                        int headerCount = this.mHeaderViewInfos.size();
                        if (headerCount <= 0) {
                            nextSelectedPosition = currselectedPos - getColumnNum();
                            if (nextSelectedPosition < 0) {
                                nextSelectedPosition = -1;
                            }
                        } else if (currselectedPos >= headerCount) {
                            nextSelectedPosition = currselectedPos - getColumnNum();
                            if (nextSelectedPosition < headerCount) {
                                nextSelectedPosition = headerCount - 1;
                            }
                        } else {
                            nextSelectedPosition = currselectedPos - 1;
                            if (nextSelectedPosition < 0) {
                                nextSelectedPosition = -1;
                            }
                        }
                        if (nextSelectedPosition != -1) {
                            setSelectedPositionInt(nextSelectedPosition);
                            checkSelectionChanged();
                            View selectedView = getChildAt(getRowEnd(nextSelectedPosition) - getFirstVisiblePosition());
                            if (selectedView != null) {
                                setReferenceViewInSelectedRow(selectedView);
                            }
                            if (nextSelectedPosition < headerCount) {
                                View view2 = ((AbsBaseListView.FixedViewInfo) this.mHeaderViewInfos.get(nextSelectedPosition)).view;
                                if (view2.getParent() == null) {
                                    view2.measure(View.MeasureSpec.makeMeasureSpec(view2.getWidth(), UCCore.VERIFY_POLICY_QUICK), View.MeasureSpec.makeMeasureSpec(view2.getHeight(), UCCore.VERIFY_POLICY_QUICK));
                                    view2.layout(0, 0, view2.getWidth(), view2.getHeight());
                                }
                                if ((view2 instanceof FocusRelativeLayout) && (view2 instanceof FlipGridView.FlipGridViewHeaderOrFooterInterface)) {
                                    FocusRelativeLayout headerView2 = (FocusRelativeLayout) view2;
                                    if (headerView2.isNeedFocusItem()) {
                                        int remainScrollDistance = getRemainScrollUpDistance(nextSelectedPosition);
                                        this.mPreFocusRect.top += remainScrollDistance;
                                        this.mPreFocusRect.bottom += remainScrollDistance;
                                        headerView2.onFocusChanged(true, this.mOnKeyDirection, this.mPreFocusRect, (ViewGroup) null);
                                    }
                                    headerView2.reset();
                                }
                            }
                            amountToCenterScroll(33, nextSelectedPosition);
                            checkSelected(preSelectedView, preSelectedPos);
                            return true;
                        }
                    }
                    return true;
                case 20:
                    if (!lockKeyEvent(20)) {
                        int nextSelectedPosition2 = getSelectedItemPosition() + getColumnNum() < this.mItemCount ? getSelectedItemPosition() + getColumnNum() : -1;
                        boolean isLastMovePos = false;
                        int selectedRowStart = getRowStart(getSelectedItemPosition());
                        if (nextSelectedPosition2 == -1 && getColumnNum() + selectedRowStart < this.mItemCount) {
                            nextSelectedPosition2 = this.mItemCount - 1;
                            isLastMovePos = true;
                        }
                        if (nextSelectedPosition2 != -1) {
                            setSelectedPositionInt(nextSelectedPosition2);
                            checkSelectionChanged();
                            View selectedView2 = getChildAt(getRowEnd(nextSelectedPosition2) - getFirstVisiblePosition());
                            if (selectedView2 != null) {
                                setReferenceViewInSelectedRow(selectedView2);
                            }
                            amountToCenterScroll(130, nextSelectedPosition2);
                            if (isLastMovePos) {
                                int existItemPos = getRowStart(this.mFirstPosition);
                                if (existItemPos < this.mHeaderViewInfos.size()) {
                                    existItemPos = this.mHeaderViewInfos.size();
                                }
                                if (existItemPos < this.mFirstPosition) {
                                    existItemPos += getColumnNum();
                                }
                                View existItem = getChildAt((existItemPos + (nextSelectedPosition2 - getRowStart(nextSelectedPosition2))) - this.mFirstPosition);
                                if (!(existItem == null || !(existItem instanceof ItemListener) || (rectParms = ((ItemListener) existItem).getFocusParams()) == null)) {
                                    Rect rect = rectParms.focusRect();
                                    offsetDescendantRectToMyCoords(existItem, rect);
                                    offsetFocusRectLeftAndRight(rect.left, rect.right);
                                }
                            }
                            checkSelected(preSelectedView, preSelectedPos);
                            return true;
                        }
                    }
                    return true;
                case 21:
                    int nextSelectedPos = getSelectedItemPosition() - 1;
                    setSelectedPositionInt(nextSelectedPos);
                    checkSelectionChanged();
                    amountToCenterScroll(17, nextSelectedPos);
                    checkSelected(preSelectedView, preSelectedPos);
                    return true;
                case 22:
                    int nextSelectedPos2 = getSelectedItemPosition() + 1;
                    setSelectedPositionInt(nextSelectedPos2);
                    checkSelectionChanged();
                    amountToCenterScroll(66, nextSelectedPos2);
                    checkSelected(preSelectedView, preSelectedPos);
                    return true;
            }
        } else {
            if (selectedPos2 < getHeaderViewsCount() && keyCode == 20) {
                int selectedIndex = selectedPos2;
                View currSelectedView = getSelectedView();
                if (checkIsCanDown()) {
                    if (selectedIndex != getHeaderViewsCount() - 1) {
                        selectedPos = selectedPos2 + 1;
                        int nextSelectedIndex = selectedPos2;
                        if (nextSelectedIndex >= 0 && nextSelectedIndex < getHeaderViewsCount()) {
                            this.mNeedResetParam = true;
                            setAdapterSelection(nextSelectedIndex);
                            checkSelected(preSelectedView, preSelectedPos);
                            playSoundEffect(1);
                            return true;
                        }
                    } else if (!(currSelectedView == null || !(currSelectedView instanceof FocusRelativeLayout) || (focusedView = focusSearch(((FocusRelativeLayout) currSelectedView).getSelectedView(), 130)) == null)) {
                        int nextSelectedIndex2 = -1;
                        int i = 0;
                        while (true) {
                            if (i < getChildCount()) {
                                if (getChildAt(i).equals(focusedView)) {
                                    nextSelectedIndex2 = i + getFirstVisiblePosition();
                                } else {
                                    i++;
                                }
                            }
                        }
                        if (nextSelectedIndex2 >= 0) {
                            this.mNeedResetParam = true;
                            setAdapterSelection(nextSelectedIndex2);
                            checkSelected(preSelectedView, preSelectedPos);
                            playSoundEffect(1);
                            return true;
                        }
                    }
                }
            } else if (selectedPos2 <= getHeaderViewsCount() && keyCode == 19) {
                selectedPos = selectedPos2 - 1;
                int nextSelectedIndex3 = selectedPos2;
                if (nextSelectedIndex3 >= 0 && nextSelectedIndex3 < getHeaderViewsCount()) {
                    this.mNeedResetParam = true;
                    setAdapterSelection(nextSelectedIndex3);
                    checkSelected(preSelectedView, preSelectedPos);
                    playSoundEffect(1);
                    return true;
                }
            }
            int i2 = selectedPos;
        }
        this.mNeedResetParam = true;
        boolean ret = super.onKeyDown(keyCode, event);
        checkSelected(preSelectedView, preSelectedPos);
        return ret;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!hasFocus()) {
            ZpLogger.i(TAG, "requestFocus for touch event to onKeyUp");
            requestFocus();
        }
        if (getAdapter() == null || getChildCount() <= 0) {
            return false;
        }
        if (getSelectedItemPosition() < getHeaderViewsCount()) {
            View view = getSelectedView();
            if (view instanceof FocusRelativeLayout) {
                return ((FocusRelativeLayout) view).onKeyUp(keyCode, event);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        this.mGainFocus = gainFocus;
        if (this.mNeedAutoSearchFocused) {
            super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
            if (gainFocus) {
                this.mNeedAutoSearchFocused = false;
            }
        } else {
            if (getOnFocusChangeListener() != null) {
                getOnFocusChangeListener().onFocusChange(this, gainFocus);
            }
            if (gainFocus) {
                setSelection(getSelectedItemPosition());
            }
        }
        if (!gainFocus || getChildCount() <= 0) {
            performSelect(false);
        } else {
            if (getSelectedItemPosition() < getHeaderViewsCount()) {
                View view = getSelectedView();
                if (view instanceof FocusRelativeLayout) {
                    ((FocusRelativeLayout) view).onFocusChanged(true, direction, previouslyFocusedRect, this);
                }
            }
            this.mNeedResetParam = true;
            layoutResetParam();
            performSelect(true);
        }
        this.mIsAnimate = checkAnimate(direction);
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

    /* access modifiers changed from: protected */
    public int amountToCenterScroll(int direction, int nextSelectedPosition) {
        int nextSelectedCenter;
        int nextSelectedCenter2;
        boolean reset;
        int nextSelectedCenter3;
        boolean reset2;
        int verticalSpacing = getVerticalSpacing();
        int center = getHeight() / 2;
        int amountToScroll = 0;
        int distanceLeft = getFlipColumnFirstItemLeftMoveDistance(nextSelectedPosition);
        switch (direction) {
            case 17:
            case 66:
                int lastVisiblePos = getLastVisiblePosition();
                int firstVisiblePos = getFirstVisiblePosition();
                if (nextSelectedPosition <= lastVisiblePos) {
                    if (nextSelectedPosition >= firstVisiblePos) {
                        resetParam(getSelectedView(), getFlipItemLeftMoveDistance(nextSelectedPosition, 0));
                        break;
                    } else {
                        int visibleRowStart = getRowStart(firstVisiblePos);
                        if (visibleRowStart != firstVisiblePos) {
                            visibleRowStart = getRowStart(getColumnNum() + firstVisiblePos);
                        }
                        int selectedRowStart = getRowStart(nextSelectedPosition);
                        int columnDelta = nextSelectedPosition - selectedRowStart;
                        View visibleView = getChildAt((visibleRowStart + columnDelta) - firstVisiblePos);
                        resetParam(visibleView, (-((visibleView.getHeight() + verticalSpacing) * ((visibleRowStart - selectedRowStart) / getColumnNum()))) + getFlipItemLeftMoveDistance(visibleRowStart + columnDelta, 0));
                        break;
                    }
                } else {
                    int visibleRowStart2 = getRowStart(lastVisiblePos);
                    if (lastVisiblePos - visibleRowStart2 < getColumnNum() - 1) {
                        visibleRowStart2 = getRowStart(lastVisiblePos - getColumnNum());
                    }
                    int selectedRowStart2 = getRowStart(nextSelectedPosition);
                    int columnDelta2 = nextSelectedPosition - selectedRowStart2;
                    View visibleView2 = getChildAt((visibleRowStart2 + columnDelta2) - firstVisiblePos);
                    resetParam(visibleView2, ((visibleView2.getHeight() + verticalSpacing) * ((selectedRowStart2 - visibleRowStart2) / getColumnNum())) + getFlipItemLeftMoveDistance(visibleRowStart2 + columnDelta2, 0));
                    break;
                }
            case 33:
                View nextSelctedView = getChildAt(nextSelectedPosition - this.mFirstPosition);
                View firstView = getChildAt(0);
                int firstTop = firstView.getTop();
                if (firstView instanceof GridView.GridViewHeaderViewExpandDistance) {
                    firstTop += ((GridView.GridViewHeaderViewExpandDistance) firstView).getUpExpandDistance();
                }
                boolean notResetParam = false;
                if (nextSelctedView == null) {
                    nextSelctedView = getChildAt(0);
                    int oldRowStart = getRowStart(getFirstVisiblePosition());
                    int rowStart = getRowStart(nextSelectedPosition);
                    int headerCount = this.mHeaderViewInfos.size();
                    if (headerCount <= 0) {
                        nextSelectedCenter2 = (nextSelctedView.getTop() + (nextSelctedView.getHeight() / 2)) - ((nextSelctedView.getHeight() + verticalSpacing) * ((oldRowStart - rowStart) / getColumnNum()));
                    } else if (rowStart < headerCount) {
                        nextSelectedCenter2 = nextSelctedView.getTop() - ((nextSelctedView.getHeight() + verticalSpacing) * ((oldRowStart - headerCount) / getColumnNum()));
                        for (int i = rowStart; i < headerCount; i++) {
                            View headerView = ((AbsBaseListView.FixedViewInfo) this.mHeaderViewInfos.get(i)).view;
                            int headerViewHeight = headerView.getHeight();
                            if (headerView instanceof GridView.GridViewHeaderViewExpandDistance) {
                                headerViewHeight = (headerViewHeight - ((GridView.GridViewHeaderViewExpandDistance) headerView).getUpExpandDistance()) - ((GridView.GridViewHeaderViewExpandDistance) headerView).getDownExpandDistance();
                            }
                            nextSelectedCenter2 -= (headerViewHeight / 2) + verticalSpacing;
                        }
                        View headerView2 = ((AbsBaseListView.FixedViewInfo) this.mHeaderViewInfos.get(0)).view;
                        if (headerView2 != null && (headerView2 instanceof ItemListener)) {
                            ItemListener item = (ItemListener) headerView2;
                            if (this.mFocusRectparams == null) {
                                this.mFocusRectparams = new FocusRectParams();
                            }
                            this.mFocusRectparams.set(item.getFocusParams());
                        }
                        int leftOffset = getHeaderViewLeft(0);
                        int topOffset = this.mListPadding.top;
                        if (headerView2 instanceof GridView.GridViewHeaderViewExpandDistance) {
                            topOffset -= ((GridView.GridViewHeaderViewExpandDistance) headerView2).getUpExpandDistance();
                        }
                        int secondIndex = getHeaderViewSecondIndex(headerView2);
                        if (nextSelectedPosition >= getFirstVisiblePosition() && nextSelectedPosition <= getLastVisiblePosition()) {
                            topOffset += distanceLeft - getFlipItemLeftMoveDistance(nextSelectedPosition, secondIndex);
                        }
                        offsetFocusRect(leftOffset, leftOffset, topOffset, topOffset);
                        notResetParam = true;
                    } else {
                        nextSelectedCenter2 = (nextSelctedView.getTop() + (nextSelctedView.getHeight() / 2)) - ((nextSelctedView.getHeight() + verticalSpacing) * ((oldRowStart - rowStart) / getColumnNum()));
                    }
                    reset = false;
                } else {
                    if (nextSelctedView instanceof GridView.GridViewHeaderViewExpandDistance) {
                        int upDistance = ((GridView.GridViewHeaderViewExpandDistance) nextSelctedView).getUpExpandDistance();
                        nextSelectedCenter = nextSelctedView.getTop() + upDistance + (((nextSelctedView.getHeight() - upDistance) - ((GridView.GridViewHeaderViewExpandDistance) nextSelctedView).getDownExpandDistance()) / 2);
                    } else {
                        nextSelectedCenter = nextSelctedView.getTop() + (nextSelctedView.getHeight() / 2);
                    }
                    reset = true;
                }
                int finalNextSelectedCenter = nextSelectedCenter2 + distanceLeft;
                if (finalNextSelectedCenter < center) {
                    int amountToScroll2 = center - finalNextSelectedCenter;
                    int maxDiff = this.mListPadding.top - (firstTop - getTopLeftDistance(getFirstVisiblePosition()));
                    if (maxDiff < 0) {
                        maxDiff = 0;
                    }
                    int maxDiff2 = maxDiff - getFlipColumnFirstItemLeftMoveDistance(getFirstVisiblePosition());
                    if (maxDiff2 < 0) {
                        maxDiff2 = 0;
                    }
                    if (amountToScroll2 > maxDiff2) {
                        amountToScroll2 = maxDiff2;
                    }
                    if (reset) {
                        int headerCount2 = this.mHeaderViewInfos.size();
                        if (headerCount2 <= 0) {
                            resetParam(nextSelctedView, 0);
                            offsetFocusRectTopAndBottom(distanceLeft, distanceLeft);
                        } else if (nextSelectedPosition < headerCount2) {
                            int secondIndex2 = getHeaderViewSecondIndex(nextSelctedView);
                            if (secondIndex2 >= 0) {
                                resetParam(nextSelctedView, getFlipItemLeftMoveDistance(nextSelectedPosition, secondIndex2));
                            }
                        } else {
                            resetParam(nextSelctedView, 0);
                            offsetFocusRectTopAndBottom(distanceLeft, distanceLeft);
                        }
                    }
                    if (amountToScroll > 0) {
                        if (!notResetParam) {
                            if (reset) {
                                offsetFocusRectTopAndBottom(amountToScroll, amountToScroll);
                            } else {
                                offsetFocusRectTopAndBottom(-((nextSelctedView.getHeight() + verticalSpacing) - amountToScroll), -((nextSelctedView.getHeight() + verticalSpacing) - amountToScroll));
                            }
                        }
                        startRealScroll(amountToScroll);
                        this.mIsAnimate = true;
                    } else {
                        if (!reset && !notResetParam) {
                            offsetFocusRectTopAndBottom(-(nextSelctedView.getHeight() + verticalSpacing), -(nextSelctedView.getHeight() + verticalSpacing));
                        }
                        this.mIsAnimate = true;
                    }
                } else {
                    resetParam(getSelectedView(), 0);
                    this.mIsAnimate = true;
                }
                this.mPreFocusRect.set(this.mFocusRectparams.focusRect());
                return amountToScroll;
            case 130:
                View nextSelctedView2 = getChildAt(nextSelectedPosition - this.mFirstPosition);
                int lastBottom = getChildAt(getChildCount() - 1).getBottom();
                if (nextSelctedView2 == null) {
                    nextSelctedView2 = getChildAt(getChildCount() - 1);
                    nextSelectedCenter3 = (nextSelctedView2.getBottom() - (nextSelctedView2.getHeight() / 2)) + ((nextSelctedView2.getHeight() + verticalSpacing) * ((getRowStart(nextSelectedPosition) - getRowStart(getLastVisiblePosition())) / getColumnNum()));
                    reset2 = false;
                } else {
                    nextSelectedCenter3 = nextSelctedView2.getBottom() - (nextSelctedView2.getHeight() / 2);
                    reset2 = true;
                }
                int finalNextSelectedCenter2 = nextSelectedCenter3 + distanceLeft;
                if (finalNextSelectedCenter2 > center) {
                    amountToScroll = finalNextSelectedCenter2 - center;
                    int maxDiff3 = ((lastBottom + getBottomLeftDistance(getLastVisiblePosition())) + this.mListPadding.bottom) - getHeight();
                    if (maxDiff3 < 0) {
                        maxDiff3 = 0;
                    }
                    int maxDiff4 = maxDiff3 + getFlipColumnFirstItemLeftMoveDistance(getLastVisiblePosition());
                    if (amountToScroll > maxDiff4) {
                        amountToScroll = maxDiff4;
                    }
                    if (reset2) {
                        resetParam(nextSelctedView2, 0);
                        offsetFocusRectTopAndBottom(distanceLeft, distanceLeft);
                    }
                    if (amountToScroll > 0) {
                        if (reset2) {
                            offsetFocusRectTopAndBottom(-amountToScroll, -amountToScroll);
                        } else {
                            offsetFocusRectTopAndBottom((nextSelctedView2.getHeight() + verticalSpacing) - amountToScroll, (nextSelctedView2.getHeight() + verticalSpacing) - amountToScroll);
                        }
                        startRealScroll(-amountToScroll);
                        this.mIsAnimate = true;
                    } else {
                        if (!reset2) {
                            offsetFocusRectTopAndBottom(nextSelctedView2.getHeight() + verticalSpacing, nextSelctedView2.getHeight() + verticalSpacing);
                        }
                        this.mIsAnimate = true;
                    }
                } else {
                    resetParam(getSelectedView(), 0);
                    this.mIsAnimate = true;
                }
                return amountToScroll;
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void adjustForBottomFadingEdge(View childInSelectedRow, int topSelectionPixel, int bottomSelectionPixel) {
        int tempTopSelectionPixel;
        int tempBottomSelectionPixel;
        int top = childInSelectedRow.getTop();
        if (childInSelectedRow instanceof GridView.GridViewHeaderViewExpandDistance) {
            top += ((GridView.GridViewHeaderViewExpandDistance) childInSelectedRow).getUpExpandDistance();
        }
        int bottom = childInSelectedRow.getBottom();
        if (childInSelectedRow instanceof GridView.GridViewHeaderViewExpandDistance) {
            bottom -= ((GridView.GridViewHeaderViewExpandDistance) childInSelectedRow).getDownExpandDistance();
        }
        if (this.mCenterFocus) {
            int center = getHeight() / 2;
            int childHeight = childInSelectedRow.getHeight();
            if (childInSelectedRow instanceof GridView.GridViewHeaderViewExpandDistance) {
                childHeight = (childHeight - ((GridView.GridViewHeaderViewExpandDistance) childInSelectedRow).getUpExpandDistance()) - ((GridView.GridViewHeaderViewExpandDistance) childInSelectedRow).getDownExpandDistance();
            }
            tempTopSelectionPixel = center - (childHeight / 2);
            tempBottomSelectionPixel = center + (childHeight / 2);
        } else {
            tempTopSelectionPixel = topSelectionPixel;
            tempBottomSelectionPixel = bottomSelectionPixel;
        }
        if (bottom > tempBottomSelectionPixel) {
            int offset = Math.min(top - tempTopSelectionPixel, bottom - tempBottomSelectionPixel);
            if (this.mCenterFocus) {
                int maxDiff = ((bottom + getBottomLeftDistance(getSelectedItemPosition())) + this.mListPadding.bottom) - getHeight();
                if (maxDiff < 0) {
                    maxDiff = 0;
                }
                if (offset > maxDiff) {
                    offset = maxDiff;
                }
            }
            offsetChildrenTopAndBottom(-offset);
        }
    }

    /* access modifiers changed from: protected */
    public void adjustForTopFadingEdge(View childInSelectedRow, int topSelectionPixel, int bottomSelectionPixel) {
        int tempTopSelectionPixel;
        int tempBottomSelectionPixel;
        int top = childInSelectedRow.getTop();
        if (childInSelectedRow instanceof GridView.GridViewHeaderViewExpandDistance) {
            top += ((GridView.GridViewHeaderViewExpandDistance) childInSelectedRow).getUpExpandDistance();
        }
        int bottom = childInSelectedRow.getBottom();
        if (childInSelectedRow instanceof GridView.GridViewHeaderViewExpandDistance) {
            bottom -= ((GridView.GridViewHeaderViewExpandDistance) childInSelectedRow).getDownExpandDistance();
        }
        if (this.mCenterFocus) {
            int center = getHeight() / 2;
            int childHeight = childInSelectedRow.getHeight();
            if (childInSelectedRow instanceof GridView.GridViewHeaderViewExpandDistance) {
                childHeight = (childHeight - ((GridView.GridViewHeaderViewExpandDistance) childInSelectedRow).getUpExpandDistance()) - ((GridView.GridViewHeaderViewExpandDistance) childInSelectedRow).getDownExpandDistance();
            }
            tempTopSelectionPixel = center - (childHeight / 2);
            tempBottomSelectionPixel = center + (childHeight / 2);
        } else {
            tempTopSelectionPixel = topSelectionPixel;
            tempBottomSelectionPixel = bottomSelectionPixel;
        }
        if (top < tempTopSelectionPixel) {
            int offset = Math.min(tempTopSelectionPixel - top, tempBottomSelectionPixel - bottom);
            if (this.mCenterFocus) {
                int maxDiff = this.mListPadding.top - (top - getTopLeftDistance(getSelectedItemPosition()));
                if (maxDiff < 0) {
                    maxDiff = 0;
                }
                if (offset > maxDiff) {
                    offset = maxDiff;
                }
            }
            offsetChildrenTopAndBottom(offset);
        }
    }

    /* access modifiers changed from: protected */
    public void layoutChildren() {
        if (!isFlipFinished()) {
            ZpLogger.i(TAG, "layoutChildren flip is running can not layout");
            return;
        }
        super.layoutChildren();
        this.mClipFocusRect.set(0, 0, getWidth(), getHeight());
    }

    /* access modifiers changed from: protected */
    public void onLayoutChildrenDone() {
        boolean isFirst = false;
        if (this.mIsFirstLayout) {
            this.mIsFirstLayout = false;
            isFirst = true;
        }
        if (this.mOnFocusFlipGridViewListener != null) {
            this.mOnFocusFlipGridViewListener.onLayoutDone(isFirst);
        }
        resetFocusParam();
    }

    public void offsetChildrenTopAndBottom(int offset) {
        super.offsetChildrenTopAndBottom(offset);
    }

    /* access modifiers changed from: protected */
    public void onFlipItemRunnableRunning(float moveRatio, View itemView, int index) {
        if (this.mAnimAlpha && !this.mFirstAnimDone && itemView != null) {
            this.mAnimAlphaValue = (int) (255.0f * moveRatio);
            setAlpha(moveRatio);
        }
        super.onFlipItemRunnableRunning(moveRatio, itemView, index);
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        if (this.mAnimAlpha) {
            if (this.mAlphaRectF == null) {
                this.mAlphaRectF = new RectF(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
            }
            super.dispatchDraw(canvas);
            return;
        }
        super.dispatchDraw(canvas);
    }

    /* access modifiers changed from: protected */
    public void onFlipItemRunnableFinished() {
        if (!this.mFirstAnimDone) {
            setFocusable(true);
            this.mFirstAnimDone = true;
        }
        super.onFlipItemRunnableFinished();
    }

    public Params getParams() {
        if (this.mParams != null) {
            return this.mParams;
        }
        throw new IllegalArgumentException("The params is null, you must call setScaleParams before it's running");
    }

    public void getFocusedRect(Rect r) {
        if (hasFocus()) {
            super.getFocusedRect(r);
        } else {
            getDrawingRect(r);
        }
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (hasFocus()) {
            super.addFocusables(views, direction, focusableMode);
        } else if (views != null && isFocusable()) {
            if ((focusableMode & 1) != 1 || !isInTouchMode() || isFocusableInTouchMode()) {
                views.add(this);
            }
        }
    }

    public void forceResetFocusParams(FocusPositionManager positionManager) {
        if (positionManager != null) {
            this.forceResetFocusParams = true;
            positionManager.forceDrawFocus();
        }
    }

    public FocusRectParams getFocusParams() {
        if (getSelectedView() != null) {
            if (this.mFocusRectparams == null || isScrolling()) {
                resetFocusParam();
            } else if (this.forceResetFocusParams) {
                this.forceResetFocusParams = false;
                resetFocusParam();
            }
            return this.mFocusRectparams;
        }
        Rect r = new Rect();
        getFocusedRect(r);
        this.mFocusRectparams.set(r, 0.5f, 0.5f);
        return this.mFocusRectparams;
    }

    public boolean canDraw() {
        return getSelectedView() != null;
    }

    public boolean isAnimate() {
        return this.mIsAnimate;
    }

    public ItemListener getItem() {
        View view = getSelectedView();
        if (view instanceof FocusRelativeLayout) {
            return ((FocusRelativeLayout) view).getItem();
        }
        View v = getSelectedView();
        if (v == null) {
            ZpLogger.e(TAG, "getItem: getSelectedView is null! this:" + toString());
        }
        return (ItemListener) v;
    }

    public boolean isScrolling() {
        getClass();
        return isFliping();
    }

    public boolean preOnKeyDown(int keyCode, KeyEvent event) {
        if (getAdapter() == null || getChildCount() <= 0) {
            return false;
        }
        int selectedPos = getSelectedItemPosition();
        if (selectedPos < getHeaderViewsCount()) {
            View view = getSelectedView();
            if (view instanceof FocusRelativeLayout) {
                if (!isFlipFinished()) {
                    return false;
                }
                if (((FocusRelativeLayout) view).preOnKeyDown(keyCode, event)) {
                    return true;
                }
            }
        }
        switch (keyCode) {
            case 19:
            case 92:
            case SecExceptionCode.SEC_ERROR_INIT_NO_DATA_FILE:
                int headerCount = this.mHeaderViewInfos.size();
                if (headerCount > 0) {
                    if (selectedPos <= 0) {
                        if (this.mOnFocusFlipGridViewListener != null) {
                            this.mOnFocusFlipGridViewListener.onReachGridViewTop();
                        }
                        return false;
                    } else if (getRowStart(selectedPos) != headerCount || ((AbsBaseListView.FixedViewInfo) this.mHeaderViewInfos.get(headerCount - 1)).view.isFocusable()) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (getSelectedItemPosition() >= getColumnNum()) {
                    return true;
                } else {
                    if (this.mOnFocusFlipGridViewListener != null) {
                        this.mOnFocusFlipGridViewListener.onReachGridViewTop();
                    }
                    return false;
                }
            case 20:
            case 93:
            case 123:
                return checkIsCanDown() && getSelectedItemPosition() < this.mItemCount + -1;
            case 21:
                int selected = getSelectedItemPosition();
                return selected >= getHeaderViewsCount() && (selected - getHeaderViewsCount()) % getColumnNum() != 0;
            case 22:
                int selected2 = getSelectedItemPosition();
                return selected2 >= getHeaderViewsCount() && selected2 < this.mItemCount + -1 && ((selected2 - getHeaderViewsCount()) + 1) % getColumnNum() != 0;
            case 23:
            case 66:
                return true;
            default:
                return false;
        }
    }

    public boolean hasFocus() {
        return super.hasFocus() || this.mGainFocus;
    }

    public boolean hasDeepFocus() {
        return this.mDeepFocus;
    }

    public boolean canDeep() {
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
        return getWidth();
    }

    public int getItemHeight() {
        return getHeight();
    }

    public Rect getManualPadding() {
        return null;
    }

    public void onItemSelected(boolean selected) {
        performSelect(selected);
    }

    public void onItemClick() {
        ZpLogger.v(TAG, "FocusFlipGridView.onItemClick.getSelectedView = " + getSelectedView());
        if (getSelectedView() != null) {
            performItemClick(getSelectedView(), getSelectedItemPosition(), 0);
        }
    }

    public void setOnFocusFlipGridViewListener(OnFocusFlipGridViewListener listener) {
        this.mOnFocusFlipGridViewListener = listener;
    }

    public void stopOutAnimation() {
        ZpLogger.i(TAG, "stopOutAnimation");
        this.mOutAnimationRunnable.stop();
    }

    public void startOutAnimation() {
        this.mOutAnimationRunnable.start();
    }

    public void startInAnimation() {
        if (this.mFirstAnimDone) {
            this.mFirstAnimDone = false;
            setFocusable(false);
            int count = getChildCount();
            int delta = getHeight() / 2;
            if (this.mAnimAlpha) {
                setAlpha(0.0f);
                this.mAnimAlphaValue = 0;
            }
            for (int i = 0; i < count; i++) {
                getChildAt(i).offsetTopAndBottom(delta);
            }
            startFlip(-delta);
        }
    }

    private void init() {
        this.mOutAnimationRunnable = new OutAnimationRunnable();
    }

    private int getHeaderViewLeft(int index) {
        if (index >= this.mHeaderViewInfos.size()) {
            return 0;
        }
        View headerView = ((AbsBaseListView.FixedViewInfo) this.mHeaderViewInfos.get(index)).view;
        int childLeft = this.mListPadding.left;
        int width = (getWidth() - this.mListPadding.left) - this.mListPadding.right;
        switch (1) {
            case 1:
                return childLeft + ((width - headerView.getWidth()) / 2);
            case 5:
                return childLeft + (width - headerView.getWidth());
            default:
                return childLeft;
        }
    }

    private int getRowEnd(int position) {
        int rowEnd = position;
        int headerCount = getHeaderViewsCount();
        if (position < headerCount) {
            return rowEnd;
        }
        if (position >= this.mItemCount - getFooterViewsCount()) {
            return position;
        }
        int newPosition = position - headerCount;
        int rowEnd2 = newPosition + (getColumnNum() - ((newPosition % getColumnNum()) + 1)) + headerCount;
        if (rowEnd2 >= this.mItemCount) {
            return this.mItemCount - 1;
        }
        return rowEnd2;
    }

    private void checkSelected(View preSelectedView, int preSelectedPos) {
        View currSelectedView = getSelectedView();
        int currSelectedPos = getSelectedItemPosition();
        if (preSelectedPos != currSelectedPos) {
            if (preSelectedPos >= 0 && preSelectedView != null) {
                preSelectedView.setSelected(false);
                if (this.mItemSelectedListener != null) {
                    this.mItemSelectedListener.onItemSelected(preSelectedView, preSelectedPos, false, this);
                }
            }
            if (currSelectedView != null) {
                currSelectedView.setSelected(true);
            }
            if (this.mItemSelectedListener != null) {
                this.mItemSelectedListener.onItemSelected(currSelectedView, currSelectedPos, true, this);
            }
        }
    }

    private void performSelect(boolean select) {
        View selectedView = getSelectedView();
        if (selectedView != null) {
            selectedView.setSelected(select);
            if (this.mItemSelectedListener != null) {
                this.mItemSelectedListener.onItemSelected(selectedView, getSelectedItemPosition(), select, this);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void resetFocusParam() {
        this.mNeedResetParam = true;
        layoutResetParam();
    }

    private void layoutResetParam() {
        if (this.mNeedResetParam) {
            this.mNeedResetParam = false;
            int scrollOffset = this.mScrollOffset;
            int selectedPos = getSelectedItemPosition();
            if (selectedPos < getHeaderViewsCount()) {
                View view = getSelectedView();
                if ((view instanceof FocusRelativeLayout) && (view instanceof FlipGridView.FlipGridViewHeaderOrFooterInterface)) {
                    FocusRelativeLayout headerView = (FocusRelativeLayout) view;
                    if (headerView.isNeedFocusItem() && (hasFocus() || hasDeepFocus())) {
                        headerView.onFocusChanged(true, this.mOnKeyDirection, this.mPreFocusRect, this);
                    }
                    FlipGridView.FlipGridViewHeaderOrFooterInterface headerInt = (FlipGridView.FlipGridViewHeaderOrFooterInterface) view;
                    int validChildCount = headerInt.getHorCount() * headerInt.getVerticalCount();
                    int secondIndex = -1;
                    int i = 0;
                    while (true) {
                        if (i < validChildCount) {
                            View childView = headerInt.getView(i);
                            if (childView != null && childView.equals(headerView.getSelectedView())) {
                                secondIndex = i;
                                break;
                            }
                            i++;
                        } else {
                            break;
                        }
                    }
                    if (secondIndex >= 0) {
                        int leftMove = getFlipItemLeftMoveDistance(selectedPos, secondIndex);
                        if (scrollOffset == 0) {
                            scrollOffset = leftMove;
                        }
                        headerView.reset();
                    }
                }
            } else {
                int leftMove2 = getFlipItemLeftMoveDistance(selectedPos, 0);
                if (scrollOffset == 0) {
                    scrollOffset = leftMove2;
                }
            }
            resetParam(getSelectedView(), scrollOffset);
        }
    }

    private void resetParam(View view, int offset) {
        if (view != null && (view instanceof ItemListener)) {
            ItemListener item = (ItemListener) view;
            if (this.mFocusRectparams == null) {
                this.mFocusRectparams = new FocusRectParams();
            }
            this.mFocusRectparams.set(item.getFocusParams());
        } else if (view == null || !(view instanceof FocusListener)) {
            ZpLogger.w(TAG, "resetParam error view=" + view + " mItemCount=" + this.mItemCount + " mFirstIndex=" + this.mFirstPosition + " mSelectedIndex=" + this.mSelectedPosition);
            return;
        } else {
            FocusListener item2 = (FocusListener) view;
            if (this.mFocusRectparams == null) {
                this.mFocusRectparams = new FocusRectParams();
            }
            this.mFocusRectparams.set(item2.getFocusParams());
        }
        if (this.mFocusRectparams != null) {
            if (this.mFocusRectparams.focusRect() != null) {
                offsetFocusRectTopAndBottom(offset, offset);
            }
            offsetDescendantRectToMyCoords(view, this.mFocusRectparams.focusRect());
            this.mPreFocusRect.set(this.mFocusRectparams.focusRect());
        }
    }

    private int getTopLeftDistance(int itemIndex) {
        View itemView = getChildAt(itemIndex - getFirstVisiblePosition());
        if (itemView == null) {
            return Integer.MAX_VALUE;
        }
        int bottomDistance = 0;
        int columnCount = getColumnNum();
        int headerCount = this.mHeaderViewInfos.size();
        int footerCount = this.mFooterViewInfos.size();
        if (itemIndex < headerCount) {
            for (int i = itemIndex - 1; i >= 0; i--) {
                View headerView = ((AbsBaseListView.FixedViewInfo) this.mHeaderViewInfos.get(i)).view;
                bottomDistance += headerView.getHeight() + getVerticalSpacing();
                if (headerView instanceof GridView.GridViewHeaderViewExpandDistance) {
                    bottomDistance = (bottomDistance - ((GridView.GridViewHeaderViewExpandDistance) headerView).getUpExpandDistance()) - ((GridView.GridViewHeaderViewExpandDistance) headerView).getDownExpandDistance();
                }
            }
            return bottomDistance;
        } else if (itemIndex < headerCount || itemIndex >= this.mItemCount - footerCount) {
            int footerStart = itemIndex - (this.mItemCount - footerCount);
            if (footerStart > 0) {
                for (int i2 = footerStart - 1; i2 >= 0; i2--) {
                    View footerView = ((AbsBaseListView.FixedViewInfo) this.mFooterViewInfos.get(i2)).view;
                    int bottomDistance2 = bottomDistance + footerView.getHeight() + getVerticalSpacing();
                    if (footerView instanceof GridView.GridViewHeaderViewExpandDistance) {
                        bottomDistance2 = (bottomDistance2 - ((GridView.GridViewHeaderViewExpandDistance) footerView).getUpExpandDistance()) - ((GridView.GridViewHeaderViewExpandDistance) footerView).getDownExpandDistance();
                    }
                }
            }
            int preColumnIndex = (this.mItemCount - footerCount) - 1;
            int firstAdapterIndex = headerCount;
            if (preColumnIndex >= firstAdapterIndex) {
                int bottomColumnCount = ((preColumnIndex - firstAdapterIndex) + 1) / columnCount;
                if (((preColumnIndex - firstAdapterIndex) + 1) % columnCount > 0) {
                    bottomColumnCount++;
                }
                if (bottomColumnCount > 0) {
                    View adatperView = getChildAt(preColumnIndex - this.mFirstPosition);
                    if (adatperView == null) {
                        return Integer.MAX_VALUE;
                    }
                    bottomDistance += (adatperView.getHeight() + getVerticalSpacing()) * bottomColumnCount;
                }
            }
            for (int i3 = headerCount - 1; i3 >= 0; i3--) {
                View headerView2 = ((AbsBaseListView.FixedViewInfo) this.mHeaderViewInfos.get(i3)).view;
                int bottomDistance3 = bottomDistance + headerView2.getHeight() + getVerticalSpacing();
                if (headerView2 instanceof GridView.GridViewHeaderViewExpandDistance) {
                    bottomDistance3 = (bottomDistance3 - ((GridView.GridViewHeaderViewExpandDistance) headerView2).getUpExpandDistance()) - ((GridView.GridViewHeaderViewExpandDistance) headerView2).getDownExpandDistance();
                }
            }
            return bottomDistance;
        } else {
            int preColumnIndex2 = (itemIndex - ((itemIndex - this.mHeaderViewInfos.size()) % columnCount)) - 1;
            int firstAdapterIndex2 = this.mHeaderViewInfos.size();
            if (preColumnIndex2 >= firstAdapterIndex2) {
                int bottomColumnCount2 = ((preColumnIndex2 - firstAdapterIndex2) + 1) / columnCount;
                if (((preColumnIndex2 - firstAdapterIndex2) + 1) % columnCount > 0) {
                    bottomColumnCount2++;
                }
                if (bottomColumnCount2 > 0) {
                    bottomDistance = 0 + ((itemView.getHeight() + getVerticalSpacing()) * bottomColumnCount2);
                }
            }
            for (int i4 = headerCount - 1; i4 >= 0; i4--) {
                View headerView3 = ((AbsBaseListView.FixedViewInfo) this.mHeaderViewInfos.get(i4)).view;
                int bottomDistance4 = bottomDistance + headerView3.getHeight() + getVerticalSpacing();
                if (headerView3 instanceof GridView.GridViewHeaderViewExpandDistance) {
                    bottomDistance4 = (bottomDistance4 - ((GridView.GridViewHeaderViewExpandDistance) headerView3).getUpExpandDistance()) - ((GridView.GridViewHeaderViewExpandDistance) headerView3).getDownExpandDistance();
                }
            }
            return bottomDistance;
        }
    }

    private int getBottomLeftDistance(int itemIndex) {
        View itemView = getChildAt(itemIndex - getFirstVisiblePosition());
        if (itemView == null) {
            return Integer.MAX_VALUE;
        }
        int bottomDistance = 0;
        int columnCount = getColumnNum();
        int headerCount = this.mHeaderViewInfos.size();
        int footerCount = this.mFooterViewInfos.size();
        if (itemIndex < headerCount) {
            for (int i = itemIndex + 1; i < headerCount; i++) {
                View headerView = ((AbsBaseListView.FixedViewInfo) this.mHeaderViewInfos.get(i)).view;
                bottomDistance += headerView.getHeight() + getVerticalSpacing();
                if (headerView instanceof GridView.GridViewHeaderViewExpandDistance) {
                    bottomDistance = (bottomDistance - ((GridView.GridViewHeaderViewExpandDistance) headerView).getUpExpandDistance()) - ((GridView.GridViewHeaderViewExpandDistance) headerView).getDownExpandDistance();
                }
            }
            int nextColumnIndex = headerCount;
            int lastAdapterIndex = (this.mItemCount - this.mFooterViewInfos.size()) - 1;
            if (nextColumnIndex <= lastAdapterIndex) {
                int bottomColumnCount = ((lastAdapterIndex - nextColumnIndex) + 1) / columnCount;
                if (((lastAdapterIndex - nextColumnIndex) + 1) % columnCount > 0) {
                    bottomColumnCount++;
                }
                if (bottomColumnCount > 0) {
                    View adatperView = getChildAt(nextColumnIndex - this.mFirstPosition);
                    if (adatperView == null) {
                        return Integer.MAX_VALUE;
                    }
                    bottomDistance += (adatperView.getHeight() + getVerticalSpacing()) * bottomColumnCount;
                }
            }
            for (int i2 = 0; i2 < footerCount; i2++) {
                View footerView = ((AbsBaseListView.FixedViewInfo) this.mFooterViewInfos.get(i2)).view;
                int bottomDistance2 = bottomDistance + footerView.getHeight() + getVerticalSpacing();
                if (footerView instanceof GridView.GridViewHeaderViewExpandDistance) {
                    bottomDistance2 = (bottomDistance2 - ((GridView.GridViewHeaderViewExpandDistance) footerView).getUpExpandDistance()) - ((GridView.GridViewHeaderViewExpandDistance) footerView).getDownExpandDistance();
                }
            }
            return bottomDistance;
        } else if (itemIndex < headerCount || itemIndex >= this.mItemCount - footerCount) {
            int footerStart = footerCount - ((this.mItemCount - 1) - itemIndex);
            if (footerStart <= 0) {
                return 0;
            }
            for (int i3 = footerStart; i3 < footerCount; i3++) {
                View footerView2 = ((AbsBaseListView.FixedViewInfo) this.mFooterViewInfos.get(i3)).view;
                int bottomDistance3 = bottomDistance + footerView2.getHeight() + getVerticalSpacing();
                if (footerView2 instanceof GridView.GridViewHeaderViewExpandDistance) {
                    bottomDistance3 = (bottomDistance3 - ((GridView.GridViewHeaderViewExpandDistance) footerView2).getUpExpandDistance()) - ((GridView.GridViewHeaderViewExpandDistance) footerView2).getDownExpandDistance();
                }
            }
            return bottomDistance;
        } else {
            int nextColumnIndex2 = (itemIndex + columnCount) - ((itemIndex - this.mHeaderViewInfos.size()) % columnCount);
            int lastAdapterIndex2 = (this.mItemCount - this.mFooterViewInfos.size()) - 1;
            if (nextColumnIndex2 <= lastAdapterIndex2) {
                int bottomColumnCount2 = ((lastAdapterIndex2 - nextColumnIndex2) + 1) / columnCount;
                if (((lastAdapterIndex2 - nextColumnIndex2) + 1) % columnCount > 0) {
                    bottomColumnCount2++;
                }
                if (bottomColumnCount2 > 0) {
                    bottomDistance = 0 + ((itemView.getHeight() + getVerticalSpacing()) * bottomColumnCount2);
                }
            }
            for (int i4 = 0; i4 < footerCount; i4++) {
                View footerView3 = ((AbsBaseListView.FixedViewInfo) this.mFooterViewInfos.get(i4)).view;
                int bottomDistance4 = bottomDistance + footerView3.getHeight() + getVerticalSpacing();
                if (footerView3 instanceof GridView.GridViewHeaderViewExpandDistance) {
                    bottomDistance4 = (bottomDistance4 - ((GridView.GridViewHeaderViewExpandDistance) footerView3).getUpExpandDistance()) - ((GridView.GridViewHeaderViewExpandDistance) footerView3).getDownExpandDistance();
                }
            }
            return bottomDistance;
        }
    }

    private boolean checkIsCanDown() {
        int selectedIndex = getSelectedItemPosition();
        int headerCount = this.mHeaderViewInfos.size();
        int footerCount = this.mFooterViewInfos.size();
        if (selectedIndex < headerCount) {
            if (selectedIndex < this.mItemCount - 1) {
                return true;
            }
            if (this.mOnFocusFlipGridViewListener != null) {
                this.mOnFocusFlipGridViewListener.onReachGridViewBottom();
            }
            return false;
        } else if (footerCount <= 0) {
            if (getRowStart(selectedIndex) + getColumnNum() < this.mItemCount) {
                return true;
            }
            if (this.mOnFocusFlipGridViewListener != null) {
                this.mOnFocusFlipGridViewListener.onReachGridViewBottom();
            }
            return false;
        } else if (selectedIndex < this.mItemCount - 1) {
            return true;
        } else {
            if (this.mOnFocusFlipGridViewListener != null) {
                this.mOnFocusFlipGridViewListener.onReachGridViewBottom();
            }
            return false;
        }
    }

    private int getHeaderViewSecondIndex(View headerView) {
        View selectedChildView;
        if (!(headerView instanceof FocusRelativeLayout) || (selectedChildView = ((FocusRelativeLayout) headerView).getSelectedView()) == null || !(headerView instanceof FlipGridView.FlipGridViewHeaderOrFooterInterface)) {
            return -1;
        }
        return ((FlipGridView.FlipGridViewHeaderOrFooterInterface) headerView).getViewIndex(selectedChildView);
    }

    private void setAdapterSelection(int index) {
        super.setSelection(index);
    }

    public void setOutAnimFrameCount(int outAnimFrameCount) {
        if (this.mOutAnimationRunnable != null) {
            this.mOutAnimationRunnable.setOutAnimFrameCount(outAnimFrameCount);
        }
    }

    private class OutAnimationRunnable implements Runnable {
        private int mCurrFrameCount;
        private boolean mIsFinished;
        private int outAnimFrameCount;

        private OutAnimationRunnable() {
            this.outAnimFrameCount = 15;
            this.mIsFinished = true;
        }

        public void setOutAnimFrameCount(int outAnimFrameCount2) {
            this.outAnimFrameCount = outAnimFrameCount2;
        }

        public void start() {
            if (this.mIsFinished) {
                FocusFlipGridView.this.setFocusable(false);
                this.mIsFinished = false;
                this.mCurrFrameCount = 0;
                FocusFlipGridView.this.post(this);
            }
        }

        public void stop() {
            if (!this.mIsFinished) {
                ZpLogger.i(FocusFlipGridView.TAG, "OutAnimationRunnable stop");
                FocusFlipGridView.this.setFocusable(true);
                this.mIsFinished = true;
                setChild(1.0f);
                if (FocusFlipGridView.this.mOnFocusFlipGridViewListener != null) {
                    FocusFlipGridView.this.mOnFocusFlipGridViewListener.onOutAnimationDone();
                }
            }
        }

        public void run() {
            if (!this.mIsFinished) {
                if (this.mCurrFrameCount > this.outAnimFrameCount) {
                    stop();
                    return;
                }
                this.mCurrFrameCount++;
                setChild(1.0f - (((float) this.mCurrFrameCount) / ((float) this.outAnimFrameCount)));
                FocusFlipGridView.this.post(this);
            }
        }

        private void setChild(float scale) {
            int itemCount = FocusFlipGridView.this.getChildCount();
            if (FocusFlipGridView.this.mAnimAlpha) {
                FocusFlipGridView.this.setAlpha(scale);
                int unused = FocusFlipGridView.this.mAnimAlphaValue = (int) (255.0f * scale);
            }
            for (int i = 0; i < itemCount; i++) {
                View itemView = FocusFlipGridView.this.getChildAt(i);
                if (itemView instanceof FlipGridView.FlipGridViewHeaderOrFooterInterface) {
                    FlipGridView.FlipGridViewHeaderOrFooterInterface headerOrFooterView = (FlipGridView.FlipGridViewHeaderOrFooterInterface) itemView;
                    int childCount = headerOrFooterView.getHorCount() * headerOrFooterView.getVerticalCount();
                    for (int j = 0; j < childCount; j++) {
                        View childView = headerOrFooterView.getView(j);
                        if (childView != null) {
                            childView.setScaleX(scale);
                            childView.setScaleY(scale);
                        }
                    }
                } else {
                    itemView.setScaleX(scale);
                    itemView.setScaleY(scale);
                }
            }
        }
    }

    public boolean isFocusBackground() {
        return false;
    }

    public void drawBeforeFocus(Canvas canvas) {
    }

    public void drawAfterFocus(Canvas canvas) {
    }

    public boolean isFinished() {
        return true;
    }

    private int getRemainScrollUpDistance(int nextSelectedPosition) {
        int nextSelectedCenter;
        int verticalSpacing = getVerticalSpacing();
        int center = getHeight() / 2;
        int distanceLeft = getFlipColumnFirstItemLeftMoveDistance(nextSelectedPosition);
        View nextSelctedView = getChildAt(nextSelectedPosition - this.mFirstPosition);
        View firstView = getChildAt(0);
        int firstTop = firstView.getTop();
        if (firstView instanceof GridView.GridViewHeaderViewExpandDistance) {
            firstTop += ((GridView.GridViewHeaderViewExpandDistance) firstView).getUpExpandDistance();
        }
        if (nextSelctedView == null) {
            View nextSelctedView2 = getChildAt(0);
            int oldRowStart = getRowStart(getFirstVisiblePosition());
            int rowStart = getRowStart(nextSelectedPosition);
            int headerCount = this.mHeaderViewInfos.size();
            if (headerCount <= 0) {
                nextSelectedCenter = (nextSelctedView2.getTop() + (nextSelctedView2.getHeight() / 2)) - ((nextSelctedView2.getHeight() + verticalSpacing) * ((oldRowStart - rowStart) / getColumnNum()));
            } else if (rowStart < headerCount) {
                nextSelectedCenter = nextSelctedView2.getTop() - ((nextSelctedView2.getHeight() + verticalSpacing) * ((oldRowStart - headerCount) / getColumnNum()));
                for (int i = rowStart; i < headerCount; i++) {
                    View headerView = ((AbsBaseListView.FixedViewInfo) this.mHeaderViewInfos.get(i)).view;
                    int headerViewHeight = headerView.getHeight();
                    if (headerView instanceof GridView.GridViewHeaderViewExpandDistance) {
                        headerViewHeight = (headerViewHeight - ((GridView.GridViewHeaderViewExpandDistance) headerView).getUpExpandDistance()) - ((GridView.GridViewHeaderViewExpandDistance) headerView).getDownExpandDistance();
                    }
                    nextSelectedCenter -= (headerViewHeight / 2) + verticalSpacing;
                }
            } else {
                nextSelectedCenter = (nextSelctedView2.getTop() + (nextSelctedView2.getHeight() / 2)) - ((nextSelctedView2.getHeight() + verticalSpacing) * ((oldRowStart - rowStart) / getColumnNum()));
            }
        } else if (nextSelctedView instanceof GridView.GridViewHeaderViewExpandDistance) {
            int upDistance = ((GridView.GridViewHeaderViewExpandDistance) nextSelctedView).getUpExpandDistance();
            nextSelectedCenter = nextSelctedView.getTop() + upDistance + (((nextSelctedView.getHeight() - upDistance) - ((GridView.GridViewHeaderViewExpandDistance) nextSelctedView).getDownExpandDistance()) / 2);
        } else {
            nextSelectedCenter = nextSelctedView.getTop() + (nextSelctedView.getHeight() / 2);
        }
        int finalNextSelectedCenter = nextSelectedCenter + distanceLeft;
        if (finalNextSelectedCenter >= center) {
            return 0;
        }
        int remainAmountToScroll = center - finalNextSelectedCenter;
        int maxDiff = this.mListPadding.top - (firstTop - getTopLeftDistance(getFirstVisiblePosition()));
        if (maxDiff < 0) {
            maxDiff = 0;
        }
        int maxDiff2 = maxDiff - getFlipColumnFirstItemLeftMoveDistance(getFirstVisiblePosition());
        if (maxDiff2 < 0) {
            maxDiff2 = 0;
        }
        if (remainAmountToScroll > maxDiff2) {
            return maxDiff2;
        }
        return remainAmountToScroll;
    }

    public void offsetFocusRect(int offsetX, int offsetY) {
        if (SystemProUtils.getGlobalFocusMode() == 0) {
            this.mFocusRectparams.focusRect().offset(offsetX, offsetY);
        }
    }

    public void offsetFocusRect(int left, int right, int top, int bottom) {
        if (SystemProUtils.getGlobalFocusMode() == 0) {
            this.mFocusRectparams.focusRect().left += left;
            this.mFocusRectparams.focusRect().right += right;
            this.mFocusRectparams.focusRect().top += top;
            this.mFocusRectparams.focusRect().bottom += bottom;
        }
    }

    public void offsetFocusRectLeftAndRight(int left, int right) {
        if (SystemProUtils.getGlobalFocusMode() == 0) {
            this.mFocusRectparams.focusRect().left += left;
            this.mFocusRectparams.focusRect().right += right;
        }
    }

    public void offsetFocusRectTopAndBottom(int top, int bottom) {
        if (SystemProUtils.getGlobalFocusMode() == 0) {
            this.mFocusRectparams.focusRect().top += top;
            this.mFocusRectparams.focusRect().bottom += bottom;
        }
    }

    public Rect getClipFocusRect() {
        return this.mClipFocusRect;
    }
}
