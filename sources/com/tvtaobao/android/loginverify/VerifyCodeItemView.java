package com.tvtaobao.android.loginverify;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class VerifyCodeItemView extends TextView {
    private int[] underLineColors;
    private int underLineHeight;
    private Paint underLinePaint;
    private UnderLineStyle underLineStyle;

    public enum UnderLineStyle {
        normal,
        wait4Set
    }

    public VerifyCodeItemView(Context context) {
        this(context, (AttributeSet) null);
    }

    public VerifyCodeItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerifyCodeItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.underLineHeight = 2;
        this.underLineStyle = UnderLineStyle.normal;
        this.underLineColors = new int[UnderLineStyle.values().length];
        this.underLineColors[UnderLineStyle.normal.ordinal()] = Color.parseColor("#66ffffff");
        this.underLineColors[UnderLineStyle.wait4Set.ordinal()] = -1;
    }

    public boolean isInEditMode() {
        return super.isInEditMode();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.underLinePaint == null) {
            this.underLinePaint = new Paint();
            this.underLinePaint.setAntiAlias(true);
            this.underLinePaint.setStyle(Paint.Style.FILL);
        }
        this.underLinePaint.setColor(this.underLineColors[this.underLineStyle.ordinal()]);
        if (canvas != null) {
            canvas.drawRect(0.0f, (float) (getHeight() - this.underLineHeight), (float) getWidth(), (float) getHeight(), this.underLinePaint);
        }
    }

    public int getUnderLineHeight() {
        return this.underLineHeight;
    }

    public void setUnderLineHeight(int underLineHeight2) {
        this.underLineHeight = underLineHeight2;
        postInvalidate();
    }

    public UnderLineStyle getUnderLineStyle() {
        return this.underLineStyle;
    }

    public void setUnderLineStyle(UnderLineStyle style) {
        this.underLineStyle = style;
        if (this.underLineStyle == UnderLineStyle.normal) {
            this.underLineHeight = 3;
        } else if (this.underLineStyle == UnderLineStyle.wait4Set) {
            this.underLineHeight = 6;
        }
        postInvalidate();
    }

    public int[] getUnderLineColors() {
        return this.underLineColors;
    }

    public void setUnderLineColors(int[] underLineColors2) {
        if (underLineColors2.length == this.underLineColors.length) {
            this.underLineColors = underLineColors2;
            postInvalidate();
        }
    }
}
