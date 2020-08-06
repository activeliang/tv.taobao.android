package com.ali.auth.third.offline.iv;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.ali.auth.third.core.callback.ResultCallback;
import com.ali.auth.third.core.model.Constants;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.offline.R;
import com.ali.auth.third.offline.iv.AliUserSmsCodeView;
import com.alibaba.analytics.core.Constants;
import java.util.Map;

public class SMSVerificationView extends ScrollView implements SmsVerifyFormView {
    public static final String PAGE_NAME = "Page_Reg";
    public static final String TAG = "login.numAuthReg";
    protected IVParam mIVParams;
    protected SMSVerifyPresenter mPresenter;
    private ResultCallback<String> mResultCallback;
    protected CountDownButton mSendSMSCodeBtn;
    protected AliUserSmsCodeView mSmsCodeView;

    public SMSVerificationView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SMSVerificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mIVParams = new IVParam();
        LayoutInflater.from(context).inflate(getLayoutId(), this);
    }

    public void init(Map<String, Object> params) {
        if (params != null) {
            this.mIVParams.mobileNum = (String) params.get("mobile");
            this.mIVParams.ivToken = (String) params.get(Constants.PARAM_IV_TOKEN);
            this.mIVParams.validatorTag = Constants.LogTransferLevel.HIGH;
        }
        this.mPresenter = new SMSVerifyPresenter(this);
        initViews();
        this.mPresenter.sendSMS(this.mIVParams);
    }

    public void setResultCallback(ResultCallback resultCallback) {
        this.mResultCallback = resultCallback;
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return R.layout.ali_auth_sms_verification;
    }

    /* access modifiers changed from: protected */
    public void initViews() {
        if (!TextUtils.isEmpty(this.mIVParams.mobileNum)) {
            ((TextView) getRootView().findViewById(R.id.ali_auth_mobile_tv)).setText(getContext().getString(R.string.ali_auth_sms_veri_title, new Object[]{" " + this.mIVParams.mobileNum + " "}));
        }
        this.mSmsCodeView = (AliUserSmsCodeView) getRootView().findViewById(R.id.ali_auth_sms_code_view);
        this.mSmsCodeView.setOnCompletedListener(new AliUserSmsCodeView.OnCompletedListener() {
            public void onCompleted(String text) {
                SMSVerificationView.this.submitRegisterForm();
            }
        });
        this.mSmsCodeView.focus();
        this.mSendSMSCodeBtn = (CountDownButton) getRootView().findViewById(R.id.ali_auth_send_smscode_btn);
        this.mSendSMSCodeBtn.setGetCodeTitle(R.string.ali_auth_verification_reGetCode);
        this.mSendSMSCodeBtn.setTickTitleRes(R.string.ali_auth_sms_code_success_hint);
        this.mSendSMSCodeBtn.startCountDown(60000, 1000);
        this.mSendSMSCodeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SMSVerificationView.this.sendCodeAction();
            }
        });
    }

    public void onSendSMSSuccess(long millSecond) {
        this.mSendSMSCodeBtn.startCountDown(millSecond, 1000);
    }

    public void onSMSSendFail(RpcResponse response) {
        if (this.mSmsCodeView != null) {
            this.mSmsCodeView.clearText();
        }
    }

    /* access modifiers changed from: protected */
    public void sendCodeAction() {
        if (this.mSmsCodeView != null) {
            this.mSmsCodeView.clearText();
        }
        try {
            onSendSMSAction();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onSendSMSAction() {
        this.mPresenter.sendSMS(this.mIVParams);
    }

    public void submitRegisterForm() {
        if (this.mSmsCodeView != null) {
            this.mIVParams.checkCode = this.mSmsCodeView.getText();
            this.mPresenter.verifyCode(this.mIVParams);
        }
    }

    public String getPageName() {
        return "";
    }

    public void onVerifySuccess(String loginToken) {
        if (this.mResultCallback != null) {
            this.mResultCallback.onSuccess(loginToken);
        }
    }

    public void onVerifyFail(int code, String message) {
        Context context = getContext();
        if (TextUtils.isEmpty(message)) {
            message = getContext().getString(R.string.aliusersdk_network_error);
        }
        Toast.makeText(context, message, 0).show();
    }
}
