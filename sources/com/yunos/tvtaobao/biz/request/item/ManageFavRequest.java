package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tv.core.common.User;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONObject;

public class ManageFavRequest extends BaseMtopRequest {
    private static final String API = "com.taobao.client.favorite.manage";
    private static final long serialVersionUID = 4510663894049267108L;
    private String func = "addAuction";
    private String itemNumId = null;
    private int page = 1;
    private int pageSize = 10;

    public ManageFavRequest(String itemNumId2, String func2) {
        this.itemNumId = itemNumId2;
        this.func = func2;
    }

    public ManageFavRequest(String func2, int page2, int pageSize2) {
        this.func = func2;
        this.page = page2;
        this.pageSize = pageSize2;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        addParams("sid", User.getSessionId());
        addParams("func", this.func);
        if (this.func.startsWith("add")) {
            addParams("itemNumId", this.itemNumId);
            return null;
        } else if (this.func.startsWith("del")) {
            addParams("infoId", this.itemNumId);
            return null;
        } else if (!this.func.equals("getAuctions")) {
            return null;
        } else {
            addParams("page", String.valueOf(this.page));
            addParams("pageSize", String.valueOf(this.pageSize));
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        return obj.toString();
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
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }
}
