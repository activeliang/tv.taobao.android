package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import android.util.Pair;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActivityComponent extends Component {
    private List<ActivityGift> gifts = loadGifts(this.fields.getJSONArray("gifts"));

    public ActivityComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public String getPromotionId() {
        return this.fields.getString("promotionId");
    }

    public String getName() {
        return this.fields.getString("name");
    }

    public String getSummary() {
        return this.fields.getString("description");
    }

    public int getMaxGiftQuantity() {
        return this.fields.getIntValue("maxGiftQuantity");
    }

    public List<ActivityGift> getGifts() {
        return this.gifts;
    }

    public void reload(JSONObject data) {
        super.reload(data);
        try {
            this.gifts = loadGifts(this.fields.getJSONArray("gifts"));
        } catch (Throwable th) {
        }
    }

    public Pair<Boolean, String> checkGifts() {
        if (this.gifts == null || this.gifts.size() == 0) {
            return Pair.create(false, "赠品已全部领完");
        }
        int n = 0;
        for (ActivityGift gift : this.gifts) {
            if (gift.isValid()) {
                n++;
            }
        }
        if (n == 0) {
            return Pair.create(false, "赠品已全部领完");
        }
        if (n <= getMaxGiftQuantity()) {
            return Pair.create(false, "已无其他赠品可选");
        }
        return Pair.create(true, (Object) null);
    }

    public Pair<Boolean, String> isValidQuantity() {
        int currentQuantity = 0;
        for (ActivityGift gift : this.gifts) {
            if (gift.isSelected()) {
                currentQuantity++;
            }
        }
        int giftsCount = this.gifts.size();
        int maxGiftCount = getMaxGiftQuantity();
        if (currentQuantity < maxGiftCount) {
            return Pair.create(false, String.format("还可以选择%d件赠品", new Object[]{Integer.valueOf(maxGiftCount - currentQuantity)}));
        } else if (currentQuantity > maxGiftCount) {
            return Pair.create(false, String.format("只能在%d件赠品中选择%d件赠品", new Object[]{Integer.valueOf(giftsCount), Integer.valueOf(maxGiftCount)}));
        } else {
            notifyLinkageDelegate();
            return Pair.create(true, (Object) null);
        }
    }

    private List<ActivityGift> loadGifts(JSONArray giftJSONArray) {
        if (giftJSONArray == null) {
            throw new IllegalStateException();
        }
        List<ActivityGift> gifts2 = new ArrayList<>(giftJSONArray.size());
        Iterator<Object> it = giftJSONArray.iterator();
        while (it.hasNext()) {
            gifts2.add(new ActivityGift((JSONObject) it.next(), this.engine));
        }
        return gifts2;
    }
}
