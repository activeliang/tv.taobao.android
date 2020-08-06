package com.tvtaobao.android.ui3.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import com.tvtaobao.android.ui3.R;

public class SurfaceAnimationView extends LoadingAnimView {
    private static final String TAG = SurfaceAnimationView.class.getSimpleName();
    private Paint mPaint;

    public SurfaceAnimationView(Context context) {
        super(context);
        init();
    }

    public SurfaceAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SurfaceAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.mPaint = new Paint();
    }

    public void drawCanvas(Canvas canvas) {
        super.drawCanvas(canvas);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(Color.parseColor("#b2000000"));
        this.mPaint.setStyle(Paint.Style.FILL);
        RectF mBrounds = new RectF();
        mBrounds.left = (float) getContext().getResources().getDimensionPixelSize(R.dimen.values_dp_10);
        mBrounds.top = (float) getContext().getResources().getDimensionPixelSize(R.dimen.values_dp_10);
        mBrounds.right = (float) getContext().getResources().getDimensionPixelSize(R.dimen.values_dp_110);
        mBrounds.bottom = (float) getContext().getResources().getDimensionPixelSize(R.dimen.values_dp_110);
        int mRadius = getContext().getResources().getDimensionPixelSize(R.dimen.values_dp_8);
        canvas.drawRoundRect(mBrounds, (float) mRadius, (float) mRadius, this.mPaint);
        this.mPaint.setColor(Color.parseColor("#99ffffff"));
        this.mPaint.setTextAlign(Paint.Align.CENTER);
        this.mPaint.setTextSize((float) getContext().getResources().getDimensionPixelSize(R.dimen.values_sp_14));
        canvas.drawText("加载中...", (float) getContext().getResources().getDimensionPixelSize(R.dimen.values_dp_64), (float) getContext().getResources().getDimensionPixelSize(R.dimen.values_dp_98), this.mPaint);
    }
}
