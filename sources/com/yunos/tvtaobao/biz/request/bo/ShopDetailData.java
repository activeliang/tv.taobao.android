package com.yunos.tvtaobao.biz.request.bo;

import android.text.TextUtils;
import java.io.Serializable;
import java.util.List;

public class ShopDetailData implements Serializable {
    private String genreIds;
    private String haveNext;
    private List<ItemGenreWithItemsListBean> itemGenreWithItemsList;
    private String pageNo;
    private String pageSize;
    private StoreDetailDTOBean storeDetailDTO;
    private String totalNum;
    private String totalPage;
    private VoucherBean voucher;

    public void marge(ShopDetailData data) {
        if (data != null) {
            if (this.genreIds == null || this.genreIds.length() == 0) {
                this.genreIds = data.genreIds;
            }
            if (this.haveNext == null || this.haveNext.length() == 0) {
                this.haveNext = data.haveNext;
            }
            if (this.pageNo == null || this.pageNo.length() == 0) {
                this.pageNo = data.pageNo;
            }
            if (this.pageSize == null || this.pageSize.length() == 0) {
                this.pageSize = data.pageSize;
            }
            if (this.genreIds == null || this.genreIds.length() == 0) {
                this.genreIds = data.genreIds;
            }
            if (this.totalNum == null || this.totalNum.length() == 0) {
                this.totalNum = data.totalNum;
            }
            if (this.totalPage == null || this.totalPage.length() == 0) {
                this.totalPage = data.totalPage;
            }
            if (this.storeDetailDTO == null) {
                this.storeDetailDTO = data.storeDetailDTO;
            } else if (TextUtils.isEmpty(this.storeDetailDTO.shopId) && data.storeDetailDTO != null) {
                this.storeDetailDTO = data.storeDetailDTO;
            }
            if (this.voucher == null) {
                this.voucher = data.voucher;
            } else if (TextUtils.isEmpty(this.voucher.deductDesc)) {
                this.voucher = data.voucher;
            }
            if (this.itemGenreWithItemsList == null) {
                this.itemGenreWithItemsList = data.itemGenreWithItemsList;
            } else if (data.itemGenreWithItemsList != null) {
                this.itemGenreWithItemsList.addAll(data.itemGenreWithItemsList);
            }
        }
    }

    public String getGenreIds() {
        return this.genreIds;
    }

    public void setGenreIds(String genreIds2) {
        this.genreIds = genreIds2;
    }

    public String getHaveNext() {
        return this.haveNext;
    }

    public void setHaveNext(String haveNext2) {
        this.haveNext = haveNext2;
    }

    public String getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(String pageNo2) {
        this.pageNo = pageNo2;
    }

    public String getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(String pageSize2) {
        this.pageSize = pageSize2;
    }

    public StoreDetailDTOBean getStoreDetailDTO() {
        return this.storeDetailDTO;
    }

    public void setStoreDetailDTO(StoreDetailDTOBean storeDetailDTO2) {
        this.storeDetailDTO = storeDetailDTO2;
    }

    public String getTotalNum() {
        return this.totalNum;
    }

    public void setTotalNum(String totalNum2) {
        this.totalNum = totalNum2;
    }

    public String getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(String totalPage2) {
        this.totalPage = totalPage2;
    }

    public VoucherBean getVoucher() {
        return this.voucher;
    }

    public void setVoucher(VoucherBean voucher2) {
        this.voucher = voucher2;
    }

    public List<ItemGenreWithItemsListBean> getItemGenreWithItemsList() {
        return this.itemGenreWithItemsList;
    }

    public void setItemGenreWithItemsList(List<ItemGenreWithItemsListBean> itemGenreWithItemsList2) {
        this.itemGenreWithItemsList = itemGenreWithItemsList2;
    }

    public static class StoreDetailDTOBean implements Serializable {
        private List<ActivityListBean> activityList;
        private String addressText;
        private String agentFee;
        private String alreadyBought;
        private AttributesBean attributes;
        private String averageDeliverTime;
        private String busyLevel;
        private String categoryIds;
        private String city;
        private String currentDeliveryRule;
        private String dataSource;
        private String deliverAmount;
        private String deliverSpent;
        private String deliverTime;
        private String description;
        private String distRst;
        private String distance;
        private String entityType;
        private String foodSafetyCertificateImage;
        private String index;
        private String intervalRestTime;
        private String invoice;
        private String invoiceMinAmount;
        private String isBookable;
        private String latitude;
        private String licenseImage;
        private String longitude;
        private String mobile;
        private String name;
        private String newRestaurant;
        private String noAgentFeeTotal;
        private String notice;
        private String onlinePayment;
        private String orderLimit;
        private String outId;
        private String overArea;
        private String payOnDelivery;
        private String perCapitaPrice;
        private List<String> phoneList;
        private String premium;
        private String reportAndAdviceUrl;
        private String restShowText;
        private String saleCount;
        private String sellerId;
        private String serviceId;
        private String serviceLicenseImage;
        private List<ServiceListBean> serviceList;
        private List<String> servingTime;
        /* access modifiers changed from: private */
        public String shopId;
        private String shopLogo;
        private String shopStatus;
        public ShopStatusDetail shopStatusDetail;
        private String shopStatusIcon;
        private ShopStatusIconMapBean shopStatusIconMap;
        private String starLevel;
        private String starPicUrl;
        private String storeClosed;
        private String storeId;
        private String supportInsurance;
        private String timeEnsure;
        private String valid;
        private WillRestStatusDetailBean willRestStatusDetail;
        private String willResting;

        public class ShopStatusDetail implements Serializable {
            public String backGroundColor;
            public String color;
            public String statusDesc;

            public ShopStatusDetail() {
            }
        }

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

        public String getAlreadyBought() {
            return this.alreadyBought;
        }

        public void setAlreadyBought(String alreadyBought2) {
            this.alreadyBought = alreadyBought2;
        }

        public AttributesBean getAttributes() {
            return this.attributes;
        }

        public void setAttributes(AttributesBean attributes2) {
            this.attributes = attributes2;
        }

        public String getAverageDeliverTime() {
            return this.averageDeliverTime;
        }

        public void setAverageDeliverTime(String averageDeliverTime2) {
            this.averageDeliverTime = averageDeliverTime2;
        }

        public String getBusyLevel() {
            return this.busyLevel;
        }

        public void setBusyLevel(String busyLevel2) {
            this.busyLevel = busyLevel2;
        }

        public String getCategoryIds() {
            return this.categoryIds;
        }

        public void setCategoryIds(String categoryIds2) {
            this.categoryIds = categoryIds2;
        }

        public String getCity() {
            return this.city;
        }

        public void setCity(String city2) {
            this.city = city2;
        }

        public String getCurrentDeliveryRule() {
            return this.currentDeliveryRule;
        }

        public void setCurrentDeliveryRule(String currentDeliveryRule2) {
            this.currentDeliveryRule = currentDeliveryRule2;
        }

        public String getDataSource() {
            return this.dataSource;
        }

        public void setDataSource(String dataSource2) {
            this.dataSource = dataSource2;
        }

        public String getDeliverAmount() {
            return this.deliverAmount;
        }

        public void setDeliverAmount(String deliverAmount2) {
            this.deliverAmount = deliverAmount2;
        }

        public String getDeliverSpent() {
            return this.deliverSpent;
        }

        public void setDeliverSpent(String deliverSpent2) {
            this.deliverSpent = deliverSpent2;
        }

        public String getDeliverTime() {
            return this.deliverTime;
        }

        public void setDeliverTime(String deliverTime2) {
            this.deliverTime = deliverTime2;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description2) {
            this.description = description2;
        }

        public String getDistRst() {
            return this.distRst;
        }

        public void setDistRst(String distRst2) {
            this.distRst = distRst2;
        }

        public String getDistance() {
            return this.distance;
        }

        public void setDistance(String distance2) {
            this.distance = distance2;
        }

        public String getEntityType() {
            return this.entityType;
        }

        public void setEntityType(String entityType2) {
            this.entityType = entityType2;
        }

        public String getFoodSafetyCertificateImage() {
            return this.foodSafetyCertificateImage;
        }

        public void setFoodSafetyCertificateImage(String foodSafetyCertificateImage2) {
            this.foodSafetyCertificateImage = foodSafetyCertificateImage2;
        }

        public String getIndex() {
            return this.index;
        }

        public void setIndex(String index2) {
            this.index = index2;
        }

        public String getIntervalRestTime() {
            return this.intervalRestTime;
        }

        public void setIntervalRestTime(String intervalRestTime2) {
            this.intervalRestTime = intervalRestTime2;
        }

        public String getInvoice() {
            return this.invoice;
        }

        public void setInvoice(String invoice2) {
            this.invoice = invoice2;
        }

        public String getInvoiceMinAmount() {
            return this.invoiceMinAmount;
        }

        public void setInvoiceMinAmount(String invoiceMinAmount2) {
            this.invoiceMinAmount = invoiceMinAmount2;
        }

        public String getIsBookable() {
            return this.isBookable;
        }

        public void setIsBookable(String isBookable2) {
            this.isBookable = isBookable2;
        }

        public String getLatitude() {
            return this.latitude;
        }

        public void setLatitude(String latitude2) {
            this.latitude = latitude2;
        }

        public String getLicenseImage() {
            return this.licenseImage;
        }

        public void setLicenseImage(String licenseImage2) {
            this.licenseImage = licenseImage2;
        }

        public String getLongitude() {
            return this.longitude;
        }

        public void setLongitude(String longitude2) {
            this.longitude = longitude2;
        }

        public String getMobile() {
            return this.mobile;
        }

        public void setMobile(String mobile2) {
            this.mobile = mobile2;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name2) {
            this.name = name2;
        }

        public String getNewRestaurant() {
            return this.newRestaurant;
        }

        public void setNewRestaurant(String newRestaurant2) {
            this.newRestaurant = newRestaurant2;
        }

        public String getNoAgentFeeTotal() {
            return this.noAgentFeeTotal;
        }

        public void setNoAgentFeeTotal(String noAgentFeeTotal2) {
            this.noAgentFeeTotal = noAgentFeeTotal2;
        }

        public String getNotice() {
            return this.notice;
        }

        public void setNotice(String notice2) {
            this.notice = notice2;
        }

        public String getOnlinePayment() {
            return this.onlinePayment;
        }

        public void setOnlinePayment(String onlinePayment2) {
            this.onlinePayment = onlinePayment2;
        }

        public String getOrderLimit() {
            return this.orderLimit;
        }

        public void setOrderLimit(String orderLimit2) {
            this.orderLimit = orderLimit2;
        }

        public String getOutId() {
            return this.outId;
        }

        public void setOutId(String outId2) {
            this.outId = outId2;
        }

        public String getOverArea() {
            return this.overArea;
        }

        public void setOverArea(String overArea2) {
            this.overArea = overArea2;
        }

        public String getPayOnDelivery() {
            return this.payOnDelivery;
        }

        public void setPayOnDelivery(String payOnDelivery2) {
            this.payOnDelivery = payOnDelivery2;
        }

        public String getPerCapitaPrice() {
            return this.perCapitaPrice;
        }

        public void setPerCapitaPrice(String perCapitaPrice2) {
            this.perCapitaPrice = perCapitaPrice2;
        }

        public String getPremium() {
            return this.premium;
        }

        public void setPremium(String premium2) {
            this.premium = premium2;
        }

        public String getReportAndAdviceUrl() {
            return this.reportAndAdviceUrl;
        }

        public void setReportAndAdviceUrl(String reportAndAdviceUrl2) {
            this.reportAndAdviceUrl = reportAndAdviceUrl2;
        }

        public String getRestShowText() {
            return this.restShowText;
        }

        public void setRestShowText(String restShowText2) {
            this.restShowText = restShowText2;
        }

        public String getSaleCount() {
            return this.saleCount;
        }

        public void setSaleCount(String saleCount2) {
            this.saleCount = saleCount2;
        }

        public String getSellerId() {
            return this.sellerId;
        }

        public void setSellerId(String sellerId2) {
            this.sellerId = sellerId2;
        }

        public String getServiceId() {
            return this.serviceId;
        }

        public void setServiceId(String serviceId2) {
            this.serviceId = serviceId2;
        }

        public String getServiceLicenseImage() {
            return this.serviceLicenseImage;
        }

        public void setServiceLicenseImage(String serviceLicenseImage2) {
            this.serviceLicenseImage = serviceLicenseImage2;
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

        public String getShopStatusIcon() {
            return this.shopStatusIcon;
        }

        public void setShopStatusIcon(String shopStatusIcon2) {
            this.shopStatusIcon = shopStatusIcon2;
        }

        public ShopStatusIconMapBean getShopStatusIconMap() {
            return this.shopStatusIconMap;
        }

        public void setShopStatusIconMap(ShopStatusIconMapBean shopStatusIconMap2) {
            this.shopStatusIconMap = shopStatusIconMap2;
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

        public String getStoreClosed() {
            return this.storeClosed;
        }

        public void setStoreClosed(String storeClosed2) {
            this.storeClosed = storeClosed2;
        }

        public String getStoreId() {
            return this.storeId;
        }

        public void setStoreId(String storeId2) {
            this.storeId = storeId2;
        }

        public String getSupportInsurance() {
            return this.supportInsurance;
        }

        public void setSupportInsurance(String supportInsurance2) {
            this.supportInsurance = supportInsurance2;
        }

        public String getTimeEnsure() {
            return this.timeEnsure;
        }

        public void setTimeEnsure(String timeEnsure2) {
            this.timeEnsure = timeEnsure2;
        }

        public String getValid() {
            return this.valid;
        }

        public void setValid(String valid2) {
            this.valid = valid2;
        }

        public WillRestStatusDetailBean getWillRestStatusDetail() {
            return this.willRestStatusDetail;
        }

        public void setWillRestStatusDetail(WillRestStatusDetailBean willRestStatusDetail2) {
            this.willRestStatusDetail = willRestStatusDetail2;
        }

        public String getWillResting() {
            return this.willResting;
        }

        public void setWillResting(String willResting2) {
            this.willResting = willResting2;
        }

        public List<ActivityListBean> getActivityList() {
            return this.activityList;
        }

        public void setActivityList(List<ActivityListBean> activityList2) {
            this.activityList = activityList2;
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

        public static class AttributesBean implements Serializable {
            private String agent_fee;
            private String authen_status;
            private String bizOuterId;
            private String book_time_bitmap;
            private String brand_id;
            private String brand_info;
            private String businessTime;
            private String business_license;
            private String busy_level;
            private String category_ids;
            private String catering_service_license;
            private String city_id;
            private String contact_number;
            private String currentDeliveryRule;
            private String data_source;
            private String deliver_spent;
            private String description;
            private String image_url;
            private String invoice;
            private String invoice_min_amount;
            private String is_bookable;
            private String is_dist_rst;
            private String is_double_cert;
            private String is_koubei_rst;
            private String is_new_retail;
            private String is_on_time;
            private String is_phone_hidden;
            private String is_premium;
            private String is_time_ensure;
            private String is_valid;
            private String minosproduct_available_time;
            private String name_for_url;
            private String new_restaurant;
            private String no_agent_fee_total;
            private String notice;
            private String num_ratings;
            private String online_payment;
            private String open_time_bitmap;
            private String operation_labels;
            private String order_activity_limit_counter;
            private String order_mode;
            private String out_category_ids;
            private String out_id;
            private String parent_category_ids;
            private String payment_method;
            private String phone_list;
            private String ranking_score;
            private String recent_order_num;
            private String recent_sales;
            private String register_info;
            private String rst_category;
            private String rst_category_ka;
            private String service_id;
            private String serving_time;
            private String support_insurance;
            private String support_online;
            private String total_status;
            private String unit_price;
            private String updated_at;
            private String zeroDeliveryRule;

            public String getService_id() {
                return this.service_id;
            }

            public void setService_id(String service_id2) {
                this.service_id = service_id2;
            }

            public String getRecent_order_num() {
                return this.recent_order_num;
            }

            public void setRecent_order_num(String recent_order_num2) {
                this.recent_order_num = recent_order_num2;
            }

            public String getParent_category_ids() {
                return this.parent_category_ids;
            }

            public void setParent_category_ids(String parent_category_ids2) {
                this.parent_category_ids = parent_category_ids2;
            }

            public String getInvoice() {
                return this.invoice;
            }

            public void setInvoice(String invoice2) {
                this.invoice = invoice2;
            }

            public String getData_source() {
                return this.data_source;
            }

            public void setData_source(String data_source2) {
                this.data_source = data_source2;
            }

            public String getMinosproduct_available_time() {
                return this.minosproduct_available_time;
            }

            public void setMinosproduct_available_time(String minosproduct_available_time2) {
                this.minosproduct_available_time = minosproduct_available_time2;
            }

            public String getUnit_price() {
                return this.unit_price;
            }

            public void setUnit_price(String unit_price2) {
                this.unit_price = unit_price2;
            }

            public String getTotal_status() {
                return this.total_status;
            }

            public void setTotal_status(String total_status2) {
                this.total_status = total_status2;
            }

            public String getImage_url() {
                return this.image_url;
            }

            public void setImage_url(String image_url2) {
                this.image_url = image_url2;
            }

            public String getDescription() {
                return this.description;
            }

            public void setDescription(String description2) {
                this.description = description2;
            }

            public String getOut_category_ids() {
                return this.out_category_ids;
            }

            public void setOut_category_ids(String out_category_ids2) {
                this.out_category_ids = out_category_ids2;
            }

            public String getBusy_level() {
                return this.busy_level;
            }

            public void setBusy_level(String busy_level2) {
                this.busy_level = busy_level2;
            }

            public String getName_for_url() {
                return this.name_for_url;
            }

            public void setName_for_url(String name_for_url2) {
                this.name_for_url = name_for_url2;
            }

            public String getCatering_service_license() {
                return this.catering_service_license;
            }

            public void setCatering_service_license(String catering_service_license2) {
                this.catering_service_license = catering_service_license2;
            }

            public String getServing_time() {
                return this.serving_time;
            }

            public void setServing_time(String serving_time2) {
                this.serving_time = serving_time2;
            }

            public String getCurrentDeliveryRule() {
                return this.currentDeliveryRule;
            }

            public void setCurrentDeliveryRule(String currentDeliveryRule2) {
                this.currentDeliveryRule = currentDeliveryRule2;
            }

            public String getBrand_id() {
                return this.brand_id;
            }

            public void setBrand_id(String brand_id2) {
                this.brand_id = brand_id2;
            }

            public String getIs_dist_rst() {
                return this.is_dist_rst;
            }

            public void setIs_dist_rst(String is_dist_rst2) {
                this.is_dist_rst = is_dist_rst2;
            }

            public String getBizOuterId() {
                return this.bizOuterId;
            }

            public void setBizOuterId(String bizOuterId2) {
                this.bizOuterId = bizOuterId2;
            }

            public String getBrand_info() {
                return this.brand_info;
            }

            public void setBrand_info(String brand_info2) {
                this.brand_info = brand_info2;
            }

            public String getAuthen_status() {
                return this.authen_status;
            }

            public void setAuthen_status(String authen_status2) {
                this.authen_status = authen_status2;
            }

            public String getDeliver_spent() {
                return this.deliver_spent;
            }

            public void setDeliver_spent(String deliver_spent2) {
                this.deliver_spent = deliver_spent2;
            }

            public String getNew_restaurant() {
                return this.new_restaurant;
            }

            public void setNew_restaurant(String new_restaurant2) {
                this.new_restaurant = new_restaurant2;
            }

            public String getOnline_payment() {
                return this.online_payment;
            }

            public void setOnline_payment(String online_payment2) {
                this.online_payment = online_payment2;
            }

            public String getContact_number() {
                return this.contact_number;
            }

            public void setContact_number(String contact_number2) {
                this.contact_number = contact_number2;
            }

            public String getNum_ratings() {
                return this.num_ratings;
            }

            public void setNum_ratings(String num_ratings2) {
                this.num_ratings = num_ratings2;
            }

            public String getUpdated_at() {
                return this.updated_at;
            }

            public void setUpdated_at(String updated_at2) {
                this.updated_at = updated_at2;
            }

            public String getIs_on_time() {
                return this.is_on_time;
            }

            public void setIs_on_time(String is_on_time2) {
                this.is_on_time = is_on_time2;
            }

            public String getOperation_labels() {
                return this.operation_labels;
            }

            public void setOperation_labels(String operation_labels2) {
                this.operation_labels = operation_labels2;
            }

            public String getSupport_insurance() {
                return this.support_insurance;
            }

            public void setSupport_insurance(String support_insurance2) {
                this.support_insurance = support_insurance2;
            }

            public String getOrder_mode() {
                return this.order_mode;
            }

            public void setOrder_mode(String order_mode2) {
                this.order_mode = order_mode2;
            }

            public String getIs_premium() {
                return this.is_premium;
            }

            public void setIs_premium(String is_premium2) {
                this.is_premium = is_premium2;
            }

            public String getPayment_method() {
                return this.payment_method;
            }

            public void setPayment_method(String payment_method2) {
                this.payment_method = payment_method2;
            }

            public String getIs_phone_hidden() {
                return this.is_phone_hidden;
            }

            public void setIs_phone_hidden(String is_phone_hidden2) {
                this.is_phone_hidden = is_phone_hidden2;
            }

            public String getNo_agent_fee_total() {
                return this.no_agent_fee_total;
            }

            public void setNo_agent_fee_total(String no_agent_fee_total2) {
                this.no_agent_fee_total = no_agent_fee_total2;
            }

            public String getSupport_online() {
                return this.support_online;
            }

            public void setSupport_online(String support_online2) {
                this.support_online = support_online2;
            }

            public String getAgent_fee() {
                return this.agent_fee;
            }

            public void setAgent_fee(String agent_fee2) {
                this.agent_fee = agent_fee2;
            }

            public String getIs_double_cert() {
                return this.is_double_cert;
            }

            public void setIs_double_cert(String is_double_cert2) {
                this.is_double_cert = is_double_cert2;
            }

            public String getBusinessTime() {
                return this.businessTime;
            }

            public void setBusinessTime(String businessTime2) {
                this.businessTime = businessTime2;
            }

            public String getBusiness_license() {
                return this.business_license;
            }

            public void setBusiness_license(String business_license2) {
                this.business_license = business_license2;
            }

            public String getPhone_list() {
                return this.phone_list;
            }

            public void setPhone_list(String phone_list2) {
                this.phone_list = phone_list2;
            }

            public String getBook_time_bitmap() {
                return this.book_time_bitmap;
            }

            public void setBook_time_bitmap(String book_time_bitmap2) {
                this.book_time_bitmap = book_time_bitmap2;
            }

            public String getIs_koubei_rst() {
                return this.is_koubei_rst;
            }

            public void setIs_koubei_rst(String is_koubei_rst2) {
                this.is_koubei_rst = is_koubei_rst2;
            }

            public String getIs_valid() {
                return this.is_valid;
            }

            public void setIs_valid(String is_valid2) {
                this.is_valid = is_valid2;
            }

            public String getIs_bookable() {
                return this.is_bookable;
            }

            public void setIs_bookable(String is_bookable2) {
                this.is_bookable = is_bookable2;
            }

            public String getCity_id() {
                return this.city_id;
            }

            public void setCity_id(String city_id2) {
                this.city_id = city_id2;
            }

            public String getInvoice_min_amount() {
                return this.invoice_min_amount;
            }

            public void setInvoice_min_amount(String invoice_min_amount2) {
                this.invoice_min_amount = invoice_min_amount2;
            }

            public String getOpen_time_bitmap() {
                return this.open_time_bitmap;
            }

            public void setOpen_time_bitmap(String open_time_bitmap2) {
                this.open_time_bitmap = open_time_bitmap2;
            }

            public String getRecent_sales() {
                return this.recent_sales;
            }

            public void setRecent_sales(String recent_sales2) {
                this.recent_sales = recent_sales2;
            }

            public String getNotice() {
                return this.notice;
            }

            public void setNotice(String notice2) {
                this.notice = notice2;
            }

            public String getOrder_activity_limit_counter() {
                return this.order_activity_limit_counter;
            }

            public void setOrder_activity_limit_counter(String order_activity_limit_counter2) {
                this.order_activity_limit_counter = order_activity_limit_counter2;
            }

            public String getIs_new_retail() {
                return this.is_new_retail;
            }

            public void setIs_new_retail(String is_new_retail2) {
                this.is_new_retail = is_new_retail2;
            }

            public String getZeroDeliveryRule() {
                return this.zeroDeliveryRule;
            }

            public void setZeroDeliveryRule(String zeroDeliveryRule2) {
                this.zeroDeliveryRule = zeroDeliveryRule2;
            }

            public String getRst_category() {
                return this.rst_category;
            }

            public void setRst_category(String rst_category2) {
                this.rst_category = rst_category2;
            }

            public String getRanking_score() {
                return this.ranking_score;
            }

            public void setRanking_score(String ranking_score2) {
                this.ranking_score = ranking_score2;
            }

            public String getCategory_ids() {
                return this.category_ids;
            }

            public void setCategory_ids(String category_ids2) {
                this.category_ids = category_ids2;
            }

            public String getIs_time_ensure() {
                return this.is_time_ensure;
            }

            public void setIs_time_ensure(String is_time_ensure2) {
                this.is_time_ensure = is_time_ensure2;
            }

            public String getRst_category_ka() {
                return this.rst_category_ka;
            }

            public void setRst_category_ka(String rst_category_ka2) {
                this.rst_category_ka = rst_category_ka2;
            }

            public String getRegister_info() {
                return this.register_info;
            }

            public void setRegister_info(String register_info2) {
                this.register_info = register_info2;
            }

            public String getOut_id() {
                return this.out_id;
            }

            public void setOut_id(String out_id2) {
                this.out_id = out_id2;
            }
        }

        public static class ShopStatusIconMapBean implements Serializable {
            private String BOOKING;
            private String RESTING;
            private String SELLING;
            private String WILLRESTING;

            public String getRESTING() {
                return this.RESTING;
            }

            public void setRESTING(String RESTING2) {
                this.RESTING = RESTING2;
            }

            public String getBOOKING() {
                return this.BOOKING;
            }

            public void setBOOKING(String BOOKING2) {
                this.BOOKING = BOOKING2;
            }

            public String getSELLING() {
                return this.SELLING;
            }

            public void setSELLING(String SELLING2) {
                this.SELLING = SELLING2;
            }

            public String getWILLRESTING() {
                return this.WILLRESTING;
            }

            public void setWILLRESTING(String WILLRESTING2) {
                this.WILLRESTING = WILLRESTING2;
            }
        }

        public static class WillRestStatusDetailBean implements Serializable {
            private String backGroundColor;
            private String color;
            private String statusDesc;

            public String getBackGroundColor() {
                return this.backGroundColor;
            }

            public void setBackGroundColor(String backGroundColor2) {
                this.backGroundColor = backGroundColor2;
            }

            public String getColor() {
                return this.color;
            }

            public void setColor(String color2) {
                this.color = color2;
            }

            public String getStatusDesc() {
                return this.statusDesc;
            }

            public void setStatusDesc(String statusDesc2) {
                this.statusDesc = statusDesc2;
            }
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

    public static class VoucherBean implements Serializable {
        private String conditionDesc;
        /* access modifiers changed from: private */
        public String deductDesc;
        private String description;
        private String icon;
        private String status;
        private String statusContent;
        private String storeId;
        private String storeName;

        public String getConditionDesc() {
            return this.conditionDesc;
        }

        public void setConditionDesc(String conditionDesc2) {
            this.conditionDesc = conditionDesc2;
        }

        public String getDeductDesc() {
            return this.deductDesc;
        }

        public void setDeductDesc(String deductDesc2) {
            this.deductDesc = deductDesc2;
        }

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

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status2) {
            this.status = status2;
        }

        public String getStatusContent() {
            return this.statusContent;
        }

        public void setStatusContent(String statusContent2) {
            this.statusContent = statusContent2;
        }

        public String getStoreId() {
            return this.storeId;
        }

        public void setStoreId(String storeId2) {
            this.storeId = storeId2;
        }

        public String getStoreName() {
            return this.storeName;
        }

        public void setStoreName(String storeName2) {
            this.storeName = storeName2;
        }
    }

    public static class ItemGenreWithItemsListBean implements Serializable {
        public FocusStatus focusStatus;
        private String icon;
        private String id;
        private List<ItemListBean> itemList;
        private String light;
        private String name;
        private String sort;
        private String status;
        private String storeId;
        public int totleGoodCount;

        public enum FocusStatus {
            NO,
            FOCUS,
            TEMPLETE
        }

        public String getId() {
            return this.id;
        }

        public void setId(String id2) {
            this.id = id2;
        }

        public String getLight() {
            return this.light;
        }

        public void setLight(String light2) {
            this.light = light2;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name2) {
            this.name = name2;
        }

        public String getSort() {
            return this.sort;
        }

        public void setSort(String sort2) {
            this.sort = sort2;
        }

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status2) {
            this.status = status2;
        }

        public String getStoreId() {
            return this.storeId;
        }

        public void setStoreId(String storeId2) {
            this.storeId = storeId2;
        }

        public String getIcon() {
            return this.icon;
        }

        public void setIcon(String icon2) {
            this.icon = icon2;
        }

        public List<ItemListBean> getItemList() {
            return this.itemList;
        }

        public void setItemList(List<ItemListBean> itemList2) {
            this.itemList = itemList2;
        }
    }
}
