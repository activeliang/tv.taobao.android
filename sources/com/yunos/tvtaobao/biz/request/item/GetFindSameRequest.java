package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tv.alitvasrsdk.CommonData;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.FindSameBean;
import com.yunos.tvtaobao.biz.request.bo.FindSameContainerBean;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class GetFindSameRequest extends BaseMtopRequest {
    private static final String API = "com.taobao.wireless.chanel.realTimeRecommond";
    private static final String TAG = "findSameRequest";
    private String catid;
    private int currentPage = 1;
    private String nid;
    private int pageSize = 10;

    public GetFindSameRequest(Integer pageSize2, Integer currentPage2, String catid2, String nid2) {
        int i = 10;
        this.pageSize = pageSize2 != null ? pageSize2.intValue() : i;
        this.currentPage = currentPage2 == null ? 1 : currentPage2.intValue();
        this.catid = catid2;
        this.nid = nid2;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "2.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(BaseConfig.CATID_FROM_CART, this.catid);
            jsonObject.put(BaseConfig.NID_FROM_CART, this.nid);
            jsonObject.put("tabid", "default");
            jsonObject.put("appid", "2016");
            jsonObject.put("istmall", "");
            addParams(CommonData.PARAM, jsonObject.toString());
        } catch (JSONException e) {
            ZpLogger.d(TAG, e.toString());
        }
        addParams("albumId", "NEW_FIND_SIMILAR");
        addParams("pageSize", String.valueOf(this.pageSize));
        addParams("currentPage", String.valueOf(this.currentPage));
        addParams("h5version", String.valueOf(2));
        return null;
    }

    /* access modifiers changed from: protected */
    public FindSameContainerBean resolveResponse(JSONObject obj) throws Exception {
        JSONObject jsonResult;
        JSONObject recommedResult;
        if (obj == null) {
            ZpLogger.d(TAG, "--------->>findsameBean is null<<------------");
            return null;
        }
        JSONObject jsonObject = new JSONObject(obj.toString()).optJSONObject("model");
        if (jsonObject == null || (jsonResult = jsonObject.getJSONObject("result")) == null || (recommedResult = jsonResult.getJSONObject("recommedResult")) == null) {
            return null;
        }
        String recommedResultString = recommedResult.getString("result");
        String sourceItemStr = recommedResult.getString("sourceItem");
        FindSameContainerBean findSameContainerBean = new FindSameContainerBean();
        String pic = new JSONObject(sourceItemStr).getString("pic");
        findSameContainerBean.setFindSameBeanList(com.alibaba.fastjson.JSONObject.parseArray(recommedResultString, FindSameBean.class));
        findSameContainerBean.setPic(pic);
        return findSameContainerBean;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }
}
