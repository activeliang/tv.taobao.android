package com.yunos.tvtaobao.payment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.callback.NQrCodeLoginCallback;
import com.ali.auth.third.core.callback.ResultCallback;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.offline.LoginFragment;
import com.ali.auth.third.offline.login.task.LoginByIVTokenTask;
import com.ali.auth.third.offline.webview.AuthWebView;
import com.bftv.fui.constantplugin.Constant;
import com.google.zxing.WriterException;
import com.tvtaobao.android.runtime.RtBaseEnv;
import com.tvtaobao.android.ui3.widget.CustomDialog;
import com.ut.mini.UTAnalytics;
import com.yunos.ott.sdk.core.Environment;
import com.yunos.tv.core.common.CoreIntentKey;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.payment.account.AccountUtil;
import com.yunos.tvtaobao.payment.analytics.Utils;
import com.yunos.tvtaobao.payment.broadcast.BroadcastLogin;
import com.yunos.tvtaobao.payment.config.DebugConfig;
import com.yunos.tvtaobao.payment.qrcode.QRCodeManager;
import com.yunos.tvtaobao.payment.utils.CloudUUIDWrapper;
import com.yunos.tvtaobao.payment.utils.SPMConfig;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import com.yunos.tvtaobao.payment.utils.UtilsDistance;
import com.yunos.tvtaobao.payment.view.AlphaDialog;
import com.yunos.tvtaobao.payment.view.CustomNQRView;
import com.yunos.tvtaobao.payment.view.CustomSMSVerificationView;
import com.zhiping.dev.android.logger.ZpLogger;
import com.zhiping.tvtao.payment.alipay.task.AlipayAuthTask;
import com.zhiping.tvtao.payment.alipay.task.AlipayQRResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.mtop.domain.MtopRequest;

public class CustomLoginFragment extends LoginFragment implements ViewPager.OnPageChangeListener {
    /* access modifiers changed from: private */
    public static String TAG = "memberSDK";
    /* access modifiers changed from: private */
    public static String mPageName = "Page_login";
    private AlipayAuthTask alipayAuthTask;
    /* access modifiers changed from: private */
    public Bitmap alipayLogo;
    private String appkey;
    /* access modifiers changed from: private */
    public NQrCodeLoginCallback.NQrCodeLoginController controller;
    /* access modifiers changed from: private */
    public AlphaDialog dialog;
    /* access modifiers changed from: private */
    public Handler handler;
    /* access modifiers changed from: private */
    public ImageView ivAlipayLoginQR;
    /* access modifiers changed from: private */
    public ImageView ivQRScanSuccess;
    /* access modifiers changed from: private */
    public ImageView ivTaobaoLoginQR;
    /* access modifiers changed from: private */
    public NQrCodeLoginCallback loginCallback = new NQrCodeLoginCallback() {
        public void onQrImageLoaded(String qrToken, Bitmap qrBitmap, NQrCodeLoginCallback.NQrCodeLoginController nQrCodeLoginController) {
            NQrCodeLoginCallback.NQrCodeLoginController unused = CustomLoginFragment.this.controller = nQrCodeLoginController;
            Bitmap unused2 = CustomLoginFragment.this.taoBaoLogo = BitmapFactory.decodeResource(CustomLoginFragment.this.getResources(), R.drawable.icon_taobao_qr_small);
            CustomLoginFragment.this.ivTaobaoLoginQR.setImageBitmap(QRCodeManager.create2DCode(qrBitmap, UtilsDistance.dp2px(CustomLoginFragment.this.getContext(), 347), UtilsDistance.dp2px(CustomLoginFragment.this.getContext(), 347), CustomLoginFragment.this.taoBaoLogo, UtilsDistance.dp2px(CustomLoginFragment.this.getContext(), 55), UtilsDistance.dp2px(CustomLoginFragment.this.getContext(), 55)));
        }

        public void onQrImageStatusChanged(String qrToken, int status) {
            if (status == 5) {
                CustomLoginFragment.this.onQRScanCodeSuccess(status, "扫码成功");
            } else if (status == 6) {
                CustomLoginFragment.this.onQROrdverdue(status, "超时");
            } else if (status != 4) {
                CustomLoginFragment.this.showLoginFailure("状态码：" + status);
            }
        }

        public void onSuccess(Session session) {
            PaymentApplication.setLoginFragmentHasShowed(true);
            CustomLoginFragment.this.onQRLoginSuccess(session);
            if (CustomLoginFragment.this.getActivity() != null) {
                CustomLoginFragment.this.getActivity().finish();
            }
            ZpLogger.d(CustomLoginFragment.TAG, " memberSDK login succsss" + session.openSid);
        }

        public void onFailure(int code, String msg) {
            ZpLogger.d(CustomLoginFragment.TAG, " memberSDK onFailure " + code + " " + msg);
            CustomLoginFragment.this.onQRLoginFailure(code, msg);
        }
    };
    /* access modifiers changed from: private */
    public AlphaDialog loginFailureDialog;
    private String mFrom;
    private String mHuoDong;
    private MtopRequest mtopRequest;
    CustomNQRView nqrView;
    private PackageInfo packInfo;
    CustomSMSVerificationView smsVerificationView;
    /* access modifiers changed from: private */
    public Bitmap taoBaoLogo;
    private ArrayList<View> viewList;
    private ViewPagerAdapter viewPagerAdapter;
    /* access modifiers changed from: private */
    public ViewPager viewPagerQR;

    /* access modifiers changed from: protected */
    public void initViews(View view) {
        try {
            this.packInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (Environment.getInstance().isYunos()) {
            this.appkey = (String) TvTaoSharedPerference.getSp(getActivity(), "device_appkey", "", "com.yunos.tvtaobao_preferences");
            if (TextUtils.isEmpty(this.appkey)) {
                this.appkey = Config.MOHE;
            }
        } else {
            this.appkey = BuildConfig.CHANNELID;
        }
        this.mFrom = getStringFromUri(getActivity().getIntent(), "from", "");
        this.mHuoDong = getStringFromUri(getActivity().getIntent(), CoreIntentKey.URI_HUODONG, (String) null);
        this.handler = new Handler();
        super.initViews(view);
        initViewPager(view);
        startTaobaoQR();
        this.dialog = new AlphaDialog(getContext(), LayoutInflater.from(getContext()).inflate(R.layout.dialog_qr_invalid, (ViewGroup) null));
        if (DebugConfig.whetherIsMonkey()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (CustomLoginFragment.this != null && CustomLoginFragment.this.getActivity() != null) {
                        CustomLoginFragment.this.getActivity().finish();
                    }
                }
            }, 30000);
        }
    }

    /* access modifiers changed from: protected */
    public AuthWebView createTaeWebView() {
        return null;
    }

    private void initViewPager(View view) {
        this.viewPagerQR = (ViewPager) view.findViewById(R.id.vp_qr);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View taobaoLoginQR = inflater.inflate(R.layout.user_taobao_login_qr, (ViewGroup) null);
        View alipayLoginQR = inflater.inflate(R.layout.user_alipay_login_qr, (ViewGroup) null);
        this.ivTaobaoLoginQR = (ImageView) taobaoLoginQR.findViewById(R.id.iv_qr);
        this.ivQRScanSuccess = (ImageView) taobaoLoginQR.findViewById(R.id.iv_qr_scan_success);
        this.ivAlipayLoginQR = (ImageView) alipayLoginQR.findViewById(R.id.iv_qr);
        this.viewList = new ArrayList<>();
        this.viewList.add(taobaoLoginQR);
        this.viewList.add(alipayLoginQR);
        this.viewPagerAdapter = new ViewPagerAdapter(this.viewList);
        this.viewPagerQR.setAdapter(this.viewPagerAdapter);
        this.viewPagerQR.addOnPageChangeListener(this);
        this.nqrView = (CustomNQRView) view.findViewById(R.id.nqrview);
        this.smsVerificationView = (CustomSMSVerificationView) view.findViewById(R.id.custom_sms_verification_view);
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.user_login_fragment_test;
    }

    public void onQRLoginSuccess(Session session) {
        if (session != null && !TextUtils.isEmpty(session.nick)) {
            UTAnalytics.getInstance().updateUserAccount(session.nick, session.userid, (String) null);
        }
        this.nqrView.cancel();
        BroadcastLogin.sendBroadcastLogin(getActivity(), true);
        AccountUtil.saveAccountInfo(getActivity(), session);
        AccountUtil.notifyListener(getActivity(), AccountUtil.ACTION.LOGIN_ACTION);
    }

    /* access modifiers changed from: private */
    public void showLoginFailure(final String msg) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    View dialogView = View.inflate(CustomLoginFragment.this.getContext() == null ? CustomLoginFragment.this.getActivity() == null ? CustomLoginFragment.this.getActivity().getApplicationContext() : CustomLoginFragment.this.getActivity() : CustomLoginFragment.this.getContext(), R.layout.dialog_login_fail, (ViewGroup) null);
                    TextView textView = (TextView) dialogView.findViewById(R.id.f66tv);
                    TextView tv_msg = (TextView) dialogView.findViewById(R.id.msg);
                    if (!TextUtils.isEmpty(msg) && tv_msg != null) {
                        tv_msg.setText(msg);
                    }
                    if (CustomLoginFragment.this.loginFailureDialog != null) {
                        CustomLoginFragment.this.loginFailureDialog.dismiss();
                    }
                    AlphaDialog unused = CustomLoginFragment.this.loginFailureDialog = new AlphaDialog(CustomLoginFragment.this.getContext() == null ? CustomLoginFragment.this.getActivity() == null ? CustomLoginFragment.this.getActivity().getApplicationContext() : CustomLoginFragment.this.getActivity() : CustomLoginFragment.this.getContext(), dialogView);
                    CustomLoginFragment.this.loginFailureDialog.setCancelable(false);
                    CustomLoginFragment.this.loginFailureDialog.show();
                    CustomLoginFragment.this.handler.postDelayed(new Runnable() {
                        public void run() {
                            if (CustomLoginFragment.this.loginFailureDialog != null) {
                                CustomLoginFragment.this.loginFailureDialog.dismiss();
                            }
                            AlphaDialog unused = CustomLoginFragment.this.loginFailureDialog = null;
                        }
                    }, 3000);
                }
            });
        }
    }

    public void onQRLoginFailure(int code, String msg) {
        ZpLogger.d(TAG, "onQRLoginFailure" + code);
        showLoginFailure(msg);
    }

    public void onQRScanCodeSuccess(int code, String msg) {
        ZpLogger.d(TAG, "onQRScanCodeSuccess   " + code);
        PaymentApplication.setLoginFragmentHasShowed(true);
        if (getActivity() != null && !getActivity().isFinishing()) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    CustomLoginFragment.this.ivQRScanSuccess.setVisibility(0);
                }
            });
        }
    }

    public void onQROrdverdue(int code, String msg) {
        ZpLogger.d(TAG, "onQROrdverdue  code = " + code + "，msg = " + msg);
        if (getActivity() != null && !getActivity().isFinishing()) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (CustomLoginFragment.this.viewPagerQR.getCurrentItem() == 0 && CustomLoginFragment.this.getActivity() != null && !CustomLoginFragment.this.getActivity().isFinishing()) {
                        CustomLoginFragment.this.dialog.show();
                    }
                    CustomLoginFragment.this.startTaobaoQR();
                    CustomLoginFragment.this.handler.postDelayed(new Runnable() {
                        public void run() {
                            CustomLoginFragment.this.dialog.dismiss();
                            CustomLoginFragment.this.ivQRScanSuccess.setVisibility(8);
                        }
                    }, 3000);
                    Utils.utCustomHit(CustomLoginFragment.mPageName, CustomLoginFragment.this.viewPagerQR.getCurrentItem() == 0 ? "Expore_taobao_login_Disuse" : "Expore_zhifubao_login_Disuse", CustomLoginFragment.this.viewPagerQR.getCurrentItem() == 0 ? CustomLoginFragment.this.initTBSProperty(SPMConfig.CUSTOM_LOGIN_TB_DISUSE) : CustomLoginFragment.this.initTBSProperty(SPMConfig.CUSTOM_LOGIN_ZHIFUBAO_DISUSE));
                }
            });
        }
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void onPageSelected(int position) {
        if (position == 1) {
            startAlipay();
            Utils.utCustomHit(mPageName, "Expore_zhifubao", initTBSProperty(SPMConfig.CUSTOM_LOGIN_ZHIFUBAO_EXPORE));
            return;
        }
        if (this.alipayAuthTask != null) {
            this.alipayAuthTask.setListener((AlipayAuthTask.AlipayAuthTaskListener) null);
            this.alipayAuthTask.cancel(true);
            this.alipayAuthTask = null;
        }
        Utils.utCustomHit(mPageName, "Expore_taobao", initTBSProperty(SPMConfig.CUSTOM_LOGIN_TB_EXPORE));
    }

    public Map<String, String> initTBSProperty(String spm) {
        Map<String, String> p = Utils.getProperties();
        p.put("uuid", CloudUUIDWrapper.getCloudUUID());
        p.put("appkey", this.appkey);
        if (TextUtils.isEmpty(this.mFrom)) {
            p.put("from_channel", this.mFrom);
        }
        if (TextUtils.isEmpty(this.mHuoDong)) {
            p.put("from_act", this.mHuoDong);
        }
        p.put(CoreIntentKey.URI_FROM_APP, "tvtaobao" + this.packInfo.versionName);
        p.put("is_login", "0");
        p.put("version", this.packInfo.versionName);
        p.put(com.yunos.tv.core.config.SPMConfig.SPM, spm);
        ZpLogger.i(TAG, TAG + ".uuid=" + CloudUUIDWrapper.getCloudUUID() + ", appkey=" + this.appkey + ", from_channel=" + this.mFrom + ", mHuoDong = " + this.mHuoDong + ", from_app = " + "tvtaobao" + this.packInfo.versionName + ", is_login = " + "0" + ", version = " + this.packInfo.versionName);
        return p;
    }

    public static String getStringFromUri(Intent intent, String name, String defaultValue) {
        if (intent == null) {
            return null;
        }
        String value = defaultValue;
        Uri uri = intent.getData();
        if (uri != null) {
            try {
                value = uri.getQueryParameter(name);
                if (TextUtils.isEmpty(value)) {
                    value = defaultValue;
                }
            } catch (Exception e) {
                value = defaultValue;
            }
        }
        ZpLogger.i(TAG, TAG + ".getString 2==> name=" + name + ", value=" + value + ", defaultValue=" + defaultValue + ", uri = " + uri);
        return value;
    }

    /* access modifiers changed from: private */
    @MainThread
    public void startTaobaoQR() {
        this.nqrView.cancel();
        this.controller = null;
        if (this.nqrView != null) {
            final LoginCallback placeHolderCallBack = new LoginCallback() {
                public void onSuccess(Session session) {
                    ZpLogger.d(CustomLoginFragment.TAG, CustomLoginFragment.TAG + (session != null ? session.toString() : Constant.NULL));
                    RtBaseEnv.broadcast(new RtBaseEnv.Msg(AccountUtil.EVENT_LOGIN, new AccountUtil.LoginResult(true, session)));
                    if (CustomLoginFragment.this.loginCallback != null) {
                        CustomLoginFragment.this.loginCallback.onSuccess(session);
                    }
                }

                public void onFailure(int i, String s) {
                    ZpLogger.i(CustomLoginFragment.TAG, CustomLoginFragment.TAG + "(" + i + "," + s + ")");
                    if (CustomLoginFragment.this.loginCallback != null) {
                        CustomLoginFragment.this.loginCallback.onFailure(i, s);
                    }
                }
            };
            this.nqrView.setOnShowIVPage(new CustomNQRView.OnShowIVPage() {
                public void showIVPage(Map params, final String scene, final String loginToken) {
                    CustomLoginFragment.this.smsVerificationView.setVisibility(0);
                    CustomLoginFragment.this.smsVerificationView.init(params);
                    CustomLoginFragment.this.smsVerificationView.setResultCallback(new ResultCallback<String>() {
                        public void onSuccess(String ivToken) {
                            new LoginByIVTokenTask(CustomLoginFragment.this.getActivity(), new LoginCallback() {
                                public void onSuccess(Session session) {
                                    placeHolderCallBack.onSuccess(session);
                                }

                                public void onFailure(int code, String msg) {
                                    CustomDialog.Builder builder = new CustomDialog.Builder(CustomLoginFragment.this.getContext());
                                    builder.setType(CustomDialog.Type.no_btn);
                                    builder.setTwoLineResultMessage(msg);
                                    builder.create().show();
                                    placeHolderCallBack.onFailure(1003, "");
                                }
                            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{loginToken, scene, "havana_iv_token=" + ivToken});
                        }

                        public void onFailure(int code, String msg) {
                            CustomDialog.Builder builder = new CustomDialog.Builder(CustomLoginFragment.this.getContext());
                            builder.setType(CustomDialog.Type.no_btn);
                            builder.setTwoLineResultMessage(msg);
                            builder.create().show();
                            placeHolderCallBack.onFailure(code, msg);
                        }
                    });
                }
            });
            Map<String, String> params = new HashMap<>();
            params.put("width", "300");
            params.put("height", "300");
            this.nqrView.setNQrCodeLoginCallback(this.loginCallback);
            this.nqrView.showQR(params, placeHolderCallBack);
        }
    }

    @MainThread
    private void startAlipay() {
        if (this.alipayAuthTask != null) {
            this.alipayAuthTask.cancel(true);
            this.alipayAuthTask.setListener((AlipayAuthTask.AlipayAuthTaskListener) null);
        }
        this.alipayAuthTask = new AlipayAuthTask();
        this.alipayAuthTask.setListener(new AlipayAuthTask.AlipayAuthTaskListener() {
            public void onReceivedAlipayAuthStateNotify(final AlipayAuthTask.AlipayAuthTaskResult result) {
                if (result.getStep() == AlipayAuthTask.STEP.GEN_QRCODE && result.getStatus() == AlipayAuthTask.STATUS.SUCCESS) {
                    String url = result.getQrResult() instanceof AlipayQRResult ? result.getQrResult().qrCode : null;
                    try {
                        if (CustomLoginFragment.this.alipayLogo == null) {
                            Bitmap unused = CustomLoginFragment.this.alipayLogo = BitmapFactory.decodeResource(CustomLoginFragment.this.getResources(), R.drawable.payment_icon_alipay);
                        }
                        CustomLoginFragment.this.ivAlipayLoginQR.setImageBitmap(QRCodeManager.create2DCode(url, UtilsDistance.dp2px(CustomLoginFragment.this.getContext(), 347), UtilsDistance.dp2px(CustomLoginFragment.this.getContext(), 347), CustomLoginFragment.this.alipayLogo, UtilsDistance.dp2px(CustomLoginFragment.this.getContext(), 55), UtilsDistance.dp2px(CustomLoginFragment.this.getContext(), 55)));
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } else if (result.getStep() != AlipayAuthTask.STEP.TOKEN_GET) {
                } else {
                    if (result.getStatus() == AlipayAuthTask.STATUS.SUCCESS) {
                        AccountUtil.doLoginByAlipayToken(CustomLoginFragment.this.getActivity(), result.getTokenResult().token, new AccountUtil.AlipayLoginListener() {
                            public void onLoginSuccess(Session session) {
                                CustomLoginFragment.this.getActivity().finish();
                            }

                            public void onLoginFailure(int i, String s) {
                                if (result != null && result.getTokenResult() != null) {
                                    CustomLoginFragment.this.showLoginFailure(result.getTokenResult().subMsg);
                                }
                            }
                        });
                    } else if (result != null && result.getTokenResult() != null) {
                        CustomLoginFragment.this.showLoginFailure(result.getTokenResult().subMsg);
                    }
                }
            }
        });
        this.alipayAuthTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[0]);
    }

    public void onPageScrollStateChanged(int state) {
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.alipayAuthTask != null) {
            this.alipayAuthTask.setListener((AlipayAuthTask.AlipayAuthTaskListener) null);
            this.alipayAuthTask.cancel(true);
        }
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages((Object) null);
        }
        this.nqrView.cancel();
        this.controller = null;
        MemberSDKLoginStatus.compareAndSetLogin(true, false);
    }

    /* access modifiers changed from: protected */
    public void enterUT() {
        if (TextUtils.isEmpty(mPageName)) {
            throw new IllegalArgumentException("The PageName was null and TBS is open");
        }
        Utils.utPageAppear(mPageName, mPageName);
    }

    /* access modifiers changed from: protected */
    public void exitUT() {
        if (!TextUtils.isEmpty(mPageName)) {
            Map<String, String> p = Utils.getProperties();
            p.put("spm-cnt", SPMConfig.CUSTOM_LOGIN_FRAGMENT);
            try {
                p.put("uuid", CloudUUIDWrapper.getCloudUUID());
                p.put("appkey", this.appkey);
                if (TextUtils.isEmpty(this.mFrom)) {
                    p.put("from_channel", this.mFrom);
                }
                if (TextUtils.isEmpty(this.mHuoDong)) {
                    p.put("from_act", this.mHuoDong);
                }
                p.put(CoreIntentKey.URI_FROM_APP, "tvtaobao" + this.packInfo.versionName);
                p.put("is_login", "0");
                p.put("version", this.packInfo.versionName);
                ZpLogger.i(TAG, TAG + ".uuid=" + CloudUUIDWrapper.getCloudUUID() + ", appkey=" + this.appkey + ", from_channel=" + this.mFrom + ", mHuoDong = " + this.mHuoDong + ", from_app = " + "tvtaobao" + this.packInfo.versionName + ", is_login = " + "0" + ", version = " + this.packInfo.versionName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Utils.utUpdatePageProperties(mPageName, p);
            Utils.utPageDisAppear(mPageName);
        }
    }

    public void onResume() {
        super.onResume();
        enterUT();
    }

    public void onPause() {
        super.onPause();
        exitUT();
    }
}
