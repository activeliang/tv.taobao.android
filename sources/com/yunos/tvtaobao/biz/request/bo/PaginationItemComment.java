package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class PaginationItemComment implements Serializable {
    private static final long serialVersionUID = 7663724653341032521L;
    private List<ItemComment> comments;
    private Integer currPage;
    private Integer pageSize;
    private Integer totalPage;
    private Integer totalSize;

    public Integer getTotalSize() {
        return this.totalSize;
    }

    public void setTotalSize(Integer totalSize2) {
        this.totalSize = totalSize2;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize2) {
        this.pageSize = pageSize2;
    }

    public List<ItemComment> getComments() {
        return this.comments;
    }

    public void setComments(List<ItemComment> comments2) {
        this.comments = comments2;
    }

    public static PaginationItemComment resolveFromTop(String response) throws Exception {
        JSONObject rateListInfoObj = new JSONObject(response.substring(response.indexOf("{"))).getJSONObject("rateListInfo");
        JSONObject paginatorObj = rateListInfoObj.getJSONObject("paginator");
        PaginationItemComment comment = new PaginationItemComment();
        comment.setPageSize(Integer.valueOf(paginatorObj.getInt("itemsPerPage")));
        comment.setTotalSize(Integer.valueOf(paginatorObj.getInt("length")));
        comment.setTotalPage(Integer.valueOf(paginatorObj.getInt("pages")));
        comment.setCurrPage(Integer.valueOf(paginatorObj.getInt("page")));
        JSONArray rateArray = rateListInfoObj.getJSONArray("rateList");
        if (rateArray != null) {
            comment.comments = new ArrayList();
            for (int i = 0; i < rateArray.length(); i++) {
                ItemComment itemComment = ItemComment.resolveFromTop(rateArray.getJSONObject(i));
                if (itemComment != null) {
                    comment.comments.add(itemComment);
                }
            }
        }
        return comment;
    }

    public Integer getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(Integer totalPage2) {
        this.totalPage = totalPage2;
    }

    public Integer getCurrPage() {
        return this.currPage;
    }

    public void setCurrPage(Integer currPage2) {
        this.currPage = currPage2;
    }
}
