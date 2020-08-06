package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.ArrayList;

public class BrandMO implements Serializable {
    private static final long serialVersionUID = 4767298014606039748L;
    private String background;
    private String brandDes;
    private String code;
    private String firstLatter;
    private String infoUrl;
    private boolean isFollow;
    private ArrayList<ItemMO> itemList;
    private String logo;
    private String mainPicUrl;
    private String name;

    public String toString() {
        return "BrandMO [code=" + this.code + ", name=" + this.name + ", logo=" + this.logo + ", brandDes=" + this.brandDes + ", isFollow=" + this.isFollow + ", firstLatter=" + this.firstLatter + ", infoUrl=" + this.infoUrl + ", background=" + this.background + ", itemList=" + this.itemList + "]";
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code2) {
        this.code = code2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo2) {
        this.logo = logo2;
    }

    public String getBrandDes() {
        return this.brandDes;
    }

    public void setBrandDes(String brandDes2) {
        this.brandDes = brandDes2;
    }

    public String getFirstLatter() {
        return this.firstLatter;
    }

    public void setFirstLatter(String firstLatter2) {
        if (firstLatter2 != null) {
            this.firstLatter = firstLatter2.toUpperCase();
        }
    }

    public String getInfoUrl() {
        return this.infoUrl;
    }

    public void setInfoUrl(String infoUrl2) {
        this.infoUrl = infoUrl2;
    }

    public String getBackground() {
        return this.background;
    }

    public void setBackground(String background2) {
        this.background = background2;
    }

    public String getMainPicUrl() {
        return this.mainPicUrl;
    }

    public void setMainPicUrl(String mainPicUrl2) {
        this.mainPicUrl = mainPicUrl2;
    }

    public ArrayList<ItemMO> getItemList() {
        return this.itemList;
    }

    public void addItem(ItemMO item) {
        if (item != null) {
            if (this.itemList == null) {
                this.itemList = new ArrayList<>();
            }
            this.itemList.add(item);
        }
    }
}
