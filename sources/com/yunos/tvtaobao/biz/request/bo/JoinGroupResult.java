package com.yunos.tvtaobao.biz.request.bo;

import com.yunos.tv.core.util.DataEncoder;
import com.yunos.tv.core.util.SystemUtil;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.Serializable;
import org.json.JSONObject;

public class JoinGroupResult implements Serializable {
    private static final long serialVersionUID = 6668095838315101627L;
    private Long bizOrderId;
    private String key;
    private String message;
    private int resultCode;
    private String url;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key2) {
        this.key = key2;
    }

    public Long getBizOrderId() {
        return this.bizOrderId;
    }

    public void setBizOrderId(Long bizOrderId2) {
        this.bizOrderId = bizOrderId2;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static JoinGroupResult fromMTOP(JSONObject dataObj) throws Exception {
        JoinGroupResult item = new JoinGroupResult();
        ZpLogger.v("", "the jhs key is:" + dataObj.optString("key"));
        item.setKey(DataEncoder.decodeUrl(dataObj.optString("key")));
        ZpLogger.v("", "the jhs key decode is:" + item.getKey());
        ZpLogger.v("", "the jhs key encode is:" + SystemUtil.encodeUrl(item.getKey()));
        item.setUrl(dataObj.optString("url"));
        item.setResultCode(dataObj.optInt("resultCode"));
        item.setMessage(dataObj.optString("message"));
        return item;
    }

    public int getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(int resultCode2) {
        this.resultCode = resultCode2;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }
}
