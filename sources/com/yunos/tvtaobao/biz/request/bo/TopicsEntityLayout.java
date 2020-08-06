package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class TopicsEntityLayout implements Serializable {
    private static final long serialVersionUID = -4174664940715806550L;
    private String bg_color;
    private int border;
    private int style;

    public String getBg_color() {
        return this.bg_color;
    }

    public void setBg_color(String bg_color2) {
        this.bg_color = bg_color2;
    }

    public int getStyle() {
        return this.style;
    }

    public void setStyle(int style2) {
        this.style = style2;
    }

    public int getBorder() {
        return this.border;
    }

    public void setBorder(int border2) {
        this.border = border2;
    }
}
