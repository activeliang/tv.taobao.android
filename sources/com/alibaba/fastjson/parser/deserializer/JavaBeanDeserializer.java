package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import mtopsdk.common.util.SymbolExpUtil;

public class JavaBeanDeserializer implements ObjectDeserializer {
    public final JavaBeanInfo beanInfo;
    protected final Class<?> clazz;
    private ConcurrentMap<String, Object> extraFieldDeserializers;
    private final FieldDeserializer[] fieldDeserializers;
    protected final FieldDeserializer[] sortedFieldDeserializers;

    public JavaBeanDeserializer(ParserConfig config, Class<?> clazz2) {
        this(config, clazz2, clazz2);
    }

    public JavaBeanDeserializer(ParserConfig config, Class<?> clazz2, Type type) {
        this(config, JavaBeanInfo.build(clazz2, type, config.propertyNamingStrategy));
    }

    public JavaBeanDeserializer(ParserConfig config, JavaBeanInfo beanInfo2) {
        this.clazz = beanInfo2.clazz;
        this.beanInfo = beanInfo2;
        this.sortedFieldDeserializers = new FieldDeserializer[beanInfo2.sortedFields.length];
        int size = beanInfo2.sortedFields.length;
        for (int i = 0; i < size; i++) {
            this.sortedFieldDeserializers[i] = config.createFieldDeserializer(config, beanInfo2, beanInfo2.sortedFields[i]);
        }
        this.fieldDeserializers = new FieldDeserializer[beanInfo2.fields.length];
        int size2 = beanInfo2.fields.length;
        for (int i2 = 0; i2 < size2; i2++) {
            this.fieldDeserializers[i2] = getFieldDeserializer(beanInfo2.fields[i2].name);
        }
    }

    public FieldDeserializer getFieldDeserializer(String key) {
        if (key == null) {
            return null;
        }
        int low = 0;
        int high = this.sortedFieldDeserializers.length - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = this.sortedFieldDeserializers[mid].fieldInfo.name.compareTo(key);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp <= 0) {
                return this.sortedFieldDeserializers[mid];
            } else {
                high = mid - 1;
            }
        }
        return null;
    }

    public Object createInstance(DefaultJSONParser parser, Type type) {
        String parentName;
        Object object;
        String clsName;
        if ((type instanceof Class) && this.clazz.isInterface()) {
            return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{(Class) type}, new JSONObject());
        } else if (this.beanInfo.defaultConstructor == null) {
            return null;
        } else {
            try {
                Constructor<?> constructor = this.beanInfo.defaultConstructor;
                if (this.beanInfo.defaultConstructorParameterSize == 0) {
                    object = constructor.newInstance(new Object[0]);
                } else {
                    ParseContext context = parser.getContext();
                    parentName = context.object.getClass().getName();
                    String typeName = "";
                    if (type instanceof Class) {
                        typeName = ((Class) type).getName();
                    }
                    if (parentName.length() != typeName.lastIndexOf(36) - 1) {
                        char[] typeChars = typeName.toCharArray();
                        StringBuilder clsNameBuilder = new StringBuilder();
                        clsNameBuilder.append(parentName).append(SymbolExpUtil.SYMBOL_DOLLAR);
                        HashMap hashMap = new HashMap();
                        hashMap.put(parentName, context.object);
                        for (int i = parentName.length() + 1; i <= typeName.lastIndexOf(36); i++) {
                            char thisChar = typeChars[i];
                            if (thisChar == '$') {
                                clsName = clsNameBuilder.toString();
                                Object outter = hashMap.get(parentName);
                                Class<?> clazz2 = Class.forName(parentName);
                                if (outter != null) {
                                    Constructor<?> innerClsConstructor = Class.forName(clsName).getDeclaredConstructor(new Class[]{clazz2});
                                    if (!innerClsConstructor.isAccessible()) {
                                        innerClsConstructor.setAccessible(true);
                                    }
                                    hashMap.put(clsName, innerClsConstructor.newInstance(new Object[]{outter}));
                                    parentName = clsName;
                                }
                            }
                            clsNameBuilder.append(thisChar);
                        }
                        object = constructor.newInstance(new Object[]{hashMap.get(parentName)});
                    } else {
                        object = constructor.newInstance(new Object[]{context.object});
                    }
                }
                if (parser != null && parser.lexer.isEnabled(Feature.InitStringFieldAsEmpty)) {
                    FieldInfo[] fieldInfoArr = this.beanInfo.fields;
                    int length = fieldInfoArr.length;
                    for (int i2 = 0; i2 < length; i2++) {
                        FieldInfo fieldInfo = fieldInfoArr[i2];
                        if (fieldInfo.fieldClass == String.class) {
                            try {
                                fieldInfo.set(object, "");
                            } catch (Exception e) {
                                throw new JSONException("create instance error, class " + this.clazz.getName(), e);
                            }
                        }
                    }
                }
                return object;
            } catch (ClassNotFoundException e2) {
                throw new JSONException("unable to find class " + parentName);
            } catch (NoSuchMethodException e3) {
                throw new RuntimeException(e3);
            } catch (InvocationTargetException e4) {
                throw new RuntimeException("can not instantiate " + clsName);
            } catch (IllegalAccessException e5) {
                throw new RuntimeException(e5);
            } catch (InstantiationException e6) {
                throw new RuntimeException(e6);
            } catch (Exception e7) {
                throw new JSONException("create instance error, class " + this.clazz.getName(), e7);
            }
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        return deserialze(parser, type, fieldName, 0);
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName, int features) {
        return deserialze(parser, type, fieldName, (Object) null, features);
    }

    public <T> T deserialzeArrayMapping(DefaultJSONParser parser, Type type, Object fieldName, Object object) {
        Enum value;
        JSONLexer lexer = parser.lexer;
        if (lexer.token() != 14) {
            throw new JSONException("error");
        }
        T createInstance = createInstance(parser, type);
        int i = 0;
        int size = this.sortedFieldDeserializers.length;
        while (i < size) {
            char seperator = i == size + -1 ? ']' : ',';
            FieldDeserializer fieldDeser = this.sortedFieldDeserializers[i];
            Class<?> fieldClass = fieldDeser.fieldInfo.fieldClass;
            if (fieldClass == Integer.TYPE) {
                fieldDeser.setValue((Object) createInstance, lexer.scanInt(seperator));
            } else if (fieldClass == String.class) {
                fieldDeser.setValue((Object) createInstance, lexer.scanString(seperator));
            } else if (fieldClass == Long.TYPE) {
                fieldDeser.setValue((Object) createInstance, lexer.scanLong(seperator));
            } else if (fieldClass.isEnum()) {
                char ch = lexer.getCurrent();
                if (ch == '\"' || ch == 'n') {
                    value = lexer.scanEnum(fieldClass, parser.getSymbolTable(), seperator);
                } else if (ch < '0' || ch > '9') {
                    value = scanEnum(lexer, seperator);
                } else {
                    value = ((EnumDeserializer) ((DefaultFieldDeserializer) fieldDeser).getFieldValueDeserilizer(parser.getConfig())).valueOf(lexer.scanInt(seperator));
                }
                fieldDeser.setValue((Object) createInstance, (Object) value);
            } else if (fieldClass == Boolean.TYPE) {
                fieldDeser.setValue((Object) createInstance, lexer.scanBoolean(seperator));
            } else if (fieldClass == Float.TYPE) {
                fieldDeser.setValue((Object) createInstance, (Object) Float.valueOf(lexer.scanFloat(seperator)));
            } else if (fieldClass == Double.TYPE) {
                fieldDeser.setValue((Object) createInstance, (Object) Double.valueOf(lexer.scanDouble(seperator)));
            } else if (fieldClass == Date.class && lexer.getCurrent() == '1') {
                fieldDeser.setValue((Object) createInstance, (Object) new Date(lexer.scanLong(seperator)));
            } else {
                lexer.nextToken(14);
                fieldDeser.setValue((Object) createInstance, parser.parseObject(fieldDeser.fieldInfo.fieldType));
                check(lexer, seperator == ']' ? 15 : 16);
            }
            i++;
        }
        lexer.nextToken(16);
        return createInstance;
    }

    /* access modifiers changed from: protected */
    public void check(JSONLexer lexer, int token) {
        if (lexer.token() != token) {
            throw new JSONException("syntax error");
        }
    }

    /* access modifiers changed from: protected */
    public Enum<?> scanEnum(JSONLexer lexer, char seperator) {
        throw new JSONException("illegal enum. " + lexer.info());
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:321:0x062b, code lost:
        throw new com.alibaba.fastjson.JSONException("syntax error, unexpect token " + com.alibaba.fastjson.parser.JSONToken.name(r31.token()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:361:0x06db, code lost:
        r17 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:364:0x0701, code lost:
        throw new com.alibaba.fastjson.JSONException("create instance error, " + r47.beanInfo.creatorConstructor.toGenericString(), r17);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:370:0x0718, code lost:
        r17 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:373:0x073e, code lost:
        throw new com.alibaba.fastjson.JSONException("create factory method error, " + r47.beanInfo.factoryMethod.toString(), r17);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:380:0x0755, code lost:
        r17 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:383:0x0760, code lost:
        throw new com.alibaba.fastjson.JSONException("build object error", r17);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0135, code lost:
        r4 = th;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:15:0x0042, B:352:0x06ba, B:368:0x070c, B:375:0x0740] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0081 A[Catch:{ Exception -> 0x0755, Exception -> 0x0718, Exception -> 0x06db, all -> 0x0135 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T deserialze(com.alibaba.fastjson.parser.DefaultJSONParser r48, java.lang.reflect.Type r49, java.lang.Object r50, java.lang.Object r51, int r52) {
        /*
            r47 = this;
            java.lang.Class<com.alibaba.fastjson.JSON> r4 = com.alibaba.fastjson.JSON.class
            r0 = r49
            if (r0 == r4) goto L_0x000c
            java.lang.Class<com.alibaba.fastjson.JSONObject> r4 = com.alibaba.fastjson.JSONObject.class
            r0 = r49
            if (r0 != r4) goto L_0x0011
        L_0x000c:
            java.lang.Object r12 = r48.parse()
        L_0x0010:
            return r12
        L_0x0011:
            r0 = r48
            com.alibaba.fastjson.parser.JSONLexer r0 = r0.lexer
            r31 = r0
            com.alibaba.fastjson.parser.JSONLexerBase r31 = (com.alibaba.fastjson.parser.JSONLexerBase) r31
            int r43 = r31.token()
            r4 = 8
            r0 = r43
            if (r0 != r4) goto L_0x002c
            r4 = 16
            r0 = r31
            r0.nextToken(r4)
            r12 = 0
            goto L_0x0010
        L_0x002c:
            com.alibaba.fastjson.parser.ParseContext r15 = r48.getContext()
            if (r51 == 0) goto L_0x0036
            if (r15 == 0) goto L_0x0036
            com.alibaba.fastjson.parser.ParseContext r15 = r15.parent
        L_0x0036:
            r13 = 0
            r9 = 0
            r4 = 13
            r0 = r43
            if (r0 != r4) goto L_0x0059
            r4 = 16
            r0 = r31
            r0.nextToken(r4)     // Catch:{ all -> 0x0135 }
            if (r51 != 0) goto L_0x004b
            java.lang.Object r51 = r47.createInstance((com.alibaba.fastjson.parser.DefaultJSONParser) r48, (java.lang.reflect.Type) r49)     // Catch:{ all -> 0x0135 }
        L_0x004b:
            if (r13 == 0) goto L_0x0051
            r0 = r51
            r13.object = r0
        L_0x0051:
            r0 = r48
            r0.setContext(r15)
            r12 = r51
            goto L_0x0010
        L_0x0059:
            r4 = 14
            r0 = r43
            if (r0 != r4) goto L_0x0094
            com.alibaba.fastjson.parser.Feature r4 = com.alibaba.fastjson.parser.Feature.SupportArrayToBean     // Catch:{ all -> 0x0135 }
            int r0 = r4.mask     // Catch:{ all -> 0x0135 }
            r32 = r0
            r0 = r47
            com.alibaba.fastjson.util.JavaBeanInfo r4 = r0.beanInfo     // Catch:{ all -> 0x0135 }
            int r4 = r4.parserFeatures     // Catch:{ all -> 0x0135 }
            r4 = r4 & r32
            if (r4 != 0) goto L_0x007d
            com.alibaba.fastjson.parser.Feature r4 = com.alibaba.fastjson.parser.Feature.SupportArrayToBean     // Catch:{ all -> 0x0135 }
            r0 = r31
            boolean r4 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r4)     // Catch:{ all -> 0x0135 }
            if (r4 != 0) goto L_0x007d
            r4 = r52 & r32
            if (r4 == 0) goto L_0x0091
        L_0x007d:
            r30 = 1
        L_0x007f:
            if (r30 == 0) goto L_0x0094
            java.lang.Object r12 = r47.deserialzeArrayMapping(r48, r49, r50, r51)     // Catch:{ all -> 0x0135 }
            if (r13 == 0) goto L_0x008b
            r0 = r51
            r13.object = r0
        L_0x008b:
            r0 = r48
            r0.setContext(r15)
            goto L_0x0010
        L_0x0091:
            r30 = 0
            goto L_0x007f
        L_0x0094:
            r4 = 12
            r0 = r43
            if (r0 == r4) goto L_0x0142
            r4 = 16
            r0 = r43
            if (r0 == r4) goto L_0x0142
            boolean r4 = r31.isBlankInput()     // Catch:{ all -> 0x0135 }
            if (r4 == 0) goto L_0x00b4
            r12 = 0
            if (r13 == 0) goto L_0x00ad
            r0 = r51
            r13.object = r0
        L_0x00ad:
            r0 = r48
            r0.setContext(r15)
            goto L_0x0010
        L_0x00b4:
            r4 = 4
            r0 = r43
            if (r0 != r4) goto L_0x00d4
            java.lang.String r42 = r31.stringVal()     // Catch:{ all -> 0x0135 }
            int r4 = r42.length()     // Catch:{ all -> 0x0135 }
            if (r4 != 0) goto L_0x00d4
            r31.nextToken()     // Catch:{ all -> 0x0135 }
            r12 = 0
            if (r13 == 0) goto L_0x00cd
            r0 = r51
            r13.object = r0
        L_0x00cd:
            r0 = r48
            r0.setContext(r15)
            goto L_0x0010
        L_0x00d4:
            r4 = 14
            r0 = r43
            if (r0 != r4) goto L_0x00f6
            char r4 = r31.getCurrent()     // Catch:{ all -> 0x0135 }
            r5 = 93
            if (r4 != r5) goto L_0x00f6
            r31.next()     // Catch:{ all -> 0x0135 }
            r31.nextToken()     // Catch:{ all -> 0x0135 }
            r12 = 0
            if (r13 == 0) goto L_0x00ef
            r0 = r51
            r13.object = r0
        L_0x00ef:
            r0 = r48
            r0.setContext(r15)
            goto L_0x0010
        L_0x00f6:
            java.lang.StringBuffer r4 = new java.lang.StringBuffer     // Catch:{ all -> 0x0135 }
            r4.<init>()     // Catch:{ all -> 0x0135 }
            java.lang.String r5 = "syntax error, expect {, actual "
            java.lang.StringBuffer r4 = r4.append(r5)     // Catch:{ all -> 0x0135 }
            java.lang.String r5 = r31.tokenName()     // Catch:{ all -> 0x0135 }
            java.lang.StringBuffer r4 = r4.append(r5)     // Catch:{ all -> 0x0135 }
            java.lang.String r5 = ", pos "
            java.lang.StringBuffer r4 = r4.append(r5)     // Catch:{ all -> 0x0135 }
            int r5 = r31.pos()     // Catch:{ all -> 0x0135 }
            java.lang.StringBuffer r10 = r4.append(r5)     // Catch:{ all -> 0x0135 }
            r0 = r50
            boolean r4 = r0 instanceof java.lang.String     // Catch:{ all -> 0x0135 }
            if (r4 == 0) goto L_0x012b
            java.lang.String r4 = ", fieldName "
            java.lang.StringBuffer r4 = r10.append(r4)     // Catch:{ all -> 0x0135 }
            r0 = r50
            r4.append(r0)     // Catch:{ all -> 0x0135 }
        L_0x012b:
            com.alibaba.fastjson.JSONException r4 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0135 }
            java.lang.String r5 = r10.toString()     // Catch:{ all -> 0x0135 }
            r4.<init>(r5)     // Catch:{ all -> 0x0135 }
            throw r4     // Catch:{ all -> 0x0135 }
        L_0x0135:
            r4 = move-exception
        L_0x0136:
            if (r13 == 0) goto L_0x013c
            r0 = r51
            r13.object = r0
        L_0x013c:
            r0 = r48
            r0.setContext(r15)
            throw r4
        L_0x0142:
            r0 = r48
            int r4 = r0.resolveStatus     // Catch:{ all -> 0x0135 }
            r5 = 2
            if (r4 != r5) goto L_0x014e
            r4 = 0
            r0 = r48
            r0.resolveStatus = r4     // Catch:{ all -> 0x0135 }
        L_0x014e:
            r22 = 0
            r28 = r9
        L_0x0152:
            r6 = 0
            r21 = 0
            r23 = 0
            r20 = 0
            r19 = 0
            r0 = r47
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r4 = r0.sortedFieldDeserializers     // Catch:{ all -> 0x03fb }
            int r4 = r4.length     // Catch:{ all -> 0x03fb }
            r0 = r22
            if (r0 >= r4) goto L_0x017a
            r0 = r47
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r4 = r0.sortedFieldDeserializers     // Catch:{ all -> 0x03fb }
            r21 = r4[r22]     // Catch:{ all -> 0x03fb }
            r0 = r21
            com.alibaba.fastjson.util.FieldInfo r0 = r0.fieldInfo     // Catch:{ all -> 0x03fb }
            r23 = r0
            r0 = r23
            java.lang.Class<?> r0 = r0.fieldClass     // Catch:{ all -> 0x03fb }
            r20 = r0
            com.alibaba.fastjson.annotation.JSONField r19 = r23.getAnnotation()     // Catch:{ all -> 0x03fb }
        L_0x017a:
            r34 = 0
            r46 = 0
            r26 = 0
            if (r21 == 0) goto L_0x01aa
            r0 = r23
            char[] r0 = r0.name_chars     // Catch:{ all -> 0x03fb }
            r35 = r0
            java.lang.Class r4 = java.lang.Integer.TYPE     // Catch:{ all -> 0x03fb }
            r0 = r20
            if (r0 == r4) goto L_0x0194
            java.lang.Class<java.lang.Integer> r4 = java.lang.Integer.class
            r0 = r20
            if (r0 != r4) goto L_0x01fd
        L_0x0194:
            r0 = r31
            r1 = r35
            int r4 = r0.scanFieldInt(r1)     // Catch:{ all -> 0x03fb }
            java.lang.Integer r26 = java.lang.Integer.valueOf(r4)     // Catch:{ all -> 0x03fb }
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            if (r4 <= 0) goto L_0x01ee
            r34 = 1
            r46 = 1
        L_0x01aa:
            if (r34 != 0) goto L_0x0541
            r0 = r48
            com.alibaba.fastjson.parser.SymbolTable r4 = r0.symbolTable     // Catch:{ all -> 0x03fb }
            r0 = r31
            java.lang.String r6 = r0.scanSymbol(r4)     // Catch:{ all -> 0x03fb }
            if (r6 != 0) goto L_0x03bc
            int r43 = r31.token()     // Catch:{ all -> 0x03fb }
            r4 = 13
            r0 = r43
            if (r0 != r4) goto L_0x03a8
            r4 = 16
            r0 = r31
            r0.nextToken(r4)     // Catch:{ all -> 0x03fb }
            r9 = r28
        L_0x01cb:
            if (r51 != 0) goto L_0x06c4
            if (r9 != 0) goto L_0x062c
            java.lang.Object r51 = r47.createInstance((com.alibaba.fastjson.parser.DefaultJSONParser) r48, (java.lang.reflect.Type) r49)     // Catch:{ all -> 0x0135 }
            if (r13 != 0) goto L_0x01df
            r0 = r48
            r1 = r51
            r2 = r50
            com.alibaba.fastjson.parser.ParseContext r13 = r0.setContext(r15, r1, r2)     // Catch:{ all -> 0x0135 }
        L_0x01df:
            if (r13 == 0) goto L_0x01e5
            r0 = r51
            r13.object = r0
        L_0x01e5:
            r0 = r48
            r0.setContext(r15)
            r12 = r51
            goto L_0x0010
        L_0x01ee:
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            r5 = -2
            if (r4 != r5) goto L_0x01aa
            r9 = r28
        L_0x01f7:
            int r22 = r22 + 1
            r28 = r9
            goto L_0x0152
        L_0x01fd:
            java.lang.Class r4 = java.lang.Long.TYPE     // Catch:{ all -> 0x03fb }
            r0 = r20
            if (r0 == r4) goto L_0x0209
            java.lang.Class<java.lang.Long> r4 = java.lang.Long.class
            r0 = r20
            if (r0 != r4) goto L_0x022a
        L_0x0209:
            r0 = r31
            r1 = r35
            long r4 = r0.scanFieldLong(r1)     // Catch:{ all -> 0x03fb }
            java.lang.Long r26 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x03fb }
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            if (r4 <= 0) goto L_0x0220
            r34 = 1
            r46 = 1
            goto L_0x01aa
        L_0x0220:
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            r5 = -2
            if (r4 != r5) goto L_0x01aa
            r9 = r28
            goto L_0x01f7
        L_0x022a:
            java.lang.Class<java.lang.String> r4 = java.lang.String.class
            r0 = r20
            if (r0 != r4) goto L_0x024e
            r0 = r31
            r1 = r35
            java.lang.String r26 = r0.scanFieldString(r1)     // Catch:{ all -> 0x03fb }
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            if (r4 <= 0) goto L_0x0244
            r34 = 1
            r46 = 1
            goto L_0x01aa
        L_0x0244:
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            r5 = -2
            if (r4 != r5) goto L_0x01aa
            r9 = r28
            goto L_0x01f7
        L_0x024e:
            java.lang.Class r4 = java.lang.Boolean.TYPE     // Catch:{ all -> 0x03fb }
            r0 = r20
            if (r0 == r4) goto L_0x025a
            java.lang.Class<java.lang.Boolean> r4 = java.lang.Boolean.class
            r0 = r20
            if (r0 != r4) goto L_0x027d
        L_0x025a:
            r0 = r31
            r1 = r35
            boolean r4 = r0.scanFieldBoolean(r1)     // Catch:{ all -> 0x03fb }
            java.lang.Boolean r26 = java.lang.Boolean.valueOf(r4)     // Catch:{ all -> 0x03fb }
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            if (r4 <= 0) goto L_0x0272
            r34 = 1
            r46 = 1
            goto L_0x01aa
        L_0x0272:
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            r5 = -2
            if (r4 != r5) goto L_0x01aa
            r9 = r28
            goto L_0x01f7
        L_0x027d:
            java.lang.Class r4 = java.lang.Float.TYPE     // Catch:{ all -> 0x03fb }
            r0 = r20
            if (r0 == r4) goto L_0x0289
            java.lang.Class<java.lang.Float> r4 = java.lang.Float.class
            r0 = r20
            if (r0 != r4) goto L_0x02ac
        L_0x0289:
            r0 = r31
            r1 = r35
            float r4 = r0.scanFieldFloat(r1)     // Catch:{ all -> 0x03fb }
            java.lang.Float r26 = java.lang.Float.valueOf(r4)     // Catch:{ all -> 0x03fb }
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            if (r4 <= 0) goto L_0x02a1
            r34 = 1
            r46 = 1
            goto L_0x01aa
        L_0x02a1:
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            r5 = -2
            if (r4 != r5) goto L_0x01aa
            r9 = r28
            goto L_0x01f7
        L_0x02ac:
            java.lang.Class r4 = java.lang.Double.TYPE     // Catch:{ all -> 0x03fb }
            r0 = r20
            if (r0 == r4) goto L_0x02b8
            java.lang.Class<java.lang.Double> r4 = java.lang.Double.class
            r0 = r20
            if (r0 != r4) goto L_0x02db
        L_0x02b8:
            r0 = r31
            r1 = r35
            double r4 = r0.scanFieldDouble(r1)     // Catch:{ all -> 0x03fb }
            java.lang.Double r26 = java.lang.Double.valueOf(r4)     // Catch:{ all -> 0x03fb }
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            if (r4 <= 0) goto L_0x02d0
            r34 = 1
            r46 = 1
            goto L_0x01aa
        L_0x02d0:
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            r5 = -2
            if (r4 != r5) goto L_0x01aa
            r9 = r28
            goto L_0x01f7
        L_0x02db:
            boolean r4 = r20.isEnum()     // Catch:{ all -> 0x03fb }
            if (r4 == 0) goto L_0x032b
            com.alibaba.fastjson.parser.ParserConfig r4 = r48.getConfig()     // Catch:{ all -> 0x03fb }
            r0 = r20
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r4 = r4.getDeserializer((java.lang.reflect.Type) r0)     // Catch:{ all -> 0x03fb }
            boolean r4 = r4 instanceof com.alibaba.fastjson.parser.deserializer.EnumDeserializer     // Catch:{ all -> 0x03fb }
            if (r4 == 0) goto L_0x032b
            if (r19 == 0) goto L_0x02f9
            java.lang.Class r4 = r19.deserializeUsing()     // Catch:{ all -> 0x03fb }
            java.lang.Class<java.lang.Void> r5 = java.lang.Void.class
            if (r4 != r5) goto L_0x032b
        L_0x02f9:
            r0 = r21
            boolean r4 = r0 instanceof com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer     // Catch:{ all -> 0x03fb }
            if (r4 == 0) goto L_0x01aa
            r0 = r21
            com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer r0 = (com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer) r0     // Catch:{ all -> 0x03fb }
            r4 = r0
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r0 = r4.fieldValueDeserilizer     // Catch:{ all -> 0x03fb }
            r27 = r0
            r0 = r47
            r1 = r31
            r2 = r35
            r3 = r27
            java.lang.Enum r26 = r0.scanEnum(r1, r2, r3)     // Catch:{ all -> 0x03fb }
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            if (r4 <= 0) goto L_0x0320
            r34 = 1
            r46 = 1
            goto L_0x01aa
        L_0x0320:
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            r5 = -2
            if (r4 != r5) goto L_0x01aa
            r9 = r28
            goto L_0x01f7
        L_0x032b:
            java.lang.Class<int[]> r4 = int[].class
            r0 = r20
            if (r0 != r4) goto L_0x0350
            r0 = r31
            r1 = r35
            int[] r26 = r0.scanFieldIntArray(r1)     // Catch:{ all -> 0x03fb }
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            if (r4 <= 0) goto L_0x0345
            r34 = 1
            r46 = 1
            goto L_0x01aa
        L_0x0345:
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            r5 = -2
            if (r4 != r5) goto L_0x01aa
            r9 = r28
            goto L_0x01f7
        L_0x0350:
            java.lang.Class<float[]> r4 = float[].class
            r0 = r20
            if (r0 != r4) goto L_0x0375
            r0 = r31
            r1 = r35
            float[] r26 = r0.scanFieldFloatArray(r1)     // Catch:{ all -> 0x03fb }
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            if (r4 <= 0) goto L_0x036a
            r34 = 1
            r46 = 1
            goto L_0x01aa
        L_0x036a:
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            r5 = -2
            if (r4 != r5) goto L_0x01aa
            r9 = r28
            goto L_0x01f7
        L_0x0375:
            java.lang.Class<float[][]> r4 = float[][].class
            r0 = r20
            if (r0 != r4) goto L_0x039a
            r0 = r31
            r1 = r35
            float[][] r26 = r0.scanFieldFloatArray2(r1)     // Catch:{ all -> 0x03fb }
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            if (r4 <= 0) goto L_0x038f
            r34 = 1
            r46 = 1
            goto L_0x01aa
        L_0x038f:
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x03fb }
            r5 = -2
            if (r4 != r5) goto L_0x01aa
            r9 = r28
            goto L_0x01f7
        L_0x039a:
            r0 = r31
            r1 = r35
            boolean r4 = r0.matchField(r1)     // Catch:{ all -> 0x03fb }
            if (r4 == 0) goto L_0x0769
            r34 = 1
            goto L_0x01aa
        L_0x03a8:
            r4 = 16
            r0 = r43
            if (r0 != r4) goto L_0x03bc
            com.alibaba.fastjson.parser.Feature r4 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas     // Catch:{ all -> 0x03fb }
            r0 = r31
            boolean r4 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r4)     // Catch:{ all -> 0x03fb }
            if (r4 == 0) goto L_0x03bc
            r9 = r28
            goto L_0x01f7
        L_0x03bc:
            java.lang.String r4 = "$ref"
            if (r4 != r6) goto L_0x04bc
            r4 = 4
            r0 = r31
            r0.nextTokenWithColon(r4)     // Catch:{ all -> 0x03fb }
            int r43 = r31.token()     // Catch:{ all -> 0x03fb }
            r4 = 4
            r0 = r43
            if (r0 != r4) goto L_0x047f
            java.lang.String r39 = r31.stringVal()     // Catch:{ all -> 0x03fb }
            java.lang.String r4 = "@"
            r0 = r39
            boolean r4 = r4.equals(r0)     // Catch:{ all -> 0x03fb }
            if (r4 == 0) goto L_0x0400
            java.lang.Object r0 = r15.object     // Catch:{ all -> 0x03fb }
            r51 = r0
        L_0x03e3:
            r4 = 13
            r0 = r31
            r0.nextToken(r4)     // Catch:{ all -> 0x03fb }
            int r4 = r31.token()     // Catch:{ all -> 0x03fb }
            r5 = 13
            if (r4 == r5) goto L_0x049d
            com.alibaba.fastjson.JSONException r4 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x03fb }
            java.lang.String r5 = "illegal ref"
            r4.<init>(r5)     // Catch:{ all -> 0x03fb }
            throw r4     // Catch:{ all -> 0x03fb }
        L_0x03fb:
            r4 = move-exception
            r9 = r28
            goto L_0x0136
        L_0x0400:
            java.lang.String r4 = ".."
            r0 = r39
            boolean r4 = r4.equals(r0)     // Catch:{ all -> 0x03fb }
            if (r4 == 0) goto L_0x0430
            com.alibaba.fastjson.parser.ParseContext r0 = r15.parent     // Catch:{ all -> 0x03fb }
            r38 = r0
            r0 = r38
            java.lang.Object r4 = r0.object     // Catch:{ all -> 0x03fb }
            if (r4 == 0) goto L_0x041c
            r0 = r38
            java.lang.Object r0 = r0.object     // Catch:{ all -> 0x03fb }
            r51 = r0
            goto L_0x03e3
        L_0x041c:
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r4 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x03fb }
            r0 = r38
            r1 = r39
            r4.<init>(r0, r1)     // Catch:{ all -> 0x03fb }
            r0 = r48
            r0.addResolveTask(r4)     // Catch:{ all -> 0x03fb }
            r4 = 1
            r0 = r48
            r0.resolveStatus = r4     // Catch:{ all -> 0x03fb }
            goto L_0x03e3
        L_0x0430:
            java.lang.String r4 = "$"
            r0 = r39
            boolean r4 = r4.equals(r0)     // Catch:{ all -> 0x03fb }
            if (r4 == 0) goto L_0x046c
            r40 = r15
        L_0x043d:
            r0 = r40
            com.alibaba.fastjson.parser.ParseContext r4 = r0.parent     // Catch:{ all -> 0x03fb }
            if (r4 == 0) goto L_0x044a
            r0 = r40
            com.alibaba.fastjson.parser.ParseContext r0 = r0.parent     // Catch:{ all -> 0x03fb }
            r40 = r0
            goto L_0x043d
        L_0x044a:
            r0 = r40
            java.lang.Object r4 = r0.object     // Catch:{ all -> 0x03fb }
            if (r4 == 0) goto L_0x0457
            r0 = r40
            java.lang.Object r0 = r0.object     // Catch:{ all -> 0x03fb }
            r51 = r0
            goto L_0x03e3
        L_0x0457:
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r4 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x03fb }
            r0 = r40
            r1 = r39
            r4.<init>(r0, r1)     // Catch:{ all -> 0x03fb }
            r0 = r48
            r0.addResolveTask(r4)     // Catch:{ all -> 0x03fb }
            r4 = 1
            r0 = r48
            r0.resolveStatus = r4     // Catch:{ all -> 0x03fb }
            goto L_0x03e3
        L_0x046c:
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r4 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x03fb }
            r0 = r39
            r4.<init>(r15, r0)     // Catch:{ all -> 0x03fb }
            r0 = r48
            r0.addResolveTask(r4)     // Catch:{ all -> 0x03fb }
            r4 = 1
            r0 = r48
            r0.resolveStatus = r4     // Catch:{ all -> 0x03fb }
            goto L_0x03e3
        L_0x047f:
            com.alibaba.fastjson.JSONException r4 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x03fb }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x03fb }
            r5.<init>()     // Catch:{ all -> 0x03fb }
            java.lang.String r7 = "illegal ref, "
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ all -> 0x03fb }
            java.lang.String r7 = com.alibaba.fastjson.parser.JSONToken.name(r43)     // Catch:{ all -> 0x03fb }
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ all -> 0x03fb }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x03fb }
            r4.<init>(r5)     // Catch:{ all -> 0x03fb }
            throw r4     // Catch:{ all -> 0x03fb }
        L_0x049d:
            r4 = 16
            r0 = r31
            r0.nextToken(r4)     // Catch:{ all -> 0x03fb }
            r0 = r48
            r1 = r51
            r2 = r50
            r0.setContext(r15, r1, r2)     // Catch:{ all -> 0x03fb }
            if (r13 == 0) goto L_0x04b3
            r0 = r51
            r13.object = r0
        L_0x04b3:
            r0 = r48
            r0.setContext(r15)
            r12 = r51
            goto L_0x0010
        L_0x04bc:
            java.lang.String r4 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ all -> 0x03fb }
            if (r4 != r6) goto L_0x0541
            r4 = 4
            r0 = r31
            r0.nextTokenWithColon(r4)     // Catch:{ all -> 0x03fb }
            int r4 = r31.token()     // Catch:{ all -> 0x03fb }
            r5 = 4
            if (r4 != r5) goto L_0x0538
            java.lang.String r44 = r31.stringVal()     // Catch:{ all -> 0x03fb }
            r4 = 16
            r0 = r31
            r0.nextToken(r4)     // Catch:{ all -> 0x03fb }
            r0 = r47
            com.alibaba.fastjson.util.JavaBeanInfo r4 = r0.beanInfo     // Catch:{ all -> 0x03fb }
            java.lang.String r4 = r4.typeName     // Catch:{ all -> 0x03fb }
            r0 = r44
            boolean r4 = r0.equals(r4)     // Catch:{ all -> 0x03fb }
            if (r4 == 0) goto L_0x04f5
            int r4 = r31.token()     // Catch:{ all -> 0x03fb }
            r5 = 13
            if (r4 != r5) goto L_0x0769
            r31.nextToken()     // Catch:{ all -> 0x03fb }
            r9 = r28
            goto L_0x01cb
        L_0x04f5:
            com.alibaba.fastjson.parser.ParserConfig r14 = r48.getConfig()     // Catch:{ all -> 0x03fb }
            r0 = r47
            com.alibaba.fastjson.util.JavaBeanInfo r4 = r0.beanInfo     // Catch:{ all -> 0x03fb }
            r0 = r47
            r1 = r44
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r16 = r0.getSeeAlso(r14, r4, r1)     // Catch:{ all -> 0x03fb }
            r45 = 0
            if (r16 != 0) goto L_0x051f
            java.lang.Class r18 = com.alibaba.fastjson.util.TypeUtils.getClass(r49)     // Catch:{ all -> 0x03fb }
            r0 = r44
            r1 = r18
            java.lang.Class r45 = r14.checkAutoType(r0, r1)     // Catch:{ all -> 0x03fb }
            com.alibaba.fastjson.parser.ParserConfig r4 = r48.getConfig()     // Catch:{ all -> 0x03fb }
            r0 = r45
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r16 = r4.getDeserializer((java.lang.reflect.Type) r0)     // Catch:{ all -> 0x03fb }
        L_0x051f:
            r0 = r16
            r1 = r48
            r2 = r45
            r3 = r50
            java.lang.Object r12 = r0.deserialze(r1, r2, r3)     // Catch:{ all -> 0x03fb }
            if (r13 == 0) goto L_0x0531
            r0 = r51
            r13.object = r0
        L_0x0531:
            r0 = r48
            r0.setContext(r15)
            goto L_0x0010
        L_0x0538:
            com.alibaba.fastjson.JSONException r4 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x03fb }
            java.lang.String r5 = "syntax error"
            r4.<init>(r5)     // Catch:{ all -> 0x03fb }
            throw r4     // Catch:{ all -> 0x03fb }
        L_0x0541:
            if (r51 != 0) goto L_0x0765
            if (r28 != 0) goto L_0x0765
            java.lang.Object r51 = r47.createInstance((com.alibaba.fastjson.parser.DefaultJSONParser) r48, (java.lang.reflect.Type) r49)     // Catch:{ all -> 0x03fb }
            if (r51 != 0) goto L_0x0761
            java.util.HashMap r9 = new java.util.HashMap     // Catch:{ all -> 0x03fb }
            r0 = r47
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r4 = r0.fieldDeserializers     // Catch:{ all -> 0x03fb }
            int r4 = r4.length     // Catch:{ all -> 0x03fb }
            r9.<init>(r4)     // Catch:{ all -> 0x03fb }
        L_0x0555:
            r0 = r48
            r1 = r51
            r2 = r50
            com.alibaba.fastjson.parser.ParseContext r13 = r0.setContext(r15, r1, r2)     // Catch:{ all -> 0x0135 }
        L_0x055f:
            if (r34 == 0) goto L_0x05cf
            if (r46 != 0) goto L_0x0587
            r0 = r21
            r1 = r48
            r2 = r51
            r3 = r49
            r0.parseField(r1, r2, r3, r9)     // Catch:{ all -> 0x0135 }
        L_0x056e:
            int r4 = r31.token()     // Catch:{ all -> 0x0135 }
            r5 = 16
            if (r4 == r5) goto L_0x01f7
            int r4 = r31.token()     // Catch:{ all -> 0x0135 }
            r5 = 13
            if (r4 != r5) goto L_0x05fb
            r4 = 16
            r0 = r31
            r0.nextToken(r4)     // Catch:{ all -> 0x0135 }
            goto L_0x01cb
        L_0x0587:
            if (r51 != 0) goto L_0x059b
            r0 = r23
            java.lang.String r4 = r0.name     // Catch:{ all -> 0x0135 }
            r0 = r26
            r9.put(r4, r0)     // Catch:{ all -> 0x0135 }
        L_0x0592:
            r0 = r31
            int r4 = r0.matchStat     // Catch:{ all -> 0x0135 }
            r5 = 4
            if (r4 != r5) goto L_0x056e
            goto L_0x01cb
        L_0x059b:
            if (r26 != 0) goto L_0x05c5
            java.lang.Class r4 = java.lang.Integer.TYPE     // Catch:{ all -> 0x0135 }
            r0 = r20
            if (r0 == r4) goto L_0x0592
            java.lang.Class r4 = java.lang.Long.TYPE     // Catch:{ all -> 0x0135 }
            r0 = r20
            if (r0 == r4) goto L_0x0592
            java.lang.Class r4 = java.lang.Float.TYPE     // Catch:{ all -> 0x0135 }
            r0 = r20
            if (r0 == r4) goto L_0x0592
            java.lang.Class r4 = java.lang.Double.TYPE     // Catch:{ all -> 0x0135 }
            r0 = r20
            if (r0 == r4) goto L_0x0592
            java.lang.Class r4 = java.lang.Boolean.TYPE     // Catch:{ all -> 0x0135 }
            r0 = r20
            if (r0 == r4) goto L_0x0592
            r0 = r21
            r1 = r51
            r2 = r26
            r0.setValue((java.lang.Object) r1, (java.lang.Object) r2)     // Catch:{ all -> 0x0135 }
            goto L_0x0592
        L_0x05c5:
            r0 = r21
            r1 = r51
            r2 = r26
            r0.setValue((java.lang.Object) r1, (java.lang.Object) r2)     // Catch:{ all -> 0x0135 }
            goto L_0x0592
        L_0x05cf:
            r4 = r47
            r5 = r48
            r7 = r51
            r8 = r49
            boolean r33 = r4.parseField(r5, r6, r7, r8, r9)     // Catch:{ all -> 0x0135 }
            if (r33 != 0) goto L_0x05ea
            int r4 = r31.token()     // Catch:{ all -> 0x0135 }
            r5 = 13
            if (r4 != r5) goto L_0x01f7
            r31.nextToken()     // Catch:{ all -> 0x0135 }
            goto L_0x01cb
        L_0x05ea:
            int r4 = r31.token()     // Catch:{ all -> 0x0135 }
            r5 = 17
            if (r4 != r5) goto L_0x056e
            com.alibaba.fastjson.JSONException r4 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0135 }
            java.lang.String r5 = "syntax error, unexpect token ':'"
            r4.<init>(r5)     // Catch:{ all -> 0x0135 }
            throw r4     // Catch:{ all -> 0x0135 }
        L_0x05fb:
            int r4 = r31.token()     // Catch:{ all -> 0x0135 }
            r5 = 18
            if (r4 == r5) goto L_0x060a
            int r4 = r31.token()     // Catch:{ all -> 0x0135 }
            r5 = 1
            if (r4 != r5) goto L_0x01f7
        L_0x060a:
            com.alibaba.fastjson.JSONException r4 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0135 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0135 }
            r5.<init>()     // Catch:{ all -> 0x0135 }
            java.lang.String r7 = "syntax error, unexpect token "
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ all -> 0x0135 }
            int r7 = r31.token()     // Catch:{ all -> 0x0135 }
            java.lang.String r7 = com.alibaba.fastjson.parser.JSONToken.name(r7)     // Catch:{ all -> 0x0135 }
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ all -> 0x0135 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0135 }
            r4.<init>(r5)     // Catch:{ all -> 0x0135 }
            throw r4     // Catch:{ all -> 0x0135 }
        L_0x062c:
            r0 = r47
            com.alibaba.fastjson.util.JavaBeanInfo r4 = r0.beanInfo     // Catch:{ all -> 0x0135 }
            com.alibaba.fastjson.util.FieldInfo[] r0 = r4.fields     // Catch:{ all -> 0x0135 }
            r24 = r0
            r0 = r24
            int r0 = r0.length     // Catch:{ all -> 0x0135 }
            r41 = r0
            r0 = r41
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x0135 }
            r37 = r0
            r29 = 0
        L_0x0641:
            r0 = r29
            r1 = r41
            if (r0 >= r1) goto L_0x06b0
            r23 = r24[r29]     // Catch:{ all -> 0x0135 }
            r0 = r23
            java.lang.String r4 = r0.name     // Catch:{ all -> 0x0135 }
            java.lang.Object r36 = r9.get(r4)     // Catch:{ all -> 0x0135 }
            if (r36 != 0) goto L_0x0664
            r0 = r23
            java.lang.reflect.Type r0 = r0.fieldType     // Catch:{ all -> 0x0135 }
            r25 = r0
            java.lang.Class r4 = java.lang.Byte.TYPE     // Catch:{ all -> 0x0135 }
            r0 = r25
            if (r0 != r4) goto L_0x0669
            r4 = 0
            java.lang.Byte r36 = java.lang.Byte.valueOf(r4)     // Catch:{ all -> 0x0135 }
        L_0x0664:
            r37[r29] = r36     // Catch:{ all -> 0x0135 }
            int r29 = r29 + 1
            goto L_0x0641
        L_0x0669:
            java.lang.Class r4 = java.lang.Short.TYPE     // Catch:{ all -> 0x0135 }
            r0 = r25
            if (r0 != r4) goto L_0x0675
            r4 = 0
            java.lang.Short r36 = java.lang.Short.valueOf(r4)     // Catch:{ all -> 0x0135 }
            goto L_0x0664
        L_0x0675:
            java.lang.Class r4 = java.lang.Integer.TYPE     // Catch:{ all -> 0x0135 }
            r0 = r25
            if (r0 != r4) goto L_0x0681
            r4 = 0
            java.lang.Integer r36 = java.lang.Integer.valueOf(r4)     // Catch:{ all -> 0x0135 }
            goto L_0x0664
        L_0x0681:
            java.lang.Class r4 = java.lang.Long.TYPE     // Catch:{ all -> 0x0135 }
            r0 = r25
            if (r0 != r4) goto L_0x068e
            r4 = 0
            java.lang.Long r36 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x0135 }
            goto L_0x0664
        L_0x068e:
            java.lang.Class r4 = java.lang.Float.TYPE     // Catch:{ all -> 0x0135 }
            r0 = r25
            if (r0 != r4) goto L_0x069a
            r4 = 0
            java.lang.Float r36 = java.lang.Float.valueOf(r4)     // Catch:{ all -> 0x0135 }
            goto L_0x0664
        L_0x069a:
            java.lang.Class r4 = java.lang.Double.TYPE     // Catch:{ all -> 0x0135 }
            r0 = r25
            if (r0 != r4) goto L_0x06a7
            r4 = 0
            java.lang.Double r36 = java.lang.Double.valueOf(r4)     // Catch:{ all -> 0x0135 }
            goto L_0x0664
        L_0x06a7:
            java.lang.Class r4 = java.lang.Boolean.TYPE     // Catch:{ all -> 0x0135 }
            r0 = r25
            if (r0 != r4) goto L_0x0664
            java.lang.Boolean r36 = java.lang.Boolean.FALSE     // Catch:{ all -> 0x0135 }
            goto L_0x0664
        L_0x06b0:
            r0 = r47
            com.alibaba.fastjson.util.JavaBeanInfo r4 = r0.beanInfo     // Catch:{ all -> 0x0135 }
            java.lang.reflect.Constructor<?> r4 = r4.creatorConstructor     // Catch:{ all -> 0x0135 }
            if (r4 == 0) goto L_0x0702
            r0 = r47
            com.alibaba.fastjson.util.JavaBeanInfo r4 = r0.beanInfo     // Catch:{ Exception -> 0x06db }
            java.lang.reflect.Constructor<?> r4 = r4.creatorConstructor     // Catch:{ Exception -> 0x06db }
            r0 = r37
            java.lang.Object r51 = r4.newInstance(r0)     // Catch:{ Exception -> 0x06db }
        L_0x06c4:
            r0 = r47
            com.alibaba.fastjson.util.JavaBeanInfo r4 = r0.beanInfo     // Catch:{ all -> 0x0135 }
            java.lang.reflect.Method r11 = r4.buildMethod     // Catch:{ all -> 0x0135 }
            if (r11 != 0) goto L_0x073f
            if (r13 == 0) goto L_0x06d2
            r0 = r51
            r13.object = r0
        L_0x06d2:
            r0 = r48
            r0.setContext(r15)
            r12 = r51
            goto L_0x0010
        L_0x06db:
            r17 = move-exception
            com.alibaba.fastjson.JSONException r4 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0135 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0135 }
            r5.<init>()     // Catch:{ all -> 0x0135 }
            java.lang.String r7 = "create instance error, "
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ all -> 0x0135 }
            r0 = r47
            com.alibaba.fastjson.util.JavaBeanInfo r7 = r0.beanInfo     // Catch:{ all -> 0x0135 }
            java.lang.reflect.Constructor<?> r7 = r7.creatorConstructor     // Catch:{ all -> 0x0135 }
            java.lang.String r7 = r7.toGenericString()     // Catch:{ all -> 0x0135 }
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ all -> 0x0135 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0135 }
            r0 = r17
            r4.<init>(r5, r0)     // Catch:{ all -> 0x0135 }
            throw r4     // Catch:{ all -> 0x0135 }
        L_0x0702:
            r0 = r47
            com.alibaba.fastjson.util.JavaBeanInfo r4 = r0.beanInfo     // Catch:{ all -> 0x0135 }
            java.lang.reflect.Method r4 = r4.factoryMethod     // Catch:{ all -> 0x0135 }
            if (r4 == 0) goto L_0x06c4
            r0 = r47
            com.alibaba.fastjson.util.JavaBeanInfo r4 = r0.beanInfo     // Catch:{ Exception -> 0x0718 }
            java.lang.reflect.Method r4 = r4.factoryMethod     // Catch:{ Exception -> 0x0718 }
            r5 = 0
            r0 = r37
            java.lang.Object r51 = r4.invoke(r5, r0)     // Catch:{ Exception -> 0x0718 }
            goto L_0x06c4
        L_0x0718:
            r17 = move-exception
            com.alibaba.fastjson.JSONException r4 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0135 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0135 }
            r5.<init>()     // Catch:{ all -> 0x0135 }
            java.lang.String r7 = "create factory method error, "
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ all -> 0x0135 }
            r0 = r47
            com.alibaba.fastjson.util.JavaBeanInfo r7 = r0.beanInfo     // Catch:{ all -> 0x0135 }
            java.lang.reflect.Method r7 = r7.factoryMethod     // Catch:{ all -> 0x0135 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0135 }
            java.lang.StringBuilder r5 = r5.append(r7)     // Catch:{ all -> 0x0135 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0135 }
            r0 = r17
            r4.<init>(r5, r0)     // Catch:{ all -> 0x0135 }
            throw r4     // Catch:{ all -> 0x0135 }
        L_0x073f:
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x0755 }
            r0 = r51
            java.lang.Object r12 = r11.invoke(r0, r4)     // Catch:{ Exception -> 0x0755 }
            if (r13 == 0) goto L_0x074e
            r0 = r51
            r13.object = r0
        L_0x074e:
            r0 = r48
            r0.setContext(r15)
            goto L_0x0010
        L_0x0755:
            r17 = move-exception
            com.alibaba.fastjson.JSONException r4 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0135 }
            java.lang.String r5 = "build object error"
            r0 = r17
            r4.<init>(r5, r0)     // Catch:{ all -> 0x0135 }
            throw r4     // Catch:{ all -> 0x0135 }
        L_0x0761:
            r9 = r28
            goto L_0x0555
        L_0x0765:
            r9 = r28
            goto L_0x055f
        L_0x0769:
            r9 = r28
            goto L_0x01f7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.deserialze(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object, java.lang.Object, int):java.lang.Object");
    }

    /* access modifiers changed from: protected */
    public Enum scanEnum(JSONLexerBase lexer, char[] name_chars, ObjectDeserializer fieldValueDeserilizer) {
        EnumDeserializer enumDeserializer = null;
        if (fieldValueDeserilizer instanceof EnumDeserializer) {
            enumDeserializer = (EnumDeserializer) fieldValueDeserilizer;
        }
        if (enumDeserializer == null) {
            lexer.matchStat = -1;
            return null;
        }
        long enumNameHashCode = lexer.scanFieldSymbol(name_chars);
        if (lexer.matchStat > 0) {
            return enumDeserializer.getEnumByHashCode(enumNameHashCode);
        }
        return null;
    }

    public boolean parseField(DefaultJSONParser parser, String key, Object object, Type objectType, Map<String, Object> fieldValues) {
        JSONLexer lexer = parser.lexer;
        FieldDeserializer fieldDeserializer = smartMatch(key);
        int mask = Feature.SupportNonPublicField.mask;
        if (fieldDeserializer == null && (parser.lexer.isEnabled(mask) || (this.beanInfo.parserFeatures & mask) != 0)) {
            if (this.extraFieldDeserializers == null) {
                ConcurrentHashMap extraFieldDeserializers2 = new ConcurrentHashMap(1, 0.75f, 1);
                for (Field field : this.clazz.getDeclaredFields()) {
                    String fieldName = field.getName();
                    if (getFieldDeserializer(fieldName) == null) {
                        int fieldModifiers = field.getModifiers();
                        if ((fieldModifiers & 16) == 0 && (fieldModifiers & 8) == 0) {
                            extraFieldDeserializers2.put(fieldName, field);
                        }
                    }
                }
                this.extraFieldDeserializers = extraFieldDeserializers2;
            }
            Object deserOrField = this.extraFieldDeserializers.get(key);
            if (deserOrField != null) {
                if (deserOrField instanceof FieldDeserializer) {
                    fieldDeserializer = (FieldDeserializer) deserOrField;
                } else {
                    Field field2 = (Field) deserOrField;
                    field2.setAccessible(true);
                    fieldDeserializer = new DefaultFieldDeserializer(parser.getConfig(), this.clazz, new FieldInfo(key, field2.getDeclaringClass(), field2.getType(), field2.getGenericType(), field2, 0, 0, 0));
                    this.extraFieldDeserializers.put(key, fieldDeserializer);
                }
            }
        }
        if (fieldDeserializer == null) {
            if (!lexer.isEnabled(Feature.IgnoreNotMatch)) {
                throw new JSONException("setter not found, class " + this.clazz.getName() + ", property " + key);
            }
            parser.parseExtra(object, key);
            return false;
        }
        lexer.nextTokenWithColon(fieldDeserializer.getFastMatchToken());
        fieldDeserializer.parseField(parser, object, objectType, fieldValues);
        return true;
    }

    public FieldDeserializer smartMatch(String key) {
        if (key == null) {
            return null;
        }
        FieldDeserializer fieldDeserializer = getFieldDeserializer(key);
        if (fieldDeserializer == null) {
            boolean startsWithIs = key.startsWith("is");
            FieldDeserializer[] fieldDeserializerArr = this.sortedFieldDeserializers;
            int length = fieldDeserializerArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                FieldDeserializer fieldDeser = fieldDeserializerArr[i];
                FieldInfo fieldInfo = fieldDeser.fieldInfo;
                Class<?> fieldClass = fieldInfo.fieldClass;
                String fieldName = fieldInfo.name;
                if (!fieldName.equalsIgnoreCase(key)) {
                    if (startsWithIs && ((fieldClass == Boolean.TYPE || fieldClass == Boolean.class) && fieldName.equalsIgnoreCase(key.substring(2)))) {
                        fieldDeserializer = fieldDeser;
                        break;
                    }
                    i++;
                } else {
                    fieldDeserializer = fieldDeser;
                    break;
                }
            }
        }
        if (fieldDeserializer == null) {
            boolean snakeOrkebab = false;
            String key2 = null;
            int i2 = 0;
            while (true) {
                if (i2 >= key.length()) {
                    break;
                }
                char ch = key.charAt(i2);
                if (ch == '_') {
                    snakeOrkebab = true;
                    key2 = key.replaceAll("_", "");
                    break;
                } else if (ch == '-') {
                    snakeOrkebab = true;
                    key2 = key.replaceAll("-", "");
                    break;
                } else {
                    i2++;
                }
            }
            if (snakeOrkebab && (fieldDeserializer = getFieldDeserializer(key2)) == null) {
                FieldDeserializer[] fieldDeserializerArr2 = this.sortedFieldDeserializers;
                int length2 = fieldDeserializerArr2.length;
                int i3 = 0;
                while (true) {
                    if (i3 >= length2) {
                        break;
                    }
                    FieldDeserializer fieldDeser2 = fieldDeserializerArr2[i3];
                    if (fieldDeser2.fieldInfo.name.equalsIgnoreCase(key2)) {
                        fieldDeserializer = fieldDeser2;
                        break;
                    }
                    i3++;
                }
            }
        }
        if (fieldDeserializer != null) {
            return fieldDeserializer;
        }
        for (FieldDeserializer fieldDeser3 : this.sortedFieldDeserializers) {
            if (fieldDeser3.fieldInfo.alternateName(key)) {
                return fieldDeser3;
            }
        }
        return fieldDeserializer;
    }

    public int getFastMatchToken() {
        return 12;
    }

    public Object createInstance(Map<String, Object> map, ParserConfig config) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (this.beanInfo.creatorConstructor == null && this.beanInfo.factoryMethod == null) {
            Object object = createInstance((DefaultJSONParser) null, (Type) this.clazz);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Object value = entry.getValue();
                FieldDeserializer fieldDeser = smartMatch(entry.getKey());
                if (fieldDeser != null) {
                    fieldDeser.setValue(object, TypeUtils.cast(value, fieldDeser.fieldInfo.fieldType, config));
                }
            }
            if (this.beanInfo.buildMethod == null) {
                return object;
            }
            try {
                return this.beanInfo.buildMethod.invoke(object, new Object[0]);
            } catch (Exception e) {
                throw new JSONException("build object error", e);
            }
        } else {
            FieldInfo[] fieldInfoList = this.beanInfo.fields;
            int size = fieldInfoList.length;
            Object[] params = new Object[size];
            for (int i = 0; i < size; i++) {
                params[i] = map.get(fieldInfoList[i].name);
            }
            if (this.beanInfo.creatorConstructor != null) {
                try {
                    return this.beanInfo.creatorConstructor.newInstance(params);
                } catch (Exception e2) {
                    throw new JSONException("create instance error, " + this.beanInfo.creatorConstructor.toGenericString(), e2);
                }
            } else if (this.beanInfo.factoryMethod == null) {
                return null;
            } else {
                try {
                    return this.beanInfo.factoryMethod.invoke((Object) null, params);
                } catch (Exception e3) {
                    throw new JSONException("create factory method error, " + this.beanInfo.factoryMethod.toString(), e3);
                }
            }
        }
    }

    public Type getFieldType(int ordinal) {
        return this.sortedFieldDeserializers[ordinal].fieldInfo.fieldType;
    }

    /* access modifiers changed from: protected */
    public Object parseRest(DefaultJSONParser parser, Type type, Object fieldName, Object instance, int features) {
        return deserialze(parser, type, fieldName, instance, features);
    }

    /* access modifiers changed from: protected */
    public JavaBeanDeserializer getSeeAlso(ParserConfig config, JavaBeanInfo beanInfo2, String typeName) {
        if (beanInfo2.jsonType == null) {
            return null;
        }
        for (Class<?> seeAlsoClass : beanInfo2.jsonType.seeAlso()) {
            ObjectDeserializer seeAlsoDeser = config.getDeserializer((Type) seeAlsoClass);
            if (seeAlsoDeser instanceof JavaBeanDeserializer) {
                JavaBeanDeserializer seeAlsoJavaBeanDeser = (JavaBeanDeserializer) seeAlsoDeser;
                JavaBeanInfo subBeanInfo = seeAlsoJavaBeanDeser.beanInfo;
                if (subBeanInfo.typeName.equals(typeName)) {
                    return seeAlsoJavaBeanDeser;
                }
                JavaBeanDeserializer subSeeAlso = getSeeAlso(config, subBeanInfo, typeName);
                if (subSeeAlso != null) {
                    return subSeeAlso;
                }
            }
        }
        return null;
    }

    protected static void parseArray(Collection collection, ObjectDeserializer deser, DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexerBase lexer = (JSONLexerBase) parser.lexer;
        int token = lexer.token();
        if (token == 8) {
            lexer.nextToken(16);
            int token2 = lexer.token();
            return;
        }
        if (token != 14) {
            parser.throwException(token);
        }
        if (lexer.getCurrent() == '[') {
            lexer.next();
            lexer.setToken(14);
        } else {
            lexer.nextToken(14);
        }
        if (lexer.token() == 15) {
            lexer.nextToken();
            return;
        }
        int index = 0;
        while (true) {
            collection.add(deser.deserialze(parser, type, Integer.valueOf(index)));
            index++;
            if (lexer.token() != 16) {
                break;
            } else if (lexer.getCurrent() == '[') {
                lexer.next();
                lexer.setToken(14);
            } else {
                lexer.nextToken(14);
            }
        }
        int token3 = lexer.token();
        if (token3 != 15) {
            parser.throwException(token3);
        }
        if (lexer.getCurrent() == ',') {
            lexer.next();
            lexer.setToken(16);
            return;
        }
        lexer.nextToken(16);
    }
}
