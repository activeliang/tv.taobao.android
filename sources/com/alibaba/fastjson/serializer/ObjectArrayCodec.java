package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;

public class ObjectArrayCodec implements ObjectSerializer, ObjectDeserializer {
    public static final ObjectArrayCodec instance = new ObjectArrayCodec();

    public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        Object[] array = (Object[]) object;
        if (object == null) {
            out.writeNull(SerializerFeature.WriteNullListAsEmpty);
            return;
        }
        int size = array.length;
        int end = size - 1;
        if (end == -1) {
            out.append((CharSequence) "[]");
            return;
        }
        SerialContext context = serializer.context;
        serializer.setContext(context, object, fieldName, 0);
        Class<?> preClazz = null;
        ObjectSerializer preWriter = null;
        try {
            out.append('[');
            if (out.isEnabled(SerializerFeature.PrettyFormat)) {
                serializer.incrementIndent();
                serializer.println();
                for (int i = 0; i < size; i++) {
                    if (i != 0) {
                        out.write(44);
                        serializer.println();
                    }
                    serializer.write(array[i]);
                }
                serializer.decrementIdent();
                serializer.println();
                out.write(93);
                return;
            }
            for (int i2 = 0; i2 < end; i2++) {
                Object item = array[i2];
                if (item == null) {
                    out.append((CharSequence) "null,");
                } else {
                    if (serializer.containsReference(item)) {
                        serializer.writeReference(item);
                    } else {
                        Class<?> clazz = item.getClass();
                        if (clazz == preClazz) {
                            preWriter.write(serializer, item, (Object) null, (Type) null, 0);
                        } else {
                            preClazz = clazz;
                            preWriter = serializer.getObjectWriter(clazz);
                            preWriter.write(serializer, item, (Object) null, (Type) null, 0);
                        }
                    }
                    out.append(',');
                }
            }
            Object item2 = array[end];
            if (item2 == null) {
                out.append((CharSequence) "null]");
            } else {
                if (serializer.containsReference(item2)) {
                    serializer.writeReference(item2);
                } else {
                    serializer.writeWithFieldName(item2, Integer.valueOf(end));
                }
                out.append(']');
            }
            serializer.context = context;
        } finally {
            serializer.context = context;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v14, resolved type: java.lang.reflect.Type[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T deserialze(com.alibaba.fastjson.parser.DefaultJSONParser r18, java.lang.reflect.Type r19, java.lang.Object r20) {
        /*
            r17 = this;
            r0 = r18
            com.alibaba.fastjson.parser.JSONLexer r9 = r0.lexer
            int r15 = r9.token()
            r16 = 8
            r0 = r16
            if (r15 != r0) goto L_0x0015
            r15 = 16
            r9.nextToken(r15)
            r4 = 0
        L_0x0014:
            return r4
        L_0x0015:
            int r15 = r9.token()
            r16 = 4
            r0 = r16
            if (r15 != r0) goto L_0x0029
            byte[] r4 = r9.bytesValue()
            r15 = 16
            r9.nextToken(r15)
            goto L_0x0014
        L_0x0029:
            r0 = r19
            boolean r15 = r0 instanceof java.lang.reflect.GenericArrayType
            if (r15 == 0) goto L_0x00a8
            r5 = r19
            java.lang.reflect.GenericArrayType r5 = (java.lang.reflect.GenericArrayType) r5
            java.lang.reflect.Type r7 = r5.getGenericComponentType()
            boolean r15 = r7 instanceof java.lang.reflect.TypeVariable
            if (r15 == 0) goto L_0x00a3
            r14 = r7
            java.lang.reflect.TypeVariable r14 = (java.lang.reflect.TypeVariable) r14
            com.alibaba.fastjson.parser.ParseContext r15 = r18.getContext()
            java.lang.reflect.Type r12 = r15.type
            boolean r15 = r12 instanceof java.lang.reflect.ParameterizedType
            if (r15 == 0) goto L_0x0096
            r10 = r12
            java.lang.reflect.ParameterizedType r10 = (java.lang.reflect.ParameterizedType) r10
            java.lang.reflect.Type r11 = r10.getRawType()
            r2 = 0
            boolean r15 = r11 instanceof java.lang.Class
            if (r15 == 0) goto L_0x0077
            java.lang.Class r11 = (java.lang.Class) r11
            java.lang.reflect.TypeVariable[] r13 = r11.getTypeParameters()
            r8 = 0
        L_0x005b:
            int r15 = r13.length
            if (r8 >= r15) goto L_0x0077
            r15 = r13[r8]
            java.lang.String r15 = r15.getName()
            java.lang.String r16 = r14.getName()
            boolean r15 = r15.equals(r16)
            if (r15 == 0) goto L_0x0074
            java.lang.reflect.Type[] r15 = r10.getActualTypeArguments()
            r2 = r15[r8]
        L_0x0074:
            int r8 = r8 + 1
            goto L_0x005b
        L_0x0077:
            boolean r15 = r2 instanceof java.lang.Class
            if (r15 == 0) goto L_0x0093
            r6 = r2
            java.lang.Class r6 = (java.lang.Class) r6
        L_0x007e:
            com.alibaba.fastjson.JSONArray r3 = new com.alibaba.fastjson.JSONArray
            r3.<init>()
            r0 = r18
            r1 = r20
            r0.parseArray(r7, r3, r1)
            r0 = r17
            r1 = r18
            java.lang.Object r4 = r0.toObjectArray(r1, r6, r3)
            goto L_0x0014
        L_0x0093:
            java.lang.Class<java.lang.Object> r6 = java.lang.Object.class
            goto L_0x007e
        L_0x0096:
            java.lang.reflect.Type[] r15 = r14.getBounds()
            r16 = 0
            r15 = r15[r16]
            java.lang.Class r6 = com.alibaba.fastjson.util.TypeUtils.getClass(r15)
            goto L_0x007e
        L_0x00a3:
            java.lang.Class r6 = com.alibaba.fastjson.util.TypeUtils.getClass(r7)
            goto L_0x007e
        L_0x00a8:
            r5 = r19
            java.lang.Class r5 = (java.lang.Class) r5
            java.lang.Class r6 = r5.getComponentType()
            r7 = r6
            goto L_0x007e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.ObjectArrayCodec.deserialze(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object):java.lang.Object");
    }

    /* JADX WARNING: type inference failed for: r13v0, types: [java.lang.reflect.Type, java.lang.Class<?>, java.lang.Class] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private <T> T toObjectArray(com.alibaba.fastjson.parser.DefaultJSONParser r12, java.lang.Class<?> r13, com.alibaba.fastjson.JSONArray r14) {
        /*
            r11 = this;
            if (r14 != 0) goto L_0x0004
            r3 = 0
        L_0x0003:
            return r3
        L_0x0004:
            int r4 = r14.size()
            java.lang.Object r3 = java.lang.reflect.Array.newInstance(r13, r4)
            r2 = 0
        L_0x000d:
            if (r2 >= r4) goto L_0x0064
            java.lang.Object r5 = r14.get(r2)
            if (r5 != r14) goto L_0x001b
            java.lang.reflect.Array.set(r3, r2, r3)
        L_0x0018:
            int r2 = r2 + 1
            goto L_0x000d
        L_0x001b:
            boolean r10 = r13.isArray()
            if (r10 == 0) goto L_0x0033
            boolean r10 = r13.isInstance(r5)
            if (r10 == 0) goto L_0x002c
            r1 = r5
        L_0x0028:
            java.lang.reflect.Array.set(r3, r2, r1)
            goto L_0x0018
        L_0x002c:
            com.alibaba.fastjson.JSONArray r5 = (com.alibaba.fastjson.JSONArray) r5
            java.lang.Object r1 = r11.toObjectArray(r12, r13, r5)
            goto L_0x0028
        L_0x0033:
            r1 = 0
            boolean r10 = r5 instanceof com.alibaba.fastjson.JSONArray
            if (r10 == 0) goto L_0x0056
            r0 = 0
            r6 = r5
            com.alibaba.fastjson.JSONArray r6 = (com.alibaba.fastjson.JSONArray) r6
            int r7 = r6.size()
            r9 = 0
        L_0x0041:
            if (r9 >= r7) goto L_0x0050
            java.lang.Object r8 = r6.get(r9)
            if (r8 != r14) goto L_0x004d
            r6.set(r2, r3)
            r0 = 1
        L_0x004d:
            int r9 = r9 + 1
            goto L_0x0041
        L_0x0050:
            if (r0 == 0) goto L_0x0056
            java.lang.Object[] r1 = r6.toArray()
        L_0x0056:
            if (r1 != 0) goto L_0x0060
            com.alibaba.fastjson.parser.ParserConfig r10 = r12.getConfig()
            java.lang.Object r1 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r5, r13, (com.alibaba.fastjson.parser.ParserConfig) r10)
        L_0x0060:
            java.lang.reflect.Array.set(r3, r2, r1)
            goto L_0x0018
        L_0x0064:
            r14.setRelatedArray(r3)
            r14.setComponentType(r13)
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.ObjectArrayCodec.toObjectArray(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.Class, com.alibaba.fastjson.JSONArray):java.lang.Object");
    }

    public int getFastMatchToken() {
        return 14;
    }
}
