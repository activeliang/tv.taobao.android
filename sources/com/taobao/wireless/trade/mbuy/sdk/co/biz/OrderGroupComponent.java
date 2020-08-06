package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OrderGroupComponent extends Component {
    public OrderGroupComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public List<String> getCoupon() {
        JSONArray array = this.fields.getJSONArray("coupon");
        if (array == null || array.isEmpty()) {
            return null;
        }
        List<String> coupons = new ArrayList<>();
        Iterator<Object> it = array.iterator();
        while (it.hasNext()) {
            coupons.add((String) it.next());
        }
        return coupons;
    }
}
