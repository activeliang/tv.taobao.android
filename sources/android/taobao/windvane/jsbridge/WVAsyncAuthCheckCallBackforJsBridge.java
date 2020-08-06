package android.taobao.windvane.jsbridge;

import android.taobao.windvane.jsbridge.WVAsyncAuthCheck;
import android.taobao.windvane.util.TaoLog;

public class WVAsyncAuthCheckCallBackforJsBridge implements WVAsyncAuthCheck.AsyncAuthCheckCallBack {
    String TAG = "WVAsyncAuthCheckCallBackforJsBridge";

    public void callBackSuccess(String pageUrl, WVCallMethodContext callmethod) {
        WVJsBridge.aftercallMethod(callmethod, pageUrl);
        TaoLog.w(this.TAG, "Async preprocessor callBackSuccess ");
    }

    public void callBackFail(String pageUrl, WVCallMethodContext callmethod) {
        WVJsBridge.startCall(3, callmethod);
        TaoLog.w(this.TAG, "Async preprocessor callBackSuccess ,");
    }
}
