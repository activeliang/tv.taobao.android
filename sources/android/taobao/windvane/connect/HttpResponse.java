package android.taobao.windvane.connect;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private byte[] data = null;
    private String errorMsg = null;
    private Map<String, String> headers = new HashMap();
    private int httpCode = 0;

    public boolean isSuccess() {
        return this.httpCode == 200;
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public void setHttpCode(int httpCode2) {
        this.httpCode = httpCode2;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, String> headers2) {
        if (headers2 != null) {
            this.headers = headers2;
        }
    }

    public void addHeader(String key, String val) {
        this.headers.put(key, val);
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data2) {
        this.data = data2;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg2) {
        this.errorMsg = errorMsg2;
    }
}
