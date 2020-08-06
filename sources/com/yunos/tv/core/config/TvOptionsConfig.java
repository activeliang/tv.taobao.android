package com.yunos.tv.core.config;

import android.taobao.windvane.monitor.WVMonitorConstants;
import android.taobao.windvane.monitor.WVPackageMonitorInterface;
import android.text.TextUtils;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.HashMap;
import java.util.Map;

public class TvOptionsConfig {
    private static Map<TvOptionsChannel, String> mTvOptionMap;
    private static String[] tvOptions = {"1", "0", "0", "0", "0", "0"};

    static {
        initTvOptionMap();
    }

    private static void initTvOptionMap() {
        if (mTvOptionMap == null) {
            mTvOptionMap = new HashMap();
        }
        mTvOptionMap.put(TvOptionsChannel.OTHER, "00");
        mTvOptionMap.put(TvOptionsChannel.SEARCH, "01");
        mTvOptionMap.put(TvOptionsChannel.RETRIEVE, "02");
        mTvOptionMap.put(TvOptionsChannel.TTTJ, "03");
        mTvOptionMap.put(TvOptionsChannel.TMCS, "04");
        mTvOptionMap.put(TvOptionsChannel.JHS, "05");
        mTvOptionMap.put(TvOptionsChannel.SNYG, "06");
        mTvOptionMap.put(TvOptionsChannel.TAO_QG, "07");
        mTvOptionMap.put(TvOptionsChannel.TMGJ, "08");
        mTvOptionMap.put(TvOptionsChannel.CZ_YK, "09");
        mTvOptionMap.put(TvOptionsChannel.CZ_HF, "10");
        mTvOptionMap.put(TvOptionsChannel.SPP, "11");
        mTvOptionMap.put(TvOptionsChannel.SP_HC, "12");
        mTvOptionMap.put(TvOptionsChannel.COLLECT, "13");
        mTvOptionMap.put(TvOptionsChannel.ORDER, "14");
        mTvOptionMap.put(TvOptionsChannel.HC, WVMonitorConstants.FORCE_ONLINE_FAILED);
        mTvOptionMap.put(TvOptionsChannel.FZ_TRIP, WVMonitorConstants.MAPPING_URL_NULL_FAILED);
        mTvOptionMap.put(TvOptionsChannel.GUESSLIKE, WVMonitorConstants.MAPPING_URL_MATCH_FAILED);
        mTvOptionMap.put(TvOptionsChannel.FAST_ORDER, WVPackageMonitorInterface.NOT_INSTALL_FAILED);
        mTvOptionMap.put(TvOptionsChannel.VOICE_SEARCH_ORDER, WVPackageMonitorInterface.FORCE_UPDATE_FAILED);
        mTvOptionMap.put(TvOptionsChannel.VOICE_VIEW_MORE, WVPackageMonitorInterface.CONFIG_CLOSED_FAILED);
        mTvOptionMap.put(TvOptionsChannel.FIND_SAME, "24");
        mTvOptionMap.put(TvOptionsChannel.FOOT_MARK, "25");
        mTvOptionMap.put(TvOptionsChannel.DAILY_GOOD_SHOP, "26");
        mTvOptionMap.put(TvOptionsChannel.TAOBAO_TV, "27");
    }

    public static String getTvOptions() {
        StringBuilder tvOptionsBuilder = new StringBuilder();
        for (String s : tvOptions) {
            tvOptionsBuilder.append(s);
        }
        return String.valueOf(tvOptionsBuilder);
    }

    public static void setTvOptionsChannel(TvOptionsChannel channel) {
        if (mTvOptionMap == null) {
            initTvOptionMap();
        }
        String channelCode = mTvOptionMap.get(channel);
        if (channelCode != null) {
            tvOptions[1] = String.valueOf(channelCode.charAt(0));
            tvOptions[2] = String.valueOf(channelCode.charAt(1));
            ZpLogger.i("[tvOptionsDebug] setTvOptionsChannel ----> ", tvOptions[1] + tvOptions[2]);
        }
    }

    public static void setTvOptionsChannel(String channelCode) {
        if (channelCode.length() == 2) {
            tvOptions[1] = String.valueOf(channelCode.charAt(0));
            tvOptions[2] = String.valueOf(channelCode.charAt(1));
            ZpLogger.i("[tvOptionsDebug] setTvOptionsChannel ----> ", tvOptions[1] + tvOptions[2]);
        }
    }

    public static void setTvOptionVoiceSystem(String from) {
        if (TextUtils.isEmpty(from)) {
            tvOptions[3] = "0";
            tvOptions[4] = "0";
            ZpLogger.i("[tvOptionsDebug] setTvOptionsVoice ----> ", "0");
            ZpLogger.i("[tvOptionsDebug] setTvOptionsSystem ----> ", "0");
            return;
        }
        char c = 65535;
        switch (from.hashCode()) {
            case -2121201213:
                if (from.equals(BaseConfig.INTENT_KEY_FROM_VAL_VA)) {
                    c = 2;
                    break;
                }
                break;
            case 201667036:
                if (from.equals(BaseConfig.INTENT_KEY_FROM_VAL_VS)) {
                    c = 0;
                    break;
                }
                break;
            case 1052683748:
                if (from.equals("tvspeech")) {
                    c = 1;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                tvOptions[3] = "1";
                tvOptions[4] = "1";
                ZpLogger.i("[tvOptionsDebug] setTvOptionsVoice ----> ", "1");
                ZpLogger.i("[tvOptionsDebug] setTvOptionsSystem ----> ", "1");
                return;
            case 1:
                tvOptions[3] = "1";
                tvOptions[4] = "1";
                ZpLogger.i("[tvOptionsDebug] setTvOptionsVoice ----> ", "1");
                ZpLogger.i("[tvOptionsDebug] setTvOptionsSystem ----> ", "1");
                return;
            case 2:
                tvOptions[3] = "1";
                tvOptions[4] = "0";
                ZpLogger.i("[tvOptionsDebug] setTvOptionsVoice ----> ", "1");
                ZpLogger.i("[tvOptionsDebug] setTvOptionsSystem ----> ", "0");
                return;
            default:
                tvOptions[3] = "0";
                tvOptions[4] = "0";
                ZpLogger.i("[tvOptionsDebug] setTvOptionsVoice ----> ", "0");
                ZpLogger.i("[tvOptionsDebug] setTvOptionsSystem ----> ", "0");
                return;
        }
    }

    public static void setTvOptionsVoice(boolean voice) {
        if (voice) {
            tvOptions[3] = "1";
            ZpLogger.i("[tvOptionsDebug] setTvOptionsVoice ----> ", "1");
            return;
        }
        tvOptions[3] = "0";
        ZpLogger.i("[tvOptionsDebug] setTvOptionsVoice ----> ", "0");
    }

    public static void setTvOptionsSystem(boolean system) {
        if (system) {
            tvOptions[4] = "1";
            tvOptions[3] = "1";
            ZpLogger.i("[tvOptionsDebug] setTvOptionsSystem ----> ", "1");
            ZpLogger.i("[tvOptionsDebug] setTvOptionsVoice ----> ", "1");
            return;
        }
        tvOptions[4] = "0";
        ZpLogger.i("[tvOptionsDebug] setTvOptionsSystem ----> ", "0");
    }

    public static void setTvOptionsCart(boolean fromShopCart) {
        if (fromShopCart) {
            tvOptions[5] = "1";
            ZpLogger.i("[tvOptionsDebug] setTvOptionsCart ----> ", "1");
            return;
        }
        tvOptions[5] = "0";
        ZpLogger.i("[tvOptionsDebug] setTvOptionsCart ----> ", "0");
    }
}
