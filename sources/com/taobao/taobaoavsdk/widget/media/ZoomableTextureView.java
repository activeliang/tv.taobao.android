package com.taobao.taobaoavsdk.widget.media;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.TextureView;
import android.view.View;

public class ZoomableTextureView extends TextureView implements View.OnTouchListener {
    /* access modifiers changed from: private */
    public float mFocusX = 0.0f;
    /* access modifiers changed from: private */
    public float mFocusY = 0.0f;
    private GestureDetector mGestureDetector;
    private Matrix mMatrix;
    private ScaleGestureDetector mScaleDetector;
    /* access modifiers changed from: private */
    public float mScaleFactor = 1.0f;
    /* access modifiers changed from: private */
    public float mScrollX = 0.0f;
    /* access modifiers changed from: private */
    public float mScrollY = 0.0f;

    public ZoomableTextureView(Context context) {
        super(context);
        init(context);
    }

    public ZoomableTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomableTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public ZoomableTextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.mMatrix = new Matrix();
        this.mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        this.mGestureDetector = new GestureDetector(context, new GestureListener());
        setOnTouchListener(this);
    }

    public void setDisplayMetrics(int width, int height) {
        this.mFocusX = (float) (width / 2);
        this.mFocusY = (float) (height / 2);
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        this.mScaleDetector.onTouchEvent(motionEvent);
        this.mGestureDetector.onTouchEvent(motionEvent);
        float maxScrollX = this.mFocusX * (this.mScaleFactor - 1.0f);
        float maxScrollY = this.mFocusY * (this.mScaleFactor - 1.0f);
        float minScrollX = (this.mFocusX - ((float) getWidth())) * (this.mScaleFactor - 1.0f);
        float minScrollY = (this.mFocusY - ((float) getHeight())) * (this.mScaleFactor - 1.0f);
        this.mMatrix.reset();
        this.mMatrix.postScale(this.mScaleFactor, this.mScaleFactor, this.mFocusX, this.mFocusY);
        if (this.mScrollX > maxScrollX) {
            this.mScrollX = maxScrollX;
        } else if (this.mScrollX < minScrollX) {
            this.mScrollX = minScrollX;
        }
        if (this.mScrollY > maxScrollY) {
            this.mScrollY = maxScrollY;
        } else if (this.mScrollY < minScrollY) {
            this.mScrollY = minScrollY;
        }
        this.mMatrix.postTranslate(this.mScrollX, this.mScrollY);
        setTransform(this.mMatrix);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        public boolean onScale(ScaleGestureDetector detector) {
            float unused = ZoomableTextureView.this.mScrollX = 0.0f;
            float unused2 = ZoomableTextureView.this.mScrollY = 0.0f;
            float unused3 = ZoomableTextureView.this.mFocusX = detector.getFocusX();
            float unused4 = ZoomableTextureView.this.mFocusY = detector.getFocusY();
            float unused5 = ZoomableTextureView.this.mScaleFactor = ZoomableTextureView.this.mScaleFactor * detector.getScaleFactor();
            float unused6 = ZoomableTextureView.this.mScaleFactor = Math.max(1.0f, Math.min(ZoomableTextureView.this.mScaleFactor, 3.0f));
            return true;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private GestureListener() {
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float unused = ZoomableTextureView.this.mScrollX = ZoomableTextureView.this.mScrollX - distanceX;
            float unused2 = ZoomableTextureView.this.mScrollY = ZoomableTextureView.this.mScrollY - distanceY;
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        public boolean onDoubleTap(MotionEvent e) {
            float unused = ZoomableTextureView.this.mScrollX = 0.0f;
            float unused2 = ZoomableTextureView.this.mScrollY = 0.0f;
            float unused3 = ZoomableTextureView.this.mFocusX = e.getX();
            float unused4 = ZoomableTextureView.this.mFocusY = e.getY();
            if (ZoomableTextureView.this.mScaleFactor > 1.0f) {
                float unused5 = ZoomableTextureView.this.mScaleFactor = 1.0f;
            } else {
                float unused6 = ZoomableTextureView.this.mScaleFactor = 3.0f;
            }
            return super.onDoubleTap(e);
        }
    }
}
