package com.taobao.orange.util;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.taobao.orange.model.IndexDO;
import com.taobao.orange.model.NameSpaceDO;
import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class OrangeUtils {
    private static final String TAG = "OrangeUtils";

    public static long parseLong(String value) {
        long result = 0;
        try {
            if (TextUtils.isEmpty(value)) {
                return 0;
            }
            result = Long.parseLong(value);
            return result;
        } catch (Exception e) {
            OLog.e(TAG, "parseLong", e, new Object[0]);
        }
    }

    public static String getCurFormatTime() {
        return new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
    }

    public static <T> List<T> getArrayListFromArray(T[] array) {
        List<T> list = new ArrayList<>();
        if (array != null && array.length > 0) {
            for (T t : array) {
                list.add(t);
            }
        }
        return list;
    }

    public static String[] getStringFromArray(List<String> array) {
        if (array == null || array.isEmpty()) {
            return null;
        }
        return (String[]) array.toArray(new String[array.size()]);
    }

    public static String getEncodeValue(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        try {
            return URLEncoder.encode(key, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String getDecodeValue(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        try {
            return URLDecoder.decode(key, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static long hash(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0;
        }
        long h = 0;
        int l = value.length();
        char[] chars = value.toCharArray();
        for (int i = 0; i < l; i++) {
            h = (31 * h) + ((long) chars[i]);
        }
        return h & Long.MAX_VALUE;
    }

    public static List<String> randomListFromSet(Set<String> set) {
        int indexTwo;
        List<String> list = new ArrayList<>();
        int vipsLength = set.size();
        if (vipsLength > 2) {
            Random random = new Random();
            int indexOne = random.nextInt(vipsLength);
            do {
                indexTwo = random.nextInt(vipsLength);
            } while (indexTwo == indexOne);
            int i = 0;
            for (String ip : set) {
                if (i == indexOne || i == indexTwo) {
                    list.add(ip);
                    if (list.size() == 2) {
                        break;
                    }
                }
                i++;
            }
        } else {
            list.addAll(set);
        }
        return list;
    }

    public static String encodeQueryParams(Map<String, String> params, String encoding) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder(64);
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getKey() != null) {
                    builder.append(URLEncoder.encode(entry.getKey(), encoding)).append("=").append(URLEncoder.encode(entry.getValue() == null ? "" : entry.getValue(), encoding).replace("+", "%20")).append("&");
                }
            }
            builder.deleteCharAt(builder.length() - 1);
        } catch (UnsupportedEncodingException e) {
        }
        return builder.toString();
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static String formatOperateSymbols(List<String> symbols) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < symbols.size(); i++) {
            if (i == 0) {
                sb.append(symbols.get(i));
            } else {
                sb.append("|").append(symbols.get(i));
            }
        }
        return sb.toString();
    }

    public static <T> Map<String, T> sortMapByKey(Map<String, T> oriMap, final boolean isRise) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, T> sortMap = new TreeMap<>(new Comparator<String>() {
            public int compare(String o1, String o2) {
                if (isRise) {
                    return o1.compareTo(o2);
                }
                return o2.compareTo(o1);
            }
        });
        sortMap.putAll(oriMap);
        return sortMap;
    }

    public static String formatIndexDO(IndexDO indexDO) {
        if (indexDO == null) {
            return null;
        }
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(IndexDO.class, new String[0]);
        filter.getExcludes().add("mergedNamespaces");
        return JSON.toJSONString((Object) indexDO, (SerializeFilter) filter, SerializerFeature.PrettyFormat);
    }

    public static String formatNamespaceDO(NameSpaceDO nameSpaceDO) {
        if (nameSpaceDO == null) {
            return null;
        }
        return JSON.toJSONString((Object) nameSpaceDO, (SerializeFilter) new SimplePropertyPreFilter(NameSpaceDO.class, "name", "version", "resourceId", "candidates"), SerializerFeature.PrettyFormat);
    }
}
