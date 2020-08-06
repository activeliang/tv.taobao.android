package com.yunos.tvtaobao.tvsdk.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.SpinnerAdapter;
import com.yunos.tvtaobao.tvsdk.widget.AdapterView;

public abstract class AbsSpinner extends AdapterView<SpinnerAdapter> {
    SpinnerAdapter mAdapter;
    boolean mByPosition;
    private DataSetObserver mDataSetObserver;
    private boolean mDrawSelectorOnTop;
    int mHeightMeasureSpec;
    protected final RecycleBin mRecycler;
    int mSelectionBottomPadding;
    int mSelectionLeftPadding;
    int mSelectionRightPadding;
    int mSelectionTopPadding;
    protected Drawable mSelector;
    Rect mSelectorRect;
    protected final Rect mSpinnerPadding;
    private Rect mTouchFrame;
    int mWidthMeasureSpec;

    /* access modifiers changed from: protected */
    public abstract void layout(int i, boolean z);

    public AbsSpinner(Context context) {
        super(context);
        this.mSelectionLeftPadding = 0;
        this.mSelectionTopPadding = 0;
        this.mSelectionRightPadding = 0;
        this.mSelectionBottomPadding = 0;
        this.mSpinnerPadding = new Rect();
        this.mRecycler = new RecycleBin();
        this.mByPosition = false;
        this.mDrawSelectorOnTop = true;
        this.mSelectorRect = new Rect();
        initAbsSpinner();
    }

    public AbsSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mSelectionLeftPadding = 0;
        this.mSelectionTopPadding = 0;
        this.mSelectionRightPadding = 0;
        this.mSelectionBottomPadding = 0;
        this.mSpinnerPadding = new Rect();
        this.mRecycler = new RecycleBin();
        this.mByPosition = false;
        this.mDrawSelectorOnTop = true;
        this.mSelectorRect = new Rect();
        initAbsSpinner();
    }

    public void setRecycleByPosition(boolean byPosition) {
        this.mByPosition = byPosition;
    }

    public void setSelector(Drawable selector) {
        this.mSelector = selector;
        Rect padding = new Rect();
        this.mSelector.getPadding(padding);
        setSelectorPadding(padding.left, padding.top, padding.right, padding.bottom);
    }

    public void setSelector(int selectorId) {
        this.mSelector = getContext().getResources().getDrawable(selectorId);
        Rect padding = new Rect();
        this.mSelector.getPadding(padding);
        setSelectorPadding(padding.left, padding.top, padding.right, padding.bottom);
    }

    public void setSelectorPadding(int leftPadding, int topPadding, int rightPadding, int bottomPadding) {
        this.mSelectionLeftPadding = leftPadding;
        this.mSelectionTopPadding = topPadding;
        this.mSelectionRightPadding = rightPadding;
        this.mSelectionBottomPadding = bottomPadding;
    }

    public void setDrawSelectorOnTop(boolean onTop) {
        this.mDrawSelectorOnTop = onTop;
    }

    public boolean drawSclectorOnTop() {
        return this.mDrawSelectorOnTop;
    }

    /* access modifiers changed from: protected */
    public void drawSelector(Canvas canvas) {
        if (hasFocus() && this.mSelector != null && this.mSelectorRect != null && !this.mSelectorRect.isEmpty()) {
            this.mSelector.setBounds(new Rect(this.mSelectorRect));
            this.mSelector.draw(canvas);
        }
    }

    /* access modifiers changed from: protected */
    public void positionSelector(int l, int t, int r, int b) {
        this.mSelectorRect.set(l - this.mSelectionLeftPadding, t - this.mSelectionTopPadding, this.mSelectionRightPadding + r, this.mSelectionBottomPadding + b);
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        int saveCount = 0;
        boolean clipToPadding = (getGroupFlags() & 34) == 34;
        if (clipToPadding) {
            saveCount = canvas.save();
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            canvas.clipRect(getPaddingLeft() + scrollX, getPaddingTop() + scrollY, ((getRight() + scrollX) - getLeft()) - getPaddingRight(), ((getBottom() + scrollY) - getTop()) - getPaddingBottom());
            setGroupFlags(getGroupFlags() & -35);
        }
        boolean drawSelectorOnTop = drawSclectorOnTop();
        if (!drawSelectorOnTop) {
            drawSelector(canvas);
        }
        super.dispatchDraw(canvas);
        if (drawSelectorOnTop) {
            drawSelector(canvas);
        }
        if (clipToPadding) {
            canvas.restoreToCount(saveCount);
            setGroupFlags(getGroupFlags() & -35);
        }
    }

    private void initAbsSpinner() {
        setFocusable(true);
        setWillNotDraw(false);
    }

    public void setAdapter(SpinnerAdapter adapter) {
        int position = -1;
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
            resetList();
        }
        this.mAdapter = adapter;
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        if (this.mAdapter != null) {
            this.mOldItemCount = this.mItemCount;
            this.mItemCount = this.mAdapter.getCount();
            checkFocus();
            this.mDataSetObserver = new AdapterView.AdapterDataSetObserver();
            this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
            if (this.mItemCount > 0) {
                position = 0;
            }
            setSelectedPositionInt(position);
            setNextSelectedPositionInt(position);
            if (this.mItemCount == 0) {
                checkSelectionChanged();
            }
        } else {
            checkFocus();
            resetList();
            checkSelectionChanged();
        }
        requestLayout();
    }

    /* access modifiers changed from: protected */
    public void resetList() {
        this.mDataChanged = false;
        this.mNeedSync = false;
        removeAllViewsInLayout();
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        setSelectedPositionInt(-1);
        setNextSelectedPositionInt(-1);
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        this.mSpinnerPadding.left = getPaddingLeft() + this.mSelectionLeftPadding;
        this.mSpinnerPadding.top = getPaddingTop() + this.mSelectionTopPadding;
        this.mSpinnerPadding.right = getPaddingRight() + this.mSelectionRightPadding;
        this.mSpinnerPadding.bottom = getPaddingBottom() + this.mSelectionBottomPadding;
        if (this.mDataChanged) {
            handleDataChanged();
        }
        int preferredHeight = 0;
        int preferredWidth = 0;
        boolean needsMeasuring = true;
        int selectedPosition = getSelectedItemPosition();
        if (selectedPosition >= 0 && this.mAdapter != null && selectedPosition < this.mAdapter.getCount()) {
            View view = this.mRecycler.get(selectedPosition);
            if (view == null) {
                view = this.mAdapter.getView(selectedPosition, (View) null, this);
            }
            if (view != null) {
                this.mRecycler.put(selectedPosition, view);
            }
            if (view != null) {
                if (view.getLayoutParams() == null) {
                    this.mBlockLayoutRequests = true;
                    view.setLayoutParams(generateDefaultLayoutParams());
                    this.mBlockLayoutRequests = false;
                }
                measureChild(view, widthMeasureSpec, heightMeasureSpec);
                preferredHeight = getChildHeight(view) + this.mSpinnerPadding.top + this.mSpinnerPadding.bottom;
                preferredWidth = getChildWidth(view) + this.mSpinnerPadding.left + this.mSpinnerPadding.right;
                needsMeasuring = false;
            }
        }
        if (needsMeasuring) {
            preferredHeight = this.mSpinnerPadding.top + this.mSpinnerPadding.bottom;
            if (widthMode == 0) {
                preferredWidth = this.mSpinnerPadding.left + this.mSpinnerPadding.right;
            }
        }
        int preferredHeight2 = Math.max(preferredHeight, getSuggestedMinimumHeight());
        int preferredWidth2 = Math.max(preferredWidth, getSuggestedMinimumWidth());
        setMeasuredDimension(resolveSizeAndState(preferredWidth2, widthMeasureSpec, 0), resolveSizeAndState(preferredHeight2, heightMeasureSpec, 0));
        this.mHeightMeasureSpec = heightMeasureSpec;
        this.mWidthMeasureSpec = widthMeasureSpec;
    }

    /* access modifiers changed from: package-private */
    public int getChildHeight(View child) {
        return child.getMeasuredHeight();
    }

    /* access modifiers changed from: package-private */
    public int getChildWidth(View child) {
        return child.getMeasuredWidth();
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.LayoutParams(-1, -2);
    }

    /* access modifiers changed from: protected */
    public void recycleAllViews() {
        int childCount = getChildCount();
        RecycleBin recycleBin = this.mRecycler;
        int position = this.mFirstPosition;
        for (int i = 0; i < childCount; i++) {
            recycleBin.put(position + i, getChildAt(i));
        }
    }

    public void setSelection(int position, boolean animate) {
        setSelectionInt(position, animate && this.mFirstPosition <= position && position <= (this.mFirstPosition + getChildCount()) + -1);
    }

    public void setSelection(int position) {
        setNextSelectedPositionInt(position);
        requestLayout();
        invalidate();
    }

    /* access modifiers changed from: package-private */
    public void setSelectionInt(int position, boolean animate) {
        if (position != this.mOldSelectedPosition) {
            this.mBlockLayoutRequests = true;
            setNextSelectedPositionInt(position);
            layout(position - this.mSelectedPosition, animate);
            this.mBlockLayoutRequests = false;
        }
    }

    public View getSelectedView() {
        if (this.mItemCount <= 0 || this.mSelectedPosition < 0) {
            return null;
        }
        return getChildAt(this.mSelectedPosition - this.mFirstPosition);
    }

    public void requestLayout() {
        if (!this.mBlockLayoutRequests) {
            super.requestLayout();
        }
    }

    public SpinnerAdapter getAdapter() {
        return this.mAdapter;
    }

    public int getCount() {
        return this.mItemCount;
    }

    public int pointToPosition(int x, int y) {
        Rect frame = this.mTouchFrame;
        if (frame == null) {
            this.mTouchFrame = new Rect();
            frame = this.mTouchFrame;
        }
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    return this.mFirstPosition + i;
                }
            }
        }
        return -1;
    }

    static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int position;
        long selectedId;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.selectedId = in.readLong();
            this.position = in.readInt();
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeLong(this.selectedId);
            out.writeInt(this.position);
        }

        public String toString() {
            return "AbsSpinner.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " selectedId=" + this.selectedId + " position=" + this.position + "}";
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.selectedId = getSelectedItemId();
        if (ss.selectedId >= 0) {
            ss.position = getSelectedItemPosition();
        } else {
            ss.position = -1;
        }
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.selectedId >= 0) {
            this.mDataChanged = true;
            this.mNeedSync = true;
            this.mSyncRowId = ss.selectedId;
            this.mSyncPosition = ss.position;
            this.mSyncMode = 0;
            requestLayout();
        }
    }

    public class RecycleBin {
        private final SparseArray<View> mScrapHeap = new SparseArray<>();

        public RecycleBin() {
        }

        public void put(int position, View v) {
            this.mScrapHeap.put(position, v);
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x001f, code lost:
            r4 = r3.mScrapHeap.keyAt(0);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public android.view.View get(int r4) {
            /*
                r3 = this;
                android.util.SparseArray<android.view.View> r1 = r3.mScrapHeap
                java.lang.Object r0 = r1.get(r4)
                android.view.View r0 = (android.view.View) r0
                if (r0 == 0) goto L_0x000f
                android.util.SparseArray<android.view.View> r1 = r3.mScrapHeap
                r1.delete(r4)
            L_0x000f:
                if (r0 != 0) goto L_0x0035
                com.yunos.tvtaobao.tvsdk.widget.AbsSpinner r1 = com.yunos.tvtaobao.tvsdk.widget.AbsSpinner.this
                boolean r1 = r1.mByPosition
                if (r1 == 0) goto L_0x0035
                android.util.SparseArray<android.view.View> r1 = r3.mScrapHeap
                int r1 = r1.size()
                if (r1 <= 0) goto L_0x0035
                android.util.SparseArray<android.view.View> r1 = r3.mScrapHeap
                r2 = 0
                int r4 = r1.keyAt(r2)
                android.util.SparseArray<android.view.View> r1 = r3.mScrapHeap
                java.lang.Object r0 = r1.get(r4)
                android.view.View r0 = (android.view.View) r0
                if (r0 == 0) goto L_0x0035
                android.util.SparseArray<android.view.View> r1 = r3.mScrapHeap
                r1.delete(r4)
            L_0x0035:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.tvsdk.widget.AbsSpinner.RecycleBin.get(int):android.view.View");
        }

        public void clear() {
            SparseArray<View> scrapHeap = this.mScrapHeap;
            int count = scrapHeap.size();
            for (int i = 0; i < count; i++) {
                View view = scrapHeap.valueAt(i);
                if (view != null) {
                    AbsSpinner.this.removeDetachedView(view, true);
                }
            }
            scrapHeap.clear();
        }
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(AbsSpinner.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(AbsSpinner.class.getName());
    }
}
