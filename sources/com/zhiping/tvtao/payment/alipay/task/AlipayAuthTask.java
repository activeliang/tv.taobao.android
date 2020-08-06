package com.zhiping.tvtao.payment.alipay.task;

import android.os.AsyncTask;
import android.text.TextUtils;
import com.zhiping.tvtao.payment.AlipayManager;
import com.zhiping.tvtao.payment.alipay.request.AlipaySignQueryRequest;
import com.zhiping.tvtao.payment.alipay.request.AlipaySignRequest;
import com.zhiping.tvtao.payment.alipay.request.base.BaseMtopRequest;
import com.zhiping.tvtao.payment.alipay.request.base.MtopResponse;
import com.zhiping.tvtao.payment.utils.MtopHelper;

public class AlipayAuthTask extends AsyncTask implements AlipayManager.CancelableTask {
    public static final int EXPIRATION_MILLIS = 300000;
    public static final int FAIL_RETRY_DELAY_MILLIS = 5000;
    public static final int QUERY_DELAY_MILLIS = 3000;
    private String alipayUserId = null;
    private BaseMtopRequest currentRequest;
    private boolean finish = false;
    private AlipayAuthTaskListener mListener;
    private STEP step = STEP.GEN_QRCODE;
    private long timeStamp = 0;

    public interface AlipayAuthTaskListener {
        void onReceivedAlipayAuthStateNotify(AlipayAuthTaskResult alipayAuthTaskResult);
    }

    public enum STATUS {
        SUCCESS,
        FAILURE,
        EXPRIE
    }

    public enum STEP {
        GEN_QRCODE,
        TOKEN_GET
    }

    public void pause() {
    }

    public void resume() {
    }

    public void stop() {
        this.finish = true;
    }

    public static class AlipayAuthTaskResult {
        private AlipayQRResult qrResult;
        private STATUS status;
        private STEP step;
        private AlipayTokenResult tokenResult;

        AlipayAuthTaskResult(STEP step2, STATUS status2, AlipayQRResult object) {
            this.step = step2;
            this.status = status2;
            this.qrResult = object;
        }

        public AlipayAuthTaskResult(STEP step2, STATUS status2, AlipayTokenResult tokenResult2) {
            this.step = step2;
            this.status = status2;
            this.tokenResult = tokenResult2;
        }

        public STEP getStep() {
            return this.step;
        }

        public STATUS getStatus() {
            return this.status;
        }

        public AlipayQRResult getQrResult() {
            return this.qrResult;
        }

        public AlipayTokenResult getTokenResult() {
            return this.tokenResult;
        }
    }

    public void setListener(AlipayAuthTaskListener listener) {
        this.mListener = listener;
    }

    public void setAlipayUserId(String alipayUserId2) {
        this.alipayUserId = alipayUserId2;
    }

    /* access modifiers changed from: protected */
    public Object doInBackground(Object[] objects) {
        while (!this.finish && !isCancelled()) {
            if (isExpire()) {
                this.step = STEP.GEN_QRCODE;
            }
            if (this.step == STEP.GEN_QRCODE) {
                this.currentRequest = new AlipaySignRequest(this.alipayUserId);
                MtopResponse response = MtopHelper.baseRequest(this.currentRequest);
                if (!response.isSuccess()) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    publishProgress(new Object[]{new AlipayAuthTaskResult(STEP.GEN_QRCODE, STATUS.SUCCESS, AlipayQRResult.resolveFromJson(response.getJsonData()))});
                    this.timeStamp = System.currentTimeMillis();
                    this.step = STEP.TOKEN_GET;
                }
            } else if (this.step == STEP.TOKEN_GET && doAuthQuery()) {
                return null;
            }
        }
        return null;
    }

    private boolean isExpire() {
        return System.currentTimeMillis() - this.timeStamp > 300000;
    }

    private boolean doAuthQuery() {
        this.currentRequest = new AlipaySignQueryRequest();
        MtopResponse response = MtopHelper.baseRequest(this.currentRequest);
        if (!response.isSuccess()) {
            try {
                Thread.sleep(5000);
                return false;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            AlipayTokenResult result = AlipayTokenResult.resolveFromJson(response.getJsonData());
            if (TextUtils.isEmpty(result.token) || TextUtils.isEmpty(result.agreementNo)) {
                try {
                    Thread.sleep(3000);
                    return false;
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                    return false;
                }
            } else {
                publishProgress(new Object[]{new AlipayAuthTaskResult(STEP.TOKEN_GET, STATUS.SUCCESS, result)});
                return true;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        if (values.length == 1) {
            notifyResult(values[0]);
        }
    }

    private void notifyResult(AlipayAuthTaskResult result) {
        if (this.mListener != null) {
            this.mListener.onReceivedAlipayAuthStateNotify(result);
        }
    }
}
