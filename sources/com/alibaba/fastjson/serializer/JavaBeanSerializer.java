package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JavaBeanSerializer extends SerializeFilterable implements ObjectSerializer {
    protected SerializeBeanInfo beanInfo;
    protected final FieldSerializer[] getters;
    protected final FieldSerializer[] sortedGetters;

    public JavaBeanSerializer(Class<?> beanType) {
        this(beanType, (Map<String, String>) null);
    }

    public JavaBeanSerializer(Class<?> beanType, String... aliasList) {
        this(beanType, createAliasMap(aliasList));
    }

    static Map<String, String> createAliasMap(String... aliasList) {
        Map<String, String> aliasMap = new HashMap<>();
        for (String alias : aliasList) {
            aliasMap.put(alias, alias);
        }
        return aliasMap;
    }

    public JavaBeanSerializer(Class<?> beanType, Map<String, String> aliasMap) {
        this(TypeUtils.buildBeanInfo(beanType, aliasMap, (PropertyNamingStrategy) null));
    }

    public JavaBeanSerializer(SerializeBeanInfo beanInfo2) {
        this.beanInfo = beanInfo2;
        this.sortedGetters = new FieldSerializer[beanInfo2.sortedFields.length];
        for (int i = 0; i < this.sortedGetters.length; i++) {
            this.sortedGetters[i] = new FieldSerializer(beanInfo2.beanType, beanInfo2.sortedFields[i]);
        }
        if (beanInfo2.fields == beanInfo2.sortedFields) {
            this.getters = this.sortedGetters;
            return;
        }
        this.getters = new FieldSerializer[beanInfo2.fields.length];
        for (int i2 = 0; i2 < this.getters.length; i2++) {
            this.getters[i2] = getFieldSerializer(beanInfo2.fields[i2].name);
        }
    }

    public void writeDirectNonContext(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        write(serializer, object, fieldName, fieldType, features);
    }

    public void writeAsArray(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        write(serializer, object, fieldName, fieldType, features);
    }

    public void writeAsArrayNonContext(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        write(serializer, object, fieldName, fieldType, features);
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        FieldSerializer[] getters2;
        Object propertyValue;
        Class<?> fieldCLass;
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
        } else if (!writeReference(serializer, object, features)) {
            if (out.sortField) {
                getters2 = this.sortedGetters;
            } else {
                getters2 = this.getters;
            }
            SerialContext parent = serializer.context;
            serializer.setContext(parent, object, fieldName, this.beanInfo.features, features);
            boolean writeAsArray = isWriteAsArray(serializer, features);
            char startSeperator = writeAsArray ? '[' : '{';
            char endSeperator = writeAsArray ? ']' : '}';
            try {
                out.append(startSeperator);
                if (getters2.length > 0) {
                    if (out.isEnabled(SerializerFeature.PrettyFormat)) {
                        serializer.incrementIndent();
                        serializer.println();
                    }
                }
                boolean commaFlag = false;
                if (((this.beanInfo.features & SerializerFeature.WriteClassName.mask) != 0 || serializer.isWriteClassName(fieldType, object)) && object.getClass() != fieldType) {
                    writeClassName(serializer, object);
                    commaFlag = true;
                }
                char seperator = commaFlag ? ',' : 0;
                boolean directWritePrefix = out.quoteFieldNames && !out.useSingleQuotes;
                boolean commaFlag2 = writeBefore(serializer, object, seperator) == ',';
                boolean skipTransient = out.isEnabled(SerializerFeature.SkipTransientField);
                boolean ignoreNonFieldGetter = out.isEnabled(SerializerFeature.IgnoreNonFieldGetter);
                for (int i = 0; i < getters2.length; i++) {
                    FieldSerializer fieldSerializer = getters2[i];
                    Field field = fieldSerializer.fieldInfo.field;
                    FieldInfo fieldInfo = fieldSerializer.fieldInfo;
                    String fieldInfoName = fieldInfo.name;
                    Class<?> fieldClass = fieldInfo.fieldClass;
                    if ((!skipTransient || field == null || !fieldInfo.fieldTransient) && (!ignoreNonFieldGetter || field != null)) {
                        if (applyName(serializer, object, fieldInfo.name)) {
                            if (applyLabel(serializer, fieldInfo.label)) {
                                propertyValue = fieldSerializer.getPropertyValueDirect(object);
                                if (apply(serializer, object, fieldInfoName, propertyValue)) {
                                    String key = processKey(serializer, object, fieldInfoName, propertyValue);
                                    Object propertyValue2 = processValue(serializer, fieldSerializer.fieldContext, object, fieldInfoName, propertyValue);
                                    if (propertyValue2 == null && !writeAsArray && !fieldSerializer.writeNull) {
                                        if (!out.isEnabled(SerializerFeature.WRITE_MAP_NULL_FEATURES)) {
                                        }
                                    }
                                    if (propertyValue2 == null || !out.notWriteDefaultValue || (!((fieldCLass = fieldInfo.fieldClass) == Byte.TYPE && (propertyValue2 instanceof Byte) && ((Byte) propertyValue2).byteValue() == 0) && (!(fieldCLass == Short.TYPE && (propertyValue2 instanceof Short) && ((Short) propertyValue2).shortValue() == 0) && (!(fieldCLass == Integer.TYPE && (propertyValue2 instanceof Integer) && ((Integer) propertyValue2).intValue() == 0) && (!(fieldCLass == Long.TYPE && (propertyValue2 instanceof Long) && ((Long) propertyValue2).longValue() == 0) && (!(fieldCLass == Float.TYPE && (propertyValue2 instanceof Float) && ((Float) propertyValue2).floatValue() == 0.0f) && (!(fieldCLass == Double.TYPE && (propertyValue2 instanceof Double) && ((Double) propertyValue2).doubleValue() == ClientTraceData.b.f47a) && (fieldCLass != Boolean.TYPE || !(propertyValue2 instanceof Boolean) || ((Boolean) propertyValue2).booleanValue())))))))) {
                                        if (commaFlag2) {
                                            out.write(44);
                                            if (out.isEnabled(SerializerFeature.PrettyFormat)) {
                                                serializer.println();
                                            }
                                        }
                                        if (key != fieldInfoName) {
                                            if (!writeAsArray) {
                                                out.writeFieldName(key, true);
                                            }
                                            serializer.write(propertyValue2);
                                        } else if (propertyValue != propertyValue2) {
                                            if (!writeAsArray) {
                                                fieldSerializer.writePrefix(serializer);
                                            }
                                            serializer.write(propertyValue2);
                                        } else {
                                            if (!writeAsArray) {
                                                if (directWritePrefix) {
                                                    out.write(fieldInfo.name_chars, 0, fieldInfo.name_chars.length);
                                                } else {
                                                    fieldSerializer.writePrefix(serializer);
                                                }
                                            }
                                            if (!writeAsArray) {
                                                JSONField fieldAnnotation = fieldInfo.getAnnotation();
                                                if (fieldClass != String.class || (fieldAnnotation != null && fieldAnnotation.serializeUsing() != Void.class)) {
                                                    fieldSerializer.writeValue(serializer, propertyValue2);
                                                } else if (propertyValue2 != null) {
                                                    String propertyValueString = (String) propertyValue2;
                                                    if (out.useSingleQuotes) {
                                                        out.writeStringWithSingleQuote(propertyValueString);
                                                    } else {
                                                        out.writeStringWithDoubleQuote(propertyValueString, 0);
                                                    }
                                                } else if ((out.features & SerializerFeature.WriteNullStringAsEmpty.mask) == 0 && (fieldSerializer.features & SerializerFeature.WriteNullStringAsEmpty.mask) == 0) {
                                                    out.writeNull();
                                                } else {
                                                    out.writeString("");
                                                }
                                            } else {
                                                fieldSerializer.writeValue(serializer, propertyValue2);
                                            }
                                        }
                                        commaFlag2 = true;
                                    }
                                }
                            } else {
                                continue;
                            }
                        } else {
                            continue;
                        }
                    }
                }
                writeAfter(serializer, object, commaFlag2 ? ',' : 0);
                if (getters2.length > 0) {
                    if (out.isEnabled(SerializerFeature.PrettyFormat)) {
                        serializer.decrementIdent();
                        serializer.println();
                    }
                }
                out.append(endSeperator);
                serializer.context = parent;
            } catch (InvocationTargetException ex) {
                if (out.isEnabled(SerializerFeature.IgnoreErrorGetter)) {
                    propertyValue = null;
                } else {
                    throw ex;
                }
            } catch (Exception e) {
                String errorMessage = "write javaBean error";
                if (object != null) {
                    try {
                        errorMessage = errorMessage + ", class " + object.getClass().getName();
                    } catch (Throwable th) {
                        serializer.context = parent;
                        throw th;
                    }
                }
                if (fieldName != null) {
                    errorMessage = errorMessage + ", fieldName : " + fieldName;
                }
                if (e.getMessage() != null) {
                    errorMessage = errorMessage + ", " + e.getMessage();
                }
                throw new JSONException(errorMessage, e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void writeClassName(JSONSerializer serializer, Object object) {
        serializer.out.writeFieldName(serializer.config.typeKey, false);
        String typeName = this.beanInfo.typeName;
        if (typeName == null) {
            Class cls = object.getClass();
            if (TypeUtils.isProxy(cls)) {
                cls = cls.getSuperclass();
            }
            typeName = cls.getName();
        }
        serializer.write(typeName);
    }

    public boolean writeReference(JSONSerializer serializer, Object object, int fieldFeatures) {
        SerialContext context = serializer.context;
        int mask = SerializerFeature.DisableCircularReferenceDetect.mask;
        if (context == null || (context.features & mask) != 0 || (fieldFeatures & mask) != 0 || serializer.references == null || !serializer.references.containsKey(object)) {
            return false;
        }
        serializer.writeReference(object);
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isWriteAsArray(JSONSerializer serializer) {
        return isWriteAsArray(serializer, 0);
    }

    /* access modifiers changed from: protected */
    public boolean isWriteAsArray(JSONSerializer serializer, int fieldFeatrues) {
        int mask = SerializerFeature.BeanToArray.mask;
        return ((this.beanInfo.features & mask) == 0 && !serializer.out.beanToArray && (fieldFeatrues & mask) == 0) ? false : true;
    }

    public Object getFieldValue(Object object, String key) {
        FieldSerializer fieldDeser = getFieldSerializer(key);
        if (fieldDeser == null) {
            throw new JSONException("field not found. " + key);
        }
        try {
            return fieldDeser.getPropertyValue(object);
        } catch (InvocationTargetException ex) {
            throw new JSONException("getFieldValue error." + key, ex);
        } catch (IllegalAccessException ex2) {
            throw new JSONException("getFieldValue error." + key, ex2);
        }
    }

    public FieldSerializer getFieldSerializer(String key) {
        if (key == null) {
            return null;
        }
        int low = 0;
        int high = this.sortedGetters.length - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = this.sortedGetters[mid].fieldInfo.name.compareTo(key);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp <= 0) {
                return this.sortedGetters[mid];
            } else {
                high = mid - 1;
            }
        }
        return null;
    }

    public List<Object> getFieldValues(Object object) throws Exception {
        List<Object> fieldValues = new ArrayList<>(this.sortedGetters.length);
        for (FieldSerializer getter : this.sortedGetters) {
            fieldValues.add(getter.getPropertyValue(object));
        }
        return fieldValues;
    }

    public int getSize(Object object) throws Exception {
        int size = 0;
        for (FieldSerializer getter : this.sortedGetters) {
            if (getter.getPropertyValueDirect(object) != null) {
                size++;
            }
        }
        return size;
    }

    public Map<String, Object> getFieldValuesMap(Object object) throws Exception {
        Map<String, Object> map = new LinkedHashMap<>(this.sortedGetters.length);
        for (FieldSerializer getter : this.sortedGetters) {
            map.put(getter.fieldInfo.name, getter.getPropertyValue(object));
        }
        return map;
    }

    /* access modifiers changed from: protected */
    public BeanContext getBeanContext(int orinal) {
        return this.sortedGetters[orinal].fieldContext;
    }

    /* access modifiers changed from: protected */
    public Type getFieldType(int ordinal) {
        return this.sortedGetters[ordinal].fieldInfo.fieldType;
    }

    /* access modifiers changed from: protected */
    public char writeBefore(JSONSerializer jsonBeanDeser, Object object, char seperator) {
        if (jsonBeanDeser.beforeFilters != null) {
            for (BeforeFilter beforeFilter : jsonBeanDeser.beforeFilters) {
                seperator = beforeFilter.writeBefore(jsonBeanDeser, object, seperator);
            }
        }
        if (this.beforeFilters != null) {
            for (BeforeFilter beforeFilter2 : this.beforeFilters) {
                seperator = beforeFilter2.writeBefore(jsonBeanDeser, object, seperator);
            }
        }
        return seperator;
    }

    /* access modifiers changed from: protected */
    public char writeAfter(JSONSerializer jsonBeanDeser, Object object, char seperator) {
        if (jsonBeanDeser.afterFilters != null) {
            for (AfterFilter afterFilter : jsonBeanDeser.afterFilters) {
                seperator = afterFilter.writeAfter(jsonBeanDeser, object, seperator);
            }
        }
        if (this.afterFilters != null) {
            for (AfterFilter afterFilter2 : this.afterFilters) {
                seperator = afterFilter2.writeAfter(jsonBeanDeser, object, seperator);
            }
        }
        return seperator;
    }

    /* access modifiers changed from: protected */
    public boolean applyLabel(JSONSerializer jsonBeanDeser, String label) {
        if (jsonBeanDeser.labelFilters != null) {
            for (LabelFilter propertyFilter : jsonBeanDeser.labelFilters) {
                if (!propertyFilter.apply(label)) {
                    return false;
                }
            }
        }
        if (this.labelFilters != null) {
            for (LabelFilter propertyFilter2 : this.labelFilters) {
                if (!propertyFilter2.apply(label)) {
                    return false;
                }
            }
        }
        return true;
    }
}
