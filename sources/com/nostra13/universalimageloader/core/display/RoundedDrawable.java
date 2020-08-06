package com.nostra13.universalimageloader.core.display;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;

public class RoundedDrawable extends Drawable {
    private static final String TAG = "RoundedDrawable";
    private final int mBitmapHeight;
    private final Paint mBitmapPaint;
    private final RectF mBitmapRect = new RectF();
    private final BitmapShader mBitmapShader;
    private final int mBitmapWidth;
    private float mCornerRadius = 0.0f;
    private boolean mOval = false;

    public RoundedDrawable(Bitmap bitmap) {
        this.mBitmapWidth = bitmap.getWidth();
        this.mBitmapHeight = bitmap.getHeight();
        this.mBitmapRect.set(0.0f, 0.0f, (float) this.mBitmapWidth, (float) this.mBitmapHeight);
        this.mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        this.mBitmapPaint = new Paint();
        this.mBitmapPaint.setStyle(Paint.Style.FILL);
        this.mBitmapPaint.setAntiAlias(true);
        this.mBitmapPaint.setShader(this.mBitmapShader);
    }

    public static RoundedDrawable fromBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        RoundedDrawable b = new RoundedDrawable(bitmap);
        b.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        return b;
    }

    public static Drawable fromDrawable(Drawable drawable) {
        if (drawable == null || (drawable instanceof RoundedDrawable)) {
            return drawable;
        }
        if (drawable instanceof LayerDrawable) {
            LayerDrawable ld = (LayerDrawable) drawable;
            int num = ld.getNumberOfLayers();
            for (int i = 0; i < num; i++) {
                ld.setDrawableByLayerId(ld.getId(i), fromDrawable(ld.getDrawable(i)));
            }
            return ld;
        }
        Bitmap bm = drawableToBitmap(drawable);
        if (bm != null) {
            RoundedDrawable rd = new RoundedDrawable(bm);
            rd.setBounds(drawable.getBounds());
            return rd;
        }
        Log.w(TAG, "failed to create bitmap from drawable!");
        return drawable;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            Bitmap bitmap = Bitmap.createBitmap(Math.max(drawable.getIntrinsicWidth(), 1), Math.max(drawable.getIntrinsicHeight(), 1), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void draw(Canvas canvas) {
        Log.d(TAG, "draw");
        if (this.mOval) {
            canvas.drawOval(this.mBitmapRect, this.mBitmapPaint);
        } else {
            canvas.drawRoundRect(this.mBitmapRect, this.mCornerRadius, this.mCornerRadius, this.mBitmapPaint);
        }
    }

    public void setAlpha(int alpha) {
        this.mBitmapPaint.setAlpha(alpha);
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter cf) {
        this.mBitmapPaint.setColorFilter(cf);
        invalidateSelf();
    }

    public int getOpacity() {
        return -3;
    }

    public void setDither(boolean dither) {
        this.mBitmapPaint.setDither(dither);
        invalidateSelf();
    }

    public void setFilterBitmap(boolean filter) {
        this.mBitmapPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    public int getIntrinsicWidth() {
        return this.mBitmapWidth;
    }

    public int getIntrinsicHeight() {
        return this.mBitmapHeight;
    }

    public float getCornerRadius() {
        return this.mCornerRadius;
    }

    public RoundedDrawable setCornerRadius(float radius) {
        this.mCornerRadius = radius;
        return this;
    }

    public boolean isOval() {
        return this.mOval;
    }

    public RoundedDrawable setOval(boolean oval) {
        this.mOval = oval;
        return this;
    }
}
