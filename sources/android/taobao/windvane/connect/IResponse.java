package android.taobao.windvane.connect;

import android.taobao.windvane.monitor.WVPerformanceMonitorInterface;
import android.taobao.windvane.util.CommonUtils;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IResponse {
    private int StatusCode;
    private byte[] data;
    private String desc;
    private String encoding;
    private Map<String, String> headersMap = new HashMap();
    public WVPerformanceMonitorInterface.NetStat mNetstat = new WVPerformanceMonitorInterface.NetStat();
    private String mimeType;

    public String getMimeType() {
        return this.mimeType;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data2) {
        this.data = data2;
    }

    public int getStatusCode() {
        return this.StatusCode;
    }

    public void setStatusCode(int statusCode) {
        this.StatusCode = statusCode;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc2) {
        this.desc = desc2;
    }

    public Map<String, String> getHeadersMap() {
        return this.headersMap;
    }

    public void setHeadMap(Map<String, List<String>> headMap) {
        if (headMap != null) {
            for (String key : headMap.keySet()) {
                List<String> headerList = headMap.get(key);
                if (headerList != null) {
                    for (int i = 0; i < headerList.size(); i++) {
                        this.headersMap.put(key, headerList.get(i));
                    }
                }
            }
            String content_type = this.headersMap.get("content-type");
            if (content_type != null) {
                this.mimeType = CommonUtils.parseMimeType(content_type);
                this.encoding = CommonUtils.parseCharset(content_type);
                this.encoding = TextUtils.isEmpty(this.encoding) ? "utf-8" : this.encoding;
            }
        }
    }
}
