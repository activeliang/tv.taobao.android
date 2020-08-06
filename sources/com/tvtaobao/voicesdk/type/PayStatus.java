package com.tvtaobao.voicesdk.type;

public enum PayStatus {
    STATUS_NOT_PAY(1, "等待买家付款"),
    STATUS_PAID(2, "等待卖家发货"),
    STATUS_REFUNDED(4, "交易关闭"),
    STATUS_TRANSFERED(6, "交易成功"),
    STATUS_NO_OUT_ORDER(7, "没有创建外部交易"),
    STATUS_CLOSED_BY_TAOBAO(8, "交易被淘宝关闭"),
    STATUS_NOT_REDY(9, "不可付款"),
    PAY_STATUS_APPEND_PAY(10, "待补付款状态"),
    PAY_STATUS_WAIT_ACCOUNT(12, "待到账状态");
    
    private String payInfo;
    private int payStatus;

    private PayStatus(int payStatus2, String payInfo2) {
        this.payStatus = payStatus2;
        this.payInfo = payInfo2;
    }

    public int getPayStatus() {
        return this.payStatus;
    }

    public String getPayInfo() {
        return this.payInfo;
    }
}
