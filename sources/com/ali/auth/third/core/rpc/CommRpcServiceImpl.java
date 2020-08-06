package com.ali.auth.third.core.rpc;

import android.text.TextUtils;
import com.ali.auth.third.core.model.RpcRequest;
import com.ali.auth.third.core.model.RpcRequestCallbackWithCode;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.core.service.RpcService;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.JSONUtils;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

public class CommRpcServiceImpl implements RpcService {
    public String invoke(RpcRequest request) {
        try {
            String api = request.target;
            String version = request.version;
            JSONArray json = new JSONArray();
            Iterator<Object> it = request.paramValues.iterator();
            while (it.hasNext()) {
                json.put(it.next());
            }
            return HttpConnectionHelper.post(api, version, json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> RpcResponse<T> invoke(RpcRequest rpcRequest, Class<T> resultType) {
        try {
            String api = rpcRequest.target;
            String version = rpcRequest.version;
            JSONArray json = new JSONArray();
            Iterator<Object> it = rpcRequest.paramValues.iterator();
            while (it.hasNext()) {
                json.put(it.next());
            }
            String response = HttpConnectionHelper.post(api, version, json.toString());
            SDKLogger.d("", "post response = " + response);
            JSONObject jsonObject = new JSONObject(response);
            RpcResponse<T> rpcResponse = new RpcResponse<>();
            rpcResponse.code = jsonObject.optInt("code");
            rpcResponse.message = jsonObject.optString("message");
            rpcResponse.codeGroup = jsonObject.optString("codeGroup");
            rpcResponse.msgCode = jsonObject.optString("msgCode");
            rpcResponse.msgInfo = jsonObject.optString(BlitzServiceUtils.CMSG_INFO);
            rpcResponse.actionType = jsonObject.optString("actionType");
            if (TextUtils.isEmpty(jsonObject.optString("returnValue"))) {
                return rpcResponse;
            }
            rpcResponse.returnValue = JSONUtils.parseStringValue(jsonObject.optString("returnValue"), resultType);
            return rpcResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> void remoteBusiness(RpcRequest request, Class<T> cls, RpcRequestCallbackWithCode rpcRequestCallback) {
        if (rpcRequestCallback != null) {
            rpcRequestCallback.onError("usage not support.", (RpcResponse) null);
        }
    }

    public void registerSessionInfo(String sid, String userId) {
    }

    public String getDeviceId() {
        return "";
    }

    public void logout() {
    }
}
