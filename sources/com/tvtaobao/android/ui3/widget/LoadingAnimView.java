package com.tvtaobao.android.ui3.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.tvtaobao.android.ui3.R;
import com.tvtaobao.android.ui3.UI3Logger;

public class LoadingAnimView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final String TAG = LoadingAnimView.class.getSimpleName();
    private int currentPosition = 0;
    private int[] customDrawableIds = new int[0];
    private int drawInterval = this.drawableIds.length;
    private int[] drawableIds = {R.drawable.ui3wares_loading_00, R.drawable.ui3wares_loading_01, R.drawable.ui3wares_loading_02, R.drawable.ui3wares_loading_03, R.drawable.ui3wares_loading_04, R.drawable.ui3wares_loading_05, R.drawable.ui3wares_loading_06, R.drawable.ui3wares_loading_07, R.drawable.ui3wares_loading_08, R.drawable.ui3wares_loading_09, R.drawable.ui3wares_loading_10, R.drawable.ui3wares_loading_11};
    private HandlerThread handlerThread = new HandlerThread(TAG);
    private boolean isDestroy = false;
    private boolean isSurfaceCreated = false;
    private OnAnimationListener mListener;
    private Paint mPaint;
    private SurfaceHolder mSurfaceHolder;
    private Handler mWorkHandler;

    public interface OnAnimationListener {
        void onAnimationFinished();

        void onAnimationStart();
    }

    public LoadingAnimView(Context context) {
        super(context);
        init();
    }

    public LoadingAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mSurfaceHolder = getHolder();
        this.mSurfaceHolder.addCallback(this);
        setZOrderOnTop(true);
        this.mSurfaceHolder.setFormat(-3);
        this.handlerThread.start();
    }

    public void setDrawInterval(int drawInterval2) {
        this.drawInterval = drawInterval2;
    }

    public void setDrawableIds(int[] drawableIds2) {
        this.customDrawableIds = drawableIds2;
    }

    public void startAnimation() {
        UI3Logger.i(TAG, "startAnimation");
        if (this.customDrawableIds.length > 0) {
            this.drawableIds = this.customDrawableIds;
        }
        this.currentPosition = 0;
        this.isDestroy = false;
        this.mWorkHandler = new Handler(this.handlerThread.getLooper());
        setVisibility(0);
        setLayerType(2, (Paint) null);
        this.mWorkHandler.post(this);
    }

    public void stopAnimation() {
        UI3Logger.i(TAG, "stopAnimation");
        post(new Runnable() {
            public void run() {
                LoadingAnimView.this.setVisibility(4);
                LoadingAnimView.this.setLayerType(0, (Paint) null);
                LoadingAnimView.this.notifyFinished();
            }
        });
        if (this.mWorkHandler != null) {
            this.mWorkHandler.removeCallbacks(this);
        }
        this.currentPosition = 0;
        this.isDestroy = true;
    }

    public void onDestroy() {
        this.isDestroy = true;
        if (this.mWorkHandler != null) {
            this.mWorkHandler.removeCallbacks(this);
        }
    }

    public void setListener(OnAnimationListener listener) {
        this.mListener = listener;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        this.isSurfaceCreated = true;
        UI3Logger.d(TAG, "surfaceCreated");
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        UI3Logger.i(TAG, "surfaceChanged");
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        UI3Logger.d(TAG, "surfaceDestroyed");
        this.isDestroy = true;
        stopAnimation();
    }

    public void run() {
        notifyStart();
        while (!this.isDestroy) {
            try {
                long startTime = System.currentTimeMillis();
                draw();
                long sleepTime = (((long) (1000 / this.drawInterval)) - System.currentTimeMillis()) + startTime;
                if (sleepTime < 0) {
                    sleepTime = 0;
                }
                Thread.sleep(sleepTime);
                this.currentPosition++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stopAnimation();
    }

    public static Bitmap getBitmapByResource(Context context, int id) {
        return ((BitmapDrawable) context.getResources().getDrawable(id)).getBitmap();
    }

    public void drawCanvas(Canvas canvas) {
    }

    private void draw() {
        if (this.isSurfaceCreated) {
            try {
                Canvas canvas = this.mSurfaceHolder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                    drawCanvas(canvas);
                    Bitmap bitmap = getBitmapByResource(getContext(), this.drawableIds[this.currentPosition % this.drawableIds.length]);
                    if (bitmap != null) {
                        Matrix matrix = new Matrix();
                        matrix.setScale(((float) getWidth()) / ((float) bitmap.getWidth()), ((float) getHeight()) / ((float) bitmap.getHeight()));
                        canvas.drawBitmap(bitmap, matrix, this.mPaint);
                    }
                }
                this.mSurfaceHolder.unlockCanvasAndPost(canvas);
            } catch (Exception e) {
                UI3Logger.e(TAG, e.toString());
            }
        }
    }

    private void notifyStart() {
        if (this.mListener != null) {
            this.mListener.onAnimationStart();
        }
    }

    /* access modifiers changed from: private */
    public void notifyFinished() {
        if (this.mListener != null) {
            this.mListener.onAnimationFinished();
        }
    }
}
