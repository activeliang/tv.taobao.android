package com.taobao.wireless.trade.mcart.sdk.co.mtop;

import com.taobao.wireless.trade.mcart.sdk.utils.McartConstants;
import mtopsdk.mtop.domain.IMTOPDataObject;

public class MtopTradeBagToFavorRequest implements IMTOPDataObject {
    private String API_NAME = McartConstants.ADDFAVOR_API_NAME;
    private boolean NEED_ECODE = true;
    private boolean NEED_SESSION = true;
    private String VERSION = McartConstants.ADDFAVOR_API_VERSION;
    private String cartFrom;
    private String exParams;
    private int extStatus;
    private String feature;
    private String p;

    public String getCartFrom() {
        return this.cartFrom;
    }

    public void setCartFrom(String cartFrom2) {
        this.cartFrom = cartFrom2;
    }

    public String getFeature() {
        return this.feature;
    }

    public void setFeature(String feature2) {
        this.feature = feature2;
    }

    public int getExtStatus() {
        return this.extStatus;
    }

    public void setExtStatus(int extStatus2) {
        this.extStatus = extStatus2;
    }

    public void setP(String p2) {
        this.p = p2;
    }

    public String getP() {
        return this.p;
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

    public void setExParams(String exParams2) {
        this.exParams = exParams2;
    }

    public String getExParams() {
        return this.exParams;
    }

    public String toString() {
        return "MtopTradeBagToFavorRequest{API_NAME='" + this.API_NAME + '\'' + ", VERSION='" + this.VERSION + '\'' + ", NEED_ECODE=" + this.NEED_ECODE + ", NEED_SESSION=" + this.NEED_SESSION + ", p='" + this.p + '\'' + ", extStatus=" + this.extStatus + ", feature='" + this.feature + '\'' + ", cartFrom='" + this.cartFrom + '\'' + ", exParams='" + this.exParams + '\'' + '}';
    }
}
