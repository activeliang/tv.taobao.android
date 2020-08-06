package com.yunos.tv.blitz.account;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.yunos.tv.blitz.account.LoginHelper;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tv.blitz.global.BzAppConfig;
import com.yunos.tv.blitz.global.BzApplication;
import com.yunos.tv.blitz.listener.internal.BzJsCallAccountListener;
import org.json.JSONException;
import org.json.JSONObject;

public class BzJsCallImpAccountListener implements BzJsCallAccountListener {
    /* access modifiers changed from: private */
    public static final String TAG = BzJsCallImpAccountListener.class.getCanonicalName();
    public static final int TYPE_ALL = 0;
    public static final int TYPE_TAOBAO = 1;
    public static final int TYPE_YOUKU = 2;
    Handler mHandler = new Handler();

    public String onAccountIsLogin(Context context, String param) {
        return isLogin(context, param);
    }

    public String onAccountLogin(Context context, String param) {
        return login(context, param);
    }

    public String onAccountGetUserInfo(Context context, String param, int callback) {
        return getUserInfo(context, param, callback);
    }

    public String onAccountGetToken(Context context, String param, int callback) {
        return getUserToken(context, param, callback);
    }

    public String onAccountCommonApi(Context context, String param, int callback) {
        accountCommonApi_noBlock(context, param, callback);
        return "";
    }

    public String onAccountApplyMtopToken(Context context, String param, int callback) {
        accountApplyMtopToken(context, param, callback);
        return "";
    }

    public String onApplyNewMtopToken(Context context, String param, int callback) {
        accountApplyNewMtopToken(context, param, callback);
        return "";
    }

    public void accountApplyMtopToken(Context context, String param, int callback) {
    }

    public void accountApplyNewMtopToken(Context context, String param, int callback) {
    }

    public String accountCommonApi(Context context, String param, int callback) {
        BzResult result = new BzResult();
        result.setResult("HY_FAILED");
        ((BzApplication) BzAppConfig.context.getContext()).replyCallBack(callback, false, result.toJsonString());
        return "";
    }

    public void accountCommonApi_noBlock(Context context, String param, int callback) {
    }

    public int getTokeType(String param) {
        BzDebugLog.d(TAG, "getTokeType start :" + param);
        int result = 1;
        try {
            JSONObject obj = new JSONObject(param);
            if (obj == null) {
                return 1;
            }
            if (obj.has("type")) {
                Object type_obj = obj.opt("type");
                if (type_obj instanceof Integer) {
                    int type = obj.optInt("type");
                    if (type == 0) {
                        result = 0;
                    } else if (type == 2) {
                        result = 2;
                    }
                } else if (type_obj instanceof String) {
                    String type2 = obj.optString("type");
                    if (type2.equalsIgnoreCase("2")) {
                        result = 2;
                    } else if (type2.equalsIgnoreCase("0")) {
                        result = 0;
                    }
                }
            }
            BzDebugLog.d(TAG, "result = " + result);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getUserToken(Context context, String param, int callback) {
        return null;
    }

    private String login(Context context, String params) {
        BzResult result = new BzResult();
        Log.w(TAG, "context:" + context);
        if (!(context instanceof Activity)) {
            result.setResult("HY_FAILED");
            return result.toJsonString();
        }
        String force_login = null;
        try {
            force_login = new JSONObject(params).getString("force_login");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        boolean bForceLogin = force_login != null && force_login.equals("true");
        Activity activity = (Activity) context;
        int tokeType = getTokeType(params);
        BzApplication.getLoginHelper(context).startYunosAccountActivity(context, bForceLogin);
        result.setSuccess();
        return result.toJsonString();
    }

    private String isLogin(Context context, String params) {
        boolean isLogin;
        BzResult result = new BzResult();
        if (!(context instanceof Activity)) {
            result.setResult("HY_FAILED");
            return result.toJsonString();
        }
        if (getTokeType(params) == 2) {
            isLogin = AccountUtils.checkYoukuLogin();
        } else {
            isLogin = BzApplication.getLoginHelper(context).isLogin();
        }
        result.addData("result", isLogin);
        result.setSuccess();
        return result.toJsonString();
    }

    private String getUserInfo(Context context, String params, int callback) {
        final BzResult result = new BzResult();
        final BzApplication app = (BzApplication) BzAppConfig.context.getContext();
        int tokenType = getTokeType(params);
        Log.d(TAG, "tyidVersion=" + AccountUtils.getVersioncode(BzAppConfig.context.getContext(), "com.aliyun.ams.tyid") + " ,type = " + tokenType);
        if (tokenType == 1) {
            if (!BzApplication.getLoginHelper(BzAppConfig.context.getContext()).isLogin()) {
                app.replyCallBack(callback, false, BzResult.RET_NOT_LOGIN.toJsonString());
                return "";
            }
            if (BzApplication.getLoginHelper(BzAppConfig.context.getContext()).getRegistedContext() == null) {
                BzApplication.getLoginHelper(BzAppConfig.context.getContext()).registerLoginListener(BzAppConfig.context.getContext());
            }
            final LoginHelper helper = BzApplication.getLoginHelper(BzAppConfig.context.getContext());
            final int i = callback;
            helper.addSyncLoginListener(new LoginHelper.SyncLoginListener() {
                public void onLogin(boolean isSuccess) {
                    BzApplication.getLoginHelper(BzAppConfig.context.getContext()).removeSyncLoginListener(this);
                    if (isSuccess) {
                        result.addData("userNick", helper.getNick());
                        result.addData("sid", helper.getSessionId());
                        BzDebugLog.d(BzJsCallImpAccountListener.TAG, "usernick:" + helper.getNick() + "sid:" + helper.getSessionId());
                        result.setSuccess();
                        app.replyCallBack(i, true, result.toJsonString());
                        return;
                    }
                    app.replyCallBack(i, false, BzResult.RET_NOT_LOGIN.toJsonString());
                }
            });
            BzApplication.getLoginHelper(BzAppConfig.context.getContext()).login(BzAppConfig.context.getContext());
            result.setSuccess();
            return result.toJsonString();
        } else if (tokenType != 0) {
            app.replyCallBack(callback, false, BzResult.RET_NOT_LOGIN.toJsonString());
            return "";
        } else if (!BzApplication.getLoginHelper(BzAppConfig.context.getContext()).isLogin()) {
            app.replyCallBack(callback, false, BzResult.RET_NOT_LOGIN.toJsonString());
            return "";
        } else {
            if (BzApplication.getLoginHelper(BzAppConfig.context.getContext()).getRegistedContext() == null) {
                BzApplication.getLoginHelper(BzAppConfig.context.getContext()).registerLoginListener(BzAppConfig.context.getContext());
            }
            final LoginHelper helper2 = BzApplication.getLoginHelper(BzAppConfig.context.getContext());
            final int i2 = callback;
            helper2.addSyncLoginListener(new LoginHelper.SyncLoginListener() {
                public void onLogin(boolean isSuccess) {
                    BzApplication.getLoginHelper(BzAppConfig.context.getContext()).removeSyncLoginListener(this);
                    if (isSuccess) {
                        result.addData("userNick", helper2.getNick());
                        result.addData("sid", helper2.getSessionId());
                        BzDebugLog.d(BzJsCallImpAccountListener.TAG, "usernick:" + helper2.getNick() + "sid:" + helper2.getSessionId());
                        result.setSuccess();
                        app.replyCallBack(i2, true, result.toJsonString());
                        return;
                    }
                    app.replyCallBack(i2, false, BzResult.RET_NOT_LOGIN.toJsonString());
                }
            });
            BzApplication.getLoginHelper(BzAppConfig.context.getContext()).login(BzAppConfig.context.getContext());
            result.setSuccess();
            return result.toJsonString();
        }
    }
}
