package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean;

import com.yunos.tvtaobao.biz.request.bo.ProductTagBo;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.Trade;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao.ApiStackBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao.ItemBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao.SellerBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao.SkuBaseBean;
import java.io.Serializable;
import java.util.List;

public class TBDetailResultV6 implements Serializable {
    private List<ApiStackBean> apiStack;
    private List<Enity> domainList;
    private ItemBean item;
    private String mockData;
    private DetailPanelData parsedDetailPanelData;
    private ProductTagBo productTagBo;
    private String propsCut;
    private SellerBean seller;
    private SkuBaseBean skuBase;
    private Trade trade;

    public ProductTagBo getProductTagBo() {
        return this.productTagBo;
    }

    public void setProductTagBo(ProductTagBo productTagBo2) {
        this.productTagBo = productTagBo2;
    }

    public DetailPanelData getParsedDetailPanelData() {
        return this.parsedDetailPanelData;
    }

    public void setParsedDetailPanelData(DetailPanelData parsedDetailPanelData2) {
        this.parsedDetailPanelData = parsedDetailPanelData2;
    }

    public List<Enity> getDomainUnit() {
        return this.domainList;
    }

    public void setDomainList(List<Enity> domainList2) {
        this.domainList = domainList2;
    }

    public ItemBean getItem() {
        return this.item;
    }

    public void setItem(ItemBean item2) {
        this.item = item2;
    }

    public String getMockData() {
        return this.mockData;
    }

    public void setMockData(String mockData2) {
        this.mockData = mockData2;
    }

    public String getPropsCut() {
        return this.propsCut;
    }

    public void setPropsCut(String propsCut2) {
        this.propsCut = propsCut2;
    }

    public SellerBean getSeller() {
        return this.seller;
    }

    public void setSeller(SellerBean seller2) {
        this.seller = seller2;
    }

    public SkuBaseBean getSkuBase() {
        return this.skuBase;
    }

    public void setSkuBase(SkuBaseBean skuBase2) {
        this.skuBase = skuBase2;
    }

    public List<ApiStackBean> getApiStack() {
        return this.apiStack;
    }

    public void setApiStack(List<ApiStackBean> apiStack2) {
        this.apiStack = apiStack2;
    }

    public Trade getTrade() {
        return this.trade;
    }

    public void setTrade(Trade trade2) {
        this.trade = trade2;
    }
}
