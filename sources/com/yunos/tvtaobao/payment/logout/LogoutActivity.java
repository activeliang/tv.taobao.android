package com.yunos.tvtaobao.payment.logout;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import anetwork.channel.util.RequestConstant;
import com.ali.auth.third.core.MemberSDK;
import com.ali.auth.third.core.service.impl.CredentialManager;
import com.ali.auth.third.offline.login.LoginService;
import com.ali.auth.third.offline.login.callback.LogoutCallback;
import com.ali.user.open.tbauth.TbAuthConstants;
import com.ut.device.UTDevice;
import com.yunos.ott.sdk.core.Environment;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.payment.BuildConfig;
import com.yunos.tvtaobao.payment.MemberSDKLoginStatus;
import com.yunos.tvtaobao.payment.R;
import com.yunos.tvtaobao.payment.account.AccountUtil;
import com.yunos.tvtaobao.payment.analytics.Utils;
import com.yunos.tvtaobao.payment.broadcast.BroadcastLogin;
import com.yunos.tvtaobao.payment.request.RequestLoginCallBack;
import com.yunos.tvtaobao.payment.request.TvTaoBaoSwitchBean;
import com.yunos.tvtaobao.payment.request.TvtaobaoSwitchRequest;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import com.yunos.tvtaobao.payment.utils.TvTaoUtils;
import com.yunos.tvtaobao.payment.utils.TvtaoExtParamsUtil;
import com.zhiping.dev.android.logger.ZpLogger;
import com.zhiping.tvtao.payment.alipay.request.ReleaseContractRequest;
import com.zhiping.tvtao.payment.alipay.request.base.BaseMtopRequest;
import com.zhiping.tvtao.payment.alipay.request.base.MtopResponse;
import com.zhiping.tvtao.payment.utils.MtopHelper;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopFinishEvent;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.intf.Mtop;
import org.json.JSONObject;

public class LogoutActivity extends Activity {
    /* access modifiers changed from: private */
    public static String TAG = "LogoutActivity";
    /* access modifiers changed from: private */
    public String nick;
    /* access modifiers changed from: private */
    public TextView tvLogout;
    /* access modifiers changed from: private */
    public TextView tvNotLogout;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        initView();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.tvLogout.requestFocus();
        Utils.utPageAppear("page_logoff", "page_logoff");
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        Utils.utPageDisAppear("page_logoff");
    }

    private void initView() {
        this.tvLogout = (TextView) findViewById(R.id.tv_logout);
        this.tvNotLogout = (TextView) findViewById(R.id.tv_not_logout);
        this.tvLogout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    LogoutActivity.this.tvLogout.setTextColor(LogoutActivity.this.getResources().getColor(R.color.logout_focused_color));
                } else {
                    LogoutActivity.this.tvLogout.setTextColor(LogoutActivity.this.getResources().getColor(R.color.logout_unfocused_color));
                }
            }
        });
        this.tvNotLogout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    LogoutActivity.this.tvNotLogout.setTextColor(LogoutActivity.this.getResources().getColor(R.color.logout_focused_color));
                } else {
                    LogoutActivity.this.tvNotLogout.setTextColor(LogoutActivity.this.getResources().getColor(R.color.logout_unfocused_color));
                }
            }
        });
        this.tvLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.utCustomHit("sure_logoff", Utils.getProperties());
                try {
                    if (!(CredentialManager.INSTANCE == null || CredentialManager.INSTANCE.getSession() == null || TextUtils.isEmpty(CredentialManager.INSTANCE.getSession().userid))) {
                        Mtop.instance(LogoutActivity.this).build((MtopRequest) new RequestLoginCallBack(CredentialManager.INSTANCE.getSession().userid, TvtaoExtParamsUtil.getExtParamsInterface().getExtParams(), false), (String) null).reqMethod(MethodEnum.POST).useWua().addListener(new MtopCallback.MtopFinishListener() {
                            public void onFinished(MtopFinishEvent mtopFinishEvent, Object o) {
                                ZpLogger.d(LogoutActivity.TAG, "loginSuccess callback: " + mtopFinishEvent.getMtopResponse().getDataJsonObject());
                            }
                        }).asyncRequest();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CredentialManager.INSTANCE.logout();
                LogoutActivity.this.finish();
                LoginService service = (LoginService) MemberSDK.getService(LoginService.class);
                String unused = LogoutActivity.this.nick = service.getSession().nick;
                MemberSDKLoginStatus.setLoggingOut(true);
                service.logout(new LogoutCallback() {
                    public void onSuccess() {
                        ZpLogger.d(LogoutActivity.TAG, " memberSDK loginOut succsss");
                        Toast.makeText(LogoutActivity.this, "成功登出", 0).show();
                        BroadcastLogin.sendBroadcastLogin(LogoutActivity.this, false);
                        LogoutActivity.this.getTvtaobaoSwitch();
                        MemberSDKLoginStatus.setLoggingOut(false);
                    }

                    public void onFailure(int i, String s) {
                        ZpLogger.d(LogoutActivity.TAG, " memberSDK loginOut failure");
                        MemberSDKLoginStatus.setLoggingOut(false);
                    }
                });
                MtopHelper.asycSendRequest(new ReleaseContractRequest(), new MtopHelper.MtopListener() {
                    public void onFinish(BaseMtopRequest baseMtopRequest, MtopResponse mtopResponse) {
                        ZpLogger.d(RequestConstant.ENV_TEST, "releaseContract response: " + mtopResponse.getJsonData());
                    }
                });
                AccountUtil.clearAccountInfo(LogoutActivity.this);
                AccountUtil.notifyListener(LogoutActivity.this, AccountUtil.ACTION.LOGOUT_ACTION);
            }
        });
        this.tvNotLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.utCustomHit("cancel_logoff", Utils.getProperties());
                LogoutActivity.this.finish();
            }
        });
    }

    public boolean isLogin() {
        LoginService service = (LoginService) MemberSDK.getService(LoginService.class);
        if (service == null) {
            return false;
        }
        return service.checkSessionValid();
    }

    /* access modifiers changed from: private */
    public void getTvtaobaoSwitch() {
        String appkey;
        try {
            PackageInfo packInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            if (Environment.getInstance().isYunos()) {
                appkey = (String) TvTaoSharedPerference.getSp(this, "device_appkey", "", getPackageName() + "_preferences");
                if (TextUtils.isEmpty(appkey)) {
                    appkey = Config.MOHE;
                }
            } else {
                appkey = BuildConfig.CHANNELID;
            }
            JSONObject jo = new JSONObject();
            jo.put("appkey", appkey);
            jo.put(BaseConfig.INTENT_KEY_SOURCE, TvtaobaoSwitchRequest.SOURCE);
            JSONObject jo1 = new JSONObject();
            jo1.put("type", TvtaobaoSwitchRequest.TYPE);
            jo1.put("queryParams", jo);
            JSONObject jo11 = new JSONObject();
            jo11.put("type", "globalJsonConfig");
            String[] queryParams = {jo1.toString(), jo11.toString()};
            JSONObject jo2 = new JSONObject();
            jo2.put("umToken", TvTaoUtils.getUmtoken(this));
            jo2.put("appkey", appkey);
            jo2.put("versionName", packInfo.versionName);
            jo2.put("platform", TvtaobaoSwitchRequest.PLATFORM);
            jo2.put("buyerNick", this.nick);
            jo2.put(TbAuthConstants.IP, TvTaoUtils.getIpAddress(this));
            jo2.put("utdid", UTDevice.getUtdid(this));
            mtopsdk.mtop.domain.MtopResponse response = Mtop.instance(this).build(new TvtaobaoSwitchRequest(true, queryParams, jo2.toString()), (String) null).useWua().syncRequest();
            TvTaoBaoSwitchBean tvTaoBaoSwitchBean = new TvTaoBaoSwitchBean(response.getDataJsonObject());
            TvTaoSharedPerference.saveSp(this, TvTaoSharedPerference.LOGIN23, Boolean.valueOf(tvTaoBaoSwitchBean.login2_3));
            ZpLogger.v(TAG, TAG + ".TvtaobaoSwitchRequest -->response = " + response);
            ZpLogger.v(TAG, TAG + ".TvtaobaoSwitchRequest -->switch=拿到接口开关 " + tvTaoBaoSwitchBean.login2_3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
