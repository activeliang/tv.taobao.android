package com.tvtao.user.dclib.impl;

import com.alibaba.baichuan.trade.common.adapter.mtop.NetworkRequest;
import java.util.HashMap;
import org.json.JSONObject;

public class ReportRequest extends NetworkRequest {
    public ReportRequest() {
        this.apiName = "mtop.tvtao.genesis.devicestartup.reportInfo";
        this.apiVersion = "1.0";
        this.paramMap = new HashMap();
    }

    public void setParams(JSONObject params) {
        try {
            this.paramMap.put("params", params.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setExtParams(JSONObject params) {
        try {
            this.paramMap.put("extParams", params.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
