package com.yunos.tvtaobao.biz.base;

import com.yunos.tvtaobao.biz.base.IModel;
import com.yunos.tvtaobao.biz.base.IView;

public class BasePresenter<M extends IModel, V extends IView> implements IPresenter {
    protected final String TAG = getClass().getSimpleName();
    protected M mModel;
    protected V mRootView;

    public BasePresenter(M model, V rootView) {
        this.mModel = model;
        this.mRootView = rootView;
        onStart();
    }

    public BasePresenter(V rootView) {
        this.mRootView = rootView;
        onStart();
    }

    public BasePresenter() {
        onStart();
    }

    public void onStart() {
    }

    public void onDestroy() {
        if (this.mModel != null) {
            this.mModel.onDestroy();
        }
        this.mModel = null;
        this.mRootView = null;
    }
}
