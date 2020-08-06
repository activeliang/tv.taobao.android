package com.zhiping.tvtao.payment.alipay.request;

import com.zhiping.tvtao.payment.alipay.request.base.BaseMtopRequest;

public class GetAlipayAccountRequest extends BaseMtopRequest {
    private String API_NAME = "mtop.taobao.tvtao.aliuserservice.getAlipayAccount";
    private boolean NEED_ECODE = false;
    private boolean NEED_SESSION = true;
    private String VERSION = "1.0";

    public GetAlipayAccountRequest() {
        setNeedEcode(this.NEED_ECODE);
        setNeedLogin(this.NEED_SESSION);
    }

    public String getApi() {
        return this.API_NAME;
    }

    public String getApiVersion() {
        return this.VERSION;
    }
}
