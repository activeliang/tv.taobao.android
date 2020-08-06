package com.taobao.mediaplay.playercontrol;

import android.widget.FrameLayout;
import com.taobao.mediaplay.MediaContext;
import com.taobao.mediaplay.MediaPlayScreenType;
import com.taobao.mediaplay.player.IMediaPlayLifecycleListener;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MediaPlaySmallBarViewController implements IMediaPlayLifecycleListener {
    private FrameLayout mHost;
    MediaContext mMediaContext;

    public MediaPlaySmallBarViewController(MediaContext context, FrameLayout parent) {
        this.mMediaContext = context;
        this.mHost = parent;
    }

    public void onMediaStart() {
    }

    public void onMediaPause(boolean auto) {
    }

    public void onMediaPlay() {
    }

    public void onMediaSeekTo(int currentPosition) {
    }

    public void onMediaPrepared(IMediaPlayer mp) {
    }

    public void onMediaError(IMediaPlayer mp, int what, int extra) {
    }

    public void onMediaInfo(IMediaPlayer mp, long what, long extra, long ext, Object obj) {
    }

    public void onMediaComplete() {
    }

    public void onMediaClose() {
    }

    public void onMediaScreenChanged(MediaPlayScreenType type) {
    }

    public void onMediaProgressChanged(int currentPosition, int bufferPercent, int total) {
    }

    public void destroy() {
    }

    public void showProgressBar(boolean b) {
    }

    public void hideProgressBar(boolean b) {
    }
}
