package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public interface SearchedGoods {
    public static final String TYPE_ITEM = "item";
    public static final String TYPE_ZTC = "ztc";

    String getEurl();

    String getImgUrl();

    String getItemId();

    String getNick();

    String getPostFee();

    String getPrice();

    RebateBo getRebateBo();

    String getS11();

    String getS11Pre();

    String getSdkurl();

    String getShopId();

    String getSold();

    String getSoldText();

    List<String> getTagNames();

    String getTagPicUrl();

    String getTitle();

    String getType();

    String getUri();

    String getWmPrice();

    Boolean isBuyCashback();

    Boolean isPre();

    void setRebateBo(RebateBo rebateBo);
}
