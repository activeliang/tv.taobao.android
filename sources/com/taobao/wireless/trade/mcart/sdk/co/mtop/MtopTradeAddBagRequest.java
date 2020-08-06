package com.taobao.wireless.trade.mcart.sdk.co.mtop;

import com.taobao.wireless.trade.mcart.sdk.utils.McartConstants;
import mtopsdk.mtop.domain.IMTOPDataObject;

public class MtopTradeAddBagRequest implements IMTOPDataObject {
    private String API_NAME = McartConstants.ADDBAG_API_NAME;
    private boolean NEED_ECODE = true;
    private boolean NEED_SESSION = true;
    private String VERSION = McartConstants.ADDBAG_API_VERSION;
    protected String cartFrom;
    private String exParams;
    private String feature;
    private String itemId;
    private long quantity;
    private String skuId;

    public String getCartFrom() {
        return this.cartFrom;
    }

    public void setCartFrom(String cartFrom2) {
        this.cartFrom = cartFrom2;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId2) {
        this.itemId = itemId2;
    }

    public String getSkuId() {
        return this.skuId;
    }

    public void setSkuId(String skuId2) {
        this.skuId = skuId2;
    }

    public long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(long quantity2) {
        this.quantity = quantity2;
    }

    public String getExParams() {
        return this.exParams;
    }

    public void setExParams(String exParams2) {
        this.exParams = exParams2;
    }

    public String getFeature() {
        return this.feature;
    }

    public void setFeature(String feature2) {
        this.feature = feature2;
    }

    public String getAPI_NAME() {
        return this.API_NAME;
    }

    public void setAPI_NAME(String aPI_NAME) {
        this.API_NAME = aPI_NAME;
    }

    public String getVERSION() {
        return this.VERSION;
    }

    public void setVERSION(String vERSION) {
        this.VERSION = vERSION;
    }

    public boolean isNEED_ECODE() {
        return this.NEED_ECODE;
    }

    public void setNEED_ECODE(boolean nEED_ECODE) {
        this.NEED_ECODE = nEED_ECODE;
    }

    public boolean isNEED_SESSION() {
        return this.NEED_SESSION;
    }

    public void setNEED_SESSION(boolean nEED_SESSION) {
        this.NEED_SESSION = nEED_SESSION;
    }

    public String toString() {
        return "MtopTradeAddBagRequest{API_NAME='" + this.API_NAME + '\'' + ", VERSION='" + this.VERSION + '\'' + ", NEED_ECODE=" + this.NEED_ECODE + ", NEED_SESSION=" + this.NEED_SESSION + ", feature='" + this.feature + '\'' + ", itemId='" + this.itemId + '\'' + ", skuId='" + this.skuId + '\'' + ", quantity=" + this.quantity + ", exParams='" + this.exParams + '\'' + ", cartFrom='" + this.cartFrom + '\'' + '}';
    }
}
