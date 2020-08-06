package com.yunos.tvtaobao.homebundle.activity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;
import com.alibaba.analytics.core.Constants;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.yunos.RunMode;
import com.yunos.alitvcompliance.TVCompliance;
import com.yunos.alitvcompliance.types.RetCode;
import com.yunos.alitvcompliance.types.RetData;
import com.yunos.tv.blitz.activity.BzBaseActivity;
import com.yunos.tv.blitz.view.BlitzBridgeSurfaceView;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.aqm.ActGloballyUnique;
import com.yunos.tv.core.aqm.TvTaoHome;
import com.yunos.tv.core.common.DeviceJudge;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.common.User;
import com.yunos.tv.core.config.AppInfo;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.PageMapConfig;
import com.yunos.tv.core.config.TvOptionsChannel;
import com.yunos.tv.core.config.TvOptionsConfig;
import com.yunos.tv.core.debug.DebugTestBuilder;
import com.yunos.tv.core.util.DeviceUtil;
import com.yunos.tv.core.util.TaokeConst;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.activity.CoreActivity;
import com.yunos.tvtaobao.biz.activity.TaoBaoBlitzActivity;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.controller.Update;
import com.yunos.tvtaobao.biz.h5.plugin.GuardPlugin;
import com.yunos.tvtaobao.biz.h5.plugin.TvTaoSdkGetSubkeyPlugin;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.biz.request.info.GlobalConfigInfo;
import com.yunos.tvtaobao.homebundle.R;
import com.yunos.tvtaobao.homebundle.bean.DetainmentDataBuider;
import com.yunos.tvtaobao.homebundle.dialog.DetainMentDialog;
import com.yunos.tvtaobao.homebundle.h5.plugin.EventBlitz;
import com.yunos.tvtaobao.homebundle.h5.plugin.UpdatePlugin;
import com.yunos.tvtaobao.payment.utils.ChannelUtils;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import com.yunos.tvtaobao.tvsdk.utils.SystemProUtils;
import com.zhiping.dev.android.logcat.ZpLogCat;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.json.JSONObject;

@ActGloballyUnique
@TvTaoHome
public class HomeActivity extends TaoBaoBlitzActivity {
    private String TAG = TaokeConst.REFERER_HOME_ACTIVITY;
    private EventBlitz blitz;
    private DetainmentDataBuider detainmentDataBuider;
    private GuardPlugin guardPlugin;
    private boolean isDestroy = false;
    private boolean isIntercept = false;
    private int mBackFromLikeRequestCode = 777;
    private DetainMentDialog mDetainMentDialog;
    private boolean mIsFromOutside;
    private UpdatePlugin mUpdatePlugin;
    private DebugTestBuilder testBuilder;
    private TvTaoSdkGetSubkeyPlugin tvTaoSdkGetSubkeyPlugin;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerLoginListener();
        loadPage();
        this.testBuilder = new DebugTestBuilder(this);
    }

    /* access modifiers changed from: protected */
    public void registerPlugins() {
        super.registerPlugins();
        this.guardPlugin = new GuardPlugin(new WeakReference(this));
        this.mUpdatePlugin = new UpdatePlugin(new WeakReference(this));
        if (ChannelUtils.isThisTag(ChannelUtils.HY)) {
            this.tvTaoSdkGetSubkeyPlugin = new TvTaoSdkGetSubkeyPlugin(new WeakReference(this));
        }
    }

    private void loadPage() {
        boolean blockWhParams;
        String page = "";
        String gatedUrl = getIntent().getStringExtra("page");
        ZpLogger.v(this.TAG, this.TAG + ".onCreate.gatedUrl = " + gatedUrl);
        if (!TextUtils.isEmpty(gatedUrl)) {
            page = gatedUrl;
        } else {
            String cacheUrl = SharePreferences.getString("page");
            if (!TextUtils.isEmpty(cacheUrl)) {
                page = cacheUrl;
            }
        }
        if (TextUtils.isEmpty(page)) {
            page = PageMapConfig.getPageUrlMap().get("home");
        }
        String appkey = SharePreferences.getString("device_appkey", "");
        String brandName = SharePreferences.getString("device_brandname", "");
        StringBuilder params = new StringBuilder();
        if (page == null) {
            page = "";
        } else if (page.contains(WVUtils.URL_DATA_CHAR)) {
            params.append("&");
        } else {
            params.append(WVUtils.URL_DATA_CHAR);
        }
        if (GlobalConfigInfo.getInstance().getGlobalConfig() != null) {
            blockWhParams = GlobalConfigInfo.getInstance().getGlobalConfig().isBlockWhParams();
        } else {
            Object localBlock = TvTaoSharedPerference.getSp(this, "blockWhParams", Boolean.FALSE);
            blockWhParams = (localBlock instanceof Boolean) && ((Boolean) localBlock).booleanValue();
        }
        String appendKey = blockWhParams ? "appkey=" : "wh_appkey=";
        if (TextUtils.isEmpty(appkey)) {
            params.append(appendKey);
            params.append(Config.getChannel());
        } else {
            params.append(appendKey);
            params.append(appkey);
        }
        if (RunMode.isYunos()) {
            if (!TextUtils.isEmpty(brandName)) {
                params.append("&brand=");
                params.append(brandName);
            } else {
                params.append("&brand=null");
            }
        }
        if (GlobalConfigInfo.getInstance().getGlobalConfig() != null && GlobalConfigInfo.getInstance().getGlobalConfig().isBeta()) {
            String appendBeta = blockWhParams ? "&beta=true" : "&wh_beta=true";
            String appendVersion = blockWhParams ? "&version=" : "&wh_version=";
            params.append(appendBeta);
            params.append(appendVersion + AppInfo.getAppVersionName());
        }
        String page2 = page + params.toString();
        if (TextUtils.isEmpty(page2)) {
            Toast.makeText(getApplicationContext(), getString(R.string.ytbv_not_found_page), 0).show();
            finish();
            return;
        }
        String page3 = getCompliancePage(page2);
        Bundle bundle = getIntent().getExtras();
        this.mIsFromOutside = false;
        if (bundle != null) {
            this.mIsFromOutside = bundle.getBoolean(CoreActivity.INTENT_KEY_IS_FROM_OUTSIDE, false);
        }
        ZpLogger.v(this.TAG, this.TAG + ".onCreate.page = " + page3 + ", mIsFromOutside = " + this.mIsFromOutside);
        this.blitz = new EventBlitz(new WeakReference(this));
        onInitH5View(page3);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        TvOptionsConfig.setTvOptionsChannel(TvOptionsChannel.OTHER);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        unRegisterLoginListener();
    }

    public void onPageLoadFinished(String param) {
        ZpLogger.v(this.TAG, this.TAG + ".onCreate.loadfinish = " + param);
        super.onPageLoadFinished(param);
    }

    /* access modifiers changed from: protected */
    public String getCompliancePage(String page) {
        if (!RunMode.needDomainCompliance()) {
            return page;
        }
        String host = Utils.parseHost(page);
        RetData retData = null;
        if (!TextUtils.isEmpty(host)) {
            try {
                retData = TVCompliance.getComplianceDomain(host);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (retData == null) {
            return page;
        }
        ZpLogger.d(this.TAG, "Converted domain host " + host);
        ZpLogger.d(this.TAG, "Converted domain is " + retData.toString());
        if (retData.getCode() == RetCode.Success || retData.getCode() == RetCode.Default) {
            ZpLogger.d(this.TAG, "Converted domain is " + retData.getResult());
            String domainName = retData.getResult();
            String replace = page.replace(host, domainName);
            ZpLogger.d(this.TAG, "Original page is " + page);
            ZpLogger.d(this.TAG, "replace page is " + replace);
            if (!domainName.equals(host)) {
                return replace.replaceFirst("http://", "https://");
            }
            return replace;
        }
        ZpLogger.d(this.TAG, "Original domain is " + retData.getResult());
        String license = SystemProUtils.getLicense();
        String domainName2 = host;
        if ("tvos.taobao.com".equals(host)) {
            if ("1".equals(license)) {
                domainName2 = "tb.cp12.wasu.tv";
            }
            if (Constants.LogTransferLevel.L7.equals(license)) {
                domainName2 = "tb.cp12.ott.cibntv.net";
            }
        }
        if (!domainName2.equals(host)) {
            return page.replace(host, domainName2).replaceFirst("http://", "https://");
        }
        return page.replace(host, domainName2);
    }

    public void loadWithUrl(String url) {
        super.loadWithUrl(url);
        try {
            Field field = BzBaseActivity.class.getDeclaredField("mBlitzBridgeView");
            field.setAccessible(true);
            ((BlitzBridgeSurfaceView) field.get(this)).setZOrderOnTop(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadWithUrlAndPagedata(String url, String pagedata) {
        super.loadWithUrlAndPagedata(url, pagedata);
        try {
            Field field = BzBaseActivity.class.getDeclaredField("mBlitzBridgeView");
            field.setAccessible(true);
            ((BlitzBridgeSurfaceView) field.get(this)).setZOrderOnTop(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void anaylisysTaoke() {
        if (CoreApplication.getLoginHelper(this).isLogin()) {
            BusinessRequest.getBusinessRequest().requestTaokeJHSListAnalysis(DeviceUtil.initMacAddress(this), User.getNick(), "tvtaobao", TaokeConst.REFERER_HOME_ACTIVITY, (RequestListener<JSONObject>) null);
        }
    }

    public void onWebviewPageDone(String url) {
        ZpLogger.d(this.TAG, "homeActivity onWebviewPageDone " + url);
        if (this.mDetainMentDialog == null && !this.isDestroy) {
            this.mDetainMentDialog = new DetainMentDialog(this);
        }
        initDetainment();
        anaylisysTaoke();
        Update.syncCheckUpdateAndShowPage();
    }

    public void interceptBack(boolean isIntercept2) {
        this.isIntercept = isIntercept2;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (ChannelUtils.isThisChannel(ChannelUtils.DLT) && (event.getKeyCode() == 183 || event.getKeyCode() == 184 || event.getKeyCode() == 185 || event.getKeyCode() == 186)) {
            return true;
        }
        if (keyCode != 4 || this.isIntercept) {
            if (this.testBuilder != null) {
                this.testBuilder.onKeyAction(keyCode);
            }
            this.isIntercept = false;
            return super.onKeyDown(keyCode, event);
        } else if (Utils.isFastClick()) {
            return true;
        } else {
            enterDetainMent();
            return true;
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 4 && !this.isIntercept) {
            return true;
        }
        this.isIntercept = false;
        return super.onKeyUp(keyCode, event);
    }

    private boolean enterDetainMent() {
        ZpLogger.i(this.TAG, "enterDetainMent --> isFinishing = " + isFinishing());
        if (this.mDetainMentDialog != null && !this.mDetainMentDialog.hasData()) {
            handleExit();
            return true;
        } else if (this.isDestroy || this.mDetainMentDialog == null || this.mDetainMentDialog.isShowing()) {
            handleExit();
            return true;
        } else {
            this.mDetainMentDialog.show();
            return true;
        }
    }

    public void handleExit() {
        if (this.mDetainMentDialog != null) {
            this.mDetainMentDialog.onDestroy();
        }
        if (this.mDetainMentDialog != null && this.mDetainMentDialog.isShowing() && this.mDetainMentDialog.isShowing()) {
            this.mDetainMentDialog.dismiss();
        }
        exit();
    }

    private void exit() {
        ActivityManager activityManager = (ActivityManager) getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY);
        ZpLogger.i(this.TAG, "exit --> mIsFromOutside = " + this.mIsFromOutside);
        if (!this.mIsFromOutside || !DeviceJudge.isMemTypeHigh()) {
            ZpLogger.i(this.TAG, "exit --> killProcess ");
            clearAllOpenedActivity(this);
        }
        finish();
        if (exitHandler != null) {
            exitHandler.removeCallbacksAndMessages((Object) null);
            exitHandler.postDelayed(new Runnable() {
                public void run() {
                    HomeActivity.this.exitChildProcess();
                    CoreApplication.getApplication().clear();
                    Process.killProcess(Process.myPid());
                }
            }, 1000);
        }
    }

    /* access modifiers changed from: protected */
    public void onLogin() {
        super.onLogin();
    }

    /* access modifiers changed from: protected */
    public void onLogout() {
    }

    /* access modifiers changed from: protected */
    public void onLoginCancel() {
        super.onLoginCancel();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.isDestroy = true;
        if (this.testBuilder != null) {
            this.testBuilder.onDestroy();
            this.testBuilder = null;
        }
        if (ZpLogCat.getInstance((Application) null).getTurnOnState()) {
            try {
                ZpLogCat.getInstance((Application) null).uploadLogs(true, true, -1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public boolean isTbs() {
        return false;
    }

    private void initDetainment() {
        if (this.detainmentDataBuider == null) {
            this.detainmentDataBuider = new DetainmentDataBuider(this);
            this.detainmentDataBuider.checkDetainmentData();
        }
    }

    /* access modifiers changed from: protected */
    public void initBlitzContext(String initStr, int type) {
        super.initBlitzContext(initStr, type);
        try {
            Field field = BzBaseActivity.class.getDeclaredField("ActivityList");
            field.setAccessible(true);
            ((ArrayList) field.get((Object) null)).remove(this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }

    public boolean isHomeActivity() {
        return true;
    }

    public void homeToGuessYouLike(String bgUrl) {
        Intent intent = new Intent();
        intent.setClassName(this, BaseConfig.SWITCH_TO_GUESS_YOU_LIKE_ACTIVITY);
        intent.putExtra("guess_like_from", "home");
        intent.putExtra("guess_like_bg_url", bgUrl);
        startActivityForResult(intent, this.mBackFromLikeRequestCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.mBackFromLikeRequestCode && resultCode == -1) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
