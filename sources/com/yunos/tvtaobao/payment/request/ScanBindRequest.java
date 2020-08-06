package com.yunos.tvtaobao.payment.request;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import com.yunos.tvtaobao.payment.utils.CloudUUIDWrapper;
import mtopsdk.mtop.domain.MtopRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class ScanBindRequest extends MtopRequest {
    private static String appKey;
    private String API = "mtop.taobao.tvtao.user.bind";
    private String API_VERSION = "1.0";

    public ScanBindRequest(Context context) {
        setApiName(this.API);
        setVersion(this.API_VERSION);
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        JSONObject data = new JSONObject();
        try {
            data.put("uuid", CloudUUIDWrapper.getCloudUUID());
            if (ActivityCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") == 0) {
                data.put("deviceId", tm.getDeviceId());
                data.put("appKey", appKey);
                JSONObject jo = new JSONObject();
                jo.put("uuid", CloudUUIDWrapper.getCloudUUID());
                data.put("extParams", jo.toString());
                setData(data.toString());
                setNeedEcode(false);
                setNeedSession(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setAppKey(String appKeyName) {
        appKey = appKeyName;
    }
}
