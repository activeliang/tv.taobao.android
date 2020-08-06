package com.yunos.tv.tvsdk.media.data;

import com.yunos.tv.tvsdk.media.error.IMediaError;
import org.json.JSONObject;

public interface IMediaMTopInfo {
    JSONObject convertToJSObject();

    Object getDataResult();

    IMediaError getErrorInfo();

    boolean isDataEmpty();
}
