package com.yunos.tv.core.common;

import android.content.SharedPreferences;
import android.taobao.windvane.connect.api.ApiConstants;
import android.text.TextUtils;
import com.ali.auth.third.core.model.InternalSession;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.core.service.impl.CredentialManager;
import com.ut.mini.UTAnalytics;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.Serializable;
import org.json.JSONObject;

public class TvUser implements Serializable {
    private static final String TAG = "JuUser";
    private static final long serialVersionUID = 3870687249006922603L;
    private String alipay;
    private String alipayEnable;
    private String ecode;
    private String hasPaid;
    private boolean isRecorded = false;
    private boolean isRegDeviceId = false;
    private boolean isUpdateLogo = true;
    private String loginDate;
    private String loginTime;
    private boolean mIsNickSecurePayDone = false;
    private Object mLock = new Object();
    private SharedPreferences mSecurePayDonePre;
    private String moneySum;
    private String mtopToken;
    private String nick;
    private String orderSum;
    private String password;
    private String refuandBiz;
    private String saveSum;
    private String sessionId;
    private String successBiz;
    private String toConfirmBiz;
    private String toPayBiz;
    private String topSession;
    private String userId;
    private String userLogo;

    public boolean updateUserFromJson(JSONObject json) {
        if (json != null) {
            String sid = json.optString("sid", "");
            String nick2 = json.optString(TvTaoSharedPerference.NICK, "");
            String userId2 = json.optString("userId", "");
            String mtopToken2 = json.optString("token", "");
            String topSession2 = json.optString("topSession", "");
            String ecode2 = json.optString(ApiConstants.ECODE, "");
            if (!TextUtils.isEmpty(sid) && !TextUtils.isEmpty(userId2) && !TextUtils.isEmpty(ecode2)) {
                setNick(nick2);
                setSessionId(sid);
                setUserId(userId2);
                setMtopToken(mtopToken2);
                setTopSession(topSession2);
                setEcode(ecode2);
                readSecurePayDone(nick2);
                ZpLogger.d(TAG, "更新会话信息成功");
                if (!TextUtils.isEmpty(nick2)) {
                    UTAnalytics.getInstance().updateUserAccount(nick2, userId2, (String) null);
                }
                return true;
            }
        }
        ZpLogger.d(TAG, "更新会话信息失败");
        return false;
    }

    public boolean updateFromSession(Session session) {
        InternalSession internalSession;
        if (session != null) {
            String sid = session.openSid;
            if (TextUtils.isEmpty(sid) && (internalSession = CredentialManager.INSTANCE.getInternalSession()) != null) {
                sid = internalSession.sid;
            }
            String nick2 = session.nick;
            String userId2 = session.userid;
            String mtopToken2 = session.topAccessToken;
            if (!TextUtils.isEmpty(userId2)) {
                setNick(nick2);
                setSessionId(sid);
                setUserId(userId2);
                setMtopToken(mtopToken2);
                readSecurePayDone(nick2);
                ZpLogger.d(TAG, "更新会话信息成功");
                if (!TextUtils.isEmpty(nick2)) {
                    UTAnalytics.getInstance().updateUserAccount(nick2, userId2, (String) null);
                }
                return true;
            }
        }
        ZpLogger.d(TAG, "更新会话信息失败");
        return false;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId2) {
        this.userId = userId2;
    }

    public boolean isRecorded() {
        return this.isRecorded;
    }

    public void setRecorded(boolean recorded) {
        this.isRecorded = recorded;
    }

    public boolean isRegDeviceId() {
        return this.isRegDeviceId;
    }

    public void setRegDeviceId(boolean regDeviceId) {
        this.isRegDeviceId = regDeviceId;
    }

    public boolean isUpdateLogo() {
        return this.isUpdateLogo;
    }

    public void setUpdateLogo(boolean updateLogo) {
        this.isUpdateLogo = updateLogo;
    }

    public boolean getisNickSecurePayDone() {
        return this.mIsNickSecurePayDone;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick2) {
        this.nick = nick2;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password2) {
        this.password = password2;
    }

    public String getSessionId() {
        String str;
        synchronized (this.mLock) {
            str = this.sessionId;
        }
        return str;
    }

    public void setSessionId(String sessionId2) {
        synchronized (this.mLock) {
            this.sessionId = sessionId2;
        }
    }

    public String getTopSession() {
        return this.topSession;
    }

    public void setTopSession(String topSession2) {
        this.topSession = topSession2;
    }

    public String getLoginDate() {
        return this.loginDate;
    }

    public void setLoginDate(String loginDate2) {
        this.loginDate = loginDate2;
    }

    public String getLoginTime() {
        return this.loginTime;
    }

    public void setLoginTime(String loginTime2) {
        this.loginTime = loginTime2;
    }

    public String getEcode() {
        String str;
        synchronized (this.mLock) {
            str = this.ecode;
        }
        return str;
    }

    public void setEcode(String ecode2) {
        synchronized (this.mLock) {
            this.ecode = ecode2;
        }
    }

    public String getMtopToken() {
        return this.mtopToken;
    }

    public void setMtopToken(String token) {
        this.mtopToken = token;
    }

    public String getUserLogo() {
        return this.userLogo;
    }

    public void setUserLogo(String userLogo2) {
        this.userLogo = userLogo2;
    }

    public String getAlipay() {
        return this.alipay;
    }

    public void setAlipay(String alipay2) {
        this.alipay = alipay2;
    }

    public String getAlipayEnable() {
        return this.alipayEnable;
    }

    public void setAlipayEnable(String alipayEnable2) {
        this.alipayEnable = alipayEnable2;
    }

    public String getHasPaid() {
        return this.hasPaid;
    }

    public void setHasPaid(String hasPaid2) {
        this.hasPaid = hasPaid2;
    }

    public String getRefuandBiz() {
        return this.refuandBiz;
    }

    public void setRefuandBiz(String refuandBiz2) {
        this.refuandBiz = refuandBiz2;
    }

    public String getSuccessBiz() {
        return this.successBiz;
    }

    public void setSuccessBiz(String successBiz2) {
        this.successBiz = successBiz2;
    }

    public String getToConfirmBiz() {
        return this.toConfirmBiz;
    }

    public void setToConfirmBiz(String toConfirmBiz2) {
        this.toConfirmBiz = toConfirmBiz2;
    }

    public String getToPayBiz() {
        return this.toPayBiz;
    }

    public void setToPayBiz(String toPayBiz2) {
        this.toPayBiz = toPayBiz2;
    }

    public String getOrderSum() {
        return this.orderSum;
    }

    public void setOrderSum(String orderSum2) {
        this.orderSum = orderSum2;
    }

    public String getMoneySum() {
        return this.moneySum;
    }

    public void setMoneySum(String moneySum2) {
        this.moneySum = moneySum2;
    }

    public String getSaveSum() {
        return this.saveSum;
    }

    public void setSaveSum(String saveSum2) {
        this.saveSum = saveSum2;
    }

    public void saveSecurePayDone(String userName) {
        if (userName == null) {
            this.mIsNickSecurePayDone = false;
            return;
        }
        if (this.mSecurePayDonePre == null) {
            this.mSecurePayDonePre = CoreApplication.getApplication().getSharedPreferences("securePayDone", 0);
        }
        SharedPreferences.Editor editor = this.mSecurePayDonePre.edit();
        editor.putBoolean(userName, true);
        this.mIsNickSecurePayDone = true;
        editor.commit();
    }

    public void readSecurePayDone(String userName) {
        if (userName == null) {
            this.mIsNickSecurePayDone = false;
            return;
        }
        if (this.mSecurePayDonePre == null) {
            this.mSecurePayDonePre = CoreApplication.getApplication().getSharedPreferences("securePayDone", 0);
        }
        this.mIsNickSecurePayDone = this.mSecurePayDonePre.getBoolean(userName, false);
    }

    public String toString() {
        return "JuUser [isRecorded=" + this.isRecorded + ", isRegDeviceId=" + this.isRegDeviceId + ", isUpdateLogo=" + this.isUpdateLogo + ", userId=" + this.userId + ", nick=" + this.nick + ", password=" + this.password + ", mIsNickSecurePayDone=" + this.mIsNickSecurePayDone + ", sessionId=" + this.sessionId + ", topSession=" + this.topSession + ", loginDate=" + this.loginDate + ", loginTime=" + this.loginTime + ", ecode=" + this.ecode + ", mtopToken=" + this.mtopToken + ", userLogo=" + this.userLogo + ", alipay=" + this.alipay + ", alipayEnable=" + this.alipayEnable + ", hasPaid=" + this.hasPaid + ", refuandBiz=" + this.refuandBiz + ", successBiz=" + this.successBiz + ", toConfirmBiz=" + this.toConfirmBiz + ", toPayBiz=" + this.toPayBiz + ", orderSum=" + this.orderSum + ", moneySum=" + this.moneySum + ", saveSum=" + this.saveSum + ", mLock=" + this.mLock + ", mSecurePayDonePre=" + this.mSecurePayDonePre + "]";
    }
}
