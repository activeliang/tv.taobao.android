package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListAdapter;
import com.alibaba.wireless.security.SecExceptionCode;
import com.yunos.tvtaobao.tvsdk.widget.AbsBaseListView;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusListView;
import com.zhiping.dev.android.logger.ZpLogger;

public class InnerFocusGroupListView extends FocusListView {
    private boolean mItemInnerFocusState = true;
    private OnKeyListener mOnKeyListener;
    private boolean retainFocus = true;

    public interface OnKeyListener {
        void onExceedingBottom();
    }

    public InnerFocusGroupListView(Context context) {
        super(context);
    }

    public InnerFocusGroupListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerFocusGroupListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void clearInnerFocusState() {
        this.mItemInnerFocusState = false;
    }

    public void resetInnerFocusState() {
        this.mItemInnerFocusState = true;
    }

    /* access modifiers changed from: protected */
    public void performSelect(boolean select) {
        if (!select) {
            View selectedView = getSelectedView();
            if (selectedView instanceof InnerFocusLayout) {
                ((InnerFocusLayout) selectedView).clearItemSelected();
            }
            this.mItemInnerFocusState = false;
        }
        super.performSelect(select);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.mItemInnerFocusState) {
            View selectedView = getSelectedView();
            if (selectedView instanceof InnerFocusLayout) {
                return ((InnerFocusLayout) selectedView).onKeyUp(keyCode, event);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    public int getDescendantFocusability() {
        return 393216;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getChildCount() <= 0) {
            return super.onKeyDown(keyCode, event);
        }
        if (checkState(keyCode)) {
            return true;
        }
        if (this.mItemInnerFocusState) {
            View selectedView = getSelectedView();
            if (!(selectedView instanceof InnerFocusLayout)) {
                this.mItemInnerFocusState = false;
            } else if (((InnerFocusLayout) selectedView).onKeyDown(keyCode, event)) {
                return true;
            }
        }
        if (event.getKeyCode() != 20 || getNextSelectedPosition(130) != -1) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.mOnKeyListener == null || this.mLastScrollState == 2) {
            return true;
        }
        this.mOnKeyListener.onExceedingBottom();
        return true;
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return super.onKeyMultiple(keyCode, repeatCount, event);
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    public void setOnKeyListener(OnKeyListener onItemListener) {
        this.mOnKeyListener = onItemListener;
    }

    /* access modifiers changed from: protected */
    public int amountToCenterScroll(int direction, int nextSelectedPosition) {
        int nextSelectedCenter;
        boolean reset;
        int nextSelectedCenter2;
        boolean reset2;
        int center = (((getHeight() - this.mListPadding.top) - this.mListPadding.bottom) / 2) + this.mListPadding.top;
        int height = getHeight() - this.mListPadding.bottom;
        int listTop = this.mListPadding.top;
        int visibleChildCount = getVisibleChildCount();
        int amountToScroll = 0;
        int distanceLeft = getLeftScrollDistance();
        if (direction == 130) {
            View nextSelctedView = getChildAt(nextSelectedPosition - getFirstPosition());
            if (DEBUG) {
                ZpLogger.d(TAG, "down -> nextSelectedView: " + nextSelctedView);
            }
            if (nextSelctedView == null) {
                nextSelctedView = getLastChild();
                int nextSelectedCenter3 = nextSelctedView.getBottom();
                if (DEBUG) {
                    ZpLogger.d(TAG, "down -> nextSelectedCenter:" + nextSelectedCenter3 + ",center:" + center + ",spacing:" + this.mSpacing);
                }
                nextSelectedCenter2 = nextSelectedCenter3 + getItemDistance(getLastPosition(), nextSelectedPosition, 130);
                if (DEBUG) {
                    ZpLogger.d(TAG, "down -> nextSelectedCenter:" + nextSelectedCenter2 + ",center:" + center + ",spacing:" + this.mSpacing);
                }
                reset2 = false;
            } else {
                nextSelectedCenter2 = (nextSelctedView.getTop() + nextSelctedView.getBottom()) / 2;
                reset2 = true;
            }
            int finalNextSelectedCenter = nextSelectedCenter2 - distanceLeft;
            if (finalNextSelectedCenter > center) {
                amountToScroll = finalNextSelectedCenter - center;
                int maxDiff = ((nextSelctedView.getHeight() + this.mSpacing) * ((this.mItemCount - getLastVisiblePosition()) - 1)) - distanceLeft;
                View lastVisibleView = getLastVisibleChild();
                if (lastVisibleView.getBottom() > getHeight() - this.mListPadding.bottom) {
                    maxDiff += lastVisibleView.getBottom() - (getHeight() - this.mListPadding.bottom);
                }
                if (amountToScroll > maxDiff) {
                    amountToScroll = maxDiff;
                }
                if (reset2) {
                    reset();
                    offsetFocusRect(0, -distanceLeft);
                    if (DEBUG) {
                        ZpLogger.i(TAG, "amountToCenterScroll: focus rect = " + this.mFocusRectparams.focusRect() + ", distanceLeft = " + distanceLeft + ", nextSelectedPosition = " + nextSelectedPosition);
                    }
                }
                if (amountToScroll > 0) {
                    if (reset2) {
                        offsetFocusRect(0, -amountToScroll);
                    } else {
                        offsetFocusRect(0, (nextSelctedView.getHeight() + this.mSpacing) - amountToScroll);
                    }
                    if (DEBUG) {
                        ZpLogger.d(TAG, "mListLoopScroller amountToCenterScroll: focus down amountToScroll = " + amountToScroll + ", focus rect = " + this.mFocusRectparams.focusRect());
                    }
                    smoothScrollBy(amountToScroll);
                    this.mIsAnimate = true;
                } else {
                    if (!reset2) {
                        offsetFocusRect(0, nextSelctedView.getHeight() + this.mSpacing);
                    }
                    this.mIsAnimate = true;
                }
            } else {
                reset();
                offsetFocusRect(0, -distanceLeft);
                this.mIsAnimate = true;
            }
            return amountToScroll;
        } else if (direction != 33) {
            return 0;
        } else {
            View nextSelctedView2 = getChildAt(nextSelectedPosition - getFirstPosition());
            if (DEBUG) {
                ZpLogger.d(TAG, "up -> nextSelectedView: " + nextSelctedView2);
            }
            if (nextSelctedView2 == null) {
                nextSelctedView2 = getFirstVisibleChild();
                int nextSelectedCenter4 = nextSelctedView2.getTop();
                int distance = getItemDistance(getFirstVisiblePosition(), nextSelectedPosition, 33);
                ZpLogger.d(TAG, "up -> nextSelectedCenter:" + nextSelectedCenter4 + ",center:" + center + ",distance:" + distance);
                if (nextSelectedPosition >= getHeaderViewsCount()) {
                    nextSelectedCenter = nextSelectedCenter4 + distance;
                    if (DEBUG) {
                        ZpLogger.d(TAG, "up -> nextSelectedCenter, getHeaderViewsCount:" + getHeaderViewsCount() + ",nextSelectedPosition:" + nextSelectedPosition);
                    }
                } else {
                    if (DEBUG) {
                        ZpLogger.d(TAG, "up -> nextSelectedCenter:" + nextSelectedCenter4 + ",center:" + center);
                    }
                    nextSelectedCenter = nextSelectedCenter4 + getItemDistance(getFirstVisiblePosition(), getHeaderViewsCount(), 33);
                    for (int i = getHeaderViewsCount() - 1; i >= nextSelectedPosition; i--) {
                        nextSelectedCenter -= getHeaderView(i).getHeight();
                    }
                }
                if (DEBUG) {
                    ZpLogger.d(TAG, "up next distance:" + distance);
                }
                reset = false;
            } else {
                nextSelectedCenter = (nextSelctedView2.getTop() + nextSelctedView2.getBottom()) / 2;
                reset = true;
            }
            int finalNextSelectedCenter2 = nextSelectedCenter - distanceLeft;
            if (finalNextSelectedCenter2 < center) {
                int amountToScroll2 = center - finalNextSelectedCenter2;
                int maxDiff2 = 0;
                int start = getHeaderViewsCount() - 1;
                if (getFirstVisiblePosition() >= getHeaderViewsCount()) {
                    maxDiff2 = (nextSelctedView2.getHeight() + this.mSpacing) * (getFirstVisiblePosition() - getHeaderViewsCount());
                } else {
                    start = getFirstVisiblePosition() - 1;
                }
                for (int i2 = start; i2 >= 0; i2--) {
                    maxDiff2 += getHeaderView(i2).getHeight();
                }
                if (maxDiff2 < 0) {
                    maxDiff2 = 0;
                }
                int maxDiff3 = maxDiff2 + distanceLeft;
                View firstVisibleView = getFirstVisibleChild();
                if (firstVisibleView.getTop() < listTop) {
                    int firstOffset = getFirsVisibletChildIndex() - nextSelectedPosition;
                    if (firstOffset > 0) {
                        maxDiff3 += (listTop - firstVisibleView.getTop()) - (firstOffset * listTop);
                    } else {
                        maxDiff3 += listTop - firstVisibleView.getTop();
                    }
                }
                if (amountToScroll2 > maxDiff3) {
                    amountToScroll2 = maxDiff3;
                }
                if (reset) {
                    reset();
                    offsetFocusRect(0, -distanceLeft);
                    if (DEBUG) {
                        ZpLogger.i(TAG, "amountToCenterScroll: focus rect = " + this.mFocusRectparams.focusRect() + ", distanceLeft = " + distanceLeft + ", nextSelectedPosition = " + nextSelectedPosition);
                    }
                } else if (nextSelectedPosition < getHeaderViewsCount()) {
                    reset = true;
                    resetHeader(nextSelectedPosition);
                    offsetFocusRect(0, -distanceLeft);
                }
                if (amountToScroll > 0) {
                    if (reset) {
                        offsetFocusRect(0, amountToScroll);
                    } else {
                        offsetFocusRect(0, -((nextSelctedView2.getHeight() + this.mSpacing) - amountToScroll));
                    }
                    if (DEBUG) {
                        ZpLogger.d(TAG, "amountToCenterScroll: focus down amountToScroll = " + amountToScroll + ", focus rect = " + this.mFocusRectparams.focusRect());
                    }
                    smoothScrollBy(-amountToScroll);
                    this.mIsAnimate = true;
                } else {
                    if (!reset) {
                        offsetFocusRect(0, -(nextSelctedView2.getHeight() + this.mSpacing));
                    }
                    this.mIsAnimate = true;
                }
            } else {
                reset();
                offsetFocusRect(0, -distanceLeft);
                this.mIsAnimate = true;
            }
            return amountToScroll;
        }
    }

    public void manualFindFocusInner(int keyCode) {
        switch (keyCode) {
            case 21:
            case 22:
                if (!this.mItemInnerFocusState) {
                    View selectedView = getSelectedView();
                    if (selectedView instanceof InnerFocusLayout) {
                        InnerFocusLayout focusView = (InnerFocusLayout) selectedView;
                        if (focusView.isChangedInnerKey(keyCode) && focusView.findFirstFocus(keyCode)) {
                            this.mItemInnerFocusState = true;
                            focusView.setNextFocusSelected();
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public boolean actionInnerFocus(int keyCode, KeyEvent event) {
        if (this.mItemInnerFocusState && innerFocus(keyCode, event)) {
            View selectedView = getSelectedView();
            if (selectedView instanceof InnerFocusLayout) {
                ((InnerFocusLayout) selectedView).setNextFocusSelected();
                return true;
            }
        }
        return false;
    }

    public boolean checkState(int keyCode) {
        if (this.mLastScrollState == 2 && (keyCode == 21 || keyCode == 22)) {
            return true;
        }
        return false;
    }

    public void setRetainFocus(boolean retainFocus2) {
        this.retainFocus = retainFocus2;
    }

    public boolean preOnKeyDown(int keyCode, KeyEvent event) {
        if (checkState(keyCode)) {
            return false;
        }
        switch (keyCode) {
            case 19:
            case 20:
            case SecExceptionCode.SEC_ERROR_INIT_NO_DATA_FILE:
            case 123:
                if (innerFocus(keyCode, event)) {
                    return true;
                }
                if (event.getKeyCode() == 20 && getNextSelectedPosition(130) == -1) {
                    return true;
                }
            case 21:
            case 22:
                if (!this.mItemInnerFocusState) {
                    View selectedView = getSelectedView();
                    if (selectedView instanceof InnerFocusLayout) {
                        InnerFocusLayout focusView = (InnerFocusLayout) selectedView;
                        if (!focusView.isChangedInnerKey(keyCode) || !focusView.findFirstFocus(keyCode)) {
                            return false;
                        }
                        this.mItemInnerFocusState = true;
                        return true;
                    }
                } else if (innerFocus(keyCode, event)) {
                    return true;
                } else {
                    return false;
                }
                break;
        }
        return super.preOnKeyDown(keyCode, event);
    }

    private boolean innerFocus(int keyCode, KeyEvent event) {
        if (this.mItemInnerFocusState) {
            View selectedView = getSelectedView();
            if (!(selectedView instanceof InnerFocusLayout) || !((InnerFocusLayout) selectedView).findNextFocus(keyCode, event)) {
                return false;
            }
            return true;
        }
        return false;
    }

    private int getItemDistance(int startPos, int endPos, int direction) {
        ListAdapter adapter;
        int totalHeight;
        int totalHeight2;
        if (DEBUG) {
            ZpLogger.d(TAG, "before next distance, start pos:" + startPos + ",endPos:" + endPos + ",direction:" + direction);
        }
        if (getAdapter() instanceof AbsBaseListView.HeaderViewListAdapter) {
            adapter = ((AbsBaseListView.HeaderViewListAdapter) getAdapter()).getWrappedAdapter();
        } else {
            adapter = getAdapter();
        }
        if (adapter == null || !(adapter instanceof GroupBaseAdapter)) {
            return 0;
        }
        GroupBaseAdapter groupAdapter = (GroupBaseAdapter) adapter;
        if (startPos < 0 || endPos < 0) {
            throw new IllegalArgumentException();
        } else if (startPos == endPos) {
            return 0;
        } else {
            int startGroupPos = groupAdapter.getGroupPos(startPos);
            int startItemPos = groupAdapter.getGroupItemPos(startPos);
            int endGroupPos = groupAdapter.getGroupPos(endPos);
            int endItemPos = groupAdapter.getGroupItemPos(endPos);
            int hintHeight = getGroupHintHeight(groupAdapter);
            int itemHeight = getGroupItemHeight(groupAdapter);
            int totalHeight3 = 0;
            if (direction == 130) {
                if (startGroupPos != endGroupPos) {
                    if (startItemPos == Integer.MAX_VALUE) {
                        totalHeight2 = 0 + (groupAdapter.getItemCount(startGroupPos) * itemHeight);
                    } else {
                        totalHeight2 = 0 + (((groupAdapter.getItemCount(startGroupPos) - 1) - startItemPos) * itemHeight);
                    }
                    if (endItemPos == Integer.MAX_VALUE) {
                        totalHeight3 = totalHeight2 + hintHeight;
                    } else {
                        totalHeight3 = totalHeight2 + (endItemPos * itemHeight) + hintHeight + itemHeight;
                    }
                    for (int i = startGroupPos + 1; i < endGroupPos; i++) {
                        totalHeight3 = totalHeight3 + (groupAdapter.getItemCount(i) * itemHeight) + hintHeight;
                    }
                } else if (startItemPos == Integer.MAX_VALUE) {
                    totalHeight3 = 0 + ((endItemPos + 1) * itemHeight);
                } else {
                    totalHeight3 = 0 + ((endItemPos - startItemPos) * itemHeight) + itemHeight;
                }
            } else if (direction == 33) {
                if (startGroupPos != endGroupPos) {
                    if (startItemPos != Integer.MAX_VALUE) {
                        totalHeight3 = 0 - ((startItemPos * itemHeight) + hintHeight);
                    }
                    if (endItemPos == Integer.MAX_VALUE) {
                        totalHeight = totalHeight3 - ((groupAdapter.getItemCount(endGroupPos) * itemHeight) + hintHeight);
                    } else {
                        totalHeight = totalHeight3 - ((groupAdapter.getItemCount(endGroupPos) - endItemPos) * itemHeight);
                    }
                    for (int i2 = startGroupPos - 1; i2 > endGroupPos; i2--) {
                        totalHeight = (totalHeight3 - (groupAdapter.getItemCount(i2) * itemHeight)) - hintHeight;
                    }
                } else {
                    totalHeight3 = (endItemPos - startItemPos) * itemHeight;
                }
            }
            if (!DEBUG) {
                return totalHeight3;
            }
            ZpLogger.i(TAG, "getItemDistance startPos=" + startPos + " endPos=" + endPos + " startGroupPos=" + startGroupPos + " startItemPos=" + startItemPos + " endGroupPos=" + endGroupPos + " endItemPos=" + endItemPos + " hintWidth=" + hintHeight + " itemWidth=" + itemHeight + " totalHeight=" + totalHeight3);
            return totalHeight3;
        }
    }

    private int getGroupHintHeight(GroupBaseAdapter groupAdapter) {
        Rect hintRect = groupAdapter.getGroupHintRect();
        if (hintRect != null) {
            return hintRect.height();
        }
        return 0;
    }

    private int getGroupItemHeight(GroupBaseAdapter groupAdapter) {
        Rect itemRect = groupAdapter.getGroupItemRect();
        if (itemRect != null) {
            return itemRect.height();
        }
        return 0;
    }
}
