package com.yunos.tvtaobao.biz.request.bo;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

public class CategoryMO implements Parcelable {
    public static final Parcelable.Creator<CategoryMO> CREATOR = new Parcelable.Creator<CategoryMO>() {
        public CategoryMO createFromParcel(Parcel source) {
            return CategoryMO.newInstanceFromParcel(source);
        }

        public CategoryMO[] newArray(int size) {
            return new CategoryMO[size];
        }
    };
    private Long cid;
    private String name;
    private Long parentCid;
    private Integer type;

    public String toString() {
        return "JuCategoryMO [cid=" + this.cid + ", parentCid=" + this.parentCid + ", name=" + this.name + ", type=" + this.type + "]";
    }

    public Long getCid() {
        return this.cid;
    }

    public void setCid(Long cid2) {
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
        c.setCid(Long.valueOf(obj.optLong("cid")));
        c.setParentCid(Long.valueOf(obj.optLong("parentCid")));
        c.setName(obj.optString("name"));
        c.setType(Integer.valueOf(obj.optInt("type")));
        return c;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.cid.longValue());
        dest.writeLong(this.parentCid.longValue());
        dest.writeString(this.name);
        dest.writeInt(this.type.intValue());
    }

    public static CategoryMO newInstanceFromParcel(Parcel source) {
        CategoryMO c = new CategoryMO();
        c.cid = Long.valueOf(source.readLong());
        c.parentCid = Long.valueOf(source.readLong());
        c.name = source.readString();
        c.type = Integer.valueOf(source.readInt());
        return c;
    }
}
