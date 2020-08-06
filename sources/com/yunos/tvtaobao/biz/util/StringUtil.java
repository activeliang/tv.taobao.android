package com.yunos.tvtaobao.biz.util;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import com.yunos.tvtaobao.businessview.R;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static final String MOBILE_NUMBER_MATCHER = "^(((13[0-9])|(15[0-3,5-9])|(18[0,5-6,7-9]))([0-9]{8}))$";
    public static final String POSTCODE_MATCHER = "([0-9]){6}+";
    private static final String TAG = "StringUtil";
    public static final String URL_CIBN_MATCHER = "(https://|http://){0,1}(\\w*\\.)+ott\\.cibntv\\.net/{0,1}.*";
    public static final String URL_INNER_MATCHER = "((http://)|(https://)){0,1}([\\w-\\.]+\\.){0,1}(taobao|tmall|alibaba|alipay|etao|koubei|juhuasuan|yunos|xiami|wasu)\\.(com|tv)/{0,1}.*";
    public static final String URL_MATCHER = "((http://)|(https://)){0,1}[\\w-\\.]+\\.(com|net|cn|gov\\.cn|org|name|com\\.cn|net\\.cn|org\\.cn|info|biz|cc|tv|hk|mobi)/{0,1}.*";

    public static int getLength(String str) {
        if (str == null) {
            return 0;
        }
        return str.length();
    }

    public static boolean isDigit(String str) {
        int i = str.length();
        do {
            i--;
            if (i < 0) {
                return true;
            }
        } while (Character.isDigit(str.charAt(i)));
        return false;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isUrl(String s) {
        if (s == null) {
            return false;
        }
        return s.matches(URL_MATCHER);
    }

    public static boolean isInnerUrl(String s) {
        if (s == null) {
            return false;
        }
        return s.matches(URL_INNER_MATCHER) || s.matches(URL_CIBN_MATCHER);
    }

    public static boolean isMobileNumber(String s) {
        if (s == null) {
            return false;
        }
        return s.matches(MOBILE_NUMBER_MATCHER);
    }

    public static boolean isPostCode(String s) {
        if (s == null) {
            return false;
        }
        return s.matches(POSTCODE_MATCHER);
    }

    public static String fomatUrl(String s) {
        if (s == null) {
            return null;
        }
        return (s.startsWith("http://") || s.startsWith("https://")) ? s : "http://" + s;
    }

    public static String getItemIdFromUrl(String url) {
        if (url != null) {
            Matcher m = Pattern.compile("http://a.m.(taobao|tmall).com/i([0-9]{6,12}).htm").matcher(url);
            if (m.find() && m.groupCount() > 1) {
                return m.group(2);
            }
        }
        return null;
    }

    public static String formatPrice(String price) {
        if (price.indexOf("-") != -1) {
            return price.split("-")[0];
        }
        return price;
    }

    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            return s.replaceAll("0+?$", "").replaceAll("[.]$", "");
        }
        return s;
    }

    public static SpannableString formatPriceToSpan(Context context, String price) {
        SpannableString spPrice = new SpannableString("¥" + price);
        int p = price.indexOf(".");
        if (p >= 0 && p <= 4) {
            spPrice.setSpan(new AbsoluteSizeSpan((int) context.getResources().getDimension(R.dimen.sp_48)), "¥".length(), "¥".length() + p, 33);
            return spPrice;
        } else if (4 < p && p <= 6) {
            spPrice.setSpan(new AbsoluteSizeSpan((int) context.getResources().getDimension(R.dimen.sp_30)), "¥".length(), "¥".length() + p, 33);
            return spPrice;
        } else if (p != -1) {
            SpannableString spPrice2 = new SpannableString("无价之宝");
            spPrice2.setSpan(new AbsoluteSizeSpan((int) context.getResources().getDimension(R.dimen.sp_36)), 0, "无价之宝".length(), 33);
            return spPrice2;
        } else if (price.length() >= 0 && price.length() <= 4) {
            spPrice.setSpan(new AbsoluteSizeSpan((int) context.getResources().getDimension(R.dimen.sp_48)), "¥".length(), spPrice.length(), 33);
            return spPrice;
        } else if (price.length() <= 4 || price.length() > 6) {
            SpannableString spPrice3 = new SpannableString("无价之宝");
            spPrice3.setSpan(new AbsoluteSizeSpan((int) context.getResources().getDimension(R.dimen.sp_36)), 0, "无价之宝".length(), 33);
            return spPrice3;
        } else {
            spPrice.setSpan(new AbsoluteSizeSpan((int) context.getResources().getDimension(R.dimen.sp_30)), "¥".length(), spPrice.length(), 33);
            return spPrice;
        }
    }

    public static String formatValue(String value) {
        int integer = Integer.valueOf(value).intValue();
        float count = Float.valueOf(value).floatValue();
        if (integer >= 10000 && integer < 1000000) {
            return new DecimalFormat("0.00").format((double) (count / 10000.0f)) + "万";
        } else if (integer >= 1000000 && integer < 10000000) {
            return new DecimalFormat("0.00").format((double) (count / 1000000.0f)) + "百万";
        } else if (10000000 > integer || integer >= 100000000) {
            return Integer.valueOf((int) count) + "";
        } else {
            return new DecimalFormat("0.00").format((double) (count / 1.0E7f)) + "千万";
        }
    }

    public static String formatGautee(String str) {
        String currentStr = str;
        if (str.contains("天内发货") && str.contains("卖家承诺")) {
            return str.substring(2);
        }
        if (str.contains("小时内发货") && str.contains("卖家承诺")) {
            return str.substring(2);
        }
        if ((str.contains("不支持") && str.contains("天退换")) || str.contains("天无理由")) {
            return str.substring(0, str.indexOf("天")) + "天退换";
        } else if (!str.contains("天退货") && !str.contains("天无理由") && !str.contains("天退换") && !str.contains("天无理由退")) {
            return currentStr;
        } else {
            return str.substring(0, str.indexOf("天")) + "天无理由退换货";
        }
    }
}
