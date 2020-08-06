package com.yunos.tv.blitz.request.base;

import android.text.TextUtils;
import anetwork.channel.Header;
import com.yunos.tv.blitz.request.common.AppDebug;
import com.yunos.tv.blitz.request.core.DataRequest;
import com.yunos.tv.blitz.request.core.DataRequestType;
import com.yunos.tv.blitz.request.core.ServiceCode;
import com.yunos.tv.blitz.request.core.ServiceResponse;
import com.yunos.tv.blitz.request.util.DataEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public abstract class BaseHttpRequest extends DataRequest {
    private static final long serialVersionUID = 3850312713226552530L;

    public abstract <T> T resolveResult(String str) throws Exception;

    public BaseHttpRequest() {
        this.dataParams = new HashMap();
    }

    public void addParams(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            this.dataParams.put(key, value);
        }
    }

    /* access modifiers changed from: protected */
    public void initialize() {
        Map<String, String> param = getAppData();
        if (param != null) {
            this.dataParams.putAll(param);
        }
    }

    public DataRequestType getRequestType() {
        return DataRequestType.OTHER;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return null;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return null;
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
        AppDebug.v(this.TAG, this.TAG + ".getHttpParams sb = " + sb.toString());
        return sb.toString();
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

    public <T> ServiceResponse<T> resolveResponse(String response) throws Exception {
        ServiceResponse<T> serviceResponse = new ServiceResponse<>();
        T result = resolveResult(response);
        if (result != null) {
            serviceResponse.update(ServiceCode.SERVICE_OK, result);
        } else {
            serviceResponse.update(ServiceCode.DATA_PARSE_ERROR.getCode(), (String) null, ServiceCode.DATA_PARSE_ERROR.getMsg());
        }
        return serviceResponse;
    }

    public List<Header> getHttpHeader() {
        return null;
    }
}
