package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class ResolveFieldDeserializer extends FieldDeserializer {
    private final Collection collection;
    private final int index;
    private final Object key;
    private final List list;
    private final Map map;
    private final DefaultJSONParser parser;

    public ResolveFieldDeserializer(DefaultJSONParser parser2, List list2, int index2) {
        super((Class<?>) null, (FieldInfo) null);
        this.parser = parser2;
        this.index = index2;
        this.list = list2;
        this.key = null;
        this.map = null;
        this.collection = null;
    }

    public ResolveFieldDeserializer(Map map2, Object index2) {
        super((Class<?>) null, (FieldInfo) null);
        this.parser = null;
        this.index = -1;
        this.list = null;
        this.key = index2;
        this.map = map2;
        this.collection = null;
    }

    public ResolveFieldDeserializer(Collection collection2) {
        super((Class<?>) null, (FieldInfo) null);
        this.parser = null;
        this.index = -1;
        this.list = null;
        this.key = null;
        this.map = null;
        this.collection = collection2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0023, code lost:
        r3 = (com.alibaba.fastjson.JSONArray) r6.list;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setValue(java.lang.Object r7, java.lang.Object r8) {
        /*
            r6 = this;
            java.util.Map r4 = r6.map
            if (r4 == 0) goto L_0x000c
            java.util.Map r4 = r6.map
            java.lang.Object r5 = r6.key
            r4.put(r5, r8)
        L_0x000b:
            return
        L_0x000c:
            java.util.Collection r4 = r6.collection
            if (r4 == 0) goto L_0x0016
            java.util.Collection r4 = r6.collection
            r4.add(r8)
            goto L_0x000b
        L_0x0016:
            java.util.List r4 = r6.list
            int r5 = r6.index
            r4.set(r5, r8)
            java.util.List r4 = r6.list
            boolean r4 = r4 instanceof com.alibaba.fastjson.JSONArray
            if (r4 == 0) goto L_0x000b
            java.util.List r3 = r6.list
            com.alibaba.fastjson.JSONArray r3 = (com.alibaba.fastjson.JSONArray) r3
            java.lang.Object r0 = r3.getRelatedArray()
            if (r0 == 0) goto L_0x000b
            int r1 = java.lang.reflect.Array.getLength(r0)
            int r4 = r6.index
            if (r1 <= r4) goto L_0x000b
            java.lang.reflect.Type r4 = r3.getComponentType()
            if (r4 == 0) goto L_0x004f
            java.lang.reflect.Type r4 = r3.getComponentType()
            com.alibaba.fastjson.parser.DefaultJSONParser r5 = r6.parser
            com.alibaba.fastjson.parser.ParserConfig r5 = r5.getConfig()
            java.lang.Object r2 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r8, (java.lang.reflect.Type) r4, (com.alibaba.fastjson.parser.ParserConfig) r5)
        L_0x0049:
            int r4 = r6.index
            java.lang.reflect.Array.set(r0, r4, r2)
            goto L_0x000b
        L_0x004f:
            r2 = r8
            goto L_0x0049
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.ResolveFieldDeserializer.setValue(java.lang.Object, java.lang.Object):void");
    }

    public void parseField(DefaultJSONParser parser2, Object object, Type objectType, Map<String, Object> map2) {
    }
}
