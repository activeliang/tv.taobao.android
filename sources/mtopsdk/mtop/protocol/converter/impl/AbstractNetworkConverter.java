package mtopsdk.mtop.protocol.converter.impl;

import android.taobao.windvane.util.WVNativeCallbackUtil;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopNetworkProp;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.global.MtopConfig;
import mtopsdk.mtop.global.SwitchConfig;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.protocol.converter.INetworkConverter;
import mtopsdk.mtop.protocol.converter.util.NetworkConverterUtils;
import mtopsdk.network.domain.ParcelableRequestBodyImpl;
import mtopsdk.network.domain.Request;

public abstract class AbstractNetworkConverter implements INetworkConverter {
    private static final String TAG = "mtopsdk.AbstractNetworkConverter";

    /* access modifiers changed from: protected */
    public abstract Map<String, String> getHeaderConversionMap();

    public Request convert(MtopContext mtopContext) {
        URL url;
        MtopNetworkProp property = mtopContext.property;
        MtopConfig mtopConfig = mtopContext.mtopInstance.getMtopConfig();
        String seqNo = mtopContext.seqNo;
        Request.Builder builder = new Request.Builder();
        builder.seqNo(seqNo);
        builder.reqContext(property.reqContext);
        builder.bizId(property.bizId);
        builder.connectTimeout(property.connTimeout);
        builder.readTimeout(property.socketTimeout);
        builder.retryTimes(property.retryTimes);
        builder.appKey(property.reqAppKey);
        builder.authCode(property.authCode);
        EnvModeEnum envMode = mtopConfig.envMode;
        if (envMode != null) {
            switch (envMode) {
                case ONLINE:
                    builder.env(0);
                    break;
                case PREPARE:
                    builder.env(1);
                    break;
                case TEST:
                case TEST_SANDBOX:
                    builder.env(2);
                    break;
            }
        }
        MethodEnum method = property.method;
        Map<String, String> protocolParamsMap = mtopContext.protocolParams;
        Map<String, String> originalHeaderMap = property.requestHeaders;
        Map<String, String> globalHeaders = mtopConfig.mtopGlobalHeaders;
        if (!globalHeaders.isEmpty()) {
            if (originalHeaderMap != null) {
                for (Map.Entry<String, String> entry : globalHeaders.entrySet()) {
                    String headerKey = entry.getKey();
                    if (!originalHeaderMap.containsKey(headerKey)) {
                        originalHeaderMap.put(headerKey, entry.getValue());
                    }
                }
            } else {
                originalHeaderMap = globalHeaders;
            }
        }
        Map<String, String> headers = buildRequestHeaders(protocolParamsMap, originalHeaderMap, mtopConfig.enableHeaderUrlEncode);
        try {
            String apiName = protocolParamsMap.remove("api");
            builder.api(apiName);
            String baseUrl = buildBaseUrl(mtopContext, apiName, protocolParamsMap.remove("v"));
            mtopContext.baseUrl = baseUrl;
            addMtopSdkProperty(mtopContext.mtopInstance, protocolParamsMap);
            if (property.queryParameterMap != null && !property.queryParameterMap.isEmpty()) {
                for (Map.Entry<String, String> entry2 : property.queryParameterMap.entrySet()) {
                    protocolParamsMap.put(entry2.getKey(), entry2.getValue());
                }
            }
            Map<String, String> globalQuerys = mtopConfig.mtopGlobalQuerys;
            if (!globalQuerys.isEmpty()) {
                for (Map.Entry<String, String> entry3 : globalQuerys.entrySet()) {
                    String paramKey = entry3.getKey();
                    if (!protocolParamsMap.containsKey(paramKey)) {
                        protocolParamsMap.put(paramKey, entry3.getValue());
                    }
                }
            }
            headers.put("content-type", HttpHeaderConstant.FORM_CONTENT_TYPE);
            if (MethodEnum.POST.getMethod().equals(method.getMethod())) {
                byte[] postData = null;
                String queryStr = NetworkConverterUtils.createParamQueryStr(protocolParamsMap, "utf-8");
                if (queryStr != null) {
                    postData = queryStr.getBytes("utf-8");
                }
                builder.method(method.getMethod(), new ParcelableRequestBodyImpl(HttpHeaderConstant.FORM_CONTENT_TYPE, postData));
                url = NetworkConverterUtils.initUrl(baseUrl, (Map<String, String>) null);
            } else {
                if (!(mtopContext.mtopListener instanceof MtopCallback.MtopCacheListener) && !property.useCache) {
                    headers.put("cache-control", HttpHeaderConstant.NO_CACHE);
                }
                mtopContext.queryParams = protocolParamsMap;
                url = NetworkConverterUtils.initUrl(baseUrl, protocolParamsMap);
            }
        } catch (Exception e) {
            TBSdkLog.e(TAG, "[createParamPostData]getPostData error");
        } catch (Throwable e2) {
            TBSdkLog.e(TAG, seqNo, "[convert]convert Request failed!", e2);
            return null;
        }
        if (url != null) {
            mtopContext.stats.domain = url.getHost();
            builder.url(url.toString());
        }
        builder.headers(headers);
        return builder.build();
    }

    /* access modifiers changed from: protected */
    public Map<String, String> buildRequestHeaders(Map<String, String> paramsMap, Map<String, String> headers, boolean headerUrlEncode) {
        Map<String, String> headerConversionMap = getHeaderConversionMap();
        if (headerConversionMap == null) {
            TBSdkLog.e(TAG, "[buildRequestHeaders]headerConversionMap is null,buildRequestHeaders error.");
            return headers;
        }
        int size = headerConversionMap.size();
        if (headers != null) {
            size += headers.size();
        }
        Map<String, String> requestHeaders = new HashMap<>(size);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String headerKey = entry.getKey();
                String headerValue = entry.getValue();
                if (headerUrlEncode) {
                    if (headerValue != null) {
                        try {
                            headerValue = URLEncoder.encode(headerValue, "utf-8");
                        } catch (Exception e) {
                            TBSdkLog.e(TAG, "[buildRequestHeaders]urlEncode " + headerKey + "=" + headerValue + "error");
                        }
                    } else {
                        headerValue = null;
                    }
                }
                requestHeaders.put(headerKey, headerValue);
            }
        }
        for (Map.Entry<String, String> entry2 : headerConversionMap.entrySet()) {
            String headerKey2 = entry2.getKey();
            String headerValue2 = paramsMap.remove(entry2.getValue());
            if (headerValue2 != null) {
                try {
                    requestHeaders.put(headerKey2, URLEncoder.encode(headerValue2, "utf-8"));
                } catch (Exception e2) {
                    TBSdkLog.e(TAG, "[buildRequestHeaders]urlEncode " + headerKey2 + "=" + headerValue2 + "error");
                }
            }
        }
        String lngReader = paramsMap.remove("lng");
        String latReader = paramsMap.remove("lat");
        if (!(lngReader == null || latReader == null)) {
            StringBuilder location = new StringBuilder();
            location.append(lngReader);
            location.append(",");
            location.append(latReader);
            try {
                requestHeaders.put(HttpHeaderConstant.X_LOCATION, URLEncoder.encode(location.toString(), "utf-8"));
            } catch (Exception e3) {
                TBSdkLog.e(TAG, "[buildRequestHeaders]urlEncode x-location=" + location.toString() + "error");
            }
        }
        return requestHeaders;
    }

    /* access modifiers changed from: protected */
    public String buildBaseUrl(MtopContext mtopContext, String apiName, String apiVersion) {
        StringBuilder baseUrl = new StringBuilder(64);
        try {
            MtopConfig mtopConfig = mtopContext.mtopInstance.getMtopConfig();
            MtopNetworkProp property = mtopContext.property;
            property.envMode = mtopConfig.envMode;
            baseUrl.append(property.protocol.getProtocol());
            String customDomain = getCustomDomain(mtopContext);
            if (StringUtils.isNotBlank(customDomain)) {
                baseUrl.append(customDomain);
            } else {
                baseUrl.append(mtopConfig.mtopDomain.getDomain(mtopContext.property.envMode));
            }
            baseUrl.append(WVNativeCallbackUtil.SEPERATER).append(mtopConfig.entrance.getEntrance());
            baseUrl.append(WVNativeCallbackUtil.SEPERATER).append(apiName);
            baseUrl.append(WVNativeCallbackUtil.SEPERATER).append(apiVersion).append(WVNativeCallbackUtil.SEPERATER);
        } catch (Exception e) {
            TBSdkLog.e(TAG, mtopContext.seqNo, "[buildBaseUrl] build mtop baseUrl error.", e);
        }
        return baseUrl.toString();
    }

    private String getCustomDomain(MtopContext mtopContext) {
        MtopNetworkProp property = mtopContext.property;
        if (StringUtils.isNotBlank(property.customDomain)) {
            return property.customDomain;
        }
        switch (property.envMode) {
            case ONLINE:
                if (StringUtils.isNotBlank(property.customOnlineDomain)) {
                    return property.customOnlineDomain;
                }
                break;
            case PREPARE:
                if (StringUtils.isNotBlank(property.customPreDomain)) {
                    return property.customPreDomain;
                }
                break;
            case TEST:
                if (StringUtils.isNotBlank(property.customDailyDomain)) {
                    return property.customDailyDomain;
                }
                break;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void addMtopSdkProperty(Mtop mtopInstance, Map<String, String> paramsMap) {
        if (SwitchConfig.getInstance().isMtopsdkPropertySwitchOpen()) {
            for (Map.Entry<String, String> entry : mtopInstance.getMtopConfig().getMtopProperties().entrySet()) {
                try {
                    String key = entry.getKey();
                    if (StringUtils.isNotBlank(key) && key.startsWith(HttpHeaderConstant.MTOPSDK_PROPERTY_PREFIX)) {
                        paramsMap.put(key.substring(HttpHeaderConstant.MTOPSDK_PROPERTY_PREFIX.length()), entry.getValue());
                    }
                } catch (Exception e) {
                    TBSdkLog.e(TAG, "[addMtopSdkProperty]get mtopsdk properties error,key=" + entry.getKey() + ",value=" + entry.getValue());
                }
            }
        }
    }
}
