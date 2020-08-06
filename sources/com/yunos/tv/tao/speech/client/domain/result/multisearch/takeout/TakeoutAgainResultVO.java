package com.yunos.tv.tao.speech.client.domain.result.multisearch.takeout;

import com.tvtaobao.voicesdk.bean.DetailListVO;
import com.yunos.tv.tao.speech.client.domain.result.multisearch.BaseResultVO;
import java.io.Serializable;
import java.util.List;

public class TakeoutAgainResultVO extends BaseResultVO implements Serializable {
    private List<DetailListVO> detailList;
    private String storeId;
    private String tbMainOrderId;

    public String getTbMainOrderId() {
        return this.tbMainOrderId;
    }

    public void setTbMainOrderId(String tbMainOrderId2) {
        this.tbMainOrderId = tbMainOrderId2;
    }

    public String getStoreId() {
        return this.storeId;
    }

    public void setStoreId(String storeId2) {
        this.storeId = storeId2;
    }

    public List<DetailListVO> getDetailList() {
        return this.detailList;
    }

    public void setDetailList(List<DetailListVO> detailList2) {
        this.detailList = detailList2;
    }
}
