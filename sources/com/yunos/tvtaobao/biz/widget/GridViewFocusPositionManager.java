package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusFlipGridView;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusPositionManager;
import com.zhiping.dev.android.logger.ZpLogger;

public class GridViewFocusPositionManager extends FocusPositionManager {
    private static final float DEFAULT_SCROLL_THRESHOLD_RATE = 2.0f;
    private FocusFlipGridView mGridView;
    private float mScrollThresholdRate = DEFAULT_SCROLL_THRESHOLD_RATE;

    public GridViewFocusPositionManager(Context context) {
        super(context);
    }

    public GridViewFocusPositionManager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewFocusPositionManager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setGridView(FocusFlipGridView mGridView2) {
        this.mGridView = mGridView2;
    }

    public void setGridViewScrollRate(float rate) {
        this.mScrollThresholdRate = rate;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.mGridView != null && !this.mGridView.isFlipFinished()) {
            if (keyCode == 19 && this.mGridView.isFlipDown()) {
                return true;
            }
            if (keyCode == 20 && !this.mGridView.isFlipDown()) {
                return true;
            }
            if (keyCode == 19 || keyCode == 20) {
                int firstLeft = this.mGridView.getFlipItemLeftMoveDistance(this.mGridView.getFirstPosition(), 0);
                int maxDistance = (int) (((float) this.mGridView.getHeight()) * this.mScrollThresholdRate);
                if (maxDistance > 0 && Math.abs(firstLeft) > maxDistance) {
                    ZpLogger.w(TAG, "left move distance over max distance");
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
