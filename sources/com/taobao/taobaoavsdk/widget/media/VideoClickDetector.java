package com.taobao.taobaoavsdk.widget.media;

import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import com.yunos.tv.core.util.DeviceUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class VideoClickDetector {
    private static final String TAG = VideoClickDetector.class.getSimpleName();

    public static int transX(int x, int viewWidth, int baseWidth) {
        int width = DeviceUtil.SCREENTYPE.ScreenType_720p;
        if (baseWidth > 0) {
            width = baseWidth;
        }
        if (viewWidth > 0) {
            return (int) ((((float) (x * width)) * 1.0f) / ((float) viewWidth));
        }
        return x;
    }

    public static int transY(int y, int viewHeight, int baseHigh) {
        int heigh = IMediaPlayer.MEDIA_INFO_HTTPDNS_CONNECT_FAIL;
        if (baseHigh > 0) {
            heigh = baseHigh;
        }
        if (viewHeight > 0) {
            return (int) ((((float) (y * heigh)) * 1.0f) / ((float) viewHeight));
        }
        return y;
    }

    public static int transW(int viewWidth, int w, int baseWidth) {
        int width = DeviceUtil.SCREENTYPE.ScreenType_720p;
        if (baseWidth > 0) {
            width = baseWidth;
        }
        return (int) ((((float) (w * viewWidth)) * 1.0f) / ((float) width));
    }

    public static int transH(int viewHeight, int h, int baseHigh) {
        int heigh = IMediaPlayer.MEDIA_INFO_HTTPDNS_CONNECT_FAIL;
        if (baseHigh > 0) {
            heigh = baseHigh;
        }
        return (int) ((((float) (h * viewHeight)) * 1.0f) / ((float) heigh));
    }

    public static class RectInfo {
        public int mIndex = -1;
        public Rect mRect;

        public RectInfo(Rect rect, int index) {
            this.mRect = rect;
            this.mIndex = index;
        }
    }

    public static RectInfo detectVideoClick(int x, int y, String seiData) {
        if (seiData != null) {
            try {
                JSONArray array = new JSONObject(seiData).optJSONArray("rect");
                if (array != null && array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        Rect rect = parseRect(array.optString(i));
                        if (rect.contains(x, y)) {
                            return new RectInfo(rect, i);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Rect parseRect(String str) {
        String[] strs;
        if (TextUtils.isEmpty(str) || (strs = str.split("-")) == null || strs.length != 4) {
            return null;
        }
        Rect rect = new Rect();
        rect.left = parserTypeInt(strs[0]);
        rect.top = parserTypeInt(strs[1]);
        rect.right = rect.left + parserTypeInt(strs[2]);
        rect.bottom = rect.top + parserTypeInt(strs[3]);
        Log.d(TAG, "rect = " + rect.toString());
        return rect;
    }

    public static int parserTypeInt(String str) {
        if (str == null) {
            return 0;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
