package com.yunos.tv.tao.speech.client.domain.result.newtakeout;

public class SupportTags {
    private Background background;
    private String border;
    private String color;
    private String tagCode;
    private String text;
    private String type;

    public void setBorder(String border2) {
        this.border = border2;
    }

    public String getBorder() {
        return this.border;
    }

    public void setTagCode(String tagCode2) {
        this.tagCode = tagCode2;
    }

    public String getTagCode() {
        return this.tagCode;
    }

    public void setColor(String color2) {
        this.color = color2;
    }

    public String getColor() {
        return this.color;
    }

    public void setBackground(Background background2) {
        this.background = background2;
    }

    public Background getBackground() {
        return this.background;
    }

    public void setText(String text2) {
        this.text = text2;
    }

    public String getText() {
        return this.text;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getType() {
        return this.type;
    }
}
