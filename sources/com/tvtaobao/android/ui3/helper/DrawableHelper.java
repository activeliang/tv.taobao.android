package com.tvtaobao.android.ui3.helper;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class DrawableHelper {

    public enum PaintStyle {
        psStroke,
        psFill
    }

    public enum ShapeStyle {
        ssHalfCircleInLR,
        ssHalfCircleInTB,
        ssRect,
        ssRoundRect
    }

    public static DrawableA mkStrokeRoundRectDrawable(float radius) {
        DrawableA rtn = new DrawableA();
        rtn.setPaintStyle(PaintStyle.psStroke);
        rtn.setShapeStyle(ShapeStyle.ssRoundRect);
        rtn.setRadius(radius);
        return rtn;
    }

    public static DrawableA mkStrokeRectDrawable() {
        DrawableA rtn = new DrawableA();
        rtn.setPaintStyle(PaintStyle.psStroke);
        rtn.setShapeStyle(ShapeStyle.ssRect);
        return rtn;
    }

    public static DrawableA mkStrokeLRCircleDrawable() {
        DrawableA rtn = new DrawableA();
        rtn.setPaintStyle(PaintStyle.psStroke);
        rtn.setShapeStyle(ShapeStyle.ssHalfCircleInLR);
        return rtn;
    }

    public static DrawableA mkStrokeTBCircleDrawable() {
        DrawableA rtn = new DrawableA();
        rtn.setPaintStyle(PaintStyle.psStroke);
        rtn.setShapeStyle(ShapeStyle.ssHalfCircleInTB);
        return rtn;
    }

    public static DrawableA mkFillRoundRectDrawable(float radius) {
        DrawableA rtn = new DrawableA();
        rtn.setPaintStyle(PaintStyle.psFill);
        rtn.setShapeStyle(ShapeStyle.ssRoundRect);
        rtn.setRadius(radius);
        return rtn;
    }

    public static DrawableA mkFillRectDrawable() {
        DrawableA rtn = new DrawableA();
        rtn.setPaintStyle(PaintStyle.psFill);
        rtn.setShapeStyle(ShapeStyle.ssRect);
        return rtn;
    }

    public static DrawableA mkFillLRCircleDrawable() {
        DrawableA rtn = new DrawableA();
        rtn.setPaintStyle(PaintStyle.psFill);
        rtn.setShapeStyle(ShapeStyle.ssHalfCircleInLR);
        return rtn;
    }

    public static DrawableA mkFillTBCircleDrawable() {
        DrawableA rtn = new DrawableA();
        rtn.setPaintStyle(PaintStyle.psFill);
        rtn.setShapeStyle(ShapeStyle.ssHalfCircleInTB);
        return rtn;
    }

    public static class DrawableA extends Drawable {
        private int bgColor = Integer.MIN_VALUE;
        private Paint bgPaint;
        private int color = -1;
        private Paint paint;
        private PaintStyle paintStyle = PaintStyle.psStroke;
        private float radius = 0.0f;
        private RectF rect;
        private ShapeStyle shapeStyle = ShapeStyle.ssRect;
        private float strokeWidth = 3.0f;

        private void applyCfg() {
            if (this.paint == null) {
                this.paint = new Paint();
                this.paint.setAntiAlias(true);
            }
            if (this.bgPaint == null) {
                this.bgPaint = new Paint();
                this.bgPaint.setAntiAlias(true);
                this.bgPaint.setStyle(Paint.Style.FILL);
            }
            if (this.paintStyle == PaintStyle.psStroke) {
                this.paint.setStyle(Paint.Style.STROKE);
            } else if (this.paintStyle == PaintStyle.psFill) {
                this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
            }
            if (this.rect == null) {
                this.rect = new RectF();
            }
            Rect bounds = getBounds();
            float offset = this.strokeWidth / 2.0f;
            this.rect.set(((float) bounds.left) + offset, ((float) bounds.top) + offset, ((float) bounds.right) - offset, ((float) bounds.bottom) - offset);
            this.paint.setStrokeWidth(this.strokeWidth);
            this.paint.setColor(this.color);
            this.bgPaint.setColor(this.bgColor);
        }

        public void draw(Canvas canvas) {
            applyCfg();
            if (this.shapeStyle == ShapeStyle.ssHalfCircleInLR) {
                canvas.drawRoundRect(this.rect, this.rect.height() / 2.0f, this.rect.height() / 2.0f, this.bgPaint);
                canvas.drawRoundRect(this.rect, this.rect.height() / 2.0f, this.rect.height() / 2.0f, this.paint);
            } else if (this.shapeStyle == ShapeStyle.ssHalfCircleInTB) {
                canvas.drawRoundRect(this.rect, this.rect.width() / 2.0f, this.rect.width() / 2.0f, this.bgPaint);
                canvas.drawRoundRect(this.rect, this.rect.width() / 2.0f, this.rect.width() / 2.0f, this.paint);
            } else if (this.shapeStyle == ShapeStyle.ssRoundRect) {
                canvas.drawRoundRect(this.rect, this.radius, this.radius, this.bgPaint);
                canvas.drawRoundRect(this.rect, this.radius, this.radius, this.paint);
            } else if (this.shapeStyle == ShapeStyle.ssRect) {
                canvas.drawRect(this.rect, this.bgPaint);
                canvas.drawRect(this.rect, this.paint);
            }
        }

        public void setAlpha(int alpha) {
            applyCfg();
            this.paint.setAlpha(alpha);
        }

        public void setColorFilter(ColorFilter colorFilter) {
            applyCfg();
            this.paint.setColorFilter(colorFilter);
        }

        public int getOpacity() {
            return -1;
        }

        public PaintStyle getPaintStyle() {
            return this.paintStyle;
        }

        public void setPaintStyle(PaintStyle paintStyle2) {
            this.paintStyle = paintStyle2;
        }

        public float getRadius() {
            return this.radius;
        }

        public void setRadius(float radius2) {
            this.radius = radius2;
        }

        public float getStrokeWidth() {
            return this.strokeWidth;
        }

        public void setStrokeWidth(float strokeWidth2) {
            this.strokeWidth = strokeWidth2;
        }

        public int getColor() {
            return this.color;
        }

        public void setColor(int color2) {
            this.color = color2;
        }

        public int getBgColor() {
            return this.bgColor;
        }

        public void setBgColor(int bgColor2) {
            this.bgColor = bgColor2;
        }

        public ShapeStyle getShapeStyle() {
            return this.shapeStyle;
        }

        public void setShapeStyle(ShapeStyle shapeStyle2) {
            this.shapeStyle = shapeStyle2;
        }
    }
}
