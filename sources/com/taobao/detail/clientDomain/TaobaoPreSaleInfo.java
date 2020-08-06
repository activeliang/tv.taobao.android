package com.taobao.detail.clientDomain;

import java.io.Serializable;

public class TaobaoPreSaleInfo implements Serializable {
    public Long currentSolicitationNum;
    public String payEnd;
    public Long payEndTimeMillis;
    public String payStart;
    public Long payStartTimeMillis;
    public Integer shipCycleDay;
    public String shipFixedTime;
    public Integer solicitationNum;
    public Long systemTimeMillis = Long.valueOf(System.currentTimeMillis());
    public Integer type;
}
