package com.taobao.wireless.trade.mbuy.sdk.co.misc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemIcons {
    private JSONObject data;
    private List<IconExt> iconExtList;
    private List<IconExt> iconMainList;

    public ItemIcons(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public List<IconExt> getIconExtList() {
        if (this.iconExtList != null) {
            return this.iconExtList;
        }
        this.iconExtList = getIconListByKey("ext");
        return this.iconExtList;
    }

    public List<IconExt> getIconMainList() {
        if (this.iconMainList != null) {
            return this.iconMainList;
        }
        this.iconMainList = getIconListByKey("main");
        return this.iconMainList;
    }

    private List<IconExt> getIconListByKey(String key) {
        JSONArray array = this.data.getJSONArray(key);
        if (array == null || array.isEmpty()) {
            return null;
        }
        List<IconExt> iconList = new ArrayList<>(array.size());
        Iterator<Object> it = array.iterator();
        while (it.hasNext()) {
            try {
                iconList.add(new IconExt((JSONObject) it.next()));
            } catch (Throwable th) {
            }
        }
        return iconList;
    }

    public static class IconExt {
        private JSONObject data;

        public IconExt(JSONObject data2) {
            if (data2 == null) {
                throw new IllegalStateException();
            }
            this.data = data2;
        }

        public String getId() {
            return this.data.getString("id");
        }

        public String getText() {
            return this.data.getString(TuwenConstants.MODEL_LIST_KEY.TEXT);
        }

        public String getBgColor() {
            return this.data.getString("bgColor");
        }

        public String getBorderColor() {
            return this.data.getString("borderColor");
        }
    }
}
