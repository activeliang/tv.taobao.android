package com.tvtao.user.dclib.impl;

import mtopsdk.mtop.domain.MtopRequest;

public class ReportInnerRequest extends MtopRequest {
    private static final String API = "mtop.tvtao.genesis.devicestartup.reportInfo";
    private static final String API_VERSION = "1.0";

    public ReportInnerRequest(String params) {
        setApiName(API);
        setVersion("1.0");
        setData(params);
    }
}
