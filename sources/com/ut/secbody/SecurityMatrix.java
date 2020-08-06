package com.ut.secbody;

import com.taobao.securityjni.GlobalInit;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.securitybody.ISecurityBodyComponent;

@Deprecated
public class SecurityMatrix {
    public static boolean dataReceive(String usertrackData) {
        ISecurityBodyComponent securityBodyComponent = SecurityGuardManager.getInstance(GlobalInit.getGlobalContext()).getSecurityBodyComp();
        if (securityBodyComponent != null) {
            return securityBodyComponent.putUserTrackRecord(usertrackData);
        }
        return false;
    }
}
