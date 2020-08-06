package com.taobao.wireless.trade.mcart.sdk.constant;

import com.taobao.wireless.trade.mcart.sdk.utils.StringUtils;

public enum CartFrom {
    TMALL_CLIENT("tmall_client"),
    JHS_CLIENT("jhs_client"),
    TSM_NATIVE_TAOBAO("tsm_native_taobao"),
    TSM_NATIVE_TAOBAO_CROSS_STORE("tsm_native_taobao$cross_store"),
    TSM_NATIVE_TMALL("tsm_native_tmall"),
    TAOBAO_CLIENT("taobao_client"),
    TAOBAO_CLIENT_CROSS_STORE("taobao_client$cross_store"),
    YUNOS_HOME_FRIDGE("yunos_home_fridge"),
    DEFAULT_CLIENT("default_client"),
    DEFAULT_CLIENT_CROSS_STORE("default_client$cross_store");
    
    private String value;

    private CartFrom(String value2) {
        this.value = value2;
    }

    public String getValue() {
        return this.value;
    }

    public boolean isCrossCartFrom() {
        if (this == DEFAULT_CLIENT_CROSS_STORE || this == TAOBAO_CLIENT_CROSS_STORE || this == TSM_NATIVE_TAOBAO_CROSS_STORE) {
            return true;
        }
        return false;
    }

    public CartFrom convert2cross() {
        CartFrom cartFrom = DEFAULT_CLIENT_CROSS_STORE;
        if (this == TAOBAO_CLIENT) {
            return TAOBAO_CLIENT_CROSS_STORE;
        }
        if (this == TSM_NATIVE_TAOBAO) {
            return TSM_NATIVE_TAOBAO_CROSS_STORE;
        }
        return cartFrom;
    }

    public CartFrom convert2mtop() {
        if (this == TSM_NATIVE_TAOBAO_CROSS_STORE) {
            return TSM_NATIVE_TAOBAO;
        }
        if (this == TAOBAO_CLIENT_CROSS_STORE) {
            return TAOBAO_CLIENT;
        }
        if (this == DEFAULT_CLIENT_CROSS_STORE) {
            return DEFAULT_CLIENT;
        }
        return this;
    }

    public static CartFrom parseCartFrom(String cartFromValue) {
        if (!StringUtils.isBlank(cartFromValue)) {
            for (CartFrom cartFrom : values()) {
                if (cartFrom.getValue().equals(cartFromValue)) {
                    return cartFrom;
                }
            }
        }
        return DEFAULT_CLIENT;
    }
}
