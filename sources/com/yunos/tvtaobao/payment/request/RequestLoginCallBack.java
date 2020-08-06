package com.yunos.tvtaobao.payment.request;

import com.yunos.tvtaobao.biz.common.BaseConfig;
import mtopsdk.mtop.domain.MtopRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestLoginCallBack extends MtopRequest {
    private static final String API = "mtop.tvtao.genesis.userloginout.callback";
    private static final String API_VERSION = "1.0";

    public RequestLoginCallBack(String uid, String extParams, boolean isLogin) {
        setApiName(API);
        setVersion("1.0");
        JSONObject data = new JSONObject();
        try {
            data.put("tbUid", uid);
            if (isLogin) {
                data.put("opt", "login");
            } else {
                data.put("opt", BaseConfig.INTENT_KEY_LOGOUT);
            }
            data.put("extParams", extParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setData(data.toString());
    }
}
