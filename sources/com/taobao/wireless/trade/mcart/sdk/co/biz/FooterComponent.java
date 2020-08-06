package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;
import java.util.ArrayList;
import java.util.List;

public class FooterComponent extends Component {
    private CheckAll checkAllF = null;
    private List<DynamicCrossShopPromotion> dynamicCrossShopPromotions = null;
    private Pay payF = null;
    private Quantity quantityF = null;
    private Submit submitF = null;
    private Weight weightF = null;

    public FooterComponent(JSONObject data, CartFrom cartFrom) {
        super(data, cartFrom);
    }

    public Quantity getQuantity() {
        if (this.quantityF == null) {
            generateQantity();
        }
        return this.quantityF;
    }

    private void generateQantity() {
        JSONObject quantity = this.fields.getJSONObject("quantity");
        if (quantity != null) {
            try {
                this.quantityF = new Quantity(quantity);
            } catch (Throwable th) {
            }
        }
    }

    public Weight getWeight() {
        if (this.weightF == null) {
            generateWeight();
        }
        return this.weightF;
    }

    private void generateWeight() {
        JSONObject weight = this.fields.getJSONObject("weight");
        if (weight != null) {
            try {
                this.weightF = new Weight(weight);
            } catch (Throwable th) {
            }
        }
    }

    public Pay getPay() {
        if (this.payF == null) {
            generatePay();
        }
        return this.payF;
    }

    public void reloadPay() {
        generatePay();
    }

    private void generatePay() {
        JSONObject pay = this.fields.getJSONObject("pay");
        if (pay != null) {
            try {
                this.payF = new Pay(pay);
            } catch (Throwable th) {
            }
        }
    }

    public Submit getSubmit() {
        if (this.submitF == null) {
            generateSubmit();
        }
        return this.submitF;
    }

    private void generateSubmit() {
        JSONObject submit = this.fields.getJSONObject("submit");
        if (submit != null) {
            try {
                this.submitF = new Submit(submit);
            } catch (Throwable th) {
            }
        }
    }

    public CheckAll getCheckAll() {
        if (this.checkAllF == null) {
            generateCheckAll();
        }
        return this.checkAllF;
    }

    private void generateCheckAll() {
        JSONObject checkAll = this.fields.getJSONObject("checkAll");
        if (checkAll != null) {
            try {
                this.checkAllF = new CheckAll(checkAll, this.cartFrom);
            } catch (Throwable th) {
            }
        }
    }

    public void cleanDynamicCrossShopPromotions() {
        this.fields.put("dynamicCrossShopPromotions", (Object) "");
        this.dynamicCrossShopPromotions = null;
    }

    public List<DynamicCrossShopPromotion> getDynamicCrossShopPromotions() {
        if (this.dynamicCrossShopPromotions == null) {
            this.dynamicCrossShopPromotions = generateDynamicCrossShopPromotions();
        }
        return this.dynamicCrossShopPromotions;
    }

    private List<DynamicCrossShopPromotion> generateDynamicCrossShopPromotions() {
        try {
            List<DynamicCrossShopPromotion> dynamicCrossShopPromotionList = new ArrayList<>();
            JSONArray jsonArray = this.fields.getJSONArray("dynamicCrossShopPromotions");
            if (jsonArray == null || jsonArray.isEmpty()) {
                return dynamicCrossShopPromotionList;
            }
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject itemObj = jsonArray.getJSONObject(i);
                if (itemObj != null) {
                    dynamicCrossShopPromotionList.add(new DynamicCrossShopPromotion(itemObj));
                }
            }
            return dynamicCrossShopPromotionList;
        } catch (Exception e) {
            return null;
        }
    }

    public void reload(JSONObject data) {
        boolean isCheckAll = this.checkAllF != null ? this.checkAllF.isChecked() : false;
        super.reload(data);
        generateQantity();
        generateWeight();
        generatePay();
        generateSubmit();
        if (isCheckAll) {
            this.fields.put("checkAll", (Object) this.checkAllF.getData());
        } else {
            generateCheckAll();
        }
        this.dynamicCrossShopPromotions = null;
        generateDynamicCrossShopPromotions();
    }

    public String toString() {
        return super.toString() + " - FooterComponent [quantity=" + getQuantity() + ",weight=" + getWeight() + ",pay=" + getPay() + ",submit=" + getSubmit() + ",checkAll=" + getCheckAll() + ",dynamicCrossShopPromotions=" + getDynamicCrossShopPromotions() + "]";
    }
}
