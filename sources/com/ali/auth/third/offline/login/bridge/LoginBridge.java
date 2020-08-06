package com.ali.auth.third.offline.login.bridge;

import android.os.AsyncTask;
import android.text.TextUtils;
import com.ali.auth.third.core.util.JSONUtils;
import com.ali.auth.third.offline.context.BridgeCallbackContext;
import com.ali.auth.third.offline.login.task.BindByUsernameTask;
import com.ali.auth.third.offline.login.task.LoginByQrCodeTask;
import com.ali.auth.third.offline.login.task.LoginByUsernameTask;
import com.ali.auth.third.offline.login.task.QrLoginConfirmTask;
import com.ali.auth.third.offline.webview.BridgeMethod;
import com.ali.user.open.tbauth.TbAuthConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginBridge {
    @BridgeMethod
    public void auth(BridgeCallbackContext bridgeCallbackContext, String loginRequest) {
        new LoginByUsernameTask(bridgeCallbackContext).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{loginRequest});
    }

    @BridgeMethod
    public void bindByUsername(BridgeCallbackContext bridgeCallbackContext, String loginRequest) {
        if (!TextUtils.isEmpty(loginRequest)) {
            try {
                JSONObject params = new JSONObject(loginRequest).optJSONObject("params");
                if (params != null) {
                    String loginInfo = JSONUtils.optString(params, TbAuthConstants.PARAN_LOGIN_INFO);
                    new BindByUsernameTask(bridgeCallbackContext).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{loginInfo});
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @BridgeMethod
    public void unbindByUsername(BridgeCallbackContext bridgeCallbackContext, String loginRequest) {
    }

    @BridgeMethod
    public void loginByUsername(BridgeCallbackContext bridgeCallbackContext, String loginRequest) {
        new LoginByUsernameTask(bridgeCallbackContext).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{loginRequest});
    }

    @BridgeMethod
    public void loginByQrCode(BridgeCallbackContext bridgeCallbackContext, String loginRequest) {
        new LoginByQrCodeTask(bridgeCallbackContext, false).execute(new String[]{loginRequest});
    }

    @BridgeMethod
    public void qrLoginConfirm(BridgeCallbackContext bridgeCallbackContext, String loginRequest) {
        new QrLoginConfirmTask(bridgeCallbackContext).execute(new String[]{loginRequest});
    }
}
