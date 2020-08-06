package com.taobao.taobaoavsdk.widget.extra.danmu;

import com.taobao.taobaoavsdk.IAVObject;
import java.util.Map;

public class DWDanmakuRequest implements IAVObject {
    public String apiName;
    public String apiVersion;
    public boolean needLogin;
    public Map<String, String> paramMap;
    public Class<?> responseClass;
}
