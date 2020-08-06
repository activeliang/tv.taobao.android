package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.co.ComponentTag;
import com.taobao.wireless.trade.mbuy.sdk.co.ComponentType;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngineContext;
import com.taobao.wireless.trade.mbuy.sdk.engine.LinkageType;
import com.taobao.wireless.trade.mbuy.sdk.engine.RollbackProtocol;
import com.tvtaobao.voicesdk.register.type.ActionType;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import java.util.HashMap;
import java.util.Map;

public class CardDeckComponent extends Component {
    private Map<String, JSONObject> mCards = null;
    private JSONArray mShownSequence = null;

    public CardDeckComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
        processCompData();
    }

    private void processCompData() {
        this.mCards = new HashMap();
        this.mShownSequence = this.fields.getJSONArray("shownSequence");
        JSONArray cards = this.fields.getJSONArray("cards");
        if (cards != null && cards.size() > 0) {
            for (int i = 0; i < cards.size(); i++) {
                JSONObject card = cards.getJSONObject(i);
                if (card != null) {
                    String cardName = card.getString("cardName");
                    if (!TextUtils.isEmpty(cardName)) {
                        this.mCards.put(cardName, card);
                    }
                }
            }
        }
    }

    public void reload(JSONObject data) {
        super.reload(data);
        processCompData();
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public String getSubTitle() {
        return this.fields.getString("subtitle");
    }

    public String getDesc() {
        return this.fields.getString("desc");
    }

    /* access modifiers changed from: private */
    public void setDesc(String desc) {
        this.fields.put("desc", (Object) desc);
    }

    public String getCancelBtnTitle() {
        return this.fields.getString(UpdatePreference.UT_CANCEL);
    }

    public String getConfirmBtnTitle() {
        return this.fields.getString(ActionType.CONFIRM);
    }

    public CardDeckComponentCard getCard(String cardName) {
        JSONObject cardData;
        if (!TextUtils.isEmpty(cardName) && (cardData = this.mCards.get(cardName)) != null && cardData.size() > 0) {
            boolean isStyleDetail = false;
            JSONArray cells = cardData.getJSONArray("cells");
            if (cells != null && cells.size() > 0) {
                isStyleDetail = true;
            }
            if (isStyleDetail) {
                return new CardDeckComponentStandardTypeCard(cardData);
            }
            JSONObject time = cardData.getJSONObject("time");
            if (time != null && time.size() > 0) {
                return new CardDeckComponentPeriodPointTypeCard(cardData);
            }
        }
        return null;
    }

    public CardDeckComponentCard getRootCard() {
        return getCard(getRootCardName());
    }

    public CardDeckComponentCard getSecondCard() {
        return getCard(getSecondCardName());
    }

    /* access modifiers changed from: private */
    public void setRootCardSelectedValue(String value) {
        JSONObject cardData = getRootCardData();
        if (cardData != null) {
            cardData.put("selectedValue", (Object) value);
        }
    }

    private String getRootCardSelectedValue() {
        JSONObject cardData = getRootCardData();
        if (cardData != null) {
            return cardData.getString("selectedValue");
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void setSecondCardSelectedValue(String value) {
        JSONObject cardData = getSecondCardData();
        if (cardData != null) {
            cardData.put("selectedValue", (Object) value);
        }
    }

    private String getSecondCardSelectedValue() {
        JSONObject cardData = getSecondCardData();
        if (cardData != null) {
            return cardData.getString("selectedValue");
        }
        return null;
    }

    private JSONObject getRootCardData() {
        String cardName = getRootCardName();
        if (!TextUtils.isEmpty(cardName)) {
            return this.mCards.get(cardName);
        }
        return null;
    }

    private JSONObject getSecondCardData() {
        String cardName = getSecondCardName();
        if (!TextUtils.isEmpty(cardName)) {
            return this.mCards.get(cardName);
        }
        return null;
    }

    private String getRootCardName() {
        return this.mShownSequence.getString(0);
    }

    private String getSecondCardName() {
        return this.mShownSequence.getString(1);
    }

    /* access modifiers changed from: private */
    public void updateSecondCardNameIntoShownSequence(String cardName) {
        if (!TextUtils.isEmpty(cardName)) {
            this.mShownSequence.set(1, cardName);
        }
    }

    public String getTag() {
        return ComponentTag.CARD_DECK.desc;
    }

    public ComponentType getType() {
        return ComponentType.CARDDECK;
    }

    public boolean enableCardDeck() {
        if (this.mShownSequence == null || this.mCards == null || this.mShownSequence.size() < 2 || this.mCards.size() < 2) {
            return false;
        }
        return true;
    }

    public void setCardSelectedValue(String selectedSecondCardName, String rootCardSelectedValue, String rootCardSelectedTitle, String secondCardSelectedValue, String secondCardSelectedTitle) {
        BuyEngineContext context = this.engine.getContext();
        String desc = rootCardSelectedTitle + " " + secondCardSelectedTitle;
        if (getLinkageType() == LinkageType.REQUEST) {
            final String lastRootCardSelectedValue = getRootCardSelectedValue();
            final String lastSecondCardSelectedValue = getSecondCardSelectedValue();
            final String orignalDesc = getDesc();
            final String orignalSecondCardName = getSecondCardName();
            context.setRollbackProtocol(new RollbackProtocol() {
                public void rollback() {
                    CardDeckComponent.this.setRootCardSelectedValue(lastRootCardSelectedValue);
                    CardDeckComponent.this.updateSecondCardNameIntoShownSequence(orignalSecondCardName);
                    CardDeckComponent.this.setSecondCardSelectedValue(lastSecondCardSelectedValue);
                    CardDeckComponent.this.setDesc(orignalDesc);
                }
            });
        }
        setDesc(desc);
        setRootCardSelectedValue(rootCardSelectedValue);
        updateSecondCardNameIntoShownSequence(selectedSecondCardName);
        setSecondCardSelectedValue(secondCardSelectedValue);
        notifyLinkageDelegate();
    }
}
