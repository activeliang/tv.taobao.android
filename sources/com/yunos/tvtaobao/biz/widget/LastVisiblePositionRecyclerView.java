package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class LastVisiblePositionRecyclerView extends RecyclerView {
    private int lastExposePosition = -1;

    public LastVisiblePositionRecyclerView(Context context) {
        super(context);
    }

    public LastVisiblePositionRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LastVisiblePositionRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == 0) {
            getLastVisiblePosition();
        }
    }

    public int getLastVisiblePosition() {
        int childCount = getChildCount();
        if (childCount == 0) {
            return 0;
        }
        int lastVisiblePosition = getChildAdapterPosition(getChildAt(childCount - 1));
        if (lastVisiblePosition <= this.lastExposePosition) {
            return lastVisiblePosition;
        }
        setLastExposePosition(lastVisiblePosition);
        return lastVisiblePosition;
    }

    public int getLastExposePosition() {
        return this.lastExposePosition;
    }

    public void setLastExposePosition(int lastExposePosition2) {
        this.lastExposePosition = lastExposePosition2;
    }
}
