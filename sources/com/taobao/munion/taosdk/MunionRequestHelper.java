package com.taobao.munion.taosdk;

import android.content.Context;
import android.support.annotation.Keep;
import android.text.TextUtils;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.taobao.alimama.net.pojo.request.AlimamaZzAdGetRequest;
import com.taobao.alimama.net.pojo.request.SendCpcInfoRequest;
import com.taobao.alimama.net.pojo.request.SendCpmInfoRequest;
import com.taobao.alimama.net.pojo.request.SendCpsInfoRequest;
import com.taobao.muniontaobaosdk.util.MunionDeviceUtil;
import com.taobao.utils.Global;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import mtopsdk.mtop.domain.IMTOPDataObject;

@Keep
public class MunionRequestHelper {
    private static IMTOPDataObject addExInfoForRequest(IMTOPDataObject iMTOPDataObject, Map<String, String> map) {
        if (map != null && !map.isEmpty()) {
            try {
                for (Field field : iMTOPDataObject.getClass().getFields()) {
                    String name = field.getName();
                    JSONField jSONField = (JSONField) field.getAnnotation(JSONField.class);
                    String str = map.get((jSONField == null || TextUtils.isEmpty(jSONField.name())) ? name : jSONField.name());
                    if (str != null) {
                        try {
                            field.set(iMTOPDataObject, str);
                        } catch (Exception e) {
                        }
                    }
                }
            } catch (Exception e2) {
            }
        }
        return iMTOPDataObject;
    }

    public static SendCpcInfoRequest getCpcInfoRequest(Context context, String str, String str2) {
        SendCpcInfoRequest sendCpcInfoRequest = new SendCpcInfoRequest();
        sendCpcInfoRequest.setCna("");
        sendCpcInfoRequest.setExt("");
        sendCpcInfoRequest.setReferer("");
        sendCpcInfoRequest.setUtkey("");
        sendCpcInfoRequest.setUtsid("");
        sendCpcInfoRequest.setHost("");
        sendCpcInfoRequest.setE(str2);
        sendCpcInfoRequest.setUtdid(MunionDeviceUtil.getUtdid(context));
        sendCpcInfoRequest.setAccept(MunionDeviceUtil.getAccept(context, str));
        sendCpcInfoRequest.setClickid(str);
        return sendCpcInfoRequest;
    }

    public static SendCpmInfoRequest getCpmInfoRequest(Context context, String str, String str2) {
        SendCpmInfoRequest sendCpmInfoRequest = new SendCpmInfoRequest();
        sendCpmInfoRequest.setCna("");
        sendCpmInfoRequest.setE(str2);
        sendCpmInfoRequest.setExt("");
        sendCpmInfoRequest.setReferer("");
        sendCpmInfoRequest.setUtdid(MunionDeviceUtil.getUtdid(context));
        sendCpmInfoRequest.setAccept(MunionDeviceUtil.getAccept(context, str));
        sendCpmInfoRequest.setUseragent(MunionDeviceUtil.getUserAgent());
        sendCpmInfoRequest.setClickid(str);
        return sendCpmInfoRequest;
    }

    public static SendCpsInfoRequest getCpsInfoRequest(Context context, String str, String str2, String str3, int i, MunionParameterHolder munionParameterHolder) {
        SendCpsInfoRequest sendCpsInfoRequest = new SendCpsInfoRequest();
        sendCpsInfoRequest.setSid(str3);
        sendCpsInfoRequest.setSellerid(str);
        sendCpsInfoRequest.setItemid(str2);
        sendCpsInfoRequest.setShopid("0");
        sendCpsInfoRequest.setIsmall(i);
        sendCpsInfoRequest.setE(munionParameterHolder.getParameter("e"));
        sendCpsInfoRequest.setCna(munionParameterHolder.getParameter("cna"));
        sendCpsInfoRequest.setExt(munionParameterHolder.getParameter("ext"));
        sendCpsInfoRequest.setReferer(munionParameterHolder.getParameter(RequestParameters.SUBRESOURCE_REFERER));
        sendCpsInfoRequest.setUnid(munionParameterHolder.getParameter("unid"));
        sendCpsInfoRequest.setUtdid(MunionDeviceUtil.getUtdid(context));
        sendCpsInfoRequest.setAccept(MunionDeviceUtil.getAccept(context, (String) null));
        return sendCpsInfoRequest;
    }

    public static AlimamaZzAdGetRequest getZzAdGetRequest(Context context, String str, String[] strArr, Map<String, String> map) {
        AlimamaZzAdGetRequest alimamaZzAdGetRequest = new AlimamaZzAdGetRequest();
        alimamaZzAdGetRequest.pid = TextUtils.join(",", Arrays.asList(strArr));
        alimamaZzAdGetRequest.pvid = UUID.randomUUID().toString().replaceAll("-", "");
        alimamaZzAdGetRequest.st = "android_native";
        if (str == null) {
            str = "";
        }
        alimamaZzAdGetRequest.userid = str;
        alimamaZzAdGetRequest.app_version = Global.getVersionName(context);
        alimamaZzAdGetRequest.utdid = MunionDeviceUtil.getUtdid(context);
        alimamaZzAdGetRequest.X_Client_Scheme = "https";
        return (AlimamaZzAdGetRequest) addExInfoForRequest(alimamaZzAdGetRequest, map);
    }
}
