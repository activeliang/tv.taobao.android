package com.taobao.mediaplay.model;

import android.text.TextUtils;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import org.json.JSONObject;

public class CacheKeyDefinition {
    private String mCacheKey;
    private JSONObject mData;
    private String mDefinition;
    private int mSize;

    public CacheKeyDefinition(JSONObject jsonObject) {
        this.mData = jsonObject;
    }

    public String getCacheKey() {
        if (TextUtils.isEmpty(this.mCacheKey) && this.mData != null) {
            Object obj = this.mData.opt("cacheKey");
            this.mCacheKey = obj == null ? null : obj.toString();
        }
        return this.mCacheKey;
    }

    public int getVideoSize() {
        try {
            if (this.mSize == 0 && this.mData != null) {
                Object obj = this.mData.opt("length");
                double size = (obj == null || !TextUtils.isDigitsOnly(obj.toString())) ? ClientTraceData.b.f47a : (double) Integer.parseInt(obj.toString());
                this.mSize = (size < 102400.0d || size > 2.097152E8d) ? -1 : (int) size;
            }
        } catch (Exception e) {
            this.mSize = -1;
        }
        return this.mSize;
    }

    public String getDefinition() {
        if (TextUtils.isEmpty(this.mDefinition) && this.mData != null) {
            Object obj = this.mData.opt("definition");
            this.mDefinition = obj == null ? null : obj.toString();
        }
        return this.mDefinition;
    }
}
