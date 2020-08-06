package com.taobao.mtop.api;

import java.io.Serializable;
import java.util.Map;

public class ApiResponse implements Serializable {
    public String actionText;
    public String actionUrl;
    public String btnColor;
    public boolean btnDisable;
    public String btnText;
    public String errorMessage;
    public boolean isSuccess;
    public Map<String, String> params;
}
