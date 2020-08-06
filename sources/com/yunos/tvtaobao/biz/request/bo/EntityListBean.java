package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.List;

public class EntityListBean implements Serializable {
    private List<ActivityListBean> activityList;
    private String addressText;
    private String agentFee;
    private String averageDeliverTime;
    private String deliverAmount;
    private String description;
    private String distance;
    private List<ItemListBean> itemList;
    private String latitude;
    private String longitude;
    private String mobile;
    private String name;
    private String notice;
    private String perCapitaPrice;
    private List<String> phoneList;
    private String saleCount;
    private String serviceId;
    private List<ServiceListBean> serviceList;
    private List<String> servingTime;
    private String shopId;
    private String shopLogo;
    private String shopStatus;
    private String starLevel;
    private String starPicUrl;

    public String getAddressText() {
        return this.addressText;
    }

    public void setAddressText(String addressText2) {
        this.addressText = addressText2;
    }

    public String getAgentFee() {
        return this.agentFee;
    }

    public void setAgentFee(String agentFee2) {
        this.agentFee = agentFee2;
    }

    public String getAverageDeliverTime() {
        return this.averageDeliverTime;
    }

    public void setAverageDeliverTime(String averageDeliverTime2) {
        this.averageDeliverTime = averageDeliverTime2;
    }

    public String getDeliverAmount() {
        return this.deliverAmount;
    }

    public void setDeliverAmount(String deliverAmount2) {
        this.deliverAmount = deliverAmount2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getDistance() {
        return this.distance;
    }

    public void setDistance(String distance2) {
        this.distance = distance2;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude2) {
        this.latitude = latitude2;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude2) {
        this.longitude = longitude2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getNotice() {
        return this.notice;
    }

    public void setNotice(String notice2) {
        this.notice = notice2;
    }

    public String getPerCapitaPrice() {
        return this.perCapitaPrice;
    }

    public void setPerCapitaPrice(String perCapitaPrice2) {
        this.perCapitaPrice = perCapitaPrice2;
    }

    public String getSaleCount() {
        return this.saleCount;
    }

    public void setSaleCount(String saleCount2) {
        this.saleCount = saleCount2;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(String serviceId2) {
        this.serviceId = serviceId2;
    }

    public String getShopId() {
        return this.shopId;
    }

    public void setShopId(String shopId2) {
        this.shopId = shopId2;
    }

    public String getShopLogo() {
        return this.shopLogo;
    }

    public void setShopLogo(String shopLogo2) {
        this.shopLogo = shopLogo2;
    }

    public String getShopStatus() {
        return this.shopStatus;
    }

    public void setShopStatus(String shopStatus2) {
        this.shopStatus = shopStatus2;
    }

    public String getStarLevel() {
        return this.starLevel;
    }

    public void setStarLevel(String starLevel2) {
        this.starLevel = starLevel2;
    }

    public String getStarPicUrl() {
        return this.starPicUrl;
    }

    public void setStarPicUrl(String starPicUrl2) {
        this.starPicUrl = starPicUrl2;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile2) {
        this.mobile = mobile2;
    }

    public List<ActivityListBean> getActivityList() {
        return this.activityList;
    }

    public void setActivityList(List<ActivityListBean> activityList2) {
        this.activityList = activityList2;
    }

    public List<ItemListBean> getItemList() {
        return this.itemList;
    }

    public void setItemList(List<ItemListBean> itemList2) {
        this.itemList = itemList2;
    }

    public List<String> getPhoneList() {
        return this.phoneList;
    }

    public void setPhoneList(List<String> phoneList2) {
        this.phoneList = phoneList2;
    }

    public List<ServiceListBean> getServiceList() {
        return this.serviceList;
    }

    public void setServiceList(List<ServiceListBean> serviceList2) {
        this.serviceList = serviceList2;
    }

    public List<String> getServingTime() {
        return this.servingTime;
    }

    public void setServingTime(List<String> servingTime2) {
        this.servingTime = servingTime2;
    }

    public static class ActivityListBean implements Serializable {
        private String description;
        private List<FullReduceDetailDTOListBean> fullReduceDetailDTOList;
        private String icon;
        private String id;
        private String name;
        private String sortId;
        private String storeId;
        private String type;

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description2) {
            this.description = description2;
        }

        public String getIcon() {
            return this.icon;
        }

        public void setIcon(String icon2) {
            this.icon = icon2;
        }

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

        public String getSortId() {
            return this.sortId;
        }

        public void setSortId(String sortId2) {
            this.sortId = sortId2;
        }

        public String getStoreId() {
            return this.storeId;
        }

        public void setStoreId(String storeId2) {
            this.storeId = storeId2;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type2) {
            this.type = type2;
        }

        public List<FullReduceDetailDTOListBean> getFullReduceDetailDTOList() {
            return this.fullReduceDetailDTOList;
        }

        public void setFullReduceDetailDTOList(List<FullReduceDetailDTOListBean> fullReduceDetailDTOList2) {
            this.fullReduceDetailDTOList = fullReduceDetailDTOList2;
        }

        public static class FullReduceDetailDTOListBean implements Serializable {
            private String offlineReduce;
            private String onLineReduce;
            private String sumCondition;

            public String getOfflineReduce() {
                return this.offlineReduce;
            }

            public void setOfflineReduce(String offlineReduce2) {
                this.offlineReduce = offlineReduce2;
            }

            public String getOnLineReduce() {
                return this.onLineReduce;
            }

            public void setOnLineReduce(String onLineReduce2) {
                this.onLineReduce = onLineReduce2;
            }

            public String getSumCondition() {
                return this.sumCondition;
            }

            public void setSumCondition(String sumCondition2) {
                this.sumCondition = sumCondition2;
            }
        }
    }

    public static class ServiceListBean implements Serializable {
        private String description;
        private String icon;
        private String name;
        private String type;

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description2) {
            this.description = description2;
        }

        public String getIcon() {
            return this.icon;
        }

        public void setIcon(String icon2) {
            this.icon = icon2;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name2) {
            this.name = name2;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type2) {
            this.type = type2;
        }
    }
}
