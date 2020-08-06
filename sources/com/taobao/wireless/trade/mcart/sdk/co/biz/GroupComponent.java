package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;

public class GroupComponent extends Component {
    private GroupPromotion mGroupPromotion = null;

    public GroupComponent(JSONObject data, CartFrom cartFrom) {
        super(data, cartFrom);
    }

    public void reload(JSONObject data) {
        super.reload(data);
        this.mGroupPromotion = generateGroupPromotion();
    }

    public void updatePromotion(JSONObject promotion) {
        this.fields.put("groupPromotion", (Object) promotion);
        this.mGroupPromotion = generateGroupPromotion();
    }

    public String getGroupId() {
        return this.fields.getString("groupId");
    }

    public String getRuleId() {
        return this.fields.getString("ruleId");
    }

    public boolean getIsRelationItem() {
        return this.fields.getBooleanValue("isRelationItem");
    }

    public String getBackgroundColor() {
        return this.fields.getString(TuwenConstants.PARAMS.BACKGROUND_COLOR);
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public GroupPromotion getGroupPromotion() {
        if (this.mGroupPromotion == null) {
            this.mGroupPromotion = generateGroupPromotion();
        }
        return this.mGroupPromotion;
    }

    private GroupPromotion generateGroupPromotion() {
        JSONObject groupPromotion = this.fields.getJSONObject("groupPromotion");
        if (groupPromotion != null) {
            try {
                return new GroupPromotion(groupPromotion);
            } catch (Throwable th) {
            }
        }
        return null;
    }

    public JSONObject convertToSubmitData() {
        JSONObject value = new JSONObject();
        value.put("groupId", (Object) this.fields.getString("groupId"));
        value.put("ruleId", (Object) this.fields.getString("ruleId"));
        JSONObject groupPromotion = this.fields.getJSONObject("groupPromotion");
        if (groupPromotion != null) {
            JSONObject promotion = new JSONObject();
            promotion.put("subTitle", (Object) groupPromotion.getString("subTitle"));
            value.put("groupPromotion", (Object) promotion);
        }
        JSONObject data = new JSONObject();
        data.put("fields", (Object) value);
        return data;
    }

    public String toString() {
        return super.toString() + " - GroupComponent [groupId=" + getGroupId() + ",getIsRelationItem=" + getIsRelationItem() + "]";
    }
}
