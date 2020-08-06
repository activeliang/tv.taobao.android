package com.yunos.tvtaobao.biz.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.yunos.tvtaobao.biz.activity.BaseActivity;
import com.yunos.tvtaobao.biz.base.IPresenter;

public abstract class BaseMVPActivity<T extends IPresenter> extends BaseActivity {
    protected final String TAG = getClass().getSimpleName();
    @Nullable
    protected T mPresenter;

    /* access modifiers changed from: protected */
    public abstract T createPresenter();

    /* access modifiers changed from: protected */
    public abstract int provideContentViewId();

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideContentViewId());
        this.mPresenter = createPresenter();
        initView();
        initData();
        initListener();
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
}
