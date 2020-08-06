package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

import java.util.List;

public class ItemExtraBean {
    private List<String> children;
    private DataBeanXXXXXXX data;
    private String tag;

    public static class DataBeanXXXXXXX {
    }

    public DataBeanXXXXXXX getData() {
        return this.data;
    }

    public void setData(DataBeanXXXXXXX data2) {
        this.data = data2;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag2) {
        this.tag = tag2;
    }

    public List<String> getChildren() {
        return this.children;
    }

    public void setChildren(List<String> children2) {
        this.children = children2;
    }
}
