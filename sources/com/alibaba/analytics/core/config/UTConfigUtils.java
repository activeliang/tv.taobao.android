package com.alibaba.analytics.core.config;

import com.alibaba.analytics.utils.StringUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class UTConfigUtils {
    private static final String ORANGE_CONF_PREFIX = "B02N";

    public static final Map<String, String> convertJsonConfToOrange(JSONObject aConfContent) {
        Map<String, String> lMap = new HashMap<>();
        try {
            Iterator<String> lKeys = aConfContent.keys();
            while (lKeys.hasNext()) {
                String lKey = lKeys.next();
                String lValue = aConfContent.getString(lKey);
                if (!(lKey == null || lValue == null)) {
                    lMap.put(lKey, lValue);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lMap;
    }

    public static final List<UTDBConfigEntity> convertOnlineJsonConfToUTDBConfigEntities(JSONObject aJsonObject) {
        JSONObject lConfJson = aJsonObject;
        if (lConfJson == null) {
            return null;
        }
        List<UTDBConfigEntity> lOnlineConfigs = new LinkedList<>();
        Iterator<String> lKeys = lConfJson.keys();
        while (lKeys.hasNext()) {
            String lKey = lKeys.next();
            if (lKey.startsWith(ORANGE_CONF_PREFIX)) {
                JSONObject lDataObj = null;
                try {
                    lDataObj = lConfJson.getJSONObject(lKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (lKey.length() > ORANGE_CONF_PREFIX.length() + 1) {
                    lKey = lKey.substring(ORANGE_CONF_PREFIX.length() + 1);
                }
                if (lDataObj != null) {
                    String lDataStr = lDataObj.optString("content");
                    if (lDataStr == null || !lDataStr.equals("gc_304")) {
                        JSONObject lContentObj = null;
                        try {
                            lContentObj = lDataObj.getJSONObject("content");
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
                        long lTimestamp = 0;
                        if (lDataObj.has("t")) {
                            try {
                                lTimestamp = lDataObj.getLong("t");
                            } catch (JSONException e3) {
                                e3.printStackTrace();
                            }
                        }
                        if (lContentObj != null) {
                            lOnlineConfigs.add(convertKVToDBConfigEntity(lKey, convertJsonConfToOrange(lContentObj), lTimestamp));
                        }
                    } else {
                        UTDBConfigEntity lEntity = new UTDBConfigEntity();
                        lEntity.setGroupname(lKey);
                        lEntity.set304Flag();
                        lOnlineConfigs.add(lEntity);
                    }
                }
            }
        }
        return lOnlineConfigs;
    }

    public static final UTDBConfigEntity convertKVToDBConfigEntity(String aGroupname, Map<String, String> aConfKVContent, long aTimestamp) {
        UTDBConfigEntity lOnlineEitity = new UTDBConfigEntity();
        lOnlineEitity.setConfContent(StringUtils.transMapToString(aConfKVContent));
        lOnlineEitity.setGroupname(aGroupname);
        lOnlineEitity.setConfTimestamp(aTimestamp);
        return lOnlineEitity;
    }
}
