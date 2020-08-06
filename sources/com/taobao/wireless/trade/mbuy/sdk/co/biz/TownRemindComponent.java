package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngineContext;
import com.taobao.wireless.trade.mbuy.sdk.engine.LinkageType;
import com.taobao.wireless.trade.mbuy.sdk.engine.RollbackProtocol;
import com.taobao.wireless.trade.mbuy.sdk.engine.ValidateResult;

public class TownRemindComponent extends Component {
    public TownRemindComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public JSONObject getSourceAddress() {
        return this.fields.getJSONObject("sourceAddress");
    }

    private JSONObject getTownAddress() {
        return this.fields.getJSONObject("townAddress");
    }

    public String getTown() {
        JSONObject townAddress = getTownAddress();
        if (townAddress != null) {
            return townAddress.getString("town");
        }
        return null;
    }

    public String getTownDivisionCode() {
        JSONObject townAddress = getTownAddress();
        if (townAddress != null) {
            return townAddress.getString("townDivisionCode");
        }
        return null;
    }

    public String getTip() {
        JSONObject townAddress = getTownAddress();
        if (townAddress != null) {
            return townAddress.getString("tip");
        }
        return null;
    }

    public String getConfirmBtn() {
        JSONObject townAddress = getTownAddress();
        if (townAddress != null) {
            return townAddress.getString("confirmBtn");
        }
        return null;
    }

    public String getUpdateBtn() {
        JSONObject townAddress = getTownAddress();
        if (townAddress != null) {
            return townAddress.getString("updateBtn");
        }
        return null;
    }

    public String getSupplementsBtn() {
        JSONObject townAddress = getTownAddress();
        if (townAddress != null) {
            return townAddress.getString("supplementsBtn");
        }
        return null;
    }

    public boolean isNeedSupplementTownAddress() {
        JSONObject townAddress = getTownAddress();
        if (townAddress != null) {
            return townAddress.getBooleanValue("supplementsFlag");
        }
        return false;
    }

    public boolean isBlockOrder() {
        return this.fields.getBooleanValue("blockOrder");
    }

    public void setBlockOrder(boolean isBlockOrder) {
        this.fields.put("blockOrder", (Object) Boolean.valueOf(isBlockOrder));
    }

    public boolean isNeedSaveTown() {
        return this.fields.getBooleanValue("needSaveTown");
    }

    public void setNeedSaveTown(final boolean needSaveTown) {
        BuyEngineContext context = this.engine.getContext();
        if (getLinkageType() == LinkageType.REQUEST) {
            context.setRollbackProtocol(new RollbackProtocol() {
                public void rollback() {
                    TownRemindComponent.this.fields.put("needSaveTown", (Object) Boolean.valueOf(!needSaveTown));
                }
            });
        }
        this.fields.put("needSaveTown", (Object) Boolean.valueOf(needSaveTown));
        notifyLinkageDelegate();
    }

    public ValidateResult validate() {
        ValidateResult result = new ValidateResult();
        result.setValid(!isBlockOrder());
        return result;
    }
}
