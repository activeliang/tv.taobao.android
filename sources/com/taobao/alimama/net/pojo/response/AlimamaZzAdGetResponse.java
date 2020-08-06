package com.taobao.alimama.net.pojo.response;

import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.IMTOPDataObject;

public class AlimamaZzAdGetResponse extends BaseOutDo implements IMTOPDataObject {
    private AlimamaZzAdGetResponseData data;

    public AlimamaZzAdGetResponseData getData() {
        return this.data;
    }

    public void setData(AlimamaZzAdGetResponseData alimamaZzAdGetResponseData) {
        this.data = alimamaZzAdGetResponseData;
    }
}
