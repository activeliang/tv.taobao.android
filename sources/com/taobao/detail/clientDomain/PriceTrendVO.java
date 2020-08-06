package com.taobao.detail.clientDomain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PriceTrendVO implements Serializable {
    public int buyerAmount;
    public List<BRatioVO> buyerRatios;
    public int saleAmount;
    public Map<String, List<DatePrice>> skuDatePricesMap;
    public List<RatioVO> skuRatios;
    public int tmallbuyerAmount;

    public static class DatePrice implements Serializable {
        public String date;
        public int dateIndex;
        public String price;
        public String sales;
        public long skuId;
    }

    public static class RatioVO implements Serializable, Comparable<RatioVO> {
        public int amount = 0;
        public int level;
        public String txt;

        public int compareTo(RatioVO o) {
            return o.getAmount() - this.amount;
        }

        public int getAmount() {
            return this.amount;
        }
    }

    public static class BRatioVO implements Serializable, Comparable<BRatioVO> {
        public int amount = 0;
        public int buyAmount = 0;
        public int level;

        public int compareTo(BRatioVO o) {
            return o.getLevel() - this.level;
        }

        public int getLevel() {
            return this.level;
        }
    }
}
