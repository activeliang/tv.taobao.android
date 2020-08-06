package com.yunos.tv.blitz.video.data;

import android.widget.FrameLayout;

public class VideoViewInfo {
    private FrameLayout.LayoutParams layoutParams;
    private VideoItem mVideoItem;

    public VideoViewInfo(FrameLayout.LayoutParams lp) {
        this.layoutParams = lp;
    }

    public FrameLayout.LayoutParams getLayoutParams() {
        return this.layoutParams;
    }

    public void setLayoutParams(FrameLayout.LayoutParams lp) {
        this.layoutParams = lp;
    }

    public void setVideoItem(VideoItem item) {
        this.mVideoItem = item;
    }

    public VideoItem getVideoItem() {
        return this.mVideoItem;
    }
}
