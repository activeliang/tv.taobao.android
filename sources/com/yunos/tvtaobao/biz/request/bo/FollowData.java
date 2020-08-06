package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class FollowData {
    private String followCount;
    private List<FollowItemInfo> list;

    public void setFollowCount(String followCount2) {
        this.followCount = followCount2;
    }

    public String getFollowCount() {
        return this.followCount;
    }

    public void setList(List<FollowItemInfo> list2) {
        this.list = list2;
    }

    public List<FollowItemInfo> getList() {
        return this.list;
    }
}
