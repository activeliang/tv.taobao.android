package tv.danmaku.ijk.media.player;

import android.annotation.TargetApi;
import android.media.MediaCodecInfo;
import android.os.Build;
import android.text.TextUtils;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class IjkMediaCodecInfo {
    public static int RANK_ACCEPTABLE = 700;
    public static int RANK_LAST_CHANCE = 600;
    public static int RANK_MAX = 1000;
    public static int RANK_NON_STANDARD = 100;
    public static int RANK_NO_SENSE = 0;
    public static int RANK_SECURE = 300;
    public static int RANK_SOFTWARE = 200;
    public static int RANK_TESTED = 800;
    private static final String TAG = "IjkMediaCodecInfo";
    private static Map<String, Integer> sKnownCodecList;
    public MediaCodecInfo mCodecInfo;
    public String mMimeType;
    public int mRank = 0;

    private static synchronized Map<String, Integer> getKnownCodecList() {
        Map<String, Integer> map;
        synchronized (IjkMediaCodecInfo.class) {
            if (sKnownCodecList != null) {
                map = sKnownCodecList;
            } else {
                sKnownCodecList = new TreeMap(String.CASE_INSENSITIVE_ORDER);
                sKnownCodecList.put("OMX.Nvidia.h264.decode", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.Nvidia.h264.decode.secure", Integer.valueOf(RANK_SECURE));
                sKnownCodecList.put("OMX.Intel.hw_vd.h264", Integer.valueOf(RANK_TESTED + 1));
                sKnownCodecList.put("OMX.Intel.VideoDecoder.AVC", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.qcom.video.decoder.avc", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.ittiam.video.decoder.avc", Integer.valueOf(RANK_NO_SENSE));
                sKnownCodecList.put("OMX.SEC.avc.dec", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.SEC.AVC.Decoder", Integer.valueOf(RANK_TESTED - 1));
                sKnownCodecList.put("OMX.SEC.avcdec", Integer.valueOf(RANK_TESTED - 2));
                sKnownCodecList.put("OMX.SEC.avc.sw.dec", Integer.valueOf(RANK_SOFTWARE));
                sKnownCodecList.put("OMX.SEC.hevc.sw.dec", Integer.valueOf(RANK_SOFTWARE));
                sKnownCodecList.put("OMX.Exynos.avc.dec", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.Exynos.AVC.Decoder", Integer.valueOf(RANK_TESTED - 1));
                sKnownCodecList.put("OMX.k3.video.decoder.avc", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.IMG.MSVDX.Decoder.AVC", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.TI.DUCATI1.VIDEO.DECODER", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.rk.video_decoder.avc", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.amlogic.avc.decoder.awesome", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.MARVELL.VIDEO.HW.CODA7542DECODER", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.MARVELL.VIDEO.H264DECODER", Integer.valueOf(RANK_SOFTWARE));
                sKnownCodecList.remove("OMX.Action.Video.Decoder");
                sKnownCodecList.remove("OMX.allwinner.video.decoder.avc");
                sKnownCodecList.remove("OMX.BRCM.vc4.decoder.avc");
                sKnownCodecList.remove("OMX.brcm.video.h264.hw.decoder");
                sKnownCodecList.remove("OMX.brcm.video.h264.decoder");
                sKnownCodecList.remove("OMX.cosmo.video.decoder.avc");
                sKnownCodecList.remove("OMX.duos.h264.decoder");
                sKnownCodecList.remove("OMX.hantro.81x0.video.decoder");
                sKnownCodecList.remove("OMX.hantro.G1.video.decoder");
                sKnownCodecList.remove("OMX.hisi.video.decoder");
                sKnownCodecList.remove("OMX.LG.decoder.video.avc");
                sKnownCodecList.remove("OMX.MS.AVC.Decoder");
                sKnownCodecList.remove("OMX.RENESAS.VIDEO.DECODER.H264");
                sKnownCodecList.remove("OMX.RTK.video.decoder");
                sKnownCodecList.remove("OMX.sprd.h264.decoder");
                sKnownCodecList.remove("OMX.ST.VFM.H264Dec");
                sKnownCodecList.remove("OMX.vpu.video_decoder.avc");
                sKnownCodecList.remove("OMX.WMT.decoder.avc");
                sKnownCodecList.remove("OMX.bluestacks.hw.decoder");
                sKnownCodecList.put("OMX.google.h264.decoder", Integer.valueOf(RANK_SOFTWARE));
                sKnownCodecList.put("OMX.google.h264.lc.decoder", Integer.valueOf(RANK_SOFTWARE));
                sKnownCodecList.put("OMX.k3.ffmpeg.decoder", Integer.valueOf(RANK_SOFTWARE));
                sKnownCodecList.put("OMX.ffmpeg.video.decoder", Integer.valueOf(RANK_SOFTWARE));
                sKnownCodecList.put("OMX.sprd.soft.h264.decoder", Integer.valueOf(RANK_SOFTWARE));
                map = sKnownCodecList;
            }
        }
        return map;
    }

    @TargetApi(16)
    public static IjkMediaCodecInfo setupCandidate(MediaCodecInfo codecInfo, String mimeType) {
        int rank;
        if (codecInfo == null || Build.VERSION.SDK_INT < 16) {
            return null;
        }
        String name = codecInfo.getName();
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        String name2 = name.toLowerCase(Locale.US);
        int i = RANK_NO_SENSE;
        if (!name2.startsWith("omx.")) {
            rank = RANK_NON_STANDARD;
        } else if (name2.startsWith("omx.pv")) {
            rank = RANK_SOFTWARE;
        } else if (name2.startsWith("omx.google.")) {
            rank = RANK_SOFTWARE;
        } else if (name2.startsWith("omx.ffmpeg.")) {
            rank = RANK_SOFTWARE;
        } else if (name2.startsWith("omx.k3.ffmpeg.")) {
            rank = RANK_SOFTWARE;
        } else if (name2.startsWith("omx.avcodec.")) {
            rank = RANK_SOFTWARE;
        } else if (name2.startsWith("omx.ittiam.")) {
            rank = RANK_NO_SENSE;
        } else if (!name2.startsWith("omx.mtk.")) {
            Integer knownRank = getKnownCodecList().get(name2);
            if (knownRank != null) {
                rank = knownRank.intValue();
            } else {
                try {
                    if (codecInfo.getCapabilitiesForType(mimeType) != null) {
                        rank = RANK_ACCEPTABLE;
                    } else {
                        rank = RANK_LAST_CHANCE;
                    }
                } catch (Throwable th) {
                    rank = RANK_LAST_CHANCE;
                }
            }
        } else if (Build.VERSION.SDK_INT < 18) {
            rank = RANK_NO_SENSE;
        } else {
            rank = RANK_TESTED;
        }
        IjkMediaCodecInfo candidate = new IjkMediaCodecInfo();
        candidate.mCodecInfo = codecInfo;
        candidate.mRank = rank;
        candidate.mMimeType = mimeType;
        return candidate;
    }
}
