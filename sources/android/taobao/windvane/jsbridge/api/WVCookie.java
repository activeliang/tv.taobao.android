package android.taobao.windvane.jsbridge.api;

import android.taobao.windvane.WVCookieManager;
import android.taobao.windvane.connect.HttpConnector;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.util.EnvUtil;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import com.alibaba.appmonitor.sample.SampleConfigConstant;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class WVCookie extends WVApiPlugin {
    private static final String TAG = "WVCookie";

    public boolean execute(String action, String params, WVCallBackContext callback) {
        if ("readCookies".equals(action)) {
            readCookies(callback, params);
        } else if ("writeCookies".equals(action)) {
            writeCookies(callback, params);
        } else if ("read".equals(action)) {
            read(callback, params);
        } else if (!"write".equals(action)) {
            return false;
        } else {
            write(callback, params);
        }
        return true;
    }

    public void readCookies(WVCallBackContext callback, String param) {
        WVResult result = new WVResult();
        String url = null;
        if (!TextUtils.isEmpty(param)) {
            try {
                param = URLDecoder.decode(param, "utf-8");
            } catch (Exception e) {
                TaoLog.e("WVCookie", "readCookies: param decode error, param=" + param);
            }
            try {
                url = new JSONObject(param).getString("url");
            } catch (JSONException e2) {
                TaoLog.e("WVCookie", "readCookies: param parse to JSON error, param=" + param);
                result.setResult("HY_PARAM_ERR");
                callback.error(result);
                return;
            }
        }
        String cookieStr = WVCookieManager.getCookie(url);
        if (cookieStr == null) {
            TaoLog.w("WVCookie", "readCookies: cookieStr is null");
            cookieStr = "";
        }
        String cookieData = cookieStr.replace("\"", "\\\\\"");
        String[] split = cookieData.split(SymbolExpUtil.SYMBOL_SEMICOLON);
        result.addData("value", cookieData);
        callback.success(result);
    }

    public void writeCookies(WVCallBackContext callback, String param) {
        String value;
        WVResult result = new WVResult();
        StringBuilder cookiesb = new StringBuilder();
        String domain = null;
        if (!TextUtils.isEmpty(param)) {
            try {
                param = URLDecoder.decode(param, "utf-8");
            } catch (Exception e) {
                TaoLog.e("WVCookie", "writeCookies: param decode error, param=" + param);
            }
            try {
                JSONObject jsObj = new JSONObject(param);
                String name = jsObj.getString("name");
                try {
                    value = URLEncoder.encode(jsObj.getString("value"), "utf-8");
                } catch (UnsupportedEncodingException e2) {
                    TaoLog.e("WVCookie", "writeCookies: URLEncoder.encode error: value=" + value);
                }
                domain = jsObj.getString("domain");
                String expires = jsObj.optString(HttpConnector.EXPIRES);
                String path = jsObj.optString(TuwenConstants.PARAMS.SKU_PATH);
                String secure = jsObj.optString("secure");
                cookiesb.append(name).append("=").append(value);
                cookiesb.append("; ").append("Domain=").append(domain);
                if (!TextUtils.isEmpty(path)) {
                    cookiesb.append("; ").append("Path=").append(path);
                }
                if (!TextUtils.isEmpty(expires)) {
                    cookiesb.append("; ").append("Expires=").append(expires);
                }
                if (!TextUtils.isEmpty(secure)) {
                    cookiesb.append("; ").append("Secure");
                }
            } catch (JSONException e3) {
                TaoLog.e("WVCookie", "writeCookies: param parse to JSON error, param=" + param);
                result.setResult("HY_PARAM_ERR");
                callback.error(result);
                return;
            }
        }
        String cookieStr = cookiesb.toString();
        if (TextUtils.isEmpty(cookieStr) || TextUtils.isEmpty(domain)) {
            if (TaoLog.getLogStatus()) {
                TaoLog.w("WVCookie", "writeCookies: Illegal param: cookieStr=" + cookieStr + ";domain=" + domain);
            }
            callback.error(result);
            return;
        }
        WVCookieManager.setCookie(domain, cookieStr);
        callback.success(result);
    }

    public void read(WVCallBackContext callback, String param) {
        WVResult result = new WVResult();
        String urlString = null;
        if (!TextUtils.isEmpty(param)) {
            try {
                param = URLDecoder.decode(param, "utf-8");
            } catch (Exception e) {
                TaoLog.e("WVCookie", "readCookies: param decode error, param=" + param);
            }
            try {
                urlString = new JSONObject(param).getString("url");
            } catch (JSONException e2) {
                TaoLog.e("WVCookie", "readCookies: param parse to JSON error, param=" + param);
                result.setResult("HY_PARAM_ERR");
                callback.error(result);
                return;
            }
        }
        String cookieStr = WVCookieManager.getCookie(urlString);
        if (cookieStr == null) {
            TaoLog.w("WVCookie", "readCookies: cookieStr is null");
            cookieStr = "";
        }
        String[] cookies = cookieStr.replace("\"", "\\\\\"").split(SymbolExpUtil.SYMBOL_SEMICOLON);
        JSONObject resultValues = new JSONObject();
        JSONObject resultData = new JSONObject();
        for (String cookie : cookies) {
            String[] datas = cookie.split("=");
            if (datas != null && datas.length > 1) {
                try {
                    resultData.put(datas[0].trim(), datas[1].trim());
                } catch (JSONException e3) {
                    e3.printStackTrace();
                }
            }
        }
        try {
            if (!EnvUtil.isAppDebug()) {
                resultValues.put(SampleConfigConstant.VALUES, resultData);
            }
            resultValues.put("value", resultData);
        } catch (JSONException e4) {
            e4.printStackTrace();
        }
        result.addData("value", resultValues);
        callback.success(result);
    }

    public void write(WVCallBackContext callback, String param) {
        WVResult result = new WVResult();
        StringBuilder cookiesb = new StringBuilder();
        String domain = null;
        if (!TextUtils.isEmpty(param)) {
            try {
                param = URLDecoder.decode(param, "utf-8");
            } catch (Exception e) {
                TaoLog.e("WVCookie", "writeCookies: param decode error, param=" + param);
            }
            try {
                JSONObject jsObj = new JSONObject(param);
                Iterator it = jsObj.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    cookiesb.append(key).append("=").append(jsObj.getString(key));
                    if (it.hasNext()) {
                        cookiesb.append("; ");
                    }
                }
                domain = jsObj.getString("domain");
            } catch (JSONException e2) {
                TaoLog.e("WVCookie", "writeCookies: param parse to JSON error, param=" + param);
                result.setResult("HY_PARAM_ERR");
                callback.error(result);
                return;
            }
        }
        String cookieStr = cookiesb.toString();
        if (TextUtils.isEmpty(cookieStr) || TextUtils.isEmpty(domain)) {
            if (TaoLog.getLogStatus()) {
                TaoLog.w("WVCookie", "writeCookies: Illegal param: cookieStr=" + cookieStr + ";domain=" + domain);
            }
            callback.error(result);
            return;
        }
        WVCookieManager.setCookie(domain, cookieStr);
        callback.success(result);
    }
}
