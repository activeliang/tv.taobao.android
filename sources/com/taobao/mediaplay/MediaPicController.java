package com.taobao.mediaplay;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MediaPicController {
    private MediaContext mMediaContext;
    private ImageView mPicView;
    private FrameLayout mRootView;
    private ImageView.ScaleType mScaleType = ImageView.ScaleType.FIT_CENTER;

    MediaPicController(MediaContext context) {
        this.mMediaContext = context;
        this.mRootView = new FrameLayout(this.mMediaContext.getContext());
    }

    /* access modifiers changed from: package-private */
    public void setPicImageView(ImageView imageView) {
        this.mPicView = imageView;
        this.mRootView.removeAllViews();
        this.mRootView.setVisibility(0);
        this.mRootView.addView(this.mPicView, new FrameLayout.LayoutParams(-1, -1, 17));
    }

    /* access modifiers changed from: package-private */
    public void setPicScaleType(ImageView.ScaleType scaleType) {
        this.mScaleType = scaleType;
        if (this.mPicView != null) {
            this.mPicView.setScaleType(scaleType);
        }
    }

    /* access modifiers changed from: package-private */
    public View getView() {
        return this.mRootView;
    }

    /* access modifiers changed from: package-private */
    public void destroy() {
        if (this.mMediaContext != null) {
            this.mRootView.removeAllViews();
        }
    }
}
