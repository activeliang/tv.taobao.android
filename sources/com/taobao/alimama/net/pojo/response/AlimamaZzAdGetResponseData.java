package com.taobao.alimama.net.pojo.response;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import mtopsdk.mtop.domain.IMTOPDataObject;

public class AlimamaZzAdGetResponseData implements IMTOPDataObject {
    @JSONField(name = "httpStatusCode")
    public String httpStatusCode;
    @JSONField(name = "model")
    public List<AlimamaZzAd> model;
}
