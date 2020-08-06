package com.yunos.tvtaobao.biz.request.item;

import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.SearchRelationRecommendBean;
import com.yunos.tvtaobao.biz.request.bo.SearchRelationRecommendItemBean;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class GetSearhRelationRecommendRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.tvtao.item.center.sug";
    private static final String TAG = "GetSearhRelationRecommendRequest";
    private static final String VERSION = "1.0";

    public GetSearhRelationRecommendRequest(String key, String utdid) {
        addParams("q", key);
        addParams("area", "wireless_gbdt_newoutput");
        addParams("tab", "");
        addParams("sversion", "5.8");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("business", "apk_suggest");
            jsonObject.put("utdid", utdid);
            ZpLogger.i(TAG, Config.getPackInfoAndAppkey(CoreApplication.getApplication()));
            jsonObject.put("appkey", Config.getPackInfoAndAppkey(CoreApplication.getApplication()));
            addParams("extParams", jsonObject.toString());
        } catch (JSONException e) {
            ZpLogger.d(TAG, e.toString());
        } catch (PackageManager.NameNotFoundException e2) {
            e2.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public ArrayList<String> resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            ZpLogger.d(TAG, "--------->>obj is null<<------------");
            return null;
        }
        SearchRelationRecommendBean searchRelationRecommendBean = (SearchRelationRecommendBean) JSON.parseObject(obj.toString(), SearchRelationRecommendBean.class);
        ArrayList<String> returnList = new ArrayList<>();
        if (searchRelationRecommendBean == null || searchRelationRecommendBean.getResult() == null || searchRelationRecommendBean.getResult().size() <= 0) {
            return returnList;
        }
        List<SearchRelationRecommendBean.Result> results = searchRelationRecommendBean.getResult();
        for (int i = 0; i < results.size(); i++) {
            if (!(results.get(i) == null || results.get(i).getData() == null)) {
                SearchRelationRecommendItemBean data = results.get(i).getData();
                if (data.getResult() != null && data.getResult().size() > 0) {
                    List<SearchRelationRecommendItemBean.DataResult> dataResultList = data.getResult();
                    for (int j = 0; j < dataResultList.size(); j++) {
                        if (dataResultList.get(j) != null) {
                            SearchRelationRecommendItemBean.DataResult dataResult = dataResultList.get(j);
                            if (!TextUtils.isEmpty(dataResult.getShowtext())) {
                                returnList.add(dataResult.getShowtext());
                            }
                        }
                    }
                }
            }
        }
        return returnList;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }
}
