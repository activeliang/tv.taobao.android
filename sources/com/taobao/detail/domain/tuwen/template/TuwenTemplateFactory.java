package com.taobao.detail.domain.tuwen.template;

import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.taobao.detail.domain.tuwen.data.GoodsMatchingCO;
import com.taobao.detail.domain.tuwen.data.PackinglistCO;
import com.taobao.detail.domain.tuwen.data.ProductInfoCO;
import com.taobao.detail.domain.tuwen.data.SizeChartCO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TuwenTemplateFactory extends TemplateFactory {
    public static TuwenTemplate makeTemplate(String type, Long itemId, Map<String, Object> requestMap) {
        if (requestMap == null) {
            requestMap = new HashMap<>();
        }
        if (type.equals("main")) {
            count = 0;
            return new TuwenTemplate("detail_layout_" + itemId, "native", TuwenConstants.KEY.SYS_LIST, (Map<String, Object>) null, (List<TuwenTemplate>) null);
        } else if (type.equals("coupon")) {
            Map maps = new HashMap();
            maps.put(TuwenConstants.PARAMS.EXTRA_TEXT, "无更多优惠券");
            requestMap.put("coupon", true);
            StringBuilder append = new StringBuilder().append("coupon_division_title");
            int i = count + 1;
            count = i;
            return new TuwenTemplate(TuwenConstants.CONTAINER_ID.COUPON, "native", TuwenConstants.CONTAINER.STYLE2, maps, makeChildren(makeChildren(append.append(i).toString(), "native", TuwenConstants.KEY.DIVISION_TITLE, "0xffffff", "0x051b28", "0xffffff", "优惠券"), makeChildren(TuwenConstants.COMPONENT_ID.COUPON, "native", TuwenConstants.KEY.DETAIL_COUPON, TuwenConstants.STYLE.LOOP)));
        } else if (type.equals(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)) {
            Map maps2 = new HashMap();
            maps2.put(TuwenConstants.PARAMS.CHILDREN_STYLE, TuwenConstants.STYLE.SEQUENCE);
            requestMap.put(TuwenConstants.MODEL_LIST_KEY.ACTIVITY, true);
            StringBuilder append2 = new StringBuilder().append("activity_division_title");
            int i2 = count + 1;
            count = i2;
            return new TuwenTemplate(TuwenConstants.CONTAINER_ID.ACTIVITY, "native", TuwenConstants.CONTAINER.STYLE2, maps2, makeChildren(makeChildren(append2.append(i2).toString(), "native", TuwenConstants.KEY.DIVISION_TITLE, "0xffffff", "0x051b28", "0xffffff", "店铺活动"), makeChildren(TuwenConstants.COMPONENT_ID.ACTIVITY, "native", TuwenConstants.KEY.PIC_JUMPER, TuwenConstants.STYLE.LOOP)));
        } else if (type.equals("recommend")) {
            Map maps3 = new HashMap();
            maps3.put(TuwenConstants.PARAMS.CHILDREN_STYLE, TuwenConstants.STYLE.SEQUENCE);
            requestMap.put("recommend", true);
            StringBuilder append3 = new StringBuilder().append("sellerRecommend_division_title");
            int i3 = count + 1;
            count = i3;
            return new TuwenTemplate(TuwenConstants.CONTAINER_ID.RECOMMEND, "native", TuwenConstants.CONTAINER.STYLE3, maps3, makeChildren(makeChildren(append3.append(i3).toString(), "native", TuwenConstants.KEY.DIVISION_TITLE, "0xffffff", "0x051b28", "0xffffff", "卖家推荐"), makeChildren(TuwenConstants.COMPONENT_ID.RECOMMEND, "native", TuwenConstants.KEY.DETAIL_ITEMINFO, TuwenConstants.STYLE.LOOP)));
        } else if (type.equals(TuwenConstants.MODEL_LIST_KEY.SEE_MORE)) {
            Map maps4 = new HashMap();
            maps4.put(TuwenConstants.PARAMS.CHILDREN_STYLE, TuwenConstants.STYLE.SEQUENCE);
            requestMap.put(TuwenConstants.MODEL_LIST_KEY.SEE_MORE, true);
            StringBuilder append4 = new StringBuilder().append("seemore_division_title");
            int i4 = count + 1;
            count = i4;
            return new TuwenTemplate(TuwenConstants.CONTAINER_ID.SEEMORE, "native", TuwenConstants.CONTAINER.STYLE3, maps4, makeChildren(makeChildren(append4.append(i4).toString(), "native", TuwenConstants.KEY.DIVISION_TITLE, "0xffffff", "0x051b28", "0xffffff", "看了又看"), makeChildren(TuwenConstants.COMPONENT_ID.SEEMORE, "native", TuwenConstants.KEY.DETAIL_ITEMINFO, TuwenConstants.STYLE.LOOP)));
        } else if (type.equals(TuwenConstants.MODEL_LIST_KEY.PICTURE)) {
            Map maps5 = new HashMap();
            maps5.put(TuwenConstants.PARAMS.CHILDREN_STYLE, TuwenConstants.STYLE.SEQUENCE);
            StringBuilder append5 = new StringBuilder().append("detail_pic_");
            int i5 = count + 1;
            count = i5;
            return new TuwenTemplate(append5.append(i5).toString(), "native", TuwenConstants.CONTAINER.STYLE7, maps5, (List<TuwenTemplate>) null);
        } else if (type.equals(TuwenConstants.MODEL_LIST_KEY.FIXED_PICTURE)) {
            Map maps6 = new HashMap();
            maps6.put(TuwenConstants.PARAMS.CHILDREN_STYLE, TuwenConstants.STYLE.SEQUENCE);
            StringBuilder append6 = new StringBuilder().append("detail_pic_");
            int i6 = count + 1;
            count = i6;
            return new TuwenTemplate(append6.append(i6).toString(), "native", TuwenConstants.CONTAINER.STYLE1, maps6, (List<TuwenTemplate>) null);
        } else if (!type.equals(TuwenConstants.MODEL_LIST_KEY.RECOMMEND_2)) {
            return new TuwenTemplate();
        } else {
            Map maps7 = new HashMap();
            maps7.put(TuwenConstants.PARAMS.CHILDREN_STYLE, TuwenConstants.STYLE.SEQUENCE);
            requestMap.put(TuwenConstants.MODEL_LIST_KEY.RECOMMEND_2, true);
            StringBuilder append7 = new StringBuilder().append("sellerRecommend_division_title");
            int i7 = count + 1;
            count = i7;
            return new TuwenTemplate(TuwenConstants.CONTAINER_ID.RECOMMEND_2, "native", TuwenConstants.CONTAINER.STYLE6, maps7, makeChildren(makeChildren(append7.append(i7).toString(), "native", TuwenConstants.KEY.DIVISION_TITLE, "0xffffff", "0x051b28", "0xffffff", "卖家推荐"), makeChildren(TuwenConstants.COMPONENT_ID.RECOMMEND_2, "native", TuwenConstants.KEY.DETAIL_ITEMINFO_2, TuwenConstants.STYLE.LOOP, 0.33d)));
        }
    }

    public static TuwenTemplate makeSizeChart(Long itemId, Map<String, Object> map, List<SizeChartCO> chartCOs) {
        if (chartCOs == null || chartCOs.size() == 0) {
            return null;
        }
        Map maps = new HashMap();
        maps.put(TuwenConstants.PARAMS.CHILDREN_STYLE, TuwenConstants.STYLE.SEQUENCE);
        StringBuilder append = new StringBuilder().append("sizechart_division_title");
        int i = count + 1;
        count = i;
        TuwenTemplate tuwenTemplate = new TuwenTemplate(TuwenConstants.CONTAINER_ID.SIZECHART, "native", TuwenConstants.CONTAINER.STYLE4, maps, makeChildren(makeChildren(append.append(i).toString(), "native", TuwenConstants.KEY.DIVISION_TITLE, "0xffffff", "0x051b28", "0xffffff", "商品尺码对照表")));
        boolean hasModelWear = false;
        boolean hasPicUrl = false;
        int tableCount = 0;
        for (int i2 = 0; i2 < chartCOs.size(); i2++) {
            SizeChartCO co = chartCOs.get(i2);
            if (co.data != null && co.data.size() > 0) {
                Map<String, Object> params = new HashMap<>();
                params.put("data", co.data);
                params.put(TuwenConstants.PARAMS.LOOP_STYLE, TuwenConstants.STYLE.SEQUENCE);
                params.put("title", "");
                params.put(TuwenConstants.PARAMS.SHOW_TITLE, co.title);
                tuwenTemplate.addChild(new TuwenTemplate(TuwenConstants.COMPONENT_ID.SIZECHART + (i2 + 1), "native", TuwenConstants.KEY.DETAIL_SIZECHART, params, (List<TuwenTemplate>) null));
                tableCount++;
            } else if (co.picUrl != null && !co.picUrl.equals("")) {
                Map<String, Object> params2 = new HashMap<>();
                params2.put(TuwenConstants.PARAMS.PIC_URL, co.picUrl);
                params2.put("title", "");
                params2.put(TuwenConstants.PARAMS.SHOW_TITLE, co.title);
                tuwenTemplate.addChild(new TuwenTemplate(TuwenConstants.COMPONENT_ID.SIZECHART + (i2 + 1), "native", TuwenConstants.KEY.DETAIL_PICWITHTITLE, params2, (List<TuwenTemplate>) null));
                hasPicUrl = true;
            } else if (co.modelWears != null && co.modelWears.size() > 0) {
                Map<String, Object> params3 = new HashMap<>();
                params3.put("data", co.modelWears);
                params3.put(TuwenConstants.PARAMS.LOOP_STYLE, TuwenConstants.STYLE.SEQUENCE);
                params3.put("title", "");
                params3.put(TuwenConstants.PARAMS.SHOW_TITLE, co.title);
                tuwenTemplate.addChild(new TuwenTemplate(TuwenConstants.COMPONENT_ID.MODELWEAR + (i2 + 1), "native", TuwenConstants.KEY.DETAIL_MODELWEAR, params3, (List<TuwenTemplate>) null));
                hasModelWear = true;
            }
        }
        if (hasPicUrl) {
            tuwenTemplate.addParams(TuwenConstants.MODEL_LIST_KEY.TEXT, "查看测量示意图");
        }
        if (!hasModelWear && tableCount != 2) {
            return tuwenTemplate;
        }
        tuwenTemplate.addParams(TuwenConstants.MODEL_LIST_KEY.TEXT, "查看模特试穿");
        return tuwenTemplate;
    }

    public static TuwenTemplate makeProductInfo(Long itemId, Map<String, Object> map, List<ProductInfoCO> infoCO) {
        if (infoCO == null || infoCO.size() == 0) {
            return null;
        }
        Map maps = new HashMap();
        maps.put(TuwenConstants.PARAMS.CHILDREN_STYLE, TuwenConstants.STYLE.SEQUENCE);
        StringBuilder append = new StringBuilder().append("product_division_title");
        int i = count + 1;
        count = i;
        TuwenTemplate template = new TuwenTemplate(TuwenConstants.CONTAINER_ID.PRODUCTINFO, "native", TuwenConstants.CONTAINER.STYLE5, maps, makeChildren(makeChildren(append.append(i).toString(), "native", TuwenConstants.KEY.DIVISION_TITLE, "0xffffff", "0x051b28", "0xffffff", "商品信息")));
        Map<String, Object> params = new HashMap<>();
        params.put("data", infoCO);
        params.put(TuwenConstants.PARAMS.LOOP_STYLE, TuwenConstants.STYLE.SEQUENCE);
        params.put("maxNoMoreRows", 8);
        params.put("maxShowRows", 5);
        template.addChild(new TuwenTemplate(TuwenConstants.COMPONENT_ID.PRODUCT_INFO, "native", TuwenConstants.KEY.DETAIL_PRODUCTINFO, params, (List<TuwenTemplate>) null));
        return template;
    }

    public static TuwenTemplate makeGoodsMatching(Long itemId, Map<String, Object> map, GoodsMatchingCO co) {
        if (co == null) {
            return null;
        }
        Map maps = new HashMap();
        Map<String, Object> params = new HashMap<>();
        params.put("data", co);
        params.put(TuwenConstants.PARAMS.LOOP_STYLE, TuwenConstants.STYLE.SEQUENCE);
        maps.put(TuwenConstants.PARAMS.CHILDREN_STYLE, TuwenConstants.STYLE.SEQUENCE);
        StringBuilder append = new StringBuilder().append("product_division_title");
        int i = count + 1;
        count = i;
        TuwenTemplate tuwenTemplate = new TuwenTemplate(TuwenConstants.CONTAINER_ID.GOODSMATCHING, "native", TuwenConstants.CONTAINER.STYLE5, maps, makeChildren(makeChildren(append.append(i).toString(), "native", TuwenConstants.KEY.DIVISION_TITLE, "0xffffff", "0x051b28", "0xffffff", "搭配推荐")));
        tuwenTemplate.addChild(new TuwenTemplate(TuwenConstants.COMPONENT_ID.GOODSMATCHING, "native", TuwenConstants.KEY.DETAIL_GOODMATCHING, params, (List<TuwenTemplate>) null));
        return tuwenTemplate;
    }

    public static TuwenTemplate makePackinglist(Long itemId, Map<String, Object> map, List<PackinglistCO> listCO) {
        if (listCO == null || listCO.size() == 0) {
            return null;
        }
        Map maps = new HashMap();
        maps.put(TuwenConstants.PARAMS.CHILDREN_STYLE, TuwenConstants.STYLE.SEQUENCE);
        StringBuilder append = new StringBuilder().append("packing_division_title");
        int i = count + 1;
        count = i;
        TuwenTemplate template = new TuwenTemplate(TuwenConstants.CONTAINER_ID.PACKINGLIST, "native", TuwenConstants.CONTAINER.STYLE5, maps, makeChildren(makeChildren(append.append(i).toString(), "native", TuwenConstants.KEY.DIVISION_TITLE, "0xffffff", "0x051b28", "0xffffff", "包装清单")));
        Map<String, Object> params = new HashMap<>();
        params.put("data", listCO);
        params.put(TuwenConstants.PARAMS.LOOP_STYLE, TuwenConstants.STYLE.SEQUENCE);
        params.put("maxNoMoreRows", 8);
        params.put("maxShowRows", 5);
        template.addChild(new TuwenTemplate(TuwenConstants.COMPONENT_ID.PACKING_LIST, "native", TuwenConstants.KEY.DETAIL_PACKINGLIST, params, (List<TuwenTemplate>) null));
        return template;
    }
}
