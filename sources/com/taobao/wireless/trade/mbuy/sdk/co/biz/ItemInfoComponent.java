package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.co.misc.ItemIcons;
import com.taobao.wireless.trade.mbuy.sdk.co.misc.SkuLevelInfo;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mtopsdk.common.util.SymbolExpUtil;

public class ItemInfoComponent extends Component {
    private ItemIcons itemIcons;
    private String skuInfo;
    private List<SkuLevelInfo> skuLevelInfoList;

    public ItemInfoComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public void reload(JSONObject data) {
        super.reload(data);
        this.itemIcons = null;
        this.skuLevelInfoList = null;
        this.skuInfo = null;
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public String getPic() {
        return this.fields.getString("pic");
    }

    public String getCurrencySymbol() {
        return this.engine.getCurrencySymbol();
    }

    public String getSkuInfo(boolean filter) {
        if (this.skuInfo != null) {
            return this.skuInfo;
        }
        JSONArray skuInfoArray = this.fields.getJSONArray("skuInfo");
        if (skuInfoArray == null) {
            return null;
        }
        StringBuilder skuInfoBuilder = new StringBuilder();
        int size = skuInfoArray.size();
        int i = 0;
        while (i < size) {
            try {
                JSONObject kv = (JSONObject) skuInfoArray.get(i);
                if (!filter || !kv.getBooleanValue("forOld")) {
                    String name = kv.getString("name");
                    String value = kv.getString("value");
                    if (name != null && value != null && !name.isEmpty() && !value.isEmpty()) {
                        if (i != 0) {
                            skuInfoBuilder.append(",");
                        }
                        skuInfoBuilder.append(name);
                        skuInfoBuilder.append(SymbolExpUtil.SYMBOL_COLON);
                        skuInfoBuilder.append(value);
                    }
                    i++;
                } else {
                    i++;
                }
            } catch (Throwable th) {
            }
        }
        this.skuInfo = skuInfoBuilder.toString();
        return this.skuInfo;
    }

    public String getSkuInfo() {
        return getSkuInfo(false);
    }

    public String getPrice() {
        return this.fields.getString("price");
    }

    public String getActivityIcon() {
        JSONObject iconNode = this.fields.getJSONObject("activityIcon");
        if (iconNode != null) {
            return iconNode.getString("image");
        }
        return null;
    }

    public String getActivityIcon2() {
        JSONObject iconNode = this.fields.getJSONObject("activityIcon");
        if (iconNode != null) {
            return iconNode.getString(UpdatePreference.IMAGE2);
        }
        return null;
    }

    public boolean isGift() {
        return this.fields.getBooleanValue("isGift");
    }

    public String getGiftIcon() {
        return this.fields.getString("giftIcon");
    }

    public List<SkuLevelInfo> getSkuLevelInfos() {
        if (this.skuLevelInfoList != null) {
            return this.skuLevelInfoList;
        }
        JSONArray skuLevelInfoArray = this.fields.getJSONArray("skuLevelInfo");
        if (skuLevelInfoArray == null || skuLevelInfoArray.isEmpty()) {
            return null;
        }
        this.skuLevelInfoList = new ArrayList(skuLevelInfoArray.size());
        Iterator<Object> it = skuLevelInfoArray.iterator();
        while (it.hasNext()) {
            try {
                this.skuLevelInfoList.add(new SkuLevelInfo((JSONObject) it.next()));
            } catch (Throwable th) {
            }
        }
        return this.skuLevelInfoList;
    }

    public ItemIcons getItemIcons() {
        if (this.itemIcons != null) {
            return this.itemIcons;
        }
        try {
            JSONObject iconsObj = this.fields.getJSONObject("icons");
            if (iconsObj != null) {
                this.itemIcons = new ItemIcons(iconsObj);
            }
            return this.itemIcons;
        } catch (Throwable th) {
            return null;
        }
    }
}
