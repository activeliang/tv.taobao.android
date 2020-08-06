package com.taobao.wireless.trade.mcart.sdk.co.biz;

import java.io.Serializable;
import java.util.List;

public class ItemLogo implements Serializable {
    public ItemLogoField fields;
    public String type;

    public static class ItemLogoField implements Serializable {
        public String iconUrl;
        public List<ItemTag> tags;
        public String title;
        public String titleColor;
        public String type;
        public String value;
        public String valueColor;
    }

    public static class ItemTag implements Serializable {
        public String bgColor;
        public String borderColor;
        public String text;
    }
}
