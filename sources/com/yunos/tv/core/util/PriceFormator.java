package com.yunos.tv.core.util;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import com.taobao.atlas.dexmerge.dx.io.Opcodes;
import java.text.NumberFormat;

public class PriceFormator {
    private static String formatLong(Long price) {
        if (price == null) {
            return "";
        }
        long mod = price.longValue() % 100;
        StringBuilder sb = new StringBuilder();
        sb.append("&#165;");
        sb.append(price.longValue() / 100);
        sb.append(".");
        if (mod < 10) {
            sb.append("0");
        }
        sb.append(mod);
        return sb.toString();
    }

    public static String formatNoSymbolLong(Long price) {
        if (price == null) {
            return "";
        }
        long mod = price.longValue() % 100;
        StringBuilder sb = new StringBuilder();
        sb.append(price.longValue() / 100);
        sb.append(".");
        if (mod < 10) {
            sb.append("0");
        }
        sb.append(mod);
        return sb.toString();
    }

    public static String formatDoublePrice(double price, boolean withSymbol) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        StringBuilder sb = new StringBuilder(nf.format(price));
        int index = sb.lastIndexOf(".");
        if (index != -1) {
            int len = 2 - ((sb.length() - index) - 1);
            for (int i = 1; i <= len; i++) {
                sb.append("0");
            }
        } else {
            sb.append(".00");
        }
        if (withSymbol) {
            sb.insert(0, "¥ ");
        }
        return sb.toString();
    }

    public static SpannableString formatSmallPrice(Long price, String... strings) {
        if (price == null) {
            return new SpannableString("");
        }
        String postFee = "";
        if (!(strings == null || strings.length <= 0 || strings[0] == null)) {
            postFee = strings[0];
        }
        String str = formatLong(price) + postFee;
        int index3 = str.length();
        int index2 = str.indexOf(".");
        if (index2 < 0) {
            return new SpannableString(str);
        }
        int color = Color.argb(255, Opcodes.SHL_LONG_2ADDR, 34, 61);
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(color), 0, 1, 33);
        ss.setSpan(new AbsoluteSizeSpan(12, true), 0, 1, 33);
        ss.setSpan(new ForegroundColorSpan(color), 1, index2, 33);
        ss.setSpan(new AbsoluteSizeSpan(18, true), 1, index2, 33);
        ss.setSpan(new ForegroundColorSpan(color), index2, index3, 33);
        ss.setSpan(new AbsoluteSizeSpan(12, true), index2, index3, 33);
        return ss;
    }

    public static SpannableString formatBigPrice(Long price) {
        String str = formatLong(price);
        int index3 = str.length();
        int index2 = str.lastIndexOf(".");
        if (index2 < 0) {
            return new SpannableString(str);
        }
        int bigFontSize = 18;
        int smallFontSize = 12;
        if (str.length() >= 8) {
            bigFontSize = 16;
            smallFontSize = 10;
        }
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(-1), 0, 1, 33);
        ss.setSpan(new AbsoluteSizeSpan(smallFontSize, true), 0, 1, 33);
        ss.setSpan(new ForegroundColorSpan(-1), 1, index2, 33);
        ss.setSpan(new AbsoluteSizeSpan(bigFontSize, true), 1, index2, 33);
        ss.setSpan(new ForegroundColorSpan(-1), index2, index3, 33);
        ss.setSpan(new AbsoluteSizeSpan(smallFontSize, true), index2, index3, 33);
        return ss;
    }

    public static void formatOriginalPrice(TextView text, Long originalPrice) {
        text.getPaint().setFlags(17);
        text.getPaint().setAntiAlias(true);
        String price = String.valueOf(originalPrice);
        if (price.length() > 2) {
            price = price.substring(0, price.length() - 2) + "." + price.substring(price.length() - 2);
        }
        text.setText(price);
    }

    public static SpannableString formatSmallTranPrice(Long price, int quantity, Long transportCharges) {
        if (transportCharges == null) {
            transportCharges = 0L;
        }
        if (price == null) {
            price = 0L;
        }
        return formatSmallPrice(Long.valueOf((price.longValue() * ((long) quantity)) + transportCharges.longValue()), new String[0]);
    }

    public static String formatSmallTranAndQuantity(int quantity, long transportCharges) {
        return (transportCharges > 0 ? "(含快递 " + formatNoSymbolLong(Long.valueOf(transportCharges)) + " 元) " : "") + "共 " + quantity + " 件";
    }

    public static String formatNoSymbolLongNoDecimals(Long price) {
        if (price == null) {
            return "";
        }
        long mod = price.longValue() % 100;
        StringBuilder sb = new StringBuilder();
        sb.append(price.longValue() / 100);
        if (mod != 0) {
            sb.append(".");
            sb.append(mod);
        }
        return sb.toString();
    }
}
