package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONStreamAware;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec;
import com.alibaba.fastjson.parser.deserializer.OptionalCodec;
import com.alibaba.fastjson.support.springfox.SwaggerJsonSerializer;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.IdentityHashMap;
import com.alibaba.fastjson.util.ServiceLoader;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.File;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import javax.xml.datatype.XMLGregorianCalendar;

public class SerializeConfig {
    private static boolean awtError = false;
    public static final SerializeConfig globalInstance = new SerializeConfig();
    private static boolean guavaError = false;
    private static boolean jdk8Error = false;
    private static boolean oracleJdbcError = false;
    private static boolean springfoxError = false;
    private boolean asm;
    private ASMSerializerFactory asmFactory;
    public PropertyNamingStrategy propertyNamingStrategy;
    private final IdentityHashMap<Type, ObjectSerializer> serializers;
    protected String typeKey;

    public String getTypeKey() {
        return this.typeKey;
    }

    public void setTypeKey(String typeKey2) {
        this.typeKey = typeKey2;
    }

    private final JavaBeanSerializer createASMSerializer(SerializeBeanInfo beanInfo) throws Exception {
        JavaBeanSerializer serializer = this.asmFactory.createJavaBeanSerializer(beanInfo);
        for (FieldSerializer fieldDeser : serializer.sortedGetters) {
            Class<?> fieldClass = fieldDeser.fieldInfo.fieldClass;
            if (fieldClass.isEnum() && !(getObjectWriter(fieldClass) instanceof EnumSerializer)) {
                serializer.writeDirect = false;
            }
        }
        return serializer;
    }

    private final ObjectSerializer createJavaBeanSerializer(Class<?> clazz) {
        SerializeBeanInfo beanInfo = TypeUtils.buildBeanInfo(clazz, (Map<String, String>) null, this.propertyNamingStrategy);
        if (beanInfo.fields.length != 0 || !Iterable.class.isAssignableFrom(clazz)) {
            return createJavaBeanSerializer(beanInfo);
        }
        return MiscCodec.instance;
    }

    public ObjectSerializer createJavaBeanSerializer(SerializeBeanInfo beanInfo) {
        int i = 0;
        JSONType jsonType = beanInfo.jsonType;
        if (jsonType != null) {
            Class<?> serializerClass = jsonType.serializer();
            if (serializerClass != Void.class) {
                try {
                    Object seralizer = serializerClass.newInstance();
                    if (seralizer instanceof ObjectSerializer) {
                        return (ObjectSerializer) seralizer;
                    }
                } catch (Throwable th) {
                }
            }
            if (!jsonType.asm()) {
                this.asm = false;
            }
        }
        Class<?> clazz = beanInfo.beanType;
        if (!Modifier.isPublic(beanInfo.beanType.getModifiers())) {
            return new JavaBeanSerializer(beanInfo);
        }
        boolean asm2 = this.asm;
        if ((asm2 && this.asmFactory.classLoader.isExternalClass(clazz)) || clazz == Serializable.class || clazz == Object.class) {
            asm2 = false;
        }
        if (asm2 && !ASMUtils.checkName(clazz.getSimpleName())) {
            asm2 = false;
        }
        if (asm2) {
            FieldInfo[] fieldInfoArr = beanInfo.fields;
            int length = fieldInfoArr.length;
            while (true) {
                if (i >= length) {
                    break;
                }
                JSONField annotation = fieldInfoArr[i].getAnnotation();
                if (annotation != null && (!ASMUtils.checkName(annotation.name()) || annotation.format().length() != 0 || annotation.jsonDirect() || annotation.serializeUsing() != Void.class)) {
                    asm2 = false;
                } else {
                    i++;
                }
            }
        }
        if (asm2) {
            try {
                ObjectSerializer asmSerializer = createASMSerializer(beanInfo);
                if (asmSerializer != null) {
                    return asmSerializer;
                }
            } catch (ClassCastException | ClassFormatError e) {
            } catch (Throwable e2) {
                throw new JSONException("create asm serializer error, class " + clazz, e2);
            }
        }
        return new JavaBeanSerializer(beanInfo);
    }

    public boolean isAsmEnable() {
        return this.asm;
    }

    public void setAsmEnable(boolean asmEnable) {
        if (!ASMUtils.IS_ANDROID) {
            this.asm = asmEnable;
        }
    }

    public static SerializeConfig getGlobalInstance() {
        return globalInstance;
    }

    public SerializeConfig() {
        this(1024);
    }

    public SerializeConfig(int tableSize) {
        this.asm = !ASMUtils.IS_ANDROID;
        this.typeKey = JSON.DEFAULT_TYPE_KEY;
        this.serializers = new IdentityHashMap<>(1024);
        try {
            if (this.asm) {
                this.asmFactory = new ASMSerializerFactory();
            }
        } catch (NoClassDefFoundError e) {
            this.asm = false;
        } catch (ExceptionInInitializerError e2) {
            this.asm = false;
        }
        put((Type) Boolean.class, (ObjectSerializer) BooleanCodec.instance);
        put((Type) Character.class, (ObjectSerializer) CharacterCodec.instance);
        put((Type) Byte.class, (ObjectSerializer) IntegerCodec.instance);
        put((Type) Short.class, (ObjectSerializer) IntegerCodec.instance);
        put((Type) Integer.class, (ObjectSerializer) IntegerCodec.instance);
        put((Type) Long.class, (ObjectSerializer) LongCodec.instance);
        put((Type) Float.class, (ObjectSerializer) FloatCodec.instance);
        put((Type) Double.class, (ObjectSerializer) DoubleSerializer.instance);
        put((Type) BigDecimal.class, (ObjectSerializer) BigDecimalCodec.instance);
        put((Type) BigInteger.class, (ObjectSerializer) BigIntegerCodec.instance);
        put((Type) String.class, (ObjectSerializer) StringCodec.instance);
        put((Type) byte[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put((Type) short[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put((Type) int[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put((Type) long[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put((Type) float[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put((Type) double[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put((Type) boolean[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put((Type) char[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put((Type) Object[].class, (ObjectSerializer) ObjectArrayCodec.instance);
        put((Type) Class.class, (ObjectSerializer) MiscCodec.instance);
        put((Type) SimpleDateFormat.class, (ObjectSerializer) MiscCodec.instance);
        put((Type) Currency.class, (ObjectSerializer) new MiscCodec());
        put((Type) TimeZone.class, (ObjectSerializer) MiscCodec.instance);
        put((Type) InetAddress.class, (ObjectSerializer) MiscCodec.instance);
        put((Type) Inet4Address.class, (ObjectSerializer) MiscCodec.instance);
        put((Type) Inet6Address.class, (ObjectSerializer) MiscCodec.instance);
        put((Type) InetSocketAddress.class, (ObjectSerializer) MiscCodec.instance);
        put((Type) File.class, (ObjectSerializer) MiscCodec.instance);
        put((Type) Appendable.class, (ObjectSerializer) AppendableSerializer.instance);
        put((Type) StringBuffer.class, (ObjectSerializer) AppendableSerializer.instance);
        put((Type) StringBuilder.class, (ObjectSerializer) AppendableSerializer.instance);
        put((Type) Charset.class, (ObjectSerializer) ToStringSerializer.instance);
        put((Type) Pattern.class, (ObjectSerializer) ToStringSerializer.instance);
        put((Type) Locale.class, (ObjectSerializer) ToStringSerializer.instance);
        put((Type) URI.class, (ObjectSerializer) ToStringSerializer.instance);
        put((Type) URL.class, (ObjectSerializer) ToStringSerializer.instance);
        put((Type) UUID.class, (ObjectSerializer) ToStringSerializer.instance);
        put((Type) AtomicBoolean.class, (ObjectSerializer) AtomicCodec.instance);
        put((Type) AtomicInteger.class, (ObjectSerializer) AtomicCodec.instance);
        put((Type) AtomicLong.class, (ObjectSerializer) AtomicCodec.instance);
        put((Type) AtomicReference.class, (ObjectSerializer) ReferenceCodec.instance);
        put((Type) AtomicIntegerArray.class, (ObjectSerializer) AtomicCodec.instance);
        put((Type) AtomicLongArray.class, (ObjectSerializer) AtomicCodec.instance);
        put((Type) WeakReference.class, (ObjectSerializer) ReferenceCodec.instance);
        put((Type) SoftReference.class, (ObjectSerializer) ReferenceCodec.instance);
    }

    public void addFilter(Class<?> clazz, SerializeFilter filter) {
        ObjectSerializer serializer = getObjectWriter(clazz);
        if (serializer instanceof SerializeFilterable) {
            SerializeFilterable filterable = (SerializeFilterable) serializer;
            if (this == globalInstance || filterable != MapSerializer.instance) {
                filterable.addFilter(filter);
                return;
            }
            MapSerializer newMapSer = new MapSerializer();
            put((Type) clazz, (ObjectSerializer) newMapSer);
            newMapSer.addFilter(filter);
        }
    }

    public void config(Class<?> clazz, SerializerFeature feature, boolean value) {
        ObjectSerializer serializer = getObjectWriter(clazz, false);
        if (serializer == null) {
            SerializeBeanInfo beanInfo = TypeUtils.buildBeanInfo(clazz, (Map<String, String>) null, this.propertyNamingStrategy);
            if (value) {
                beanInfo.features |= feature.mask;
            } else {
                beanInfo.features &= feature.mask ^ -1;
            }
            put((Type) clazz, createJavaBeanSerializer(beanInfo));
        } else if (serializer instanceof JavaBeanSerializer) {
            SerializeBeanInfo beanInfo2 = ((JavaBeanSerializer) serializer).beanInfo;
            int originalFeaturs = beanInfo2.features;
            if (value) {
                beanInfo2.features |= feature.mask;
            } else {
                beanInfo2.features &= feature.mask ^ -1;
            }
            if (originalFeaturs != beanInfo2.features && serializer.getClass() != JavaBeanSerializer.class) {
                put((Type) clazz, createJavaBeanSerializer(beanInfo2));
            }
        }
    }

    public ObjectSerializer getObjectWriter(Class<?> clazz) {
        return getObjectWriter(clazz, true);
    }

    private ObjectSerializer getObjectWriter(Class<?> clazz, boolean create) {
        ClassLoader classLoader;
        ObjectSerializer writer = this.serializers.get(clazz);
        if (writer == null) {
            try {
                for (AutowiredObjectSerializer next : ServiceLoader.load(AutowiredObjectSerializer.class, Thread.currentThread().getContextClassLoader())) {
                    if (next instanceof AutowiredObjectSerializer) {
                        AutowiredObjectSerializer autowired = next;
                        for (Type forType : autowired.getAutowiredFor()) {
                            put(forType, (ObjectSerializer) autowired);
                        }
                    }
                }
            } catch (ClassCastException e) {
            }
            writer = this.serializers.get(clazz);
        }
        if (writer == null && (classLoader = JSON.class.getClassLoader()) != Thread.currentThread().getContextClassLoader()) {
            try {
                for (AutowiredObjectSerializer next2 : ServiceLoader.load(AutowiredObjectSerializer.class, classLoader)) {
                    if (next2 instanceof AutowiredObjectSerializer) {
                        AutowiredObjectSerializer autowired2 = next2;
                        for (Type forType2 : autowired2.getAutowiredFor()) {
                            put(forType2, (ObjectSerializer) autowired2);
                        }
                    }
                }
            } catch (ClassCastException e2) {
            }
            writer = this.serializers.get(clazz);
        }
        if (writer == null) {
            if (Map.class.isAssignableFrom(clazz)) {
                put((Type) clazz, (ObjectSerializer) MapSerializer.instance);
            } else if (List.class.isAssignableFrom(clazz)) {
                put((Type) clazz, (ObjectSerializer) ListSerializer.instance);
            } else if (Collection.class.isAssignableFrom(clazz)) {
                put((Type) clazz, (ObjectSerializer) CollectionCodec.instance);
            } else if (Date.class.isAssignableFrom(clazz)) {
                put((Type) clazz, (ObjectSerializer) DateCodec.instance);
            } else if (JSONAware.class.isAssignableFrom(clazz)) {
                put((Type) clazz, (ObjectSerializer) JSONAwareSerializer.instance);
            } else if (JSONSerializable.class.isAssignableFrom(clazz)) {
                put((Type) clazz, (ObjectSerializer) JSONSerializableSerializer.instance);
            } else if (JSONStreamAware.class.isAssignableFrom(clazz)) {
                put((Type) clazz, (ObjectSerializer) MiscCodec.instance);
            } else if (clazz.isEnum() || (clazz.getSuperclass() != null && clazz.getSuperclass().isEnum())) {
                JSONType jsonType = (JSONType) clazz.getAnnotation(JSONType.class);
                if (jsonType == null || !jsonType.serializeEnumAsJavaBean()) {
                    put((Type) clazz, (ObjectSerializer) EnumSerializer.instance);
                } else {
                    put((Type) clazz, createJavaBeanSerializer(clazz));
                }
            } else if (clazz.isArray()) {
                Class<?> componentType = clazz.getComponentType();
                put((Type) clazz, (ObjectSerializer) new ArraySerializer(componentType, getObjectWriter(componentType)));
            } else if (Throwable.class.isAssignableFrom(clazz)) {
                SerializeBeanInfo beanInfo = TypeUtils.buildBeanInfo(clazz, (Map<String, String>) null, this.propertyNamingStrategy);
                beanInfo.features |= SerializerFeature.WriteClassName.mask;
                put((Type) clazz, (ObjectSerializer) new JavaBeanSerializer(beanInfo));
            } else if (TimeZone.class.isAssignableFrom(clazz) || Map.Entry.class.isAssignableFrom(clazz)) {
                put((Type) clazz, (ObjectSerializer) MiscCodec.instance);
            } else if (Appendable.class.isAssignableFrom(clazz)) {
                put((Type) clazz, (ObjectSerializer) AppendableSerializer.instance);
            } else if (Charset.class.isAssignableFrom(clazz)) {
                put((Type) clazz, (ObjectSerializer) ToStringSerializer.instance);
            } else if (Enumeration.class.isAssignableFrom(clazz)) {
                put((Type) clazz, (ObjectSerializer) EnumerationSerializer.instance);
            } else if (Calendar.class.isAssignableFrom(clazz) || XMLGregorianCalendar.class.isAssignableFrom(clazz)) {
                put((Type) clazz, (ObjectSerializer) CalendarCodec.instance);
            } else if (Clob.class.isAssignableFrom(clazz)) {
                put((Type) clazz, (ObjectSerializer) ClobSeriliazer.instance);
            } else if (TypeUtils.isPath(clazz)) {
                put((Type) clazz, (ObjectSerializer) ToStringSerializer.instance);
            } else if (Iterator.class.isAssignableFrom(clazz)) {
                put((Type) clazz, (ObjectSerializer) MiscCodec.instance);
            } else {
                String className = clazz.getName();
                if (!className.startsWith("java.awt.") || !AwtCodec.support(clazz)) {
                    if (!jdk8Error && (className.startsWith("java.time.") || className.startsWith("java.util.Optional"))) {
                        try {
                            put((Type) Class.forName("java.time.LocalDateTime"), (ObjectSerializer) Jdk8DateCodec.instance);
                            put((Type) Class.forName("java.time.LocalDate"), (ObjectSerializer) Jdk8DateCodec.instance);
                            put((Type) Class.forName("java.time.LocalTime"), (ObjectSerializer) Jdk8DateCodec.instance);
                            put((Type) Class.forName("java.time.ZonedDateTime"), (ObjectSerializer) Jdk8DateCodec.instance);
                            put((Type) Class.forName("java.time.OffsetDateTime"), (ObjectSerializer) Jdk8DateCodec.instance);
                            put((Type) Class.forName("java.time.OffsetTime"), (ObjectSerializer) Jdk8DateCodec.instance);
                            put((Type) Class.forName("java.time.ZoneOffset"), (ObjectSerializer) Jdk8DateCodec.instance);
                            put((Type) Class.forName("java.time.ZoneRegion"), (ObjectSerializer) Jdk8DateCodec.instance);
                            put((Type) Class.forName("java.time.Period"), (ObjectSerializer) Jdk8DateCodec.instance);
                            put((Type) Class.forName("java.time.Duration"), (ObjectSerializer) Jdk8DateCodec.instance);
                            put((Type) Class.forName("java.time.Instant"), (ObjectSerializer) Jdk8DateCodec.instance);
                            put((Type) Class.forName("java.util.Optional"), (ObjectSerializer) OptionalCodec.instance);
                            put((Type) Class.forName("java.util.OptionalDouble"), (ObjectSerializer) OptionalCodec.instance);
                            put((Type) Class.forName("java.util.OptionalInt"), (ObjectSerializer) OptionalCodec.instance);
                            put((Type) Class.forName("java.util.OptionalLong"), (ObjectSerializer) OptionalCodec.instance);
                            ObjectSerializer writer2 = this.serializers.get(clazz);
                            if (writer2 != null) {
                                return writer2;
                            }
                        } catch (Throwable th) {
                            jdk8Error = true;
                        }
                    }
                    if (!oracleJdbcError && className.startsWith("oracle.sql.")) {
                        try {
                            put((Type) Class.forName("oracle.sql.DATE"), (ObjectSerializer) DateCodec.instance);
                            put((Type) Class.forName("oracle.sql.TIMESTAMP"), (ObjectSerializer) DateCodec.instance);
                            ObjectSerializer writer3 = this.serializers.get(clazz);
                            if (writer3 != null) {
                                return writer3;
                            }
                        } catch (Throwable th2) {
                            oracleJdbcError = true;
                        }
                    }
                    if (!springfoxError && className.equals("springfox.documentation.spring.web.json.Json")) {
                        try {
                            put((Type) Class.forName("springfox.documentation.spring.web.json.Json"), (ObjectSerializer) SwaggerJsonSerializer.instance);
                            ObjectSerializer writer4 = this.serializers.get(clazz);
                            if (writer4 != null) {
                                return writer4;
                            }
                        } catch (ClassNotFoundException e3) {
                            springfoxError = true;
                        }
                    }
                    if (!guavaError && className.startsWith("com.google.common.collect.")) {
                        try {
                            put((Type) Class.forName("com.google.common.collect.HashMultimap"), (ObjectSerializer) GuavaCodec.instance);
                            put((Type) Class.forName("com.google.common.collect.LinkedListMultimap"), (ObjectSerializer) GuavaCodec.instance);
                            put((Type) Class.forName("com.google.common.collect.ArrayListMultimap"), (ObjectSerializer) GuavaCodec.instance);
                            put((Type) Class.forName("com.google.common.collect.TreeMultimap"), (ObjectSerializer) GuavaCodec.instance);
                            ObjectSerializer writer5 = this.serializers.get(clazz);
                            if (writer5 != null) {
                                return writer5;
                            }
                        } catch (ClassNotFoundException e4) {
                            guavaError = true;
                        }
                    }
                    if (className.equals("net.sf.json.JSONNull")) {
                        try {
                            put((Type) Class.forName("net.sf.json.JSONNull"), (ObjectSerializer) MiscCodec.instance);
                        } catch (ClassNotFoundException e5) {
                        }
                        ObjectSerializer writer6 = this.serializers.get(clazz);
                        if (writer6 != null) {
                            return writer6;
                        }
                    }
                    if (TypeUtils.isProxy(clazz)) {
                        ObjectSerializer superWriter = getObjectWriter(clazz.getSuperclass());
                        put((Type) clazz, superWriter);
                        return superWriter;
                    } else if (create) {
                        put((Type) clazz, createJavaBeanSerializer(clazz));
                    }
                } else {
                    if (!awtError) {
                        try {
                            put((Type) Class.forName("java.awt.Color"), (ObjectSerializer) AwtCodec.instance);
                            put((Type) Class.forName("java.awt.Font"), (ObjectSerializer) AwtCodec.instance);
                            put((Type) Class.forName("java.awt.Point"), (ObjectSerializer) AwtCodec.instance);
                            put((Type) Class.forName("java.awt.Rectangle"), (ObjectSerializer) AwtCodec.instance);
                        } catch (Throwable th3) {
                            awtError = true;
                        }
                    }
                    return AwtCodec.instance;
                }
            }
            writer = this.serializers.get(clazz);
        }
        return writer;
    }

    public final ObjectSerializer get(Type key) {
        return this.serializers.get(key);
    }

    public boolean put(Object type, Object value) {
        return put((Type) type, (ObjectSerializer) value);
    }

    public boolean put(Type type, ObjectSerializer value) {
        return this.serializers.put(type, value);
    }

    public void configEnumAsJavaBean(Class<? extends Enum>... enumClasses) {
        for (Class<? extends Enum> enumClass : enumClasses) {
            put((Type) enumClass, createJavaBeanSerializer((Class<?>) enumClass));
        }
    }
}
