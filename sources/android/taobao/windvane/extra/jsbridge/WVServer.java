package android.taobao.windvane.extra.jsbridge;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.WindVaneSDKForTB;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.connect.HttpConnector;
import android.taobao.windvane.connect.HttpRequest;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.connect.api.ApiConstants;
import android.taobao.windvane.connect.api.ApiRequest;
import android.taobao.windvane.connect.api.ApiResponse;
import android.taobao.windvane.connect.api.WVApiWrapper;
import android.taobao.windvane.extra.mtop.MtopApiAdapter;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WindVaneInterface;
import android.taobao.windvane.thread.LockObject;
import android.taobao.windvane.util.EnvUtil;
import android.taobao.windvane.util.TaoLog;
import android.widget.Toast;
import com.yunos.tv.alitvasrsdk.CommonData;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WVServer extends WVApiPlugin implements Handler.Callback {
    public static final String API_SERVER = "WVServer";
    private static final int NOTIFY_RESULT = 500;
    private static final int NOT_REG_LOGIN = 510;
    static boolean NeedApiLock = false;
    private static final String TAG = "WVServer";
    static long lastlocktime = 0;
    static long notiTime = 0;
    private boolean isUserLogin;
    /* access modifiers changed from: private */
    public Object jsContext;
    /* access modifiers changed from: private */
    public final Object lockLock;
    /* access modifiers changed from: private */
    public LinkedBlockingQueue<LockObject> lockQueue;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public String mParams;
    /* access modifiers changed from: private */
    public boolean needLock;
    private ExecutorService singleExecutor;

    public WVServer() {
        this.mHandler = null;
        this.singleExecutor = Executors.newSingleThreadExecutor();
        this.lockQueue = new LinkedBlockingQueue<>();
        this.lockLock = new Object();
        this.jsContext = null;
        this.mParams = null;
        this.needLock = false;
        this.isUserLogin = false;
        this.mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public boolean isLock() {
        return this.needLock;
    }

    public void setLock(boolean lock) {
        this.needLock = lock;
    }

    public boolean execute(String action, String params, WVCallBackContext callback) {
        if (EnvUtil.isAppDebug()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - notiTime > 3600000) {
                notiTime = currentTime;
                if (this.mContext instanceof Activity) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(this.mContext);
                    dlg.setMessage("因安全原因，lib-mtop.js 需升级至1.5.0以上，WVServer接口已废弃，请使用MtopWVPlugin。 详询 ：益零");
                    dlg.setTitle("警告(仅debug版本会弹出)");
                    dlg.setCancelable(true);
                    dlg.setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dlg.create();
                    dlg.show();
                }
            }
        }
        if (!"send".equals(action)) {
            return false;
        }
        if (!NeedApiLock || System.currentTimeMillis() - lastlocktime >= 5000) {
            NeedApiLock = false;
            send(callback, params);
            return true;
        }
        Toast.makeText(this.mContext, "哎呦喂，被挤爆啦，请稍后重试", 1).show();
        return true;
    }

    @WindVaneInterface
    public void send(Object context, String params) {
        this.singleExecutor.execute(new ServerRequestTask(context, params));
    }

    private class ServerRequestTask implements Runnable {
        private Object context;
        private String params;

        public ServerRequestTask(Object context2, String params2) {
            this.context = context2;
            this.params = params2;
        }

        public void run() {
            LockObject thisLock;
            ServerParams paramObj = WVServer.this.parseParams(this.params);
            if (paramObj == null) {
                MtopResult result = new MtopResult(this.context);
                result.addData("ret", new JSONArray().put("HY_PARAM_ERR"));
                WVServer.this.callResult(result);
                return;
            }
            if (WVServer.this.needLock) {
                boolean isRunning = false;
                synchronized (WVServer.this.lockLock) {
                    int size = WVServer.this.lockQueue.size();
                    thisLock = (LockObject) WVServer.this.lockQueue.peek();
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d("WVServer", "queue size: " + size + " lock: " + thisLock);
                    }
                    if (WVServer.this.lockQueue.offer(new LockObject()) && size > 0) {
                        isRunning = true;
                    }
                }
                if (isRunning && thisLock != null) {
                    thisLock.lwait();
                }
            }
            String unused = WVServer.this.mParams = this.params;
            Object unused2 = WVServer.this.jsContext = this.context;
            HttpRequest request = WVServer.this.wrapRequest(paramObj);
            if (request == null) {
                TaoLog.w("WVServer", "HttpRequest is null, and do nothing");
                return;
            }
            WVServer.this.parseResult(this.context, new HttpConnector().syncConnect(request));
        }
    }

    /* access modifiers changed from: private */
    public ServerParams parseParams(String params) {
        boolean z;
        boolean z2;
        boolean z3 = true;
        try {
            ServerParams mParams2 = new ServerParams();
            JSONObject jso = new JSONObject(params);
            mParams2.api = jso.getString("api");
            mParams2.v = jso.optString("v", "*");
            if (jso.optInt("post", 0) != 0) {
                z = true;
            } else {
                z = false;
            }
            mParams2.post = z;
            if (jso.optInt(ApiConstants.ECODE, 0) != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            mParams2.ecode = z2;
            if (jso.optInt("isSec", 1) == 0) {
                z3 = false;
            }
            mParams2.isSec = z3;
            JSONObject dataParam = jso.optJSONObject(CommonData.PARAM);
            if (dataParam == null) {
                return mParams2;
            }
            Iterator it = dataParam.keys();
            while (it.hasNext()) {
                String key = it.next();
                mParams2.addData(key, dataParam.getString(key));
            }
            return mParams2;
        } catch (JSONException e) {
            TaoLog.e("WVServer", "parseParams error, param=" + params);
            return null;
        }
    }

    /* access modifiers changed from: private */
    public HttpRequest wrapRequest(ServerParams sParams) {
        ApiRequest request = new ApiRequest();
        request.addParam("api", sParams.api);
        request.addParam("v", sParams.v);
        if (WindVaneSDKForTB.wvAdapter == null) {
            TaoLog.w("WVServer", "wrapRequest wvAdapter is not exist.");
            if (sParams.ecode) {
                this.mHandler.sendEmptyMessage(NOT_REG_LOGIN);
                return null;
            }
        } else {
            this.isUserLogin = false;
            Map<String, String> logininfo = WindVaneSDKForTB.wvAdapter.getLoginInfo(this.mHandler);
            if (sParams.ecode) {
                if (logininfo == null) {
                    TaoLog.w("WVServer", "wrapRequest loginInfo is null.");
                } else {
                    request.addParam("sid", logininfo.get("sid"));
                    request.addParam(ApiConstants.ECODE, logininfo.get(ApiConstants.ECODE));
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d("WVServer", "login info, sid: " + logininfo.get("sid") + " ecode: " + logininfo.get(ApiConstants.ECODE));
                    }
                }
            } else if (logininfo != null) {
                request.addParam("sid", logininfo.get("sid"));
            }
        }
        request.addDataParams(sParams.getData());
        String url = GlobalConfig.getMtopUrl();
        String postData = null;
        if (sParams.isSec) {
            request.setSec(true);
            postData = WVApiWrapper.formatBody(request, MtopApiAdapter.class);
        } else if (sParams.post) {
            postData = WVApiWrapper.formatBody(request, MtopApiAdapter.class);
        } else {
            url = WVApiWrapper.formatUrl(request, MtopApiAdapter.class);
        }
        HttpRequest req = new HttpRequest(url);
        req.setRedirect(false);
        if (postData == null) {
            return req;
        }
        req.setMethod("POST");
        try {
            req.setPostData(postData.getBytes("utf-8"));
            return req;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return req;
        }
    }

    /* access modifiers changed from: private */
    public void parseResult(Object context, HttpResponse response) {
        MtopResult result = new MtopResult(context);
        result.addData("ret", new JSONArray().put("HY_FAILED"));
        result.addData("code", String.valueOf(response.getHttpCode()));
        if (!response.isSuccess() || response.getData() == null) {
            TaoLog.d("WVServer", "parseResult: request illegal, response is null");
            int responseCode = response.getHttpCode();
            if (responseCode == 420 || responseCode == 499 || responseCode == 599) {
                lastlocktime = System.currentTimeMillis();
                NeedApiLock = true;
                if (this.mHandler != null) {
                    this.mHandler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(WVServer.this.mContext, "哎呦喂，被挤爆啦，请稍后重试", 1).show();
                        }
                    });
                }
            } else if (responseCode >= 410 && responseCode <= 419) {
                Map<String, String> map = response.getHeaders();
                String locationurl = "http://h5.m.taobao.com/";
                if (map != null && map.containsKey("location")) {
                    locationurl = map.get("location");
                }
                Intent intent = new Intent();
                intent.setData(Uri.parse(locationurl));
                intent.setPackage(this.mContext.getPackageName());
                try {
                    this.mContext.startActivity(intent);
                    if (this.mHandler != null) {
                        this.mHandler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(WVServer.this.mContext, " 哎呦喂，被挤爆啦，请稍后重试", 1).show();
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
            callResult(result);
            return;
        }
        try {
            String dataStr = new String(response.getData(), "utf-8");
            try {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d("WVServer", "parseResult: content=" + dataStr);
                }
                try {
                    JSONObject jso = new JSONObject(dataStr);
                    jso.put("code", String.valueOf(response.getHttpCode()));
                    JSONArray jArray = jso.getJSONArray("ret");
                    int size = jArray.length();
                    int i = 0;
                    while (true) {
                        if (i >= size) {
                            break;
                        }
                        String retStr = jArray.getString(i);
                        if (retStr.startsWith("SUCCESS")) {
                            result.setSuccess(true);
                            break;
                        } else if (!retStr.startsWith(ApiResponse.ERR_SID_INVALID)) {
                            i++;
                        } else if (WindVaneSDKForTB.wvAdapter != null) {
                            this.isUserLogin = true;
                            WindVaneSDKForTB.wvAdapter.login(this.mHandler);
                            return;
                        }
                    }
                    result.setData(jso);
                    callResult(result);
                } catch (Exception e2) {
                    TaoLog.e("WVServer", "parseResult mtop response parse fail, content: " + dataStr);
                    callResult(result);
                }
            } catch (UnsupportedEncodingException e3) {
                e = e3;
                String str = dataStr;
                e.printStackTrace();
                callResult(result);
            }
        } catch (UnsupportedEncodingException e4) {
            e = e4;
            e.printStackTrace();
            callResult(result);
        }
    }

    /* access modifiers changed from: private */
    public void callResult(MtopResult result) {
        Message msg = Message.obtain();
        msg.what = 500;
        msg.obj = result;
        this.mHandler.sendMessage(msg);
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                if (this.isUserLogin) {
                    MtopResult result = new MtopResult();
                    result.addData("ret", new JSONArray().put(ApiResponse.ERR_SID_INVALID));
                    if (this.jsContext instanceof WVCallBackContext) {
                        ((WVCallBackContext) this.jsContext).error(result.toString());
                    }
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d("WVServer", "login fail, call result, " + result.toString());
                    }
                    this.isUserLogin = false;
                }
                notifyNext();
                return true;
            case 1:
                notifyNext();
                this.isUserLogin = false;
                this.singleExecutor.execute(new ServerRequestTask(this.jsContext, this.mParams));
                if (!TaoLog.getLogStatus()) {
                    return true;
                }
                TaoLog.d("WVServer", "login success, execute task, mParams:" + this.mParams);
                return true;
            case 500:
                if (msg.obj instanceof MtopResult) {
                    MtopResult result2 = (MtopResult) msg.obj;
                    if (result2.isSuccess()) {
                        if (result2.getJsContext() instanceof WVCallBackContext) {
                            ((WVCallBackContext) result2.getJsContext()).success(result2.toString());
                        }
                    } else if (result2.getJsContext() instanceof WVCallBackContext) {
                        ((WVCallBackContext) result2.getJsContext()).error(result2.toString());
                    }
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d("WVServer", "call result, retString: " + result2.toString());
                    }
                }
                notifyNext();
                return true;
            case NOT_REG_LOGIN /*510*/:
                MtopResult result3 = new MtopResult();
                result3.addData("ret", new JSONArray().put("HY_FAILED"));
                result3.addData("code", "-1");
                if (this.jsContext instanceof WVCallBackContext) {
                    ((WVCallBackContext) this.jsContext).error(result3.toString());
                }
                if (TaoLog.getLogStatus()) {
                    TaoLog.d("WVServer", "not reg login, call fail, " + result3.toString());
                }
                notifyNext();
                return true;
            default:
                return false;
        }
    }

    private void notifyNext() {
        LockObject lock;
        if (this.needLock) {
            synchronized (this.lockLock) {
                lock = this.lockQueue.poll();
            }
            if (lock != null) {
                lock.lnotify();
            }
        }
    }

    public void onDestroy() {
        this.lockQueue.clear();
        this.jsContext = null;
    }

    private class MtopResult {
        private Object jsContext = null;
        private JSONObject result = new JSONObject();
        private boolean success = false;

        public MtopResult() {
        }

        public MtopResult(Object context) {
            this.jsContext = context;
        }

        public void setData(JSONObject data) {
            if (data != null) {
                this.result = data;
            }
        }

        public void addData(String key, String value) {
            if (key != null && value != null) {
                try {
                    this.result.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public void addData(String key, JSONArray value) {
            if (key != null && value != null) {
                try {
                    this.result.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public boolean isSuccess() {
            return this.success;
        }

        public void setSuccess(boolean success2) {
            this.success = success2;
        }

        public Object getJsContext() {
            return this.jsContext;
        }

        public String toString() {
            return this.result.toString();
        }
    }
}
