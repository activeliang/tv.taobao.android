package com.tvtaobao.android.ui3.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.tvtaobao.android.ui3.R;

public class SimpleCardView extends FrameLayout {
    private static final String TAG = SimpleCardView.class.getSimpleName();
    /* access modifiers changed from: private */
    public float[] corners;
    private DrawWay drawWay;
    private RectF fitRectF;
    private Paint normalClearPaint;
    private Path normalClearPath;
    private Paint normalCopyPaint;
    private Path poorClipPath;
    private float radius;
    private StrokeDrawable strokeDrawable;
    Runnable syncTask;

    public enum DrawWay {
        poor,
        normal
    }

    public SimpleCardView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SimpleCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.drawWay = DrawWay.poor;
        this.strokeDrawable = new StrokeDrawable();
        this.corners = null;
        this.syncTask = new Runnable() {
            public void run() {
                SimpleCardView.this.postInvalidate();
            }
        };
        setWillNotDraw(false);
        if (context != null && attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ui3wares_SimpleCardView);
            float radius2 = ta.getDimension(R.styleable.ui3wares_SimpleCardView_ui3wares_simpleCardViewRadius, 0.0f);
            ta.recycle();
            setRadius(radius2);
        }
    }

    private void doPoorDraw(Canvas canvas) {
        canvas.save();
        boolean calculateFitRectF = calculateFitRectF();
        try {
            float[] tmpCorners = new float[this.corners.length];
            for (int i = 0; i < this.corners.length; i++) {
                tmpCorners[i] = this.corners[i] / 2.0f;
            }
            if (this.poorClipPath == null) {
                this.poorClipPath = new Path();
            } else {
                this.poorClipPath.reset();
            }
            this.poorClipPath.addRoundRect(this.fitRectF, tmpCorners, Path.Direction.CW);
            canvas.clipPath(this.poorClipPath);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        super.draw(canvas);
        canvas.restore();
    }

    public void draw(Canvas canvas) {
        if (canvas != null) {
            try {
                if (this.drawWay == DrawWay.poor) {
                    doPoorDraw(canvas);
                } else if (this.drawWay == DrawWay.normal) {
                    doNormalDraw(canvas);
                }
            } catch (Throwable th) {
                super.draw(canvas);
            }
            if (this.strokeDrawable != null) {
                this.strokeDrawable.draw(canvas);
            }
        }
    }

    private void doNormalDraw(Canvas canvas) {
        if (this.normalClearPath == null) {
            this.normalClearPath = new Path();
        }
        if (this.normalClearPaint == null) {
            this.normalClearPaint = new Paint();
            this.normalClearPaint.setAntiAlias(true);
            this.normalClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        if (this.normalCopyPaint == null) {
            this.normalCopyPaint = new Paint();
            this.normalCopyPaint.setAntiAlias(true);
        }
        boolean calculateFitRectF = calculateFitRectF();
        if (this.normalClearPaint != null && this.normalClearPath != null) {
            int count = canvas.saveLayer(this.fitRectF, this.normalCopyPaint, 31);
            Canvas useCanvas = canvas;
            super.draw(useCanvas);
            RectF rectf = new RectF();
            if (!isInvalid(0, 1)) {
                float w = this.corners[0];
                float h = this.corners[1];
                this.normalClearPath.reset();
                this.normalClearPath.moveTo(0.0f, h / 2.0f);
                rectf.set(0.0f, 0.0f, w, h);
                this.normalClearPath.arcTo(rectf, 180.0f, 90.0f);
                this.normalClearPath.lineTo(0.0f, 0.0f);
                this.normalClearPath.close();
                useCanvas.drawPath(this.normalClearPath, this.normalClearPaint);
            }
            if (!isInvalid(2, 3)) {
                float w2 = this.corners[2];
                float h2 = this.corners[3];
                this.normalClearPath.reset();
                this.normalClearPath.moveTo(((float) getWidth()) - (w2 / 2.0f), 0.0f);
                rectf.set(((float) getWidth()) - w2, 0.0f, (float) getWidth(), h2);
                this.normalClearPath.arcTo(rectf, 270.0f, 90.0f);
                this.normalClearPath.lineTo((float) getWidth(), 0.0f);
                this.normalClearPath.close();
                useCanvas.drawPath(this.normalClearPath, this.normalClearPaint);
            }
            if (!isInvalid(4, 5)) {
                float w3 = this.corners[4];
                float h3 = this.corners[5];
                this.normalClearPath.reset();
                this.normalClearPath.moveTo((float) getWidth(), ((float) getHeight()) - (h3 / 2.0f));
                rectf.set(((float) getWidth()) - w3, ((float) getHeight()) - h3, (float) getWidth(), (float) getHeight());
                this.normalClearPath.arcTo(rectf, 0.0f, 90.0f);
                this.normalClearPath.lineTo((float) getWidth(), (float) getHeight());
                this.normalClearPath.close();
                useCanvas.drawPath(this.normalClearPath, this.normalClearPaint);
            }
            if (!isInvalid(6, 7)) {
                float w4 = this.corners[6];
                float h4 = this.corners[7];
                this.normalClearPath.reset();
                this.normalClearPath.moveTo(0.0f, ((float) getHeight()) - (h4 / 2.0f));
                rectf.set(0.0f, ((float) getHeight()) - h4, w4, (float) getHeight());
                this.normalClearPath.arcTo(rectf, 180.0f, -90.0f);
                this.normalClearPath.lineTo(0.0f, (float) getHeight());
                this.normalClearPath.close();
                useCanvas.drawPath(this.normalClearPath, this.normalClearPaint);
            }
            canvas.restoreToCount(count);
        }
    }

    /* access modifiers changed from: private */
    public boolean isInvalid(int i1, int i2) {
        if (this.corners == null || this.corners.length <= i1 || this.corners.length <= i2 || this.corners[i1] != 0.0f || this.corners[i2] != 0.0f) {
            return false;
        }
        return true;
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

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius2) {
        this.radius = radius2;
        setCorners(new float[]{radius2 * 2.0f, radius2 * 2.0f, radius2 * 2.0f, radius2 * 2.0f, radius2 * 2.0f, radius2 * 2.0f, radius2 * 2.0f, radius2 * 2.0f});
    }

    public DrawWay getDrawWay() {
        return this.drawWay;
    }

    public void setDrawWay(DrawWay drawWay2) {
        this.drawWay = drawWay2;
        apply();
    }

    private void invalidateDrawParams() {
        if (this.normalClearPath != null) {
            this.normalClearPath.reset();
            this.normalClearPath = null;
        }
        apply();
    }

    public void apply() {
        removeCallbacks(this.syncTask);
        postDelayed(this.syncTask, 100);
    }

    public void setStroke(int width, int color) {
        if (width <= 0 || color == 0) {
            if (this.strokeDrawable != null) {
                this.strokeDrawable.strokeWidth = width;
                this.strokeDrawable.strokeColor = color;
                this.strokeDrawable.enable = false;
            }
        } else if (this.strokeDrawable != null) {
            this.strokeDrawable.strokeWidth = width;
            this.strokeDrawable.strokeColor = color;
            this.strokeDrawable.enable = true;
        }
        invalidateDrawParams();
    }

    private class StrokeDrawable extends Drawable {
        boolean enable;
        Paint paint;
        Path path;
        int strokeColor;
        int strokeWidth;

        private StrokeDrawable() {
            this.enable = false;
            this.strokeWidth = 3;
            this.strokeColor = Color.parseColor("#dedede");
        }

        private void applyCfg() {
            if (this.paint == null) {
                this.paint = new Paint();
                this.paint.setAntiAlias(true);
                this.paint.setStyle(Paint.Style.STROKE);
            }
            float offset = (float) (this.strokeWidth / 2);
            float offset2 = offset;
            float l = 0.0f + offset;
            float t = 0.0f + offset;
            float r = ((float) SimpleCardView.this.getWidth()) - offset;
            float b = ((float) SimpleCardView.this.getHeight()) - offset;
            if (this.path == null) {
                this.path = new Path();
            } else {
                this.path.reset();
            }
            RectF rectf = new RectF();
            if (!SimpleCardView.this.isInvalid(0, 1)) {
                float w = SimpleCardView.this.corners[0] - offset2;
                float h = SimpleCardView.this.corners[1] - offset2;
                this.path.moveTo(l, h / 2.0f);
                rectf.set(l, t, l + w, t + h);
                this.path.arcTo(rectf, 180.0f, 90.0f);
            } else {
                this.path.moveTo(l, t);
            }
            if (!SimpleCardView.this.isInvalid(2, 3)) {
                float w2 = SimpleCardView.this.corners[2] - offset2;
                float h2 = SimpleCardView.this.corners[3] - offset2;
                this.path.lineTo(r - (w2 / 2.0f), t);
                rectf.set(r - w2, t, r, t + h2);
                this.path.arcTo(rectf, 270.0f, 90.0f);
            } else {
                this.path.lineTo(r, t);
            }
            if (!SimpleCardView.this.isInvalid(4, 5)) {
                float w3 = SimpleCardView.this.corners[4] - offset2;
                float h3 = SimpleCardView.this.corners[5] - offset2;
                this.path.lineTo(r, b - (h3 / 2.0f));
                rectf.set(r - w3, b - h3, r, b);
                this.path.arcTo(rectf, 0.0f, 90.0f);
            } else {
                this.path.lineTo(r, b);
            }
            if (!SimpleCardView.this.isInvalid(6, 7)) {
                float w4 = SimpleCardView.this.corners[6] - offset2;
                float h4 = SimpleCardView.this.corners[7] - offset2;
                this.path.lineTo(l + w4, b);
                rectf.set(l, b - h4, l + w4, b);
                this.path.arcTo(rectf, 90.0f, 90.0f);
            } else {
                this.path.lineTo(l, b);
            }
            this.path.close();
            this.paint.setStrokeWidth((float) this.strokeWidth);
            this.paint.setColor(this.strokeColor);
        }

        public void draw(Canvas canvas) {
            if (this.enable) {
                applyCfg();
                canvas.drawPath(this.path, this.paint);
            }
        }

        public void setAlpha(int alpha) {
            if (this.enable) {
                applyCfg();
                this.paint.setAlpha(alpha);
            }
        }

        public void setColorFilter(ColorFilter colorFilter) {
            if (this.enable) {
                applyCfg();
                this.paint.setColorFilter(colorFilter);
            }
        }

        public int getOpacity() {
            return -1;
        }
    }

    private class FillDrawable extends Drawable {
        boolean enable = false;
        int fillColor = 0;
        Paint paint;
        Path path;

        private FillDrawable() {
        }

        private void applyCfg() {
            if (this.paint == null) {
                this.paint = new Paint();
                this.paint.setAntiAlias(true);
                this.paint.setStyle(Paint.Style.FILL);
            }
            float l = 0.0f + 0.0f;
            float t = 0.0f + 0.0f;
            float r = ((float) SimpleCardView.this.getWidth()) - 0.0f;
            float b = ((float) SimpleCardView.this.getHeight()) - 0.0f;
            if (this.path == null) {
                this.path = new Path();
            } else {
                this.path.reset();
            }
            RectF rectf = new RectF();
            if (!SimpleCardView.this.isInvalid(0, 1)) {
                float w = SimpleCardView.this.corners[0] - 0.0f;
                float h = SimpleCardView.this.corners[1] - 0.0f;
                this.path.moveTo(l, h / 2.0f);
                rectf.set(l, t, l + w, t + h);
                this.path.arcTo(rectf, 180.0f, 90.0f);
            } else {
                this.path.moveTo(l, t);
            }
            if (!SimpleCardView.this.isInvalid(2, 3)) {
                float w2 = SimpleCardView.this.corners[2] - 0.0f;
                float h2 = SimpleCardView.this.corners[3] - 0.0f;
                this.path.lineTo(r - (w2 / 2.0f), t);
                rectf.set(r - w2, t, r, t + h2);
                this.path.arcTo(rectf, 270.0f, 90.0f);
            } else {
                this.path.lineTo(r, t);
            }
            if (!SimpleCardView.this.isInvalid(4, 5)) {
                float w3 = SimpleCardView.this.corners[4] - 0.0f;
                float h3 = SimpleCardView.this.corners[5] - 0.0f;
                this.path.lineTo(r, b - (h3 / 2.0f));
                rectf.set(r - w3, b - h3, r, b);
                this.path.arcTo(rectf, 0.0f, 90.0f);
            } else {
                this.path.lineTo(r, b);
            }
            if (!SimpleCardView.this.isInvalid(6, 7)) {
                float w4 = SimpleCardView.this.corners[6] - 0.0f;
                float h4 = SimpleCardView.this.corners[7] - 0.0f;
                this.path.lineTo(l + w4, b);
                rectf.set(l, b - h4, l + w4, b);
                this.path.arcTo(rectf, 90.0f, 90.0f);
            } else {
                this.path.lineTo(l, b);
            }
            this.path.close();
            this.paint.setColor(this.fillColor);
        }

        public void draw(Canvas canvas) {
            if (this.enable) {
                applyCfg();
                canvas.drawPath(this.path, this.paint);
            }
        }

        public void setAlpha(int alpha) {
            if (this.enable) {
                applyCfg();
                this.paint.setAlpha(alpha);
            }
        }

        public void setColorFilter(ColorFilter colorFilter) {
            if (this.enable) {
                applyCfg();
                this.paint.setColorFilter(colorFilter);
            }
        }

        public int getOpacity() {
            return -1;
        }
    }

    public boolean isInEditMode() {
        return super.isInEditMode();
    }
}
