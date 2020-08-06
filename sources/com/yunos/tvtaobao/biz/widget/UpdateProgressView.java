package com.yunos.tvtaobao.biz.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.tvtaobao.android.ui3.widget.ButtonView;
import com.tvtaobao.android.ui3.widget.LoadingAnimView;
import com.yunos.tvtaobao.biz.activity.CoreActivity;
import com.yunos.tvtaobao.businessview.R;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;

public class UpdateProgressView implements View.OnClickListener {
    private final String TAG = "UpdateProgressView";
    private ButtonView btAgain;
    private ButtonView btBack;
    private OnClickListener clickListener;
    private WeakReference<Context> contextWeakReference;
    private boolean isShow = false;
    private int lastProgress = 0;
    private LoadingAnimView lavLoafAnimation;
    /* access modifiers changed from: private */
    public ProgressBar pbDownload;
    private int progressBarWidth = 0;
    private TextView tvProgressInfo;
    private ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        public void onAnimationUpdate(ValueAnimator animation) {
            float progress = ((Float) animation.getAnimatedValue()).floatValue();
            ZpLogger.i("UpdateProgressView", "UpdateProgressView.onAnimationUpdate progress=" + progress);
            UpdateProgressView.this.pbDownload.setProgress((int) progress);
            UpdateProgressView.this.setAnimation(progress);
        }
    };
    private ValueAnimator valueAnimator;

    public interface OnClickListener {
        void again();

        void back();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.bt_back && this.clickListener != null) {
            this.clickListener.back();
        }
        if (v.getId() == R.id.bt_again && this.clickListener != null) {
            this.clickListener.again();
        }
    }

    public UpdateProgressView(CoreActivity context) {
        this.contextWeakReference = new WeakReference<>(context);
        this.pbDownload = (ProgressBar) context.findViewById(R.id.pb_download);
        this.tvProgressInfo = (TextView) context.findViewById(R.id.tv_update_progress_info);
        this.lavLoafAnimation = (LoadingAnimView) context.findViewById(R.id.lav_loading_animation);
        this.btBack = (ButtonView) context.findViewById(R.id.bt_back);
        this.btBack.setPadding(0, 0, 0, 0);
        this.btAgain = (ButtonView) context.findViewById(R.id.bt_again);
        this.btAgain.setPadding(0, 0, 0, 0);
        this.progressBarWidth = context.getResources().getDimensionPixelSize(R.dimen.dp_624);
        this.btBack.setOnClickListener(this);
        this.btAgain.setOnClickListener(this);
        this.valueAnimator = new ValueAnimator();
        this.valueAnimator.setDuration(300);
        this.valueAnimator.addUpdateListener(this.updateListener);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.clickListener = listener;
    }

    public void setDownloadProgress(int progress) {
        ZpLogger.i("UpdateProgressView", "UpdateProgressView.setDownloadProgress progress=" + progress);
        if (this.isShow) {
            this.tvProgressInfo.setText(String.format(((Context) this.contextWeakReference.get()).getResources().getString(R.string.bs_update_progress), new Object[]{Integer.valueOf(progress)}));
            this.valueAnimator.setFloatValues(new float[]{(float) this.lastProgress, (float) progress});
            this.valueAnimator.start();
            this.lastProgress = progress;
        }
    }

    public void showProgressInfo() {
        if (this.pbDownload.getVisibility() == 8) {
            this.pbDownload.setVisibility(0);
        }
        if (this.tvProgressInfo.getVisibility() == 8) {
            this.tvProgressInfo.setVisibility(0);
        }
        if (this.lavLoafAnimation.getVisibility() == 8) {
            this.lavLoafAnimation.setVisibility(0);
            this.lavLoafAnimation.startAnimation();
        }
        this.isShow = true;
    }

    public void downloadError() {
        showProgressInfo();
        this.tvProgressInfo.setText(((Context) this.contextWeakReference.get()).getResources().getString(R.string.ytm_network_link_error));
        showButton();
    }

    private void showButton() {
        if (this.btBack.getVisibility() == 8) {
            this.btBack.setVisibility(0);
        }
        if (this.btAgain.getVisibility() == 8) {
            this.btAgain.setVisibility(0);
            this.btAgain.requestFocus();
        }
    }

    private void hideButton() {
        if (this.btBack.getVisibility() == 0) {
            this.btBack.setVisibility(8);
        }
        if (this.btAgain.getVisibility() == 0) {
            this.btAgain.setVisibility(8);
        }
    }

    /* access modifiers changed from: private */
    public void setAnimation(float progress) {
        this.lavLoafAnimation.setTranslationX((progress / 100.0f) * ((float) this.progressBarWidth));
    }

    public void onResumeDownload() {
        hideButton();
    }

    public void onDestroy() {
        this.lavLoafAnimation.onDestroy();
    }
}
