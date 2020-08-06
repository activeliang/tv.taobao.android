package com.taobao.mediaplay.playercontrol;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.taobao.media.DWViewUtil;
import com.taobao.media.MediaSystemUtils;
import com.taobao.media.MediaTimeUtils;
import com.taobao.mediaplay.MediaContext;
import com.taobao.mediaplay.MediaPlayScreenType;
import com.taobao.mediaplay.player.IMediaPlayLifecycleListener;
import com.taobao.taobaoavsdk.R;
import com.taobao.taobaoavsdk.util.DWLogUtils;
import tv.danmaku.ijk.media.player.AbstractMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MediaPlayNormalController implements Handler.Callback, IMediaPlayLifecycleListener, SeekBar.OnSeekBarChangeListener {
    private static final int HIDE_DELAY_TIME = 4000;
    private static final int MSG_HIDE_CONTROLLER = 0;
    private static final String TAG = "MediaPlayController";
    int duration;
    public ControllerHolder mControllerHolder;
    private MediaContext mDWContext;
    private Handler mHandler;
    private boolean mHideControllerView = false;
    private FrameLayout mHost;
    /* access modifiers changed from: private */
    public IMediaPlayControlListener mMediaPlayControlListener;
    private INormalControllerListener mNormalControllerListener;
    private int mTotalTime;
    private int newPosition = 0;
    boolean startTracking;

    public interface INormalControllerListener {
        void hide();

        void show();
    }

    public MediaPlayNormalController(MediaContext context) {
        this.mDWContext = context;
        initView();
        this.mHandler = new Handler(this);
    }

    public void setIMediaPlayControlListener(IMediaPlayControlListener listener) {
        this.mMediaPlayControlListener = listener;
    }

    public View getView() {
        return this.mHost;
    }

    private void initView() {
        this.mHost = (FrameLayout) LayoutInflater.from(this.mDWContext.getContext()).inflate(R.layout.media_play_bottom_controller, (ViewGroup) null, false);
        this.mControllerHolder = new ControllerHolder();
        this.mControllerHolder.parentLayout = this.mHost;
        this.mControllerHolder.controllerLayout = this.mHost.findViewById(R.id.mediaplay_controller_layout);
        this.mControllerHolder.currentTimeTv = (TextView) this.mHost.findViewById(R.id.mediaplay_controller_current_time);
        this.mControllerHolder.totalTimeTv = (TextView) this.mHost.findViewById(R.id.mediaplay_controller_total_time);
        this.mControllerHolder.seekBar = (SeekBar) this.mHost.findViewById(R.id.mediaplay_controller_seekBar);
        this.mControllerHolder.toggleScreenButtonContainer = (FrameLayout) this.mHost.findViewById(R.id.video_controller_fullscreen);
        this.mControllerHolder.toggleScreenButton = new ImageView(this.mDWContext.getContext());
        int padding = DWViewUtil.dip2px(this.mDWContext.getContext(), 2.0f);
        this.mControllerHolder.toggleScreenButton.setPadding(padding, padding, padding, padding);
        this.mControllerHolder.toggleScreenButtonContainer.addView(this.mControllerHolder.toggleScreenButton, new FrameLayout.LayoutParams(-1, -1));
        this.mControllerHolder.toggleScreenButtonContainer.setVisibility(this.mDWContext.mNeedScreenButton ? 0 : 4);
        if (!this.mDWContext.mNeedScreenButton) {
            this.mControllerHolder.toggleScreenButtonContainer.getLayoutParams().width = DWViewUtil.dip2px(this.mDWContext.getContext(), 12.0f);
        }
        if (this.mControllerHolder.seekBar != null) {
            this.mControllerHolder.seekBar.setOnSeekBarChangeListener(this);
            this.mControllerHolder.seekBar.setMax(1000);
        }
        if (this.mDWContext.getVideo() != null) {
            this.duration = this.duration == 0 ? this.mDWContext.getVideo().getDuration() : this.duration;
            if (this.duration >= 0) {
                this.mControllerHolder.totalTimeTv.setText(MediaTimeUtils.msStringForTime(this.duration));
            }
        }
        this.mControllerHolder.fullscreenResId = R.drawable.mediaplay_sdk_fullscreen;
        this.mControllerHolder.unFullscreenResId = R.drawable.mediaplay_sdk_unfullscreen;
        this.mControllerHolder.toggleScreenButton.setImageResource(this.mControllerHolder.fullscreenResId);
        if (this.mControllerHolder.toggleScreenButton != null) {
            this.mControllerHolder.toggleScreenButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (MediaPlayNormalController.this.mMediaPlayControlListener != null) {
                        MediaPlayNormalController.this.mMediaPlayControlListener.screenButtonClick();
                    }
                }
            });
        }
    }

    public boolean showing() {
        return this.mControllerHolder.controllerLayout.getVisibility() == 0;
    }

    public void addFullScreenCustomView(View view) {
        if (view != null && this.mControllerHolder.toggleScreenButtonContainer != null) {
            this.mControllerHolder.toggleScreenButtonContainer.removeAllViews();
            this.mControllerHolder.toggleScreenButtonContainer.addView(view, new FrameLayout.LayoutParams(-1, -1));
        }
    }

    public void removeFullScreenCustomView() {
        if (this.mControllerHolder.toggleScreenButtonContainer != null) {
            this.mControllerHolder.toggleScreenButtonContainer.removeAllViews();
            this.mControllerHolder.toggleScreenButtonContainer.addView(this.mControllerHolder.toggleScreenButton);
        }
    }

    public void hideControllerView() {
        this.mHideControllerView = true;
        hideControllerInner();
    }

    public void showControllerView() {
        this.mHideControllerView = false;
        showControllerInner();
    }

    public void setNormalControllerListener(INormalControllerListener controllerListener) {
        this.mNormalControllerListener = controllerListener;
    }

    public void showControllerInner() {
        if (!this.mHideControllerView && !showing() && this.mControllerHolder != null) {
            this.mControllerHolder.controllerLayout.setVisibility(0);
            if (this.mHandler != null) {
                this.mHandler.removeMessages(0);
                this.mHandler.sendEmptyMessageDelayed(0, 4000);
            }
            if (this.mNormalControllerListener != null) {
                this.mNormalControllerListener.show();
            }
        }
    }

    public void hideControllerInner() {
        if (showing()) {
            this.mControllerHolder.controllerLayout.setVisibility(8);
            if (this.mHandler != null) {
                this.mHandler.removeMessages(0);
            }
            if (this.mNormalControllerListener != null) {
                this.mNormalControllerListener.hide();
            }
        }
    }

    public void onMediaStart() {
        if (!TextUtils.isEmpty(this.mDWContext.getVideoToken()) && this.duration == 0) {
            this.duration = this.mDWContext.getVideo().getDuration();
            this.mControllerHolder.totalTimeTv.setText(MediaTimeUtils.msStringForTime(this.duration));
        }
        this.mControllerHolder.seekBar.setEnabled(true);
        hideControllerInner();
        if (this.mHandler != null) {
            this.mHandler.sendEmptyMessageDelayed(0, 4000);
        }
    }

    public void onMediaPause(boolean auto) {
    }

    public void onMediaPlay() {
        if (!TextUtils.isEmpty(this.mDWContext.getVideoToken()) && this.duration == 0) {
            this.duration = this.mDWContext.getVideo().getDuration();
            this.mControllerHolder.totalTimeTv.setText(MediaTimeUtils.msStringForTime(this.duration));
        }
        this.mControllerHolder.seekBar.setEnabled(true);
    }

    public void onMediaPrepared(IMediaPlayer mp) {
        this.mControllerHolder.seekBar.setEnabled(true);
        this.duration = this.duration == 0 ? (int) ((AbstractMediaPlayer) mp).getDuration() : this.duration;
        if (this.duration >= 0) {
            this.mControllerHolder.totalTimeTv.setText(MediaTimeUtils.msStringForTime(this.duration));
        }
    }

    public void onMediaError(IMediaPlayer mp, int what, int extra) {
        resetViewState();
    }

    public void onMediaInfo(IMediaPlayer mp, long what, long extra, long ext, Object obj) {
    }

    public void onMediaComplete() {
        this.mControllerHolder.seekBar.setEnabled(false);
    }

    public void onMediaClose() {
        this.newPosition = 0;
    }

    public void onMediaScreenChanged(MediaPlayScreenType type) {
        if (type == MediaPlayScreenType.NORMAL) {
            onVideoNormalScreen();
        } else {
            onVideoFullScreen(type);
        }
    }

    private void onVideoFullScreen(MediaPlayScreenType type) {
        if (MediaPlayScreenType.LANDSCAPE_FULL_SCREEN == type) {
            updatePlayerController(true);
        }
        if (this.mControllerHolder.toggleScreenButton != null) {
            this.mControllerHolder.toggleScreenButton.setImageResource(this.mControllerHolder.unFullscreenResId);
        }
    }

    private void onVideoNormalScreen() {
        updatePlayerController(false);
        if (this.mControllerHolder.toggleScreenButton != null) {
            this.mControllerHolder.toggleScreenButton.setImageResource(this.mControllerHolder.fullscreenResId);
        }
    }

    private void updatePlayerController(boolean landscape) {
        if (this.mControllerHolder != null) {
            if (landscape) {
                this.mControllerHolder.controllerLayout.getLayoutParams().height = DWViewUtil.dip2px(this.mDWContext.getContext(), 68.0f);
                this.mControllerHolder.currentTimeTv.setTextSize(2, 14.0f);
                this.mControllerHolder.totalTimeTv.setTextSize(2, 14.0f);
                if (this.mDWContext.mNeedScreenButton) {
                    this.mControllerHolder.toggleScreenButtonContainer.getLayoutParams().width = DWViewUtil.dip2px(this.mDWContext.getContext(), 40.0f);
                } else {
                    this.mControllerHolder.toggleScreenButtonContainer.getLayoutParams().width = DWViewUtil.dip2px(this.mDWContext.getContext(), 14.0f);
                }
                this.mControllerHolder.toggleScreenButtonContainer.getLayoutParams().height = DWViewUtil.dip2px(this.mDWContext.getContext(), 40.0f);
                this.mControllerHolder.parentLayout.requestLayout();
                return;
            }
            this.mControllerHolder.controllerLayout.getLayoutParams().height = DWViewUtil.dip2px(this.mDWContext.getContext(), 48.0f);
            this.mControllerHolder.currentTimeTv.setTextSize(2, 10.0f);
            this.mControllerHolder.totalTimeTv.setTextSize(2, 10.0f);
            if (this.mDWContext.mNeedScreenButton) {
                this.mControllerHolder.toggleScreenButtonContainer.getLayoutParams().width = DWViewUtil.dip2px(this.mDWContext.getContext(), 30.0f);
            } else {
                this.mControllerHolder.toggleScreenButtonContainer.getLayoutParams().width = DWViewUtil.dip2px(this.mDWContext.getContext(), 12.0f);
            }
            this.mControllerHolder.toggleScreenButtonContainer.getLayoutParams().height = -1;
            this.mControllerHolder.parentLayout.requestLayout();
        }
    }

    public void onMediaProgressChanged(int currentPosition, int bufferPercent, int total) {
        if (!this.startTracking) {
            this.mTotalTime = total;
            if (currentPosition > total) {
                currentPosition = total;
            }
            this.mControllerHolder.currentTimeTv.setText(MediaTimeUtils.msStringForTime(currentPosition));
            this.mControllerHolder.seekBar.setProgress((int) Math.ceil((double) (1000.0f * ((1.0f * ((float) currentPosition)) / ((float) total)))));
            this.mControllerHolder.seekBar.setSecondaryProgress(bufferPercent * 10);
            this.newPosition = currentPosition;
        }
    }

    public void onStartTrackingTouch(SeekBar bar) {
        this.startTracking = true;
        if (MediaSystemUtils.isApkDebuggable()) {
            DWLogUtils.d(TAG, "onProgressChanged --- onStartTrackingTouch ");
        }
    }

    public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
        if (this.mTotalTime >= 0 && fromUser) {
            this.newPosition = (int) (((float) this.mTotalTime) * (((float) progress) / 1000.0f));
            if (MediaSystemUtils.isApkDebuggable()) {
                DWLogUtils.d(TAG, "onProgressChanged >>> progress:" + progress + ", newPosition:" + this.newPosition);
            }
            if (this.mControllerHolder != null) {
                this.mControllerHolder.currentTimeTv.setText(MediaTimeUtils.msStringForTime(this.newPosition));
            }
        }
    }

    public void onStopTrackingTouch(SeekBar bar) {
        this.startTracking = false;
        if (this.mMediaPlayControlListener != null) {
            this.mMediaPlayControlListener.seekTo(this.newPosition);
        }
        showControllerInner();
    }

    private void resetViewState() {
        this.newPosition = 0;
        this.mControllerHolder.currentTimeTv.setText(MediaTimeUtils.msStringForTime(0));
        this.mControllerHolder.seekBar.setProgress(0);
        this.mControllerHolder.seekBar.setSecondaryProgress(0);
        this.mControllerHolder.seekBar.setEnabled(false);
    }

    public boolean handleMessage(Message msg) {
        if (MediaSystemUtils.isApkDebuggable()) {
            DWLogUtils.d(TAG, "handleMessage >>> what:" + String.valueOf(msg.what) + "," + msg.toString());
        }
        switch (msg.what) {
            case 0:
                hideControllerInner();
                return false;
            default:
                return false;
        }
    }

    public void destroy() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
            this.mHandler = null;
        }
    }

    public void onMediaSeekTo(int currentPosition) {
    }
}
