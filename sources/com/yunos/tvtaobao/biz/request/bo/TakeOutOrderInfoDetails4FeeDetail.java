package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class TakeOutOrderInfoDetails4FeeDetail implements Serializable {
    private static final long serialVersionUID = -610484326142980001L;
    private int fee;
    private String feeContent;
    private boolean liangpiao;

    public String getFeeContent() {
        return this.feeContent;
    }

    public void setFeeContent(String feeContent2) {
        this.feeContent = feeContent2;
    }

    public int getFee() {
        return this.fee;
    }

    public void setFee(int fee2) {
        this.fee = fee2;
    }

    public boolean isLiangpiao() {
        return this.liangpiao;
    }

    public void setLiangpiao(boolean liangpiao2) {
        this.liangpiao = liangpiao2;
    }

    public static TakeOutOrderInfoDetails4FeeDetail resolverFromMtop(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        TakeOutOrderInfoDetails4FeeDetail details4FeeDetail = new TakeOutOrderInfoDetails4FeeDetail();
        if (obj == null) {
            return details4FeeDetail;
        }
        details4FeeDetail.setFee(obj.optInt("fee", 0));
        details4FeeDetail.setFeeContent(obj.optString("feeContent"));
        details4FeeDetail.setLiangpiao(obj.optBoolean("liangpiao", false));
        return details4FeeDetail;
    }
}
