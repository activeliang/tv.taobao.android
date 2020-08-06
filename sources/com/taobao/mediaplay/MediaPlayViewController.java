package com.taobao.mediaplay;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.taobao.media.DWViewUtil;
import com.taobao.mediaplay.common.IMediaBackKeyEvent;
import com.taobao.mediaplay.player.BaseVideoView;
import com.taobao.mediaplay.player.IMediaLoopCompleteListener;
import com.taobao.mediaplay.player.IMediaPlayLifecycleListener;
import com.taobao.mediaplay.player.MediaAspectRatio;
import com.taobao.mediaplay.player.TextureVideoView;
import com.taobao.taobaoavsdk.util.DWLogUtils;
import com.taobao.taobaoavsdk.widget.media.TaoLiveVideoView;
import tv.danmaku.ijk.media.player.IMediaPlayer;

class MediaPlayViewController implements IMediaPlayLifecycleListener, IMedia, IMediaBackKeyEvent {
    private static final String TAG = "MediaPlayViewController";
    private final int TOGGLESCREEN_ANIMATION = 150;
    /* access modifiers changed from: private */
    public ViewGroup containerView;
    /* access modifiers changed from: private */
    public volatile boolean mAnimationRunning;
    private FrameLayout mDecorView;
    /* access modifiers changed from: private */
    public int mFadeInCount = 0;
    private boolean mFirstAnimation = true;
    /* access modifiers changed from: private */
    public int mFullHeight;
    private boolean mFullScreenOutside;
    /* access modifiers changed from: private */
    public int mFullWidth;
    private AnimatorSet mFulltoNormalSet;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public int mHeight;
    private int mLastUiOptions;
    /* access modifiers changed from: private */
    public MediaContext mMediaContext;
    private boolean mMute;
    private boolean mNormalScreenOutside;
    /* access modifiers changed from: private */
    public int[] mNormallocation = new int[2];
    /* access modifiers changed from: private */
    public AnimatorSet mNormaltoFullSet;
    private RetryListener mRetryListener;
    /* access modifiers changed from: private */
    public BaseVideoView mVideoView;
    /* access modifiers changed from: private */
    public float mVolume;
    private boolean mVolumeFadeIn = true;
    /* access modifiers changed from: private */
    public Runnable mVolumeRun;
    /* access modifiers changed from: private */
    public int mWidth;
    /* access modifiers changed from: private */
    public ViewGroup rootView;
    /* access modifiers changed from: private */
    public boolean statusBarHide;
    /* access modifiers changed from: private */
    public float translationX;
    /* access modifiers changed from: private */
    public float translationY;

    interface OnStartListener {
        void start();
    }

    interface RetryListener {
        void retry();
    }

    static /* synthetic */ int access$1808(MediaPlayViewController x0) {
        int i = x0.mFadeInCount;
        x0.mFadeInCount = i + 1;
        return i;
    }

    public String getVideoToken() {
        if (this.mVideoView != null) {
            return this.mVideoView.getToken();
        }
        return null;
    }

    public void toggleScreen() {
        boolean fromPortrait = true;
        if (this.mMediaContext != null && (this.mMediaContext.getContext() instanceof Activity)) {
            if (this.mNormaltoFullSet != null && this.mNormaltoFullSet.isRunning()) {
                return;
            }
            if ((this.mFulltoNormalSet != null && this.mFulltoNormalSet.isRunning()) || this.mAnimationRunning) {
                return;
            }
            if (this.mMediaContext.screenType() == MediaPlayScreenType.NORMAL) {
                if (((double) this.mVideoView.getAspectRatio()) > 1.01d || this.mVideoView.getAspectRatio() == 0.0f) {
                    fromPortrait = false;
                }
                toFullScreen(fromPortrait);
                this.mLastUiOptions = DWViewUtil.hideNavigationBar(this.mMediaContext.getWindow() == null ? ((Activity) this.mMediaContext.getContext()).getWindow() : this.mMediaContext.getWindow());
                return;
            }
            if (((double) this.mVideoView.getAspectRatio()) > 1.01d || this.mVideoView.getAspectRatio() == 0.0f) {
                fromPortrait = false;
            }
            toNormalScreen(fromPortrait);
            DWViewUtil.setNavigationBar(this.mMediaContext.getWindow() == null ? ((Activity) this.mMediaContext.getContext()).getWindow() : this.mMediaContext.getWindow(), this.mLastUiOptions);
        }
    }

    private void toNormalScreen(boolean fromPortrait) {
        if (this.mMediaContext != null && (this.mMediaContext.getContext() instanceof Activity) && getView().getParent() != null && getView().getParent().getParent() != null) {
            this.mAnimationRunning = true;
            if (this.containerView == null && this.rootView == null) {
                this.containerView = (ViewGroup) getView().getParent();
                this.rootView = (ViewGroup) this.containerView.getParent();
            }
            this.containerView.setLayerType(2, (Paint) null);
            if (this.mDecorView == null) {
                this.mDecorView = (FrameLayout) ((Activity) this.mMediaContext.getContext()).getWindow().getDecorView();
            }
            if (Build.VERSION.SDK_INT == 18) {
                WindowManager.LayoutParams attrs = ((Activity) this.mMediaContext.getContext()).getWindow().getAttributes();
                attrs.flags &= -1025;
                ((Activity) this.mMediaContext.getContext()).getWindow().setAttributes(attrs);
                ((Activity) this.mMediaContext.getContext()).getWindow().clearFlags(512);
            }
            int difheight = 0;
            if (Build.VERSION.SDK_INT < 18 && !fromPortrait) {
                difheight = 0 - (DWViewUtil.getStatusBarHeight(this.mMediaContext.getContext()) / 2);
            }
            if (fromPortrait) {
                this.mFullHeight = DWViewUtil.getVideoWidthInLandscape((Activity) this.mMediaContext.getContext());
                this.mFullWidth = DWViewUtil.getPortraitScreenWidth();
                startNormalfromPortraitAnimation(this.mNormallocation[0], difheight + this.mNormallocation[1]);
                return;
            }
            this.mFullHeight = DWViewUtil.getRealWithInPx(this.mMediaContext.getContext());
            this.mFullWidth = DWViewUtil.getVideoWidthInLandscape((Activity) this.mMediaContext.getContext());
            startNormalfromLandscapeAnimation(((-(this.mFullHeight - this.mWidth)) / 2) + this.mNormallocation[0], difheight + ((-(this.mFullWidth - this.mHeight)) / 2) + this.mNormallocation[1]);
        }
    }

    private void startNormalfromPortraitAnimation(int difwidth, int difheight) {
        ObjectAnimator oaNX = ObjectAnimator.ofFloat(this.containerView, "translationX", new float[]{0.0f, (float) difwidth});
        ObjectAnimator oaNY = ObjectAnimator.ofFloat(this.containerView, "translationY", new float[]{0.0f, (float) difheight});
        this.containerView.setTranslationY(this.translationY);
        this.containerView.setTranslationX(this.translationX);
        this.mFulltoNormalSet = new AnimatorSet();
        ValueAnimator vaA = ValueAnimator.ofFloat(new float[]{90.0f, 0.0f});
        vaA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) animation.getAnimatedValue()).floatValue();
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
                layoutParams.width = (int) (((float) MediaPlayViewController.this.mWidth) + ((((float) (MediaPlayViewController.this.mFullWidth - MediaPlayViewController.this.mWidth)) * value) / 90.0f));
                layoutParams.height = (int) (((float) MediaPlayViewController.this.mHeight) + ((((float) (MediaPlayViewController.this.mFullHeight - MediaPlayViewController.this.mHeight)) * value) / 90.0f));
                MediaPlayViewController.this.containerView.setLayoutParams(layoutParams);
            }
        });
        this.mFulltoNormalSet.setDuration(150);
        this.mFulltoNormalSet.play(vaA);
        this.mFulltoNormalSet.play(oaNX);
        this.mFulltoNormalSet.play(oaNY);
        this.mFulltoNormalSet.start();
        this.mFulltoNormalSet.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                MediaPlayViewController.this.startNormalfromPortraitEnd();
            }

            public void onAnimationCancel(Animator animation) {
                MediaPlayViewController.this.startNormalfromPortraitEnd();
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void startNormalfromPortraitEnd() {
        if (this.mMediaContext != null && (this.mMediaContext.getContext() instanceof Activity)) {
            DWViewUtil.setNavigationBar(this.mMediaContext.getWindow() == null ? ((Activity) this.mMediaContext.getContext()).getWindow() : this.mMediaContext.getWindow(), this.mLastUiOptions);
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (MediaPlayViewController.this.containerView.getParent() != MediaPlayViewController.this.rootView) {
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
                        params.gravity = 17;
                        MediaPlayViewController.this.containerView.requestLayout();
                        if (MediaPlayViewController.this.containerView.getParent() != null && (MediaPlayViewController.this.containerView.getParent() instanceof ViewGroup)) {
                            ((ViewGroup) MediaPlayViewController.this.containerView.getParent()).removeView(MediaPlayViewController.this.containerView);
                            MediaPlayViewController.this.rootView.addView(MediaPlayViewController.this.containerView, params);
                        }
                        if (MediaPlayViewController.this.mMediaContext.getVideo().getVideoState() == 4) {
                            MediaPlayViewController.this.mVideoView.seekToWithoutNotify(MediaPlayViewController.this.getDuration(), false);
                        }
                        MediaPlayViewController.this.containerView.setTranslationX(MediaPlayViewController.this.translationX);
                        MediaPlayViewController.this.containerView.setTranslationY(MediaPlayViewController.this.translationY);
                        MediaPlayViewController.this.containerView.requestLayout();
                        boolean unused = MediaPlayViewController.this.mAnimationRunning = false;
                    }
                    MediaPlayViewController.this.mMediaContext.setVideoScreenType(MediaPlayScreenType.NORMAL);
                    MediaPlayViewController.this.mVideoView.onVideoScreenChanged(MediaPlayScreenType.NORMAL);
                    MediaPlayViewController.this.containerView.setLayerType(0, (Paint) null);
                }
            });
            if (this.mMediaContext != null && !this.mMediaContext.mHookKeyBackToggleEvent) {
                this.mMediaContext.unregisterKeyBackEventListener(this);
            }
        }
    }

    private void startNormalfromLandscapeAnimation(int difwidth, int difheight) {
        ObjectAnimator oaNX = ObjectAnimator.ofFloat(this.containerView, "translationX", new float[]{(float) difwidth});
        ObjectAnimator oaNY = ObjectAnimator.ofFloat(this.containerView, "translationY", new float[]{(float) difheight});
        this.containerView.setTranslationY(this.translationY);
        this.containerView.setTranslationX(this.translationX);
        this.mFulltoNormalSet = new AnimatorSet();
        ObjectAnimator oaN = ObjectAnimator.ofFloat(this.containerView, "rotation", new float[]{0.0f});
        oaN.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = Math.abs(((Float) animation.getAnimatedValue()).floatValue());
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
                layoutParams.width = (int) (((float) MediaPlayViewController.this.mWidth) + ((((float) (MediaPlayViewController.this.mFullWidth - MediaPlayViewController.this.mWidth)) * value) / 90.0f));
                layoutParams.height = (int) (((float) MediaPlayViewController.this.mHeight) + ((((float) (MediaPlayViewController.this.mFullHeight - MediaPlayViewController.this.mHeight)) * value) / 90.0f));
                layoutParams.gravity = 17;
                MediaPlayViewController.this.containerView.setLayoutParams(layoutParams);
            }
        });
        this.mFulltoNormalSet.setDuration(150);
        this.mFulltoNormalSet.play(oaN);
        this.mFulltoNormalSet.play(oaNX);
        this.mFulltoNormalSet.play(oaNY);
        this.mFulltoNormalSet.start();
        this.mFulltoNormalSet.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                MediaPlayViewController.this.startNormalfromLandscapeEnd();
            }

            public void onAnimationCancel(Animator animation) {
                MediaPlayViewController.this.startNormalfromLandscapeEnd();
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void startNormalfromLandscapeEnd() {
        if (this.mMediaContext != null && (this.mMediaContext.getContext() instanceof Activity)) {
            DWViewUtil.setNavigationBar(this.mMediaContext.getWindow() == null ? ((Activity) this.mMediaContext.getContext()).getWindow() : this.mMediaContext.getWindow(), this.mLastUiOptions);
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (MediaPlayViewController.this.containerView.getParent() != MediaPlayViewController.this.rootView) {
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
                        params.gravity = 17;
                        MediaPlayViewController.this.containerView.requestLayout();
                        if (MediaPlayViewController.this.containerView.getParent() != null && (MediaPlayViewController.this.containerView.getParent() instanceof ViewGroup)) {
                            ((ViewGroup) MediaPlayViewController.this.containerView.getParent()).removeView(MediaPlayViewController.this.containerView);
                            MediaPlayViewController.this.rootView.addView(MediaPlayViewController.this.containerView, params);
                        }
                        if (MediaPlayViewController.this.mMediaContext.getVideo().getVideoState() == 4) {
                            MediaPlayViewController.this.mVideoView.seekToWithoutNotify(MediaPlayViewController.this.getDuration(), false);
                        }
                        MediaPlayViewController.this.containerView.setTranslationX(MediaPlayViewController.this.translationX);
                        MediaPlayViewController.this.containerView.setTranslationY(MediaPlayViewController.this.translationY);
                        MediaPlayViewController.this.containerView.requestLayout();
                        boolean unused = MediaPlayViewController.this.mAnimationRunning = false;
                    }
                    MediaPlayViewController.this.mMediaContext.setVideoScreenType(MediaPlayScreenType.NORMAL);
                    MediaPlayViewController.this.mVideoView.onVideoScreenChanged(MediaPlayScreenType.NORMAL);
                    MediaPlayViewController.this.containerView.setLayerType(0, (Paint) null);
                }
            });
            if (this.mMediaContext != null && !this.mMediaContext.mHookKeyBackToggleEvent) {
                this.mMediaContext.unregisterKeyBackEventListener(this);
            }
        }
    }

    private void toFullScreen(boolean portrait) {
        if (this.mMediaContext != null && (this.mMediaContext.getContext() instanceof Activity)) {
            this.statusBarHide = false;
            if (getView().getParent() != null && getView().getParent().getParent() != null) {
                this.mAnimationRunning = true;
                if (this.containerView == null && this.rootView == null) {
                    this.containerView = (ViewGroup) getView().getParent();
                    this.rootView = (ViewGroup) this.containerView.getParent();
                }
                this.containerView.setLayerType(2, (Paint) null);
                this.mNormallocation = new int[2];
                this.rootView.getLocationInWindow(this.mNormallocation);
                this.mWidth = this.containerView.getWidth();
                this.mHeight = this.containerView.getHeight();
                if (portrait) {
                    this.translationX = this.containerView.getTranslationX();
                    this.translationY = this.containerView.getTranslationY();
                }
                if (this.mDecorView == null) {
                    this.mDecorView = (FrameLayout) ((Activity) this.mMediaContext.getContext()).getWindow().getDecorView();
                }
                if (portrait) {
                    this.mFullHeight = DWViewUtil.getVideoWidthInLandscape((Activity) this.mMediaContext.getContext());
                    this.mFullWidth = DWViewUtil.getPortraitScreenWidth();
                } else {
                    this.mFullHeight = DWViewUtil.getRealWithInPx(this.mMediaContext.getContext());
                    this.mFullWidth = DWViewUtil.getVideoWidthInLandscape((Activity) this.mMediaContext.getContext());
                }
                if (this.containerView.getParent() != this.mDecorView) {
                    this.rootView.removeView(this.containerView);
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.containerView.getLayoutParams();
                    layoutParams.gravity = 17;
                    layoutParams.width = this.mWidth;
                    layoutParams.height = this.mHeight;
                    if (layoutParams.topMargin != this.mNormallocation[1]) {
                        layoutParams.topMargin = this.mNormallocation[1];
                    }
                    if (layoutParams.leftMargin != this.mNormallocation[0]) {
                        layoutParams.leftMargin = this.mNormallocation[0];
                    }
                    this.mDecorView.addView(this.containerView, layoutParams);
                    if (this.mMediaContext.getVideo().getVideoState() == 4) {
                        this.mVideoView.seekToWithoutNotify(getDuration(), false);
                    }
                }
                int difheight = 0;
                if (Build.VERSION.SDK_INT < 18) {
                    difheight = DWViewUtil.getStatusBarHeight(this.mMediaContext.getContext());
                }
                if (portrait) {
                    startPortraitFullAnimation(-this.mNormallocation[0], difheight - this.mNormallocation[1]);
                } else {
                    startLandscapeFullAnimation(((this.mFullHeight - this.mFullWidth) / 2) - this.mNormallocation[0], difheight + (((this.mFullWidth - this.mFullHeight) / 2) - this.mNormallocation[1]));
                }
            }
        }
    }

    private void startPortraitFullAnimation(int difwidth, int difheight) {
        if (this.mMediaContext != null && (this.mMediaContext.getContext() instanceof Activity)) {
            ObjectAnimator oaNX = ObjectAnimator.ofFloat(this.containerView, "translationX", new float[]{(float) difwidth});
            ObjectAnimator oaNY = ObjectAnimator.ofFloat(this.containerView, "translationY", new float[]{(float) difheight});
            ValueAnimator vaN = ValueAnimator.ofFloat(new float[]{0.0f, 90.0f});
            this.mNormaltoFullSet = new AnimatorSet();
            this.mNormaltoFullSet.setDuration((long) 150);
            this.mNormaltoFullSet.play(vaN);
            this.mNormaltoFullSet.play(oaNX);
            this.mNormaltoFullSet.play(oaNY);
            this.mHandler.post(new Runnable() {
                public void run() {
                    MediaPlayViewController.this.mNormaltoFullSet.start();
                }
            });
            vaN.setDuration((long) 150);
            vaN.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = ((Float) animation.getAnimatedValue()).floatValue();
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) MediaPlayViewController.this.containerView.getLayoutParams();
                    layoutParams.width = (int) (((float) MediaPlayViewController.this.mWidth) + ((((float) (MediaPlayViewController.this.mFullWidth - MediaPlayViewController.this.mWidth)) * value) / 90.0f));
                    layoutParams.height = (int) (((float) MediaPlayViewController.this.mHeight) + ((((float) (MediaPlayViewController.this.mFullHeight - MediaPlayViewController.this.mHeight)) * value) / 90.0f));
                    MediaPlayViewController.this.containerView.requestLayout();
                    if (value > 20.0f && Build.VERSION.SDK_INT == 18 && !MediaPlayViewController.this.statusBarHide) {
                        ((Activity) MediaPlayViewController.this.mMediaContext.getContext()).getWindow().setFlags(1024, 1024);
                        boolean unused = MediaPlayViewController.this.statusBarHide = true;
                    }
                }
            });
            vaN.addListener(new Animator.AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    MediaPlayViewController.this.startPortraitFullEnd();
                }

                public void onAnimationCancel(Animator animation) {
                    MediaPlayViewController.this.startPortraitFullEnd();
                }

                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void startPortraitFullEnd() {
        this.mHandler.post(new Runnable() {
            public void run() {
                MediaPlayViewController.this.containerView.requestLayout();
                MediaPlayViewController.this.mMediaContext.setVideoScreenType(MediaPlayScreenType.PORTRAIT_FULL_SCREEN);
                MediaPlayViewController.this.mVideoView.onVideoScreenChanged(MediaPlayScreenType.PORTRAIT_FULL_SCREEN);
                if (MediaPlayViewController.this.mMediaContext != null && !MediaPlayViewController.this.mMediaContext.mHookKeyBackToggleEvent) {
                    MediaPlayViewController.this.mMediaContext.registerKeyBackEventListener(MediaPlayViewController.this);
                }
                MediaPlayViewController.this.containerView.setLayerType(0, (Paint) null);
                boolean unused = MediaPlayViewController.this.mAnimationRunning = false;
            }
        });
    }

    private void startLandscapeFullAnimation(int difwidth, int difheight) {
        if (this.mMediaContext != null && (this.mMediaContext.getContext() instanceof Activity)) {
            ObjectAnimator oaNX = ObjectAnimator.ofFloat(this.containerView, "translationX", new float[]{(float) difwidth});
            ObjectAnimator oaNY = ObjectAnimator.ofFloat(this.containerView, "translationY", new float[]{(float) difheight});
            ObjectAnimator oaF = ObjectAnimator.ofFloat(this.containerView, "rotation", new float[]{0.0f, 90.0f});
            this.mNormaltoFullSet = new AnimatorSet();
            this.mNormaltoFullSet.setDuration((long) 150);
            this.mNormaltoFullSet.play(oaF);
            this.mNormaltoFullSet.play(oaNX);
            this.mNormaltoFullSet.play(oaNY);
            this.mHandler.post(new Runnable() {
                public void run() {
                    MediaPlayViewController.this.mNormaltoFullSet.start();
                }
            });
            oaF.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = Math.abs(((Float) animation.getAnimatedValue()).floatValue());
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
                    layoutParams.width = (int) (((float) MediaPlayViewController.this.mWidth) + ((((float) (MediaPlayViewController.this.mFullWidth - MediaPlayViewController.this.mWidth)) * value) / 90.0f));
                    layoutParams.height = (int) (((float) MediaPlayViewController.this.mHeight) + ((((float) (MediaPlayViewController.this.mFullHeight - MediaPlayViewController.this.mHeight)) * value) / 90.0f));
                    layoutParams.topMargin = MediaPlayViewController.this.mNormallocation[1];
                    layoutParams.leftMargin = MediaPlayViewController.this.mNormallocation[0];
                    MediaPlayViewController.this.containerView.setLayoutParams(layoutParams);
                    if (value > 20.0f && Build.VERSION.SDK_INT == 18 && !MediaPlayViewController.this.statusBarHide) {
                        ((Activity) MediaPlayViewController.this.mMediaContext.getContext()).getWindow().setFlags(1024, 1024);
                        boolean unused = MediaPlayViewController.this.statusBarHide = true;
                    }
                }
            });
            this.mNormaltoFullSet.addListener(new Animator.AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    MediaPlayViewController.this.startLandscapeFullEnd();
                }

                public void onAnimationCancel(Animator animation) {
                    MediaPlayViewController.this.startLandscapeFullEnd();
                }

                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void startLandscapeFullEnd() {
        this.mHandler.post(new Runnable() {
            public void run() {
                MediaPlayViewController.this.containerView.requestLayout();
                MediaPlayViewController.this.mMediaContext.setVideoScreenType(MediaPlayScreenType.LANDSCAPE_FULL_SCREEN);
                MediaPlayViewController.this.mVideoView.onVideoScreenChanged(MediaPlayScreenType.LANDSCAPE_FULL_SCREEN);
                if (MediaPlayViewController.this.mMediaContext != null && !MediaPlayViewController.this.mMediaContext.mHookKeyBackToggleEvent) {
                    MediaPlayViewController.this.mMediaContext.registerKeyBackEventListener(MediaPlayViewController.this);
                }
                MediaPlayViewController.this.containerView.setLayerType(0, (Paint) null);
                boolean unused = MediaPlayViewController.this.mAnimationRunning = false;
            }
        });
    }

    public void videoPlayError() {
        if (this.mVideoView != null) {
            this.mVideoView.videoPlayError();
        }
    }

    public void onMediaStart() {
        volumeFadeIn();
    }

    public void onMediaPause(boolean auto) {
    }

    public void onMediaPlay() {
        volumeFadeIn();
    }

    public void onMediaSeekTo(int currentPosition) {
    }

    public void onMediaPrepared(IMediaPlayer mp) {
    }

    public void onMediaError(IMediaPlayer mp, int what, int extra) {
    }

    public void onMediaInfo(IMediaPlayer mp, long what, long extra, long ext, Object obj) {
        if (3 == what) {
            Log.d("TAG", " sdadsa MEDIA_INFO_VIDEO_RENDERING_START START ");
        }
    }

    public void onMediaComplete() {
    }

    public void onMediaClose() {
    }

    public void onMediaScreenChanged(MediaPlayScreenType type) {
    }

    public void onMediaProgressChanged(int currentPosition, int bufferPercent, int total) {
    }

    public boolean onBackKeyDown(KeyEvent event) {
        if (this.mMediaContext.screenType() != MediaPlayScreenType.LANDSCAPE_FULL_SCREEN && this.mMediaContext.screenType() != MediaPlayScreenType.PORTRAIT_FULL_SCREEN) {
            return false;
        }
        toggleScreen();
        return true;
    }

    public boolean isPlaying() {
        return this.mVideoView.isPlaying();
    }

    public boolean isInPlaybackState() {
        return this.mVideoView.isInPlaybackState();
    }

    public void destroy() {
        this.mVideoView.destroy();
    }

    public void setMediaSourceType(String mediaSourceType) {
        this.mVideoView.setMediaSourceType(mediaSourceType);
    }

    public void setMediaId(String mediaId) {
        this.mVideoView.setMediaId(mediaId);
    }

    public void setAccountId(String accountId) {
        this.mVideoView.setAccountId(accountId);
    }

    public void setSurfaceListener(TaoLiveVideoView.SurfaceListener listener) {
        if (this.mVideoView != null) {
            this.mVideoView.setSurfaceListener(listener);
        }
    }

    MediaPlayViewController(MediaContext context) {
        this.mMediaContext = context;
        if (!TextUtils.isEmpty(this.mMediaContext.getVideoToken())) {
            this.mVideoView = new TextureVideoView(this.mMediaContext, this.mMediaContext.getVideoToken());
        } else {
            this.mVideoView = new TextureVideoView(this.mMediaContext);
        }
        this.mVideoView.setLooping(context.mLoop);
        this.mVideoView.registerIMediaLifecycleListener(this);
    }

    /* access modifiers changed from: package-private */
    public View getView() {
        return this.mVideoView.getView();
    }

    /* access modifiers changed from: package-private */
    public BaseVideoView getBaseVideoView() {
        return this.mVideoView;
    }

    public void startVideo() {
        if (this.mVideoView.getVideoState() == 1 && TextUtils.isEmpty(this.mMediaContext.getVideoToken())) {
            return;
        }
        if (TextUtils.isEmpty(this.mMediaContext.getVideoToken()) || this.mVideoView.getVideoState() != 1) {
            startVideoInner();
        }
    }

    /* access modifiers changed from: package-private */
    public void startVideoInner() {
        if (this.mVideoView.getVideoState() == 5 || this.mVideoView.getVideoState() == 8 || !TextUtils.isEmpty(this.mMediaContext.getVideoToken())) {
            this.mVideoView.startVideo();
        } else if (this.mVideoView.getVideoState() == 4 || (this.mVideoView.isRecycled() && this.mVideoView.getStatebfRelease() == 4)) {
            if (this.mVideoView.isRecycled()) {
                this.mVideoView.setLastPosition(0);
            } else {
                this.mVideoView.seekTo(0);
            }
            playVideo();
        } else if (this.mVideoView.getVideoState() == 2) {
            playVideo();
        } else {
            this.mVideoView.startVideo();
        }
    }

    /* access modifiers changed from: package-private */
    public void setVideoSource(String url) {
        if (TextUtils.isEmpty(url)) {
            if (this.mMediaContext != null) {
                DWLogUtils.e(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "setVideoSource## sorry  VideoSource is Empty  ");
            }
        } else if (this.mMediaContext.mMediaPlayContext.mLocalVideo) {
            this.mVideoView.setVideoPath(url);
        } else {
            if (url.startsWith(WVUtils.URL_SEPARATOR)) {
                url = "http:" + url;
            }
            this.mVideoView.setMonitorData(this.mMediaContext.getUTParams());
            this.mVideoView.setVideoPath(url);
        }
    }

    public void mute(boolean mute) {
        this.mHandler.removeCallbacks(this.mVolumeRun);
        if (mute) {
            this.mVolumeFadeIn = true;
            this.mVideoView.setVolume(0.0f);
            this.mMute = mute;
            return;
        }
        this.mVolume = BaseVideoView.VOLUME_MULTIPLIER;
        this.mMute = mute;
        if (this.mVolumeFadeIn) {
            this.mVideoView.setVolume(this.mVolume * 0.2f);
            if (this.mVideoView.getVideoState() == 1) {
                volumeFadeIn();
                return;
            }
            return;
        }
        this.mVideoView.setVolume(this.mVolume);
    }

    public int getSurfaceWidth() {
        return this.mVideoView.getSurfaceWidth();
    }

    public int getSurfaceHeight() {
        return this.mVideoView.getSurfaceHeight();
    }

    public void setVolume(float volume) {
        this.mHandler.removeCallbacks(this.mVolumeRun);
        this.mVolume = volume;
        if (!this.mVolumeFadeIn || volume == 0.0f) {
            this.mVideoView.setVolume(volume);
        } else {
            this.mVideoView.setVolume(0.2f * volume);
        }
    }

    public void setSysVolume(float volume) {
        this.mVideoView.setSysVolume(volume);
    }

    public float getSysVolume() {
        return this.mVideoView.getSysVolume();
    }

    public void playVideo() {
        this.mVideoView.playVideo();
    }

    public void pauseVideo() {
        this.mVideoView.pauseVideo(false);
    }

    public void asyncPrepareVideo() {
        this.mVideoView.asyncPrepare();
    }

    public void seekTo(int progress) {
        this.mVideoView.seekTo(progress);
    }

    public void instantSeekTo(int progress) {
        this.mVideoView.instantSeekTo(progress);
    }

    public void closeVideo() {
        this.mVideoView.closeVideo();
    }

    public void setPlayRate(float playRate) {
        this.mVideoView.setPlayRate(playRate);
    }

    public int getDuration() {
        return this.mVideoView.getDuration();
    }

    public int getCurrentPosition() {
        return this.mVideoView.getCurrentPosition();
    }

    public int getBufferPercentage() {
        return this.mVideoView.getVideoBufferPercent();
    }

    public int getVideoState() {
        return this.mVideoView.getVideoState();
    }

    public int getVideoState2() {
        if (this.mVideoView.isRecycled()) {
            return this.mVideoView.getStatebfRelease();
        }
        return this.mVideoView.getVideoState();
    }

    public void retryVideo() {
        if (this.mVideoView.getVideoState() == 3 || ((TextUtils.isEmpty(this.mMediaContext.mMediaPlayContext.getVideoUrl()) && TextUtils.isEmpty(this.mMediaContext.getVideoToken())) || (this.mVideoView.isRecycled() && this.mVideoView.getStatebfRelease() == 3))) {
            if (this.mRetryListener != null) {
                this.mRetryListener.retry();
            }
            this.mVideoView.startVideo();
            if (this.mMute) {
                this.mVideoView.setVolume(0.0f);
            }
        }
    }

    public void enableVideoClickDetect(boolean enableVideoDetect) {
        this.mVideoView.enableVideoClickDetect(enableVideoDetect);
    }

    public void blockTouchEvent(boolean block) {
        this.mVideoView.blockTouchEvent(block);
    }

    public void registerIMediaLifecycleListener(IMediaPlayLifecycleListener listener) {
        this.mVideoView.registerIMediaLifecycleListener(listener);
    }

    public void registerIMediaLoopCompleteListener(IMediaLoopCompleteListener listener) {
        this.mVideoView.registerIVideoLoopCompleteListener(listener);
    }

    public void setMediaAspectRatio(MediaAspectRatio aspectRatio) {
        if (this.mVideoView != null) {
            this.mVideoView.setAspectRatio(aspectRatio);
        }
    }

    public void registerOnVideoClickListener(IMediaPlayer.OnVideoClickListener listener) {
        if (this.mVideoView != null) {
            this.mVideoView.registerOnVideoClickListener(listener);
        }
    }

    public void unregisterOnVideoClickListener(IMediaPlayer.OnVideoClickListener listener) {
        if (this.mVideoView != null) {
            this.mVideoView.unregisterOnVideoClickListener(listener);
        }
    }

    public void setPropertyLong(int property, long value) {
        if (this.mVideoView != null) {
            this.mVideoView.setPropertyLong(property, value);
        }
    }

    public void setPropertyFloat(int property, float value) {
        if (this.mVideoView != null) {
            this.mVideoView.setPropertyFloat(property, value);
        }
    }

    private void volumeFadeIn() {
        if (!this.mMute && this.mVolume != 0.0f && this.mVolumeFadeIn) {
            this.mVolumeFadeIn = false;
            this.mFadeInCount = 0;
            if (this.mVolumeRun == null) {
                this.mVolumeRun = new Runnable() {
                    public void run() {
                        MediaPlayViewController.access$1808(MediaPlayViewController.this);
                        MediaPlayViewController.this.mVideoView.setVolume(MediaPlayViewController.this.mVolume * ((((float) MediaPlayViewController.this.mFadeInCount) * 0.2f) + 0.2f));
                        if (MediaPlayViewController.this.mFadeInCount < 4) {
                            MediaPlayViewController.this.mHandler.postDelayed(MediaPlayViewController.this.mVolumeRun, 500);
                        }
                    }
                };
            }
            this.mHandler.postDelayed(this.mVolumeRun, 500);
        }
    }

    public void setRetryListener(RetryListener retryListener) {
        this.mRetryListener = retryListener;
    }

    public void release() {
        this.mVideoView.close();
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
    }
}
