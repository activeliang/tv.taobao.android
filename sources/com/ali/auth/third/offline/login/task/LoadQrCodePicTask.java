package com.ali.auth.third.offline.login.task;

import com.ali.auth.third.core.callback.ResultCallback;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.rpc.PureHttpConnectionHelper;
import com.ali.auth.third.core.task.AbsAsyncTask;
import com.ali.auth.third.core.util.JSONUtils;
import com.ali.auth.third.offline.login.util.QRCodeUtil;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class LoadQrCodePicTask extends AbsAsyncTask {
    /* access modifiers changed from: private */
    public ResultCallback callback;

    public LoadQrCodePicTask(ResultCallback callback2) {
        this.callback = callback2;
    }

    /* access modifiers changed from: protected */
    public Object asyncExecute(Object[] objects) {
        int qrWidth;
        String error_correction;
        int qrHeight = 480;
        try {
            final Map<String, Object> resultData = new HashMap<>();
            JSONObject context = new JSONObject(PureHttpConnectionHelper.get(ConfigManager.getQrCodeContentUrl));
            JSONUtils.toMap(resultData, context);
            if (objects == null || objects.length < 1) {
                qrWidth = 480;
            } else {
                qrWidth = objects[0].intValue();
            }
            if (objects != null && objects.length >= 2) {
                qrHeight = objects[1].intValue();
            }
            if (objects == null || objects.length < 3) {
                error_correction = null;
            } else {
                error_correction = objects[2];
            }
            resultData.put("imageBitMap", QRCodeUtil.createQRCodeBitmap(context.getString("url"), qrWidth, qrHeight, error_correction));
            KernelContext.executorService.postUITask(new Runnable() {
                public void run() {
                    LoadQrCodePicTask.this.callback.onSuccess(resultData);
                }
            });
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* access modifiers changed from: protected */
    public void doWhenException(Throwable t) {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                LoadQrCodePicTask.this.callback.onFailure(0, "");
            }
        });
    }

    /* access modifiers changed from: protected */
    public void doFinally() {
    }
}
