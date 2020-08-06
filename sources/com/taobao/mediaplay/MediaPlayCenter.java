package com.taobao.mediaplay;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.taobao.media.MediaConstant;
import com.taobao.media.MediaSystemUtils;
import com.taobao.mediaplay.common.IRootViewClickListener;
import com.taobao.mediaplay.model.MediaLiveInfo;
import com.taobao.mediaplay.player.IMediaLoopCompleteListener;
import com.taobao.mediaplay.player.IMediaPlayLifecycleListener;
import com.taobao.mediaplay.player.MediaAspectRatio;
import com.taobao.taobaoavsdk.util.DWLogUtils;
import com.taobao.taobaoavsdk.widget.media.TaoLiveVideoView;
import java.io.Serializable;
import java.net.URI;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MediaPlayCenter implements Serializable, IMediaLifecycleListener {
    AlphaAnimation mAlphaAnimation;
    ImageView mCoverImgView;
    private boolean mDestroy;
    private IMediaPlayer.OnVideoClickListener mMediaClickListener;
    private MediaContext mMediaContext;
    private MediaController mMediaController;
    private IMediaLoopCompleteListener mMediaLoopCompleteListener;
    private IMediaPlayLifecycleListener mMediaPlayLifecycleListener;
    private MediaPicController mPicController;
    private FrameLayout mRootView;
    private IRootViewClickListener mRootViewClickListener;
    private boolean mSetup;
    TaoLiveVideoView.SurfaceListener mSurfaceListener;

    private MediaPlayCenter() {
    }

    public MediaPlayCenter(Context context) {
        this.mMediaContext = new MediaContext(context);
        this.mMediaContext.mMediaPlayContext = new MediaPlayControlContext(context);
        this.mMediaContext.genPlayToken();
        this.mRootView = new FrameLayout(context);
        this.mRootView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
    }

    public void setsetBackgroundColor(int color) {
        if (this.mRootView != null) {
            this.mRootView.setBackgroundColor(color);
        }
    }

    public void setup() {
        if (!this.mSetup) {
            this.mSetup = true;
            if (!checkParams()) {
                if (MediaSystemUtils.isApkDebuggable()) {
                    String utParamsStr = "";
                    if (this.mMediaContext.getUTParams() != null) {
                        for (Map.Entry<String, String> entry : this.mMediaContext.getUTParams().entrySet()) {
                            utParamsStr = utParamsStr + entry.getKey() + "=" + entry.getValue() + SymbolExpUtil.SYMBOL_SEMICOLON;
                        }
                    }
                    if (this.mMediaContext != null) {
                        DWLogUtils.e(this.mMediaContext.mMediaPlayContext.mTLogAdapter, " please set mBizCode , mVideoSource and  mVideoId parameters" + utParamsStr);
                    }
                } else if (this.mMediaContext != null) {
                    DWLogUtils.e(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "  please set mBizCode , mVideoSource and  mVideoId parameters" + this.mMediaContext.mMediaPlayContext.getVideoUrl());
                }
            }
            if (this.mMediaController == null) {
                this.mMediaController = new MediaController(this.mMediaContext);
                this.mMediaController.setRootViewClickListener(this.mRootViewClickListener);
                this.mMediaController.registerLifecycle(this);
                if (this.mSurfaceListener != null) {
                    this.mMediaController.setSurfaceListener(this.mSurfaceListener);
                }
                this.mRootView.addView(this.mMediaController.getView(), 0, new FrameLayout.LayoutParams(-1, -1, 17));
                this.mMediaController.setMuted(this.mMediaContext.mMute);
                if (this.mMediaLoopCompleteListener != null) {
                    this.mMediaContext.getVideo().registerIMediaLoopCompleteListener(this.mMediaLoopCompleteListener);
                }
                if (this.mMediaPlayLifecycleListener != null) {
                    this.mMediaContext.getVideo().registerIMediaLifecycleListener(this.mMediaPlayLifecycleListener);
                }
                if (this.mMediaClickListener != null) {
                    this.mMediaContext.getVideo().registerOnVideoClickListener(this.mMediaClickListener);
                }
            }
        }
    }

    private boolean checkParams() {
        if (TextUtils.isEmpty(this.mMediaContext.mMediaPlayContext.mFrom) || TextUtils.isEmpty(this.mMediaContext.mMediaPlayContext.mVideoSource) || TextUtils.isEmpty(this.mMediaContext.mMediaPlayContext.mVideoId)) {
            DWLogUtils.e(DWLogUtils.TAG, "缺少必填参数 bizCode、videoSource、videoId！！");
        }
        if (TextUtils.isEmpty(this.mMediaContext.mMediaPlayContext.mFrom)) {
            this.mMediaContext.mMediaPlayContext.mFrom = "default";
        }
        if (TextUtils.isEmpty(this.mMediaContext.mMediaPlayContext.getVideoUrl()) && MediaConstant.YOUKU_SOURCE.equals(this.mMediaContext.mMediaPlayContext.mVideoSource) && this.mMediaContext.mMediaPlayContext.mYKVideoSourceAdapter != null && !TextUtils.isEmpty(this.mMediaContext.mMediaPlayContext.mVideoId)) {
            return true;
        }
        if (!TextUtils.isEmpty(this.mMediaContext.mMediaPlayContext.getVideoUrl()) && TextUtils.isEmpty(this.mMediaContext.mMediaPlayContext.mVideoId) && MediaConstant.TBVIDEO_SOURCE.equals(this.mMediaContext.mMediaPlayContext.mVideoSource)) {
            try {
                String rawPath = new URI(this.mMediaContext.mMediaPlayContext.getVideoUrl()).getRawPath();
                int start = rawPath.lastIndexOf(47);
                int end = rawPath.lastIndexOf(46);
                if (start >= 0 && end >= 0 && start + 1 < rawPath.length() && end > start + 1) {
                    this.mMediaContext.mMediaPlayContext.mVideoId = rawPath.substring(start + 1, end);
                }
            } catch (Exception e) {
            }
        }
        if (TextUtils.isEmpty(this.mMediaContext.mMediaPlayContext.mVideoId)) {
            return false;
        }
        return true;
    }

    private void initMediaMode(MediaType mediaType) {
        this.mMediaContext.mMediaType = mediaType;
        if (this.mMediaController == null) {
            this.mMediaController = new MediaController(this.mMediaContext);
            this.mMediaController.setRootViewClickListener(this.mRootViewClickListener);
            this.mRootView.addView(this.mMediaController.getView(), new FrameLayout.LayoutParams(-1, -1, 17));
            this.mMediaContext.getVideo().registerIMediaLoopCompleteListener(this.mMediaLoopCompleteListener);
            this.mMediaContext.getVideo().registerIMediaLifecycleListener(this.mMediaPlayLifecycleListener);
            this.mMediaContext.getVideo().registerOnVideoClickListener(this.mMediaClickListener);
        }
    }

    private void initPicMode() {
        this.mMediaContext.setMediaType(MediaType.PIC);
        this.mPicController = new MediaPicController(this.mMediaContext);
        this.mRootView.addView(this.mPicController.getView(), new FrameLayout.LayoutParams(-1, -1, 17));
    }

    public void updateLiveMediaInfoData(MediaLiveInfo infoData) {
        this.mMediaContext.mMediaPlayContext.mMediaLiveInfo = infoData;
        if (this.mMediaController != null) {
            this.mMediaController.updateLiveMediaInfoData();
        }
    }

    public void changeQuality(int index) {
        if (this.mMediaController != null) {
            this.mMediaController.changeQuality(index);
        }
    }

    public void setMediaType(MediaType mediaType) {
        if (!this.mSetup) {
            this.mMediaContext.setMediaType(mediaType);
        } else if (this.mDestroy) {
        } else {
            if (this.mMediaContext.getMediaType() == MediaType.PIC && mediaType != MediaType.PIC) {
                this.mMediaContext.setMediaType(mediaType);
                initMediaMode(mediaType);
            } else if ((this.mMediaContext.getMediaType() == MediaType.VIDEO || this.mMediaContext.getMediaType() == MediaType.LIVE) && this.mMediaController != null && mediaType == MediaType.PIC) {
                this.mMediaContext.setMediaType(MediaType.PIC);
                if (this.mPicController == null) {
                    initPicMode();
                } else {
                    this.mMediaController.setLifecycleType(MediaLifecycleType.BEFORE);
                }
            }
        }
    }

    public void setMediaLifecycleListener(IMediaPlayLifecycleListener mediaPlayLifecycleListener) {
        this.mMediaPlayLifecycleListener = mediaPlayLifecycleListener;
        if (this.mMediaContext != null && this.mMediaContext.getVideo() != null) {
            this.mMediaContext.getVideo().registerIMediaLifecycleListener(mediaPlayLifecycleListener);
        }
    }

    public void setMediaLoopCompleteListener(IMediaLoopCompleteListener loopCompleteListener) {
        this.mMediaLoopCompleteListener = loopCompleteListener;
        if (this.mMediaContext != null && this.mMediaContext.getVideo() != null) {
            this.mMediaContext.getVideo().registerIMediaLoopCompleteListener(loopCompleteListener);
        }
    }

    public void setSEIVideoClickListener(IMediaPlayer.OnVideoClickListener listener) {
        this.mMediaClickListener = listener;
        if (this.mMediaContext != null && this.mMediaContext.getVideo() != null && listener != null) {
            this.mMediaContext.getVideo().registerOnVideoClickListener(listener);
        }
    }

    public void setIMediaLoopCompleteListener(IMediaLoopCompleteListener loopCompleteListener) {
        this.mMediaLoopCompleteListener = loopCompleteListener;
        if (this.mMediaContext != null && this.mMediaContext.getVideo() != null) {
            this.mMediaContext.getVideo().registerIMediaLoopCompleteListener(loopCompleteListener);
        }
    }

    public View getView() {
        return this.mRootView;
    }

    public void setHardwareAvc(boolean hardwareAvc) {
        if (!this.mSetup && this.mMediaContext.mMediaPlayContext != null) {
            this.mMediaContext.mMediaPlayContext.setHardwareAvc(hardwareAvc);
        }
    }

    public void setUserId(String userId) {
        if (!this.mSetup && this.mMediaContext != null) {
            this.mMediaContext.mUserId = userId;
        }
    }

    public void setHardwareHevc(boolean hardwareHevc) {
        if (!this.mSetup && this.mMediaContext.mMediaPlayContext != null) {
            this.mMediaContext.mMediaPlayContext.setHardwareHevc(hardwareHevc);
        }
    }

    public void setCoverImg(Drawable coverDrawable, boolean bFullscreen) {
        if (this.mPicController == null) {
            this.mPicController = new MediaPicController(this.mMediaContext);
            this.mCoverImgView = new ImageView(this.mMediaContext.getContext());
            this.mPicController.setPicImageView(this.mCoverImgView);
            this.mRootView.addView(this.mPicController.getView(), new FrameLayout.LayoutParams(-1, -1, 17));
            this.mPicController.getView().bringToFront();
        }
        setMediaLifecycleType(MediaLifecycleType.BEFORE);
        if (bFullscreen) {
            this.mCoverImgView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
            this.mCoverImgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
            params.gravity = 17;
            this.mCoverImgView.setLayoutParams(params);
        }
        this.mCoverImgView.setImageDrawable(coverDrawable);
    }

    public void setMediaLifecycleType(MediaLifecycleType lifecycleType) {
        if (this.mMediaController != null) {
            this.mMediaController.setLifecycleType(lifecycleType);
        }
    }

    public void start() {
        if (!this.mDestroy && this.mSetup && this.mMediaController != null) {
            this.mMediaController.start();
        }
    }

    public void pause() {
        if (!this.mDestroy && this.mSetup && this.mMediaController != null) {
            this.mMediaController.pause();
        }
    }

    public void seekTo(int position) {
        if (this.mMediaController != null) {
            this.mMediaController.seekTo(position);
        } else {
            this.mMediaContext.mMediaPlayContext.mSeekWhenPrepared = position;
        }
    }

    public void registerOnVideoClickListener(IMediaPlayer.OnVideoClickListener clickListener) {
        if (this.mMediaController != null) {
            this.mMediaController.registerOnVideoClickListener(clickListener);
        }
    }

    public void unregisterOnVideoClickListener(IMediaPlayer.OnVideoClickListener clickListener) {
        if (this.mMediaController != null) {
            this.mMediaController.unregisterOnVideoClickListener(clickListener);
        }
    }

    public void setPicImageView(ImageView imageView) {
        if (imageView != null && this.mPicController != null) {
            this.mPicController.setPicImageView(imageView);
        }
    }

    public void setRootViewClickListener(IRootViewClickListener listener) {
        this.mRootViewClickListener = listener;
        if (this.mMediaController != null) {
            this.mMediaController.setRootViewClickListener(this.mRootViewClickListener);
        }
    }

    public void setMediaUrl(String mediaUrl) {
        this.mMediaContext.mMediaPlayContext.setVideoUrl(mediaUrl);
        if (this.mMediaController != null) {
            this.mMediaController.updateLiveMediaUrl();
        }
    }

    public void setLocalVideo(boolean localVideo) {
        if (!this.mSetup) {
            this.mMediaContext.mMediaPlayContext.mLocalVideo = localVideo;
        }
    }

    public void setRenderType(boolean vrLive, int vrRenderType, int vrLng, int vrLat) {
        if (!this.mSetup) {
            this.mMediaContext.mVRRenderType = vrRenderType;
            this.mMediaContext.mVRLive = vrLive;
            this.mMediaContext.mVRLng = vrLng;
            this.mMediaContext.mVRLat = vrLat;
        }
    }

    public void setPropertyFloat(int property, float value) {
        if (this.mMediaContext.getVideo() != null) {
            this.mMediaContext.getVideo().setPropertyFloat(property, value);
        }
    }

    public void setPropertyLong(int property, long value) {
        if (this.mMediaContext.getVideo() != null) {
            this.mMediaContext.getVideo().setPropertyLong(property, value);
        }
    }

    public void setMediaId(String videoId) {
        this.mMediaContext.mMediaPlayContext.mVideoId = videoId;
        if (this.mMediaController != null) {
            this.mMediaController.setMediaId(videoId);
        }
    }

    public void setAccountId(String accountId) {
        if (!(this.mMediaContext == null || this.mMediaContext.mMediaPlayContext == null)) {
            this.mMediaContext.mMediaPlayContext.mAccountId = accountId;
        }
        if (this.mMediaController != null) {
            this.mMediaController.setAccountId(accountId);
        }
    }

    public void setNeedPlayControlView(boolean need) {
        this.mMediaContext.setNeedPlayControlView(need);
    }

    public void setVideoLoop(boolean loop) {
        this.mMediaContext.mLoop = loop;
    }

    public void setMediaSource(String mediaSource) {
        this.mMediaContext.mMediaPlayContext.mVideoSource = mediaSource;
    }

    public void setUseArtp(boolean useArtp) {
        this.mMediaContext.mMediaPlayContext.setUseArtp(useArtp);
    }

    public void setBizCode(String from) {
        this.mMediaContext.mMediaPlayContext.mFrom = from;
    }

    public void setScenarioType(int scenarioType) {
        this.mMediaContext.mScenarioType = scenarioType;
    }

    public void setBusinessId(String businessId) {
        if (!this.mSetup) {
            this.mMediaContext.mMediaPlayContext.setBusinessId(businessId);
        }
    }

    public void setConfigGroup(String configGroup) {
        this.mMediaContext.mMediaPlayContext.mConfigGroup = configGroup;
    }

    public void setMute(boolean mute) {
        this.mMediaContext.mMute = mute;
    }

    public void setH265Enable(boolean h265Enable) {
        if (!this.mSetup) {
            this.mMediaContext.mMediaPlayContext.mH265Enable = h265Enable;
        }
    }

    public void setPlayerType(int playerType) {
        this.mMediaContext.mMediaPlayContext.setPlayerType(playerType);
    }

    public void setShowNoWifiToast(boolean showNoWifiToast) {
        this.mMediaContext.mbShowNoWifiToast = showNoWifiToast;
    }

    public void setMediaAspectRatio(MediaAspectRatio aspectRatio) {
        this.mMediaContext.setMediaAspectRatio(aspectRatio);
        if (this.mMediaContext != null && this.mMediaContext.getVideo() != null) {
            this.mMediaContext.getVideo().setMediaAspectRatio(aspectRatio);
        }
    }

    public void setNeedScreenButton(boolean need) {
        if (!this.mSetup) {
            this.mMediaContext.mNeedScreenButton = need;
        }
    }

    public void setMediaSourceType(String mediaSourceType) {
        this.mMediaContext.mMediaPlayContext.mMediaSourceType = mediaSourceType;
        if (this.mMediaController != null) {
            this.mMediaController.setMediaSourceType(mediaSourceType);
        }
    }

    public void addFullScreenCustomView(View view) {
        if (this.mMediaController != null) {
            this.mMediaController.addFullScreenCustomView(view);
        }
    }

    public void removeFullScreenCustomView() {
        if (this.mMediaController != null) {
            this.mMediaController.removeFullScreenCustomView();
        }
    }

    public void showController() {
        this.mMediaContext.hideControllerView(false);
        if (this.mMediaController != null) {
            this.mMediaController.showController();
        }
    }

    public void hideController() {
        this.mMediaContext.hideControllerView(true);
        if (this.mMediaController != null) {
            this.mMediaController.hideController();
        }
    }

    public void setPlayRate(float playRate) {
        if (this.mMediaContext != null && this.mMediaContext.getVideo() != null) {
            this.mMediaContext.getVideo().setPlayRate(playRate);
        }
    }

    public void hiddenPlayingIcon(boolean hide) {
        if (!this.mSetup) {
            this.mMediaContext.mHiddenPlayingIcon = hide;
        }
    }

    public void hiddenMiniProgressBar(boolean hide) {
        if (!this.mSetup) {
            this.mMediaContext.mHiddenMiniProgressBar = hide;
        }
    }

    public void enableVideoClickDetect(boolean enableClickDetect) {
        if (this.mMediaContext.getVideo() != null) {
            this.mMediaContext.getVideo().enableVideoClickDetect(enableClickDetect);
        }
    }

    public void blockTouchEvent(boolean block) {
        if (this.mMediaContext.getVideo() != null) {
            this.mMediaContext.getVideo().blockTouchEvent(block);
        }
    }

    public void hiddenThumbnailPlayBtn(boolean hide) {
        if (!this.mSetup) {
            this.mMediaContext.mHiddenThumbnailPlayBtn = hide;
        }
    }

    public void hiddenLoading(boolean hide) {
        if (!this.mSetup) {
            this.mMediaContext.mHiddenLoading = hide;
        }
    }

    public boolean isPlaying() {
        if (this.mMediaController != null) {
            return this.mMediaController.isPlaying();
        }
        return false;
    }

    public boolean isInPlaybackState() {
        if (this.mMediaController != null) {
            return this.mMediaController.isInPlaybackState();
        }
        return false;
    }

    public int getVideoHeight() {
        if (this.mMediaController != null) {
            return this.mMediaController.getVideoHeight();
        }
        return 0;
    }

    public int getVideoWidth() {
        if (this.mMediaController != null) {
            return this.mMediaController.getVideoWidth();
        }
        return 0;
    }

    public void mute(boolean muted) {
        if (this.mMediaController != null) {
            this.mMediaController.setMuted(muted);
        } else {
            setMute(muted);
        }
    }

    public void release() {
        this.mSetup = false;
        if (this.mMediaController != null) {
            this.mMediaController.release();
        }
        if (this.mMediaContext != null) {
            this.mMediaContext.release();
        }
    }

    public void destroy() {
        this.mDestroy = true;
        if (this.mMediaController != null) {
            this.mMediaController.destroy();
        }
        if (this.mAlphaAnimation != null) {
            this.mAlphaAnimation.cancel();
        }
    }

    public void onLifecycleChanged(MediaLifecycleType lifecycleType) {
        if (lifecycleType == MediaLifecycleType.PLAY) {
            if (this.mPicController != null) {
                startViewFadeAnimation(this.mPicController.getView());
            }
        } else if (lifecycleType == MediaLifecycleType.BEFORE && this.mPicController != null) {
            if (this.mAlphaAnimation != null) {
                this.mAlphaAnimation.cancel();
            }
            this.mPicController.getView().setVisibility(0);
        }
    }

    public void startViewFadeAnimation(final View view) {
        if (view != null) {
            if (this.mAlphaAnimation == null) {
                this.mAlphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                this.mAlphaAnimation.setDuration(200);
                this.mAlphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        if (view != null) {
                            view.setVisibility(4);
                        }
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
            view.startAnimation(this.mAlphaAnimation);
        }
    }

    public int getBufferPercentage() {
        if (this.mMediaContext.getVideo() != null) {
            return this.mMediaContext.getVideo().getBufferPercentage();
        }
        return 0;
    }

    public int getCurrentPosition() {
        if (this.mMediaContext.getVideo() != null) {
            return this.mMediaContext.getVideo().getCurrentPosition();
        }
        return 0;
    }

    public int getDuration() {
        if (this.mMediaContext.getVideo() != null) {
            return this.mMediaContext.getVideo().getDuration();
        }
        return 0;
    }

    public String getMediaPlayUrl() {
        if (this.mMediaContext == null || this.mMediaContext.mMediaPlayContext == null) {
            return "";
        }
        return this.mMediaContext.mMediaPlayContext.getVideoUrl();
    }

    public void setTransH265(boolean transH265) {
        this.mMediaContext.mMediaPlayContext.setTransH265(transH265);
    }

    public void setLowDeviceFirstRender(boolean open) {
        this.mMediaContext.mMediaPlayContext.setLowDeviceFirstRender(open);
    }

    public void setSurfaceListener(TaoLiveVideoView.SurfaceListener listener) {
        this.mSurfaceListener = listener;
        if (listener != null && this.mMediaController != null) {
            this.mMediaController.setSurfaceListener(listener);
        }
    }
}
