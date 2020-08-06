package android.taobao.windvane.connect.api;

import android.taobao.windvane.util.TaoLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiResponse {
    public static final String ERR_CODE = "ERR_CODE";
    public static final String ERR_SID_INVALID = "ERR_SID_INVALID";
    public static final String FAIL = "FAIL";
    public static final String KEY = "KEY";
    public static final String SUCCESS = "SUCCESS";
    private static String TAG = "core.ApiResponse";
    public static final String VALUE = "VALUE";
    public String api;
    public JSONObject data;
    public String errCode;
    public String errInfo;
    public boolean success;
    public String v;

    public ApiResponse parseResult(String str) {
        this.success = false;
        try {
            JSONObject jso = new JSONObject(str);
            this.api = jso.getString("api");
            this.v = jso.getString("v");
            parseRet(jso.getJSONArray("ret"));
            this.data = jso.getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
            this.success = false;
            this.errCode = "";
            this.errInfo = "";
        }
        return this;
    }

    public void parseRet(JSONArray jArray) throws JSONException {
        if (jArray != null) {
            int size = jArray.length();
            List<Map<String, String>> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                String result = jArray.getString(i);
                int index = result.indexOf("::");
                if (index >= 0) {
                    Map<String, String> map = new HashMap<>();
                    map.put(KEY, result.substring(0, index));
                    map.put(VALUE, result.substring(index + 2));
                    list.add(map);
                }
            }
            if (list.size() == 1) {
                Map<String, String> map2 = list.get(0);
                if ("SUCCESS".equals(map2.get(KEY))) {
                    this.success = true;
                } else {
                    this.success = false;
                }
                this.errCode = map2.get(KEY);
                this.errInfo = map2.get(VALUE);
            } else if (list.size() == 2) {
                Map<String, String> map1 = list.get(0);
                Map<String, String> map22 = list.get(1);
                if (!"FAIL".equals(map1.get(KEY)) || !ERR_CODE.equals(map22.get(KEY))) {
                    this.success = false;
                    this.errCode = map22.get(KEY);
                    this.errInfo = map22.get(VALUE);
                    return;
                }
                this.success = false;
                this.errCode = map22.get(VALUE);
                this.errInfo = map1.get(VALUE);
            }
        }
    }

    public ApiResponse parseJsonResult(String str) {
        this.success = false;
        try {
            this.data = new JSONObject(str);
            this.success = true;
        } catch (JSONException e) {
            TaoLog.e(TAG, "parseJsonResult fail, str = " + str);
            this.success = false;
        }
        return this;
    }
}
