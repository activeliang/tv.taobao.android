package com.yunos.tv.core.helpers;

import com.yunos.tv.core.util.RSASign;

public class YunOSOrderManager {
    private String Order = null;
    private String Sign = null;

    public void GenerateOrder(String orderFromApp) {
        this.Order = orderFromApp;
    }

    public void GenerateOrder(String prikey, String orderFromApp) {
        this.Order = orderFromApp;
        this.Sign = RSASign.sign(this.Order, prikey, "utf-8");
    }

    public void GenerateOrder(String prikey, String partner, String subject_id, String subject, String price, String partner_notify_url, String partner_order_no) {
        this.Order = "partner=" + partner + "&subject_id=" + subject_id + "&subject=" + subject + "&price=" + price + "&partner_notify_url=" + partner_notify_url + "&partner_order_no=" + partner_order_no;
        this.Sign = RSASign.sign(this.Order, prikey, "utf-8");
    }

    public String getOrder() {
        return this.Order;
    }

    public String getSign() {
        return this.Sign;
    }
}
