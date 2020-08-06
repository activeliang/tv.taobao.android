package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class FindSameContainerBean {
    private List<FindSameBean> findSameBeanList;
    private String pic;

    public String getPic() {
        return this.pic;
    }

    public void setPic(String pic2) {
        this.pic = pic2;
    }

    public List<FindSameBean> getFindSameBeanList() {
        return this.findSameBeanList;
    }

    public void setFindSameBeanList(List<FindSameBean> findSameBeanList2) {
        this.findSameBeanList = findSameBeanList2;
    }
}
