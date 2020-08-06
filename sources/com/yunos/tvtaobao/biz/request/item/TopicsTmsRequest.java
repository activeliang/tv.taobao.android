package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseHttpRequest;
import com.yunos.tvtaobao.biz.request.bo.TopicsEntity;
import java.util.Map;

public class TopicsTmsRequest extends BaseHttpRequest {
    private static final long serialVersionUID = 4723801614094686062L;
    private String url = "";

    public TopicsTmsRequest(String url2) {
        this.url = url2;
    }

    /* access modifiers changed from: protected */
    public String getHttpDomain() {
        return this.url;
    }

    public TopicsEntity resolveResult(String response) throws Exception {
        return (TopicsEntity) JSON.parseObject(response, TopicsEntity.class);
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
