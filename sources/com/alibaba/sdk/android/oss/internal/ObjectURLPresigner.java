package com.alibaba.sdk.android.oss.internal;

import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import anet.channel.util.HttpConstant;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.common.HttpMethod;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.DateUtil;
import com.alibaba.sdk.android.oss.common.utils.HttpHeaders;
import com.alibaba.sdk.android.oss.common.utils.HttpUtil;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.model.GeneratePresignedUrlRequest;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public class ObjectURLPresigner {
    private ClientConfiguration conf;
    private OSSCredentialProvider credentialProvider;
    private URI endpoint;

    public ObjectURLPresigner(URI endpoint2, OSSCredentialProvider credentialProvider2, ClientConfiguration conf2) {
        this.endpoint = endpoint2;
        this.credentialProvider = credentialProvider2;
        this.conf = conf2;
    }

    public String presignConstrainedURL(GeneratePresignedUrlRequest request) throws ClientException {
        String signature;
        String bucketName = request.getBucketName();
        String objectKey = request.getKey();
        String expires = String.valueOf((DateUtil.getFixedSkewedTimeMillis() / 1000) + request.getExpiration());
        HttpMethod method = request.getMethod() != null ? request.getMethod() : HttpMethod.GET;
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(method);
        requestMessage.setBucketName(bucketName);
        requestMessage.setObjectKey(objectKey);
        requestMessage.getHeaders().put("Date", expires);
        if (request.getContentType() != null && !request.getContentType().trim().equals("")) {
            requestMessage.getHeaders().put("Content-Type", request.getContentType());
        }
        if (request.getContentMD5() != null && !request.getContentMD5().trim().equals("")) {
            requestMessage.getHeaders().put(HttpHeaders.CONTENT_MD5, request.getContentMD5());
        }
        if (request.getQueryParameter() != null && request.getQueryParameter().size() > 0) {
            for (Map.Entry<String, String> entry : request.getQueryParameter().entrySet()) {
                requestMessage.getParameters().put(entry.getKey(), entry.getValue());
            }
        }
        if (request.getProcess() != null && !request.getProcess().trim().equals("")) {
            requestMessage.getParameters().put(RequestParameters.X_OSS_PROCESS, request.getProcess());
        }
        OSSFederationToken token = null;
        if (this.credentialProvider instanceof OSSFederationCredentialProvider) {
            token = ((OSSFederationCredentialProvider) this.credentialProvider).getValidFederationToken();
            requestMessage.getParameters().put(RequestParameters.SECURITY_TOKEN, token.getSecurityToken());
            if (token == null) {
                throw new ClientException("Can not get a federation token!");
            }
        } else if (this.credentialProvider instanceof OSSStsTokenCredentialProvider) {
            token = ((OSSStsTokenCredentialProvider) this.credentialProvider).getFederationToken();
            requestMessage.getParameters().put(RequestParameters.SECURITY_TOKEN, token.getSecurityToken());
        }
        String contentToSign = OSSUtils.buildCanonicalString(requestMessage);
        if ((this.credentialProvider instanceof OSSFederationCredentialProvider) || (this.credentialProvider instanceof OSSStsTokenCredentialProvider)) {
            signature = OSSUtils.sign(token.getTempAK(), token.getTempSK(), contentToSign);
        } else if (this.credentialProvider instanceof OSSPlainTextAKSKCredentialProvider) {
            signature = OSSUtils.sign(((OSSPlainTextAKSKCredentialProvider) this.credentialProvider).getAccessKeyId(), ((OSSPlainTextAKSKCredentialProvider) this.credentialProvider).getAccessKeySecret(), contentToSign);
        } else if (this.credentialProvider instanceof OSSCustomSignerCredentialProvider) {
            signature = ((OSSCustomSignerCredentialProvider) this.credentialProvider).signContent(contentToSign);
        } else {
            throw new ClientException("Unknown credentialProvider!");
        }
        String accessKey = signature.split(SymbolExpUtil.SYMBOL_COLON)[0].substring(4);
        String signature2 = signature.split(SymbolExpUtil.SYMBOL_COLON)[1];
        String host = this.endpoint.getHost();
        if (!OSSUtils.isCname(host) || OSSUtils.isInCustomCnameExcludeList(host, this.conf.getCustomCnameExcludeList())) {
            host = bucketName + "." + host;
        }
        Map<String, String> params = new LinkedHashMap<>();
        params.put(HttpHeaders.EXPIRES, expires);
        params.put(RequestParameters.OSS_ACCESS_KEY_ID, accessKey);
        params.put(RequestParameters.SIGNATURE, signature2);
        params.putAll(requestMessage.getParameters());
        return this.endpoint.getScheme() + HttpConstant.SCHEME_SPLIT + host + WVNativeCallbackUtil.SEPERATER + HttpUtil.urlEncode(objectKey, "utf-8") + WVUtils.URL_DATA_CHAR + HttpUtil.paramToQueryString(params, "utf-8");
    }

    public String presignConstrainedURL(String bucketName, String objectKey, long expiredTimeInSeconds) throws ClientException {
        GeneratePresignedUrlRequest presignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey);
        presignedUrlRequest.setExpiration(expiredTimeInSeconds);
        return presignConstrainedURL(presignedUrlRequest);
    }

    public String presignPublicURL(String bucketName, String objectKey) {
        String host = this.endpoint.getHost();
        if (!OSSUtils.isCname(host) || OSSUtils.isInCustomCnameExcludeList(host, this.conf.getCustomCnameExcludeList())) {
            host = bucketName + "." + host;
        }
        return this.endpoint.getScheme() + HttpConstant.SCHEME_SPLIT + host + WVNativeCallbackUtil.SEPERATER + HttpUtil.urlEncode(objectKey, "utf-8");
    }
}
