package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.request.base.BaseHttpRequest;
import com.yunos.tvtaobao.biz.request.bo.GoodsSearchMO;
import java.util.HashMap;
import java.util.Map;

public class SearchRequest extends BaseHttpRequest {
    private static final long serialVersionUID = 4041797502287250945L;
    private String catmap = null;
    private int curPage = 1;
    private String kw = "";
    private String nid = null;
    private int pageSize = 10;
    private String sort = "_coefp";
    private String tab = null;

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        Map<String, String> params = new HashMap<>();
        params.put("vm", "nw");
        params.put("ttid", Config.getTTid());
        if (this.nid == null || this.nid.length() < 1) {
            if (!TextUtils.isEmpty(this.catmap)) {
                params.put("catmap", this.catmap);
            }
            if (!TextUtils.isEmpty(this.kw)) {
                params.put("q", this.kw);
            }
        } else {
            params.put(BaseConfig.NID_FROM_CART, this.nid);
        }
        if (!TextUtils.isEmpty(this.tab) && ("mall".equals(this.tab) || "discount".equals(this.tab))) {
            params.put("tab", this.tab);
        }
        params.put("page", String.valueOf(this.curPage));
        params.put("sort", this.sort);
        params.put("n", String.valueOf(this.pageSize));
        return params;
    }

    /* access modifiers changed from: protected */
    public String getHttpDomain() {
        return "http://api.s.m.taobao.com/search.json";
    }

    public GoodsSearchMO resolveResult(String response) throws Exception {
        return GoodsSearchMO.resolveFromMTOP(response);
    }

    public int getCurPage() {
        return this.curPage;
    }

    public void setCurPage(int curPage2) {
        this.curPage = curPage2;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize2) {
        this.pageSize = pageSize2;
    }

    public String getKw() {
        return this.kw;
    }

    public void setKw(String kw2) {
        this.kw = kw2;
    }

    public String getSort() {
        return this.sort;
    }

    public void setSort(String sort2) {
        this.sort = sort2;
    }

    public String getNid() {
        return this.nid;
    }

    public void setNid(String nid2) {
        this.nid = nid2;
    }

    public String getCatmap() {
        return this.catmap;
    }

    public void setCatmap(String catmap2) {
        this.catmap = catmap2;
    }

    public String getTab() {
        return this.tab;
    }

    public void setTab(String tab2) {
        this.tab = tab2;
    }
}
