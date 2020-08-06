package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory;
import com.alibaba.fastjson.parser.deserializer.ArrayListTypeFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.AutowiredObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.EnumDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec;
import com.alibaba.fastjson.parser.deserializer.MapDeserializer;
import com.alibaba.fastjson.parser.deserializer.NumberDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.OptionalCodec;
import com.alibaba.fastjson.parser.deserializer.SqlDateDeserializer;
import com.alibaba.fastjson.parser.deserializer.StackTraceElementDeserializer;
import com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer;
import com.alibaba.fastjson.parser.deserializer.TimeDeserializer;
import com.alibaba.fastjson.serializer.AtomicCodec;
import com.alibaba.fastjson.serializer.AwtCodec;
import com.alibaba.fastjson.serializer.BigDecimalCodec;
import com.alibaba.fastjson.serializer.BigIntegerCodec;
import com.alibaba.fastjson.serializer.BooleanCodec;
import com.alibaba.fastjson.serializer.CalendarCodec;
import com.alibaba.fastjson.serializer.CharArrayCodec;
import com.alibaba.fastjson.serializer.CharacterCodec;
import com.alibaba.fastjson.serializer.CollectionCodec;
import com.alibaba.fastjson.serializer.DateCodec;
import com.alibaba.fastjson.serializer.FloatCodec;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.LongCodec;
import com.alibaba.fastjson.serializer.MiscCodec;
import com.alibaba.fastjson.serializer.ObjectArrayCodec;
import com.alibaba.fastjson.serializer.ReferenceCodec;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.IdentityHashMap;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.ServiceLoader;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.Closeable;
import java.io.File;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.AccessControlException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import javax.xml.datatype.XMLGregorianCalendar;

public class ParserConfig {
    public static final String AUTOTYPE_ACCEPT = "fastjson.parser.autoTypeAccept";
    public static final String AUTOTYPE_SUPPORT_PROPERTY = "fastjson.parser.autoTypeSupport";
    public static final boolean AUTO_SUPPORT = "true".equals(IOUtils.getStringProperty(AUTOTYPE_SUPPORT_PROPERTY));
    private static final String[] AUTO_TYPE_ACCEPT_LIST;
    public static final String[] DENYS = splitItemsFormProperty(IOUtils.getStringProperty(DENY_PROPERTY));
    public static final String DENY_PROPERTY = "fastjson.parser.deny";
    private static boolean awtError = false;
    public static ParserConfig global = new ParserConfig();
    private static boolean jdk8Error = false;
    private String[] acceptList;
    private boolean asmEnable;
    protected ASMDeserializerFactory asmFactory;
    private boolean autoTypeSupport;
    protected ClassLoader defaultClassLoader;
    private String[] denyList;
    private final IdentityHashMap<Type, ObjectDeserializer> deserializers;
    public PropertyNamingStrategy propertyNamingStrategy;
    public final SymbolTable symbolTable;

    static {
        String[] items = splitItemsFormProperty(IOUtils.getStringProperty(AUTOTYPE_ACCEPT));
        if (items == null) {
            items = new String[0];
        }
        AUTO_TYPE_ACCEPT_LIST = items;
    }

    public static ParserConfig getGlobalInstance() {
        return global;
    }

    public ParserConfig() {
        this((ASMDeserializerFactory) null, (ClassLoader) null);
    }

    public ParserConfig(ClassLoader parentClassLoader) {
        this((ASMDeserializerFactory) null, parentClassLoader);
    }

    public ParserConfig(ASMDeserializerFactory asmFactory2) {
        this(asmFactory2, (ClassLoader) null);
    }

    private ParserConfig(ASMDeserializerFactory asmFactory2, ClassLoader parentClassLoader) {
        this.deserializers = new IdentityHashMap<>();
        this.asmEnable = !ASMUtils.IS_ANDROID;
        this.symbolTable = new SymbolTable(4096);
        this.autoTypeSupport = AUTO_SUPPORT;
        this.denyList = "bsh,com.mchange,com.sun.,java.lang.Thread,java.net.Socket,java.rmi,javax.xml,org.apache.bcel,org.apache.commons.beanutils,org.apache.commons.collections.Transformer,org.apache.commons.collections.functors,org.apache.commons.collections4.comparators,org.apache.commons.fileupload,org.apache.myfaces.context.servlet,org.apache.tomcat,org.apache.wicket.util,org.codehaus.groovy.runtime,org.hibernate,org.jboss,org.mozilla.javascript,org.python.core,org.springframework".split(",");
        this.acceptList = AUTO_TYPE_ACCEPT_LIST;
        if (asmFactory2 == null && !ASMUtils.IS_ANDROID) {
            if (parentClassLoader == null) {
                try {
                    asmFactory2 = new ASMDeserializerFactory(new ASMClassLoader());
                } catch (ExceptionInInitializerError | NoClassDefFoundError | AccessControlException e) {
                }
            } else {
                asmFactory2 = new ASMDeserializerFactory(parentClassLoader);
            }
        }
        this.asmFactory = asmFactory2;
        if (asmFactory2 == null) {
            this.asmEnable = false;
        }
        this.deserializers.put(SimpleDateFormat.class, MiscCodec.instance);
        this.deserializers.put(Timestamp.class, SqlDateDeserializer.instance_timestamp);
        this.deserializers.put(Date.class, SqlDateDeserializer.instance);
        this.deserializers.put(Time.class, TimeDeserializer.instance);
        this.deserializers.put(java.util.Date.class, DateCodec.instance);
        this.deserializers.put(Calendar.class, CalendarCodec.instance);
        this.deserializers.put(XMLGregorianCalendar.class, CalendarCodec.instance);
        this.deserializers.put(JSONObject.class, MapDeserializer.instance);
        this.deserializers.put(JSONArray.class, CollectionCodec.instance);
        this.deserializers.put(Map.class, MapDeserializer.instance);
        this.deserializers.put(HashMap.class, MapDeserializer.instance);
        this.deserializers.put(LinkedHashMap.class, MapDeserializer.instance);
        this.deserializers.put(TreeMap.class, MapDeserializer.instance);
        this.deserializers.put(ConcurrentMap.class, MapDeserializer.instance);
        this.deserializers.put(ConcurrentHashMap.class, MapDeserializer.instance);
        this.deserializers.put(Collection.class, CollectionCodec.instance);
        this.deserializers.put(List.class, CollectionCodec.instance);
        this.deserializers.put(ArrayList.class, CollectionCodec.instance);
        this.deserializers.put(Object.class, JavaObjectDeserializer.instance);
        this.deserializers.put(String.class, StringCodec.instance);
        this.deserializers.put(StringBuffer.class, StringCodec.instance);
        this.deserializers.put(StringBuilder.class, StringCodec.instance);
        this.deserializers.put(Character.TYPE, CharacterCodec.instance);
        this.deserializers.put(Character.class, CharacterCodec.instance);
        this.deserializers.put(Byte.TYPE, NumberDeserializer.instance);
        this.deserializers.put(Byte.class, NumberDeserializer.instance);
        this.deserializers.put(Short.TYPE, NumberDeserializer.instance);
        this.deserializers.put(Short.class, NumberDeserializer.instance);
        this.deserializers.put(Integer.TYPE, IntegerCodec.instance);
        this.deserializers.put(Integer.class, IntegerCodec.instance);
        this.deserializers.put(Long.TYPE, LongCodec.instance);
        this.deserializers.put(Long.class, LongCodec.instance);
        this.deserializers.put(BigInteger.class, BigIntegerCodec.instance);
        this.deserializers.put(BigDecimal.class, BigDecimalCodec.instance);
        this.deserializers.put(Float.TYPE, FloatCodec.instance);
        this.deserializers.put(Float.class, FloatCodec.instance);
        this.deserializers.put(Double.TYPE, NumberDeserializer.instance);
        this.deserializers.put(Double.class, NumberDeserializer.instance);
        this.deserializers.put(Boolean.TYPE, BooleanCodec.instance);
        this.deserializers.put(Boolean.class, BooleanCodec.instance);
        this.deserializers.put(Class.class, MiscCodec.instance);
        this.deserializers.put(char[].class, new CharArrayCodec());
        this.deserializers.put(AtomicBoolean.class, BooleanCodec.instance);
        this.deserializers.put(AtomicInteger.class, IntegerCodec.instance);
        this.deserializers.put(AtomicLong.class, LongCodec.instance);
        this.deserializers.put(AtomicReference.class, ReferenceCodec.instance);
        this.deserializers.put(WeakReference.class, ReferenceCodec.instance);
        this.deserializers.put(SoftReference.class, ReferenceCodec.instance);
        this.deserializers.put(UUID.class, MiscCodec.instance);
        this.deserializers.put(TimeZone.class, MiscCodec.instance);
        this.deserializers.put(Locale.class, MiscCodec.instance);
        this.deserializers.put(Currency.class, MiscCodec.instance);
        this.deserializers.put(InetAddress.class, MiscCodec.instance);
        this.deserializers.put(Inet4Address.class, MiscCodec.instance);
        this.deserializers.put(Inet6Address.class, MiscCodec.instance);
        this.deserializers.put(InetSocketAddress.class, MiscCodec.instance);
        this.deserializers.put(File.class, MiscCodec.instance);
        this.deserializers.put(URI.class, MiscCodec.instance);
        this.deserializers.put(URL.class, MiscCodec.instance);
        this.deserializers.put(Pattern.class, MiscCodec.instance);
        this.deserializers.put(Charset.class, MiscCodec.instance);
        this.deserializers.put(JSONPath.class, MiscCodec.instance);
        this.deserializers.put(Number.class, NumberDeserializer.instance);
        this.deserializers.put(AtomicIntegerArray.class, AtomicCodec.instance);
        this.deserializers.put(AtomicLongArray.class, AtomicCodec.instance);
        this.deserializers.put(StackTraceElement.class, StackTraceElementDeserializer.instance);
        this.deserializers.put(Serializable.class, JavaObjectDeserializer.instance);
        this.deserializers.put(Cloneable.class, JavaObjectDeserializer.instance);
        this.deserializers.put(Comparable.class, JavaObjectDeserializer.instance);
        this.deserializers.put(Closeable.class, JavaObjectDeserializer.instance);
        addItemsToDeny(DENYS);
        addItemsToAccept(AUTO_TYPE_ACCEPT_LIST);
    }

    private static String[] splitItemsFormProperty(String property) {
        if (property == null || property.length() <= 0) {
            return null;
        }
        return property.split(",");
    }

    public void configFromPropety(Properties properties) {
        addItemsToDeny(splitItemsFormProperty(properties.getProperty(DENY_PROPERTY)));
        addItemsToAccept(splitItemsFormProperty(properties.getProperty(AUTOTYPE_ACCEPT)));
        String property = properties.getProperty(AUTOTYPE_SUPPORT_PROPERTY);
        if ("true".equals(property)) {
            this.autoTypeSupport = true;
        } else if ("false".equals(property)) {
            this.autoTypeSupport = false;
        }
    }

    private void addItemsToDeny(String[] items) {
        if (items != null) {
            for (String item : items) {
                addDeny(item);
            }
        }
    }

    private void addItemsToAccept(String[] items) {
        if (items != null) {
            for (String item : items) {
                addAccept(item);
            }
        }
    }

    public boolean isAutoTypeSupport() {
        return this.autoTypeSupport;
    }

    public void setAutoTypeSupport(boolean autoTypeSupport2) {
        this.autoTypeSupport = autoTypeSupport2;
    }

    public boolean isAsmEnable() {
        return this.asmEnable;
    }

    public void setAsmEnable(boolean asmEnable2) {
        this.asmEnable = asmEnable2;
    }

    public IdentityHashMap<Type, ObjectDeserializer> getDeserializers() {
        return this.deserializers;
    }

    public ObjectDeserializer getDeserializer(Type type) {
        ObjectDeserializer derializer = this.deserializers.get(type);
        if (derializer != null) {
            return derializer;
        }
        if (type instanceof Class) {
            return getDeserializer((Class) type, type);
        }
        if (!(type instanceof ParameterizedType)) {
            return JavaObjectDeserializer.instance;
        }
        Type rawType = ((ParameterizedType) type).getRawType();
        if (rawType instanceof Class) {
            return getDeserializer((Class) rawType, type);
        }
        return getDeserializer(rawType);
    }

    public ObjectDeserializer getDeserializer(Class<?> clazz, Type type) {
        ObjectDeserializer derializer;
        Class<?> mappingTo;
        ObjectDeserializer derializer2 = this.deserializers.get(type);
        if (derializer2 != null) {
            return derializer2;
        }
        if (type == null) {
            type = clazz;
        }
        ObjectDeserializer derializer3 = this.deserializers.get(type);
        if (derializer3 != null) {
            return derializer3;
        }
        JSONType annotation = (JSONType) clazz.getAnnotation(JSONType.class);
        if (annotation != null && (mappingTo = annotation.mappingTo()) != Void.class) {
            return getDeserializer(mappingTo, mappingTo);
        }
        if ((type instanceof WildcardType) || (type instanceof TypeVariable) || (type instanceof ParameterizedType)) {
            derializer3 = this.deserializers.get(clazz);
        }
        if (derializer3 != null) {
            return derializer3;
        }
        String className = clazz.getName().replace('$', '.');
        if (className.startsWith("java.awt.") && AwtCodec.support(clazz) && !awtError) {
            try {
                this.deserializers.put(Class.forName("java.awt.Point"), AwtCodec.instance);
                this.deserializers.put(Class.forName("java.awt.Font"), AwtCodec.instance);
                this.deserializers.put(Class.forName("java.awt.Rectangle"), AwtCodec.instance);
                this.deserializers.put(Class.forName("java.awt.Color"), AwtCodec.instance);
            } catch (Throwable th) {
                awtError = true;
            }
            derializer3 = AwtCodec.instance;
        }
        if (!jdk8Error) {
            try {
                if (className.startsWith("java.time.")) {
                    this.deserializers.put(Class.forName("java.time.LocalDateTime"), Jdk8DateCodec.instance);
                    this.deserializers.put(Class.forName("java.time.LocalDate"), Jdk8DateCodec.instance);
                    this.deserializers.put(Class.forName("java.time.LocalTime"), Jdk8DateCodec.instance);
                    this.deserializers.put(Class.forName("java.time.ZonedDateTime"), Jdk8DateCodec.instance);
                    this.deserializers.put(Class.forName("java.time.OffsetDateTime"), Jdk8DateCodec.instance);
                    this.deserializers.put(Class.forName("java.time.OffsetTime"), Jdk8DateCodec.instance);
                    this.deserializers.put(Class.forName("java.time.ZoneOffset"), Jdk8DateCodec.instance);
                    this.deserializers.put(Class.forName("java.time.ZoneRegion"), Jdk8DateCodec.instance);
                    this.deserializers.put(Class.forName("java.time.ZoneId"), Jdk8DateCodec.instance);
                    this.deserializers.put(Class.forName("java.time.Period"), Jdk8DateCodec.instance);
                    this.deserializers.put(Class.forName("java.time.Duration"), Jdk8DateCodec.instance);
                    this.deserializers.put(Class.forName("java.time.Instant"), Jdk8DateCodec.instance);
                    derializer3 = this.deserializers.get(clazz);
                } else if (className.startsWith("java.util.Optional")) {
                    this.deserializers.put(Class.forName("java.util.Optional"), OptionalCodec.instance);
                    this.deserializers.put(Class.forName("java.util.OptionalDouble"), OptionalCodec.instance);
                    this.deserializers.put(Class.forName("java.util.OptionalInt"), OptionalCodec.instance);
                    this.deserializers.put(Class.forName("java.util.OptionalLong"), OptionalCodec.instance);
                    derializer3 = this.deserializers.get(clazz);
                }
            } catch (Throwable th2) {
                jdk8Error = true;
            }
        }
        if (className.equals("java.nio.file.Path")) {
            this.deserializers.put(clazz, MiscCodec.instance);
        }
        if (clazz == Map.Entry.class) {
            this.deserializers.put(clazz, MiscCodec.instance);
        }
        try {
            for (AutowiredObjectDeserializer autowired : ServiceLoader.load(AutowiredObjectDeserializer.class, Thread.currentThread().getContextClassLoader())) {
                for (Type forType : autowired.getAutowiredFor()) {
                    this.deserializers.put(forType, autowired);
                }
            }
        } catch (Exception e) {
        }
        if (derializer3 == null) {
            derializer3 = this.deserializers.get(type);
        }
        if (derializer3 != null) {
            return derializer3;
        }
        if (clazz.isEnum()) {
            derializer = new EnumDeserializer(clazz);
        } else if (clazz.isArray()) {
            derializer = ObjectArrayCodec.instance;
        } else if (clazz == Set.class || clazz == HashSet.class || clazz == Collection.class || clazz == List.class || clazz == ArrayList.class) {
            derializer = CollectionCodec.instance;
        } else if (Collection.class.isAssignableFrom(clazz)) {
            derializer = CollectionCodec.instance;
        } else if (Map.class.isAssignableFrom(clazz)) {
            derializer = MapDeserializer.instance;
        } else if (Throwable.class.isAssignableFrom(clazz)) {
            derializer = new ThrowableDeserializer(this, clazz);
        } else {
            derializer = createJavaBeanDeserializer(clazz, type);
        }
        putDeserializer(type, derializer);
        return derializer;
    }

    public void initJavaBeanDeserializers(Class<?>... classes) {
        if (classes != null) {
            for (Class<?> type : classes) {
                if (type != null) {
                    putDeserializer(type, createJavaBeanDeserializer(type, type));
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:83:0x015e, code lost:
        r4 = false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.alibaba.fastjson.parser.deserializer.ObjectDeserializer createJavaBeanDeserializer(java.lang.Class<?> r23, java.lang.reflect.Type r24) {
        /*
            r22 = this;
            r0 = r22
            boolean r4 = r0.asmEnable
            if (r4 == 0) goto L_0x0045
            java.lang.Class<com.alibaba.fastjson.annotation.JSONType> r17 = com.alibaba.fastjson.annotation.JSONType.class
            r0 = r23
            r1 = r17
            java.lang.annotation.Annotation r15 = r0.getAnnotation(r1)
            com.alibaba.fastjson.annotation.JSONType r15 = (com.alibaba.fastjson.annotation.JSONType) r15
            if (r15 == 0) goto L_0x0030
            java.lang.Class r9 = r15.deserializer()
            java.lang.Class<java.lang.Void> r17 = java.lang.Void.class
            r0 = r17
            if (r9 == r0) goto L_0x002c
            java.lang.Object r8 = r9.newInstance()     // Catch:{ Throwable -> 0x002b }
            boolean r0 = r8 instanceof com.alibaba.fastjson.parser.deserializer.ObjectDeserializer     // Catch:{ Throwable -> 0x002b }
            r17 = r0
            if (r17 == 0) goto L_0x002c
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r8 = (com.alibaba.fastjson.parser.deserializer.ObjectDeserializer) r8     // Catch:{ Throwable -> 0x002b }
        L_0x002a:
            return r8
        L_0x002b:
            r17 = move-exception
        L_0x002c:
            boolean r4 = r15.asm()
        L_0x0030:
            if (r4 == 0) goto L_0x0045
            java.lang.Class r16 = com.alibaba.fastjson.util.JavaBeanInfo.getBuilderClass(r15)
            if (r16 != 0) goto L_0x003a
            r16 = r23
        L_0x003a:
            int r17 = r16.getModifiers()
            boolean r17 = java.lang.reflect.Modifier.isPublic(r17)
            if (r17 != 0) goto L_0x00f2
            r4 = 0
        L_0x0045:
            java.lang.reflect.TypeVariable[] r17 = r23.getTypeParameters()
            r0 = r17
            int r0 = r0.length
            r17 = r0
            if (r17 == 0) goto L_0x0051
            r4 = 0
        L_0x0051:
            if (r4 == 0) goto L_0x0072
            r0 = r22
            com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory r0 = r0.asmFactory
            r17 = r0
            if (r17 == 0) goto L_0x0072
            r0 = r22
            com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory r0 = r0.asmFactory
            r17 = r0
            r0 = r17
            com.alibaba.fastjson.util.ASMClassLoader r0 = r0.classLoader
            r17 = r0
            r0 = r17
            r1 = r23
            boolean r17 = r0.isExternalClass(r1)
            if (r17 == 0) goto L_0x0072
            r4 = 0
        L_0x0072:
            if (r4 == 0) goto L_0x007c
            java.lang.String r17 = r23.getSimpleName()
            boolean r4 = com.alibaba.fastjson.util.ASMUtils.checkName(r17)
        L_0x007c:
            if (r4 == 0) goto L_0x00d0
            boolean r17 = r23.isInterface()
            if (r17 == 0) goto L_0x0085
            r4 = 0
        L_0x0085:
            r0 = r22
            com.alibaba.fastjson.PropertyNamingStrategy r0 = r0.propertyNamingStrategy
            r17 = r0
            r0 = r23
            r1 = r24
            r2 = r17
            com.alibaba.fastjson.util.JavaBeanInfo r6 = com.alibaba.fastjson.util.JavaBeanInfo.build(r0, r1, r2)
            if (r4 == 0) goto L_0x00a9
            com.alibaba.fastjson.util.FieldInfo[] r0 = r6.fields
            r17 = r0
            r0 = r17
            int r0 = r0.length
            r17 = r0
            r18 = 200(0xc8, float:2.8E-43)
            r0 = r17
            r1 = r18
            if (r0 <= r1) goto L_0x00a9
            r4 = 0
        L_0x00a9:
            java.lang.reflect.Constructor<?> r7 = r6.defaultConstructor
            if (r4 == 0) goto L_0x00b6
            if (r7 != 0) goto L_0x00b6
            boolean r17 = r23.isInterface()
            if (r17 != 0) goto L_0x00b6
            r4 = 0
        L_0x00b6:
            com.alibaba.fastjson.util.FieldInfo[] r0 = r6.fields
            r18 = r0
            r0 = r18
            int r0 = r0.length
            r19 = r0
            r17 = 0
        L_0x00c1:
            r0 = r17
            r1 = r19
            if (r0 >= r1) goto L_0x00d0
            r14 = r18[r17]
            boolean r0 = r14.getOnly
            r20 = r0
            if (r20 == 0) goto L_0x0102
            r4 = 0
        L_0x00d0:
            if (r4 == 0) goto L_0x00e3
            boolean r17 = r23.isMemberClass()
            if (r17 == 0) goto L_0x00e3
            int r17 = r23.getModifiers()
            boolean r17 = java.lang.reflect.Modifier.isStatic(r17)
            if (r17 != 0) goto L_0x00e3
            r4 = 0
        L_0x00e3:
            if (r4 != 0) goto L_0x017a
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r8 = new com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer
            r0 = r22
            r1 = r23
            r2 = r24
            r8.<init>(r0, r1, r2)
            goto L_0x002a
        L_0x00f2:
            java.lang.Class r16 = r16.getSuperclass()
            java.lang.Class<java.lang.Object> r17 = java.lang.Object.class
            r0 = r16
            r1 = r17
            if (r0 == r1) goto L_0x0045
            if (r16 != 0) goto L_0x003a
            goto L_0x0045
        L_0x0102:
            java.lang.Class<?> r12 = r14.fieldClass
            int r20 = r12.getModifiers()
            boolean r20 = java.lang.reflect.Modifier.isPublic(r20)
            if (r20 != 0) goto L_0x0110
            r4 = 0
            goto L_0x00d0
        L_0x0110:
            boolean r20 = r12.isMemberClass()
            if (r20 == 0) goto L_0x0122
            int r20 = r12.getModifiers()
            boolean r20 = java.lang.reflect.Modifier.isStatic(r20)
            if (r20 != 0) goto L_0x0122
            r4 = 0
            goto L_0x00d0
        L_0x0122:
            java.lang.reflect.Member r20 = r14.getMember()
            if (r20 == 0) goto L_0x0138
            java.lang.reflect.Member r20 = r14.getMember()
            java.lang.String r20 = r20.getName()
            boolean r20 = com.alibaba.fastjson.util.ASMUtils.checkName(r20)
            if (r20 != 0) goto L_0x0138
            r4 = 0
            goto L_0x00d0
        L_0x0138:
            com.alibaba.fastjson.annotation.JSONField r3 = r14.getAnnotation()
            if (r3 == 0) goto L_0x0161
            java.lang.String r20 = r3.name()
            boolean r20 = com.alibaba.fastjson.util.ASMUtils.checkName(r20)
            if (r20 == 0) goto L_0x015e
            java.lang.String r20 = r3.format()
            int r20 = r20.length()
            if (r20 != 0) goto L_0x015e
            java.lang.Class r20 = r3.deserializeUsing()
            java.lang.Class<java.lang.Void> r21 = java.lang.Void.class
            r0 = r20
            r1 = r21
            if (r0 == r1) goto L_0x0161
        L_0x015e:
            r4 = 0
            goto L_0x00d0
        L_0x0161:
            boolean r20 = r12.isEnum()
            if (r20 == 0) goto L_0x0176
            r0 = r22
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r13 = r0.getDeserializer((java.lang.reflect.Type) r12)
            boolean r0 = r13 instanceof com.alibaba.fastjson.parser.deserializer.EnumDeserializer
            r20 = r0
            if (r20 != 0) goto L_0x0176
            r4 = 0
            goto L_0x00d0
        L_0x0176:
            int r17 = r17 + 1
            goto L_0x00c1
        L_0x017a:
            r0 = r22
            com.alibaba.fastjson.PropertyNamingStrategy r0 = r0.propertyNamingStrategy
            r17 = r0
            r0 = r23
            r1 = r24
            r2 = r17
            com.alibaba.fastjson.util.JavaBeanInfo r6 = com.alibaba.fastjson.util.JavaBeanInfo.build(r0, r1, r2)
            r0 = r22
            com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory r0 = r0.asmFactory     // Catch:{ NoSuchMethodException -> 0x019a, JSONException -> 0x01a8, Exception -> 0x01b2 }
            r17 = r0
            r0 = r17
            r1 = r22
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r8 = r0.createJavaBeanDeserializer(r1, r6)     // Catch:{ NoSuchMethodException -> 0x019a, JSONException -> 0x01a8, Exception -> 0x01b2 }
            goto L_0x002a
        L_0x019a:
            r11 = move-exception
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r8 = new com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer
            r0 = r22
            r1 = r23
            r2 = r24
            r8.<init>(r0, r1, r2)
            goto L_0x002a
        L_0x01a8:
            r5 = move-exception
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r8 = new com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer
            r0 = r22
            r8.<init>((com.alibaba.fastjson.parser.ParserConfig) r0, (com.alibaba.fastjson.util.JavaBeanInfo) r6)
            goto L_0x002a
        L_0x01b2:
            r10 = move-exception
            com.alibaba.fastjson.JSONException r17 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r18 = new java.lang.StringBuilder
            r18.<init>()
            java.lang.String r19 = "create asm deserializer error, "
            java.lang.StringBuilder r18 = r18.append(r19)
            java.lang.String r19 = r23.getName()
            java.lang.StringBuilder r18 = r18.append(r19)
            java.lang.String r18 = r18.toString()
            r0 = r17
            r1 = r18
            r0.<init>(r1, r10)
            throw r17
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.ParserConfig.createJavaBeanDeserializer(java.lang.Class, java.lang.reflect.Type):com.alibaba.fastjson.parser.deserializer.ObjectDeserializer");
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, JavaBeanInfo beanInfo, FieldInfo fieldInfo) {
        Class<?> clazz = beanInfo.clazz;
        Class<?> fieldClass = fieldInfo.fieldClass;
        Class<?> deserializeUsing = null;
        JSONField annotation = fieldInfo.getAnnotation();
        if (annotation != null && (deserializeUsing = annotation.deserializeUsing()) == Void.class) {
            deserializeUsing = null;
        }
        if (deserializeUsing == null && (fieldClass == List.class || fieldClass == ArrayList.class)) {
            return new ArrayListTypeFieldDeserializer(mapping, clazz, fieldInfo);
        }
        return new DefaultFieldDeserializer(mapping, clazz, fieldInfo);
    }

    public void putDeserializer(Type type, ObjectDeserializer deserializer) {
        this.deserializers.put(type, deserializer);
    }

    public ObjectDeserializer getDeserializer(FieldInfo fieldInfo) {
        return getDeserializer(fieldInfo.fieldClass, fieldInfo.fieldType);
    }

    public boolean isPrimitive(Class<?> clazz) {
        return isPrimitive2(clazz);
    }

    public static boolean isPrimitive2(Class<?> clazz) {
        if (clazz.isPrimitive() || clazz == Boolean.class || clazz == Character.class || clazz == Byte.class || clazz == Short.class || clazz == Integer.class || clazz == Long.class || clazz == Float.class || clazz == Double.class || clazz == BigInteger.class || clazz == BigDecimal.class || clazz == String.class || clazz == java.util.Date.class || clazz == Date.class || clazz == Time.class || clazz == Timestamp.class || clazz.isEnum()) {
            return true;
        }
        return false;
    }

    public static void parserAllFieldToCache(Class<?> clazz, Map<String, Field> fieldCacheMap) {
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();
            if (!fieldCacheMap.containsKey(fieldName)) {
                fieldCacheMap.put(fieldName, field);
            }
        }
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            parserAllFieldToCache(clazz.getSuperclass(), fieldCacheMap);
        }
    }

    public static Field getFieldFromCache(String fieldName, Map<String, Field> fieldCacheMap) {
        Field field = fieldCacheMap.get(fieldName);
        if (field == null) {
            field = fieldCacheMap.get("_" + fieldName);
        }
        if (field == null) {
            return fieldCacheMap.get("m_" + fieldName);
        }
        return field;
    }

    public ClassLoader getDefaultClassLoader() {
        return this.defaultClassLoader;
    }

    public void setDefaultClassLoader(ClassLoader defaultClassLoader2) {
        this.defaultClassLoader = defaultClassLoader2;
    }

    public void addDeny(String name) {
        if (name != null && name.length() != 0) {
            String[] strArr = this.denyList;
            int length = strArr.length;
            int i = 0;
            while (i < length) {
                if (!name.equals(strArr[i])) {
                    i++;
                } else {
                    return;
                }
            }
            String[] denyList2 = new String[(this.denyList.length + 1)];
            System.arraycopy(this.denyList, 0, denyList2, 0, this.denyList.length);
            denyList2[denyList2.length - 1] = name;
            this.denyList = denyList2;
        }
    }

    public void addAccept(String name) {
        if (name != null && name.length() != 0) {
            String[] strArr = this.acceptList;
            int length = strArr.length;
            int i = 0;
            while (i < length) {
                if (!name.equals(strArr[i])) {
                    i++;
                } else {
                    return;
                }
            }
            String[] acceptList2 = new String[(this.acceptList.length + 1)];
            System.arraycopy(this.acceptList, 0, acceptList2, 0, this.acceptList.length);
            acceptList2[acceptList2.length - 1] = name;
            this.acceptList = acceptList2;
        }
    }

    public Class<?> checkAutoType(String typeName, Class<?> expectClass) {
        if (typeName == null) {
            return null;
        }
        String className = typeName.replace('$', '.');
        if (this.autoTypeSupport || expectClass != null) {
            for (String accept : this.acceptList) {
                if (className.startsWith(accept)) {
                    return TypeUtils.loadClass(typeName, this.defaultClassLoader);
                }
            }
            for (String deny : this.denyList) {
                if (className.startsWith(deny)) {
                    throw new JSONException("autoType is not support. " + typeName);
                }
            }
        }
        Class<?> clazz = TypeUtils.getClassFromMapping(typeName);
        if (clazz == null) {
            clazz = this.deserializers.findClass(typeName);
        }
        if (clazz == null) {
            if (!this.autoTypeSupport) {
                for (String deny2 : this.denyList) {
                    if (className.startsWith(deny2)) {
                        throw new JSONException("autoType is not support. " + typeName);
                    }
                }
                for (String accept2 : this.acceptList) {
                    if (className.startsWith(accept2)) {
                        Class<?> clazz2 = TypeUtils.loadClass(typeName, this.defaultClassLoader);
                        if (expectClass == null || !expectClass.isAssignableFrom(clazz2)) {
                            return clazz2;
                        }
                        throw new JSONException("type not match. " + typeName + " -> " + expectClass.getName());
                    }
                }
            }
            if (this.autoTypeSupport || expectClass != null) {
                clazz = TypeUtils.loadClass(typeName, this.defaultClassLoader);
            }
            if (clazz != null) {
                if (ClassLoader.class.isAssignableFrom(clazz) || DataSource.class.isAssignableFrom(clazz)) {
                    throw new JSONException("autoType is not support. " + typeName);
                } else if (expectClass != null) {
                    if (expectClass.isAssignableFrom(clazz)) {
                        return clazz;
                    }
                    throw new JSONException("type not match. " + typeName + " -> " + expectClass.getName());
                }
            }
            if (this.autoTypeSupport) {
                return clazz;
            }
            throw new JSONException("autoType is not support. " + typeName);
        } else if (expectClass == null || expectClass.isAssignableFrom(clazz)) {
            return clazz;
        } else {
            throw new JSONException("type not match. " + typeName + " -> " + expectClass.getName());
        }
    }
}
