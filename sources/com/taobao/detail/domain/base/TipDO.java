package com.taobao.detail.domain.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TipDO implements Serializable {
    public Map<String, String> css;
    public String html;
    public String logo;
    public String txt;
    public String url;

    public static TipDO createImg(String img) {
        TipDO tipDO = new TipDO();
        tipDO.logo = img;
        return tipDO;
    }

    public static TipDO createTxt(String txt2) {
        TipDO tipDO = new TipDO();
        tipDO.txt = txt2;
        return tipDO;
    }

    public void addCss(String key, String value) {
        if (this.css == null) {
            this.css = new HashMap();
        }
        this.css.put(key, value);
    }
}
