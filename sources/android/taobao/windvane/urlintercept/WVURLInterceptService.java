package android.taobao.windvane.urlintercept;

import android.taobao.windvane.urlintercept.WVURLInterceptData;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class WVURLInterceptService {
    private static WVABTestUrlHandler mABTestHandler = null;
    private static WVURLIntercepterHandler mHandler = null;
    private static WVURLIntercepterInterface mIntercepter = null;
    private static Map<String, Pattern> rulePat = Collections.synchronizedMap(new HashMap());
    private static Set<WVURLInterceptData.RuleData> urlRules = Collections.synchronizedSet(new HashSet());

    public static Set<WVURLInterceptData.RuleData> getWVURLinterceptRules() {
        return urlRules;
    }

    public static void registerWVURLinterceptRules(Set<WVURLInterceptData.RuleData> urlRules2) {
        Iterator<WVURLInterceptData.RuleData> it = urlRules2.iterator();
        while (it != null && it.hasNext()) {
            WVURLInterceptData.RuleData data = it.next();
            if (data.needdecode) {
                try {
                    data.pattern = URLDecoder.decode(data.pattern, "utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        urlRules = urlRules2;
    }

    public static Map<String, Pattern> getWVURLInterceptRulePats() {
        return rulePat;
    }

    public static void registerWVURLInterceptRulePats(Map<String, Pattern> rulePat2) {
        rulePat = rulePat2;
    }

    public static WVURLIntercepterInterface getWVURLIntercepter() {
        return mIntercepter;
    }

    public static void registerWVURLIntercepter(WVURLIntercepterInterface mIntercepter2) {
        mIntercepter = mIntercepter2;
    }

    public static void resetRulesAndPat() {
        urlRules.clear();
        rulePat.clear();
    }

    public static WVURLIntercepterHandler getWVURLInterceptHandler() {
        return mHandler;
    }

    public static void registerWVURLInterceptHandler(WVURLIntercepterHandler mHandler2) {
        mHandler = mHandler2;
    }

    public static WVABTestUrlHandler getWVABTestHandler() {
        return mABTestHandler;
    }

    public static void registerWVABTestURLHandler(WVABTestUrlHandler mHandler2) {
        mABTestHandler = mHandler2;
    }
}
