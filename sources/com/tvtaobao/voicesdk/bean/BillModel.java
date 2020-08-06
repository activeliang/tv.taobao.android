package com.tvtaobao.voicesdk.bean;

import java.io.Serializable;
import java.util.List;

public class BillModel implements Serializable {
    private String amounts;
    private List<BillDo> detailDOList;

    public void setAmounts(String amounts2) {
        this.amounts = amounts2;
    }

    public String getAmounts() {
        return this.amounts;
    }

    public void setDetailDOList(List<BillDo> detailDOList2) {
        this.detailDOList = detailDOList2;
    }

    public List<BillDo> getDetailDOList() {
        return this.detailDOList;
    }
}
