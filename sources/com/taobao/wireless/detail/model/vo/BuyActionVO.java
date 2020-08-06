package com.taobao.wireless.detail.model.vo;

import com.taobao.wireless.lang.CheckUtils;
import java.io.Serializable;
import java.util.Map;

public class BuyActionVO implements Serializable {
    public String buyUrl;
    public BaseControl controlVO;
    public String itemId;
    public Map<String, String> options;
    public String skuId;
    public boolean useV3Trade = false;

    public boolean isH5() {
        return !CheckUtils.isEmpty(this.buyUrl);
    }

    public boolean isCartSupport() {
        return this.controlVO.cartSupport;
    }

    public boolean isBuySupport() {
        return this.controlVO.buySupport;
    }

    public String getErrorMsg() {
        return CheckUtils.isEmpty(this.controlVO.msgTip) ? "暂不支持" : this.controlVO.msgTip;
    }
}
