package com.taobao.taobaoavsdk.widget.media;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import com.taobao.taobaoavsdk.widget.media.IRenderView;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class TextureRenderView extends TextureView implements IRenderView, TextureView.SurfaceTextureListener {
    private static String TAG = "TextureRenderView";
    private IRenderView.IRenderCallback mCallback;
    private int mHeight;
    private boolean mIsFormatChanged;
    private MeasureHelper mMeasureHelper;
    private InternalSurfaceHolder mSurfaceHolder;
    private SurfaceTexture mSurfaceTexture;
    private int mWidth;

    public TextureRenderView(Context context) {
        super(context);
        initView(context);
    }

    public TextureRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TextureRenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mMeasureHelper = new MeasureHelper();
        setSurfaceTextureListener(this);
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

    private static final class InternalSurfaceHolder implements IRenderView.ISurfaceHolder {
        /* access modifiers changed from: private */
        public Surface mSurface;
        private SurfaceTexture mSurfaceTexture;
        private TextureRenderView mTextureView;

        public InternalSurfaceHolder(@NonNull TextureRenderView textureView, @Nullable SurfaceTexture surfaceTexture) {
            this.mTextureView = textureView;
            this.mSurfaceTexture = surfaceTexture;
        }

        public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
            this.mSurfaceTexture = surfaceTexture;
        }

        @TargetApi(16)
        public void bindToMediaPlayer(IMediaPlayer mp) {
            if (mp != null) {
                if (this.mSurfaceTexture == null) {
                    mp.setSurface((Surface) null);
                    return;
                }
                if (this.mSurface == null || Build.VERSION.SDK_INT < TaoLiveVideoView.SDK_INT_FOR_OPTIMIZE) {
                    this.mSurface = new Surface(this.mSurfaceTexture);
                }
                mp.setSurface(this.mSurface);
            }
        }

        @NonNull
        public IRenderView getRenderView() {
            return this.mTextureView;
        }

        @Nullable
        public Surface getSurface() {
            return this.mSurface;
        }
    }

    public void addRenderCallback(@NonNull IRenderView.IRenderCallback callback) {
        this.mCallback = callback;
        if (this.mSurfaceHolder == null && this.mSurfaceTexture != null) {
            this.mSurfaceHolder = new InternalSurfaceHolder(this, this.mSurfaceTexture);
            callback.onSurfaceCreated(this.mSurfaceHolder, this.mWidth, this.mHeight);
        }
        if (this.mIsFormatChanged) {
            if (this.mSurfaceHolder == null) {
                this.mSurfaceHolder = new InternalSurfaceHolder(this, this.mSurfaceTexture);
            }
            callback.onSurfaceChanged(this.mSurfaceHolder, 0, this.mWidth, this.mHeight);
        }
    }

    public void removeRenderCallback(@NonNull IRenderView.IRenderCallback callback) {
        this.mCallback = null;
    }

    public View getView() {
        return this;
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (Build.VERSION.SDK_INT >= TaoLiveVideoView.SDK_INT_FOR_OPTIMIZE) {
            if (this.mSurfaceTexture != null) {
                setSurfaceTexture(this.mSurfaceTexture);
            }
            if (this.mSurfaceTexture == null) {
                this.mSurfaceTexture = surface;
            }
        } else {
            this.mSurfaceTexture = surface;
        }
        this.mIsFormatChanged = false;
        this.mWidth = 0;
        this.mHeight = 0;
        if (this.mSurfaceHolder == null) {
            this.mSurfaceHolder = new InternalSurfaceHolder(this, this.mSurfaceTexture);
        } else {
            this.mSurfaceHolder.setSurfaceTexture(this.mSurfaceTexture);
        }
        if (this.mCallback != null) {
            this.mCallback.onSurfaceCreated(this.mSurfaceHolder, 0, 0);
        }
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        this.mIsFormatChanged = true;
        this.mWidth = width;
        this.mHeight = height;
        if (this.mSurfaceHolder == null) {
            this.mSurfaceHolder = new InternalSurfaceHolder(this, surface);
        }
        if (this.mCallback != null) {
            this.mCallback.onSurfaceChanged(this.mSurfaceHolder, 0, width, height);
        }
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        this.mIsFormatChanged = false;
        this.mWidth = 0;
        this.mHeight = 0;
        if (this.mSurfaceHolder == null) {
            this.mSurfaceHolder = new InternalSurfaceHolder(this, surface);
        }
        if (this.mCallback != null) {
            this.mCallback.onSurfaceDestroyed(this.mSurfaceHolder);
        }
        if (Build.VERSION.SDK_INT < TaoLiveVideoView.SDK_INT_FOR_OPTIMIZE) {
            return true;
        }
        return false;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    public void releaseSurface() {
        if (Build.VERSION.SDK_INT >= TaoLiveVideoView.SDK_INT_FOR_OPTIMIZE && this.mSurfaceHolder != null && this.mSurfaceHolder.mSurface != null) {
            this.mSurfaceHolder.mSurface.release();
            Surface unused = this.mSurfaceHolder.mSurface = null;
        }
    }
}
