package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

public class TvNestedScrollView extends NestedScrollView {
    public TvNestedScrollView(Context context) {
        this(context, (AttributeSet) null);
    }

    public TvNestedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        try {
            Rect rect1 = new Rect();
            getDrawingRect(rect1);
            Rect rect2 = new Rect();
            View leafView = findFocus();
            leafView.getDrawingRect(rect2);
            offsetDescendantRectToMyCoords(leafView, rect2);
            int dy = rect2.centerY() - rect1.centerY();
            if (dy != 0) {
                smoothScrollBy(0, dy);
                postInvalidate();
            }
        } catch (Throwable var9) {
            var9.printStackTrace();
        }
    }

    public View focusSearch(int direction) {
        return super.focusSearch(direction);
    }
}
