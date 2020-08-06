package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class GetJhsListRequest extends BaseMtopRequest {
    private static final String API = "mtop.tvtao.ju.flow.query";
    private static final int categoryId = 1890;
    private static final int superChoiceId = 1940;
    private int catId;
    private boolean isSuperChoice = false;
    private int pageNum;
    private int pageSize;

    public GetJhsListRequest(int catId2, int pageNum2, int pageSize2, boolean isSuperChoice2) {
        this.catId = catId2;
        this.pageNum = pageNum2;
        this.pageSize = pageSize2;
        this.isSuperChoice = isSuperChoice2;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        HashMap<String, String> variables = new HashMap<>();
        addParams("scenario", "common");
        if (this.isSuperChoice) {
            addParams("id", String.valueOf(superChoiceId));
        } else {
            addParams("id", String.valueOf(categoryId));
            variables.put("catId", String.valueOf(this.catId));
        }
        variables.put("pageNum", String.valueOf(this.pageNum));
        variables.put("pageSize", String.valueOf(this.pageSize));
        addParams("variables", JSON.toJSONString(variables));
        return null;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public <T> T resolveResponse(JSONObject obj) throws Exception {
        return null;
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }
}
