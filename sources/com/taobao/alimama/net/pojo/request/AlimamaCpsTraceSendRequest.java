package com.taobao.alimama.net.pojo.request;

import mtopsdk.mtop.domain.IMTOPDataObject;

public class AlimamaCpsTraceSendRequest implements IMTOPDataObject {
    public String API_NAME = "mtop.alimama.cps.trace.adsdk.send";
    public boolean NEED_ECODE = false;
    public boolean NEED_SESSION = false;
    public String VERSION = "1.0";
    public String emap = null;
    public String ext = null;
    public long ismall = 0;
    public long itemid = 0;
    public long sellerid = 0;
    public String utdid = null;
}
