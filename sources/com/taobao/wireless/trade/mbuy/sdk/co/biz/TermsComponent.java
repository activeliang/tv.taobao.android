package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngineContext;
import com.taobao.wireless.trade.mbuy.sdk.engine.LinkageType;
import com.taobao.wireless.trade.mbuy.sdk.engine.RollbackProtocol;

public class TermsComponent extends Component {
    public TermsComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public String getSimpleTerms() {
        return this.fields.getString("simpleTerms");
    }

    public String getUrl() {
        return this.fields.getString("url");
    }

    public boolean isAgree() {
        return this.fields.getBooleanValue("agree");
    }

    public void setAgree(final boolean agree) {
        BuyEngineContext context = this.engine.getContext();
        if (getLinkageType() == LinkageType.REQUEST) {
            context.setRollbackProtocol(new RollbackProtocol() {
                public void rollback() {
                    TermsComponent.this.fields.put("agree", (Object) Boolean.valueOf(!agree));
                }
            });
        }
        this.fields.put("agree", (Object) Boolean.valueOf(agree));
        notifyLinkageDelegate();
    }

    /* access modifiers changed from: protected */
    public String getValidateContent() {
        return isAgree() ? "true" : "false";
    }
}
