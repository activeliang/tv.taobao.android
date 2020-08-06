package com.tvtaobao.android.buildorderwares.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.tvtaobao.android.buildorderwares.BOWConfig;
import com.tvtaobao.android.buildorderwares.R;

public class CCFrameLayout extends FrameLayout {
    private static final String TAG = CCFrameLayout.class.getSimpleName();
    private Path clipPath;
    private Paint copyPaint;
    private float[] corners;
    DrawWay drawWay;
    private RectF fitRectF;
    float initRadius;
    private Bitmap offscreenBmp;
    private Canvas offscreenCanvas;
    private Paint offscreenPaint;
    private Path offscreenPath;
    float radius;
    Style style;

    public enum DrawWay {
        poor,
        normal
    }

    public enum Style {
        blank,
        clipCorner
    }

    public CCFrameLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public CCFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CCFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.corners = null;
        setWillNotDraw(false);
        this.initRadius = context.getResources().getDimension(R.dimen.values_dp_3);
        if (!(context == null || attrs == null)) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.buildorderwares_styled_a);
            this.initRadius = ta.getDimension(R.styleable.buildorderwares_styled_a_buildorderwares_style_a_radius, this.initRadius);
            ta.recycle();
        }
        init();
    }

    private void init() {
        setDrawWay(DrawWay.normal);
        setStyle(Style.clipCorner);
        setRadius(this.initRadius);
    }

    private void doPoorDispatchDraw(Canvas canvas) {
        canvas.save();
        boolean changed = calculateFitRectF();
        try {
            if (this.clipPath == null) {
                this.clipPath = new Path();
                this.clipPath.addRoundRect(this.fitRectF, this.corners, Path.Direction.CW);
            }
            if (changed) {
                this.clipPath.reset();
                this.clipPath.addRoundRect(this.fitRectF, this.corners, Path.Direction.CW);
            }
            canvas.clipPath(this.clipPath);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        if (canvas != null) {
            try {
                if (this.drawWay == DrawWay.poor) {
                    doPoorDispatchDraw(canvas);
                } else {
                    doNormalDispatchDraw(canvas);
                }
            } catch (Throwable th) {
                super.dispatchDraw(canvas);
            }
        }
    }

    private void doNormalDispatchDraw(Canvas canvas) {
        if (this.copyPaint == null) {
            this.copyPaint = new Paint();
            this.copyPaint.setAntiAlias(true);
        }
        if (this.offscreenCanvas == null) {
            this.offscreenCanvas = new Canvas();
        }
        if (this.offscreenPath == null) {
            this.offscreenPath = new Path();
        }
        if (this.offscreenPaint == null) {
            this.offscreenPaint = new Paint();
            this.offscreenPaint.setAntiAlias(true);
            this.offscreenPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        boolean changed = calculateFitRectF();
        if (this.offscreenBmp == null) {
            this.offscreenBmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            this.offscreenCanvas.setBitmap(this.offscreenBmp);
        }
        if (changed && this.offscreenBmp != null) {
            if (!this.offscreenBmp.isRecycled()) {
                this.offscreenBmp.recycle();
            }
            this.offscreenBmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            this.offscreenCanvas.setBitmap(this.offscreenBmp);
        }
        if (this.offscreenCanvas != null && this.offscreenPaint != null && this.offscreenBmp != null) {
            super.dispatchDraw(this.offscreenCanvas);
            RectF rectf = new RectF();
            if (!isInvalid(0, 1)) {
                float w = this.corners[0] * 2.0f;
                float h = this.corners[1] * 2.0f;
                this.offscreenPath.reset();
                this.offscreenPath.moveTo(0.0f, h / 2.0f);
                rectf.set(0.0f, 0.0f, w, h);
                this.offscreenPath.arcTo(rectf, 180.0f, 90.0f);
                this.offscreenPath.lineTo(0.0f, 0.0f);
                this.offscreenPath.close();
                this.offscreenCanvas.drawPath(this.offscreenPath, this.offscreenPaint);
            }
            if (!isInvalid(2, 3)) {
                float w2 = this.corners[2] * 2.0f;
                float h2 = this.corners[3] * 2.0f;
                this.offscreenPath.reset();
                this.offscreenPath.moveTo(((float) getWidth()) - (w2 / 2.0f), 0.0f);
                rectf.set(((float) getWidth()) - w2, 0.0f, (float) getWidth(), h2);
                this.offscreenPath.arcTo(rectf, 270.0f, 90.0f);
                this.offscreenPath.lineTo((float) getWidth(), 0.0f);
                this.offscreenPath.close();
                this.offscreenCanvas.drawPath(this.offscreenPath, this.offscreenPaint);
            }
            if (!isInvalid(4, 5)) {
                float w3 = this.corners[4] * 2.0f;
                float h3 = this.corners[5] * 2.0f;
                this.offscreenPath.reset();
                this.offscreenPath.moveTo((float) getWidth(), ((float) getHeight()) - (h3 / 2.0f));
                rectf.set(((float) getWidth()) - w3, ((float) getHeight()) - h3, (float) getWidth(), (float) getHeight());
                this.offscreenPath.arcTo(rectf, 0.0f, 90.0f);
                this.offscreenPath.lineTo((float) getWidth(), (float) getHeight());
                this.offscreenPath.close();
                this.offscreenCanvas.drawPath(this.offscreenPath, this.offscreenPaint);
            }
            if (!isInvalid(6, 7)) {
                float w4 = this.corners[6] * 2.0f;
                float h4 = this.corners[7] * 2.0f;
                this.offscreenPath.reset();
                this.offscreenPath.moveTo(0.0f, ((float) getHeight()) - (h4 / 2.0f));
                rectf.set(0.0f, ((float) getHeight()) - h4, w4, (float) getHeight());
                this.offscreenPath.arcTo(rectf, 180.0f, -90.0f);
                this.offscreenPath.lineTo(0.0f, (float) getHeight());
                this.offscreenPath.close();
                this.offscreenCanvas.drawPath(this.offscreenPath, this.offscreenPaint);
            }
            canvas.drawBitmap(this.offscreenBmp, 0.0f, 0.0f, this.copyPaint);
        }
    }

    private boolean isInvalid(int i1, int i2) {
        if (this.corners == null || this.corners.length <= i1 || this.corners.length <= i2 || this.corners[i1] != 0.0f || this.corners[i2] != 0.0f) {
            return false;
        }
        return true;
    }

    private int getFitSize() {
        float rtn = 0.0f;
        int i = 0;
        while (this.corners != null && i < this.corners.length) {
            if (this.corners[i] > rtn) {
                rtn = (float) ((int) this.corners[i]);
            }
            i++;
        }
        return Math.round(2.0f * rtn);
    }

    private boolean calculateFitRectF() {
        if (this.fitRectF == null) {
            this.fitRectF = new RectF(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
            return true;
        } else if (this.fitRectF.width() == ((float) getWidth()) && this.fitRectF.height() == ((float) getHeight())) {
            return false;
        } else {
            this.fitRectF.set(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
            return true;
        }
    }

    public void setCorners(float[] corners2) {
        if (corners2 != null && corners2.length == 8) {
            this.corners = corners2;
        }
        invalidateDrawParams();
    }

    public Style getStyle() {
        return this.style;
    }

    public void setStyle(Style style2) {
        if (style2 != null) {
            this.style = style2;
            if (style2 == Style.blank) {
                setCorners((float[]) null);
            } else if (style2 == Style.clipCorner) {
                setCorners(new float[]{this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f});
            }
        }
    }

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius2) {
        this.radius = radius2;
        setStyle(this.style);
    }

    public DrawWay getDrawWay() {
        return this.drawWay;
    }

    public void setDrawWay(DrawWay drawWay2) {
        this.drawWay = drawWay2;
        postInvalidate();
    }

    private void invalidateDrawParams() {
        if (this.offscreenPath != null) {
            this.offscreenPath.reset();
            this.offscreenPath = null;
        }
        postInvalidate();
    }

    public boolean isInEditMode() {
        if (BOWConfig.DEBUG) {
            return true;
        }
        return super.isInEditMode();
    }
}
