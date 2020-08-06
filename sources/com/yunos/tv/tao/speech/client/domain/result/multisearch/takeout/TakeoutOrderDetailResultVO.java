package com.yunos.tv.tao.speech.client.domain.result.multisearch.takeout;

import com.tvtaobao.voicesdk.bean.DetailListVO;
import java.io.Serializable;

public class TakeoutOrderDetailResultVO extends DetailListVO implements Serializable {
    private static final long serialVersionUID = 6831257153606166174L;
    private String quantity;
    private String sku;
    private String skuId;
    private String skuTitle;

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantity2) {
        this.quantity = quantity2;
    }

    public String getSkuId() {
        return this.skuId;
    }

    public void setSkuId(String skuId2) {
        this.skuId = skuId2;
    }

    public String getSkuTitle() {
        return this.skuTitle;
    }

    public void setSkuTitle(String skuTitle2) {
        this.skuTitle = skuTitle2;
    }

    public String getSku() {
        return this.sku;
    }

    public void setSku(String sku2) {
        this.sku = sku2;
    }
}
