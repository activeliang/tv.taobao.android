package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class Cat implements Serializable {
    private static final long serialVersionUID = -1673011182508847114L;
    public int currentPage;
    private String id;
    private String imagePath;
    public boolean isLast;
    private String name;

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath2) {
        this.imagePath = imagePath2;
    }
}
