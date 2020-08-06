package com.yunos.tvtaobao.tvsdk.widget;

import com.zhiping.dev.android.logger.ZpLogger;

public class ListLoopScroller {
    private final boolean DEBUG = false;
    public final float DEFALUT_MAX_STEP = 300.0f;
    public final float SLOW_DOWN_DISTANCE_RATIO = 4.0f;
    public final float SLOW_DOWN_RATIO = 3.0f;
    private final String TAG = "ListLoopScroller";
    private int mCurr;
    private int mCurrFrameIndex;
    private int mFinal;
    private boolean mFinished = true;
    private float mMaxStep = 300.0f;
    private float mSlowDownDistance;
    private float mSlowDownFrameCount;
    private int mSlowDownIndex;
    private float mSlowDownRatio = 4.0f;
    private int mSlowDownStart;
    private float mSlowDownStep;
    private int mStart;
    private long mStartTime;
    private float mStep;
    private int mTotalFrameCount;

    public int getCurr() {
        return this.mCurr;
    }

    public int getFinal() {
        return this.mFinal;
    }

    public int getStart() {
        return this.mStart;
    }

    public boolean isFinished() {
        return this.mFinished;
    }

    public void setMaxStep(float maxStep) {
        this.mMaxStep = maxStep;
    }

    public void setSlowDownRatio(float ratio) {
        if (!this.mFinished) {
            throw new IllegalStateException("setSlowDownRatio before start");
        } else if (this.mSlowDownRatio > 1.0f) {
            this.mSlowDownRatio = ratio;
        } else {
            ZpLogger.e("ListLoopScroller", "setSlowDownRatio value must > 1.0");
        }
    }

    public void startScroll(int start, int distance, int frameCount) {
        int distance2 = distance + (this.mFinal - this.mCurr);
        this.mTotalFrameCount = frameCount;
        this.mStep = ((float) distance2) / ((float) this.mTotalFrameCount);
        if (this.mStep > this.mMaxStep) {
            this.mStep = this.mMaxStep;
            this.mTotalFrameCount = (int) (((float) distance2) / this.mStep);
        } else if (this.mStep < (-this.mMaxStep)) {
            this.mStep = -this.mMaxStep;
            this.mTotalFrameCount = (int) (((float) distance2) / this.mStep);
        }
        this.mCurr = start;
        this.mStart = start;
        this.mFinal = this.mStart + distance2;
        this.mFinished = false;
        this.mCurrFrameIndex = 0;
        this.mSlowDownFrameCount = 0.0f;
        this.mSlowDownStep = this.mStep / 3.0f;
        computeSlowDownDistance();
    }

    public boolean computeScrollOffset() {
        if (this.mFinished) {
            return false;
        }
        if (this.mCurrFrameIndex >= this.mTotalFrameCount) {
            finish();
            return false;
        }
        if (this.mSlowDownFrameCount <= 0.0f) {
            this.mCurr = (int) (((float) this.mStart) + (((float) (this.mFinal - this.mStart)) * (((float) (this.mCurrFrameIndex + 1)) / ((float) this.mTotalFrameCount))));
            this.mCurrFrameIndex++;
            int leftDistance = this.mFinal - this.mCurr;
            if (leftDistance < 0) {
                leftDistance = -leftDistance;
            }
            if (((float) leftDistance) < this.mSlowDownDistance) {
                setSlowDown(leftDistance);
            } else {
                resetSlowDown();
            }
        } else if (((float) this.mSlowDownIndex) > this.mSlowDownFrameCount) {
            finish();
            return false;
        } else {
            this.mSlowDownIndex++;
            if (((float) this.mSlowDownIndex) >= this.mSlowDownFrameCount) {
                this.mCurr = this.mFinal;
            } else {
                this.mCurr = this.mSlowDownStart + ((int) ((this.mSlowDownStep * ((float) this.mSlowDownIndex)) - ((((float) (this.mSlowDownIndex * this.mSlowDownIndex)) * this.mSlowDownStep) / (2.0f * this.mSlowDownFrameCount))));
            }
        }
        return true;
    }

    public void finish() {
        if (!this.mFinished) {
            this.mCurr = this.mFinal;
            this.mCurrFrameIndex = this.mTotalFrameCount;
            this.mSlowDownFrameCount = 0.0f;
            this.mFinished = true;
        }
    }

    private void resetSlowDown() {
        this.mSlowDownStart = 0;
        this.mSlowDownFrameCount = 0.0f;
        this.mSlowDownIndex = 0;
    }

    private void setSlowDown(int distance) {
        this.mSlowDownStart = this.mCurr;
        this.mSlowDownIndex = 0;
        this.mSlowDownFrameCount = ((float) (distance * 2)) / this.mSlowDownStep;
        if (this.mSlowDownFrameCount < 0.0f) {
            this.mSlowDownFrameCount = -this.mSlowDownFrameCount;
        }
    }

    private void computeSlowDownDistance() {
        int distance = (this.mFinal - this.mStart) / 2;
        if (distance < 0) {
            distance = -distance;
        }
        this.mSlowDownDistance = (this.mStep * this.mStep) / this.mSlowDownRatio;
        if (this.mSlowDownDistance > ((float) distance)) {
            ZpLogger.w("ListLoopScroller", "computeSlowDownDistance mSlowDownDistance too big=" + this.mSlowDownDistance + " distance=" + distance + " mStep=" + this.mStep);
            this.mSlowDownDistance = (float) distance;
        }
    }
}
