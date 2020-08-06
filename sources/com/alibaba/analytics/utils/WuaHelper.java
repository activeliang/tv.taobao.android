package com.alibaba.analytics.utils;

import android.content.Context;
import com.alibaba.analytics.core.Variables;
import com.taobao.orange.OConstant;
import java.util.HashMap;

public class WuaHelper {
    public static String getMiniWua() {
        long time = 0;
        try {
            if (Logger.isDebug()) {
                time = System.currentTimeMillis();
            }
            Class<?> SecurityGuardManagerClass = Class.forName(OConstant.REFLECT_SECURITYGUARD);
            Object SecurityGuardManagerObj = SecurityGuardManagerClass.getMethod("getInstance", new Class[]{Context.class}).invoke((Object) null, new Object[]{Variables.getInstance().getContext()});
            Class<?> ISecurityBodyComponentClass = Class.forName("com.alibaba.wireless.security.open.securitybody.ISecurityBodyComponent");
            Object ISecurityBodyComponentObj = SecurityGuardManagerClass.getMethod("getInterface", new Class[]{Class.class}).invoke(SecurityGuardManagerObj, new Object[]{ISecurityBodyComponentClass});
            Class<?> SecurityBodyDefineClass = Class.forName("com.alibaba.wireless.security.open.securitybody.SecurityBodyDefine");
            int OPEN_SECURITYBODY_FLAG_FORMAT_MINI = SecurityBodyDefineClass.getField("OPEN_SECURITYBODY_FLAG_FORMAT_MINI").getInt(SecurityBodyDefineClass);
            int OPEN_SECURITYBODY_ENV_ONLINE = SecurityBodyDefineClass.getField("OPEN_SECURITYBODY_ENV_ONLINE").getInt(SecurityBodyDefineClass);
            Logger.d("OPEN_SECURITYBODY_FLAG_FORMAT_MINI:" + OPEN_SECURITYBODY_FLAG_FORMAT_MINI, new Object[0]);
            Logger.d("OPEN_SECURITYBODY_ENV_ONLINE:" + OPEN_SECURITYBODY_ENV_ONLINE, new Object[0]);
            String miniWua = (String) ISecurityBodyComponentClass.getMethod("getSecurityBodyDataEx", new Class[]{String.class, String.class, String.class, HashMap.class, Integer.TYPE, Integer.TYPE}).invoke(ISecurityBodyComponentObj, new Object[]{null, null, null, null, Integer.valueOf(OPEN_SECURITYBODY_FLAG_FORMAT_MINI), Integer.valueOf(OPEN_SECURITYBODY_ENV_ONLINE)});
            if (!Logger.isDebug()) {
                return miniWua;
            }
            Logger.d("Mini Wua: " + miniWua + ",waste time:" + (System.currentTimeMillis() - time), new Object[0]);
            return miniWua;
        } catch (Throwable e) {
            Logger.d("", e.toString());
            return null;
        }
    }
}
