package com.yunos.tvtaobao.biz.widget.newsku.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.util.BitMapUtil;
import com.yunos.tvtaobao.businessview.R;

@SuppressLint({"AppCompatCustomView"})
public class SkuItem extends TextView {
    private State currentState = State.UNSELECT;
    private Bitmap disableUnfocusBG;
    private int pading = getResources().getDimensionPixelSize(R.dimen.dp_1);
    private Bitmap selectFocused;
    private Bitmap selectUnfocus;
    private long valueId;

    public enum State {
        UNSELECT,
        SELECT,
        DISABLE
    }

    public SkuItem(Context context) {
        super(context);
        initView(context);
    }

    public SkuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SkuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initBitmap() {
        this.selectFocused = BitMapUtil.readBmp(CoreApplication.getApplication(), R.drawable.iv_sku_item_select_focused);
        this.selectUnfocus = BitMapUtil.readBmp(CoreApplication.getApplication(), R.drawable.iv_sku_item_select_unfocus);
        this.disableUnfocusBG = BitMapUtil.readBmp(CoreApplication.getApplication(), R.drawable.iv_sku_item_disable_bg);
    }

    private void initView(Context context) {
        initBitmap();
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.END);
        setBackgroundResource(R.drawable.bg_sku_item_unfocused_enable);
        setTextColor(Color.parseColor("#202020"));
    }

    public void setValueId(long vid) {
        this.valueId = vid;
    }

    public long getValueId() {
        return this.valueId;
    }

    public void setState(State state) {
        if (this.currentState != state) {
            this.currentState = state;
            updateBackground(isFocused());
            invalidate();
        }
    }

    public State getCurrentState() {
        return this.currentState;
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        startMarquee(gainFocus);
        updateBackground(gainFocus);
    }

    private void updateBackground(boolean isFocused) {
        if (isFocused) {
            setBackgroundResource(R.drawable.bg_sku_item_focused_color);
            setTextColor(Color.parseColor("#FFFFFF"));
            return;
        }
        switch (this.currentState) {
            case SELECT:
            case UNSELECT:
                setBackgroundResource(R.drawable.bg_sku_item_unfocused_enable);
                setTextColor(Color.parseColor("#202020"));
                return;
            case DISABLE:
                setBackgroundResource(R.drawable.bg_sku_item_unfocused_disable);
                setTextColor(Color.parseColor("#afafaf"));
                return;
            default:
                return;
        }
    }

    private void startMarquee(boolean gainFocus) {
        if (gainFocus) {
            setSelected(true);
            setEllipsize(TextUtils.TruncateAt.MARQUEE);
            return;
        }
        setEllipsize(TextUtils.TruncateAt.END);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (State.SELECT == this.currentState) {
            if (isFocused()) {
                Rect src = new Rect();
                src.left = 0;
                src.top = 0;
                src.right = this.selectUnfocus.getWidth();
                src.bottom = this.selectUnfocus.getHeight();
                Rect target = new Rect();
                target.left = getMeasuredWidth() - getMeasuredHeight();
                target.top = this.pading;
                target.right = getMeasuredWidth() - this.pading;
                target.bottom = getMeasuredHeight() - this.pading;
                canvas.drawBitmap(this.selectFocused, src, target, getPaint());
            } else {
                Rect src2 = new Rect();
                src2.left = 0;
                src2.top = 0;
                src2.right = this.selectUnfocus.getWidth();
                src2.bottom = this.selectUnfocus.getHeight();
                Rect target2 = new Rect();
                target2.left = getMeasuredWidth() - getMeasuredHeight();
                target2.top = this.pading;
                target2.right = getMeasuredWidth() - this.pading;
                target2.bottom = getMeasuredHeight() - this.pading;
                canvas.drawBitmap(this.selectUnfocus, src2, target2, getPaint());
            }
        }
        super.onDraw(canvas);
    }
}
