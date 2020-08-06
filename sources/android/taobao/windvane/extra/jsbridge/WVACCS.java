package android.taobao.windvane.extra.jsbridge;

import android.content.Context;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;
import com.taobao.accs.ACCSManager;
import com.taobao.tao.remotebusiness.js.MtopJSBridge;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

public class WVACCS extends WVApiPlugin {
    private static final String serviceClassName = "android.taobao.windvane.extra.jsbridge.WVACCSService";
    private static final String serviceIdDefault = "windvane";
    private ArrayList<String> serviceIdList = new ArrayList<>();

    public static class ACCSWVEventListener implements WVEventListener {
        private WeakReference<IWVWebView> webview;

        public ACCSWVEventListener(IWVWebView view) {
            this.webview = new WeakReference<>(view);
        }

        public WVEventResult onEvent(int eventId, WVEventContext context, Object... objs) {
            IWVWebView mWebView = (IWVWebView) this.webview.get();
            if (mWebView != null) {
                switch (eventId) {
                    case WVEventId.ACCS_ONDATA /*5001*/:
                        String serviceId = objs[0];
                        String resultData = new String(objs[1]);
                        try {
                            JSONObject object = new JSONObject();
                            object.put(BaseConfig.INTENT_KEY_SERVIECID, serviceId);
                            object.put("resultData", resultData);
                            String data = object.toString();
                            mWebView.fireEvent("WV.Event.ACCS.OnData", data);
                            if (TaoLog.getLogStatus()) {
                                TaoLog.i("ACCS", data);
                                break;
                            }
                        } catch (Throwable th) {
                            break;
                        }
                        break;
                    case WVEventId.ACCS_ONCONNECTED /*5002*/:
                        mWebView.fireEvent("WV.Event.ACCS.OnConnected", "{}");
                        if (TaoLog.getLogStatus()) {
                            TaoLog.e("ACCS", "ACCS connect");
                            break;
                        }
                        break;
                    case WVEventId.ACCS_ONDISONNECTED /*5003*/:
                        mWebView.fireEvent("WV.Event.ACCS.OnDisConnected", "{}");
                        if (TaoLog.getLogStatus()) {
                            TaoLog.e("ACCS", "ACCS disconnect");
                            break;
                        }
                        break;
                }
            } else if (TaoLog.getLogStatus()) {
                TaoLog.e("ACCS", "webview is recycled");
            }
            return null;
        }
    }

    public void initialize(Context context, IWVWebView webView) {
        super.initialize(context, webView);
        init(context);
    }

    private void init(Context context) {
        WVEventService.getInstance().addEventListener(new ACCSWVEventListener(this.mWebView));
    }

    public boolean execute(String action, String params, WVCallBackContext callback) {
        if ("bindService".equals(action)) {
            bindService(callback, params);
        } else if ("unBindService".equals(action)) {
            unBindService(callback, params);
        } else if ("setData".equals(action)) {
            setData(callback, params);
        } else if (!"connectionState".equals(action)) {
            return false;
        } else {
            connectionState(callback, params);
        }
        return true;
    }

    private void bindService(WVCallBackContext context, String params) {
        String serviceId = null;
        try {
            serviceId = new JSONObject(params).optString(BaseConfig.INTENT_KEY_SERVIECID, "");
        } catch (JSONException e) {
            context.error(new WVResult("HY_PARAM_ERR"));
        }
        if (TextUtils.isEmpty(serviceId)) {
            context.error(new WVResult("HY_PARAM_ERR"));
            return;
        }
        if (this.serviceIdList == null) {
            try {
                this.serviceIdList = new ArrayList<>();
                this.serviceIdList.add(serviceIdDefault);
                ACCSManager.registerSerivce(this.mContext.getApplicationContext(), serviceIdDefault, serviceClassName);
            } catch (Exception e2) {
            }
        }
        if (this.serviceIdList.contains(serviceId)) {
            context.success();
        } else if (this.mContext != null) {
            this.serviceIdList.add(serviceId);
            ACCSManager.registerSerivce(this.mContext.getApplicationContext(), serviceId, serviceClassName);
            context.success();
        } else {
            context.error();
        }
    }

    private void unBindService(WVCallBackContext context, String params) {
        String serviceId = null;
        try {
            serviceId = new JSONObject(params).optString(BaseConfig.INTENT_KEY_SERVIECID, "");
        } catch (JSONException e) {
            context.error(new WVResult("HY_PARAM_ERR"));
        }
        if (TextUtils.isEmpty(serviceId)) {
            context.error(new WVResult("HY_PARAM_ERR"));
            return;
        }
        if (this.serviceIdList == null) {
            this.serviceIdList = new ArrayList<>();
        }
        if (!this.serviceIdList.contains(serviceId)) {
            context.success();
        } else if (this.mContext != null) {
            this.serviceIdList.remove(serviceId);
            ACCSManager.unregisterService(this.mContext.getApplicationContext(), serviceId);
            context.success();
        } else {
            context.error();
        }
    }

    private void setData(WVCallBackContext context, String params) {
        ACCSManager.AccsRequest request;
        String serviceId = null;
        String data = null;
        try {
            JSONObject jsObj = new JSONObject(params);
            serviceId = jsObj.optString(BaseConfig.INTENT_KEY_SERVIECID, "");
            if (TextUtils.isEmpty(serviceId)) {
                WVResult result = new WVResult();
                result.addData("msg", "serviceId " + serviceId + " is not bind!");
                context.error(result);
                return;
            }
            String userId = jsObj.optString("userId", "");
            JSONObject options = jsObj.optJSONObject("options");
            data = jsObj.optString("data", "");
            if (options == null) {
                context.error(new WVResult("HY_PARAM_ERR"));
                return;
            }
            String dataId = options.optString("dataId", "");
            String host = options.optString("host", "");
            String tag = options.optString("tag", "");
            boolean isUnit = options.optBoolean("isUnit", false);
            int timeout = options.optInt(MtopJSBridge.MtopJSParam.TIMEOUT, 0);
            String target = options.optString("target", "");
            String businessId = options.optString("businessId", "");
            URL url = null;
            try {
                url = new URL(host);
            } catch (Exception e) {
            }
            request = new ACCSManager.AccsRequest(userId, serviceId, data.getBytes(), dataId, target, url, businessId);
            try {
                request.setTag(tag);
                request.setIsUnitBusiness(isUnit);
                request.setTimeOut(timeout);
            } catch (JSONException e2) {
                context.error(new WVResult("HY_PARAM_ERR"));
                if (!TextUtils.isEmpty(serviceId) && !TextUtils.isEmpty(data)) {
                }
                context.error(new WVResult("HY_PARAM_ERR"));
            }
            if (!TextUtils.isEmpty(serviceId) || !TextUtils.isEmpty(data) || request == null) {
                context.error(new WVResult("HY_PARAM_ERR"));
            }
            ACCSManager.sendData(this.mContext, request);
            context.success();
        } catch (JSONException e3) {
            request = null;
            context.error(new WVResult("HY_PARAM_ERR"));
            context.error(new WVResult("HY_PARAM_ERR"));
        }
    }

    private void connectionState(WVCallBackContext context, String params) {
        WVResult result = new WVResult();
        try {
            if (ACCSManager.getChannelState(this.mContext) == null) {
                result.addData("status", "false");
                context.error();
            }
        } catch (Exception e) {
            result.addData("status", "false");
            context.error();
        }
        result.addData("status", "true");
        context.success(result);
    }

    public void onDestroy() {
        if (!(this.mContext == null || this.serviceIdList == null)) {
            for (int i = 0; i < this.serviceIdList.size(); i++) {
                ACCSManager.unregisterService(this.mContext.getApplicationContext(), this.serviceIdList.get(i));
            }
            this.serviceIdList.clear();
            this.serviceIdList = null;
        }
        super.onDestroy();
    }
}
