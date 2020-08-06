package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngineContext;
import com.taobao.wireless.trade.mbuy.sdk.engine.LinkageType;
import com.taobao.wireless.trade.mbuy.sdk.engine.RollbackProtocol;

public class InstallmentToggleComponent extends Component {
    public InstallmentToggleComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public String getSubtitle() {
        return this.fields.getString("subtitle");
    }

    public boolean isChecked() {
        return this.fields.getBooleanValue("checked");
    }

    public void setChecked(final boolean checked) {
        BuyEngineContext context = this.engine.getContext();
        if (getLinkageType() == LinkageType.REQUEST) {
            context.setRollbackProtocol(new RollbackProtocol() {
                public void rollback() {
                    InstallmentToggleComponent.this.fields.put("checked", (Object) Boolean.valueOf(!checked));
                }
            });
        }
        this.fields.put("checked", (Object) Boolean.valueOf(checked));
        notifyLinkageDelegate();
    }
}
