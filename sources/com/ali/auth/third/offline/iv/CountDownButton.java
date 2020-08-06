package com.ali.auth.third.offline.iv;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.Button;
import com.ali.auth.third.offline.R;

public class CountDownButton extends Button {
    /* access modifiers changed from: private */
    public boolean isCountDowning;
    protected CountListener mCountListener;
    protected int mGetCodeTitleRes;
    protected int mTickTitleRes;
    private TimeCountDown mTimeCountDown;

    public interface CountListener {
        void onTick(long j);
    }

    public CountDownButton(Context context) {
        super(context);
        this.isCountDowning = false;
        this.isCountDowning = false;
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isCountDowning = false;
        this.isCountDowning = false;
    }

    public void startCountDown(long millisInFuture, long countDownInterval) {
        this.mTimeCountDown = new TimeCountDown(millisInFuture, countDownInterval);
        this.mTimeCountDown.start();
        this.isCountDowning = true;
    }

    public void setTickListener(CountListener listener) {
        this.mCountListener = listener;
    }

    public void cancelCountDown() {
        if (this.mTimeCountDown != null) {
            this.mTimeCountDown.cancel();
        }
        this.isCountDowning = false;
    }

    private class TimeCountDown extends CountDownTimer {
        TimeCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
            long seconds = (millisUntilFinished / 1000) + 1;
            if (CountDownButton.this.mTickTitleRes != 0) {
                CountDownButton.this.setText(CountDownButton.this.getResources().getString(CountDownButton.this.mTickTitleRes, new Object[]{String.valueOf(seconds)}));
            } else {
                CountDownButton.this.setText(String.valueOf(seconds) + CountDownButton.this.getContext().getString(R.string.ali_auth_verification_reGetCode));
            }
            CountDownButton.this.setEnabled(false);
            if (CountDownButton.this.mCountListener != null) {
                CountDownButton.this.mCountListener.onTick(millisUntilFinished);
            }
        }

        public void onFinish() {
            if (CountDownButton.this.mGetCodeTitleRes != 0) {
                CountDownButton.this.setText(CountDownButton.this.getContext().getString(CountDownButton.this.mGetCodeTitleRes));
            } else {
                CountDownButton.this.setText(CountDownButton.this.getContext().getString(R.string.ali_auth_verification_reGetCode));
            }
            CountDownButton.this.setEnabled(true);
            boolean unused = CountDownButton.this.isCountDowning = false;
        }
    }

    public void setGetCodeTitle(int getCodeTitleRes) {
        this.mGetCodeTitleRes = getCodeTitleRes;
    }

    public void setTickTitleRes(int tickTitleRes) {
        this.mTickTitleRes = tickTitleRes;
    }

    public boolean isCountDowning() {
        return this.isCountDowning;
    }
}
