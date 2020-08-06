package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShopPromotionDetailComponent extends Component {
    private List<ShopPromotionDetailOption> options = loadOptions();

    public ShopPromotionDetailComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public void reload(JSONObject data) {
        super.reload(data);
        this.options = loadOptions();
    }

    private List<ShopPromotionDetailOption> loadOptions() {
        JSONArray promotionInfo = this.fields.getJSONArray("promotionInfo");
        if (promotionInfo == null || promotionInfo.isEmpty()) {
            return null;
        }
        List<ShopPromotionDetailOption> options2 = new ArrayList<>(promotionInfo.size());
        Iterator<Object> it = promotionInfo.iterator();
        while (it.hasNext()) {
            options2.add(new ShopPromotionDetailOption((JSONObject) it.next()));
        }
        return options2;
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public List<ShopPromotionDetailOption> getOptions() {
        return this.options;
    }
}
