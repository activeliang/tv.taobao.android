package com.taobao.securityjni;

import android.content.ContextWrapper;
import com.taobao.securityjni.tools.DataContext;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.SecurityGuardParamContext;
import com.taobao.wireless.security.sdk.indiekit.IIndieKitComponent;
import java.util.HashMap;

@Deprecated
public class SecurityCheck {
    private SecurityGuardManager manager;
    private IIndieKitComponent proxy;

    public SecurityCheck(ContextWrapper context) {
        this.manager = SecurityGuardManager.getInstance(context);
        if (this.manager != null) {
            this.proxy = this.manager.getIndieKitComp();
        }
    }

    public String getCheckSignature(String timeStamp) {
        DataContext ctx = new DataContext();
        ctx.index = 0;
        return getCheckSignature(timeStamp, ctx);
    }

    public String getCheckSignature(String timeStamp, DataContext dataCtx) {
        if (this.proxy == null || timeStamp == null || dataCtx == null) {
            return null;
        }
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("timestamp", timeStamp);
        SecurityGuardParamContext paramContext = new SecurityGuardParamContext();
        paramContext.paramMap = paramMap;
        paramContext.requestType = 1;
        if (dataCtx.extData == null || "".equals(dataCtx.extData)) {
            dataCtx.index = dataCtx.index < 0 ? 0 : dataCtx.index;
            String appKey = this.manager.getStaticDataStoreComp().getAppKeyByIndex(dataCtx.index);
            if (appKey == null || "".equals(appKey)) {
                return null;
            }
            paramContext.appKey = appKey;
        } else {
            paramContext.appKey = new String(dataCtx.extData);
        }
        return this.proxy.indieKitRequest(paramContext);
    }

    public String indieKitRequest(SecurityGuardParamContext paramContext) {
        if (this.proxy == null) {
            return null;
        }
        return this.proxy.indieKitRequest(paramContext);
    }

    public int validateFileSignature(String fileSignature, String fileHash, String secretKey) {
        return -1;
    }

    public int reportSusText(String text, String extraKey) {
        throw new UnsupportedOperationException();
    }
}
