package android.taobao.windvane.jsbridge;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class WVJsBridge implements Handler.Callback {
    public static final int CALL_EXECUTE = 0;
    public static final int CALL_METHOD = 1;
    public static final int CLOSED = 4;
    public static final int NO_METHOD = 2;
    public static final int NO_PERMISSION = 3;
    private static final String TAG = "WVJsBridge";
    private static Handler mHandler;
    private static WVJsBridge mJsBridge;
    private boolean enabled = true;
    private boolean isInit = false;
    private boolean mSkipPreprocess;
    public ArrayList<WVCallMethodContext> mTailBridges = null;

    public synchronized void tryToRunTailBridges() {
        if (this.mTailBridges != null) {
            Iterator i$ = this.mTailBridges.iterator();
            while (i$.hasNext()) {
                WVCallMethodContext callMethodContext = i$.next();
                aftercallMethod(callMethodContext, "");
                TaoLog.i(TAG, "excute TailJSBridge : " + callMethodContext.objectName + " : " + callMethodContext.methodName);
            }
            this.mTailBridges.clear();
            this.mTailBridges = null;
        }
    }

    public static synchronized WVJsBridge getInstance() {
        WVJsBridge wVJsBridge;
        synchronized (WVJsBridge.class) {
            if (mJsBridge == null) {
                mJsBridge = new WVJsBridge();
            }
            wVJsBridge = mJsBridge;
        }
        return wVJsBridge;
    }

    private WVJsBridge() {
        mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public synchronized void init() {
        this.isInit = true;
    }

    public void skipPreprocess() {
        this.mSkipPreprocess = true;
    }

    public void setEnabled(boolean enabled2) {
        this.enabled = enabled2;
    }

    public void exCallMethod(WVPluginEntryManager entryManager, WVCallMethodContext wvCallMethodContext, IJsApiFailedCallBack failedCallBack, IJsApiSucceedCallBack succeedCallBack) {
        if (wvCallMethodContext != null) {
            wvCallMethodContext.failedCallBack = failedCallBack;
            wvCallMethodContext.succeedCallBack = succeedCallBack;
            if (wvCallMethodContext.objectName != null) {
                wvCallMethodContext.classinstance = entryManager.getEntry(wvCallMethodContext.objectName);
                if (wvCallMethodContext.classinstance instanceof WVApiPlugin) {
                    TaoLog.i(TAG, "call new method execute.");
                    startCall(0, wvCallMethodContext);
                }
            }
        }
    }

    public void callMethod(IWVWebView webView, String url) {
        callMethod(webView, url, (IJsApiSucceedCallBack) null, (IJsApiFailedCallBack) null);
    }

    private void callMethod(IWVWebView webView, String url, IJsApiSucceedCallBack succeedCallBack, IJsApiFailedCallBack failedCallBack) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d(TAG, "callMethod: url=" + url);
        }
        if (!this.isInit) {
            TaoLog.w(TAG, "jsbridge is not init.");
            return;
        }
        final WVCallMethodContext request = getRequest(url);
        if (request == null) {
            TaoLog.w(TAG, "url format error and call canceled. url=" + url);
            return;
        }
        request.webview = webView;
        if (succeedCallBack != null) {
            request.succeedCallBack = succeedCallBack;
        }
        if (failedCallBack != null) {
            request.failedCallBack = failedCallBack;
        }
        final String pageUrl = webView.getUrl();
        new AsyncTask<Void, Integer, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... params) {
                WVJsBridge.this.callMethod(request, pageUrl);
                return null;
            }
        }.execute(new Void[0]);
    }

    /* access modifiers changed from: private */
    public void callMethod(WVCallMethodContext callMethod, String pageUrl) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d(TAG, String.format("callMethod-obj:%s method:%s param:%s sid:%s", new Object[]{callMethod.objectName, callMethod.methodName, callMethod.params, callMethod.token}));
        }
        if (WVMonitorService.getJsBridgeMonitor() != null) {
            WVMonitorService.getJsBridgeMonitor().didCallAtURL(callMethod.objectName, callMethod.methodName, pageUrl);
        }
        if (!this.enabled || callMethod.webview == null) {
            TaoLog.w(TAG, "jsbridge is closed.");
            startCall(4, callMethod);
            return;
        }
        if (!this.mSkipPreprocess) {
            if (WVJsbridgeService.getJSBridgePreprocessors() != null && !WVJsbridgeService.getJSBridgePreprocessors().isEmpty()) {
                for (WVJSAPIAuthCheck preprocessor : WVJsbridgeService.getJSBridgePreprocessors()) {
                    if (!preprocessor.apiAuthCheck(pageUrl, callMethod.objectName, callMethod.methodName, callMethod.params)) {
                        TaoLog.w(TAG, "preprocessor call fail, callMethod cancel.");
                        startCall(3, callMethod);
                        return;
                    }
                }
            }
            if (WVJsbridgeService.getJSBridgeayncPreprocessors() != null && !WVJsbridgeService.getJSBridgeayncPreprocessors().isEmpty()) {
                for (WVAsyncAuthCheck preprocessor2 : WVJsbridgeService.getJSBridgeayncPreprocessors()) {
                    if (preprocessor2.AsyncapiAuthCheck(pageUrl, callMethod, new WVAsyncAuthCheckCallBackforJsBridge())) {
                        TaoLog.w(TAG, "enter  WVAsyncAuthCheck preprocessor  ");
                        return;
                    }
                }
            }
        }
        aftercallMethod(callMethod, pageUrl);
    }

    public static void aftercallMethod(WVCallMethodContext callmethod, String pageUrl) {
        Map<String, String> origPlugin = WVPluginManager.getOriginalPlugin(callmethod.objectName, callmethod.methodName);
        if (origPlugin != null) {
            if (TaoLog.getLogStatus()) {
                TaoLog.i(TAG, "call method through alias name. newObject: " + origPlugin.get("name") + " newMethod: " + origPlugin.get("method"));
            }
            callmethod.objectName = origPlugin.get("name");
            callmethod.methodName = origPlugin.get("method");
        }
        Object classinstance = callmethod.webview.getJsObject(callmethod.objectName);
        if (classinstance == null) {
            TaoLog.w(TAG, "callMethod: Plugin " + callmethod.objectName + " didn't found, you should call WVPluginManager.registerPlugin first.");
        } else if (classinstance instanceof WVApiPlugin) {
            TaoLog.i(TAG, "call new method execute.");
            callmethod.classinstance = classinstance;
            startCall(0, callmethod);
            return;
        } else {
            try {
                if (callmethod.methodName != null) {
                    Method method = classinstance.getClass().getMethod(callmethod.methodName, new Class[]{Object.class, String.class});
                    if (method.isAnnotationPresent(WindVaneInterface.class)) {
                        callmethod.classinstance = classinstance;
                        callmethod.method = method;
                        startCall(1, callmethod);
                        return;
                    }
                    TaoLog.w(TAG, "callMethod: Method " + callmethod.methodName + " didn't has @WindVaneInterface annotation, obj=" + callmethod.objectName);
                }
            } catch (NoSuchMethodException e) {
                TaoLog.e(TAG, "callMethod: Method " + callmethod.methodName + " didn't found. It must has two parameter, Object.class and String.class, obj=" + callmethod.objectName);
            }
        }
        startCall(2, callmethod);
    }

    public static void startCall(int type, WVCallMethodContext callmethod) {
        Message msg = Message.obtain();
        msg.what = type;
        msg.obj = callmethod;
        mHandler.sendMessage(msg);
    }

    public boolean handleMessage(Message msg) {
        WVCallMethodContext callmethod = (WVCallMethodContext) msg.obj;
        if (callmethod == null) {
            TaoLog.e(TAG, "CallMethodContext is null, and do nothing.");
            return false;
        }
        WVCallBackContext callback = new WVCallBackContext(callmethod.webview, callmethod.token, callmethod.objectName, callmethod.methodName, callmethod.succeedCallBack, callmethod.failedCallBack);
        switch (msg.what) {
            case 0:
                if (!((WVApiPlugin) callmethod.classinstance).execute(callmethod.methodName, TextUtils.isEmpty(callmethod.params) ? "{}" : callmethod.params, callback)) {
                    if (TaoLog.getLogStatus()) {
                        TaoLog.w(TAG, "WVApiPlugin execute failed. method: " + callmethod.methodName);
                    }
                    startCall(2, callmethod);
                } else {
                    try {
                        IWVWebView iWVWebView = callmethod.webview;
                        ConcurrentMap<String, Integer> jsHisMap = IWVWebView.JsbridgeHis;
                        int count = 1;
                        String bridge = String.format("%s.%s", new Object[]{callmethod.objectName, callmethod.methodName});
                        if (jsHisMap.containsKey(bridge)) {
                            count = Integer.valueOf(((Integer) jsHisMap.get(bridge)).intValue() + 1);
                        }
                        IWVWebView iWVWebView2 = callmethod.webview;
                        IWVWebView.JsbridgeHis.put(bridge, count);
                    } catch (Exception e) {
                    }
                }
                return true;
            case 1:
                Object object = callmethod.classinstance;
                try {
                    Method method = callmethod.method;
                    Object[] objArr = new Object[2];
                    objArr[0] = callback;
                    objArr[1] = TextUtils.isEmpty(callmethod.params) ? "{}" : callmethod.params;
                    method.invoke(object, objArr);
                } catch (Exception e2) {
                    TaoLog.e(TAG, "call method " + callmethod.method + " exception. " + e2.getMessage());
                }
                return true;
            case 2:
                WVResult result = new WVResult();
                result.setResult("HY_NO_HANDLER");
                if (WVMonitorService.getJsBridgeMonitor() != null) {
                    WVMonitorService.getJsBridgeMonitor().didOccurError(callmethod.objectName, callmethod.methodName, "HY_NO_HANDLER", callback.getWebview().getUrl());
                }
                callback.error(result);
                return true;
            case 3:
                WVResult result2 = new WVResult();
                result2.setResult("HY_NO_PERMISSION");
                if (WVMonitorService.getJsBridgeMonitor() != null) {
                    WVMonitorService.getJsBridgeMonitor().didOccurError(callmethod.objectName, callmethod.methodName, "HY_NO_PERMISSION", callback.getWebview().getUrl());
                }
                callback.error(result2);
                return true;
            case 4:
                WVResult result3 = new WVResult();
                result3.setResult("HY_CLOSED");
                if (WVMonitorService.getJsBridgeMonitor() != null) {
                    WVMonitorService.getJsBridgeMonitor().didOccurError(callmethod.objectName, callmethod.methodName, "HY_CLOSED", callback.getWebview().getUrl());
                }
                callback.error(result3);
                return true;
            default:
                return false;
        }
    }

    private WVCallMethodContext getRequest(String url) {
        if (url == null || !url.startsWith("hybrid://")) {
            return null;
        }
        try {
            WVCallMethodContext ctx = new WVCallMethodContext();
            int tmpIndex1 = url.indexOf(58, 9);
            ctx.objectName = url.substring(9, tmpIndex1);
            int tmpIndex2 = url.indexOf(47, tmpIndex1);
            ctx.token = url.substring(tmpIndex1 + 1, tmpIndex2);
            int tmpIndex3 = url.indexOf(63, tmpIndex2);
            if (tmpIndex3 > 0) {
                ctx.methodName = url.substring(tmpIndex2 + 1, tmpIndex3);
                ctx.params = url.substring(tmpIndex3 + 1);
            } else {
                ctx.methodName = url.substring(tmpIndex2 + 1);
            }
            if (ctx.objectName.length() > 0 && ctx.token.length() > 0 && ctx.methodName.length() > 0) {
                return ctx;
            }
        } catch (StringIndexOutOfBoundsException e) {
        }
        return null;
    }

    public void destroy() {
        this.isInit = false;
    }
}
