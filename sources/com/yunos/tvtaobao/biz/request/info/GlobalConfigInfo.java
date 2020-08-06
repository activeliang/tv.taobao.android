package com.yunos.tvtaobao.biz.request.info;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.ali.user.open.tbauth.TbAuthConstants;
import com.alibaba.fastjson.JSONObject;
import com.taobao.detail.DisplayTypeConstants;
import com.ut.device.UTDevice;
import com.ut.mini.UTAnalytics;
import com.yunos.CloudUUIDWrapper;
import com.yunos.ott.sdk.core.Environment;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.RtEnv;
import com.yunos.tv.core.account.LoginHelperImpl;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.common.User;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.util.SharedPreferencesUtils;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import com.yunos.tvtaobao.payment.BuildConfig;
import com.yunos.tvtaobao.payment.request.GlobalConfig;
import com.yunos.tvtaobao.payment.request.TvTaoBaoSwitchBean;
import com.yunos.tvtaobao.payment.request.TvtaobaoSwitchRequest;
import com.yunos.tvtaobao.payment.utils.ErrorReport;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import com.yunos.tvtaobao.payment.utils.TvTaoUtils;
import com.zhiping.dev.android.logger.ZpLogger;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopFinishEvent;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.intf.Mtop;
import org.json.JSONException;

public class GlobalConfigInfo {
    /* access modifiers changed from: private */
    public static String TAG = "GlobalConfigInfo";
    private static GlobalConfigInfo instance = null;
    private String appkey;
    /* access modifiers changed from: private */
    public GlobalConfig mGlobalConfig;
    private PackageInfo packInfo;
    /* access modifiers changed from: private */
    public boolean updateUserFlag = false;

    private GlobalConfigInfo() {
    }

    public static GlobalConfigInfo getInstance() {
        if (instance == null) {
            synchronized (GlobalConfigInfo.class) {
                if (instance == null) {
                    instance = new GlobalConfigInfo();
                }
            }
        }
        return instance;
    }

    public GlobalConfig getGlobalConfig() {
        return this.mGlobalConfig;
    }

    public void getTvtaobaoSwitch(Context context) {
        getTvtaobaoSwitch(context, (Runnable) null);
    }

    public void getTvtaobaoSwitch(final Context context, final Runnable task) {
        try {
            Mtop.instance(context).build(new TvtaobaoSwitchRequest(true, getQueryParams(context), getExtParams(context)), (String) null).useWua().addListener(new MtopCallback.MtopFinishListener() {
                public void onFinished(MtopFinishEvent mtopFinishEvent, Object o) {
                    MtopResponse response = mtopFinishEvent.getMtopResponse();
                    ZpLogger.v(GlobalConfigInfo.TAG, GlobalConfigInfo.TAG + ".TvtaobaoSwitchRequest -->response = " + response);
                    TvTaoBaoSwitchBean tvTaoBaoSwitchBean = new TvTaoBaoSwitchBean(response.getDataJsonObject());
                    RtEnv.set(RtEnv.CURRENT_USER_WHETHER_SAFE, Boolean.valueOf(tvTaoBaoSwitchBean.is_safe));
                    RtEnv.set(RtEnv.CURRENT_USER_WHETHER_SHOW_COMMONT, Boolean.valueOf(tvTaoBaoSwitchBean.is_show_commont));
                    TvTaoSharedPerference.saveSp(context, TvTaoSharedPerference.LOGIN23, Boolean.valueOf(tvTaoBaoSwitchBean.login2_3));
                    GlobalConfig unused = GlobalConfigInfo.this.mGlobalConfig = tvTaoBaoSwitchBean.globalJsonConfig;
                    boolean isBlitzShop = GlobalConfigInfo.this.mGlobalConfig.isBlitzShop();
                    boolean isMoHeLogOn = GlobalConfigInfo.this.mGlobalConfig.isMoHeLogOn();
                    boolean isLianMengLogOn = GlobalConfigInfo.this.mGlobalConfig.isLianMengLogOn();
                    boolean isYiTiJiLogOn = GlobalConfigInfo.this.mGlobalConfig.isYiTiJiLogOn();
                    int low_memory_page_show_degrade = GlobalConfigInfo.this.mGlobalConfig.getLow_memory_page_show_degrade();
                    int low_memory_page_stack_degrade = GlobalConfigInfo.this.mGlobalConfig.getLow_memory_page_stack_degrade();
                    int low_memory_img_show_degrade = GlobalConfigInfo.this.mGlobalConfig.getLow_memory_img_show_degrade();
                    int low_memory_widget_degrade = GlobalConfigInfo.this.mGlobalConfig.getLow_memory_widget_degrade();
                    SharePreferences.put("isBlitzShop", isBlitzShop);
                    SharePreferences.put(UpdatePreference.IS_MOHE_LOG_ON, isMoHeLogOn);
                    SharePreferences.put(UpdatePreference.IS_LIANMNEG_LOG_ON, isLianMengLogOn);
                    SharePreferences.put(UpdatePreference.IS_YITIJI_LOG_ON, isYiTiJiLogOn);
                    if (low_memory_page_show_degrade != -1) {
                        SharePreferences.put("low_memory_page_show_degrade", low_memory_page_show_degrade);
                    } else {
                        SharePreferences.rmv("low_memory_page_show_degrade");
                    }
                    if (low_memory_page_stack_degrade != -1) {
                        SharePreferences.put("low_memory_page_stack_degrade", low_memory_page_stack_degrade);
                    } else {
                        SharePreferences.rmv("low_memory_page_stack_degrade");
                    }
                    if (low_memory_img_show_degrade != -1) {
                        SharePreferences.put("low_memory_img_show_degrade", low_memory_img_show_degrade);
                    } else {
                        SharePreferences.rmv("low_memory_img_show_degrade");
                    }
                    if (low_memory_widget_degrade != -1) {
                        SharePreferences.put("low_memory_widget_degrade", low_memory_widget_degrade);
                    } else {
                        SharePreferences.rmv("low_memory_widget_degrade");
                    }
                    if (GlobalConfigInfo.this.mGlobalConfig.getChannelDegradeConfigs() != null) {
                        SharePreferences.put("channelDegradeConfigs", JSONObject.toJSONString(GlobalConfigInfo.this.mGlobalConfig.getChannelDegradeConfigs()));
                    } else {
                        SharePreferences.rmv("channelDegradeConfigs");
                    }
                    if (GlobalConfigInfo.this.mGlobalConfig.getSwitchTaobaoTVFlow() != null) {
                        SharePreferences.put("switchTaobaoTVFlow", GlobalConfigInfo.this.mGlobalConfig.getSwitchTaobaoTVFlow());
                    } else {
                        SharePreferences.rmv("switchTaobaoTVFlow");
                    }
                    SharePreferences.put("isDisasterToleranceOn", GlobalConfigInfo.this.mGlobalConfig.isDisasterToleranceOn());
                    SharePreferences.put("isBkbmUT19999On", GlobalConfigInfo.this.mGlobalConfig.isBkbmUT19999On());
                    SharePreferences.put(DisplayTypeConstants.JHS, GlobalConfigInfo.this.mGlobalConfig.getJhsConfig());
                    if (GlobalConfigInfo.this.mGlobalConfig.isDisasterToleranceOn()) {
                        SharePreferences.put("isDisasterToleranceOn", GlobalConfigInfo.this.mGlobalConfig.isDisasterToleranceOn());
                    } else {
                        SharePreferences.rmv("isDisasterToleranceOn");
                    }
                    if (GlobalConfigInfo.this.mGlobalConfig.isBkbmUT19999On()) {
                        SharePreferences.put("isBkbmUT19999On", GlobalConfigInfo.this.mGlobalConfig.isBkbmUT19999On());
                    } else {
                        SharePreferences.rmv("isBkbmUT19999On");
                    }
                    if ("yes".equals(GlobalConfigInfo.this.mGlobalConfig.getHideTaobaoTVRebate())) {
                        SharePreferences.put("hideTaobaoTVRebate", GlobalConfigInfo.this.mGlobalConfig.getHideTaobaoTVRebate());
                    } else {
                        SharePreferences.rmv("hideTaobaoTVRebate");
                    }
                    ErrorReport.getInstance().setRate(GlobalConfigInfo.this.mGlobalConfig.getErrorReportRate());
                    RtEnv.set(RtEnv.MASHANGTAO_ICON, tvTaoBaoSwitchBean.globalJsonConfig.mashangtao);
                    TvTaoSharedPerference.saveSp(CoreApplication.getApplication(), "blockWhParams", Boolean.valueOf(GlobalConfigInfo.this.mGlobalConfig.isBlockWhParams()));
                    TvTaoSharedPerference.saveSp(CoreApplication.getApplication(), "error_report", GlobalConfigInfo.this.mGlobalConfig.getErrorReportRate());
                    if (!GlobalConfigInfo.this.mGlobalConfig.isStopUpdateUserAccount() && LoginHelperImpl.getJuLoginHelper().isLogin()) {
                        if (System.currentTimeMillis() >= SharedPreferencesUtils.getUpdateUserIdFlag(context) && !GlobalConfigInfo.this.updateUserFlag) {
                            boolean unused2 = GlobalConfigInfo.this.updateUserFlag = true;
                            UTAnalytics.getInstance().updateUserAccount(User.getNick(), User.getUserId(), (String) null);
                            ZpLogger.e(GlobalConfigInfo.TAG, "UT用户信息传入：session.nick:" + User.getNick() + "++session.userid:" + User.getUserId());
                            SharedPreferencesUtils.saveUpdateUserIdFlag(context, System.currentTimeMillis() + 604800000);
                        }
                    }
                    if (task != null) {
                        new Handler(Looper.getMainLooper()).post(task);
                    }
                }
            }).asyncRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getQueryParams(Context context) throws JSONException, PackageManager.NameNotFoundException {
        if (TextUtils.isEmpty(this.appkey)) {
            getPackInfoAndAppkey(context);
        }
        org.json.JSONObject jo = new org.json.JSONObject();
        jo.put("appkey", this.appkey);
        jo.put(BaseConfig.INTENT_KEY_SOURCE, TvtaobaoSwitchRequest.SOURCE);
        org.json.JSONObject jo1 = new org.json.JSONObject();
        jo1.put("type", TvtaobaoSwitchRequest.TYPE);
        jo1.put("queryParams", jo);
        org.json.JSONObject joScoreParam = new org.json.JSONObject();
        joScoreParam.put("appKey", this.appkey);
        joScoreParam.put(BaseConfig.INTENT_KEY_SOURCE, TvtaobaoSwitchRequest.SOURCE);
        org.json.JSONObject joscore = new org.json.JSONObject();
        joscore.put("type", TvtaobaoSwitchRequest.SCORE_TYPE);
        joscore.put("queryParams", joScoreParam);
        org.json.JSONObject jo11 = new org.json.JSONObject();
        jo11.put("type", "globalJsonConfig");
        return new String[]{jo1.toString(), jo11.toString(), joscore.toString()};
    }

    private void getPackInfoAndAppkey(Context context) throws PackageManager.NameNotFoundException {
        this.packInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        this.appkey = "";
        if (Environment.getInstance().isYunos()) {
            this.appkey = (String) TvTaoSharedPerference.getSp(context, "device_appkey", "", context.getPackageName() + "_preferences");
            if (TextUtils.isEmpty(this.appkey)) {
                this.appkey = Config.MOHE;
                return;
            }
            return;
        }
        this.appkey = BuildConfig.CHANNELID;
    }

    public String getExtParams(Context context) throws JSONException, PackageManager.NameNotFoundException {
        if (TextUtils.isEmpty(this.appkey) || this.packInfo == null) {
            getPackInfoAndAppkey(context);
        }
        org.json.JSONObject jo2 = new org.json.JSONObject();
        jo2.put("umToken", TvTaoUtils.getUmtoken(context));
        jo2.put("appkey", this.appkey);
        jo2.put("versionName", this.packInfo.versionName);
        jo2.put("platform", TvtaobaoSwitchRequest.PLATFORM);
        jo2.put("buyerNick", User.getNick());
        jo2.put(TbAuthConstants.IP, TvTaoUtils.getIpAddress(context));
        jo2.put("utdid", UTDevice.getUtdid(context));
        jo2.put("wua", Config.getWua(context));
        jo2.put("isSimulator", Config.isSimulator(context));
        jo2.put("userAgent", Config.getAndroidSystem(context));
        jo2.put("uuid", CloudUUIDWrapper.getCloudUUID());
        return jo2.toString();
    }

    public boolean shouldHideTaobaoTvRebateInfo() {
        if ("yes".equals(RtEnv.get("test_hideTaobaoTVRebate"))) {
            return true;
        }
        if ((this.mGlobalConfig == null || !"yes".equals(this.mGlobalConfig.getHideTaobaoTVRebate())) && !"yes".equals(SharePreferences.getString("hideTaobaoTVRebate"))) {
            return false;
        }
        return true;
    }
}
