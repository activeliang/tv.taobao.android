package com.yunos.tvtaobao.biz.request.bo;

import com.taobao.detail.domain.base.Unit;
import java.io.Serializable;
import java.util.List;
import org.json.JSONObject;

public class TBDetailResultV6 implements Serializable {
    private List<ApiStackBean> apiStack;
    private ConsumerProtectionBean consumerProtection;
    private List<ContractData> contractDataList;
    private Delivery delivery;
    private List<Unit> domainList;
    private Feature feature;
    private ItemBean item;
    private String mockData;
    private ParamsBean params;
    private PriceBeanX price;
    private PropsBean props;
    private RateBean rate;
    private ResourceBean resource;
    private SellerBean seller;
    private SkuBaseBean skuBase;
    private String skuKore;
    private Trade trade;
    private VerticalBean vertical;

    public List<Unit> getDomainUnit() {
        return this.domainList;
    }

    public void setDomainList(List<Unit> domainList2) {
        this.domainList = domainList2;
    }

    public String getSkuKore() {
        return this.skuKore;
    }

    public void setSkuKore(String skuKore2) {
        this.skuKore = skuKore2;
    }

    public PriceBeanX getPrice() {
        return this.price;
    }

    public void setPrice(PriceBeanX price2) {
        this.price = price2;
    }

    public Delivery getDelivery() {
        return this.delivery;
    }

    public void setDelivery(Delivery delivery2) {
        this.delivery = delivery2;
    }

    public Feature getFeature() {
        return this.feature;
    }

    public void setFeature(Feature feature2) {
        this.feature = feature2;
    }

    public Trade getTrade() {
        return this.trade;
    }

    public void setTrade(Trade trade2) {
        this.trade = trade2;
    }

    public ConsumerProtectionBean getConsumerProtection() {
        return this.consumerProtection;
    }

    public void setConsumerProtection(ConsumerProtectionBean consumerProtection2) {
        this.consumerProtection = consumerProtection2;
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

    public ParamsBean getParams() {
        return this.params;
    }

    public void setParams(ParamsBean params2) {
        this.params = params2;
    }

    public PropsBean getProps() {
        return this.props;
    }

    public void setProps(PropsBean props2) {
        this.props = props2;
    }

    public RateBean getRate() {
        return this.rate;
    }

    public void setRate(RateBean rate2) {
        this.rate = rate2;
    }

    public ResourceBean getResource() {
        return this.resource;
    }

    public void setResource(ResourceBean resource2) {
        this.resource = resource2;
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

    public VerticalBean getVertical() {
        return this.vertical;
    }

    public void setVertical(VerticalBean vertical2) {
        this.vertical = vertical2;
    }

    public List<ApiStackBean> getApiStack() {
        return this.apiStack;
    }

    public void setContractData(List<ContractData> contractDataList2) {
        this.contractDataList = contractDataList2;
    }

    public List<ContractData> getContractData() {
        return this.contractDataList;
    }

    public void setApiStack(List<ApiStackBean> apiStack2) {
        this.apiStack = apiStack2;
    }

    public static class ContractData implements Serializable {
        public VersionData versionData;

        public static class VersionData implements Serializable {
            public boolean enableClick;
            public boolean noShopCart;
            public String planId;
            public String versionCode;
            public String versionName;

            private VersionData() {
            }

            public static VersionData resolveVersionData(JSONObject data) {
                if (data == null) {
                    return null;
                }
                VersionData versionData = new VersionData();
                versionData.enableClick = data.optBoolean("enableClick");
                versionData.planId = data.optString("planId");
                versionData.noShopCart = data.optBoolean("noShopCart");
                versionData.versionCode = data.optString("versionCode");
                versionData.versionName = data.optString("versionName");
                return versionData;
            }
        }
    }

    public static class PriceBeanX implements Serializable {
        private PriceBean price;

        public PriceBean getPrice() {
            return this.price;
        }

        public void setPrice(PriceBean price2) {
            this.price = price2;
        }

        public static class PriceBean implements Serializable {
            private String priceText;
            private String priceTitle;

            public String getPriceTitle() {
                return this.priceTitle;
            }

            public void setPriceTitle(String priceTitle2) {
                this.priceTitle = priceTitle2;
            }

            public String getPriceText() {
                return this.priceText;
            }

            public void setPriceText(String priceText2) {
                this.priceText = priceText2;
            }
        }
    }

    public static class Delivery implements Serializable {
        private String postage;

        public String getPostage() {
            return this.postage;
        }

        public void setPostage(String postage2) {
            this.postage = postage2;
        }
    }

    public static class Feature implements Serializable {
        private String hasSku;
        private String secKill;

        public String getHasSku() {
            return this.hasSku;
        }

        public void setHasSku(String hasSku2) {
            this.hasSku = hasSku2;
        }

        public String getSecKill() {
            return this.secKill;
        }

        public void setSecKill(String secKill2) {
            this.secKill = secKill2;
        }
    }

    public static class ConsumerProtectionBean implements Serializable {
        private ChannelBean channel;
        private String passValue;

        public ChannelBean getChannel() {
            return this.channel;
        }

        public void setChannel(ChannelBean channel2) {
            this.channel = channel2;
        }

        public String getPassValue() {
            return this.passValue;
        }

        public void setPassValue(String passValue2) {
            this.passValue = passValue2;
        }

        public static class ChannelBean implements Serializable {
            private String logo;
            private String title;

            public String getLogo() {
                return this.logo;
            }

            public void setLogo(String logo2) {
                this.logo = logo2;
            }

            public String getTitle() {
                return this.title;
            }

            public void setTitle(String title2) {
                this.title = title2;
            }
        }
    }

    public static class ItemBean implements Serializable {
        private String brandValueId;
        private String categoryId;
        private String commentCount;
        private String favcount;
        private String h5moduleDescUrl;
        private List<String> images;
        private String itemId;
        private ModuleDescParamsBean moduleDescParams;
        private String moduleDescUrl;
        private String rootCategoryId;
        private String skuText;
        private String subtitle;
        private String taobaoDescUrl;
        private String taobaoPcDescUrl;
        private String themeType;
        private String title;
        private String titleIcon;
        private String tmallDescUrl;

        public String getItemId() {
            return this.itemId;
        }

        public void setItemId(String itemId2) {
            this.itemId = itemId2;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }

        public String getSubtitle() {
            return this.subtitle;
        }

        public void setSubtitle(String subtitle2) {
            this.subtitle = subtitle2;
        }

        public String getCategoryId() {
            return this.categoryId;
        }

        public void setCategoryId(String categoryId2) {
            this.categoryId = categoryId2;
        }

        public String getRootCategoryId() {
            return this.rootCategoryId;
        }

        public void setRootCategoryId(String rootCategoryId2) {
            this.rootCategoryId = rootCategoryId2;
        }

        public String getBrandValueId() {
            return this.brandValueId;
        }

        public void setBrandValueId(String brandValueId2) {
            this.brandValueId = brandValueId2;
        }

        public String getSkuText() {
            return this.skuText;
        }

        public void setSkuText(String skuText2) {
            this.skuText = skuText2;
        }

        public String getCommentCount() {
            return this.commentCount;
        }

        public void setCommentCount(String commentCount2) {
            this.commentCount = commentCount2;
        }

        public String getFavcount() {
            return this.favcount;
        }

        public void setFavcount(String favcount2) {
            this.favcount = favcount2;
        }

        public String getTaobaoDescUrl() {
            return this.taobaoDescUrl;
        }

        public void setTaobaoDescUrl(String taobaoDescUrl2) {
            this.taobaoDescUrl = taobaoDescUrl2;
        }

        public String getTmallDescUrl() {
            return this.tmallDescUrl;
        }

        public void setTmallDescUrl(String tmallDescUrl2) {
            this.tmallDescUrl = tmallDescUrl2;
        }

        public String getTaobaoPcDescUrl() {
            return this.taobaoPcDescUrl;
        }

        public void setTaobaoPcDescUrl(String taobaoPcDescUrl2) {
            this.taobaoPcDescUrl = taobaoPcDescUrl2;
        }

        public String getModuleDescUrl() {
            return this.moduleDescUrl;
        }

        public void setModuleDescUrl(String moduleDescUrl2) {
            this.moduleDescUrl = moduleDescUrl2;
        }

        public ModuleDescParamsBean getModuleDescParams() {
            return this.moduleDescParams;
        }

        public void setModuleDescParams(ModuleDescParamsBean moduleDescParams2) {
            this.moduleDescParams = moduleDescParams2;
        }

        public String getH5moduleDescUrl() {
            return this.h5moduleDescUrl;
        }

        public void setH5moduleDescUrl(String h5moduleDescUrl2) {
            this.h5moduleDescUrl = h5moduleDescUrl2;
        }

        public String getTitleIcon() {
            return this.titleIcon;
        }

        public void setTitleIcon(String titleIcon2) {
            this.titleIcon = titleIcon2;
        }

        public String getThemeType() {
            return this.themeType;
        }

        public void setThemeType(String themeType2) {
            this.themeType = themeType2;
        }

        public List<String> getImages() {
            return this.images;
        }

        public void setImages(List<String> images2) {
            this.images = images2;
        }

        public static class ModuleDescParamsBean implements Serializable {
            private String f;
            private String id;

            public String getF() {
                return this.f;
            }

            public void setF(String f2) {
                this.f = f2;
            }

            public String getId() {
                return this.id;
            }

            public void setId(String id2) {
                this.id = id2;
            }
        }
    }

    public static class ParamsBean implements Serializable {
        private TrackParamsBean trackParams;

        public TrackParamsBean getTrackParams() {
            return this.trackParams;
        }

        public void setTrackParams(TrackParamsBean trackParams2) {
            this.trackParams = trackParams2;
        }

        public static class TrackParamsBean implements Serializable {
            private String BC_type;
            private String brandId;
            private String categoryId;

            public String getBrandId() {
                return this.brandId;
            }

            public void setBrandId(String brandId2) {
                this.brandId = brandId2;
            }

            public String getBC_type() {
                return this.BC_type;
            }

            public void setBC_type(String BC_type2) {
                this.BC_type = BC_type2;
            }

            public String getCategoryId() {
                return this.categoryId;
            }

            public void setCategoryId(String categoryId2) {
                this.categoryId = categoryId2;
            }
        }
    }

    public static class PropsBean implements Serializable {
        private List<GroupPropsBean> groupProps;

        public static class GroupPropsBean implements Serializable {
        }

        public List<GroupPropsBean> getGroupProps() {
            return this.groupProps;
        }

        public void setGroupProps(List<GroupPropsBean> groupProps2) {
            this.groupProps = groupProps2;
        }
    }

    public static class RateBean implements Serializable {
        private List<RateListBean> rateList;
        private String totalCount;

        public String getTotalCount() {
            return this.totalCount;
        }

        public void setTotalCount(String totalCount2) {
            this.totalCount = totalCount2;
        }

        public List<RateListBean> getRateList() {
            return this.rateList;
        }

        public void setRateList(List<RateListBean> rateList2) {
            this.rateList = rateList2;
        }

        public static class RateListBean implements Serializable {
            private String content;
            private String dateTime;
            private String headPic;
            private String memberLevel;
            private String skuInfo;
            private String tmallMemberLevel;
            private String userName;

            public String getContent() {
                return this.content;
            }

            public void setContent(String content2) {
                this.content = content2;
            }

            public String getUserName() {
                return this.userName;
            }

            public void setUserName(String userName2) {
                this.userName = userName2;
            }

            public String getHeadPic() {
                return this.headPic;
            }

            public void setHeadPic(String headPic2) {
                this.headPic = headPic2;
            }

            public String getMemberLevel() {
                return this.memberLevel;
            }

            public void setMemberLevel(String memberLevel2) {
                this.memberLevel = memberLevel2;
            }

            public String getDateTime() {
                return this.dateTime;
            }

            public void setDateTime(String dateTime2) {
                this.dateTime = dateTime2;
            }

            public String getSkuInfo() {
                return this.skuInfo;
            }

            public void setSkuInfo(String skuInfo2) {
                this.skuInfo = skuInfo2;
            }

            public String getTmallMemberLevel() {
                return this.tmallMemberLevel;
            }

            public void setTmallMemberLevel(String tmallMemberLevel2) {
                this.tmallMemberLevel = tmallMemberLevel2;
            }
        }
    }

    public static class ResourceBean implements Serializable {
        private EntrancesBean entrances;

        public EntrancesBean getEntrances() {
            return this.entrances;
        }

        public void setEntrances(EntrancesBean entrances2) {
            this.entrances = entrances2;
        }

        public static class EntrancesBean implements Serializable {
            private AskAllBean askAll;

            public AskAllBean getAskAll() {
                return this.askAll;
            }

            public void setAskAll(AskAllBean askAll2) {
                this.askAll = askAll2;
            }

            public static class AskAllBean implements Serializable {
                private String icon;
                private String link;
                private String text;

                public String getIcon() {
                    return this.icon;
                }

                public void setIcon(String icon2) {
                    this.icon = icon2;
                }

                public String getText() {
                    return this.text;
                }

                public void setText(String text2) {
                    this.text = text2;
                }

                public String getLink() {
                    return this.link;
                }

                public void setLink(String link2) {
                    this.link = link2;
                }
            }
        }
    }

    public static class SellerBean implements Serializable {
        private String allItemCount;
        private String certIcon;
        private String creditLevel;
        private List<EvaluatesBean> evaluates;
        private String fans;
        private String goodRatePercentage;
        private String newItemCount;
        private String sellerNick;
        private String sellerType;
        private String shopCard;
        private String shopIcon;
        private String shopId;
        private String shopName;
        private String shopType;
        private String shopUrl;
        private String starts;
        private String tagIcon;
        private String userId;

        public String getUserId() {
            return this.userId;
        }

        public void setUserId(String userId2) {
            this.userId = userId2;
        }

        public String getShopId() {
            return this.shopId;
        }

        public void setShopId(String shopId2) {
            this.shopId = shopId2;
        }

        public String getShopName() {
            return this.shopName;
        }

        public void setShopName(String shopName2) {
            this.shopName = shopName2;
        }

        public String getShopUrl() {
            return this.shopUrl;
        }

        public void setShopUrl(String shopUrl2) {
            this.shopUrl = shopUrl2;
        }

        public String getShopIcon() {
            return this.shopIcon;
        }

        public void setShopIcon(String shopIcon2) {
            this.shopIcon = shopIcon2;
        }

        public String getFans() {
            return this.fans;
        }

        public void setFans(String fans2) {
            this.fans = fans2;
        }

        public String getCertIcon() {
            return this.certIcon;
        }

        public void setCertIcon(String certIcon2) {
            this.certIcon = certIcon2;
        }

        public String getAllItemCount() {
            return this.allItemCount;
        }

        public void setAllItemCount(String allItemCount2) {
            this.allItemCount = allItemCount2;
        }

        public String getNewItemCount() {
            return this.newItemCount;
        }

        public void setNewItemCount(String newItemCount2) {
            this.newItemCount = newItemCount2;
        }

        public String getShopCard() {
            return this.shopCard;
        }

        public void setShopCard(String shopCard2) {
            this.shopCard = shopCard2;
        }

        public String getSellerType() {
            return this.sellerType;
        }

        public void setSellerType(String sellerType2) {
            this.sellerType = sellerType2;
        }

        public String getShopType() {
            return this.shopType;
        }

        public void setShopType(String shopType2) {
            this.shopType = shopType2;
        }

        public String getSellerNick() {
            return this.sellerNick;
        }

        public void setSellerNick(String sellerNick2) {
            this.sellerNick = sellerNick2;
        }

        public String getCreditLevel() {
            return this.creditLevel;
        }

        public void setCreditLevel(String creditLevel2) {
            this.creditLevel = creditLevel2;
        }

        public String getTagIcon() {
            return this.tagIcon;
        }

        public void setTagIcon(String tagIcon2) {
            this.tagIcon = tagIcon2;
        }

        public String getStarts() {
            return this.starts;
        }

        public void setStarts(String starts2) {
            this.starts = starts2;
        }

        public String getGoodRatePercentage() {
            return this.goodRatePercentage;
        }

        public void setGoodRatePercentage(String goodRatePercentage2) {
            this.goodRatePercentage = goodRatePercentage2;
        }

        public List<EvaluatesBean> getEvaluates() {
            return this.evaluates;
        }

        public void setEvaluates(List<EvaluatesBean> evaluates2) {
            this.evaluates = evaluates2;
        }

        public static class EvaluatesBean implements Serializable {
            private String level;
            private String score;
            private String title;
            private String type;

            public String getTitle() {
                return this.title;
            }

            public void setTitle(String title2) {
                this.title = title2;
            }

            public String getScore() {
                return this.score;
            }

            public void setScore(String score2) {
                this.score = score2;
            }

            public String getType() {
                return this.type;
            }

            public void setType(String type2) {
                this.type = type2;
            }

            public String getLevel() {
                return this.level;
            }

            public void setLevel(String level2) {
                this.level = level2;
            }
        }
    }

    public static class SkuBaseBean implements Serializable {
        private List<PropsBeanX> props;
        private List<SkusBean> skus;

        public List<SkusBean> getSkus() {
            return this.skus;
        }

        public void setSkus(List<SkusBean> skus2) {
            this.skus = skus2;
        }

        public List<PropsBeanX> getProps() {
            return this.props;
        }

        public void setProps(List<PropsBeanX> props2) {
            this.props = props2;
        }

        public static class SkusBean implements Serializable {
            private String propPath;
            private String skuId;

            public String getSkuId() {
                return this.skuId;
            }

            public void setSkuId(String skuId2) {
                this.skuId = skuId2;
            }

            public String getPropPath() {
                return this.propPath;
            }

            public void setPropPath(String propPath2) {
                this.propPath = propPath2;
            }
        }

        public static class PropsBeanX implements Serializable {
            private String name;
            private String pid;
            private List<ValuesBean> values;

            public String getPid() {
                return this.pid;
            }

            public void setPid(String pid2) {
                this.pid = pid2;
            }

            public String getName() {
                return this.name;
            }

            public void setName(String name2) {
                this.name = name2;
            }

            public List<ValuesBean> getValues() {
                return this.values;
            }

            public void setValues(List<ValuesBean> values2) {
                this.values = values2;
            }

            public static class ValuesBean implements Serializable {
                private String image;
                private String name;
                private String vid;

                public String getVid() {
                    return this.vid;
                }

                public void setVid(String vid2) {
                    this.vid = vid2;
                }

                public String getName() {
                    return this.name;
                }

                public void setName(String name2) {
                    this.name = name2;
                }

                public String getImage() {
                    return this.image;
                }

                public void setImage(String image2) {
                    this.image = image2;
                }
            }
        }
    }

    public static class Trade implements Serializable {
        private String buyEnable;
        private String cartEnable;
        private String redirectUrl;

        public String getBuyEnable() {
            return this.buyEnable;
        }

        public void setBuyEnable(String buyEnable2) {
            this.buyEnable = buyEnable2;
        }

        public String getCartEnable() {
            return this.cartEnable;
        }

        public void setCartEnable(String cartEnable2) {
            this.cartEnable = cartEnable2;
        }

        public String getRedirectUrl() {
            return this.redirectUrl;
        }

        public void setRedirectUrl(String redirectUrl2) {
            this.redirectUrl = redirectUrl2;
        }
    }

    public static class VerticalBean implements Serializable {
        private AskAllBeanX askAll;
        private JyjBean jyj;

        public JyjBean getJyj() {
            return this.jyj;
        }

        public void setJyj(JyjBean jyj2) {
            this.jyj = jyj2;
        }

        public AskAllBeanX getAskAll() {
            return this.askAll;
        }

        public void setAskAll(AskAllBeanX askAll2) {
            this.askAll = askAll2;
        }

        public static class JyjBean implements Serializable {
            private String logoText;

            public String getLogoText() {
                return this.logoText;
            }

            public void setLogoText(String logoText2) {
                this.logoText = logoText2;
            }
        }

        public static class AskAllBeanX implements Serializable {
            private String answerIcon;
            private String answerText;
            private String askIcon;
            private String askText;
            private String linkUrl;
            private List<ModelListBean> modelList;
            private String questNum;
            private String title;

            public String getAskText() {
                return this.askText;
            }

            public void setAskText(String askText2) {
                this.askText = askText2;
            }

            public String getAskIcon() {
                return this.askIcon;
            }

            public void setAskIcon(String askIcon2) {
                this.askIcon = askIcon2;
            }

            public String getAnswerText() {
                return this.answerText;
            }

            public void setAnswerText(String answerText2) {
                this.answerText = answerText2;
            }

            public String getAnswerIcon() {
                return this.answerIcon;
            }

            public void setAnswerIcon(String answerIcon2) {
                this.answerIcon = answerIcon2;
            }

            public String getLinkUrl() {
                return this.linkUrl;
            }

            public void setLinkUrl(String linkUrl2) {
                this.linkUrl = linkUrl2;
            }

            public String getTitle() {
                return this.title;
            }

            public void setTitle(String title2) {
                this.title = title2;
            }

            public String getQuestNum() {
                return this.questNum;
            }

            public void setQuestNum(String questNum2) {
                this.questNum = questNum2;
            }

            public List<ModelListBean> getModelList() {
                return this.modelList;
            }

            public void setModelList(List<ModelListBean> modelList2) {
                this.modelList = modelList2;
            }

            public static class ModelListBean implements Serializable {
                private String answerCountText;
                private String askText;
                private String firstAnswer;

                public String getAskText() {
                    return this.askText;
                }

                public void setAskText(String askText2) {
                    this.askText = askText2;
                }

                public String getAnswerCountText() {
                    return this.answerCountText;
                }

                public void setAnswerCountText(String answerCountText2) {
                    this.answerCountText = answerCountText2;
                }

                public String getFirstAnswer() {
                    return this.firstAnswer;
                }

                public void setFirstAnswer(String firstAnswer2) {
                    this.firstAnswer = firstAnswer2;
                }
            }
        }
    }

    public static class ApiStackBean implements Serializable {
        private String name;
        private String value;

        public String getName() {
            return this.name;
        }

        public void setName(String name2) {
            this.name = name2;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value2) {
            this.value = value2;
        }
    }
}
