package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class JhsConfigBean {
    private String bgColor;
    private String bgPic;
    private FloorBean floor;

    public String getBgPic() {
        return this.bgPic;
    }

    public void setBgPic(String bgPic2) {
        this.bgPic = bgPic2;
    }

    public String getBgColor() {
        return this.bgColor;
    }

    public void setBgColor(String bgColor2) {
        this.bgColor = bgColor2;
    }

    public FloorBean getFloor() {
        return this.floor;
    }

    public void setFloor(FloorBean floor2) {
        this.floor = floor2;
    }

    public static class FloorBean {
        private List<ItemsBean> items;
        private StyleBean style;

        public StyleBean getStyle() {
            return this.style;
        }

        public void setStyle(StyleBean style2) {
            this.style = style2;
        }

        public List<ItemsBean> getItems() {
            return this.items;
        }

        public void setItems(List<ItemsBean> items2) {
            this.items = items2;
        }

        public static class StyleBean {
            private String defaultDrawableColor;
            private String focusDrawableColor;
            private String focusTextColor;
            private String selectDrawableColor;
            private String textColor;

            public String getTextColor() {
                return this.textColor;
            }

            public void setTextColor(String textColor2) {
                this.textColor = textColor2;
            }

            public String getFocusTextColor() {
                return this.focusTextColor;
            }

            public void setFocusTextColor(String focusTextColor2) {
                this.focusTextColor = focusTextColor2;
            }

            public String getSelectDrawableColor() {
                return this.selectDrawableColor;
            }

            public void setSelectDrawableColor(String selectDrawableColor2) {
                this.selectDrawableColor = selectDrawableColor2;
            }

            public String getFocusDrawableColor() {
                return this.focusDrawableColor;
            }

            public void setFocusDrawableColor(String focusDrawableColor2) {
                this.focusDrawableColor = focusDrawableColor2;
            }

            public String getDefaultDrawableColor() {
                return this.defaultDrawableColor;
            }

            public void setDefaultDrawableColor(String defaultDrawableColor2) {
                this.defaultDrawableColor = defaultDrawableColor2;
            }
        }

        public static class ItemsBean {
            private String catId;
            private String title;

            public String getTitle() {
                return this.title;
            }

            public void setTitle(String title2) {
                this.title = title2;
            }

            public String getCatId() {
                return this.catId;
            }

            public void setCatId(String catId2) {
                this.catId = catId2;
            }
        }
    }
}
