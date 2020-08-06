package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngineContext;
import com.taobao.wireless.trade.mbuy.sdk.engine.LinkageType;
import com.taobao.wireless.trade.mbuy.sdk.engine.RollbackProtocol;

public class ToggleComponent extends Component {
    public ToggleComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public JSONObject convertToFinalSubmitData() {
        BuyEngineContext context = this.engine.getContext();
        context.addRecoveryEntry(this.fields, "name", getName());
        context.addRecoveryEntry(this.fields, "url", getUrl());
        this.fields.remove("name");
        this.fields.remove("url");
        return super.convertToFinalSubmitData();
    }

    public void setChecked(final Boolean checked) {
        BuyEngineContext context = this.engine.getContext();
        if (getLinkageType() == LinkageType.REQUEST) {
            context.setRollbackProtocol(new RollbackProtocol() {
                public void rollback() {
                    ToggleComponent.this.fields.put("checked", (Object) Boolean.valueOf(!checked.booleanValue()));
                }
            });
        }
        this.fields.put("checked", (Object) checked);
        notifyLinkageDelegate();
    }

    public String getName() {
        return this.fields.getString("name");
    }

    public String getUrl() {
        return this.fields.getString("url");
    }

    public boolean isChecked() {
        return this.fields.getBooleanValue("checked");
    }
}
