package com.yunos.tvtaobao.payment.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import com.ali.auth.third.core.callback.ResultCallback;
import com.ali.auth.third.core.model.Constants;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.offline.iv.IVParam;
import com.ali.auth.third.offline.iv.SMSVerifyPresenter;
import com.ali.auth.third.offline.iv.SmsVerifyFormView;
import com.alibaba.analytics.core.Constants;
import com.alibaba.fastjson.JSON;
import com.bftv.fui.constantplugin.Constant;
import com.tvtaobao.android.loginverify.VerifyLayout;
import com.tvtaobao.android.ui3.widget.CustomDialog;
import com.tvtaobao.android.values.StringUtil;
import com.yunos.tv.core.config.SPMConfig;
import com.yunos.tvtaobao.payment.R;
import com.yunos.tvtaobao.payment.analytics.Utils;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class CustomSMSVerificationView extends FrameLayout {
    public static final String PAGE_NAME = "Page_Reg";
    public static final String TAG = CustomSMSVerificationView.class.getSimpleName();
    /* access modifiers changed from: private */
    public boolean SMSErr;
    protected IVParam mIVParams;
    protected SMSVerifyPresenter mPresenter;
    /* access modifiers changed from: private */
    public ResultCallback<String> mResultCallback;
    /* access modifiers changed from: private */
    public VerifyLayout verifyLayout;

    public CustomSMSVerificationView(Context context) {
        this(context, (AttributeSet) null);
    }

    public CustomSMSVerificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.SMSErr = false;
        this.mIVParams = new IVParam();
        LayoutInflater.from(context).inflate(R.layout.custom_sms_verification_view, this);
        this.verifyLayout = (VerifyLayout) findViewById(R.id.verify_layout);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (!this.SMSErr || (event.getKeyCode() != 19 && event.getKeyCode() != 20 && event.getKeyCode() != 21 && event.getKeyCode() != 22)) {
            return super.dispatchKeyEvent(event);
        }
        return true;
    }

    public void init(Map<String, Object> params) {
        if (params != null) {
            this.mIVParams.mobileNum = (String) params.get("mobile");
            this.mIVParams.ivToken = (String) params.get(Constants.PARAM_IV_TOKEN);
            this.mIVParams.validatorTag = Constants.LogTransferLevel.HIGH;
        }
        this.mPresenter = new SMSVerifyPresenter(new SmsVerifyFormView() {
            public void onSendSMSSuccess(long millSecond) {
                ZpLogger.d(CustomSMSVerificationView.TAG, ".onSendSMSSuccess " + millSecond);
                CustomSMSVerificationView.this.verifyLayout.countDownStart((int) (millSecond / 1000), 6);
            }

            public void onSMSSendFail(RpcResponse rpcResponse) {
                ZpLogger.d(CustomSMSVerificationView.TAG, ".onSMSSendFail " + (rpcResponse != null ? rpcResponse.returnValue : Constant.NULL));
                String msg = rpcResponse.message;
                String rspJson = null;
                if (TextUtils.isEmpty(msg)) {
                    try {
                        msg = new JSONObject(rpcResponse.returnValue.toString()).optString("message");
                        rspJson = JSON.toJSONString(rpcResponse);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                CustomDialog.Builder builder = new CustomDialog.Builder(CustomSMSVerificationView.this.getContext());
                builder.setType(CustomDialog.Type.no_btn);
                if (!TextUtils.isEmpty(msg)) {
                    rspJson = msg;
                }
                builder.setTwoLineResultMessage(rspJson);
                builder.create().show();
                CustomSMSVerificationView.this.verifyLayout.sendCodeError(6, msg);
                boolean unused = CustomSMSVerificationView.this.SMSErr = true;
            }

            public String getPageName() {
                return "Page_Reg";
            }

            public void onVerifySuccess(String s) {
                ZpLogger.d(CustomSMSVerificationView.TAG, ".onVerifySuccess " + s);
                CustomDialog.Builder builder = new CustomDialog.Builder(CustomSMSVerificationView.this.getContext());
                builder.setIcon(R.drawable.ui3wares_success_icon);
                builder.setHasIcon(true);
                builder.setType(CustomDialog.Type.no_btn);
                builder.setTwoLineResultMessage("验证成功");
                builder.create().show();
                if (CustomSMSVerificationView.this.mResultCallback != null) {
                    CustomSMSVerificationView.this.mResultCallback.onSuccess(s);
                }
            }

            public void onVerifyFail(int i, String message) {
                ZpLogger.d(CustomSMSVerificationView.TAG, ".onVerifyFail " + i + "," + message);
                CustomDialog.Builder builder = new CustomDialog.Builder(CustomSMSVerificationView.this.getContext());
                builder.setType(CustomDialog.Type.no_btn);
                if (TextUtils.isEmpty(message)) {
                    message = CustomSMSVerificationView.this.getContext().getString(R.string.aliusersdk_network_error);
                }
                builder.setTwoLineResultMessage(message);
                builder.create().show();
            }
        });
        initViews();
        this.mPresenter.sendSMS(this.mIVParams);
    }

    /* access modifiers changed from: protected */
    public void initViews() {
        this.verifyLayout.setPhoneNum(StringUtil.maskMobile(this.mIVParams.mobileNum));
        this.verifyLayout.setActionListener(new VerifyLayout.ActionListener() {
            public void onCountDownFinish() {
                CustomSMSVerificationView.this.verifyLayout.clearTxt(6);
            }

            public void onSureClick(String s) {
                CustomSMSVerificationView.this.mIVParams.checkCode = s;
                CustomSMSVerificationView.this.mPresenter.verifyCode(CustomSMSVerificationView.this.mIVParams);
            }

            public void onReVerifyClick() {
                Map<String, String> properties = Utils.getProperties();
                properties.put(SPMConfig.SPM, com.yunos.tvtaobao.payment.utils.SPMConfig.Retrieve_validation);
                Utils.utControlHit(Utils.utGetCurrentPage(), "Retrieve_validation", properties);
                CustomSMSVerificationView.this.mPresenter.sendSMS(CustomSMSVerificationView.this.mIVParams);
            }
        });
        Map<String, String> properties = Utils.getProperties();
        properties.put(SPMConfig.SPM, com.yunos.tvtaobao.payment.utils.SPMConfig.Expore_MessageVerification);
        Utils.utCustomHit(Utils.utGetCurrentPage(), "Expore_MessageVerification", properties);
    }

    public ResultCallback<String> getResultCallback() {
        return this.mResultCallback;
    }

    public void setResultCallback(ResultCallback<String> mResultCallback2) {
        this.mResultCallback = mResultCallback2;
    }
}
