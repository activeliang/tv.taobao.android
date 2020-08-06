package com.taobao.wireless.detail.model.vo;

import com.taobao.detail.domain.HintBanner;
import com.taobao.detail.domain.base.Unit;
import java.io.Serializable;

public class BaseControl implements Serializable {
    public String basetime;
    public Unit beforeBuyApi;
    public Unit beforeCartApi;
    public boolean buySupport;
    public String buyText;
    public boolean cartSupport;
    public String cartText;
    public HintBanner hintBanner;
    public int limitCount;
    public int limitMultiCount;
    public String limitMultiText;
    public String msgTip;
    public String otherPrice;
    public String otherPriceName;
    public String price;
    public int quantity;
    public String quantityText;
}
