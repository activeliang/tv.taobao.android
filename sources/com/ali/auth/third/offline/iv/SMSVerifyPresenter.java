package com.ali.auth.third.offline.iv;

import com.ali.auth.third.core.model.Constants;
import com.ali.auth.third.core.model.RpcRequestCallbackWithCode;
import com.ali.auth.third.core.model.RpcResponse;
import com.alibaba.wireless.security.SecExceptionCode;
import org.json.JSONObject;

public class SMSVerifyPresenter {
    private static final String TAG = SMSVerifyPresenter.class.getSimpleName();
    /* access modifiers changed from: private */
    public SmsVerifyFormView mViewer;

    public SMSVerifyPresenter(SmsVerifyFormView viewer) {
        this.mViewer = viewer;
    }

    public void sendSMS(IVParam param) {
        DataRepository.sendSMSCode(param.ivToken, param.validatorTag, param.mobileNum, new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse response) {
                if (SMSVerifyPresenter.this.mViewer != null && SMSVerifyPresenter.this.mViewer != null) {
                    SMSVerifyPresenter.this.mViewer.onSendSMSSuccess(60000);
                }
            }

            public void onSystemError(String code, RpcResponse response) {
                if (SMSVerifyPresenter.this.mViewer != null && SMSVerifyPresenter.this.mViewer != null) {
                    SMSVerifyPresenter.this.mViewer.onSMSSendFail(response);
                }
            }

            public void onError(String code, RpcResponse response) {
                if (SMSVerifyPresenter.this.mViewer != null && SMSVerifyPresenter.this.mViewer != null) {
                    SMSVerifyPresenter.this.mViewer.onSMSSendFail(response);
                }
            }
        });
    }

    public void verifyCode(IVParam param) {
        DataRepository.checkCommonCode(param.ivToken, param.checkCode, param.mobileNum, new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse response) {
                if (SMSVerifyPresenter.this.mViewer != null && SMSVerifyPresenter.this.mViewer != null) {
                    try {
                        SMSVerifyPresenter.this.mViewer.onVerifySuccess(new JSONObject((String) response.returnValue).optString(Constants.PARAM_HAVANA_IV_TOKEN));
                    } catch (Throwable e) {
                        e.printStackTrace();
                        SMSVerifyPresenter.this.mViewer.onVerifyFail(SecExceptionCode.SEC_ERROE_OPENSDK_DECODE_FAILED, "数据异常");
                    }
                }
            }

            public void onSystemError(String code, RpcResponse response) {
                if (SMSVerifyPresenter.this.mViewer != null && SMSVerifyPresenter.this.mViewer != null) {
                    SMSVerifyPresenter.this.mViewer.onVerifyFail(response == null ? 1101 : response.code, response == null ? "" : response.message);
                }
            }

            public void onError(String code, RpcResponse response) {
                if (SMSVerifyPresenter.this.mViewer != null && SMSVerifyPresenter.this.mViewer != null) {
                    String message = "";
                    if (response != null) {
                        try {
                            message = new JSONObject((String) response.returnValue).optString("message");
                        } catch (Throwable th) {
                        }
                    }
                    SMSVerifyPresenter.this.mViewer.onVerifyFail(response == null ? SecExceptionCode.SEC_ERROE_OPENSDK_INVALID_PARAM : response.code, message);
                }
            }
        });
    }
}
