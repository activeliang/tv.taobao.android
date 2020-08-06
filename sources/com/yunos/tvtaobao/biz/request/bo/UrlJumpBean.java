package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.List;

public class UrlJumpBean implements Serializable {
    private List<DataBean> data;
    private MetaBean meta;

    public List<DataBean> getData() {
        return this.data;
    }

    public void setData(List<DataBean> data2) {
        this.data = data2;
    }

    public MetaBean getMeta() {
        return this.meta;
    }

    public void setMeta(MetaBean meta2) {
        this.meta = meta2;
    }

    public static class DataBean implements Comparable<DataBean>, Serializable {
        private int since_v;
        private String type;
        private String uri;
        private int weight;
        private List<String> words;

        public String getType() {
            return this.type;
        }

        public void setType(String type2) {
            this.type = type2;
        }

        public String getUri() {
            return this.uri;
        }

        public void setUri(String uri2) {
            this.uri = uri2;
        }

        public int getWeight() {
            return this.weight;
        }

        public void setWeight(int weight2) {
            this.weight = weight2;
        }

        public int getSince_v() {
            return this.since_v;
        }

        public void setSince_v(int since_v2) {
            this.since_v = since_v2;
        }

        public List<String> getWords() {
            return this.words;
        }

        public void setWords(List<String> words2) {
            this.words = words2;
        }

        public int compareTo(DataBean another) {
            return getWeight() - another.getWeight();
        }
    }

    public static class MetaBean {
        private List<String> notification;
        private String notify_title;

        public String getNotify_title() {
            return this.notify_title;
        }

        public void setNotify_title(String notify_title2) {
            this.notify_title = notify_title2;
        }

        public List<String> getNotification() {
            return this.notification;
        }

        public void setNotification(List<String> notification2) {
            this.notification = notification2;
        }
    }
}
