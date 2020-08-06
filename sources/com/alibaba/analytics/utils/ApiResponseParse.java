package com.alibaba.analytics.utils;

import android.text.TextUtils;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiResponseParse {
    private static final String TAG_ERRORMSG = "ret";
    private static final String TAG_SUCCESS = "success";

    public static BizResponse parseResult(String lContent) {
        BizResponse response = new BizResponse();
        try {
            JSONObject lJson = new JSONObject(lContent);
            if (lJson.has("success")) {
                String lValue = lJson.getString("success");
                if (!TextUtils.isEmpty(lValue) && lValue.equals("success")) {
                    response.isSuccess = true;
                }
            }
            if (lJson.has("ret")) {
                response.bizCode = lJson.getString("ret");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static class BizResponse {
        public static BizResponse defaultResponse = new BizResponse();
        public String bizCode = null;
        public String errCode;
        public boolean isSuccess = false;
        public int receiveLen = 0;
        public double rt = ClientTraceData.b.f47a;

        public boolean isSignError() {
            if ("E0102".equalsIgnoreCase(this.bizCode)) {
                return true;
            }
            return false;
        }

        public boolean isParamError() {
            if ("E0101".equalsIgnoreCase(this.bizCode)) {
                return true;
            }
            return false;
        }

        public boolean isNotFoundSecret() {
            if ("E0111".equalsIgnoreCase(this.bizCode) || "E0112".equalsIgnoreCase(this.bizCode)) {
                return true;
            }
            return false;
        }
    }
}
