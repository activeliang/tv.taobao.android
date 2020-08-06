package com.taobao.wireless.detail.api;

import com.alibaba.fastjson.JSON;
import com.taobao.detail.domain.base.Unit;
import com.taobao.wireless.detail.DetailConfig;
import com.taobao.wireless.lang.CheckUtils;
import com.taobao.wireless.lang.UrlBuilder;
import java.util.HashMap;
import java.util.Map;

public class ApiUnitHelper {
    public static final String EX_QUERY_KEY = "exParams";
    public static final String PROTOCAL_ESI = "esi";
    public static final String PROTOCAL_HTTP = "http";
    public static final String PROTOCAL_MTOP = "mtop";
    public static final String TTID = "ttid";

    public static void appendMainUnitQuery(Unit unit, Map<String, String> query) {
        if (CheckUtils.isEmpty(DetailConfig.ttid)) {
            System.err.println("DetailConfig.ttid must be inited");
        } else if ("http".equals(unit.name)) {
            Map<String, String> safeMap = cloneMap(query);
            UrlBuilder urlBuilder = new UrlBuilder(unit.value);
            urlBuilder.appendQuery("id", safeMap.get("id"));
            safeMap.remove("id");
            urlBuilder.appendQuery(EX_QUERY_KEY, JSON.toJSONString(safeMap));
            urlBuilder.appendQuery("ttid", DetailConfig.ttid);
            unit.value = urlBuilder.toString();
        }
    }

    public static void appendApiUnitQueryIfNeeded(Unit unit, Map<String, String> query) {
        if (query != null && query.size() != 0) {
            if ("http".equals(unit.name)) {
                appendHttpUnitQuery(unit, query);
            }
            if ("mtop".equals(unit.name)) {
                appendMtopUnitQuery(unit, query);
            }
        }
    }

    private static void appendHttpUnitQuery(Unit unit, Map<String, String> query) {
        if ("http".equals(unit.name)) {
            Map<String, String> safeMap = cloneMap(query);
            safeMap.put("ttid", DetailConfig.ttid);
            unit.value = UrlBuilder.appendQueryIfNotExist(unit.value, safeMap);
        }
    }

    private static void appendMtopUnitQuery(Unit unit, Map<String, String> query) {
        HashMap<String, String> mtopParams;
        HashMap exMap;
        if ("mtop".equals(unit.name) && (mtopParams = (HashMap) JSON.parseObject(unit.value, HashMap.class)) != null) {
            String exParams = mtopParams.get(EX_QUERY_KEY);
            Map<String, String> safeMap = cloneMap(query);
            if (!(exParams == null || "".equals(exParams) || (exMap = (HashMap) JSON.parseObject(exParams, HashMap.class)) == null)) {
                safeMap.putAll(exMap);
            }
            mtopParams.put(EX_QUERY_KEY, JSON.toJSONString(safeMap));
            unit.value = JSON.toJSONString(mtopParams);
        }
    }

    private static Map<String, String> cloneMap(Map<String, String> query) {
        if (query == null) {
            return new HashMap();
        }
        return new HashMap(query);
    }
}
