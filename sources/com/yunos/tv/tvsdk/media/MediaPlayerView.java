package com.yunos.tv.tvsdk.media;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import anetwork.channel.util.RequestConstant;

public class MediaPlayerView extends GLSurfaceView {
    private final String TAG = "MediaPlayerView";
    private MediaPlayerRender mMediaPlayerRender;

    public MediaPlayerView(Context context) {
        super(context);
    }

    public MediaPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onRenderSurfaceCreate() {
    }

    public void setRenderer(GLSurfaceView.Renderer renderer) {
        if (!(renderer instanceof MediaPlayerRender)) {
            throw new IllegalStateException("setRenderer must use MediaPlayerRender.");
        }
        this.mMediaPlayerRender = (MediaPlayerRender) renderer;
        super.setRenderer(renderer);
    }

    public void setRendererRotate(float rotate) {
        if (this.mMediaPlayerRender != null) {
            this.mMediaPlayerRender.setRotate(rotate);
        }
    }

    public void setRenderMode(int renderMode) {
        if (this.mMediaPlayerRender != null) {
            super.setRenderMode(renderMode);
        }
    }

    public int getRenderMode() {
        if (this.mMediaPlayerRender != null) {
            return super.getRenderMode();
        }
        return -1;
    }

    public void requestRender() {
        if (this.mMediaPlayerRender != null) {
            super.requestRender();
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(RequestConstant.ENV_TEST, "MediaPlayerView surfaceCreated");
        if (this.mMediaPlayerRender != null) {
            super.surfaceCreated(holder);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (this.mMediaPlayerRender != null) {
            super.surfaceDestroyed(holder);
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (this.mMediaPlayerRender != null) {
            super.surfaceChanged(holder, format, w, h);
        }
    }

    public void onPause() {
        if (this.mMediaPlayerRender != null) {
            super.onPause();
        }
    }

    public void onResume() {
        if (this.mMediaPlayerRender != null) {
            super.onResume();
        }
    }

    public void queueEvent(Runnable r) {
        if (this.mMediaPlayerRender != null) {
            super.queueEvent(r);
        }
    }
}
