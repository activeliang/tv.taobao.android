package com.powyin.scroll.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.powyin.scroll.R;
import com.powyin.scroll.widget.SwipeController;

public class SwipeControllerStyleNormal implements SwipeController {
    private View footView;
    private View headView;
    private LoadProgressBar loadProgressBar;
    private Context mContent;
    private View overHead = this.headView.findViewById(R.id.powyin_swipe_over_head);
    private ImageView statueRefreshErrorAutoHide = ((ImageView) this.headView.findViewById(R.id.powyin_swipe_error_auto_hide));
    private ImageView statusLoad = ((ImageView) this.headView.findViewById(R.id.powyin_swipe_refresh));
    private CircleViewBac statusPre = ((CircleViewBac) this.headView.findViewById(R.id.powyin_swipe_image_info));
    private ImageView statusRefreshErrorFixed = ((ImageView) this.headView.findViewById(R.id.powyin_swipe_error_fixed));
    private ImageView statusRefreshSuccess = ((ImageView) this.headView.findViewById(R.id.powyin_swipe_ok));
    private TextView textInfo = ((TextView) this.headView.findViewById(R.id.powyin_swipe_text_info));
    private TextView textLoad;

    SwipeControllerStyleNormal(Context context) {
        this.mContent = context;
        LayoutInflater inflater = LayoutInflater.from(this.mContent);
        this.headView = inflater.inflate(R.layout.powyin_scroll_style_normal_head_swipe, (ViewGroup) null);
        this.footView = inflater.inflate(R.layout.powyin_scroll_style_normal_loading_more, (ViewGroup) null);
        this.loadProgressBar = (LoadProgressBar) this.footView.findViewById(R.id.powyin_scroll_load_bar);
        this.textLoad = (TextView) this.footView.findViewById(R.id.powyin_scroll_load_more);
    }

    public View getSwipeHead() {
        return this.headView;
    }

    public View getSwipeFoot() {
        return this.footView;
    }

    public int getOverScrollHei() {
        return this.overHead.getHeight();
    }

    public void onSwipeStatue(SwipeController.SwipeStatus status, int visibleHei, int wholeHei) {
        switch (status) {
            case SWIPE_HEAD_OVER:
                this.statusPre.setVisibility(0);
                this.statusPre.setProgress(1.0f);
                this.statusLoad.clearAnimation();
                this.statusLoad.setVisibility(4);
                this.statusRefreshSuccess.setVisibility(4);
                this.statueRefreshErrorAutoHide.setVisibility(4);
                this.statusRefreshErrorFixed.setVisibility(4);
                if (!this.textInfo.getText().toString().equals("松开刷新")) {
                    this.textInfo.setText("松开刷新");
                    return;
                }
                return;
            case SWIPE_HEAD_TOAST:
                this.statusPre.setVisibility(0);
                this.statusLoad.clearAnimation();
                this.statusLoad.setVisibility(4);
                this.statusRefreshSuccess.setVisibility(4);
                this.statueRefreshErrorAutoHide.setVisibility(4);
                this.statusRefreshErrorFixed.setVisibility(4);
                this.statusPre.setProgress((2.0f * (((float) (visibleHei - this.textInfo.getHeight())) - (((float) this.statusPre.getHeight()) / 1.35f))) / ((float) this.statusPre.getHeight()));
                if (!this.textInfo.getText().toString().equals("上拉刷新")) {
                    this.textInfo.setText("上拉刷新");
                    return;
                }
                return;
            case SWIPE_HEAD_LOADING:
                this.statusPre.setVisibility(4);
                if (this.statusLoad.getVisibility() != 0) {
                    this.statusLoad.setVisibility(0);
                    this.statusLoad.setAnimation(AnimationUtils.loadAnimation(this.mContent, R.anim.powyin_scroll_rotale));
                }
                this.statusRefreshSuccess.setVisibility(4);
                this.statueRefreshErrorAutoHide.setVisibility(4);
                this.statusRefreshErrorFixed.setVisibility(4);
                if (!this.textInfo.getText().toString().equals("正在拼命刷新中")) {
                    this.textInfo.setText("正在拼命刷新中");
                    return;
                }
                return;
            case SWIPE_HEAD_COMPLETE_OK:
                this.statusPre.setVisibility(4);
                this.statusLoad.clearAnimation();
                this.statusLoad.setVisibility(4);
                this.statusRefreshSuccess.setVisibility(0);
                this.statueRefreshErrorAutoHide.setVisibility(4);
                this.statusRefreshErrorFixed.setVisibility(4);
                if (!this.textInfo.getText().toString().equals("刷新成功")) {
                    this.textInfo.setText("刷新成功");
                    return;
                }
                return;
            case SWIPE_HEAD_COMPLETE_ERROR:
                this.statusPre.setVisibility(4);
                this.statusLoad.clearAnimation();
                this.statusLoad.setVisibility(4);
                this.statusRefreshSuccess.setVisibility(4);
                this.statueRefreshErrorAutoHide.setVisibility(0);
                this.statusRefreshErrorFixed.setVisibility(4);
                if (!this.textInfo.getText().toString().equals("刷新失败")) {
                    this.textInfo.setText("刷新失败");
                    return;
                }
                return;
            case SWIPE_HEAD_COMPLETE_ERROR_NET:
                this.statusPre.setVisibility(4);
                this.statusLoad.clearAnimation();
                this.statusLoad.setVisibility(4);
                this.statusRefreshSuccess.setVisibility(4);
                this.statueRefreshErrorAutoHide.setVisibility(4);
                this.statusRefreshErrorFixed.setVisibility(0);
                if (!this.textInfo.getText().toString().equals("刷新失败")) {
                    this.textInfo.setText("刷新失败");
                    break;
                }
                break;
            case SWIPE_LOAD_LOADING:
                break;
            case SWIPE_LOAD_NO_MORE:
                this.loadProgressBar.setVisibility(8);
                this.textLoad.setVisibility(0);
                return;
            case SWIPE_LOAD_ERROR:
                this.loadProgressBar.setVisibility(8);
                this.textLoad.setVisibility(0);
                return;
            default:
                return;
        }
        this.loadProgressBar.setVisibility(0);
        this.loadProgressBar.ensureAnimation(false);
        this.textLoad.setVisibility(8);
    }
}
