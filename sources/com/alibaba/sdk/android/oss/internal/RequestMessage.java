package com.alibaba.sdk.android.oss.internal;

import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.alibaba.sdk.android.oss.common.HttpMethod;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.HttpUtil;
import com.alibaba.sdk.android.oss.common.utils.HttpdnsMini;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RequestMessage extends HttpMessage {
    private String bucketName;
    private boolean checkCRC64;
    private OSSCredentialProvider credentialProvider;
    private URI endpoint;
    private boolean httpDnsEnable = false;
    private boolean isAuthorizationRequired = true;
    private boolean isInCustomCnameExcludeList = false;
    private HttpMethod method;
    private String objectKey;
    private Map<String, String> parameters = new LinkedHashMap();
    private URI service;
    private byte[] uploadData;
    private String uploadFilePath;

    public /* bridge */ /* synthetic */ void addHeader(String str, String str2) {
        super.addHeader(str, str2);
    }

    public /* bridge */ /* synthetic */ void close() throws IOException {
        super.close();
    }

    public /* bridge */ /* synthetic */ InputStream getContent() {
        return super.getContent();
    }

    public /* bridge */ /* synthetic */ long getContentLength() {
        return super.getContentLength();
    }

    public /* bridge */ /* synthetic */ Map getHeaders() {
        return super.getHeaders();
    }

    public /* bridge */ /* synthetic */ String getStringBody() {
        return super.getStringBody();
    }

    public /* bridge */ /* synthetic */ void setContent(InputStream inputStream) {
        super.setContent(inputStream);
    }

    public /* bridge */ /* synthetic */ void setContentLength(long j) {
        super.setContentLength(j);
    }

    public /* bridge */ /* synthetic */ void setHeaders(Map map) {
        super.setHeaders(map);
    }

    public /* bridge */ /* synthetic */ void setStringBody(String str) {
        super.setStringBody(str);
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public void setMethod(HttpMethod method2) {
        this.method = method2;
    }

    public OSSCredentialProvider getCredentialProvider() {
        return this.credentialProvider;
    }

    public void setCredentialProvider(OSSCredentialProvider credentialProvider2) {
        this.credentialProvider = credentialProvider2;
    }

    public URI getService() {
        return this.service;
    }

    public void setService(URI service2) {
        this.service = service2;
    }

    public URI getEndpoint() {
        return this.endpoint;
    }

    public void setEndpoint(URI endpoint2) {
        this.endpoint = endpoint2;
    }

    public boolean isHttpDnsEnable() {
        return this.httpDnsEnable;
    }

    public void setHttpDnsEnable(boolean httpDnsEnable2) {
        this.httpDnsEnable = httpDnsEnable2;
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public void setBucketName(String bucketName2) {
        this.bucketName = bucketName2;
    }

    public String getObjectKey() {
        return this.objectKey;
    }

    public void setObjectKey(String objectKey2) {
        this.objectKey = objectKey2;
    }

    public Map<String, String> getParameters() {
        return this.parameters;
    }

    public void setParameters(Map<String, String> parameters2) {
        this.parameters = parameters2;
    }

    public String getUploadFilePath() {
        return this.uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath2) {
        this.uploadFilePath = uploadFilePath2;
    }

    public byte[] getUploadData() {
        return this.uploadData;
    }

    public void setUploadData(byte[] uploadData2) {
        this.uploadData = uploadData2;
    }

    public boolean isAuthorizationRequired() {
        return this.isAuthorizationRequired;
    }

    public void setIsAuthorizationRequired(boolean isAuthorizationRequired2) {
        this.isAuthorizationRequired = isAuthorizationRequired2;
    }

    public boolean isInCustomCnameExcludeList() {
        return this.isInCustomCnameExcludeList;
    }

    public void setIsInCustomCnameExcludeList(boolean isInExcludeCnameList) {
        this.isInCustomCnameExcludeList = isInExcludeCnameList;
    }

    public boolean isCheckCRC64() {
        return this.checkCRC64;
    }

    public void setCheckCRC64(boolean checkCRC642) {
        this.checkCRC64 = checkCRC642;
    }

    public void createBucketRequestBodyMarshall(Map<String, String> configures) throws UnsupportedEncodingException {
        StringBuffer xmlBody = new StringBuffer();
        if (configures != null) {
            xmlBody.append("<CreateBucketConfiguration>");
            for (Map.Entry<String, String> entry : configures.entrySet()) {
                xmlBody.append("<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">");
            }
            xmlBody.append("</CreateBucketConfiguration>");
            byte[] binaryData = xmlBody.toString().getBytes("utf-8");
            setContent(new ByteArrayInputStream(binaryData));
            setContentLength((long) binaryData.length);
        }
    }

    public byte[] deleteMultipleObjectRequestBodyMarshall(List<String> objectKeys, boolean isQuiet) throws UnsupportedEncodingException {
        StringBuffer xmlBody = new StringBuffer();
        xmlBody.append("<Delete>");
        if (isQuiet) {
            xmlBody.append("<Quiet>true</Quiet>");
        } else {
            xmlBody.append("<Quiet>false</Quiet>");
        }
        for (String key : objectKeys) {
            xmlBody.append("<Object>");
            xmlBody.append("<Key>").append(key).append("</Key>");
            xmlBody.append("</Object>");
        }
        xmlBody.append("</Delete>");
        byte[] binaryData = xmlBody.toString().getBytes("utf-8");
        setContent(new ByteArrayInputStream(binaryData));
        setContentLength((long) binaryData.length);
        return binaryData;
    }

    public String buildOSSServiceURL() {
        OSSUtils.assertTrue(this.service != null, "Service haven't been set!");
        String originHost = this.service.getHost();
        String scheme = this.service.getScheme();
        String urlHost = null;
        if (isHttpDnsEnable()) {
            urlHost = HttpdnsMini.getInstance().getIpByHostAsync(originHost);
        } else {
            OSSLog.logDebug("[buildOSSServiceURL], disable httpdns");
        }
        if (urlHost == null) {
            urlHost = originHost;
        }
        getHeaders().put("Host", originHost);
        String baseURL = scheme + HttpConstant.SCHEME_SPLIT + urlHost;
        String queryString = OSSUtils.paramToQueryString(this.parameters, "utf-8");
        return OSSUtils.isEmptyString(queryString) ? baseURL : baseURL + WVUtils.URL_DATA_CHAR + queryString;
    }

    public String buildCanonicalURL() throws Exception {
        String baseURL;
        OSSUtils.assertTrue(this.endpoint != null, "Endpoint haven't been set!");
        String scheme = this.endpoint.getScheme();
        String originHost = this.endpoint.getHost();
        String portString = null;
        int port = this.endpoint.getPort();
        if (port != -1) {
            portString = String.valueOf(port);
        }
        if (TextUtils.isEmpty(originHost)) {
            String url = this.endpoint.toString();
            OSSLog.logDebug("endpoint url : " + url);
            originHost = url.substring((scheme + HttpConstant.SCHEME_SPLIT).length(), url.length());
        }
        OSSLog.logDebug(" scheme : " + scheme);
        OSSLog.logDebug(" originHost : " + originHost);
        OSSLog.logDebug(" port : " + portString);
        String uri = this.endpoint.toString();
        if (TextUtils.isEmpty(this.bucketName)) {
            baseURL = this.endpoint.toString();
        } else if (OSSUtils.isValidateIP(originHost)) {
            baseURL = this.endpoint.toString() + WVNativeCallbackUtil.SEPERATER + this.bucketName;
        } else if (OSSUtils.isOssOriginHost(originHost)) {
            String originHost2 = this.bucketName + "." + originHost;
            String urlHost = null;
            if (isHttpDnsEnable()) {
                urlHost = HttpdnsMini.getInstance().getIpByHostAsync(originHost2);
            } else {
                OSSLog.logDebug("[buildCannonicalURL], disable httpdns");
            }
            addHeader("Host", originHost2);
            if (!TextUtils.isEmpty(urlHost)) {
                baseURL = scheme + HttpConstant.SCHEME_SPLIT + urlHost;
            } else {
                baseURL = scheme + HttpConstant.SCHEME_SPLIT + originHost2;
            }
        } else {
            baseURL = this.endpoint.toString();
        }
        if (!TextUtils.isEmpty(this.objectKey)) {
            baseURL = baseURL + WVNativeCallbackUtil.SEPERATER + HttpUtil.urlEncode(this.objectKey, "utf-8");
        }
        String queryString = OSSUtils.paramToQueryString(this.parameters, "utf-8");
        StringBuilder printReq = new StringBuilder();
        printReq.append("request---------------------\n");
        printReq.append("request url=" + baseURL + "\n");
        printReq.append("request params=" + queryString + "\n");
        for (String key : getHeaders().keySet()) {
            printReq.append("requestHeader [" + key + "]: ").append(((String) getHeaders().get(key)) + "\n");
        }
        OSSLog.logDebug(printReq.toString());
        return OSSUtils.isEmptyString(queryString) ? baseURL : baseURL + WVUtils.URL_DATA_CHAR + queryString;
    }
}
