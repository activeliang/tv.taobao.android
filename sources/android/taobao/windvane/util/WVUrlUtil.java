package android.taobao.windvane.util;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.text.TextUtils;
import com.bftv.fui.constantplugin.Constant;
import java.util.HashMap;
import java.util.Map;

@SuppressLint({"DefaultLocale"})
public class WVUrlUtil {
    private static Map<String, String> mimeTypes = new HashMap();

    static {
        for (MimeTypeEnum type : MimeTypeEnum.values()) {
            mimeTypes.put(type.getSuffix(), type.getMimeType());
        }
    }

    public static boolean isRes(String url) {
        String suffix = getSuffix(url);
        return MimeTypeEnum.JS.getSuffix().equals(suffix) || MimeTypeEnum.CSS.getSuffix().equals(suffix);
    }

    public static boolean shouldTryABTest(String url) {
        Uri uri = Uri.parse(url);
        if (!uri.isHierarchical()) {
            return false;
        }
        String urlPath = uri.getPath();
        if (urlPath.endsWith(".htm") || urlPath.endsWith(".html") || urlPath.endsWith(".js")) {
            return true;
        }
        return false;
    }

    public static boolean isImg(String url) {
        return getMimeType(url).startsWith("image");
    }

    public static boolean isHtml(String url) {
        String path;
        if (TextUtils.isEmpty(url) || url.contains("??") || (path = Uri.parse(url).getPath()) == null) {
            return false;
        }
        if (path.endsWith("." + MimeTypeEnum.HTM.getSuffix()) || path.endsWith("." + MimeTypeEnum.HTML.getSuffix()) || TextUtils.isEmpty(path) || WVNativeCallbackUtil.SEPERATER.equals(path)) {
            return true;
        }
        return false;
    }

    public static String addParam(String url, String key, String value) {
        if (url == null || TextUtils.isEmpty(key)) {
            return url;
        }
        Uri uri = Uri.parse(url);
        if (uri.getQueryParameter(key) != null) {
            return url;
        }
        Uri.Builder builder = uri.buildUpon();
        builder.appendQueryParameter(key, value);
        return builder.toString();
    }

    public static Map<String, String> getParamMap(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        int indexSep = url.indexOf(WVUtils.URL_DATA_CHAR);
        if (indexSep == -1) {
            return map;
        }
        String qStr = url.substring(indexSep + 1);
        if (qStr.contains(Constant.INTENT_JSON_MARK)) {
            qStr = qStr.substring(0, qStr.indexOf(Constant.INTENT_JSON_MARK));
        }
        for (String p : qStr.split("&")) {
            String[] sp = p.split("=");
            if (sp.length < 2) {
                map.put(sp[0], "");
            } else {
                map.put(sp[0], sp[1]);
            }
        }
        return map;
    }

    public static String getParamVal(String url, String name) {
        if (url == null || name == null) {
            return null;
        }
        return Uri.parse(url).getQueryParameter(name);
    }

    public static String getSuffix(String url) {
        String path;
        int start;
        if (TextUtils.isEmpty(url) || (path = Uri.parse(url).getPath()) == null || (start = path.lastIndexOf(".")) == -1) {
            return "";
        }
        return path.substring(start + 1);
    }

    public static String getMimeType(String url) {
        String mimeType = mimeTypes.get(getSuffix(url));
        if (mimeType == null) {
            return "";
        }
        return mimeType;
    }

    public static String getMimeTypeExtra(String url) {
        if (url.contains("??")) {
            url = url.replaceFirst("\\?\\?", "");
        }
        return getMimeType(url);
    }

    public static boolean isCommonUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://")) {
            return true;
        }
        return false;
    }

    public static String removeQueryParam(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        int index = getQureyIndex(url);
        if (index != -1) {
            return url.substring(0, index);
        }
        if (url.indexOf(Constant.INTENT_JSON_MARK) > 0) {
            return url.substring(0, url.indexOf(Constant.INTENT_JSON_MARK));
        }
        return url;
    }

    public static String rebuildWVurl(String oldurl, String newurl) {
        StringBuilder newsb = new StringBuilder(newurl);
        char lastChar = getLastChar(newurl);
        if (!('?' == lastChar || '&' == lastChar)) {
            if (newurl.contains(WVUtils.URL_DATA_CHAR)) {
                newsb.append("&");
            } else {
                newsb.append(WVUtils.URL_DATA_CHAR);
            }
        }
        if ('?' != getLastChar(oldurl) && oldurl.contains(WVUtils.URL_DATA_CHAR)) {
            String[] str = oldurl.split("\\?");
            for (int i = 1; i < str.length; i++) {
                if (str[i] != null) {
                    newsb.append(str[i]);
                }
                if (i != str.length - 1) {
                    newsb.append(WVUtils.URL_DATA_CHAR);
                }
            }
        }
        return newsb.toString();
    }

    private static char getLastChar(String str) {
        return str.charAt(str.length() - 1);
    }

    public static int getQureyIndex(String url) {
        int index = 0;
        int length = url.length();
        while (true) {
            int index2 = url.indexOf(WVUtils.URL_DATA_CHAR, index);
            if (index2 == -1) {
                return -1;
            }
            int nextCharIndex = index2 + 1;
            if (nextCharIndex >= length || url.charAt(nextCharIndex) != '?') {
                return index2;
            }
            index = index2 + 2;
        }
    }

    public static String[] parseCombo(String url) {
        String url2 = removeQueryParam(url);
        int comboIndex = url2.indexOf("??");
        if (-1 == comboIndex) {
            return null;
        }
        return url2.substring(comboIndex + 2).split("\\,");
    }

    public static String removeScheme(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        String temp = null;
        if (url.startsWith("http:")) {
            temp = url.replace("http:", "");
        }
        if (url.startsWith("https:")) {
            temp = url.replace("https:", "");
        }
        if (TextUtils.isEmpty(temp)) {
            temp = url;
        }
        return temp;
    }

    public static String removeHashCode(String url) {
        String temp = url;
        if (TextUtils.isEmpty(temp)) {
            return temp;
        }
        if (temp.indexOf(Constant.INTENT_JSON_MARK) != -1) {
            temp = temp.substring(0, temp.indexOf(Constant.INTENT_JSON_MARK));
        }
        return temp;
    }
}
