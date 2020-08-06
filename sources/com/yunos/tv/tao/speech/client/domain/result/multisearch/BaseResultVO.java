package com.yunos.tv.tao.speech.client.domain.result.multisearch;

import java.io.Serializable;
import java.util.ArrayList;

public class BaseResultVO implements Serializable {
    private String spoken;
    private ArrayList<String> tips;

    public ArrayList<String> getTips() {
        return this.tips;
    }

    public void setTips(ArrayList<String> tips2) {
        this.tips = tips2;
    }

    public String getSpoken() {
        return this.spoken;
    }

    public void setSpoken(String spoken2) {
        this.spoken = spoken2;
    }
}
