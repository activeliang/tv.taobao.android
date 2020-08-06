package com.yunos.tvtaobao.biz.request.bo;

public class GuessLikeFieldsVO {
    private BottomTipVO bottomTip;
    private String itemId;
    private MasterPicVO masterPic;
    private PriceVO price;
    private SimilarVO similar;
    private TitleVO title;

    public BottomTipVO getBottomTip() {
        return this.bottomTip;
    }

    public void setBottomTip(BottomTipVO bottomTip2) {
        this.bottomTip = bottomTip2;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId2) {
        this.itemId = itemId2;
    }

    public MasterPicVO getMasterPic() {
        return this.masterPic;
    }

    public void setMasterPic(MasterPicVO masterPic2) {
        this.masterPic = masterPic2;
    }

    public PriceVO getPrice() {
        return this.price;
    }

    public void setPrice(PriceVO price2) {
        this.price = price2;
    }

    public SimilarVO getSimilar() {
        return this.similar;
    }

    public void setSimilar(SimilarVO similar2) {
        this.similar = similar2;
    }

    public TitleVO getTitle() {
        return this.title;
    }

    public void setTitle(TitleVO title2) {
        this.title = title2;
    }

    public static class BottomTipVO {
        private TextVO text;

        public String toString() {
            return "BottomTipVO{text=" + this.text + '}';
        }

        public TextVO getText() {
            return this.text;
        }

        public void setText(TextVO text2) {
            this.text = text2;
        }

        public static class TextVO {
            private String content;

            public String toString() {
                return "TextVO{content='" + this.content + '\'' + '}';
            }

            public String getContent() {
                return this.content;
            }

            public void setContent(String content2) {
                this.content = content2;
            }
        }
    }

    public static class MasterPicVO {
        private String picUrl;

        public String toString() {
            return "MasterPicVO{picUrl='" + this.picUrl + '\'' + '}';
        }

        public String getPicUrl() {
            return this.picUrl;
        }

        public void setPicUrl(String picUrl2) {
            this.picUrl = picUrl2;
        }
    }

    public static class PriceVO {
        private String cent;
        private String separator;
        private String symbol;
        private String unit;
        private String yuan;

        public String toString() {
            return "PriceVO{cent='" + this.cent + '\'' + ", separator='" + this.separator + '\'' + ", symbol='" + this.symbol + '\'' + ", unit='" + this.unit + '\'' + ", yuan='" + this.yuan + '\'' + '}';
        }

        public String getCent() {
            return this.cent;
        }

        public void setCent(String cent2) {
            this.cent = cent2;
        }

        public String getSeparator() {
            return this.separator;
        }

        public void setSeparator(String separator2) {
            this.separator = separator2;
        }

        public String getSymbol() {
            return this.symbol;
        }

        public void setSymbol(String symbol2) {
            this.symbol = symbol2;
        }

        public String getUnit() {
            return this.unit;
        }

        public void setUnit(String unit2) {
            this.unit = unit2;
        }

        public String getYuan() {
            return this.yuan;
        }

        public void setYuan(String yuan2) {
            this.yuan = yuan2;
        }
    }

    public class SimilarVO {
        private String action;

        public SimilarVO() {
        }

        public String getAction() {
            return this.action;
        }

        public void setAction(String action2) {
            this.action = action2;
        }
    }

    public static class TitleVO {
        private ContextVO context;
        private IconVo icon;

        public String toString() {
            return "TitleVO{context=" + this.context + ", icon=" + this.icon + '}';
        }

        public static class ContextVO {
            private String content;

            public String getContent() {
                return this.content;
            }

            public void setContent(String content2) {
                this.content = content2;
            }
        }

        private class IconVo {
            private String picUrl;

            private IconVo() {
            }

            public String getPicUrl() {
                return this.picUrl;
            }

            public void setPicUrl(String picUrl2) {
                this.picUrl = picUrl2;
            }
        }

        public ContextVO getContext() {
            return this.context;
        }

        public void setContext(ContextVO context2) {
            this.context = context2;
        }

        public IconVo getIcon() {
            return this.icon;
        }

        public void setIcon(IconVo icon2) {
            this.icon = icon2;
        }
    }

    public String toString() {
        return "GuessLikeFieldsVO{bottomTip=" + this.bottomTip + ", itemId='" + this.itemId + '\'' + ", masterPic=" + this.masterPic + ", price=" + this.price + ", similar=" + this.similar + ", title=" + this.title + '}';
    }
}
