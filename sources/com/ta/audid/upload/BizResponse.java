package com.ta.audid.upload;

import android.text.TextUtils;
import com.ta.audid.Variables;
import com.ta.audid.filesync.UtdidBroadcastMgr;
import com.ta.audid.utils.UtdidLogger;
import org.json.JSONException;
import org.json.JSONObject;

public class BizResponse {
    private static final String TAG_AUDID = "audid";
    private static final String TAG_CODE = "code";
    private static final String TAG_DATA = "data";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_UTDID = "utdid";
    public int code = -1;
    public String message = "";

    public static BizResponse parseResult(String content) {
        JSONObject dataJson;
        BizResponse response = new BizResponse();
        try {
            JSONObject json = new JSONObject(content);
            if (json.has("code")) {
                response.code = json.getInt("code");
            }
            if (json.has("message")) {
                response.message = json.getString("message");
            }
            if (json.has("data") && (dataJson = json.getJSONObject("data")) != null) {
                if (dataJson.has("audid")) {
                    String audid = dataJson.getString("audid");
                    if (!TextUtils.isEmpty(audid)) {
                        UtdidKeyFile.writeAudidFile(audid);
                    }
                }
                if (dataJson.has("utdid")) {
                    String utdid = dataJson.getString("utdid");
                    if (!TextUtils.isEmpty(utdid)) {
                        UtdidKeyFile.writeUtdidToSettings(Variables.getInstance().getContext(), utdid);
                        UtdidKeyFile.writeSdcardUtdidFile(utdid);
                        UtdidKeyFile.writeAppUtdidFile(utdid);
                        UtdidBroadcastMgr.getInstance().sendBroadCast(utdid);
                    }
                }
            }
        } catch (JSONException e) {
            UtdidLogger.d("", e.toString());
        }
        return response;
    }

    public static boolean isSuccess(int code2) {
        if (code2 < 0 || code2 == 10012) {
            return false;
        }
        return true;
    }
}
