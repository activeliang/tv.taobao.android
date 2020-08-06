package com.taobao.mtop.api;

import java.io.Serializable;

public class Request implements Serializable {
    public String API_NAME;
    public String VERSION;
    public String exParams;
    public String itemNumId;
    public boolean needEcode;
    public boolean needLogin;
    public boolean wua;
}
