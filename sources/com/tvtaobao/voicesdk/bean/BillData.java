package com.tvtaobao.voicesdk.bean;

public class BillData {
    private String code;
    private String end;
    private BillModel model;
    private String spoken;
    private String tip;

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

    public void setModel(BillModel model2) {
        this.model = model2;
    }

    public BillModel getModel() {
        return this.model;
    }

    public String getTip() {
        return this.tip;
    }

    public void setTip(String tip2) {
        this.tip = tip2;
    }

    public String getSpoken() {
        return this.spoken;
    }

    public void setSpoken(String spoken2) {
        this.spoken = spoken2;
    }
}
