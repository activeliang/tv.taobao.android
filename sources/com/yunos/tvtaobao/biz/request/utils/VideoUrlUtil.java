package com.yunos.tvtaobao.biz.request.utils;

import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import com.yunos.tv.tvsdk.media.data.HuasuVideo;
import com.yunos.tvtaobao.biz.request.bo.juhuasuan.bo.CountList;
import com.yunos.tvtaobao.biz.request.bo.juhuasuan.bo.ItemMO;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.StringUtils;
import mtopsdk.xstate.util.XStateConstants;

public class VideoUrlUtil {
    public static String definition = "";
    public static String equipmentType = "2";
    public static String flashVersion = "";
    public static String playType = "1";
    public static String templateId = "10200";
    public static String videoCdnDomain = "http://cloud.video.taobao.com/";

    public static CountList<ItemMO> toM3u8(CountList<ItemMO> items) {
        if (items != null && !items.isEmpty()) {
            Iterator it = items.iterator();
            while (it.hasNext()) {
                ItemMO item = (ItemMO) it.next();
                if (!StringUtils.isEmpty(item.getVideoUrl())) {
                    item.setVideoUrl(toM3u8(item.getVideoUrl()));
                }
            }
        }
        return items;
    }

    public static List<ItemMO> toM3u8(List<ItemMO> items) {
        if (items != null && !items.isEmpty()) {
            for (ItemMO item : items) {
                if (!StringUtils.isEmpty(item.getVideoUrl())) {
                    item.setVideoUrl(toM3u8(item.getVideoUrl()));
                }
            }
        }
        return items;
    }

    public static String toM3u8(String url) {
        if (url.endsWith(".swf")) {
            return swfToM3u8(url);
        }
        if (url.indexOf(WVUtils.URL_DATA_CHAR) != -1) {
            return infoToM3u8(url);
        }
        return url;
    }

    public static String swfToM3u8(String url) {
        return url.replaceAll("\\/p\\/[^\\/]*\\/", "/p/" + playType + WVNativeCallbackUtil.SEPERATER).replaceAll("\\/e\\/[^\\/]*\\/", "/e/" + equipmentType + WVNativeCallbackUtil.SEPERATER).replaceAll("\\/t\\/[^\\/]*\\/", "/t/" + templateId + WVNativeCallbackUtil.SEPERATER).replaceAll("\\/d\\/[^\\/]*\\/", "/d/" + definition + WVNativeCallbackUtil.SEPERATER).replaceAll("\\.(swf|mp4)$", ".m3u8");
    }

    public static String infoToM3u8(String url) {
        Map<String, String> mapRequest = new HashMap<>();
        String[] arrSplit = url.split("[?]");
        if (arrSplit.length > 1) {
            for (String strSplit : arrSplit[1].split("[&]")) {
                String[] arrSplitEqual = strSplit.split("[=]");
                if (arrSplitEqual.length > 1) {
                    mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
                } else if (arrSplitEqual[0] != "") {
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        StringBuffer urlStringBuffer = new StringBuffer();
        urlStringBuffer.append(videoCdnDomain);
        urlStringBuffer.append("u/").append(mapRequest.get(XStateConstants.KEY_UID)).append(WVNativeCallbackUtil.SEPERATER);
        urlStringBuffer.append("p/").append(playType).append(WVNativeCallbackUtil.SEPERATER);
        urlStringBuffer.append("e/").append(equipmentType).append(WVNativeCallbackUtil.SEPERATER);
        urlStringBuffer.append("t/").append(templateId).append(WVNativeCallbackUtil.SEPERATER);
        urlStringBuffer.append("d/").append(definition).append(WVNativeCallbackUtil.SEPERATER);
        urlStringBuffer.append(mapRequest.get(HuasuVideo.TAG_VID)).append(".m3u8");
        return urlStringBuffer.toString();
    }
}
