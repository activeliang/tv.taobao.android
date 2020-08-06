package anet.channel.strategy.dispatch;

import android.os.Build;
import android.text.TextUtils;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.utils.Utils;
import anet.channel.util.ALog;
import com.bftv.fui.constantplugin.Constant;
import java.util.Map;
import java.util.Set;

class DispatchParamBuilder {
    public static final String TAG = "amdc.DispatchParamBuilder";

    DispatchParamBuilder() {
    }

    public static Map buildParamMap(Map<String, Object> map) {
        IAmdcSign iAmdcSign = AmdcRuntimeInfo.getSign();
        if (iAmdcSign == null || TextUtils.isEmpty(iAmdcSign.getAppkey())) {
            return null;
        }
        map.put("appkey", iAmdcSign.getAppkey());
        map.put("v", DispatchConstants.VER_CODE);
        map.put("platform", DispatchConstants.ANDROID);
        map.put(DispatchConstants.PLATFORM_VERSION, Build.VERSION.RELEASE);
        if (!TextUtils.isEmpty(GlobalAppRuntimeInfo.getUserId())) {
            map.put("sid", GlobalAppRuntimeInfo.getUserId());
        }
        if (!TextUtils.isEmpty(GlobalAppRuntimeInfo.getUtdid())) {
            map.put("deviceId", GlobalAppRuntimeInfo.getUtdid());
        }
        NetworkStatusHelper.NetworkStatus networkStatus = NetworkStatusHelper.getStatus();
        map.put("netType", networkStatus.toString());
        if (networkStatus.isWifi()) {
            map.put(DispatchConstants.BSSID, NetworkStatusHelper.getWifiBSSID());
        }
        map.put(DispatchConstants.CARRIER, NetworkStatusHelper.getCarrier());
        map.put(DispatchConstants.MNC, NetworkStatusHelper.getSimOp());
        map.put("lat", String.valueOf(AmdcRuntimeInfo.latitude));
        map.put("lng", String.valueOf(AmdcRuntimeInfo.longitude));
        fillTtidInfo(map);
        map.put("domain", formatDomains(map));
        map.put(DispatchConstants.SIGNTYPE, iAmdcSign.useSecurityGuard() ? "sec" : "noSec");
        map.put("t", String.valueOf(System.currentTimeMillis()));
        String sign = getSign(iAmdcSign, map);
        if (TextUtils.isEmpty(sign)) {
            return null;
        }
        map.put("sign", sign);
        return map;
    }

    private static String formatDomains(Map map) {
        StringBuilder sb = new StringBuilder();
        for (String domain : (Set) map.remove(DispatchConstants.HOSTS)) {
            sb.append(domain).append(' ');
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private static void fillTtidInfo(Map<String, Object> map) {
        try {
            String ttid = GlobalAppRuntimeInfo.getTtid();
            if (!TextUtils.isEmpty(ttid)) {
                int pos = ttid.indexOf(Constant.NLP_CACHE_TYPE);
                if (pos != -1) {
                    map.put("channel", ttid.substring(0, pos));
                }
                String tmp = ttid.substring(pos + 1);
                int pos2 = tmp.lastIndexOf("_");
                if (pos2 != -1) {
                    map.put("appName", tmp.substring(0, pos2));
                    map.put("appVersion", tmp.substring(pos2 + 1));
                    return;
                }
                map.put("appName", tmp);
            }
        } catch (Exception e) {
        }
    }

    static String getSign(IAmdcSign iSign, Map<String, String> args) {
        StringBuilder sb = new StringBuilder(128);
        sb.append(Utils.stringNull2Empty(args.get("appkey"))).append("&").append(Utils.stringNull2Empty(args.get("domain"))).append("&").append(Utils.stringNull2Empty(args.get("appName"))).append("&").append(Utils.stringNull2Empty(args.get("appVersion"))).append("&").append(Utils.stringNull2Empty(args.get(DispatchConstants.BSSID))).append("&").append(Utils.stringNull2Empty(args.get("channel"))).append("&").append(Utils.stringNull2Empty(args.get("deviceId"))).append("&").append(Utils.stringNull2Empty(args.get("lat"))).append("&").append(Utils.stringNull2Empty(args.get("lng"))).append("&").append(Utils.stringNull2Empty(args.get(DispatchConstants.MACHINE))).append("&").append(Utils.stringNull2Empty(args.get("netType"))).append("&").append(Utils.stringNull2Empty(args.get(DispatchConstants.OTHER))).append("&").append(Utils.stringNull2Empty(args.get("platform"))).append("&").append(Utils.stringNull2Empty(args.get(DispatchConstants.PLATFORM_VERSION))).append("&").append(Utils.stringNull2Empty(args.get(DispatchConstants.PRE_IP))).append("&").append(Utils.stringNull2Empty(args.get("sid"))).append("&").append(Utils.stringNull2Empty(args.get("t"))).append("&").append(Utils.stringNull2Empty(args.get("v"))).append("&").append(Utils.stringNull2Empty(args.get(DispatchConstants.SIGNTYPE)));
        try {
            return iSign.sign(sb.toString());
        } catch (Exception e) {
            ALog.e(TAG, "get sign failed", (String) null, e, new Object[0]);
            return null;
        }
    }
}
