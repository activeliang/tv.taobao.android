package com.tvtaobao.voicesdk.bean;

public class PriceData {
    private String code;
    private String end;
    private PriceModel model;
    private String tip;
    private String tts;

    public void setCode(String code2) {
        this.code = code2;
    }

    public String getCode() {
        return this.code;
    }

    public void setEnd(String end2) {
        this.end = end2;
    }

    public String getEnd() {
        return this.end;
    }

    public void setModel(PriceModel model2) {
        this.model = model2;
    }

    public PriceModel getModel() {
        return this.model;
    }

    public void setTts(String tts2) {
        this.tts = tts2;
    }

    public String getTts() {
        return this.tts;
    }

    public String getTip() {
        return this.tip;
    }

    public void setTip(String tip2) {
        this.tip = tip2;
    }
}
