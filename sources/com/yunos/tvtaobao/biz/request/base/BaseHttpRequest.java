package com.yunos.tvtaobao.biz.request.base;

import android.text.TextUtils;
import anetwork.channel.Param;
import com.yunos.tv.core.util.DataEncoder;
import com.yunos.tvtaobao.biz.request.core.DataRequest;
import com.yunos.tvtaobao.biz.request.core.DataRequestType;
import com.yunos.tvtaobao.biz.request.core.ServiceCode;
import com.yunos.tvtaobao.biz.request.core.ServiceResponse;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.NameValuePair;

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
        ZpLogger.v(this.TAG, this.TAG + ".getHttpParams sb = " + sb.toString());
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public List<? extends NameValuePair> getPostParameters() {
        return null;
    }

    public List<Param> getPostParametersForRequestImpl() {
        List<Param> paramList = new ArrayList<>();
        if (this.dataParams != null) {
            for (Map.Entry<String, String> entry : this.dataParams.entrySet()) {
                paramList.add(new PostParam(entry.getKey(), entry.getValue()));
            }
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
