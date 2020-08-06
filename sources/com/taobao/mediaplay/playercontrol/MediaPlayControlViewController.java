package com.taobao.mediaplay.playercontrol;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.taobao.media.DWViewUtil;
import com.taobao.mediaplay.IMediaLifecycleListener;
import com.taobao.mediaplay.MediaContext;
import com.taobao.mediaplay.MediaLifecycleType;
import com.taobao.mediaplay.playercontrol.MediaPlayNormalController;
import com.taobao.taobaoavsdk.R;

public class MediaPlayControlViewController implements IMediaLifecycleListener, IMediaPlayControlListener {
    /* access modifiers changed from: private */
    public MediaContext mContext;
    private boolean mDestoryed;
    private FrameLayout mHost;
    private IMediaPlayControlListener mMediaPlayControlListener;
    private MediaPlayNormalController mMediaPlayerController;
    private MediaPlaySmallBarViewController mMediaSmallBarViewController;
    private int mPauseResId;
    private ImageView mPlayOrPauseView;
    private int mStartResId;

    public MediaPlayControlViewController(MediaContext context, boolean force) {
        this.mContext = context;
        initView();
        if (force) {
            initPlayControlView();
        }
    }

    private void initView() {
        this.mHost = new FrameLayout(this.mContext.getContext());
        this.mPauseResId = R.drawable.mediaplay_sdk_pause;
        this.mStartResId = R.drawable.mediaplay_sdk_play;
        this.mPlayOrPauseView = new ImageView(this.mContext.getContext());
        this.mPlayOrPauseView.setVisibility(8);
        this.mPlayOrPauseView.setImageResource(R.drawable.mediaplay_sdk_play);
        this.mHost.addView(this.mPlayOrPauseView, new FrameLayout.LayoutParams(DWViewUtil.dip2px(this.mContext.getContext(), 62.0f), DWViewUtil.dip2px(this.mContext.getContext(), 62.0f), 17));
        this.mPlayOrPauseView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (MediaPlayControlViewController.this.mContext != null && MediaPlayControlViewController.this.mContext.getVideo() != null && MediaPlayControlViewController.this.mContext.getVideo().getVideoState() == 1) {
                    MediaPlayControlViewController.this.mContext.getVideo().pauseVideo();
                } else if (MediaPlayControlViewController.this.mContext != null && MediaPlayControlViewController.this.mContext.getVideo() != null && MediaPlayControlViewController.this.mContext.getVideo().getVideoState() == 2) {
                    MediaPlayControlViewController.this.mContext.getVideo().playVideo();
                } else if (MediaPlayControlViewController.this.mContext != null && MediaPlayControlViewController.this.mContext.getVideo() != null && MediaPlayControlViewController.this.mContext.getVideo().getVideoState() != 2) {
                    MediaPlayControlViewController.this.mContext.getVideo().startVideo();
                }
            }
        });
    }

    private void initPlayControlView() {
        if (this.mMediaPlayerController == null) {
            this.mMediaSmallBarViewController = new MediaPlaySmallBarViewController(this.mContext, this.mHost);
            this.mMediaPlayerController = new MediaPlayNormalController(this.mContext);
            this.mMediaPlayerController.setIMediaPlayControlListener(this.mMediaPlayControlListener);
            this.mHost.addView(this.mMediaPlayerController.getView(), new FrameLayout.LayoutParams(-1, -2, 80));
            this.mContext.getVideo().registerIMediaLifecycleListener(this.mMediaSmallBarViewController);
            this.mContext.getVideo().registerIMediaLifecycleListener(this.mMediaPlayerController);
            this.mMediaPlayerController.setNormalControllerListener(new MediaPlayNormalController.INormalControllerListener() {
                public void hide() {
                    MediaPlayControlViewController.this.hidePlayView();
                }

                public void show() {
                    MediaPlayControlViewController.this.showPlayView();
                }
            });
        }
    }

    public void hideMiniProgressBar() {
        this.mMediaSmallBarViewController.hideProgressBar(true);
    }

    public void showMiniProgressBar() {
        this.mMediaSmallBarViewController.showProgressBar(true);
    }

    public void setIMediaPlayerControlListener(IMediaPlayControlListener listener) {
        this.mMediaPlayControlListener = listener;
        if (this.mMediaPlayerController != null) {
            this.mMediaPlayerController.setIMediaPlayControlListener(listener);
        }
    }

    public void hidePlayView() {
        if (this.mPlayOrPauseView != null && this.mPlayOrPauseView.getVisibility() == 0) {
            this.mPlayOrPauseView.setVisibility(8);
        }
    }

    public void showPlayView() {
        if (this.mPlayOrPauseView != null && this.mPlayOrPauseView.getVisibility() != 0) {
            this.mPlayOrPauseView.setVisibility(0);
        }
    }

    public void showSmallControlBar() {
        hideControllerInner();
    }

    public void hideSmallControlBar() {
        showControllerInner();
    }

    public void showControllerInner() {
        if (this.mMediaPlayerController != null) {
            this.mMediaPlayerController.showControllerInner();
        }
    }

    public void showControllerView() {
        if (this.mMediaPlayerController != null) {
            this.mMediaPlayerController.showControllerView();
        }
    }

    public void hideControllerView() {
        if (this.mMediaPlayerController != null) {
            this.mMediaPlayerController.hideControllerView();
        }
    }

    public void addFullScreenCustomView(View view) {
        if (this.mMediaPlayerController != null) {
            this.mMediaPlayerController.addFullScreenCustomView(view);
        }
    }

    public void removeFullScreenCustomView() {
        if (this.mMediaPlayerController != null) {
            this.mMediaPlayerController.removeFullScreenCustomView();
        }
    }

    public void setPauseSrc() {
        if (this.mPlayOrPauseView != null) {
            this.mPlayOrPauseView.setImageResource(this.mPauseResId);
        }
    }

    public void setPlaySrc() {
        if (this.mPlayOrPauseView != null) {
            this.mPlayOrPauseView.setImageResource(this.mStartResId);
        }
    }

    public void hideControllerInner() {
        if (this.mMediaPlayerController != null) {
            this.mMediaPlayerController.hideControllerInner();
        }
    }

    public boolean showing() {
        if (this.mMediaPlayerController == null) {
            return false;
        }
        return this.mMediaPlayerController.showing();
    }

    public ViewGroup getView() {
        return this.mHost;
    }

    public void destroy() {
        this.mDestoryed = true;
        if (this.mMediaPlayerController != null) {
            this.mMediaPlayerController.destroy();
        }
        if (this.mMediaSmallBarViewController != null) {
            this.mMediaSmallBarViewController.destroy();
        }
    }

    public void onLifecycleChanged(MediaLifecycleType lifecycleType) {
        if (MediaLifecycleType.PLAY == lifecycleType && this.mMediaPlayerController == null && !this.mDestoryed) {
            initPlayControlView();
        }
    }

    public void screenButtonClick() {
    }

    public void seekTo(int position) {
    }

    public boolean onPlayRateChanged(float playRate) {
        return false;
    }
}
