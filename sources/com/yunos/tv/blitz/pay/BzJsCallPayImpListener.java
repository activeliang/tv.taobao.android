package com.yunos.tv.blitz.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import com.taobao.wireless.trade.mcart.sdk.co.biz.PromotionComponent;
import com.yunos.tv.blitz.account.BzDebugLog;
import com.yunos.tv.blitz.activity.BzBaseActivity;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tv.blitz.global.BzAppConfig;
import com.yunos.tv.blitz.listener.internal.BzPayListener;
import com.yunos.tv.paysdk.AliTVPayClient;
import com.yunos.tv.paysdk.AliTVPayResult;
import java.lang.ref.WeakReference;
import org.json.JSONException;
import org.json.JSONObject;

public class BzJsCallPayImpListener implements BzPayListener {
    static final String TAG = BzJsCallPayImpListener.class.getSimpleName();

    private class BzIPayCallBack implements AliTVPayClient.IPayCallback {
        public WeakReference<BzBaseActivity> activityRef;
        public int mCallback;
        public String order;

        private BzIPayCallBack() {
            this.mCallback = -1;
            this.order = null;
            this.activityRef = null;
        }

        public void onPayProcessEnd(AliTVPayResult arg0) {
            Log.i(BzJsCallPayImpListener.TAG, "=====PayCallback:===");
            if (this.mCallback == -1 || this.order == null || this.activityRef == null || this.activityRef.get() == null) {
                BzDebugLog.e(BzJsCallPayImpListener.TAG, "error: param invalid");
            } else if (arg0 != null) {
                try {
                    boolean payResult = arg0.getPayResult();
                    String payFeedback = arg0.getPayFeedback();
                    Bundle infoBundle = arg0.getPayInformation();
                    BzDebugLog.i(BzJsCallPayImpListener.TAG, "PayCallback:===" + payResult + " payFeedback:" + payFeedback + "infobundle:" + (infoBundle == null ? "" : infoBundle.toString()));
                    BzResult result = new BzResult();
                    result.setSuccess();
                    result.addData(PromotionComponent.ORDER_TYPE, this.order);
                    result.addData("payCancel", (payResult || !payFeedback.equals("取消")) ? "fasle" : "true");
                    result.addData("payResult", payResult);
                    result.addData("payFeedback", payFeedback);
                    result.addData("payInfomation", infoBundle == null ? "" : infoBundle.toString());
                    ((BzBaseActivity) this.activityRef.get()).getBlitzContext().replyCallBack(this.mCallback, true, result.toJsonString());
                } catch (Exception e) {
                    ((BzBaseActivity) this.activityRef.get()).getBlitzContext().replyCallBack(this.mCallback, false, "");
                    e.printStackTrace();
                }
            } else {
                ((BzBaseActivity) this.activityRef.get()).getBlitzContext().replyCallBack(this.mCallback, false, "");
            }
        }
    }

    public void onPay(Context context, String param, int callback) {
        if (!(context instanceof Activity)) {
            BzDebugLog.d(TAG, "can not response to js. may leak memory");
            return;
        }
        Activity activity = (Activity) context;
        if (!(activity instanceof BzBaseActivity)) {
            BzDebugLog.d(TAG, "can not response to js. may leak memory");
            return;
        }
        BzBaseActivity bzActivity = (BzBaseActivity) activity;
        final Bundle bundle = new Bundle();
        bundle.putString("provider", "alipay");
        String order = null;
        String sign = null;
        try {
            JSONObject paramJson = new JSONObject(param);
            try {
                if (paramJson.has(PromotionComponent.ORDER_TYPE)) {
                    order = paramJson.getString(PromotionComponent.ORDER_TYPE);
                    BzDebugLog.d(TAG, "order:" + order);
                }
                if (paramJson.has("sign")) {
                    sign = paramJson.getString("sign");
                    BzDebugLog.d(TAG, "sign:" + sign);
                }
                if (order == null || sign == null) {
                    BzDebugLog.w(TAG, "param not completed");
                    bzActivity.getBlitzContext().replyCallBack(callback, false, "");
                    return;
                }
                final String paramOrder = order;
                final String paramSign = sign;
                final BzIPayCallBack payCallback = new BzIPayCallBack();
                payCallback.mCallback = callback;
                payCallback.order = paramOrder;
                payCallback.activityRef = new WeakReference<>(bzActivity);
                bzActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            new AliTVPayClient().aliTVPay(BzAppConfig.context.getContext(), paramOrder, paramSign, bundle, payCallback);
                        } catch (RemoteException e) {
                            BzDebugLog.d(BzJsCallPayImpListener.TAG, "remote exception");
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e) {
                e = e;
                JSONObject jSONObject = paramJson;
                e.printStackTrace();
                bzActivity.getBlitzContext().replyCallBack(callback, false, "");
            }
        } catch (JSONException e2) {
            e = e2;
            e.printStackTrace();
            bzActivity.getBlitzContext().replyCallBack(callback, false, "");
        }
    }
}
