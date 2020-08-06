package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import com.yunos.tvtaobao.businessview.R;
import com.yunos.tvtaobao.tvsdk.widget.FrameLayout;

public class CardView extends FrameLayout {
    private Paint mBackgroundPaint;
    private Bitmap mBitMap;
    private final Paint mBitmapPaint;
    private RectF mImageRectF;
    private int mRadio;
    private Canvas mapCanvas;

    public CardView(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    public CardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mBitmapPaint = new Paint();
        this.mBackgroundPaint = new Paint();
        this.mImageRectF = new RectF();
        this.mRadio = 0;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CardView, defStyle, 0);
        this.mRadio = a.getDimensionPixelSize(R.styleable.CardView_cv_radius, 0);
        if (this.mRadio == 0) {
            this.mRadio = context.getResources().getDimensionPixelOffset(R.dimen.dp_12);
        }
        this.mBackgroundPaint.setColor(a.getColor(R.styleable.CardView_backgroundColor, -1));
        this.mBackgroundPaint.setAntiAlias(true);
        this.mBitmapPaint.setAntiAlias(true);
        this.mBitmapPaint.setColor(-1);
        a.recycle();
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        canvas.save();
        if (this.mapCanvas == null) {
            super.dispatchDraw(canvas);
            canvas.restore();
            return;
        }
        this.mapCanvas.drawRoundRect(this.mImageRectF, (float) this.mRadio, (float) this.mRadio, this.mBackgroundPaint);
        super.dispatchDraw(this.mapCanvas);
        canvas.drawRoundRect(this.mImageRectF, (float) this.mRadio, (float) this.mRadio, this.mBitmapPaint);
        canvas.restore();
    }

    public void setRadio(int radio) {
        this.mRadio = radio;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        this.mImageRectF.left = (float) paddingLeft;
        this.mImageRectF.right = (float) ((right - left) - paddingRight);
        this.mImageRectF.top = (float) paddingTop;
        this.mImageRectF.bottom = (float) (((bottom - top) - paddingTop) - paddingBottom);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int i = w << (h + 16);
        if (this.mBitMap == null || w != this.mBitMap.getWidth() || h != this.mBitMap.getHeight()) {
            this.mBitMap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
            this.mapCanvas = new Canvas(this.mBitMap);
            this.mBitmapPaint.setShader(new BitmapShader(this.mBitMap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        }
    }

    public boolean isInEditMode() {
        return true;
    }
}
