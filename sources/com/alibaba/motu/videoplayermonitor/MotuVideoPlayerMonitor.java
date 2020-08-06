package com.alibaba.motu.videoplayermonitor;

import com.alibaba.analytics.utils.Logger;
import com.alibaba.motu.videoplayermonitor.errorStatistics.MotuRequestErrInfo;
import com.alibaba.motu.videoplayermonitor.errorStatistics.MotuRequestErrStatisticsInfo;
import com.alibaba.motu.videoplayermonitor.errorStatistics.MotuVideoPlayErrInfo;
import com.alibaba.motu.videoplayermonitor.errorStatistics.MotuVideoPlayErrStatisticsInfo;
import com.alibaba.motu.videoplayermonitor.errorStatistics.MotuVideoVIPErrInfo;
import com.alibaba.motu.videoplayermonitor.errorStatistics.MotuVideoVIPErrStatisticsInfo;
import com.alibaba.motu.videoplayermonitor.fluentStatistics.FluentInfo;
import com.alibaba.motu.videoplayermonitor.fluentStatistics.FluentStatisticsInfo;
import com.alibaba.motu.videoplayermonitor.impairmentStatistics.ImpairmentStatisticsInfo;
import com.alibaba.motu.videoplayermonitor.model.MotuMediaBase;
import com.alibaba.motu.videoplayermonitor.smoothSwitchStatistics.SmoothSwitchStatisticsInfo;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;

public class MotuVideoPlayerMonitor {
    public static void commitVideoVIPErrInfoStatistics(MotuVideoVIPErrInfo info, MotuVideoVIPErrStatisticsInfo statisticsInfo) {
        if (info == null) {
            Logger.d("VPM", "videoErrInfo is null");
            return;
        }
        DimensionSet ds = DimensionSet.create();
        ds.addDimension(VPMConstants.DIMENSION_MEDIATYPE);
        ds.addDimension(VPMConstants.DIMENSION_VIDEOFORMAT);
        ds.addDimension(VPMConstants.DIMENSION_SOURCEIDENTYTY);
        ds.addDimension(VPMConstants.DIMENSION_PLAYERCORE);
        ds.addDimension(VPMConstants.DIMENSION_ISSUCCESS);
        ds.addDimension(VPMConstants.DIMENSION_VIPERRORCODE);
        ds.addDimension(VPMConstants.DIMENSION_VIPERRORMSG);
        if (info.extInfoData != null && info.extInfoData.size() > 0) {
            for (String key : info.extInfoData.keySet()) {
                ds.addDimension(key);
            }
        }
        MeasureSet ms = MeasureSet.create(new String[0]);
        if (statisticsInfo.extStatisticsData != null && statisticsInfo.extStatisticsData.size() > 0) {
            for (String key2 : statisticsInfo.extStatisticsData.keySet()) {
                ms.addMeasure(key2);
            }
        }
        AppMonitor.register(VPMConstants.VPM, VPMConstants.MONITORPOINTER_VIP_ERROR, ms, ds, true);
        DimensionValueSet dvs = DimensionValueSet.create();
        dvs.setMap(info.toMap());
        AppMonitor.Stat.commit(VPMConstants.VPM, VPMConstants.MONITORPOINTER_VIP_ERROR, dvs, MeasureValueSet.create(statisticsInfo.toMap()));
    }

    @Deprecated
    public static void commitVideoVIPErrInfoStatistics(MotuVideoVIPErrInfo info) {
        commitVideoVIPErrInfoStatistics(info, new MotuVideoVIPErrStatisticsInfo());
    }

    public static void commitRequestErrInfoStatistics(MotuRequestErrInfo info, MotuRequestErrStatisticsInfo statisticsInfo) {
        if (info == null || statisticsInfo == null) {
            Logger.d("VPM", "requestErrInfo is null");
            return;
        }
        DimensionSet ds = DimensionSet.create();
        ds.addDimension(VPMConstants.DIMENSION_MEDIATYPE);
        ds.addDimension(VPMConstants.DIMENSION_VIDEOFORMAT);
        ds.addDimension(VPMConstants.DIMENSION_SOURCEIDENTYTY);
        ds.addDimension(VPMConstants.DIMENSION_PLAYERCORE);
        ds.addDimension(VPMConstants.DIMENSION_ISSUCCESS);
        ds.addDimension(VPMConstants.DIMENSION_REQUESTERRCODE);
        ds.addDimension(VPMConstants.DIMENSION_REQUESTERRMSG);
        ds.addDimension(VPMConstants.DIMENSION_VIDEOPLAYTYPE);
        ds.addDimension(VPMConstants.DIMENSION_CDNIP);
        ds.addDimension(VPMConstants.DIMENSION_PLAYWAY);
        if (info.extInfoData != null && info.extInfoData.size() > 0) {
            for (String key : info.extInfoData.keySet()) {
                ds.addDimension(key);
            }
        }
        MeasureSet ms = MeasureSet.create(new String[0]);
        if (statisticsInfo.extStatisticsData != null && statisticsInfo.extStatisticsData.size() > 0) {
            for (String key2 : statisticsInfo.extStatisticsData.keySet()) {
                ms.addMeasure(key2);
            }
        }
        AppMonitor.register(VPMConstants.VPM, VPMConstants.MONITORPOINTER_REQUEST_SERVICE, ms, ds, true);
        DimensionValueSet dvs = DimensionValueSet.create();
        dvs.setMap(info.toMap());
        AppMonitor.Stat.commit(VPMConstants.VPM, VPMConstants.MONITORPOINTER_REQUEST_SERVICE, dvs, MeasureValueSet.create(statisticsInfo.toMap()));
    }

    @Deprecated
    public static void commitRequestErrInfoStatistics(MotuRequestErrInfo info) {
        commitRequestErrInfoStatistics(info, new MotuRequestErrStatisticsInfo());
    }

    public static void commitPlayErrInfoStatistics(MotuVideoPlayErrInfo info, MotuVideoPlayErrStatisticsInfo statisticsInfo, Boolean isPlaying) {
        String monitorPointer;
        if (info == null || isPlaying == null || statisticsInfo == null) {
            Logger.d("VPM", "VideoPlayErrInfo is null");
            return;
        }
        if (isPlaying.booleanValue()) {
            monitorPointer = VPMConstants.MONITORPOINTER_PLAYING;
        } else {
            monitorPointer = VPMConstants.MONITORPOINTER_BEFORE_PLAY;
        }
        DimensionSet ds = DimensionSet.create();
        ds.addDimension(VPMConstants.DIMENSION_MEDIATYPE);
        ds.addDimension(VPMConstants.DIMENSION_VIDEOFORMAT);
        ds.addDimension(VPMConstants.DIMENSION_SOURCEIDENTYTY);
        ds.addDimension(VPMConstants.DIMENSION_PLAYERCORE);
        ds.addDimension(VPMConstants.DIMENSION_ISSUCCESS);
        ds.addDimension(VPMConstants.DIMENSION_VIDEOERRORCODE);
        ds.addDimension(VPMConstants.DIMENSION_VIDEOERRMSA);
        ds.addDimension(VPMConstants.DIMENSION_BUSINESSTYPE);
        ds.addDimension(VPMConstants.DIMENSION_PLAYWAY);
        ds.addDimension(VPMConstants.DIMENSION_VIDEOPLAYTYPE);
        ds.addDimension(VPMConstants.DIMENSION_CDNIP);
        if (info.extInfoData != null && info.extInfoData.size() > 0) {
            for (String key : info.extInfoData.keySet()) {
                ds.addDimension(key);
            }
        }
        MeasureSet ms = MeasureSet.create(new String[0]);
        if (statisticsInfo.extStatisticsData != null && statisticsInfo.extStatisticsData.size() > 0) {
            for (String key2 : statisticsInfo.extStatisticsData.keySet()) {
                ms.addMeasure(key2);
            }
        }
        AppMonitor.register(VPMConstants.VPM, monitorPointer, ms, ds, true);
        DimensionValueSet dvs = DimensionValueSet.create();
        dvs.setMap(info.toMap());
        AppMonitor.Stat.commit(VPMConstants.VPM, monitorPointer, dvs, MeasureValueSet.create(statisticsInfo.toMap()));
    }

    @Deprecated
    public static void commitPlayErrInfoStatistics(MotuVideoPlayErrInfo info, Boolean isPlaying) {
        commitPlayErrInfoStatistics(info, new MotuVideoPlayErrStatisticsInfo(), isPlaying);
    }

    public static void commitImpairmentStatistic(MotuMediaBase baseInfo, ImpairmentStatisticsInfo statisticsInfo) {
        if (baseInfo == null || statisticsInfo == null) {
            Logger.d("VPM", "baseInfo or statisticsInfo is null");
            return;
        }
        DimensionSet ds = DimensionSet.create();
        ds.addDimension(VPMConstants.DIMENSION_VIDEOFORMAT);
        ds.addDimension(VPMConstants.DIMENSION_MEDIATYPE);
        ds.addDimension(VPMConstants.DIMENSION_SOURCEIDENTYTY);
        ds.addDimension(VPMConstants.DIMENSION_PLAYERCORE);
        if (baseInfo.extInfoData != null && baseInfo.extInfoData.size() > 0) {
            for (String key : baseInfo.extInfoData.keySet()) {
                ds.addDimension(key);
            }
        }
        MeasureSet ms = MeasureSet.create(new String[]{"impairmentDuration", VPMConstants.MEASURE_IMP_IMPAIRMENTINTERVAL});
        if (statisticsInfo.extStatisticsData != null && statisticsInfo.extStatisticsData.size() > 0) {
            for (String key2 : statisticsInfo.extStatisticsData.keySet()) {
                ms.addMeasure(key2);
            }
        }
        AppMonitor.register(VPMConstants.VPM, VPMConstants.MONITORPOINTER_IMPAIRMENT, ms, ds, true);
        DimensionValueSet dvs = DimensionValueSet.create();
        dvs.setMap(baseInfo.toBaseMap());
        AppMonitor.Stat.commit(VPMConstants.VPM, VPMConstants.MONITORPOINTER_IMPAIRMENT, dvs, MeasureValueSet.create(statisticsInfo.toMap()));
    }

    public static void commitFluentStatistic(FluentInfo fluentInfo, FluentStatisticsInfo statisticsInfo) {
        if (fluentInfo == null || statisticsInfo == null) {
            Logger.d("VPM", "fluentInfo or statisticsInfo is null");
            return;
        }
        DimensionSet ds = DimensionSet.create();
        ds.addDimension(VPMConstants.DIMENSION_VIDEOFORMAT);
        ds.addDimension(VPMConstants.DIMENSION_MEDIATYPE);
        ds.addDimension(VPMConstants.DIMENSION_SOURCEIDENTYTY);
        ds.addDimension(VPMConstants.DIMENSION_PLAYERCORE);
        ds.addDimension("playType");
        if (fluentInfo.extInfoData != null && fluentInfo.extInfoData.size() > 0) {
            for (String key : fluentInfo.extInfoData.keySet()) {
                ds.addDimension(key);
            }
        }
        MeasureSet ms = MeasureSet.create(new String[]{VPMConstants.MEASURE_FLUENT_PLAYFLUENTSLICES, VPMConstants.MEASURE_FLUENT_PLAYSLICES});
        if (statisticsInfo.extStatisticsData != null && statisticsInfo.extStatisticsData.size() > 0) {
            for (String key2 : statisticsInfo.extStatisticsData.keySet()) {
                ms.addMeasure(key2);
            }
        }
        AppMonitor.register(VPMConstants.VPM, VPMConstants.MONITORPOINTER_FLUENT, ms, ds, true);
        DimensionValueSet dvs = DimensionValueSet.create();
        dvs.setMap(fluentInfo.toMap());
        AppMonitor.Stat.commit(VPMConstants.VPM, VPMConstants.MONITORPOINTER_FLUENT, dvs, MeasureValueSet.create(statisticsInfo.toMap()));
    }

    public static void commitSmoothSwitchStatistics(MotuMediaBase baseInfo, SmoothSwitchStatisticsInfo statisticsInfo) {
        if (baseInfo == null || statisticsInfo == null) {
            Logger.d("VPM", "baseInfo or statisticsInfo is null");
            return;
        }
        DimensionSet ds = DimensionSet.create();
        ds.addDimension(VPMConstants.DIMENSION_VIDEOFORMAT);
        ds.addDimension(VPMConstants.DIMENSION_MEDIATYPE);
        ds.addDimension(VPMConstants.DIMENSION_SOURCEIDENTYTY);
        ds.addDimension(VPMConstants.DIMENSION_PLAYERCORE);
        if (baseInfo.extInfoData != null && baseInfo.extInfoData.size() > 0) {
            for (String key : baseInfo.extInfoData.keySet()) {
                ds.addDimension(key);
            }
        }
        MeasureSet ms = MeasureSet.create(new String[]{VPMConstants.MEASURE_SMOOTHSWITCHSUCCESS, VPMConstants.MEASURE_SMOOTHSWITCHCOUNTS});
        if (statisticsInfo.extStatisticsData != null && statisticsInfo.extStatisticsData.size() > 0) {
            for (String key2 : statisticsInfo.extStatisticsData.keySet()) {
                ms.addMeasure(key2);
            }
        }
        AppMonitor.register(VPMConstants.VPM, VPMConstants.MONITORPOINTER_SMOOTHSWITCH, ms, ds, true);
        DimensionValueSet dvs = DimensionValueSet.create();
        dvs.setMap(baseInfo.toBaseMap());
        AppMonitor.Stat.commit(VPMConstants.VPM, VPMConstants.MONITORPOINTER_SMOOTHSWITCH, dvs, MeasureValueSet.create(statisticsInfo.toMap()));
    }

    public static void commitPlayKeyStatistics(MotuMediaInfo mediaInfo, MotuStatisticsInfo statisticsInfo) {
        if (mediaInfo == null || statisticsInfo == null) {
            Logger.d("VPM", "mediaInfo,mediaInfo", "StatisticsInfo", statisticsInfo);
            return;
        }
        DimensionSet ds = DimensionSet.create();
        ds.addDimension(VPMConstants.DIMENSION_MEDIATYPE);
        ds.addDimension(VPMConstants.DIMENSION_VIDEOWIDTH);
        ds.addDimension(VPMConstants.DIMENSION_VIDEOHEIGHT);
        ds.addDimension(VPMConstants.DIMENSION_VIDEOCODE);
        ds.addDimension(VPMConstants.DIMENSION_SCREENSIZE);
        ds.addDimension(VPMConstants.DIMENSION_VIDEOFORMAT);
        ds.addDimension(VPMConstants.DIMENSION_BEFOREDURATIONADTYPE);
        ds.addDimension("playType");
        ds.addDimension(VPMConstants.DIMENSION_PLAYWAY);
        ds.addDimension(VPMConstants.DIMENSION_VIDEOPROTOCOL);
        ds.addDimension(VPMConstants.DIMENSION_SOURCEIDENTYTY);
        ds.addDimension(VPMConstants.DIMENSION_PLAYERCORE);
        if (mediaInfo.extInfoData != null && mediaInfo.extInfoData.size() > 0) {
            for (String key : mediaInfo.extInfoData.keySet()) {
                ds.addDimension(key);
            }
        }
        MeasureSet ms = MeasureSet.create(new String[]{VPMConstants.MEASURE_ADPLAYDURATION, VPMConstants.MEASURE_VIDEOPLAYDURATION, VPMConstants.MEASURE_BUFFERLATENCY, VPMConstants.MEASURE_VIDEOFIRSTFRAMEDURATION, VPMConstants.MEASURE_VIDEOFRAMERATE, VPMConstants.MEASURE_AVG_VIDEOBITRATE, VPMConstants.MEASURE_AVG_KEYFRAMESIZE, VPMConstants.MEASURE_IMPAIRMENTFREQUENCY, "impairmentDuration", VPMConstants.MEASURE_IMPAIRMENTDEGREE, VPMConstants.MEASURE_DURATION, VPMConstants.MEASURE_ADURLREQTIME, VPMConstants.MEASURE_ADPLAYERPREPARE, VPMConstants.MEASURE_VIDEOURLREQTIME, VPMConstants.MEASURE_VIDEOPLAYERPREPARE, VPMConstants.MEASURE_SEEKDURATION, VPMConstants.MEASURE_CDNURLREQDURATION, VPMConstants.MEASURE_SEEKCOUNT, VPMConstants.MEASURE_VIDEOLOCALCACHESIZE});
        if (statisticsInfo.extStatisticsData != null && statisticsInfo.extStatisticsData.size() > 0) {
            for (String key2 : statisticsInfo.extStatisticsData.keySet()) {
                ms.addMeasure(key2);
            }
        }
        AppMonitor.register(VPMConstants.VPM, VPMConstants.MONITORPOINTER_ONE_PLAY, ms, ds, true);
        DimensionValueSet dvs = DimensionValueSet.create();
        dvs.setMap(mediaInfo.toMap());
        AppMonitor.Stat.commit(VPMConstants.VPM, VPMConstants.MONITORPOINTER_ONE_PLAY, dvs, MeasureValueSet.create(statisticsInfo.toMap()));
    }
}
