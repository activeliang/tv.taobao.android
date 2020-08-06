package com.taobao.mtop.api;

import java.io.Serializable;
import java.util.Map;

public class MtopRequest implements Serializable {
    public String API_NAME;
    public String VERSION;
    public boolean needEcode;
    public boolean needLogin;
    public Map<String, String> params;
}
