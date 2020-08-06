package com.taobao.detail.domain.tuwen;

public class TuwenConstants {

    public interface COMPONENT_ID {
        public static final String ACTIVITY = "_SL_Activity_Component";
        public static final String COUPON = "_SL_Coupon_Component";
        public static final String GOODSMATCHING = "GoodsMatching_Component_1";
        public static final String MODELWEAR = "ModelWear_Component";
        public static final String PACKING_LIST = "PackingList_Component_1";
        public static final String PRODUCT_INFO = "ProductInfo_Component_1";
        public static final String RECOMMEND = "_SL_SellerRecommend_Component";
        public static final String RECOMMEND_2 = "_SL_SellerRecommend_Component_2";
        public static final String SEEMORE = "_SL_SeeMore_Component";
        public static final String SIZECHART = "Sizechart_Component";
    }

    public interface CONTAINER {
        public static final String STYLE1 = "detail_container_style1";
        public static final String STYLE2 = "detail_container_style2";
        public static final String STYLE3 = "detail_container_style3";
        public static final String STYLE4 = "detail_container_style4";
        public static final String STYLE5 = "detail_container_style5";
        public static final String STYLE6 = "detail_container_style6";
        public static final String STYLE7 = "detail_container_style7";
    }

    public interface CONTAINER_ID {
        public static final String ACTIVITY = "_SL_Activity";
        public static final String COUPON = "_SL_Coupon";
        public static final String GOODSMATCHING = "GoodsMatching";
        public static final String PACKINGLIST = "PackingList";
        public static final String PRODUCTINFO = "ProductInfo";
        public static final String RECOMMEND = "_SL_SellerRecommend";
        public static final String RECOMMEND_2 = "_SL_SellerRecommend_2";
        public static final String SEEMORE = "_SL_SeeMore";
        public static final String SIZECHART = "SizeChart";
    }

    public interface COUPON_TYPE {
        public static final int COUPON = 2;
        public static final int HONGBAO = 1;
    }

    public interface ITEM_AV_TYPE {
        public static final String SELLER_RECOMMEND = "SellerRecommend";
        public static final String SHOP_RECOMMEND = "ShopRecommend";
    }

    public interface KEY {
        public static final String DETAIL_COUPON = "detail_coupon";
        public static final String DETAIL_GOODMATCHING = "detail_goodsmatching";
        public static final String DETAIL_HOTAREA = "detail_hotarea";
        public static final String DETAIL_ITEMINFO = "detail_iteminfo";
        public static final String DETAIL_ITEMINFO_2 = "detail_iteminfo2";
        public static final String DETAIL_MODELWEAR = "detail_modelwear";
        public static final String DETAIL_PACKINGLIST = "detail_packinglist";
        public static final String DETAIL_PICWITHTITLE = "detail_picwithtitle";
        public static final String DETAIL_PRODUCTINFO = "detail_productinfo";
        public static final String DETAIL_SIZECHART = "detail_sizechart";
        public static final String DETAIL_SKU_BAR = "detail_sku_bar";
        public static final String DIVISION = "division";
        public static final String DIVISION_TITLE = "detail_division_title";
        public static final String OPEN_URL = "open_url";
        public static final String PIC_JUMPER = "detail_pic_jumper";
        public static final String SYS_LIST = "sys_list";
    }

    public interface MODEL_LIST_KEY {
        public static final String ACTIVITY = "activity";
        public static final String COUPON = "coupon";
        @Deprecated
        public static final String FIXED_PICTURE = "fixed_picture";
        public static final String HONGBAO = "hongbao";
        public static final String MAIN = "main";
        public static final String PICTURE = "picture";
        public static final String RECOMMEND = "recommend";
        public static final String RECOMMEND_2 = "recommend_2";
        public static final String SEE_MORE = "see_more";
        public static final String TEXT = "text";
    }

    public interface PARAMS {
        public static final String ACTIVITY_IDS = "activityIds";
        public static final String ACT_APPKEYS = "actAppkeys";
        public static final String ACT_OFFICIALTPE = "actOfficialtpe";
        public static final String ACT_PAGE_ID = "actPageId";
        public static final String ACT_PIC_URL = "actPicUrl";
        public static final String ACT_PLUGIN_TYPE = "actPluginType";
        public static final String ACT_TEMPLET_ID = "actTempletId";
        public static final String BACKGROUND_COLOR = "backgroundColor";
        public static final String CHILDREN_STYLE = "childrenStyle";
        public static final String DATA = "data";
        public static final String EXTRA_TEXT = "extraText";
        public static final String HEIGHT_RADIO = "heightRatio";
        public static final String JUMP_URL = "jumpUrl";
        public static final String LINE_COLOR = "lineColor";
        public static final String LOOP_STYLE = "loopStyle";
        public static final String MODEL_LIST = "modelList";
        public static final String MODULE_NAME = "moduleName";
        public static final String PIC_LIST = "picList";
        public static final String PIC_URL = "picUrl";
        public static final String POSITION = "position";
        public static final String REQUEST_MAP = "requestMap";
        public static final String SHOW_TITLE = "showTitle";
        public static final String SIZE = "size";
        public static final String SKU_NAME = "name";
        public static final String SKU_PATH = "path";
        public static final String SR_ITEM_IDS = "srItemIds";
        public static final String SR_ITEM_IDS_2 = "srItemIds_2";
        public static final String SR_TEMPLET_ID = "srTempletId";
        public static final String TITLE = "title";
        public static final String TITLE_COLOR = "titleColor";
        public static final String WIDTH_RADIO = "widthRatio";
    }

    public interface STYLE {
        public static final String LOOP = "loop";
        public static final String SEQUENCE = "sequence";
    }

    public interface TYPE {
        public static final String NATIVE = "native";
        public static final String PUTI = "puti";
        public static final String webView = "webView";
    }
}
