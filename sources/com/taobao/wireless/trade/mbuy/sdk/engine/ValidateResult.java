package com.taobao.wireless.trade.mbuy.sdk.engine;

import com.taobao.wireless.trade.mbuy.sdk.co.Component;

public class ValidateResult {
    private String errorMsg;
    private Component invalidComponent;
    private boolean valid = true;

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg2) {
        this.errorMsg = errorMsg2;
    }

    public boolean isValid() {
        return this.valid;
    }

    public void setValid(boolean valid2) {
        this.valid = valid2;
    }

    public Component getInvalidComponent() {
        return this.invalidComponent;
    }

    public void setInvalidComponent(Component invalidComponent2) {
        this.invalidComponent = invalidComponent2;
    }
}
