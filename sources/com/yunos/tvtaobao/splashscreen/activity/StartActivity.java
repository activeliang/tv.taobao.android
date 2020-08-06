package com.yunos.tvtaobao.splashscreen.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.SPMConfig;
import com.yunos.tv.core.config.SystemConfig;
import com.yunos.tv.core.util.ActivityPathRecorder;
import com.yunos.tv.core.util.BitMapUtil;
import com.yunos.tv.core.util.NetWorkUtil;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.dialog.util.DialogUtil;
import com.yunos.tvtaobao.biz.request.bo.LoadingBo;
import com.yunos.tvtaobao.biz.util.FileUtil;
import com.yunos.tvtaobao.biz.util.MD5Util;
import com.yunos.tvtaobao.biz.util.StringUtil;
import com.yunos.tvtaobao.biz.util.TimeUtil;
import com.yunos.tvtaobao.splashscreen.R;
import com.yunos.tvtaobao.splashscreen.service.LoadingService;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StartActivity extends Activity {
    /* access modifiers changed from: private */
    public static final String TAG = StartActivity.class.getSimpleName();
    private static final String mPageName = "OpenLoading";
    private LoadingBo currentLoadingBo;
    private int duration = 0;
    private boolean flag = false;
    /* access modifiers changed from: private */
    public TextView loadingAppVersion;
    /* access modifiers changed from: private */
    public FrameLayout loadingLayout;
    /* access modifiers changed from: private */
    public boolean toHome = true;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        ZpLogger.d(TAG, ".onCreate(" + savedInstanceState + ") and getIntent:" + getIntent());
        super.onCreate(savedInstanceState);
        setContentView(generateView());
        findViews();
        transmitIntent(getIntent());
    }

    /* access modifiers changed from: protected */
    public View generateView() {
        View rtn = LayoutInflater.from(this).inflate(R.layout.ytm_activity_loading, (ViewGroup) null);
        rtn.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        return rtn;
    }

    /* access modifiers changed from: protected */
    public void transmitIntent(Intent intent) {
        new Thread(new Runnable() {
            public void run() {
                StartActivity.this.displayImage();
            }
        }).start();
        startService(new Intent(this, LoadingService.class));
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    private void findViews() {
        this.loadingLayout = (FrameLayout) findViewById(R.id.loading_layout);
        this.loadingAppVersion = (TextView) findViewById(R.id.loading_app_version);
    }

    /* access modifiers changed from: private */
    public void displayImage() {
        BitmapDrawable mBitmap;
        String loadingJson = FileUtil.read(this, getFilesDir() + WVNativeCallbackUtil.SEPERATER + "loading_cache_json");
        ZpLogger.v(TAG, "displayImage loadingJsonCache result:" + loadingJson);
        if (TextUtils.isEmpty(loadingJson)) {
            setBackgroundImage((BitmapDrawable) null);
            return;
        }
        new ArrayList();
        List<LoadingBo> cacheListLoading = JSON.parseArray(loadingJson, LoadingBo.class);
        BitmapDrawable mBitmap2 = null;
        int i = 0;
        while (true) {
            if (i >= cacheListLoading.size()) {
                break;
            }
            LoadingBo mLoadingBo = cacheListLoading.get(i);
            if (mLoadingBo != null && !TextUtils.isEmpty(mLoadingBo.getStartTime()) && !TextUtils.isEmpty(mLoadingBo.getEndTime()) && TimeUtil.isBteenStartAndEnd(mLoadingBo.getStartTime(), mLoadingBo.getEndTime())) {
                String fileName = "loading/" + mLoadingBo.getMd5();
                File file = new File(getFilesDir(), fileName);
                String md5 = MD5Util.getFileMD5(file);
                if (!TextUtils.isEmpty(md5)) {
                    md5 = md5.toUpperCase();
                }
                ZpLogger.i("md5", "md5:" + md5);
                try {
                    ZpLogger.i("fileName", "fileName:" + getFilesDir() + WVNativeCallbackUtil.SEPERATER + fileName);
                    this.currentLoadingBo = mLoadingBo;
                    mBitmap = new BitmapDrawable(FileUtil.getBitmap(this, file));
                } catch (Exception e) {
                    e = e;
                    e.printStackTrace();
                    i++;
                }
                try {
                    if (mLoadingBo.getDuration() > 0) {
                        this.duration = mLoadingBo.getDuration() * 1000;
                    }
                    mBitmap2 = mBitmap;
                } catch (Exception e2) {
                    e = e2;
                    mBitmap2 = mBitmap;
                    e.printStackTrace();
                    i++;
                }
            }
            i++;
        }
        setBackgroundImage(mBitmap2);
    }

    private void setBackgroundImage(BitmapDrawable mBitmapDrawable) {
        if (mBitmapDrawable == null) {
            mBitmapDrawable = new BitmapDrawable(BitMapUtil.readBmp(this, R.drawable.ytm_ui2_loading));
        }
        final BitmapDrawable bmpDrawable = mBitmapDrawable;
        runOnUiThread(new Runnable() {
            public void run() {
                ZpLogger.i(StartActivity.TAG, "setBackgroundImage for loadingLayout");
                StartActivity.this.loadingLayout.setBackgroundDrawable(bmpDrawable);
                if (!TextUtils.isEmpty(SystemConfig.APP_VERSION)) {
                    StartActivity.this.loadingAppVersion.setText(SystemConfig.APP_VERSION);
                }
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        if (!NetWorkUtil.isNetWorkAvailable()) {
                            new DialogUtil(StartActivity.this).showNetworkErrorDialog(true);
                        } else {
                            StartActivity.this.goHomeAndExit();
                        }
                    }
                }, 1000);
            }
        });
    }

    /* access modifiers changed from: private */
    public void goHomeAndExit() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (StartActivity.this.toHome) {
                    Intent intent = new Intent();
                    intent.putExtra(ActivityPathRecorder.INTENTKEY_PATHRECORDER_URI, Uri.parse("tvtaobao://home"));
                    intent.setClassName(StartActivity.this, BaseConfig.SWITCH_TO_HOME_ACTIVITY);
                    StartActivity.this.startActivity(intent);
                }
            }
        }, (long) this.duration);
    }

    public void enterUT() {
        Utils.utPageAppear(mPageName, mPageName);
        ZpLogger.i(TAG, ".enterUT end mPageName=OpenLoading");
    }

    /* access modifiers changed from: protected */
    public void exitUT() {
        try {
            if (!TextUtils.isEmpty(mPageName)) {
                ZpLogger.i(TAG, ".exitUI TBS=updatePageProperties(" + initTBSProperty(true) + ")");
                Utils.utUpdatePageProperties(mPageName, initTBSProperty(true));
                Utils.utPageDisAppear(mPageName);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        enterUT();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        exitUT();
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        if (!this.toHome) {
            Intent intent = new Intent();
            intent.putExtra(ActivityPathRecorder.INTENTKEY_PATHRECORDER_URI, Uri.parse("tvtaobao://home"));
            intent.setClassName(this, BaseConfig.SWITCH_TO_HOME_ACTIVITY);
            startActivity(intent);
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.flag = true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ZpLogger.e(TAG, keyCode + "" + "toHomePage=false");
        if (keyCode == 23) {
            Utils.updateNextPageProperties(SPMConfig.EVENT_PAGE_LOADING_BOTTON);
            Utils.utControlHit("", "Click_OpenLoading", initTBSProperty(false));
            Intent intent = new Intent();
            if (!(intent == null || this.currentLoadingBo == null || StringUtil.isEmpty(this.currentLoadingBo.getUri()))) {
                this.toHome = false;
                intent.setData(Uri.parse(this.currentLoadingBo.getUri()));
                startActivityForResult(intent, 767);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 767 && this.flag) {
            Intent intent = new Intent();
            intent.putExtra(ActivityPathRecorder.INTENTKEY_PATHRECORDER_URI, Uri.parse("tvtaobao://home"));
            intent.setClassName(this, BaseConfig.SWITCH_TO_HOME_ACTIVITY);
            startActivity(intent);
            finish();
        }
    }

    public Map<String, String> initTBSProperty(Boolean page) {
        Map<String, String> p = Utils.getProperties((String) null, (String) null, (String) null);
        if (!TextUtils.isEmpty(Config.getDeviceAppKey(this))) {
            p.put("appkey", Config.getDeviceAppKey(this));
        }
        if (CoreApplication.getLoginHelper(CoreApplication.getApplication()).isLogin()) {
            p.put("is_login", "1");
        } else {
            p.put("is_login", "0");
        }
        p.put("uuid", CloudUUIDWrapper.getCloudUUID());
        if (page.booleanValue()) {
            p.put("spm-cnt", SPMConfig.PAGE_ELEME_ACCOUNT_AUTH);
        } else {
            p.put(SPMConfig.SPM, SPMConfig.EVENT_PAGE_LOADING_BOTTON);
        }
        if (this.currentLoadingBo != null && !StringUtil.isEmpty(this.currentLoadingBo.getShopIdStr())) {
            p.put("shop_id", this.currentLoadingBo.getShopIdStr());
        }
        if (this.currentLoadingBo != null && !StringUtil.isEmpty(this.currentLoadingBo.getItemIdStr())) {
            p.put("item_id", this.currentLoadingBo.getItemIdStr());
        }
        if (this.currentLoadingBo != null && !StringUtil.isEmpty(this.currentLoadingBo.getZpAdId())) {
            p.put("zpAdid", this.currentLoadingBo.getZpAdId());
        }
        if (this.currentLoadingBo != null && !StringUtil.isEmpty(this.currentLoadingBo.getZpAdCT())) {
            p.put(BaseConfig.zpAdCT, this.currentLoadingBo.getZpAdCT());
        }
        return p;
    }
}
