package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CardDeckComponentStandardTypeCard extends CardDeckComponentCard {
    public CardDeckComponentStandardTypeCard(JSONObject cardData) {
        super(cardData);
    }

    public List<CardDeckComponentCheckBoxItemData> getCheckBoxItems() {
        JSONArray cells = this.cardData.getJSONArray("cells");
        if (cells == null || cells.size() <= 0) {
            return null;
        }
        int size = cells.size();
        List<CardDeckComponentCheckBoxItemData> checkBoxItemList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            JSONObject cellItem = cells.getJSONObject(i);
            String displayTitle = cellItem.getString("displayTitle");
            String realValue = cellItem.getString("realValue");
            String child = cellItem.getString("child");
            if (!TextUtils.isEmpty(displayTitle) && !TextUtils.isEmpty(realValue)) {
                CardDeckComponentCheckBoxItemData item = new CardDeckComponentCheckBoxItemData();
                item.setDisplayTitle(displayTitle);
                item.setRealValue(realValue);
                item.setChild(child);
                checkBoxItemList.add(item);
            }
        }
        return checkBoxItemList;
    }
}
