package com.yunos.tvtaobao.homebundle.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import com.yunos.tvtaobao.homebundle.R;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;

public class DetainMentTextView extends AppCompatTextView implements ItemListener {
    private int step_pad;

    public DetainMentTextView(Context context) {
        super(context);
        init();
    }

    public DetainMentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DetainMentTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.step_pad = getResources().getDimensionPixelSize(R.dimen.dp_1);
    }

    private void onAdjustFocusedRect(Rect r) {
        r.top += this.step_pad;
        r.bottom -= this.step_pad;
    }

    private Rect getFocusedRect() {
        Rect r = new Rect();
        getFocusedRect(r);
        onAdjustFocusedRect(r);
        return r;
    }

    public FocusRectParams getFocusParams() {
        return new FocusRectParams(getFocusedRect(), 0.5f, 0.5f);
    }

    public boolean isScale() {
        return true;
    }

    public int getItemWidth() {
        return getWidth();
    }

    public int getItemHeight() {
        return getHeight();
    }

    public Rect getManualPadding() {
        return null;
    }

    public void drawBeforeFocus(Canvas canvas) {
    }

    public void drawAfterFocus(Canvas canvas) {
    }

    public boolean isFinished() {
        return true;
    }
}
