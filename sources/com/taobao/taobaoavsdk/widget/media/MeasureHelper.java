package com.taobao.taobaoavsdk.widget.media;

import android.view.View;

public final class MeasureHelper {
    private int mCurrentAspectRatio = 0;
    private int mMeasuredHeight;
    private int mMeasuredWidth;
    private int mVideoHeight;
    private int mVideoRotationDegree;
    private int mVideoSarDen;
    private int mVideoSarNum;
    private int mVideoWidth;

    public void setVideoSize(int videoWidth, int videoHeight) {
        this.mVideoWidth = videoWidth;
        this.mVideoHeight = videoHeight;
    }

    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        this.mVideoSarNum = videoSarNum;
        this.mVideoSarDen = videoSarDen;
    }

    public void setVideoRotation(int videoRotationDegree) {
        this.mVideoRotationDegree = videoRotationDegree;
    }

    public void doMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float displayAspectRatio;
        if (this.mVideoRotationDegree == 90 || this.mVideoRotationDegree == 270) {
            int tempSpec = widthMeasureSpec;
            widthMeasureSpec = heightMeasureSpec;
            heightMeasureSpec = tempSpec;
        }
        int width = View.getDefaultSize(this.mVideoWidth, widthMeasureSpec);
        int height = View.getDefaultSize(this.mVideoHeight, heightMeasureSpec);
        if (this.mCurrentAspectRatio != 3) {
            if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
                int widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec);
                int widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
                int heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec);
                int heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);
                if ((widthSpecMode == Integer.MIN_VALUE && heightSpecMode == Integer.MIN_VALUE) || (widthSpecMode == 1073741824 && heightSpecMode == 1073741824)) {
                    float specAspectRatio = ((float) widthSpecSize) / ((float) heightSpecSize);
                    switch (this.mCurrentAspectRatio) {
                        case 4:
                            displayAspectRatio = 1.7777778f;
                            if (this.mVideoRotationDegree == 90 || this.mVideoRotationDegree == 270) {
                                displayAspectRatio = 1.0f / 1.7777778f;
                                break;
                            }
                        case 5:
                            displayAspectRatio = 1.3333334f;
                            if (this.mVideoRotationDegree == 90 || this.mVideoRotationDegree == 270) {
                                displayAspectRatio = 1.0f / 1.3333334f;
                                break;
                            }
                        default:
                            displayAspectRatio = ((float) this.mVideoWidth) / ((float) this.mVideoHeight);
                            if (this.mVideoSarNum > 0 && this.mVideoSarDen > 0) {
                                displayAspectRatio = (((float) this.mVideoSarNum) * displayAspectRatio) / ((float) this.mVideoSarDen);
                                break;
                            }
                    }
                    boolean shouldBeWider = displayAspectRatio > specAspectRatio;
                    switch (this.mCurrentAspectRatio) {
                        case 0:
                        case 4:
                        case 5:
                            if (!shouldBeWider) {
                                height = heightSpecSize;
                                width = (int) (((float) height) * displayAspectRatio);
                                break;
                            } else {
                                width = widthSpecSize;
                                height = (int) (((float) width) / displayAspectRatio);
                                break;
                            }
                        case 1:
                            if (!shouldBeWider) {
                                width = widthSpecSize;
                                height = (int) (((float) width) / displayAspectRatio);
                                break;
                            } else {
                                height = heightSpecSize;
                                width = (int) (((float) height) * displayAspectRatio);
                                break;
                            }
                        case 3:
                            height = heightSpecSize;
                            width = widthSpecSize;
                            break;
                        default:
                            if (!shouldBeWider) {
                                height = Math.min(this.mVideoHeight, heightSpecSize);
                                width = (int) (((float) height) * displayAspectRatio);
                                break;
                            } else {
                                width = Math.min(this.mVideoWidth, widthSpecSize);
                                height = (int) (((float) width) / displayAspectRatio);
                                break;
                            }
                    }
                } else if (widthSpecMode == 1073741824) {
                    width = widthSpecSize;
                    height = (this.mVideoHeight * width) / this.mVideoWidth;
                    if (this.mVideoSarNum > 0 && this.mVideoSarDen > 0) {
                        height = (this.mVideoSarDen * height) / this.mVideoSarNum;
                    }
                    if (heightSpecMode == Integer.MIN_VALUE && height > heightSpecSize) {
                        height = heightSpecSize;
                    }
                } else if (heightSpecMode == 1073741824) {
                    height = heightSpecSize;
                    width = (this.mVideoWidth * height) / this.mVideoHeight;
                    if (this.mVideoSarNum > 0 && this.mVideoSarDen > 0) {
                        width = (this.mVideoSarNum * width) / this.mVideoSarDen;
                    }
                    if (widthSpecMode == Integer.MIN_VALUE && width > widthSpecSize) {
                        width = widthSpecSize;
                    }
                } else {
                    int width2 = this.mVideoWidth;
                    int height2 = this.mVideoHeight;
                    if (heightSpecMode == Integer.MIN_VALUE && height2 > heightSpecSize) {
                        height2 = heightSpecSize;
                        width2 = (this.mVideoWidth * height2) / this.mVideoHeight;
                    }
                    if (widthSpecMode == Integer.MIN_VALUE && width > widthSpecSize) {
                        width = widthSpecSize;
                        height = (this.mVideoHeight * width) / this.mVideoWidth;
                    }
                }
            }
        } else {
            width = widthMeasureSpec;
            height = heightMeasureSpec;
        }
        this.mMeasuredWidth = width;
        this.mMeasuredHeight = height;
    }

    public void setAspectRatio(int aspectRatio) {
        this.mCurrentAspectRatio = aspectRatio;
    }

    public int getMeasuredWidth() {
        return this.mMeasuredWidth;
    }

    public void setMeasuredWidth(int measuredWidth) {
        this.mMeasuredWidth = measuredWidth;
    }

    public int getMeasuredHeight() {
        return this.mMeasuredHeight;
    }

    public void setMeasuredHeight(int measuredHeight) {
        this.mMeasuredHeight = measuredHeight;
    }

    public float getDisplayAspectRatio() {
        if (this.mMeasuredWidth <= 0 || this.mMeasuredHeight <= 0) {
            return 0.0f;
        }
        return ((float) this.mMeasuredWidth) / ((float) this.mMeasuredHeight);
    }
}
