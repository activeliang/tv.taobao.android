package com.yunos.tvtaobao.homebundle.h5.plugin;

import android.os.Handler;
import android.os.Message;
import com.alibaba.fastjson.JSON;
import com.yunos.tv.blitz.BlitzPlugin;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tvtaobao.homebundle.activity.HomeActivity;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;

public class EventBlitz {
    private static final int HAVE_SUPERNATANT = 0;
    private static final String TAG = "EventBlitz";
    private final String RESULT = "result";
    private BlitzEvent mBlitzCallback;
    /* access modifiers changed from: private */
    public WeakReference<HomeActivity> mEventReference;
    private MyHandler mHandler;

    public EventBlitz(WeakReference<HomeActivity> videoForJsReference) {
        ZpLogger.i(TAG, "init --> param_final" + videoForJsReference.get());
        this.mEventReference = videoForJsReference;
        this.mHandler = new MyHandler(new WeakReference(this));
        onInitPlugin();
    }

    private void onInitPlugin() {
        this.mBlitzCallback = new BlitzEvent(new WeakReference(this));
        BlitzPlugin.bindingJs("haveSupernatant", this.mBlitzCallback);
    }

    private static class MyHandler extends Handler {
        private WeakReference<EventBlitz> eventBlitzWR;

        private MyHandler(WeakReference<EventBlitz> eventBlitzWeakReference) {
            this.eventBlitzWR = eventBlitzWeakReference;
        }

        public void handleMessage(Message msg) {
            HomeActivity activity;
            switch (msg.what) {
                case 0:
                    if (this.eventBlitzWR != null && this.eventBlitzWR.get() != null && (activity = (HomeActivity) ((EventBlitz) this.eventBlitzWR.get()).mEventReference.get()) != null) {
                        activity.interceptBack(JSON.parseObject(msg.obj.toString()).getBoolean("isIntercept").booleanValue());
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean onHandleCall(String param, long cbData) {
        String param_final = param;
        long cbData_final = cbData;
        ZpLogger.i(TAG, "onHandleCall --> param_final  =" + param_final + ";  cbData_final = " + cbData_final);
        HomeActivity homeActivity = null;
        if (!(this.mEventReference == null || this.mEventReference.get() == null)) {
            homeActivity = (HomeActivity) this.mEventReference.get();
        }
        if (homeActivity == null) {
            BzResult result = new BzResult();
            result.addData("result", "false");
            result.setSuccess();
            BlitzPlugin.responseJs(false, result.toJsonString(), cbData_final);
            return true;
        }
        BzResult result2 = new BzResult();
        result2.setSuccess();
        BlitzPlugin.responseJs(true, result2.toJsonString(), cbData_final);
        String methodName = JSON.parseObject(param_final).getString("methodName");
        if (!(param_final == null || methodName == null)) {
            ZpLogger.e(TAG, "param : " + param_final + "  methodName : " + methodName);
            Message msg = new Message();
            msg.obj = param_final;
            if (methodName.equals("haveSupernatant")) {
                msg.what = 0;
            }
            this.mHandler.sendMessage(msg);
        }
        return true;
    }

    private class BlitzEvent implements BlitzPlugin.JsCallback {
        private WeakReference<EventBlitz> mReference;

        public BlitzEvent(WeakReference<EventBlitz> reference) {
            this.mReference = reference;
        }

        public void onCall(String param, long cbData) {
            ZpLogger.i(EventBlitz.TAG, "onCall --> param  =" + param + ";  cbData = " + cbData + "; mRefrence = " + this.mReference + "; mReference.get=" + this.mReference.get());
            if (this.mReference != null && this.mReference.get() != null) {
                boolean unused = ((EventBlitz) this.mReference.get()).onHandleCall(param, cbData);
            }
        }
    }
}
