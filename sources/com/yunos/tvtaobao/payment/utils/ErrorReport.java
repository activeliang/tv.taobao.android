package com.yunos.tvtaobao.payment.utils;

import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.payment.PaymentApplication;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import mtopsdk.mtop.intf.Mtop;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ErrorReport {
    public static final String ERRORTYPE_APIERROR = "API_ERROR";
    public static final String ERRORTYPE_EXCEPTION = "EXCEPTION";
    public static final String ERRORTYPE_FILTERERROR = "FILTER_ERROR";
    public static final String ERRORTYPE_HTTPERROR = "HTTP_ERROR";
    public static final String ERRORTYPE_PARSEERROR = "PARSE_ERROR";
    public static final String ERRORTYPE_TIMEOUT = "TIME_OUT";
    static final String H5_STORE_URL = "http://androidlog.cn-hangzhou.sls.aliyuncs.com/logstores/h5log/track?APIVersion=0.6.0";
    public static final int MAX_PARAM_LENGTH = 1024;
    static final String STORE_URL = "http://androidlog.cn-hangzhou.sls.aliyuncs.com/logstores/log/track?APIVersion=0.6.0";
    private static final String TAG = "ErrorReport";
    private static Callback callback = new Callback() {
        public void onFailure(Call call, IOException e) {
            ZpLogger.d(ErrorReport.TAG, "upload failed");
        }

        public void onResponse(Call call, Response response) throws IOException {
            ZpLogger.d(ErrorReport.TAG, "upload response code:" + response.code());
        }
    };
    private static volatile ErrorReport instance;
    private OkHttpClient client;
    private int rate = 0;
    private int sum = 1;
    private String ttid;

    public static class Error {
        public String api;
        public String apiV;
        public String bizCode;
        public boolean mtop = true;
        public String params;
        public String statusCode;
        public String type;
    }

    public static ErrorReport getInstance() {
        if (instance == null) {
            synchronized (ErrorReport.class) {
                if (instance == null) {
                    instance = new ErrorReport();
                }
            }
        }
        return instance;
    }

    private ErrorReport() {
        try {
            this.ttid = Mtop.instance(PaymentApplication.getApplication()).getTtid();
        } catch (Throwable th) {
            this.ttid = null;
        }
        this.client = new OkHttpClient.Builder().connectionPool(new ConnectionPool()).connectTimeout(5, TimeUnit.SECONDS).build();
        Object obj = TvTaoSharedPerference.getSp(PaymentApplication.getApplication(), "error_report", "0");
        if (obj instanceof String) {
            setRate((String) obj);
        }
    }

    public void setRate(String rate2) {
        int value;
        if (TextUtils.isEmpty(rate2)) {
            this.rate = 0;
        } else if (!rate2.contains(WVNativeCallbackUtil.SEPERATER)) {
            try {
                value = Integer.valueOf(rate2).intValue();
            } catch (Exception e) {
                value = 0;
            }
            if (value <= 0) {
                this.rate = 0;
                this.sum = 1;
            }
            if (value >= 1) {
                this.rate = 1;
                this.sum = 1;
            }
        } else {
            String[] value2 = rate2.split(WVNativeCallbackUtil.SEPERATER);
            if (value2.length >= 2) {
                this.rate = parseString(value2[0], 0, 0);
                this.sum = parseString(value2[1], 1, 1);
            } else {
                this.rate = 0;
                this.sum = 1;
            }
        }
        ZpLogger.d(TAG, "errorRate=" + this.rate + WVNativeCallbackUtil.SEPERATER + this.sum);
    }

    private int parseString(String str, int defaultValue, int minValue) {
        try {
            return Math.max(Integer.valueOf(str).intValue(), minValue);
        } catch (Exception e) {
            return Math.max(defaultValue, minValue);
        }
    }

    public void uploadHttpError(String url, String type, String statusCode, String bizCode) {
        boolean needUpload;
        if (this.rate > 0) {
            if (this.rate >= this.sum) {
                needUpload = true;
            } else {
                needUpload = new Random().nextInt(this.sum + 1) <= this.rate;
            }
            if (needUpload) {
                try {
                    StringBuilder urlBuilder = new StringBuilder(STORE_URL);
                    urlBuilder.append("&r=").append("HTTP");
                    if (!TextUtils.isEmpty(url)) {
                        urlBuilder.append("&a=").append(URLEncoder.encode(url, "utf-8"));
                    }
                    if (!TextUtils.isEmpty(type)) {
                        urlBuilder.append("&t=").append(type);
                    }
                    if (!TextUtils.isEmpty(statusCode)) {
                        urlBuilder.append("&sc=").append(statusCode);
                    }
                    if (!TextUtils.isEmpty(this.ttid)) {
                        urlBuilder.append("&ttid=").append(URLEncoder.encode(this.ttid, "utf-8"));
                    }
                    if (!TextUtils.isEmpty(bizCode)) {
                        urlBuilder.append("&bc=").append(URLEncoder.encode(bizCode, "utf-8"));
                    }
                    Request uploadRequest = new Request.Builder().url(urlBuilder.toString()).get().build();
                    if (this.client != null) {
                        this.client.newCall(uploadRequest).enqueue(callback);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void uploadMtopError(String apiName, String apiVersion, Map<String, String> params, String type, String statusCode, String bizCode, String bizMsg) {
        uploadMtopError(false, apiName, apiVersion, params, type, statusCode, bizCode, bizMsg);
    }

    public void uploadH5MtopError(String apiName, String apiVersion, Map<String, String> params, String type, String statusCode, String bizCode, String bizMsg) {
        uploadMtopError(true, apiName, apiVersion, params, type, statusCode, bizCode, bizMsg);
    }

    public void uploadMtopError(boolean h5, String apiName, String apiVersion, Map<String, String> params, String type, String statusCode, String bizCode, String bizMsg) {
        boolean needUpload;
        StringBuilder urlBuilder;
        if (this.rate > 0) {
            if (this.rate >= this.sum) {
                needUpload = true;
            } else {
                needUpload = new Random().nextInt(this.sum + 1) <= this.rate;
            }
            if (needUpload) {
                if (h5) {
                    try {
                        urlBuilder = new StringBuilder(H5_STORE_URL);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        return;
                    }
                } else {
                    urlBuilder = new StringBuilder(STORE_URL);
                }
                urlBuilder.append("&r=").append("MTOP");
                if (!TextUtils.isEmpty(apiName)) {
                    urlBuilder.append("&a=").append(apiName);
                }
                if (!TextUtils.isEmpty(apiVersion)) {
                    urlBuilder.append("&v=").append(apiVersion);
                }
                if (params != null && !params.isEmpty()) {
                    urlBuilder.append("&p=").append(URLEncoder.encode(buildParams(params), "utf-8"));
                }
                if (!TextUtils.isEmpty(type)) {
                    urlBuilder.append("&t=").append(type);
                }
                if (!TextUtils.isEmpty(statusCode)) {
                    urlBuilder.append("&sc=").append(statusCode);
                }
                if (!TextUtils.isEmpty(bizCode)) {
                    urlBuilder.append("&bc=").append(URLEncoder.encode(bizCode + "|" + bizMsg, "utf-8"));
                }
                if (!TextUtils.isEmpty(this.ttid)) {
                    urlBuilder.append("&ttid=").append(URLEncoder.encode(this.ttid, "utf-8"));
                }
                urlBuilder.append("&env=").append(Mtop.instance(PaymentApplication.getApplication()).getMtopConfig().envMode.getEnvMode());
                Request uploadRequest = new Request.Builder().url(urlBuilder.toString()).get().build();
                if (this.client != null) {
                    this.client.newCall(uploadRequest).enqueue(callback);
                }
            }
        }
    }

    private String buildParams(Map<String, String> params) {
        if (params == null) {
            return null;
        }
        try {
            String str = JSON.toJSONString(params);
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            if (str.length() >= 1024) {
                return str.substring(0, 1024);
            }
            return str;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
