package com.yunos.tvtaobao.biz.request.bo;

import android.text.TextUtils;
import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoodsSearchMO implements Serializable {
    private static final long serialVersionUID = 2821380640539172830L;
    private Goods[] goodsList;
    private String orderBy;
    private int page;
    private int pageSize;
    private String paramValue;
    private int totalPage;
    private int totalResults;

    public int getTotalResults() {
        return this.totalResults;
    }

    public void setTotalResults(int totalResults2) {
        this.totalResults = totalResults2;
    }

    public int getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(int totalPage2) {
        this.totalPage = totalPage2;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize2) {
        this.pageSize = pageSize2;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page2) {
        this.page = page2;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String orderBy2) {
        this.orderBy = orderBy2;
    }

    public String getParamValue() {
        return this.paramValue;
    }

    public void setParamValue(String paramValue2) {
        this.paramValue = paramValue2;
    }

    public Goods[] getGoodsList() {
        return this.goodsList;
    }

    public void setGoodsList(Goods[] goodsList2) {
        this.goodsList = goodsList2;
    }

    public static GoodsSearchMO resolveFromMTOP(String response) throws JSONException {
        JSONObject obj;
        GoodsSearchMO goodsSearchMO = null;
        if (!TextUtils.isEmpty(response)) {
            String response2 = response.replaceAll("\\\\", "");
            if (!(response2.length() == 0 || (obj = new JSONObject(response2)) == null)) {
                goodsSearchMO = new GoodsSearchMO();
                if (!obj.isNull("totalResults")) {
                    goodsSearchMO.setTotalResults(obj.getInt("totalResults"));
                }
                if (!obj.isNull("totalPage")) {
                    goodsSearchMO.setTotalPage(obj.getInt("totalPage"));
                }
                if (!obj.isNull("pageSize")) {
                    goodsSearchMO.setPageSize(obj.getInt("pageSize"));
                }
                if (!obj.isNull("page")) {
                    goodsSearchMO.setPage(obj.getInt("page"));
                }
                if (!obj.isNull("paramValue")) {
                    goodsSearchMO.setParamValue(obj.getString("paramValue"));
                }
                if (!obj.isNull("order_by")) {
                    goodsSearchMO.setOrderBy(obj.getString("order_by"));
                }
                if (!obj.isNull("itemsArray")) {
                    JSONArray array = obj.getJSONArray("itemsArray");
                    Goods[] goodsList2 = new Goods[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        goodsList2[i] = Goods.resolveFromMTOP(array.getJSONObject(i));
                    }
                    goodsSearchMO.setGoodsList(goodsList2);
                }
            }
        }
        return goodsSearchMO;
    }
}
