package com.yunos.tvtaobao.biz.request.bo.juhuasuan.bo;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class CountList<T> extends ArrayList<T> {
    private static final long serialVersionUID = -6358779524239576369L;
    private Integer currentPage;
    private Integer itemCount;
    private Integer pageSize;
    private Integer totalPage;

    public CountList(List<T> list) {
        for (T t : list) {
            add(t);
        }
    }

    public CountList() {
    }

    public static <T> CountList<T> fromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        CountList<T> item = new CountList<>();
        item.setItemCount(Integer.valueOf(obj.optInt("totalItem")));
        if (item.getItemCount() == null || item.getItemCount().intValue() <= 0) {
            item.setItemCount(Integer.valueOf(obj.optInt("itemCount")));
        }
        item.setPageSize(Integer.valueOf(obj.optInt("pageSize")));
        item.setCurrentPage(Integer.valueOf(obj.optInt("currentPage")));
        item.setTotalPage(Integer.valueOf(obj.optInt("totalPage")));
        return item;
    }

    public Integer getItemCount() {
        return this.itemCount;
    }

    public void setItemCount(Integer itemCount2) {
        this.itemCount = itemCount2;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize2) {
        this.pageSize = pageSize2;
    }

    public Integer getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(Integer currentPage2) {
        this.currentPage = currentPage2;
    }

    public Integer getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(Integer totalPage2) {
        this.totalPage = totalPage2;
    }

    public String toString() {
        return "CountList [itemCount=" + this.itemCount + ", pageSize=" + this.pageSize + ", currentPage=" + this.currentPage + ", totalPage=" + this.totalPage + ", size=" + size() + "]";
    }
}
