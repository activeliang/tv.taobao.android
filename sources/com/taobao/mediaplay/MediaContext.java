package com.taobao.mediaplay;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Window;
import com.taobao.mediaplay.common.IMediaBackKeyEvent;
import com.taobao.mediaplay.player.MediaAspectRatio;
import com.taobao.mediaplay.playercontrol.MediaKeyBackController;
import com.taobao.taobaoavsdk.recycle.MediaPlayerManager;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MediaContext implements Serializable {
    private MediaAspectRatio mAspectRatio = MediaAspectRatio.DW_FIT_CENTER;
    private Context mContext;
    boolean mHiddenGestureView;
    boolean mHiddenLoading;
    boolean mHiddenMiniProgressBar;
    boolean mHiddenNetworkErrorView;
    boolean mHiddenPlayErrorView;
    boolean mHiddenPlayingIcon;
    boolean mHiddenThumbnailPlayBtn;
    boolean mHiddenToastView;
    private boolean mHideControllder;
    public boolean mHookKeyBackToggleEvent;
    private MediaKeyBackController mKeyBackController;
    public boolean mLoop;
    private IMedia mMedia;
    public MediaPlayControlContext mMediaPlayContext;
    public MediaType mMediaType;
    public boolean mMute;
    private boolean mNeedCloseUT = true;
    private boolean mNeedFirstPlayUT = true;
    private boolean mNeedGesture;
    private boolean mNeedPlayControlView = true;
    public boolean mNeedScreenButton = true;
    public String mPlayToken;
    private String mRID;
    public int mScenarioType = 0;
    public String mUserId;
    private Map<String, String> mUtParams;
    public int mVRLat;
    public boolean mVRLive = false;
    public int mVRLng;
    public int mVRRenderType;
    private MediaPlayScreenType mVideoScreenType = MediaPlayScreenType.NORMAL;
    private String mVideoToken;
    private Window mWindow;
    public boolean mbShowNoWifiToast;

    public MediaType getMediaType() {
        return this.mMediaType;
    }

    public void setMediaType(MediaType mInstanceType) {
        this.mMediaType = mInstanceType;
    }

    public void genPlayToken() {
        try {
            if (TextUtils.isEmpty(this.mPlayToken)) {
                this.mPlayToken = MediaPlayerManager.generateToken() + new Random().nextInt(100000);
            }
        } catch (Throwable th) {
            this.mPlayToken = System.currentTimeMillis() + "_";
        }
    }

    public IMedia getVideo() {
        return this.mMedia;
    }

    /* access modifiers changed from: package-private */
    public void setVideo(IMedia media) {
        this.mMedia = media;
    }

    public Window getWindow() {
        return this.mWindow;
    }

    public boolean isHiddenLoading() {
        return this.mHiddenLoading;
    }

    public void setHiddenLoading(boolean hiddenLoading) {
        this.mHiddenLoading = hiddenLoading;
    }

    public String getVideoToken() {
        return this.mVideoToken;
    }

    public MediaAspectRatio getVideoAspectRatio() {
        return this.mAspectRatio;
    }

    public void setMediaAspectRatio(MediaAspectRatio aspectRatio) {
        this.mAspectRatio = aspectRatio;
    }

    public boolean needCloseUT() {
        return this.mNeedCloseUT;
    }

    /* access modifiers changed from: package-private */
    public void setNeedCloseUT(boolean needCloseUT) {
        this.mNeedCloseUT = needCloseUT;
    }

    public boolean needFirstPlayUT() {
        return this.mNeedFirstPlayUT;
    }

    /* access modifiers changed from: package-private */
    public void setNeedFirstPlayUT(boolean needFirstPlayUT) {
        this.mNeedFirstPlayUT = needFirstPlayUT;
    }

    public MediaContext(Context context) {
        this.mContext = context;
        if (context instanceof Activity) {
            this.mKeyBackController = new MediaKeyBackController((Activity) this.mContext);
        }
        this.mUtParams = new HashMap();
    }

    public void handleKeyBack() {
        if (this.mKeyBackController != null) {
            this.mKeyBackController.handleKeyBack();
        }
    }

    /* access modifiers changed from: package-private */
    public void mute(boolean mute) {
        this.mMute = mute;
    }

    public boolean isMute() {
        return this.mMute;
    }

    public boolean isHideControllder() {
        return this.mHideControllder;
    }

    public void hideControllerView(boolean hideControllder) {
        this.mHideControllder = hideControllder;
    }

    public void addUtParams(Map<String, String> params) {
        if (params != null) {
            this.mUtParams.putAll(params);
        }
    }

    public Map<String, String> getUTParams() {
        return this.mUtParams;
    }

    public Context getContext() {
        return this.mContext;
    }

    /* access modifiers changed from: package-private */
    public void setVideoScreenType(MediaPlayScreenType type) {
        this.mVideoScreenType = type;
    }

    public MediaPlayScreenType screenType() {
        return this.mVideoScreenType;
    }

    public void release() {
        if (this.mKeyBackController != null) {
            this.mKeyBackController.destroy();
        }
        this.mMediaPlayContext.mMediaSourceType = "";
        if (this.mMediaPlayContext != null) {
            this.mMediaPlayContext.mDropFrameForH265 = false;
            this.mMediaPlayContext.mSVCEnable = false;
            this.mMediaPlayContext.mLowQualityUrl = "";
            this.mMediaPlayContext.setEdgePcdn(false);
        }
    }

    public void setRID(String rid) {
        this.mRID = rid;
    }

    public String getRID() {
        return this.mRID;
    }

    public void setVideoToken(String videoToken) {
        this.mVideoToken = videoToken;
    }

    public void unregisterKeyBackEventListener(IMediaBackKeyEvent backKeyEvent) {
        if (this.mKeyBackController != null) {
            this.mKeyBackController.unregisterKeyBackEventListener(backKeyEvent);
        }
    }

    public void registerKeyBackEventListener(IMediaBackKeyEvent backKeyEvent) {
        if (this.mKeyBackController != null) {
            this.mKeyBackController.registerKeyBackEventListener(backKeyEvent);
        }
    }

    public void setNeedPlayControlView(boolean needPlayControlView) {
        this.mNeedPlayControlView = needPlayControlView;
    }

    public boolean isNeedPlayControlView() {
        return this.mNeedPlayControlView;
    }
}
