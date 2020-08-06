package com.yunos.tvtaobao.biz.activity;

import android.os.Bundle;
import com.yunos.tv.core.common.ImageHandleManager;
import com.zhiping.dev.android.logger.ZpLogger;

public abstract class SdkHaveTabBaseActivity extends TabBaseActivity {
    private final String BASETAG = "TbBaseActivity";
    protected final String TAOBAO_SDK_TV_COUPON_KEYWORD = "TVHongbao";
    protected boolean isDestroyActivity = false;

    /* access modifiers changed from: protected */
    public abstract String getTag();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZpLogger.d("TbBaseActivity", "TAG  ---> " + getTag() + ";   ---- >  onCreate;  this =  " + this);
        this.isDestroyActivity = false;
    }

    /* access modifiers changed from: protected */
    public void onStartActivityNetWorkError() {
        showNetworkErrorDialog(false);
    }

    public boolean isHasDestroyActivity() {
        return this.isDestroyActivity;
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        ZpLogger.d("TbBaseActivity", "TAG  ---> " + getTag() + ";   ---- >  onPause;  this =  " + this);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        ZpLogger.d("TbBaseActivity", "TAG   ---> " + getTag() + ";   ---- >   onDestroy;  this =  " + this);
        removeNetworkOkDoListener();
        this.isDestroyActivity = true;
        ImageHandleManager.getImageHandleManager(getApplicationContext()).purge();
    }

    /* access modifiers changed from: protected */
    public String getAppTag() {
        return "Tb";
    }
}
