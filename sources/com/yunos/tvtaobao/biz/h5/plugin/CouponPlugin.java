package com.yunos.tvtaobao.biz.h5.plugin;

import com.yunos.tv.blitz.BlitzPlugin;
import com.yunos.tv.blitz.account.LoginHelper;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.common.User;
import com.yunos.tvtaobao.biz.activity.TaoBaoBlitzActivity;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import org.json.JSONException;
import org.json.JSONObject;

public class CouponPlugin {
    private static final String COUPONKEY = "couponKey";
    private static final String DATA = "data";
    private static final String ITEMID = "itemId";
    private static final String PID = "pid";
    /* access modifiers changed from: private */
    public static final String TAG = CouponPlugin.class.getSimpleName();
    private CouponJsCallback mCouponJsCallback;
    /* access modifiers changed from: private */
    public WeakReference<TaoBaoBlitzActivity> mTaoBaoBlitzActivityWeakReference;

    public CouponPlugin(WeakReference<TaoBaoBlitzActivity> taoBaoBlitzActivityWeakReference) {
        this.mTaoBaoBlitzActivityWeakReference = taoBaoBlitzActivityWeakReference;
        onInitPlugin();
    }

    private void onInitPlugin() {
        this.mCouponJsCallback = new CouponJsCallback(new WeakReference(this));
        BlitzPlugin.bindingJs("applyCoupon", this.mCouponJsCallback);
        ZpLogger.v(TAG, "CouponPlugin init finished.");
    }

    /* access modifiers changed from: private */
    public void onHandleCallApply(String param, long cbData) {
        ZpLogger.v(TAG, "onHandleCallApply --> param = " + param + ";  cbData_final = " + cbData);
        TaoBaoBlitzActivity taoBaoBlitzActivity = null;
        JSONObject couponParams = null;
        try {
            couponParams = new JSONObject(param);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        if (couponParams == null) {
            failResponseJs(BzResult.RET_PARAM_ERR, cbData);
            return;
        }
        final String itemId = couponParams.optString("itemId");
        final String couponKey = couponParams.optString(COUPONKEY);
        final String pid = couponParams.optString(PID);
        if (itemId == null || couponKey == null) {
            failResponseJs(BzResult.RET_PARAM_ERR, cbData);
            return;
        }
        if (!(this.mTaoBaoBlitzActivityWeakReference == null || this.mTaoBaoBlitzActivityWeakReference.get() == null)) {
            taoBaoBlitzActivity = (TaoBaoBlitzActivity) this.mTaoBaoBlitzActivityWeakReference.get();
        }
        if (taoBaoBlitzActivity == null) {
            failResponseJs(BzResult.RET_CLOSED, cbData);
        } else if (CoreApplication.getLoginHelper(taoBaoBlitzActivity).isLogin()) {
            final String userId = User.getUserId();
            ZpLogger.v(TAG, userId + itemId + couponKey + pid);
            if (userId != null) {
                final long j = cbData;
                taoBaoBlitzActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        CouponPlugin.this.doCouponApply(itemId, pid, couponKey, userId, j);
                    }
                });
                return;
            }
            final String str = itemId;
            final String str2 = pid;
            final String str3 = couponKey;
            final long j2 = cbData;
            CoreApplication.getLoginHelper(CoreApplication.getApplication()).addSyncLoginListener(new LoginHelper.SyncLoginListener() {
                public void onLogin(boolean isSuccess) {
                    CoreApplication.getLoginHelper(CoreApplication.getApplication()).removeSyncLoginListener(this);
                    if (isSuccess) {
                        TaoBaoBlitzActivity taoBaoBlitzActivity = null;
                        if (!(CouponPlugin.this.mTaoBaoBlitzActivityWeakReference == null || CouponPlugin.this.mTaoBaoBlitzActivityWeakReference.get() == null)) {
                            taoBaoBlitzActivity = (TaoBaoBlitzActivity) CouponPlugin.this.mTaoBaoBlitzActivityWeakReference.get();
                        }
                        if (taoBaoBlitzActivity != null) {
                            taoBaoBlitzActivity.runOnUiThread(new Runnable() {
                                public void run() {
                                    CouponPlugin.this.doCouponApply(str, str2, str3, User.getUserId(), j2);
                                }
                            });
                        } else {
                            CouponPlugin.failResponseJs(BzResult.RET_CLOSED, j2);
                        }
                    } else {
                        CouponPlugin.failResponseJs(BzResult.RET_NOT_LOGIN, j2);
                    }
                }
            });
            CoreApplication.getLoginHelper(CoreApplication.getApplication()).login(CoreApplication.getApplication());
        } else {
            failResponseJs(BzResult.RET_NOT_LOGIN, cbData);
        }
    }

    /* access modifiers changed from: private */
    public static void failResponseJs(JSONObject data, long cbData) {
        BzResult result = new BzResult("HY_FAILED");
        if (data != null) {
            result.addData("data", data);
        }
        BlitzPlugin.responseJs(false, result.toJsonString(), cbData);
    }

    /* access modifiers changed from: private */
    public static void failResponseJs(BzResult result, long cbData) {
        BlitzPlugin.responseJs(false, result.toJsonString(), cbData);
    }

    /* access modifiers changed from: private */
    public static void failResponseJs(String msg, long cbData) {
        BzResult result = new BzResult();
        if (msg != null) {
            result.setResult(msg);
        }
        BlitzPlugin.responseJs(false, result.toJsonString(), cbData);
    }

    /* access modifiers changed from: private */
    public static void successResponseJs(JSONObject data, long cbData) {
        BzResult result = new BzResult("HY_SUCCESS");
        if (data != null) {
            result.addData("data", data);
        }
        BlitzPlugin.responseJs(true, result.toJsonString(), cbData);
    }

    /* access modifiers changed from: private */
    public void doCouponApply(String itemId, String pid, String couponKey, String userId, long cbData) {
        BusinessRequest.getBusinessRequest().requestAlimamaApplyCoupon(itemId, pid, couponKey, userId, new couponRequestListener(cbData));
    }

    private static class couponRequestListener implements RequestListener<String> {
        private long cbData;

        private couponRequestListener(long cbData2) {
            this.cbData = 0;
            this.cbData = cbData2;
        }

        public void onRequestDone(String data, int resultCode, String msg) {
            ZpLogger.v(CouponPlugin.TAG, "onRequestDone ---> data=" + data + ",resultCode=" + resultCode + ",msg=" + msg);
            if (resultCode == 200) {
                JSONObject resultData = null;
                try {
                    JSONObject resultData2 = new JSONObject(data);
                    if (resultData2.optBoolean(BlitzServiceUtils.CSUCCESS)) {
                        JSONObject resultData3 = resultData2.optJSONObject("result");
                        if (resultData3 == null || !resultData3.optBoolean(BlitzServiceUtils.CSUCCESS)) {
                            CouponPlugin.failResponseJs(resultData3, this.cbData);
                        } else {
                            CouponPlugin.successResponseJs(resultData3, this.cbData);
                        }
                    } else {
                        CouponPlugin.failResponseJs(resultData2.optString("msg"), this.cbData);
                        JSONObject jSONObject = resultData2;
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                    if (resultData.optBoolean(BlitzServiceUtils.CSUCCESS)) {
                        JSONObject resultData4 = resultData.optJSONObject("result");
                        if (resultData4 == null || !resultData4.optBoolean(BlitzServiceUtils.CSUCCESS)) {
                            CouponPlugin.failResponseJs(resultData4, this.cbData);
                        } else {
                            CouponPlugin.successResponseJs(resultData4, this.cbData);
                        }
                    } else {
                        CouponPlugin.failResponseJs(resultData.optString("msg"), this.cbData);
                    }
                } catch (Throwable th) {
                    if (resultData.optBoolean(BlitzServiceUtils.CSUCCESS)) {
                        JSONObject resultData5 = resultData.optJSONObject("result");
                        if (resultData5 == null || !resultData5.optBoolean(BlitzServiceUtils.CSUCCESS)) {
                            CouponPlugin.failResponseJs(resultData5, this.cbData);
                        } else {
                            CouponPlugin.successResponseJs(resultData5, this.cbData);
                        }
                    } else {
                        CouponPlugin.failResponseJs(resultData.optString("msg"), this.cbData);
                    }
                    throw th;
                }
            } else {
                CouponPlugin.failResponseJs("http status code is " + resultCode, this.cbData);
            }
        }
    }

    private static class CouponJsCallback implements BlitzPlugin.JsCallback {
        private WeakReference<CouponPlugin> mReference;

        private CouponJsCallback(WeakReference<CouponPlugin> reference) {
            this.mReference = reference;
        }

        public void onCall(String param, long cbData) {
            ZpLogger.v(CouponPlugin.TAG, "onCall --> param  =" + param + ";  cbData = " + cbData);
            if (this.mReference != null && this.mReference.get() != null) {
                ((CouponPlugin) this.mReference.get()).onHandleCallApply(param, cbData);
            }
        }
    }
}
