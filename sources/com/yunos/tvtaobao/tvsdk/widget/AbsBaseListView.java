package com.yunos.tvtaobao.tvsdk.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.LongSparseArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.StateSet;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;
import com.yunos.tvtaobao.tvsdk.utils.ReflectUtils;
import com.yunos.tvtaobao.tvsdk.widget.AdapterView;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.OnScrollListener;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbsBaseListView extends AdapterView<ListAdapter> {
    public static final int CHOICE_MODE_MULTIPLE = 2;
    public static final int CHOICE_MODE_MULTIPLE_MODAL = 3;
    public static final int CHOICE_MODE_NONE = 0;
    public static final int CHOICE_MODE_SINGLE = 1;
    private static final boolean DEBUG = false;
    static final int FLAG_DISALLOW_INTERCEPT = 524288;
    protected static final int FLAG_SUPPORT_STATIC_TRANSFORMATIONS = 2048;
    protected static final int FLAG_USE_CHILD_DRAWING_ORDER = 1024;
    static final int INVALID_POINTER = -1;
    protected static final int LAYOUT_FORCE_BOTTOM = 3;
    protected static final int LAYOUT_FORCE_LEFT = 7;
    protected static final int LAYOUT_FORCE_RIGHT = 8;
    protected static final int LAYOUT_FORCE_TOP = 1;
    protected static final int LAYOUT_FROM_MIDDLE = 9;
    protected static final int LAYOUT_MOVE_SELECTION = 6;
    protected static final int LAYOUT_NORMAL = 0;
    protected static final int LAYOUT_SET_SELECTION = 2;
    protected static final int LAYOUT_SPECIFIC = 4;
    protected static final int LAYOUT_SYNC = 5;
    static final float MAX_SCROLL_FACTOR = 0.33f;
    static final int NO_POSITION = -1;
    static final boolean PROFILE_FLINGING = false;
    private static final String TAG = "AbsBaseListView";
    static final int TOUCH_MODE_DONE_WAITING = 2;
    protected static final int TOUCH_MODE_DOWN = 0;
    static final int TOUCH_MODE_FLING = 4;
    static final int TOUCH_MODE_OVERFLING = 6;
    static final int TOUCH_MODE_OVERSCROLL = 5;
    static final int TOUCH_MODE_REST = -1;
    protected static final int TOUCH_MODE_SCROLL = 3;
    static final int TOUCH_MODE_TAP = 1;
    ListAdapter mAdapter;
    boolean mAdapterHasStableIds;
    boolean mAreAllItemsSelectable;
    protected boolean mBlockLayoutRequests;
    boolean mCachingActive;
    boolean mCachingStarted;
    SparseBooleanArray mCheckStates;
    LongSparseArray<Integer> mCheckedIdStates;
    int mCheckedItemCount;
    ActionMode mChoiceActionMode;
    int mChoiceMode;
    private Runnable mClearScrollingCache;
    ContextMenu.ContextMenuInfo mContextMenuInfo;
    private DataSetObserver mDataSetObserver;
    int mDividerWidth;
    private int mDownTouchPosition;
    private View mDownTouchView;
    private boolean mDrawSelectorOnTop;
    private Rect mExactlyUserSelectedRect;
    protected ArrayList<FixedViewInfo> mFooterViewInfos;
    protected ArrayList<FixedViewInfo> mHeaderViewInfos;
    int mHeightMeasureSpec;
    boolean mIsAttached;
    private boolean mIsChildViewEnabled;
    protected final boolean[] mIsScrap;
    int mItemHeight;
    int mItemWidth;
    protected int mLastScrollState;
    protected int mLayoutMode;
    protected final Rect mListPadding;
    protected int mMotionPosition;
    MultiChoiceModeWrapper mMultiChoiceModeCallback;
    protected boolean mNeedLayout;
    private OnScrollListener mOnScrollListener;
    CheckForLongPress mPendingCheckForLongPress;
    PerformClick mPerformClick;
    int mPreLoadCount;
    protected final RecycleBin mRecycler;
    int mResurrectToPosition;
    Adapter mScalableAdapter;
    final RecycleBin mScalableRecycler;
    View mScalableView;
    int mScalableViewSpacing;
    private boolean mScrollingCacheEnabled;
    int mSelectedLeft;
    int mSelectionBottomPadding;
    int mSelectionLeftPadding;
    int mSelectionRightPadding;
    int mSelectionTopPadding;
    protected Drawable mSelector;
    int mSelectorPosition;
    protected Rect mSelectorRect;
    protected int mSpacing;
    final Rect mTempRect;
    private Rect mTouchFrame;
    protected int mTouchMode;
    VelocityTracker mVelocityTracker;
    int mWidthMeasureSpec;
    protected boolean needMeasureSelectedView;

    public interface MultiChoiceModeListener extends ActionMode.Callback {
        void onItemCheckedStateChanged(ActionMode actionMode, int i, long j, boolean z);
    }

    public interface RecyclerListener {
        void onMovedToScrapHeap(View view);
    }

    /* access modifiers changed from: protected */
    public abstract void layoutChildren();

    public class FixedViewInfo {
        public Object data;
        public boolean isSelectable;
        public View view;

        public FixedViewInfo() {
        }
    }

    public void setPreLoadCount(int count) {
        this.mPreLoadCount = count;
    }

    public int getPreLoadCount() {
        return this.mPreLoadCount;
    }

    public int getFirstPosition() {
        return getFirstVisiblePosition();
    }

    public int getLastPosition() {
        return getLastVisiblePosition();
    }

    public int getFirsVisibletChildIndex() {
        return 0;
    }

    public int getLastVisibleChildIndex() {
        return getChildCount() - 1;
    }

    public int getVisibleChildCount() {
        return getChildCount();
    }

    public View getFirstChild() {
        return getChildAt(0);
    }

    public View getLastChild() {
        return getChildAt(getChildCount() - 1);
    }

    public View getFirstVisibleChild() {
        return getChildAt(0);
    }

    public View getLastVisibleChild() {
        return getChildAt(getChildCount() - 1);
    }

    /* access modifiers changed from: protected */
    public boolean isDirectChildHeaderOrFooter(View child) {
        ArrayList<FixedViewInfo> headers = this.mHeaderViewInfos;
        int numHeaders = headers.size();
        for (int i = 0; i < numHeaders; i++) {
            if (child == headers.get(i).view) {
                return true;
            }
        }
        ArrayList<FixedViewInfo> footers = this.mFooterViewInfos;
        int numFooters = footers.size();
        for (int i2 = 0; i2 < numFooters; i2++) {
            if (child == footers.get(i2).view) {
                return true;
            }
        }
        return false;
    }

    public void addHeaderView(View v, Object data, boolean isSelectable) {
        if (this.mAdapter == null || (this.mAdapter instanceof HeaderViewListAdapter)) {
            FixedViewInfo info = new FixedViewInfo();
            info.view = v;
            info.data = data;
            info.isSelectable = isSelectable;
            this.mHeaderViewInfos.add(info);
            this.mAreAllItemsSelectable &= isSelectable;
            if (this.mAdapter != null && this.mDataSetObserver != null) {
                this.mDataSetObserver.onChanged();
                return;
            }
            return;
        }
        throw new IllegalStateException("Cannot add header view to list -- setAdapter has already been called.");
    }

    public void setHeadViewSelectable(View v, boolean isSelectable) {
        Iterator<FixedViewInfo> it = this.mHeaderViewInfos.iterator();
        while (it.hasNext()) {
            FixedViewInfo info = it.next();
            if (v == info.view) {
                info.isSelectable = isSelectable;
            }
        }
    }

    public View getHeaderView(int index) {
        if (index <= getHeaderViewsCount() - 1 && index >= 0) {
            return this.mHeaderViewInfos.get(index).view;
        }
        throw new IllegalArgumentException("Cannot get header");
    }

    public void addHeaderView(View v) {
        addHeaderView(v, (Object) null, true);
    }

    public int getHeaderViewsCount() {
        return this.mHeaderViewInfos.size();
    }

    public boolean removeHeaderView(View v) {
        if (this.mHeaderViewInfos.size() <= 0) {
            return false;
        }
        boolean result = false;
        if (this.mAdapter != null && ((HeaderViewListAdapter) this.mAdapter).removeHeader(v)) {
            if (this.mDataSetObserver != null) {
                this.mDataSetObserver.onChanged();
            }
            result = true;
        }
        removeFixedViewInfo(v, this.mHeaderViewInfos);
        return result;
    }

    private void removeFixedViewInfo(View v, ArrayList<FixedViewInfo> where) {
        int len = where.size();
        for (int i = 0; i < len; i++) {
            if (where.get(i).view == v) {
                where.remove(i);
                return;
            }
        }
    }

    public void addFooterView(View v, Object data, boolean isSelectable) {
        FixedViewInfo info = new FixedViewInfo();
        info.view = v;
        info.data = data;
        info.isSelectable = isSelectable;
        this.mAreAllItemsSelectable &= isSelectable;
        this.mFooterViewInfos.add(info);
        if (this.mAdapter != null && this.mDataSetObserver != null) {
            this.mDataSetObserver.onChanged();
        }
    }

    public void addFooterView(View v) {
        addFooterView(v, (Object) null, true);
    }

    public int getFooterViewsCount() {
        return this.mFooterViewInfos.size();
    }

    public boolean removeFooterView(View v) {
        if (this.mFooterViewInfos.size() <= 0) {
            return false;
        }
        boolean result = false;
        if (this.mAdapter != null && ((HeaderViewListAdapter) this.mAdapter).removeFooter(v)) {
            if (this.mDataSetObserver != null) {
                this.mDataSetObserver.onChanged();
            }
            result = true;
        }
        removeFixedViewInfo(v, this.mFooterViewInfos);
        return result;
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.mOnScrollListener = l;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldShowSelector() {
        return (hasFocus() && !isInTouchMode()) || touchModeDrawsInPressedState();
    }

    /* access modifiers changed from: package-private */
    public void reportScrollStateChange(int newState) {
        if (newState != this.mLastScrollState) {
            this.mLastScrollState = newState;
            if (this.mOnScrollListener != null) {
                this.mLastScrollState = newState;
                this.mOnScrollListener.onScrollStateChanged(this, newState);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean touchModeDrawsInPressedState() {
        return false;
    }

    public AbsBaseListView(Context context) {
        super(context);
        this.mChoiceMode = 0;
        this.mSelectionLeftPadding = 0;
        this.mSelectionTopPadding = 0;
        this.mSelectionRightPadding = 0;
        this.mSelectionBottomPadding = 0;
        this.mListPadding = new Rect();
        this.mRecycler = new RecycleBin();
        this.mIsScrap = new boolean[1];
        this.mSelectedLeft = 0;
        this.mScalableRecycler = new RecycleBin();
        this.mSpacing = 0;
        this.mSelectorRect = new Rect();
        this.mResurrectToPosition = 0;
        this.mLayoutMode = 0;
        this.mTouchMode = -1;
        this.mAreAllItemsSelectable = true;
        this.mDrawSelectorOnTop = true;
        this.mLastScrollState = 0;
        this.mTempRect = new Rect();
        this.mHeaderViewInfos = new ArrayList<>();
        this.mFooterViewInfos = new ArrayList<>();
        this.mNeedLayout = false;
        this.mPreLoadCount = 0;
        this.needMeasureSelectedView = true;
        initAbsSpinner();
    }

    public AbsBaseListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsBaseListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mChoiceMode = 0;
        this.mSelectionLeftPadding = 0;
        this.mSelectionTopPadding = 0;
        this.mSelectionRightPadding = 0;
        this.mSelectionBottomPadding = 0;
        this.mListPadding = new Rect();
        this.mRecycler = new RecycleBin();
        this.mIsScrap = new boolean[1];
        this.mSelectedLeft = 0;
        this.mScalableRecycler = new RecycleBin();
        this.mSpacing = 0;
        this.mSelectorRect = new Rect();
        this.mResurrectToPosition = 0;
        this.mLayoutMode = 0;
        this.mTouchMode = -1;
        this.mAreAllItemsSelectable = true;
        this.mDrawSelectorOnTop = true;
        this.mLastScrollState = 0;
        this.mTempRect = new Rect();
        this.mHeaderViewInfos = new ArrayList<>();
        this.mFooterViewInfos = new ArrayList<>();
        this.mNeedLayout = false;
        this.mPreLoadCount = 0;
        this.needMeasureSelectedView = true;
        initAbsSpinner();
    }

    /* access modifiers changed from: protected */
    public void initOrResetVelocityTracker() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            this.mVelocityTracker.clear();
        }
    }

    /* access modifiers changed from: protected */
    public void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    /* access modifiers changed from: protected */
    public void recycleVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void initAbsSpinner() {
        setFocusable(true);
        setWillNotDraw(false);
    }

    public void setAdapter(ListAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
            resetList();
        }
        if (this.mHeaderViewInfos.size() > 0 || this.mFooterViewInfos.size() > 0) {
            this.mAdapter = new HeaderViewListAdapter(this.mHeaderViewInfos, this.mFooterViewInfos, adapter);
        } else {
            this.mAdapter = adapter;
        }
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        this.mRecycler.clear();
        if (this.mAdapter != null) {
            this.mAreAllItemsSelectable = this.mAdapter.areAllItemsEnabled();
            this.mOldItemCount = this.mItemCount;
            this.mAdapterHasStableIds = this.mAdapter.hasStableIds();
            this.mItemCount = this.mAdapter.getCount();
            checkFocus();
            this.mDataSetObserver = new AdapterView.AdapterDataSetObserver();
            this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
            this.mRecycler.setViewTypeCount(this.mAdapter.getViewTypeCount());
            int position = initPosition();
            setSelectedPositionInt(position);
            setNextSelectedPositionInt(position);
            if (this.mItemCount == 0) {
                checkSelectionChanged();
            }
        } else {
            this.mAreAllItemsSelectable = true;
            checkFocus();
            resetList();
            checkSelectionChanged();
        }
        requestLayout();
    }

    /* access modifiers changed from: protected */
    public void positionSelector(int position, View sel) {
        if (position != -1) {
            this.mSelectorPosition = position;
        }
        Rect selectorRect = this.mSelectorRect;
        selectorRect.set(sel.getLeft(), sel.getTop(), sel.getRight(), sel.getBottom());
        if (sel instanceof AbsListView.SelectionBoundsAdjuster) {
            ((AbsListView.SelectionBoundsAdjuster) sel).adjustListItemSelectionBounds(selectorRect);
        }
        positionSelector(selectorRect.left, selectorRect.top, selectorRect.right, selectorRect.bottom);
        boolean isChildViewEnabled = this.mIsChildViewEnabled;
        if (sel.isEnabled() != isChildViewEnabled) {
            this.mIsChildViewEnabled = !isChildViewEnabled;
            if (getSelectedItemPosition() != -1) {
                refreshDrawableState();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void positionSelector(int l, int t, int r, int b) {
        this.mSelectorRect.set(l - this.mSelectionLeftPadding, t - this.mSelectionTopPadding, this.mSelectionRightPadding + r, this.mSelectionBottomPadding + b);
    }

    /* access modifiers changed from: protected */
    public View obtainView(int position, boolean[] isScrap) {
        View child;
        LayoutParams lp;
        isScrap[0] = false;
        View scrapView = this.mRecycler.getTransientStateView(position);
        if (scrapView != null) {
            return scrapView;
        }
        View scrapView2 = this.mRecycler.getScrapView(position);
        if (this.mAdapter == null) {
            ZpLogger.e(TAG, "AbsBaseListView.obtainView.mAdapter = " + this.mAdapter);
            return null;
        }
        if (scrapView2 != null) {
            child = this.mAdapter.getView(position, scrapView2, this);
            if (child != scrapView2) {
                this.mRecycler.addScrapView(scrapView2, position);
            } else {
                isScrap[0] = true;
            }
        } else {
            child = this.mAdapter.getView(position, (View) null, this);
        }
        if (!this.mAdapterHasStableIds) {
            return child;
        }
        ViewGroup.LayoutParams vlp = child.getLayoutParams();
        if (vlp == null) {
            lp = (LayoutParams) generateDefaultLayoutParams();
        } else if (!checkLayoutParams(vlp)) {
            lp = (LayoutParams) generateLayoutParams(vlp);
        } else {
            lp = (LayoutParams) vlp;
        }
        lp.itemId = this.mAdapter.getItemId(position);
        child.setLayoutParams(lp);
        return child;
    }

    /* access modifiers changed from: protected */
    public void invalidateParentIfNeeded() {
        if (isHardwareAccelerated() && (getParent() instanceof View)) {
            ((View) getParent()).invalidate();
        }
    }

    /* access modifiers changed from: package-private */
    public int initPosition() {
        return this.mItemCount > 0 ? 0 : -1;
    }

    /* access modifiers changed from: protected */
    public int getListLeft() {
        return this.mListPadding.left;
    }

    /* access modifiers changed from: protected */
    public int getListTop() {
        return this.mListPadding.top;
    }

    /* access modifiers changed from: protected */
    public int getListRight() {
        return getWidth() - this.mListPadding.right;
    }

    /* access modifiers changed from: protected */
    public int getListBottom() {
        return getHeight() - this.mListPadding.bottom;
    }

    public int getListPaddingTop() {
        return this.mListPadding.top;
    }

    public int getListPaddingBottom() {
        return this.mListPadding.bottom;
    }

    public int getListPaddingLeft() {
        return this.mListPadding.left;
    }

    public int getListPaddingRight() {
        return this.mListPadding.right;
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

    /* access modifiers changed from: protected */
    public void drawSelector(Canvas canvas) {
        if (hasFocus() && this.mSelector != null && this.mSelectorRect != null && !this.mSelectorRect.isEmpty()) {
            this.mSelector.setBounds(new Rect(this.mExactlyUserSelectedRect != null ? this.mExactlyUserSelectedRect : this.mSelectorRect));
            this.mSelector.draw(canvas);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean resurrectSelectionIfNeeded() {
        if (this.mSelectedPosition >= 0 || !resurrectSelection()) {
            return false;
        }
        updateSelectorState();
        return true;
    }

    /* access modifiers changed from: package-private */
    public void updateSelectorState() {
        if (this.mSelector == null) {
            return;
        }
        if (shouldShowSelector()) {
            this.mSelector.setState(getDrawableState());
        } else {
            this.mSelector.setState(StateSet.NOTHING);
        }
    }

    public void setDrawSelectorOnTop(boolean onTop) {
        this.mDrawSelectorOnTop = onTop;
    }

    public boolean drawSclectorOnTop() {
        return this.mDrawSelectorOnTop;
    }

    public void setExactlyUserSelectedRect(int left, int top, int right, int bottom) {
        this.mExactlyUserSelectedRect = new Rect(left, top, right, bottom);
    }

    public void clearExactlyUserSelectedRect() {
        this.mExactlyUserSelectedRect = null;
    }

    public void setSelectorPadding(int leftPadding, int topPadding, int rightPadding, int bottomPadding) {
        this.mSelectionLeftPadding = leftPadding;
        this.mSelectionTopPadding = topPadding;
        this.mSelectionRightPadding = rightPadding;
        this.mSelectionBottomPadding = bottomPadding;
    }

    private void setupScalableView(View scalableView, View child) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (lp == null) {
            lp = (LayoutParams) generateDefaultLayoutParams();
        }
        scalableView.measure(getChildMeasureSpec(this.mWidthMeasureSpec, this.mListPadding.left + this.mListPadding.right, lp.width), getChildMeasureSpec(this.mHeightMeasureSpec, this.mListPadding.top + this.mListPadding.bottom, lp.height));
        int height = scalableView.getMeasuredHeight();
        int l = child.getLeft();
        int r = child.getRight();
        int t = child.getBottom() + this.mScalableViewSpacing;
        scalableView.layout(l, t, r, t + height);
    }

    /* access modifiers changed from: protected */
    public boolean performButtonActionOnTouchDown(MotionEvent event) {
        if ((event.getButtonState() & 2) == 0 || !showContextMenu()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public ContextMenu.ContextMenuInfo createContextMenuInfo(View view, int position, long id) {
        return new AdapterView.AdapterContextMenuInfo(view, position, id);
    }

    /* access modifiers changed from: protected */
    public ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return this.mContextMenuInfo;
    }

    public boolean showContextMenu() {
        if (!isPressed() || this.mSelectedPosition < 0) {
            return false;
        }
        return dispatchLongPress(getChildAt(this.mSelectedPosition - this.mFirstPosition), this.mSelectedPosition, this.mSelectedRowId);
    }

    public boolean showContextMenuForChild(View originalView) {
        int longPressPosition = getPositionForView(originalView);
        if (longPressPosition < 0) {
            return false;
        }
        return dispatchLongPress(originalView, longPressPosition, this.mAdapter.getItemId(longPressPosition));
    }

    /* access modifiers changed from: package-private */
    public boolean performLongPress(View child, int longPressPosition, long longPressId) {
        boolean handled = true;
        if (this.mChoiceMode != 3) {
            handled = false;
            if (this.mOnItemLongClickListener != null) {
                handled = this.mOnItemLongClickListener.onItemLongClick(this, child, longPressPosition, longPressId);
            }
            if (!handled) {
                this.mContextMenuInfo = createContextMenuInfo(child, longPressPosition, longPressId);
                handled = super.showContextMenuForChild(this);
            }
            if (handled) {
                performHapticFeedback(0);
            }
        } else if (this.mChoiceActionMode == null) {
            ActionMode startActionMode = startActionMode(this.mMultiChoiceModeCallback);
            this.mChoiceActionMode = startActionMode;
            if (startActionMode != null) {
                setItemChecked(longPressPosition, true);
                performHapticFeedback(0);
            }
        }
        return handled;
    }

    private boolean dispatchLongPress(View view, int position, long id) {
        boolean handled = false;
        if (this.mOnItemLongClickListener != null) {
            handled = this.mOnItemLongClickListener.onItemLongClick(this, this.mDownTouchView, this.mDownTouchPosition, id);
        }
        if (!handled) {
            this.mContextMenuInfo = new AdapterView.AdapterContextMenuInfo(view, position, id);
            handled = super.showContextMenuForChild(this);
        }
        if (handled) {
            performHapticFeedback(0);
        }
        return handled;
    }

    public int getCheckedItemCount() {
        return this.mCheckedItemCount;
    }

    public boolean isItemChecked(int position) {
        if (this.mChoiceMode == 0 || this.mCheckStates == null) {
            return false;
        }
        return this.mCheckStates.get(position);
    }

    public void setItemChecked(int position, boolean value) {
        boolean updateIds;
        if (this.mChoiceMode != 0) {
            if (value && this.mChoiceMode == 3 && this.mChoiceActionMode == null) {
                if (this.mMultiChoiceModeCallback == null || !this.mMultiChoiceModeCallback.hasWrappedCallback()) {
                    throw new IllegalStateException("AbsListView: attempted to start selection mode for CHOICE_MODE_MULTIPLE_MODAL but no choice mode callback was supplied. Call setMultiChoiceModeListener to set a callback.");
                }
                this.mChoiceActionMode = startActionMode(this.mMultiChoiceModeCallback);
            }
            if (this.mChoiceMode == 2 || this.mChoiceMode == 3) {
                boolean oldValue = this.mCheckStates.get(position);
                this.mCheckStates.put(position, value);
                if (this.mCheckedIdStates != null && this.mAdapter.hasStableIds()) {
                    if (value) {
                        this.mCheckedIdStates.put(this.mAdapter.getItemId(position), Integer.valueOf(position));
                    } else {
                        this.mCheckedIdStates.delete(this.mAdapter.getItemId(position));
                    }
                }
                if (oldValue != value) {
                    if (value) {
                        this.mCheckedItemCount++;
                    } else {
                        this.mCheckedItemCount--;
                    }
                }
                if (this.mChoiceActionMode != null) {
                    this.mMultiChoiceModeCallback.onItemCheckedStateChanged(this.mChoiceActionMode, position, this.mAdapter.getItemId(position), value);
                }
            } else {
                if (this.mCheckedIdStates == null || !this.mAdapter.hasStableIds()) {
                    updateIds = false;
                } else {
                    updateIds = true;
                }
                if (value || isItemChecked(position)) {
                    this.mCheckStates.clear();
                    if (updateIds) {
                        this.mCheckedIdStates.clear();
                    }
                }
                if (value) {
                    this.mCheckStates.put(position, true);
                    if (updateIds) {
                        this.mCheckedIdStates.put(this.mAdapter.getItemId(position), Integer.valueOf(position));
                    }
                    this.mCheckedItemCount = 1;
                } else if (this.mCheckStates.size() == 0 || !this.mCheckStates.valueAt(0)) {
                    this.mCheckedItemCount = 0;
                }
            }
            if (!this.mInLayout && !this.mBlockLayoutRequests) {
                this.mDataChanged = true;
                rememberSyncState();
                requestLayout();
            }
        }
    }

    public void getFocusedRect(Rect r) {
        View view = getSelectedView();
        if (view == null || view.getParent() != this) {
            super.getFocusedRect(r);
            return;
        }
        view.getFocusedRect(r);
        offsetDescendantRectToMyCoords(view, r);
    }

    public void clearChoices() {
        if (this.mCheckStates != null) {
            this.mCheckStates.clear();
        }
        if (this.mCheckedIdStates != null) {
            this.mCheckedIdStates.clear();
        }
        this.mCheckedItemCount = 0;
    }

    private class WindowRunnnable {
        private int mOriginalAttachCount;

        private WindowRunnnable() {
        }

        public void rememberWindowAttachCount() {
            this.mOriginalAttachCount = AbsBaseListView.this.getWindowAttachCount();
        }

        public boolean sameWindow() {
            return AbsBaseListView.this.hasWindowFocus() && AbsBaseListView.this.getWindowAttachCount() == this.mOriginalAttachCount;
        }
    }

    class PerformClick extends WindowRunnnable implements Runnable {
        int mClickMotionPosition;

        PerformClick() {
            super();
        }

        public void run() {
            View view;
            if (!AbsBaseListView.this.mDataChanged) {
                ListAdapter adapter = AbsBaseListView.this.mAdapter;
                int motionPosition = this.mClickMotionPosition;
                if (adapter != null && AbsBaseListView.this.mItemCount > 0 && motionPosition != -1 && motionPosition < adapter.getCount() && sameWindow() && (view = AbsBaseListView.this.getChildAt(motionPosition - AbsBaseListView.this.mFirstPosition)) != null) {
                    AbsBaseListView.this.performItemClick(view, motionPosition, adapter.getItemId(motionPosition));
                }
            }
        }
    }

    class CheckForLongPress extends WindowRunnnable implements Runnable {
        CheckForLongPress() {
            super();
        }

        public void run() {
            View child = AbsBaseListView.this.getChildAt(AbsBaseListView.this.mMotionPosition - AbsBaseListView.this.mFirstPosition);
            if (child != null) {
                int longPressPosition = AbsBaseListView.this.mMotionPosition;
                long longPressId = AbsBaseListView.this.mAdapter.getItemId(AbsBaseListView.this.mMotionPosition);
                boolean handled = false;
                if (sameWindow() && !AbsBaseListView.this.mDataChanged) {
                    handled = AbsBaseListView.this.performLongPress(child, longPressPosition, longPressId);
                }
                if (handled) {
                    AbsBaseListView.this.mTouchMode = -1;
                    AbsBaseListView.this.setPressed(false);
                    child.setPressed(false);
                    return;
                }
                AbsBaseListView.this.mTouchMode = 2;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setScalableView(int position, View child) {
        if (this.mScalableAdapter != null) {
            View scalView = this.mScalableAdapter.getView(position, (View) null, this);
            ZpLogger.d(TAG, " getScalableView position = " + position + " child = " + getChildAt(position));
            if (scalView == null || child == null) {
                this.mScalableView = null;
                return;
            }
            setupScalableView(scalView, child);
            this.mScalableView = scalView;
        }
    }

    /* access modifiers changed from: package-private */
    public int getItemWidth() {
        return this.mItemWidth;
    }

    /* access modifiers changed from: package-private */
    public int getItemHeight() {
        return this.mItemHeight;
    }

    /* access modifiers changed from: package-private */
    public void clearScalableView() {
        this.mScalableView = null;
    }

    public void setScalableAdapter(Adapter adapter) {
        this.mScalableAdapter = adapter;
        this.mScalableRecycler.clear();
    }

    public void setScalableViewSpacing(int spacing) {
        this.mScalableViewSpacing = spacing;
    }

    @ViewDebug.ExportedProperty
    public boolean isScrollingCacheEnabled() {
        return this.mScrollingCacheEnabled;
    }

    public void setScrollingCacheEnabled(boolean enabled) {
        if (this.mScrollingCacheEnabled && !enabled) {
            clearScrollingCache();
        }
        this.mScrollingCacheEnabled = enabled;
    }

    /* access modifiers changed from: protected */
    public void createScrollingCache() {
        if (this.mScrollingCacheEnabled && !this.mCachingStarted) {
            setChildrenDrawnWithCacheEnabled(true);
            setChildrenDrawingCacheEnabled(true);
            this.mCachingActive = true;
            this.mCachingStarted = true;
        }
    }

    /* access modifiers changed from: protected */
    public void clearScrollingCache() {
        if (this.mClearScrollingCache == null) {
            this.mClearScrollingCache = new Runnable() {
                public void run() {
                    if (AbsBaseListView.this.mCachingStarted) {
                        AbsBaseListView absBaseListView = AbsBaseListView.this;
                        AbsBaseListView.this.mCachingActive = false;
                        absBaseListView.mCachingStarted = false;
                        AbsBaseListView.this.setChildrenDrawnWithCacheEnabled(false);
                        if ((AbsBaseListView.this.getPersistentDrawingCache() & 2) == 0) {
                            AbsBaseListView.this.setChildrenDrawingCacheEnabled(false);
                        }
                        if (!AbsBaseListView.this.isAlwaysDrawnWithCacheEnabled()) {
                            AbsBaseListView.this.invalidate();
                        }
                    }
                }
            };
        }
        post(this.mClearScrollingCache);
    }

    /* access modifiers changed from: protected */
    public void resetList() {
        this.mDataChanged = false;
        this.mNeedSync = false;
        removeAllViewsInLayout();
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        this.mOldItemCount = this.mItemCount;
        this.mItemCount = 0;
        setSelectedPositionInt(-1);
        setNextSelectedPositionInt(-1);
        invalidate();
    }

    /* access modifiers changed from: package-private */
    public void requestLayoutIfNecessary() {
        if (getChildCount() > 0) {
            resetList();
            requestLayout();
            invalidate();
        }
    }

    public void offsetChildrenTopAndBottom(int offset) {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            getChildAt(i).offsetTopAndBottom(offset);
        }
    }

    /* access modifiers changed from: protected */
    public void offsetChildrenLeftAndRight(int offset) {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            getChildAt(i).offsetLeftAndRight(offset);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        this.mListPadding.left = getPaddingLeft() + this.mSelectionLeftPadding;
        this.mListPadding.top = getPaddingTop() + this.mSelectionTopPadding;
        this.mListPadding.right = getPaddingRight() + this.mSelectionRightPadding;
        this.mListPadding.bottom = getPaddingBottom() + this.mSelectionBottomPadding;
        if (this.mDataChanged) {
            handleDataChanged();
        }
        int preferredHeight = 0;
        int preferredWidth = 0;
        boolean needsMeasuring = true;
        if (this.needMeasureSelectedView) {
            int selectedPosition = getSelectedItemPosition();
            if (this.mAdapter != null && getHeaderViewsCount() < this.mAdapter.getCount()) {
                selectedPosition = getHeaderViewsCount();
            }
            if (selectedPosition >= 0 && this.mAdapter != null && selectedPosition < this.mAdapter.getCount()) {
                View view = this.mRecycler.getScrapView(selectedPosition);
                if (view == null && (view = this.mAdapter.getView(selectedPosition, (View) null, this)) != null) {
                    this.mRecycler.addScrapView(view, selectedPosition);
                }
                if (view != null) {
                    if (view.getLayoutParams() == null) {
                        this.mBlockLayoutRequests = true;
                        view.setLayoutParams(generateDefaultLayoutParams());
                        this.mBlockLayoutRequests = false;
                    }
                    measureChild(view, widthMeasureSpec, heightMeasureSpec);
                    this.mItemHeight = getChildHeight(view);
                    this.mItemWidth = getChildWidth(view);
                    preferredHeight = getChildHeight(view) + this.mListPadding.top + this.mListPadding.bottom;
                    preferredWidth = getChildWidth(view) + this.mListPadding.left + this.mListPadding.right;
                    needsMeasuring = false;
                }
            }
        }
        if (needsMeasuring) {
            preferredHeight = this.mListPadding.top + this.mListPadding.bottom;
            if (widthMode == 0) {
                preferredWidth = this.mListPadding.left + this.mListPadding.right;
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

    /* access modifiers changed from: protected */
    public int getChildWidth(View child) {
        return child.getMeasuredWidth();
    }

    /* access modifiers changed from: protected */
    public void keyPressed() {
        View child = getChildAt(this.mSelectedPosition - this.mFirstPosition);
        if (child != null) {
            child.setPressed(true);
        }
        setPressed(true);
    }

    /* access modifiers changed from: protected */
    public void dispatchUnpress() {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            getChildAt(i).setPressed(false);
        }
        setPressed(false);
    }

    /* access modifiers changed from: package-private */
    public void recycleAllViews() {
        int childCount = getChildCount();
        RecycleBin recycleBin = this.mRecycler;
        int position = this.mFirstPosition;
        for (int i = 0; i < childCount; i++) {
            recycleBin.addScrapView(getChildAt(i), position + i);
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
    public void setSelectionInt(int position) {
        setNextSelectedPositionInt(position);
        boolean awakeScrollbars = false;
        int selectedPosition = this.mSelectedPosition;
        if (selectedPosition >= 0) {
            if (position == selectedPosition - 1) {
                awakeScrollbars = true;
            } else if (position == selectedPosition + 1) {
                awakeScrollbars = true;
            }
        }
        if (awakeScrollbars) {
            awakenScrollBars();
        }
    }

    /* access modifiers changed from: package-private */
    public void setSelectionInt(int position, boolean animate) {
        if (position != this.mOldSelectedPosition) {
            this.mBlockLayoutRequests = true;
            int i = position - this.mSelectedPosition;
            setNextSelectedPositionInt(position);
            this.mBlockLayoutRequests = false;
        }
    }

    public void setSpacing(int spacing) {
        this.mSpacing = spacing;
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2, 0);
    }

    public View getSelectedView() {
        if (this.mItemCount > 0 && this.mSelectedPosition >= 0) {
            return getChildAt(this.mSelectedPosition - this.mFirstPosition);
        }
        ZpLogger.e(TAG, "getSelectedView: return null! this:" + toString() + ", mItemCount:" + this.mItemCount + ", mSelectedPosition:" + this.mSelectedPosition);
        return null;
    }

    public void requestLayout() {
        if (!this.mBlockLayoutRequests) {
            this.mNeedLayout = true;
            super.requestLayout();
        }
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus && this.mSelectedPosition < 0 && !isInTouchMode()) {
            if (!this.mIsAttached && this.mAdapter != null) {
                this.mDataChanged = true;
                this.mOldItemCount = this.mItemCount;
                this.mItemCount = this.mAdapter.getCount();
            }
            resurrectSelection();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean resurrectSelection() {
        return true;
    }

    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    public int getCount() {
        return this.mItemCount;
    }

    /* access modifiers changed from: package-private */
    public void hideSelector() {
        if (this.mSelectedPosition != -1) {
            if (this.mLayoutMode != 4) {
                this.mResurrectToPosition = this.mSelectedPosition;
            }
            if (this.mNextSelectedPosition >= 0 && this.mNextSelectedPosition != this.mSelectedPosition) {
                this.mResurrectToPosition = this.mNextSelectedPosition;
            }
            setSelectedPositionInt(-1);
            setNextSelectedPositionInt(-1);
            this.mSelectedLeft = 0;
        }
    }

    /* access modifiers changed from: protected */
    public int reconcileSelectedPosition() {
        int position = this.mSelectedPosition;
        if (position < 0) {
            position = this.mResurrectToPosition;
        }
        return Math.min(Math.max(0, position), this.mItemCount - 1);
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

    public static class LayoutParams extends ViewGroup.LayoutParams {
        @ViewDebug.ExportedProperty(category = "list")
        boolean forceAdd;
        long itemId = -1;
        @ViewDebug.ExportedProperty(category = "list")
        boolean recycledHeaderFooter;
        int scrappedFromPosition;
        int viewType;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(int w, int h, int viewType2) {
            super(w, h);
            this.viewType = viewType2;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    static View retrieveFromScrap(ArrayList<View> scrapViews, int position) {
        int size = scrapViews.size();
        if (size <= 0) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            View view = scrapViews.get(i);
            if (((LayoutParams) view.getLayoutParams()).scrappedFromPosition == position) {
                scrapViews.remove(i);
                return view;
            }
        }
        return scrapViews.remove(size - 1);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mIsAttached = true;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mRecycler.clear();
        this.mIsAttached = false;
    }

    public class RecycleBin {
        private View[] mActiveViews = new View[0];
        private ArrayList<View> mCurrentScrap;
        private int mFirstActivePosition;
        private RecyclerListener mRecyclerListener;
        private ArrayList<View>[] mScrapViews;
        private ArrayList<View> mSkippedScrap;
        private SparseArray<View> mTransientStateViews;
        private int mViewTypeCount;

        public RecycleBin() {
        }

        public void setViewTypeCount(int viewTypeCount) {
            if (viewTypeCount < 1) {
                throw new IllegalArgumentException("Can't have a viewTypeCount < 1");
            }
            ArrayList<View>[] scrapViews = new ArrayList[viewTypeCount];
            for (int i = 0; i < viewTypeCount; i++) {
                scrapViews[i] = new ArrayList<>();
            }
            this.mViewTypeCount = viewTypeCount;
            this.mCurrentScrap = scrapViews[0];
            this.mScrapViews = scrapViews;
        }

        public void markChildrenDirty() {
            if (this.mViewTypeCount == 1) {
                ArrayList<View> scrap = this.mCurrentScrap;
                int scrapCount = scrap.size();
                for (int i = 0; i < scrapCount; i++) {
                    scrap.get(i).forceLayout();
                }
            } else {
                int typeCount = this.mViewTypeCount;
                for (int i2 = 0; i2 < typeCount; i2++) {
                    ArrayList<View> scrap2 = this.mScrapViews[i2];
                    int scrapCount2 = scrap2.size();
                    for (int j = 0; j < scrapCount2; j++) {
                        scrap2.get(j).forceLayout();
                    }
                }
            }
            if (this.mTransientStateViews != null) {
                int count = this.mTransientStateViews.size();
                for (int i3 = 0; i3 < count; i3++) {
                    this.mTransientStateViews.valueAt(i3).forceLayout();
                }
            }
        }

        public boolean shouldRecycleViewType(int viewType) {
            return viewType >= 0;
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            if (this.mViewTypeCount == 1) {
                ArrayList<View> scrap = this.mCurrentScrap;
                int scrapCount = scrap.size();
                for (int i = 0; i < scrapCount; i++) {
                    AbsBaseListView.this.removeDetachedView(scrap.remove((scrapCount - 1) - i), false);
                }
            } else {
                int typeCount = this.mViewTypeCount;
                for (int i2 = 0; i2 < typeCount; i2++) {
                    ArrayList<View> scrap2 = this.mScrapViews[i2];
                    int scrapCount2 = scrap2.size();
                    for (int j = 0; j < scrapCount2; j++) {
                        AbsBaseListView.this.removeDetachedView(scrap2.remove((scrapCount2 - 1) - j), false);
                    }
                }
            }
            if (this.mTransientStateViews != null) {
                this.mTransientStateViews.clear();
            }
        }

        public void fillActiveViews(int childCount, int firstActivePosition) {
            if (this.mActiveViews.length < childCount) {
                this.mActiveViews = new View[childCount];
            }
            this.mFirstActivePosition = firstActivePosition;
            View[] activeViews = this.mActiveViews;
            for (int i = 0; i < childCount; i++) {
                View child = AbsBaseListView.this.getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (!(lp == null || lp.viewType == -2)) {
                    activeViews[i] = child;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public View getActiveView(int position) {
            int index = position - this.mFirstActivePosition;
            View[] activeViews = this.mActiveViews;
            if (index < 0 || index >= activeViews.length) {
                return null;
            }
            View match = activeViews[index];
            activeViews[index] = null;
            return match;
        }

        /* access modifiers changed from: package-private */
        public View getTransientStateView(int position) {
            int index;
            if (this.mTransientStateViews == null || (index = this.mTransientStateViews.indexOfKey(position)) < 0) {
                return null;
            }
            View valueAt = this.mTransientStateViews.valueAt(index);
            this.mTransientStateViews.removeAt(index);
            return valueAt;
        }

        /* access modifiers changed from: package-private */
        public void clearTransientStateViews() {
            if (this.mTransientStateViews != null) {
                this.mTransientStateViews.clear();
            }
        }

        /* access modifiers changed from: package-private */
        public View getScrapView(int position) {
            int whichScrap;
            if (this.mViewTypeCount == 1) {
                return AbsBaseListView.retrieveFromScrap(this.mCurrentScrap, position);
            }
            if (AbsBaseListView.this.mAdapter == null || (whichScrap = AbsBaseListView.this.mAdapter.getItemViewType(position)) < 0 || whichScrap >= this.mScrapViews.length) {
                return null;
            }
            return AbsBaseListView.retrieveFromScrap(this.mScrapViews[whichScrap], position);
        }

        public void addScrapView(View scrap, int position) {
            LayoutParams lp = (LayoutParams) scrap.getLayoutParams();
            if (lp != null) {
                lp.scrappedFromPosition = position;
                int viewType = lp.viewType;
                boolean scrapHasTransientState = false;
                try {
                    scrapHasTransientState = ((Boolean) ReflectUtils.invokeMethod(scrap, "hasTransientState", new Class[0], new Object[0])).booleanValue();
                } catch (Exception e) {
                }
                if (!shouldRecycleViewType(viewType) || scrapHasTransientState) {
                    if (viewType != -2 || scrapHasTransientState) {
                        if (this.mSkippedScrap == null) {
                            this.mSkippedScrap = new ArrayList<>();
                        }
                        this.mSkippedScrap.add(scrap);
                    }
                    if (scrapHasTransientState) {
                        if (this.mTransientStateViews == null) {
                            this.mTransientStateViews = new SparseArray<>();
                        }
                        try {
                            ReflectUtils.invokeMethod(scrap, "dispatchStartTemporaryDetach", new Object[0]);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        this.mTransientStateViews.put(position, scrap);
                        return;
                    }
                    return;
                }
                try {
                    ReflectUtils.invokeMethod(scrap, "dispatchStartTemporaryDetach", new Object[0]);
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
                if (this.mViewTypeCount == 1) {
                    this.mCurrentScrap.add(scrap);
                } else {
                    this.mScrapViews[viewType].add(scrap);
                }
                scrap.setAccessibilityDelegate((View.AccessibilityDelegate) null);
                if (this.mRecyclerListener != null) {
                    this.mRecyclerListener.onMovedToScrapHeap(scrap);
                }
            }
        }

        public void removeSkippedScrap() {
            if (this.mSkippedScrap != null) {
                int count = this.mSkippedScrap.size();
                for (int i = 0; i < count; i++) {
                    AbsBaseListView.this.removeDetachedView(this.mSkippedScrap.get(i), false);
                }
                this.mSkippedScrap.clear();
            }
        }

        public void scrapActiveViews() {
            View[] activeViews = this.mActiveViews;
            boolean hasListener = this.mRecyclerListener != null;
            boolean multipleScraps = this.mViewTypeCount > 1;
            ArrayList<View> scrapViews = this.mCurrentScrap;
            for (int i = activeViews.length - 1; i >= 0; i--) {
                View victim = activeViews[i];
                if (victim != null) {
                    LayoutParams lp = (LayoutParams) victim.getLayoutParams();
                    int whichScrap = lp.viewType;
                    activeViews[i] = null;
                    boolean scrapHasTransientState = false;
                    try {
                        scrapHasTransientState = ((Boolean) ReflectUtils.invokeMethod(victim, "hasTransientState", new Class[0], new Object[0])).booleanValue();
                    } catch (Exception e) {
                    }
                    if (!shouldRecycleViewType(whichScrap) || scrapHasTransientState) {
                        if (whichScrap != -2 || scrapHasTransientState) {
                            AbsBaseListView.this.removeDetachedView(victim, false);
                        }
                        if (scrapHasTransientState) {
                            if (this.mTransientStateViews == null) {
                                this.mTransientStateViews = new SparseArray<>();
                            }
                            this.mTransientStateViews.put(this.mFirstActivePosition + i, victim);
                        }
                    } else {
                        if (multipleScraps) {
                            scrapViews = this.mScrapViews[whichScrap];
                        }
                        try {
                            ReflectUtils.invokeMethod(victim, "dispatchStartTemporaryDetach", new Object[0]);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        lp.scrappedFromPosition = this.mFirstActivePosition + i;
                        scrapViews.add(victim);
                        victim.setAccessibilityDelegate((View.AccessibilityDelegate) null);
                        if (hasListener) {
                            this.mRecyclerListener.onMovedToScrapHeap(victim);
                        }
                    }
                }
            }
            pruneScrapViews();
        }

        private void pruneScrapViews() {
            int maxViews = this.mActiveViews.length;
            int viewTypeCount = this.mViewTypeCount;
            ArrayList<View>[] scrapViews = this.mScrapViews;
            for (int i = 0; i < viewTypeCount; i++) {
                ArrayList<View> scrapPile = scrapViews[i];
                int size = scrapPile.size();
                int extras = size - maxViews;
                int j = 0;
                int size2 = size - 1;
                while (j < extras) {
                    AbsBaseListView.this.removeDetachedView(scrapPile.remove(size2), false);
                    j++;
                    size2--;
                }
            }
            boolean hasTransientState = false;
            if (this.mTransientStateViews != null) {
                int i2 = 0;
                while (i2 < this.mTransientStateViews.size()) {
                    try {
                        hasTransientState = ((Boolean) ReflectUtils.invokeMethod(this.mTransientStateViews.valueAt(i2), "hasTransientState", new Class[0], new Object[0])).booleanValue();
                    } catch (Exception e) {
                    }
                    if (!hasTransientState) {
                        this.mTransientStateViews.removeAt(i2);
                        i2--;
                    }
                    i2++;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void reclaimScrapViews(List<View> views) {
            if (this.mViewTypeCount == 1) {
                views.addAll(this.mCurrentScrap);
                return;
            }
            int viewTypeCount = this.mViewTypeCount;
            ArrayList<View>[] scrapViews = this.mScrapViews;
            for (int i = 0; i < viewTypeCount; i++) {
                views.addAll(scrapViews[i]);
            }
        }

        /* access modifiers changed from: package-private */
        public void setCacheColorHint(int color) {
            if (this.mViewTypeCount == 1) {
                ArrayList<View> scrap = this.mCurrentScrap;
                int scrapCount = scrap.size();
                for (int i = 0; i < scrapCount; i++) {
                    scrap.get(i).setDrawingCacheBackgroundColor(color);
                }
            } else {
                int typeCount = this.mViewTypeCount;
                for (int i2 = 0; i2 < typeCount; i2++) {
                    ArrayList<View> scrap2 = this.mScrapViews[i2];
                    int scrapCount2 = scrap2.size();
                    for (int j = 0; j < scrapCount2; j++) {
                        scrap2.get(j).setDrawingCacheBackgroundColor(color);
                    }
                }
            }
            for (View victim : this.mActiveViews) {
                if (victim != null) {
                    victim.setDrawingCacheBackgroundColor(color);
                }
            }
        }
    }

    class MultiChoiceModeWrapper implements MultiChoiceModeListener {
        private MultiChoiceModeListener mWrapped;

        MultiChoiceModeWrapper() {
        }

        public void setWrapped(MultiChoiceModeListener wrapped) {
            this.mWrapped = wrapped;
        }

        public boolean hasWrappedCallback() {
            return this.mWrapped != null;
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            if (!this.mWrapped.onCreateActionMode(mode, menu)) {
                return false;
            }
            AbsBaseListView.this.setLongClickable(false);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return this.mWrapped.onPrepareActionMode(mode, menu);
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return this.mWrapped.onActionItemClicked(mode, item);
        }

        public void onDestroyActionMode(ActionMode mode) {
            this.mWrapped.onDestroyActionMode(mode);
            AbsBaseListView.this.mChoiceActionMode = null;
            AbsBaseListView.this.clearChoices();
            AbsBaseListView.this.mDataChanged = true;
            AbsBaseListView.this.rememberSyncState();
            AbsBaseListView.this.requestLayout();
            AbsBaseListView.this.setLongClickable(true);
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            this.mWrapped.onItemCheckedStateChanged(mode, position, id, checked);
            if (AbsBaseListView.this.getCheckedItemCount() == 0) {
                mode.finish();
            }
        }
    }

    public class HeaderViewListAdapter implements WrapperListAdapter, Filterable {
        final ArrayList<FixedViewInfo> EMPTY_INFO_LIST = new ArrayList<>();
        private final ListAdapter mAdapter;
        boolean mAreAllFixedViewsSelectable;
        ArrayList<FixedViewInfo> mFooterViewInfos;
        ArrayList<FixedViewInfo> mHeaderViewInfos;
        private final boolean mIsFilterable;

        public HeaderViewListAdapter(ArrayList<FixedViewInfo> headerViewInfos, ArrayList<FixedViewInfo> footerViewInfos, ListAdapter adapter) {
            this.mAdapter = adapter;
            this.mIsFilterable = adapter instanceof Filterable;
            if (headerViewInfos == null) {
                this.mHeaderViewInfos = this.EMPTY_INFO_LIST;
            } else {
                this.mHeaderViewInfos = headerViewInfos;
            }
            if (footerViewInfos == null) {
                this.mFooterViewInfos = this.EMPTY_INFO_LIST;
            } else {
                this.mFooterViewInfos = footerViewInfos;
            }
            this.mAreAllFixedViewsSelectable = areAllListInfosSelectable(this.mHeaderViewInfos) && areAllListInfosSelectable(this.mFooterViewInfos);
        }

        public int getHeadersCount() {
            return this.mHeaderViewInfos.size();
        }

        public int getFootersCount() {
            return this.mFooterViewInfos.size();
        }

        public boolean isEmpty() {
            return this.mAdapter == null || this.mAdapter.isEmpty();
        }

        private boolean areAllListInfosSelectable(ArrayList<FixedViewInfo> infos) {
            if (infos != null) {
                Iterator<FixedViewInfo> it = infos.iterator();
                while (it.hasNext()) {
                    if (!it.next().isSelectable) {
                        return false;
                    }
                }
            }
            return true;
        }

        public boolean removeHeader(View v) {
            boolean z = false;
            for (int i = 0; i < this.mHeaderViewInfos.size(); i++) {
                if (this.mHeaderViewInfos.get(i).view == v) {
                    this.mHeaderViewInfos.remove(i);
                    if (areAllListInfosSelectable(this.mHeaderViewInfos) && areAllListInfosSelectable(this.mFooterViewInfos)) {
                        z = true;
                    }
                    this.mAreAllFixedViewsSelectable = z;
                    return true;
                }
            }
            return false;
        }

        public boolean removeFooter(View v) {
            boolean z = false;
            for (int i = 0; i < this.mFooterViewInfos.size(); i++) {
                if (this.mFooterViewInfos.get(i).view == v) {
                    this.mFooterViewInfos.remove(i);
                    if (areAllListInfosSelectable(this.mHeaderViewInfos) && areAllListInfosSelectable(this.mFooterViewInfos)) {
                        z = true;
                    }
                    this.mAreAllFixedViewsSelectable = z;
                    return true;
                }
            }
            return false;
        }

        public int getCount() {
            if (this.mAdapter != null) {
                return getFootersCount() + getHeadersCount() + this.mAdapter.getCount();
            }
            return getFootersCount() + getHeadersCount();
        }

        public boolean areAllItemsEnabled() {
            if (this.mAdapter == null) {
                return true;
            }
            if (!this.mAreAllFixedViewsSelectable || !this.mAdapter.areAllItemsEnabled()) {
                return false;
            }
            return true;
        }

        public boolean isEnabled(int position) {
            int numHeaders = getHeadersCount();
            if (position < numHeaders) {
                return this.mHeaderViewInfos.get(position).isSelectable;
            }
            int adjPosition = position - numHeaders;
            int adapterCount = 0;
            if (this.mAdapter == null || adjPosition >= (adapterCount = this.mAdapter.getCount())) {
                return this.mFooterViewInfos.get(adjPosition - adapterCount).isSelectable;
            }
            return this.mAdapter.isEnabled(adjPosition);
        }

        public Object getItem(int position) {
            int numHeaders = getHeadersCount();
            if (position < numHeaders) {
                return this.mHeaderViewInfos.get(position).data;
            }
            int adjPosition = position - numHeaders;
            int adapterCount = 0;
            if (this.mAdapter == null || adjPosition >= (adapterCount = this.mAdapter.getCount())) {
                return this.mFooterViewInfos.get(adjPosition - adapterCount).data;
            }
            return this.mAdapter.getItem(adjPosition);
        }

        public long getItemId(int position) {
            int adjPosition;
            int numHeaders = getHeadersCount();
            if (this.mAdapter == null || position < numHeaders || (adjPosition = position - numHeaders) >= this.mAdapter.getCount()) {
                return -1;
            }
            return this.mAdapter.getItemId(adjPosition);
        }

        public boolean hasStableIds() {
            if (this.mAdapter != null) {
                return this.mAdapter.hasStableIds();
            }
            return false;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            int numHeaders = getHeadersCount();
            if (position < numHeaders) {
                return this.mHeaderViewInfos.get(position).view;
            }
            int adjPosition = position - numHeaders;
            int adapterCount = 0;
            if (this.mAdapter == null || adjPosition >= (adapterCount = this.mAdapter.getCount())) {
                return this.mFooterViewInfos.get(adjPosition - adapterCount).view;
            }
            return this.mAdapter.getView(adjPosition, convertView, parent);
        }

        public int getItemViewType(int position) {
            int adjPosition;
            int numHeaders = getHeadersCount();
            if (this.mAdapter == null || position < numHeaders || (adjPosition = position - numHeaders) >= this.mAdapter.getCount()) {
                return -2;
            }
            return this.mAdapter.getItemViewType(adjPosition);
        }

        public int getViewTypeCount() {
            if (this.mAdapter != null) {
                return this.mAdapter.getViewTypeCount();
            }
            return 1;
        }

        public void registerDataSetObserver(DataSetObserver observer) {
            if (this.mAdapter != null) {
                this.mAdapter.registerDataSetObserver(observer);
            }
        }

        public void unregisterDataSetObserver(DataSetObserver observer) {
            if (this.mAdapter != null) {
                this.mAdapter.unregisterDataSetObserver(observer);
            }
        }

        public Filter getFilter() {
            if (this.mIsFilterable) {
                return ((Filterable) this.mAdapter).getFilter();
            }
            return null;
        }

        public ListAdapter getWrappedAdapter() {
            return this.mAdapter;
        }
    }

    public static String getExceptionString(Throwable e) {
        if (e == null) {
            return "e==null";
        }
        StringBuffer err = new StringBuffer();
        err.append(e.toString());
        err.append("\n");
        err.append("at ");
        StackTraceElement[] stack = e.getStackTrace();
        if (stack != null) {
            for (StackTraceElement stackTraceElement : stack) {
                err.append(stackTraceElement.toString());
                err.append("\n");
            }
        }
        Throwable cause = e.getCause();
        if (cause != null) {
            err.append("\n");
            err.append("Caused by: ");
            err.append(getExceptionString(cause));
        }
        return err.toString();
    }
}
