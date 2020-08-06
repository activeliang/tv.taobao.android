package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessable;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldTypeResolver;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.ResolveFieldDeserializer;
import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.Closeable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import mtopsdk.common.util.SymbolExpUtil;

public class DefaultJSONParser implements Closeable {
    public static final int NONE = 0;
    public static final int NeedToResolve = 1;
    public static final int TypeNameRedirect = 2;
    private static final Set<Class<?>> primitiveClasses = new HashSet();
    private String[] autoTypeAccept;
    private boolean autoTypeEnable;
    protected ParserConfig config;
    protected ParseContext context;
    private ParseContext[] contextArray;
    private int contextArrayIndex;
    private DateFormat dateFormat;
    private String dateFormatPattern;
    private List<ExtraProcessor> extraProcessors;
    private List<ExtraTypeProvider> extraTypeProviders;
    protected FieldTypeResolver fieldTypeResolver;
    public final Object input;
    protected transient BeanContext lastBeanContext;
    public final JSONLexer lexer;
    public int resolveStatus;
    private List<ResolveTask> resolveTaskList;
    public final SymbolTable symbolTable;

    static {
        for (Class add : new Class[]{Boolean.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Boolean.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, BigInteger.class, BigDecimal.class, String.class}) {
            primitiveClasses.add(add);
        }
    }

    public String getDateFomartPattern() {
        return this.dateFormatPattern;
    }

    public DateFormat getDateFormat() {
        if (this.dateFormat == null) {
            this.dateFormat = new SimpleDateFormat(this.dateFormatPattern, this.lexer.getLocale());
            this.dateFormat.setTimeZone(this.lexer.getTimeZone());
        }
        return this.dateFormat;
    }

    public void setDateFormat(String dateFormat2) {
        this.dateFormatPattern = dateFormat2;
        this.dateFormat = null;
    }

    public void setDateFomrat(DateFormat dateFormat2) {
        this.dateFormat = dateFormat2;
    }

    public DefaultJSONParser(String input2) {
        this(input2, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
    }

    public DefaultJSONParser(String input2, ParserConfig config2) {
        this((Object) input2, (JSONLexer) new JSONScanner(input2, JSON.DEFAULT_PARSER_FEATURE), config2);
    }

    public DefaultJSONParser(String input2, ParserConfig config2, int features) {
        this((Object) input2, (JSONLexer) new JSONScanner(input2, features), config2);
    }

    public DefaultJSONParser(char[] input2, int length, ParserConfig config2, int features) {
        this((Object) input2, (JSONLexer) new JSONScanner(input2, length, features), config2);
    }

    public DefaultJSONParser(JSONLexer lexer2) {
        this(lexer2, ParserConfig.getGlobalInstance());
    }

    public DefaultJSONParser(JSONLexer lexer2, ParserConfig config2) {
        this((Object) null, lexer2, config2);
    }

    public DefaultJSONParser(Object input2, JSONLexer lexer2, ParserConfig config2) {
        this.dateFormatPattern = JSON.DEFFAULT_DATE_FORMAT;
        this.contextArrayIndex = 0;
        this.resolveStatus = 0;
        this.extraTypeProviders = null;
        this.extraProcessors = null;
        this.fieldTypeResolver = null;
        this.autoTypeAccept = null;
        this.lexer = lexer2;
        this.input = input2;
        this.config = config2;
        this.symbolTable = config2.symbolTable;
        int ch = lexer2.getCurrent();
        if (ch == 123) {
            lexer2.next();
            ((JSONLexerBase) lexer2).token = 12;
        } else if (ch == 91) {
            lexer2.next();
            ((JSONLexerBase) lexer2).token = 14;
        } else {
            lexer2.nextToken();
        }
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    public String getInput() {
        if (this.input instanceof char[]) {
            return new String((char[]) this.input);
        }
        return this.input.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:109:0x02f8, code lost:
        r17.nextToken(16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x030b, code lost:
        if (r17.token() != 13) goto L_0x036c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x030d, code lost:
        r17.nextToken(16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:112:0x0316, code lost:
        r13 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:?, code lost:
        r8 = r36.config.getDeserializer((java.lang.reflect.Type) r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:115:0x0327, code lost:
        if ((r8 instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) == false) goto L_0x0331;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:116:0x0329, code lost:
        r13 = ((com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) r8).createInstance(r36, (java.lang.reflect.Type) r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:117:0x0331, code lost:
        if (r13 != null) goto L_0x033e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:119:0x0337, code lost:
        if (r4 != java.lang.Cloneable.class) goto L_0x0347;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:120:0x0339, code lost:
        r13 = new java.util.HashMap();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:121:0x033e, code lost:
        setContext(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:125:0x0352, code lost:
        if ("java.util.Collections$EmptyMap".equals(r31) == false) goto L_0x0359;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:126:0x0354, code lost:
        r13 = java.util.Collections.emptyMap();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:127:0x0359, code lost:
        r13 = r4.newInstance();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:132:0x036c, code lost:
        setResolveStatus(2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:133:0x037b, code lost:
        if (r36.context == null) goto L_0x0388;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:135:0x0383, code lost:
        if ((r38 instanceof java.lang.Integer) != false) goto L_0x0388;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:136:0x0385, code lost:
        popContext();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:138:0x038c, code lost:
        if (r37.size() <= 0) goto L_0x03ac;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:139:0x038e, code lost:
        r19 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r37, r4, r36.config);
        parseObject((java.lang.Object) r19);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:140:0x03a3, code lost:
        setContext(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:143:?, code lost:
        r37 = r36.config.getDeserializer((java.lang.reflect.Type) r4).deserialze(r36, r4, r38);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:144:0x03bf, code lost:
        setContext(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:150:0x03dc, code lost:
        r17.nextToken(4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:151:0x03ef, code lost:
        if (r17.token() != 4) goto L_0x050e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:152:0x03f1, code lost:
        r23 = r17.stringVal();
        r17.nextToken(13);
        r24 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:153:0x040b, code lost:
        if (com.bftv.fui.constantplugin.Constant.NLP_CACHE_TYPE.equals(r23) == false) goto L_0x045f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:155:0x0413, code lost:
        if (r36.context == null) goto L_0x04fa;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:156:0x0415, code lost:
        r29 = r36.context;
        r30 = r29.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:157:0x0427, code lost:
        if ((r30 instanceof java.lang.Object[]) != false) goto L_0x0431;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:159:0x042f, code lost:
        if ((r30 instanceof java.util.Collection) == false) goto L_0x044a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:160:0x0431, code lost:
        r24 = r30;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:161:0x0433, code lost:
        r37 = r24;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:163:0x043f, code lost:
        if (r17.token() == 13) goto L_0x04fe;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:165:0x0449, code lost:
        throw new com.alibaba.fastjson.JSONException("syntax error");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:167:0x0450, code lost:
        if (r29.parent == null) goto L_0x0433;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:168:0x0452, code lost:
        r24 = r29.parent.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:170:0x046a, code lost:
        if ("..".equals(r23) == false) goto L_0x0495;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:172:0x0470, code lost:
        if (r5.object == null) goto L_0x0479;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:173:0x0472, code lost:
        r37 = r5.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:174:0x0479, code lost:
        addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r5, r23));
        setResolveStatus(1);
        r37 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:176:0x04a0, code lost:
        if (mtopsdk.common.util.SymbolExpUtil.SYMBOL_DOLLAR.equals(r23) == false) goto L_0x04e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:177:0x04a2, code lost:
        r26 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:179:0x04aa, code lost:
        if (r26.parent == null) goto L_0x04b3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:180:0x04ac, code lost:
        r26 = r26.parent;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:182:0x04b9, code lost:
        if (r26.object == null) goto L_0x04c5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:183:0x04bb, code lost:
        r24 = r26.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:184:0x04c1, code lost:
        r37 = r24;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:185:0x04c5, code lost:
        addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r26, r23));
        setResolveStatus(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:186:0x04e1, code lost:
        addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r5, r23));
        setResolveStatus(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:187:0x04fa, code lost:
        r37 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:188:0x04fe, code lost:
        r17.nextToken(16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:189:0x0507, code lost:
        setContext(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:192:0x052f, code lost:
        throw new com.alibaba.fastjson.JSONException("illegal ref, " + com.alibaba.fastjson.parser.JSONToken.name(r17.token()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:313:0x0816, code lost:
        if (r3 != '}') goto L_0x0831;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:314:0x0818, code lost:
        r17.next();
        r17.resetStringPosition();
        r17.nextToken();
        setContext(r32, r16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:315:0x082a, code lost:
        setContext(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:318:0x085d, code lost:
        throw new com.alibaba.fastjson.JSONException("syntax error, position at " + r17.pos() + ", name " + r16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        r17.next();
        r17.resetStringPosition();
        r17.nextToken();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0100, code lost:
        if (r27 != false) goto L_0x0132;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:347:?, code lost:
        return r37;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:348:?, code lost:
        return r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:349:?, code lost:
        return r19;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0108, code lost:
        if (r36.context == null) goto L_0x0139;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:350:?, code lost:
        return r37;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:351:?, code lost:
        return r37;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:355:?, code lost:
        return r37;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x011a, code lost:
        if (r38 != r36.context.fieldName) goto L_0x0139;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x012c, code lost:
        if (r37 != r36.context.object) goto L_0x0139;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x012e, code lost:
        r5 = r36.context;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0132, code lost:
        setContext(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
        r6 = setContext(r37, r38);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x013d, code lost:
        if (r5 != null) goto L_0x0140;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x013f, code lost:
        r5 = r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object parseObject(java.util.Map r37, java.lang.Object r38) {
        /*
            r36 = this;
            r0 = r36
            com.alibaba.fastjson.parser.JSONLexer r0 = r0.lexer
            r17 = r0
            int r33 = r17.token()
            r34 = 8
            r0 = r33
            r1 = r34
            if (r0 != r1) goto L_0x0018
            r17.nextToken()
            r37 = 0
        L_0x0017:
            return r37
        L_0x0018:
            int r33 = r17.token()
            r34 = 13
            r0 = r33
            r1 = r34
            if (r0 != r1) goto L_0x0028
            r17.nextToken()
            goto L_0x0017
        L_0x0028:
            int r33 = r17.token()
            r34 = 12
            r0 = r33
            r1 = r34
            if (r0 == r1) goto L_0x006d
            int r33 = r17.token()
            r34 = 16
            r0 = r33
            r1 = r34
            if (r0 == r1) goto L_0x006d
            com.alibaba.fastjson.JSONException r33 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r34 = new java.lang.StringBuilder
            r34.<init>()
            java.lang.String r35 = "syntax error, expect {, actual "
            java.lang.StringBuilder r34 = r34.append(r35)
            java.lang.String r35 = r17.tokenName()
            java.lang.StringBuilder r34 = r34.append(r35)
            java.lang.String r35 = ", "
            java.lang.StringBuilder r34 = r34.append(r35)
            java.lang.String r35 = r17.info()
            java.lang.StringBuilder r34 = r34.append(r35)
            java.lang.String r34 = r34.toString()
            r33.<init>(r34)
            throw r33
        L_0x006d:
            r0 = r36
            com.alibaba.fastjson.parser.ParseContext r5 = r0.context
            r27 = 0
        L_0x0073:
            r17.skipWhitespace()     // Catch:{ all -> 0x00ea }
            char r3 = r17.getCurrent()     // Catch:{ all -> 0x00ea }
            com.alibaba.fastjson.parser.Feature r33 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas     // Catch:{ all -> 0x00ea }
            r0 = r17
            r1 = r33
            boolean r33 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r1)     // Catch:{ all -> 0x00ea }
            if (r33 == 0) goto L_0x0097
        L_0x0086:
            r33 = 44
            r0 = r33
            if (r3 != r0) goto L_0x0097
            r17.next()     // Catch:{ all -> 0x00ea }
            r17.skipWhitespace()     // Catch:{ all -> 0x00ea }
            char r3 = r17.getCurrent()     // Catch:{ all -> 0x00ea }
            goto L_0x0086
        L_0x0097:
            r14 = 0
            r33 = 34
            r0 = r33
            if (r3 != r0) goto L_0x00f1
            r0 = r36
            com.alibaba.fastjson.parser.SymbolTable r0 = r0.symbolTable     // Catch:{ all -> 0x00ea }
            r33 = r0
            r34 = 34
            r0 = r17
            r1 = r33
            r2 = r34
            java.lang.String r16 = r0.scanSymbol(r1, r2)     // Catch:{ all -> 0x00ea }
            r17.skipWhitespace()     // Catch:{ all -> 0x00ea }
            char r3 = r17.getCurrent()     // Catch:{ all -> 0x00ea }
            r33 = 58
            r0 = r33
            if (r3 == r0) goto L_0x0247
            com.alibaba.fastjson.JSONException r33 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r34 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ea }
            r34.<init>()     // Catch:{ all -> 0x00ea }
            java.lang.String r35 = "expect ':' at "
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            int r35 = r17.pos()     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            java.lang.String r35 = ", name "
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            r0 = r34
            r1 = r16
            java.lang.StringBuilder r34 = r0.append(r1)     // Catch:{ all -> 0x00ea }
            java.lang.String r34 = r34.toString()     // Catch:{ all -> 0x00ea }
            r33.<init>(r34)     // Catch:{ all -> 0x00ea }
            throw r33     // Catch:{ all -> 0x00ea }
        L_0x00ea:
            r33 = move-exception
            r0 = r36
            r0.setContext(r5)
            throw r33
        L_0x00f1:
            r33 = 125(0x7d, float:1.75E-43)
            r0 = r33
            if (r3 != r0) goto L_0x0143
            r17.next()     // Catch:{ all -> 0x00ea }
            r17.resetStringPosition()     // Catch:{ all -> 0x00ea }
            r17.nextToken()     // Catch:{ all -> 0x00ea }
            if (r27 != 0) goto L_0x0132
            r0 = r36
            com.alibaba.fastjson.parser.ParseContext r0 = r0.context     // Catch:{ all -> 0x00ea }
            r33 = r0
            if (r33 == 0) goto L_0x0139
            r0 = r36
            com.alibaba.fastjson.parser.ParseContext r0 = r0.context     // Catch:{ all -> 0x00ea }
            r33 = r0
            r0 = r33
            java.lang.Object r0 = r0.fieldName     // Catch:{ all -> 0x00ea }
            r33 = r0
            r0 = r38
            r1 = r33
            if (r0 != r1) goto L_0x0139
            r0 = r36
            com.alibaba.fastjson.parser.ParseContext r0 = r0.context     // Catch:{ all -> 0x00ea }
            r33 = r0
            r0 = r33
            java.lang.Object r0 = r0.object     // Catch:{ all -> 0x00ea }
            r33 = r0
            r0 = r37
            r1 = r33
            if (r0 != r1) goto L_0x0139
            r0 = r36
            com.alibaba.fastjson.parser.ParseContext r5 = r0.context     // Catch:{ all -> 0x00ea }
        L_0x0132:
            r0 = r36
            r0.setContext(r5)
            goto L_0x0017
        L_0x0139:
            com.alibaba.fastjson.parser.ParseContext r6 = r36.setContext(r37, r38)     // Catch:{ all -> 0x00ea }
            if (r5 != 0) goto L_0x0140
            r5 = r6
        L_0x0140:
            r27 = 1
            goto L_0x0132
        L_0x0143:
            r33 = 39
            r0 = r33
            if (r3 != r0) goto L_0x019b
            com.alibaba.fastjson.parser.Feature r33 = com.alibaba.fastjson.parser.Feature.AllowSingleQuotes     // Catch:{ all -> 0x00ea }
            r0 = r17
            r1 = r33
            boolean r33 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r1)     // Catch:{ all -> 0x00ea }
            if (r33 != 0) goto L_0x015e
            com.alibaba.fastjson.JSONException r33 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x00ea }
            java.lang.String r34 = "syntax error"
            r33.<init>(r34)     // Catch:{ all -> 0x00ea }
            throw r33     // Catch:{ all -> 0x00ea }
        L_0x015e:
            r0 = r36
            com.alibaba.fastjson.parser.SymbolTable r0 = r0.symbolTable     // Catch:{ all -> 0x00ea }
            r33 = r0
            r34 = 39
            r0 = r17
            r1 = r33
            r2 = r34
            java.lang.String r16 = r0.scanSymbol(r1, r2)     // Catch:{ all -> 0x00ea }
            r17.skipWhitespace()     // Catch:{ all -> 0x00ea }
            char r3 = r17.getCurrent()     // Catch:{ all -> 0x00ea }
            r33 = 58
            r0 = r33
            if (r3 == r0) goto L_0x0247
            com.alibaba.fastjson.JSONException r33 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r34 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ea }
            r34.<init>()     // Catch:{ all -> 0x00ea }
            java.lang.String r35 = "expect ':' at "
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            int r35 = r17.pos()     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            java.lang.String r34 = r34.toString()     // Catch:{ all -> 0x00ea }
            r33.<init>(r34)     // Catch:{ all -> 0x00ea }
            throw r33     // Catch:{ all -> 0x00ea }
        L_0x019b:
            r33 = 26
            r0 = r33
            if (r3 != r0) goto L_0x01aa
            com.alibaba.fastjson.JSONException r33 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x00ea }
            java.lang.String r34 = "syntax error"
            r33.<init>(r34)     // Catch:{ all -> 0x00ea }
            throw r33     // Catch:{ all -> 0x00ea }
        L_0x01aa:
            r33 = 44
            r0 = r33
            if (r3 != r0) goto L_0x01b9
            com.alibaba.fastjson.JSONException r33 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x00ea }
            java.lang.String r34 = "syntax error"
            r33.<init>(r34)     // Catch:{ all -> 0x00ea }
            throw r33     // Catch:{ all -> 0x00ea }
        L_0x01b9:
            r33 = 48
            r0 = r33
            if (r3 < r0) goto L_0x01c5
            r33 = 57
            r0 = r33
            if (r3 <= r0) goto L_0x01cb
        L_0x01c5:
            r33 = 45
            r0 = r33
            if (r3 != r0) goto L_0x0233
        L_0x01cb:
            r17.resetStringPosition()     // Catch:{ all -> 0x00ea }
            r17.scanNumber()     // Catch:{ all -> 0x00ea }
            int r33 = r17.token()     // Catch:{ NumberFormatException -> 0x0214 }
            r34 = 2
            r0 = r33
            r1 = r34
            if (r0 != r1) goto L_0x0209
            java.lang.Number r16 = r17.integerValue()     // Catch:{ NumberFormatException -> 0x0214 }
        L_0x01e1:
            char r3 = r17.getCurrent()     // Catch:{ all -> 0x00ea }
            r33 = 58
            r0 = r33
            if (r3 == r0) goto L_0x0247
            com.alibaba.fastjson.JSONException r33 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r34 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ea }
            r34.<init>()     // Catch:{ all -> 0x00ea }
            java.lang.String r35 = "parse number key error"
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            java.lang.String r35 = r17.info()     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            java.lang.String r34 = r34.toString()     // Catch:{ all -> 0x00ea }
            r33.<init>(r34)     // Catch:{ all -> 0x00ea }
            throw r33     // Catch:{ all -> 0x00ea }
        L_0x0209:
            r33 = 1
            r0 = r17
            r1 = r33
            java.lang.Number r16 = r0.decimalValue(r1)     // Catch:{ NumberFormatException -> 0x0214 }
            goto L_0x01e1
        L_0x0214:
            r9 = move-exception
            com.alibaba.fastjson.JSONException r33 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r34 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ea }
            r34.<init>()     // Catch:{ all -> 0x00ea }
            java.lang.String r35 = "parse number key error"
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            java.lang.String r35 = r17.info()     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            java.lang.String r34 = r34.toString()     // Catch:{ all -> 0x00ea }
            r33.<init>(r34)     // Catch:{ all -> 0x00ea }
            throw r33     // Catch:{ all -> 0x00ea }
        L_0x0233:
            r33 = 123(0x7b, float:1.72E-43)
            r0 = r33
            if (r3 == r0) goto L_0x023f
            r33 = 91
            r0 = r33
            if (r3 != r0) goto L_0x029d
        L_0x023f:
            r17.nextToken()     // Catch:{ all -> 0x00ea }
            java.lang.Object r16 = r36.parse()     // Catch:{ all -> 0x00ea }
            r14 = 1
        L_0x0247:
            if (r14 != 0) goto L_0x024f
            r17.next()     // Catch:{ all -> 0x00ea }
            r17.skipWhitespace()     // Catch:{ all -> 0x00ea }
        L_0x024f:
            char r3 = r17.getCurrent()     // Catch:{ all -> 0x00ea }
            r17.resetStringPosition()     // Catch:{ all -> 0x00ea }
            java.lang.String r33 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ all -> 0x00ea }
            r0 = r16
            r1 = r33
            if (r0 != r1) goto L_0x03c7
            com.alibaba.fastjson.parser.Feature r33 = com.alibaba.fastjson.parser.Feature.DisableSpecialKeyDetect     // Catch:{ all -> 0x00ea }
            r0 = r17
            r1 = r33
            boolean r33 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r1)     // Catch:{ all -> 0x00ea }
            if (r33 != 0) goto L_0x03c7
            r0 = r36
            com.alibaba.fastjson.parser.SymbolTable r0 = r0.symbolTable     // Catch:{ all -> 0x00ea }
            r33 = r0
            r34 = 34
            r0 = r17
            r1 = r33
            r2 = r34
            java.lang.String r31 = r0.scanSymbol(r1, r2)     // Catch:{ all -> 0x00ea }
            r0 = r36
            com.alibaba.fastjson.parser.ParserConfig r0 = r0.config     // Catch:{ all -> 0x00ea }
            r33 = r0
            r34 = 0
            r0 = r33
            r1 = r31
            r2 = r34
            java.lang.Class r4 = r0.checkAutoType(r1, r2)     // Catch:{ all -> 0x00ea }
            if (r4 != 0) goto L_0x02f8
            java.lang.String r33 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ all -> 0x00ea }
            r0 = r37
            r1 = r33
            r2 = r31
            r0.put(r1, r2)     // Catch:{ all -> 0x00ea }
            goto L_0x0073
        L_0x029d:
            com.alibaba.fastjson.parser.Feature r33 = com.alibaba.fastjson.parser.Feature.AllowUnQuotedFieldNames     // Catch:{ all -> 0x00ea }
            r0 = r17
            r1 = r33
            boolean r33 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r1)     // Catch:{ all -> 0x00ea }
            if (r33 != 0) goto L_0x02b2
            com.alibaba.fastjson.JSONException r33 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x00ea }
            java.lang.String r34 = "syntax error"
            r33.<init>(r34)     // Catch:{ all -> 0x00ea }
            throw r33     // Catch:{ all -> 0x00ea }
        L_0x02b2:
            r0 = r36
            com.alibaba.fastjson.parser.SymbolTable r0 = r0.symbolTable     // Catch:{ all -> 0x00ea }
            r33 = r0
            r0 = r17
            r1 = r33
            java.lang.String r16 = r0.scanSymbolUnQuoted(r1)     // Catch:{ all -> 0x00ea }
            r17.skipWhitespace()     // Catch:{ all -> 0x00ea }
            char r3 = r17.getCurrent()     // Catch:{ all -> 0x00ea }
            r33 = 58
            r0 = r33
            if (r3 == r0) goto L_0x0247
            com.alibaba.fastjson.JSONException r33 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r34 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ea }
            r34.<init>()     // Catch:{ all -> 0x00ea }
            java.lang.String r35 = "expect ':' at "
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            int r35 = r17.pos()     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            java.lang.String r35 = ", actual "
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            r0 = r34
            java.lang.StringBuilder r34 = r0.append(r3)     // Catch:{ all -> 0x00ea }
            java.lang.String r34 = r34.toString()     // Catch:{ all -> 0x00ea }
            r33.<init>(r34)     // Catch:{ all -> 0x00ea }
            throw r33     // Catch:{ all -> 0x00ea }
        L_0x02f8:
            r33 = 16
            r0 = r17
            r1 = r33
            r0.nextToken(r1)     // Catch:{ all -> 0x00ea }
            int r33 = r17.token()     // Catch:{ all -> 0x00ea }
            r34 = 13
            r0 = r33
            r1 = r34
            if (r0 != r1) goto L_0x036c
            r33 = 16
            r0 = r17
            r1 = r33
            r0.nextToken(r1)     // Catch:{ all -> 0x00ea }
            r13 = 0
            r0 = r36
            com.alibaba.fastjson.parser.ParserConfig r0 = r0.config     // Catch:{ Exception -> 0x035e }
            r33 = r0
            r0 = r33
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r8 = r0.getDeserializer((java.lang.reflect.Type) r4)     // Catch:{ Exception -> 0x035e }
            boolean r0 = r8 instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer     // Catch:{ Exception -> 0x035e }
            r33 = r0
            if (r33 == 0) goto L_0x0331
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r8 = (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) r8     // Catch:{ Exception -> 0x035e }
            r0 = r36
            java.lang.Object r13 = r8.createInstance((com.alibaba.fastjson.parser.DefaultJSONParser) r0, (java.lang.reflect.Type) r4)     // Catch:{ Exception -> 0x035e }
        L_0x0331:
            if (r13 != 0) goto L_0x033e
            java.lang.Class<java.lang.Cloneable> r33 = java.lang.Cloneable.class
            r0 = r33
            if (r4 != r0) goto L_0x0347
            java.util.HashMap r13 = new java.util.HashMap     // Catch:{ Exception -> 0x035e }
            r13.<init>()     // Catch:{ Exception -> 0x035e }
        L_0x033e:
            r0 = r36
            r0.setContext(r5)
            r37 = r13
            goto L_0x0017
        L_0x0347:
            java.lang.String r33 = "java.util.Collections$EmptyMap"
            r0 = r33
            r1 = r31
            boolean r33 = r0.equals(r1)     // Catch:{ Exception -> 0x035e }
            if (r33 == 0) goto L_0x0359
            java.util.Map r13 = java.util.Collections.emptyMap()     // Catch:{ Exception -> 0x035e }
            goto L_0x033e
        L_0x0359:
            java.lang.Object r13 = r4.newInstance()     // Catch:{ Exception -> 0x035e }
            goto L_0x033e
        L_0x035e:
            r9 = move-exception
            com.alibaba.fastjson.JSONException r33 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x00ea }
            java.lang.String r34 = "create instance error"
            r0 = r33
            r1 = r34
            r0.<init>(r1, r9)     // Catch:{ all -> 0x00ea }
            throw r33     // Catch:{ all -> 0x00ea }
        L_0x036c:
            r33 = 2
            r0 = r36
            r1 = r33
            r0.setResolveStatus(r1)     // Catch:{ all -> 0x00ea }
            r0 = r36
            com.alibaba.fastjson.parser.ParseContext r0 = r0.context     // Catch:{ all -> 0x00ea }
            r33 = r0
            if (r33 == 0) goto L_0x0388
            r0 = r38
            boolean r0 = r0 instanceof java.lang.Integer     // Catch:{ all -> 0x00ea }
            r33 = r0
            if (r33 != 0) goto L_0x0388
            r36.popContext()     // Catch:{ all -> 0x00ea }
        L_0x0388:
            int r33 = r37.size()     // Catch:{ all -> 0x00ea }
            if (r33 <= 0) goto L_0x03ac
            r0 = r36
            com.alibaba.fastjson.parser.ParserConfig r0 = r0.config     // Catch:{ all -> 0x00ea }
            r33 = r0
            r0 = r37
            r1 = r33
            java.lang.Object r19 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r0, r4, (com.alibaba.fastjson.parser.ParserConfig) r1)     // Catch:{ all -> 0x00ea }
            r0 = r36
            r1 = r19
            r0.parseObject((java.lang.Object) r1)     // Catch:{ all -> 0x00ea }
            r0 = r36
            r0.setContext(r5)
            r37 = r19
            goto L_0x0017
        L_0x03ac:
            r0 = r36
            com.alibaba.fastjson.parser.ParserConfig r0 = r0.config     // Catch:{ all -> 0x00ea }
            r33 = r0
            r0 = r33
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r8 = r0.getDeserializer((java.lang.reflect.Type) r4)     // Catch:{ all -> 0x00ea }
            r0 = r36
            r1 = r38
            java.lang.Object r37 = r8.deserialze(r0, r4, r1)     // Catch:{ all -> 0x00ea }
            r0 = r36
            r0.setContext(r5)
            goto L_0x0017
        L_0x03c7:
            java.lang.String r33 = "$ref"
            r0 = r16
            r1 = r33
            if (r0 != r1) goto L_0x0530
            com.alibaba.fastjson.parser.Feature r33 = com.alibaba.fastjson.parser.Feature.DisableSpecialKeyDetect     // Catch:{ all -> 0x00ea }
            r0 = r17
            r1 = r33
            boolean r33 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r1)     // Catch:{ all -> 0x00ea }
            if (r33 != 0) goto L_0x0530
            r33 = 4
            r0 = r17
            r1 = r33
            r0.nextToken(r1)     // Catch:{ all -> 0x00ea }
            int r33 = r17.token()     // Catch:{ all -> 0x00ea }
            r34 = 4
            r0 = r33
            r1 = r34
            if (r0 != r1) goto L_0x050e
            java.lang.String r23 = r17.stringVal()     // Catch:{ all -> 0x00ea }
            r33 = 13
            r0 = r17
            r1 = r33
            r0.nextToken(r1)     // Catch:{ all -> 0x00ea }
            r24 = 0
            java.lang.String r33 = "@"
            r0 = r33
            r1 = r23
            boolean r33 = r0.equals(r1)     // Catch:{ all -> 0x00ea }
            if (r33 == 0) goto L_0x045f
            r0 = r36
            com.alibaba.fastjson.parser.ParseContext r0 = r0.context     // Catch:{ all -> 0x00ea }
            r33 = r0
            if (r33 == 0) goto L_0x04fa
            r0 = r36
            com.alibaba.fastjson.parser.ParseContext r0 = r0.context     // Catch:{ all -> 0x00ea }
            r29 = r0
            r0 = r29
            java.lang.Object r0 = r0.object     // Catch:{ all -> 0x00ea }
            r30 = r0
            r0 = r30
            boolean r0 = r0 instanceof java.lang.Object[]     // Catch:{ all -> 0x00ea }
            r33 = r0
            if (r33 != 0) goto L_0x0431
            r0 = r30
            boolean r0 = r0 instanceof java.util.Collection     // Catch:{ all -> 0x00ea }
            r33 = r0
            if (r33 == 0) goto L_0x044a
        L_0x0431:
            r24 = r30
        L_0x0433:
            r37 = r24
        L_0x0435:
            int r33 = r17.token()     // Catch:{ all -> 0x00ea }
            r34 = 13
            r0 = r33
            r1 = r34
            if (r0 == r1) goto L_0x04fe
            com.alibaba.fastjson.JSONException r33 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x00ea }
            java.lang.String r34 = "syntax error"
            r33.<init>(r34)     // Catch:{ all -> 0x00ea }
            throw r33     // Catch:{ all -> 0x00ea }
        L_0x044a:
            r0 = r29
            com.alibaba.fastjson.parser.ParseContext r0 = r0.parent     // Catch:{ all -> 0x00ea }
            r33 = r0
            if (r33 == 0) goto L_0x0433
            r0 = r29
            com.alibaba.fastjson.parser.ParseContext r0 = r0.parent     // Catch:{ all -> 0x00ea }
            r33 = r0
            r0 = r33
            java.lang.Object r0 = r0.object     // Catch:{ all -> 0x00ea }
            r24 = r0
            goto L_0x0433
        L_0x045f:
            java.lang.String r33 = ".."
            r0 = r33
            r1 = r23
            boolean r33 = r0.equals(r1)     // Catch:{ all -> 0x00ea }
            if (r33 == 0) goto L_0x0495
            java.lang.Object r0 = r5.object     // Catch:{ all -> 0x00ea }
            r33 = r0
            if (r33 == 0) goto L_0x0479
            java.lang.Object r0 = r5.object     // Catch:{ all -> 0x00ea }
            r24 = r0
            r37 = r24
            goto L_0x0435
        L_0x0479:
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r33 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x00ea }
            r0 = r33
            r1 = r23
            r0.<init>(r5, r1)     // Catch:{ all -> 0x00ea }
            r0 = r36
            r1 = r33
            r0.addResolveTask(r1)     // Catch:{ all -> 0x00ea }
            r33 = 1
            r0 = r36
            r1 = r33
            r0.setResolveStatus(r1)     // Catch:{ all -> 0x00ea }
            r37 = r24
            goto L_0x0435
        L_0x0495:
            java.lang.String r33 = "$"
            r0 = r33
            r1 = r23
            boolean r33 = r0.equals(r1)     // Catch:{ all -> 0x00ea }
            if (r33 == 0) goto L_0x04e1
            r26 = r5
        L_0x04a4:
            r0 = r26
            com.alibaba.fastjson.parser.ParseContext r0 = r0.parent     // Catch:{ all -> 0x00ea }
            r33 = r0
            if (r33 == 0) goto L_0x04b3
            r0 = r26
            com.alibaba.fastjson.parser.ParseContext r0 = r0.parent     // Catch:{ all -> 0x00ea }
            r26 = r0
            goto L_0x04a4
        L_0x04b3:
            r0 = r26
            java.lang.Object r0 = r0.object     // Catch:{ all -> 0x00ea }
            r33 = r0
            if (r33 == 0) goto L_0x04c5
            r0 = r26
            java.lang.Object r0 = r0.object     // Catch:{ all -> 0x00ea }
            r24 = r0
        L_0x04c1:
            r37 = r24
            goto L_0x0435
        L_0x04c5:
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r33 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x00ea }
            r0 = r33
            r1 = r26
            r2 = r23
            r0.<init>(r1, r2)     // Catch:{ all -> 0x00ea }
            r0 = r36
            r1 = r33
            r0.addResolveTask(r1)     // Catch:{ all -> 0x00ea }
            r33 = 1
            r0 = r36
            r1 = r33
            r0.setResolveStatus(r1)     // Catch:{ all -> 0x00ea }
            goto L_0x04c1
        L_0x04e1:
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r33 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x00ea }
            r0 = r33
            r1 = r23
            r0.<init>(r5, r1)     // Catch:{ all -> 0x00ea }
            r0 = r36
            r1 = r33
            r0.addResolveTask(r1)     // Catch:{ all -> 0x00ea }
            r33 = 1
            r0 = r36
            r1 = r33
            r0.setResolveStatus(r1)     // Catch:{ all -> 0x00ea }
        L_0x04fa:
            r37 = r24
            goto L_0x0435
        L_0x04fe:
            r33 = 16
            r0 = r17
            r1 = r33
            r0.nextToken(r1)     // Catch:{ all -> 0x00ea }
            r0 = r36
            r0.setContext(r5)
            goto L_0x0017
        L_0x050e:
            com.alibaba.fastjson.JSONException r33 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r34 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ea }
            r34.<init>()     // Catch:{ all -> 0x00ea }
            java.lang.String r35 = "illegal ref, "
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            int r35 = r17.token()     // Catch:{ all -> 0x00ea }
            java.lang.String r35 = com.alibaba.fastjson.parser.JSONToken.name(r35)     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            java.lang.String r34 = r34.toString()     // Catch:{ all -> 0x00ea }
            r33.<init>(r34)     // Catch:{ all -> 0x00ea }
            throw r33     // Catch:{ all -> 0x00ea }
        L_0x0530:
            if (r27 != 0) goto L_0x0562
            r0 = r36
            com.alibaba.fastjson.parser.ParseContext r0 = r0.context     // Catch:{ all -> 0x00ea }
            r33 = r0
            if (r33 == 0) goto L_0x05c1
            r0 = r36
            com.alibaba.fastjson.parser.ParseContext r0 = r0.context     // Catch:{ all -> 0x00ea }
            r33 = r0
            r0 = r33
            java.lang.Object r0 = r0.fieldName     // Catch:{ all -> 0x00ea }
            r33 = r0
            r0 = r38
            r1 = r33
            if (r0 != r1) goto L_0x05c1
            r0 = r36
            com.alibaba.fastjson.parser.ParseContext r0 = r0.context     // Catch:{ all -> 0x00ea }
            r33 = r0
            r0 = r33
            java.lang.Object r0 = r0.object     // Catch:{ all -> 0x00ea }
            r33 = r0
            r0 = r37
            r1 = r33
            if (r0 != r1) goto L_0x05c1
            r0 = r36
            com.alibaba.fastjson.parser.ParseContext r5 = r0.context     // Catch:{ all -> 0x00ea }
        L_0x0562:
            java.lang.Class r33 = r37.getClass()     // Catch:{ all -> 0x00ea }
            java.lang.Class<com.alibaba.fastjson.JSONObject> r34 = com.alibaba.fastjson.JSONObject.class
            r0 = r33
            r1 = r34
            if (r0 != r1) goto L_0x0573
            if (r16 != 0) goto L_0x05cb
            java.lang.String r16 = "null"
        L_0x0573:
            r33 = 34
            r0 = r33
            if (r3 != r0) goto L_0x05d0
            r17.scanString()     // Catch:{ all -> 0x00ea }
            java.lang.String r28 = r17.stringVal()     // Catch:{ all -> 0x00ea }
            r32 = r28
            com.alibaba.fastjson.parser.Feature r33 = com.alibaba.fastjson.parser.Feature.AllowISO8601DateFormat     // Catch:{ all -> 0x00ea }
            r0 = r17
            r1 = r33
            boolean r33 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r1)     // Catch:{ all -> 0x00ea }
            if (r33 == 0) goto L_0x05a6
            com.alibaba.fastjson.parser.JSONScanner r15 = new com.alibaba.fastjson.parser.JSONScanner     // Catch:{ all -> 0x00ea }
            r0 = r28
            r15.<init>(r0)     // Catch:{ all -> 0x00ea }
            boolean r33 = r15.scanISO8601DateIfMatch()     // Catch:{ all -> 0x00ea }
            if (r33 == 0) goto L_0x05a3
            java.util.Calendar r33 = r15.getCalendar()     // Catch:{ all -> 0x00ea }
            java.util.Date r32 = r33.getTime()     // Catch:{ all -> 0x00ea }
        L_0x05a3:
            r15.close()     // Catch:{ all -> 0x00ea }
        L_0x05a6:
            r0 = r37
            r1 = r16
            r2 = r32
            r0.put(r1, r2)     // Catch:{ all -> 0x00ea }
        L_0x05af:
            r17.skipWhitespace()     // Catch:{ all -> 0x00ea }
            char r3 = r17.getCurrent()     // Catch:{ all -> 0x00ea }
            r33 = 44
            r0 = r33
            if (r3 != r0) goto L_0x0812
            r17.next()     // Catch:{ all -> 0x00ea }
            goto L_0x0073
        L_0x05c1:
            com.alibaba.fastjson.parser.ParseContext r6 = r36.setContext(r37, r38)     // Catch:{ all -> 0x00ea }
            if (r5 != 0) goto L_0x05c8
            r5 = r6
        L_0x05c8:
            r27 = 1
            goto L_0x0562
        L_0x05cb:
            java.lang.String r16 = r16.toString()     // Catch:{ all -> 0x00ea }
            goto L_0x0573
        L_0x05d0:
            r33 = 48
            r0 = r33
            if (r3 < r0) goto L_0x05dc
            r33 = 57
            r0 = r33
            if (r3 <= r0) goto L_0x05e2
        L_0x05dc:
            r33 = 45
            r0 = r33
            if (r3 != r0) goto L_0x0612
        L_0x05e2:
            r17.scanNumber()     // Catch:{ all -> 0x00ea }
            int r33 = r17.token()     // Catch:{ all -> 0x00ea }
            r34 = 2
            r0 = r33
            r1 = r34
            if (r0 != r1) goto L_0x05ff
            java.lang.Number r32 = r17.integerValue()     // Catch:{ all -> 0x00ea }
        L_0x05f5:
            r0 = r37
            r1 = r16
            r2 = r32
            r0.put(r1, r2)     // Catch:{ all -> 0x00ea }
            goto L_0x05af
        L_0x05ff:
            com.alibaba.fastjson.parser.Feature r33 = com.alibaba.fastjson.parser.Feature.UseBigDecimal     // Catch:{ all -> 0x00ea }
            r0 = r17
            r1 = r33
            boolean r33 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r1)     // Catch:{ all -> 0x00ea }
            r0 = r17
            r1 = r33
            java.lang.Number r32 = r0.decimalValue(r1)     // Catch:{ all -> 0x00ea }
            goto L_0x05f5
        L_0x0612:
            r33 = 91
            r0 = r33
            if (r3 != r0) goto L_0x068a
            r17.nextToken()     // Catch:{ all -> 0x00ea }
            com.alibaba.fastjson.JSONArray r18 = new com.alibaba.fastjson.JSONArray     // Catch:{ all -> 0x00ea }
            r18.<init>()     // Catch:{ all -> 0x00ea }
            if (r38 == 0) goto L_0x066f
            java.lang.Class r33 = r38.getClass()     // Catch:{ all -> 0x00ea }
            java.lang.Class<java.lang.Integer> r34 = java.lang.Integer.class
            r0 = r33
            r1 = r34
            if (r0 != r1) goto L_0x066f
            r22 = 1
        L_0x0630:
            if (r38 != 0) goto L_0x0637
            r0 = r36
            r0.setContext(r5)     // Catch:{ all -> 0x00ea }
        L_0x0637:
            r0 = r36
            r1 = r18
            r2 = r16
            r0.parseArray((java.util.Collection) r1, (java.lang.Object) r2)     // Catch:{ all -> 0x00ea }
            com.alibaba.fastjson.parser.Feature r33 = com.alibaba.fastjson.parser.Feature.UseObjectArray     // Catch:{ all -> 0x00ea }
            r0 = r17
            r1 = r33
            boolean r33 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r1)     // Catch:{ all -> 0x00ea }
            if (r33 == 0) goto L_0x0672
            java.lang.Object[] r32 = r18.toArray()     // Catch:{ all -> 0x00ea }
        L_0x0650:
            r0 = r37
            r1 = r16
            r2 = r32
            r0.put(r1, r2)     // Catch:{ all -> 0x00ea }
            int r33 = r17.token()     // Catch:{ all -> 0x00ea }
            r34 = 13
            r0 = r33
            r1 = r34
            if (r0 != r1) goto L_0x0675
            r17.nextToken()     // Catch:{ all -> 0x00ea }
            r0 = r36
            r0.setContext(r5)
            goto L_0x0017
        L_0x066f:
            r22 = 0
            goto L_0x0630
        L_0x0672:
            r32 = r18
            goto L_0x0650
        L_0x0675:
            int r33 = r17.token()     // Catch:{ all -> 0x00ea }
            r34 = 16
            r0 = r33
            r1 = r34
            if (r0 == r1) goto L_0x0073
            com.alibaba.fastjson.JSONException r33 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x00ea }
            java.lang.String r34 = "syntax error"
            r33.<init>(r34)     // Catch:{ all -> 0x00ea }
            throw r33     // Catch:{ all -> 0x00ea }
        L_0x068a:
            r33 = 123(0x7b, float:1.72E-43)
            r0 = r33
            if (r3 != r0) goto L_0x07a3
            r17.nextToken()     // Catch:{ all -> 0x00ea }
            if (r38 == 0) goto L_0x0759
            java.lang.Class r33 = r38.getClass()     // Catch:{ all -> 0x00ea }
            java.lang.Class<java.lang.Integer> r34 = java.lang.Integer.class
            r0 = r33
            r1 = r34
            if (r0 != r1) goto L_0x0759
            r22 = 1
        L_0x06a3:
            com.alibaba.fastjson.JSONObject r12 = new com.alibaba.fastjson.JSONObject     // Catch:{ all -> 0x00ea }
            com.alibaba.fastjson.parser.Feature r33 = com.alibaba.fastjson.parser.Feature.OrderedField     // Catch:{ all -> 0x00ea }
            r0 = r17
            r1 = r33
            boolean r33 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r1)     // Catch:{ all -> 0x00ea }
            r0 = r33
            r12.<init>((boolean) r0)     // Catch:{ all -> 0x00ea }
            r7 = 0
            if (r22 != 0) goto L_0x06bf
            r0 = r36
            r1 = r16
            com.alibaba.fastjson.parser.ParseContext r7 = r0.setContext(r5, r12, r1)     // Catch:{ all -> 0x00ea }
        L_0x06bf:
            r20 = 0
            r21 = 0
            r0 = r36
            com.alibaba.fastjson.parser.deserializer.FieldTypeResolver r0 = r0.fieldTypeResolver     // Catch:{ all -> 0x00ea }
            r33 = r0
            if (r33 == 0) goto L_0x06f9
            if (r16 == 0) goto L_0x075d
            java.lang.String r25 = r16.toString()     // Catch:{ all -> 0x00ea }
        L_0x06d1:
            r0 = r36
            com.alibaba.fastjson.parser.deserializer.FieldTypeResolver r0 = r0.fieldTypeResolver     // Catch:{ all -> 0x00ea }
            r33 = r0
            r0 = r33
            r1 = r37
            r2 = r25
            java.lang.reflect.Type r11 = r0.resolve(r1, r2)     // Catch:{ all -> 0x00ea }
            if (r11 == 0) goto L_0x06f9
            r0 = r36
            com.alibaba.fastjson.parser.ParserConfig r0 = r0.config     // Catch:{ all -> 0x00ea }
            r33 = r0
            r0 = r33
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r10 = r0.getDeserializer((java.lang.reflect.Type) r11)     // Catch:{ all -> 0x00ea }
            r0 = r36
            r1 = r16
            java.lang.Object r20 = r10.deserialze(r0, r11, r1)     // Catch:{ all -> 0x00ea }
            r21 = 1
        L_0x06f9:
            if (r21 != 0) goto L_0x0703
            r0 = r36
            r1 = r16
            java.lang.Object r20 = r0.parseObject((java.util.Map) r12, (java.lang.Object) r1)     // Catch:{ all -> 0x00ea }
        L_0x0703:
            if (r7 == 0) goto L_0x070d
            r0 = r20
            if (r12 == r0) goto L_0x070d
            r0 = r37
            r7.object = r0     // Catch:{ all -> 0x00ea }
        L_0x070d:
            java.lang.String r33 = r16.toString()     // Catch:{ all -> 0x00ea }
            r0 = r36
            r1 = r37
            r2 = r33
            r0.checkMapResolve(r1, r2)     // Catch:{ all -> 0x00ea }
            java.lang.Class r33 = r37.getClass()     // Catch:{ all -> 0x00ea }
            java.lang.Class<com.alibaba.fastjson.JSONObject> r34 = com.alibaba.fastjson.JSONObject.class
            r0 = r33
            r1 = r34
            if (r0 != r1) goto L_0x0761
            java.lang.String r33 = r16.toString()     // Catch:{ all -> 0x00ea }
            r0 = r37
            r1 = r33
            r2 = r20
            r0.put(r1, r2)     // Catch:{ all -> 0x00ea }
        L_0x0733:
            if (r22 == 0) goto L_0x073e
            r0 = r36
            r1 = r20
            r2 = r16
            r0.setContext(r1, r2)     // Catch:{ all -> 0x00ea }
        L_0x073e:
            int r33 = r17.token()     // Catch:{ all -> 0x00ea }
            r34 = 13
            r0 = r33
            r1 = r34
            if (r0 != r1) goto L_0x076b
            r17.nextToken()     // Catch:{ all -> 0x00ea }
            r0 = r36
            r0.setContext(r5)     // Catch:{ all -> 0x00ea }
            r0 = r36
            r0.setContext(r5)
            goto L_0x0017
        L_0x0759:
            r22 = 0
            goto L_0x06a3
        L_0x075d:
            r25 = 0
            goto L_0x06d1
        L_0x0761:
            r0 = r37
            r1 = r16
            r2 = r20
            r0.put(r1, r2)     // Catch:{ all -> 0x00ea }
            goto L_0x0733
        L_0x076b:
            int r33 = r17.token()     // Catch:{ all -> 0x00ea }
            r34 = 16
            r0 = r33
            r1 = r34
            if (r0 != r1) goto L_0x0785
            if (r22 == 0) goto L_0x077e
            r36.popContext()     // Catch:{ all -> 0x00ea }
            goto L_0x0073
        L_0x077e:
            r0 = r36
            r0.setContext(r5)     // Catch:{ all -> 0x00ea }
            goto L_0x0073
        L_0x0785:
            com.alibaba.fastjson.JSONException r33 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r34 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ea }
            r34.<init>()     // Catch:{ all -> 0x00ea }
            java.lang.String r35 = "syntax error, "
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            java.lang.String r35 = r17.tokenName()     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            java.lang.String r34 = r34.toString()     // Catch:{ all -> 0x00ea }
            r33.<init>(r34)     // Catch:{ all -> 0x00ea }
            throw r33     // Catch:{ all -> 0x00ea }
        L_0x07a3:
            r17.nextToken()     // Catch:{ all -> 0x00ea }
            java.lang.Object r32 = r36.parse()     // Catch:{ all -> 0x00ea }
            java.lang.Class r33 = r37.getClass()     // Catch:{ all -> 0x00ea }
            java.lang.Class<com.alibaba.fastjson.JSONObject> r34 = com.alibaba.fastjson.JSONObject.class
            r0 = r33
            r1 = r34
            if (r0 != r1) goto L_0x07ba
            java.lang.String r16 = r16.toString()     // Catch:{ all -> 0x00ea }
        L_0x07ba:
            r0 = r37
            r1 = r16
            r2 = r32
            r0.put(r1, r2)     // Catch:{ all -> 0x00ea }
            int r33 = r17.token()     // Catch:{ all -> 0x00ea }
            r34 = 13
            r0 = r33
            r1 = r34
            if (r0 != r1) goto L_0x07d9
            r17.nextToken()     // Catch:{ all -> 0x00ea }
            r0 = r36
            r0.setContext(r5)
            goto L_0x0017
        L_0x07d9:
            int r33 = r17.token()     // Catch:{ all -> 0x00ea }
            r34 = 16
            r0 = r33
            r1 = r34
            if (r0 == r1) goto L_0x0073
            com.alibaba.fastjson.JSONException r33 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r34 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ea }
            r34.<init>()     // Catch:{ all -> 0x00ea }
            java.lang.String r35 = "syntax error, position at "
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            int r35 = r17.pos()     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            java.lang.String r35 = ", name "
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            r0 = r34
            r1 = r16
            java.lang.StringBuilder r34 = r0.append(r1)     // Catch:{ all -> 0x00ea }
            java.lang.String r34 = r34.toString()     // Catch:{ all -> 0x00ea }
            r33.<init>(r34)     // Catch:{ all -> 0x00ea }
            throw r33     // Catch:{ all -> 0x00ea }
        L_0x0812:
            r33 = 125(0x7d, float:1.75E-43)
            r0 = r33
            if (r3 != r0) goto L_0x0831
            r17.next()     // Catch:{ all -> 0x00ea }
            r17.resetStringPosition()     // Catch:{ all -> 0x00ea }
            r17.nextToken()     // Catch:{ all -> 0x00ea }
            r0 = r36
            r1 = r32
            r2 = r16
            r0.setContext(r1, r2)     // Catch:{ all -> 0x00ea }
            r0 = r36
            r0.setContext(r5)
            goto L_0x0017
        L_0x0831:
            com.alibaba.fastjson.JSONException r33 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r34 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ea }
            r34.<init>()     // Catch:{ all -> 0x00ea }
            java.lang.String r35 = "syntax error, position at "
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            int r35 = r17.pos()     // Catch:{ all -> 0x00ea }
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            java.lang.String r35 = ", name "
            java.lang.StringBuilder r34 = r34.append(r35)     // Catch:{ all -> 0x00ea }
            r0 = r34
            r1 = r16
            java.lang.StringBuilder r34 = r0.append(r1)     // Catch:{ all -> 0x00ea }
            java.lang.String r34 = r34.toString()     // Catch:{ all -> 0x00ea }
            r33.<init>(r34)     // Catch:{ all -> 0x00ea }
            throw r33     // Catch:{ all -> 0x00ea }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parseObject(java.util.Map, java.lang.Object):java.lang.Object");
    }

    public ParserConfig getConfig() {
        return this.config;
    }

    public void setConfig(ParserConfig config2) {
        this.config = config2;
    }

    public <T> T parseObject(Class<T> clazz) {
        return parseObject((Type) clazz, (Object) null);
    }

    public <T> T parseObject(Type type) {
        return parseObject(type, (Object) null);
    }

    public <T> T parseObject(Type type, Object fieldName) {
        int token = this.lexer.token();
        if (token == 8) {
            this.lexer.nextToken();
            return null;
        }
        if (token == 4) {
            if (type == byte[].class) {
                Object bytesValue = this.lexer.bytesValue();
                this.lexer.nextToken();
                return bytesValue;
            } else if (type == char[].class) {
                String strVal = this.lexer.stringVal();
                this.lexer.nextToken();
                return strVal.toCharArray();
            }
        }
        try {
            return this.config.getDeserializer(type).deserialze(this, type, fieldName);
        } catch (JSONException e) {
            throw e;
        } catch (Throwable e2) {
            throw new JSONException(e2.getMessage(), e2);
        }
    }

    public <T> List<T> parseArray(Class<T> clazz) {
        List<T> array = new ArrayList<>();
        parseArray((Class<?>) clazz, (Collection) array);
        return array;
    }

    public void parseArray(Class<?> clazz, Collection array) {
        parseArray((Type) clazz, array);
    }

    public void parseArray(Type type, Collection array) {
        parseArray(type, array, (Object) null);
    }

    /* JADX INFO: finally extract failed */
    public void parseArray(Type type, Collection array, Object fieldName) {
        ObjectDeserializer deserializer;
        Object deserialze;
        String value;
        if (this.lexer.token() == 21 || this.lexer.token() == 22) {
            this.lexer.nextToken();
        }
        if (this.lexer.token() != 14) {
            throw new JSONException("exepct '[', but " + JSONToken.name(this.lexer.token()) + ", " + this.lexer.info());
        }
        if (Integer.TYPE == type) {
            deserializer = IntegerCodec.instance;
            this.lexer.nextToken(2);
        } else if (String.class == type) {
            deserializer = StringCodec.instance;
            this.lexer.nextToken(4);
        } else {
            deserializer = this.config.getDeserializer(type);
            this.lexer.nextToken(deserializer.getFastMatchToken());
        }
        ParseContext context2 = this.context;
        setContext(array, fieldName);
        int i = 0;
        while (true) {
            if (this.lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                while (this.lexer.token() == 16) {
                    this.lexer.nextToken();
                }
            }
            try {
                if (this.lexer.token() == 15) {
                    setContext(context2);
                    this.lexer.nextToken(16);
                    return;
                }
                if (Integer.TYPE == type) {
                    array.add(IntegerCodec.instance.deserialze(this, (Type) null, (Object) null));
                } else if (String.class == type) {
                    if (this.lexer.token() == 4) {
                        value = this.lexer.stringVal();
                        this.lexer.nextToken(16);
                    } else {
                        Object obj = parse();
                        if (obj == null) {
                            value = null;
                        } else {
                            value = obj.toString();
                        }
                    }
                    array.add(value);
                } else {
                    if (this.lexer.token() == 8) {
                        this.lexer.nextToken();
                        deserialze = null;
                    } else {
                        deserialze = deserializer.deserialze(this, type, Integer.valueOf(i));
                    }
                    array.add(deserialze);
                    checkListResolve(array);
                }
                if (this.lexer.token() == 16) {
                    this.lexer.nextToken(deserializer.getFastMatchToken());
                }
                i++;
            } catch (Throwable th) {
                setContext(context2);
                throw th;
            }
        }
    }

    public Object[] parseArray(Type[] types) {
        Object value;
        if (this.lexer.token() == 8) {
            this.lexer.nextToken(16);
            return null;
        } else if (this.lexer.token() != 14) {
            throw new JSONException("syntax error : " + this.lexer.tokenName());
        } else {
            Object[] list = new Object[types.length];
            if (types.length == 0) {
                this.lexer.nextToken(15);
                if (this.lexer.token() != 15) {
                    throw new JSONException("syntax error");
                }
                this.lexer.nextToken(16);
                return new Object[0];
            }
            this.lexer.nextToken(2);
            int i = 0;
            while (i < types.length) {
                if (this.lexer.token() == 8) {
                    value = null;
                    this.lexer.nextToken(16);
                } else {
                    Class<?> type = types[i];
                    if (type == Integer.TYPE || type == Integer.class) {
                        if (this.lexer.token() == 2) {
                            value = Integer.valueOf(this.lexer.intValue());
                            this.lexer.nextToken(16);
                        } else {
                            value = TypeUtils.cast(parse(), (Type) type, this.config);
                        }
                    } else if (type != String.class) {
                        boolean isArray = false;
                        Class<?> componentType = null;
                        if (i == types.length - 1 && (type instanceof Class)) {
                            Class<?> clazz = type;
                            isArray = clazz.isArray();
                            componentType = clazz.getComponentType();
                        }
                        if (!isArray || this.lexer.token() == 14) {
                            value = this.config.getDeserializer((Type) type).deserialze(this, type, (Object) null);
                        } else {
                            List<Object> varList = new ArrayList<>();
                            ObjectDeserializer derializer = this.config.getDeserializer((Type) componentType);
                            int fastMatch = derializer.getFastMatchToken();
                            if (this.lexer.token() != 15) {
                                while (true) {
                                    varList.add(derializer.deserialze(this, type, (Object) null));
                                    if (this.lexer.token() != 16) {
                                        break;
                                    }
                                    this.lexer.nextToken(fastMatch);
                                }
                                if (this.lexer.token() != 15) {
                                    throw new JSONException("syntax error :" + JSONToken.name(this.lexer.token()));
                                }
                            }
                            value = TypeUtils.cast((Object) varList, (Type) type, this.config);
                        }
                    } else if (this.lexer.token() == 4) {
                        value = this.lexer.stringVal();
                        this.lexer.nextToken(16);
                    } else {
                        value = TypeUtils.cast(parse(), (Type) type, this.config);
                    }
                }
                list[i] = value;
                if (this.lexer.token() == 15) {
                    break;
                } else if (this.lexer.token() != 16) {
                    throw new JSONException("syntax error :" + JSONToken.name(this.lexer.token()));
                } else {
                    if (i == types.length - 1) {
                        this.lexer.nextToken(15);
                    } else {
                        this.lexer.nextToken(2);
                    }
                    i++;
                }
            }
            if (this.lexer.token() != 15) {
                throw new JSONException("syntax error");
            }
            this.lexer.nextToken(16);
            return list;
        }
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [com.alibaba.fastjson.parser.deserializer.ObjectDeserializer] */
    /* JADX WARNING: CFG modification limit reached, blocks count: 150 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void parseObject(java.lang.Object r13) {
        /*
            r12 = this;
            java.lang.Class r1 = r13.getClass()
            r0 = 0
            com.alibaba.fastjson.parser.ParserConfig r9 = r12.config
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r2 = r9.getDeserializer((java.lang.reflect.Type) r1)
            boolean r9 = r2 instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer
            if (r9 == 0) goto L_0x0012
            r0 = r2
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r0 = (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) r0
        L_0x0012:
            com.alibaba.fastjson.parser.JSONLexer r9 = r12.lexer
            int r9 = r9.token()
            r10 = 12
            if (r9 == r10) goto L_0x005a
            com.alibaba.fastjson.parser.JSONLexer r9 = r12.lexer
            int r9 = r9.token()
            r10 = 16
            if (r9 == r10) goto L_0x005a
            com.alibaba.fastjson.JSONException r9 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "syntax error, expect {, actual "
            java.lang.StringBuilder r10 = r10.append(r11)
            com.alibaba.fastjson.parser.JSONLexer r11 = r12.lexer
            java.lang.String r11 = r11.tokenName()
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r10 = r10.toString()
            r9.<init>(r10)
            throw r9
        L_0x0046:
            com.alibaba.fastjson.parser.JSONLexer r9 = r12.lexer
            int r9 = r9.token()
            r10 = 16
            if (r9 != r10) goto L_0x0076
            com.alibaba.fastjson.parser.JSONLexer r9 = r12.lexer
            com.alibaba.fastjson.parser.Feature r10 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas
            boolean r9 = r9.isEnabled((com.alibaba.fastjson.parser.Feature) r10)
            if (r9 == 0) goto L_0x0076
        L_0x005a:
            com.alibaba.fastjson.parser.JSONLexer r9 = r12.lexer
            com.alibaba.fastjson.parser.SymbolTable r10 = r12.symbolTable
            java.lang.String r8 = r9.scanSymbol(r10)
            if (r8 != 0) goto L_0x0076
            com.alibaba.fastjson.parser.JSONLexer r9 = r12.lexer
            int r9 = r9.token()
            r10 = 13
            if (r9 != r10) goto L_0x0046
            com.alibaba.fastjson.parser.JSONLexer r9 = r12.lexer
            r10 = 16
            r9.nextToken(r10)
        L_0x0075:
            return
        L_0x0076:
            r4 = 0
            if (r0 == 0) goto L_0x007d
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer r4 = r0.getFieldDeserializer(r8)
        L_0x007d:
            if (r4 != 0) goto L_0x00ca
            com.alibaba.fastjson.parser.JSONLexer r9 = r12.lexer
            com.alibaba.fastjson.parser.Feature r10 = com.alibaba.fastjson.parser.Feature.IgnoreNotMatch
            boolean r9 = r9.isEnabled((com.alibaba.fastjson.parser.Feature) r10)
            if (r9 != 0) goto L_0x00b2
            com.alibaba.fastjson.JSONException r9 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "setter not found, class "
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r11 = r1.getName()
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r11 = ", property "
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r8)
            java.lang.String r10 = r10.toString()
            r9.<init>(r10)
            throw r9
        L_0x00b2:
            com.alibaba.fastjson.parser.JSONLexer r9 = r12.lexer
            r9.nextTokenWithColon()
            r12.parse()
            com.alibaba.fastjson.parser.JSONLexer r9 = r12.lexer
            int r9 = r9.token()
            r10 = 13
            if (r9 != r10) goto L_0x005a
            com.alibaba.fastjson.parser.JSONLexer r9 = r12.lexer
            r9.nextToken()
            goto L_0x0075
        L_0x00ca:
            com.alibaba.fastjson.util.FieldInfo r9 = r4.fieldInfo
            java.lang.Class<?> r3 = r9.fieldClass
            com.alibaba.fastjson.util.FieldInfo r9 = r4.fieldInfo
            java.lang.reflect.Type r5 = r9.fieldType
            java.lang.Class r9 = java.lang.Integer.TYPE
            if (r3 != r9) goto L_0x0103
            com.alibaba.fastjson.parser.JSONLexer r9 = r12.lexer
            r10 = 2
            r9.nextTokenWithColon(r10)
            com.alibaba.fastjson.serializer.IntegerCodec r9 = com.alibaba.fastjson.serializer.IntegerCodec.instance
            r10 = 0
            java.lang.Object r6 = r9.deserialze(r12, r5, r10)
        L_0x00e3:
            r4.setValue((java.lang.Object) r13, (java.lang.Object) r6)
            com.alibaba.fastjson.parser.JSONLexer r9 = r12.lexer
            int r9 = r9.token()
            r10 = 16
            if (r9 == r10) goto L_0x005a
            com.alibaba.fastjson.parser.JSONLexer r9 = r12.lexer
            int r9 = r9.token()
            r10 = 13
            if (r9 != r10) goto L_0x005a
            com.alibaba.fastjson.parser.JSONLexer r9 = r12.lexer
            r10 = 16
            r9.nextToken(r10)
            goto L_0x0075
        L_0x0103:
            java.lang.Class<java.lang.String> r9 = java.lang.String.class
            if (r3 != r9) goto L_0x0112
            com.alibaba.fastjson.parser.JSONLexer r9 = r12.lexer
            r10 = 4
            r9.nextTokenWithColon(r10)
            java.lang.Object r6 = com.alibaba.fastjson.serializer.StringCodec.deserialze(r12)
            goto L_0x00e3
        L_0x0112:
            java.lang.Class r9 = java.lang.Long.TYPE
            if (r3 != r9) goto L_0x0124
            com.alibaba.fastjson.parser.JSONLexer r9 = r12.lexer
            r10 = 2
            r9.nextTokenWithColon(r10)
            com.alibaba.fastjson.serializer.LongCodec r9 = com.alibaba.fastjson.serializer.LongCodec.instance
            r10 = 0
            java.lang.Object r6 = r9.deserialze(r12, r5, r10)
            goto L_0x00e3
        L_0x0124:
            com.alibaba.fastjson.parser.ParserConfig r9 = r12.config
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r7 = r9.getDeserializer(r3, r5)
            com.alibaba.fastjson.parser.JSONLexer r9 = r12.lexer
            int r10 = r7.getFastMatchToken()
            r9.nextTokenWithColon(r10)
            r9 = 0
            java.lang.Object r6 = r7.deserialze(r12, r5, r9)
            goto L_0x00e3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parseObject(java.lang.Object):void");
    }

    public Object parseArrayWithType(Type collectionType) {
        if (this.lexer.token() == 8) {
            this.lexer.nextToken();
            return null;
        }
        Type[] actualTypes = ((ParameterizedType) collectionType).getActualTypeArguments();
        if (actualTypes.length != 1) {
            throw new JSONException("not support type " + collectionType);
        }
        Type actualTypeArgument = actualTypes[0];
        if (actualTypeArgument instanceof Class) {
            List<Object> array = new ArrayList<>();
            parseArray((Class<?>) (Class) actualTypeArgument, (Collection) array);
            return array;
        } else if (actualTypeArgument instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) actualTypeArgument;
            Type upperBoundType = wildcardType.getUpperBounds()[0];
            if (!Object.class.equals(upperBoundType)) {
                List<Object> array2 = new ArrayList<>();
                parseArray((Class<?>) (Class) upperBoundType, (Collection) array2);
                return array2;
            } else if (wildcardType.getLowerBounds().length == 0) {
                return parse();
            } else {
                throw new JSONException("not support type : " + collectionType);
            }
        } else {
            if (actualTypeArgument instanceof TypeVariable) {
                TypeVariable<?> typeVariable = (TypeVariable) actualTypeArgument;
                Type[] bounds = typeVariable.getBounds();
                if (bounds.length != 1) {
                    throw new JSONException("not support : " + typeVariable);
                }
                Type boundType = bounds[0];
                if (boundType instanceof Class) {
                    List<Object> array3 = new ArrayList<>();
                    parseArray((Class<?>) (Class) boundType, (Collection) array3);
                    return array3;
                }
            }
            if (actualTypeArgument instanceof ParameterizedType) {
                List<Object> array4 = new ArrayList<>();
                parseArray((Type) (ParameterizedType) actualTypeArgument, (Collection) array4);
                return array4;
            }
            throw new JSONException("TODO : " + collectionType);
        }
    }

    public void acceptType(String typeName) {
        JSONLexer lexer2 = this.lexer;
        lexer2.nextTokenWithColon();
        if (lexer2.token() != 4) {
            throw new JSONException("type not match error");
        } else if (typeName.equals(lexer2.stringVal())) {
            lexer2.nextToken();
            if (lexer2.token() == 16) {
                lexer2.nextToken();
            }
        } else {
            throw new JSONException("type not match error");
        }
    }

    public int getResolveStatus() {
        return this.resolveStatus;
    }

    public void setResolveStatus(int resolveStatus2) {
        this.resolveStatus = resolveStatus2;
    }

    public Object getObject(String path) {
        for (int i = 0; i < this.contextArrayIndex; i++) {
            if (path.equals(this.contextArray[i].toString())) {
                return this.contextArray[i].object;
            }
        }
        return null;
    }

    public void checkListResolve(Collection array) {
        if (this.resolveStatus != 1) {
            return;
        }
        if (array instanceof List) {
            ResolveTask task = getLastResolveTask();
            task.fieldDeserializer = new ResolveFieldDeserializer(this, (List) array, array.size() - 1);
            task.ownerContext = this.context;
            setResolveStatus(0);
            return;
        }
        ResolveTask task2 = getLastResolveTask();
        task2.fieldDeserializer = new ResolveFieldDeserializer(array);
        task2.ownerContext = this.context;
        setResolveStatus(0);
    }

    public void checkMapResolve(Map object, Object fieldName) {
        if (this.resolveStatus == 1) {
            ResolveFieldDeserializer fieldResolver = new ResolveFieldDeserializer(object, fieldName);
            ResolveTask task = getLastResolveTask();
            task.fieldDeserializer = fieldResolver;
            task.ownerContext = this.context;
            setResolveStatus(0);
        }
    }

    public Object parseObject(Map object) {
        return parseObject(object, (Object) null);
    }

    public JSONObject parseObject() {
        return (JSONObject) parseObject((Map) new JSONObject(this.lexer.isEnabled(Feature.OrderedField)));
    }

    public final void parseArray(Collection array) {
        parseArray(array, (Object) null);
    }

    public final void parseArray(Collection array, Object fieldName) {
        Object value;
        Object value2;
        JSONLexer lexer2 = this.lexer;
        if (lexer2.token() == 21 || lexer2.token() == 22) {
            lexer2.nextToken();
        }
        if (lexer2.token() != 14) {
            throw new JSONException("syntax error, expect [, actual " + JSONToken.name(lexer2.token()) + ", pos " + lexer2.pos());
        }
        lexer2.nextToken(4);
        ParseContext context2 = this.context;
        setContext(array, fieldName);
        int i = 0;
        while (true) {
            if (lexer2.isEnabled(Feature.AllowArbitraryCommas)) {
                while (lexer2.token() == 16) {
                    lexer2.nextToken();
                }
            }
            try {
                switch (lexer2.token()) {
                    case 2:
                        value = lexer2.integerValue();
                        lexer2.nextToken(16);
                        break;
                    case 3:
                        if (lexer2.isEnabled(Feature.UseBigDecimal)) {
                            value = lexer2.decimalValue(true);
                        } else {
                            value = lexer2.decimalValue(false);
                        }
                        lexer2.nextToken(16);
                        break;
                    case 4:
                        String stringLiteral = lexer2.stringVal();
                        lexer2.nextToken(16);
                        if (!lexer2.isEnabled(Feature.AllowISO8601DateFormat)) {
                            value = stringLiteral;
                            break;
                        } else {
                            JSONScanner iso8601Lexer = new JSONScanner(stringLiteral);
                            if (iso8601Lexer.scanISO8601DateIfMatch()) {
                                value2 = iso8601Lexer.getCalendar().getTime();
                            } else {
                                value2 = stringLiteral;
                            }
                            iso8601Lexer.close();
                            break;
                        }
                    case 6:
                        value = Boolean.TRUE;
                        lexer2.nextToken(16);
                        break;
                    case 7:
                        value = Boolean.FALSE;
                        lexer2.nextToken(16);
                        break;
                    case 8:
                        value = null;
                        lexer2.nextToken(4);
                        break;
                    case 12:
                        value = parseObject((Map) new JSONObject(lexer2.isEnabled(Feature.OrderedField)), (Object) Integer.valueOf(i));
                        break;
                    case 14:
                        Collection items = new JSONArray();
                        parseArray(items, (Object) Integer.valueOf(i));
                        if (!lexer2.isEnabled(Feature.UseObjectArray)) {
                            value = items;
                            break;
                        } else {
                            value = items.toArray();
                            break;
                        }
                    case 15:
                        lexer2.nextToken(16);
                        return;
                    case 20:
                        throw new JSONException("unclosed jsonArray");
                    case 23:
                        value = null;
                        lexer2.nextToken(4);
                        break;
                    default:
                        value = parse();
                        break;
                }
                array.add(value);
                checkListResolve(array);
                if (lexer2.token() == 16) {
                    lexer2.nextToken(4);
                }
                i++;
            } finally {
                setContext(context2);
            }
        }
    }

    public ParseContext getContext() {
        return this.context;
    }

    public List<ResolveTask> getResolveTaskList() {
        if (this.resolveTaskList == null) {
            this.resolveTaskList = new ArrayList(2);
        }
        return this.resolveTaskList;
    }

    public void addResolveTask(ResolveTask task) {
        if (this.resolveTaskList == null) {
            this.resolveTaskList = new ArrayList(2);
        }
        this.resolveTaskList.add(task);
    }

    public ResolveTask getLastResolveTask() {
        return this.resolveTaskList.get(this.resolveTaskList.size() - 1);
    }

    public List<ExtraProcessor> getExtraProcessors() {
        if (this.extraProcessors == null) {
            this.extraProcessors = new ArrayList(2);
        }
        return this.extraProcessors;
    }

    public List<ExtraTypeProvider> getExtraTypeProviders() {
        if (this.extraTypeProviders == null) {
            this.extraTypeProviders = new ArrayList(2);
        }
        return this.extraTypeProviders;
    }

    public FieldTypeResolver getFieldTypeResolver() {
        return this.fieldTypeResolver;
    }

    public void setFieldTypeResolver(FieldTypeResolver fieldTypeResolver2) {
        this.fieldTypeResolver = fieldTypeResolver2;
    }

    public void setContext(ParseContext context2) {
        if (!this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            this.context = context2;
        }
    }

    public void popContext() {
        if (!this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            this.context = this.context.parent;
            this.contextArray[this.contextArrayIndex - 1] = null;
            this.contextArrayIndex--;
        }
    }

    public ParseContext setContext(Object object, Object fieldName) {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }
        return setContext(this.context, object, fieldName);
    }

    public ParseContext setContext(ParseContext parent, Object object, Object fieldName) {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }
        this.context = new ParseContext(parent, object, fieldName);
        addContext(this.context);
        return this.context;
    }

    private void addContext(ParseContext context2) {
        int i = this.contextArrayIndex;
        this.contextArrayIndex = i + 1;
        if (this.contextArray == null) {
            this.contextArray = new ParseContext[8];
        } else if (i >= this.contextArray.length) {
            ParseContext[] newArray = new ParseContext[((this.contextArray.length * 3) / 2)];
            System.arraycopy(this.contextArray, 0, newArray, 0, this.contextArray.length);
            this.contextArray = newArray;
        }
        this.contextArray[i] = context2;
    }

    public Object parse() {
        return parse((Object) null);
    }

    public Object parseKey() {
        if (this.lexer.token() != 18) {
            return parse((Object) null);
        }
        String value = this.lexer.stringVal();
        this.lexer.nextToken(16);
        return value;
    }

    public Object parse(Object fieldName) {
        JSONLexer lexer2 = this.lexer;
        switch (lexer2.token()) {
            case 2:
                Number intValue = lexer2.integerValue();
                lexer2.nextToken();
                return intValue;
            case 3:
                Number value = lexer2.decimalValue(lexer2.isEnabled(Feature.UseBigDecimal));
                lexer2.nextToken();
                return value;
            case 4:
                String stringLiteral = lexer2.stringVal();
                lexer2.nextToken(16);
                if (lexer2.isEnabled(Feature.AllowISO8601DateFormat)) {
                    JSONScanner iso8601Lexer = new JSONScanner(stringLiteral);
                    try {
                        if (iso8601Lexer.scanISO8601DateIfMatch()) {
                            return iso8601Lexer.getCalendar().getTime();
                        }
                        iso8601Lexer.close();
                    } finally {
                        iso8601Lexer.close();
                    }
                }
                return stringLiteral;
            case 6:
                lexer2.nextToken();
                return Boolean.TRUE;
            case 7:
                lexer2.nextToken();
                return Boolean.FALSE;
            case 8:
                lexer2.nextToken();
                return null;
            case 9:
                lexer2.nextToken(18);
                if (lexer2.token() != 18) {
                    throw new JSONException("syntax error");
                }
                lexer2.nextToken(10);
                accept(10);
                long time = lexer2.integerValue().longValue();
                accept(2);
                accept(11);
                return new Date(time);
            case 12:
                return parseObject((Map) new JSONObject(lexer2.isEnabled(Feature.OrderedField)), fieldName);
            case 14:
                JSONArray array = new JSONArray();
                parseArray((Collection) array, fieldName);
                return lexer2.isEnabled(Feature.UseObjectArray) ? array.toArray() : array;
            case 20:
                if (lexer2.isBlankInput()) {
                    return null;
                }
                throw new JSONException("unterminated json string, " + lexer2.info());
            case 21:
                lexer2.nextToken();
                HashSet<Object> set = new HashSet<>();
                parseArray((Collection) set, fieldName);
                return set;
            case 22:
                lexer2.nextToken();
                TreeSet<Object> treeSet = new TreeSet<>();
                parseArray((Collection) treeSet, fieldName);
                return treeSet;
            case 23:
                lexer2.nextToken();
                return null;
            default:
                throw new JSONException("syntax error, " + lexer2.info());
        }
    }

    public void config(Feature feature, boolean state) {
        this.lexer.config(feature, state);
    }

    public boolean isEnabled(Feature feature) {
        return this.lexer.isEnabled(feature);
    }

    public JSONLexer getLexer() {
        return this.lexer;
    }

    public final void accept(int token) {
        JSONLexer lexer2 = this.lexer;
        if (lexer2.token() == token) {
            lexer2.nextToken();
            return;
        }
        throw new JSONException("syntax error, expect " + JSONToken.name(token) + ", actual " + JSONToken.name(lexer2.token()));
    }

    public final void accept(int token, int nextExpectToken) {
        JSONLexer lexer2 = this.lexer;
        if (lexer2.token() == token) {
            lexer2.nextToken(nextExpectToken);
        } else {
            throwException(token);
        }
    }

    public void throwException(int token) {
        throw new JSONException("syntax error, expect " + JSONToken.name(token) + ", actual " + JSONToken.name(this.lexer.token()));
    }

    public void close() {
        JSONLexer lexer2 = this.lexer;
        try {
            if (lexer2.isEnabled(Feature.AutoCloseSource) && lexer2.token() != 20) {
                throw new JSONException("not close json text, token : " + JSONToken.name(lexer2.token()));
            }
        } finally {
            lexer2.close();
        }
    }

    public void handleResovleTask(Object value) {
        if (this.resolveTaskList != null) {
            int size = this.resolveTaskList.size();
            for (int i = 0; i < size; i++) {
                ResolveTask task = this.resolveTaskList.get(i);
                String ref = task.referenceValue;
                Object object = null;
                if (task.ownerContext != null) {
                    object = task.ownerContext.object;
                }
                Object refValue = ref.startsWith(SymbolExpUtil.SYMBOL_DOLLAR) ? getObject(ref) : task.context.object;
                FieldDeserializer fieldDeser = task.fieldDeserializer;
                if (fieldDeser != null) {
                    fieldDeser.setValue(object, refValue);
                }
            }
        }
    }

    public static class ResolveTask {
        public final ParseContext context;
        public FieldDeserializer fieldDeserializer;
        public ParseContext ownerContext;
        public final String referenceValue;

        public ResolveTask(ParseContext context2, String referenceValue2) {
            this.context = context2;
            this.referenceValue = referenceValue2;
        }
    }

    public void parseExtra(Object object, String key) {
        Object value;
        this.lexer.nextTokenWithColon();
        Type type = null;
        if (this.extraTypeProviders != null) {
            for (ExtraTypeProvider extraProvider : this.extraTypeProviders) {
                type = extraProvider.getExtraType(object, key);
            }
        }
        if (type == null) {
            value = parse();
        } else {
            value = parseObject(type);
        }
        if (object instanceof ExtraProcessable) {
            ((ExtraProcessable) object).processExtra(key, value);
            return;
        }
        if (this.extraProcessors != null) {
            for (ExtraProcessor process : this.extraProcessors) {
                process.processExtra(object, key, value);
            }
        }
        if (this.resolveStatus == 1) {
            this.resolveStatus = 0;
        }
    }
}
