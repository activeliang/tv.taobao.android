package com.yunos.tv.blitz.request.base;

import android.taobao.windvane.connect.api.ApiResponse;
import android.text.TextUtils;
import android.util.Log;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tv.blitz.request.common.AppDebug;
import com.yunos.tv.blitz.request.common.MtopRequestConfig;
import com.yunos.tv.blitz.request.core.DataRequest;
import com.yunos.tv.blitz.request.core.DataRequestType;
import com.yunos.tv.blitz.request.core.ServiceCode;
import com.yunos.tv.blitz.request.core.ServiceResponse;
import com.yunos.tv.blitz.request.util.DataEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mtopsdk.mtop.util.ErrorConstant;
import mtopsdk.mtop.util.ReflectUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseMtopRequest extends DataRequest {
    private static final long serialVersionUID = -8470193626187609708L;

    public abstract boolean getNeedEcode();

    public abstract boolean getNeedSession();

    /* access modifiers changed from: protected */
    public abstract <T> T resolveResponse(JSONObject jSONObject) throws Exception;

    public BaseMtopRequest() {
        this.dataParams = new HashMap();
    }

    public void addParams(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            this.dataParams.put(key, value);
        }
    }

    public void setParamsData() {
        initialize();
        setData(ReflectUtil.converMapToDataStr(this.dataParams));
    }

    /* access modifiers changed from: protected */
    public void initialize() {
        if (!TextUtils.isEmpty(getApi())) {
            setApiName(getApi());
        }
        if (!TextUtils.isEmpty(getApiVersion())) {
            setVersion(getApiVersion());
        }
        setNeedEcode(getNeedEcode());
        setNeedSession(getNeedSession());
        Map<String, String> param = getAppData();
        if (param != null) {
            this.dataParams.putAll(param);
        }
    }

    public DataRequestType getRequestType() {
        return DataRequestType.MTOP;
    }

    /* access modifiers changed from: protected */
    public List<? extends NameValuePair> getPostParameters() {
        initialize();
        List<BasicNameValuePair> paramList = new ArrayList<>();
        for (Map.Entry<String, String> entry : this.dataParams.entrySet()) {
            paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return paramList;
    }

    /* access modifiers changed from: protected */
    public String getHttpParams() {
        initialize();
        StringBuilder sb = new StringBuilder();
        int len = this.dataParams.size();
        for (Map.Entry<String, String> entry : this.dataParams.entrySet()) {
            sb.append(entry.getKey()).append("=").append(DataEncoder.encodeUrl(DataEncoder.decodeUrl(entry.getValue())));
            if (0 < len - 1) {
                sb.append("&");
            }
        }
        AppDebug.v(this.TAG, this.TAG + ".getHttpParams sb = " + sb.toString());
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String getHttpDomain() {
        return MtopRequestConfig.getHttpDomain();
    }

    public JSONObject getDataElement(String json) {
        if (json == null) {
            return null;
        }
        JSONObject data = null;
        try {
            data = new JSONObject(json).optJSONObject("data");
            Log.d(this.TAG, "getDataElement " + data.toString());
            return data;
        } catch (JSONException e) {
            return data;
        }
    }

    public <T> ServiceResponse<T> resolveResponse(String response) throws Exception {
        ServiceResponse<T> serviceResponse = new ServiceResponse<>();
        JSONArray retArray = new JSONObject(response).optJSONArray("ret");
        if (retArray != null) {
            serviceResponse.setRetArray(retArray);
        }
        if (retArray != null && retArray.length() > 0 && !retArray.toString().contains("SUCCESS")) {
            if (retArray.length() == 1) {
                String[] flagStatus = retArray.getString(0).split("::");
                if (flagStatus.length == 1) {
                    serviceResponse.update(ServiceCode.API_ERROR.getCode(), flagStatus[0], ServiceCode.API_ERROR.getMsg());
                } else if (flagStatus[0].equals(ApiResponse.ERR_SID_INVALID) || flagStatus[0].equals(ErrorConstant.ERRCODE_FAIL_SYS_SESSION_EXPIRED)) {
                    serviceResponse.update(ServiceCode.API_SID_INVALID.getCode(), ApiResponse.ERR_SID_INVALID, flagStatus[1]);
                } else if (flagStatus[0].equals("FAIL_SYS_HSF_THROWN_EXCEPTION")) {
                    serviceResponse.update(ServiceCode.USER_LOGIN_OTHER_FAIL.getCode(), "FAIL_SYS_HSF_THROWN_EXCEPTION", flagStatus[1]);
                } else if (flagStatus[0].equals("ERRCODE_AUTH_REJECT") || flagStatus[0].equals("FAIL_SYS_ILEGEL_SIGN")) {
                    serviceResponse.update(ServiceCode.API_ERRCODE_AUTH_REJECT.getCode(), "ERRCODE_AUTH_REJECT", flagStatus[1]);
                } else {
                    serviceResponse.update(ServiceCode.API_ERROR.getCode(), flagStatus[0], (TextUtils.isEmpty(flagStatus[1]) || flagStatus[1].equals(Constant.NULL)) ? ServiceCode.API_ERROR.getMsg() : flagStatus[1]);
                }
            } else {
                String[] errCodes = retArray.getString(1).split("::");
                String[] errMsgs = retArray.getString(0).split("::");
                if (errCodes.length < 2 || !ApiResponse.ERR_CODE.equals(errCodes[0])) {
                    serviceResponse.update(ServiceCode.API_ERROR.getCode(), "api_error", ServiceCode.API_ERROR.getMsg());
                } else {
                    ServiceCode serviceCode = ServiceCode.valueOf(errCodes[1]);
                    int errorCodeValue = ServiceCode.API_ERROR.getCode();
                    String errorMsg = ServiceCode.API_ERROR.getMsg();
                    if (serviceCode != null) {
                        errorCodeValue = serviceCode.getCode();
                        errorMsg = serviceCode.getMsg();
                    } else if (errMsgs.length >= 2) {
                        errorMsg = errMsgs[1];
                    }
                    serviceResponse.update(errorCodeValue, errCodes[1], errorMsg);
                }
            }
        }
        JSONObject dataElement = getDataElement(response);
        if (serviceResponse.isSucess()) {
            T t = null;
            if (!(dataElement == null || dataElement.length() == 0)) {
                t = resolveResponse(dataElement);
            }
            serviceResponse.setData(t);
            serviceResponse.setStatusCode(Integer.valueOf(ServiceCode.SERVICE_OK.getCode()));
        } else if (!(dataElement == null || dataElement.length() == 0)) {
            serviceResponse.setData(resolveResponse(dataElement));
        }
        return serviceResponse;
    }
}
