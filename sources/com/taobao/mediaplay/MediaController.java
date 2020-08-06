package com.taobao.mediaplay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.taobao.mediaplay.common.IRootViewClickListener;
import com.taobao.mediaplay.player.IMediaPlayLifecycleListener;
import com.taobao.mediaplay.player.IMediaSurfaceTextureListener;
import com.taobao.mediaplay.player.TextureVideoView;
import com.taobao.mediaplay.playercontrol.IMediaPlayControlListener;
import com.taobao.mediaplay.playercontrol.MediaPlayControlViewController;
import com.taobao.taobaoavsdk.R;
import com.taobao.taobaoavsdk.widget.media.TaoLiveVideoView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MediaController implements IMediaUrlPickCallBack, IMediaPlayLifecycleListener, IMediaLifecycleListener, IMediaPlayControlListener {
    /* access modifiers changed from: private */
    public MediaContext mMediaContext;
    private ArrayList<IMediaLifecycleListener> mMediaLifecycleListeners = new ArrayList<>();
    /* access modifiers changed from: private */
    public MediaLifecycleType mMediaLifecycleType;
    /* access modifiers changed from: private */
    public MediaPlayControlViewController mMediaPlayControlViewController;
    private MediaPlayViewController mMediaPlayViewController;
    private BroadcastReceiver mNetReceiver;
    private MediaPlayControlManager mPlayControlManager;
    /* access modifiers changed from: private */
    public boolean mRenderStarted;
    private FrameLayout mRootView;
    /* access modifiers changed from: private */
    public IRootViewClickListener mRootViewClickListener;

    public MediaController(MediaContext mediaContext) {
        this.mMediaContext = mediaContext;
        this.mRootView = new FrameLayout(mediaContext.getContext());
        this.mMediaPlayViewController = new MediaPlayViewController(mediaContext);
        this.mMediaContext.setVideo(this.mMediaPlayViewController);
        registerSurfaceTextureListener();
        this.mRootView.addView(this.mMediaPlayViewController.getView(), 0, new FrameLayout.LayoutParams(-2, -2, 17));
        this.mMediaPlayViewController.registerIMediaLifecycleListener(this);
        this.mPlayControlManager = new MediaPlayControlManager(this.mMediaContext.mMediaPlayContext);
        if (!TextUtils.isEmpty(this.mMediaContext.mMediaPlayContext.getVideoUrl()) || this.mMediaContext.mMediaPlayContext.mMediaLiveInfo != null) {
            this.mPlayControlManager.pickVideoUrl(this);
        }
        registerLifecycle(this);
        if (this.mMediaContext.isNeedPlayControlView() || !this.mMediaContext.isHideControllder()) {
            registerMediaPlayControlVC(false);
        }
        if (!this.mMediaContext.mMediaPlayContext.mTBLive) {
            this.mRootView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if ((MediaController.this.mRootViewClickListener != null && MediaController.this.mRootViewClickListener.hook()) || MediaController.this.mMediaPlayControlViewController == null) {
                        return;
                    }
                    if (!MediaController.this.mMediaPlayControlViewController.showing()) {
                        MediaController.this.mMediaPlayControlViewController.showControllerInner();
                    } else if (MediaController.this.mMediaPlayControlViewController != null) {
                        MediaController.this.mMediaPlayControlViewController.hideControllerInner();
                    }
                }
            });
        }
    }

    public View getView() {
        return this.mRootView;
    }

    private void registerSurfaceTextureListener() {
        ((TextureVideoView) this.mMediaPlayViewController.getBaseVideoView()).setSurfaceTextureListener(new ProcessSurfaceTextureEvent());
    }

    /* access modifiers changed from: package-private */
    public void registerLifecycle(IMediaLifecycleListener lifecycleListener) {
        if (!this.mMediaLifecycleListeners.contains(lifecycleListener)) {
            this.mMediaLifecycleListeners.add(lifecycleListener);
        }
    }

    public void onLifecycleChanged(MediaLifecycleType lifecycleType) {
        if (lifecycleType == MediaLifecycleType.PLAY) {
            registerNetworkReceiver();
        }
    }

    private void unregisterNetworkReceiver() {
        try {
            if (this.mNetReceiver != null) {
                this.mMediaContext.getContext().unregisterReceiver(this.mNetReceiver);
                this.mNetReceiver = null;
            }
        } catch (Exception e) {
        }
    }

    private void initNetReceiver() {
        this.mNetReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                    NetworkInfo netInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                    int tmpType = netInfo != null ? netInfo.getType() : -1;
                    if (netInfo != null && netInfo.isAvailable() && tmpType == 0 && MediaController.this.mMediaContext.mbShowNoWifiToast) {
                        Toast.makeText(MediaController.this.mMediaContext.getContext(), MediaController.this.mMediaContext.getContext().getResources().getString(R.string.avsdk_mobile_network_hint), 0).show();
                    }
                }
            }
        };
    }

    private void registerNetworkReceiver() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        if (this.mNetReceiver == null) {
            initNetReceiver();
        } else {
            try {
                this.mMediaContext.getContext().unregisterReceiver(this.mNetReceiver);
            } catch (Exception e) {
            }
        }
        try {
            this.mMediaContext.getContext().registerReceiver(this.mNetReceiver, mFilter);
        } catch (Exception e2) {
        }
    }

    public void registerMediaPlayControlVC(boolean force) {
        if (this.mMediaPlayControlViewController == null) {
            this.mMediaPlayControlViewController = new MediaPlayControlViewController(this.mMediaContext, force);
            this.mRootView.addView(this.mMediaPlayControlViewController.getView(), new FrameLayout.LayoutParams(-1, -1, 17));
            registerLifecycle(this.mMediaPlayControlViewController);
            this.mMediaPlayControlViewController.setIMediaPlayerControlListener(this);
        }
    }

    /* access modifiers changed from: package-private */
    public void toggleScreen() {
        this.mMediaPlayViewController.toggleScreen();
    }

    public void screenButtonClick() {
        toggleScreen();
    }

    public void seekTo(int position) {
        if (this.mMediaPlayViewController.getBaseVideoView() == null || this.mMediaPlayViewController.getBaseVideoView().getVideoState() == 6 || this.mMediaPlayViewController.getBaseVideoView().getVideoState() == 3 || this.mMediaPlayViewController.getBaseVideoView().getVideoState() == 0 || this.mMediaPlayViewController.getBaseVideoView().getVideoState() == 8) {
            this.mMediaContext.mMediaPlayContext.mSeekWhenPrepared = position;
            return;
        }
        this.mMediaPlayViewController.getBaseVideoView().seekTo(position);
        this.mMediaContext.mMediaPlayContext.mSeekWhenPrepared = 0;
    }

    public boolean onPlayRateChanged(float playRate) {
        if (this.mMediaPlayViewController.getBaseVideoView() == null || this.mMediaPlayViewController.getBaseVideoView().getVideoState() == 6 || this.mMediaPlayViewController.getBaseVideoView().getVideoState() == 3 || this.mMediaPlayViewController.getBaseVideoView().getVideoState() == 0) {
            return false;
        }
        this.mMediaPlayViewController.getBaseVideoView().setPlayRate(playRate);
        return true;
    }

    public void setRootViewClickListener(IRootViewClickListener rootViewClickListener) {
        this.mRootViewClickListener = rootViewClickListener;
    }

    public void updateLiveMediaInfoData() {
        if (this.mMediaContext.mMediaPlayContext.mTBLive && this.mMediaContext.mMediaPlayContext.mMediaLiveInfo != null) {
            this.mPlayControlManager.pickVideoUrl(this);
        }
    }

    public void updateLiveMediaUrl() {
        if (this.mMediaContext.mMediaPlayContext.mTBLive) {
            this.mPlayControlManager.pickVideoUrl(this);
        }
    }

    public void close() {
        if (this.mMediaPlayViewController != null) {
            this.mMediaPlayViewController.closeVideo();
        }
    }

    public void addFullScreenCustomView(View view) {
        if (this.mMediaPlayControlViewController != null) {
            this.mMediaPlayControlViewController.addFullScreenCustomView(view);
        }
    }

    public void removeFullScreenCustomView() {
        if (this.mMediaPlayControlViewController != null) {
            this.mMediaPlayControlViewController.removeFullScreenCustomView();
        }
    }

    public void showController() {
        if (this.mMediaPlayControlViewController != null) {
            this.mMediaPlayControlViewController.showControllerView();
        } else {
            registerMediaPlayControlVC(true);
        }
    }

    public void hideController() {
        if (this.mMediaPlayControlViewController != null) {
            this.mMediaPlayControlViewController.hideControllerView();
        }
    }

    public void changeQuality(int qualityIndex) {
        if (this.mMediaContext.mMediaPlayContext.mTBLive && this.mMediaContext.mMediaPlayContext.mMediaLiveInfo != null) {
            this.mPlayControlManager.changeQuality(qualityIndex, this);
        }
    }

    public void pause() {
        if (this.mMediaPlayViewController != null) {
            this.mMediaPlayViewController.pauseVideo();
        }
    }

    public void registerOnVideoClickListener(IMediaPlayer.OnVideoClickListener clickListener) {
        if (this.mMediaPlayViewController != null) {
            this.mMediaPlayViewController.registerOnVideoClickListener(clickListener);
        }
    }

    public void unregisterOnVideoClickListener(IMediaPlayer.OnVideoClickListener clickListener) {
        if (this.mMediaPlayViewController != null) {
            this.mMediaPlayViewController.unregisterOnVideoClickListener(clickListener);
        }
    }

    public boolean isPlaying() {
        if (this.mMediaPlayViewController != null) {
            return this.mMediaPlayViewController.isPlaying();
        }
        return false;
    }

    public boolean isInPlaybackState() {
        if (this.mMediaPlayViewController != null) {
            return this.mMediaPlayViewController.isInPlaybackState();
        }
        return false;
    }

    public int getVideoHeight() {
        if (this.mMediaPlayViewController == null || this.mMediaPlayViewController.getBaseVideoView() == null) {
            return 0;
        }
        return this.mMediaPlayViewController.getBaseVideoView().getVideoHeight();
    }

    public int getVideoWidth() {
        if (this.mMediaPlayViewController == null || this.mMediaPlayViewController.getBaseVideoView() == null) {
            return 0;
        }
        return this.mMediaPlayViewController.getBaseVideoView().getVideoWidth();
    }

    public void setMuted(boolean muted) {
        if (this.mMediaPlayViewController != null) {
            this.mMediaPlayViewController.mute(muted);
        }
    }

    public void destroy() {
        if (this.mMediaPlayViewController != null) {
            this.mMediaPlayViewController.destroy();
        }
    }

    public void setMediaSourceType(String mediaSourceType) {
        if (this.mMediaPlayViewController != null) {
            this.mMediaPlayViewController.setMediaSourceType(mediaSourceType);
        }
    }

    public void setMediaId(String mediaId) {
        if (this.mMediaPlayViewController != null) {
            this.mMediaPlayViewController.setMediaId(mediaId);
        }
    }

    public void setAccountId(String accountId) {
        if (this.mMediaPlayViewController != null) {
            this.mMediaPlayViewController.setAccountId(accountId);
        }
    }

    public void setSurfaceListener(TaoLiveVideoView.SurfaceListener listener) {
        if (this.mMediaPlayViewController != null) {
            this.mMediaPlayViewController.setSurfaceListener(listener);
        }
    }

    private class ProcessSurfaceTextureEvent implements IMediaSurfaceTextureListener {
        private ProcessSurfaceTextureEvent() {
        }

        public void updated(TextureVideoView videoView) {
            if (MediaController.this.mMediaLifecycleType != MediaLifecycleType.PLAY) {
                if (videoView.getVideoState() == 1 && (MediaController.this.mRenderStarted || Build.VERSION.SDK_INT < 17)) {
                    MediaController.this.setLifecycleType(MediaLifecycleType.PLAY);
                } else if (!TextUtils.isEmpty(MediaController.this.mMediaContext.getVideoToken()) && videoView.getVideoState() == 1) {
                    MediaController.this.setLifecycleType(MediaLifecycleType.PLAY);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final void setLifecycleType(MediaLifecycleType lifecycleType) {
        this.mMediaLifecycleType = lifecycleType;
        notifyLifecycleChanged(lifecycleType);
    }

    private void notifyLifecycleChanged(MediaLifecycleType lifecycleType) {
        Iterator<IMediaLifecycleListener> it = this.mMediaLifecycleListeners.iterator();
        while (it.hasNext()) {
            it.next().onLifecycleChanged(lifecycleType);
        }
    }

    public void onMediaStart() {
        if (this.mMediaPlayControlViewController != null) {
            this.mMediaPlayControlViewController.setPauseSrc();
        }
    }

    public void onMediaPause(boolean auto) {
        if (this.mMediaPlayControlViewController != null) {
            this.mMediaPlayControlViewController.setPlaySrc();
        }
    }

    public void onMediaPlay() {
        if (this.mMediaPlayControlViewController != null) {
            this.mMediaPlayControlViewController.setPauseSrc();
        }
    }

    public void onMediaSeekTo(int currentPosition) {
    }

    public void onMediaPrepared(IMediaPlayer mp) {
    }

    public void onMediaError(IMediaPlayer mp, int what, int extra) {
    }

    public void onMediaInfo(IMediaPlayer mp, long what, long extra, long ext, Object obj) {
        if (3 == what && (obj instanceof Map)) {
            this.mRenderStarted = true;
        }
    }

    public void onMediaComplete() {
        if (this.mMediaPlayControlViewController != null) {
            this.mMediaPlayControlViewController.setPlaySrc();
        }
    }

    public void onMediaClose() {
        if (this.mMediaPlayControlViewController != null) {
            this.mMediaPlayControlViewController.setPlaySrc();
        }
    }

    public void onMediaScreenChanged(MediaPlayScreenType type) {
    }

    public void onMediaProgressChanged(int currentPosition, int bufferPercent, int total) {
    }

    public void onPick(boolean usePlayManger, String extra) {
        this.mMediaPlayViewController.setVideoSource(this.mMediaContext.mMediaPlayContext.getVideoUrl());
    }

    public void start() {
        this.mMediaPlayViewController.startVideo();
    }

    public void release() {
        this.mMediaLifecycleType = MediaLifecycleType.BEFORE;
        unregisterNetworkReceiver();
        this.mMediaPlayViewController.release();
    }
}
