package com.ali.auth.third.offline.login.task;

import android.text.TextUtils;
import com.ali.auth.third.core.message.Message;
import com.ali.auth.third.core.message.MessageUtils;
import com.ali.auth.third.core.model.ResultCode;
import com.ali.auth.third.core.task.AbsAsyncTask;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.JSONUtils;
import com.ali.auth.third.offline.context.BridgeCallbackContext;
import java.util.Map;
import org.json.JSONObject;

public class QrLoginConfirmTask extends AbsAsyncTask<String, Void, Void> {
    private static final String TAG = "login";
    private BridgeCallbackContext bridgeCallbackContext;

    public QrLoginConfirmTask(BridgeCallbackContext bridgeCallbackContext2) {
        this.bridgeCallbackContext = bridgeCallbackContext2;
    }

    /* access modifiers changed from: protected */
    public Void asyncExecute(String... params) {
        if (TextUtils.isEmpty(params[0])) {
            this.bridgeCallbackContext.getActivity().setResult(ResultCode.SUCCESS.code);
            this.bridgeCallbackContext.getActivity().finish();
        } else {
            try {
                Map m = (Map) JSONUtils.toMap(new JSONObject(params[0]).optJSONObject("params")).get("qrConfirmInfo");
                if (m == null) {
                    this.bridgeCallbackContext.getActivity().setResult(ResultCode.SUCCESS.code);
                    this.bridgeCallbackContext.getActivity().finish();
                } else {
                    int code = ((Integer) m.get("action")).intValue();
                    if (code == 0) {
                        this.bridgeCallbackContext.getActivity().setResult(ResultCode.SUCCESS.code);
                    } else if (-1 == code) {
                        this.bridgeCallbackContext.getActivity().setResult(ResultCode.USER_CANCEL.code);
                    }
                    this.bridgeCallbackContext.getActivity().finish();
                }
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void doWhenException(Throwable t) {
        Message errorMessage = MessageUtils.createMessage(10010, t.getMessage());
        SDKLogger.log("login", errorMessage, t);
        this.bridgeCallbackContext.onFailure(errorMessage.code, errorMessage.message);
    }

    /* access modifiers changed from: protected */
    public void doFinally() {
    }
}
