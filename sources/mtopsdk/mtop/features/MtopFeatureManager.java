package mtopsdk.mtop.features;

import android.content.Context;
import java.util.Set;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.intf.Mtop;

public final class MtopFeatureManager {
    private static final String TAG = "mtopsdk.MtopFeatureManager";

    public enum MtopFeatureEnum {
        SUPPORT_RELATIVE_URL(1),
        UNIT_INFO_FEATURE(2),
        DISABLE_WHITEBOX_SIGN(3),
        SUPPORT_UTDID_UNIT(4),
        DISABLE_X_COMMAND(5),
        SUPPORT_OPEN_ACCOUNT(6);
        
        long feature;

        public long getFeature() {
            return this.feature;
        }

        private MtopFeatureEnum(long feature2) {
            this.feature = feature2;
        }
    }

    public static long getMtopTotalFeatures(Mtop mtopInstance) {
        Mtop mtop;
        long totalFeature = 0;
        if (mtopInstance == null) {
            mtop = Mtop.instance((Context) null);
        } else {
            mtop = mtopInstance;
        }
        try {
            for (Integer intValue : mtop.getMtopConfig().mtopFeatures) {
                totalFeature |= getMtopFeatureValue(intValue.intValue());
            }
        } catch (Exception e) {
            TBSdkLog.w(TAG, mtop.getInstanceId() + " [getMtopTotalFeatures] get mtop total features error.---" + e.toString());
        }
        return totalFeature;
    }

    @Deprecated
    private static void setMtopFeatureFlag(MtopFeatureEnum featureEnum, boolean openFlag) {
        if (featureEnum != null) {
            setMtopFeatureFlag((Mtop) null, getMtopFeatureByFeatureEnum(featureEnum), openFlag);
        }
    }

    public static int getMtopFeatureByFeatureEnum(MtopFeatureEnum featureEnum) {
        if (featureEnum == null) {
            return 0;
        }
        switch (featureEnum) {
            case SUPPORT_RELATIVE_URL:
                return 1;
            case UNIT_INFO_FEATURE:
                return 2;
            case DISABLE_WHITEBOX_SIGN:
                return 3;
            case SUPPORT_UTDID_UNIT:
                return 4;
            case DISABLE_X_COMMAND:
                return 5;
            case SUPPORT_OPEN_ACCOUNT:
                return 6;
            default:
                return 0;
        }
    }

    public static void setMtopFeatureFlag(Mtop mtopInstance, int feature, boolean openFlag) {
        Mtop mtop;
        if (mtopInstance == null) {
            mtop = Mtop.instance((Context) null);
        } else {
            mtop = mtopInstance;
        }
        Set<Integer> mtopFeatures = mtop.getMtopConfig().mtopFeatures;
        if (openFlag) {
            mtopFeatures.add(Integer.valueOf(feature));
        } else {
            mtopFeatures.remove(Integer.valueOf(feature));
        }
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtop.getInstanceId() + " [setMtopFeatureFlag] set feature=" + feature + " , openFlag=" + openFlag);
        }
    }

    public static long getMtopFeatureValue(int feature) {
        if (feature < 1) {
            return 0;
        }
        return (long) (1 << (feature - 1));
    }
}
