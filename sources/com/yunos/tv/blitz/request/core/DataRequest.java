package com.yunos.tv.blitz.request.core;

import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.text.TextUtils;
import com.yunos.tv.blitz.request.common.AppDebug;
import java.util.List;
import java.util.Map;
import mtopsdk.mtop.domain.MtopRequest;
import org.apache.http.NameValuePair;

public abstract class DataRequest extends MtopRequest {
    private static final long serialVersionUID = -2737660281410004209L;
    protected String TAG = getClass().getSimpleName();

    /* access modifiers changed from: protected */
    public abstract String getApi();

    /* access modifiers changed from: protected */
    public abstract String getApiVersion();

    /* access modifiers changed from: protected */
    public abstract Map<String, String> getAppData();

    /* access modifiers changed from: protected */
    public abstract String getHttpDomain();

    /* access modifiers changed from: protected */
    public abstract String getHttpParams();

    /* access modifiers changed from: protected */
    public abstract List<? extends NameValuePair> getPostParameters();

    public abstract DataRequestType getRequestType();

    public abstract <T> ServiceResponse<T> resolveResponse(String str) throws Exception;

    /* access modifiers changed from: protected */
    public void initialize() {
        throw new RuntimeException("request not initialize");
    }

    public String getUrl() {
        initialize();
        String url = null;
        try {
            url = getHttpDomain();
            if (!TextUtils.isEmpty(getHttpParams())) {
                url = url + WVUtils.URL_DATA_CHAR + getHttpParams();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppDebug.v(this.TAG, this.TAG + ".getUrl.url = " + url);
        return url;
    }

    public String getPostUrl() {
        initialize();
        try {
            return getHttpDomain();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getResponseEncode() {
        return "UTF-8";
    }
}
