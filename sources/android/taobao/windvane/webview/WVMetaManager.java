package android.taobao.windvane.webview;

import android.text.TextUtils;
import org.json.JSONObject;

public class WVMetaManager {
    private static final String TAG = "WVMetaManager";
    private static volatile WVMetaManager instance = null;
    private JSONObject metaDataObject = null;
    private String[] metaKeys = null;

    public static WVMetaManager getInstance() {
        if (instance == null) {
            synchronized (WVMetaManager.class) {
                if (instance == null) {
                    instance = new WVMetaManager();
                }
            }
        }
        return instance;
    }

    public void setMetaData(String data) {
        try {
            if (!TextUtils.isEmpty(data)) {
                if (data.startsWith("\"") && data.endsWith("\"")) {
                    data = data.substring(1, data.length() - 1);
                }
                this.metaDataObject = new JSONObject(data.replace("\\", ""));
            }
        } catch (Exception e) {
            this.metaDataObject = null;
        }
    }

    public JSONObject getMetaData() {
        return this.metaDataObject;
    }

    public void clear() {
        this.metaDataObject = null;
    }

    public String[] getMetaKeys() {
        return this.metaKeys;
    }

    public void setMetaKeys(String[] keys) {
        this.metaKeys = keys;
    }
}
