package com.yunos.tvtaobao.biz.request.bo;

import com.taobao.detail.domain.tuwen.TuwenConstants;
import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TakeOutOrderInfoDetails4Fee implements Serializable {
    private static final long serialVersionUID = -610484326142980001L;
    private int actualPaidFee;
    private ArrayList<TakeOutOrderInfoDetails4FeeDetail> feeDetails;
    private int hongbao;
    private int postFee;
    private int totalPrice;

    public ArrayList<TakeOutOrderInfoDetails4FeeDetail> getFeeDetails() {
        return this.feeDetails;
    }

    public void setFeeDetails(ArrayList<TakeOutOrderInfoDetails4FeeDetail> feeDetails2) {
        this.feeDetails = feeDetails2;
    }

    public int getActualPaidFee() {
        return this.actualPaidFee;
    }

    public void setActualPaidFee(int actualPaidFee2) {
        this.actualPaidFee = actualPaidFee2;
    }

    public int getHongbao() {
        return this.hongbao;
    }

    public void setHongbao(int hongbao2) {
        this.hongbao = hongbao2;
    }

    public int getPostFee() {
        return this.postFee;
    }

    public void setPostFee(int postFee2) {
        this.postFee = postFee2;
    }

    public int getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(int totalPrice2) {
        this.totalPrice = totalPrice2;
    }

    public static TakeOutOrderInfoDetails4Fee resolverFromMtop(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        TakeOutOrderInfoDetails4Fee infoDetails4Fee = new TakeOutOrderInfoDetails4Fee();
        if (obj == null) {
            return infoDetails4Fee;
        }
        infoDetails4Fee.setTotalPrice(obj.optInt("totalPrice", 0));
        infoDetails4Fee.setHongbao(obj.optInt(TuwenConstants.MODEL_LIST_KEY.HONGBAO, 0));
        infoDetails4Fee.setActualPaidFee(obj.optInt("actualPaidFee", 0));
        infoDetails4Fee.setPostFee(obj.optInt("postFee", 0));
        if (obj.isNull("extraFeeDetail")) {
            return infoDetails4Fee;
        }
        JSONArray array = obj.getJSONArray("extraFeeDetail");
        ArrayList<TakeOutOrderInfoDetails4FeeDetail> temp = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            temp.add(TakeOutOrderInfoDetails4FeeDetail.resolverFromMtop(array.getJSONObject(i)));
        }
        infoDetails4Fee.setFeeDetails(temp);
        return infoDetails4Fee;
    }
}
