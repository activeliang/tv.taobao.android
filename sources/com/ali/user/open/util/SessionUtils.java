package com.ali.user.open.util;

import com.ali.user.open.core.util.JSONUtils;
import com.ali.user.open.session.InternalSession;
import com.ali.user.open.tbauth.TbAuthConstants;
import com.ali.user.open.ucc.data.ApiConstants;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import org.json.JSONException;
import org.json.JSONObject;

public class SessionUtils {
    public static String toInternalSessionJSON(InternalSession session) {
        JSONObject object = new JSONObject();
        try {
            object.put("loginTime", session.loginTime);
            object.put("expireIn", session.expireIn);
            object.put("sid", session.sid);
            object.put("site", session.site);
            object.put("mobile", session.mobile);
            object.put("loginId", session.loginId);
            object.put(TbAuthConstants.KEY_AUTOLOGINTOKEN, session.autoLoginToken);
            object.put("topAccessToken", session.topAccessToken);
            object.put("topAuthCode", session.topAuthCode);
            object.put("topExpireTime", session.topExpireTime);
            object.put("avatarUrl", session.avatarUrl);
            object.put("userId", session.userId);
            object.put("openId", session.openId);
            object.put("openSid", session.openSid);
            object.put(TvTaoSharedPerference.NICK, session.nick);
            object.put(ApiConstants.ApiField.DEVICE_TOKEN_KEY, session.deviceTokenKey);
            object.put("deviceTokenSalt", session.deviceTokenSalt);
            if (session.otherInfo != null) {
                object.put("otherInfo", JSONUtils.toJsonObject(session.otherInfo));
            }
            return object.toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
