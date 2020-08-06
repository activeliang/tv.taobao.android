package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.taobao.wireless.detail.api.ApiUnitHelper;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.TvOptionsConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.QueryBagRequestBo;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryBagRequest extends BaseMtopRequest {
    private static final String API = "mtop.trade.queryBag";
    private static final long serialVersionUID = 4725190889190641767L;
    private String TAG = QueryBagRequest.class.getSimpleName();

    public QueryBagRequest(QueryBagRequestBo queryBagRequestBo) {
        if (queryBagRequestBo != null) {
            if (!TextUtils.isEmpty(queryBagRequestBo.getP())) {
                addParams("p", queryBagRequestBo.getP());
                ZpLogger.e(this.TAG + "p = ", queryBagRequestBo.getP());
            }
            if (!TextUtils.isEmpty(queryBagRequestBo.getFeature())) {
                addParams("feature", queryBagRequestBo.getFeature());
                ZpLogger.e(this.TAG + "feature = ", queryBagRequestBo.getFeature());
            }
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("version", "1.1.1");
                jsonObject.put("mergeCombo", true);
                jsonObject.put("globalSell", "1");
                JSONObject tvtaoExtra = new JSONObject();
                tvtaoExtra.put("appKey", Config.getChannel());
                String tvOptions = TvOptionsConfig.getTvOptions();
                String tvOptionsResult = tvOptions.substring(0, tvOptions.length() - 1);
                ZpLogger.e("tvOptions = ", tvOptionsResult + "1");
                tvtaoExtra.put("tvOptions", tvOptionsResult + "1");
                jsonObject.put("tvtaoExtra", tvtaoExtra.toString());
                addParams(ApiUnitHelper.EX_QUERY_KEY, jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            addParams("isPage", String.valueOf(queryBagRequestBo.isPage()));
            addParams("extStatus", String.valueOf(queryBagRequestBo.getExtStatus()));
            if (!TextUtils.isEmpty(queryBagRequestBo.getCartFrom())) {
                addParams("cartFrom", queryBagRequestBo.getCartFrom());
                ZpLogger.e(this.TAG + "cartFrom = ", queryBagRequestBo.getCartFrom());
            }
            ZpLogger.e(this.TAG, "queryBagRequestBo = " + queryBagRequestBo.toString());
        }
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    public String getTTid() {
        return Config.getChannel() + "@taobao_android_7.10.0";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "5.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ZpLogger.e("resolveResponse", obj.toString());
        return obj.toString();
    }
}
