package com.taobao.detail.domain.feature;

import java.io.Serializable;
import java.util.List;

public class PreSale implements Serializable {
    private Double depositPriceRatio;
    private List<PreSaleStepInfo> preSaleStepInfoList;
    public String showTime;
    private Integer type;
    private Boolean valid = true;

    public Boolean getValid() {
        return this.valid;
    }

    public void setValid(Boolean valid2) {
        this.valid = valid2;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type2) {
        this.type = type2;
    }

    public List<PreSaleStepInfo> getPreSaleStepInfoList() {
        return this.preSaleStepInfoList;
    }

    public void setPreSaleStepInfoList(List<PreSaleStepInfo> preSaleStepInfoList2) {
        this.preSaleStepInfoList = preSaleStepInfoList2;
    }

    public Double getDepositPriceRatio() {
        return this.depositPriceRatio;
    }

    public void setDepositPriceRatio(Double depositPriceRatio2) {
        this.depositPriceRatio = depositPriceRatio2;
    }

    public static class PreSaleStepInfo implements Serializable {
        private String endTime;
        private String name;
        private String startTime;
        private Integer stepNo;

        public Integer getStepNo() {
            return this.stepNo;
        }

        public void setStepNo(Integer stepNo2) {
            this.stepNo = stepNo2;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name2) {
            this.name = name2;
        }

        public String getStartTime() {
            return this.startTime;
        }

        public void setStartTime(String startTime2) {
            this.startTime = startTime2;
        }

        public String getEndTime() {
            return this.endTime;
        }

        public void setEndTime(String endTime2) {
            this.endTime = endTime2;
        }
    }
}
