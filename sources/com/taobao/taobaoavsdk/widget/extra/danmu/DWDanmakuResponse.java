package com.taobao.taobaoavsdk.widget.extra.danmu;

import com.taobao.taobaoavsdk.IAVObject;
import org.json.JSONObject;

public class DWDanmakuResponse implements IAVObject {
    public JSONObject data;
    public String errorCode;
    public String errorMsg;
    public int httpCode;
}
