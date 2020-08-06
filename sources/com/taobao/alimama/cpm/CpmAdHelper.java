package com.taobao.alimama.cpm;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;
import java.util.Collection;
import java.util.Map;

public final class CpmAdHelper {
    private static CpmAdvertiseBundle a;

    public static Pair<Long, Long> getCachedCpmAdvertiseTimetag(CpmAdvertiseBundle cpmAdvertiseBundle, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (cpmAdvertiseBundle == null || cpmAdvertiseBundle.advertises == null) {
            return null;
        }
        for (CpmAdvertise next : cpmAdvertiseBundle.advertises.values()) {
            if (isCpmAdValid(next, false)) {
                String queryParameter = Uri.parse(next.clickUrl).getQueryParameter("eurl");
                if (!TextUtils.isEmpty(queryParameter) && queryParameter.equals(str)) {
                    return new Pair<>(Long.valueOf(cpmAdvertiseBundle.timeStamp), Long.valueOf(next.cachetime * 1000));
                }
            }
        }
        return null;
    }

    public static Pair<Long, Long> getCachedCpmAdvertiseTimetag(String str) {
        return getCachedCpmAdvertiseTimetag(a, str);
    }

    public static boolean isAdsSameWithLocalCachedData(Map<String, CpmAdvertise> map) {
        if (a == null || map == null) {
            return false;
        }
        if (map.size() != a.advertises.size()) {
            return false;
        }
        for (Map.Entry next : map.entrySet()) {
            if (!((CpmAdvertise) next.getValue()).dataEquals(a.advertises.get(next.getKey()))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isCpmAdValid(CpmAdvertise cpmAdvertise, boolean z) {
        return !TextUtils.isEmpty(cpmAdvertise.clickUrl) && !TextUtils.isEmpty(cpmAdvertise.pid) && (!z || cpmAdvertise.bitmap != null);
    }

    public static boolean isCpmAdsValid(Collection<CpmAdvertise> collection, boolean z) {
        for (CpmAdvertise isCpmAdValid : collection) {
            if (!isCpmAdValid(isCpmAdValid, z)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isIfsUrlInCachedCpmAdvertise(CpmAdvertiseBundle cpmAdvertiseBundle, String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (cpmAdvertiseBundle == null || cpmAdvertiseBundle.advertises == null) {
            return false;
        }
        for (CpmAdvertise next : cpmAdvertiseBundle.advertises.values()) {
            if (isCpmAdValid(next, false) && str.equals(next.ifs)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIfsUrlInCachedCpmAdvertise(String str) {
        return isIfsUrlInCachedCpmAdvertise(a, str);
    }

    public static void recordLocalCacheAdvertises(CpmAdvertiseBundle cpmAdvertiseBundle) {
        if (!cpmAdvertiseBundle.advertises.isEmpty()) {
            a = cpmAdvertiseBundle.clone();
            for (CpmAdvertise cpmAdvertise : a.advertises.values()) {
                cpmAdvertise.bitmap = null;
            }
        }
    }
}
