package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.taobao.wireless.trade.mbuy.sdk.engine.LinkageType;
import com.taobao.wireless.trade.mbuy.sdk.engine.RollbackProtocol;

public class VerificationCodeComponent extends Component {
    /* access modifiers changed from: private */
    public long beginCountDownTime;

    public VerificationCodeComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public String getPlaceholder() {
        return this.fields.getString("placeholder");
    }

    public String getValue() {
        return this.fields.getString("value");
    }

    public void setValue(String value) {
        this.fields.put("value", (Object) value);
    }

    private JSONObject getButton() {
        return this.fields.getJSONObject("button");
    }

    public int getBtnClickCount() {
        JSONObject button = getButton();
        if (button != null) {
            return button.getIntValue("clickCount");
        }
        return 0;
    }

    public String getBtnTitle() {
        JSONObject button = getButton();
        if (button != null) {
            return button.getString("btnTitle");
        }
        return null;
    }

    public int getBtnCountDownSecond() {
        JSONObject button = getButton();
        if (button != null) {
            return button.getIntValue("countDownSecond");
        }
        return 0;
    }

    public boolean isBeginCountDown() {
        return this.beginCountDownTime > 0;
    }

    public void beginCountDown() {
        this.beginCountDownTime = System.currentTimeMillis();
    }

    public int getRemainBtnCountDownSecond() {
        int countDownSecond = getBtnCountDownSecond();
        if (!isBeginCountDown()) {
            return countDownSecond;
        }
        int timeInterval = Math.round(((float) (System.currentTimeMillis() - this.beginCountDownTime)) / 1000.0f);
        if (countDownSecond > timeInterval) {
            return countDownSecond - timeInterval;
        }
        return 0;
    }

    public String getBtnCountDownTitle() {
        JSONObject button = getButton();
        if (button != null) {
            return button.getString("btnCountDownTitle");
        }
        return null;
    }

    public void setBtnClickCount(int btnClickCount) {
        final JSONObject button = getButton();
        if (btnClickCount >= 0 && button != null) {
            final long previousTime = this.beginCountDownTime;
            this.beginCountDownTime = 0;
            if (this.linkageType == LinkageType.REQUEST) {
                final String originalClickCount = button.getString("clickCount");
                this.engine.getContext().setRollbackProtocol(new RollbackProtocol() {
                    public void rollback() {
                        button.put("clickCount", (Object) originalClickCount);
                        long unused = VerificationCodeComponent.this.beginCountDownTime = previousTime;
                    }
                });
            }
            button.put("clickCount", (Object) String.valueOf(btnClickCount));
            notifyLinkageDelegate();
        }
    }
}
