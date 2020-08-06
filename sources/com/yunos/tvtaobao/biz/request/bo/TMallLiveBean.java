package com.yunos.tvtaobao.biz.request.bo;

public class TMallLiveBean {
    private String end_time;
    private String id;
    private String img_url;
    private String live_id;
    private String name;
    private String start_time;
    private String stream_url;

    public String getImg_url() {
        return this.img_url;
    }

    public void setImg_url(String img_url2) {
        this.img_url = img_url2;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getEnd_time() {
        return this.end_time;
    }

    public void setEnd_time(String end_time2) {
        this.end_time = end_time2;
    }

    public String getStream_url() {
        return this.stream_url;
    }

    public void setStream_url(String stream_url2) {
        this.stream_url = stream_url2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getStart_time() {
        return this.start_time;
    }

    public void setStart_time(String start_time2) {
        this.start_time = start_time2;
    }

    public String getLive_id() {
        return this.live_id;
    }

    public void setLive_id(String live_id2) {
        this.live_id = live_id2;
    }
}
