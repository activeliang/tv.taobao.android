package com.yunos.tvtaobao.biz.request.bo;

import android.net.Uri;
import java.io.Serializable;
import java.util.List;

public class TBaoShopBean implements Serializable {
    private List<ItemListBean.GoodsListBean> hotList;
    private List<ItemListBean> itemList;

    public List<ItemListBean.GoodsListBean> getHotList() {
        return this.hotList;
    }

    public void setHotList(List<ItemListBean.GoodsListBean> hotList2) {
        this.hotList = hotList2;
    }

    public List<ItemListBean> getItemList() {
        return this.itemList;
    }

    public void setItemList(List<ItemListBean> itemList2) {
        this.itemList = itemList2;
    }

    public static class ItemListBean implements Serializable {
        private String goodsIndex;
        private List<GoodsListBean> goodsList;

        public String getGoodsIndex() {
            return this.goodsIndex;
        }

        public void setGoodsIndex(String goodsIndex2) {
            this.goodsIndex = goodsIndex2;
        }

        public List<GoodsListBean> getGoodsList() {
            return this.goodsList;
        }

        public void setGoodsList(List<GoodsListBean> goodsList2) {
            this.goodsList = goodsList2;
        }

        public static class GoodsListBean {
            private String buyCount;
            private String favored;
            private String groupNum;
            private String itemId;
            private String itemName;
            private String itemPic;
            private String itemPrice;
            private String itemUrl;

            public String getBuyCount() {
                return this.buyCount;
            }

            public void setBuyCount(String buyCount2) {
                this.buyCount = buyCount2;
            }

            public String getFavored() {
                return this.favored;
            }

            public void setFavored(String favored2) {
                this.favored = favored2;
            }

            public String getGroupNum() {
                return this.groupNum;
            }

            public void setGroupNum(String groupNum2) {
                this.groupNum = groupNum2;
            }

            public String getItemId() {
                return this.itemId;
            }

            public void setItemId(String itemId2) {
                this.itemId = itemId2;
            }

            public String getItemName() {
                return this.itemName;
            }

            public void setItemName(String itemName2) {
                this.itemName = itemName2;
            }

            public String getItemPic() {
                return this.itemPic;
            }

            public void setItemPic(String itemPic2) {
                this.itemPic = itemPic2;
            }

            public String getItemPrice() {
                return this.itemPrice;
            }

            public void setItemPrice(String itemPrice2) {
                this.itemPrice = itemPrice2;
            }

            public String getItemUrl() {
                return this.itemUrl;
            }

            public String getItemPic(int width, int height) {
                if (this.itemPic == null) {
                    return null;
                }
                if (width <= 0 || height <= 0) {
                    return this.itemPic;
                }
                try {
                    if ("gw.alicdn.com".equals(Uri.parse(this.itemPic).getHost())) {
                        return String.format("%s_%dx%d.jpg", new Object[]{this.itemPic, Integer.valueOf(width), Integer.valueOf(height)});
                    }
                } catch (Exception e) {
                }
                return this.itemPic;
            }

            public void setItemUrl(String itemUrl2) {
                this.itemUrl = itemUrl2;
            }
        }
    }
}
