package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.yunos.tvtaobao.biz.request.base.BaseHttpRequest;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetSearchResultRequest extends BaseHttpRequest {
    private static final long serialVersionUID = 4196001261446570498L;

    public GetSearchResultRequest(String q, String area, String code, int n) {
        if (n > 0) {
            addParams("n", String.valueOf(n));
        }
        addParams("q", q);
        addParams("area", area);
        addParams("code", code);
    }

    public ArrayList<String> resolveResult(String result) throws Exception {
        JSONArray array;
        ArrayList<String> returnList = new ArrayList<>();
        if (!TextUtils.isEmpty(result) && result.contains("result") && (array = new JSONObject(result).getJSONArray("result")) != null) {
            for (int i = 0; i < array.length(); i++) {
                JSONArray child = array.getJSONArray(i);
                if (child != null) {
                    for (int j = 0; j < child.length(); j++) {
                        if (!TextUtils.isEmpty(child.getString(j))) {
                            returnList.add(child.getString(j));
                        }
                    }
                }
            }
        }
        return returnList;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public String getHttpDomain() {
        return "https://suggest.taobao.com/sug";
    }
}
