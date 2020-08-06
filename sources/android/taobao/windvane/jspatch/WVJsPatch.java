package android.taobao.windvane.jspatch;

import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.json.JSONException;
import org.json.JSONObject;

public class WVJsPatch implements WVEventListener {
    private static final String TAG = "WVJsPatch";
    private static WVJsPatch jsPatch = null;
    private Map<String, WVPatchConfig> configRuleMap = new HashMap();
    private Map<String, WVPatchConfig> ruleMap = new HashMap();

    public static synchronized WVJsPatch getInstance() {
        WVJsPatch wVJsPatch;
        synchronized (WVJsPatch.class) {
            if (jsPatch == null) {
                jsPatch = new WVJsPatch();
            }
            wVJsPatch = jsPatch;
        }
        return wVJsPatch;
    }

    private WVJsPatch() {
        WVEventService.getInstance().addEventListener(jsPatch);
    }

    public void addRuleWithPattern(String pattern, String script) {
        WVPatchConfig patchConfig = new WVPatchConfig();
        patchConfig.jsString = script;
        this.ruleMap.put(pattern, patchConfig);
    }

    public void addRuleWithPattern(String key, String pattern, String script) {
        WVPatchConfig patchConfig = new WVPatchConfig();
        patchConfig.jsString = script;
        patchConfig.key = key;
        this.ruleMap.put(pattern, patchConfig);
    }

    public void removeRuleWithKey(String key) {
        if (this.ruleMap == null || this.ruleMap.isEmpty() || key == null) {
            TaoLog.w(TAG, "not need removeRuleWithKey");
            return;
        }
        for (Map.Entry<String, WVPatchConfig> map : this.ruleMap.entrySet()) {
            WVPatchConfig config = map.getValue();
            if (!(config == null || config.key == null || !key.equals(config.key))) {
                String mapKey = map.getKey();
                this.ruleMap.remove(mapKey);
                TaoLog.i(TAG, "removeRuleWithKey : " + mapKey);
            }
        }
    }

    public void removeAllRules() {
        this.ruleMap.clear();
    }

    public void removeAllConfigRules() {
        this.configRuleMap.clear();
    }

    public synchronized void config(String config) {
        removeAllConfigRules();
        if (TextUtils.isEmpty(config)) {
            TaoLog.d(TAG, "no jspatch");
        } else {
            try {
                JSONObject json = new JSONObject(config);
                Iterator<String> keys = json.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = (String) json.get(key);
                    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                        WVPatchConfig patchConfig = new WVPatchConfig();
                        patchConfig.jsString = value;
                        this.configRuleMap.put(key, patchConfig);
                    }
                }
                if (this.ruleMap.isEmpty()) {
                    TaoLog.d(TAG, "jspatch config is Empty");
                } else if (TaoLog.getLogStatus()) {
                    TaoLog.d(TAG, "config success, config: " + config);
                }
            } catch (JSONException e) {
                TaoLog.e(TAG, "get config error, config: " + config);
            }
        }
        return;
    }

    public synchronized void execute(IWVWebView webView, String url) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d(TAG, "start execute jspatch, url: " + url);
        }
        tryJsPatch(this.ruleMap, webView, url);
        tryJsPatch(this.configRuleMap, webView, url);
    }

    private boolean tryJsPatch(Map<String, WVPatchConfig> rulesMap, IWVWebView webView, String url) {
        if (rulesMap == null || rulesMap.isEmpty() || webView == null || TextUtils.isEmpty(url)) {
            TaoLog.d(TAG, "no jspatch need execute");
            return false;
        }
        for (Map.Entry<String, WVPatchConfig> map : rulesMap.entrySet()) {
            String rule = map.getKey();
            WVPatchConfig config = map.getValue();
            if (config == null) {
                TaoLog.w(TAG, "config is null");
            } else {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TAG, "start match rules, rule: " + rule);
                }
                if (config.pattern == null) {
                    try {
                        config.pattern = Pattern.compile(rule);
                    } catch (PatternSyntaxException e) {
                        TaoLog.e(TAG, "compile rule error, pattern: " + rule);
                    }
                }
                if (config.pattern != null && config.pattern.matcher(url).matches()) {
                    if (!config.jsString.startsWith("javascript:")) {
                        config.jsString = "javascript:" + config.jsString;
                    }
                    webView.evaluateJavascript(config.jsString);
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d(TAG, "url matched, start execute jspatch, jsString: " + config.jsString);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public Map<String, WVPatchConfig> getRuleMap() {
        return this.ruleMap;
    }

    public Map<String, WVPatchConfig> getConfigRuleMap() {
        return this.configRuleMap;
    }

    public synchronized void putConfig(String rule, String jsString) {
        if (!TextUtils.isEmpty(rule) && !TextUtils.isEmpty(jsString)) {
            WVPatchConfig config = new WVPatchConfig();
            config.jsString = jsString;
            this.configRuleMap.put(rule, config);
            TaoLog.d(TAG, "putConfig, url: " + rule + " js: " + config.jsString);
        }
    }

    public WVEventResult onEvent(int id, WVEventContext ctx, Object... obj) {
        if (id == 1002) {
            execute(ctx.webView, ctx.url);
        }
        return new WVEventResult(false);
    }
}
