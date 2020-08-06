package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import com.yunos.tvtaobao.tvsdk.widget.FlipGridView;
import com.yunos.tvtaobao.tvsdk.widget.GridView;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusRelativeLayout;
import com.zhiping.dev.android.logger.ZpLogger;

public abstract class TabFlipGridViewHeaderView extends FocusRelativeLayout implements GridView.GridViewHeaderViewExpandDistance, FlipGridView.FlipGridViewHeaderOrFooterInterface {
    protected static final String TAG = "TabFlipGridViewHeaderView";
    private BitmapDrawable mBanderDrawable;
    private Rect mBanderDrawableRect;
    private SparseArray<View> mChildViewMap;

    /* access modifiers changed from: protected */
    public abstract void fillChildViewMap(SparseArray<View> sparseArray);

    public abstract void onHandleHeaderContent(int i);

    public TabFlipGridViewHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setChildrenDrawingOrderEnabled(true);
    }

    public TabFlipGridViewHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
    }

    public TabFlipGridViewHeaderView(Context context) {
        super(context);
        setChildrenDrawingOrderEnabled(true);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    public int getHorCount() {
        return 1;
    }

    public View getView(int index) {
        ZpLogger.i(TAG, "getView --> index =  " + index + "; mChildViewMap = " + this.mChildViewMap + "; this = " + this);
        if (index < 0) {
            return null;
        }
        return this.mChildViewMap.get(index);
    }

    public int getViewIndex(View view) {
        if (view == null || this.mChildViewMap == null) {
            return -1;
        }
        for (int i = 0; i < this.mChildViewMap.size(); i++) {
            int key = this.mChildViewMap.keyAt(i);
            if (this.mChildViewMap.get(key).equals(view)) {
                return key;
            }
        }
        return -1;
    }

    private void init() {
        this.mChildViewMap = new SparseArray<>();
        fillChildViewMap(this.mChildViewMap);
    }

    public void onDestory() {
        if (this.mChildViewMap != null) {
            this.mChildViewMap.clear();
            this.mChildViewMap = null;
        }
        this.mBanderDrawable = null;
        this.mBanderDrawableRect = null;
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect, ViewGroup findRoot) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect, findRoot);
    }

    public void setHeaderBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            this.mBanderDrawable = null;
            this.mBanderDrawable = new BitmapDrawable(bitmap);
            this.mBanderDrawableRect = new Rect();
            this.mBanderDrawableRect.setEmpty();
            this.mBanderDrawableRect.right = -1;
            this.mBanderDrawableRect.top = getUpExpandDistance();
            this.mBanderDrawableRect.bottom = this.mBanderDrawableRect.top + bitmap.getHeight();
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        if (!(this.mBanderDrawable == null || this.mBanderDrawableRect == null)) {
            this.mBanderDrawableRect.right = this.mBanderDrawableRect.left + getWidth();
            this.mBanderDrawable.setBounds(this.mBanderDrawableRect);
            this.mBanderDrawable.draw(canvas);
        }
        super.dispatchDraw(canvas);
    }
}
