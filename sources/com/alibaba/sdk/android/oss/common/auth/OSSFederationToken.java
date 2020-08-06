package com.alibaba.sdk.android.oss.common.auth;

import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.utils.DateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class OSSFederationToken {
    private long expiration;
    private String securityToken;
    private String tempAk;
    private String tempSk;

    public OSSFederationToken(String tempAK, String tempSK, String securityToken2, long expiration2) {
        setTempAk(tempAK);
        setTempSk(tempSK);
        setSecurityToken(securityToken2);
        setExpiration(expiration2);
    }

    public OSSFederationToken(String tempAK, String tempSK, String securityToken2, String expirationInGMTFormat) {
        setTempAk(tempAK);
        setTempSk(tempSK);
        setSecurityToken(securityToken2);
        setExpirationInGMTFormat(expirationInGMTFormat);
    }

    public String toString() {
        return "OSSFederationToken [tempAk=" + this.tempAk + ", tempSk=" + this.tempSk + ", securityToken=" + this.securityToken + ", expiration=" + this.expiration + "]";
    }

    public String getTempAK() {
        return this.tempAk;
    }

    public String getTempSK() {
        return this.tempSk;
    }

    public String getSecurityToken() {
        return this.securityToken;
    }

    public void setSecurityToken(String securityToken2) {
        this.securityToken = securityToken2;
    }

    public void setTempAk(String tempAk2) {
        this.tempAk = tempAk2;
    }

    public void setTempSk(String tempSk2) {
        this.tempSk = tempSk2;
    }

    public long getExpiration() {
        return this.expiration;
    }

    public void setExpiration(long expiration2) {
        this.expiration = expiration2;
    }

    public void setExpirationInGMTFormat(String expirationInGMTFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            this.expiration = sdf.parse(expirationInGMTFormat).getTime() / 1000;
        } catch (ParseException e) {
            if (OSSLog.isEnableLog()) {
                e.printStackTrace();
            }
            this.expiration = (DateUtil.getFixedSkewedTimeMillis() / 1000) + 30;
        }
    }
}
