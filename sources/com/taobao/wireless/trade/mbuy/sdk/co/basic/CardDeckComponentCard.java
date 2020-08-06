package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

public abstract class CardDeckComponentCard {
    protected JSONObject cardData;
    private List<CardDeckComponentCheckBoxItemData> checkBoxItems;
    private String name;
    private String selectedValue;
    private String tips;
    private String title;

    public abstract List<CardDeckComponentCheckBoxItemData> getCheckBoxItems();

    public String getName() {
        return this.name;
    }

    public void setName(String cardName) {
        this.name = cardName;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String cardTitle) {
        this.title = cardTitle;
    }

    public String getSelectedValue() {
        return this.selectedValue;
    }

    public void setSelectedValue(String selectedValue2) {
        this.selectedValue = selectedValue2;
    }

    public String getTips() {
        return this.tips;
    }

    public void setTips(String tips2) {
        this.tips = tips2;
    }

    public CardDeckComponentCard(JSONObject cardData2) {
        if (cardData2 != null) {
            String cardTitle = cardData2.getString("cardTitle");
            if (!TextUtils.isEmpty(cardTitle)) {
                setTitle(cardTitle);
            }
            String cardName = cardData2.getString("cardName");
            if (!TextUtils.isEmpty(cardName)) {
                setName(cardName);
            }
            String selectedValue2 = cardData2.getString("selectedValue");
            if (!TextUtils.isEmpty(selectedValue2)) {
                setSelectedValue(selectedValue2);
            }
            String tips2 = cardData2.getString("tips");
            if (!TextUtils.isEmpty(tips2)) {
                setTips(tips2);
            }
            this.cardData = cardData2;
        }
    }
}
