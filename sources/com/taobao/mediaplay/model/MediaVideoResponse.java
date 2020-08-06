package com.taobao.mediaplay.model;

import java.io.Serializable;
import org.json.JSONObject;

public class MediaVideoResponse implements Serializable {
    public JSONObject data;
    public String errorCode;
    public String errorMsg;
}
