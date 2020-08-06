package mtopsdk.mtop.global;

import android.content.Context;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.xstate.XState;
import mtopsdk.xstate.util.XStateConstants;

public class SDKUtils {
    private static final String TAG = "mtopsdk.SDKUtils";

    public static long getCorrectionTime() {
        return getTimeOffset() + (System.currentTimeMillis() / 1000);
    }

    public static long getCorrectionTimeMillis() {
        return getCorrectionTime() * 1000;
    }

    public static long getTimeOffset() {
        String t_offset = XState.getTimeOffset();
        if (StringUtils.isNotBlank(t_offset)) {
            try {
                return Long.parseLong(t_offset);
            } catch (NumberFormatException e) {
                TBSdkLog.e(TAG, "[getTimeOffset]parse t_offset failed");
                return 0;
            }
        } else {
            XState.setValue(XStateConstants.KEY_TIME_OFFSET, "0");
            return 0;
        }
    }

    @Deprecated
    public static void registerSessionInfo(String sid, String userId) {
        Mtop.instance(Mtop.Id.INNER, (Context) null).registerSessionInfo(sid, userId);
    }

    @Deprecated
    public static void registerSessionInfo(String sid, String ecode, String userId) {
        Mtop.instance(Mtop.Id.INNER, (Context) null).registerSessionInfo(sid, userId);
    }

    @Deprecated
    public static void logOut() {
        Mtop.instance(Mtop.Id.INNER, (Context) null).logout();
    }

    @Deprecated
    public static void registerTtid(String ttid) {
        Mtop.instance(Mtop.Id.INNER, (Context) null).registerTtid(ttid);
    }

    @Deprecated
    public static void registerMtopSdkProperty(String key, String value) {
        Mtop.instance(Mtop.Id.INNER, (Context) null).getMtopConfig().registerMtopSdkProperty(key, value);
    }

    @Deprecated
    public static boolean removeCacheBlock(String blockName) {
        return Mtop.instance(Mtop.Id.INNER, (Context) null).removeCacheBlock(blockName);
    }

    @Deprecated
    public static boolean unintallCacheBlock(String blockName) {
        return Mtop.instance(Mtop.Id.INNER, (Context) null).unintallCacheBlock(blockName);
    }

    @Deprecated
    public static boolean removeCacheItem(String blockName, String cacheKey) {
        return Mtop.instance(Mtop.Id.INNER, (Context) null).removeCacheItem(blockName, cacheKey);
    }
}
