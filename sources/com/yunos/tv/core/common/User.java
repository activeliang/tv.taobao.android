package com.yunos.tv.core.common;

import android.text.TextUtils;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.core.service.impl.CredentialManager;
import com.yunos.tv.blitz.global.BzApplication;
import org.json.JSONObject;

public class User {
    private static TvUser user;

    public static TvUser getUser() {
        return user;
    }

    public static void setUser(TvUser user2) {
        user = user2;
    }

    public static boolean updateUser(JSONObject json) {
        if (user == null) {
            user = new TvUser();
        }
        return user.updateUserFromJson(json);
    }

    public static boolean updateUser(Session session) {
        if (user == null) {
            user = new TvUser();
        }
        return user.updateFromSession(session);
    }

    public static void clearUser() {
        user = null;
    }

    public static boolean isLogined() {
        return BzApplication.getLoginHelper(KernelContext.getApplicationContext()).isLogin();
    }

    public static String getUserId() {
        String uid = user == null ? null : user.getUserId();
        try {
            if (!TextUtils.isEmpty(uid) || CredentialManager.INSTANCE.getSession() == null) {
                return uid;
            }
            return CredentialManager.INSTANCE.getSession().userid;
        } catch (Exception e) {
            e.printStackTrace();
            return uid;
        } catch (NoClassDefFoundError e2) {
            e2.printStackTrace();
            return uid;
        }
    }

    public static String getNick() {
        String nick = user == null ? null : user.getNick();
        if (!TextUtils.isEmpty(nick) || CredentialManager.INSTANCE.getSession() == null) {
            return nick;
        }
        return CredentialManager.INSTANCE.getSession().nick;
    }

    public static String getPassword() {
        if (user == null) {
            return null;
        }
        return user.getPassword();
    }

    public static String getSessionId() {
        if (user == null) {
            return null;
        }
        return user.getSessionId();
    }

    public static String getTopSessionId() {
        if (user == null) {
            return null;
        }
        return user.getTopSession();
    }

    public static String getEcode() {
        if (user == null) {
            return null;
        }
        return user.getEcode();
    }

    public static boolean getIsNickSecurePayDone() {
        return (user == null ? null : Boolean.valueOf(user.getisNickSecurePayDone())).booleanValue();
    }
}
