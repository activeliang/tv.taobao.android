package com.taobao.alimama.net.pojo.request;

import mtopsdk.mtop.domain.IMTOPDataObject;

public class AlimamaZzAdGetRequest implements IMTOPDataObject {
    public String API_NAME = "mtop.alimama.zz.ad.get";
    public boolean NEED_ECODE = false;
    public boolean NEED_SESSION = false;
    public String VERSION = "1.0";
    public String X_Client_Scheme;
    public String app_version = null;
    public String ip = null;
    public String pid = "";
    public String pvid = null;
    public String pvoff = null;
    public String retry;
    public String scene;
    public String st = null;
    public String userid = null;
    public String utdid = null;
}
