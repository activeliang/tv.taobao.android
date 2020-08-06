package com.taobao.wireless.trade.mcart.sdk.co.mtop;

import com.taobao.wireless.trade.mcart.sdk.utils.McartConstants;
import java.util.HashMap;
import mtopsdk.mtop.domain.IMTOPDataObject;

public class MtopTradeItemRecommendRequest implements IMTOPDataObject {
    private String API_NAME = McartConstants.RECOMMEND_API_NAME;
    private boolean NEED_ECODE = true;
    private boolean NEED_SESSION = true;
    private String VERSION = "1.0";
    private Long appId;
    private HashMap params;

    public Long getAppId() {
        return this.appId;
    }

    public void setAppId(Long appId2) {
        this.appId = appId2;
    }

    public HashMap getParams() {
        return this.params;
    }

    public void setParams(HashMap params2) {
        this.params = params2;
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
        return "MtopTradeItemRecommendRequest{API_NAME='" + this.API_NAME + '\'' + ", VERSION='" + this.VERSION + '\'' + ", NEED_ECODE=" + this.NEED_ECODE + ", NEED_SESSION=" + this.NEED_SESSION + ", appId=" + this.appId + ", params=" + this.params + '}';
    }
}
