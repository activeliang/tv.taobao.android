package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

import java.util.List;

public class PageContainerBean {
    private List<String> children;
    private DataBeanX data;
    private String tag;

    public DataBeanX getData() {
        return this.data;
    }

    public void setData(DataBeanX data2) {
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

    public static class DataBeanX {
        private String eagleEyeId;
        private String systemTime;

        public String getEagleEyeId() {
            return this.eagleEyeId;
        }

        public void setEagleEyeId(String eagleEyeId2) {
            this.eagleEyeId = eagleEyeId2;
        }

        public String getSystemTime() {
            return this.systemTime;
        }

        public void setSystemTime(String systemTime2) {
            this.systemTime = systemTime2;
        }
    }
}
