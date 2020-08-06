package com.yunos.tvtaobao.blitz;

import android.content.Context;
import com.yunos.tv.blitz.BlitzContextWrapper;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tv.blitz.listener.BzMiscListener;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tvtaobao.biz.activity.BaseActivity;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import org.json.JSONObject;

public class TaobaoBzMiscListener implements BzMiscListener {
    private static final String TAG = "TaobaoBzMiscListener";

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x00df, code lost:
        r5 = (com.yunos.tvtaobao.biz.activity.BaseActivity) r10;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onStartActivity(android.content.Context r19, java.lang.String r20, int r21) {
        /*
            r18 = this;
            java.lang.String r2 = "TaobaoBzMiscListener"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r17 = "onStartActivity -->  param = "
            r0 = r17
            java.lang.StringBuilder r3 = r3.append(r0)
            r0 = r20
            java.lang.StringBuilder r3 = r3.append(r0)
            java.lang.String r3 = r3.toString()
            com.zhiping.dev.android.logger.ZpLogger.i(r2, r3)
            java.lang.ref.WeakReference r13 = new java.lang.ref.WeakReference
            r0 = r19
            r13.<init>(r0)
            r15 = 0
            org.json.JSONObject r16 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0128 }
            r0 = r16
            r1 = r20
            r0.<init>(r1)     // Catch:{ JSONException -> 0x0128 }
            java.lang.String r2 = "blitzOpenType"
            r0 = r16
            java.lang.String r9 = r0.optString(r2)     // Catch:{ JSONException -> 0x0128 }
            java.lang.String r2 = "uri"
            r0 = r16
            java.lang.String r6 = r0.optString(r2)     // Catch:{ JSONException -> 0x0128 }
            java.lang.String r2 = "data"
            r0 = r16
            org.json.JSONObject r11 = r0.optJSONObject(r2)     // Catch:{ JSONException -> 0x0128 }
            java.lang.String r2 = "flags"
            r3 = 0
            r0 = r16
            int r7 = r0.optInt(r2, r3)     // Catch:{ JSONException -> 0x0128 }
            java.lang.String r2 = "action"
            r0 = r16
            java.lang.String r8 = r0.optString(r2)     // Catch:{ JSONException -> 0x0128 }
            java.lang.String r2 = "TaobaoBzMiscListener"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x0128 }
            r3.<init>()     // Catch:{ JSONException -> 0x0128 }
            java.lang.String r17 = "onStartActivity -->  blitzOpenType  = "
            r0 = r17
            java.lang.StringBuilder r3 = r3.append(r0)     // Catch:{ JSONException -> 0x0128 }
            java.lang.StringBuilder r3 = r3.append(r9)     // Catch:{ JSONException -> 0x0128 }
            java.lang.String r17 = "; action_temp = "
            r0 = r17
            java.lang.StringBuilder r3 = r3.append(r0)     // Catch:{ JSONException -> 0x0128 }
            java.lang.StringBuilder r3 = r3.append(r8)     // Catch:{ JSONException -> 0x0128 }
            java.lang.String r17 = "; data = "
            r0 = r17
            java.lang.StringBuilder r3 = r3.append(r0)     // Catch:{ JSONException -> 0x0128 }
            java.lang.StringBuilder r3 = r3.append(r11)     // Catch:{ JSONException -> 0x0128 }
            java.lang.String r17 = ";  uri = "
            r0 = r17
            java.lang.StringBuilder r3 = r3.append(r0)     // Catch:{ JSONException -> 0x0128 }
            java.lang.StringBuilder r3 = r3.append(r6)     // Catch:{ JSONException -> 0x0128 }
            java.lang.String r3 = r3.toString()     // Catch:{ JSONException -> 0x0128 }
            com.zhiping.dev.android.logger.ZpLogger.i(r2, r3)     // Catch:{ JSONException -> 0x0128 }
            boolean r2 = android.text.TextUtils.isEmpty(r8)     // Catch:{ JSONException -> 0x0128 }
            if (r2 == 0) goto L_0x00a9
            java.lang.String r8 = "android.intent.action.VIEW"
        L_0x00a9:
            r4 = r8
            java.lang.String r2 = "TaobaoBzMiscListener"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x0128 }
            r3.<init>()     // Catch:{ JSONException -> 0x0128 }
            java.lang.String r17 = "onStartActivity -->  action = "
            r0 = r17
            java.lang.StringBuilder r3 = r3.append(r0)     // Catch:{ JSONException -> 0x0128 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ JSONException -> 0x0128 }
            java.lang.String r17 = "; uri = "
            r0 = r17
            java.lang.StringBuilder r3 = r3.append(r0)     // Catch:{ JSONException -> 0x0128 }
            java.lang.StringBuilder r3 = r3.append(r6)     // Catch:{ JSONException -> 0x0128 }
            java.lang.String r3 = r3.toString()     // Catch:{ JSONException -> 0x0128 }
            com.zhiping.dev.android.logger.ZpLogger.i(r2, r3)     // Catch:{ JSONException -> 0x0128 }
            java.lang.Object r10 = r13.get()     // Catch:{ JSONException -> 0x0128 }
            android.content.Context r10 = (android.content.Context) r10     // Catch:{ JSONException -> 0x0128 }
            if (r10 == 0) goto L_0x00ef
            boolean r2 = r10 instanceof com.yunos.tvtaobao.biz.activity.BaseActivity     // Catch:{ JSONException -> 0x0128 }
            if (r2 == 0) goto L_0x00ef
            r0 = r10
            com.yunos.tvtaobao.biz.activity.BaseActivity r0 = (com.yunos.tvtaobao.biz.activity.BaseActivity) r0     // Catch:{ JSONException -> 0x0128 }
            r5 = r0
            if (r5 == 0) goto L_0x00ef
            com.yunos.tvtaobao.blitz.TaobaoBzMiscListener$1 r2 = new com.yunos.tvtaobao.blitz.TaobaoBzMiscListener$1     // Catch:{ JSONException -> 0x0128 }
            r3 = r18
            r2.<init>(r4, r5, r6, r7)     // Catch:{ JSONException -> 0x0128 }
            r5.runOnUiThread(r2)     // Catch:{ JSONException -> 0x0128 }
        L_0x00ef:
            com.yunos.tv.blitz.data.BzResult r14 = new com.yunos.tv.blitz.data.BzResult
            r14.<init>()
            java.lang.String r2 = "retcode"
            r14.addData((java.lang.String) r2, (int) r15)
            r2 = -1
            if (r15 != r2) goto L_0x014d
            java.lang.String r2 = "TaobaoBzMiscListener"
            java.lang.String r3 = "startactivity fail"
            com.yunos.tv.blitz.account.BzDebugLog.d(r2, r3)
            java.lang.Object r10 = r13.get()
            android.content.Context r10 = (android.content.Context) r10
            if (r10 == 0) goto L_0x0127
            boolean r2 = r10 instanceof com.yunos.tvtaobao.biz.activity.BaseActivity
            if (r2 == 0) goto L_0x0127
            r5 = r10
            com.yunos.tvtaobao.biz.activity.BaseActivity r5 = (com.yunos.tvtaobao.biz.activity.BaseActivity) r5
            if (r5 == 0) goto L_0x0127
            com.yunos.tv.blitz.BlitzContextWrapper r2 = r5.getBlitzContext()
            r3 = 0
            java.lang.String r17 = r14.toJsonString()
            r0 = r21
            r1 = r17
            r2.replyCallBack(r0, r3, r1)
        L_0x0127:
            return
        L_0x0128:
            r12 = move-exception
            java.lang.String r2 = "TaobaoBzMiscListener"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r17 = "onStartActivity -->  e = "
            r0 = r17
            java.lang.StringBuilder r3 = r3.append(r0)
            java.lang.String r17 = r12.toString()
            r0 = r17
            java.lang.StringBuilder r3 = r3.append(r0)
            java.lang.String r3 = r3.toString()
            com.zhiping.dev.android.logger.ZpLogger.i(r2, r3)
            r15 = -1
            goto L_0x00ef
        L_0x014d:
            java.lang.String r2 = "TaobaoBzMiscListener"
            java.lang.String r3 = "startactivity successful"
            com.yunos.tv.blitz.account.BzDebugLog.d(r2, r3)
            java.lang.Object r10 = r13.get()
            android.content.Context r10 = (android.content.Context) r10
            if (r10 == 0) goto L_0x0177
            boolean r2 = r10 instanceof com.yunos.tvtaobao.biz.activity.BaseActivity
            if (r2 == 0) goto L_0x0177
            r5 = r10
            com.yunos.tvtaobao.biz.activity.BaseActivity r5 = (com.yunos.tvtaobao.biz.activity.BaseActivity) r5
            if (r5 == 0) goto L_0x0177
            com.yunos.tv.blitz.BlitzContextWrapper r2 = r5.getBlitzContext()
            r3 = 1
            java.lang.String r17 = r14.toJsonString()
            r0 = r21
            r1 = r17
            r2.replyCallBack(r0, r3, r1)
        L_0x0177:
            r14.setSuccess()
            goto L_0x0127
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.blitz.TaobaoBzMiscListener.onStartActivity(android.content.Context, java.lang.String, int):void");
    }

    public void onGetMtopResponse(String s, int i, String s1, String s2) {
    }

    public String onGetMtopRequest(Context context, String param, int callback) {
        BaseActivity taoBaoBlitzActivity;
        WeakReference<Context> mReferenceContext = new WeakReference<>(context);
        ZpLogger.i(TAG, "onGetMtopRequest -->  final_callback = " + callback + ";  final_param = " + param);
        Context ctx = (Context) mReferenceContext.get();
        if (ctx == null || !(ctx instanceof BaseActivity) || (taoBaoBlitzActivity = (BaseActivity) ctx) == null) {
            return null;
        }
        taoBaoBlitzActivity.runOnUiThread(new Runnable() {
            public void run() {
            }
        });
        return null;
    }

    public static class BlitzMtopListener implements RequestListener<JSONObject> {
        private int mAddrCallback;
        private WeakReference<BaseActivity> mTaoBaoBlitzActivityRef = null;

        public BlitzMtopListener(WeakReference<BaseActivity> baseActivityRef, int addr_callback) {
            this.mAddrCallback = addr_callback;
            this.mTaoBaoBlitzActivityRef = baseActivityRef;
        }

        public boolean onError(int resultCode, String msg) {
            ZpLogger.i(TaobaoBzMiscListener.TAG, "BlitzMtopListener --> onError -->  resultCode = " + resultCode + ";  msg = " + msg);
            replyCallBack(new BzResult(), false);
            return false;
        }

        public void onSuccess(JSONObject data) {
            ZpLogger.i(TaobaoBzMiscListener.TAG, "BlitzMtopListener --> onSuccess --> data = " + data);
            BzResult result = new BzResult();
            result.setSuccess();
            replyCallBack(result, true);
        }

        /* access modifiers changed from: package-private */
        public boolean replyCallBack(BzResult bzResult, boolean success) {
            BaseActivity taoBaoBlitzActivity;
            BlitzContextWrapper blitzContextWrapper;
            if (this.mTaoBaoBlitzActivityRef == null || this.mTaoBaoBlitzActivityRef.get() == null || (taoBaoBlitzActivity = (BaseActivity) this.mTaoBaoBlitzActivityRef.get()) == null || (blitzContextWrapper = taoBaoBlitzActivity.getBlitzContext()) == null) {
                return false;
            }
            blitzContextWrapper.replyCallBack(this.mAddrCallback, success, bzResult.toJsonString());
            return true;
        }

        public void onRequestDone(JSONObject data, int resultCode, String msg) {
            if (resultCode == 200) {
                onSuccess(data);
            } else {
                onError(resultCode, msg);
            }
        }
    }

    public void onStartActivityForResult(Context arg0, String arg1) {
    }
}
