package com.taobao.taobaoavsdk.widget.extra;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.taobao.windvane.jsbridge.api.BlowSensor;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.taobao.media.DWViewUtil;
import com.taobao.taobaoavsdk.R;
import com.taobao.taobaoavsdk.widget.extra.KeyBackController;
import com.taobao.taobaoavsdk.widget.media.TaoLiveVideoView;
import com.taobao.taobaoavsdk.widget.media.TaoLiveVideoViewConfig;
import java.util.Locale;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class PlayerController implements SeekBar.OnSeekBarChangeListener, Handler.Callback, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnPreparedListener, TaoLiveVideoView.OnStartListener, TaoLiveVideoView.OnPauseListener, KeyBackController.OnBackKeyListener {
    public static final int MSG_UPDATE_SEEKBAR_PROGRESS = 1;
    public static final int SHOW_ALL = 1;
    public static final int SHOW_FULL_SCREEN_BTN = 3;
    public static final int SHOW_NONE = 4;
    public static final int SHOW_PLAY_CONTROLLER = 2;
    private static final String TAG = "PlayerController";
    public static final int UPDATE_PROGRESS_TIME = 700;
    boolean mAnimationRunning;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public ControllerHolder mControllerHolder;
    FrameLayout mDecorView;
    private View mDefaultControllerView;
    int mFullHeight;
    int mFullWidth;
    AnimatorSet mFulltoNormalSet;
    private Handler mHandler;
    int mHeight;
    int mIndex;
    private boolean mIsDefaultController = false;
    /* access modifiers changed from: private */
    public boolean mIsFullScreen = false;
    private boolean mIsSeekBarOnChange = false;
    /* access modifiers changed from: private */
    public KeyBackController mKeyBackController;
    ViewGroup.LayoutParams mLayoutParams;
    int[] mNormallocation = new int[2];
    AnimatorSet mNormaltoFullSet;
    private PlayProgressListener mPlayProgressListener;
    private SeekStopTrackingListener mSeekStopTrackingListener;
    private int mShowType = 1;
    private ToggleScreenListener mToggleScreenListener;
    /* access modifiers changed from: private */
    public TaoLiveVideoView mVideoView;
    int mWidth;
    private int newPosition = 0;
    ViewGroup rootView;
    boolean statusBarHide;
    float translationX;
    float translationY;

    public interface PlayProgressListener {
        void onPlayProgress(int i);
    }

    public interface SeekStopTrackingListener {
        void onStopTrackingTouch(boolean z);
    }

    public interface ToggleScreenListener {
        boolean toFullScreen();

        boolean toNormalScreen();
    }

    public boolean onBackKeyDown(KeyEvent event) {
        if (!this.mIsFullScreen) {
            return false;
        }
        toggleScreen(false);
        return true;
    }

    public PlayerController(Context context, @NonNull TaoLiveVideoView videoView) {
        this.mContext = context;
        this.mVideoView = videoView;
        this.mVideoView.registerOnCompletionListener(this);
        this.mVideoView.registerOnErrorListener(this);
        this.mVideoView.registerOnPreparedListener(this);
        this.mVideoView.registerOnStartListener(this);
        this.mVideoView.registerOnPauseListener(this);
        if (context instanceof Activity) {
            this.mKeyBackController = new KeyBackController((Activity) context);
        }
    }

    public void setControllerHolder(ControllerHolder holder) {
        if (holder != null) {
            removeControllerView();
            this.mControllerHolder = holder;
            this.mIsDefaultController = false;
            init();
        }
    }

    private void init() {
        if (this.mControllerHolder.playOrPauseButton != null) {
            this.mControllerHolder.playOrPauseButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (PlayerController.this.mVideoView.isPlaying()) {
                        PlayerController.this.mVideoView.pause();
                        PlayerController.this.mControllerHolder.playOrPauseButton.setImageResource(PlayerController.this.mControllerHolder.startResId);
                        return;
                    }
                    PlayerController.this.mVideoView.start();
                    PlayerController.this.mControllerHolder.playOrPauseButton.setImageResource(PlayerController.this.mControllerHolder.pauseResId);
                }
            });
            if (this.mVideoView.isPlaying()) {
                this.mControllerHolder.playOrPauseButton.setImageResource(this.mControllerHolder.pauseResId);
            } else {
                this.mControllerHolder.playOrPauseButton.setImageResource(this.mControllerHolder.startResId);
            }
        }
        if (this.mControllerHolder.toggleScreenButton != null) {
            this.mControllerHolder.toggleScreenButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    PlayerController.this.toggleScreen(false);
                }
            });
        }
        if (this.mControllerHolder.seekBar != null) {
            this.mControllerHolder.seekBar.setOnSeekBarChangeListener(this);
            this.mControllerHolder.seekBar.setMax(1000);
        }
        if (this.mControllerHolder.currentTimeTv != null) {
            this.mControllerHolder.currentTimeTv.setText(this.mContext.getString(R.string.avsdk_defaulttime));
        }
        if (this.mControllerHolder.totalTimeTv != null) {
            this.mControllerHolder.totalTimeTv.setText(this.mContext.getString(R.string.avsdk_defaulttime));
        }
        watchTimer();
        showController();
    }

    public void setToggleScreenListener(ToggleScreenListener listener) {
        this.mToggleScreenListener = listener;
    }

    public void setSeekStopTrackingListener(SeekStopTrackingListener listener) {
        this.mSeekStopTrackingListener = listener;
    }

    public void setPlayProgressListener(PlayProgressListener listener) {
        this.mPlayProgressListener = listener;
    }

    public void setDefaultControllerHolder() {
        if (!this.mIsDefaultController) {
            this.mDefaultControllerView = LayoutInflater.from(this.mContext.getApplicationContext()).inflate(R.layout.avsdk_video_bottom_controller, (ViewGroup) null, false);
            this.mControllerHolder = new ControllerHolder();
            this.mControllerHolder.controllerLayout = this.mDefaultControllerView.findViewById(R.id.video_controller_layout);
            this.mControllerHolder.controllerPlayLayout = this.mDefaultControllerView.findViewById(R.id.video_controller_play_layout);
            this.mControllerHolder.playOrPauseButton = (ImageView) this.mDefaultControllerView.findViewById(R.id.video_controller_play_btn);
            this.mControllerHolder.currentTimeTv = (TextView) this.mDefaultControllerView.findViewById(R.id.video_controller_current_time);
            this.mControllerHolder.totalTimeTv = (TextView) this.mDefaultControllerView.findViewById(R.id.video_controller_total_time);
            this.mControllerHolder.seekBar = (SeekBar) this.mDefaultControllerView.findViewById(R.id.video_controller_seekBar);
            this.mControllerHolder.toggleScreenButton = (ImageView) this.mDefaultControllerView.findViewById(R.id.video_controller_fullscreen);
            this.mControllerHolder.mPlayRateView = (TextView) this.mDefaultControllerView.findViewById(R.id.video_controller_playrate_icon);
            if (this.mControllerHolder.mPlayRateView != null) {
                this.mControllerHolder.mPlayRateView.setVisibility(8);
            }
            this.mControllerHolder.pauseResId = R.drawable.avsdk_video_btn_pause;
            this.mControllerHolder.startResId = R.drawable.avsdk_video_btn_start;
            this.mControllerHolder.fullscreenResId = R.drawable.avsdk_video_fullscreen;
            this.mControllerHolder.unFullscreenResId = R.drawable.avsdk_video_unfullscreen;
            this.mVideoView.addView(this.mDefaultControllerView, new FrameLayout.LayoutParams(-1, -1));
            this.mIsDefaultController = true;
            init();
        }
    }

    public boolean isVisible() {
        if (this.mControllerHolder == null || this.mControllerHolder.controllerLayout == null || this.mControllerHolder.controllerLayout.getVisibility() != 0) {
            return false;
        }
        return true;
    }

    public void removeControllerView() {
        if (this.mIsDefaultController && this.mDefaultControllerView != null) {
            this.mVideoView.removeView(this.mDefaultControllerView);
        }
    }

    public void addControllerView() {
        if (this.mIsDefaultController && this.mDefaultControllerView != null && this.mDefaultControllerView.getParent() == null) {
            this.mVideoView.addView(this.mDefaultControllerView, new FrameLayout.LayoutParams(-1, -1));
        }
    }

    public void setFullScreen(boolean isFullScreen) {
        this.mIsFullScreen = isFullScreen;
    }

    public void refreshController() {
        if (this.mControllerHolder != null) {
            if (this.mVideoView.isPlaying()) {
                if (this.mControllerHolder.playOrPauseButton != null) {
                    this.mControllerHolder.playOrPauseButton.setImageResource(this.mControllerHolder.pauseResId);
                }
            } else if (this.mControllerHolder.playOrPauseButton != null) {
                this.mControllerHolder.playOrPauseButton.setImageResource(this.mControllerHolder.startResId);
            }
            if (this.mIsFullScreen) {
                if (this.mControllerHolder.toggleScreenButton != null) {
                    this.mControllerHolder.toggleScreenButton.setImageResource(this.mControllerHolder.unFullscreenResId);
                }
            } else if (this.mControllerHolder.toggleScreenButton != null) {
                this.mControllerHolder.toggleScreenButton.setImageResource(this.mControllerHolder.fullscreenResId);
            }
        }
    }

    public void showController() {
        if (this.mControllerHolder != null && this.mControllerHolder.controllerLayout != null) {
            this.mControllerHolder.controllerLayout.setVisibility(0);
        }
    }

    public void showController(int type) {
        if (this.mControllerHolder != null) {
            if (this.mControllerHolder.controllerLayout != null) {
                this.mControllerHolder.controllerLayout.setVisibility(0);
            }
            this.mShowType = type;
            switch (type) {
                case 1:
                    if (this.mControllerHolder.controllerPlayLayout != null) {
                        this.mControllerHolder.controllerPlayLayout.setVisibility(0);
                    }
                    if (this.mControllerHolder.toggleScreenButton != null) {
                        this.mControllerHolder.toggleScreenButton.setVisibility(0);
                    }
                    if (this.mIsDefaultController && this.mControllerHolder.controllerLayout != null) {
                        this.mControllerHolder.controllerLayout.setBackgroundResource(R.drawable.avsdk_video_play_bg);
                        return;
                    }
                    return;
                case 2:
                    if (this.mControllerHolder.controllerPlayLayout != null) {
                        this.mControllerHolder.controllerPlayLayout.setVisibility(0);
                    }
                    if (this.mControllerHolder.toggleScreenButton != null) {
                        this.mControllerHolder.toggleScreenButton.setVisibility(8);
                    }
                    if (this.mIsDefaultController && this.mControllerHolder.controllerLayout != null) {
                        this.mControllerHolder.controllerLayout.setBackgroundResource(R.drawable.avsdk_video_play_bg);
                        return;
                    }
                    return;
                case 3:
                    if (this.mControllerHolder.controllerPlayLayout != null) {
                        this.mControllerHolder.controllerPlayLayout.setVisibility(4);
                    }
                    if (this.mControllerHolder.toggleScreenButton != null) {
                        this.mControllerHolder.toggleScreenButton.setVisibility(0);
                    }
                    if (this.mIsDefaultController && this.mControllerHolder.controllerLayout != null) {
                        this.mControllerHolder.controllerLayout.setBackgroundResource(0);
                        return;
                    }
                    return;
                case 4:
                    if (this.mControllerHolder.controllerPlayLayout != null) {
                        this.mControllerHolder.controllerPlayLayout.setVisibility(4);
                    }
                    if (this.mControllerHolder.toggleScreenButton != null) {
                        this.mControllerHolder.toggleScreenButton.setVisibility(8);
                    }
                    if (this.mIsDefaultController && this.mControllerHolder.controllerLayout != null) {
                        this.mControllerHolder.controllerLayout.setBackgroundResource(0);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void hideController() {
        if (this.mControllerHolder != null && this.mControllerHolder.controllerLayout != null) {
            this.mControllerHolder.controllerLayout.setVisibility(8);
        }
    }

    private void updateSeekBarProgress() {
        if (this.mControllerHolder != null && this.mVideoView != null && this.mHandler != null) {
            int currentPosition = this.mVideoView.getCurrentPosition();
            if (!this.mIsSeekBarOnChange && currentPosition != this.newPosition) {
                this.newPosition = currentPosition;
                int duration = this.mVideoView.getDuration();
                int progress = 0;
                int secondaryProgress = 0;
                if (duration > 0) {
                    progress = (int) Math.ceil((double) (1000.0f * ((1.0f * ((float) currentPosition)) / ((float) duration))));
                    secondaryProgress = this.mVideoView.getBufferPercentage();
                }
                if (this.mControllerHolder.totalTimeTv != null) {
                    this.mControllerHolder.totalTimeTv.setText(stringForTime(duration));
                }
                if (this.mControllerHolder.currentTimeTv != null) {
                    this.mControllerHolder.currentTimeTv.setText(stringForTime(currentPosition));
                }
                if (this.mControllerHolder.seekBar != null) {
                    this.mControllerHolder.seekBar.setProgress(progress);
                    this.mControllerHolder.seekBar.setSecondaryProgress(secondaryProgress * 10);
                }
                if (this.mPlayProgressListener != null) {
                    this.mPlayProgressListener.onPlayProgress(currentPosition);
                }
            }
            this.mHandler.sendEmptyMessageDelayed(1, 700);
        }
    }

    public void toggleScreen(boolean bOnlyChangeIcon) {
        if (this.mControllerHolder != null) {
            if (this.mIsFullScreen) {
                this.mIsFullScreen = false;
                if (this.mControllerHolder.toggleScreenButton != null) {
                    this.mControllerHolder.toggleScreenButton.setImageResource(this.mControllerHolder.fullscreenResId);
                }
                if (bOnlyChangeIcon) {
                    return;
                }
                if (this.mToggleScreenListener == null || !this.mToggleScreenListener.toNormalScreen()) {
                    toNormalScreen();
                    return;
                }
                return;
            }
            this.mIsFullScreen = true;
            if (this.mControllerHolder.toggleScreenButton != null) {
                this.mControllerHolder.toggleScreenButton.setImageResource(this.mControllerHolder.unFullscreenResId);
            }
            if (bOnlyChangeIcon) {
                return;
            }
            if (this.mToggleScreenListener == null || !this.mToggleScreenListener.toFullScreen()) {
                toFullScreen();
            }
        }
    }

    public void onStartTrackingTouch(SeekBar bar) {
    }

    public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
        if (fromUser) {
            this.mIsSeekBarOnChange = true;
            this.newPosition = (int) (((float) this.mVideoView.getDuration()) * (((float) progress) / 1000.0f));
            if (this.mControllerHolder.currentTimeTv != null) {
                this.mControllerHolder.currentTimeTv.setText(stringForTime(this.newPosition));
            }
        }
    }

    public void onStopTrackingTouch(SeekBar bar) {
        int duration = this.mVideoView.getDuration();
        if (duration <= 0 || this.newPosition < duration) {
            this.mVideoView.seekTo(this.newPosition);
        } else {
            this.mVideoView.onCompletion();
            this.mVideoView.release();
        }
        if (this.mSeekStopTrackingListener != null) {
            this.mSeekStopTrackingListener.onStopTrackingTouch(this.mIsSeekBarOnChange);
        }
        this.mIsSeekBarOnChange = false;
    }

    public void onCompletion(IMediaPlayer mp) {
        resetViewState();
    }

    public boolean onError(IMediaPlayer mp, int what, int extra) {
        resetViewState();
        return false;
    }

    public void onPrepared(IMediaPlayer mp) {
        if (this.mControllerHolder != null) {
            resetViewState();
            int duration = this.mVideoView.getDuration();
            if (duration >= 0 && this.mControllerHolder.totalTimeTv != null) {
                this.mControllerHolder.totalTimeTv.setText(stringForTime(duration));
            }
        }
    }

    public void onStart(IMediaPlayer mp) {
        if (this.mControllerHolder != null) {
            if (this.mControllerHolder.playOrPauseButton != null && (this.mContext instanceof Activity)) {
                ((Activity) this.mContext).runOnUiThread(new Runnable() {
                    public void run() {
                        PlayerController.this.mControllerHolder.playOrPauseButton.setImageResource(PlayerController.this.mControllerHolder.pauseResId);
                    }
                });
            }
            watchTimer();
        }
    }

    public void onPause(IMediaPlayer mp) {
        if (this.mControllerHolder != null) {
            if (this.mControllerHolder.playOrPauseButton != null && (this.mContext instanceof Activity)) {
                ((Activity) this.mContext).runOnUiThread(new Runnable() {
                    public void run() {
                        PlayerController.this.mControllerHolder.playOrPauseButton.setImageResource(PlayerController.this.mControllerHolder.startResId);
                    }
                });
            }
            stopTimer();
        }
    }

    public void resetViewState() {
        if (this.mControllerHolder != null) {
            stopTimer();
            this.newPosition = 0;
            if (this.mControllerHolder.playOrPauseButton != null) {
                this.mControllerHolder.playOrPauseButton.setImageResource(this.mControllerHolder.startResId);
            }
            if (this.mControllerHolder.currentTimeTv != null) {
                this.mControllerHolder.currentTimeTv.setText(stringForTime(0));
            }
            if (this.mControllerHolder.seekBar != null) {
                this.mControllerHolder.seekBar.setProgress(0);
                this.mControllerHolder.seekBar.setSecondaryProgress(0);
            }
        }
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                updateSeekBarProgress();
                return false;
            default:
                return false;
        }
    }

    public void destroy() {
        stopTimer();
        this.mVideoView.unregisterOnCompletionListener(this);
        this.mVideoView.unregisterOnErrorListener(this);
        this.mVideoView.unregisterOnPreparedListener(this);
        this.mVideoView.unregisterOnStartListener(this);
        this.mVideoView.unregisterOnPauseListener(this);
        if (this.mKeyBackController != null) {
            this.mKeyBackController.unregisterKeyBackEventListener(this);
            this.mKeyBackController = null;
        }
        if (this.mControllerHolder != null) {
            if (!this.mIsDefaultController || this.mDefaultControllerView == null) {
                hideController();
            } else {
                this.mVideoView.removeView(this.mDefaultControllerView);
            }
        }
    }

    private void stopTimer() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
            this.mHandler = null;
        }
    }

    private void watchTimer() {
        if (this.mHandler == null) {
            this.mHandler = new Handler(this);
            this.mHandler.sendEmptyMessageDelayed(1, 700);
        }
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        if (hours > 0) {
            return String.format(Locale.getDefault(), "%02d:%02d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)});
        }
        return String.format(Locale.getDefault(), "%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)});
    }

    private void toFullScreen() {
        TaoLiveVideoViewConfig config;
        this.statusBarHide = false;
        if (this.mVideoView.getParent() != null && (this.mContext instanceof Activity) && !this.mAnimationRunning && (config = this.mVideoView.getConfig()) != null && config.mRenderType != 1) {
            this.mAnimationRunning = true;
            if (this.rootView == null) {
                this.rootView = (ViewGroup) this.mVideoView.getParent();
            }
            this.mIndex = this.rootView.indexOfChild(this.mVideoView);
            if (this.mLayoutParams == null) {
                this.mLayoutParams = this.mVideoView.getLayoutParams();
            }
            this.mNormallocation = new int[2];
            this.mVideoView.getLocationInWindow(this.mNormallocation);
            this.translationX = this.mVideoView.getTranslationX();
            this.translationY = this.mVideoView.getTranslationY();
            if (this.mDecorView == null) {
                this.mDecorView = (FrameLayout) ((Activity) this.mContext).getWindow().getDecorView();
            }
            setSystemBarsVisibility(this.mDecorView, false);
            this.mFullHeight = this.mFullHeight == 0 ? getRealWidthInPx(this.mContext) : this.mFullHeight;
            this.mFullWidth = getVideoWidthInLandscape((Activity) this.mContext);
            this.mWidth = this.mVideoView.getWidth();
            this.mHeight = this.mVideoView.getHeight();
            if (this.mVideoView.getParent() != this.mDecorView) {
                this.rootView.removeView(this.mVideoView);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(this.mWidth, this.mHeight);
                layoutParams.gravity = 0;
                layoutParams.topMargin = this.mNormallocation[1];
                layoutParams.leftMargin = this.mNormallocation[0];
                this.mDecorView.addView(this.mVideoView, layoutParams);
            }
            int difwidth = ((this.mFullHeight - this.mFullWidth) / 2) - this.mNormallocation[0];
            int difheight = ((this.mFullWidth - this.mFullHeight) / 2) - this.mNormallocation[1];
            if (Build.VERSION.SDK_INT < 18) {
                difheight += getStatusBarHeight(this.mContext);
            }
            ObjectAnimator oaNX = ObjectAnimator.ofFloat(this.mVideoView, "translationX", new float[]{(float) difwidth});
            ObjectAnimator oaNY = ObjectAnimator.ofFloat(this.mVideoView, "translationY", new float[]{(float) difheight});
            ObjectAnimator oaF = ObjectAnimator.ofFloat(this.mVideoView, "rotation", new float[]{0.0f, 90.0f});
            this.mNormaltoFullSet = new AnimatorSet();
            this.mNormaltoFullSet.setDuration(300);
            this.mNormaltoFullSet.play(oaF);
            this.mNormaltoFullSet.play(oaNX);
            this.mNormaltoFullSet.play(oaNY);
            this.mNormaltoFullSet.start();
            oaF.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = ((Float) animation.getAnimatedValue()).floatValue();
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
                    layoutParams.width = (int) (((float) PlayerController.this.mWidth) + ((((float) (PlayerController.this.mFullWidth - PlayerController.this.mWidth)) * value) / 90.0f));
                    layoutParams.height = (int) (((float) PlayerController.this.mHeight) + ((((float) (PlayerController.this.mFullHeight - PlayerController.this.mHeight)) * value) / 90.0f));
                    layoutParams.topMargin = PlayerController.this.mNormallocation[1];
                    layoutParams.leftMargin = PlayerController.this.mNormallocation[0];
                    PlayerController.this.mVideoView.setLayoutParams(layoutParams);
                    if (value > 20.0f && Build.VERSION.SDK_INT == 18 && !PlayerController.this.statusBarHide) {
                        ((Activity) PlayerController.this.mContext).getWindow().setFlags(1024, 1024);
                        PlayerController.this.statusBarHide = true;
                    }
                }
            });
            this.mNormaltoFullSet.addListener(new Animator.AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    PlayerController.this.mAnimationRunning = false;
                    PlayerController.this.mVideoView.requestLayout();
                    boolean unused = PlayerController.this.mIsFullScreen = true;
                    if (PlayerController.this.mKeyBackController != null) {
                        PlayerController.this.mKeyBackController.registerKeyBackEventListener(PlayerController.this);
                    }
                }

                public void onAnimationCancel(Animator animation) {
                    PlayerController.this.mAnimationRunning = false;
                }

                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
    }

    private void toNormalScreen() {
        TaoLiveVideoViewConfig config;
        if (this.mVideoView.getParent() != null && (this.mContext instanceof Activity) && !this.mAnimationRunning && (config = this.mVideoView.getConfig()) != null && config.mRenderType != 1) {
            this.mAnimationRunning = true;
            if (this.rootView == null) {
                this.rootView = (ViewGroup) this.mVideoView.getParent();
            }
            this.mFullHeight = this.mFullHeight == 0 ? getRealWidthInPx(this.mContext) : this.mFullHeight;
            this.mFullWidth = getVideoWidthInLandscape((Activity) this.mContext);
            if (this.mDecorView == null) {
                this.mDecorView = (FrameLayout) ((Activity) this.mContext).getWindow().getDecorView();
            }
            setSystemBarsVisibility(this.mDecorView, true);
            if (Build.VERSION.SDK_INT == 18) {
                WindowManager.LayoutParams attrs = ((Activity) this.mContext).getWindow().getAttributes();
                attrs.flags &= -1025;
                ((Activity) this.mContext).getWindow().setAttributes(attrs);
                ((Activity) this.mContext).getWindow().clearFlags(512);
            }
            int difwidth = ((-(this.mFullHeight - this.mWidth)) / 2) + this.mNormallocation[0];
            int difheight = ((-(this.mFullWidth - this.mHeight)) / 2) + this.mNormallocation[1];
            if (Build.VERSION.SDK_INT < 18) {
                difheight -= getStatusBarHeight(this.mContext) / 2;
            }
            ObjectAnimator oaNX = ObjectAnimator.ofFloat(this.mVideoView, "translationX", new float[]{(float) difwidth});
            ObjectAnimator oaNY = ObjectAnimator.ofFloat(this.mVideoView, "translationY", new float[]{(float) difheight});
            this.mFulltoNormalSet = new AnimatorSet();
            ObjectAnimator oaN = ObjectAnimator.ofFloat(this.mVideoView, "rotation", new float[]{0.0f});
            oaN.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = ((Float) animation.getAnimatedValue()).floatValue();
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
                    layoutParams.width = (int) (((float) PlayerController.this.mWidth) + ((((float) (PlayerController.this.mFullWidth - PlayerController.this.mWidth)) * value) / 90.0f));
                    layoutParams.height = (int) (((float) PlayerController.this.mHeight) + ((((float) (PlayerController.this.mFullHeight - PlayerController.this.mHeight)) * value) / 90.0f));
                    layoutParams.gravity = 17;
                    PlayerController.this.mVideoView.setLayoutParams(layoutParams);
                }
            });
            this.mFulltoNormalSet.setDuration(300);
            this.mFulltoNormalSet.play(oaN);
            this.mFulltoNormalSet.play(oaNX);
            this.mFulltoNormalSet.play(oaNY);
            this.mFulltoNormalSet.start();
            this.mFulltoNormalSet.addListener(new Animator.AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    PlayerController.this.mAnimationRunning = false;
                    if (PlayerController.this.mLayoutParams == null) {
                        PlayerController.this.mLayoutParams = new FrameLayout.LayoutParams(PlayerController.this.mWidth, PlayerController.this.mHeight);
                        ((FrameLayout.LayoutParams) PlayerController.this.mLayoutParams).gravity = 17;
                    }
                    PlayerController.this.mDecorView.removeView(PlayerController.this.mVideoView);
                    PlayerController.this.rootView.addView(PlayerController.this.mVideoView, PlayerController.this.mIndex, PlayerController.this.mLayoutParams);
                    PlayerController.this.mVideoView.setTranslationX(PlayerController.this.translationX);
                    PlayerController.this.mVideoView.setTranslationY(PlayerController.this.translationY);
                    PlayerController.this.mVideoView.requestLayout();
                    boolean unused = PlayerController.this.mIsFullScreen = false;
                    if (PlayerController.this.mKeyBackController != null) {
                        PlayerController.this.mKeyBackController.unregisterKeyBackEventListener(PlayerController.this);
                    }
                }

                public void onAnimationCancel(Animator animation) {
                    PlayerController.this.mAnimationRunning = false;
                }

                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
    }

    public static int getRealWidthInPx(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            Class.forName("android.view.Display").getMethod("getRealMetrics", new Class[]{DisplayMetrics.class}).invoke(display, new Object[]{dm});
            return dm.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getVideoWidthInLandscape(Activity activity) {
        if (Build.VERSION.SDK_INT == 18) {
            return getScreenHeight(activity);
        }
        if (Build.VERSION.SDK_INT < 18) {
            return getScreenHeight(activity) - getStatusBarHeight(activity);
        }
        int width = getRealHeightInPx(activity);
        if (Build.VERSION.SDK_INT >= 26) {
            return width - DWViewUtil.getDisplayCutoutHeight(activity);
        }
        return width;
    }

    public static int getScreenHeight(Activity context) {
        return ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getHeight();
    }

    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", DispatchConstants.ANDROID);
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getRealHeightInPx(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            Class.forName("android.view.Display").getMethod("getRealMetrics", new Class[]{DisplayMetrics.class}).invoke(display, new Object[]{dm});
            return dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void setSystemBarsVisibility(View decorView, boolean visible) {
        if (Build.VERSION.SDK_INT > 18 && decorView != null) {
            int currentSystemUIVisibility = decorView.getSystemUiVisibility();
            int newSystemUIVisibility = visible ? 0 : BlowSensor.BLOW_HANDLER_FAIL;
            if (newSystemUIVisibility != currentSystemUIVisibility) {
                decorView.setSystemUiVisibility(newSystemUIVisibility);
            }
        }
    }
}
