package com.ali.user.open.ucc.biz;

import android.text.TextUtils;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.model.RpcRequestCallbackWithCode;
import com.ali.user.open.core.model.RpcResponse;
import com.ali.user.open.ucc.UccCallback;
import com.ali.user.open.ucc.UccServiceProviderFactory;
import com.ali.user.open.ucc.data.DataRepository;
import com.ali.user.open.ucc.model.UccParams;
import com.ali.user.open.ucc.util.UTHitConstants;
import com.ali.user.open.ucc.util.UTHitUtils;
import com.ali.user.open.ucc.util.Utils;
import java.util.HashMap;
import java.util.Map;

public class UccUnbindPresenter {
    private static volatile UccUnbindPresenter instance;

    public static UccUnbindPresenter getInstance() {
        if (instance == null) {
            synchronized (UccUnbindPresenter.class) {
                if (instance == null) {
                    instance = new UccUnbindPresenter();
                }
            }
        }
        return instance;
    }

    public void doUnbind(final UccParams uccParams, final String targetSite, final UccCallback uccCallback) {
        DataRepository.unbind(uccParams, new RpcRequestCallbackWithCode() {
            public void onSuccess(RpcResponse response) {
                rpcResultlHit(response.code + "");
                if (response == null) {
                    return;
                }
                if (TextUtils.equals("SUCCESS", response.actionType)) {
                    UccServiceProviderFactory.getInstance().getUccServiceProvider(targetSite).cleanUp(KernelContext.getApplicationContext());
                    if (uccCallback != null) {
                        uccCallback.onSuccess(targetSite, new HashMap<>());
                    }
                } else if (uccCallback != null) {
                    uccCallback.onFail(targetSite, Utils.buidErrorCode(response, 1009), Utils.buidErrorMessage(response, "解绑失败"));
                }
            }

            public void onSystemError(String code, RpcResponse response) {
                int errorCode = Utils.buidErrorCode(response, 1009);
                rpcResultlHit(errorCode + "");
                if (uccCallback != null) {
                    uccCallback.onFail(targetSite, errorCode, Utils.buidErrorMessage(response, "解绑失败"));
                }
            }

            public void onError(String code, RpcResponse response) {
                int errorCode = Utils.buidErrorCode(response, 1009);
                rpcResultlHit(errorCode + "");
                if (uccCallback != null) {
                    uccCallback.onFail(targetSite, errorCode, Utils.buidErrorMessage(response, "解绑失败"));
                }
            }

            private void rpcResultlHit(String code) {
                Map<String, String> props = new HashMap<>();
                props.put("code", code);
                UTHitUtils.send(UTHitConstants.PageUccUnBind, "UccUnbind_Invoke_Result", uccParams, props);
            }
        });
    }
}
