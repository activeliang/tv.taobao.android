package com.alibaba.fastjson;

import android.taobao.windvane.util.WVNativeCallbackUtil;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.FieldSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.IOUtils;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
import mtopsdk.common.util.SymbolExpUtil;

public class JSONPath implements JSONAware {
    private static int CACHE_SIZE = 1024;
    private static ConcurrentMap<String, JSONPath> pathCache = new ConcurrentHashMap(128, 0.75f, 1);
    private ParserConfig parserConfig;
    private final String path;
    private Segement[] segments;
    private SerializeConfig serializeConfig;

    interface Filter {
        boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3);
    }

    enum Operator {
        EQ,
        NE,
        GT,
        GE,
        LT,
        LE,
        LIKE,
        NOT_LIKE,
        RLIKE,
        NOT_RLIKE,
        IN,
        NOT_IN,
        BETWEEN,
        NOT_BETWEEN
    }

    interface Segement {
        Object eval(JSONPath jSONPath, Object obj, Object obj2);
    }

    public JSONPath(String path2) {
        this(path2, SerializeConfig.getGlobalInstance(), ParserConfig.getGlobalInstance());
    }

    public JSONPath(String path2, SerializeConfig serializeConfig2, ParserConfig parserConfig2) {
        if (path2 == null || path2.length() == 0) {
            throw new JSONPathException("json-path can not be null or empty");
        }
        this.path = path2;
        this.serializeConfig = serializeConfig2;
        this.parserConfig = parserConfig2;
    }

    /* access modifiers changed from: protected */
    public void init() {
        if (this.segments == null) {
            if ("*".equals(this.path)) {
                this.segments = new Segement[]{WildCardSegement.instance};
                return;
            }
            this.segments = new JSONPathParser(this.path).explain();
        }
    }

    public Object eval(Object rootObject) {
        if (rootObject == null) {
            return null;
        }
        init();
        Object currentObject = rootObject;
        for (Segement eval : this.segments) {
            currentObject = eval.eval(this, rootObject, currentObject);
        }
        return currentObject;
    }

    public boolean contains(Object rootObject) {
        if (rootObject == null) {
            return false;
        }
        init();
        Object currentObject = rootObject;
        for (Segement eval : this.segments) {
            currentObject = eval.eval(this, rootObject, currentObject);
            if (currentObject == null) {
                return false;
            }
        }
        return true;
    }

    public boolean containsValue(Object rootObject, Object value) {
        Object currentObject = eval(rootObject);
        if (currentObject == value) {
            return true;
        }
        if (currentObject == null) {
            return false;
        }
        if (!(currentObject instanceof Iterable)) {
            return eq(currentObject, value);
        }
        for (Object item : (Iterable) currentObject) {
            if (eq(item, value)) {
                return true;
            }
        }
        return false;
    }

    public int size(Object rootObject) {
        if (rootObject == null) {
            return -1;
        }
        init();
        Object currentObject = rootObject;
        for (Segement eval : this.segments) {
            currentObject = eval.eval(this, rootObject, currentObject);
        }
        return evalSize(currentObject);
    }

    public void arrayAdd(Object rootObject, Object... values) {
        if (values != null && values.length != 0 && rootObject != null) {
            init();
            Object currentObject = rootObject;
            Object parentObject = null;
            for (int i = 0; i < this.segments.length; i++) {
                if (i == this.segments.length - 1) {
                    parentObject = currentObject;
                }
                currentObject = this.segments[i].eval(this, rootObject, currentObject);
            }
            Object result = currentObject;
            if (result == null) {
                throw new JSONPathException("value not found in path " + this.path);
            } else if (result instanceof Collection) {
                Collection collection = (Collection) result;
                for (Object value : values) {
                    collection.add(value);
                }
            } else {
                Class<?> resultClass = result.getClass();
                if (resultClass.isArray()) {
                    int length = Array.getLength(result);
                    Object descArray = Array.newInstance(resultClass.getComponentType(), values.length + length);
                    System.arraycopy(result, 0, descArray, 0, length);
                    for (int i2 = 0; i2 < values.length; i2++) {
                        Array.set(descArray, length + i2, values[i2]);
                    }
                    Object newResult = descArray;
                    Segement lastSegement = this.segments[this.segments.length - 1];
                    if (lastSegement instanceof PropertySegement) {
                        ((PropertySegement) lastSegement).setValue(this, parentObject, newResult);
                    } else if (lastSegement instanceof ArrayAccessSegement) {
                        ((ArrayAccessSegement) lastSegement).setValue(this, parentObject, newResult);
                    } else {
                        throw new UnsupportedOperationException();
                    }
                } else {
                    throw new JSONException("unsupported array put operation. " + resultClass);
                }
            }
        }
    }

    public boolean remove(Object rootObject) {
        if (rootObject == null) {
            return false;
        }
        init();
        Object currentObject = rootObject;
        Object parentObject = null;
        int i = 0;
        while (true) {
            if (i < this.segments.length) {
                if (i != this.segments.length - 1) {
                    currentObject = this.segments[i].eval(this, rootObject, currentObject);
                    if (currentObject == null) {
                        break;
                    }
                    i++;
                } else {
                    parentObject = currentObject;
                    break;
                }
            } else {
                break;
            }
        }
        if (parentObject == null) {
            return false;
        }
        Segement lastSegement = this.segments[this.segments.length - 1];
        if (lastSegement instanceof PropertySegement) {
            return ((PropertySegement) lastSegement).remove(this, parentObject);
        }
        if (lastSegement instanceof ArrayAccessSegement) {
            return ((ArrayAccessSegement) lastSegement).remove(this, parentObject);
        }
        throw new UnsupportedOperationException();
    }

    public boolean set(Object rootObject, Object value) {
        return set(rootObject, value, true);
    }

    public boolean set(Object rootObject, Object value, boolean p) {
        Object currentObject;
        Object newObj;
        if (rootObject == null) {
            return false;
        }
        init();
        Object currentObject2 = rootObject;
        Object parentObject = null;
        for (int i = 0; i < this.segments.length; i++) {
            parentObject = currentObject2;
            Segement segment = this.segments[i];
            currentObject2 = segment.eval(this, rootObject, currentObject2);
            if (currentObject2 == null) {
                Segement nextSegement = null;
                if (i < this.segments.length - 1) {
                    nextSegement = this.segments[i + 1];
                }
                if (nextSegement instanceof PropertySegement) {
                    JavaBeanDeserializer beanDeserializer = null;
                    Class<?> fieldClass = null;
                    if (segment instanceof PropertySegement) {
                        String propertyName = ((PropertySegement) segment).propertyName;
                        JavaBeanDeserializer parentBeanDeserializer = getJavaBeanDeserializer(parentObject.getClass());
                        if (parentBeanDeserializer != null) {
                            fieldClass = parentBeanDeserializer.getFieldDeserializer(propertyName).fieldInfo.fieldClass;
                            beanDeserializer = getJavaBeanDeserializer(fieldClass);
                        }
                    }
                    if (beanDeserializer == null) {
                        newObj = new JSONObject();
                    } else if (beanDeserializer.beanInfo.defaultConstructor == null) {
                        return false;
                    } else {
                        newObj = beanDeserializer.createInstance((DefaultJSONParser) null, (Type) fieldClass);
                    }
                    currentObject = newObj;
                } else if (nextSegement instanceof ArrayAccessSegement) {
                    currentObject = new JSONArray();
                } else {
                    currentObject = null;
                }
                if (currentObject != null) {
                    if (!(segment instanceof PropertySegement)) {
                        if (!(segment instanceof ArrayAccessSegement)) {
                            break;
                        }
                        ((ArrayAccessSegement) segment).setValue(this, parentObject, currentObject);
                        currentObject2 = currentObject;
                    } else {
                        ((PropertySegement) segment).setValue(this, parentObject, currentObject);
                        currentObject2 = currentObject;
                    }
                } else {
                    break;
                }
            }
        }
        if (parentObject == null) {
            return false;
        }
        Segement lastSegement = this.segments[this.segments.length - 1];
        if (lastSegement instanceof PropertySegement) {
            ((PropertySegement) lastSegement).setValue(this, parentObject, value);
            return true;
        } else if (lastSegement instanceof ArrayAccessSegement) {
            return ((ArrayAccessSegement) lastSegement).setValue(this, parentObject, value);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static Object eval(Object rootObject, String path2) {
        return compile(path2).eval(rootObject);
    }

    public static int size(Object rootObject, String path2) {
        JSONPath jsonpath = compile(path2);
        return jsonpath.evalSize(jsonpath.eval(rootObject));
    }

    public static boolean contains(Object rootObject, String path2) {
        if (rootObject == null) {
            return false;
        }
        return compile(path2).contains(rootObject);
    }

    public static boolean containsValue(Object rootObject, String path2, Object value) {
        return compile(path2).containsValue(rootObject, value);
    }

    public static void arrayAdd(Object rootObject, String path2, Object... values) {
        compile(path2).arrayAdd(rootObject, values);
    }

    public static boolean set(Object rootObject, String path2, Object value) {
        return compile(path2).set(rootObject, value);
    }

    public static boolean remove(Object root, String path2) {
        return compile(path2).remove(root);
    }

    public static JSONPath compile(String path2) {
        if (path2 == null) {
            throw new JSONPathException("jsonpath can not be null");
        }
        JSONPath jsonpath = (JSONPath) pathCache.get(path2);
        if (jsonpath != null) {
            return jsonpath;
        }
        JSONPath jsonpath2 = new JSONPath(path2);
        if (pathCache.size() >= CACHE_SIZE) {
            return jsonpath2;
        }
        pathCache.putIfAbsent(path2, jsonpath2);
        return (JSONPath) pathCache.get(path2);
    }

    public static Object read(String json, String path2) {
        return compile(path2).eval(JSON.parse(json));
    }

    public static Map<String, Object> paths(Object javaObject) {
        return paths(javaObject, SerializeConfig.globalInstance);
    }

    public static Map<String, Object> paths(Object javaObject, SerializeConfig config) {
        Map<Object, String> values = new IdentityHashMap<>();
        paths(values, WVNativeCallbackUtil.SEPERATER, javaObject, config);
        Map<String, Object> paths = new HashMap<>();
        for (Map.Entry<Object, String> entry : values.entrySet()) {
            paths.put(entry.getValue(), entry.getKey());
        }
        return paths;
    }

    private static void paths(Map<Object, String> paths, String parent, Object javaObject, SerializeConfig config) {
        if (javaObject != null && !paths.containsKey(javaObject)) {
            paths.put(javaObject, parent);
            if (javaObject instanceof Map) {
                for (Map.Entry entry : ((Map) javaObject).entrySet()) {
                    Object key = entry.getKey();
                    if (key instanceof String) {
                        paths(paths, parent.equals(WVNativeCallbackUtil.SEPERATER) ? WVNativeCallbackUtil.SEPERATER + key : parent + WVNativeCallbackUtil.SEPERATER + key, entry.getValue(), config);
                    }
                }
            } else if (javaObject instanceof Collection) {
                int i = 0;
                for (Object item : (Collection) javaObject) {
                    paths(paths, parent.equals(WVNativeCallbackUtil.SEPERATER) ? WVNativeCallbackUtil.SEPERATER + i : parent + WVNativeCallbackUtil.SEPERATER + i, item, config);
                    i++;
                }
            } else {
                Class<?> clazz = javaObject.getClass();
                if (clazz.isArray()) {
                    int len = Array.getLength(javaObject);
                    for (int i2 = 0; i2 < len; i2++) {
                        paths(paths, parent.equals(WVNativeCallbackUtil.SEPERATER) ? WVNativeCallbackUtil.SEPERATER + i2 : parent + WVNativeCallbackUtil.SEPERATER + i2, Array.get(javaObject, i2), config);
                    }
                } else if (!ParserConfig.isPrimitive2(clazz) && !clazz.isEnum()) {
                    ObjectSerializer serializer = config.getObjectWriter(clazz);
                    if (serializer instanceof JavaBeanSerializer) {
                        try {
                            for (Map.Entry<String, Object> entry2 : ((JavaBeanSerializer) serializer).getFieldValuesMap(javaObject).entrySet()) {
                                String key2 = entry2.getKey();
                                if (key2 instanceof String) {
                                    paths(paths, parent.equals(WVNativeCallbackUtil.SEPERATER) ? WVNativeCallbackUtil.SEPERATER + key2 : parent + WVNativeCallbackUtil.SEPERATER + key2, entry2.getValue(), config);
                                }
                            }
                        } catch (Exception e) {
                            throw new JSONException("toJSON error", e);
                        }
                    }
                }
            }
        }
    }

    public String getPath() {
        return this.path;
    }

    static class JSONPathParser {
        private char ch;
        private int level;
        private final String path;
        private int pos;

        public JSONPathParser(String path2) {
            this.path = path2;
            next();
        }

        /* access modifiers changed from: package-private */
        public void next() {
            String str = this.path;
            int i = this.pos;
            this.pos = i + 1;
            this.ch = str.charAt(i);
        }

        /* access modifiers changed from: package-private */
        public boolean isEOF() {
            return this.pos >= this.path.length();
        }

        /* access modifiers changed from: package-private */
        public Segement readSegement() {
            if (this.level == 0 && this.path.length() == 1) {
                if (isDigitFirst(this.ch)) {
                    return new ArrayAccessSegement(this.ch - '0');
                }
                if ((this.ch >= 'a' && this.ch <= 'z') || (this.ch >= 'A' && this.ch <= 'Z')) {
                    return new PropertySegement(Character.toString(this.ch), false);
                }
            }
            while (!isEOF()) {
                skipWhitespace();
                if (this.ch == '$') {
                    next();
                } else if (this.ch == '.' || this.ch == '/') {
                    int c0 = this.ch;
                    boolean deep = false;
                    next();
                    if (c0 == 46 && this.ch == '.') {
                        next();
                        deep = true;
                    }
                    if (this.ch == '*') {
                        if (!isEOF()) {
                            next();
                        }
                        return WildCardSegement.instance;
                    } else if (isDigitFirst(this.ch)) {
                        return parseArrayAccess(false);
                    } else {
                        String propertyName = readName();
                        if (this.ch != '(') {
                            return new PropertySegement(propertyName, deep);
                        }
                        next();
                        if (this.ch == ')') {
                            if (!isEOF()) {
                                next();
                            }
                            if ("size".equals(propertyName)) {
                                return SizeSegement.instance;
                            }
                            throw new UnsupportedOperationException();
                        }
                        throw new UnsupportedOperationException();
                    }
                } else if (this.ch == '[') {
                    return parseArrayAccess(true);
                } else {
                    if (this.level == 0) {
                        return new PropertySegement(readName(), false);
                    }
                    throw new UnsupportedOperationException();
                }
            }
            return null;
        }

        public final void skipWhitespace() {
            while (this.ch <= ' ') {
                if (this.ch == ' ' || this.ch == 13 || this.ch == 10 || this.ch == 9 || this.ch == 12 || this.ch == 8) {
                    next();
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public Segement parseArrayAccess(boolean acceptBracket) {
            int end;
            if (acceptBracket) {
                accept('[');
            }
            boolean predicateFlag = false;
            if (this.ch == '?') {
                next();
                accept('(');
                if (this.ch == '@') {
                    next();
                    accept('.');
                }
                predicateFlag = true;
            }
            if (predicateFlag || IOUtils.firstIdentifier(this.ch)) {
                String propertyName = readName();
                skipWhitespace();
                if (predicateFlag && this.ch == ')') {
                    next();
                    if (acceptBracket) {
                        accept(']');
                    }
                    return new FilterSegement(new NotNullSegement(propertyName));
                } else if (!acceptBracket || this.ch != ']') {
                    Operator op = readOp();
                    skipWhitespace();
                    if (op == Operator.BETWEEN || op == Operator.NOT_BETWEEN) {
                        boolean not = op == Operator.NOT_BETWEEN;
                        Object startValue = readValue();
                        if (!"and".equalsIgnoreCase(readName())) {
                            throw new JSONPathException(this.path);
                        }
                        Object endValue = readValue();
                        if (startValue == null || endValue == null) {
                            throw new JSONPathException(this.path);
                        } else if (JSONPath.isInt(startValue.getClass()) && JSONPath.isInt(endValue.getClass())) {
                            return new FilterSegement(new IntBetweenSegement(propertyName, ((Number) startValue).longValue(), ((Number) endValue).longValue(), not));
                        } else {
                            throw new JSONPathException(this.path);
                        }
                    } else if (op == Operator.IN || op == Operator.NOT_IN) {
                        boolean not2 = op == Operator.NOT_IN;
                        accept('(');
                        JSONArray jSONArray = new JSONArray();
                        jSONArray.add(readValue());
                        while (true) {
                            skipWhitespace();
                            if (this.ch != ',') {
                                break;
                            }
                            next();
                            jSONArray.add(readValue());
                        }
                        accept(')');
                        if (predicateFlag) {
                            accept(')');
                        }
                        if (acceptBracket) {
                            accept(']');
                        }
                        boolean isInt = true;
                        boolean isIntObj = true;
                        boolean isString = true;
                        for (Object item : jSONArray) {
                            if (item != null) {
                                Class<?> clazz = item.getClass();
                                if (!(!isInt || clazz == Byte.class || clazz == Short.class || clazz == Integer.class || clazz == Long.class)) {
                                    isInt = false;
                                    isIntObj = false;
                                }
                                if (isString && clazz != String.class) {
                                    isString = false;
                                }
                            } else if (isInt) {
                                isInt = false;
                            }
                        }
                        if (jSONArray.size() == 1 && jSONArray.get(0) == null) {
                            if (not2) {
                                return new FilterSegement(new NotNullSegement(propertyName));
                            }
                            return new FilterSegement(new NullSegement(propertyName));
                        } else if (isInt) {
                            if (jSONArray.size() == 1) {
                                return new FilterSegement(new IntOpSegement(propertyName, ((Number) jSONArray.get(0)).longValue(), not2 ? Operator.NE : Operator.EQ));
                            }
                            long[] values = new long[jSONArray.size()];
                            for (int i = 0; i < values.length; i++) {
                                values[i] = ((Number) jSONArray.get(i)).longValue();
                            }
                            return new FilterSegement(new IntInSegement(propertyName, values, not2));
                        } else if (isString) {
                            if (jSONArray.size() == 1) {
                                return new FilterSegement(new StringOpSegement(propertyName, (String) jSONArray.get(0), not2 ? Operator.NE : Operator.EQ));
                            }
                            String[] values2 = new String[jSONArray.size()];
                            jSONArray.toArray(values2);
                            return new FilterSegement(new StringInSegement(propertyName, values2, not2));
                        } else if (isIntObj) {
                            Long[] values3 = new Long[jSONArray.size()];
                            for (int i2 = 0; i2 < values3.length; i2++) {
                                Number item2 = (Number) jSONArray.get(i2);
                                if (item2 != null) {
                                    values3[i2] = Long.valueOf(item2.longValue());
                                }
                            }
                            return new FilterSegement(new IntObjInSegement(propertyName, values3, not2));
                        } else {
                            throw new UnsupportedOperationException();
                        }
                    } else if (this.ch == '\'' || this.ch == '\"') {
                        String strValue = readString();
                        if (predicateFlag) {
                            accept(')');
                        }
                        if (acceptBracket) {
                            accept(']');
                        }
                        if (op == Operator.RLIKE) {
                            return new FilterSegement(new RlikeSegement(propertyName, strValue, false));
                        }
                        if (op == Operator.NOT_RLIKE) {
                            return new FilterSegement(new RlikeSegement(propertyName, strValue, true));
                        }
                        if (op == Operator.LIKE || op == Operator.NOT_LIKE) {
                            while (strValue.indexOf("%%") != -1) {
                                strValue = strValue.replaceAll("%%", "%");
                            }
                            boolean not3 = op == Operator.NOT_LIKE;
                            int p0 = strValue.indexOf(37);
                            if (p0 != -1) {
                                String[] items = strValue.split("%");
                                String startsWithValue = null;
                                String endsWithValue = null;
                                String[] containsValues = null;
                                if (p0 == 0) {
                                    if (strValue.charAt(strValue.length() - 1) == '%') {
                                        containsValues = new String[(items.length - 1)];
                                        System.arraycopy(items, 1, containsValues, 0, containsValues.length);
                                    } else {
                                        endsWithValue = items[items.length - 1];
                                        if (items.length > 2) {
                                            containsValues = new String[(items.length - 2)];
                                            System.arraycopy(items, 1, containsValues, 0, containsValues.length);
                                        }
                                    }
                                } else {
                                    if (strValue.charAt(strValue.length() - 1) == '%') {
                                        containsValues = items;
                                    } else if (items.length == 1) {
                                        startsWithValue = items[0];
                                    } else if (items.length == 2) {
                                        startsWithValue = items[0];
                                        endsWithValue = items[1];
                                    } else {
                                        startsWithValue = items[0];
                                        endsWithValue = items[items.length - 1];
                                        containsValues = new String[(items.length - 2)];
                                        System.arraycopy(items, 1, containsValues, 0, containsValues.length);
                                    }
                                }
                                return new FilterSegement(new MatchSegement(propertyName, startsWithValue, endsWithValue, containsValues, not3));
                            } else if (op == Operator.LIKE) {
                                op = Operator.EQ;
                            } else {
                                op = Operator.NE;
                            }
                        }
                        return new FilterSegement(new StringOpSegement(propertyName, strValue, op));
                    } else if (isDigitFirst(this.ch)) {
                        long value = readLongValue();
                        double doubleValue = ClientTraceData.b.f47a;
                        if (this.ch == '.') {
                            doubleValue = readDoubleValue(value);
                        }
                        if (predicateFlag) {
                            accept(')');
                        }
                        if (acceptBracket) {
                            accept(']');
                        }
                        if (doubleValue == ClientTraceData.b.f47a) {
                            return new FilterSegement(new IntOpSegement(propertyName, value, op));
                        }
                        return new FilterSegement(new DoubleOpSegement(propertyName, doubleValue, op));
                    } else {
                        if (this.ch == 'n') {
                            if (Constant.NULL.equals(readName())) {
                                if (predicateFlag) {
                                    accept(')');
                                }
                                accept(']');
                                if (op == Operator.EQ) {
                                    return new FilterSegement(new NullSegement(propertyName));
                                }
                                if (op == Operator.NE) {
                                    return new FilterSegement(new NotNullSegement(propertyName));
                                }
                                throw new UnsupportedOperationException();
                            }
                        } else if (this.ch == 't') {
                            if ("true".equals(readName())) {
                                if (predicateFlag) {
                                    accept(')');
                                }
                                accept(']');
                                if (op == Operator.EQ) {
                                    return new FilterSegement(new ValueSegment(propertyName, Boolean.TRUE, true));
                                }
                                if (op == Operator.NE) {
                                    return new FilterSegement(new ValueSegment(propertyName, Boolean.TRUE, false));
                                }
                                throw new UnsupportedOperationException();
                            }
                        } else if (this.ch == 'f' && "false".equals(readName())) {
                            if (predicateFlag) {
                                accept(')');
                            }
                            accept(']');
                            if (op == Operator.EQ) {
                                return new FilterSegement(new ValueSegment(propertyName, Boolean.FALSE, true));
                            }
                            if (op == Operator.NE) {
                                return new FilterSegement(new ValueSegment(propertyName, Boolean.FALSE, false));
                            }
                            throw new UnsupportedOperationException();
                        }
                        throw new UnsupportedOperationException();
                    }
                } else {
                    next();
                    return new FilterSegement(new NotNullSegement(propertyName));
                }
            } else {
                int start = this.pos - 1;
                while (this.ch != ']' && this.ch != '/' && !isEOF() && (this.ch != '.' || predicateFlag || predicateFlag)) {
                    if (this.ch == '\\') {
                        next();
                    }
                    next();
                }
                if (acceptBracket) {
                    end = this.pos - 1;
                } else if (this.ch == '/' || this.ch == '.') {
                    end = this.pos - 1;
                } else {
                    end = this.pos;
                }
                String text = this.path.substring(start, end);
                if (text.indexOf("\\.") != -1) {
                    return new PropertySegement(text.replaceAll("\\\\\\.", "\\."), false);
                }
                Segement buildArraySegement = buildArraySegement(text);
                if (!acceptBracket || isEOF()) {
                    return buildArraySegement;
                }
                accept(']');
                return buildArraySegement;
            }
        }

        /* access modifiers changed from: protected */
        public long readLongValue() {
            int beginIndex = this.pos - 1;
            if (this.ch == '+' || this.ch == '-') {
                next();
            }
            while (this.ch >= '0' && this.ch <= '9') {
                next();
            }
            return Long.parseLong(this.path.substring(beginIndex, this.pos - 1));
        }

        /* access modifiers changed from: protected */
        public double readDoubleValue(long longValue) {
            int beginIndex = this.pos - 1;
            next();
            while (this.ch >= '0' && this.ch <= '9') {
                next();
            }
            return Double.parseDouble(this.path.substring(beginIndex, this.pos - 1)) + ((double) longValue);
        }

        /* access modifiers changed from: protected */
        public Object readValue() {
            skipWhitespace();
            if (isDigitFirst(this.ch)) {
                return Long.valueOf(readLongValue());
            }
            if (this.ch == '\"' || this.ch == '\'') {
                return readString();
            }
            if (this.ch != 'n') {
                throw new UnsupportedOperationException();
            } else if (Constant.NULL.equals(readName())) {
                return null;
            } else {
                throw new JSONPathException(this.path);
            }
        }

        static boolean isDigitFirst(char ch2) {
            return ch2 == '-' || ch2 == '+' || (ch2 >= '0' && ch2 <= '9');
        }

        /* access modifiers changed from: protected */
        public Operator readOp() {
            Operator op = null;
            if (this.ch == '=') {
                next();
                op = Operator.EQ;
            } else if (this.ch == '!') {
                next();
                accept('=');
                op = Operator.NE;
            } else if (this.ch == '<') {
                next();
                if (this.ch == '=') {
                    next();
                    op = Operator.LE;
                } else {
                    op = Operator.LT;
                }
            } else if (this.ch == '>') {
                next();
                if (this.ch == '=') {
                    next();
                    op = Operator.GE;
                } else {
                    op = Operator.GT;
                }
            }
            if (op != null) {
                return op;
            }
            String name = readName();
            if ("not".equalsIgnoreCase(name)) {
                skipWhitespace();
                String name2 = readName();
                if (BaseConfig.INTENT_KEY_MODULE_LIKE.equalsIgnoreCase(name2)) {
                    return Operator.NOT_LIKE;
                }
                if ("rlike".equalsIgnoreCase(name2)) {
                    return Operator.NOT_RLIKE;
                }
                if ("in".equalsIgnoreCase(name2)) {
                    return Operator.NOT_IN;
                }
                if ("between".equalsIgnoreCase(name2)) {
                    return Operator.NOT_BETWEEN;
                }
                throw new UnsupportedOperationException();
            } else if (BaseConfig.INTENT_KEY_MODULE_LIKE.equalsIgnoreCase(name)) {
                return Operator.LIKE;
            } else {
                if ("rlike".equalsIgnoreCase(name)) {
                    return Operator.RLIKE;
                }
                if ("in".equalsIgnoreCase(name)) {
                    return Operator.IN;
                }
                if ("between".equalsIgnoreCase(name)) {
                    return Operator.BETWEEN;
                }
                throw new UnsupportedOperationException();
            }
        }

        /* access modifiers changed from: package-private */
        public String readName() {
            skipWhitespace();
            if (this.ch == '\\' || IOUtils.firstIdentifier(this.ch)) {
                StringBuilder buf = new StringBuilder();
                while (!isEOF()) {
                    if (this.ch != '\\') {
                        if (!IOUtils.isIdent(this.ch)) {
                            break;
                        }
                        buf.append(this.ch);
                        next();
                    } else {
                        next();
                        buf.append(this.ch);
                        if (isEOF()) {
                            break;
                        }
                        next();
                    }
                }
                if (isEOF() && IOUtils.isIdent(this.ch)) {
                    buf.append(this.ch);
                }
                return buf.toString();
            }
            throw new JSONPathException("illeal jsonpath syntax. " + this.path);
        }

        /* access modifiers changed from: package-private */
        public String readString() {
            char quoate = this.ch;
            next();
            int beginIndex = this.pos - 1;
            while (this.ch != quoate && !isEOF()) {
                next();
            }
            String strValue = this.path.substring(beginIndex, isEOF() ? this.pos : this.pos - 1);
            accept(quoate);
            return strValue;
        }

        /* access modifiers changed from: package-private */
        public void accept(char expect) {
            if (this.ch != expect) {
                throw new JSONPathException("expect '" + expect + ", but '" + this.ch + "'");
            } else if (!isEOF()) {
                next();
            }
        }

        public Segement[] explain() {
            if (this.path == null || this.path.length() == 0) {
                throw new IllegalArgumentException();
            }
            Segement[] segements = new Segement[8];
            while (true) {
                Segement segment = readSegement();
                if (segment == null) {
                    break;
                }
                if (this.level == segements.length) {
                    Segement[] t = new Segement[((this.level * 3) / 2)];
                    System.arraycopy(segements, 0, t, 0, this.level);
                    segements = t;
                }
                int i = this.level;
                this.level = i + 1;
                segements[i] = segment;
            }
            if (this.level == segements.length) {
                return segements;
            }
            Segement[] result = new Segement[this.level];
            System.arraycopy(segements, 0, result, 0, this.level);
            return result;
        }

        /* access modifiers changed from: package-private */
        public Segement buildArraySegement(String indexText) {
            int end;
            int step;
            int indexTextLen = indexText.length();
            char firstChar = indexText.charAt(0);
            char lastChar = indexText.charAt(indexTextLen - 1);
            int commaIndex = indexText.indexOf(44);
            if (indexText.length() <= 2 || firstChar != '\'' || lastChar != '\'') {
                int colonIndex = indexText.indexOf(58);
                if (commaIndex == -1 && colonIndex == -1) {
                    return new ArrayAccessSegement(Integer.parseInt(indexText));
                }
                if (commaIndex != -1) {
                    String[] indexesText = indexText.split(",");
                    int[] indexes = new int[indexesText.length];
                    for (int i = 0; i < indexesText.length; i++) {
                        indexes[i] = Integer.parseInt(indexesText[i]);
                    }
                    return new MultiIndexSegement(indexes);
                } else if (colonIndex != -1) {
                    String[] indexesText2 = indexText.split(SymbolExpUtil.SYMBOL_COLON);
                    int[] indexes2 = new int[indexesText2.length];
                    for (int i2 = 0; i2 < indexesText2.length; i2++) {
                        String str = indexesText2[i2];
                        if (str.length() != 0) {
                            indexes2[i2] = Integer.parseInt(str);
                        } else if (i2 == 0) {
                            indexes2[i2] = 0;
                        } else {
                            throw new UnsupportedOperationException();
                        }
                    }
                    int start = indexes2[0];
                    if (indexes2.length > 1) {
                        end = indexes2[1];
                    } else {
                        end = -1;
                    }
                    if (indexes2.length == 3) {
                        step = indexes2[2];
                    } else {
                        step = 1;
                    }
                    if (end >= 0 && end < start) {
                        throw new UnsupportedOperationException("end must greater than or equals start. start " + start + ",  end " + end);
                    } else if (step > 0) {
                        return new RangeSegement(start, end, step);
                    } else {
                        throw new UnsupportedOperationException("step must greater than zero : " + step);
                    }
                } else {
                    throw new UnsupportedOperationException();
                }
            } else if (commaIndex == -1) {
                return new PropertySegement(indexText.substring(1, indexTextLen - 1), false);
            } else {
                String[] indexesText3 = indexText.split(",");
                String[] propertyNames = new String[indexesText3.length];
                for (int i3 = 0; i3 < indexesText3.length; i3++) {
                    String indexesTextItem = indexesText3[i3];
                    propertyNames[i3] = indexesTextItem.substring(1, indexesTextItem.length() - 1);
                }
                return new MultiPropertySegement(propertyNames);
            }
        }
    }

    static class SizeSegement implements Segement {
        public static final SizeSegement instance = new SizeSegement();

        SizeSegement() {
        }

        public Integer eval(JSONPath path, Object rootObject, Object currentObject) {
            return Integer.valueOf(path.evalSize(currentObject));
        }
    }

    static class PropertySegement implements Segement {
        private final boolean deep;
        /* access modifiers changed from: private */
        public final String propertyName;

        public PropertySegement(String propertyName2, boolean deep2) {
            this.propertyName = propertyName2;
            this.deep = deep2;
        }

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            if (!this.deep) {
                return path.getPropertyValue(currentObject, this.propertyName, true);
            }
            List<Object> results = new ArrayList<>();
            path.deepScan(currentObject, this.propertyName, results);
            return results;
        }

        public void setValue(JSONPath path, Object parent, Object value) {
            path.setPropertyValue(parent, this.propertyName, value);
        }

        public boolean remove(JSONPath path, Object parent) {
            return path.removePropertyValue(parent, this.propertyName);
        }
    }

    static class MultiPropertySegement implements Segement {
        private final String[] propertyNames;

        public MultiPropertySegement(String[] propertyNames2) {
            this.propertyNames = propertyNames2;
        }

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            List<Object> fieldValues = new ArrayList<>(this.propertyNames.length);
            for (String propertyName : this.propertyNames) {
                fieldValues.add(path.getPropertyValue(currentObject, propertyName, true));
            }
            return fieldValues;
        }
    }

    static class WildCardSegement implements Segement {
        public static WildCardSegement instance = new WildCardSegement();

        WildCardSegement() {
        }

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            return path.getPropertyValues(currentObject);
        }
    }

    static class ArrayAccessSegement implements Segement {
        private final int index;

        public ArrayAccessSegement(int index2) {
            this.index = index2;
        }

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            return path.getArrayItem(currentObject, this.index);
        }

        public boolean setValue(JSONPath path, Object currentObject, Object value) {
            return path.setArrayItem(path, currentObject, this.index, value);
        }

        public boolean remove(JSONPath path, Object currentObject) {
            return path.removeArrayItem(path, currentObject, this.index);
        }
    }

    static class MultiIndexSegement implements Segement {
        private final int[] indexes;

        public MultiIndexSegement(int[] indexes2) {
            this.indexes = indexes2;
        }

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            List<Object> items = new ArrayList<>(this.indexes.length);
            for (int arrayItem : this.indexes) {
                items.add(path.getArrayItem(currentObject, arrayItem));
            }
            return items;
        }
    }

    static class RangeSegement implements Segement {
        private final int end;
        private final int start;
        private final int step;

        public RangeSegement(int start2, int end2, int step2) {
            this.start = start2;
            this.end = end2;
            this.step = step2;
        }

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            int size = SizeSegement.instance.eval(path, rootObject, currentObject).intValue();
            int start2 = this.start >= 0 ? this.start : this.start + size;
            int end2 = this.end >= 0 ? this.end : this.end + size;
            int array_size = ((end2 - start2) / this.step) + 1;
            if (array_size == -1) {
                return null;
            }
            List<Object> items = new ArrayList<>(array_size);
            int i = start2;
            while (i <= end2 && i < size) {
                items.add(path.getArrayItem(currentObject, i));
                i += this.step;
            }
            return items;
        }
    }

    static class NotNullSegement implements Filter {
        private final String propertyName;

        public NotNullSegement(String propertyName2) {
            this.propertyName = propertyName2;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            if (path.getPropertyValue(item, this.propertyName, false) != null) {
                return true;
            }
            return false;
        }
    }

    static class NullSegement implements Filter {
        private final String propertyName;

        public NullSegement(String propertyName2) {
            this.propertyName = propertyName2;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            if (path.getPropertyValue(item, this.propertyName, false) == null) {
                return true;
            }
            return false;
        }
    }

    static class ValueSegment implements Filter {
        private boolean eq = true;
        private final String propertyName;
        private final Object value;

        public ValueSegment(String propertyName2, Object value2, boolean eq2) {
            if (value2 == null) {
                throw new IllegalArgumentException("value is null");
            }
            this.propertyName = propertyName2;
            this.value = value2;
            this.eq = eq2;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            boolean result = this.value.equals(path.getPropertyValue(item, this.propertyName, false));
            if (this.eq) {
                return result;
            }
            if (!result) {
                return true;
            }
            return false;
        }
    }

    static class IntInSegement implements Filter {
        private final boolean not;
        private final String propertyName;
        private final long[] values;

        public IntInSegement(String propertyName2, long[] values2, boolean not2) {
            this.propertyName = propertyName2;
            this.values = values2;
            this.not = not2;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, this.propertyName, false);
            if (propertyValue == null) {
                return false;
            }
            if (propertyValue instanceof Number) {
                long longPropertyValue = ((Number) propertyValue).longValue();
                long[] jArr = this.values;
                int length = jArr.length;
                int i = 0;
                while (i < length) {
                    if (jArr[i] != longPropertyValue) {
                        i++;
                    } else if (!this.not) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return this.not;
        }
    }

    static class IntBetweenSegement implements Filter {
        private final long endValue;
        private final boolean not;
        private final String propertyName;
        private final long startValue;

        public IntBetweenSegement(String propertyName2, long startValue2, long endValue2, boolean not2) {
            this.propertyName = propertyName2;
            this.startValue = startValue2;
            this.endValue = endValue2;
            this.not = not2;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, this.propertyName, false);
            if (propertyValue == null) {
                return false;
            }
            if (propertyValue instanceof Number) {
                long longPropertyValue = ((Number) propertyValue).longValue();
                if (longPropertyValue >= this.startValue && longPropertyValue <= this.endValue) {
                    if (!this.not) {
                        return true;
                    }
                    return false;
                }
            }
            return this.not;
        }
    }

    static class IntObjInSegement implements Filter {
        private final boolean not;
        private final String propertyName;
        private final Long[] values;

        public IntObjInSegement(String propertyName2, Long[] values2, boolean not2) {
            this.propertyName = propertyName2;
            this.values = values2;
            this.not = not2;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, this.propertyName, false);
            if (propertyValue == null) {
                Long[] lArr = this.values;
                int length = lArr.length;
                int i = 0;
                while (i < length) {
                    if (lArr[i] != null) {
                        i++;
                    } else if (!this.not) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return this.not;
            }
            if (propertyValue instanceof Number) {
                long longPropertyValue = ((Number) propertyValue).longValue();
                for (Long value : this.values) {
                    if (value != null && value.longValue() == longPropertyValue) {
                        return !this.not;
                    }
                }
            }
            return this.not;
        }
    }

    static class StringInSegement implements Filter {
        private final boolean not;
        private final String propertyName;
        private final String[] values;

        public StringInSegement(String propertyName2, String[] values2, boolean not2) {
            this.propertyName = propertyName2;
            this.values = values2;
            this.not = not2;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, this.propertyName, false);
            String[] strArr = this.values;
            int length = strArr.length;
            int i = 0;
            while (i < length) {
                String value = strArr[i];
                if (value == propertyValue) {
                    if (!this.not) {
                        return true;
                    }
                    return false;
                } else if (value == null || !value.equals(propertyValue)) {
                    i++;
                } else if (this.not) {
                    return false;
                } else {
                    return true;
                }
            }
            return this.not;
        }
    }

    static class IntOpSegement implements Filter {
        private final Operator op;
        private final String propertyName;
        private final long value;

        public IntOpSegement(String propertyName2, long value2, Operator op2) {
            this.propertyName = propertyName2;
            this.value = value2;
            this.op = op2;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, this.propertyName, false);
            if (propertyValue == null) {
                return false;
            }
            if (!(propertyValue instanceof Number)) {
                return false;
            }
            long longValue = ((Number) propertyValue).longValue();
            if (this.op == Operator.EQ) {
                if (longValue != this.value) {
                    return false;
                }
                return true;
            } else if (this.op == Operator.NE) {
                if (longValue == this.value) {
                    return false;
                }
                return true;
            } else if (this.op == Operator.GE) {
                if (longValue < this.value) {
                    return false;
                }
                return true;
            } else if (this.op == Operator.GT) {
                if (longValue <= this.value) {
                    return false;
                }
                return true;
            } else if (this.op == Operator.LE) {
                if (longValue > this.value) {
                    return false;
                }
                return true;
            } else if (this.op != Operator.LT) {
                return false;
            } else {
                if (longValue >= this.value) {
                    return false;
                }
                return true;
            }
        }
    }

    static class DoubleOpSegement implements Filter {
        private final Operator op;
        private final String propertyName;
        private final double value;

        public DoubleOpSegement(String propertyName2, double value2, Operator op2) {
            this.propertyName = propertyName2;
            this.value = value2;
            this.op = op2;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, this.propertyName, false);
            if (propertyValue == null) {
                return false;
            }
            if (!(propertyValue instanceof Number)) {
                return false;
            }
            double doubleValue = ((Number) propertyValue).doubleValue();
            if (this.op == Operator.EQ) {
                if (doubleValue != this.value) {
                    return false;
                }
                return true;
            } else if (this.op == Operator.NE) {
                if (doubleValue == this.value) {
                    return false;
                }
                return true;
            } else if (this.op == Operator.GE) {
                if (doubleValue < this.value) {
                    return false;
                }
                return true;
            } else if (this.op == Operator.GT) {
                if (doubleValue <= this.value) {
                    return false;
                }
                return true;
            } else if (this.op == Operator.LE) {
                if (doubleValue > this.value) {
                    return false;
                }
                return true;
            } else if (this.op != Operator.LT) {
                return false;
            } else {
                if (doubleValue >= this.value) {
                    return false;
                }
                return true;
            }
        }
    }

    static class MatchSegement implements Filter {
        private final String[] containsValues;
        private final String endsWithValue;
        private final int minLength;
        private final boolean not;
        private final String propertyName;
        private final String startsWithValue;

        public MatchSegement(String propertyName2, String startsWithValue2, String endsWithValue2, String[] containsValues2, boolean not2) {
            this.propertyName = propertyName2;
            this.startsWithValue = startsWithValue2;
            this.endsWithValue = endsWithValue2;
            this.containsValues = containsValues2;
            this.not = not2;
            int len = startsWithValue2 != null ? 0 + startsWithValue2.length() : 0;
            len = endsWithValue2 != null ? len + endsWithValue2.length() : len;
            if (containsValues2 != null) {
                for (String item : containsValues2) {
                    len += item.length();
                }
            }
            this.minLength = len;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, this.propertyName, false);
            if (propertyValue == null) {
                return false;
            }
            String strPropertyValue = propertyValue.toString();
            if (strPropertyValue.length() < this.minLength) {
                return this.not;
            }
            int start = 0;
            if (this.startsWithValue != null) {
                if (!strPropertyValue.startsWith(this.startsWithValue)) {
                    return this.not;
                }
                start = 0 + this.startsWithValue.length();
            }
            if (this.containsValues != null) {
                for (String containsValue : this.containsValues) {
                    int index = strPropertyValue.indexOf(containsValue, start);
                    if (index == -1) {
                        return this.not;
                    }
                    start = index + containsValue.length();
                }
            }
            if (this.endsWithValue != null && !strPropertyValue.endsWith(this.endsWithValue)) {
                return this.not;
            }
            if (!this.not) {
                return true;
            }
            return false;
        }
    }

    static class RlikeSegement implements Filter {
        private final boolean not;
        private final Pattern pattern;
        private final String propertyName;

        public RlikeSegement(String propertyName2, String pattern2, boolean not2) {
            this.propertyName = propertyName2;
            this.pattern = Pattern.compile(pattern2);
            this.not = not2;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, this.propertyName, false);
            if (propertyValue == null) {
                return false;
            }
            boolean match = this.pattern.matcher(propertyValue.toString()).matches();
            if (this.not) {
                if (!match) {
                    match = true;
                } else {
                    match = false;
                }
            }
            return match;
        }
    }

    static class StringOpSegement implements Filter {
        private final Operator op;
        private final String propertyName;
        private final String value;

        public StringOpSegement(String propertyName2, String value2, Operator op2) {
            this.propertyName = propertyName2;
            this.value = value2;
            this.op = op2;
        }

        public boolean apply(JSONPath path, Object rootObject, Object currentObject, Object item) {
            Object propertyValue = path.getPropertyValue(item, this.propertyName, false);
            if (this.op == Operator.EQ) {
                return this.value.equals(propertyValue);
            }
            if (this.op == Operator.NE) {
                if (this.value.equals(propertyValue)) {
                    return false;
                }
                return true;
            } else if (propertyValue == null) {
                return false;
            } else {
                int compareResult = this.value.compareTo(propertyValue.toString());
                if (this.op == Operator.GE) {
                    if (compareResult > 0) {
                        return false;
                    }
                    return true;
                } else if (this.op == Operator.GT) {
                    if (compareResult >= 0) {
                        return false;
                    }
                    return true;
                } else if (this.op == Operator.LE) {
                    if (compareResult < 0) {
                        return false;
                    }
                    return true;
                } else if (this.op != Operator.LT) {
                    return false;
                } else {
                    if (compareResult <= 0) {
                        return false;
                    }
                    return true;
                }
            }
        }
    }

    public static class FilterSegement implements Segement {
        private final Filter filter;

        public FilterSegement(Filter filter2) {
            this.filter = filter2;
        }

        public Object eval(JSONPath path, Object rootObject, Object currentObject) {
            if (currentObject == null) {
                return null;
            }
            List<Object> items = new JSONArray();
            if (currentObject instanceof Iterable) {
                for (Object item : (Iterable) currentObject) {
                    if (this.filter.apply(path, rootObject, currentObject, item)) {
                        items.add(item);
                    }
                }
                return items;
            } else if (this.filter.apply(path, rootObject, currentObject, currentObject)) {
                return currentObject;
            } else {
                return null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public Object getArrayItem(Object currentObject, int index) {
        if (currentObject == null) {
            return null;
        }
        if (currentObject instanceof List) {
            List list = (List) currentObject;
            if (index >= 0) {
                if (index < list.size()) {
                    return list.get(index);
                }
                return null;
            } else if (Math.abs(index) <= list.size()) {
                return list.get(list.size() + index);
            } else {
                return null;
            }
        } else if (currentObject.getClass().isArray()) {
            int arrayLenth = Array.getLength(currentObject);
            if (index >= 0) {
                if (index < arrayLenth) {
                    return Array.get(currentObject, index);
                }
                return null;
            } else if (Math.abs(index) <= arrayLenth) {
                return Array.get(currentObject, arrayLenth + index);
            } else {
                return null;
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public boolean setArrayItem(JSONPath path2, Object currentObject, int index, Object value) {
        if (currentObject instanceof List) {
            List list = (List) currentObject;
            if (index >= 0) {
                list.set(index, value);
            } else {
                list.set(list.size() + index, value);
            }
        } else {
            Class<?> clazz = currentObject.getClass();
            if (clazz.isArray()) {
                int arrayLenth = Array.getLength(currentObject);
                if (index >= 0) {
                    if (index < arrayLenth) {
                        Array.set(currentObject, index, value);
                    }
                } else if (Math.abs(index) <= arrayLenth) {
                    Array.set(currentObject, arrayLenth + index, value);
                }
            } else {
                throw new JSONPathException("unsupported set operation." + clazz);
            }
        }
        return true;
    }

    public boolean removeArrayItem(JSONPath path2, Object currentObject, int index) {
        if (currentObject instanceof List) {
            List list = (List) currentObject;
            if (index < 0) {
                int newIndex = list.size() + index;
                if (newIndex < 0) {
                    return false;
                }
                list.remove(newIndex);
            } else if (index >= list.size()) {
                return false;
            } else {
                list.remove(index);
            }
            return true;
        }
        throw new JSONPathException("unsupported set operation." + currentObject.getClass());
    }

    /* access modifiers changed from: protected */
    public Collection<Object> getPropertyValues(Object currentObject) {
        JavaBeanSerializer beanSerializer = getJavaBeanSerializer(currentObject.getClass());
        if (beanSerializer != null) {
            try {
                return beanSerializer.getFieldValues(currentObject);
            } catch (Exception e) {
                throw new JSONPathException("jsonpath error, path " + this.path, e);
            }
        } else if (currentObject instanceof Map) {
            return ((Map) currentObject).values();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    static boolean eq(Object a, Object b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.getClass() == b.getClass()) {
            return a.equals(b);
        }
        if (!(a instanceof Number)) {
            return a.equals(b);
        }
        if (b instanceof Number) {
            return eqNotNull((Number) a, (Number) b);
        }
        return false;
    }

    static boolean eqNotNull(Number a, Number b) {
        Class clazzA = a.getClass();
        boolean isIntA = isInt(clazzA);
        Class clazzB = b.getClass();
        boolean isIntB = isInt(clazzB);
        if (a instanceof BigDecimal) {
            BigDecimal decimalA = (BigDecimal) a;
            if (isIntB) {
                return decimalA.equals(BigDecimal.valueOf(b.longValue()));
            }
        }
        if (isIntA) {
            if (isIntB) {
                return a.longValue() == b.longValue();
            }
            if (b instanceof BigInteger) {
                return BigInteger.valueOf(a.longValue()).equals((BigInteger) a);
            }
        }
        if (isIntB && (a instanceof BigInteger)) {
            return ((BigInteger) a).equals(BigInteger.valueOf(b.longValue()));
        }
        boolean isDoubleA = isDouble(clazzA);
        boolean isDoubleB = isDouble(clazzB);
        return ((isDoubleA && isDoubleB) || ((isDoubleA && isIntB) || (isDoubleB && isIntA))) && a.doubleValue() == b.doubleValue();
    }

    protected static boolean isDouble(Class<?> clazzA) {
        return clazzA == Float.class || clazzA == Double.class;
    }

    protected static boolean isInt(Class<?> clazzA) {
        return clazzA == Byte.class || clazzA == Short.class || clazzA == Integer.class || clazzA == Long.class;
    }

    /* access modifiers changed from: protected */
    public Object getPropertyValue(Object currentObject, String propertyName, boolean strictMode) {
        if (currentObject == null) {
            return null;
        }
        if (currentObject instanceof Map) {
            Map map = (Map) currentObject;
            Object val = map.get(propertyName);
            if (val != null || !"size".equals(propertyName)) {
                return val;
            }
            return Integer.valueOf(map.size());
        }
        JavaBeanSerializer beanSerializer = getJavaBeanSerializer(currentObject.getClass());
        if (beanSerializer != null) {
            try {
                return beanSerializer.getFieldValue(currentObject, propertyName);
            } catch (Exception e) {
                throw new JSONPathException("jsonpath error, path " + this.path + ", segement " + propertyName, e);
            }
        } else if (currentObject instanceof List) {
            List list = (List) currentObject;
            if ("size".equals(propertyName)) {
                return Integer.valueOf(list.size());
            }
            List<Object> fieldValues = new JSONArray(list.size());
            for (int i = 0; i < list.size(); i++) {
                fieldValues.add(getPropertyValue(list.get(i), propertyName, strictMode));
            }
            return fieldValues;
        } else {
            if (currentObject instanceof Enum) {
                Enum e2 = (Enum) currentObject;
                if ("name".equals(propertyName)) {
                    return e2.name();
                }
                if ("ordinal".equals(propertyName)) {
                    return Integer.valueOf(e2.ordinal());
                }
            }
            if (currentObject instanceof Calendar) {
                Calendar e3 = (Calendar) currentObject;
                if ("year".equals(propertyName)) {
                    return Integer.valueOf(e3.get(1));
                }
                if ("month".equals(propertyName)) {
                    return Integer.valueOf(e3.get(2));
                }
                if ("day".equals(propertyName)) {
                    return Integer.valueOf(e3.get(5));
                }
                if ("hour".equals(propertyName)) {
                    return Integer.valueOf(e3.get(11));
                }
                if ("minute".equals(propertyName)) {
                    return Integer.valueOf(e3.get(12));
                }
                if ("second".equals(propertyName)) {
                    return Integer.valueOf(e3.get(13));
                }
            }
            throw new JSONPathException("jsonpath error, path " + this.path + ", segement " + propertyName);
        }
    }

    /* access modifiers changed from: protected */
    public void deepScan(Object currentObject, String propertyName, List<Object> results) {
        if (currentObject != null) {
            if (currentObject instanceof Map) {
                Map<?, ?> map = (Map) currentObject;
                if (map.containsKey(propertyName)) {
                    results.add(map.get(propertyName));
                    return;
                }
                for (Object val : map.values()) {
                    deepScan(val, propertyName, results);
                }
                return;
            }
            JavaBeanSerializer beanSerializer = getJavaBeanSerializer(currentObject.getClass());
            if (beanSerializer != null) {
                try {
                    FieldSerializer fieldDeser = beanSerializer.getFieldSerializer(propertyName);
                    if (fieldDeser != null) {
                        results.add(fieldDeser.getPropertyValueDirect(currentObject));
                        return;
                    }
                    for (Object val2 : beanSerializer.getFieldValues(currentObject)) {
                        deepScan(val2, propertyName, results);
                    }
                } catch (InvocationTargetException ex) {
                    throw new JSONException("getFieldValue error." + propertyName, ex);
                } catch (IllegalAccessException ex2) {
                    throw new JSONException("getFieldValue error." + propertyName, ex2);
                } catch (Exception e) {
                    throw new JSONPathException("jsonpath error, path " + this.path + ", segement " + propertyName, e);
                }
            } else if (currentObject instanceof List) {
                List list = (List) currentObject;
                for (int i = 0; i < list.size(); i++) {
                    deepScan(list.get(i), propertyName, results);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean setPropertyValue(Object parent, String name, Object value) {
        if (parent instanceof Map) {
            ((Map) parent).put(name, value);
            return true;
        } else if (parent instanceof List) {
            for (Object element : (List) parent) {
                if (element != null) {
                    setPropertyValue(element, name, value);
                }
            }
            return true;
        } else {
            ObjectDeserializer derializer = this.parserConfig.getDeserializer((Type) parent.getClass());
            JavaBeanDeserializer beanDerializer = null;
            if (derializer instanceof JavaBeanDeserializer) {
                beanDerializer = (JavaBeanDeserializer) derializer;
            }
            if (beanDerializer != null) {
                FieldDeserializer fieldDeserializer = beanDerializer.getFieldDeserializer(name);
                if (fieldDeserializer == null) {
                    return false;
                }
                fieldDeserializer.setValue(parent, value);
                return true;
            }
            throw new UnsupportedOperationException();
        }
    }

    /* access modifiers changed from: protected */
    public boolean removePropertyValue(Object parent, String name) {
        if (!(parent instanceof Map)) {
            ObjectDeserializer derializer = this.parserConfig.getDeserializer((Type) parent.getClass());
            JavaBeanDeserializer beanDerializer = null;
            if (derializer instanceof JavaBeanDeserializer) {
                beanDerializer = (JavaBeanDeserializer) derializer;
            }
            if (beanDerializer != null) {
                FieldDeserializer fieldDeserializer = beanDerializer.getFieldDeserializer(name);
                if (fieldDeserializer == null) {
                    return false;
                }
                fieldDeserializer.setValue(parent, (String) null);
                return true;
            }
            throw new UnsupportedOperationException();
        } else if (((Map) parent).remove(name) != null) {
            return true;
        } else {
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public JavaBeanSerializer getJavaBeanSerializer(Class<?> currentClass) {
        ObjectSerializer serializer = this.serializeConfig.getObjectWriter(currentClass);
        if (serializer instanceof JavaBeanSerializer) {
            return (JavaBeanSerializer) serializer;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public JavaBeanDeserializer getJavaBeanDeserializer(Class<?> currentClass) {
        ObjectDeserializer deserializer = this.parserConfig.getDeserializer((Type) currentClass);
        if (deserializer instanceof JavaBeanDeserializer) {
            return (JavaBeanDeserializer) deserializer;
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public int evalSize(Object currentObject) {
        if (currentObject == null) {
            return -1;
        }
        if (currentObject instanceof Collection) {
            return ((Collection) currentObject).size();
        }
        if (currentObject instanceof Object[]) {
            return ((Object[]) currentObject).length;
        }
        if (currentObject.getClass().isArray()) {
            return Array.getLength(currentObject);
        }
        if (currentObject instanceof Map) {
            int count = 0;
            for (Object value : ((Map) currentObject).values()) {
                if (value != null) {
                    count++;
                }
            }
            return count;
        }
        JavaBeanSerializer beanSerializer = getJavaBeanSerializer(currentObject.getClass());
        if (beanSerializer == null) {
            return -1;
        }
        try {
            return beanSerializer.getSize(currentObject);
        } catch (Exception e) {
            throw new JSONPathException("evalSize error : " + this.path, e);
        }
    }

    public String toJSONString() {
        return JSON.toJSONString(this.path);
    }
}
