package com.yunos.tvtaobao.biz.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int bottomSpace;
    private int rightSpace;

    public SpaceItemDecoration(int rightSpace2) {
        this.rightSpace = rightSpace2;
    }

    public SpaceItemDecoration(int rightSpace2, int bottomSpace2) {
        this.rightSpace = rightSpace2;
        this.bottomSpace = bottomSpace2;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = this.rightSpace;
        outRect.bottom = this.bottomSpace;
    }
}
