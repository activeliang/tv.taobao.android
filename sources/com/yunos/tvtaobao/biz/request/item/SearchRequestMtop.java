package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.GoodsSearchResult;
import java.util.Map;
import org.json.JSONObject;

public class SearchRequestMtop extends BaseMtopRequest {
    private static final long serialVersionUID = 505653598028887525L;
    private String API = "mtop.tmall.search.searchProduct";
    private String auction_tag = null;
    private String cat = null;
    private String category = null;
    private String end_price = null;
    private String from = "tvtaobao";
    private String item_ids = null;
    private int page_no = 1;
    private int page_size = 40;
    private String prop = null;
    private String q = "";
    private String sort = "s";
    private String start_price = null;
    private String user_id = null;
    private String version = "1.0";

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        if (this.page_no <= 0) {
            this.page_no = 1;
        }
        if (this.page_no > 100) {
            this.page_no = 100;
        }
        if (this.page_size <= 0) {
            this.page_size = 40;
        }
        if (this.page_size > 100) {
            this.page_size = 100;
        }
        if (TextUtils.isEmpty(this.q)) {
            this.q = "";
        }
        if (TextUtils.isEmpty(this.sort)) {
            this.sort = "s";
        }
        if (TextUtils.isEmpty(this.cat)) {
            this.cat = "";
        }
        if (TextUtils.isEmpty(this.category)) {
            this.category = "";
        }
        if (TextUtils.isEmpty(this.prop)) {
            this.prop = "";
        }
        if (TextUtils.isEmpty(this.start_price)) {
            this.start_price = "";
        }
        if (TextUtils.isEmpty(this.end_price)) {
            this.end_price = "";
        }
        if (TextUtils.isEmpty(this.auction_tag)) {
            this.auction_tag = "";
        }
        if (TextUtils.isEmpty(this.item_ids)) {
            this.item_ids = "";
        }
        if (TextUtils.isEmpty(this.user_id)) {
            this.user_id = "";
        }
        if (TextUtils.isEmpty(this.from)) {
            this.from = "tvtaobao";
        }
        if (!TextUtils.isEmpty(this.item_ids)) {
            addParams("item_ids", this.item_ids);
        } else {
            if (!TextUtils.isEmpty(this.q)) {
                addParams("q", this.q);
            }
            if (!TextUtils.isEmpty(this.cat)) {
                addParams("cat", this.cat);
            }
            if (!TextUtils.isEmpty(this.category)) {
                addParams("category", this.category);
            }
        }
        addParams("page_no", String.valueOf(this.page_no));
        addParams("page_size", String.valueOf(this.page_size));
        addParams("sort", this.sort);
        if (!TextUtils.isEmpty(this.prop)) {
            addParams("prop", this.prop);
        }
        if (!TextUtils.isEmpty(this.start_price)) {
            addParams("start_price", this.start_price);
        }
        if (!TextUtils.isEmpty(this.end_price)) {
            addParams("end_price", this.end_price);
        }
        if (!TextUtils.isEmpty(this.auction_tag)) {
            addParams("auction_tag", this.auction_tag);
        }
        if (!TextUtils.isEmpty(this.user_id)) {
            addParams("user_id", this.user_id);
        }
        addParams("from", this.from);
        return null;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return this.API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return this.version;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return true;
    }

    public GoodsSearchResult resolveResponse(JSONObject obj) throws Exception {
        return GoodsSearchResult.resolveFromJson(obj);
    }

    public void setPageNo(int page_no2) {
        this.page_no = page_no2;
    }

    public void setPageSize(int page_size2) {
        this.page_size = page_size2;
    }

    public void setQueryKW(String q2) {
        this.q = q2;
    }

    public void setSort(String sort2) {
        this.sort = sort2;
    }

    public void setCat(String cat2) {
        this.cat = cat2;
    }

    public void setCategory(String category2) {
        this.category = category2;
    }

    public void setProp(String prop2) {
        this.prop = prop2;
    }

    public void setStartPrice(String start_price2) {
        this.start_price = start_price2;
    }

    public void setEndPrice(String end_price2) {
        this.end_price = end_price2;
    }

    public void setItemIds(String item_ids2) {
        this.item_ids = item_ids2;
    }

    public void setUserId(String user_id2) {
        this.user_id = user_id2;
    }

    public void setFrom(String from2) {
        this.from = from2;
    }

    public void setAuctionTag(String auction_tag2) {
        this.auction_tag = auction_tag2;
    }
}
