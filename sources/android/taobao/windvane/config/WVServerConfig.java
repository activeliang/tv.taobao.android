package android.taobao.windvane.config;

import android.taobao.windvane.connect.api.ApiResponse;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVConstants;
import android.text.TextUtils;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.json.JSONObject;

public class WVServerConfig {
    public static boolean CACHE = true;
    public static String DOMAIN_PATTERN = WVConstants.DEFAULT_DOMAIN_PATTERN;
    public static String FORBIDDEN_DOMAIN_PATTERN = "";
    public static boolean LOG = false;
    public static boolean STATISTICS = false;
    public static String SUPPORT_DOWNLOAD_DOMAIN_PATTERN = "";
    public static String THIRD_PARTY_DOMAIN_PATTERN = WVConstants.THIRD_PARTY_DOMAIN_PATTERN;
    public static boolean URL_FILTER = true;
    public static Pattern domainPat = null;
    public static Pattern forbiddenDomain = null;
    public static Pattern supportDownloadDomain = null;
    public static Pattern thirdPartyDomain = null;
    public static String v = "0";

    static {
        try {
            updateGlobalConfig(ConfigStorage.getStringVal("WVURLCacheDefault", ConfigStorage.KEY_DATA));
        } catch (Exception e) {
        }
    }

    public static boolean isTrustedUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (domainPat == null) {
            if (TextUtils.isEmpty(DOMAIN_PATTERN)) {
                DOMAIN_PATTERN = WVConstants.DEFAULT_DOMAIN_PATTERN;
            }
            try {
                domainPat = Pattern.compile(DOMAIN_PATTERN, 2);
                TaoLog.d("WVServerConfig", "compile pattern domainPat rule, " + DOMAIN_PATTERN);
            } catch (PatternSyntaxException e) {
                TaoLog.e("WVServerConfig", " PatternSyntaxException pattern:" + e.getMessage());
            }
        }
        try {
            if (domainPat != null) {
                return domainPat.matcher(url).matches();
            }
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    public static boolean isThirdPartyUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (thirdPartyDomain == null) {
            if (TextUtils.isEmpty(THIRD_PARTY_DOMAIN_PATTERN)) {
                THIRD_PARTY_DOMAIN_PATTERN = WVConstants.THIRD_PARTY_DOMAIN_PATTERN;
            }
            try {
                thirdPartyDomain = Pattern.compile(THIRD_PARTY_DOMAIN_PATTERN, 2);
                TaoLog.d("WVServerConfig", "compile pattern thirdPartyDomain rule, " + THIRD_PARTY_DOMAIN_PATTERN);
            } catch (PatternSyntaxException e) {
                TaoLog.e("WVServerConfig", " PatternSyntaxException pattern:" + e.getMessage());
            }
        }
        try {
            if (thirdPartyDomain != null) {
                return thirdPartyDomain.matcher(url).matches();
            }
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    public static boolean isBlackUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (forbiddenDomain == null) {
            if (TextUtils.isEmpty(FORBIDDEN_DOMAIN_PATTERN)) {
                FORBIDDEN_DOMAIN_PATTERN = "";
            }
            try {
                forbiddenDomain = Pattern.compile(FORBIDDEN_DOMAIN_PATTERN, 2);
                TaoLog.d("WVServerConfig", "compile pattern black rule, " + FORBIDDEN_DOMAIN_PATTERN);
            } catch (PatternSyntaxException e) {
                TaoLog.e("WVServerConfig", " PatternSyntaxException pattern:" + e.getMessage());
            }
        }
        try {
            if (!TextUtils.isEmpty(url)) {
                return forbiddenDomain.matcher(url).matches();
            }
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    public static boolean isSupportDownload(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (supportDownloadDomain == null) {
            if (TextUtils.isEmpty(SUPPORT_DOWNLOAD_DOMAIN_PATTERN)) {
                SUPPORT_DOWNLOAD_DOMAIN_PATTERN = "";
            }
            try {
                supportDownloadDomain = Pattern.compile(SUPPORT_DOWNLOAD_DOMAIN_PATTERN, 2);
                TaoLog.d("WVServerConfig", "compile pattern supportDownloadDomain rule, " + SUPPORT_DOWNLOAD_DOMAIN_PATTERN);
            } catch (PatternSyntaxException e) {
                TaoLog.e("WVServerConfig", " PatternSyntaxException pattern:" + e.getMessage());
            }
        }
        try {
            if (!TextUtils.isEmpty(url)) {
                return supportDownloadDomain.matcher(url).matches();
            }
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    public static boolean updateGlobalConfig(String content) {
        boolean z;
        boolean z2 = false;
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        JSONObject jsObj = null;
        ApiResponse response = new ApiResponse();
        if (response.parseJsonResult(content).success) {
            jsObj = response.data;
        }
        if (jsObj == null) {
            return false;
        }
        CACHE = jsObj.optInt("lock", 0) == 0;
        if (jsObj.optInt("log") == 1) {
            z = true;
        } else {
            z = false;
        }
        LOG = z;
        if (jsObj.optInt("statistics") == 1) {
            z2 = true;
        }
        STATISTICS = z2;
        DOMAIN_PATTERN = jsObj.optString("alidomain");
        domainPat = null;
        return true;
    }
}
