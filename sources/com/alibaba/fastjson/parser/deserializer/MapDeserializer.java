package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import mtopsdk.common.util.SymbolExpUtil;

public class MapDeserializer implements ObjectDeserializer {
    public static MapDeserializer instance = new MapDeserializer();

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        if (type == JSONObject.class && parser.getFieldTypeResolver() == null) {
            return parser.parseObject();
        }
        JSONLexer lexer = parser.lexer;
        if (lexer.token() == 8) {
            lexer.nextToken(16);
            return null;
        }
        Map<Object, Object> map = createMap(type);
        ParseContext context = parser.getContext();
        try {
            parser.setContext(context, map, fieldName);
            return deserialze(parser, type, fieldName, map);
        } finally {
            parser.setContext(context);
        }
    }

    /* access modifiers changed from: protected */
    public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName, Map map) {
        if (!(type instanceof ParameterizedType)) {
            return parser.parseObject(map, fieldName);
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type keyType = parameterizedType.getActualTypeArguments()[0];
        Type valueType = parameterizedType.getActualTypeArguments()[1];
        if (String.class == keyType) {
            return parseMap(parser, map, valueType, fieldName);
        }
        return parseMap(parser, map, keyType, valueType, fieldName);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:55:?, code lost:
        r5 = r3.getDeserializer((java.lang.reflect.Type) r2);
        r8.nextToken(16);
        r15.setResolveStatus(2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0187, code lost:
        if (r4 == null) goto L_0x0192;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x018d, code lost:
        if ((r18 instanceof java.lang.Integer) != false) goto L_0x0192;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x018f, code lost:
        r15.popContext();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0192, code lost:
        r12 = (java.util.Map) r5.deserialze(r15, r2, r18);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x019a, code lost:
        r15.setContext(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:?, code lost:
        return r12;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Map parseMap(com.alibaba.fastjson.parser.DefaultJSONParser r15, java.util.Map<java.lang.String, java.lang.Object> r16, java.lang.reflect.Type r17, java.lang.Object r18) {
        /*
            com.alibaba.fastjson.parser.JSONLexer r8 = r15.lexer
            int r12 = r8.token()
            r13 = 12
            if (r12 == r13) goto L_0x0028
            com.alibaba.fastjson.JSONException r12 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = "syntax error, expect {, actual "
            java.lang.StringBuilder r13 = r13.append(r14)
            int r14 = r8.token()
            java.lang.StringBuilder r13 = r13.append(r14)
            java.lang.String r13 = r13.toString()
            r12.<init>(r13)
            throw r12
        L_0x0028:
            com.alibaba.fastjson.parser.ParseContext r4 = r15.getContext()
            r6 = 0
        L_0x002d:
            r8.skipWhitespace()     // Catch:{ all -> 0x0082 }
            char r1 = r8.getCurrent()     // Catch:{ all -> 0x0082 }
            com.alibaba.fastjson.parser.Feature r12 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas     // Catch:{ all -> 0x0082 }
            boolean r12 = r8.isEnabled((com.alibaba.fastjson.parser.Feature) r12)     // Catch:{ all -> 0x0082 }
            if (r12 == 0) goto L_0x004b
        L_0x003c:
            r12 = 44
            if (r1 != r12) goto L_0x004b
            r8.next()     // Catch:{ all -> 0x0082 }
            r8.skipWhitespace()     // Catch:{ all -> 0x0082 }
            char r1 = r8.getCurrent()     // Catch:{ all -> 0x0082 }
            goto L_0x003c
        L_0x004b:
            r12 = 34
            if (r1 != r12) goto L_0x0087
            com.alibaba.fastjson.parser.SymbolTable r12 = r15.getSymbolTable()     // Catch:{ all -> 0x0082 }
            r13 = 34
            java.lang.String r7 = r8.scanSymbol(r12, r13)     // Catch:{ all -> 0x0082 }
            r8.skipWhitespace()     // Catch:{ all -> 0x0082 }
            char r1 = r8.getCurrent()     // Catch:{ all -> 0x0082 }
            r12 = 58
            if (r1 == r12) goto L_0x012f
            com.alibaba.fastjson.JSONException r12 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0082 }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x0082 }
            r13.<init>()     // Catch:{ all -> 0x0082 }
            java.lang.String r14 = "expect ':' at "
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ all -> 0x0082 }
            int r14 = r8.pos()     // Catch:{ all -> 0x0082 }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ all -> 0x0082 }
            java.lang.String r13 = r13.toString()     // Catch:{ all -> 0x0082 }
            r12.<init>(r13)     // Catch:{ all -> 0x0082 }
            throw r12     // Catch:{ all -> 0x0082 }
        L_0x0082:
            r12 = move-exception
            r15.setContext(r4)
            throw r12
        L_0x0087:
            r12 = 125(0x7d, float:1.75E-43)
            if (r1 != r12) goto L_0x009a
            r8.next()     // Catch:{ all -> 0x0082 }
            r8.resetStringPosition()     // Catch:{ all -> 0x0082 }
            r12 = 16
            r8.nextToken(r12)     // Catch:{ all -> 0x0082 }
            r15.setContext(r4)
        L_0x0099:
            return r16
        L_0x009a:
            r12 = 39
            if (r1 != r12) goto L_0x00e2
            com.alibaba.fastjson.parser.Feature r12 = com.alibaba.fastjson.parser.Feature.AllowSingleQuotes     // Catch:{ all -> 0x0082 }
            boolean r12 = r8.isEnabled((com.alibaba.fastjson.parser.Feature) r12)     // Catch:{ all -> 0x0082 }
            if (r12 != 0) goto L_0x00af
            com.alibaba.fastjson.JSONException r12 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0082 }
            java.lang.String r13 = "syntax error"
            r12.<init>(r13)     // Catch:{ all -> 0x0082 }
            throw r12     // Catch:{ all -> 0x0082 }
        L_0x00af:
            com.alibaba.fastjson.parser.SymbolTable r12 = r15.getSymbolTable()     // Catch:{ all -> 0x0082 }
            r13 = 39
            java.lang.String r7 = r8.scanSymbol(r12, r13)     // Catch:{ all -> 0x0082 }
            r8.skipWhitespace()     // Catch:{ all -> 0x0082 }
            char r1 = r8.getCurrent()     // Catch:{ all -> 0x0082 }
            r12 = 58
            if (r1 == r12) goto L_0x012f
            com.alibaba.fastjson.JSONException r12 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0082 }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x0082 }
            r13.<init>()     // Catch:{ all -> 0x0082 }
            java.lang.String r14 = "expect ':' at "
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ all -> 0x0082 }
            int r14 = r8.pos()     // Catch:{ all -> 0x0082 }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ all -> 0x0082 }
            java.lang.String r13 = r13.toString()     // Catch:{ all -> 0x0082 }
            r12.<init>(r13)     // Catch:{ all -> 0x0082 }
            throw r12     // Catch:{ all -> 0x0082 }
        L_0x00e2:
            com.alibaba.fastjson.parser.Feature r12 = com.alibaba.fastjson.parser.Feature.AllowUnQuotedFieldNames     // Catch:{ all -> 0x0082 }
            boolean r12 = r8.isEnabled((com.alibaba.fastjson.parser.Feature) r12)     // Catch:{ all -> 0x0082 }
            if (r12 != 0) goto L_0x00f3
            com.alibaba.fastjson.JSONException r12 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0082 }
            java.lang.String r13 = "syntax error"
            r12.<init>(r13)     // Catch:{ all -> 0x0082 }
            throw r12     // Catch:{ all -> 0x0082 }
        L_0x00f3:
            com.alibaba.fastjson.parser.SymbolTable r12 = r15.getSymbolTable()     // Catch:{ all -> 0x0082 }
            java.lang.String r7 = r8.scanSymbolUnQuoted(r12)     // Catch:{ all -> 0x0082 }
            r8.skipWhitespace()     // Catch:{ all -> 0x0082 }
            char r1 = r8.getCurrent()     // Catch:{ all -> 0x0082 }
            r12 = 58
            if (r1 == r12) goto L_0x012f
            com.alibaba.fastjson.JSONException r12 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0082 }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x0082 }
            r13.<init>()     // Catch:{ all -> 0x0082 }
            java.lang.String r14 = "expect ':' at "
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ all -> 0x0082 }
            int r14 = r8.pos()     // Catch:{ all -> 0x0082 }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ all -> 0x0082 }
            java.lang.String r14 = ", actual "
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ all -> 0x0082 }
            java.lang.StringBuilder r13 = r13.append(r1)     // Catch:{ all -> 0x0082 }
            java.lang.String r13 = r13.toString()     // Catch:{ all -> 0x0082 }
            r12.<init>(r13)     // Catch:{ all -> 0x0082 }
            throw r12     // Catch:{ all -> 0x0082 }
        L_0x012f:
            r8.next()     // Catch:{ all -> 0x0082 }
            r8.skipWhitespace()     // Catch:{ all -> 0x0082 }
            char r1 = r8.getCurrent()     // Catch:{ all -> 0x0082 }
            r8.resetStringPosition()     // Catch:{ all -> 0x0082 }
            java.lang.String r12 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ all -> 0x0082 }
            if (r7 != r12) goto L_0x01a1
            com.alibaba.fastjson.parser.Feature r12 = com.alibaba.fastjson.parser.Feature.DisableSpecialKeyDetect     // Catch:{ all -> 0x0082 }
            boolean r12 = r8.isEnabled((com.alibaba.fastjson.parser.Feature) r12)     // Catch:{ all -> 0x0082 }
            if (r12 != 0) goto L_0x01a1
            com.alibaba.fastjson.parser.SymbolTable r12 = r15.getSymbolTable()     // Catch:{ all -> 0x0082 }
            r13 = 34
            java.lang.String r10 = r8.scanSymbol(r12, r13)     // Catch:{ all -> 0x0082 }
            com.alibaba.fastjson.parser.ParserConfig r3 = r15.getConfig()     // Catch:{ all -> 0x0082 }
            r12 = 0
            java.lang.Class r2 = r3.checkAutoType(r10, r12)     // Catch:{ all -> 0x0082 }
            java.lang.Class<java.util.Map> r12 = java.util.Map.class
            boolean r12 = r12.isAssignableFrom(r2)     // Catch:{ all -> 0x0082 }
            if (r12 == 0) goto L_0x017a
            r12 = 16
            r8.nextToken(r12)     // Catch:{ all -> 0x0082 }
            int r12 = r8.token()     // Catch:{ all -> 0x0082 }
            r13 = 13
            if (r12 != r13) goto L_0x01e9
            r12 = 16
            r8.nextToken(r12)     // Catch:{ all -> 0x0082 }
            r15.setContext(r4)
            goto L_0x0099
        L_0x017a:
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r5 = r3.getDeserializer((java.lang.reflect.Type) r2)     // Catch:{ all -> 0x0082 }
            r12 = 16
            r8.nextToken(r12)     // Catch:{ all -> 0x0082 }
            r12 = 2
            r15.setResolveStatus(r12)     // Catch:{ all -> 0x0082 }
            if (r4 == 0) goto L_0x0192
            r0 = r18
            boolean r12 = r0 instanceof java.lang.Integer     // Catch:{ all -> 0x0082 }
            if (r12 != 0) goto L_0x0192
            r15.popContext()     // Catch:{ all -> 0x0082 }
        L_0x0192:
            r0 = r18
            java.lang.Object r12 = r5.deserialze(r15, r2, r0)     // Catch:{ all -> 0x0082 }
            java.util.Map r12 = (java.util.Map) r12     // Catch:{ all -> 0x0082 }
            r15.setContext(r4)
            r16 = r12
            goto L_0x0099
        L_0x01a1:
            r8.nextToken()     // Catch:{ all -> 0x0082 }
            if (r6 == 0) goto L_0x01a9
            r15.setContext(r4)     // Catch:{ all -> 0x0082 }
        L_0x01a9:
            int r12 = r8.token()     // Catch:{ all -> 0x0082 }
            r13 = 8
            if (r12 != r13) goto L_0x01d6
            r11 = 0
            r8.nextToken()     // Catch:{ all -> 0x0082 }
        L_0x01b5:
            r0 = r16
            r0.put(r7, r11)     // Catch:{ all -> 0x0082 }
            r0 = r16
            r15.checkMapResolve(r0, r7)     // Catch:{ all -> 0x0082 }
            r15.setContext(r4, r11, r7)     // Catch:{ all -> 0x0082 }
            r15.setContext(r4)     // Catch:{ all -> 0x0082 }
            int r9 = r8.token()     // Catch:{ all -> 0x0082 }
            r12 = 20
            if (r9 == r12) goto L_0x01d1
            r12 = 15
            if (r9 != r12) goto L_0x01dd
        L_0x01d1:
            r15.setContext(r4)
            goto L_0x0099
        L_0x01d6:
            r0 = r17
            java.lang.Object r11 = r15.parseObject((java.lang.reflect.Type) r0, (java.lang.Object) r7)     // Catch:{ all -> 0x0082 }
            goto L_0x01b5
        L_0x01dd:
            r12 = 13
            if (r9 != r12) goto L_0x01e9
            r8.nextToken()     // Catch:{ all -> 0x0082 }
            r15.setContext(r4)
            goto L_0x0099
        L_0x01e9:
            int r6 = r6 + 1
            goto L_0x002d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.MapDeserializer.parseMap(com.alibaba.fastjson.parser.DefaultJSONParser, java.util.Map, java.lang.reflect.Type, java.lang.Object):java.util.Map");
    }

    public static Object parseMap(DefaultJSONParser parser, Map<Object, Object> map, Type keyType, Type valueType, Object fieldName) {
        JSONLexer lexer = parser.lexer;
        if (lexer.token() == 12 || lexer.token() == 16) {
            ObjectDeserializer keyDeserializer = parser.getConfig().getDeserializer(keyType);
            ObjectDeserializer valueDeserializer = parser.getConfig().getDeserializer(valueType);
            lexer.nextToken(keyDeserializer.getFastMatchToken());
            ParseContext context = parser.getContext();
            while (lexer.token() != 13) {
                if (lexer.token() != 4 || !lexer.isRef() || lexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
                    try {
                        if (map.size() == 0 && lexer.token() == 4 && JSON.DEFAULT_TYPE_KEY.equals(lexer.stringVal()) && !lexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
                            lexer.nextTokenWithColon(4);
                            lexer.nextToken(16);
                            if (lexer.token() == 13) {
                                lexer.nextToken();
                                return map;
                            }
                            lexer.nextToken(keyDeserializer.getFastMatchToken());
                        }
                        Object key = keyDeserializer.deserialze(parser, keyType, (Object) null);
                        if (lexer.token() != 17) {
                            throw new JSONException("syntax error, expect :, actual " + lexer.token());
                        }
                        lexer.nextToken(valueDeserializer.getFastMatchToken());
                        Object value = valueDeserializer.deserialze(parser, valueType, key);
                        parser.checkMapResolve(map, key);
                        map.put(key, value);
                        if (lexer.token() == 16) {
                            lexer.nextToken(keyDeserializer.getFastMatchToken());
                        }
                    } finally {
                        parser.setContext(context);
                    }
                } else {
                    Object object = null;
                    lexer.nextTokenWithColon(4);
                    if (lexer.token() == 4) {
                        String ref = lexer.stringVal();
                        if ("..".equals(ref)) {
                            object = context.parent.object;
                        } else if (SymbolExpUtil.SYMBOL_DOLLAR.equals(ref)) {
                            ParseContext rootContext = context;
                            while (rootContext.parent != null) {
                                rootContext = rootContext.parent;
                            }
                            object = rootContext.object;
                        } else {
                            parser.addResolveTask(new DefaultJSONParser.ResolveTask(context, ref));
                            parser.setResolveStatus(1);
                        }
                        lexer.nextToken(13);
                        if (lexer.token() != 13) {
                            throw new JSONException("illegal ref");
                        }
                        lexer.nextToken(16);
                        parser.setContext(context);
                        return object;
                    }
                    throw new JSONException("illegal ref, " + JSONToken.name(lexer.token()));
                }
            }
            lexer.nextToken(16);
            parser.setContext(context);
            return map;
        }
        throw new JSONException("syntax error, expect {, actual " + lexer.tokenName());
    }

    /* access modifiers changed from: protected */
    public Map<Object, Object> createMap(Type type) {
        if (type == Properties.class) {
            return new Properties();
        }
        if (type == Hashtable.class) {
            return new Hashtable();
        }
        if (type == IdentityHashMap.class) {
            return new IdentityHashMap();
        }
        if (type == SortedMap.class || type == TreeMap.class) {
            return new TreeMap();
        }
        if (type == ConcurrentMap.class || type == ConcurrentHashMap.class) {
            return new ConcurrentHashMap();
        }
        if (type == Map.class || type == HashMap.class) {
            return new HashMap();
        }
        if (type == LinkedHashMap.class) {
            return new LinkedHashMap();
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (EnumMap.class.equals(rawType)) {
                return new EnumMap((Class) parameterizedType.getActualTypeArguments()[0]);
            }
            return createMap(rawType);
        }
        Class<?> clazz = (Class) type;
        if (clazz.isInterface()) {
            throw new JSONException("unsupport type " + type);
        }
        try {
            return (Map) clazz.newInstance();
        } catch (Exception e) {
            throw new JSONException("unsupport type " + type, e);
        }
    }

    public int getFastMatchToken() {
        return 12;
    }
}
