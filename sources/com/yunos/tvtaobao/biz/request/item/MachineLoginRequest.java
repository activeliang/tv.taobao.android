package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseHttpRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

public class MachineLoginRequest extends BaseHttpRequest {
    private String appKey = "";
    private String appSecret = "alitv";
    private String machineId = "";
    private String sign = "";

    public MachineLoginRequest(String appKey2, String machineId2) {
        this.appKey = appKey2;
        this.machineId = machineId2;
    }

    public String resolveResult(String result) throws Exception {
        return result;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        Map<String, String> params = new TreeMap<>();
        params.put("app_key", this.appKey);
        params.put("user_id", this.machineId);
        params.put("sign", createSign(params));
        return params;
    }

    /* access modifiers changed from: protected */
    public String getHttpDomain() {
        return "http://218.108.132.66:7430/api/v2/alitv/get_session";
    }

    /* access modifiers changed from: protected */
    public String createSign(Map<String, String> params) {
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(this.appSecret);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sbBuffer.append(entry.getKey()).append(entry.getValue());
        }
        sbBuffer.append(this.appSecret);
        this.sign = getMD5(sbBuffer.toString());
        return this.sign;
    }

    private String getMD5(String str) {
        String reStr = null;
        try {
            byte[] bytes = MessageDigest.getInstance("MD5").digest(str.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bytes) {
                int bt = b & 255;
                if (bt < 16) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(bt));
            }
            reStr = stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return reStr.toUpperCase();
    }
}
