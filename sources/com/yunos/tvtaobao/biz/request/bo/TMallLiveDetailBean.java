package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class TMallLiveDetailBean {
    private List<AnchormenBean> anchormen;
    private List<CameraListBean> cameraList;
    private ChannelBean channel;
    private String followSellerId;
    private List<InteractBean> interact;
    private String isTaobaoOnly;
    private List<ItemsBean> items;
    private ShowSettingBean showSetting;

    public ShowSettingBean getShowSetting() {
        return this.showSetting;
    }

    public void setShowSetting(ShowSettingBean showSetting2) {
        this.showSetting = showSetting2;
    }

    public String getFollowSellerId() {
        return this.followSellerId;
    }

    public void setFollowSellerId(String followSellerId2) {
        this.followSellerId = followSellerId2;
    }

    public ChannelBean getChannel() {
        return this.channel;
    }

    public void setChannel(ChannelBean channel2) {
        this.channel = channel2;
    }

    public String getIsTaobaoOnly() {
        return this.isTaobaoOnly;
    }

    public void setIsTaobaoOnly(String isTaobaoOnly2) {
        this.isTaobaoOnly = isTaobaoOnly2;
    }

    public List<AnchormenBean> getAnchormen() {
        return this.anchormen;
    }

    public void setAnchormen(List<AnchormenBean> anchormen2) {
        this.anchormen = anchormen2;
    }

    public List<ItemsBean> getItems() {
        return this.items;
    }

    public void setItems(List<ItemsBean> items2) {
        this.items = items2;
    }

    public List<CameraListBean> getCameraList() {
        return this.cameraList;
    }

    public void setCameraList(List<CameraListBean> cameraList2) {
        this.cameraList = cameraList2;
    }

    public List<InteractBean> getInteract() {
        return this.interact;
    }

    public void setInteract(List<InteractBean> interact2) {
        this.interact = interact2;
    }

    public static class ShowSettingBean {
        private String anchorTable;
        private String discussTable;
        private String marketTable;

        public String getMarketTable() {
            return this.marketTable;
        }

        public void setMarketTable(String marketTable2) {
            this.marketTable = marketTable2;
        }

        public String getAnchorTable() {
            return this.anchorTable;
        }

        public void setAnchorTable(String anchorTable2) {
            this.anchorTable = anchorTable2;
        }

        public String getDiscussTable() {
            return this.discussTable;
        }

        public void setDiscussTable(String discussTable2) {
            this.discussTable = discussTable2;
        }
    }

    public static class ChannelBean {
        private String address;
        private String anchormenId;
        private String bannerUrl;
        private String cid;
        private String cname;
        private String coverUrl;
        private String enableChatRoom;
        private String enableLiveText;
        private String enableLiveVideo;
        private String etime;
        private String followerId;
        private String followerType;
        private String hlsUrl;
        private String isShare;
        private String itemsId;
        private String ownerId;
        private String ownerNick;
        private String partyUrl;
        private String playbackUrl;
        private String publicNotice;
        private String pushUrl;
        private String rightText;
        private String roomId;
        private String rtmpUrl;
        private String shareDoc;
        private String sharePicUrl;
        private String shareUrl;
        private String state;
        private String stime;
        private String subjectId;
        private String tidbitsUrl;

        public String getCid() {
            return this.cid;
        }

        public void setCid(String cid2) {
            this.cid = cid2;
        }

        public String getCname() {
            return this.cname;
        }

        public void setCname(String cname2) {
            this.cname = cname2;
        }

        public String getState() {
            return this.state;
        }

        public void setState(String state2) {
            this.state = state2;
        }

        public String getHlsUrl() {
            return this.hlsUrl;
        }

        public void setHlsUrl(String hlsUrl2) {
            this.hlsUrl = hlsUrl2;
        }

        public String getRtmpUrl() {
            return this.rtmpUrl;
        }

        public void setRtmpUrl(String rtmpUrl2) {
            this.rtmpUrl = rtmpUrl2;
        }

        public String getPlaybackUrl() {
            return this.playbackUrl;
        }

        public void setPlaybackUrl(String playbackUrl2) {
            this.playbackUrl = playbackUrl2;
        }

        public String getEnableLiveText() {
            return this.enableLiveText;
        }

        public void setEnableLiveText(String enableLiveText2) {
            this.enableLiveText = enableLiveText2;
        }

        public String getEnableLiveVideo() {
            return this.enableLiveVideo;
        }

        public void setEnableLiveVideo(String enableLiveVideo2) {
            this.enableLiveVideo = enableLiveVideo2;
        }

        public String getEnableChatRoom() {
            return this.enableChatRoom;
        }

        public void setEnableChatRoom(String enableChatRoom2) {
            this.enableChatRoom = enableChatRoom2;
        }

        public String getStime() {
            return this.stime;
        }

        public void setStime(String stime2) {
            this.stime = stime2;
        }

        public String getEtime() {
            return this.etime;
        }

        public void setEtime(String etime2) {
            this.etime = etime2;
        }

        public String getOwnerId() {
            return this.ownerId;
        }

        public void setOwnerId(String ownerId2) {
            this.ownerId = ownerId2;
        }

        public String getOwnerNick() {
            return this.ownerNick;
        }

        public void setOwnerNick(String ownerNick2) {
            this.ownerNick = ownerNick2;
        }

        public String getAnchormenId() {
            return this.anchormenId;
        }

        public void setAnchormenId(String anchormenId2) {
            this.anchormenId = anchormenId2;
        }

        public String getSubjectId() {
            return this.subjectId;
        }

        public void setSubjectId(String subjectId2) {
            this.subjectId = subjectId2;
        }

        public String getItemsId() {
            return this.itemsId;
        }

        public void setItemsId(String itemsId2) {
            this.itemsId = itemsId2;
        }

        public String getRightText() {
            return this.rightText;
        }

        public void setRightText(String rightText2) {
            this.rightText = rightText2;
        }

        public String getFollowerId() {
            return this.followerId;
        }

        public void setFollowerId(String followerId2) {
            this.followerId = followerId2;
        }

        public String getFollowerType() {
            return this.followerType;
        }

        public void setFollowerType(String followerType2) {
            this.followerType = followerType2;
        }

        public String getBannerUrl() {
            return this.bannerUrl;
        }

        public void setBannerUrl(String bannerUrl2) {
            this.bannerUrl = bannerUrl2;
        }

        public String getCoverUrl() {
            return this.coverUrl;
        }

        public void setCoverUrl(String coverUrl2) {
            this.coverUrl = coverUrl2;
        }

        public String getPushUrl() {
            return this.pushUrl;
        }

        public void setPushUrl(String pushUrl2) {
            this.pushUrl = pushUrl2;
        }

        public String getShareUrl() {
            return this.shareUrl;
        }

        public void setShareUrl(String shareUrl2) {
            this.shareUrl = shareUrl2;
        }

        public String getSharePicUrl() {
            return this.sharePicUrl;
        }

        public void setSharePicUrl(String sharePicUrl2) {
            this.sharePicUrl = sharePicUrl2;
        }

        public String getShareDoc() {
            return this.shareDoc;
        }

        public void setShareDoc(String shareDoc2) {
            this.shareDoc = shareDoc2;
        }

        public String getAddress() {
            return this.address;
        }

        public void setAddress(String address2) {
            this.address = address2;
        }

        public String getTidbitsUrl() {
            return this.tidbitsUrl;
        }

        public void setTidbitsUrl(String tidbitsUrl2) {
            this.tidbitsUrl = tidbitsUrl2;
        }

        public String getPartyUrl() {
            return this.partyUrl;
        }

        public void setPartyUrl(String partyUrl2) {
            this.partyUrl = partyUrl2;
        }

        public String getIsShare() {
            return this.isShare;
        }

        public void setIsShare(String isShare2) {
            this.isShare = isShare2;
        }

        public String getPublicNotice() {
            return this.publicNotice;
        }

        public void setPublicNotice(String publicNotice2) {
            this.publicNotice = publicNotice2;
        }

        public String getRoomId() {
            return this.roomId;
        }

        public void setRoomId(String roomId2) {
            this.roomId = roomId2;
        }
    }

    public static class AnchormenBean {
        private String avatar;
        private String chatroomId;
        private String cid;
        private String displayName;
        private String gmtCreate;
        private String gmtModified;
        private String id;
        private String nickname;
        private String subjectId;
        private String type;
        private String uin;

        public String getId() {
            return this.id;
        }

        public void setId(String id2) {
            this.id = id2;
        }

        public String getGmtCreate() {
            return this.gmtCreate;
        }

        public void setGmtCreate(String gmtCreate2) {
            this.gmtCreate = gmtCreate2;
        }

        public String getGmtModified() {
            return this.gmtModified;
        }

        public void setGmtModified(String gmtModified2) {
            this.gmtModified = gmtModified2;
        }

        public String getNickname() {
            return this.nickname;
        }

        public void setNickname(String nickname2) {
            this.nickname = nickname2;
        }

        public String getAvatar() {
            return this.avatar;
        }

        public void setAvatar(String avatar2) {
            this.avatar = avatar2;
        }

        public String getUin() {
            return this.uin;
        }

        public void setUin(String uin2) {
            this.uin = uin2;
        }

        public String getCid() {
            return this.cid;
        }

        public void setCid(String cid2) {
            this.cid = cid2;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type2) {
            this.type = type2;
        }

        public String getSubjectId() {
            return this.subjectId;
        }

        public void setSubjectId(String subjectId2) {
            this.subjectId = subjectId2;
        }

        public String getChatroomId() {
            return this.chatroomId;
        }

        public void setChatroomId(String chatroomId2) {
            this.chatroomId = chatroomId2;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public void setDisplayName(String displayName2) {
            this.displayName = displayName2;
        }
    }

    public static class ItemsBean {
        private String favorate;
        private String id;
        private String name;
        private String picUrl;
        private String price;
        private String taokeurl;
        private String url;

        public String getId() {
            return this.id;
        }

        public void setId(String id2) {
            this.id = id2;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name2) {
            this.name = name2;
        }

        public String getPicUrl() {
            return this.picUrl;
        }

        public void setPicUrl(String picUrl2) {
            this.picUrl = picUrl2;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url2) {
            this.url = url2;
        }

        public String getTaokeurl() {
            return this.taokeurl;
        }

        public void setTaokeurl(String taokeurl2) {
            this.taokeurl = taokeurl2;
        }

        public String getPrice() {
            return this.price;
        }

        public void setPrice(String price2) {
            this.price = price2;
        }

        public String getFavorate() {
            return this.favorate;
        }

        public void setFavorate(String favorate2) {
            this.favorate = favorate2;
        }
    }

    public static class CameraListBean {
        private String bannerUrl;
        private String cameraIndex;
        private List<CameraResolutionListBean> cameraResolutionList;
        private String cid;
        private String etime;
        private String name;
        private String orientation;
        private String pushUrl;
        private String state;
        private String stime;

        public String getName() {
            return this.name;
        }

        public void setName(String name2) {
            this.name = name2;
        }

        public String getState() {
            return this.state;
        }

        public void setState(String state2) {
            this.state = state2;
        }

        public String getCid() {
            return this.cid;
        }

        public void setCid(String cid2) {
            this.cid = cid2;
        }

        public String getStime() {
            return this.stime;
        }

        public void setStime(String stime2) {
            this.stime = stime2;
        }

        public String getEtime() {
            return this.etime;
        }

        public void setEtime(String etime2) {
            this.etime = etime2;
        }

        public String getCameraIndex() {
            return this.cameraIndex;
        }

        public void setCameraIndex(String cameraIndex2) {
            this.cameraIndex = cameraIndex2;
        }

        public String getPushUrl() {
            return this.pushUrl;
        }

        public void setPushUrl(String pushUrl2) {
            this.pushUrl = pushUrl2;
        }

        public String getBannerUrl() {
            return this.bannerUrl;
        }

        public void setBannerUrl(String bannerUrl2) {
            this.bannerUrl = bannerUrl2;
        }

        public String getOrientation() {
            return this.orientation;
        }

        public void setOrientation(String orientation2) {
            this.orientation = orientation2;
        }

        public List<CameraResolutionListBean> getCameraResolutionList() {
            return this.cameraResolutionList;
        }

        public void setCameraResolutionList(List<CameraResolutionListBean> cameraResolutionList2) {
            this.cameraResolutionList = cameraResolutionList2;
        }

        public static class CameraResolutionListBean {
            private String flvUrl;
            private String hlsUrl;
            private String name;
            private String playbackUrl;
            private String resolution;
            private String rtmpUrl;

            public String getResolution() {
                return this.resolution;
            }

            public void setResolution(String resolution2) {
                this.resolution = resolution2;
            }

            public String getName() {
                return this.name;
            }

            public void setName(String name2) {
                this.name = name2;
            }

            public String getHlsUrl() {
                return this.hlsUrl;
            }

            public void setHlsUrl(String hlsUrl2) {
                this.hlsUrl = hlsUrl2;
            }

            public String getFlvUrl() {
                return this.flvUrl;
            }

            public void setFlvUrl(String flvUrl2) {
                this.flvUrl = flvUrl2;
            }

            public String getRtmpUrl() {
                return this.rtmpUrl;
            }

            public void setRtmpUrl(String rtmpUrl2) {
                this.rtmpUrl = rtmpUrl2;
            }

            public String getPlaybackUrl() {
                return this.playbackUrl;
            }

            public void setPlaybackUrl(String playbackUrl2) {
                this.playbackUrl = playbackUrl2;
            }
        }
    }

    public static class InteractBean {
        private String cid;
        private String name;
        private String targeturl;
        private String thumbnail;

        public String getName() {
            return this.name;
        }

        public void setName(String name2) {
            this.name = name2;
        }

        public String getThumbnail() {
            return this.thumbnail;
        }

        public void setThumbnail(String thumbnail2) {
            this.thumbnail = thumbnail2;
        }

        public String getTargeturl() {
            return this.targeturl;
        }

        public void setTargeturl(String targeturl2) {
            this.targeturl = targeturl2;
        }

        public String getCid() {
            return this.cid;
        }

        public void setCid(String cid2) {
            this.cid = cid2;
        }
    }

    public void clear() {
        this.showSetting = null;
        this.channel = null;
        if (this.anchormen != null) {
            this.anchormen.clear();
        }
        if (this.items != null) {
            this.items.clear();
        }
        if (this.cameraList != null) {
            this.cameraList.clear();
        }
        if (this.interact != null) {
            this.interact.clear();
        }
    }
}
