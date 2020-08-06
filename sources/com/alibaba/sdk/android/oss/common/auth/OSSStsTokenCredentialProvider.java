package com.alibaba.sdk.android.oss.common.auth;

public class OSSStsTokenCredentialProvider implements OSSCredentialProvider {
    private String accessKeyId;
    private String secretKeyId;
    private String securityToken;

    public OSSStsTokenCredentialProvider(String accessKeyId2, String secretKeyId2, String securityToken2) {
        setAccessKeyId(accessKeyId2.trim());
        setSecretKeyId(secretKeyId2.trim());
        setSecurityToken(securityToken2.trim());
    }

    public OSSStsTokenCredentialProvider(OSSFederationToken token) {
        setAccessKeyId(token.getTempAK().trim());
        setSecretKeyId(token.getTempSK().trim());
        setSecurityToken(token.getSecurityToken().trim());
    }

    public String getAccessKeyId() {
        return this.accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId2) {
        this.accessKeyId = accessKeyId2;
    }

    public String getSecretKeyId() {
        return this.secretKeyId;
    }

    public void setSecretKeyId(String secretKeyId2) {
        this.secretKeyId = secretKeyId2;
    }

    public String getSecurityToken() {
        return this.securityToken;
    }

    public void setSecurityToken(String securityToken2) {
        this.securityToken = securityToken2;
    }

    public OSSFederationToken getFederationToken() {
        return new OSSFederationToken(this.accessKeyId, this.secretKeyId, this.securityToken, Long.MAX_VALUE);
    }
}
