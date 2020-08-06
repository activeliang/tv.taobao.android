package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeBeanInfo;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bftv.fui.constantplugin.Constant;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.AccessControlException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import mtopsdk.common.util.SymbolExpUtil;

public class TypeUtils {
    public static boolean compatibleWithFieldName = false;
    public static boolean compatibleWithJavaBean = false;
    private static ConcurrentMap<String, Class<?>> mappings = new ConcurrentHashMap(16, 0.75f, 1);
    private static Class<?> optionalClass;
    private static boolean optionalClassInited = false;
    private static Method oracleDateMethod;
    private static boolean oracleDateMethodInited = false;
    private static Method oracleTimestampMethod;
    private static boolean oracleTimestampMethodInited = false;
    private static Class<?> pathClass;
    private static boolean pathClass_error = false;
    private static boolean setAccessibleEnable = true;
    private static Class<? extends Annotation> transientClass;
    private static boolean transientClassInited = false;

    static {
        addBaseClassMappings();
    }

    public static String castToString(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public static Byte castToByte(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return Byte.valueOf(((Number) value).byteValue());
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 || Constant.NULL.equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            return Byte.valueOf(Byte.parseByte(strVal));
        }
        throw new JSONException("can not cast to byte, value : " + value);
    }

    public static Character castToChar(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Character) {
            return (Character) value;
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }
            if (strVal.length() == 1) {
                return Character.valueOf(strVal.charAt(0));
            }
            throw new JSONException("can not cast to char, value : " + value);
        }
        throw new JSONException("can not cast to char, value : " + value);
    }

    public static Short castToShort(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return Short.valueOf(((Number) value).shortValue());
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 || Constant.NULL.equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            return Short.valueOf(Short.parseShort(strVal));
        }
        throw new JSONException("can not cast to short, value : " + value);
    }

    public static BigDecimal castToBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger) value);
        }
        String strVal = value.toString();
        if (strVal.length() == 0) {
            return null;
        }
        return new BigDecimal(strVal);
    }

    public static BigInteger castToBigInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }
        if ((value instanceof Float) || (value instanceof Double)) {
            return BigInteger.valueOf(((Number) value).longValue());
        }
        String strVal = value.toString();
        if (strVal.length() == 0 || Constant.NULL.equals(strVal) || "NULL".equals(strVal)) {
            return null;
        }
        return new BigInteger(strVal);
    }

    public static Float castToFloat(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return Float.valueOf(((Number) value).floatValue());
        }
        if (value instanceof String) {
            String strVal = value.toString();
            if (strVal.length() == 0 || Constant.NULL.equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            if (strVal.indexOf(44) != 0) {
                strVal = strVal.replaceAll(",", "");
            }
            return Float.valueOf(Float.parseFloat(strVal));
        }
        throw new JSONException("can not cast to float, value : " + value);
    }

    public static Double castToDouble(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return Double.valueOf(((Number) value).doubleValue());
        }
        if (value instanceof String) {
            String strVal = value.toString();
            if (strVal.length() == 0 || Constant.NULL.equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            if (strVal.indexOf(44) != 0) {
                strVal = strVal.replaceAll(",", "");
            }
            return Double.valueOf(Double.parseDouble(strVal));
        }
        throw new JSONException("can not cast to double, value : " + value);
    }

    public static Date castToDate(Object value) {
        String format;
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return (Date) value;
        }
        if (value instanceof Calendar) {
            return ((Calendar) value).getTime();
        }
        long longValue = -1;
        if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        }
        if (value instanceof String) {
            String strVal = (String) value;
            JSONScanner dateLexer = new JSONScanner(strVal);
            try {
                if (dateLexer.scanISO8601DateIfMatch(false)) {
                    return dateLexer.getCalendar().getTime();
                }
                dateLexer.close();
                if (strVal.startsWith("/Date(") && strVal.endsWith(")/")) {
                    strVal = strVal.substring(6, strVal.length() - 2);
                }
                if (strVal.indexOf(45) != -1) {
                    if (strVal.length() == JSON.DEFFAULT_DATE_FORMAT.length()) {
                        format = JSON.DEFFAULT_DATE_FORMAT;
                    } else if (strVal.length() == 10) {
                        format = "yyyy-MM-dd";
                    } else if (strVal.length() == "yyyy-MM-dd HH:mm:ss".length()) {
                        format = "yyyy-MM-dd HH:mm:ss";
                    } else {
                        format = "yyyy-MM-dd HH:mm:ss.SSS";
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat(format, JSON.defaultLocale);
                    dateFormat.setTimeZone(JSON.defaultTimeZone);
                    try {
                        return dateFormat.parse(strVal);
                    } catch (ParseException e) {
                        throw new JSONException("can not cast to Date, value : " + strVal);
                    }
                } else if (strVal.length() == 0) {
                    return null;
                } else {
                    longValue = Long.parseLong(strVal);
                }
            } finally {
                dateLexer.close();
            }
        }
        if (longValue >= 0) {
            return new Date(longValue);
        }
        Class<?> clazz = value.getClass();
        if ("oracle.sql.TIMESTAMP".equals(clazz.getName())) {
            if (oracleTimestampMethod == null && !oracleTimestampMethodInited) {
                try {
                    oracleTimestampMethod = clazz.getMethod("toJdbc", new Class[0]);
                } catch (NoSuchMethodException e2) {
                } finally {
                    oracleTimestampMethodInited = true;
                }
            }
            try {
                return (Date) oracleTimestampMethod.invoke(value, new Object[0]);
            } catch (Exception e3) {
                throw new JSONException("can not cast oracle.sql.TIMESTAMP to Date", e3);
            }
        } else if ("oracle.sql.DATE".equals(clazz.getName())) {
            if (oracleDateMethod == null && !oracleDateMethodInited) {
                try {
                    oracleDateMethod = clazz.getMethod("toJdbc", new Class[0]);
                } catch (NoSuchMethodException e4) {
                } finally {
                    oracleDateMethodInited = true;
                }
            }
            try {
                return (Date) oracleDateMethod.invoke(value, new Object[0]);
            } catch (Exception e5) {
                throw new JSONException("can not cast oracle.sql.DATE to Date", e5);
            }
        } else {
            throw new JSONException("can not cast to Date, value : " + value);
        }
    }

    public static java.sql.Date castToSqlDate(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof java.sql.Date) {
            return (java.sql.Date) value;
        }
        if (value instanceof Date) {
            return new java.sql.Date(((Date) value).getTime());
        }
        if (value instanceof Calendar) {
            return new java.sql.Date(((Calendar) value).getTimeInMillis());
        }
        long longValue = 0;
        if (value instanceof Number) {
            longValue = ((Number) value).longValue();
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 || Constant.NULL.equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            longValue = Long.parseLong(strVal);
        }
        if (longValue > 0) {
            return new java.sql.Date(longValue);
        }
        throw new JSONException("can not cast to Date, value : " + value);
    }

    public static Timestamp castToTimestamp(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Calendar) {
            return new Timestamp(((Calendar) value).getTimeInMillis());
        }
        if (value instanceof Timestamp) {
            return (Timestamp) value;
        }
        if (value instanceof Date) {
            return new Timestamp(((Date) value).getTime());
        }
        long longValue = 0;
        if (value instanceof Number) {
            longValue = ((Number) value).longValue();
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 || Constant.NULL.equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            longValue = Long.parseLong(strVal);
        }
        if (longValue > 0) {
            return new Timestamp(longValue);
        }
        throw new JSONException("can not cast to Date, value : " + value);
    }

    public static Long castToLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return Long.valueOf(((Number) value).longValue());
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 || Constant.NULL.equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            if (strVal.indexOf(44) != 0) {
                strVal = strVal.replaceAll(",", "");
            }
            try {
                return Long.valueOf(Long.parseLong(strVal));
            } catch (NumberFormatException e) {
                JSONScanner dateParser = new JSONScanner(strVal);
                Calendar calendar = null;
                if (dateParser.scanISO8601DateIfMatch(false)) {
                    calendar = dateParser.getCalendar();
                }
                dateParser.close();
                if (calendar != null) {
                    return Long.valueOf(calendar.getTimeInMillis());
                }
            }
        }
        throw new JSONException("can not cast to long, value : " + value);
    }

    public static Integer castToInt(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return Integer.valueOf(((Number) value).intValue());
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 || Constant.NULL.equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            if (strVal.indexOf(44) != 0) {
                strVal = strVal.replaceAll(",", "");
            }
            return Integer.valueOf(Integer.parseInt(strVal));
        } else if (value instanceof Boolean) {
            return Integer.valueOf(((Boolean) value).booleanValue() ? 1 : 0);
        } else {
            throw new JSONException("can not cast to int, value : " + value);
        }
    }

    public static byte[] castToBytes(Object value) {
        if (value instanceof byte[]) {
            return (byte[]) value;
        }
        if (value instanceof String) {
            return IOUtils.decodeBase64((String) value);
        }
        throw new JSONException("can not cast to int, value : " + value);
    }

    public static Boolean castToBoolean(Object value) {
        boolean z = true;
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Number) {
            if (((Number) value).intValue() != 1) {
                z = false;
            }
            return Boolean.valueOf(z);
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 || Constant.NULL.equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            if ("true".equalsIgnoreCase(strVal) || "1".equals(strVal)) {
                return Boolean.TRUE;
            }
            if ("false".equalsIgnoreCase(strVal) || "0".equals(strVal)) {
                return Boolean.FALSE;
            }
        }
        throw new JSONException("can not cast to boolean, value : " + value);
    }

    public static <T> T castToJavaBean(Object obj, Class<T> clazz) {
        return cast(obj, clazz, ParserConfig.getGlobalInstance());
    }

    public static <T> T cast(Object obj, Class<T> clazz, ParserConfig config) {
        Calendar calendar;
        if (obj == null) {
            return null;
        }
        if (clazz == null) {
            throw new IllegalArgumentException("clazz is null");
        } else if (clazz == obj.getClass()) {
            return obj;
        } else {
            if (!(obj instanceof Map)) {
                if (clazz.isArray()) {
                    if (obj instanceof Collection) {
                        Collection<Object> collection = (Collection) obj;
                        int index = 0;
                        Object array = Array.newInstance(clazz.getComponentType(), collection.size());
                        for (Object item : collection) {
                            Array.set(array, index, cast(item, clazz.getComponentType(), config));
                            index++;
                        }
                        return array;
                    } else if (clazz == byte[].class) {
                        return castToBytes(obj);
                    }
                }
                if (clazz.isAssignableFrom(obj.getClass())) {
                    return obj;
                }
                if (clazz == Boolean.TYPE || clazz == Boolean.class) {
                    return castToBoolean(obj);
                }
                if (clazz == Byte.TYPE || clazz == Byte.class) {
                    return castToByte(obj);
                }
                if (clazz == Short.TYPE || clazz == Short.class) {
                    return castToShort(obj);
                }
                if (clazz == Integer.TYPE || clazz == Integer.class) {
                    return castToInt(obj);
                }
                if (clazz == Long.TYPE || clazz == Long.class) {
                    return castToLong(obj);
                }
                if (clazz == Float.TYPE || clazz == Float.class) {
                    return castToFloat(obj);
                }
                if (clazz == Double.TYPE || clazz == Double.class) {
                    return castToDouble(obj);
                }
                if (clazz == String.class) {
                    return castToString(obj);
                }
                if (clazz == BigDecimal.class) {
                    return castToBigDecimal(obj);
                }
                if (clazz == BigInteger.class) {
                    return castToBigInteger(obj);
                }
                if (clazz == Date.class) {
                    return castToDate(obj);
                }
                if (clazz == java.sql.Date.class) {
                    return castToSqlDate(obj);
                }
                if (clazz == Timestamp.class) {
                    return castToTimestamp(obj);
                }
                if (clazz.isEnum()) {
                    return castToEnum(obj, clazz, config);
                }
                if (Calendar.class.isAssignableFrom(clazz)) {
                    Date date = castToDate(obj);
                    if (clazz == Calendar.class) {
                        calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
                    } else {
                        try {
                            calendar = clazz.newInstance();
                        } catch (Exception e) {
                            throw new JSONException("can not cast to : " + clazz.getName(), e);
                        }
                    }
                    calendar.setTime(date);
                    return calendar;
                }
                if (obj instanceof String) {
                    String strVal = (String) obj;
                    if (strVal.length() == 0 || Constant.NULL.equals(strVal) || "NULL".equals(strVal)) {
                        return null;
                    }
                    if (clazz == Currency.class) {
                        return Currency.getInstance(strVal);
                    }
                }
                throw new JSONException("can not cast to : " + clazz.getName());
            } else if (clazz == Map.class) {
                return obj;
            } else {
                Map map = (Map) obj;
                if (clazz != Object.class || map.containsKey(JSON.DEFAULT_TYPE_KEY)) {
                    return castToJavaBean((Map) obj, clazz, config);
                }
                return obj;
            }
        }
    }

    public static <T> T castToEnum(Object obj, Class<T> clazz, ParserConfig mapping) {
        try {
            if (obj instanceof String) {
                String name = (String) obj;
                if (name.length() == 0) {
                    return null;
                }
                return Enum.valueOf(clazz, name);
            }
            if (obj instanceof Number) {
                int ordinal = ((Number) obj).intValue();
                Object[] values = clazz.getEnumConstants();
                if (ordinal < values.length) {
                    return values[ordinal];
                }
            }
            throw new JSONException("can not cast to : " + clazz.getName());
        } catch (Exception ex) {
            throw new JSONException("can not cast to : " + clazz.getName(), ex);
        }
    }

    public static <T> T cast(Object obj, Type type, ParserConfig mapping) {
        if (obj == null) {
            return null;
        }
        if (type instanceof Class) {
            return cast(obj, (Class) type, mapping);
        }
        if (type instanceof ParameterizedType) {
            return cast(obj, (ParameterizedType) type, mapping);
        }
        if (obj instanceof String) {
            String strVal = (String) obj;
            if (strVal.length() == 0 || Constant.NULL.equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
        }
        if (type instanceof TypeVariable) {
            return obj;
        }
        throw new JSONException("can not cast to : " + type);
    }

    public static <T> T cast(Object obj, ParameterizedType type, ParserConfig mapping) {
        Collection collection;
        Type rawTye = type.getRawType();
        if (rawTye == Set.class || rawTye == HashSet.class || rawTye == TreeSet.class || rawTye == List.class || rawTye == ArrayList.class) {
            Type itemType = type.getActualTypeArguments()[0];
            if (obj instanceof Iterable) {
                if (rawTye == Set.class || rawTye == HashSet.class) {
                    collection = new HashSet();
                } else if (rawTye == TreeSet.class) {
                    collection = new TreeSet();
                } else {
                    collection = new ArrayList();
                }
                for (Object item : (Iterable) obj) {
                    collection.add(cast(item, itemType, mapping));
                }
                return collection;
            }
        }
        if (rawTye == Map.class || rawTye == HashMap.class) {
            Type keyType = type.getActualTypeArguments()[0];
            Type valueType = type.getActualTypeArguments()[1];
            if (obj instanceof Map) {
                Map map = new HashMap();
                for (Map.Entry entry : ((Map) obj).entrySet()) {
                    map.put(cast(entry.getKey(), keyType, mapping), cast(entry.getValue(), valueType, mapping));
                }
                return map;
            }
        }
        if ((obj instanceof String) && ((String) obj).length() == 0) {
            return null;
        }
        if (type.getActualTypeArguments().length == 1 && (type.getActualTypeArguments()[0] instanceof WildcardType)) {
            return cast(obj, rawTye, mapping);
        }
        throw new JSONException("can not cast to : " + type);
    }

    /* JADX WARNING: type inference failed for: r4v0, types: [com.alibaba.fastjson.parser.deserializer.ObjectDeserializer] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> T castToJavaBean(java.util.Map<java.lang.String, java.lang.Object> r17, java.lang.Class<T> r18, com.alibaba.fastjson.parser.ParserConfig r19) {
        /*
            java.lang.Class<java.lang.StackTraceElement> r14 = java.lang.StackTraceElement.class
            r0 = r18
            if (r0 != r14) goto L_0x0040
            java.lang.String r14 = "className"
            r0 = r17
            java.lang.Object r3 = r0.get(r14)     // Catch:{ Exception -> 0x0077 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Exception -> 0x0077 }
            java.lang.String r14 = "methodName"
            r0 = r17
            java.lang.Object r11 = r0.get(r14)     // Catch:{ Exception -> 0x0077 }
            java.lang.String r11 = (java.lang.String) r11     // Catch:{ Exception -> 0x0077 }
            java.lang.String r14 = "fileName"
            r0 = r17
            java.lang.Object r6 = r0.get(r14)     // Catch:{ Exception -> 0x0077 }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ Exception -> 0x0077 }
            java.lang.String r14 = "lineNumber"
            r0 = r17
            java.lang.Object r13 = r0.get(r14)     // Catch:{ Exception -> 0x0077 }
            java.lang.Number r13 = (java.lang.Number) r13     // Catch:{ Exception -> 0x0077 }
            if (r13 != 0) goto L_0x003b
            r9 = 0
        L_0x0035:
            java.lang.StackTraceElement r14 = new java.lang.StackTraceElement     // Catch:{ Exception -> 0x0077 }
            r14.<init>(r3, r11, r6, r9)     // Catch:{ Exception -> 0x0077 }
        L_0x003a:
            return r14
        L_0x003b:
            int r9 = r13.intValue()     // Catch:{ Exception -> 0x0077 }
            goto L_0x0035
        L_0x0040:
            java.lang.String r14 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ Exception -> 0x0077 }
            r0 = r17
            java.lang.Object r7 = r0.get(r14)     // Catch:{ Exception -> 0x0077 }
            boolean r14 = r7 instanceof java.lang.String     // Catch:{ Exception -> 0x0077 }
            if (r14 == 0) goto L_0x0093
            r0 = r7
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0077 }
            r2 = r0
            if (r19 != 0) goto L_0x0054
            com.alibaba.fastjson.parser.ParserConfig r19 = com.alibaba.fastjson.parser.ParserConfig.global     // Catch:{ Exception -> 0x0077 }
        L_0x0054:
            r14 = 0
            r0 = r19
            java.lang.Class r10 = r0.checkAutoType(r2, r14)     // Catch:{ Exception -> 0x0077 }
            if (r10 != 0) goto L_0x0082
            java.lang.ClassNotFoundException r14 = new java.lang.ClassNotFoundException     // Catch:{ Exception -> 0x0077 }
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0077 }
            r15.<init>()     // Catch:{ Exception -> 0x0077 }
            java.lang.StringBuilder r15 = r15.append(r2)     // Catch:{ Exception -> 0x0077 }
            java.lang.String r16 = " not found"
            java.lang.StringBuilder r15 = r15.append(r16)     // Catch:{ Exception -> 0x0077 }
            java.lang.String r15 = r15.toString()     // Catch:{ Exception -> 0x0077 }
            r14.<init>(r15)     // Catch:{ Exception -> 0x0077 }
            throw r14     // Catch:{ Exception -> 0x0077 }
        L_0x0077:
            r5 = move-exception
            com.alibaba.fastjson.JSONException r14 = new com.alibaba.fastjson.JSONException
            java.lang.String r15 = r5.getMessage()
            r14.<init>(r15, r5)
            throw r14
        L_0x0082:
            r0 = r18
            boolean r14 = r10.equals(r0)     // Catch:{ Exception -> 0x0077 }
            if (r14 != 0) goto L_0x0093
            r0 = r17
            r1 = r19
            java.lang.Object r14 = castToJavaBean(r0, r10, r1)     // Catch:{ Exception -> 0x0077 }
            goto L_0x003a
        L_0x0093:
            boolean r14 = r18.isInterface()     // Catch:{ Exception -> 0x0077 }
            if (r14 == 0) goto L_0x00c0
            r0 = r17
            boolean r14 = r0 instanceof com.alibaba.fastjson.JSONObject     // Catch:{ Exception -> 0x0077 }
            if (r14 == 0) goto L_0x00b8
            r0 = r17
            com.alibaba.fastjson.JSONObject r0 = (com.alibaba.fastjson.JSONObject) r0     // Catch:{ Exception -> 0x0077 }
            r12 = r0
        L_0x00a4:
            java.lang.Thread r14 = java.lang.Thread.currentThread()     // Catch:{ Exception -> 0x0077 }
            java.lang.ClassLoader r14 = r14.getContextClassLoader()     // Catch:{ Exception -> 0x0077 }
            r15 = 1
            java.lang.Class[] r15 = new java.lang.Class[r15]     // Catch:{ Exception -> 0x0077 }
            r16 = 0
            r15[r16] = r18     // Catch:{ Exception -> 0x0077 }
            java.lang.Object r14 = java.lang.reflect.Proxy.newProxyInstance(r14, r15, r12)     // Catch:{ Exception -> 0x0077 }
            goto L_0x003a
        L_0x00b8:
            com.alibaba.fastjson.JSONObject r12 = new com.alibaba.fastjson.JSONObject     // Catch:{ Exception -> 0x0077 }
            r0 = r17
            r12.<init>((java.util.Map<java.lang.String, java.lang.Object>) r0)     // Catch:{ Exception -> 0x0077 }
            goto L_0x00a4
        L_0x00c0:
            if (r19 != 0) goto L_0x00c6
            com.alibaba.fastjson.parser.ParserConfig r19 = com.alibaba.fastjson.parser.ParserConfig.getGlobalInstance()     // Catch:{ Exception -> 0x0077 }
        L_0x00c6:
            r8 = 0
            r0 = r19
            r1 = r18
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r4 = r0.getDeserializer((java.lang.reflect.Type) r1)     // Catch:{ Exception -> 0x0077 }
            boolean r14 = r4 instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer     // Catch:{ Exception -> 0x0077 }
            if (r14 == 0) goto L_0x00d7
            r0 = r4
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r0 = (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) r0     // Catch:{ Exception -> 0x0077 }
            r8 = r0
        L_0x00d7:
            if (r8 != 0) goto L_0x00e2
            com.alibaba.fastjson.JSONException r14 = new com.alibaba.fastjson.JSONException     // Catch:{ Exception -> 0x0077 }
            java.lang.String r15 = "can not get javaBeanDeserializer"
            r14.<init>(r15)     // Catch:{ Exception -> 0x0077 }
            throw r14     // Catch:{ Exception -> 0x0077 }
        L_0x00e2:
            r0 = r17
            r1 = r19
            java.lang.Object r14 = r8.createInstance((java.util.Map<java.lang.String, java.lang.Object>) r0, (com.alibaba.fastjson.parser.ParserConfig) r1)     // Catch:{ Exception -> 0x0077 }
            goto L_0x003a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.castToJavaBean(java.util.Map, java.lang.Class, com.alibaba.fastjson.parser.ParserConfig):java.lang.Object");
    }

    private static void addBaseClassMappings() {
        mappings.put("byte", Byte.TYPE);
        mappings.put("short", Short.TYPE);
        mappings.put("int", Integer.TYPE);
        mappings.put("long", Long.TYPE);
        mappings.put("float", Float.TYPE);
        mappings.put("double", Double.TYPE);
        mappings.put("boolean", Boolean.TYPE);
        mappings.put("char", Character.TYPE);
        mappings.put("[byte", byte[].class);
        mappings.put("[short", short[].class);
        mappings.put("[int", int[].class);
        mappings.put("[long", long[].class);
        mappings.put("[float", float[].class);
        mappings.put("[double", double[].class);
        mappings.put("[boolean", boolean[].class);
        mappings.put("[char", char[].class);
        mappings.put("[B", byte[].class);
        mappings.put("[S", short[].class);
        mappings.put("[I", int[].class);
        mappings.put("[J", long[].class);
        mappings.put("[F", float[].class);
        mappings.put("[D", double[].class);
        mappings.put("[C", char[].class);
        mappings.put("[Z", boolean[].class);
        for (Class clazz : new Class[]{Object.class, Cloneable.class, loadClass("java.lang.AutoCloseable"), Exception.class, RuntimeException.class, IllegalAccessError.class, IllegalAccessException.class, IllegalArgumentException.class, IllegalMonitorStateException.class, IllegalStateException.class, IllegalThreadStateException.class, IndexOutOfBoundsException.class, InstantiationError.class, InstantiationException.class, InternalError.class, InterruptedException.class, LinkageError.class, NegativeArraySizeException.class, NoClassDefFoundError.class, NoSuchFieldError.class, NoSuchFieldException.class, NoSuchMethodError.class, NoSuchMethodException.class, NullPointerException.class, NumberFormatException.class, OutOfMemoryError.class, SecurityException.class, StackOverflowError.class, StringIndexOutOfBoundsException.class, TypeNotPresentException.class, VerifyError.class, StackTraceElement.class, HashMap.class, Hashtable.class, TreeMap.class, IdentityHashMap.class, WeakHashMap.class, LinkedHashMap.class, HashSet.class, LinkedHashSet.class, TreeSet.class, TimeUnit.class, ConcurrentHashMap.class, loadClass("java.util.concurrent.ConcurrentSkipListMap"), loadClass("java.util.concurrent.ConcurrentSkipListSet"), AtomicInteger.class, AtomicLong.class, Collections.EMPTY_MAP.getClass(), BitSet.class, Calendar.class, Date.class, Locale.class, UUID.class, Time.class, java.sql.Date.class, Timestamp.class, SimpleDateFormat.class, JSONObject.class, loadClass("java.awt.Rectangle"), loadClass("java.awt.Point"), loadClass("java.awt.Font"), loadClass("java.awt.Color")}) {
            if (clazz != null) {
                mappings.put(clazz.getName(), clazz);
            }
        }
    }

    public static void clearClassMapping() {
        mappings.clear();
        addBaseClassMappings();
    }

    public static Class<?> loadClass(String className) {
        return loadClass(className, (ClassLoader) null);
    }

    public static boolean isPath(Class<?> clazz) {
        if (pathClass == null && !pathClass_error) {
            try {
                pathClass = Class.forName("java.nio.file.Path");
            } catch (Throwable th) {
                pathClass_error = true;
            }
        }
        if (pathClass != null) {
            return pathClass.isAssignableFrom(clazz);
        }
        return false;
    }

    public static Class<?> getClassFromMapping(String className) {
        return (Class) mappings.get(className);
    }

    public static Class<?> loadClass(String className, ClassLoader classLoader) {
        if (className == null || className.length() == 0) {
            return null;
        }
        Class<?> clazz = (Class) mappings.get(className);
        if (clazz != null) {
            return clazz;
        }
        if (className.charAt(0) == '[') {
            return Array.newInstance(loadClass(className.substring(1), classLoader), 0).getClass();
        }
        if (className.startsWith("L") && className.endsWith(SymbolExpUtil.SYMBOL_SEMICOLON)) {
            return loadClass(className.substring(1, className.length() - 1), classLoader);
        }
        if (classLoader != null) {
            try {
                clazz = classLoader.loadClass(className);
                mappings.put(className, clazz);
                return clazz;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (!(contextClassLoader == null || contextClassLoader == classLoader)) {
                Class<?> clazz2 = contextClassLoader.loadClass(className);
                mappings.put(className, clazz2);
                return clazz2;
            }
        } catch (Throwable th) {
        }
        try {
            clazz = Class.forName(className);
            mappings.put(className, clazz);
            return clazz;
        } catch (Throwable th2) {
            return clazz;
        }
    }

    public static SerializeBeanInfo buildBeanInfo(Class<?> beanType, Map<String, String> aliasMap, PropertyNamingStrategy propertyNamingStrategy) {
        int features;
        List<FieldInfo> sortedFieldList;
        JSONType jsonType = (JSONType) beanType.getAnnotation(JSONType.class);
        Map<String, Field> fieldCacheMap = new HashMap<>();
        ParserConfig.parserAllFieldToCache(beanType, fieldCacheMap);
        List<FieldInfo> fieldInfoList = computeGetters(beanType, jsonType, aliasMap, fieldCacheMap, false, propertyNamingStrategy);
        FieldInfo[] fields = new FieldInfo[fieldInfoList.size()];
        fieldInfoList.toArray(fields);
        String[] orders = null;
        String typeName = null;
        if (jsonType != null) {
            orders = jsonType.orders();
            typeName = jsonType.typeName();
            if (typeName.length() == 0) {
                typeName = null;
            }
            features = SerializerFeature.of(jsonType.serialzeFeatures());
        } else {
            features = 0;
        }
        if (orders == null || orders.length == 0) {
            sortedFieldList = new ArrayList<>(fieldInfoList);
            Collections.sort(sortedFieldList);
        } else {
            sortedFieldList = computeGetters(beanType, jsonType, aliasMap, fieldCacheMap, true, propertyNamingStrategy);
        }
        FieldInfo[] sortedFields = new FieldInfo[sortedFieldList.size()];
        sortedFieldList.toArray(sortedFields);
        if (Arrays.equals(sortedFields, fields)) {
            sortedFields = fields;
        }
        return new SerializeBeanInfo(beanType, jsonType, typeName, features, fields, sortedFields);
    }

    public static List<FieldInfo> computeGetters(Class<?> clazz, JSONType jsonType, Map<String, String> aliasMap, Map<String, Field> fieldCacheMap, boolean sorted, PropertyNamingStrategy propertyNamingStrategy) {
        String propertyName;
        String propertyName2;
        String propertyName3;
        char ch;
        String propertyName4;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Method[] methods = clazz.getMethods();
        int length = methods.length;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= length) {
                break;
            }
            Method method = methods[i2];
            String methodName = method.getName();
            int ordinal = 0;
            int serialzeFeatures = 0;
            int parserFeatures = 0;
            String label = null;
            if (!Modifier.isStatic(method.getModifiers()) && !method.getReturnType().equals(Void.TYPE) && method.getParameterTypes().length == 0 && method.getReturnType() != ClassLoader.class && (!method.getName().equals("getMetaClass") || !method.getReturnType().getName().equals("groovy.lang.MetaClass"))) {
                JSONField annotation = (JSONField) method.getAnnotation(JSONField.class);
                if (annotation == null) {
                    annotation = getSuperMethodAnnotation(clazz, method);
                }
                if (annotation != null) {
                    if (annotation.serialize()) {
                        ordinal = annotation.ordinal();
                        serialzeFeatures = SerializerFeature.of(annotation.serialzeFeatures());
                        parserFeatures = Feature.of(annotation.parseFeatures());
                        if (annotation.name().length() != 0) {
                            String propertyName5 = annotation.name();
                            if (aliasMap == null || (propertyName5 = aliasMap.get(propertyName5)) != null) {
                                linkedHashMap.put(propertyName5, new FieldInfo(propertyName5, method, (Field) null, clazz, (Type) null, ordinal, serialzeFeatures, parserFeatures, annotation, (JSONField) null, (String) null));
                            }
                        } else if (annotation.label().length() != 0) {
                            label = annotation.label();
                        }
                    }
                }
                if (methodName.startsWith("get")) {
                    if (methodName.length() >= 4 && !methodName.equals("getClass") && (!methodName.equals("getDeclaringClass") || !clazz.isEnum())) {
                        char c3 = methodName.charAt(3);
                        if (Character.isUpperCase(c3) || c3 > 512) {
                            if (compatibleWithJavaBean) {
                                propertyName4 = decapitalize(methodName.substring(3));
                            } else {
                                propertyName4 = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                            }
                            propertyName3 = getPropertyNameByCompatibleFieldName(fieldCacheMap, methodName, propertyName4, 3);
                        } else if (c3 == '_') {
                            propertyName3 = methodName.substring(4);
                        } else if (c3 == 'f') {
                            propertyName3 = methodName.substring(3);
                        } else if (methodName.length() >= 5 && Character.isUpperCase(methodName.charAt(4))) {
                            propertyName3 = decapitalize(methodName.substring(3));
                        }
                        if (!isJSONTypeIgnore(clazz, propertyName3)) {
                            Field field = ParserConfig.getFieldFromCache(propertyName3, fieldCacheMap);
                            if (field == null && propertyName3.length() > 1 && (ch = propertyName3.charAt(1)) >= 'A' && ch <= 'Z') {
                                field = ParserConfig.getFieldFromCache(decapitalize(methodName.substring(3)), fieldCacheMap);
                            }
                            JSONField fieldAnnotation = null;
                            if (!(field == null || (fieldAnnotation = (JSONField) field.getAnnotation(JSONField.class)) == null)) {
                                if (fieldAnnotation.serialize()) {
                                    ordinal = fieldAnnotation.ordinal();
                                    serialzeFeatures = SerializerFeature.of(fieldAnnotation.serialzeFeatures());
                                    parserFeatures = Feature.of(fieldAnnotation.parseFeatures());
                                    if (fieldAnnotation.name().length() != 0) {
                                        propertyName3 = fieldAnnotation.name();
                                        if (aliasMap != null && (propertyName3 = aliasMap.get(propertyName3)) == null) {
                                        }
                                    }
                                    if (fieldAnnotation.label().length() != 0) {
                                        label = fieldAnnotation.label();
                                    }
                                }
                            }
                            if (aliasMap == null || (propertyName3 = aliasMap.get(propertyName3)) != null) {
                                if (propertyNamingStrategy != null) {
                                    propertyName3 = propertyNamingStrategy.translate(propertyName3);
                                }
                                linkedHashMap.put(propertyName3, new FieldInfo(propertyName3, method, field, clazz, (Type) null, ordinal, serialzeFeatures, parserFeatures, annotation, fieldAnnotation, label));
                            }
                        }
                    }
                }
                if (methodName.startsWith("is") && methodName.length() >= 3 && (method.getReturnType() == Boolean.TYPE || method.getReturnType() == Boolean.class)) {
                    char c2 = methodName.charAt(2);
                    if (Character.isUpperCase(c2)) {
                        if (compatibleWithJavaBean) {
                            propertyName2 = decapitalize(methodName.substring(2));
                        } else {
                            propertyName2 = Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);
                        }
                        propertyName = getPropertyNameByCompatibleFieldName(fieldCacheMap, methodName, propertyName2, 2);
                    } else if (c2 == '_') {
                        propertyName = methodName.substring(3);
                    } else if (c2 == 'f') {
                        propertyName = methodName.substring(2);
                    }
                    Field field2 = ParserConfig.getFieldFromCache(propertyName, fieldCacheMap);
                    if (field2 == null) {
                        field2 = ParserConfig.getFieldFromCache(methodName, fieldCacheMap);
                    }
                    JSONField fieldAnnotation2 = null;
                    if (!(field2 == null || (fieldAnnotation2 = (JSONField) field2.getAnnotation(JSONField.class)) == null)) {
                        if (fieldAnnotation2.serialize()) {
                            ordinal = fieldAnnotation2.ordinal();
                            serialzeFeatures = SerializerFeature.of(fieldAnnotation2.serialzeFeatures());
                            parserFeatures = Feature.of(fieldAnnotation2.parseFeatures());
                            if (fieldAnnotation2.name().length() != 0) {
                                propertyName = fieldAnnotation2.name();
                                if (aliasMap != null && (propertyName = aliasMap.get(propertyName)) == null) {
                                }
                            }
                            if (fieldAnnotation2.label().length() != 0) {
                                label = fieldAnnotation2.label();
                            }
                        }
                    }
                    if (aliasMap == null || (propertyName = aliasMap.get(propertyName)) != null) {
                        if (propertyNamingStrategy != null) {
                            propertyName = propertyNamingStrategy.translate(propertyName);
                        }
                        if (!linkedHashMap.containsKey(propertyName)) {
                            linkedHashMap.put(propertyName, new FieldInfo(propertyName, method, field2, clazz, (Type) null, ordinal, serialzeFeatures, parserFeatures, annotation, fieldAnnotation2, label));
                        }
                    }
                }
            }
            i = i2 + 1;
        }
        for (Field field3 : clazz.getFields()) {
            if (!Modifier.isStatic(field3.getModifiers())) {
                JSONField fieldAnnotation3 = (JSONField) field3.getAnnotation(JSONField.class);
                int ordinal2 = 0;
                int serialzeFeatures2 = 0;
                int parserFeatures2 = 0;
                String propertyName6 = field3.getName();
                String label2 = null;
                if (fieldAnnotation3 != null) {
                    if (fieldAnnotation3.serialize()) {
                        ordinal2 = fieldAnnotation3.ordinal();
                        serialzeFeatures2 = SerializerFeature.of(fieldAnnotation3.serialzeFeatures());
                        parserFeatures2 = Feature.of(fieldAnnotation3.parseFeatures());
                        if (fieldAnnotation3.name().length() != 0) {
                            propertyName6 = fieldAnnotation3.name();
                        }
                        if (fieldAnnotation3.label().length() != 0) {
                            label2 = fieldAnnotation3.label();
                        }
                    }
                }
                if (aliasMap == null || (propertyName6 = aliasMap.get(propertyName6)) != null) {
                    if (propertyNamingStrategy != null) {
                        propertyName6 = propertyNamingStrategy.translate(propertyName6);
                    }
                    if (!linkedHashMap.containsKey(propertyName6)) {
                        linkedHashMap.put(propertyName6, new FieldInfo(propertyName6, (Method) null, field3, clazz, (Type) null, ordinal2, serialzeFeatures2, parserFeatures2, (JSONField) null, fieldAnnotation3, label2));
                    }
                }
            }
        }
        ArrayList arrayList = new ArrayList();
        boolean containsAll = false;
        String[] orders = null;
        JSONType annotation2 = (JSONType) clazz.getAnnotation(JSONType.class);
        if (annotation2 != null) {
            orders = annotation2.orders();
            if (orders == null || orders.length != linkedHashMap.size()) {
                containsAll = false;
            } else {
                containsAll = true;
                int length2 = orders.length;
                int i3 = 0;
                while (true) {
                    if (i3 >= length2) {
                        break;
                    } else if (!linkedHashMap.containsKey(orders[i3])) {
                        containsAll = false;
                        break;
                    } else {
                        i3++;
                    }
                }
            }
        }
        if (containsAll) {
            for (String item : orders) {
                arrayList.add((FieldInfo) linkedHashMap.get(item));
            }
        } else {
            for (FieldInfo fieldInfo : linkedHashMap.values()) {
                arrayList.add(fieldInfo);
            }
            if (sorted) {
                Collections.sort(arrayList);
            }
        }
        return arrayList;
    }

    private static String getPropertyNameByCompatibleFieldName(Map<String, Field> fieldCacheMap, String methodName, String propertyName, int fromIdx) {
        if (!compatibleWithFieldName || fieldCacheMap.containsKey(propertyName)) {
            return propertyName;
        }
        String tempPropertyName = methodName.substring(fromIdx);
        return fieldCacheMap.containsKey(tempPropertyName) ? tempPropertyName : propertyName;
    }

    public static JSONField getSuperMethodAnnotation(Class<?> clazz, Method method) {
        JSONField annotation;
        JSONField annotation2;
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            Class<?>[] types = method.getParameterTypes();
            for (Class<?> interfaceClass : interfaces) {
                for (Method interfaceMethod : interfaceClass.getMethods()) {
                    Class<?>[] interfaceTypes = interfaceMethod.getParameterTypes();
                    if (interfaceTypes.length == types.length && interfaceMethod.getName().equals(method.getName())) {
                        boolean match = true;
                        int i = 0;
                        while (true) {
                            if (i >= types.length) {
                                break;
                            } else if (!interfaceTypes[i].equals(types[i])) {
                                match = false;
                                break;
                            } else {
                                i++;
                            }
                        }
                        if (match && (annotation2 = (JSONField) interfaceMethod.getAnnotation(JSONField.class)) != null) {
                            return annotation2;
                        }
                    }
                }
            }
        }
        Class<? super Object> superclass = clazz.getSuperclass();
        if (superclass != null && Modifier.isAbstract(superclass.getModifiers())) {
            Class<?>[] types2 = method.getParameterTypes();
            for (Method interfaceMethod2 : superclass.getMethods()) {
                Class<?>[] interfaceTypes2 = interfaceMethod2.getParameterTypes();
                if (interfaceTypes2.length == types2.length && interfaceMethod2.getName().equals(method.getName())) {
                    boolean match2 = true;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= types2.length) {
                            break;
                        } else if (!interfaceTypes2[i2].equals(types2[i2])) {
                            match2 = false;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (match2 && (annotation = (JSONField) interfaceMethod2.getAnnotation(JSONField.class)) != null) {
                        return annotation;
                    }
                }
            }
        }
        return null;
    }

    private static boolean isJSONTypeIgnore(Class<?> clazz, String propertyName) {
        JSONType jsonType = (JSONType) clazz.getAnnotation(JSONType.class);
        if (jsonType != null) {
            String[] fields = jsonType.includes();
            if (fields.length > 0) {
                for (String equals : fields) {
                    if (propertyName.equals(equals)) {
                        return false;
                    }
                }
                return true;
            }
            String[] fields2 = jsonType.ignores();
            for (String equals2 : fields2) {
                if (propertyName.equals(equals2)) {
                    return true;
                }
            }
        }
        if (clazz.getSuperclass() == Object.class || clazz.getSuperclass() == null || !isJSONTypeIgnore(clazz.getSuperclass(), propertyName)) {
            return false;
        }
        return true;
    }

    public static boolean isGenericParamType(Type type) {
        Type superType;
        if (type instanceof ParameterizedType) {
            return true;
        }
        if (!(type instanceof Class) || (superType = ((Class) type).getGenericSuperclass()) == Object.class) {
            return false;
        }
        return isGenericParamType(superType);
    }

    public static Type getGenericParamType(Type type) {
        if (!(type instanceof ParameterizedType) && (type instanceof Class)) {
            return getGenericParamType(((Class) type).getGenericSuperclass());
        }
        return type;
    }

    public static Type unwrapOptional(Type type) {
        if (!optionalClassInited) {
            try {
                optionalClass = Class.forName("java.util.Optional");
            } catch (Exception e) {
            } finally {
                optionalClassInited = true;
            }
        }
        if (!(type instanceof ParameterizedType)) {
            return type;
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        if (parameterizedType.getRawType() == optionalClass) {
            return parameterizedType.getActualTypeArguments()[0];
        }
        return type;
    }

    public static Class<?> getClass(Type type) {
        if (type.getClass() == Class.class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        }
        if (type instanceof TypeVariable) {
            return (Class) ((TypeVariable) type).getBounds()[0];
        }
        return Object.class;
    }

    public static Field getField(Class<?> clazz, String fieldName, Field[] declaredFields) {
        for (Field field : declaredFields) {
            if (fieldName.equals(field.getName())) {
                return field;
            }
        }
        Class<? super Object> superclass = clazz.getSuperclass();
        if (superclass == null || superclass == Object.class) {
            return null;
        }
        return getField(superclass, fieldName, superclass.getDeclaredFields());
    }

    public static int getSerializeFeatures(Class<?> clazz) {
        JSONType annotation = (JSONType) clazz.getAnnotation(JSONType.class);
        if (annotation == null) {
            return 0;
        }
        return SerializerFeature.of(annotation.serialzeFeatures());
    }

    public static int getParserFeatures(Class<?> clazz) {
        JSONType annotation = (JSONType) clazz.getAnnotation(JSONType.class);
        if (annotation == null) {
            return 0;
        }
        return Feature.of(annotation.parseFeatures());
    }

    public static String decapitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0))) {
            return name;
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    static void setAccessible(AccessibleObject obj) {
        if (setAccessibleEnable && !obj.isAccessible()) {
            try {
                obj.setAccessible(true);
            } catch (AccessControlException e) {
                setAccessibleEnable = false;
            }
        }
    }

    public static Type getCollectionItemType(Type fieldType) {
        Type itemType = null;
        if (fieldType instanceof ParameterizedType) {
            Type actualTypeArgument = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
            if (actualTypeArgument instanceof WildcardType) {
                Type[] upperBounds = ((WildcardType) actualTypeArgument).getUpperBounds();
                if (upperBounds.length == 1) {
                    actualTypeArgument = upperBounds[0];
                }
            }
            itemType = actualTypeArgument;
        } else if (fieldType instanceof Class) {
            Class<?> clazz = (Class) fieldType;
            if (!clazz.getName().startsWith("java.")) {
                itemType = getCollectionItemType(clazz.getGenericSuperclass());
            }
        }
        if (itemType == null) {
            return Object.class;
        }
        return itemType;
    }

    public static Class<?> getCollectionItemClass(Type fieldType) {
        if (!(fieldType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type actualTypeArgument = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
        if (actualTypeArgument instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) actualTypeArgument).getUpperBounds();
            if (upperBounds.length == 1) {
                actualTypeArgument = upperBounds[0];
            }
        }
        if (actualTypeArgument instanceof Class) {
            Class<?> itemClass = (Class) actualTypeArgument;
            if (Modifier.isPublic(itemClass.getModifiers())) {
                return itemClass;
            }
            throw new JSONException("can not create ASMParser");
        }
        throw new JSONException("can not create ASMParser");
    }

    public static Collection createCollection(Type type) {
        Type itemType;
        Class<?> rawClass = getRawClass(type);
        if (rawClass == AbstractCollection.class || rawClass == Collection.class) {
            return new ArrayList();
        }
        if (rawClass.isAssignableFrom(HashSet.class)) {
            return new HashSet();
        }
        if (rawClass.isAssignableFrom(LinkedHashSet.class)) {
            return new LinkedHashSet();
        }
        if (rawClass.isAssignableFrom(TreeSet.class)) {
            return new TreeSet();
        }
        if (rawClass.isAssignableFrom(ArrayList.class)) {
            return new ArrayList();
        }
        if (rawClass.isAssignableFrom(EnumSet.class)) {
            if (type instanceof ParameterizedType) {
                itemType = ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                itemType = Object.class;
            }
            return EnumSet.noneOf((Class) itemType);
        }
        try {
            return (Collection) rawClass.newInstance();
        } catch (Exception e) {
            throw new JSONException("create instance error, class " + rawClass.getName());
        }
    }

    public static Class<?> getRawClass(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getRawClass(((ParameterizedType) type).getRawType());
        }
        throw new JSONException("TODO");
    }

    public static boolean isProxy(Class<?> clazz) {
        for (Class<?> item : clazz.getInterfaces()) {
            String interfaceName = item.getName();
            if (interfaceName.equals("net.sf.cglib.proxy.Factory") || interfaceName.equals("org.springframework.cglib.proxy.Factory") || interfaceName.equals("javassist.util.proxy.ProxyObject") || interfaceName.equals("org.apache.ibatis.javassist.util.proxy.ProxyObject")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTransient(Method method) {
        boolean z = true;
        if (method == null) {
            return false;
        }
        if (!transientClassInited) {
            try {
                transientClass = Class.forName("java.beans.Transient");
            } catch (Exception e) {
            } finally {
                transientClassInited = z;
            }
        }
        if (transientClass == null) {
            return false;
        }
        if (method.getAnnotation(transientClass) == null) {
            z = false;
        }
        return z;
    }
}
