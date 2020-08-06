package android.taobao.windvane.extra.security;

import android.content.ContextWrapper;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.connect.api.ApiConstants;
import android.text.TextUtils;
import com.taobao.securityjni.GlobalInit;
import com.taobao.securityjni.SecretUtil;
import com.taobao.securityjni.tools.DataContext;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.securitybody.ISecurityBodyComponent;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SecurityManager {
    public static final int TYPE_SIGN_MTOP = 0;
    public static final int TYPE_SIGN_TOP = 1;
    private static SecurityManager mSecurityManager = null;
    private boolean isInit = false;
    private SecretUtil mSecretUtil;

    private SecurityManager() {
        if (!this.isInit) {
            init(GlobalConfig.context);
        }
    }

    public void init(ContextWrapper context) {
        try {
            GlobalInit.SetGlobalAppKey(GlobalConfig.getInstance().getAppKey());
            this.mSecretUtil = new SecretUtil(context);
            GlobalInit.GlobalSecurityInitAsyncSo(context);
            this.isInit = true;
        } catch (Throwable th) {
        }
    }

    public static synchronized SecurityManager getInstance() {
        SecurityManager securityManager;
        synchronized (SecurityManager.class) {
            if (mSecurityManager == null) {
                mSecurityManager = new SecurityManager();
            }
            securityManager = mSecurityManager;
        }
        return securityManager;
    }

    public String getSign(int signType, Map<String, String> params, String appkey) {
        if (!this.isInit) {
            return null;
        }
        switch (signType) {
            case 0:
                return getMTopSign((HashMap) params, appkey);
            case 1:
                return getTopSing((TreeMap) params, appkey);
            default:
                return null;
        }
    }

    private String getMTopSign(HashMap<String, String> params, String appkey) {
        if (!this.isInit || params == null || appkey == null) {
            return null;
        }
        HashMap<String, String> signParams = new HashMap<>();
        if (!TextUtils.isEmpty(params.get("api"))) {
            signParams.put("API", params.get("api"));
        }
        if (!TextUtils.isEmpty(params.get("data"))) {
            signParams.put("DATA", params.get("data"));
        }
        if (!TextUtils.isEmpty(params.get(ApiConstants.ECODE))) {
            signParams.put("ECODE", params.get(ApiConstants.ECODE));
        }
        if (!TextUtils.isEmpty(params.get("imei"))) {
            signParams.put("IMEI", params.get("imei"));
        }
        if (!TextUtils.isEmpty(params.get("imsi"))) {
            signParams.put("IMSI", params.get("imsi"));
        }
        if (!TextUtils.isEmpty(params.get("t"))) {
            signParams.put("TIME", params.get("t"));
        }
        if (!TextUtils.isEmpty(params.get("v"))) {
            signParams.put("V", params.get("v"));
        }
        DataContext dataCtx = new DataContext();
        dataCtx.extData = appkey.getBytes();
        return this.mSecretUtil.getSign(signParams, dataCtx);
    }

    private String getTopSing(TreeMap<String, String> params, String appkey) {
        if (!this.isInit || params == null || appkey == null) {
            return null;
        }
        DataContext dataCtx = new DataContext();
        dataCtx.extData = appkey.getBytes();
        return this.mSecretUtil.getTopSign(params, dataCtx);
    }

    public String getLoginTopToken(String userName, String time, String appkey) {
        if (!this.isInit || appkey == null || TextUtils.isEmpty(userName) || TextUtils.isEmpty(time)) {
            return null;
        }
        DataContext dataCtx = new DataContext();
        dataCtx.extData = appkey.getBytes();
        return this.mSecretUtil.getLoginTopToken(userName, time, dataCtx);
    }

    public String getSecBody(ContextWrapper context, String time, String appkey) {
        ISecurityBodyComponent sbComp;
        if (!this.isInit || appkey == null || TextUtils.isEmpty(time)) {
            return null;
        }
        try {
            SecurityGuardManager sgMgr = SecurityGuardManager.getInstance(context);
            if (sgMgr == null || (sbComp = sgMgr.getSecurityBodyComp()) == null || !sbComp.initSecurityBody(appkey)) {
                return null;
            }
            return sbComp.getSecurityBodyData(time, appkey);
        } catch (Throwable th) {
            return null;
        }
    }
}
