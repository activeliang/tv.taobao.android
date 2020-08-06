package com.yunos.tvtaobao.biz.request.core;

import com.yunos.tvtaobao.biz.request.bo.Address;
import com.yunos.tvtaobao.biz.request.bo.CategoryMO;
import com.yunos.tvtaobao.biz.request.bo.ItemMO;
import com.yunos.tvtaobao.biz.request.bo.JuOrderMO;
import com.yunos.tvtaobao.biz.request.bo.YGAcrVideoItem;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonResolver {
    public static List<ItemMO> resolveItemMOList(JSONObject dataObj) throws JSONException {
        JSONArray array = getResultArray(dataObj);
        if (array == null || array.length() == 0) {
            return null;
        }
        List<ItemMO> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            ItemMO item = ItemMO.fromMTOP(array.optJSONObject(i));
            if (item != null) {
                list.add(item);
            } else {
                ZpLogger.v("", "null item:-----------------------------------" + i);
            }
        }
        return list;
    }

    public static List<Address> resolveAddressList(JSONObject dataObj) throws JSONException {
        JSONArray array = dataObj.getJSONArray("addressList");
        if (array == null || array.length() == 0) {
            return null;
        }
        List<Address> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Address a = Address.fromMTOP(array.getJSONObject(i));
            if (a != null) {
                if (a.getStatus() == 1 && a.getAddressType() == 1) {
                    a.setStatus(0);
                }
                list.add(a);
            }
        }
        return list;
    }

    public static List<YGAcrVideoItem> resolveYGAcrVideoItem(JSONObject obj) throws JSONException {
        List<YGAcrVideoItem> list = null;
        if (obj != null && obj.has("result")) {
            JSONArray result = obj.getJSONArray("result");
            if (result.length() > 0) {
                list = new ArrayList<>();
                int length = result.length();
                for (int i = 0; i < length; i++) {
                    YGAcrVideoItem yg = YGAcrVideoItem.fromMTOP(result.getJSONObject(i));
                    if (yg != null) {
                        list.add(yg);
                    }
                }
            }
        }
        return list;
    }

    public static List<String> resolveStringArray(JSONObject dataObj) throws JSONException {
        JSONArray array = getResultArray(dataObj);
        if (array == null || array.length() == 0) {
            return null;
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }
        return list;
    }

    public static List<String> resolveStringArray(JSONArray array) throws JSONException {
        if (array == null || array.length() == 0) {
            return null;
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }
        return list;
    }

    public static List<CategoryMO> resolveFrontCategory(JSONObject dataObj) throws JSONException {
        JSONArray array = getResultArray(dataObj);
        if (array == null || array.length() == 0) {
            return null;
        }
        List<CategoryMO> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(CategoryMO.fromMTOP(array.getJSONObject(i)));
        }
        return list;
    }

    public static JSONObject getDataElement(String json) {
        if (json == null) {
            return null;
        }
        try {
            return new JSONObject(json).getJSONObject("data");
        } catch (JSONException e) {
            return null;
        }
    }

    public static String getElement(String json, String key) {
        if (json == null) {
            return null;
        }
        try {
            return new JSONObject(json).get(key).toString();
        } catch (JSONException e) {
            return null;
        }
    }

    private static JSONArray getResultArray(JSONObject dataObj) {
        try {
            return dataObj.getJSONArray("result");
        } catch (JSONException e) {
            return null;
        }
    }

    public static List<JuOrderMO> resolveJuOrderList(JSONObject dataObj) throws JSONException {
        JSONArray array = getResultArray(dataObj);
        if (array == null || array.length() == 0) {
            return null;
        }
        List<JuOrderMO> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(JuOrderMO.fromMTOP(array.getJSONObject(i)));
        }
        return list;
    }

    public static Map<String, String> jsonobjToMap(JSONObject obj) {
        if (obj == null || obj.length() <= 0) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        JSONArray array = obj.names();
        for (int i = 0; i < array.length(); i++) {
            try {
                map.put(array.getString(i), obj.optString(array.getString(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
