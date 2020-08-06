package com.yunos.tvtaobao.tvsdk.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.GridLayoutAnimationController;
import android.widget.Checkable;
import com.alibaba.wireless.security.SecExceptionCode;
import com.uc.webview.export.extension.UCCore;
import com.yunos.tvtaobao.tvsdk.widget.AbsBaseListView;

public class GridView extends AbsListView {
    public static final int AUTO_FIT = -1;
    private static final boolean DEBUG = false;
    public static final int NO_STRETCH = 0;
    public static final int STRETCH_COLUMN_WIDTH = 2;
    public static final int STRETCH_SPACING = 1;
    public static final int STRETCH_SPACING_UNIFORM = 3;
    private static final String TAG = "GridView";
    int firstColumnMarginleft = 0;
    private int mColumnWidth;
    private int mHorizontalSpacing = 0;
    private int mMinFirstPos = Integer.MAX_VALUE;
    private int mMinLastPos = -1;
    private int mNumColumns = -1;
    private View mReferenceView = null;
    private View mReferenceViewInSelectedRow = null;
    private int mRequestedColumnWidth = -1;
    private int mRequestedHorizontalSpacing;
    private int mRequestedNumColumns;
    private int mStretchMode = 2;
    private int mVerticalSpacing = 0;

    public interface GridViewHeaderViewExpandDistance {
        int getDownExpandDistance();

        int getUpExpandDistance();
    }

    public GridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridView(Context context) {
        super(context);
    }

    public void setMinLastPos(int pos) {
        this.mMinLastPos = pos;
    }

    public void setMinFirstPos(int pos) {
        this.mMinFirstPos = pos;
    }

    public void setReferenceViewInSelectedRow(View selectedRowView) {
        this.mReferenceViewInSelectedRow = selectedRowView;
    }

    public void setNumColumns(int numColumns) {
        if (numColumns != this.mRequestedNumColumns) {
            this.mRequestedNumColumns = numColumns;
            requestLayoutIfNecessary();
        }
    }

    public void setColumnWidth(int columnWidth) {
        if (columnWidth != this.mRequestedColumnWidth) {
            this.mRequestedColumnWidth = columnWidth;
            requestLayoutIfNecessary();
        }
    }

    public void setVerticalSpacing(int verticalSpacing) {
        if (verticalSpacing != this.mVerticalSpacing) {
            this.mVerticalSpacing = verticalSpacing;
            requestLayoutIfNecessary();
        }
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        if (horizontalSpacing != this.mRequestedHorizontalSpacing) {
            this.mRequestedHorizontalSpacing = horizontalSpacing;
            requestLayoutIfNecessary();
        }
    }

    public int getHorizontalSpacing() {
        return this.mHorizontalSpacing;
    }

    public int getRequestedHorizontalSpacing() {
        return this.mRequestedHorizontalSpacing;
    }

    public int getVerticalSpacing() {
        return this.mVerticalSpacing;
    }

    public int getColumnNum() {
        return this.mNumColumns;
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        int closestChildIndex = -1;
        if (gainFocus && previouslyFocusedRect != null) {
            previouslyFocusedRect.offset(getScrollX(), getScrollY());
            Rect otherRect = this.mTempRect;
            int minDistance = Integer.MAX_VALUE;
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (isCandidateSelection(i, direction)) {
                    View other = getChildAt(i);
                    boolean isHeaderOrFooter = false;
                    if (this.mHeaderViewInfos != null && this.mHeaderViewInfos.size() > 0) {
                        for (int h = 0; h < this.mHeaderViewInfos.size(); h++) {
                            AbsBaseListView.FixedViewInfo info = (AbsBaseListView.FixedViewInfo) this.mHeaderViewInfos.get(h);
                            if (info != null && info.view.equals(other)) {
                                isHeaderOrFooter = true;
                            }
                        }
                    }
                    if (!isHeaderOrFooter && this.mFooterViewInfos != null && this.mFooterViewInfos.size() > 0) {
                        for (int f = 0; f < this.mFooterViewInfos.size(); f++) {
                            AbsBaseListView.FixedViewInfo info2 = (AbsBaseListView.FixedViewInfo) this.mFooterViewInfos.get(f);
                            if (info2 != null && info2.view.equals(other)) {
                                isHeaderOrFooter = true;
                            }
                        }
                    }
                    if (!isHeaderOrFooter || !(other instanceof ViewGroup)) {
                        other.getDrawingRect(otherRect);
                        offsetDescendantRectToMyCoords(other, otherRect);
                        int distance = getDistance(previouslyFocusedRect, otherRect, direction);
                        if (distance < minDistance) {
                            minDistance = distance;
                            closestChildIndex = i;
                        }
                    } else {
                        ViewGroup headerorFooterView = (ViewGroup) other;
                        int headerorFooterCount = headerorFooterView == null ? 0 : headerorFooterView.getChildCount();
                        for (int childIndex = 0; childIndex < headerorFooterCount; childIndex++) {
                            View childView = headerorFooterView.getChildAt(childIndex);
                            childView.getDrawingRect(otherRect);
                            offsetDescendantRectToMyCoords(childView, otherRect);
                            int distance2 = getDistance(previouslyFocusedRect, otherRect, direction);
                            if (distance2 < minDistance) {
                                minDistance = distance2;
                                closestChildIndex = i;
                            }
                        }
                    }
                }
            }
        }
        if (closestChildIndex >= 0) {
            setHeaderSelection(this.mFirstPosition + closestChildIndex);
        } else if (getChildCount() > 0 && gainFocus) {
            setHeaderSelection(this.mFirstPosition);
        }
    }

    private void setHeaderSelection(int selectedPos) {
        int headerCount = getHeaderViewsCount();
        if (selectedPos < headerCount) {
            for (int i = selectedPos; i < headerCount; i++) {
                if (((AbsBaseListView.FixedViewInfo) this.mHeaderViewInfos.get(i)).view.isFocusable()) {
                    setSelection(i);
                    return;
                }
            }
            setSelection(headerCount);
            return;
        }
        setSelection(selectedPos);
    }

    private boolean isCandidateSelection(int childIndex, int direction) {
        int rowEnd;
        int rowStart;
        int count = getChildCount();
        int invertedIndex = (count - 1) - childIndex;
        if (!this.mStackFromBottom) {
            rowStart = childIndex - (childIndex % this.mNumColumns);
            rowEnd = Math.max((this.mNumColumns + rowStart) - 1, count);
        } else {
            rowEnd = (count - 1) - (invertedIndex - (invertedIndex % this.mNumColumns));
            rowStart = Math.max(0, (rowEnd - this.mNumColumns) + 1);
        }
        switch (direction) {
            case 1:
                if (childIndex == rowEnd && rowEnd == count - 1) {
                    return true;
                }
                return false;
            case 2:
                if (childIndex == rowStart && rowStart == 0) {
                    return true;
                }
                return false;
            case 17:
                if (childIndex != rowEnd) {
                    return false;
                }
                return true;
            case 33:
                if (rowEnd != count - 1) {
                    return false;
                }
                return true;
            case 66:
                if (childIndex == rowStart) {
                    return true;
                }
                return false;
            case 130:
                if (rowStart != 0) {
                    return false;
                }
                return true;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT, FOCUS_FORWARD, FOCUS_BACKWARD}.");
        }
    }

    /* access modifiers changed from: protected */
    public int getFillGapNextChildIndex(boolean isDown) {
        if (isDown) {
            return getChildCount() - 1;
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    public void fillGap(boolean isDown) {
        int numColumns = this.mNumColumns;
        int verticalSpacing = this.mVerticalSpacing;
        int count = getChildCount();
        if (isDown) {
            if ((getGroupFlags() & 34) == 34) {
                int paddingTop = getListPaddingTop();
            }
            int position = this.mFirstPosition + count;
            if (this.mStackFromBottom) {
                position += numColumns - 1;
            }
            fillDown(position, getNextTop(position, getChildAt(getFillGapNextChildIndex(isDown))));
            correctTooHigh(numColumns, verticalSpacing, getChildCount());
            return;
        }
        if ((getGroupFlags() & 34) == 34) {
            int paddingBottom = getListPaddingBottom();
        }
        int position2 = this.mFirstPosition;
        if (this.mStackFromBottom) {
            position2--;
        }
        fillUpWithHeaderOrFooter(position2, getNextBottom(position2, getChildAt(getFillGapNextChildIndex(isDown))));
        correctTooLow(numColumns, verticalSpacing, getChildCount());
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count;
        int widthSize;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize2 = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == 0) {
            if (this.mColumnWidth > 0) {
                widthSize = this.mColumnWidth + this.mListPadding.left + this.mListPadding.right;
            } else {
                widthSize = this.mListPadding.left + this.mListPadding.right;
            }
            widthSize2 = widthSize + getVerticalScrollbarWidth();
        }
        int childWidth = (widthSize2 - this.mListPadding.left) - this.mListPadding.right;
        int childHeight = 0;
        if (this.mAdapter == null) {
            count = 0;
        } else {
            count = this.mAdapter.getCount();
        }
        this.mItemCount = count;
        int count2 = this.mItemCount;
        if (count2 > getHeaderViewsCount() + getFooterViewsCount()) {
            View child = obtainView(getHeaderViewsCount(), this.mIsScrap);
            AbsBaseListView.LayoutParams p = (AbsBaseListView.LayoutParams) child.getLayoutParams();
            if (p == null) {
                p = (AbsBaseListView.LayoutParams) generateDefaultLayoutParams();
                child.setLayoutParams(p);
            }
            p.viewType = this.mAdapter.getItemViewType(0);
            p.forceAdd = true;
            child.measure(getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(0, 0), 0, p.width), getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(0, 0), 0, p.height));
            childHeight = child.getMeasuredHeight();
            if (this.mNumColumns == -1 && this.mRequestedColumnWidth < 0) {
                setColumnWidth(child.getMeasuredWidth());
            }
            int childState = combineMeasuredStates(0, child.getMeasuredState());
            if (this.mRecycler.shouldRecycleViewType(p.viewType)) {
                this.mRecycler.addScrapView(child, -1);
            }
        }
        if (heightMode == 0) {
            heightSize = this.mListPadding.top + this.mListPadding.bottom + childHeight + (getVerticalFadingEdgeLength() * 2);
        }
        if (heightMode == Integer.MIN_VALUE) {
            int ourSize = this.mListPadding.top + this.mListPadding.bottom;
            int numColumns = this.mNumColumns;
            int i = 0;
            while (true) {
                if (i >= count2) {
                    break;
                }
                ourSize += childHeight;
                if (i + numColumns < count2) {
                    ourSize += this.mVerticalSpacing;
                }
                if (ourSize >= heightSize) {
                    ourSize = heightSize;
                    break;
                }
                i += numColumns;
            }
            heightSize = ourSize;
        }
        boolean didNotInitiallyFit = determineColumns(childWidth);
        if (widthMode == Integer.MIN_VALUE && this.mRequestedNumColumns != -1 && ((this.mRequestedNumColumns * this.mColumnWidth) + ((this.mRequestedNumColumns - 1) * this.mHorizontalSpacing) + this.mListPadding.left + this.mListPadding.right > widthSize2 || didNotInitiallyFit)) {
            widthSize2 |= 16777216;
        }
        setMeasuredDimension(widthSize2, heightSize);
        this.mWidthMeasureSpec = widthMeasureSpec;
    }

    private boolean determineColumns(int availableSpace) {
        int requestedHorizontalSpacing = this.mRequestedHorizontalSpacing;
        int stretchMode = this.mStretchMode;
        int requestedColumnWidth = this.mRequestedColumnWidth;
        boolean didNotInitiallyFit = false;
        if (this.mRequestedNumColumns != -1) {
            this.mNumColumns = this.mRequestedNumColumns;
        } else if (requestedColumnWidth > 0) {
            this.mNumColumns = (availableSpace + requestedHorizontalSpacing) / (requestedColumnWidth + requestedHorizontalSpacing);
        } else {
            this.mNumColumns = 2;
        }
        if (this.mNumColumns <= 0) {
            this.mNumColumns = 1;
        }
        switch (stretchMode) {
            case 0:
                this.mColumnWidth = requestedColumnWidth;
                this.mHorizontalSpacing = requestedHorizontalSpacing;
                break;
            default:
                int spaceLeftOver = (availableSpace - (this.mNumColumns * requestedColumnWidth)) - ((this.mNumColumns - 1) * requestedHorizontalSpacing);
                if (spaceLeftOver < 0) {
                    didNotInitiallyFit = true;
                }
                switch (stretchMode) {
                    case 1:
                        this.mColumnWidth = requestedColumnWidth;
                        if (this.mNumColumns <= 1) {
                            this.mHorizontalSpacing = requestedHorizontalSpacing + spaceLeftOver;
                            break;
                        } else {
                            this.mHorizontalSpacing = (spaceLeftOver / (this.mNumColumns - 1)) + requestedHorizontalSpacing;
                            break;
                        }
                    case 2:
                        this.mColumnWidth = (spaceLeftOver / this.mNumColumns) + requestedColumnWidth;
                        this.mHorizontalSpacing = requestedHorizontalSpacing;
                        break;
                    case 3:
                        this.mColumnWidth = requestedColumnWidth;
                        if (this.mNumColumns <= 1) {
                            this.mHorizontalSpacing = requestedHorizontalSpacing + spaceLeftOver;
                            break;
                        } else {
                            this.mHorizontalSpacing = (spaceLeftOver / (this.mNumColumns + 1)) + requestedHorizontalSpacing;
                            break;
                        }
                }
        }
        return didNotInitiallyFit;
    }

    /* access modifiers changed from: protected */
    public void attachLayoutAnimationParameters(View child, ViewGroup.LayoutParams params, int index, int count) {
        GridLayoutAnimationController.AnimationParameters animationParams = (GridLayoutAnimationController.AnimationParameters) params.layoutAnimationParameters;
        if (animationParams == null) {
            animationParams = new GridLayoutAnimationController.AnimationParameters();
            params.layoutAnimationParameters = animationParams;
        }
        animationParams.count = count;
        animationParams.index = index;
        animationParams.columnsCount = this.mNumColumns;
        animationParams.rowsCount = count / this.mNumColumns;
        if (!this.mStackFromBottom) {
            animationParams.column = index % this.mNumColumns;
            animationParams.row = index / this.mNumColumns;
            return;
        }
        int invertedIndex = (count - 1) - index;
        animationParams.column = (this.mNumColumns - 1) - (invertedIndex % this.mNumColumns);
        animationParams.row = (animationParams.rowsCount - 1) - (invertedIndex / this.mNumColumns);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00a5 A[Catch:{ all -> 0x01fe }] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00b0 A[Catch:{ all -> 0x01fe }] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00ef A[Catch:{ all -> 0x01fe }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void layoutChildren() {
        /*
            r21 = this;
            r0 = r21
            boolean r0 = r0.mNeedLayout
            r19 = r0
            if (r19 != 0) goto L_0x0009
        L_0x0008:
            return
        L_0x0009:
            r0 = r21
            boolean r3 = r0.mBlockLayoutRequests
            if (r3 != 0) goto L_0x0017
            r19 = 1
            r0 = r19
            r1 = r21
            r1.mBlockLayoutRequests = r0
        L_0x0017:
            r21.invalidate()     // Catch:{ all -> 0x01fe }
            r0 = r21
            android.widget.ListAdapter r0 = r0.mAdapter     // Catch:{ all -> 0x01fe }
            r19 = r0
            if (r19 != 0) goto L_0x0030
            r21.resetList()     // Catch:{ all -> 0x01fe }
            if (r3 != 0) goto L_0x0008
            r19 = 0
            r0 = r19
            r1 = r21
            r1.mBlockLayoutRequests = r0
            goto L_0x0008
        L_0x0030:
            r0 = r21
            android.graphics.Rect r0 = r0.mListPadding     // Catch:{ all -> 0x01fe }
            r19 = r0
            r0 = r19
            int r7 = r0.top     // Catch:{ all -> 0x01fe }
            int r19 = r21.getBottom()     // Catch:{ all -> 0x01fe }
            int r20 = r21.getTop()     // Catch:{ all -> 0x01fe }
            int r19 = r19 - r20
            r0 = r21
            android.graphics.Rect r0 = r0.mListPadding     // Catch:{ all -> 0x01fe }
            r20 = r0
            r0 = r20
            int r0 = r0.bottom     // Catch:{ all -> 0x01fe }
            r20 = r0
            int r6 = r19 - r20
            int r5 = r21.getChildCount()     // Catch:{ all -> 0x01fe }
            r12 = 0
            r9 = 0
            r16 = 0
            r15 = 0
            r14 = 0
            r0 = r21
            int r0 = r0.mLayoutMode     // Catch:{ all -> 0x01fe }
            r19 = r0
            switch(r19) {
                case 1: goto L_0x009f;
                case 2: goto L_0x00bf;
                case 3: goto L_0x009f;
                case 4: goto L_0x009f;
                case 5: goto L_0x009f;
                case 6: goto L_0x00d8;
                default: goto L_0x0065;
            }     // Catch:{ all -> 0x01fe }
        L_0x0065:
            r0 = r21
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x01fe }
            r19 = r0
            r0 = r21
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x01fe }
            r20 = r0
            int r12 = r19 - r20
            if (r12 < 0) goto L_0x0093
            if (r12 >= r5) goto L_0x0093
            r0 = r21
            android.widget.ListAdapter r0 = r0.mAdapter     // Catch:{ all -> 0x01fe }
            r19 = r0
            if (r19 == 0) goto L_0x0093
            r0 = r21
            android.widget.ListAdapter r0 = r0.mAdapter     // Catch:{ all -> 0x01fe }
            r19 = r0
            int r19 = r19.getCount()     // Catch:{ all -> 0x01fe }
            r0 = r19
            if (r12 >= r0) goto L_0x0093
            r0 = r21
            android.view.View r16 = r0.getChildAt(r12)     // Catch:{ all -> 0x01fe }
        L_0x0093:
            int r19 = r21.getHeaderViewsCount()     // Catch:{ all -> 0x01fe }
            r0 = r21
            r1 = r19
            android.view.View r15 = r0.getChildAt(r1)     // Catch:{ all -> 0x01fe }
        L_0x009f:
            r0 = r21
            boolean r8 = r0.mDataChanged     // Catch:{ all -> 0x01fe }
            if (r8 == 0) goto L_0x00a8
            r21.handleDataChanged()     // Catch:{ all -> 0x01fe }
        L_0x00a8:
            r0 = r21
            int r0 = r0.mItemCount     // Catch:{ all -> 0x01fe }
            r19 = r0
            if (r19 != 0) goto L_0x00ef
            r21.resetList()     // Catch:{ all -> 0x01fe }
            if (r3 != 0) goto L_0x0008
            r19 = 0
            r0 = r19
            r1 = r21
            r1.mBlockLayoutRequests = r0
            goto L_0x0008
        L_0x00bf:
            r0 = r21
            int r0 = r0.mNextSelectedPosition     // Catch:{ all -> 0x01fe }
            r19 = r0
            r0 = r21
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x01fe }
            r20 = r0
            int r12 = r19 - r20
            if (r12 < 0) goto L_0x009f
            if (r12 >= r5) goto L_0x009f
            r0 = r21
            android.view.View r14 = r0.getChildAt(r12)     // Catch:{ all -> 0x01fe }
            goto L_0x009f
        L_0x00d8:
            r0 = r21
            int r0 = r0.mNextSelectedPosition     // Catch:{ all -> 0x01fe }
            r19 = r0
            if (r19 < 0) goto L_0x009f
            r0 = r21
            int r0 = r0.mNextSelectedPosition     // Catch:{ all -> 0x01fe }
            r19 = r0
            r0 = r21
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x01fe }
            r20 = r0
            int r9 = r19 - r20
            goto L_0x009f
        L_0x00ef:
            r0 = r21
            int r0 = r0.mNextSelectedPosition     // Catch:{ all -> 0x01fe }
            r19 = r0
            r0 = r21
            r1 = r19
            r0.setSelectedPositionInt(r1)     // Catch:{ all -> 0x01fe }
            if (r16 == 0) goto L_0x0124
            r0 = r21
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x01fe }
            r19 = r0
            r0 = r21
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x01fe }
            r20 = r0
            int r19 = r19 - r20
            r0 = r19
            if (r0 == r12) goto L_0x0124
            r0 = r21
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x01fe }
            r19 = r0
            r0 = r21
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x01fe }
            r20 = r0
            int r12 = r19 - r20
            r0 = r21
            android.view.View r16 = r0.getChildAt(r12)     // Catch:{ all -> 0x01fe }
        L_0x0124:
            r0 = r21
            int r10 = r0.mFirstPosition     // Catch:{ all -> 0x01fe }
            r0 = r21
            com.yunos.tvtaobao.tvsdk.widget.AbsBaseListView$RecycleBin r0 = r0.mRecycler     // Catch:{ all -> 0x01fe }
            r17 = r0
            if (r8 == 0) goto L_0x0147
            r11 = 0
        L_0x0131:
            if (r11 >= r5) goto L_0x014c
            r0 = r21
            android.view.View r19 = r0.getChildAt(r11)     // Catch:{ all -> 0x01fe }
            int r20 = r10 + r11
            r0 = r17
            r1 = r19
            r2 = r20
            r0.addScrapView(r1, r2)     // Catch:{ all -> 0x01fe }
            int r11 = r11 + 1
            goto L_0x0131
        L_0x0147:
            r0 = r17
            r0.fillActiveViews(r5, r10)     // Catch:{ all -> 0x01fe }
        L_0x014c:
            r21.detachAllViewsFromParent()     // Catch:{ all -> 0x01fe }
            r17.removeSkippedScrap()     // Catch:{ all -> 0x01fe }
            r0 = r21
            int r0 = r0.mLayoutMode     // Catch:{ all -> 0x01fe }
            r19 = r0
            switch(r19) {
                case 1: goto L_0x01ec;
                case 2: goto L_0x01d6;
                case 3: goto L_0x020a;
                case 4: goto L_0x021f;
                case 5: goto L_0x0237;
                case 6: goto L_0x024f;
                default: goto L_0x015b;
            }     // Catch:{ all -> 0x01fe }
        L_0x015b:
            if (r5 != 0) goto L_0x0285
            r0 = r21
            boolean r0 = r0.mStackFromBottom     // Catch:{ all -> 0x01fe }
            r19 = r0
            if (r19 != 0) goto L_0x025b
            r0 = r21
            android.widget.ListAdapter r0 = r0.mAdapter     // Catch:{ all -> 0x01fe }
            r19 = r0
            if (r19 == 0) goto L_0x0173
            boolean r19 = r21.isInTouchMode()     // Catch:{ all -> 0x01fe }
            if (r19 == 0) goto L_0x0257
        L_0x0173:
            r19 = -1
        L_0x0175:
            r0 = r21
            r1 = r19
            r0.setSelectedPositionInt(r1)     // Catch:{ all -> 0x01fe }
            r0 = r21
            android.view.View r18 = r0.fillFromTop(r7)     // Catch:{ all -> 0x01fe }
        L_0x0182:
            r17.scrapActiveViews()     // Catch:{ all -> 0x01fe }
            if (r18 == 0) goto L_0x02eb
            r19 = -1
            r0 = r21
            r1 = r19
            r2 = r18
            r0.positionSelector(r1, r2)     // Catch:{ all -> 0x01fe }
        L_0x0192:
            r19 = 0
            r0 = r19
            r1 = r21
            r1.mLayoutMode = r0     // Catch:{ all -> 0x01fe }
            r19 = 0
            r0 = r19
            r1 = r21
            r1.mDataChanged = r0     // Catch:{ all -> 0x01fe }
            r19 = 0
            r0 = r19
            r1 = r21
            r1.mNeedSync = r0     // Catch:{ all -> 0x01fe }
            r0 = r21
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x01fe }
            r19 = r0
            r0 = r21
            r1 = r19
            r0.setNextSelectedPositionInt(r1)     // Catch:{ all -> 0x01fe }
            r0 = r21
            int r0 = r0.mItemCount     // Catch:{ all -> 0x01fe }
            r19 = r0
            if (r19 <= 0) goto L_0x01c2
            r21.checkSelectionChanged()     // Catch:{ all -> 0x01fe }
        L_0x01c2:
            if (r3 != 0) goto L_0x01cc
            r19 = 0
            r0 = r19
            r1 = r21
            r1.mBlockLayoutRequests = r0
        L_0x01cc:
            r19 = 0
            r0 = r19
            r1 = r21
            r1.mNeedLayout = r0
            goto L_0x0008
        L_0x01d6:
            if (r14 == 0) goto L_0x01e5
            int r19 = r14.getTop()     // Catch:{ all -> 0x01fe }
            r0 = r21
            r1 = r19
            android.view.View r18 = r0.fillFromSelection(r1, r7, r6)     // Catch:{ all -> 0x01fe }
            goto L_0x0182
        L_0x01e5:
            r0 = r21
            android.view.View r18 = r0.fillSelection(r7, r6)     // Catch:{ all -> 0x01fe }
            goto L_0x0182
        L_0x01ec:
            r19 = 0
            r0 = r19
            r1 = r21
            r1.mFirstPosition = r0     // Catch:{ all -> 0x01fe }
            r0 = r21
            android.view.View r18 = r0.fillFromTop(r7)     // Catch:{ all -> 0x01fe }
            r21.adjustViewsUpOrDown()     // Catch:{ all -> 0x01fe }
            goto L_0x0182
        L_0x01fe:
            r19 = move-exception
            if (r3 != 0) goto L_0x0209
            r20 = 0
            r0 = r20
            r1 = r21
            r1.mBlockLayoutRequests = r0
        L_0x0209:
            throw r19
        L_0x020a:
            r0 = r21
            int r0 = r0.mItemCount     // Catch:{ all -> 0x01fe }
            r19 = r0
            int r19 = r19 + -1
            r0 = r21
            r1 = r19
            android.view.View r18 = r0.fillUp(r1, r6)     // Catch:{ all -> 0x01fe }
            r21.adjustViewsUpOrDown()     // Catch:{ all -> 0x01fe }
            goto L_0x0182
        L_0x021f:
            r0 = r21
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x01fe }
            r19 = r0
            r0 = r21
            int r0 = r0.mSpecificTop     // Catch:{ all -> 0x01fe }
            r20 = r0
            r0 = r21
            r1 = r19
            r2 = r20
            android.view.View r18 = r0.fillSpecific(r1, r2)     // Catch:{ all -> 0x01fe }
            goto L_0x0182
        L_0x0237:
            r0 = r21
            int r0 = r0.mSyncPosition     // Catch:{ all -> 0x01fe }
            r19 = r0
            r0 = r21
            int r0 = r0.mSpecificTop     // Catch:{ all -> 0x01fe }
            r20 = r0
            r0 = r21
            r1 = r19
            r2 = r20
            android.view.View r18 = r0.fillSpecific(r1, r2)     // Catch:{ all -> 0x01fe }
            goto L_0x0182
        L_0x024f:
            r0 = r21
            android.view.View r18 = r0.moveSelection(r9, r7, r6)     // Catch:{ all -> 0x01fe }
            goto L_0x0182
        L_0x0257:
            r19 = 0
            goto L_0x0175
        L_0x025b:
            r0 = r21
            int r0 = r0.mItemCount     // Catch:{ all -> 0x01fe }
            r19 = r0
            int r13 = r19 + -1
            r0 = r21
            android.widget.ListAdapter r0 = r0.mAdapter     // Catch:{ all -> 0x01fe }
            r19 = r0
            if (r19 == 0) goto L_0x0271
            boolean r19 = r21.isInTouchMode()     // Catch:{ all -> 0x01fe }
            if (r19 == 0) goto L_0x0282
        L_0x0271:
            r19 = -1
        L_0x0273:
            r0 = r21
            r1 = r19
            r0.setSelectedPositionInt(r1)     // Catch:{ all -> 0x01fe }
            r0 = r21
            android.view.View r18 = r0.fillFromBottom(r13, r6)     // Catch:{ all -> 0x01fe }
            goto L_0x0182
        L_0x0282:
            r19 = r13
            goto L_0x0273
        L_0x0285:
            r0 = r21
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x01fe }
            r19 = r0
            if (r19 < 0) goto L_0x02b6
            r0 = r21
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x01fe }
            r19 = r0
            r0 = r21
            int r0 = r0.mItemCount     // Catch:{ all -> 0x01fe }
            r20 = r0
            r0 = r19
            r1 = r20
            if (r0 >= r1) goto L_0x02b6
            r0 = r21
            int r0 = r0.mSelectedPosition     // Catch:{ all -> 0x01fe }
            r19 = r0
            if (r16 != 0) goto L_0x02b1
        L_0x02a7:
            r0 = r21
            r1 = r19
            android.view.View r18 = r0.fillSpecific(r1, r7)     // Catch:{ all -> 0x01fe }
            goto L_0x0182
        L_0x02b1:
            int r7 = r16.getTop()     // Catch:{ all -> 0x01fe }
            goto L_0x02a7
        L_0x02b6:
            r0 = r21
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x01fe }
            r19 = r0
            r0 = r21
            int r0 = r0.mItemCount     // Catch:{ all -> 0x01fe }
            r20 = r0
            r0 = r19
            r1 = r20
            if (r0 >= r1) goto L_0x02df
            r0 = r21
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x01fe }
            r19 = r0
            if (r15 != 0) goto L_0x02da
        L_0x02d0:
            r0 = r21
            r1 = r19
            android.view.View r18 = r0.fillSpecific(r1, r7)     // Catch:{ all -> 0x01fe }
            goto L_0x0182
        L_0x02da:
            int r7 = r15.getTop()     // Catch:{ all -> 0x01fe }
            goto L_0x02d0
        L_0x02df:
            r19 = 0
            r0 = r21
            r1 = r19
            android.view.View r18 = r0.fillSpecific(r1, r7)     // Catch:{ all -> 0x01fe }
            goto L_0x0182
        L_0x02eb:
            r0 = r21
            int r0 = r0.mTouchMode     // Catch:{ all -> 0x01fe }
            r19 = r0
            if (r19 <= 0) goto L_0x0328
            r0 = r21
            int r0 = r0.mTouchMode     // Catch:{ all -> 0x01fe }
            r19 = r0
            r20 = 3
            r0 = r19
            r1 = r20
            if (r0 >= r1) goto L_0x0328
            r0 = r21
            int r0 = r0.mMotionPosition     // Catch:{ all -> 0x01fe }
            r19 = r0
            r0 = r21
            int r0 = r0.mFirstPosition     // Catch:{ all -> 0x01fe }
            r20 = r0
            int r19 = r19 - r20
            r0 = r21
            r1 = r19
            android.view.View r4 = r0.getChildAt(r1)     // Catch:{ all -> 0x01fe }
            if (r4 == 0) goto L_0x0192
            r0 = r21
            int r0 = r0.mMotionPosition     // Catch:{ all -> 0x01fe }
            r19 = r0
            r0 = r21
            r1 = r19
            r0.positionSelector(r1, r4)     // Catch:{ all -> 0x01fe }
            goto L_0x0192
        L_0x0328:
            r0 = r21
            android.graphics.Rect r0 = r0.mSelectorRect     // Catch:{ all -> 0x01fe }
            r19 = r0
            r19.setEmpty()     // Catch:{ all -> 0x01fe }
            goto L_0x0192
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.tvsdk.widget.GridView.layoutChildren():void");
    }

    /* access modifiers changed from: protected */
    public void adjustViewsUpOrDown() {
        int delta;
        int childCount = getChildCount();
        if (childCount > 0) {
            if (!this.mStackFromBottom) {
                delta = getChildAt(0).getTop() - this.mListPadding.top;
                if (this.mFirstPosition != 0) {
                    delta -= this.mVerticalSpacing;
                }
                if (delta < 0) {
                    delta = 0;
                }
            } else {
                int delta2 = getChildAt(childCount - 1).getBottom() - (getHeight() - this.mListPadding.bottom);
                if (this.mFirstPosition + childCount < this.mItemCount) {
                    delta2 += this.mVerticalSpacing;
                }
                if (delta > 0) {
                    delta = 0;
                }
            }
            if (delta != 0) {
                offsetChildrenTopAndBottom(-delta);
            }
        }
    }

    /* access modifiers changed from: protected */
    public View fillFromSelection(int selectedTop, int childrenTop, int childrenBottom) {
        View sel;
        int i;
        int fadingEdgeLength = getVerticalFadingEdgeLength();
        int selectedPosition = this.mSelectedPosition;
        int numColumns = this.mNumColumns;
        int verticalSpacing = this.mVerticalSpacing;
        int rowStart = this.mSelectedPosition;
        int rowEnd = -1;
        if (this.mStackFromBottom) {
            int invertedSelection = (this.mItemCount - 1) - selectedPosition;
            rowEnd = (this.mItemCount - 1) - (invertedSelection - (invertedSelection % numColumns));
            rowStart = Math.max(0, (rowEnd - numColumns) + 1);
        } else if (selectedPosition >= getHeaderViewsCount()) {
            int selectedPosition2 = selectedPosition - getHeaderViewsCount();
            rowStart = (selectedPosition2 - (selectedPosition2 % numColumns)) + getHeaderViewsCount();
        }
        int topSelectionPixel = getTopSelectionPixel(childrenTop, fadingEdgeLength, rowStart);
        int bottomSelectionPixel = getBottomSelectionPixel(childrenBottom, fadingEdgeLength, numColumns, rowStart);
        if (rowStart < getHeaderViewsCount() || rowStart > (this.mItemCount - getFooterViewsCount()) - 1) {
            if (this.mStackFromBottom) {
                i = rowEnd;
            } else {
                i = rowStart;
            }
            sel = makeHeaderOrFooter(i, selectedTop, true);
            numColumns = 1;
        } else {
            sel = makeRow(this.mStackFromBottom ? rowEnd : rowStart, selectedTop, true);
        }
        this.mFirstPosition = rowStart;
        View referenceView = this.mReferenceView;
        adjustForTopFadingEdge(referenceView, topSelectionPixel, bottomSelectionPixel);
        adjustForBottomFadingEdge(referenceView, topSelectionPixel, bottomSelectionPixel);
        if (!this.mStackFromBottom) {
            fillUpWithHeaderOrFooter(rowStart, getNextBottom(rowStart, referenceView));
            adjustViewsUpOrDown();
            fillDownWithHeaderOrFooter(rowStart, getNextTop(rowStart, referenceView));
        } else {
            fillDown(rowEnd + numColumns, referenceView.getBottom() + verticalSpacing);
            adjustViewsUpOrDown();
            fillUp(rowStart - 1, referenceView.getTop() - verticalSpacing);
        }
        return sel;
    }

    /* access modifiers changed from: protected */
    public void adjustForBottomFadingEdge(View childInSelectedRow, int topSelectionPixel, int bottomSelectionPixel) {
        if (childInSelectedRow.getBottom() > bottomSelectionPixel) {
            offsetChildrenTopAndBottom(-Math.min(childInSelectedRow.getTop() - topSelectionPixel, childInSelectedRow.getBottom() - bottomSelectionPixel));
        }
    }

    /* access modifiers changed from: protected */
    public void adjustForTopFadingEdge(View childInSelectedRow, int topSelectionPixel, int bottomSelectionPixel) {
        int top = childInSelectedRow.getTop();
        if (childInSelectedRow instanceof GridViewHeaderViewExpandDistance) {
            top += ((GridViewHeaderViewExpandDistance) childInSelectedRow).getUpExpandDistance();
        }
        int bottom = childInSelectedRow.getBottom();
        if (childInSelectedRow instanceof GridViewHeaderViewExpandDistance) {
            bottom -= ((GridViewHeaderViewExpandDistance) childInSelectedRow).getDownExpandDistance();
        }
        if (top < topSelectionPixel) {
            offsetChildrenTopAndBottom(Math.min(topSelectionPixel - top, bottomSelectionPixel - bottom));
        }
    }

    private int getBottomSelectionPixel(int childrenBottom, int fadingEdgeLength, int numColumns, int rowStart) {
        int bottomSelectionPixel = childrenBottom;
        if ((rowStart + numColumns) - 1 < this.mItemCount - 1) {
            return bottomSelectionPixel - fadingEdgeLength;
        }
        return bottomSelectionPixel;
    }

    private int getTopSelectionPixel(int childrenTop, int fadingEdgeLength, int rowStart) {
        int topSelectionPixel = childrenTop;
        if (rowStart > 0) {
            return topSelectionPixel + fadingEdgeLength;
        }
        return topSelectionPixel;
    }

    /* access modifiers changed from: protected */
    public View fillSelection(int childrenTop, int childrenBottom) {
        int rowStart;
        int i;
        int selectedPosition = reconcileSelectedPosition();
        int numColumns = this.mNumColumns;
        int verticalSpacing = this.mVerticalSpacing;
        int rowEnd = -1;
        if (!this.mStackFromBottom) {
            rowStart = getRowStart(selectedPosition);
        } else {
            int invertedSelection = (this.mItemCount - 1) - selectedPosition;
            rowEnd = (this.mItemCount - 1) - (invertedSelection - (invertedSelection % numColumns));
            rowStart = Math.max(0, (rowEnd - numColumns) + 1);
        }
        int fadingEdgeLength = getVerticalFadingEdgeLength();
        int topSelectionPixel = getTopSelectionPixel(childrenTop, fadingEdgeLength, rowStart);
        if (this.mStackFromBottom) {
            i = rowEnd;
        } else {
            i = rowStart;
        }
        View sel = makeRow(i, topSelectionPixel, true);
        this.mFirstPosition = rowStart;
        View referenceView = this.mReferenceView;
        if (!this.mStackFromBottom) {
            fillDownWithHeaderOrFooter(rowStart, getNextTop(rowStart, referenceView));
            pinToBottom(childrenBottom);
            fillUpWithHeaderOrFooter(rowStart, getNextBottom(rowStart, referenceView));
            adjustViewsUpOrDown();
        } else {
            offsetChildrenTopAndBottom(getBottomSelectionPixel(childrenBottom, fadingEdgeLength, numColumns, rowStart) - referenceView.getBottom());
            fillUp(rowStart - 1, referenceView.getTop() - verticalSpacing);
            pinToTop(childrenTop);
            fillDown(rowEnd + numColumns, referenceView.getBottom() + verticalSpacing);
            adjustViewsUpOrDown();
        }
        return sel;
    }

    /* access modifiers changed from: protected */
    public View fillSelectionMiddle(int childrenTop, int childrenBottom) {
        int rowStart;
        int i;
        int height = getHeight();
        int selectedPosition = reconcileSelectedPosition();
        int numColumns = this.mNumColumns;
        int verticalSpacing = this.mVerticalSpacing;
        int rowEnd = -1;
        if (!this.mStackFromBottom) {
            rowStart = getRowStart(selectedPosition);
        } else {
            int invertedSelection = (this.mItemCount - 1) - selectedPosition;
            rowEnd = (this.mItemCount - 1) - (invertedSelection - (invertedSelection % numColumns));
            rowStart = Math.max(0, (rowEnd - numColumns) + 1);
        }
        int fadingEdgeLength = getVerticalFadingEdgeLength();
        int topSelectionPixel = getTopSelectionPixel(childrenTop, fadingEdgeLength, rowStart);
        int bottomSelectionPixel = getBottomSelectionPixel(childrenBottom, fadingEdgeLength, numColumns, rowStart);
        if (this.mStackFromBottom) {
            i = rowEnd;
        } else {
            i = rowStart;
        }
        View sel = makeRow(i, topSelectionPixel, true);
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            View childView = getChildAt(i2);
            adjustForTopFadingEdge(childView, topSelectionPixel, bottomSelectionPixel);
            adjustForBottomFadingEdge(childView, topSelectionPixel, bottomSelectionPixel);
        }
        this.mFirstPosition = rowStart;
        View referenceView = this.mReferenceView;
        if (!this.mStackFromBottom) {
            fillDownWithHeaderOrFooter(rowStart, getNextTop(rowStart, referenceView));
            pinToBottom(childrenBottom);
            fillUpWithHeaderOrFooter(rowStart, getNextBottom(rowStart, referenceView));
            adjustViewsUpOrDown();
        } else {
            offsetChildrenTopAndBottom(bottomSelectionPixel - referenceView.getBottom());
            fillUp(rowStart - 1, referenceView.getTop() - verticalSpacing);
            pinToTop(childrenTop);
            fillDown(rowEnd + numColumns, referenceView.getBottom() + verticalSpacing);
            adjustViewsUpOrDown();
        }
        return sel;
    }

    private void pinToTop(int childrenTop) {
        int offset;
        if (this.mFirstPosition == 0 && (offset = childrenTop - getChildAt(0).getTop()) < 0) {
            offsetChildrenTopAndBottom(offset);
        }
    }

    private void pinToBottom(int childrenBottom) {
        int offset;
        int count = getChildCount();
        if (this.mFirstPosition + count == this.mItemCount && (offset = childrenBottom - getChildAt(count - 1).getBottom()) > 0) {
            offsetChildrenTopAndBottom(offset);
        }
    }

    /* access modifiers changed from: package-private */
    public int findMotionRow(int y) {
        int childCount = getChildCount();
        if (childCount > 0) {
            int numColumns = this.mNumColumns;
            if (!this.mStackFromBottom) {
                for (int i = 0; i < childCount; i += numColumns) {
                    if (y <= getChildAt(i).getBottom()) {
                        return this.mFirstPosition + i;
                    }
                }
            } else {
                for (int i2 = childCount - 1; i2 >= 0; i2 -= numColumns) {
                    if (y >= getChildAt(i2).getTop()) {
                        return this.mFirstPosition + i2;
                    }
                }
            }
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public View fillSpecific(int position, int top) {
        View temp;
        View below;
        View above;
        int numColumns = this.mNumColumns;
        int motionRowStart = position;
        int motionRowEnd = -1;
        if (this.mStackFromBottom) {
            int invertedSelection = (this.mItemCount - 1) - position;
            motionRowEnd = (this.mItemCount - 1) - (invertedSelection - (invertedSelection % numColumns));
            motionRowStart = Math.max(0, (motionRowEnd - numColumns) + 1);
        } else if (position >= getHeaderViewsCount()) {
            int position2 = position - getHeaderViewsCount();
            motionRowStart = (position2 - (position2 % numColumns)) + getHeaderViewsCount();
        }
        if (motionRowStart < getHeaderViewsCount() || motionRowStart > (this.mItemCount - getFooterViewsCount()) - 1) {
            temp = makeHeaderOrFooter(motionRowStart, top, true);
            numColumns = 1;
        } else {
            temp = makeRow(this.mStackFromBottom ? motionRowEnd : motionRowStart, top, true);
        }
        this.mFirstPosition = motionRowStart;
        View referenceView = this.mReferenceView;
        if (referenceView == null) {
            return null;
        }
        int verticalSpacing = this.mVerticalSpacing;
        if (!this.mStackFromBottom) {
            above = fillUpWithHeaderOrFooter(motionRowStart, getNextBottom(motionRowStart, referenceView));
            adjustViewsUpOrDown();
            below = fillDownWithHeaderOrFooter(motionRowStart, getNextTop(motionRowStart, referenceView));
            int childCount = getChildCount();
            if (childCount > 0) {
                correctTooHigh(numColumns, verticalSpacing, childCount);
            }
        } else {
            below = fillDown(motionRowEnd + numColumns, referenceView.getBottom() + verticalSpacing);
            adjustViewsUpOrDown();
            above = fillUp(motionRowStart - 1, referenceView.getTop() - verticalSpacing);
            int childCount2 = getChildCount();
            if (childCount2 > 0) {
                correctTooLow(numColumns, verticalSpacing, childCount2);
            }
        }
        if (temp != null) {
            return temp;
        }
        if (above != null) {
            return above;
        }
        return below;
    }

    /* access modifiers changed from: protected */
    public void correctTooHigh(int numColumns, int verticalSpacing, int childCount) {
        if ((this.mFirstPosition + childCount) - 1 == this.mItemCount - 1 && childCount > 0) {
            View lastChild = getChildAt(childCount - 1);
            int lastBottom = lastChild.getBottom();
            if (lastChild instanceof GridViewHeaderViewExpandDistance) {
                lastBottom -= ((GridViewHeaderViewExpandDistance) lastChild).getDownExpandDistance();
            }
            int bottomOffset = ((getBottom() - getTop()) - this.mListPadding.bottom) - lastBottom;
            View firstChild = getChildAt(0);
            int firstTop = firstChild.getTop();
            if (firstChild instanceof GridViewHeaderViewExpandDistance) {
                firstTop += ((GridViewHeaderViewExpandDistance) firstChild).getUpExpandDistance();
            }
            if (bottomOffset <= 0) {
                return;
            }
            if (this.mFirstPosition > 0 || firstTop < this.mListPadding.top) {
                if (this.mFirstPosition == 0) {
                    bottomOffset = Math.min(bottomOffset, this.mListPadding.top - firstTop);
                }
                offsetChildrenTopAndBottom(bottomOffset);
                if (this.mFirstPosition > 0) {
                    fillUp(this.mFirstPosition - this.mNumColumns, getNextBottom(this.mFirstPosition - 1, firstChild));
                    adjustViewsUpOrDown();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void correctTooLow(int numColumns, int verticalSpacing, int childCount) {
        if (this.mFirstPosition == 0 && childCount > 0) {
            View firstChild = getChildAt(0);
            int firstTop = firstChild.getTop();
            if (firstChild instanceof GridViewHeaderViewExpandDistance) {
                firstTop += ((GridViewHeaderViewExpandDistance) firstChild).getUpExpandDistance();
            }
            int start = this.mListPadding.top;
            int end = (getBottom() - getTop()) - this.mListPadding.bottom;
            int topOffset = firstTop - start;
            View lastChild = getChildAt(childCount - 1);
            int lastBottom = lastChild.getBottom();
            if (lastChild instanceof GridViewHeaderViewExpandDistance) {
                lastBottom -= ((GridViewHeaderViewExpandDistance) lastChild).getDownExpandDistance();
            }
            int lastPosition = (this.mFirstPosition + childCount) - 1;
            if (topOffset <= 0) {
                return;
            }
            if (lastPosition < this.mItemCount - 1 || lastBottom > end) {
                if (lastPosition == this.mItemCount - 1) {
                    topOffset = Math.min(topOffset, lastBottom - end);
                }
                offsetChildrenTopAndBottom(-topOffset);
                if (lastPosition < this.mItemCount - 1) {
                    fillDown(this.mNumColumns + lastPosition, getNextTop(lastPosition + 1, lastChild));
                    adjustViewsUpOrDown();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public View fillFromTop(int nextTop) {
        this.mFirstPosition = Math.min(this.mFirstPosition, this.mSelectedPosition);
        this.mFirstPosition = Math.min(this.mFirstPosition, this.mItemCount - 1);
        if (this.mFirstPosition < 0) {
            this.mFirstPosition = 0;
        }
        this.mFirstPosition -= this.mFirstPosition % this.mNumColumns;
        return fillDown(this.mFirstPosition, nextTop);
    }

    /* access modifiers changed from: protected */
    public View fillFromBottom(int lastPosition, int nextBottom) {
        int invertedPosition = (this.mItemCount - 1) - Math.min(Math.max(lastPosition, this.mSelectedPosition), this.mItemCount - 1);
        return fillUp((this.mItemCount - 1) - (invertedPosition - (invertedPosition % this.mNumColumns)), nextBottom);
    }

    private View makeHeaderOrFooter(int pos, int y, boolean flow) {
        int i = this.mColumnWidth;
        int i2 = this.mHorizontalSpacing;
        int nextLeft = this.mListPadding.left;
        boolean selected = pos == this.mSelectedPosition;
        this.mReferenceView = makeAndAddView(pos, y, flow, nextLeft, selected, flow ? -1 : 0, (getWidth() - this.mListPadding.left) - this.mListPadding.right);
        boolean hasFocus = shouldShowSelector();
        boolean inClick = touchModeDrawsInPressedState();
        View selectedView = null;
        if (selected && (hasFocus || inClick)) {
            selectedView = this.mReferenceView;
        }
        if (selectedView != null) {
            this.mReferenceViewInSelectedRow = this.mReferenceView;
        }
        return this.mReferenceView;
    }

    public void setFirstColumnMarginleft(int firstColumnMarginleft2) {
        this.firstColumnMarginleft = firstColumnMarginleft2;
    }

    private View makeRow(int startPos, int y, boolean flow) {
        int last;
        int columnWidth = this.mColumnWidth;
        int horizontalSpacing = this.mHorizontalSpacing;
        int nextLeft = this.mListPadding.left + this.firstColumnMarginleft + (this.mStretchMode == 3 ? horizontalSpacing : 0);
        if (!this.mStackFromBottom) {
            last = Math.min(this.mNumColumns + startPos, this.mItemCount - getFooterViewsCount());
        } else {
            last = startPos + 1;
            startPos = Math.max(0, (startPos - this.mNumColumns) + 1);
            if (last - startPos < this.mNumColumns) {
                nextLeft += (this.mNumColumns - (last - startPos)) * (columnWidth + horizontalSpacing) * 1;
            }
        }
        View selectedView = null;
        boolean hasFocus = shouldShowSelector();
        boolean inClick = touchModeDrawsInPressedState();
        int selectedPosition = this.mSelectedPosition;
        View child = null;
        int pos = startPos;
        while (pos < last) {
            boolean selected = pos == selectedPosition;
            child = makeAndAddView(pos, y, flow, nextLeft, selected, flow ? -1 : pos - startPos, this.mColumnWidth);
            int nextLeft2 = nextLeft2 + (columnWidth * 1);
            if (pos < last - 1) {
                nextLeft2 += horizontalSpacing;
            }
            if (selected && (hasFocus || inClick)) {
                selectedView = child;
            }
            pos++;
        }
        this.mReferenceView = child;
        if (selectedView != null) {
            this.mReferenceViewInSelectedRow = this.mReferenceView;
        }
        return selectedView;
    }

    private View fillUpWithHeaderOrFooter(int rowStart, int nextBottom) {
        if (rowStart <= 0) {
            return null;
        }
        if (isHeader(rowStart - 1) || isFooter(rowStart - 1)) {
            return fillUp(rowStart - 1, nextBottom);
        }
        if (isFooter(rowStart) && !isFooter(rowStart - 1)) {
            return fillUp(getRowStart(rowStart - 1), nextBottom);
        }
        if (isHeader(rowStart) || !isHeader(rowStart - 1)) {
            return fillUp(rowStart - this.mNumColumns, nextBottom);
        }
        return fillUp(rowStart - 1, nextBottom);
    }

    /* access modifiers changed from: protected */
    public View fillUp(int pos, int nextBottom) {
        View selectedView = null;
        int end = 0;
        if ((getGroupFlags() & 34) == 34) {
            end = this.mListPadding.top;
        }
        while (true) {
            if ((nextBottom > end || pos >= this.mMinFirstPos) && pos >= 0) {
                if (isHeader(pos)) {
                    View header = makeHeaderOrFooter(pos, nextBottom, false);
                    if (header != null) {
                        selectedView = header;
                    }
                    nextBottom = getNextBottom(pos, this.mReferenceView);
                    this.mFirstPosition = pos;
                    pos--;
                } else if (isFooter(pos)) {
                    View header2 = makeHeaderOrFooter(pos, nextBottom, false);
                    if (header2 != null) {
                        selectedView = header2;
                    }
                    nextBottom = getNextBottom(pos, this.mReferenceView);
                    this.mFirstPosition = pos;
                    pos = getRowStart(pos - 1);
                } else {
                    View temp = makeRow(pos, nextBottom, false);
                    if (temp != null) {
                        selectedView = temp;
                    }
                    nextBottom = getNextBottom(pos, this.mReferenceView);
                    this.mFirstPosition = pos;
                    if (pos - this.mNumColumns > getHeaderViewsCount() - 1) {
                        pos -= this.mNumColumns;
                    } else {
                        pos = getHeaderViewsCount() - 1;
                    }
                }
            }
        }
        if (this.mStackFromBottom) {
            this.mFirstPosition = Math.max(0, pos + 1);
        }
        return selectedView;
    }

    private int getNextBottom(int pos, View referenceView) {
        int nextBottom;
        if (pos == 0) {
            nextBottom = referenceView.getTop();
        } else {
            nextBottom = referenceView.getTop() - this.mVerticalSpacing;
        }
        if (referenceView instanceof GridViewHeaderViewExpandDistance) {
            return nextBottom - ((GridViewHeaderViewExpandDistance) referenceView).getDownExpandDistance();
        }
        return nextBottom;
    }

    private View fillDownWithHeaderOrFooter(int rowStart, int nextTop) {
        if (isHeader(rowStart + 1) || isFooter(rowStart + 1)) {
            return fillDown(rowStart + 1, nextTop);
        }
        if (isHeader(rowStart) && !isHeader(rowStart + 1)) {
            return fillDown(rowStart + 1, nextTop);
        }
        if (isFooter(rowStart) || !isFooter(rowStart + 1)) {
            return fillDown(Math.min(this.mNumColumns + rowStart, this.mItemCount - getFooterViewsCount()), nextTop);
        }
        return fillDown(rowStart + 1, nextTop);
    }

    private View fillDown(int pos, int nextTop) {
        View headerView;
        View selectedView = null;
        int end = getBottom() - getTop();
        if ((getGroupFlags() & 34) == 34) {
            end -= this.mListPadding.bottom;
        }
        while (true) {
            if ((nextTop < end || pos <= this.mMinLastPos) && pos < this.mItemCount) {
                if (pos < getHeaderViewsCount() || pos > (this.mItemCount - getFooterViewsCount()) - 1) {
                    if (pos < getHeaderViewsCount() && (headerView = ((AbsBaseListView.FixedViewInfo) this.mHeaderViewInfos.get(pos)).view) != null && (headerView instanceof GridViewHeaderViewExpandDistance)) {
                        nextTop -= ((GridViewHeaderViewExpandDistance) headerView).getUpExpandDistance();
                    }
                    View header = makeHeaderOrFooter(pos, nextTop, true);
                    if (header != null) {
                        selectedView = header;
                    }
                    nextTop = getNextTop(pos, this.mReferenceView);
                    pos++;
                } else {
                    View temp = makeRow(pos, nextTop, true);
                    if (temp != null) {
                        selectedView = temp;
                    }
                    nextTop = getNextTop(pos, this.mReferenceView);
                    if (this.mNumColumns + pos < this.mItemCount - getFooterViewsCount()) {
                        pos += this.mNumColumns;
                    } else {
                        pos = this.mItemCount - getFooterViewsCount();
                    }
                }
            }
        }
        return selectedView;
    }

    private int getNextTop(int pos, View referenceView) {
        if (referenceView == null) {
            return 0;
        }
        int nextTop = referenceView.getBottom() + this.mVerticalSpacing;
        if (referenceView instanceof GridViewHeaderViewExpandDistance) {
            return nextTop - ((GridViewHeaderViewExpandDistance) referenceView).getDownExpandDistance();
        }
        return nextTop;
    }

    /* access modifiers changed from: protected */
    public int getRowStart(int position) {
        int i = position;
        if (position < getHeaderViewsCount()) {
            return position;
        }
        if (position >= this.mItemCount - getFooterViewsCount()) {
            return position;
        }
        int newPosition = position - getHeaderViewsCount();
        return (newPosition - (newPosition % this.mNumColumns)) + getHeaderViewsCount();
    }

    /* access modifiers changed from: protected */
    public View moveSelection(int delta, int childrenTop, int childrenBottom) {
        int rowStart;
        int oldRowStart;
        View sel;
        View referenceView;
        View sel2;
        View sel3;
        int fadingEdgeLength = getVerticalFadingEdgeLength();
        int selectedPosition = this.mSelectedPosition;
        int numColumns = this.mNumColumns;
        int verticalSpacing = this.mVerticalSpacing;
        int rowEnd = -1;
        if (!this.mStackFromBottom) {
            oldRowStart = getRowStart(selectedPosition - delta);
            rowStart = getRowStart(selectedPosition);
        } else {
            int invertedSelection = (this.mItemCount - 1) - selectedPosition;
            rowEnd = (this.mItemCount - 1) - (invertedSelection - (invertedSelection % numColumns));
            rowStart = Math.max(0, (rowEnd - numColumns) + 1);
            int invertedSelection2 = (this.mItemCount - 1) - (selectedPosition - delta);
            oldRowStart = Math.max(0, (((this.mItemCount - 1) - (invertedSelection2 - (invertedSelection2 % numColumns))) - numColumns) + 1);
        }
        int rowDelta = rowStart - oldRowStart;
        int topSelectionPixel = getTopSelectionPixel(childrenTop, fadingEdgeLength, rowStart);
        int bottomSelectionPixel = getBottomSelectionPixel(childrenBottom, fadingEdgeLength, numColumns, rowStart);
        this.mFirstPosition = rowStart;
        if (rowDelta > 0) {
            int oldBottom = this.mReferenceViewInSelectedRow == null ? 0 : this.mReferenceViewInSelectedRow.getBottom();
            if (this.mReferenceViewInSelectedRow != null && (this.mReferenceViewInSelectedRow instanceof GridViewHeaderViewExpandDistance)) {
                oldBottom -= ((GridViewHeaderViewExpandDistance) this.mReferenceViewInSelectedRow).getDownExpandDistance();
            }
            if (rowStart < getHeaderViewsCount() || rowStart > (this.mItemCount - 1) - getFooterViewsCount()) {
                sel = makeHeaderOrFooter(rowStart, oldBottom + verticalSpacing, true);
            } else {
                sel = makeRow(this.mStackFromBottom ? rowEnd : rowStart, oldBottom + verticalSpacing, true);
            }
            referenceView = this.mReferenceView;
            adjustForBottomFadingEdge(referenceView, topSelectionPixel, bottomSelectionPixel);
        } else if (rowDelta < 0) {
            int oldTop = this.mReferenceViewInSelectedRow == null ? 0 : this.mReferenceViewInSelectedRow.getTop();
            if (rowStart < getHeaderViewsCount() || rowStart > (this.mItemCount - 1) - getFooterViewsCount()) {
                sel3 = makeHeaderOrFooter(rowStart, oldTop - verticalSpacing, false);
            } else {
                sel3 = makeRow(this.mStackFromBottom ? rowEnd : rowStart, oldTop - verticalSpacing, false);
            }
            referenceView = this.mReferenceView;
            adjustForTopFadingEdge(referenceView, topSelectionPixel, bottomSelectionPixel);
        } else {
            int oldTop2 = this.mReferenceViewInSelectedRow == null ? 0 : this.mReferenceViewInSelectedRow.getTop();
            if (rowStart < getHeaderViewsCount() || rowStart > (this.mItemCount - 1) - getFooterViewsCount()) {
                sel2 = makeHeaderOrFooter(rowStart, oldTop2, true);
            } else {
                sel2 = makeRow(this.mStackFromBottom ? rowEnd : rowStart, oldTop2, true);
            }
            referenceView = this.mReferenceView;
        }
        if (!this.mStackFromBottom) {
            fillUpWithHeaderOrFooter(rowStart, getNextBottom(rowStart, referenceView));
            adjustViewsUpOrDown();
            fillDownWithHeaderOrFooter(rowStart, getNextTop(rowStart, referenceView));
        } else {
            fillDown(rowEnd + numColumns, referenceView.getBottom() + verticalSpacing);
            adjustViewsUpOrDown();
            fillUp(rowStart - 1, referenceView.getTop() - verticalSpacing);
        }
        return sel;
    }

    private View makeAndAddView(int position, int y, boolean flow, int childrenLeft, boolean selected, int where, int width) {
        View child;
        if (this.mDataChanged || (child = this.mRecycler.getActiveView(position)) == null) {
            View child2 = obtainView(position, this.mIsScrap);
            setupChild(child2, position, y, flow, childrenLeft, selected, this.mIsScrap[0], where, width);
            return child2;
        }
        setupChild(child, position, y, flow, childrenLeft, selected, true, where, width);
        return child;
    }

    private void setupChild(View child, int position, int y, boolean flow, int childrenLeft, boolean selected, boolean recycled, int where, int width) {
        int childLeft;
        boolean isSelected = selected && shouldShowSelector();
        boolean updateChildSelected = isSelected != child.isSelected();
        int mode = this.mTouchMode;
        boolean isPressed = mode > 0 && mode < 3 && this.mMotionPosition == position;
        boolean updateChildPressed = isPressed != child.isPressed();
        boolean needToMeasure = !recycled || updateChildSelected || child.isLayoutRequested();
        AbsBaseListView.LayoutParams p = (AbsBaseListView.LayoutParams) child.getLayoutParams();
        if (p == null) {
            p = (AbsBaseListView.LayoutParams) generateDefaultLayoutParams();
        }
        p.viewType = this.mAdapter.getItemViewType(position);
        if (!recycled || p.forceAdd) {
            p.forceAdd = false;
            addViewInLayout(child, where, p, true);
        } else {
            attachViewToParent(child, where, p);
        }
        if (updateChildSelected) {
            child.setSelected(isSelected);
            if (isSelected) {
            }
        }
        if (updateChildPressed) {
            child.setPressed(isPressed);
        }
        if (!(this.mChoiceMode == 0 || this.mCheckStates == null)) {
            if (child instanceof Checkable) {
                ((Checkable) child).setChecked(this.mCheckStates.get(position));
            } else if (getContext().getApplicationInfo().targetSdkVersion >= 11) {
                child.setActivated(this.mCheckStates.get(position));
            }
        }
        if (needToMeasure) {
            child.measure(getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(width, UCCore.VERIFY_POLICY_QUICK), 0, p.width), getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(0, 0), 0, p.height));
        } else {
            cleanupLayoutState(child);
        }
        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();
        int childTop = flow ? y : y - h;
        if (!flow && (child instanceof GridViewHeaderViewExpandDistance)) {
            childTop += ((GridViewHeaderViewExpandDistance) child).getDownExpandDistance();
        }
        switch (1) {
            case 1:
                childLeft = childrenLeft + ((width - w) / 2);
                break;
            case 3:
                childLeft = childrenLeft;
                break;
            case 5:
                childLeft = (childrenLeft + width) - w;
                break;
            default:
                childLeft = childrenLeft;
                break;
        }
        if (needToMeasure) {
            child.layout(childLeft, childTop, childLeft + w, childTop + h);
        } else {
            child.offsetLeftAndRight(childLeft - child.getLeft());
            child.offsetTopAndBottom(childTop - child.getTop());
        }
        if (this.mCachingStarted) {
            child.setDrawingCacheEnabled(true);
        }
        if (recycled && ((AbsBaseListView.LayoutParams) child.getLayoutParams()).scrappedFromPosition != position) {
            child.jumpDrawablesToCurrentState();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return commonKey(keyCode, 1, event);
    }

    private boolean commonKey(int keyCode, int count, KeyEvent event) {
        if (this.mAdapter == null) {
            return false;
        }
        if (this.mDataChanged) {
            layoutChildren();
        }
        boolean handled = false;
        int action = event.getAction();
        int navigation = 1;
        if (action != 1) {
            switch (keyCode) {
                case 19:
                    if (event.hasNoModifiers()) {
                        handled = resurrectSelectionIfNeeded() || arrowScroll(33);
                    } else if (event.hasModifiers(2)) {
                        handled = resurrectSelectionIfNeeded() || fullScroll(33);
                    }
                    navigation = 2;
                    break;
                case 20:
                    if (event.hasNoModifiers()) {
                        handled = resurrectSelectionIfNeeded() || arrowScroll(130);
                    } else if (event.hasModifiers(2)) {
                        handled = resurrectSelectionIfNeeded() || fullScroll(130);
                    }
                    navigation = 4;
                    break;
                case 21:
                    if (event.hasNoModifiers()) {
                        if (!resurrectSelectionIfNeeded() && !arrowScroll(17)) {
                            handled = false;
                            break;
                        } else {
                            handled = true;
                            break;
                        }
                    }
                    break;
                case 22:
                    if (event.hasNoModifiers()) {
                        if (resurrectSelectionIfNeeded() || arrowScroll(66)) {
                            handled = true;
                        } else {
                            handled = false;
                        }
                    }
                    navigation = 3;
                    break;
                case 23:
                case 66:
                    if (event.hasNoModifiers() && !(handled = resurrectSelectionIfNeeded()) && event.getRepeatCount() == 0 && getChildCount() > 0) {
                        keyPressed();
                        handled = true;
                        break;
                    }
                case 92:
                    if (event.hasNoModifiers()) {
                        handled = resurrectSelectionIfNeeded() || pageScroll(33);
                    } else if (event.hasModifiers(2)) {
                        handled = resurrectSelectionIfNeeded() || fullScroll(33);
                    }
                    navigation = 2;
                    break;
                case 93:
                    if (event.hasNoModifiers()) {
                        handled = resurrectSelectionIfNeeded() || pageScroll(130);
                    } else if (event.hasModifiers(2)) {
                        handled = resurrectSelectionIfNeeded() || fullScroll(130);
                    }
                    navigation = 4;
                    break;
                case SecExceptionCode.SEC_ERROR_INIT_NO_DATA_FILE:
                    if (event.hasNoModifiers()) {
                        if (!resurrectSelectionIfNeeded() && !fullScroll(33)) {
                            handled = false;
                            break;
                        } else {
                            handled = true;
                            break;
                        }
                    }
                    break;
                case 123:
                    if (event.hasNoModifiers()) {
                        if (!resurrectSelectionIfNeeded() && !fullScroll(130)) {
                            handled = false;
                            break;
                        } else {
                            handled = true;
                            break;
                        }
                    }
                    break;
            }
        }
        if (handled) {
            playSoundEffect(navigation);
            return true;
        }
        switch (action) {
            case 0:
                return super.onKeyDown(keyCode, event);
            case 1:
                return super.onKeyUp(keyCode, event);
            case 2:
                return super.onKeyMultiple(keyCode, count, event);
            default:
                return false;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean arrowScroll(int direction) {
        int endOfRowPos;
        int startOfRowPos;
        int selectedPosition = this.mSelectedPosition;
        int numColumns = this.mNumColumns;
        boolean moved = false;
        if (this.mStackFromBottom) {
            endOfRowPos = (this.mItemCount - 1) - ((((this.mItemCount - 1) - selectedPosition) / numColumns) * numColumns);
            startOfRowPos = Math.max(0, (endOfRowPos - numColumns) + 1);
        } else if (isHeader(selectedPosition) || isFooter(selectedPosition)) {
            startOfRowPos = selectedPosition;
            endOfRowPos = selectedPosition;
        } else {
            startOfRowPos = getRowStart(selectedPosition);
            endOfRowPos = Math.min((startOfRowPos + numColumns) - 1, (this.mItemCount - 1) - getFooterViewsCount());
        }
        switch (direction) {
            case 17:
                if (selectedPosition > startOfRowPos) {
                    this.mLayoutMode = 6;
                    setSelectionInt(Math.max(0, selectedPosition - 1));
                    moved = true;
                    break;
                }
                break;
            case 33:
                if (startOfRowPos > 0 && selectedPosition > 0) {
                    this.mLayoutMode = 6;
                    if (isHeader(selectedPosition - 1) || isFooter(selectedPosition - 1)) {
                        setSelectionInt(Math.max(0, selectedPosition - 1));
                    } else if (isFooter(selectedPosition) && !isFooter(selectedPosition - 1)) {
                        setSelectionInt(Math.max(selectedPosition - 1, getHeaderViewsCount()));
                    } else if (isHeader(selectedPosition) || !isHeader(selectedPosition - this.mNumColumns)) {
                        setSelectionInt(Math.max(0, selectedPosition - this.mNumColumns));
                    } else {
                        setSelectionInt(Math.max(getHeaderViewsCount() - 1, 0));
                    }
                    moved = true;
                    break;
                }
                break;
            case 66:
                if (selectedPosition < endOfRowPos) {
                    this.mLayoutMode = 6;
                    setSelectionInt(Math.min(selectedPosition + 1, this.mItemCount - 1));
                    moved = true;
                    break;
                }
                break;
            case 130:
                if (endOfRowPos < this.mItemCount - 1 && selectedPosition < this.mItemCount) {
                    this.mLayoutMode = 6;
                    if (isHeader(selectedPosition + 1) || isFooter(selectedPosition + 1)) {
                        setSelectionInt(Math.min(selectedPosition + 1, this.mItemCount - 1));
                    } else if (isHeader(selectedPosition) && !isHeader(selectedPosition + 1)) {
                        setSelectionInt(Math.min(selectedPosition + 1, (this.mItemCount - 1) - getFooterViewsCount()));
                    } else if (isFooter(selectedPosition) || !isFooter(this.mNumColumns + selectedPosition)) {
                        setSelectionInt(Math.min(this.mNumColumns + selectedPosition, (this.mItemCount - 1) - getFooterViewsCount()));
                    } else {
                        setSelectionInt(Math.min(this.mItemCount - getFooterViewsCount(), this.mItemCount - 1));
                    }
                    moved = true;
                    break;
                }
                break;
        }
        if (moved) {
        }
        if (moved) {
            awakenScrollBars();
        }
        return moved;
    }

    /* access modifiers changed from: package-private */
    public boolean isHeader(int position) {
        return position < getHeaderViewsCount();
    }

    /* access modifiers changed from: package-private */
    public boolean isFooter(int position) {
        return position > (this.mItemCount - getFooterViewsCount()) + -1;
    }

    public void setSelection(int position) {
        if (!isInTouchMode()) {
            setNextSelectedPositionInt(position);
        } else {
            this.mResurrectToPosition = position;
        }
        this.mLayoutMode = 2;
        this.mNeedLayout = true;
        layoutChildren();
    }

    /* access modifiers changed from: package-private */
    public void setSelectionInt(int position) {
        int previous;
        int previousSelectedPosition = this.mNextSelectedPosition;
        setNextSelectedPositionInt(position);
        this.mNeedLayout = true;
        layoutChildren();
        int next = this.mStackFromBottom ? (this.mItemCount - 1) - this.mNextSelectedPosition : this.mNextSelectedPosition;
        if (this.mStackFromBottom) {
            previous = (this.mItemCount - 1) - previousSelectedPosition;
        } else {
            previous = previousSelectedPosition;
        }
        if (next / this.mNumColumns != previous / this.mNumColumns) {
            awakenScrollBars();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean fullScroll(int direction) {
        boolean moved = false;
        if (direction == 33) {
            this.mLayoutMode = 2;
            setSelectionInt(0);
            moved = true;
        } else if (direction == 130) {
            this.mLayoutMode = 2;
            setSelectionInt(this.mItemCount - 1);
            moved = true;
        }
        if (moved) {
            awakenScrollBars();
        }
        return moved;
    }

    /* access modifiers changed from: package-private */
    public boolean pageScroll(int direction) {
        int nextPage = -1;
        if (direction == 33) {
            nextPage = Math.max(0, this.mSelectedPosition - getChildCount());
        } else if (direction == 130) {
            nextPage = Math.min(this.mItemCount - 1, this.mSelectedPosition + getChildCount());
        }
        if (nextPage < 0) {
            return false;
        }
        setSelectionInt(nextPage);
        awakenScrollBars();
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean sequenceScroll(int direction) {
        int endOfRow;
        int startOfRow;
        int selectedPosition = this.mSelectedPosition;
        int numColumns = this.mNumColumns;
        int count = this.mItemCount;
        if (!this.mStackFromBottom) {
            startOfRow = (selectedPosition / numColumns) * numColumns;
            endOfRow = Math.min((startOfRow + numColumns) - 1, count - 1);
        } else {
            endOfRow = (count - 1) - ((((count - 1) - selectedPosition) / numColumns) * numColumns);
            startOfRow = Math.max(0, (endOfRow - numColumns) + 1);
        }
        boolean moved = false;
        boolean showScroll = false;
        switch (direction) {
            case 1:
                if (selectedPosition > 0) {
                    this.mLayoutMode = 6;
                    setSelectionInt(selectedPosition - 1);
                    moved = true;
                    if (selectedPosition != startOfRow) {
                        showScroll = false;
                        break;
                    } else {
                        showScroll = true;
                        break;
                    }
                }
                break;
            case 2:
                if (selectedPosition < count - 1) {
                    this.mLayoutMode = 6;
                    setSelectionInt(selectedPosition + 1);
                    moved = true;
                    if (selectedPosition != endOfRow) {
                        showScroll = false;
                        break;
                    } else {
                        showScroll = true;
                        break;
                    }
                }
                break;
        }
        if (moved) {
        }
        if (showScroll) {
            awakenScrollBars();
        }
        return moved;
    }

    public void setStretchMode(int stretchMode) {
        if (stretchMode != this.mStretchMode) {
            this.mStretchMode = stretchMode;
            requestLayoutIfNecessary();
        }
    }
}
