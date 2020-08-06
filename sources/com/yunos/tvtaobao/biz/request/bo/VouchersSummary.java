package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class VouchersSummary {
    private List<Button> buttons;
    private String newUI;

    public class Button {
        private String description;
        private String hasMultiHongbao;
        private String status;
        private String subTitle;
        private String title;
        private String titleDetail;
        private String type;
        private String value;

        public Button() {
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description2) {
            this.description = description2;
        }

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status2) {
            this.status = status2;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type2) {
            this.type = type2;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value2) {
            this.value = value2;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }

        public String getTitleDetail() {
            return this.titleDetail;
        }

        public void setTitleDetail(String titleDetail2) {
            this.titleDetail = titleDetail2;
        }

        public String getSubTitle() {
            return this.subTitle;
        }

        public void setSubTitle(String subTitle2) {
            this.subTitle = subTitle2;
        }

        public String getHasMultiHongbao() {
            return this.hasMultiHongbao;
        }

        public void setHasMultiHongbao(String hasMultiHongbao2) {
            this.hasMultiHongbao = hasMultiHongbao2;
        }
    }

    public String getNewUI() {
        return this.newUI;
    }

    public void setNewUI(String newUI2) {
        this.newUI = newUI2;
    }

    public List<Button> getButtons() {
        return this.buttons;
    }

    public void setButtons(List<Button> buttons2) {
        this.buttons = buttons2;
    }
}
