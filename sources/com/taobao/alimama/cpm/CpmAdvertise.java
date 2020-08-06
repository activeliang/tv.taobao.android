package com.taobao.alimama.cpm;

import android.graphics.Bitmap;
import android.text.TextUtils;
import com.alibaba.fastjson.annotation.JSONField;
import com.taobao.alimama.net.pojo.response.AlimamaZzAd;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CpmAdvertise implements Serializable, Cloneable {
    @JSONField(name = "bid")
    public String bid;
    @JSONField(deserialize = false, serialize = false)
    public Bitmap bitmap;
    @JSONField(name = "cache_time")
    public long cachetime;
    @JSONField(name = "click_url")
    public String clickUrl;
    @JSONField(name = "extra")
    public Map<String, String> extra;
    @JSONField(name = "ifs")
    public String ifs;
    @JSONField(name = "image_url")
    public String imageUrl;
    @JSONField(name = "pid")
    public String pid;
    @JSONField(name = "tmpl")
    public String tmpl;

    static CpmAdvertise from(AlimamaZzAd alimamaZzAd) {
        CpmAdvertise cpmAdvertise = new CpmAdvertise();
        cpmAdvertise.clickUrl = alimamaZzAd.eurl;
        cpmAdvertise.bid = alimamaZzAd.bid;
        cpmAdvertise.pid = alimamaZzAd.pid;
        if (alimamaZzAd.extension != null) {
            cpmAdvertise.tmpl = alimamaZzAd.extension.tmpl;
        }
        cpmAdvertise.imageUrl = alimamaZzAd.tbgoodslink;
        try {
            cpmAdvertise.cachetime = Long.parseLong(alimamaZzAd.cachetime);
        } catch (NumberFormatException e) {
            cpmAdvertise.cachetime = 0;
        }
        cpmAdvertise.ifs = alimamaZzAd.ifs;
        return cpmAdvertise;
    }

    private boolean simplyCompareBitmap(Bitmap bitmap2, Bitmap bitmap3) {
        if (bitmap2 == bitmap3) {
            return true;
        }
        if (bitmap2 == null || bitmap3 == null) {
            return false;
        }
        return bitmap2.getHeight() == bitmap3.getHeight() && bitmap2.getWidth() == bitmap3.getWidth();
    }

    public void addExtra(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            if (this.extra == null) {
                this.extra = new HashMap();
            }
            this.extra.put(str, str2);
        }
    }

    public CpmAdvertise clone() {
        try {
            return (CpmAdvertise) super.clone();
        } catch (CloneNotSupportedException e) {
            return this;
        }
    }

    public boolean dataEquals(CpmAdvertise cpmAdvertise) {
        if (this == cpmAdvertise) {
            return true;
        }
        if (cpmAdvertise == null) {
            return false;
        }
        return TextUtils.equals(this.bid, cpmAdvertise.bid) && TextUtils.equals(this.pid, cpmAdvertise.pid) && TextUtils.equals(this.clickUrl, cpmAdvertise.clickUrl) && TextUtils.equals(this.ifs, cpmAdvertise.ifs) && this.cachetime == cpmAdvertise.cachetime && TextUtils.equals(this.tmpl, cpmAdvertise.tmpl) && TextUtils.equals(this.imageUrl, cpmAdvertise.imageUrl);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CpmAdvertise)) {
            return false;
        }
        CpmAdvertise cpmAdvertise = (CpmAdvertise) obj;
        return dataEquals(cpmAdvertise) && simplyCompareBitmap(this.bitmap, cpmAdvertise.bitmap);
    }

    public String getExtra(String str) {
        if (!TextUtils.isEmpty(str) && this.extra != null) {
            return this.extra.get(str);
        }
        return null;
    }
}
