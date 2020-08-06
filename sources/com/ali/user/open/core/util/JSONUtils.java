package com.ali.user.open.core.util;

import android.util.Base64;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {
    public static Integer optInteger(JSONObject object, String name) {
        if (object.has(name)) {
            return Integer.valueOf(object.optInt(name));
        }
        return null;
    }

    public static String optString(JSONObject object, String name) {
        if (object.has(name)) {
            return object.optString(name);
        }
        return null;
    }

    public static Long optLong(JSONObject object, String name) {
        if (object.has(name)) {
            return Long.valueOf(object.optLong(name));
        }
        return null;
    }

    public static Boolean optBoolean(JSONObject object, String name) {
        return Boolean.valueOf(object.has(name) ? object.optBoolean(name) : false);
    }

    public static JSONObject toJsonObject(Map<String, ? extends Object> map) {
        JSONObject jsonObj = new JSONObject();
        try {
            for (Map.Entry<String, ? extends Object> entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    if (value instanceof Map) {
                        jsonObj.put(entry.getKey(), toJsonObject((Map) value));
                    } else if (value instanceof List) {
                        jsonObj.put(entry.getKey(), toJsonArray((List<Object>) (List) value));
                    } else if (value.getClass().isArray()) {
                        jsonObj.put(entry.getKey(), toJsonArray((Object[]) (Object[]) value));
                    } else {
                        jsonObj.put(entry.getKey(), value);
                    }
                }
            }
            return jsonObj;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONArray toJsonArray(Object[] objects) {
        JSONArray jsonArray = new JSONArray();
        for (Object obj : objects) {
            if (obj instanceof Map) {
                jsonArray.put(toJsonObject((Map) obj));
            } else {
                jsonArray.put(obj);
            }
        }
        return jsonArray;
    }

    public static JSONArray toJsonArray(List<Object> list) {
        JSONArray jsonArray = new JSONArray();
        for (Object obj : list) {
            if (obj instanceof Map) {
                jsonArray.put(toJsonObject((Map) obj));
            } else {
                jsonArray.put(obj);
            }
        }
        return jsonArray;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<>();
        if (object != null) {
            Iterator it = object.keys();
            while (it.hasNext()) {
                String key = it.next();
                Object value = object.opt(key);
                if (value instanceof JSONObject) {
                    map.put(key, toMap((JSONObject) value));
                } else if (value instanceof JSONArray) {
                    map.put(key, toList((JSONArray) value));
                } else {
                    map.put(key, value);
                }
            }
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        if (array == null) {
            return Collections.emptyList();
        }
        List<Object> ret = new ArrayList<>(array.length());
        int length = array.length();
        for (int i = 0; i < length; i++) {
            Object value = array.get(i);
            if (value instanceof JSONObject) {
                ret.add(toMap((JSONObject) value));
            } else if (value instanceof JSONArray) {
                ret.add(toList((JSONArray) value));
            } else {
                ret.add(array.get(i));
            }
        }
        return ret;
    }

    public static <T> T parseStringValue(String strValue, Class<T> clazz) {
        if (strValue == null || clazz == null) {
            return null;
        }
        if (String.class.equals(clazz)) {
            return strValue;
        }
        if (Short.TYPE.equals(clazz) || Short.class.equals(clazz)) {
            return Short.valueOf(strValue);
        }
        if (Integer.TYPE.equals(clazz) || Integer.class.equals(clazz)) {
            return Integer.valueOf(strValue);
        }
        if (Long.TYPE.equals(clazz) || Long.class.equals(clazz)) {
            return Long.valueOf(strValue);
        }
        if (Boolean.TYPE.equals(clazz) || Boolean.class.equals(clazz)) {
            return Boolean.valueOf(strValue);
        }
        if (Float.TYPE.equals(clazz) || Float.class.equals(clazz)) {
            return Float.valueOf(strValue);
        }
        if (Double.TYPE.equals(clazz) || Double.class.equals(clazz)) {
            return Double.valueOf(strValue);
        }
        if (Byte.TYPE.equals(clazz) || Byte.class.equals(clazz)) {
            return Byte.valueOf(strValue);
        }
        if (Character.TYPE.equals(clazz) || Character.class.equals(clazz)) {
            return Character.valueOf(strValue.charAt(0));
        }
        if (Date.class.isAssignableFrom(clazz)) {
            try {
                return new SimpleDateFormat("yyyyMMddHHmmssSSSZ", Locale.US).parse(strValue);
            } catch (ParseException e) {
                throw new RuntimeException("Parse Date error", e);
            }
        } else {
            char c = strValue.charAt(0);
            if (clazz.isArray()) {
                Class<?> componentType = clazz.getComponentType();
                if (c == '[') {
                    try {
                        return toPOJOArray(new JSONArray(strValue), componentType);
                    } catch (Exception e2) {
                        throw new RuntimeException(e2);
                    }
                } else if (String.class.equals(componentType)) {
                    return strValue.split(",");
                } else {
                    if (Character.TYPE.equals(componentType)) {
                        return strValue.toCharArray();
                    }
                    if (Character.class.equals(componentType)) {
                        char[] tmp = strValue.toCharArray();
                        Character[] result = new Character[tmp.length];
                        for (int i = 0; i < result.length; i++) {
                            result[i] = Character.valueOf(tmp[i]);
                        }
                        return result;
                    } else if (Byte.TYPE.equals(componentType)) {
                        return Base64.decode(strValue, 0);
                    } else {
                        if (!Byte.class.equals(componentType)) {
                            return null;
                        }
                        byte[] tmp2 = Base64.decode(strValue, 0);
                        Byte[] result2 = new Byte[tmp2.length];
                        for (int i2 = 0; i2 < result2.length; i2++) {
                            result2[i2] = Byte.valueOf(tmp2[i2]);
                        }
                        return result2;
                    }
                }
            } else if (c == '{') {
                try {
                    JSONObject json = new JSONObject(strValue);
                    if (Map.class.isAssignableFrom(clazz)) {
                        return toMap(json);
                    }
                    return toPOJO(json, clazz);
                } catch (Exception e3) {
                    throw new RuntimeException(e3);
                }
            } else if (!clazz.isAssignableFrom(String.class)) {
                return null;
            } else {
                return strValue;
            }
        }
    }

    public static <T> T toPOJO(JSONObject jsonObject, Class<T> type) {
        Object obj;
        if (jsonObject == null || type == null || type == Void.TYPE) {
            return null;
        }
        try {
            T result = type.newInstance();
            for (Field field : type.getFields()) {
                Class type2 = field.getType();
                String name = field.getName();
                if (jsonObject.has(name)) {
                    if (!type2.isPrimitive()) {
                        if (type2 == String.class) {
                            obj = jsonObject.getString(name);
                        } else if (type2 == Boolean.class || type2 == Integer.class || type2 == Short.class || type2 == Long.class || type2 == Double.class) {
                            obj = jsonObject.get(name);
                        } else if (type2.isArray()) {
                            obj = toPOJOArray(jsonObject.getJSONArray(name), type2.getComponentType());
                        } else if (Map.class.isAssignableFrom(type2)) {
                            obj = toMap(jsonObject.getJSONObject(name));
                        } else {
                            obj = toPOJO(jsonObject.getJSONObject(name), type2);
                        }
                        field.set(result, obj);
                    } else if (type2 == Boolean.TYPE) {
                        field.setBoolean(result, jsonObject.getBoolean(name));
                    } else if (type2 == Byte.TYPE) {
                        field.setByte(result, (byte) jsonObject.getInt(name));
                    } else if (type2 == Character.TYPE) {
                        String tmp = jsonObject.getString(name);
                        field.setChar(result, (tmp == null || tmp.length() == 0) ? 0 : tmp.charAt(0));
                    } else if (type2 == Short.TYPE) {
                        field.setShort(result, (short) jsonObject.getInt(name));
                    } else if (type2 == Integer.TYPE) {
                        field.setInt(result, jsonObject.getInt(name));
                    } else if (type2 == Long.TYPE) {
                        field.setLong(result, jsonObject.getLong(name));
                    } else if (type2 == Float.TYPE) {
                        field.setFloat(result, (float) jsonObject.getDouble(name));
                    } else if (type2 == Double.TYPE) {
                        field.setDouble(result, jsonObject.getDouble(name));
                    }
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX WARNING: type inference failed for: r11v0, types: [java.lang.Class, java.lang.Class<T>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> T[] toPOJOArray(org.json.JSONArray r10, java.lang.Class<T> r11) {
        /*
            r6 = 0
            if (r10 == 0) goto L_0x0009
            if (r11 == 0) goto L_0x0009
            java.lang.Class r5 = java.lang.Void.TYPE
            if (r11 != r5) goto L_0x000b
        L_0x0009:
            r3 = 0
        L_0x000a:
            return r3
        L_0x000b:
            int r5 = r10.length()
            java.lang.Object r3 = java.lang.reflect.Array.newInstance(r11, r5)
            r1 = 0
        L_0x0014:
            int r5 = r10.length()     // Catch:{ JSONException -> 0x0080 }
            if (r1 >= r5) goto L_0x00f2
            boolean r5 = r11.isPrimitive()     // Catch:{ JSONException -> 0x0080 }
            if (r5 != 0) goto L_0x0074
            java.lang.Class<java.lang.String> r5 = java.lang.String.class
            if (r11 != r5) goto L_0x002e
            java.lang.String r2 = r10.getString(r1)     // Catch:{ JSONException -> 0x0080 }
        L_0x0028:
            java.lang.reflect.Array.set(r3, r1, r2)     // Catch:{ JSONException -> 0x0080 }
        L_0x002b:
            int r1 = r1 + 1
            goto L_0x0014
        L_0x002e:
            java.lang.Class<java.lang.Boolean> r5 = java.lang.Boolean.class
            if (r11 == r5) goto L_0x0042
            java.lang.Class<java.lang.Integer> r5 = java.lang.Integer.class
            if (r11 == r5) goto L_0x0042
            java.lang.Class<java.lang.Short> r5 = java.lang.Short.class
            if (r11 == r5) goto L_0x0042
            java.lang.Class<java.lang.Long> r5 = java.lang.Long.class
            if (r11 == r5) goto L_0x0042
            java.lang.Class<java.lang.Double> r5 = java.lang.Double.class
            if (r11 != r5) goto L_0x0047
        L_0x0042:
            java.lang.Object r2 = r10.get(r1)     // Catch:{ JSONException -> 0x0080 }
            goto L_0x0028
        L_0x0047:
            boolean r5 = r11.isArray()     // Catch:{ JSONException -> 0x0080 }
            if (r5 == 0) goto L_0x005a
            org.json.JSONArray r5 = r10.getJSONArray(r1)     // Catch:{ JSONException -> 0x0080 }
            java.lang.Class r7 = r11.getComponentType()     // Catch:{ JSONException -> 0x0080 }
            java.lang.Object[] r2 = toPOJOArray(r5, r7)     // Catch:{ JSONException -> 0x0080 }
            goto L_0x0028
        L_0x005a:
            java.lang.Class<java.util.Map> r5 = java.util.Map.class
            boolean r5 = r5.isAssignableFrom(r11)     // Catch:{ JSONException -> 0x0080 }
            if (r5 == 0) goto L_0x006b
            org.json.JSONObject r5 = r10.getJSONObject(r1)     // Catch:{ JSONException -> 0x0080 }
            java.util.Map r2 = toMap(r5)     // Catch:{ JSONException -> 0x0080 }
            goto L_0x0028
        L_0x006b:
            org.json.JSONObject r5 = r10.getJSONObject(r1)     // Catch:{ JSONException -> 0x0080 }
            java.lang.Object r2 = toPOJO(r5, r11)     // Catch:{ JSONException -> 0x0080 }
            goto L_0x0028
        L_0x0074:
            java.lang.Class r5 = java.lang.Boolean.TYPE     // Catch:{ JSONException -> 0x0080 }
            if (r11 != r5) goto L_0x0087
            boolean r5 = r10.getBoolean(r1)     // Catch:{ JSONException -> 0x0080 }
            java.lang.reflect.Array.setBoolean(r3, r1, r5)     // Catch:{ JSONException -> 0x0080 }
            goto L_0x002b
        L_0x0080:
            r0 = move-exception
            java.lang.RuntimeException r5 = new java.lang.RuntimeException
            r5.<init>(r0)
            throw r5
        L_0x0087:
            java.lang.Class r5 = java.lang.Byte.TYPE     // Catch:{ JSONException -> 0x0080 }
            if (r11 != r5) goto L_0x0094
            int r5 = r10.getInt(r1)     // Catch:{ JSONException -> 0x0080 }
            byte r5 = (byte) r5     // Catch:{ JSONException -> 0x0080 }
            java.lang.reflect.Array.setByte(r3, r1, r5)     // Catch:{ JSONException -> 0x0080 }
            goto L_0x002b
        L_0x0094:
            java.lang.Class r5 = java.lang.Character.TYPE     // Catch:{ JSONException -> 0x0080 }
            if (r11 != r5) goto L_0x00af
            java.lang.String r4 = r10.getString(r1)     // Catch:{ JSONException -> 0x0080 }
            if (r4 == 0) goto L_0x00a4
            int r5 = r4.length()     // Catch:{ JSONException -> 0x0080 }
            if (r5 != 0) goto L_0x00a9
        L_0x00a4:
            r5 = r6
        L_0x00a5:
            java.lang.reflect.Array.setChar(r3, r1, r5)     // Catch:{ JSONException -> 0x0080 }
            goto L_0x002b
        L_0x00a9:
            r5 = 0
            char r5 = r4.charAt(r5)     // Catch:{ JSONException -> 0x0080 }
            goto L_0x00a5
        L_0x00af:
            java.lang.Class r5 = java.lang.Short.TYPE     // Catch:{ JSONException -> 0x0080 }
            if (r11 != r5) goto L_0x00bd
            int r5 = r10.getInt(r1)     // Catch:{ JSONException -> 0x0080 }
            short r5 = (short) r5     // Catch:{ JSONException -> 0x0080 }
            java.lang.reflect.Array.setShort(r3, r1, r5)     // Catch:{ JSONException -> 0x0080 }
            goto L_0x002b
        L_0x00bd:
            java.lang.Class r5 = java.lang.Integer.TYPE     // Catch:{ JSONException -> 0x0080 }
            if (r11 != r5) goto L_0x00ca
            int r5 = r10.getInt(r1)     // Catch:{ JSONException -> 0x0080 }
            java.lang.reflect.Array.setInt(r3, r1, r5)     // Catch:{ JSONException -> 0x0080 }
            goto L_0x002b
        L_0x00ca:
            java.lang.Class r5 = java.lang.Long.TYPE     // Catch:{ JSONException -> 0x0080 }
            if (r11 != r5) goto L_0x00d7
            long r8 = r10.getLong(r1)     // Catch:{ JSONException -> 0x0080 }
            java.lang.reflect.Array.setLong(r3, r1, r8)     // Catch:{ JSONException -> 0x0080 }
            goto L_0x002b
        L_0x00d7:
            java.lang.Class r5 = java.lang.Float.TYPE     // Catch:{ JSONException -> 0x0080 }
            if (r11 != r5) goto L_0x00e5
            double r8 = r10.getDouble(r1)     // Catch:{ JSONException -> 0x0080 }
            float r5 = (float) r8     // Catch:{ JSONException -> 0x0080 }
            java.lang.reflect.Array.setFloat(r3, r1, r5)     // Catch:{ JSONException -> 0x0080 }
            goto L_0x002b
        L_0x00e5:
            java.lang.Class r5 = java.lang.Double.TYPE     // Catch:{ JSONException -> 0x0080 }
            if (r11 != r5) goto L_0x002b
            double r8 = r10.getDouble(r1)     // Catch:{ JSONException -> 0x0080 }
            java.lang.reflect.Array.setDouble(r3, r1, r8)     // Catch:{ JSONException -> 0x0080 }
            goto L_0x002b
        L_0x00f2:
            java.lang.Object[] r3 = (java.lang.Object[]) r3
            java.lang.Object[] r3 = (java.lang.Object[]) r3
            goto L_0x000a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.open.core.util.JSONUtils.toPOJOArray(org.json.JSONArray, java.lang.Class):java.lang.Object[]");
    }
}
