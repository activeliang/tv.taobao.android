package com.taobao.wireless.trade.mcart.sdk.co.service;

import com.taobao.wireless.trade.mcart.sdk.utils.NetType;
import java.util.Map;

public class CartParam {
    private NetType NetType;
    private String cartFrom;
    private String cartId;
    private boolean enableVenus = false;
    private Map<String, Object> exParam;
    private boolean isPage = false;
    private boolean refreshCache = false;

    public String getCartFrom() {
        return this.cartFrom;
    }

    public void setCartFrom(String cartFrom2) {
        this.cartFrom = cartFrom2;
    }

    public boolean isRefreshCache() {
        return this.refreshCache;
    }

    public void setRefreshCache(boolean refreshCache2) {
        this.refreshCache = refreshCache2;
    }

    public NetType getNetType() {
        return this.NetType;
    }

    public void setNetType(NetType netType) {
        this.NetType = netType;
    }

    public boolean isPage() {
        return this.isPage;
    }

    public void setPage(boolean isPage2) {
        this.isPage = isPage2;
    }

    public String getCartId() {
        return this.cartId;
    }

    public void setCartId(String cartId2) {
        this.cartId = cartId2;
    }

    public Map<String, Object> getExParam() {
        return this.exParam;
    }

    public void setExParam(Map<String, Object> exParam2) {
        this.exParam = exParam2;
    }

    public boolean isEnableVenus() {
        return this.enableVenus;
    }

    public void setEnableVenus(boolean enableVenus2) {
        this.enableVenus = enableVenus2;
    }
}
