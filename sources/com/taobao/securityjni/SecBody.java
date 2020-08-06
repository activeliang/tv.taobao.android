package com.taobao.securityjni;

import android.content.ContextWrapper;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.securitybody.ISecurityBodyComponent;
import com.taobao.wireless.security.sdk.staticdatastore.IStaticDataStoreComponent;

@Deprecated
public class SecBody {
    private ISecurityBodyComponent proxy;
    private IStaticDataStoreComponent staticDataStoreComp;

    public SecBody(ContextWrapper context) {
        SecurityGuardManager manager = SecurityGuardManager.getInstance(context);
        if (manager != null) {
            this.proxy = manager.getSecurityBodyComp();
            this.staticDataStoreComp = manager.getStaticDataStoreComp();
        }
    }

    public String getSecBodyData(String timeStamp) {
        if (this.proxy == null || timeStamp == null || timeStamp.length() <= 0) {
            return null;
        }
        String appKey = GlobalInit.GetGlobalAppKey();
        if (appKey == null) {
            appKey = this.staticDataStoreComp.getAppKeyByIndex(0);
        }
        return this.proxy.getSecurityBodyData(timeStamp, appKey);
    }
}
