package com.ali.auth.third.core.util;

import android.app.Application;
import android.content.pm.PackageInfo;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.model.InternalSession;
import com.ali.auth.third.core.model.User;
import com.ali.user.open.tbauth.TbAuthConstants;
import com.ali.user.open.ucc.data.ApiConstants;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import org.json.JSONException;
import org.json.JSONObject;

public class SystemUtils {
    public static String toInternalSessionJSON(InternalSession session) {
        JSONObject object = new JSONObject();
        try {
            object.put("loginTime", session.loginTime);
            object.put("expireIn", session.expireIn);
            object.put("sid", session.sid);
            object.put("mobile", session.mobile);
            object.put("loginId", session.loginId);
            object.put(TbAuthConstants.KEY_AUTOLOGINTOKEN, session.autoLoginToken);
            object.put("topAccessToken", session.topAccessToken);
            object.put("topAuthCode", session.topAuthCode);
            object.put("topExpireTime", session.topExpireTime);
            object.put("ssoToken", session.ssoToken);
            User user = session.user;
            if (user != null) {
                JSONObject userObject = new JSONObject();
                userObject.put("avatarUrl", user.avatarUrl);
                userObject.put("userId", user.userId);
                userObject.put("openId", user.openId);
                userObject.put("openSid", user.openSid);
                userObject.put(TvTaoSharedPerference.NICK, user.nick);
                userObject.put(ApiConstants.ApiField.DEVICE_TOKEN_KEY, user.deviceTokenKey);
                userObject.put("deviceTokenSalt", user.deviceTokenSalt);
                object.put("user", userObject);
            }
            if (session.otherInfo != null) {
                object.put("otherInfo", JSONUtils.toJsonObject(session.otherInfo));
            }
            return object.toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getApkSignNumber() {
        try {
            return ((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(KernelContext.getApplicationContext().getPackageManager().getPackageInfo(KernelContext.getApplicationContext().getPackageName(), 64).signatures[0].toByteArray()))).getSerialNumber().toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getApkPublicKeyDigest() {
        try {
            PackageInfo packageInfo = KernelContext.getApplicationContext().getPackageManager().getPackageInfo(KernelContext.getApplicationContext().getPackageName(), 64);
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            digest.update(((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(packageInfo.signatures[0].toByteArray()))).getPublicKey().toString().getBytes());
            return CommonUtils.getHashString(digest.digest());
        } catch (Exception e) {
            return "";
        }
    }

    public static Application getSystemApp() {
        try {
            Class<?> activitythread = Class.forName("android.app.ActivityThread");
            Method m_currentActivityThread = activitythread.getDeclaredMethod("currentActivityThread", new Class[0]);
            Field f_mInitialApplication = activitythread.getDeclaredField("mInitialApplication");
            f_mInitialApplication.setAccessible(true);
            return (Application) f_mInitialApplication.get(m_currentActivityThread.invoke((Object) null, new Object[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
