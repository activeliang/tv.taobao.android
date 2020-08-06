package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngineContext;
import com.taobao.wireless.trade.mbuy.sdk.engine.LinkageType;
import com.taobao.wireless.trade.mbuy.sdk.engine.RollbackProtocol;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CouponComponent extends Component {
    private List<CouponOption> options = loadOptions(this.promotionDetail);
    protected JSONObject promotionDetail = this.fields.getJSONObject("promotionDetail");

    public CouponComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public void reload(JSONObject data) {
        super.reload(data);
        this.promotionDetail = this.fields.getJSONObject("promotionDetail");
        this.options = loadOptions(this.promotionDetail);
    }

    private List<CouponOption> loadOptions(JSONObject promotionDetail2) {
        JSONArray optionJSONArray;
        List<CouponOption> details = null;
        if (!(promotionDetail2 == null || (optionJSONArray = promotionDetail2.getJSONArray("detail")) == null || optionJSONArray.isEmpty())) {
            details = new ArrayList<>(optionJSONArray.size());
            Iterator<Object> it = optionJSONArray.iterator();
            while (it.hasNext()) {
                details.add(new CouponOption((JSONObject) it.next()));
            }
        }
        return details;
    }

    public String getIcon() {
        return this.fields.getString("icon");
    }

    public String getTotalValue() {
        return this.fields.getString("totalValue");
    }

    public String getUrl() {
        return this.fields.getString("url");
    }

    public String getValue() {
        return this.fields.getString("value");
    }

    public boolean getSelected() {
        return this.fields.getBooleanValue("selected");
    }

    public void setSelected(final boolean isSelected) {
        BuyEngineContext context = this.engine.getContext();
        if (getLinkageType() == LinkageType.REQUEST) {
            context.setRollbackProtocol(new RollbackProtocol() {
                public void rollback() {
                    CouponComponent.this.fields.put("selected", (Object) Boolean.valueOf(!isSelected));
                }
            });
        }
        this.fields.put("selected", (Object) Boolean.valueOf(isSelected));
        notifyLinkageDelegate();
    }

    public String getDetailTitle() {
        if (this.promotionDetail != null) {
            return this.promotionDetail.getString("title");
        }
        return null;
    }

    public String getDetailIcon() {
        if (this.promotionDetail != null) {
            return this.promotionDetail.getString("detailIcon");
        }
        return null;
    }

    public List<CouponOption> getOptions() {
        return this.options;
    }
}
