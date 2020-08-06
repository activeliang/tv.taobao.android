package com.yunos.tvtaobao.biz.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.yunos.tv.core.common.ImageLoaderManager;
import com.yunos.tv.core.util.Utils;
import com.yunos.tv.lib.DisplayUtil;
import com.yunos.tvtaobao.biz.activity.BaseActivity;
import com.yunos.tvtaobao.biz.base.IPresenter;
import com.yunos.tvtaobao.biz.request.info.GlobalConfigInfo;
import com.yunos.tvtaobao.businessview.R;
import com.yunos.tvtaobao.payment.request.GlobalConfig;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;

public abstract class BaseTabMVPActivity<T extends IPresenter> extends BaseActivity {
    protected final String TAG = getClass().getSimpleName();
    private ConstraintLayout constraintLayout;
    protected boolean hasActive = false;
    Map<String, String> lProperties = Utils.getProperties();
    @Nullable
    protected T mPresenter;
    protected RightSideView rightSideView;

    /* access modifiers changed from: protected */
    public abstract T createPresenter();

    /* access modifiers changed from: protected */
    public abstract int provideContentViewId();

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideContentViewId());
        initContentView(R.layout.activity_tab_base_mvp);
        this.rightSideView = (RightSideView) findViewById(R.id.right_side_view);
        this.mPresenter = createPresenter();
        initView();
        initData();
        initListener();
        isHasActivityFromNet();
    }

    private void initContentView(int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, this.constraintLayout, true);
    }

    public void setContentView(int layoutResID) {
        FrameLayout viewGroup = (FrameLayout) findViewById(16908290);
        viewGroup.removeAllViews();
        this.constraintLayout = new ConstraintLayout(this) {
            public boolean dispatchKeyEvent(KeyEvent event) {
                ZpLogger.d(BaseTabMVPActivity.this.TAG, "dispatchKeyEvent focus=" + DisplayUtil.getViewStr(BaseTabMVPActivity.this.getCurrentFocus()) + " focusedChild=" + DisplayUtil.getViewStr(getFocusedChild()));
                return super.dispatchKeyEvent(event);
            }

            public View focusSearch(View focused, int direction) {
                View rtn = super.focusSearch(focused, direction);
                ZpLogger.d(BaseTabMVPActivity.this.TAG, "focusSearch focus=" + DisplayUtil.getViewStr(focused) + " rtn=" + DisplayUtil.getViewStr(rtn));
                return rtn;
            }
        };
        viewGroup.addView(this.constraintLayout);
        LayoutInflater.from(this).inflate(layoutResID, this.constraintLayout, true);
    }

    public void initListener() {
    }

    public void initData() {
    }

    public void initView() {
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.mPresenter != null) {
            this.mPresenter.onDestroy();
        }
        this.mPresenter = null;
    }

    private void isHasActivityFromNet() {
        GlobalConfig.ShopCartFlag shopCartFlag;
        GlobalConfig globalConfig = GlobalConfigInfo.getInstance().getGlobalConfig();
        if (globalConfig != null && (shopCartFlag = globalConfig.getShopCartFlag()) != null && shopCartFlag.isActing()) {
            this.rightSideView.getFiv_pierce_background().setBackgroundResource(R.drawable.goodlist_fiv_pierce_bg_d11);
            this.hasActive = shopCartFlag.isActing();
            ImageLoaderManager.get().displayImage(shopCartFlag.getSideBarIcon(), this.rightSideView.getIv_pierce_cart_active());
            this.rightSideView.getIv_pierce_cart_active().setVisibility(0);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    BaseTabMVPActivity.this.rightSideView.getIv_pierce_cart_active().setVisibility(4);
                }
            }, 5000);
            this.lProperties.put("name", "购物车气泡");
            Utils.utControlHit(getFullPageName(), "Expose_tool_cartbubble", this.lProperties);
        }
    }
}
