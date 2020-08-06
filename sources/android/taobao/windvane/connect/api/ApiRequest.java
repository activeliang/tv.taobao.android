package android.taobao.windvane.connect.api;

import java.util.HashMap;
import java.util.Map;

public class ApiRequest {
    private Map<String, String> dataParams = new HashMap();
    private boolean isSec = false;
    private Map<String, String> params = new HashMap();

    public Map<String, String> getParams() {
        return this.params;
    }

    public String getParam(String key) {
        return this.params.get(key);
    }

    public void addParam(String key, String value) {
        if (key != null && value != null) {
            this.params.put(key, value);
        }
    }

    public void removeParam(String key) {
        this.params.remove(key);
    }

    public Map<String, String> getDataParams() {
        return this.dataParams;
    }

    public String getDataParam(String key) {
        return this.dataParams.get(key);
    }

    public void addDataParams(Map<String, String> data) {
        if (data != null) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                addDataParam(entry.getKey(), entry.getValue());
            }
        }
    }

    public void addDataParam(String key, String value) {
        if (key != null && value != null) {
            this.dataParams.put(key, value);
        }
    }

    public boolean isSec() {
        return this.isSec;
    }

    public void setSec(boolean isSec2) {
        this.isSec = isSec2;
    }
}
