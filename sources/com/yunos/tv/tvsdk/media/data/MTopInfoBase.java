package com.yunos.tv.tvsdk.media.data;

import com.yunos.tv.tvsdk.media.error.IMediaError;
import java.io.Serializable;
import org.json.JSONException;

public abstract class MTopInfoBase implements Serializable, IMediaMTopInfo {
    private static final long serialVersionUID = -8718341289925671163L;
    IMediaError mMediaError;

    /* access modifiers changed from: protected */
    public abstract void parseFromJson(String str) throws JSONException;

    public IMediaError getErrorInfo() {
        return this.mMediaError;
    }

    public void setMediaError(IMediaError mMediaError2) {
        this.mMediaError = mMediaError2;
    }
}
