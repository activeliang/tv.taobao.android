package com.yunos.tvtaobao.payment.request;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.yunos.tvtaobao.payment.PaymentApplication;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobalConfig {
    @JSONField(name = "afp")
    private AfpBean afp;
    @JSONField(name = "no_agreement_channel")
    public String[] agreementChannelBlockList;
    @JSONField(name = "no_agreement_model")
    public String[] agreementModelBlockList;
    @JSONField(name = "beta")
    public Beta beta;
    @JSONField(name = "blockWhParams")
    private boolean blockWhParams = false;
    @JSONField(name = "channelDegradeConfigs")
    public ChannelDegradeCfg[] channelDegradeConfigs;
    @JSONField(name = "coupon")
    public Coupon coupon;
    @JSONField(name = "custom_launcher_screen")
    private CustomLauncherScreenBean custom_launcher_screen;
    @JSONField(name = "detail")
    public Detail detail;
    @JSONField(name = "detail_goods_info")
    public DetailGoodsInfo detail_goods_info;
    @JSONField(name = "doNotUseSkuActivity2")
    public String doNotUseSkuActivity2;
    @JSONField(name = "double11_shop_cart")
    private Double11ShopCart double11ShopCart;
    @JSONField(name = "error_report")
    private String errorReportRate;
    @JSONField(name = "handle_trade")
    private boolean handleTradeException = false;
    @JSONField(name = "hideTaobaoTVRebate")
    public String hideTaobaoTVRebate;
    @JSONField(name = "isBkbmUT19999On")
    private boolean isBkbmUT19999On;
    @JSONField(name = "isBlitzShop")
    private boolean isBlitzShop;
    @JSONField(name = "isDisasterToleranceOn")
    private boolean isDisasterToleranceOn = false;
    @JSONField(name = "isLianMengLogOn")
    private boolean isLianMengLogOn;
    @JSONField(name = "isMoHeLogOn")
    private boolean isMoHeLogOn;
    @JSONField(name = "isYiTiJiLogOn")
    private boolean isYiTiJiLogOn;
    @JSONField(name = "jhs")
    private String jhsConfig;
    @JSONField(name = "live_settings")
    public LiveSettings liveSettings;
    @JSONField(name = "low_memory_img_show_degrade")
    public int low_memory_img_show_degrade = -1;
    @JSONField(name = "low_memory_page_show_degrade")
    public int low_memory_page_show_degrade = -1;
    @JSONField(name = "low_memory_page_stack_degrade")
    public int low_memory_page_stack_degrade = -1;
    @JSONField(name = "low_memory_widget_degrade")
    public int low_memory_widget_degrade = -1;
    @JSONField(name = "double11_action")
    private Double11Action mDouble11Action;
    @JSONField(name = "shop_cart_flag")
    public ShopCartFlag mShopCartFlag;
    @JSONField(name = "tmall_live")
    public TMallLive mTMallLive;
    @JSONField(name = "upgrade")
    private Upgrade mUpgrade;
    @JSONField(name = "mashangtao")
    public String mashangtao;
    @JSONField(name = "stopUpdateUserAccount")
    private boolean stopUpdateUserAccount;
    @JSONField(name = "switchTaobaoTVFlow")
    public String switchTaobaoTVFlow;
    @JSONField(name = "taobaoPay")
    public boolean taobaoPay;
    @JSONField(name = "taobaotv_hongbaoyu_cfg")
    public TaobaoTvHongbaoyuCfg taobaoTvHongbaoyuCfg = new TaobaoTvHongbaoyuCfg();
    @JSONField(name = "taobaotv_use_origin_url")
    public String taobaotvUseOriginUrl;
    @JSONField(name = "trade_degrade")
    private boolean tradeDegrade;
    @JSONField(name = "tradeUltronPatch")
    private JSONObject tradeUltronPatch;
    @JSONField(name = "tvacr")
    public TvACR tvacr;
    @JSONField(name = "waimai")
    private Waimai waimai;
    @JSONField(name = "zhitongche")
    private ZhitongcheConfig ztcConfig;

    public static class Beta {
        @JSONField(name = "version")
        public String[] versions;
    }

    public static class LiveSettings {
        @JSONField(name = "channels")
        public List<KVItem> channels;
        @JSONField(name = "models")
        public List<KVItem> models;

        public static class KVItem {
            @JSONField(name = "name")
            public String name;
            @JSONField(name = "type")
            public int value = 1;
        }
    }

    public String getHideTaobaoTVRebate() {
        return this.hideTaobaoTVRebate;
    }

    public void setHideTaobaoTVRebate(String hideTaobaoTVRebate2) {
        this.hideTaobaoTVRebate = hideTaobaoTVRebate2;
    }

    public String getSwitchTaobaoTVFlow() {
        return this.switchTaobaoTVFlow;
    }

    public void setSwitchTaobaoTVFlow(String switchTaobaoTVFlow2) {
        this.switchTaobaoTVFlow = switchTaobaoTVFlow2;
    }

    public Waimai getWaimai() {
        return this.waimai;
    }

    public void setWaimai(Waimai waimai2) {
        this.waimai = waimai2;
    }

    public Detail getDetail() {
        return this.detail;
    }

    public void setDetail(Detail detail2) {
        this.detail = detail2;
    }

    public ZhitongcheConfig getZtcConfig() {
        return this.ztcConfig;
    }

    public void setZtcConfig(ZhitongcheConfig ztcConfig2) {
        this.ztcConfig = ztcConfig2;
    }

    public Coupon getCoupon() {
        return this.coupon;
    }

    public String getJhsConfig() {
        return this.jhsConfig;
    }

    public void setJhsConfig(String jhsConfig2) {
        this.jhsConfig = jhsConfig2;
    }

    public String getDoNotUseSkuActivity2() {
        return this.doNotUseSkuActivity2;
    }

    public void setDoNotUseSkuActivity2(String doNotUseSkuActivity22) {
        this.doNotUseSkuActivity2 = doNotUseSkuActivity22;
    }

    public String getTaobaotvUseOriginUrl() {
        return this.taobaotvUseOriginUrl;
    }

    public void setTaobaotvUseOriginUrl(String taobaotvUseOriginUrl2) {
        this.taobaotvUseOriginUrl = taobaotvUseOriginUrl2;
    }

    public TaobaoTvHongbaoyuCfg getTaobaoTvHongbaoyuCfg() {
        return this.taobaoTvHongbaoyuCfg;
    }

    public void setTaobaoTvHongbaoyuCfg(TaobaoTvHongbaoyuCfg taobaoTvHongbaoyuCfg2) {
        this.taobaoTvHongbaoyuCfg = taobaoTvHongbaoyuCfg2;
    }

    public String getMashangtao() {
        return this.mashangtao;
    }

    public void setMashangtao(String mashangtao2) {
        this.mashangtao = mashangtao2;
    }

    public DetailGoodsInfo getDetail_goods_info() {
        return this.detail_goods_info;
    }

    public void setDetail_goods_info(DetailGoodsInfo detail_goods_info2) {
        this.detail_goods_info = detail_goods_info2;
    }

    public boolean isStopUpdateUserAccount() {
        return this.stopUpdateUserAccount;
    }

    public void setStopUpdateUserAccount(boolean stopUpdateUserAccount2) {
        this.stopUpdateUserAccount = stopUpdateUserAccount2;
    }

    public String getErrorReportRate() {
        return this.errorReportRate;
    }

    public void setErrorReportRate(String errorReportRate2) {
        this.errorReportRate = errorReportRate2;
    }

    public boolean isBlockWhParams() {
        return this.blockWhParams;
    }

    public void setBlockWhParams(boolean blockWhParams2) {
        this.blockWhParams = blockWhParams2;
    }

    public TvACR getTvacr() {
        return this.tvacr;
    }

    public void setTvacr(TvACR tvacr2) {
        this.tvacr = tvacr2;
    }

    public ShopCartFlag getShopCartFlag() {
        return this.mShopCartFlag;
    }

    public void setShopCartFlag(ShopCartFlag shopCartFlag) {
        this.mShopCartFlag = shopCartFlag;
    }

    public Upgrade getUpgrade() {
        return this.mUpgrade;
    }

    public void setDouble11ShopCart(Double11ShopCart double11ShopCart2) {
        this.double11ShopCart = double11ShopCart2;
    }

    public Double11ShopCart getDouble11ShopCart() {
        return this.double11ShopCart;
    }

    public void setUpgrade(Upgrade upgrade) {
        this.mUpgrade = upgrade;
    }

    public Double11Action getDouble11Action() {
        return this.mDouble11Action;
    }

    public void setDouble11Action(Double11Action double11Action) {
        this.mDouble11Action = double11Action;
    }

    public TMallLive getTMallLive() {
        return this.mTMallLive;
    }

    public void setmTMallLive(TMallLive tMallLive) {
        this.mTMallLive = tMallLive;
    }

    public AfpBean getAfp() {
        return this.afp;
    }

    public void setAfp(AfpBean afp2) {
        this.afp = afp2;
    }

    public CustomLauncherScreenBean getCustom_launcher_screen() {
        return this.custom_launcher_screen;
    }

    public void setCustom_launcher_screen(CustomLauncherScreenBean custom_launcher_screen2) {
        this.custom_launcher_screen = custom_launcher_screen2;
    }

    public boolean isBlitzShop() {
        return this.isBlitzShop;
    }

    public void setBlitzShop(boolean blitzShop) {
        this.isBlitzShop = blitzShop;
    }

    public boolean isMoHeLogOn() {
        return this.isMoHeLogOn;
    }

    public void setMoHeLogOn(boolean moHeLogOn) {
        this.isMoHeLogOn = moHeLogOn;
    }

    public boolean isLianMengLogOn() {
        return this.isLianMengLogOn;
    }

    public void setLianMengLogOn(boolean lianMengLogOn) {
        this.isLianMengLogOn = lianMengLogOn;
    }

    public boolean isYiTiJiLogOn() {
        return this.isYiTiJiLogOn;
    }

    public void setYiTiJiLogOn(boolean yiTiJiLogOn) {
        this.isYiTiJiLogOn = yiTiJiLogOn;
    }

    public int getLow_memory_page_show_degrade() {
        return this.low_memory_page_show_degrade;
    }

    public void setLow_memory_page_show_degrade(int low_memory_page_show_degrade2) {
        this.low_memory_page_show_degrade = low_memory_page_show_degrade2;
    }

    public int getLow_memory_page_stack_degrade() {
        return this.low_memory_page_stack_degrade;
    }

    public void setLow_memory_page_stack_degrade(int low_memory_page_stack_degrade2) {
        this.low_memory_page_stack_degrade = low_memory_page_stack_degrade2;
    }

    public int getLow_memory_img_show_degrade() {
        return this.low_memory_img_show_degrade;
    }

    public void setLow_memory_img_show_degrade(int low_memory_img_show_degrade2) {
        this.low_memory_img_show_degrade = low_memory_img_show_degrade2;
    }

    public int getLow_memory_widget_degrade() {
        return this.low_memory_widget_degrade;
    }

    public void setLow_memory_widget_degrade(int low_memory_widget_degrade2) {
        this.low_memory_widget_degrade = low_memory_widget_degrade2;
    }

    public boolean isDisasterToleranceOn() {
        return this.isDisasterToleranceOn;
    }

    public void setDisasterToleranceOn(boolean disasterToleranceOn) {
        this.isDisasterToleranceOn = disasterToleranceOn;
    }

    public boolean isBkbmUT19999On() {
        return this.isBkbmUT19999On;
    }

    public void setBkbmUT19999On(boolean bkbmUT19999On) {
        this.isBkbmUT19999On = bkbmUT19999On;
    }

    public ChannelDegradeCfg[] getChannelDegradeConfigs() {
        return this.channelDegradeConfigs;
    }

    public void setChannelDegradeConfigs(ChannelDegradeCfg[] channelDegradeConfigs2) {
        this.channelDegradeConfigs = channelDegradeConfigs2;
    }

    public boolean isTradeDegrade() {
        return this.tradeDegrade;
    }

    public void setTradeDegrade(boolean tradeDegrade2) {
        this.tradeDegrade = tradeDegrade2;
    }

    public void setHandleTradeException(boolean handleTradeException2) {
        this.handleTradeException = handleTradeException2;
    }

    public boolean isHandleTradeException() {
        return this.handleTradeException;
    }

    public static class Double11ShopCart {
        @JSONField(name = "bool_shop_cart_merge_orders")
        private boolean boolShopCartMergeOrders;

        public boolean isBoolShopCartMergeOrders() {
            return this.boolShopCartMergeOrders;
        }

        public void setBoolShopCartMergeOrders(boolean boolShopCartMergeOrders2) {
            this.boolShopCartMergeOrders = boolShopCartMergeOrders2;
        }
    }

    public static class Upgrade {
        private String background;
        private String content;
        private String text;
        private String title;

        public String getText() {
            return this.text;
        }

        public void setText(String text2) {
            this.text = text2;
        }

        public String getBackground() {
            return this.background;
        }

        public void setBackground(String background2) {
            this.background = background2;
        }

        public String getContent() {
            return this.content;
        }

        public void setContent(String content2) {
            this.content = content2;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }
    }

    public static class ShopCartFlag {
        private String detailIcon;
        private boolean isActing;
        @JSONField(name = "new_shop_cat_icon")
        private String newShopCatIcon;
        private String shopCatIcon;
        private String sideBarIcon;

        public boolean isActing() {
            return this.isActing;
        }

        public void setActing(boolean acting) {
            this.isActing = acting;
        }

        public String getDetailIcon() {
            return this.detailIcon;
        }

        public void setDetailIcon(String detailIcon2) {
            this.detailIcon = detailIcon2;
        }

        public String getShopCatIcon() {
            return this.shopCatIcon;
        }

        public void setShopCatIcon(String shopCatIcon2) {
            this.shopCatIcon = shopCatIcon2;
        }

        public String getNewShopCatIcon() {
            return this.newShopCatIcon;
        }

        public void setNewShopCatIcon(String newShopCatIcon2) {
            this.newShopCatIcon = newShopCatIcon2;
        }

        public String getSideBarIcon() {
            return this.sideBarIcon;
        }

        public void setSideBarIcon(String sideBarIcon2) {
            this.sideBarIcon = sideBarIcon2;
        }
    }

    public static class Double11Action {
        @JSONField(name = "is_acting")
        private boolean isActing;
        @JSONField(name = "step")
        private String step;
        @JSONField(name = "subtitle")
        private String subtitle;

        public void setStep(String step2) {
            this.step = step2;
        }

        public String getStep() {
            return this.step;
        }

        public boolean isActing() {
            return this.isActing;
        }

        public void setActing(boolean acting) {
            this.isActing = acting;
        }

        public String getSubtitle() {
            return this.subtitle;
        }

        public void setSubtitle(String subtitle2) {
            this.subtitle = subtitle2;
        }
    }

    public static class TMallLive {
        private ArrayList<Integer> coupon_offset;
        private String is_online;
        private String live_login_prompt;
        private String live_unlogin_prompt;
        private String postfix;
        private String red_envelopes_time;

        public String postfix() {
            return this.postfix;
        }

        public void setPostfix(String postfix2) {
            this.postfix = postfix2;
        }

        public String getIs_online() {
            return this.is_online;
        }

        public void setIs_online(String is_online2) {
            this.is_online = is_online2;
        }

        public String getLive_login_prompt() {
            return this.live_login_prompt;
        }

        public void setLive_login_prompt(String live_login_prompt2) {
            this.live_login_prompt = live_login_prompt2;
        }

        public String getLive_unlogin_prompt() {
            return this.live_unlogin_prompt;
        }

        public void setLive_unlogin_prompt(String live_unlogin_prompt2) {
            this.live_unlogin_prompt = live_unlogin_prompt2;
        }

        public String getRed_envelopes_time() {
            return this.red_envelopes_time;
        }

        public void setRed_envelopes_time(String red_envelopes_time2) {
            this.red_envelopes_time = red_envelopes_time2;
        }

        public ArrayList<Integer> getCoupon_offset() {
            return this.coupon_offset;
        }

        public void setCoupon_offset(ArrayList<Integer> coupon_offset2) {
            this.coupon_offset = coupon_offset2;
        }
    }

    public static class Coupon {
        @JSONField(name = "is_show_h5coupon")
        private boolean showH5Coupon = false;
        @JSONField(name = "taobao_coupon")
        private boolean taobaoCoupon = false;

        public void setShowH5Coupon(boolean showH5Coupon2) {
            this.showH5Coupon = showH5Coupon2;
        }

        public void setTaobaoCoupon(boolean taobaoCoupon2) {
            this.taobaoCoupon = taobaoCoupon2;
        }

        public boolean isShowH5Coupon() {
            return this.showH5Coupon;
        }

        public boolean supportTaobaoCoupon() {
            return this.taobaoCoupon;
        }
    }

    public String toString() {
        return "GlobalConfig{double11ShopCart=" + this.double11ShopCart + ", mUpgrade=" + this.mUpgrade + ", errorReportRate='" + this.errorReportRate + '\'' + ", tradeDegrade=" + this.tradeDegrade + ", handleTradeException=" + this.handleTradeException + ", mDouble11Action=" + this.mDouble11Action + ", mShopCartFlag=" + this.mShopCartFlag + ", isBlitzShop=" + this.isBlitzShop + ", isMoHeLogOn=" + this.isMoHeLogOn + ", isLianMengLogOn=" + this.isLianMengLogOn + ", isYiTiJiLogOn=" + this.isYiTiJiLogOn + ", low_memory_page_show_degrade=" + this.low_memory_page_show_degrade + ", low_memory_page_stack_degrade=" + this.low_memory_page_stack_degrade + ", low_memory_img_show_degrade=" + this.low_memory_img_show_degrade + ", low_memory_widget_degrade=" + this.low_memory_widget_degrade + ", channelDegradeConfigs=" + Arrays.toString(this.channelDegradeConfigs) + ", isDisasterToleranceOn=" + this.isDisasterToleranceOn + ", isBkbmUT19999On=" + this.isBkbmUT19999On + ", mTMallLive=" + this.mTMallLive + ", afp=" + this.afp + ", tradeUltronPatch=" + this.tradeUltronPatch + ", custom_launcher_screen=" + this.custom_launcher_screen + ", waimai=" + this.waimai + ", taobaoPay=" + this.taobaoPay + ", switchTaobaoTVFlow='" + this.switchTaobaoTVFlow + '\'' + ", hideTaobaoTVRebate='" + this.hideTaobaoTVRebate + '\'' + ", stopUpdateUserAccount=" + this.stopUpdateUserAccount + ", detail=" + this.detail + ", ztcConfig=" + this.ztcConfig + ", coupon=" + this.coupon + ", tvacr=" + this.tvacr + ", beta=" + this.beta + ", detail_goods_info=" + this.detail_goods_info + ", agreementModelBlockList=" + Arrays.toString(this.agreementModelBlockList) + ", agreementChannelBlockList=" + Arrays.toString(this.agreementChannelBlockList) + ", jhsConfig='" + this.jhsConfig + '\'' + ", mashangtao='" + this.mashangtao + '\'' + ", taobaoTvHongbaoyuCfg=" + this.taobaoTvHongbaoyuCfg + '}';
    }

    public JSONObject getTradeUltronPatch() {
        return this.tradeUltronPatch;
    }

    public void setTradeUltronPatch(JSONObject tradeUltronPatch2) {
        this.tradeUltronPatch = tradeUltronPatch2;
    }

    public static class Detail {
        @JSONField(name = "handle_detail")
        private boolean handleDetailException = false;
        @JSONField(name = "handle_fulldesc")
        private boolean handleFullDescException = false;
        private PresellBean presell;

        public PresellBean getPresell() {
            return this.presell;
        }

        public void setPresell(PresellBean presell2) {
            this.presell = presell2;
        }

        public boolean isHandleDetailException() {
            return this.handleDetailException;
        }

        public void setHandleDetailException(boolean handleException) {
            this.handleDetailException = handleException;
        }

        public void setHandleFullDescException(boolean handleFullDescException2) {
            this.handleFullDescException = handleFullDescException2;
        }

        public boolean isHandleFullDescException() {
            return this.handleFullDescException;
        }

        public static class PresellBean {
            private String icon;

            public String getIcon() {
                return this.icon;
            }

            public void setIcon(String icon2) {
                this.icon = icon2;
            }
        }
    }

    public static class Waimai {
        @JSONField(name = "taobaoPay")
        private boolean taobaoPay = false;

        public void setTaobaoPay(boolean pay) {
            this.taobaoPay = pay;
        }

        public boolean isTaobaoPay() {
            return this.taobaoPay;
        }
    }

    public static class AfpBean {
        private boolean isbackhome;
        private ScreenBean screen;

        public boolean isIsbackhome() {
            return this.isbackhome;
        }

        public void setIsbackhome(boolean isbackhome2) {
            this.isbackhome = isbackhome2;
        }

        public ScreenBean getScreen() {
            return this.screen;
        }

        public void setScreen(ScreenBean screen2) {
            this.screen = screen2;
        }

        public static class ScreenBean {
            private int timeout;

            public int getTimeout() {
                return this.timeout;
            }

            public void setTimeout(int timeout2) {
                this.timeout = timeout2;
            }
        }
    }

    public static class CustomLauncherScreenBean {
        private boolean show_loading;

        public boolean isShowLoading() {
            return this.show_loading;
        }

        public void setIsShowLoading(boolean show_loading2) {
            this.show_loading = show_loading2;
        }
    }

    public static class TvACR {
        private boolean acrShow;
        private boolean isShow;

        public boolean isShow() {
            return this.isShow;
        }

        public void setShow(boolean show) {
            this.isShow = show;
        }

        public boolean isAcrShow() {
            return this.acrShow;
        }

        public void setAcrShow(boolean acrShow2) {
            this.acrShow = acrShow2;
        }
    }

    public static class ZhitongcheConfig {
        @JSONField(name = "black_list")
        public String[] blackList;
        @JSONField(name = "flag")
        public String flag = null;
        @JSONField(name = "ztc_icon")
        public String icon_url = null;
        @JSONField(name = "is_show")
        public boolean is_show = false;
        @JSONField(name = "s_request")
        public boolean singleRequest = false;

        public void setSingleRequest(boolean singleRequest2) {
            this.singleRequest = singleRequest2;
        }

        public boolean isSingleRequest() {
            return this.singleRequest;
        }

        public String[] getBlackList() {
            return this.blackList;
        }

        public void setBlackList(String[] blackList2) {
            this.blackList = blackList2;
        }

        public String getFlag() {
            return this.flag;
        }

        public void setFlag(String flag2) {
            this.flag = flag2;
        }

        public boolean is_show() {
            return this.is_show;
        }

        public void setIs_show(boolean is_show2) {
            this.is_show = is_show2;
        }

        public String getIcon_url() {
            return this.icon_url;
        }

        public void setIcon_url(String icon_url2) {
            this.icon_url = icon_url2;
        }
    }

    public boolean isBeta() {
        if (this.beta == null || this.beta.versions == null) {
            return false;
        }
        String versionName = "";
        try {
            versionName = PaymentApplication.getApplication().getPackageManager().getPackageInfo(PaymentApplication.getApplication().getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String version : this.beta.versions) {
            if (versionName.equals(version)) {
                return true;
            }
        }
        return false;
    }

    public static class DetailGoodsInfo {
        @JSONField(name = "detail_panel")
        private String detailPanel;
        @JSONField(name = "detail_share")
        private String detailShare;
        @JSONField(name = "detail_top_button")
        private String detailTopButton;
        private Boolean is_show_all = false;
        private Boolean is_show_all_price = false;

        public Boolean getIs_show_all_price() {
            return this.is_show_all_price;
        }

        public void setIs_show_all_price(Boolean showAllPrice) {
            this.is_show_all_price = showAllPrice;
        }

        public Boolean getIs_show_all() {
            return this.is_show_all;
        }

        public void setIs_show_all(Boolean showAll) {
            this.is_show_all = showAll;
        }

        public String getDetailPanel() {
            return this.detailPanel;
        }

        public void setDetailPanel(String detailPanel2) {
            this.detailPanel = detailPanel2;
        }

        public String getDetailTopButton() {
            return this.detailTopButton;
        }

        public void setDetailTopButton(String detailTopButton2) {
            this.detailTopButton = detailTopButton2;
        }

        public String getDetailShare() {
            return this.detailShare;
        }

        public void setDetailShare(String detailShare2) {
            this.detailShare = detailShare2;
        }
    }

    public static class ChannelDegradeCfg {
        public String channelId;
        public int img_show_degrade = -1;
        public int page_show_degrade = -1;
        public int page_stack_degrade = -1;
        public int widget_degrade = -1;

        public String toString() {
            return "ChannelDegradeCfg{page_show_degrade=" + this.page_show_degrade + ", page_stack_degrade=" + this.page_stack_degrade + ", img_show_degrade=" + this.img_show_degrade + ", widget_degrade=" + this.widget_degrade + ", channelId='" + this.channelId + '\'' + '}';
        }

        public int getPage_show_degrade() {
            return this.page_show_degrade;
        }

        public void setPage_show_degrade(int page_show_degrade2) {
            this.page_show_degrade = page_show_degrade2;
        }

        public int getPage_stack_degrade() {
            return this.page_stack_degrade;
        }

        public void setPage_stack_degrade(int page_stack_degrade2) {
            this.page_stack_degrade = page_stack_degrade2;
        }

        public int getImg_show_degrade() {
            return this.img_show_degrade;
        }

        public void setImg_show_degrade(int img_show_degrade2) {
            this.img_show_degrade = img_show_degrade2;
        }

        public int getWidget_degrade() {
            return this.widget_degrade;
        }

        public void setWidget_degrade(int widget_degrade2) {
            this.widget_degrade = widget_degrade2;
        }

        public String getChannelId() {
            return this.channelId;
        }

        public void setChannelId(String channelId2) {
            this.channelId = channelId2;
        }
    }

    public static class TaobaoTvHongbaoyuCfg {
        String goonWatchingBtnImgUrl = "https://gw.alicdn.com/tfs/TB1iHXBirH1gK0jSZFwXXc7aXXa-372-144.png";
        String[] hongbaoyuEffectLayer1 = {"https://gw.alicdn.com/tfs/TB12LOuiEz1gK0jSZLeXXb9kVXa-96-96.png", "https://gw.alicdn.com/tfs/TB1oaWziEY1gK0jSZFCXXcwqXXa-102-102.png"};
        String[] hongbaoyuEffectLayer2 = {"https://gw.alicdn.com/tfs/TB13L9vixn1gK0jSZKPXXXvUXXa-240-126.png"};
        String[] hongbaoyuEffectLayer3 = {"https://gw.alicdn.com/tfs/TB1xDY0hKbviK0jSZFNXXaApXXa-60-60.png", "https://gw.alicdn.com/tfs/TB10bWuiuL2gK0jSZPhXXahvXXa-60-60.png", "https://gw.alicdn.com/tfs/TB1ffWyiBr0gK0jSZFnXXbRRXXa-60-60.png"};
        String[] hongbaoyuEffectLayer4 = {"https://gw.alicdn.com/tfs/TB1UXqviBv0gK0jSZKbXXbK2FXa-636-480.png"};
        String hongbaoyuFullBgImgUrl = "https://gw.alicdn.com/tfs/TB1jbSqiuT2gK0jSZFvXXXnFXXa-108-108.png";
        String hongbaoyuFullCoverImgUrl = "https://gw.alicdn.com/tfs/TB1UWmvirj1gK0jSZFuXXcrHpXa-108-39.png";
        String hongbaoyuFullInnerImgUrl = "https://gw.alicdn.com/tfs/TB1OYCsiCf2gK0jSZFPXXXsopXa-54-72.png";
        String hongbaoyuInflateingImgUrl = "https://gw.alicdn.com/tfs/TB1BJ9wirj1gK0jSZFOXXc7GpXa-108-108.png";
        String hongbaoyuOverImgUrl = "https://gw.alicdn.com/tfs/TB14Vqtiy_1gK0jSZFqXXcpaXXa-108-108.png";
        String loseAuraImgUrl = "https://gw.alicdn.com/tfs/TB10g1ChKP2gK0jSZFoXXauIVXa-960-816.png";
        String openBtnImgUrl = "https://gw.alicdn.com/tfs/TB1qtuChKL2gK0jSZFmXXc7iXXa-372-144.png";
        String rltChaozhiyouhuiquanImgUrl = "https://gw.alicdn.com/tfs/TB105SyhUz1gK0jSZLeXXb9kVXa-288-240.png";
        String rltMisteryImgUrl = "https://gw.alicdn.com/tfs/TB1I3azhUT1gK0jSZFrXXcNCXXa-288-240.png";
        String rltShuang11jintieImgUrl = "https://gw.alicdn.com/tfs/TB1s9exhKT2gK0jSZFvXXXnFXXa-288-240.png";
        String rltTvtaobaoHongbaoImgUrl = "https://gw.alicdn.com/tfs/TB1A5WyhUz1gK0jSZLeXXb9kVXa-288-240.png";
        String rltTvtaobaoJifenImgUrl = "https://gw.alicdn.com/tfs/TB1pSyDhG61gK0jSZFlXXXDKFXa-288-240.png";
        String rltUnluckyImgUrl = "https://gw.alicdn.com/tfs/TB1UOyChKH2gK0jSZJnXXaT1FXa-288-240.png";
        String txtLuckyMainTxt = "恭喜你获得";
        String txtTip4Coupon = "已放入卡券包";
        String txtTip4Fanlika = "下单自动返利";
        String txtTip4Hongbao = "下单自动抵扣";
        String txtTip4Jifen = "我的积分查看";
        String txtTip4Jintie = "双11电视下单享用";
        String txtTip4UnHandledType = "";
        String txtTip4Unloginlucky = "登录即可领取";
        String txtTip4Unlucky = "不骄不躁，再来一次";
        String txtUnLuckyMainTxt = "好可惜";
        String txtUnLuckySubTxt = "奖励与你擦肩而过，再接再厉";
        String txtUnloginLuckyMainTxt = "你获得了";
        String txtUnloginLuckySubTxt = "神秘礼盒";
        String winAuraImgUrl = "https://gw.alicdn.com/tfs/TB158KyhND1gK0jSZFsXXbldVXa-960-816.png";

        public String getWinAuraImgUrl() {
            return this.winAuraImgUrl;
        }

        public void setWinAuraImgUrl(String winAuraImgUrl2) {
            this.winAuraImgUrl = winAuraImgUrl2;
        }

        public String getLoseAuraImgUrl() {
            return this.loseAuraImgUrl;
        }

        public void setLoseAuraImgUrl(String loseAuraImgUrl2) {
            this.loseAuraImgUrl = loseAuraImgUrl2;
        }

        public String getOpenBtnImgUrl() {
            return this.openBtnImgUrl;
        }

        public void setOpenBtnImgUrl(String openBtnImgUrl2) {
            this.openBtnImgUrl = openBtnImgUrl2;
        }

        public String getGoonWatchingBtnImgUrl() {
            return this.goonWatchingBtnImgUrl;
        }

        public void setGoonWatchingBtnImgUrl(String goonWatchingBtnImgUrl2) {
            this.goonWatchingBtnImgUrl = goonWatchingBtnImgUrl2;
        }

        public String getRltUnluckyImgUrl() {
            return this.rltUnluckyImgUrl;
        }

        public void setRltUnluckyImgUrl(String rltUnluckyImgUrl2) {
            this.rltUnluckyImgUrl = rltUnluckyImgUrl2;
        }

        public String getRltMisteryImgUrl() {
            return this.rltMisteryImgUrl;
        }

        public void setRltMisteryImgUrl(String rltMisteryImgUrl2) {
            this.rltMisteryImgUrl = rltMisteryImgUrl2;
        }

        public String getRltTvtaobaoJifenImgUrl() {
            return this.rltTvtaobaoJifenImgUrl;
        }

        public void setRltTvtaobaoJifenImgUrl(String rltTvtaobaoJifenImgUrl2) {
            this.rltTvtaobaoJifenImgUrl = rltTvtaobaoJifenImgUrl2;
        }

        public String getRltTvtaobaoHongbaoImgUrl() {
            return this.rltTvtaobaoHongbaoImgUrl;
        }

        public void setRltTvtaobaoHongbaoImgUrl(String rltTvtaobaoHongbaoImgUrl2) {
            this.rltTvtaobaoHongbaoImgUrl = rltTvtaobaoHongbaoImgUrl2;
        }

        public String getRltShuang11jintieImgUrl() {
            return this.rltShuang11jintieImgUrl;
        }

        public void setRltShuang11jintieImgUrl(String rltShuang11jintieImgUrl2) {
            this.rltShuang11jintieImgUrl = rltShuang11jintieImgUrl2;
        }

        public String getRltChaozhiyouhuiquanImgUrl() {
            return this.rltChaozhiyouhuiquanImgUrl;
        }

        public void setRltChaozhiyouhuiquanImgUrl(String rltChaozhiyouhuiquanImgUrl2) {
            this.rltChaozhiyouhuiquanImgUrl = rltChaozhiyouhuiquanImgUrl2;
        }

        public String getTxtUnLuckyMainTxt() {
            return this.txtUnLuckyMainTxt;
        }

        public void setTxtUnLuckyMainTxt(String txtUnLuckyMainTxt2) {
            this.txtUnLuckyMainTxt = txtUnLuckyMainTxt2;
        }

        public String getTxtUnLuckySubTxt() {
            return this.txtUnLuckySubTxt;
        }

        public void setTxtUnLuckySubTxt(String txtUnLuckySubTxt2) {
            this.txtUnLuckySubTxt = txtUnLuckySubTxt2;
        }

        public String getTxtLuckyMainTxt() {
            return this.txtLuckyMainTxt;
        }

        public void setTxtLuckyMainTxt(String txtLuckyMainTxt2) {
            this.txtLuckyMainTxt = txtLuckyMainTxt2;
        }

        public String getTxtTip4Jifen() {
            return this.txtTip4Jifen;
        }

        public void setTxtTip4Jifen(String txtTip4Jifen2) {
            this.txtTip4Jifen = txtTip4Jifen2;
        }

        public String getTxtTip4Hongbao() {
            return this.txtTip4Hongbao;
        }

        public void setTxtTip4Hongbao(String txtTip4Hongbao2) {
            this.txtTip4Hongbao = txtTip4Hongbao2;
        }

        public String getTxtTip4Coupon() {
            return this.txtTip4Coupon;
        }

        public void setTxtTip4Coupon(String txtTip4Coupon2) {
            this.txtTip4Coupon = txtTip4Coupon2;
        }

        public String getTxtTip4Fanlika() {
            return this.txtTip4Fanlika;
        }

        public void setTxtTip4Fanlika(String txtTip4Fanlika2) {
            this.txtTip4Fanlika = txtTip4Fanlika2;
        }

        public String getTxtTip4Jintie() {
            return this.txtTip4Jintie;
        }

        public void setTxtTip4Jintie(String txtTip4Jintie2) {
            this.txtTip4Jintie = txtTip4Jintie2;
        }

        public String getTxtUnloginLuckyMainTxt() {
            return this.txtUnloginLuckyMainTxt;
        }

        public void setTxtUnloginLuckyMainTxt(String txtUnloginLuckyMainTxt2) {
            this.txtUnloginLuckyMainTxt = txtUnloginLuckyMainTxt2;
        }

        public String getTxtUnloginLuckySubTxt() {
            return this.txtUnloginLuckySubTxt;
        }

        public void setTxtUnloginLuckySubTxt(String txtUnloginLuckySubTxt2) {
            this.txtUnloginLuckySubTxt = txtUnloginLuckySubTxt2;
        }

        public String getTxtTip4Unlucky() {
            return this.txtTip4Unlucky;
        }

        public void setTxtTip4Unlucky(String txtTip4Unlucky2) {
            this.txtTip4Unlucky = txtTip4Unlucky2;
        }

        public String getTxtTip4Unloginlucky() {
            return this.txtTip4Unloginlucky;
        }

        public void setTxtTip4Unloginlucky(String txtTip4Unloginlucky2) {
            this.txtTip4Unloginlucky = txtTip4Unloginlucky2;
        }

        public String getHongbaoyuInflateingImgUrl() {
            return this.hongbaoyuInflateingImgUrl;
        }

        public void setHongbaoyuInflateingImgUrl(String hongbaoyuInflateingImgUrl2) {
            this.hongbaoyuInflateingImgUrl = hongbaoyuInflateingImgUrl2;
        }

        public String getHongbaoyuFullBgImgUrl() {
            return this.hongbaoyuFullBgImgUrl;
        }

        public void setHongbaoyuFullBgImgUrl(String hongbaoyuFullBgImgUrl2) {
            this.hongbaoyuFullBgImgUrl = hongbaoyuFullBgImgUrl2;
        }

        public String getHongbaoyuFullInnerImgUrl() {
            return this.hongbaoyuFullInnerImgUrl;
        }

        public void setHongbaoyuFullInnerImgUrl(String hongbaoyuFullInnerImgUrl2) {
            this.hongbaoyuFullInnerImgUrl = hongbaoyuFullInnerImgUrl2;
        }

        public String getHongbaoyuFullCoverImgUrl() {
            return this.hongbaoyuFullCoverImgUrl;
        }

        public void setHongbaoyuFullCoverImgUrl(String hongbaoyuFullCoverImgUrl2) {
            this.hongbaoyuFullCoverImgUrl = hongbaoyuFullCoverImgUrl2;
        }

        public String getHongbaoyuOverImgUrl() {
            return this.hongbaoyuOverImgUrl;
        }

        public void setHongbaoyuOverImgUrl(String hongbaoyuOverImgUrl2) {
            this.hongbaoyuOverImgUrl = hongbaoyuOverImgUrl2;
        }

        public String[] getHongbaoyuEffectLayer1() {
            return this.hongbaoyuEffectLayer1;
        }

        public void setHongbaoyuEffectLayer1(String[] hongbaoyuEffectLayer12) {
            this.hongbaoyuEffectLayer1 = hongbaoyuEffectLayer12;
        }

        public String[] getHongbaoyuEffectLayer2() {
            return this.hongbaoyuEffectLayer2;
        }

        public void setHongbaoyuEffectLayer2(String[] hongbaoyuEffectLayer22) {
            this.hongbaoyuEffectLayer2 = hongbaoyuEffectLayer22;
        }

        public String[] getHongbaoyuEffectLayer3() {
            return this.hongbaoyuEffectLayer3;
        }

        public void setHongbaoyuEffectLayer3(String[] hongbaoyuEffectLayer32) {
            this.hongbaoyuEffectLayer3 = hongbaoyuEffectLayer32;
        }

        public String[] getHongbaoyuEffectLayer4() {
            return this.hongbaoyuEffectLayer4;
        }

        public void setHongbaoyuEffectLayer4(String[] hongbaoyuEffectLayer42) {
            this.hongbaoyuEffectLayer4 = hongbaoyuEffectLayer42;
        }

        public String getTxtTip4UnHandledType() {
            return this.txtTip4UnHandledType;
        }

        public void setTxtTip4UnHandledType(String txtTip4UnHandledType2) {
            this.txtTip4UnHandledType = txtTip4UnHandledType2;
        }

        public String toString() {
            return JSONObject.toJSONString(this);
        }
    }
}
