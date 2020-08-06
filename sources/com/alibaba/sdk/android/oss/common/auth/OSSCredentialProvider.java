package com.alibaba.sdk.android.oss.common.auth;

import com.alibaba.sdk.android.oss.ClientException;

public interface OSSCredentialProvider {
    OSSFederationToken getFederationToken() throws ClientException;
}
