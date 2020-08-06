package com.taobao.alimama.net.pojo.request;

import mtopsdk.mtop.domain.IMTOPDataObject;

public class AlimamaTkQueryChanneleRequest implements IMTOPDataObject {
    public String API_NAME = "mtop.taobao.union.qogir.querychannele";
    public boolean NEED_ECODE = false;
    public boolean NEED_SESSION = false;
    public String VERSION = "1.0";
    public String dynamicChannelId = null;
    public String extraInfo = null;
}
