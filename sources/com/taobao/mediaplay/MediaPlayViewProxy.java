package com.taobao.mediaplay;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import com.taobao.adapter.ConfigAdapter;
import com.taobao.adapter.LogAdapter;
import com.taobao.media.MediaAdapteManager;
import com.taobao.media.MediaConstant;
import com.taobao.media.MediaDeviceUtils;
import com.taobao.mediaplay.model.MediaLiveInfo;
import com.taobao.mediaplay.player.IMediaPlayLifecycleListener;
import com.taobao.mediaplay.player.MediaAspectRatio;
import com.taobao.taobaoavsdk.util.AndroidUtils;
import com.taobao.taobaoavsdk.widget.media.TaoLiveVideoView;
import com.taobao.taobaoavsdk.widget.media.TaoLiveVideoViewConfig;
import java.util.LinkedList;
import java.util.List;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MediaPlayViewProxy implements IMediaPlayLifecycleListener {
    private TaoLiveVideoViewConfig mConfig;
    private MediaPlayCenter mMediaPlayCenter;
    private final boolean mOldVideoView;
    private List<IMediaPlayer.OnCompletionListener> mOnCompletionListeners;
    private List<IMediaPlayer.OnErrorListener> mOnErrorListeners;
    private List<IMediaPlayer.OnInfoListener> mOnInfoListeners;
    private List<TaoLiveVideoView.OnPauseListener> mOnPauseListeners;
    private List<IMediaPlayer.OnPreparedListener> mOnPreparedListeners;
    private List<TaoLiveVideoView.OnStartListener> mOnStartListeners;
    public String mPlayUrl;
    private TaoLiveVideoView mVideoView;

    public MediaPlayViewProxy(Context context, boolean old, String businessId) {
        this.mOldVideoView = old;
        if (old) {
            this.mVideoView = new TaoLiveVideoView(context);
            this.mConfig = new TaoLiveVideoViewConfig(businessId);
            this.mVideoView.initConfig(this.mConfig);
            return;
        }
        this.mMediaPlayCenter = new MediaPlayCenter(context);
        this.mMediaPlayCenter.setMediaAspectRatio(MediaAspectRatio.DW_CENTER_CROP);
        this.mMediaPlayCenter.setBusinessId(businessId);
        this.mMediaPlayCenter.setNeedPlayControlView(false);
        this.mMediaPlayCenter.setConfigGroup("MediaLive");
        this.mMediaPlayCenter.hideController();
        this.mMediaPlayCenter.setMediaLifecycleListener(this);
    }

    public void setScenarioType(int scenarioType) {
        if (this.mOldVideoView) {
            this.mConfig.mScenarioType = scenarioType;
        } else {
            this.mMediaPlayCenter.setScenarioType(scenarioType);
        }
    }

    public void setDeviceLevel(String deviceLevel) {
        if (this.mOldVideoView) {
            this.mConfig.mDeviceLevel = deviceLevel;
        }
    }

    public void setRenderType(boolean vrLive, int type, int vrRenderType, int vrLng, int vrLat) {
        if (!this.mOldVideoView) {
            this.mMediaPlayCenter.setRenderType(vrLive, vrRenderType, vrLng, vrLat);
        } else if (vrLive) {
            this.mVideoView.setRenderType(type, vrRenderType, vrLng, vrLat);
        } else {
            this.mVideoView.setRenderType(type);
        }
    }

    public void setSubBusinessType(String subBusinessType) {
        if (this.mOldVideoView) {
            this.mConfig.mSubBusinessType = subBusinessType;
        } else {
            this.mMediaPlayCenter.setBizCode(subBusinessType);
        }
    }

    public void setDefinition(String definition) {
        if (this.mOldVideoView) {
            this.mVideoView.setVideoDefinition(definition);
        }
    }

    public void setFeedId(String feedId) {
        if (this.mOldVideoView) {
            this.mVideoView.setFeedId(feedId);
        } else {
            this.mMediaPlayCenter.setMediaId(feedId);
        }
    }

    public void setLowDeviceFirstRender(boolean open) {
        if (!this.mOldVideoView) {
            this.mMediaPlayCenter.setLowDeviceFirstRender(open);
        }
    }

    public void setMediaSourceType(String mediaSourceType) {
        if (this.mOldVideoView) {
            this.mVideoView.setMediaSourceType(mediaSourceType);
        } else {
            this.mMediaPlayCenter.setMediaSourceType(mediaSourceType);
        }
    }

    public String getVideoPlayUrl() {
        if (this.mOldVideoView) {
            return this.mPlayUrl;
        }
        return this.mMediaPlayCenter.getMediaPlayUrl();
    }

    public void setUseArtp(boolean artp) {
        if (!this.mOldVideoView) {
            this.mMediaPlayCenter.setUseArtp(artp);
        }
    }

    public void setPlayerType(int playerType) {
        if (this.mOldVideoView) {
            this.mConfig.mPlayerType = playerType;
        } else {
            this.mMediaPlayCenter.setPlayerType(playerType);
        }
    }

    public void setDecoderTypeH265(int decoderTypeH265) {
        if (this.mOldVideoView) {
            this.mConfig.mDecoderTypeH265 = decoderTypeH265;
        } else if (decoderTypeH265 == 1) {
            this.mMediaPlayCenter.setHardwareHevc(Boolean.TRUE.booleanValue());
        }
    }

    public void setDecoderTypeH264(int decoderTypeH264) {
        if (this.mOldVideoView) {
            this.mConfig.mDecoderTypeH264 = decoderTypeH264;
        } else if (decoderTypeH264 == 1) {
            this.mMediaPlayCenter.setHardwareAvc(Boolean.TRUE.booleanValue());
        }
    }

    public void setShowNoWifiToast(boolean showNoWifiToast) {
        if (this.mOldVideoView) {
            this.mConfig.mbShowNoWifiToast = showNoWifiToast;
        } else {
            this.mMediaPlayCenter.setShowNoWifiToast(showNoWifiToast);
        }
    }

    public boolean isPlaying() {
        if (this.mOldVideoView) {
            return this.mVideoView.isPlaying();
        }
        return this.mMediaPlayCenter.isPlaying();
    }

    public boolean isInPlaybackState() {
        if (this.mOldVideoView) {
            return this.mVideoView.isInPlaybackState();
        }
        return this.mMediaPlayCenter.isInPlaybackState();
    }

    public void setMuted(boolean muted) {
        if (this.mOldVideoView) {
            this.mVideoView.setMuted(muted);
        } else {
            this.mMediaPlayCenter.mute(muted);
        }
    }

    public void start() {
        if (this.mOldVideoView) {
            this.mVideoView.start();
        } else {
            this.mMediaPlayCenter.start();
        }
    }

    public void setPropertyLong(int property, float value) {
        if (this.mOldVideoView) {
            this.mVideoView.setPropertyFloat(property, value);
        } else {
            this.mMediaPlayCenter.setPropertyFloat(property, value);
        }
    }

    public void setPropertyLong(int property, long value) {
        if (this.mOldVideoView) {
            this.mVideoView.setPropertyLong(property, value);
        } else {
            this.mMediaPlayCenter.setPropertyLong(property, value);
        }
    }

    public void setSurfaceListener(TaoLiveVideoView.SurfaceListener listener) {
        if (this.mOldVideoView) {
            this.mVideoView.setSurfaceListener(listener);
        } else {
            this.mMediaPlayCenter.setSurfaceListener(listener);
        }
    }

    public void setPropertyFloat(int property, float value) {
        if (this.mOldVideoView) {
            this.mVideoView.setPropertyFloat(property, value);
        } else {
            this.mMediaPlayCenter.setPropertyFloat(property, value);
        }
    }

    public void setVideoPath(String videoPath) {
        if (this.mOldVideoView) {
            this.mVideoView.setVideoPath(videoPath);
        } else {
            this.mMediaPlayCenter.setMediaUrl(videoPath);
        }
    }

    public void pause() {
        if (this.mOldVideoView) {
            this.mVideoView.pause();
        } else {
            this.mMediaPlayCenter.pause();
        }
    }

    public void seekTo(int position) {
        if (this.mOldVideoView) {
            this.mVideoView.seekTo(position);
        } else {
            this.mMediaPlayCenter.seekTo(position);
        }
    }

    public void setCoverImg(Drawable drawable, boolean bFullscreen) {
        if (this.mOldVideoView) {
            this.mVideoView.setCoverImg(drawable, bFullscreen);
        } else {
            this.mMediaPlayCenter.setCoverImg(drawable, bFullscreen);
        }
    }

    public void setAccountId(String accountId) {
        if (!this.mOldVideoView) {
            this.mMediaPlayCenter.setAccountId(accountId);
        } else {
            this.mVideoView.setAccountId(accountId);
        }
    }

    public void setUserId(String userId) {
        if (!this.mOldVideoView) {
            this.mMediaPlayCenter.setUserId(userId);
        } else {
            this.mConfig.mUserId = userId;
        }
    }

    public void setMediaInfoData(MediaLiveInfo infoData, String url) {
        if (this.mOldVideoView) {
            this.mPlayUrl = url;
            this.mVideoView.setVideoPath(url);
        } else if (infoData != null) {
            this.mMediaPlayCenter.updateLiveMediaInfoData(infoData);
        } else if (!TextUtils.isEmpty(url)) {
            this.mMediaPlayCenter.updateLiveMediaInfoData((MediaLiveInfo) null);
            this.mMediaPlayCenter.setMediaUrl(url);
        }
    }

    public void registerOnCompletionListener(IMediaPlayer.OnCompletionListener listener) {
        if (this.mOldVideoView) {
            this.mVideoView.registerOnCompletionListener(listener);
            return;
        }
        if (this.mOnCompletionListeners == null) {
            this.mOnCompletionListeners = new LinkedList();
        }
        this.mOnCompletionListeners.add(listener);
    }

    public void unregisterOnCompletionListener(IMediaPlayer.OnCompletionListener listener) {
        if (this.mOldVideoView) {
            this.mVideoView.unregisterOnCompletionListener(listener);
        } else if (this.mOnCompletionListeners != null) {
            this.mOnCompletionListeners.remove(listener);
        }
    }

    public void registerOnStartListener(TaoLiveVideoView.OnStartListener listener) {
        if (this.mOldVideoView) {
            this.mVideoView.registerOnStartListener(listener);
            return;
        }
        if (this.mOnStartListeners == null) {
            this.mOnStartListeners = new LinkedList();
        }
        this.mOnStartListeners.add(listener);
    }

    public void unregisterOnStartListener(TaoLiveVideoView.OnStartListener listener) {
        if (this.mOldVideoView) {
            this.mVideoView.unregisterOnStartListener(listener);
        } else if (this.mOnStartListeners != null) {
            this.mOnStartListeners.remove(listener);
        }
    }

    public void registerOnPauseListener(TaoLiveVideoView.OnPauseListener listener) {
        if (this.mOldVideoView) {
            this.mVideoView.registerOnPauseListener(listener);
            return;
        }
        if (this.mOnPauseListeners == null) {
            this.mOnPauseListeners = new LinkedList();
        }
        this.mOnPauseListeners.add(listener);
    }

    public void unregisterOnPauseListener(TaoLiveVideoView.OnPauseListener listener) {
        if (this.mOldVideoView) {
            this.mVideoView.unregisterOnPauseListener(listener);
        } else if (this.mOnPauseListeners != null) {
            this.mOnPauseListeners.remove(listener);
        }
    }

    public void registerOnPreparedListener(IMediaPlayer.OnPreparedListener listener) {
        if (this.mOldVideoView) {
            this.mVideoView.registerOnPreparedListener(listener);
            return;
        }
        if (this.mOnPreparedListeners == null) {
            this.mOnPreparedListeners = new LinkedList();
        }
        this.mOnPreparedListeners.add(listener);
    }

    public void unregisterOnPreparedListener(IMediaPlayer.OnPreparedListener listener) {
        if (this.mOldVideoView) {
            this.mVideoView.unregisterOnPreparedListener(listener);
        } else if (this.mOnPreparedListeners != null) {
            this.mOnPreparedListeners.remove(listener);
        }
    }

    public void registerOnInfoListener(IMediaPlayer.OnInfoListener listener) {
        if (this.mOldVideoView) {
            this.mVideoView.registerOnInfoListener(listener);
            return;
        }
        if (this.mOnInfoListeners == null) {
            this.mOnInfoListeners = new LinkedList();
        }
        this.mOnInfoListeners.add(listener);
    }

    public void unregisterOnInfoListener(IMediaPlayer.OnInfoListener listener) {
        if (this.mOldVideoView) {
            this.mVideoView.unregisterOnInfoListener(listener);
        } else if (this.mOnInfoListeners != null) {
            this.mOnInfoListeners.remove(listener);
        }
    }

    public void registerOnErrorListener(IMediaPlayer.OnErrorListener listener) {
        if (this.mOldVideoView) {
            this.mVideoView.registerOnErrorListener(listener);
            return;
        }
        if (this.mOnErrorListeners == null) {
            this.mOnErrorListeners = new LinkedList();
        }
        this.mOnErrorListeners.add(listener);
    }

    public void unregisterOnErrorListener(IMediaPlayer.OnErrorListener listener) {
        if (this.mOldVideoView) {
            this.mVideoView.unregisterOnErrorListener(listener);
        } else if (this.mOnErrorListeners != null) {
            this.mOnErrorListeners.remove(listener);
        }
    }

    public int getDuration() {
        if (this.mOldVideoView) {
            return this.mVideoView.getDuration();
        }
        return this.mMediaPlayCenter.getDuration();
    }

    public int getCurrentPosition() {
        if (this.mOldVideoView) {
            return this.mVideoView.getCurrentPosition();
        }
        return this.mMediaPlayCenter.getCurrentPosition();
    }

    public void setPlayRate(float playRate) {
        if (this.mOldVideoView) {
            this.mVideoView.setPlayRate(playRate);
        } else {
            this.mMediaPlayCenter.setPlayRate(playRate);
        }
    }

    public int getBufferPercentage() {
        if (this.mOldVideoView) {
            return this.mVideoView.getBufferPercentage();
        }
        return this.mMediaPlayCenter.getBufferPercentage();
    }

    private boolean supportH265() {
        boolean useH265;
        if (!this.mOldVideoView) {
            return Boolean.FALSE.booleanValue();
        }
        if (this.mConfig == null || MediaAdapteManager.mConfigAdapter == null) {
            return false;
        }
        ConfigAdapter mConfigAdapter = MediaAdapteManager.mConfigAdapter;
        this.mConfig.mDecoderTypeH265 = 0;
        if (this.mConfig == null || !MediaConstant.LBLIVE_SOURCE.equals(this.mConfig.mBusinessId) || mConfigAdapter == null || this.mConfig.mPlayerType != 1 || !AndroidUtils.parseBoolean(mConfigAdapter.getConfig(this.mConfig.mConfigGroup, "h265EnableHardware", "false"))) {
            useH265 = false;
        } else {
            useH265 = true;
        }
        if (useH265) {
            if (AndroidUtils.isInList(AndroidUtils.getCPUName(), mConfigAdapter.getConfig(TaoLiveVideoView.TBLIVE_ORANGE_GROUP, "h265HardwareDecodeWhiteList", ""))) {
                if (!AndroidUtils.isInList(Build.MODEL, mConfigAdapter.getConfig(TaoLiveVideoView.TBLIVE_ORANGE_GROUP, "h265HardwareDecodeBlackList", "")) && Build.VERSION.SDK_INT >= 23) {
                    this.mConfig.mDecoderTypeH265 = 1;
                    return Boolean.TRUE.booleanValue();
                }
            }
            if (this.mConfig.mDecoderTypeH265 != 1 && Build.VERSION.SDK_INT >= 21) {
                return MediaDeviceUtils.isSupportH265(mConfigAdapter.getConfig(TaoLiveVideoView.TBLIVE_ORANGE_GROUP, "h265MaxFreq", "1.8"));
            }
        }
        return Boolean.FALSE.booleanValue();
    }

    public void setFirstRenderTime() {
        if (this.mOldVideoView) {
            this.mVideoView.setFirstRenderTime();
        }
    }

    public void release() {
        if (this.mOldVideoView) {
            this.mVideoView.release();
        } else {
            this.mMediaPlayCenter.release();
        }
    }

    public int getVideoWidth() {
        if (this.mOldVideoView) {
            return this.mVideoView.getVideoWidth();
        }
        return this.mMediaPlayCenter.getVideoWidth();
    }

    public void setTransH265(boolean transH265) {
        if (!this.mOldVideoView) {
            this.mMediaPlayCenter.setTransH265(transH265);
        }
    }

    public void setH265Enable(boolean h265Enable) {
        if (!this.mOldVideoView) {
            this.mMediaPlayCenter.setH265Enable(h265Enable);
        }
    }

    public int getVideoHeight() {
        if (this.mOldVideoView) {
            return this.mVideoView.getVideoHeight();
        }
        return this.mMediaPlayCenter.getVideoHeight();
    }

    public void setConfigAdapter(ConfigAdapter adapter) {
        if (this.mOldVideoView) {
            this.mVideoView.setConfigAdapter(adapter);
        }
    }

    public void setLogAdapter(LogAdapter adapter) {
        if (this.mOldVideoView) {
            this.mVideoView.setLogAdapter(adapter);
        }
    }

    public void setup() {
        if (!this.mOldVideoView) {
            this.mMediaPlayCenter.setup();
        }
    }

    public View getView() {
        if (this.mOldVideoView) {
            return this.mVideoView;
        }
        return this.mMediaPlayCenter.getView();
    }

    public void setConfigGroup(String configGroup) {
        if (this.mOldVideoView) {
            this.mConfig.mConfigGroup = configGroup;
        }
    }

    public void onMediaStart() {
        if (this.mOnStartListeners != null) {
            for (TaoLiveVideoView.OnStartListener listener : this.mOnStartListeners) {
                if (listener != null) {
                    listener.onStart((IMediaPlayer) null);
                }
            }
        }
    }

    public void onMediaPause(boolean auto) {
        if (this.mOnPauseListeners != null) {
            for (TaoLiveVideoView.OnPauseListener listener : this.mOnPauseListeners) {
                if (listener != null) {
                    listener.onPause((IMediaPlayer) null);
                }
            }
        }
    }

    public void onMediaPlay() {
        if (this.mOnStartListeners != null) {
            for (TaoLiveVideoView.OnStartListener listener : this.mOnStartListeners) {
                if (listener != null) {
                    listener.onStart((IMediaPlayer) null);
                }
            }
        }
    }

    public void onMediaSeekTo(int currentPosition) {
    }

    public void onMediaPrepared(IMediaPlayer mp) {
        if (this.mOnPreparedListeners != null) {
            for (IMediaPlayer.OnPreparedListener listener : this.mOnPreparedListeners) {
                if (listener != null) {
                    listener.onPrepared(mp);
                }
            }
        }
    }

    public void onMediaError(IMediaPlayer mp, int what, int extra) {
        if (this.mOnErrorListeners != null) {
            for (IMediaPlayer.OnErrorListener listener : this.mOnErrorListeners) {
                listener.onError(mp, what, extra);
            }
        }
    }

    public void onMediaInfo(IMediaPlayer mp, long what, long extra, long ext, Object obj) {
        if (this.mOnInfoListeners != null) {
            for (IMediaPlayer.OnInfoListener listener : this.mOnInfoListeners) {
                if (listener != null) {
                    listener.onInfo(mp, what, extra, ext, obj);
                }
            }
        }
    }

    public void onMediaComplete() {
        if (this.mOnCompletionListeners != null) {
            for (IMediaPlayer.OnCompletionListener listener : this.mOnCompletionListeners) {
                if (listener != null) {
                    listener.onCompletion((IMediaPlayer) null);
                }
            }
        }
    }

    public void onMediaClose() {
    }

    public void onMediaScreenChanged(MediaPlayScreenType type) {
    }

    public void onMediaProgressChanged(int currentPosition, int bufferPercent, int total) {
    }

    public void changeQuality(int index) {
        if (!this.mOldVideoView) {
            this.mMediaPlayCenter.changeQuality(index);
        }
    }

    public void destroy() {
        if (!this.mOldVideoView) {
            this.mMediaPlayCenter.destroy();
        }
    }
}
