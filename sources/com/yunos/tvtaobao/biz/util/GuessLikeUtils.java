package com.yunos.tvtaobao.biz.util;

import android.text.TextUtils;
import com.yunos.tvtaobao.biz.request.bo.GuessLikeGoods;
import com.yunos.tvtaobao.biz.request.bo.GuessLikeGoodsData;
import com.yunos.tvtaobao.biz.request.bo.KMGoods;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.List;

public class GuessLikeUtils {
    public static String getItemString(int firstExposePosition, int lastExposePosition, List<GuessLikeGoodsData> recommemdList) {
        StringBuffer sbItemId = new StringBuffer();
        if (recommemdList != null && recommemdList.size() > 0 && firstExposePosition >= 0 && firstExposePosition < recommemdList.size() && lastExposePosition >= 0 && lastExposePosition < recommemdList.size()) {
            for (int i = firstExposePosition; i <= lastExposePosition; i++) {
                GuessLikeGoodsData recommendGoods = recommemdList.get(i);
                if (recommendGoods != null) {
                    if (GuessLikeGoodsData.TYPE_ITEM.equals(recommendGoods.getType())) {
                        GuessLikeGoods guessLikeGoods = recommendGoods.getGuessLikeGoods();
                        if (i == lastExposePosition) {
                            if (!TextUtils.isEmpty(guessLikeGoods.getTid())) {
                                sbItemId.append(guessLikeGoods.getTid());
                            }
                        } else if (!TextUtils.isEmpty(guessLikeGoods.getTid())) {
                            sbItemId.append(guessLikeGoods.getTid() + ",");
                        }
                    } else if (GuessLikeGoodsData.TYPE_ZTC.equals(recommendGoods.getType())) {
                        KMGoods kmGoods = recommendGoods.getKmGoods();
                        if (i == lastExposePosition) {
                            if (!TextUtils.isEmpty(kmGoods.getResourceid())) {
                                sbItemId.append(kmGoods.getResourceid());
                            }
                        } else if (!TextUtils.isEmpty(kmGoods.getResourceid())) {
                            sbItemId.append(kmGoods.getResourceid() + ",");
                        }
                    }
                }
            }
        }
        ZpLogger.i("GuessLikeUtils", "lastExposePosition  = " + lastExposePosition);
        return sbItemId.toString();
    }
}
