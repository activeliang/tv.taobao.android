package com.tvtaobao.voicesdk.bean;

import com.yunos.tv.tao.speech.client.domain.result.multisearch.item.ProductDo;
import java.util.List;

public class Products {
    private List<JinnangDo> jinnangs;
    private String keyword;
    private List<ProductDo> products;

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword2) {
        this.keyword = keyword2;
    }

    public List<ProductDo> getProducts() {
        return this.products;
    }

    public void setProducts(List<ProductDo> products2) {
        this.products = products2;
    }

    public List<JinnangDo> getJinnangs() {
        return this.jinnangs;
    }

    public void setJinnangs(List<JinnangDo> jinnangs2) {
        this.jinnangs = jinnangs2;
    }
}
