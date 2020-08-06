package com.alibaba.sdk.android.oss.common.auth;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.common.utils.IOUtils;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class OSSAuthCredentialsProvider extends OSSFederationCredentialProvider {
    private String mAuthServerUrl;
    private AuthDecoder mDecoder;

    public interface AuthDecoder {
        String decode(String str);
    }

    public OSSAuthCredentialsProvider(String authServerUrl) {
        this.mAuthServerUrl = authServerUrl;
    }

    public void setAuthServerUrl(String authServerUrl) {
        this.mAuthServerUrl = authServerUrl;
    }

    public void setDecoder(AuthDecoder decoder) {
        this.mDecoder = decoder;
    }

    public OSSFederationToken getFederationToken() throws ClientException {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(this.mAuthServerUrl).openConnection();
            conn.setConnectTimeout(10000);
            String authData = IOUtils.readStreamAsString(conn.getInputStream(), "utf-8");
            if (this.mDecoder != null) {
                authData = this.mDecoder.decode(authData);
            }
            JSONObject jsonObj = new JSONObject(authData);
            if (jsonObj.getInt("StatusCode") == 200) {
                return new OSSFederationToken(jsonObj.getString("AccessKeyId"), jsonObj.getString("AccessKeySecret"), jsonObj.getString("SecurityToken"), jsonObj.getString("Expiration"));
            }
            String errorCode = jsonObj.getString("ErrorCode");
            throw new ClientException("ErrorCode: " + errorCode + "| ErrorMessage: " + jsonObj.getString("ErrorMessage"));
        } catch (Exception e) {
            throw new ClientException((Throwable) e);
        }
    }
}
