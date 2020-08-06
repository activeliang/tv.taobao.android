package com.taobao.detail.domain.feature;

import com.taobao.detail.domain.DetailVO;
import java.io.Serializable;
import java.util.HashMap;

public class PoiInfo implements Serializable {
    public String id;
    public String location;
    public String logo;
    public String mapUrl;
    public HashMap<String, DetailVO.DynamicItem.SkuPriceAndQuanitiy> priceAndQuanitiyInfo;
    public String title;
}
