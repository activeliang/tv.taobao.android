package android.taobao.windvane.packageapp.cleanup;

import android.taobao.windvane.util.TaoLog;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;

public class JsonUtil {
    private static final String TAG = "JsonUtil";

    public static String getJsonString(HashMap<String, InfoSnippet> table) {
        if (table == null) {
            return "{}";
        }
        try {
            return JSON.toJSONString(table);
        } catch (Exception e) {
            e.printStackTrace();
            TaoLog.e(TAG, "缓存算法模块序列化失败 : " + e.toString());
            return "{}";
        }
    }
}
