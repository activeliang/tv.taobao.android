package com.yunos.tv.tao.speech.client.domain.result.multisearch.takeout;

import com.yunos.tv.tao.speech.client.domain.result.multisearch.BaseResultVO;
import java.io.Serializable;

public class TakeoutProgressResultVO extends BaseResultVO implements Serializable {
    private String deliveryMobile;
    private String deliveryName;
    private String distance;
    private String status;
    private String tbMainOrderId;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public String getTbMainOrderId() {
        return this.tbMainOrderId;
    }

    public void setTbMainOrderId(String tbMainOrderId2) {
        this.tbMainOrderId = tbMainOrderId2;
    }

    public String getDeliveryName() {
        return this.deliveryName;
    }

    public void setDeliveryName(String deliveryName2) {
        this.deliveryName = deliveryName2;
    }

    public String getDeliveryMobile() {
        return this.deliveryMobile;
    }

    public void setDeliveryMobile(String deliveryMobile2) {
        this.deliveryMobile = deliveryMobile2;
    }

    public String getDistance() {
        return this.distance;
    }

    public void setDistance(String distance2) {
        this.distance = distance2;
    }
}
