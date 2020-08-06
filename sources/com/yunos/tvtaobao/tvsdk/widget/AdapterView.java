package com.yunos.tvtaobao.tvsdk.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Adapter;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.FocusStateListener;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.reflect.Field;

public abstract class AdapterView<T extends Adapter> extends ViewGroup {
    public static final int INVALID_POSITION = -1;
    public static final long INVALID_ROW_ID = Long.MIN_VALUE;
    public static final int ITEM_VIEW_TYPE_HEADER_OR_FOOTER = -2;
    public static final int ITEM_VIEW_TYPE_IGNORE = -1;
    static final int SYNC_FIRST_POSITION = 1;
    static final int SYNC_MAX_DURATION_MILLIS = 100;
    static final int SYNC_SELECTED_POSITION = 0;
    protected static final int TUI_TEXT_COLOR_GREY = -6710887;
    protected static final int TUI_TEXT_COLOR_WHITE = -1;
    protected static final int TUI_TEXT_SIZE_2 = 24;
    private String TAG = "AdapterView";
    boolean mBlockLayoutRequests = false;
    protected boolean mDataChanged;
    private boolean mDesiredFocusableInTouchModeState;
    private boolean mDesiredFocusableState;
    private View mEmptyView;
    @ViewDebug.ExportedProperty(category = "scrolling")
    protected int mFirstPosition = 0;
    protected FocusStateListener mFocusStateListener = null;
    boolean mInLayout = false;
    @ViewDebug.ExportedProperty(category = "list")
    protected int mItemCount;
    int mLayoutHeight;
    protected boolean mNeedSync = false;
    @ViewDebug.ExportedProperty(category = "list")
    protected int mNextSelectedPosition = -1;
    long mNextSelectedRowId = Long.MIN_VALUE;
    protected int mOldItemCount;
    protected int mOldSelectedPosition = -1;
    protected long mOldSelectedRowId = Long.MIN_VALUE;
    OnItemClickListener mOnItemClickListener;
    OnItemLongClickListener mOnItemLongClickListener;
    OnItemSelectedListener mOnItemSelectedListener;
    @ViewDebug.ExportedProperty(category = "list")
    protected int mSelectedPosition = -1;
    long mSelectedRowId = Long.MIN_VALUE;
    private AdapterView<T>.SelectionNotifier mSelectionNotifier;
    protected int mSpecificTop;
    long mSyncHeight;
    int mSyncMode;
    protected int mSyncPosition;
    long mSyncRowId = Long.MIN_VALUE;

    public interface OnItemClickListener {
        void onItemClick(AdapterView<?> adapterView, View view, int i, long j);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j);
    }

    public interface OnItemSelectedListener {
        void onItemSelected(AdapterView<?> adapterView, View view, int i, long j);

        void onNothingSelected(AdapterView<?> adapterView);
    }

    public abstract T getAdapter();

    public abstract View getSelectedView();

    public abstract void setAdapter(T t);

    public abstract void setSelection(int i);

    public AdapterView(Context context) {
        super(context);
    }

    public AdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdapterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnFocusStateListener(FocusStateListener l) {
        this.mFocusStateListener = l;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public boolean performItemClick(View view, int position, long id) {
        ZpLogger.i(this.TAG, this.TAG + ".performItemClick.view = " + view + ".positon = " + position + ".id = " + id);
        if (this.mOnItemClickListener == null) {
            return false;
        }
        playSoundEffect(0);
        if (view != null) {
            view.sendAccessibilityEvent(1);
        }
        this.mOnItemClickListener.onItemClick(this, view, position, id);
        return true;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        if (!isLongClickable()) {
            setLongClickable(true);
        }
        this.mOnItemLongClickListener = listener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.mOnItemSelectedListener = listener;
    }

    public static class AdapterContextMenuInfo implements ContextMenu.ContextMenuInfo {
        public long id;
        public int position;
        public View targetView;

        public AdapterContextMenuInfo(View targetView2, int position2, long id2) {
            this.targetView = targetView2;
            this.position = position2;
            this.id = id2;
        }
    }

    public void addView(View child) {
        throw new UnsupportedOperationException("addView(View) is not supported in AdapterView");
    }

    public void addView(View child, int index) {
        throw new UnsupportedOperationException("addView(View, int) is not supported in AdapterView");
    }

    public void addView(View child, ViewGroup.LayoutParams params) {
        throw new UnsupportedOperationException("addView(View, LayoutParams) is not supported in AdapterView");
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        throw new UnsupportedOperationException("addView(View, int, LayoutParams) is not supported in AdapterView");
    }

    public void removeView(View child) {
        throw new UnsupportedOperationException("removeView(View) is not supported in AdapterView");
    }

    public void removeViewAt(int index) {
        throw new UnsupportedOperationException("removeViewAt(int) is not supported in AdapterView");
    }

    public void removeAllViews() {
        throw new UnsupportedOperationException("removeAllViews() is not supported in AdapterView");
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.mLayoutHeight = getHeight();
    }

    @ViewDebug.CapturedViewProperty
    public int getSelectedItemPosition() {
        return this.mNextSelectedPosition;
    }

    @ViewDebug.CapturedViewProperty
    public long getSelectedItemId() {
        return this.mNextSelectedRowId;
    }

    public Object getSelectedItem() {
        T adapter = getAdapter();
        int selection = getSelectedItemPosition();
        if (adapter == null || adapter.getCount() <= 0 || selection < 0) {
            return null;
        }
        return adapter.getItem(selection);
    }

    @ViewDebug.CapturedViewProperty
    public int getCount() {
        return this.mItemCount;
    }

    public int getPositionForView(View view) {
        View listItem = view;
        while (true) {
            try {
                View v = (View) listItem.getParent();
                if (v.equals(this)) {
                    break;
                }
                listItem = v;
            } catch (ClassCastException e) {
                return -1;
            }
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (getChildAt(i).equals(listItem)) {
                return this.mFirstPosition + i;
            }
        }
        return -1;
    }

    public int getFirstVisiblePosition() {
        return this.mFirstPosition;
    }

    public int getLastVisiblePosition() {
        return (this.mFirstPosition + getChildCount()) - 1;
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        T adapter = getAdapter();
        updateEmptyStatus(adapter == null || adapter.isEmpty());
    }

    public View getEmptyView() {
        return this.mEmptyView;
    }

    /* access modifiers changed from: package-private */
    public boolean isInFilterMode() {
        return false;
    }

    public void setFocusable(boolean focusable) {
        T adapter = getAdapter();
        if (adapter == null || adapter.getCount() == 0) {
        }
        this.mDesiredFocusableState = focusable;
        if (!focusable) {
            this.mDesiredFocusableInTouchModeState = false;
        }
        super.setFocusable(focusable);
    }

    public void setFocusableInTouchMode(boolean focusable) {
        T adapter = getAdapter();
        if (adapter == null || adapter.getCount() == 0) {
        }
        this.mDesiredFocusableInTouchModeState = focusable;
        if (focusable) {
            this.mDesiredFocusableState = true;
        }
        super.setFocusableInTouchMode(focusable);
    }

    /* access modifiers changed from: package-private */
    public void checkFocus() {
        boolean empty;
        boolean z = false;
        T adapter = getAdapter();
        if (adapter == null || adapter.getCount() == 0) {
            empty = true;
        } else {
            empty = false;
        }
        if (!empty || isInFilterMode()) {
        }
        super.setFocusableInTouchMode(this.mDesiredFocusableInTouchModeState);
        super.setFocusable(this.mDesiredFocusableState);
        if (this.mEmptyView != null) {
            if (adapter == null || adapter.isEmpty()) {
                z = true;
            }
            updateEmptyStatus(z);
        }
    }

    @SuppressLint({"WrongCall"})
    private void updateEmptyStatus(boolean empty) {
        if (isInFilterMode()) {
            empty = false;
        }
        if (empty) {
            if (this.mEmptyView != null) {
                this.mEmptyView.setVisibility(0);
                setVisibility(8);
            } else {
                setVisibility(0);
            }
            if (this.mDataChanged) {
                onLayout(false, getLeft(), getTop(), getRight(), getBottom());
                return;
            }
            return;
        }
        if (this.mEmptyView != null) {
            this.mEmptyView.setVisibility(8);
        }
        setVisibility(0);
    }

    public Object getItemAtPosition(int position) {
        T adapter = getAdapter();
        if (adapter == null || position < 0) {
            return null;
        }
        return adapter.getItem(position);
    }

    public long getItemIdAtPosition(int position) {
        T adapter = getAdapter();
        if (adapter == null || position < 0) {
            return Long.MIN_VALUE;
        }
        return adapter.getItemId(position);
    }

    public void setOnClickListener(View.OnClickListener l) {
        throw new RuntimeException("Don't call setOnClickListener for an AdapterView. You probably want setOnItemClickListener instead");
    }

    /* access modifiers changed from: protected */
    public void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    /* access modifiers changed from: protected */
    public void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    class AdapterDataSetObserver extends DataSetObserver {
        private Parcelable mInstanceState = null;

        AdapterDataSetObserver() {
        }

        public void onChanged() {
            AdapterView.this.mDataChanged = true;
            AdapterView.this.mOldItemCount = AdapterView.this.mItemCount;
            AdapterView.this.mItemCount = AdapterView.this.getAdapter().getCount();
            if (!AdapterView.this.getAdapter().hasStableIds() || this.mInstanceState == null || AdapterView.this.mOldItemCount != 0 || AdapterView.this.mItemCount <= 0) {
                AdapterView.this.rememberSyncState();
            } else {
                AdapterView.this.onRestoreInstanceState(this.mInstanceState);
                this.mInstanceState = null;
            }
            AdapterView.this.checkFocus();
            AdapterView.this.requestLayout();
        }

        public void onInvalidated() {
            AdapterView.this.mDataChanged = true;
            if (AdapterView.this.getAdapter().hasStableIds()) {
                this.mInstanceState = AdapterView.this.onSaveInstanceState();
            }
            AdapterView.this.mOldItemCount = AdapterView.this.mItemCount;
            AdapterView.this.mItemCount = 0;
            AdapterView.this.mSelectedPosition = -1;
            AdapterView.this.mSelectedRowId = Long.MIN_VALUE;
            AdapterView.this.mNextSelectedPosition = -1;
            AdapterView.this.mNextSelectedRowId = Long.MIN_VALUE;
            AdapterView.this.mNeedSync = false;
            AdapterView.this.checkFocus();
            AdapterView.this.requestLayout();
        }

        public void clearSavedState() {
            this.mInstanceState = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.mSelectionNotifier);
    }

    private class SelectionNotifier implements Runnable {
        private SelectionNotifier() {
        }

        public void run() {
            if (!AdapterView.this.mDataChanged) {
                AdapterView.this.fireOnSelected();
            } else if (AdapterView.this.getAdapter() != null) {
                AdapterView.this.post(this);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void selectionChanged() {
        if (this.mOnItemSelectedListener != null) {
            if (this.mInLayout || this.mBlockLayoutRequests) {
                if (this.mSelectionNotifier == null) {
                    this.mSelectionNotifier = new SelectionNotifier();
                }
                post(this.mSelectionNotifier);
            } else {
                fireOnSelected();
            }
        }
        if (this.mSelectedPosition != -1 && isShown() && !isInTouchMode()) {
            sendAccessibilityEvent(4);
        }
    }

    /* access modifiers changed from: private */
    public void fireOnSelected() {
        if (this.mOnItemSelectedListener != null) {
            int selection = getSelectedItemPosition();
            if (selection < 0 || selection >= getCount()) {
                this.mOnItemSelectedListener.onNothingSelected(this);
                return;
            }
            View v = getSelectedView();
            this.mOnItemSelectedListener.onItemSelected(this, v, selection, getAdapter().getItemId(selection));
        }
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        View selectedView = getSelectedView();
        if (selectedView == null || selectedView.getVisibility() != 0 || !selectedView.dispatchPopulateAccessibilityEvent(event)) {
            return false;
        }
        return true;
    }

    public boolean onRequestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        if (!super.onRequestSendAccessibilityEvent(child, event)) {
            return false;
        }
        AccessibilityEvent record = AccessibilityEvent.obtain();
        onInitializeAccessibilityEvent(record);
        child.dispatchPopulateAccessibilityEvent(record);
        event.appendRecord(record);
        return true;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setScrollable(isScrollableForAccessibility());
        View selectedView = getSelectedView();
        if (selectedView != null) {
            info.setEnabled(selectedView.isEnabled());
        }
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setScrollable(isScrollableForAccessibility());
        View selectedView = getSelectedView();
        if (selectedView != null) {
            event.setEnabled(selectedView.isEnabled());
        }
        event.setCurrentItemIndex(getSelectedItemPosition());
        event.setFromIndex(getFirstVisiblePosition());
        event.setToIndex(getLastVisiblePosition());
        event.setItemCount(getCount());
    }

    private boolean isScrollableForAccessibility() {
        int itemCount;
        T adapter = getAdapter();
        if (adapter == null || (itemCount = adapter.getCount()) <= 0) {
            return false;
        }
        if (getFirstVisiblePosition() > 0 || getLastVisiblePosition() < itemCount - 1) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean canAnimate() {
        return getLayoutAnimation() != null && this.mItemCount > 0;
    }

    /* access modifiers changed from: protected */
    public void handleDataChanged() {
        int count = this.mItemCount;
        boolean found = false;
        if (count > 0) {
            if (this.mNeedSync) {
                this.mNeedSync = false;
                int newPos = findSyncPosition();
                if (newPos >= 0 && lookForSelectablePosition(newPos, true) == newPos) {
                    setNextSelectedPositionInt(newPos);
                    found = true;
                }
            }
            if (!found) {
                int newPos2 = getSelectedItemPosition();
                if (newPos2 >= count) {
                    newPos2 = count - 1;
                }
                if (newPos2 < 0) {
                    newPos2 = 0;
                }
                int selectablePos = lookForSelectablePosition(newPos2, true);
                if (selectablePos < 0) {
                    selectablePos = lookForSelectablePosition(newPos2, false);
                }
                if (selectablePos >= 0) {
                    setNextSelectedPositionInt(selectablePos);
                    checkSelectionChanged();
                    found = true;
                }
            }
        }
        if (!found) {
            this.mSelectedPosition = -1;
            this.mSelectedRowId = Long.MIN_VALUE;
            this.mNextSelectedPosition = -1;
            this.mNextSelectedRowId = Long.MIN_VALUE;
            this.mNeedSync = false;
            checkSelectionChanged();
        }
    }

    /* access modifiers changed from: protected */
    public void checkSelectionChanged() {
        if (this.mSelectedPosition != this.mOldSelectedPosition || this.mSelectedRowId != this.mOldSelectedRowId) {
            selectionChanged();
            this.mOldSelectedPosition = this.mSelectedPosition;
            this.mOldSelectedRowId = this.mSelectedRowId;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: CFG modification limit reached, blocks count: 139 */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0063, code lost:
        r13 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x006d, code lost:
        if (r7 == false) goto L_0x006f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int findSyncPosition() {
        /*
            r20 = this;
            r0 = r20
            int r3 = r0.mItemCount
            if (r3 != 0) goto L_0x0008
            r13 = -1
        L_0x0007:
            return r13
        L_0x0008:
            r0 = r20
            long r10 = r0.mSyncRowId
            r0 = r20
            int r13 = r0.mSyncPosition
            r16 = -9223372036854775808
            int r16 = (r10 > r16 ? 1 : (r10 == r16 ? 0 : -1))
            if (r16 != 0) goto L_0x0018
            r13 = -1
            goto L_0x0007
        L_0x0018:
            r16 = 0
            r0 = r16
            int r13 = java.lang.Math.max(r0, r13)
            int r16 = r3 + -1
            r0 = r16
            int r13 = java.lang.Math.min(r0, r13)
            long r16 = android.os.SystemClock.uptimeMillis()
            r18 = 100
            long r4 = r16 + r18
            r6 = r13
            r9 = r13
            r12 = 0
            android.widget.Adapter r2 = r20.getAdapter()
            if (r2 != 0) goto L_0x0045
            r13 = -1
            goto L_0x0007
        L_0x003b:
            if (r7 != 0) goto L_0x0041
            if (r12 == 0) goto L_0x0069
            if (r8 != 0) goto L_0x0069
        L_0x0041:
            int r9 = r9 + 1
            r13 = r9
            r12 = 0
        L_0x0045:
            long r16 = android.os.SystemClock.uptimeMillis()
            int r16 = (r16 > r4 ? 1 : (r16 == r4 ? 0 : -1))
            if (r16 > 0) goto L_0x0063
            long r14 = r2.getItemId(r13)
            int r16 = (r14 > r10 ? 1 : (r14 == r10 ? 0 : -1))
            if (r16 == 0) goto L_0x0007
            int r16 = r3 + -1
            r0 = r16
            if (r9 != r0) goto L_0x0065
            r8 = 1
        L_0x005c:
            if (r6 != 0) goto L_0x0067
            r7 = 1
        L_0x005f:
            if (r8 == 0) goto L_0x003b
            if (r7 == 0) goto L_0x003b
        L_0x0063:
            r13 = -1
            goto L_0x0007
        L_0x0065:
            r8 = 0
            goto L_0x005c
        L_0x0067:
            r7 = 0
            goto L_0x005f
        L_0x0069:
            if (r8 != 0) goto L_0x006f
            if (r12 != 0) goto L_0x0045
            if (r7 != 0) goto L_0x0045
        L_0x006f:
            int r6 = r6 + -1
            r13 = r6
            r12 = 1
            goto L_0x0045
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.tvsdk.widget.AdapterView.findSyncPosition():int");
    }

    /* access modifiers changed from: package-private */
    public int lookForSelectablePosition(int position, boolean lookDown) {
        return position;
    }

    /* access modifiers changed from: package-private */
    public void setSelectedPositionInt(int position) {
        this.mSelectedPosition = position;
        this.mSelectedRowId = getItemIdAtPosition(position);
    }

    /* access modifiers changed from: protected */
    public void setNextSelectedPositionInt(int position) {
        this.mNextSelectedPosition = position;
        this.mNextSelectedRowId = getItemIdAtPosition(position);
        if (this.mNeedSync && this.mSyncMode == 0 && position >= 0) {
            this.mSyncPosition = position;
            this.mSyncRowId = this.mNextSelectedRowId;
        }
    }

    /* access modifiers changed from: package-private */
    public void rememberSyncState() {
        if (getChildCount() > 0) {
            this.mNeedSync = true;
            this.mSyncHeight = (long) this.mLayoutHeight;
            if (this.mSelectedPosition >= 0) {
                View v = getChildAt(this.mSelectedPosition - this.mFirstPosition);
                this.mSyncRowId = this.mNextSelectedRowId;
                this.mSyncPosition = this.mNextSelectedPosition;
                if (v != null) {
                    this.mSpecificTop = v.getTop();
                }
                this.mSyncMode = 0;
                return;
            }
            View v2 = getChildAt(0);
            T adapter = getAdapter();
            if (this.mFirstPosition < 0 || this.mFirstPosition >= adapter.getCount()) {
                this.mSyncRowId = -1;
            } else {
                this.mSyncRowId = adapter.getItemId(this.mFirstPosition);
            }
            this.mSyncPosition = this.mFirstPosition;
            if (v2 != null) {
                this.mSpecificTop = v2.getTop();
            }
            this.mSyncMode = 1;
        }
    }

    /* access modifiers changed from: protected */
    public int getGroupFlags() {
        try {
            Field flags = Class.forName("android.view.ViewGroup").getDeclaredField("mGroupFlags");
            flags.setAccessible(true);
            return flags.getInt(this);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        } catch (ClassNotFoundException e5) {
            e5.printStackTrace();
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void setGroupFlags(int f) {
        try {
            Field flags = Class.forName("android.view.ViewGroup").getDeclaredField("mGroupFlags");
            flags.setAccessible(true);
            flags.setInt(this, f);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        } catch (ClassNotFoundException e5) {
            e5.printStackTrace();
        }
    }

    public void onFocusStart() {
        if (this.mFocusStateListener != null) {
            this.mFocusStateListener.onFocusStart(getSelectedView(), this);
        }
    }

    public void onFocusFinished() {
        if (this.mFocusStateListener != null) {
            this.mFocusStateListener.onFocusFinished(getSelectedView(), this);
        }
    }
}
