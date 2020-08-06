package com.yunos.tvtaobao.biz.request.bo.juhuasuan.bo;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CategoryMO extends BaseMO implements Parcelable {
    public static final Parcelable.Creator<CategoryMO> CREATOR = new Parcelable.Creator<CategoryMO>() {
        public CategoryMO createFromParcel(Parcel source) {
            return new CategoryMO(source);
        }

        public CategoryMO[] newArray(int size) {
            return new CategoryMO[size];
        }
    };
    private static final long serialVersionUID = -7591145889378574389L;
    private ArrayList<CategoryMO> children;
    private String cid;
    private String icon;
    public String iconHl;
    private Boolean isAll = false;
    private String name;
    private String optStr;
    private Long parentCid = 0L;
    private Integer type = 0;

    public String toString() {
        return "CategoryMO [cid=" + this.cid + ", parentCid=" + this.parentCid + ", name=" + this.name + ", type=" + this.type + ", optStr=" + this.optStr + ", icon=" + this.icon + ", iconHl=" + this.iconHl + ", children=" + this.children + ", isAll=" + this.isAll + "]";
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon2) {
        this.icon = icon2;
    }

    public String getIconHl() {
        return this.iconHl;
    }

    public void setIconHl(String iconHl2) {
        this.iconHl = iconHl2;
    }

    public Boolean getIsAll() {
        return this.isAll;
    }

    public void setIsAll(Boolean isAll2) {
        this.isAll = isAll2;
    }

    public String getCid() {
        return this.cid;
    }

    public void setCid(String cid2) {
        this.cid = cid2;
    }

    public Long getParentCid() {
        return this.parentCid;
    }

    public void setParentCid(Long parentCid2) {
        this.parentCid = parentCid2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type2) {
        this.type = type2;
    }

    public static CategoryMO fromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        CategoryMO c = new CategoryMO();
        c.setCid(obj.optString("cid"));
        c.setParentCid(Long.valueOf(obj.optLong("parentCid")));
        c.setName(obj.optString("name"));
        c.setType(Integer.valueOf(obj.optInt("type")));
        c.setOptStr(obj.optString("optStr"));
        c.setIcon(obj.optString("icon"));
        c.setIconHl(obj.optString("icon1"));
        if (obj.has("children")) {
            ArrayList<CategoryMO> list = new ArrayList<>();
            JSONArray array = obj.getJSONArray("children");
            for (int i = 0; i < array.length(); i++) {
                list.add(fromMTOP(array.getJSONObject(i)));
            }
            c.setChildren(list);
        }
        c.setIsAll(Boolean.valueOf(obj.optBoolean("isAll")));
        return c;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cid);
        dest.writeLong(this.parentCid.longValue());
        dest.writeString(this.name);
        dest.writeInt(this.type.intValue());
        dest.writeString(this.optStr);
        dest.writeString(this.icon);
        dest.writeString(this.iconHl);
        dest.writeList(this.children);
        dest.writeString(String.valueOf(this.isAll));
    }

    public CategoryMO() {
    }

    public CategoryMO(Parcel source) {
        this.cid = source.readString();
        this.parentCid = Long.valueOf(source.readLong());
        this.name = source.readString();
        this.type = Integer.valueOf(source.readInt());
        this.optStr = source.readString();
        this.icon = source.readString();
        this.iconHl = source.readString();
        this.children = new ArrayList<>();
        source.readList(this.children, getClass().getClassLoader());
        this.isAll = Boolean.valueOf(source.readString());
    }

    public ArrayList<CategoryMO> getChildren() {
        return this.children;
    }

    public void setChildren(ArrayList<CategoryMO> children2) {
        this.children = children2;
    }

    public String getOptStr() {
        return this.optStr;
    }

    public void setOptStr(String optStr2) {
        this.optStr = optStr2;
    }

    public boolean isAll() {
        return this.isAll.booleanValue();
    }

    public void setAll(boolean isAll2) {
        this.isAll = Boolean.valueOf(isAll2);
    }
}
