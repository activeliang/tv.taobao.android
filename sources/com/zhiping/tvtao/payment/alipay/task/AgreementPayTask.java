package com.zhiping.tvtao.payment.alipay.task;

import android.os.AsyncTask;
import android.util.Log;
import anetwork.channel.util.RequestConstant;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import com.zhiping.tvtao.payment.AlipayManager;
import com.zhiping.tvtao.payment.alipay.request.AgreementPayRequest;
import com.zhiping.tvtao.payment.alipay.request.AlipaySignQueryRequest;
import com.zhiping.tvtao.payment.alipay.request.GetAlipayAccountRequest;
import com.zhiping.tvtao.payment.alipay.request.GetOrderDetailRequest;
import com.zhiping.tvtao.payment.alipay.request.base.MtopResponse;
import com.zhiping.tvtao.payment.utils.MtopHelper;
import org.json.JSONException;
import org.json.JSONObject;

public class AgreementPayTask extends AsyncTask implements AlipayManager.CancelableTask {
    public static final String AUTH_NOT_VALID = "invalidAuth";
    public static final String PAY_ERROR = "payerror";
    private String account;
    private String bizOrderId;
    private String buyerId;
    private boolean checkDepositStatus = false;
    private String errorMsg;
    private boolean finish = false;
    private AgreementPayListener listener;
    private Object lock = new Object();
    private int step = 0;

    public interface AgreementPayListener {
        void onNeedAuth(AgreementPayTask agreementPayTask, String str);

        void onPayMentFailure(AgreementPayTask agreementPayTask, String str);

        void onPayMentSuccess(AgreementPayTask agreementPayTask, String str);
    }

    public void setListener(AgreementPayListener listener2) {
        this.listener = listener2;
    }

    public void setCheckDepositStatus(boolean checkDepositStatus2) {
        this.checkDepositStatus = checkDepositStatus2;
    }

    public boolean shouldCheckDepositStatus() {
        return this.checkDepositStatus;
    }

    public void setBuyerId(String buyerId2) {
        this.buyerId = buyerId2;
    }

    public void setBizOrderId(String bizOrderId2) {
        this.bizOrderId = bizOrderId2;
    }

    /* access modifiers changed from: protected */
    public void onProgressUpdate(Object[] values) {
        Integer val = values[0];
        if (val.intValue() == 0) {
            notifyNeedAuth();
        } else if (val.intValue() == 1) {
            notifySuccess();
        } else if (val.intValue() == 2) {
            notifyError();
        }
        super.onProgressUpdate(values);
    }

    public void resumePay() {
        synchronized (this.lock) {
            this.lock.notify();
        }
    }

    public void stop() {
        this.finish = true;
        synchronized (this.lock) {
            this.lock.notify();
        }
    }

    public void pause() {
        try {
            synchronized (this.lock) {
                this.lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        resumePay();
    }

    /* access modifiers changed from: protected */
    public Object doInBackground(Object... objects) {
        while (!this.finish) {
            if (this.step == 0) {
                if (!checkAuthValid()) {
                    publishProgress(new Object[]{0});
                    pause();
                } else {
                    this.step++;
                }
            } else if (this.step == 1) {
                if (doPay()) {
                    publishProgress(new Object[]{1});
                } else {
                    publishProgress(new Object[]{2});
                }
                this.finish = true;
            }
        }
        return null;
    }

    private void notifyError() {
        if (this.listener != null) {
            this.listener.onPayMentFailure(this, doErrorMapping(this.errorMsg));
        }
    }

    private void notifySuccess() {
        if (this.listener != null) {
            this.listener.onPayMentSuccess(this, this.account);
        }
    }

    private void notifyNeedAuth() {
        if (this.listener != null) {
            this.listener.onNeedAuth(this, this.buyerId);
        }
    }

    private boolean checkAuthValid() {
        MtopResponse response = MtopHelper.baseRequest(new GetAlipayAccountRequest());
        if (response.isSuccess()) {
            try {
                this.buyerId = response.getJsonData().getString("accountNo");
                this.account = response.getJsonData().getString("account");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        MtopResponse signQueryResponse = MtopHelper.baseRequest(new AlipaySignQueryRequest());
        if (!signQueryResponse.isSuccess()) {
            return false;
        }
        JSONObject data = signQueryResponse.getJsonData();
        if (!data.has("agreementNo")) {
            return false;
        }
        try {
            if (!data.getString("alipayUserId").equals(this.buyerId)) {
                return false;
            }
            return true;
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }

    private String doErrorMapping(String errorCode) {
        if ("PAY_QUOTA_EXCEED".equals(errorCode)) {
            return "当日购物金额超出免密支付额度";
        }
        if ("USER_BALANCE_NOT_ENOUGH".equals(errorCode)) {
            return "您的支付宝账户余额不足";
        }
        return "网络好像有点不通畅";
    }

    public String getErrorMsg() {
        return doErrorMapping(this.errorMsg);
    }

    private boolean doPay() {
        MtopResponse response = MtopHelper.baseRequest(new AgreementPayRequest(this.bizOrderId));
        if (response.isSuccess()) {
            JSONObject data = response.getJsonData();
            Log.d(RequestConstant.ENV_TEST, "pay data:" + data);
            if (data.optBoolean(BlitzServiceUtils.CSUCCESS, false)) {
                this.errorMsg = null;
                return true;
            }
            this.errorMsg = response.getErrorCode();
            if ("USER_BALANCE_NOT_ENOUGH".equals(this.errorMsg)) {
                return false;
            }
            int retryCount = 4;
            do {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MtopResponse checkResponse = MtopHelper.baseRequest(new GetOrderDetailRequest(this.bizOrderId));
                if (checkResponse.isSuccess()) {
                    String status = GetOrderDetailRequest.getResponseStatus(checkResponse.getJsonData());
                    if (!"WAIT_BUYER_PAY".equals(status)) {
                        return checkPaid(status);
                    }
                }
                retryCount--;
            } while (retryCount > 0);
            return false;
        }
        this.errorMsg = response.getErrorCode();
        if ("USER_BALANCE_NOT_ENOUGH".equals(this.errorMsg)) {
            return false;
        }
        int retryCount2 = 4;
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            MtopResponse checkResponse2 = MtopHelper.baseRequest(new GetOrderDetailRequest(this.bizOrderId));
            if (checkResponse2.isSuccess()) {
                String status2 = GetOrderDetailRequest.getResponseStatus(checkResponse2.getJsonData());
                if (!"WAIT_BUYER_PAY".equals(status2)) {
                    return checkPaid(status2);
                }
            }
            retryCount2--;
        } while (retryCount2 > 0);
        return false;
    }

    private boolean checkPaid(String statusCode) {
        this.errorMsg = statusCode;
        if ("TRADE_FINISHED".equals(statusCode) || "WAIT_SELLER_SEND_GOODS".equals(statusCode)) {
            return true;
        }
        if ((!"BUYER_PAYED_DEPOSIT".equals(statusCode) || !shouldCheckDepositStatus()) && !"WAIT_BUYER_CONFIRM_GOODS".equals(statusCode)) {
            return false;
        }
        return true;
    }
}
