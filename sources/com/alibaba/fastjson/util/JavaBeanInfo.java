package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONPOJOBuilder;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class JavaBeanInfo {
    public final Method buildMethod;
    public final Class<?> builderClass;
    public final Class<?> clazz;
    public final Constructor<?> creatorConstructor;
    public final Constructor<?> defaultConstructor;
    public final int defaultConstructorParameterSize;
    public final Method factoryMethod;
    public final FieldInfo[] fields;
    public final JSONType jsonType;
    public final int parserFeatures;
    public final FieldInfo[] sortedFields;
    public final String typeName;

    public JavaBeanInfo(Class<?> clazz2, Class<?> builderClass2, Constructor<?> defaultConstructor2, Constructor<?> creatorConstructor2, Method factoryMethod2, Method buildMethod2, JSONType jsonType2, List<FieldInfo> fieldList) {
        int i = 0;
        this.clazz = clazz2;
        this.builderClass = builderClass2;
        this.defaultConstructor = defaultConstructor2;
        this.creatorConstructor = creatorConstructor2;
        this.factoryMethod = factoryMethod2;
        this.parserFeatures = TypeUtils.getParserFeatures(clazz2);
        this.buildMethod = buildMethod2;
        this.jsonType = jsonType2;
        if (jsonType2 != null) {
            String typeName2 = jsonType2.typeName();
            if (typeName2.length() != 0) {
                this.typeName = typeName2;
            } else {
                this.typeName = clazz2.getName();
            }
        } else {
            this.typeName = clazz2.getName();
        }
        this.fields = new FieldInfo[fieldList.size()];
        fieldList.toArray(this.fields);
        FieldInfo[] sortedFields2 = new FieldInfo[this.fields.length];
        System.arraycopy(this.fields, 0, sortedFields2, 0, this.fields.length);
        Arrays.sort(sortedFields2);
        this.sortedFields = Arrays.equals(this.fields, sortedFields2) ? this.fields : sortedFields2;
        this.defaultConstructorParameterSize = defaultConstructor2 != null ? defaultConstructor2.getParameterTypes().length : i;
    }

    private static FieldInfo getField(List<FieldInfo> fieldList, String propertyName) {
        for (FieldInfo item : fieldList) {
            if (item.name.equals(propertyName)) {
                return item;
            }
            Field field = item.field;
            if (field != null && item.getAnnotation() != null && field.getName().equals(propertyName)) {
                return item;
            }
        }
        return null;
    }

    static boolean add(List<FieldInfo> fieldList, FieldInfo field) {
        FieldInfo item;
        int i = fieldList.size() - 1;
        while (true) {
            if (i < 0) {
                break;
            }
            item = fieldList.get(i);
            if (!item.name.equals(field.name) || (item.getOnly && !field.getOnly)) {
                i--;
            }
        }
        if (item.fieldClass.isAssignableFrom(field.fieldClass)) {
            fieldList.remove(i);
        } else if (item.compareTo(field) >= 0) {
            return false;
        } else {
            fieldList.remove(i);
        }
        fieldList.add(field);
        return true;
    }

    public static JavaBeanInfo build(Class<?> clazz2, Type type, PropertyNamingStrategy propertyNamingStrategy) {
        Class<?> cls;
        String propertyName;
        String propertyName2;
        JSONType jsonType2 = (JSONType) clazz2.getAnnotation(JSONType.class);
        Class<?> builderClass2 = getBuilderClass(jsonType2);
        Field[] declaredFields = clazz2.getDeclaredFields();
        Method[] methods = clazz2.getMethods();
        if (builderClass2 == null) {
            cls = clazz2;
        } else {
            cls = builderClass2;
        }
        Constructor<?> defaultConstructor2 = getDefaultConstructor(cls);
        Method buildMethod2 = null;
        List<FieldInfo> fieldList = new ArrayList<>();
        if (defaultConstructor2 != null || clazz2.isInterface() || Modifier.isAbstract(clazz2.getModifiers())) {
            if (defaultConstructor2 != null) {
                TypeUtils.setAccessible(defaultConstructor2);
            }
            if (builderClass2 != null) {
                String withPrefix = null;
                JSONPOJOBuilder builderAnno = (JSONPOJOBuilder) builderClass2.getAnnotation(JSONPOJOBuilder.class);
                if (builderAnno != null) {
                    withPrefix = builderAnno.withPrefix();
                }
                if (withPrefix == null || withPrefix.length() == 0) {
                    withPrefix = "with";
                }
                for (Method method : builderClass2.getMethods()) {
                    if (!Modifier.isStatic(method.getModifiers()) && method.getReturnType().equals(builderClass2)) {
                        int ordinal = 0;
                        int serialzeFeatures = 0;
                        int parserFeatures2 = 0;
                        JSONField annotation = (JSONField) method.getAnnotation(JSONField.class);
                        if (annotation == null) {
                            annotation = TypeUtils.getSuperMethodAnnotation(clazz2, method);
                        }
                        if (annotation != null) {
                            if (annotation.deserialize()) {
                                ordinal = annotation.ordinal();
                                serialzeFeatures = SerializerFeature.of(annotation.serialzeFeatures());
                                parserFeatures2 = Feature.of(annotation.parseFeatures());
                                if (annotation.name().length() != 0) {
                                    add(fieldList, new FieldInfo(annotation.name(), method, (Field) null, clazz2, type, ordinal, serialzeFeatures, parserFeatures2, annotation, (JSONField) null, (String) null));
                                }
                            }
                        }
                        String methodName = method.getName();
                        if (methodName.startsWith(withPrefix) && methodName.length() > withPrefix.length()) {
                            char c0 = methodName.charAt(withPrefix.length());
                            if (Character.isUpperCase(c0)) {
                                StringBuilder sb = new StringBuilder(methodName.substring(withPrefix.length()));
                                sb.setCharAt(0, Character.toLowerCase(c0));
                                add(fieldList, new FieldInfo(sb.toString(), method, (Field) null, clazz2, type, ordinal, serialzeFeatures, parserFeatures2, annotation, (JSONField) null, (String) null));
                            }
                        }
                    }
                }
                if (builderClass2 != null) {
                    JSONPOJOBuilder builderAnnotation = (JSONPOJOBuilder) builderClass2.getAnnotation(JSONPOJOBuilder.class);
                    String buildMethodName = null;
                    if (builderAnnotation != null) {
                        buildMethodName = builderAnnotation.buildMethod();
                    }
                    if (buildMethodName == null || buildMethodName.length() == 0) {
                        buildMethodName = "build";
                    }
                    try {
                        buildMethod2 = builderClass2.getMethod(buildMethodName, new Class[0]);
                    } catch (NoSuchMethodException | SecurityException e) {
                    }
                    if (buildMethod2 == null) {
                        try {
                            buildMethod2 = builderClass2.getMethod("create", new Class[0]);
                        } catch (NoSuchMethodException | SecurityException e2) {
                        }
                    }
                    if (buildMethod2 == null) {
                        throw new JSONException("buildMethod not found.");
                    }
                    TypeUtils.setAccessible(buildMethod2);
                }
            }
            for (Method method2 : methods) {
                int ordinal2 = 0;
                int serialzeFeatures2 = 0;
                int parserFeatures3 = 0;
                String methodName2 = method2.getName();
                if (methodName2.length() >= 4 && !Modifier.isStatic(method2.getModifiers()) && (method2.getReturnType().equals(Void.TYPE) || method2.getReturnType().equals(method2.getDeclaringClass()))) {
                    Class<?>[] types = method2.getParameterTypes();
                    if (types.length == 1) {
                        JSONField annotation2 = (JSONField) method2.getAnnotation(JSONField.class);
                        if (annotation2 == null) {
                            annotation2 = TypeUtils.getSuperMethodAnnotation(clazz2, method2);
                        }
                        if (annotation2 != null) {
                            if (annotation2.deserialize()) {
                                ordinal2 = annotation2.ordinal();
                                serialzeFeatures2 = SerializerFeature.of(annotation2.serialzeFeatures());
                                parserFeatures3 = Feature.of(annotation2.parseFeatures());
                                if (annotation2.name().length() != 0) {
                                    add(fieldList, new FieldInfo(annotation2.name(), method2, (Field) null, clazz2, type, ordinal2, serialzeFeatures2, parserFeatures3, annotation2, (JSONField) null, (String) null));
                                }
                            }
                        }
                        if (methodName2.startsWith("set")) {
                            char c3 = methodName2.charAt(3);
                            if (Character.isUpperCase(c3) || c3 > 512) {
                                if (TypeUtils.compatibleWithJavaBean) {
                                    propertyName2 = TypeUtils.decapitalize(methodName2.substring(3));
                                } else {
                                    propertyName2 = Character.toLowerCase(methodName2.charAt(3)) + methodName2.substring(4);
                                }
                            } else if (c3 == '_') {
                                propertyName2 = methodName2.substring(4);
                            } else if (c3 == 'f') {
                                propertyName2 = methodName2.substring(3);
                            } else if (methodName2.length() >= 5 && Character.isUpperCase(methodName2.charAt(4))) {
                                propertyName2 = TypeUtils.decapitalize(methodName2.substring(3));
                            }
                            Field field = TypeUtils.getField(clazz2, propertyName2, declaredFields);
                            if (field == null && types[0] == Boolean.TYPE) {
                                field = TypeUtils.getField(clazz2, "is" + Character.toUpperCase(propertyName2.charAt(0)) + propertyName2.substring(1), declaredFields);
                            }
                            JSONField fieldAnnotation = null;
                            if (!(field == null || (fieldAnnotation = (JSONField) field.getAnnotation(JSONField.class)) == null)) {
                                if (fieldAnnotation.deserialize()) {
                                    ordinal2 = fieldAnnotation.ordinal();
                                    serialzeFeatures2 = SerializerFeature.of(fieldAnnotation.serialzeFeatures());
                                    parserFeatures3 = Feature.of(fieldAnnotation.parseFeatures());
                                    if (fieldAnnotation.name().length() != 0) {
                                        add(fieldList, new FieldInfo(fieldAnnotation.name(), method2, field, clazz2, type, ordinal2, serialzeFeatures2, parserFeatures3, annotation2, fieldAnnotation, (String) null));
                                    }
                                }
                            }
                            if (propertyNamingStrategy != null) {
                                propertyName2 = propertyNamingStrategy.translate(propertyName2);
                            }
                            add(fieldList, new FieldInfo(propertyName2, method2, field, clazz2, type, ordinal2, serialzeFeatures2, parserFeatures3, annotation2, fieldAnnotation, (String) null));
                        }
                    }
                }
            }
            for (Field field2 : clazz2.getFields()) {
                int modifiers = field2.getModifiers();
                if ((modifiers & 8) == 0) {
                    if ((modifiers & 16) != 0) {
                        Class<?> fieldType = field2.getType();
                        if (!(Map.class.isAssignableFrom(fieldType) || Collection.class.isAssignableFrom(fieldType) || AtomicLong.class.equals(fieldType) || AtomicInteger.class.equals(fieldType) || AtomicBoolean.class.equals(fieldType))) {
                        }
                    }
                    boolean contains = false;
                    Iterator<FieldInfo> it = fieldList.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (it.next().name.equals(field2.getName())) {
                                contains = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (!contains) {
                        int ordinal3 = 0;
                        int serialzeFeatures3 = 0;
                        int parserFeatures4 = 0;
                        String propertyName3 = field2.getName();
                        JSONField fieldAnnotation2 = (JSONField) field2.getAnnotation(JSONField.class);
                        if (fieldAnnotation2 != null) {
                            if (fieldAnnotation2.deserialize()) {
                                ordinal3 = fieldAnnotation2.ordinal();
                                serialzeFeatures3 = SerializerFeature.of(fieldAnnotation2.serialzeFeatures());
                                parserFeatures4 = Feature.of(fieldAnnotation2.parseFeatures());
                                if (fieldAnnotation2.name().length() != 0) {
                                    propertyName3 = fieldAnnotation2.name();
                                }
                            }
                        }
                        if (propertyNamingStrategy != null) {
                            propertyName3 = propertyNamingStrategy.translate(propertyName3);
                        }
                        add(fieldList, new FieldInfo(propertyName3, (Method) null, field2, clazz2, type, ordinal3, serialzeFeatures3, parserFeatures4, (JSONField) null, fieldAnnotation2, (String) null));
                    }
                }
            }
            for (Method method3 : clazz2.getMethods()) {
                String methodName3 = method3.getName();
                if (methodName3.length() >= 4 && !Modifier.isStatic(method3.getModifiers()) && methodName3.startsWith("get") && Character.isUpperCase(methodName3.charAt(3)) && method3.getParameterTypes().length == 0 && (Collection.class.isAssignableFrom(method3.getReturnType()) || Map.class.isAssignableFrom(method3.getReturnType()) || AtomicBoolean.class == method3.getReturnType() || AtomicInteger.class == method3.getReturnType() || AtomicLong.class == method3.getReturnType())) {
                    JSONField annotation3 = (JSONField) method3.getAnnotation(JSONField.class);
                    if (annotation3 == null || !annotation3.deserialize()) {
                        if (annotation3 == null || annotation3.name().length() <= 0) {
                            propertyName = Character.toLowerCase(methodName3.charAt(3)) + methodName3.substring(4);
                        } else {
                            propertyName = annotation3.name();
                        }
                        if (getField(fieldList, propertyName) == null) {
                            if (propertyNamingStrategy != null) {
                                propertyName = propertyNamingStrategy.translate(propertyName);
                            }
                            add(fieldList, new FieldInfo(propertyName, method3, (Field) null, clazz2, type, 0, 0, 0, annotation3, (JSONField) null, (String) null));
                        }
                    }
                }
            }
            return new JavaBeanInfo(clazz2, builderClass2, defaultConstructor2, (Constructor<?>) null, (Method) null, buildMethod2, jsonType2, fieldList);
        }
        Constructor<?> creatorConstructor2 = getCreatorConstructor(clazz2);
        if (creatorConstructor2 != null) {
            TypeUtils.setAccessible(creatorConstructor2);
            Class<?>[] types2 = creatorConstructor2.getParameterTypes();
            if (types2.length > 0) {
                Annotation[][] paramAnnotationArrays = creatorConstructor2.getParameterAnnotations();
                for (int i = 0; i < types2.length; i++) {
                    Annotation[] paramAnnotations = paramAnnotationArrays[i];
                    JSONField fieldAnnotation3 = null;
                    int length = paramAnnotations.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        }
                        Annotation paramAnnotation = paramAnnotations[i2];
                        if (paramAnnotation instanceof JSONField) {
                            fieldAnnotation3 = (JSONField) paramAnnotation;
                            break;
                        }
                        i2++;
                    }
                    if (fieldAnnotation3 == null) {
                        throw new JSONException("illegal json creator");
                    }
                    add(fieldList, new FieldInfo(fieldAnnotation3.name(), clazz2, types2[i], creatorConstructor2.getGenericParameterTypes()[i], TypeUtils.getField(clazz2, fieldAnnotation3.name(), declaredFields), fieldAnnotation3.ordinal(), SerializerFeature.of(fieldAnnotation3.serialzeFeatures()), Feature.of(fieldAnnotation3.parseFeatures())));
                }
            }
            return new JavaBeanInfo(clazz2, builderClass2, (Constructor<?>) null, creatorConstructor2, (Method) null, (Method) null, jsonType2, fieldList);
        }
        Method factoryMethod2 = getFactoryMethod(clazz2, methods);
        if (factoryMethod2 != null) {
            TypeUtils.setAccessible(factoryMethod2);
            Class<?>[] types3 = factoryMethod2.getParameterTypes();
            if (types3.length > 0) {
                Annotation[][] paramAnnotationArrays2 = factoryMethod2.getParameterAnnotations();
                for (int i3 = 0; i3 < types3.length; i3++) {
                    Annotation[] paramAnnotations2 = paramAnnotationArrays2[i3];
                    JSONField fieldAnnotation4 = null;
                    int length2 = paramAnnotations2.length;
                    int i4 = 0;
                    while (true) {
                        if (i4 >= length2) {
                            break;
                        }
                        Annotation paramAnnotation2 = paramAnnotations2[i4];
                        if (paramAnnotation2 instanceof JSONField) {
                            fieldAnnotation4 = (JSONField) paramAnnotation2;
                            break;
                        }
                        i4++;
                    }
                    if (fieldAnnotation4 == null) {
                        throw new JSONException("illegal json creator");
                    }
                    add(fieldList, new FieldInfo(fieldAnnotation4.name(), clazz2, types3[i3], factoryMethod2.getGenericParameterTypes()[i3], TypeUtils.getField(clazz2, fieldAnnotation4.name(), declaredFields), fieldAnnotation4.ordinal(), SerializerFeature.of(fieldAnnotation4.serialzeFeatures()), Feature.of(fieldAnnotation4.parseFeatures())));
                }
            }
            return new JavaBeanInfo(clazz2, builderClass2, (Constructor<?>) null, (Constructor<?>) null, factoryMethod2, (Method) null, jsonType2, fieldList);
        }
        throw new JSONException("default constructor not found. " + clazz2);
    }

    static Constructor<?> getDefaultConstructor(Class<?> clazz2) {
        if (Modifier.isAbstract(clazz2.getModifiers())) {
            return null;
        }
        Constructor<?> defaultConstructor2 = null;
        Constructor<?>[] constructors = clazz2.getDeclaredConstructors();
        int length = constructors.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            Constructor<?> constructor = constructors[i];
            if (constructor.getParameterTypes().length == 0) {
                defaultConstructor2 = constructor;
                break;
            }
            i++;
        }
        if (defaultConstructor2 != null || !clazz2.isMemberClass() || Modifier.isStatic(clazz2.getModifiers())) {
            return defaultConstructor2;
        }
        for (Constructor<?> constructor2 : constructors) {
            Class<?>[] types = constructor2.getParameterTypes();
            if (types.length == 1 && types[0].equals(clazz2.getDeclaringClass())) {
                return constructor2;
            }
        }
        return defaultConstructor2;
    }

    public static Constructor<?> getCreatorConstructor(Class<?> clazz2) {
        Constructor<?> creatorConstructor2 = null;
        for (Constructor<?> constructor : clazz2.getDeclaredConstructors()) {
            if (((JSONCreator) constructor.getAnnotation(JSONCreator.class)) != null) {
                if (creatorConstructor2 != null) {
                    throw new JSONException("multi-JSONCreator");
                }
                creatorConstructor2 = constructor;
            }
        }
        return creatorConstructor2;
    }

    private static Method getFactoryMethod(Class<?> clazz2, Method[] methods) {
        Method factoryMethod2 = null;
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) && clazz2.isAssignableFrom(method.getReturnType()) && ((JSONCreator) method.getAnnotation(JSONCreator.class)) != null) {
                if (factoryMethod2 != null) {
                    throw new JSONException("multi-JSONCreator");
                }
                factoryMethod2 = method;
            }
        }
        return factoryMethod2;
    }

    public static Class<?> getBuilderClass(JSONType type) {
        if (type == null) {
            return null;
        }
        Class<?> builderClass2 = type.builder();
        if (builderClass2 == Void.class) {
            return null;
        }
        return builderClass2;
    }
}
