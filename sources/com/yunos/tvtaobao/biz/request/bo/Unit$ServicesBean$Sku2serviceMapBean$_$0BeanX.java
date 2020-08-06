package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class Unit$ServicesBean$Sku2serviceMapBean$_$0BeanX {
    private String free;
    private String price;
    private String serviceId;
    private List<ServiceSkuPricesBean> serviceSkuPrices;

    public String getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(String serviceId2) {
        this.serviceId = serviceId2;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public String getFree() {
        return this.free;
    }

    public void setFree(String free2) {
        this.free = free2;
    }

    public List<ServiceSkuPricesBean> getServiceSkuPrices() {
        return this.serviceSkuPrices;
    }

    public void setServiceSkuPrices(List<ServiceSkuPricesBean> serviceSkuPrices2) {
        this.serviceSkuPrices = serviceSkuPrices2;
    }

    public static class ServiceSkuPricesBean {
        private String free;
        private String price;
        private String uniqueId;

        public String getUniqueId() {
            return this.uniqueId;
        }

        public void setUniqueId(String uniqueId2) {
            this.uniqueId = uniqueId2;
        }

        public String getPrice() {
            return this.price;
        }

        public void setPrice(String price2) {
            this.price = price2;
        }

        public String getFree() {
            return this.free;
        }

        public void setFree(String free2) {
            this.free = free2;
        }
    }
}
