package com.yunos.tv.alitvasrsdk;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

public class ASRCommandReturn {
    public String mErrorCode;
    public String mErrorMessage;
    public boolean mForceClose;
    public boolean mIsHandled;
    public String mMention;
    public boolean mOpenFarMic;
    public String mSpokenMessage;
    public String mSuccessMessage;

    public ASRCommandReturn() {
    }

    public ASRCommandReturn(boolean isHandled, String errorCode, String errorMessage) {
        this(isHandled, errorCode, errorMessage, (String) null, (String) null, (String) null, false);
    }

    public ASRCommandReturn(boolean isHandled, String errorCode, String errorMessage, String mention, String successMsg) {
        this(isHandled, errorCode, errorMessage, mention, successMsg, (String) null, false);
    }

    public ASRCommandReturn(boolean isHandled, String errorCode, String errorMessage, String mention, String successMsg, String spokenMessage, boolean openFarMic) {
        this.mIsHandled = isHandled;
        this.mErrorCode = errorCode;
        this.mErrorMessage = errorMessage;
        this.mMention = mention;
        this.mSuccessMessage = successMsg;
        this.mSpokenMessage = spokenMessage;
        this.mOpenFarMic = openFarMic;
    }

    public Map<String, String> getReturn() {
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("handled", String.valueOf(this.mIsHandled));
        if (this.mOpenFarMic) {
            returnMap.put("openFarMic", String.valueOf(this.mOpenFarMic));
        }
        if (this.mForceClose) {
            returnMap.put("forceClose", String.valueOf(this.mForceClose));
        }
        if (!TextUtils.isEmpty(this.mErrorCode)) {
            returnMap.put("errorCode", this.mErrorCode);
        }
        if (!TextUtils.isEmpty(this.mErrorMessage)) {
            returnMap.put("errorMessage", this.mErrorMessage);
        }
        if (!TextUtils.isEmpty(this.mMention)) {
            returnMap.put("mention", this.mMention);
        }
        if (!TextUtils.isEmpty(this.mSuccessMessage)) {
            returnMap.put("successMessage", this.mSuccessMessage);
        }
        if (!TextUtils.isEmpty(this.mSpokenMessage)) {
            returnMap.put("spokenMessage", this.mSpokenMessage);
        }
        return returnMap;
    }
}
