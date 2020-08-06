package com.google.gson.internal.bind;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

public final class ReflectiveTypeAdapterFactory implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;
    private final Excluder excluder;
    private final FieldNamingStrategy fieldNamingPolicy;

    public ReflectiveTypeAdapterFactory(ConstructorConstructor constructorConstructor2, FieldNamingStrategy fieldNamingPolicy2, Excluder excluder2) {
        this.constructorConstructor = constructorConstructor2;
        this.fieldNamingPolicy = fieldNamingPolicy2;
        this.excluder = excluder2;
    }

    public boolean excludeField(Field f, boolean serialize) {
        return !this.excluder.excludeClass(f.getType(), serialize) && !this.excluder.excludeField(f, serialize);
    }

    private String getFieldName(Field f) {
        SerializedName serializedName = (SerializedName) f.getAnnotation(SerializedName.class);
        return serializedName == null ? this.fieldNamingPolicy.translateName(f) : serializedName.value();
    }

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> raw = type.getRawType();
        if (!Object.class.isAssignableFrom(raw)) {
            return null;
        }
        return new Adapter(this.constructorConstructor.get(type), getBoundFields(gson, type, raw));
    }

    private BoundField createBoundField(Gson context, Field field, String name, TypeToken<?> fieldType, boolean serialize, boolean deserialize) {
        final boolean isPrimitive = Primitives.isPrimitive(fieldType.getRawType());
        final Gson gson = context;
        final TypeToken<?> typeToken = fieldType;
        final Field field2 = field;
        return new BoundField(name, serialize, deserialize) {
            final TypeAdapter<?> typeAdapter = gson.getAdapter(typeToken);

            /* access modifiers changed from: package-private */
            public void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
                new TypeAdapterRuntimeTypeWrapper(gson, this.typeAdapter, typeToken.getType()).write(writer, field2.get(value));
            }

            /* access modifiers changed from: package-private */
            public void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
                Object fieldValue = this.typeAdapter.read(reader);
                if (fieldValue != null || !isPrimitive) {
                    field2.set(value, fieldValue);
                }
            }
        };
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.lang.Class<java.lang.Object>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r19v2, resolved type: com.google.gson.reflect.TypeToken<?>} */
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.lang.Class<?>, code=java.lang.Class, for r20v0, types: [java.lang.Class<?>, java.lang.Class] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.Map<java.lang.String, com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField> getBoundFields(com.google.gson.Gson r18, com.google.gson.reflect.TypeToken<?> r19, java.lang.Class r20) {
        /*
            r17 = this;
            java.util.LinkedHashMap r16 = new java.util.LinkedHashMap
            r16.<init>()
            boolean r1 = r20.isInterface()
            if (r1 == 0) goto L_0x000c
        L_0x000b:
            return r16
        L_0x000c:
            java.lang.reflect.Type r10 = r19.getType()
        L_0x0010:
            java.lang.Class<java.lang.Object> r1 = java.lang.Object.class
            r0 = r20
            if (r0 == r1) goto L_0x000b
            java.lang.reflect.Field[] r12 = r20.getDeclaredFields()
            r8 = r12
            int r14 = r8.length
            r13 = 0
        L_0x001d:
            if (r13 >= r14) goto L_0x0086
            r3 = r8[r13]
            r1 = 1
            r0 = r17
            boolean r6 = r0.excludeField(r3, r1)
            r1 = 0
            r0 = r17
            boolean r7 = r0.excludeField(r3, r1)
            if (r6 != 0) goto L_0x0036
            if (r7 != 0) goto L_0x0036
        L_0x0033:
            int r13 = r13 + 1
            goto L_0x001d
        L_0x0036:
            r1 = 1
            r3.setAccessible(r1)
            java.lang.reflect.Type r1 = r19.getType()
            java.lang.reflect.Type r2 = r3.getGenericType()
            r0 = r20
            java.lang.reflect.Type r11 = com.google.gson.internal.C$Gson$Types.resolve(r1, r0, r2)
            r0 = r17
            java.lang.String r4 = r0.getFieldName(r3)
            com.google.gson.reflect.TypeToken r5 = com.google.gson.reflect.TypeToken.get((java.lang.reflect.Type) r11)
            r1 = r17
            r2 = r18
            com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$BoundField r9 = r1.createBoundField(r2, r3, r4, r5, r6, r7)
            java.lang.String r1 = r9.name
            r0 = r16
            java.lang.Object r15 = r0.put(r1, r9)
            com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$BoundField r15 = (com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField) r15
            if (r15 == 0) goto L_0x0033
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.StringBuilder r2 = r2.append(r10)
            java.lang.String r4 = " declares multiple JSON fields named "
            java.lang.StringBuilder r2 = r2.append(r4)
            java.lang.String r4 = r15.name
            java.lang.StringBuilder r2 = r2.append(r4)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        L_0x0086:
            java.lang.reflect.Type r1 = r19.getType()
            java.lang.reflect.Type r2 = r20.getGenericSuperclass()
            r0 = r20
            java.lang.reflect.Type r1 = com.google.gson.internal.C$Gson$Types.resolve(r1, r0, r2)
            com.google.gson.reflect.TypeToken r19 = com.google.gson.reflect.TypeToken.get((java.lang.reflect.Type) r1)
            java.lang.Class r20 = r19.getRawType()
            goto L_0x0010
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getBoundFields(com.google.gson.Gson, com.google.gson.reflect.TypeToken, java.lang.Class):java.util.Map");
    }

    static abstract class BoundField {
        final boolean deserialized;
        final String name;
        final boolean serialized;

        /* access modifiers changed from: package-private */
        public abstract void read(JsonReader jsonReader, Object obj) throws IOException, IllegalAccessException;

        /* access modifiers changed from: package-private */
        public abstract void write(JsonWriter jsonWriter, Object obj) throws IOException, IllegalAccessException;

        protected BoundField(String name2, boolean serialized2, boolean deserialized2) {
            this.name = name2;
            this.serialized = serialized2;
            this.deserialized = deserialized2;
        }
    }

    public static final class Adapter<T> extends TypeAdapter<T> {
        private final Map<String, BoundField> boundFields;
        private final ObjectConstructor<T> constructor;

        private Adapter(ObjectConstructor<T> constructor2, Map<String, BoundField> boundFields2) {
            this.constructor = constructor2;
            this.boundFields = boundFields2;
        }

        public T read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            T instance = this.constructor.construct();
            try {
                in.beginObject();
                while (in.hasNext()) {
                    BoundField field = this.boundFields.get(in.nextName());
                    if (field == null || !field.deserialized) {
                        in.skipValue();
                    } else {
                        field.read(in, instance);
                    }
                }
                in.endObject();
                return instance;
            } catch (IllegalStateException e) {
                throw new JsonSyntaxException((Throwable) e);
            } catch (IllegalAccessException e2) {
                throw new AssertionError(e2);
            }
        }

        public void write(JsonWriter out, T value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            try {
                for (BoundField boundField : this.boundFields.values()) {
                    if (boundField.serialized) {
                        out.name(boundField.name);
                        boundField.write(out, value);
                    }
                }
                out.endObject();
            } catch (IllegalAccessException e) {
                throw new AssertionError();
            }
        }
    }
}
