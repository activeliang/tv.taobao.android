package com.taobao.taobaoavsdk.widget.media;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.taobao.taobaoavsdk.widget.media.IRenderView;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class SurfaceRenderView extends SurfaceView implements IRenderView, SurfaceHolder.Callback {
    private static final String TAG = SurfaceRenderView.class.getSimpleName();
    private IRenderView.IRenderCallback mCallback;
    private MeasureHelper mMeasureHelper;

    public SurfaceRenderView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        this.mMeasureHelper = new MeasureHelper();
        getHolder().addCallback(this);
    }

    public void setVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            this.mMeasureHelper.setVideoSize(videoWidth, videoHeight);
            requestLayout();
        }
    }

    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        if (videoSarNum > 0 && videoSarDen > 0) {
            this.mMeasureHelper.setVideoSampleAspectRatio(videoSarNum, videoSarDen);
            requestLayout();
        }
    }

    public void setVideoRotation(int degree) {
        this.mMeasureHelper.setVideoRotation(degree);
        setRotation((float) degree);
    }

    public void setAspectRatio(int aspectRatio) {
        this.mMeasureHelper.setAspectRatio(aspectRatio);
        requestLayout();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(this.mMeasureHelper.getMeasuredWidth(), this.mMeasureHelper.getMeasuredHeight());
    }

    public void addRenderCallback(@NonNull IRenderView.IRenderCallback callback) {
        this.mCallback = callback;
    }

    public void removeRenderCallback(@NonNull IRenderView.IRenderCallback callback) {
        this.mCallback = null;
    }

    public View getView() {
        return this;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        IRenderView.ISurfaceHolder surfaceHolder = new InternalSurfaceHolder(this, holder);
        if (this.mCallback != null) {
            this.mCallback.onSurfaceCreated(surfaceHolder, 0, 0);
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        IRenderView.ISurfaceHolder surfaceHolder = new InternalSurfaceHolder(this, holder);
        if (this.mCallback != null) {
            this.mCallback.onSurfaceChanged(surfaceHolder, format, width, height);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        IRenderView.ISurfaceHolder surfaceHolder = new InternalSurfaceHolder(this, holder);
        if (this.mCallback != null) {
            this.mCallback.onSurfaceDestroyed(surfaceHolder);
        }
    }

    private static final class InternalSurfaceHolder implements IRenderView.ISurfaceHolder {
        private SurfaceHolder mSurfaceHolder;
        private SurfaceRenderView mSurfaceRenderView;

        public InternalSurfaceHolder(SurfaceRenderView surfaceView, SurfaceHolder holder) {
            this.mSurfaceRenderView = surfaceView;
            this.mSurfaceHolder = holder;
        }

        public void bindToMediaPlayer(IMediaPlayer mp) {
            if (mp != null) {
                mp.setDisplay(this.mSurfaceHolder);
            }
        }

        @NonNull
        public IRenderView getRenderView() {
            return this.mSurfaceRenderView;
        }

        @Nullable
        public Surface getSurface() {
            return null;
        }
    }
}
