package com.yunos.tv.tao.speech.client.domain.result.newtakeout;

import java.util.List;

public class Theme {
    private String defaultColor;
    private String headerStyle;
    private String hongbaoStyle;
    private String priceColor;
    private String thirdTabName;
    private List<String> vanishFields;

    public void setDefaultColor(String defaultColor2) {
        this.defaultColor = defaultColor2;
    }

    public String getDefaultColor() {
        return this.defaultColor;
    }

    public void setPriceColor(String priceColor2) {
        this.priceColor = priceColor2;
    }

    public String getPriceColor() {
        return this.priceColor;
    }

    public void setVanishFields(List<String> vanishFields2) {
        this.vanishFields = vanishFields2;
    }

    public List<String> getVanishFields() {
        return this.vanishFields;
    }

    public void setThirdTabName(String thirdTabName2) {
        this.thirdTabName = thirdTabName2;
    }

    public String getThirdTabName() {
        return this.thirdTabName;
    }

    public void setHongbaoStyle(String hongbaoStyle2) {
        this.hongbaoStyle = hongbaoStyle2;
    }

    public String getHongbaoStyle() {
        return this.hongbaoStyle;
    }

    public void setHeaderStyle(String headerStyle2) {
        this.headerStyle = headerStyle2;
    }

    public String getHeaderStyle() {
        return this.headerStyle;
    }
}
