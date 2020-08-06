package com.alibaba.motu.crashreporter.utrestapi;

import android.content.Context;
import com.alibaba.motu.crashreporter.LogUtil;
import com.alibaba.motu.crashreporter.utils.MD5Utils;
import com.alibaba.motu.crashreporter.utils.StringUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

public class UTRestUrlWrapper {
    public static final String FIELD_APPKEY = "appkey";
    public static final String FIELD_APPVERSION = "app_version";
    public static final String FIELD_CHANNEL = "channel";
    public static final String FIELD_PLATFORM = "platform";
    public static final String FIELD_SDK_VERSION = "sdk_version";
    public static final String FIELD_T = "t";
    public static final String FIELD_UTDID = "utdid";
    public static final String FIELD_V = "v";
    static boolean enableSecuritySDK = false;
    static Context mContext;

    public static void enableSecuritySDK() {
        enableSecuritySDK = true;
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static String getSignedTransferUrl(String pUrl, Map<String, Object> map, Map<String, Object> pDataMap, Context aContext, String aAppkey, String aChannel, String aAppVersion, String aPlatform, String aSdkVersion, String aUtdid) throws Exception {
        String lSignDataStr = "";
        if (pDataMap != null && pDataMap.size() > 0) {
            Set<String> keys = pDataMap.keySet();
            String[] lKeysArr = new String[keys.size()];
            keys.toArray(lKeysArr);
            for (String key : UTKeyArraySorter.getInstance().sortResourcesList(lKeysArr, true)) {
                lSignDataStr = lSignDataStr + key + MD5Utils.getMd5Hex((byte[]) pDataMap.get(key));
            }
        }
        try {
            return _wrapUrl(pUrl, (String) null, (String) null, lSignDataStr, aContext, aAppkey, aChannel, aAppVersion, aPlatform, aSdkVersion, aUtdid);
        } catch (Exception e) {
            return _wrapUrl(UTConstants.getTransferUrl(), (String) null, (String) null, lSignDataStr, aContext, aAppkey, aChannel, aAppVersion, aPlatform, aSdkVersion, aUtdid);
        }
    }

    private static String _wrapUrl(String url, String pUrlQueryStr, String pSignQueryStr, String pSignDataStr, Context aContext, String aAppkey, String aChannel, String aAppVersion, String aPlatform, String aSdkVersion, String aUtdid) throws Exception {
        Context context = aContext;
        String lAppkey = aAppkey;
        String lChannel = aChannel;
        String lAppVersion = aAppVersion;
        String lPlatform = aPlatform;
        String lUtdid = aUtdid;
        String t = String.valueOf(System.currentTimeMillis());
        String lisSecureflag = "";
        String lSignValue = "";
        if (enableSecuritySDK && mContext != null) {
            try {
                StringBuilder append = new StringBuilder().append(lAppkey).append(lChannel).append(lAppVersion).append(lPlatform).append(UTConstants.G_SDK_VERSION).append(lUtdid).append(t).append("3.0").append(lisSecureflag);
                if (pSignQueryStr == null) {
                    pSignQueryStr = "";
                }
                StringBuilder append2 = append.append(pSignQueryStr);
                if (pSignDataStr == null) {
                    pSignDataStr = "";
                }
                lSignValue = new UTSecuritySDKRequestAuthentication(mContext, aAppkey).getSign(MD5Utils.getMd5Hex(append2.append(pSignDataStr).toString().getBytes()));
                if (StringUtils.isNotBlank(UTConstants.G_SDK_VERSION)) {
                    lisSecureflag = "1";
                }
            } catch (Exception e) {
                LogUtil.w("security sdk signed", e);
            }
        }
        String lUrlQueryStr = "";
        if (!StringUtils.isEmpty(pUrlQueryStr)) {
            lUrlQueryStr = pUrlQueryStr + "&";
        }
        return String.format("%s?%sak=%s&av=%s&c=%s&v=%s&s=%s&d=%s&sv=%s&p=%s&t=%s&u=%s&is=%s", new Object[]{url, lUrlQueryStr, _getEncoded(lAppkey), _getEncoded(lAppVersion), _getEncoded(lChannel), _getEncoded("3.0"), _getEncoded(lSignValue), _getEncoded(lUtdid), UTConstants.G_SDK_VERSION, lPlatform, t, "", lisSecureflag});
    }

    private static String _getEncoded(String aValue) {
        if (aValue == null) {
            return "";
        }
        try {
            return URLEncoder.encode(aValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return aValue;
        }
    }
}
