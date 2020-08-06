package com.taobao.wireless.detail.api;

import com.alibaba.fastjson.JSON;
import com.taobao.detail.clientDomain.TBDetailResultVO;
import com.taobao.detail.domain.DetailVO;
import com.taobao.detail.domain.base.Unit;
import com.taobao.tao.remotebusiness.js.MtopJSBridge;
import com.taobao.wireless.detail.DetailConfig;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.xstate.util.XStateConstants;

public class DetailApiProxy {
    public static TBDetailResultVO synRequest(Map<String, String> query, DetailApiRequestor detailApiRequestor) {
        if (DetailConfig.ttid == null) {
            System.err.println("ttid is empty, use DetailConfig.ttid to set one");
            return null;
        }
        Unit main = new Unit();
        getMtopUnit(query, main);
        TBDetailResultVO mainVO = detailApiRequestor.syncRequest(main);
        if (mainVO == null) {
            return null;
        }
        parseApiStack(mainVO, query, detailApiRequestor);
        mockDyn(mainVO);
        return mainVO;
    }

    private static void getMtopUnit(Map<String, String> query, Unit main) {
        Map<String, String> safeMap;
        main.name = "mtop";
        Map<String, String> api = new HashMap<>();
        api.put("API_NAME", "mtop.taobao.detail.getDetail");
        api.put("VERSION", XStateConstants.VALUE_INNER_PV);
        api.put(MtopJSBridge.MtopJSParam.NEED_LOGIN, "false");
        api.put("needEcode", "false");
        api.put("wua", "false");
        if (query == null) {
            safeMap = new HashMap<>();
        } else {
            safeMap = new HashMap<>(query);
        }
        api.put("itemNumId", safeMap.get("id"));
        safeMap.remove("id");
        api.put(ApiUnitHelper.EX_QUERY_KEY, JSON.toJSONString(safeMap));
        main.value = JSON.toJSONString(api);
    }

    private static void getHttpUnit(Map<String, String> query, Unit main) {
        main.name = "http";
        if (query.containsKey(DetailConfig.TEST_ENV)) {
            main.value = DetailConfig.getDetailMainUrl(query.get(DetailConfig.TEST_ENV));
            query.remove(DetailConfig.TEST_ENV);
        } else {
            main.value = DetailConfig.getDetailMainUrl();
        }
        ApiUnitHelper.appendMainUnitQuery(main, query);
    }

    private static void parseApiStack(TBDetailResultVO mainVO, Map<String, String> query, DetailApiRequestor detailApiRequestor) {
        TBDetailResultVO subVO;
        ApiStackParser apiStackParser = new ApiStackParser(mainVO);
        for (Unit nextUnit = apiStackParser.nextApi(); nextUnit != null; nextUnit = apiStackParser.nextApi()) {
            if (ApiUnitHelper.PROTOCAL_ESI.equals(nextUnit.name)) {
                subVO = parseESI(nextUnit.value);
            } else {
                ApiUnitHelper.appendApiUnitQueryIfNeeded(nextUnit, query);
                subVO = detailApiRequestor.syncRequest(nextUnit);
            }
            if (subVO == null) {
                break;
            }
            DetailVOMerger.merge(mainVO, subVO);
        }
        mainVO.apiStack = null;
    }

    private static TBDetailResultVO parseESI(String json) {
        if (json == null || json.length() == 0) {
            return getEsiErrorVO();
        }
        DetailResponse detailResponse = (DetailResponse) JSON.parseObject(json, DetailResponse.class);
        if (detailResponse == null) {
            return getEsiErrorVO();
        }
        if (detailResponse.getData() != null) {
            return detailResponse.getData();
        }
        TBDetailResultVO tbDetailResultVO = new TBDetailResultVO();
        tbDetailResultVO.errorCode = detailResponse.getRetCode();
        tbDetailResultVO.errorMessage = detailResponse.getRetMsg();
        return tbDetailResultVO;
    }

    private static TBDetailResultVO getEsiErrorVO() {
        TBDetailResultVO tbDetailResultVO = new TBDetailResultVO();
        tbDetailResultVO.errorCode = "NO_ESI";
        tbDetailResultVO.errorMessage = "接口请求失败";
        return tbDetailResultVO;
    }

    private static boolean mockDyn(TBDetailResultVO mainVO) {
        if (mainVO.errorCode != null && mainVO.errorCode.length() > 0 && mainVO.extras != null && mainVO.extras.containsKey(DetailVO.MOCK_DYN)) {
            try {
                DetailVOMerger.merge(mainVO, (TBDetailResultVO) JSON.parseObject(mainVO.extras.get(DetailVO.MOCK_DYN).toString(), TBDetailResultVO.class));
                mainVO.extras.remove(DetailVO.MOCK_DYN);
                mainVO.errorCode = null;
                mainVO.errorMessage = null;
                return true;
            } catch (Throwable th) {
            }
        }
        return false;
    }
}
