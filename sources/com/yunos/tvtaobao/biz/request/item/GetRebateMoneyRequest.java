package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.TvOptionsConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.RebateBo;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetRebateMoneyRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.tvtao.tag.batchQuery";
    private static final String TAG = "GetRebateMoneyRequest";

    public GetRebateMoneyRequest(String itemIdArray, List<String> list, boolean isFromCartToBuildOrder, boolean isFromBuildOrder, boolean mjf, String extParams) {
        String tvOptions = TvOptionsConfig.getTvOptions();
        if (!TextUtils.isEmpty(tvOptions)) {
            if (isFromCartToBuildOrder) {
                String tvOptionsSubstring = tvOptions.substring(0, tvOptions.length() - 1);
                addParams("tvOptions", tvOptionsSubstring + "1");
                ZpLogger.v(TAG, "tvOptions = " + tvOptionsSubstring + "1");
            } else {
                addParams("tvOptions", tvOptions);
                ZpLogger.v(TAG, "tvOptions = " + tvOptions);
            }
        }
        if (!TextUtils.isEmpty(itemIdArray)) {
            ZpLogger.v(TAG, "params = " + itemIdArray);
            addParams("params", itemIdArray);
        }
        String appKey = Config.getChannel();
        if (!TextUtils.isEmpty(appKey)) {
            ZpLogger.e(TAG, "appKey = " + appKey);
            addParams("appKey", appKey);
        }
        if (isFromBuildOrder) {
            addParams("from", "build_order");
        }
        addParams("mjf", String.valueOf(mjf));
        JSONArray array = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            array.put(list.get(i));
        }
        ZpLogger.v(TAG, "traceRoutes =" + array);
        addParams("traceRoutes", array.toString());
        addParams("extParams", extParams);
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public List<RebateBo> resolveResponse(JSONObject obj) throws Exception {
        if (obj.isNull("result")) {
            return null;
        }
        ZpLogger.v(TAG, obj.toString());
        return JSON.parseArray(obj.getString("result"), RebateBo.class);
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }
}
