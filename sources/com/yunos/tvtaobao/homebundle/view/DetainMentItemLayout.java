package com.yunos.tvtaobao.homebundle.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.yunos.tvtaobao.biz.common.DrawRect;
import com.yunos.tvtaobao.homebundle.R;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;

public class DetainMentItemLayout extends FrameLayout implements ItemListener {
    private DrawRect mDrawRect;

    public DetainMentItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DetainMentItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DetainMentItemLayout(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.mDrawRect = new DrawRect(this);
    }

    public void setSelect(boolean selected) {
        if (!selected) {
            this.mDrawRect.showMark();
        } else {
            this.mDrawRect.hideMark();
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        this.mDrawRect.drawRect(canvas, this);
    }

    public void drawAfterFocus(Canvas arg0) {
    }

    public void drawBeforeFocus(Canvas arg0) {
    }

    private Rect getFocusedRect() {
        Rect r = new Rect();
        getFocusedRect(r);
        r.top += (int) getResources().getDimension(R.dimen.dp_2);
        r.bottom -= (int) getResources().getDimension(R.dimen.dp_2);
        return r;
    }

    public FocusRectParams getFocusParams() {
        return new FocusRectParams(getFocusedRect(), 0.5f, 0.5f);
    }

    public int getItemHeight() {
        return getHeight();
    }

    public int getItemWidth() {
        return getWidth();
    }

    public Rect getManualPadding() {
        return null;
    }

    public boolean isFinished() {
        return true;
    }

    public boolean isScale() {
        return true;
    }
}
