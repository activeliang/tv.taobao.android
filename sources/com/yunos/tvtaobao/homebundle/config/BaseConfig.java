package com.yunos.tvtaobao.homebundle.config;

import com.yunos.tv.core.config.Config;

public class BaseConfig {
    public static final int ACTIVITY_REQUEST_PAY_DONE_FINISH_CODE = 1;
    public static final String CART_URL = "http://ma.taobao.com/Z7OJAr";
    public static final String DOMAIN_DETAIL_DESC_DAILY = "http://10.125.16.172/cache/mtop.wdetail.getItemFullDesc/4.1/";
    public static final String DOMAIN_DETAIL_DESC_PRODUCTION = "http://hws.alicdn.com/cache/mtop.wdetail.getItemFullDesc/4.1/";
    public static final String INTENT_HOME_PAGE_URL = "page";
    public static final String INTENT_KEY_BUY_COUNT = "buyCount";
    public static final String INTENT_KEY_CARTFROM = "cartFrom";
    public static final String INTENT_KEY_CATEGORY_ID = "categoryId";
    public static final String INTENT_KEY_CATEGORY_NAME = "categoryName";
    public static final String INTENT_KEY_EXTPARAMS = "extParams";
    public static final String INTENT_KEY_FLASHSALE_ID = "qianggouId";
    public static final String INTENT_KEY_FLASHSALE_PRICE = "price";
    public static final String INTENT_KEY_FLASHSALE_STATUS = "status";
    public static final String INTENT_KEY_FLASHSALE_TIMER = "time";
    public static final String INTENT_KEY_FROM = "from";
    public static final String INTENT_KEY_ITEMID = "itemId";
    public static final String INTENT_KEY_JUID = "juId";
    public static final String INTENT_KEY_KEYWORDS = "keywords";
    public static final String INTENT_KEY_MODULE = "module";
    public static final String INTENT_KEY_MODULE_ADDRESS = "address";
    public static final String INTENT_KEY_MODULE_CART = "cart";
    public static final String INTENT_KEY_MODULE_CHAOSHI = "chaoshi";
    public static final String INTENT_KEY_MODULE_CHONGZHI = "chongzhi";
    public static final String INTENT_KEY_MODULE_COLLECTS = "collects";
    public static final String INTENT_KEY_MODULE_COMMON = "common";
    public static final String INTENT_KEY_MODULE_COUPON = "coupon";
    public static final String INTENT_KEY_MODULE_DETAIL = "detail";
    public static final String INTENT_KEY_MODULE_GOODSLIST = "goodsList";
    public static final String INTENT_KEY_MODULE_GRAPHICDETAILS = "graphicDetails";
    public static final String INTENT_KEY_MODULE_MAIN = "main";
    public static final String INTENT_KEY_MODULE_MENU = "menu";
    public static final String INTENT_KEY_MODULE_MYTAOBAO = "mytaobao";
    public static final String INTENT_KEY_MODULE_ORDERLIST = "orderList";
    public static final String INTENT_KEY_MODULE_RECOMMEND = "recommend";
    public static final String INTENT_KEY_MODULE_RELATIVE_RECOMMEND = "relative_recomment";
    public static final String INTENT_KEY_MODULE_SEARCH = "search";
    public static final String INTENT_KEY_MODULE_SHOP = "shop";
    public static final String INTENT_KEY_MODULE_SUREJOIN = "sureJoin";
    public static final String INTENT_KEY_MODULE_TODAYGOODS = "todayGoods";
    public static final String INTENT_KEY_MODULE_TOPICS = "topics";
    public static final String INTENT_KEY_MODULE_TVBUY = "tvbuy";
    public static final String INTENT_KEY_MODULE_VIDEO = "video";
    public static final String INTENT_KEY_MODULE_WORKSHOP = "workshop";
    public static final String INTENT_KEY_PRICE = "price";
    public static final String INTENT_KEY_REQUEST_SKUID = "skuId";
    public static final String INTENT_KEY_REQUEST_TYPE = "type";
    public static final String INTENT_KEY_REQUEST_URL = "url";
    public static final String INTENT_KEY_SEARCH_TAB = "tab";
    public static final String INTENT_KEY_SKUID = "skuId";
    public static final String INTENT_KEY_SKU_TYPE = "type";
    public static final boolean IS_DEBUG = Config.isDebug();
    public static final String LOADING_CACHE_DIR = "loading";
    public static final String LOADING_CACHE_JSON = "loading_cache_json";
    public static final String ORDER_FROM_CART = "cart";
    public static final String ORDER_FROM_ITEM = "item";
    public static final String PAGE_GUIDE_VERSION = "page_guide_version";
    public static final String SELLER_NUMID = "sellerId";
    public static final String SELLER_SHOPID = "shopId";
    public static final String SELLER_TAOBAO = "C";
    public static final String SELLER_TMALL = "B";
    public static final String SELLER_TYPE = "type";
    public static final String SHOP_FROM = "shopFrom";
    public static final int forceLoginRequestCode = 1002;
    public static final int loginRequestCode = 1001;

    private static final class ImageServer {
        public static final String[] DALIY = {"http://img01.daily.taobaocdn.net/bao/uploaded/", "http://img02.daily.taobaocdn.net/bao/uploaded/", "http://img03.daily.taobaocdn.net/bao/uploaded/"};
        public static final String[] PREDEPLOY = {"http://img01.taobaocdn.com/bao/uploaded/", "http://img02.taobaocdn.com/bao/uploaded/", "http://img03.taobaocdn.com/bao/uploaded/", "http://img04.taobaocdn.com/bao/uploaded/"};
        public static final String[] PRODUCTION = {"http://img01.taobaocdn.com/bao/uploaded/", "http://img02.taobaocdn.com/bao/uploaded/", "http://img03.taobaocdn.com/bao/uploaded/", "http://img04.taobaocdn.com/bao/uploaded/"};

        private ImageServer() {
        }
    }
}
