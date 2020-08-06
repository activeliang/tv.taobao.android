package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.TBaoLiveListBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetLiveListRequest extends BaseMtopRequest {
    private String API = "mtop.taobao.tvtao.liveservice.getlivelist";
    private List<TBaoLiveListBean> list;
    private String version = "1.0";

    public GetLiveListRequest() {
        addParams("appkey", Config.getAppKey());
        this.list = new ArrayList();
    }

    /* access modifiers changed from: protected */
    public List<TBaoLiveListBean> resolveResponse(JSONObject obj) throws Exception {
        if (TextUtils.isEmpty(obj.toString())) {
            return null;
        }
        JSONArray dataList = obj.getJSONArray("result");
        int lenght = dataList.length();
        for (int i = 0; i < lenght; i++) {
            TBaoLiveListBean tblistBean = (TBaoLiveListBean) JSON.parseObject(dataList.getJSONObject(i).toString(), TBaoLiveListBean.class);
            if (tblistBean != null) {
                this.list.add(tblistBean);
            }
        }
        return this.list;
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return this.API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return this.version;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
