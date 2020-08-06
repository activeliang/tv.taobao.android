package com.zhiping.tvtao.payment.alipay.task;

import android.os.AsyncTask;
import com.zhiping.tvtao.payment.alipay.request.AlipaySignQueryRequest;
import com.zhiping.tvtao.payment.alipay.request.GetAlipayAccountRequest;
import com.zhiping.tvtao.payment.alipay.request.base.MtopResponse;
import com.zhiping.tvtao.payment.utils.MtopHelper;
import org.json.JSONException;
import org.json.JSONObject;

public class AlipayAuthCheckTask extends AsyncTask<String, Integer, AlipayAuthCheckResult> {
    private String buyerId;

    public static class AlipayAuthCheckResult {
        public String account;
        public String alipayId;
        public boolean auth;

        AlipayAuthCheckResult(boolean auth2, String alipayId2, String account2) {
            this.auth = auth2;
            this.alipayId = alipayId2;
            this.account = account2;
        }
    }

    private AlipayAuthCheckResult checkAuthValid() {
        String account = null;
        MtopResponse alipayAccountResponse = MtopHelper.baseRequest(new GetAlipayAccountRequest());
        if (alipayAccountResponse.isSuccess()) {
            try {
                this.buyerId = alipayAccountResponse.getJsonData().getString("accountNo");
                account = alipayAccountResponse.getJsonData().getString("account");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        MtopResponse response = MtopHelper.baseRequest(new AlipaySignQueryRequest());
        if (response.isSuccess()) {
            JSONObject data = response.getJsonData();
            if (data.has("agreementNo")) {
                try {
                    if (!data.getString("alipayUserId").equals(this.buyerId)) {
                        return new AlipayAuthCheckResult(false, this.buyerId, account);
                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                return new AlipayAuthCheckResult(true, this.buyerId, account);
            }
        }
        return new AlipayAuthCheckResult(false, this.buyerId, account);
    }

    /* access modifiers changed from: protected */
    public AlipayAuthCheckResult doInBackground(String... strings) {
        return checkAuthValid();
    }
}
