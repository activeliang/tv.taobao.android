package com.yunos.tvtaobao.biz.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import java.util.List;

public class FlowLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = FlowLayoutManager.class.getSimpleName();
    private SparseArray<Rect> allItemFrames = new SparseArray<>();
    protected int height;
    private int left;
    private int lineNumberMax = 3;
    private List<Row> lineRows = new ArrayList();
    private int num = 0;
    private int right;
    private Row row = new Row();
    final FlowLayoutManager self = this;
    private int top;
    protected int totalHeight = 0;
    private int usedMaxWidth;
    private int verticalScrollOffset = 0;
    protected int width;

    public int getTotalHeight() {
        return this.totalHeight;
    }

    public FlowLayoutManager() {
        setAutoMeasureEnabled(true);
    }

    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(-2, -2);
    }

    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        ZpLogger.d(TAG, "onLayoutChildren");
        this.totalHeight = 0;
        int cuLineTop = this.top;
        int cuLineWidth = 0;
        int maxHeightItem = 0;
        this.row = new Row();
        this.lineRows.clear();
        this.allItemFrames.clear();
        removeAllViews();
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            this.verticalScrollOffset = 0;
        } else if (getChildCount() != 0 || !state.isPreLayout()) {
            detachAndScrapAttachedViews(recycler);
            if (getChildCount() == 0) {
                this.width = getWidth();
                this.height = getHeight();
                this.left = getPaddingLeft();
                this.right = getPaddingRight();
                this.top = getPaddingTop();
                this.usedMaxWidth = (this.width - this.left) - this.right;
            }
            for (int i = 0; i < getItemCount(); i++) {
                ZpLogger.d(TAG, "index:" + i);
                View childAt = recycler.getViewForPosition(i);
                if (8 != childAt.getVisibility()) {
                    measureChildWithMargins(childAt, 0, 0);
                    int childWidth = getDecoratedMeasuredWidth(childAt);
                    int childHeight = getDecoratedMeasuredHeight(childAt);
                    if (cuLineWidth + childWidth <= this.usedMaxWidth) {
                        int itemLeft = this.left + cuLineWidth;
                        Rect frame = this.allItemFrames.get(i);
                        if (frame == null) {
                            frame = new Rect();
                        }
                        frame.set(itemLeft, cuLineTop, itemLeft + childWidth, cuLineTop + childHeight);
                        this.allItemFrames.put(i, frame);
                        cuLineWidth += childWidth;
                        maxHeightItem = Math.max(maxHeightItem, childHeight);
                        this.row.addViews(new Item(childHeight, childAt, frame));
                        this.row.setCuTop((float) cuLineTop);
                        this.row.setMaxHeight((float) maxHeightItem);
                    } else {
                        formatAboveRow();
                        cuLineTop += maxHeightItem;
                        this.totalHeight += maxHeightItem;
                        int itemLeft2 = this.left;
                        Rect frame2 = this.allItemFrames.get(i);
                        if (frame2 == null) {
                            frame2 = new Rect();
                        }
                        frame2.set(itemLeft2, cuLineTop, itemLeft2 + childWidth, cuLineTop + childHeight);
                        this.allItemFrames.put(i, frame2);
                        cuLineWidth = childWidth;
                        maxHeightItem = childHeight;
                        this.row.addViews(new Item(childHeight, childAt, frame2));
                        this.row.setCuTop((float) cuLineTop);
                        this.row.setMaxHeight((float) childHeight);
                    }
                    if (i == getItemCount() - 1) {
                        formatAboveRow();
                        this.totalHeight += maxHeightItem;
                    }
                }
            }
            this.totalHeight = Math.max(this.totalHeight, getVerticalSpace());
            ZpLogger.d(TAG, "onLayoutChildren totalHeight:" + this.totalHeight);
            fillLayout(recycler, state);
        }
    }

    public void setLine(int lineNumberMax2) {
        this.lineNumberMax = lineNumberMax2;
    }

    private void fillLayout(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int resultline;
        if (!state.isPreLayout()) {
            Rect displayFrame = new Rect(getPaddingLeft(), getPaddingTop() + this.verticalScrollOffset, getWidth() - getPaddingRight(), this.verticalScrollOffset + (getHeight() - getPaddingBottom()));
            if (this.lineNumberMax == 0 || this.lineNumberMax >= this.lineRows.size()) {
                resultline = this.lineRows.size();
            } else {
                resultline = this.lineNumberMax;
            }
            for (int j = 0; j < resultline; j++) {
                Row row2 = this.lineRows.get(j);
                float lineTop = row2.cuTop;
                float lineBottom = lineTop + row2.maxHeight;
                if (lineTop >= ((float) displayFrame.bottom) || ((float) displayFrame.top) >= lineBottom) {
                    List views = row2.views;
                    for (int i = 0; i < views.size(); i++) {
                        removeAndRecycleView(views.get(i).view, recycler);
                    }
                } else {
                    List views2 = row2.views;
                    for (int i2 = 0; i2 < views2.size(); i2++) {
                        View scrap = views2.get(i2).view;
                        measureChildWithMargins(scrap, 0, 0);
                        addView(scrap);
                        Rect frame = views2.get(i2).rect;
                        layoutDecoratedWithMargins(scrap, frame.left, frame.top - this.verticalScrollOffset, frame.right, frame.bottom - this.verticalScrollOffset);
                    }
                }
            }
        }
    }

    private void formatAboveRow() {
        List<Item> views = this.row.views;
        for (int i = 0; i < views.size(); i++) {
            Item item = views.get(i);
            View view = item.view;
            int position = getPosition(view);
            if (((float) this.allItemFrames.get(position).top) < ((this.row.maxHeight - ((float) views.get(i).useHeight)) / 2.0f) + this.row.cuTop) {
                Rect frame = this.allItemFrames.get(position);
                if (frame == null) {
                    frame = new Rect();
                }
                frame.set(this.allItemFrames.get(position).left, (int) (((this.row.maxHeight - ((float) views.get(i).useHeight)) / 2.0f) + this.row.cuTop), this.allItemFrames.get(position).right, (int) (((this.row.maxHeight - ((float) views.get(i).useHeight)) / 2.0f) + this.row.cuTop + ((float) getDecoratedMeasuredHeight(view))));
                this.allItemFrames.put(position, frame);
                item.setRect(frame);
                views.set(i, item);
            }
        }
        this.row.views = views;
        this.lineRows.add(this.row);
        this.row = new Row();
    }

    public boolean canScrollVertically() {
        return true;
    }

    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        ZpLogger.d(TAG, "totalHeight:" + this.totalHeight);
        int travel = dy;
        if (this.verticalScrollOffset + dy < 0) {
            travel = -this.verticalScrollOffset;
        } else if (this.verticalScrollOffset + dy > this.totalHeight - getVerticalSpace()) {
            travel = (this.totalHeight - getVerticalSpace()) - this.verticalScrollOffset;
        }
        this.verticalScrollOffset += travel;
        offsetChildrenVertical(-travel);
        fillLayout(recycler, state);
        return travel;
    }

    private int getVerticalSpace() {
        return (this.self.getHeight() - this.self.getPaddingBottom()) - this.self.getPaddingTop();
    }

    public int getHorizontalSpace() {
        return (this.self.getWidth() - this.self.getPaddingLeft()) - this.self.getPaddingRight();
    }

    public class Row {
        float cuTop;
        float maxHeight;
        List<Item> views = new ArrayList();

        public Row() {
        }

        public void setCuTop(float cuTop2) {
            this.cuTop = cuTop2;
        }

        public void setMaxHeight(float maxHeight2) {
            this.maxHeight = maxHeight2;
        }

        public void addViews(Item view) {
            this.views.add(view);
        }
    }

    public class Item {
        Rect rect;
        int useHeight;
        View view;

        public void setRect(Rect rect2) {
            this.rect = rect2;
        }

        public Item(int useHeight2, View view2, Rect rect2) {
            this.useHeight = useHeight2;
            this.view = view2;
            this.rect = rect2;
        }
    }
}
