package com.tvtaobao.voicesdk.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.tvtaobao.voicesdk.R;

@SuppressLint({"AppCompatCustomView"})
public class RoundImageView extends ImageView {
    private boolean leftDown = true;
    private boolean leftUp = true;
    private Paint paint;
    private Paint paint2;
    private boolean rightDown = true;
    private boolean rightUp = true;
    private int round = getContext().getResources().getDimensionPixelSize(R.dimen.dp_6);

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundImageView(Context context) {
        super(context);
        init(context, (AttributeSet) null);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundAngleImageView);
            this.round = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundWidth, this.round);
            this.round = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundHeight, this.round);
        } else {
            float density = context.getResources().getDisplayMetrics().density;
            this.round = (int) (((float) this.round) * density);
            this.round = (int) (((float) this.round) * density);
        }
        this.paint = new Paint();
        this.paint.setColor(-1);
        this.paint.setAntiAlias(true);
        this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        this.paint2 = new Paint();
        this.paint2.setXfermode((Xfermode) null);
    }

    public void setRound(boolean leftUp2, boolean leftDown2, boolean rightUp2, boolean rightDown2) {
        this.leftUp = leftUp2;
        this.leftDown = leftDown2;
        this.rightUp = rightUp2;
        this.rightDown = rightDown2;
    }

    public void setCornerRadius(int radius) {
        this.round = radius;
    }

    public void draw(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);
        super.draw(canvas2);
        if (this.leftUp) {
            drawLiftUp(canvas2);
        }
        if (this.leftDown) {
            drawLiftDown(canvas2);
        }
        if (this.rightUp) {
            drawRightUp(canvas2);
        }
        if (this.rightDown) {
            drawRightDown(canvas2);
        }
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, this.paint2);
        bitmap.recycle();
    }

    private void drawLiftUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0.0f, (float) this.round);
        path.lineTo(0.0f, 0.0f);
        path.lineTo((float) this.round, 0.0f);
        path.arcTo(new RectF(0.0f, 0.0f, (float) (this.round * 2), (float) (this.round * 2)), -90.0f, -90.0f);
        path.close();
        canvas.drawPath(path, this.paint);
    }

    private void drawLiftDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0.0f, (float) (getHeight() - this.round));
        path.lineTo(0.0f, (float) getHeight());
        path.lineTo((float) this.round, (float) getHeight());
        path.arcTo(new RectF(0.0f, (float) (getHeight() - (this.round * 2)), (float) ((this.round * 2) + 0), (float) getHeight()), 90.0f, 90.0f);
        path.close();
        canvas.drawPath(path, this.paint);
    }

    private void drawRightUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo((float) getWidth(), (float) this.round);
        path.lineTo((float) getWidth(), 0.0f);
        path.lineTo((float) (getWidth() - this.round), 0.0f);
        path.arcTo(new RectF((float) (getWidth() - (this.round * 2)), 0.0f, (float) getWidth(), (float) ((this.round * 2) + 0)), -90.0f, 90.0f);
        path.close();
        canvas.drawPath(path, this.paint);
    }

    private void drawRightDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo((float) (getWidth() - this.round), (float) getHeight());
        path.lineTo((float) getWidth(), (float) getHeight());
        path.lineTo((float) getWidth(), (float) (getHeight() - this.round));
        path.arcTo(new RectF((float) (getWidth() - (this.round * 2)), (float) (getHeight() - (this.round * 2)), (float) getWidth(), (float) getHeight()), 0.0f, 90.0f);
        path.close();
        canvas.drawPath(path, this.paint);
    }
}
