package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemServices {
    private JSONObject data;

    public ItemServices(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public List<ItemServicesValue> getMain() {
        List<ItemServicesValue> itemMainServices = new ArrayList<>();
        JSONArray mainArray = this.data.getJSONArray("main");
        if (mainArray != null && !mainArray.isEmpty()) {
            Iterator<Object> it = mainArray.iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                if (obj != null) {
                    try {
                        itemMainServices.add(new ItemServicesValue((JSONObject) obj));
                    } catch (Throwable th) {
                    }
                }
            }
        }
        return itemMainServices;
    }

    public List<ItemServicesValue> getMainExt() {
        List<ItemServicesValue> itemMainExtServices = new ArrayList<>();
        JSONArray mainExtsArray = this.data.getJSONArray("mainExt");
        if (mainExtsArray != null && !mainExtsArray.isEmpty()) {
            Iterator<Object> it = mainExtsArray.iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                if (obj != null) {
                    try {
                        itemMainExtServices.add(new ItemServicesValue((JSONObject) obj));
                    } catch (Throwable th) {
                    }
                }
            }
        }
        return itemMainExtServices;
    }

    public String toString() {
        return "[main=" + getMain() + ", mainExt=" + getMainExt() + "]";
    }
}
