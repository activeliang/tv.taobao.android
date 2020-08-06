package com.taobao.mediaplay.player;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import com.taobao.mediaplay.player.IMediaRenderView;
import com.taobao.taobaoavsdk.widget.media.MeasureHelper;
import java.lang.reflect.Field;
import tv.danmaku.ijk.media.player.IMediaPlayer;

class MediaTextureView extends TextureView implements IMediaRenderView, TextureView.SurfaceTextureListener {
    private IMediaRenderView.IRenderCallback mCallback;
    private MeasureHelper mMeasureHelper;
    private InternalSurfaceHolder mSurfaceHolder;
    private String mTag = "DWTextureView";

    public MediaTextureView(Context context) {
        super(context);
        init();
    }

    public MediaTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MediaTextureView(Context context, AttributeSet attrs, int defStyleAttr, MeasureHelper measureHelper) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        this.mMeasureHelper = new MeasureHelper();
        this.mSurfaceHolder = new InternalSurfaceHolder(this);
        setSurfaceTextureListener(this);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mMeasureHelper != null) {
            this.mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(this.mMeasureHelper.getMeasuredWidth(), this.mMeasureHelper.getMeasuredHeight());
        }
    }

    public void setBackgroundDrawable(Drawable background) {
        if (Build.VERSION.SDK_INT < 24 && background != null) {
            super.setBackgroundDrawable(background);
        }
    }

    public void setVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            this.mMeasureHelper.setVideoSize(videoWidth, videoHeight);
        }
    }

    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        if (videoSarNum > 0 && videoSarDen > 0) {
            this.mMeasureHelper.setVideoSampleAspectRatio(videoSarNum, videoSarDen);
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

    private static final class InternalSurfaceHolder implements IMediaRenderView.ISurfaceHolder {
        /* access modifiers changed from: private */
        public Surface mSurface;
        /* access modifiers changed from: private */
        public SurfaceTexture mSurfaceTexture;
        private MediaTextureView mTextureView;

        public InternalSurfaceHolder(@NonNull MediaTextureView textureView) {
            this.mTextureView = textureView;
        }

        @TargetApi(16)
        public void bindToMediaPlayer(IMediaPlayer mp) {
            if (mp != null) {
                mp.setSurface(this.mSurface);
            }
        }

        @NonNull
        public IMediaRenderView getRenderView() {
            return this.mTextureView;
        }

        @Nullable
        public Surface getSurface() {
            return this.mSurface;
        }
    }

    public void addRenderCallback(@NonNull IMediaRenderView.IRenderCallback callback) {
        this.mCallback = callback;
    }

    public void removeRenderCallback(@NonNull IMediaRenderView.IRenderCallback callback) {
        this.mCallback = null;
    }

    public View getView() {
        return this;
    }

    public float getDisplayAspectRatio() {
        return this.mMeasureHelper.getDisplayAspectRatio();
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Surface access$000;
        InternalSurfaceHolder internalSurfaceHolder = this.mSurfaceHolder;
        if (Build.VERSION.SDK_INT < TextureVideoView.SDK_INT_FOR_OPTIMIZE) {
            access$000 = new Surface(surface);
        } else {
            access$000 = this.mSurfaceHolder.mSurface;
        }
        Surface unused = internalSurfaceHolder.mSurface = access$000;
        if (this.mSurfaceHolder.mSurfaceTexture != null && Build.VERSION.SDK_INT >= TextureVideoView.SDK_INT_FOR_OPTIMIZE) {
            try {
                setSurfaceTexture(this.mSurfaceHolder.mSurfaceTexture);
            } catch (Exception e) {
            }
            if (Build.VERSION.SDK_INT == 19 || Build.VERSION.SDK_INT == 21 || Build.VERSION.SDK_INT == 22) {
                try {
                    Field updateLayer = getClass().getSuperclass().getDeclaredField("mUpdateListener");
                    updateLayer.setAccessible(true);
                    this.mSurfaceHolder.mSurfaceTexture.setOnFrameAvailableListener((SurfaceTexture.OnFrameAvailableListener) updateLayer.get(this));
                } catch (Throwable e2) {
                    Log.e("", "setOnFrameAvailableListener error" + e2.getMessage());
                }
            }
        }
        if (this.mSurfaceHolder.mSurface == null) {
            Surface unused2 = this.mSurfaceHolder.mSurface = new Surface(surface);
            SurfaceTexture unused3 = this.mSurfaceHolder.mSurfaceTexture = surface;
        }
        if (this.mCallback != null) {
            this.mCallback.onSurfaceCreated(this.mSurfaceHolder, width, height);
        }
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        if (this.mCallback != null) {
            this.mCallback.onSurfaceChanged(this.mSurfaceHolder, 0, width, height);
        }
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (this.mCallback != null) {
            this.mCallback.onSurfaceDestroyed(this.mSurfaceHolder);
        }
        if (Build.VERSION.SDK_INT < TextureVideoView.SDK_INT_FOR_OPTIMIZE) {
            if (this.mSurfaceHolder.mSurface != null) {
                this.mSurfaceHolder.mSurface.release();
            }
            Surface unused = this.mSurfaceHolder.mSurface = null;
        }
        return Build.VERSION.SDK_INT < TextureVideoView.SDK_INT_FOR_OPTIMIZE;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        if (this.mCallback != null) {
            this.mCallback.onSurfaceUpdate(this.mSurfaceHolder);
        }
    }

    public void releaseSurface() {
        if (Build.VERSION.SDK_INT >= TextureVideoView.SDK_INT_FOR_OPTIMIZE) {
            try {
                if (this.mSurfaceHolder != null && this.mSurfaceHolder.mSurface != null) {
                    this.mSurfaceHolder.mSurface.release();
                    Surface unused = this.mSurfaceHolder.mSurface = null;
                }
            } catch (Throwable th) {
            }
        }
    }
}
