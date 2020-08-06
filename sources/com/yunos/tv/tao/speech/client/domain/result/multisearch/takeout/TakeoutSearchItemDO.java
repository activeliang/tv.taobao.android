package com.yunos.tv.tao.speech.client.domain.result.multisearch.takeout;

import com.yunos.tv.tao.speech.client.domain.result.newtakeout.TakeOutItems;
import java.util.ArrayList;
import java.util.List;

public class TakeoutSearchItemDO {
    private String hasNext;
    private List<TakeOutItems> items;
    private String keyword;
    private String offset;
    private String spoken;
    private String spokenTxt;
    private List<String> tips;

    public void setHasNext(String hasNext2) {
        this.hasNext = hasNext2;
    }

    public String getHasNext() {
        return this.hasNext;
    }

    public void setItems(List<TakeOutItems> items2) {
        this.items = items2;
    }

    public List<TakeOutItems> getItems() {
        return this.items;
    }

    public void setKeyword(String keyword2) {
        this.keyword = keyword2;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public String getSpoken() {
        return this.spoken == null ? "" : this.spoken;
    }

    public void setSpoken(String spoken2) {
        this.spoken = spoken2;
    }

    public String getSpokenTxt() {
        return this.spokenTxt == null ? "" : this.spokenTxt;
    }

    public void setSpokenTxt(String spokenTxt2) {
        this.spokenTxt = spokenTxt2;
    }

    public List<String> getTips() {
        if (this.tips == null) {
            return new ArrayList();
        }
        return this.tips;
    }

    public void setTips(List<String> tips2) {
        this.tips = tips2;
    }

    public String getOffset() {
        return this.offset == null ? "" : this.offset;
    }

    public void setOffset(String offset2) {
        this.offset = offset2;
    }
}
