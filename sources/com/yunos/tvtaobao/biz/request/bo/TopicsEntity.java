package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.ArrayList;

public class TopicsEntity implements Serializable {
    private static final long serialVersionUID = 7812436683558501952L;
    private TopicsEntityImage images;
    private ArrayList<TopicsEntityItem> items;
    private TopicsEntityLayout layout;

    public TopicsEntityLayout getLayout() {
        return this.layout;
    }

    public void setLayout(TopicsEntityLayout layout2) {
        this.layout = layout2;
    }

    public TopicsEntityImage getImages() {
        return this.images;
    }

    public void setImages(TopicsEntityImage images2) {
        this.images = images2;
    }

    public ArrayList<TopicsEntityItem> getItems() {
        return this.items;
    }

    public void setItems(ArrayList<TopicsEntityItem> items2) {
        this.items = items2;
    }
}
