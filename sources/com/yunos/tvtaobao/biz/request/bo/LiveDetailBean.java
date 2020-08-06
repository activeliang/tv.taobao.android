package com.yunos.tvtaobao.biz.request.bo;

import android.net.Uri;
import java.io.Serializable;

public class LiveDetailBean implements Serializable {
    private String accountId;
    private String accountNick;
    private String coverImg;
    private String coverImg169;
    private int fansNum;
    private String headImg;
    private String inputStreamUrl;
    private String landScape;
    private String liveChannelId;
    private String liveId;
    private String location;
    private String source;
    private String status;
    private String title;

    public int getFansNum() {
        return this.fansNum;
    }

    public void setFansNum(int fansNum2) {
        this.fansNum = fansNum2;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public void setAccountId(String accountId2) {
        this.accountId = accountId2;
    }

    public String getAccountNick() {
        return this.accountNick;
    }

    public void setAccountNick(String accountNick2) {
        this.accountNick = accountNick2;
    }

    public String getCoverImg() {
        return this.coverImg;
    }

    public String getCoverImg(int width, int height) {
        if (this.coverImg == null) {
            return null;
        }
        if (width <= 0 || height <= 0) {
            return this.coverImg;
        }
        try {
            if ("gw.alicdn.com".equals(Uri.parse(this.coverImg).getHost())) {
                return String.format("%s_%dx%d.jpg", new Object[]{this.coverImg, Integer.valueOf(width), Integer.valueOf(height)});
            }
        } catch (Exception e) {
        }
        return this.coverImg;
    }

    public void setCoverImg(String coverImg2) {
        this.coverImg = coverImg2;
    }

    public String getCoverImg169() {
        return this.coverImg169;
    }

    public void setCoverImg169(String coverImg1692) {
        this.coverImg169 = coverImg1692;
    }

    public String getHeadImg() {
        return this.headImg;
    }

    public String getHeadImg(int width, int height) {
        if (this.headImg == null) {
            return null;
        }
        if (width <= 0 || height <= 0) {
            return this.headImg;
        }
        try {
            if ("gw.alicdn.com".equals(Uri.parse(this.headImg).getHost())) {
                return String.format("%s_%dx%d.jpg", new Object[]{this.headImg, Integer.valueOf(width), Integer.valueOf(height)});
            }
        } catch (Exception e) {
        }
        return this.headImg;
    }

    public void setHeadImg(String headImg2) {
        this.headImg = headImg2;
    }

    public String getInputStreamUrl() {
        return this.inputStreamUrl;
    }

    public void setInputStreamUrl(String inputStreamUrl2) {
        this.inputStreamUrl = inputStreamUrl2;
    }

    public String getLiveChannelId() {
        return this.liveChannelId;
    }

    public void setLiveChannelId(String liveChannelId2) {
        this.liveChannelId = liveChannelId2;
    }

    public String getLiveId() {
        return this.liveId;
    }

    public void setLiveId(String liveId2) {
        this.liveId = liveId2;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location2) {
        this.location = location2;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source2) {
        this.source = source2;
    }

    public String getLandScape() {
        return this.landScape;
    }

    public void setLandScape(String landScape2) {
        this.landScape = landScape2;
    }
}
