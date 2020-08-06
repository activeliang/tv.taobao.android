package com.yunos.tvtaobao.biz.request.base;

import android.content.Context;
import android.taobao.windvane.connect.api.ApiResponse;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.bftv.fui.constantplugin.Constant;
import com.tvtao.user.dclib.ZPDevice;
import com.ut.device.UTDevice;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.config.AppInfo;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.MtopRequestConfig;
import com.yunos.tv.core.util.DataEncoder;
import com.yunos.tv.core.util.DeviceUtil;
import com.yunos.tvtaobao.biz.request.core.DataRequest;
import com.yunos.tvtaobao.biz.request.core.DataRequestType;
import com.yunos.tvtaobao.biz.request.core.ServiceCode;
import com.yunos.tvtaobao.biz.request.core.ServiceResponse;
import com.yunos.tvtaobao.payment.utils.TvTaoUtils;
import com.zhiping.dev.android.logger.ZpLogger;
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

    public String getCustomDomain() {
        return null;
    }

    public void addParams(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            this.dataParams.put(key, value);
        }
    }

    public void setParamsData() {
        initialize();
        try {
            if (this.dataParams != null) {
                if (this.dataParams.containsKey("extParams")) {
                    Map map = JSON.parseObject((String) this.dataParams.get("extParams"));
                    if (!map.containsKey("uuid")) {
                        map.put("uuid", CloudUUIDWrapper.getCloudUUID());
                    }
                    if (!map.containsKey("wua")) {
                        map.put("wua", Config.getWua(CoreApplication.getApplication()));
                    }
                    if (!map.containsKey("utdid")) {
                        map.put("utdid", UTDevice.getUtdid(CoreApplication.getApplication()));
                    }
                    if (!map.containsKey("umtoken")) {
                        map.put("umtoken", TvTaoUtils.getUmtoken(CoreApplication.getApplication()));
                    }
                    String zpDid = ZPDevice.getZpDid((Context) null);
                    String uid = ZPDevice.getAugurZpId((Context) null);
                    if (!map.containsKey("zpDid")) {
                        map.put("zpDid", zpDid);
                    }
                    if (!map.containsKey("augurZpUid")) {
                        map.put("augurZpUid", uid);
                    }
                    if (!map.containsKey("versionName")) {
                        map.put("versionName", AppInfo.getAppVersionName());
                    }
                    if (!map.containsKey("appkey")) {
                        map.put("appkey", Config.getChannel());
                    }
                    if (!map.containsKey("isSimulator")) {
                        map.put("isSimulator", Boolean.valueOf(Config.isSimulator(CoreApplication.getApplication())));
                    }
                    if (!map.containsKey("subkey")) {
                        map.put("subkey", SharePreferences.getString("hy_device_subkey", ""));
                    }
                    if (!map.containsKey("userAgent")) {
                        map.put("userAgent", Config.getAndroidSystem(CoreApplication.getApplication()));
                    }
                    if (!map.containsKey("mac")) {
                        map.put("mac", DeviceUtil.getStbID());
                    }
                    this.dataParams.put("extParams", map.toString());
                    ZpLogger.e(this.TAG, "containsKey,extParams:" + this.dataParams.toString());
                } else {
                    JSONObject job = new JSONObject();
                    job.put("uuid", CloudUUIDWrapper.getCloudUUID());
                    job.put("wua", Config.getWua(CoreApplication.getApplication()));
                    job.put("utdid", UTDevice.getUtdid(CoreApplication.getApplication()));
                    job.put("umtoken", TvTaoUtils.getUmtoken(CoreApplication.getApplication()));
                    String zpDid2 = ZPDevice.getZpDid((Context) null);
                    String uid2 = ZPDevice.getAugurZpId((Context) null);
                    if (!TextUtils.isEmpty(zpDid2)) {
                        job.put("zpDid", zpDid2);
                    }
                    if (!TextUtils.isEmpty(uid2)) {
                        job.put("augurZpUid", uid2);
                    }
                    job.put("versionName", AppInfo.getAppVersionName());
                    job.put("appkey", Config.getChannel());
                    job.put("isSimulator", Config.isSimulator(CoreApplication.getApplication()));
                    job.put("subkey", SharePreferences.getString("hy_device_subkey", ""));
                    job.put("userAgent", Config.getAndroidSystem(CoreApplication.getApplication()));
                    job.put("mac", DeviceUtil.getStbID());
                    this.dataParams.put("extParams", job.toString());
                    ZpLogger.e(this.TAG, "NOTcontainsKey,extParams:" + this.dataParams.toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        ZpLogger.v(this.TAG, this.TAG + ".getHttpParams sb = " + sb.toString());
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String getHttpDomain() {
        return MtopRequestConfig.getHttpDomain();
    }

    public String getTTid() {
        return Config.getTTid();
    }

    public JSONObject getDataElement(String json) {
        if (json == null) {
            return null;
        }
        try {
            return new JSONObject(json).optJSONObject("data");
        } catch (JSONException e) {
            return null;
        }
    }

    public <T> ServiceResponse<T> resolveResponse(String response) throws Exception {
        ServiceResponse<T> serviceResponse = new ServiceResponse<>();
        if (getApi().equals("mtop.taobao.tvtao.itemservice.getdetail")) {
            ZpLogger.e("GetProductTagRequest", "response = " + response);
        }
        JSONArray retArray = new JSONObject(response).optJSONArray("ret");
        if (retArray != null && retArray.length() > 0 && !retArray.toString().contains("SUCCESS")) {
            if (retArray.length() == 1) {
                String[] flagStatus = retArray.getString(0).split("::");
                if (flagStatus.length == 1) {
                    if (flagStatus[0].equals("FAIL_BIZ_PERSON_LIMIT_EXCEED")) {
                        serviceResponse.update(ServiceCode.FAIL_BIZ_PERSON_LIMIT_EXCEED.getCode(), flagStatus[0], ServiceCode.FAIL_BIZ_PERSON_LIMIT_EXCEED.getMsg());
                    } else {
                        serviceResponse.update(ServiceCode.API_ERROR.getCode(), flagStatus[0], ServiceCode.API_ERROR.getMsg());
                    }
                } else if (flagStatus[1].equals("userNotLogin")) {
                    serviceResponse.update(ServiceCode.API_NOT_LOGIN.getCode(), "userNotLogin", ServiceCode.API_NOT_LOGIN.getMsg());
                } else if (flagStatus[0].equals(ApiResponse.ERR_SID_INVALID) || flagStatus[0].equals(ErrorConstant.ERRCODE_FAIL_SYS_SESSION_EXPIRED) || flagStatus[0].equals("FAIL_SYS_ILEGEL_SIGN")) {
                    serviceResponse.update(ServiceCode.API_SID_INVALID.getCode(), ApiResponse.ERR_SID_INVALID, flagStatus[1]);
                } else if (flagStatus[0].equals("FAIL_SYS_HSF_THROWN_EXCEPTION")) {
                    serviceResponse.update(ServiceCode.USER_LOGIN_OTHER_FAIL.getCode(), "FAIL_SYS_HSF_THROWN_EXCEPTION", flagStatus[1]);
                } else if (flagStatus[0].equals("ERRCODE_AUTH_REJECT")) {
                    serviceResponse.update(ServiceCode.API_ERRCODE_AUTH_REJECT.getCode(), "ERRCODE_AUTH_REJECT", flagStatus[1]);
                } else if (flagStatus[0].equals("ADDRESS_EMPTY") || flagStatus[0].equals("NO_ADDRESS")) {
                    serviceResponse.update(ServiceCode.NO_ADDRESS.getCode(), "ADDRESS_EMPTY", flagStatus[1]);
                } else if (flagStatus[0].equals("NO_RATES")) {
                    serviceResponse.update(ServiceCode.SERVICE_OK.getCode(), "NO_RATES", flagStatus[1]);
                } else if (flagStatus[0].equals("ERROR_PARAM_DEVICE_ID") || flagStatus[0].equals("ERROR_BIZ_NO_DEVICE_ID") || flagStatus[0].equals("ERROR_DEVICE_NOT_FOUND") || flagStatus[0].equals("ERROR_INVALID_DEVICE_ID") || flagStatus[0].equals("ERROR_INVALID_PUSH_KEY") || flagStatus[0].equals("ERROR_PARAM_PUSH_TOKEN")) {
                    serviceResponse.update(ServiceCode.PUSH_MESSAGE_ERROR_DEVICE_ID.getCode(), "PUSH_MESSAGE_ERROR_DEVICE_ID", flagStatus[1]);
                } else if (flagStatus[0].equals("1142")) {
                    serviceResponse.update(ServiceCode.REPEAT_CREATE.getCode(), "REPEAT_CREATE", ServiceCode.REPEAT_CREATE.getMsg());
                } else if (flagStatus[0].equals("CREATE_ALIPAY_ORDER_ERROR")) {
                    serviceResponse.update(ServiceCode.CREATE_ALIPAY_ORDER_ERROR.getCode(), "CREATE_ALIPAY_ORDER_ERROR", ServiceCode.CREATE_ALIPAY_ORDER_ERROR.getMsg());
                } else if (flagStatus[0].equals("NEED_CHECK_CODE")) {
                    serviceResponse.update(ServiceCode.USER_NEED_CHECK_CODE.getCode(), "NEED_CHECK_CODE", flagStatus[1]);
                } else if (flagStatus[0].equals("BUYER_TOO_MANY_UNPAID_ORDERS")) {
                    serviceResponse.update(ServiceCode.ORDER_MORE.getCode(), "BUYER_TOO_MANY_UNPAID_ORDERS", flagStatus[1]);
                } else if (flagStatus[0].equals("DUPLICATED_ORDER_ERROR")) {
                    serviceResponse.update(ServiceCode.DUPLICATED_ORDER_ERROR.getCode(), "DUPLICATED_ORDER_ERROR", flagStatus[1]);
                } else if (flagStatus[0].equals("ADD_CART_FAILURE")) {
                    serviceResponse.update(ServiceCode.ADD_CART_FAILURE.getCode(), "ADD_CART_FAILURE", flagStatus[1]);
                } else if (flagStatus[0].equals("BONUS_BALANCES_INSUFFICIENT")) {
                    serviceResponse.update(ServiceCode.BONUS_BALANCES_INSUFFICIENT.getCode(), "BONUS_BALANCES_INSUFFICIENT", flagStatus[1]);
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
