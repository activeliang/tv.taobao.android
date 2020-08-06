package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PromotionComponent extends Component {
    public static final String BUNDLE_TYPE = "bundle";
    public static final String ITEM_TYPE = "item";
    public static final String ORDER_TYPE = "order";
    public static final String SHOP_TYPE = "shop";

    public PromotionComponent(JSONObject data, CartFrom cartFrom) {
        super(data, cartFrom);
    }

    public String getPromotionType() {
        return this.fields.getString("promotionType");
    }

    public List<String> getTitles() {
        List<String> titleList = new ArrayList<>();
        JSONArray titleArray = this.fields.getJSONArray("titles");
        if (titleArray != null && !titleArray.isEmpty()) {
            Iterator<Object> it = titleArray.iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                if (obj != null) {
                    try {
                        titleList.add((String) obj);
                    } catch (Throwable th) {
                    }
                }
            }
        }
        return titleList;
    }

    public String toString() {
        return super.toString() + " - PromotionComponent [promotionType=" + getPromotionType() + ",titles=" + getTitles() + "]";
    }
}
