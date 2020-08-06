package com.ali.auth.third.offline;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.callback.NQrCodeLoginCallback;
import com.ali.auth.third.core.callback.ResultCallback;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.model.Constants;
import com.ali.auth.third.core.model.RpcRequestCallbackWithCode;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.core.util.JSONUtils;
import com.ali.auth.third.offline.UIConfigFlow;
import com.ali.auth.third.offline.context.BridgeCallbackContext;
import com.ali.auth.third.offline.iv.DataRepository;
import com.ali.auth.third.offline.iv.SMSVerificationView;
import com.ali.auth.third.offline.login.task.LoadQrCodePicTask;
import com.ali.auth.third.offline.login.task.LoginByIVTokenTask;
import com.ali.auth.third.offline.login.task.LoginByQrCodeTask;
import com.alibaba.analytics.core.Constants;
import com.yunos.tv.alitvasr.sdk.AbstractClientManager;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NQRView extends FrameLayout {
    private static final String TAG = "NQRView";
    /* access modifiers changed from: private */
    public static long startTime;
    /* access modifiers changed from: private */
    public boolean isStopLooper;
    /* access modifiers changed from: private */
    public LinearLayout layoutErrorTips;
    /* access modifiers changed from: private */
    public LinearLayout layoutQr;
    /* access modifiers changed from: private */
    public LinearLayout layoutScanedTips;
    /* access modifiers changed from: private */
    public LinearLayout layoutSuccessedTips;
    /* access modifiers changed from: private */
    public LoginCallback loginCallback;
    /* access modifiers changed from: private */
    public TextView mErrorTips;
    Handler mHandler;
    /* access modifiers changed from: private */
    public ImageView mImageView;
    private RelativeLayout mIvLayout;
    private LinearLayout mQRView;
    /* access modifiers changed from: private */
    public NQrCodeLoginCallback nQrCodeLoginCallback;
    private Button refreshBut;

    public void setNQrCodeLoginCallback(NQrCodeLoginCallback qrCodeLoginCallback) {
        if (qrCodeLoginCallback != null) {
            this.nQrCodeLoginCallback = qrCodeLoginCallback;
        }
    }

    public void showQR(Map<String, String> params, LoginCallback loginCallback2) {
        this.loginCallback = loginCallback2;
        internalStart(Integer.parseInt(params.get("width")), Integer.parseInt(params.get("height")), this.nQrCodeLoginCallback);
    }

    public void showQR(LoginCallback loginCallback2) {
        this.loginCallback = loginCallback2;
        internalStart(getWidth(), getHeight(), this.nQrCodeLoginCallback);
    }

    /* access modifiers changed from: private */
    public void internalStart(int width, int height, final NQrCodeLoginCallback qrCodeLoginCallback) {
        QRView.mLoginCallback = new LoginCallback() {
            public void onSuccess(Session session) {
                NQRView.this.stop();
                qrCodeLoginCallback.onSuccess(session);
            }

            public void onFailure(int code, String msg) {
            }
        };
        this.mQRView.setVisibility(0);
        this.mIvLayout.setVisibility(8);
        new LoadQrCodePicTask(new ResultCallback() {
            public void onFailure(int code, String msg) {
                qrCodeLoginCallback.onFailure(code, msg);
            }

            public void onSuccess(Object o) {
                Map<String, Object> data = (Map) o;
                final String qrToken = (String) data.get("at");
                Map<String, Object> params = new HashMap<>();
                params.put("qrCodeInfo", data);
                Map<String, Object> loginRequest = new HashMap<>();
                loginRequest.put("params", params);
                final String checkStatusReq = JSONUtils.toJsonObject(loginRequest).toString();
                qrCodeLoginCallback.onQrImageLoaded(qrToken, (Bitmap) data.remove("imageBitMap"), (NQrCodeLoginCallback.NQrCodeLoginController) null);
                long unused = NQRView.startTime = System.currentTimeMillis();
                NQRView.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        if (!NQRView.this.isStopLooper) {
                            NQRView.this.qrLogin(qrToken, checkStatusReq, qrCodeLoginCallback);
                            NQRView.this.mHandler.postDelayed(this, AbstractClientManager.BIND_SERVICE_TIMEOUT);
                        }
                    }
                }, 3000);
                boolean unused2 = NQRView.this.isStopLooper = false;
            }
        }).execute(new Object[]{Integer.valueOf(width), Integer.valueOf(height), null});
    }

    /* access modifiers changed from: private */
    public void qrLogin(final String qrToken, String checkStatusReq, final NQrCodeLoginCallback qrCodeLoginCallback) {
        new LoginByQrCodeTask(new BridgeCallbackContext() {
            public void success(String retString) {
                try {
                    JSONObject status = new JSONObject(retString);
                    if (System.currentTimeMillis() - NQRView.startTime > 900000) {
                        NQRView.this.stop();
                        qrCodeLoginCallback.onQrImageStatusChanged(qrToken, 10022);
                        return;
                    }
                    qrCodeLoginCallback.onQrImageStatusChanged(qrToken, status.getInt("code"));
                    if (status.getInt("code") == 6) {
                        NQRView.this.stop();
                    }
                } catch (JSONException e) {
                    Log.e(NQRView.TAG, "check qrcode status error ", e);
                }
            }

            public void onFailure(int code, String message) {
                if (code == 13060) {
                    NQRView.this.stop();
                    NQRView.this.fetchIVTags(message);
                    return;
                }
                qrCodeLoginCallback.onFailure(code, message);
            }

            public void onFailure(String retString) {
                qrCodeLoginCallback.onFailure(1, retString);
            }

            public Activity getActivity() {
                return null;
            }
        }, true).execute(new String[]{checkStatusReq});
    }

    /* access modifiers changed from: private */
    public void stop() {
        if (this.mHandler != null) {
            this.isStopLooper = true;
        }
    }

    public NQRView(Context context) {
        this(context, (AttributeSet) null);
    }

    public NQRView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mHandler = new Handler();
        this.isStopLooper = true;
        this.nQrCodeLoginCallback = new NQrCodeLoginCallback() {
            public void onQrImageLoaded(String url, Bitmap qrImage, NQrCodeLoginCallback.NQrCodeLoginController controller) {
                NQRView.this.mImageView.setImageBitmap(qrImage);
                NQRView.this.showQrPic();
            }

            public void onQrImageStatusChanged(String url, int status) {
                switch (status) {
                    case 4:
                        return;
                    case 5:
                        NQRView.this.showScaned();
                        return;
                    case 6:
                        NQRView.this.stop();
                        NQRView.this.showError(R.string.auth_sdk_message_qr_expired);
                        return;
                    default:
                        NQRView.this.stop();
                        NQRView.this.showError(R.string.auth_sdk_message_10010_message);
                        return;
                }
            }

            public void onSuccess(Session session) {
                NQRView.this.stop();
                NQRView.this.showSuccessed();
                NQRView.this.loginCallback.onSuccess(session);
            }

            public void onFailure(int code, String msg) {
                NQRView.this.showError(R.string.auth_sdk_message_10010_message);
                NQRView.this.loginCallback.onFailure(code, msg);
            }
        };
        LayoutInflater.from(context).inflate(getLayoutId(), this);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mQRView = (LinearLayout) findViewById(R.id.ali_auth_nqrview);
        this.mImageView = (ImageView) findViewById(R.id.ali_auth_nqrview_qr_image);
        this.refreshBut = (Button) findViewById(R.id.ali_auth_nqrview_error_refresh);
        this.layoutQr = (LinearLayout) findViewById(R.id.ali_auth_nqrview_lay_qr);
        this.layoutErrorTips = (LinearLayout) findViewById(R.id.ali_auth_nqrview_lay_errortips);
        this.layoutScanedTips = (LinearLayout) findViewById(R.id.ali_auth_nqrview_lay_scanedtips);
        this.layoutSuccessedTips = (LinearLayout) findViewById(R.id.ali_auth_nqrview_lay_successedtips);
        this.mErrorTips = (TextView) findViewById(R.id.ali_auth_nqrview_error_main);
        this.refreshBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NQRView.this.layoutErrorTips.setVisibility(8);
                NQRView.this.internalStart(NQRView.this.getWidth(), NQRView.this.getHeight(), NQRView.this.nQrCodeLoginCallback);
            }
        });
        this.mIvLayout = (RelativeLayout) findViewById(R.id.ali_auth_verify_rl);
    }

    /* access modifiers changed from: private */
    public void fetchIVTags(String params) {
        try {
            JSONObject ivMessage = new JSONObject(params);
            final String scene = ivMessage.optString("scene");
            String ivToken = ivMessage.optString(Constants.PARAM_IV_TOKEN);
            final String loginToken = ivMessage.optString("login_token");
            if (TextUtils.equals("true", ivMessage.optString("nativeIv")) && !TextUtils.isEmpty(ivToken)) {
                DataRepository.fetchIVStrategys(ivToken, Constants.LogTransferLevel.HIGH, new RpcRequestCallbackWithCode() {
                    public void onSuccess(RpcResponse response) {
                        try {
                            JSONObject strategysJson = new JSONObject((String) response.returnValue);
                            JSONArray products = strategysJson.optJSONArray("products");
                            String strategyToken = strategysJson.optString("hToken");
                            if (products != null && products.length() > 0) {
                                int i = 0;
                                while (true) {
                                    if (i >= products.length()) {
                                        break;
                                    }
                                    JSONObject strategy = products.optJSONObject(i);
                                    if (TextUtils.equals(Constants.LogTransferLevel.HIGH, strategy.optString("tag"))) {
                                        JSONArray validatorList = strategy.optJSONArray("validatorList");
                                        if (validatorList != null && validatorList.length() > 0) {
                                            for (int j = 0; j < validatorList.length(); j++) {
                                                JSONObject values = validatorList.optJSONObject(j).getJSONObject("valueAttrbute");
                                                if (values != null) {
                                                    Map<String, Object> map = new HashMap<>();
                                                    map.put("mobile", values.keys().next());
                                                    map.put(com.ali.auth.third.core.model.Constants.PARAM_IV_TOKEN, strategyToken);
                                                    map.put("iv_type", Constants.LogTransferLevel.HIGH);
                                                    map.put("scene", scene);
                                                    NQRView.this.showIVPage(map, scene, loginToken);
                                                    return;
                                                }
                                            }
                                        }
                                    } else {
                                        i++;
                                    }
                                }
                            }
                        } catch (Throwable th) {
                        }
                        NQRView.this.showError(R.string.auth_sdk_message_10010_message);
                        NQRView.this.loginCallback.onFailure(1001, "");
                    }

                    public void onSystemError(String code, RpcResponse response) {
                        NQRView.this.showError(R.string.auth_sdk_message_10010_message);
                        NQRView.this.loginCallback.onFailure(1002, "");
                    }

                    public void onError(String code, RpcResponse response) {
                        if (response != null && response.code == 13078) {
                            try {
                                String havanaIVToken = new JSONObject((String) response.returnValue).optString(com.ali.auth.third.core.model.Constants.PARAM_HAVANA_IV_TOKEN);
                                new LoginByIVTokenTask((Activity) null, new LoginCallback() {
                                    public void onSuccess(Session session) {
                                        NQRView.this.showSuccessed();
                                        NQRView.this.loginCallback.onSuccess(session);
                                    }

                                    public void onFailure(int code, String msg) {
                                        NQRView.this.showError(R.string.auth_sdk_message_10010_message);
                                        NQRView.this.loginCallback.onFailure(1003, "");
                                    }
                                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{loginToken, scene, "havana_iv_token=" + havanaIVToken});
                                return;
                            } catch (Throwable th) {
                            }
                        }
                        NQRView.this.showError(R.string.auth_sdk_message_10010_message);
                        NQRView.this.loginCallback.onFailure(1004, "");
                    }
                });
            }
        } catch (JSONException e) {
        }
    }

    public void showIVPage(Map params, final String scene, final String loginToken) {
        SMSVerificationView smsVerificationView;
        this.mQRView.setVisibility(8);
        this.mIvLayout.setVisibility(0);
        if (UIConfigFlow.IVFlow.customizeSMSVerificationView != null) {
            smsVerificationView = UIConfigFlow.IVFlow.customizeSMSVerificationView;
        } else {
            smsVerificationView = new SMSVerificationView(getContext());
        }
        this.mIvLayout.addView(smsVerificationView, new FrameLayout.LayoutParams(-1, -1));
        invalidate();
        smsVerificationView.init(params);
        smsVerificationView.setResultCallback(new ResultCallback<String>() {
            public void onSuccess(String ivToken) {
                new LoginByIVTokenTask((Activity) null, new LoginCallback() {
                    public void onSuccess(Session session) {
                        NQRView.this.showSuccessed();
                        NQRView.this.loginCallback.onSuccess(session);
                    }

                    public void onFailure(int code, String msg) {
                        NQRView.this.showError(R.string.auth_sdk_message_10010_message);
                        NQRView.this.loginCallback.onFailure(1003, "");
                    }
                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{loginToken, scene, "havana_iv_token=" + ivToken});
            }

            public void onFailure(int code, String msg) {
                NQRView.this.showError(R.string.auth_sdk_message_10010_message);
                NQRView.this.loginCallback.onFailure(code, msg);
            }
        });
    }

    /* access modifiers changed from: private */
    public void reset() {
        this.mQRView.setVisibility(0);
        this.mIvLayout.setVisibility(8);
        this.mIvLayout.removeAllViews();
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return R.layout.ali_auth_nqrview;
    }

    /* access modifiers changed from: private */
    public void showQrPic() {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                NQRView.this.reset();
                NQRView.this.layoutQr.setVisibility(0);
                NQRView.this.layoutErrorTips.setVisibility(8);
                NQRView.this.layoutScanedTips.setVisibility(8);
                NQRView.this.layoutSuccessedTips.setVisibility(8);
            }
        });
    }

    /* access modifiers changed from: private */
    public void showError(final int textId) {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                NQRView.this.reset();
                NQRView.this.layoutQr.setVisibility(8);
                NQRView.this.layoutScanedTips.setVisibility(8);
                NQRView.this.mErrorTips.setText(textId);
                NQRView.this.layoutErrorTips.setVisibility(0);
                NQRView.this.layoutSuccessedTips.setVisibility(8);
            }
        });
    }

    /* access modifiers changed from: private */
    public void showScaned() {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                NQRView.this.layoutQr.setVisibility(8);
                NQRView.this.layoutErrorTips.setVisibility(8);
                NQRView.this.layoutSuccessedTips.setVisibility(8);
                NQRView.this.layoutScanedTips.setVisibility(0);
            }
        });
    }

    /* access modifiers changed from: private */
    public void showSuccessed() {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                NQRView.this.reset();
                NQRView.this.layoutQr.setVisibility(8);
                NQRView.this.layoutErrorTips.setVisibility(8);
                NQRView.this.layoutScanedTips.setVisibility(8);
                NQRView.this.layoutSuccessedTips.setVisibility(0);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
        if (this.mImageView != null) {
            this.mImageView = null;
        }
    }
}
