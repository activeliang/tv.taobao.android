package com.alibaba.fastjson;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class JSONObject extends JSON implements Map<String, Object>, Cloneable, Serializable, InvocationHandler {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final long serialVersionUID = 1;
    private final Map<String, Object> map;

    public JSONObject() {
        this(16, false);
    }

    public JSONObject(Map<String, Object> map2) {
        this.map = map2;
    }

    public JSONObject(boolean ordered) {
        this(16, ordered);
    }

    public JSONObject(int initialCapacity) {
        this(initialCapacity, false);
    }

    public JSONObject(int initialCapacity, boolean ordered) {
        if (ordered) {
            this.map = new LinkedHashMap(initialCapacity);
        } else {
            this.map = new HashMap(initialCapacity);
        }
    }

    public int size() {
        return this.map.size();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    public Object get(Object key) {
        return this.map.get(key);
    }

    public JSONObject getJSONObject(String key) {
        Object value = this.map.get(key);
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }
        if (value instanceof String) {
            return JSON.parseObject((String) value);
        }
        return (JSONObject) toJSON(value);
    }

    public JSONArray getJSONArray(String key) {
        Object value = this.map.get(key);
        if (value instanceof JSONArray) {
            return (JSONArray) value;
        }
        if (value instanceof String) {
            return (JSONArray) JSON.parse((String) value);
        }
        return (JSONArray) toJSON(value);
    }

    public <T> T getObject(String key, Class<T> clazz) {
        return TypeUtils.castToJavaBean(this.map.get(key), clazz);
    }

    public Boolean getBoolean(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        return TypeUtils.castToBoolean(value);
    }

    public byte[] getBytes(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        return TypeUtils.castToBytes(value);
    }

    public boolean getBooleanValue(String key) {
        Object value = get(key);
        if (value == null) {
            return false;
        }
        return TypeUtils.castToBoolean(value).booleanValue();
    }

    public Byte getByte(String key) {
        return TypeUtils.castToByte(get(key));
    }

    public byte getByteValue(String key) {
        Object value = get(key);
        if (value == null) {
            return 0;
        }
        return TypeUtils.castToByte(value).byteValue();
    }

    public Short getShort(String key) {
        return TypeUtils.castToShort(get(key));
    }

    public short getShortValue(String key) {
        Object value = get(key);
        if (value == null) {
            return 0;
        }
        return TypeUtils.castToShort(value).shortValue();
    }

    public Integer getInteger(String key) {
        return TypeUtils.castToInt(get(key));
    }

    public int getIntValue(String key) {
        Object value = get(key);
        if (value == null) {
            return 0;
        }
        return TypeUtils.castToInt(value).intValue();
    }

    public Long getLong(String key) {
        return TypeUtils.castToLong(get(key));
    }

    public long getLongValue(String key) {
        Object value = get(key);
        if (value == null) {
            return 0;
        }
        return TypeUtils.castToLong(value).longValue();
    }

    public Float getFloat(String key) {
        return TypeUtils.castToFloat(get(key));
    }

    public float getFloatValue(String key) {
        Object value = get(key);
        if (value == null) {
            return 0.0f;
        }
        return TypeUtils.castToFloat(value).floatValue();
    }

    public Double getDouble(String key) {
        return TypeUtils.castToDouble(get(key));
    }

    public double getDoubleValue(String key) {
        Object value = get(key);
        if (value == null) {
            return ClientTraceData.b.f47a;
        }
        return TypeUtils.castToDouble(value).doubleValue();
    }

    public BigDecimal getBigDecimal(String key) {
        return TypeUtils.castToBigDecimal(get(key));
    }

    public BigInteger getBigInteger(String key) {
        return TypeUtils.castToBigInteger(get(key));
    }

    public String getString(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public Date getDate(String key) {
        return TypeUtils.castToDate(get(key));
    }

    public java.sql.Date getSqlDate(String key) {
        return TypeUtils.castToSqlDate(get(key));
    }

    public Timestamp getTimestamp(String key) {
        return TypeUtils.castToTimestamp(get(key));
    }

    public Object put(String key, Object value) {
        return this.map.put(key, value);
    }

    public JSONObject fluentPut(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public void putAll(Map<? extends String, ? extends Object> m) {
        this.map.putAll(m);
    }

    public JSONObject fluentPutAll(Map<? extends String, ? extends Object> m) {
        this.map.putAll(m);
        return this;
    }

    public void clear() {
        this.map.clear();
    }

    public JSONObject fluentClear() {
        this.map.clear();
        return this;
    }

    public Object remove(Object key) {
        return this.map.remove(key);
    }

    public JSONObject fluentRemove(Object key) {
        this.map.remove(key);
        return this;
    }

    public Set<String> keySet() {
        return this.map.keySet();
    }

    public Collection<Object> values() {
        return this.map.values();
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return this.map.entrySet();
    }

    public Object clone() {
        return new JSONObject((Map<String, Object>) this.map instanceof LinkedHashMap ? new LinkedHashMap(this.map) : new HashMap(this.map));
    }

    public boolean equals(Object obj) {
        return this.map.equals(obj);
    }

    public int hashCode() {
        return this.map.hashCode();
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 1) {
            if (method.getName().equals("equals")) {
                return Boolean.valueOf(equals(args[0]));
            }
            if (method.getReturnType() != Void.TYPE) {
                throw new JSONException("illegal setter");
            }
            String name = null;
            JSONField annotation = (JSONField) method.getAnnotation(JSONField.class);
            if (!(annotation == null || annotation.name().length() == 0)) {
                name = annotation.name();
            }
            if (name == null) {
                String name2 = method.getName();
                if (!name2.startsWith("set")) {
                    throw new JSONException("illegal setter");
                }
                String name3 = name2.substring(3);
                if (name3.length() == 0) {
                    throw new JSONException("illegal setter");
                }
                name = Character.toLowerCase(name3.charAt(0)) + name3.substring(1);
            }
            this.map.put(name, args[0]);
            return null;
        } else if (parameterTypes.length != 0) {
            throw new UnsupportedOperationException(method.toGenericString());
        } else if (method.getReturnType() == Void.TYPE) {
            throw new JSONException("illegal getter");
        } else {
            String name4 = null;
            JSONField annotation2 = (JSONField) method.getAnnotation(JSONField.class);
            if (!(annotation2 == null || annotation2.name().length() == 0)) {
                name4 = annotation2.name();
            }
            if (name4 == null) {
                String name5 = method.getName();
                if (name5.startsWith("get")) {
                    String name6 = name5.substring(3);
                    if (name6.length() == 0) {
                        throw new JSONException("illegal getter");
                    }
                    name4 = Character.toLowerCase(name6.charAt(0)) + name6.substring(1);
                } else if (name5.startsWith("is")) {
                    String name7 = name5.substring(2);
                    if (name7.length() == 0) {
                        throw new JSONException("illegal getter");
                    }
                    name4 = Character.toLowerCase(name7.charAt(0)) + name7.substring(1);
                } else if (name5.startsWith("hashCode")) {
                    return Integer.valueOf(hashCode());
                } else {
                    if (name5.startsWith("toString")) {
                        return toString();
                    }
                    throw new JSONException("illegal getter");
                }
            }
            return TypeUtils.cast(this.map.get(name4), method.getGenericReturnType(), ParserConfig.getGlobalInstance());
        }
    }
}
