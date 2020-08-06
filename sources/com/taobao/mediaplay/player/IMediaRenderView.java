package com.taobao.mediaplay.player;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.View;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public interface IMediaRenderView {
    public static final int AR_16_9_FIT_PARENT = 4;
    public static final int AR_4_3_FIT_PARENT = 5;
    public static final int AR_ASPECT_FILL_PARENT = 1;
    public static final int AR_ASPECT_FIT_PARENT = 0;
    public static final int AR_ASPECT_WRAP_CONTENT = 2;
    public static final int AR_MATCH_PARENT = 3;

    public interface IRenderCallback {
        void onSurfaceChanged(@NonNull ISurfaceHolder iSurfaceHolder, int i, int i2, int i3);

        void onSurfaceCreated(@NonNull ISurfaceHolder iSurfaceHolder, int i, int i2);

        void onSurfaceDestroyed(@NonNull ISurfaceHolder iSurfaceHolder);

        void onSurfaceUpdate(ISurfaceHolder iSurfaceHolder);
    }

    public interface ISurfaceHolder {
        void bindToMediaPlayer(IMediaPlayer iMediaPlayer);

        @NonNull
        IMediaRenderView getRenderView();

        @Nullable
        Surface getSurface();
    }

    void addRenderCallback(@NonNull IRenderCallback iRenderCallback);

    float getDisplayAspectRatio();

    View getView();

    boolean isAvailable();

    void removeRenderCallback(@NonNull IRenderCallback iRenderCallback);

    void requestLayout();

    void setAspectRatio(int i);

    void setVideoRotation(int i);

    void setVideoSampleAspectRatio(int i, int i2);

    void setVideoSize(int i, int i2);
}
