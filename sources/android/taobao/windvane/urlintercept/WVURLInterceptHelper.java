package android.taobao.windvane.urlintercept;

import android.taobao.windvane.config.WVServerConfig;
import android.taobao.windvane.connect.api.ApiResponse;
import android.taobao.windvane.urlintercept.WVURLInterceptData;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONArray;
import org.json.JSONObject;

public class WVURLInterceptHelper {
    private static String TAG = WVURLInterceptHelper.class.getSimpleName();
    private static final String URL_FILTER_TAG = "_wv_url_hyid";

    public static synchronized WVURLInterceptData.URLInfo parseByRule(String url, Set<WVURLInterceptData.RuleData> urlRules, Map<String, Pattern> rulePat) {
        WVURLInterceptData.URLInfo info;
        synchronized (WVURLInterceptHelper.class) {
            info = new WVURLInterceptData.URLInfo();
            info.url = url;
            Map<String, String> paramsMap = new Hashtable<>();
            Iterator i$ = urlRules.iterator();
            while (true) {
                if (!i$.hasNext()) {
                    break;
                }
                WVURLInterceptData.RuleData data = i$.next();
                String patStr = data.pattern;
                Pattern pat = rulePat.get(patStr);
                if (pat == null) {
                    try {
                        pat = Pattern.compile(patStr);
                        rulePat.put(patStr, pat);
                    } catch (PatternSyntaxException e) {
                        TaoLog.e(TAG, "pattern:" + patStr);
                    }
                }
                if (pat == null) {
                    break;
                }
                Matcher matcher = pat.matcher(url);
                if (matcher.matches()) {
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d(TAG, "url matched for pattern " + patStr);
                    }
                    info.code = data.target;
                    info.rule = patStr;
                    int rtype = data.rutype;
                    if (rtype == 0) {
                        int count = matcher.groupCount();
                        for (Map.Entry<String, Integer> indexp : data.indexp.entrySet()) {
                            int index = indexp.getValue().intValue();
                            String val = matcher.group(index);
                            if (count >= index && !TextUtils.isEmpty(val)) {
                                paramsMap.put(indexp.getKey(), val);
                            }
                        }
                    } else if (rtype == 1) {
                        for (Map.Entry<String, String> namep : data.namep.entrySet()) {
                            String val2 = WVUrlUtil.getParamVal(url, namep.getKey());
                            if (!TextUtils.isEmpty(val2)) {
                                paramsMap.put(namep.getValue(), val2);
                            }
                        }
                    }
                }
            }
            if (paramsMap != null) {
                info.params = paramsMap;
            }
        }
        return info;
    }

    public static List<WVURLInterceptData.RuleData> parseRuleData(String content) {
        List<WVURLInterceptData.RuleData> ruleList = Collections.synchronizedList(new ArrayList());
        JSONObject jsObj = null;
        ApiResponse response = new ApiResponse();
        if (response.parseJsonResult(content).success) {
            jsObj = response.data;
        }
        if (jsObj == null) {
            return ruleList;
        }
        try {
            List<WVURLInterceptData.RuleData> ruleList2 = new ArrayList<>();
            try {
                WVServerConfig.URL_FILTER = jsObj.optInt("lock", 0) == 0;
                if (jsObj.has("rules")) {
                    JSONArray rules = jsObj.getJSONArray("rules");
                    for (int i = 0; i < rules.length(); i++) {
                        JSONObject obj = (JSONObject) rules.get(i);
                        WVURLInterceptData.RuleData data = new WVURLInterceptData.RuleData();
                        data.target = obj.getInt("target");
                        data.pattern = obj.getString("pattern");
                        data.rutype = obj.optInt("rutype");
                        for (String p : obj.optString("indexp").split(",")) {
                            String[] sp = p.split(SymbolExpUtil.SYMBOL_COLON);
                            if (sp.length == 2 && TextUtils.isDigitsOnly(sp[1].trim())) {
                                data.indexp.put(sp[0].trim(), Integer.valueOf(Integer.parseInt(sp[1].trim())));
                            }
                        }
                        for (String p2 : obj.optString("namep").split(",")) {
                            String[] sp2 = p2.split(SymbolExpUtil.SYMBOL_COLON);
                            if (sp2.length == 2) {
                                data.namep.put(sp2[1].trim(), sp2[0].trim());
                            }
                        }
                        ruleList2.add(data);
                    }
                }
                return ruleList2;
            } catch (Exception e) {
                return ruleList2;
            }
        } catch (Exception e2) {
            return ruleList;
        }
    }

    public static WVURLInterceptData.URLInfo parseByTag(String url) {
        String tagVal = WVUrlUtil.getParamVal(url, URL_FILTER_TAG);
        if (TextUtils.isEmpty(tagVal)) {
            return null;
        }
        WVURLInterceptData.URLInfo info = new WVURLInterceptData.URLInfo();
        info.url = url;
        if (!tagVal.contains(SymbolExpUtil.SYMBOL_SEMICOLON)) {
            return null;
        }
        int s1 = tagVal.indexOf(SymbolExpUtil.SYMBOL_SEMICOLON);
        info.code = Integer.parseInt(TextUtils.substring(tagVal, 0, s1));
        Map<String, String> paramsMap = new HashMap<>();
        for (String p : TextUtils.substring(tagVal, s1 + 1, tagVal.length()).split(",")) {
            String[] sp = p.split(SymbolExpUtil.SYMBOL_COLON);
            if (sp.length == 2) {
                String val = WVUrlUtil.getParamVal(url, sp[1].trim());
                if (!TextUtils.isEmpty(val)) {
                    paramsMap.put(sp[0].trim(), val);
                }
            }
        }
        if (paramsMap == null) {
            return info;
        }
        info.params = paramsMap;
        return info;
    }
}
