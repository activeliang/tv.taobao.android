package com.yunos.tvtaobao.biz.request.bo;

import com.taobao.detail.domain.base.Unit;
import com.yunos.tvtaobao.biz.request.bo.resource.entrances.Entrances;
import com.yunos.tvtaobao.biz.request.bo.resource.entrances.ShopProm;
import java.io.Serializable;
import java.util.List;

public class TBDetailResultVO_v6 {
    private List<Unit> apiStack;
    private Buyer buyer;
    private ConsumerProtection consumerProtection;
    private Delivery delivery;
    private Feature feature;
    private Item item;
    private Layout layout;
    private ShopProm mShopProm;
    private Price price;
    private Resource resource;
    private Trade trade;
    private Vertical vertical;

    public static class Buyer implements Serializable {
    }

    public static class ConsumerProtection implements Serializable {
    }

    public static class Delivery implements Serializable {
    }

    public static class Feature implements Serializable {
    }

    public static class Item implements Serializable {
    }

    public static class Layout implements Serializable {
    }

    public static class Trade implements Serializable {
    }

    public static class Vertical implements Serializable {
    }

    public List<Unit> getApiStack() {
        return this.apiStack;
    }

    public void setApiStack(List<Unit> apiStack2) {
        this.apiStack = apiStack2;
    }

    public Trade getTrade() {
        return this.trade;
    }

    public void setTrade(Trade trade2) {
        this.trade = trade2;
    }

    public Buyer getBuyer() {
        return this.buyer;
    }

    public void setBuyer(Buyer buyer2) {
        this.buyer = buyer2;
    }

    public ConsumerProtection getConsumerProtection() {
        return this.consumerProtection;
    }

    public void setConsumerProtection(ConsumerProtection consumerProtection2) {
        this.consumerProtection = consumerProtection2;
    }

    public Delivery getDelivery() {
        return this.delivery;
    }

    public void setDelivery(Delivery delivery2) {
        this.delivery = delivery2;
    }

    public Feature getFeature() {
        return this.feature;
    }

    public void setFeature(Feature feature2) {
        this.feature = feature2;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item2) {
        this.item = item2;
    }

    public Price getPrice() {
        return this.price;
    }

    public void setPrice(Price price2) {
        this.price = price2;
    }

    public Resource getResource() {
        return this.resource;
    }

    public void setResource(Resource resource2) {
        this.resource = resource2;
    }

    public Vertical getVertical() {
        return this.vertical;
    }

    public void setVertical(Vertical vertical2) {
        this.vertical = vertical2;
    }

    public Layout getLayout() {
        return this.layout;
    }

    public void setLayout(Layout layout2) {
        this.layout = layout2;
    }

    public static class Price implements Serializable {
        private List<ShopProm> mShopProm;

        public List<ShopProm> getShopProm() {
            return this.mShopProm;
        }

        public void setShopProm(List<ShopProm> shopProm) {
            this.mShopProm = shopProm;
        }
    }

    public static class Resource implements Serializable {
        private Entrances entrances;

        public Entrances getEntrances() {
            return this.entrances;
        }

        public void setEntrances(Entrances entrances2) {
            this.entrances = entrances2;
        }
    }

    public String toString() {
        return "TBDetailResultVO_v6{apiStack=" + this.apiStack + ", trade=" + this.trade + ", buyer=" + this.buyer + ", consumerProtection=" + this.consumerProtection + ", delivery=" + this.delivery + ", feature=" + this.feature + ", item=" + this.item + ", price=" + this.price + ", resource=" + this.resource + ", vertical=" + this.vertical + ", layout=" + this.layout + ", mShopProm=" + this.mShopProm + '}';
    }

    public ShopProm getShopProm() {
        return this.mShopProm;
    }

    public void setShopProm(ShopProm shopProm) {
        this.mShopProm = shopProm;
    }
}
