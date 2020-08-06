package android.taobao.windvane.webview;

import android.content.Context;
import android.taobao.windvane.view.AbstractNaviBar;
import android.taobao.windvane.view.WebErrorView;
import android.taobao.windvane.view.WebWaitingView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;

public class WVUIModel {
    private View errorView;
    private View loadingView;
    private Context mContext;
    private View mView;
    private AbstractNaviBar naviBar = null;
    private boolean showLoading = false;

    public WVUIModel(Context context, View view) {
        this.mContext = context;
        this.mView = view;
    }

    public void enableShowLoading() {
        this.showLoading = true;
    }

    public void disableShowLoading() {
        this.showLoading = false;
    }

    public void showLoadingView() {
        if (this.showLoading) {
            if (this.loadingView == null) {
                this.loadingView = new WebWaitingView(this.mContext);
                setLoadingView(this.loadingView);
            }
            this.loadingView.bringToFront();
            if (this.loadingView.getVisibility() != 0) {
                this.loadingView.setVisibility(0);
            }
        }
    }

    public void hideLoadingView() {
        if (this.showLoading && this.loadingView != null && this.loadingView.getVisibility() != 8) {
            this.loadingView.setVisibility(8);
        }
    }

    public void setLoadingView(View view) {
        if (view != null) {
            this.loadingView = view;
            this.loadingView.setVisibility(8);
            ViewParent oldParent = this.loadingView.getParent();
            if (oldParent != null && (oldParent instanceof ViewGroup)) {
                ((ViewGroup) oldParent).removeView(this.loadingView);
            }
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
            params.addRule(14, 1);
            ViewParent parentView = this.mView.getParent();
            if (parentView != null) {
                try {
                    ((ViewGroup) parentView).addView(this.loadingView, params);
                } catch (Exception e) {
                    ViewParent parentView2 = parentView.getParent();
                    if (parentView2 != null) {
                        ((ViewGroup) parentView2).addView(this.loadingView, params);
                    }
                }
            }
        }
    }

    public void loadErrorPage() {
        if (this.errorView == null) {
            this.errorView = new WebErrorView(this.mContext);
            setErrorView(this.errorView);
        }
        this.errorView.bringToFront();
        if (this.errorView.getVisibility() != 0) {
            this.errorView.setVisibility(0);
        }
    }

    public void hideErrorPage() {
        if (this.errorView != null && this.errorView.getVisibility() != 8) {
            this.errorView.setVisibility(8);
        }
    }

    public void setErrorView(View view) {
        if (view != null) {
            this.errorView = view;
            this.errorView.setVisibility(8);
            ViewParent oldParent = this.errorView.getParent();
            if (oldParent != null && (oldParent instanceof ViewGroup)) {
                ((ViewGroup) oldParent).removeView(this.errorView);
            }
            ViewGroup.LayoutParams params = this.mView.getLayoutParams();
            if (params == null) {
                params = new RelativeLayout.LayoutParams(-1, -1);
            }
            if (params instanceof RelativeLayout.LayoutParams) {
                ((RelativeLayout.LayoutParams) params).addRule(14, 1);
            }
            ViewParent parentView = this.mView.getParent();
            if (parentView != null) {
                try {
                    ((ViewGroup) parentView).addView(this.errorView, params);
                } catch (Exception e) {
                    ViewParent parentView2 = parentView.getParent();
                    if (parentView2 != null) {
                        ((ViewGroup) parentView2).addView(this.errorView, params);
                    }
                }
            }
        }
    }

    public View getErrorView() {
        if (this.errorView == null) {
            setErrorView(new WebErrorView(this.mContext));
        }
        return this.errorView;
    }

    public void setNaviBar(AbstractNaviBar view) {
        if (this.naviBar != null) {
            this.naviBar.setVisibility(8);
            this.naviBar = null;
        }
        if (view != null) {
            this.naviBar = view;
        }
    }

    public void resetNaviBar() {
        if (this.naviBar != null) {
            this.naviBar.resetState();
        }
    }

    public void hideNaviBar() {
        if (this.naviBar != null && this.naviBar.getVisibility() != 8) {
            this.naviBar.setVisibility(8);
        }
    }

    public void switchNaviBar(int start) {
        if (this.naviBar != null && start == 1) {
            this.naviBar.startLoading();
        }
    }
}
