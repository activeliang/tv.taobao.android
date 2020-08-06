package com.alibaba.sdk.android.oss.common.auth;

public abstract class OSSCustomSignerCredentialProvider implements OSSCredentialProvider {
    public abstract String signContent(String str);

    public OSSFederationToken getFederationToken() {
        return null;
    }
}
