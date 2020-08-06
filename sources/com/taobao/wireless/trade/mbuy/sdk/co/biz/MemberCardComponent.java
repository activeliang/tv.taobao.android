package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngineContext;
import com.taobao.wireless.trade.mbuy.sdk.engine.LinkageType;
import com.taobao.wireless.trade.mbuy.sdk.engine.RollbackProtocol;

public class MemberCardComponent extends Component {
    public MemberCardComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public String getName() {
        return this.fields.getString("name");
    }

    public String getAgreement() {
        return this.fields.getString("agreement");
    }

    public String getPrice() {
        return this.fields.getString("price");
    }

    public String getDesc() {
        return this.fields.getString("desc");
    }

    public String getAgreementUrl() {
        return this.fields.getString("agreementUrl");
    }

    public String getPromotion() {
        return this.fields.getString("promotion");
    }

    public String getIconUrl() {
        return this.fields.getString("iconUrl");
    }

    public boolean isChecked() {
        return this.fields.getBooleanValue("checked");
    }

    public void setChecked(final boolean checked) {
        BuyEngineContext context = this.engine.getContext();
        if (getLinkageType() == LinkageType.REQUEST) {
            context.setRollbackProtocol(new RollbackProtocol() {
                public void rollback() {
                    MemberCardComponent.this.fields.put("checked", (Object) Boolean.valueOf(!checked));
                }
            });
        }
        this.fields.put("checked", (Object) Boolean.valueOf(checked));
        notifyLinkageDelegate();
    }

    /* access modifiers changed from: protected */
    public String getValidateContent() {
        String value = getValue();
        return value != null ? value : "";
    }

    public String getValue() {
        return this.fields.getString("checked");
    }
}
