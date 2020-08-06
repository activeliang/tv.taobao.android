package com.taobao.orange.impl;

import android.content.Context;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.SecurityGuardParamContext;
import com.alibaba.wireless.security.open.securesignature.ISecureSignatureComponent;
import com.taobao.orange.OConstant;
import com.taobao.orange.inner.ISign;
import com.taobao.orange.util.OLog;
import java.util.HashMap;

public class TBGuardSign implements ISign {
    private static final String TAG = "TBGuardSign";
    private static boolean mSecurityGuardValid;

    static {
        mSecurityGuardValid = false;
        try {
            Class.forName(OConstant.REFLECT_SECURITYGUARD);
            mSecurityGuardValid = true;
        } catch (ClassNotFoundException e) {
            mSecurityGuardValid = false;
        }
    }

    public String sign(Context context, String appKey, String appSecret, String data, String authCode) {
        if (!mSecurityGuardValid) {
            OLog.e(TAG, "no security guard exist", new Object[0]);
            return null;
        }
        SecurityGuardManager sgMgr = null;
        try {
            sgMgr = SecurityGuardManager.getInstance(context);
        } catch (SecException e) {
            OLog.e(TAG, "sign", e, new Object[0]);
        }
        if (sgMgr == null) {
            OLog.e(TAG, "get SecurityGuardManager null", new Object[0]);
            return null;
        }
        ISecureSignatureComponent signComp = sgMgr.getSecureSignatureComp();
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("INPUT", data);
        SecurityGuardParamContext paramContext = new SecurityGuardParamContext();
        paramContext.appKey = appKey;
        paramContext.paramMap = paramMap;
        paramContext.requestType = 3;
        try {
            return signComp.signRequest(paramContext, authCode);
        } catch (SecException e2) {
            OLog.e(TAG, "sign", e2, new Object[0]);
            return null;
        }
    }
}
