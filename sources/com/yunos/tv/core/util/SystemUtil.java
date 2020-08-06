package com.yunos.tv.core.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import android.view.View;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.core.config.RequestConfig;
import com.yunos.tv.core.config.SystemConfig;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemUtil {
    public static String[] week = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public static int dip2Pix(float data) {
        return Float.valueOf(SystemConfig.DENSITY.floatValue() * data).intValue();
    }

    public static int pix2dip(float data) {
        return Float.valueOf(data / SystemConfig.DENSITY.floatValue()).intValue();
    }

    public static int[] getLocationOnScreen(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        return location;
    }

    public static int getYOnScreen(View v) {
        return getLocationOnScreen(v)[1];
    }

    public static int getXOnScreen(View v) {
        return getLocationOnScreen(v)[0];
    }

    public static int getYOnScreen(Activity context, int id) {
        return getLocationOnScreen(context.findViewById(id))[1];
    }

    public static int[] getLocationInWindow(View v) {
        int[] location = new int[2];
        v.getLocationInWindow(location);
        return location;
    }

    public static int getXInWindow(View v) {
        return getLocationInWindow(v)[0];
    }

    public static int getYInWindow(View v) {
        return getLocationInWindow(v)[1];
    }

    public static String encodeUrl(String str) {
        if (str == null) {
            return null;
        }
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String mergeImageUrl(String imageName) {
        return getImageServer(imageName) + imageName;
    }

    public static String getImageServer(String imageName) {
        if (imageName == null) {
            ZpLogger.e("SystemUtil", "getImageServer: imageName is null");
            return null;
        }
        return RequestConfig.getImageServer()[Math.abs(imageName.hashCode()) % RequestConfig.getImageServer().length];
    }

    public static byte[] bitmapToBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }

    public static byte[] drawableToBytes(BitmapDrawable d) {
        return bitmapToBytes(d.getBitmap());
    }

    public static Drawable bytesToDrawable(byte[] bytes) {
        return new BitmapDrawable(BitmapFactory.decodeByteArray(bytes, 0, bytes.length, (BitmapFactory.Options) null));
    }

    public static int getColor(Context context, int id) {
        return context.getResources().getColor(id);
    }

    public static int convertToInt(String s) {
        if (s == null || "".equals(s)) {
            return -1;
        }
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static long convertToLong(String s) {
        if (s == null || "".equals(s)) {
            return -1;
        }
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Date convertStringToDate(String s) {
        if (s != null) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String convertDateToString(Date date, String fomat) {
        if (date == null) {
            return "";
        }
        if (fomat == null) {
            fomat = "yyyy-MM-dd HH:mm:ss";
        }
        return new SimpleDateFormat(fomat).format(date);
    }

    public static double bigAdd(double v1, double v2) {
        return new BigDecimal(Double.toString(v1)).add(new BigDecimal(Double.toString(v2))).doubleValue();
    }

    public static double bigSub(double v1, double v2) {
        return new BigDecimal(Double.toString(v1)).subtract(new BigDecimal(Double.toString(v2))).doubleValue();
    }

    public static String getUrlFileName(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        int pointIndex = url.lastIndexOf(".");
        int lineIndex = url.lastIndexOf(WVNativeCallbackUtil.SEPERATER) + 1;
        if (pointIndex < lineIndex && (pointIndex = url.lastIndexOf(WVUtils.URL_DATA_CHAR)) < lineIndex) {
            pointIndex = url.length();
        }
        return url.substring(lineIndex, pointIndex);
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 1);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getUuid() {
        return CloudUUIDWrapper.getCloudUUID();
    }

    public static String getTopPackageName(Context context) {
        return ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningTasks(1).get(0).topActivity.getPackageName();
    }
}
