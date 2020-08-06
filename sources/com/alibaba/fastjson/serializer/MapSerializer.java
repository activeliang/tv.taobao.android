package com.alibaba.fastjson.serializer;

public class MapSerializer extends SerializeFilterable implements ObjectSerializer {
    public static MapSerializer instance = new MapSerializer();

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Removed duplicated region for block: B:156:0x0309 A[Catch:{ all -> 0x0233 }] */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x01af A[Catch:{ all -> 0x0233 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void write(com.alibaba.fastjson.serializer.JSONSerializer r38, java.lang.Object r39, java.lang.Object r40, java.lang.reflect.Type r41, int r42) throws java.io.IOException {
        /*
            r37 = this;
            r0 = r38
            com.alibaba.fastjson.serializer.SerializeWriter r0 = r0.out
            r26 = r0
            if (r39 != 0) goto L_0x000c
            r26.writeNull()
        L_0x000b:
            return
        L_0x000c:
            r23 = r39
            java.util.Map r23 = (java.util.Map) r23
            boolean r4 = r38.containsReference(r39)
            if (r4 == 0) goto L_0x001a
            r38.writeReference(r39)
            goto L_0x000b
        L_0x001a:
            r0 = r38
            com.alibaba.fastjson.serializer.SerialContext r0 = r0.context
            r27 = r0
            r4 = 0
            r0 = r38
            r1 = r27
            r2 = r39
            r3 = r40
            r0.setContext(r1, r2, r3, r4)
            r4 = 123(0x7b, float:1.72E-43)
            r0 = r26
            r0.write((int) r4)     // Catch:{ all -> 0x0233 }
            r38.incrementIndent()     // Catch:{ all -> 0x0233 }
            r28 = 0
            r10 = 0
            r21 = 1
            com.alibaba.fastjson.serializer.SerializerFeature r4 = com.alibaba.fastjson.serializer.SerializerFeature.WriteClassName     // Catch:{ all -> 0x0233 }
            r0 = r26
            boolean r4 = r0.isEnabled((com.alibaba.fastjson.serializer.SerializerFeature) r4)     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x0087
            r0 = r38
            com.alibaba.fastjson.serializer.SerializeConfig r4 = r0.config     // Catch:{ all -> 0x0233 }
            java.lang.String r0 = r4.typeKey     // Catch:{ all -> 0x0233 }
            r33 = r0
            java.lang.Class r24 = r23.getClass()     // Catch:{ all -> 0x0233 }
            java.lang.Class<com.alibaba.fastjson.JSONObject> r4 = com.alibaba.fastjson.JSONObject.class
            r0 = r24
            if (r0 == r4) goto L_0x0063
            java.lang.Class<java.util.HashMap> r4 = java.util.HashMap.class
            r0 = r24
            if (r0 == r4) goto L_0x0063
            java.lang.Class<java.util.LinkedHashMap> r4 = java.util.LinkedHashMap.class
            r0 = r24
            if (r0 != r4) goto L_0x023b
        L_0x0063:
            r0 = r23
            r1 = r33
            boolean r4 = r0.containsKey(r1)     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x023b
            r17 = 1
        L_0x006f:
            if (r17 != 0) goto L_0x0087
            r0 = r26
            r1 = r33
            r0.writeFieldName(r1)     // Catch:{ all -> 0x0233 }
            java.lang.Class r4 = r39.getClass()     // Catch:{ all -> 0x0233 }
            java.lang.String r4 = r4.getName()     // Catch:{ all -> 0x0233 }
            r0 = r26
            r0.writeString(r4)     // Catch:{ all -> 0x0233 }
            r21 = 0
        L_0x0087:
            java.util.Set r4 = r23.entrySet()     // Catch:{ all -> 0x0233 }
            java.util.Iterator r36 = r4.iterator()     // Catch:{ all -> 0x0233 }
            r30 = r10
        L_0x0091:
            boolean r4 = r36.hasNext()     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x03bf
            java.lang.Object r19 = r36.next()     // Catch:{ all -> 0x0233 }
            java.util.Map$Entry r19 = (java.util.Map.Entry) r19     // Catch:{ all -> 0x0233 }
            java.lang.Object r9 = r19.getValue()     // Catch:{ all -> 0x0233 }
            java.lang.Object r20 = r19.getKey()     // Catch:{ all -> 0x0233 }
            r0 = r38
            java.util.List r0 = r0.propertyPreFilters     // Catch:{ all -> 0x0233 }
            r29 = r0
            if (r29 == 0) goto L_0x00cc
            int r4 = r29.size()     // Catch:{ all -> 0x0233 }
            if (r4 <= 0) goto L_0x00cc
            if (r20 == 0) goto L_0x00bb
            r0 = r20
            boolean r4 = r0 instanceof java.lang.String     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x023f
        L_0x00bb:
            r0 = r20
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x0233 }
            r4 = r0
            r0 = r37
            r1 = r38
            r2 = r39
            boolean r4 = r0.applyName(r1, r2, r4)     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x0091
        L_0x00cc:
            r0 = r37
            java.util.List r0 = r0.propertyPreFilters     // Catch:{ all -> 0x0233 }
            r29 = r0
            if (r29 == 0) goto L_0x00f3
            int r4 = r29.size()     // Catch:{ all -> 0x0233 }
            if (r4 <= 0) goto L_0x00f3
            if (r20 == 0) goto L_0x00e2
            r0 = r20
            boolean r4 = r0 instanceof java.lang.String     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x0261
        L_0x00e2:
            r0 = r20
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x0233 }
            r4 = r0
            r0 = r37
            r1 = r38
            r2 = r39
            boolean r4 = r0.applyName(r1, r2, r4)     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x0091
        L_0x00f3:
            r0 = r38
            java.util.List r0 = r0.propertyFilters     // Catch:{ all -> 0x0233 }
            r31 = r0
            if (r31 == 0) goto L_0x011a
            int r4 = r31.size()     // Catch:{ all -> 0x0233 }
            if (r4 <= 0) goto L_0x011a
            if (r20 == 0) goto L_0x0109
            r0 = r20
            boolean r4 = r0 instanceof java.lang.String     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x0283
        L_0x0109:
            r0 = r20
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x0233 }
            r4 = r0
            r0 = r37
            r1 = r38
            r2 = r39
            boolean r4 = r0.apply(r1, r2, r4, r9)     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x0091
        L_0x011a:
            r0 = r37
            java.util.List r0 = r0.propertyFilters     // Catch:{ all -> 0x0233 }
            r31 = r0
            if (r31 == 0) goto L_0x0141
            int r4 = r31.size()     // Catch:{ all -> 0x0233 }
            if (r4 <= 0) goto L_0x0141
            if (r20 == 0) goto L_0x0130
            r0 = r20
            boolean r4 = r0 instanceof java.lang.String     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x02a5
        L_0x0130:
            r0 = r20
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x0233 }
            r4 = r0
            r0 = r37
            r1 = r38
            r2 = r39
            boolean r4 = r0.apply(r1, r2, r4, r9)     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x0091
        L_0x0141:
            r0 = r38
            java.util.List r0 = r0.nameFilters     // Catch:{ all -> 0x0233 }
            r25 = r0
            if (r25 == 0) goto L_0x0165
            int r4 = r25.size()     // Catch:{ all -> 0x0233 }
            if (r4 <= 0) goto L_0x0165
            if (r20 == 0) goto L_0x0157
            r0 = r20
            boolean r4 = r0 instanceof java.lang.String     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x02c7
        L_0x0157:
            java.lang.String r20 = (java.lang.String) r20     // Catch:{ all -> 0x0233 }
            r0 = r37
            r1 = r38
            r2 = r39
            r3 = r20
            java.lang.String r20 = r0.processKey(r1, r2, r3, r9)     // Catch:{ all -> 0x0233 }
        L_0x0165:
            r0 = r37
            java.util.List r0 = r0.nameFilters     // Catch:{ all -> 0x0233 }
            r25 = r0
            if (r25 == 0) goto L_0x03e4
            int r4 = r25.size()     // Catch:{ all -> 0x0233 }
            if (r4 <= 0) goto L_0x03e4
            if (r20 == 0) goto L_0x017b
            r0 = r20
            boolean r4 = r0 instanceof java.lang.String     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x02e7
        L_0x017b:
            java.lang.String r20 = (java.lang.String) r20     // Catch:{ all -> 0x0233 }
            r0 = r37
            r1 = r38
            r2 = r39
            r3 = r20
            java.lang.String r20 = r0.processKey(r1, r2, r3, r9)     // Catch:{ all -> 0x0233 }
            r35 = r20
        L_0x018b:
            r0 = r38
            java.util.List r0 = r0.valueFilters     // Catch:{ all -> 0x0233 }
            r34 = r0
            r0 = r37
            java.util.List r0 = r0.contextValueFilters     // Catch:{ all -> 0x0233 }
            r18 = r0
            if (r34 == 0) goto L_0x019f
            int r4 = r34.size()     // Catch:{ all -> 0x0233 }
            if (r4 > 0) goto L_0x01a7
        L_0x019f:
            if (r18 == 0) goto L_0x01bf
            int r4 = r18.size()     // Catch:{ all -> 0x0233 }
            if (r4 <= 0) goto L_0x01bf
        L_0x01a7:
            if (r35 == 0) goto L_0x01af
            r0 = r35
            boolean r4 = r0 instanceof java.lang.String     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x0309
        L_0x01af:
            r6 = 0
            r0 = r35
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x0233 }
            r8 = r0
            r4 = r37
            r5 = r38
            r7 = r39
            java.lang.Object r9 = r4.processValue(r5, r6, r7, r8, r9)     // Catch:{ all -> 0x0233 }
        L_0x01bf:
            r0 = r37
            java.util.List r0 = r0.valueFilters     // Catch:{ all -> 0x0233 }
            r34 = r0
            r0 = r37
            java.util.List r0 = r0.contextValueFilters     // Catch:{ all -> 0x0233 }
            r18 = r0
            if (r34 == 0) goto L_0x01d3
            int r4 = r34.size()     // Catch:{ all -> 0x0233 }
            if (r4 > 0) goto L_0x01db
        L_0x01d3:
            if (r18 == 0) goto L_0x01f4
            int r4 = r18.size()     // Catch:{ all -> 0x0233 }
            if (r4 <= 0) goto L_0x01f4
        L_0x01db:
            if (r35 == 0) goto L_0x01e3
            r0 = r35
            boolean r4 = r0 instanceof java.lang.String     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x032a
        L_0x01e3:
            r12 = 0
            r0 = r35
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x0233 }
            r14 = r0
            r10 = r37
            r11 = r38
            r13 = r39
            r15 = r9
            java.lang.Object r9 = r10.processValue(r11, r12, r13, r14, r15)     // Catch:{ all -> 0x0233 }
        L_0x01f4:
            if (r9 != 0) goto L_0x0200
            int r4 = com.alibaba.fastjson.serializer.SerializerFeature.WRITE_MAP_NULL_FEATURES     // Catch:{ all -> 0x0233 }
            r0 = r26
            boolean r4 = r0.isEnabled((int) r4)     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x0091
        L_0x0200:
            r0 = r35
            boolean r4 = r0 instanceof java.lang.String     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x034b
            r0 = r35
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x0233 }
            r22 = r0
            if (r21 != 0) goto L_0x0215
            r4 = 44
            r0 = r26
            r0.write((int) r4)     // Catch:{ all -> 0x0233 }
        L_0x0215:
            com.alibaba.fastjson.serializer.SerializerFeature r4 = com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat     // Catch:{ all -> 0x0233 }
            r0 = r26
            boolean r4 = r0.isEnabled((com.alibaba.fastjson.serializer.SerializerFeature) r4)     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x0222
            r38.println()     // Catch:{ all -> 0x0233 }
        L_0x0222:
            r4 = 1
            r0 = r26
            r1 = r22
            r0.writeFieldName(r1, r4)     // Catch:{ all -> 0x0233 }
        L_0x022a:
            r21 = 0
            if (r9 != 0) goto L_0x038e
            r26.writeNull()     // Catch:{ all -> 0x0233 }
            goto L_0x0091
        L_0x0233:
            r4 = move-exception
            r0 = r27
            r1 = r38
            r1.context = r0
            throw r4
        L_0x023b:
            r17 = 0
            goto L_0x006f
        L_0x023f:
            java.lang.Class r4 = r20.getClass()     // Catch:{ all -> 0x0233 }
            boolean r4 = r4.isPrimitive()     // Catch:{ all -> 0x0233 }
            if (r4 != 0) goto L_0x024f
            r0 = r20
            boolean r4 = r0 instanceof java.lang.Number     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x00cc
        L_0x024f:
            java.lang.String r8 = com.alibaba.fastjson.JSON.toJSONString(r20)     // Catch:{ all -> 0x0233 }
            r0 = r37
            r1 = r38
            r2 = r39
            boolean r4 = r0.applyName(r1, r2, r8)     // Catch:{ all -> 0x0233 }
            if (r4 != 0) goto L_0x00cc
            goto L_0x0091
        L_0x0261:
            java.lang.Class r4 = r20.getClass()     // Catch:{ all -> 0x0233 }
            boolean r4 = r4.isPrimitive()     // Catch:{ all -> 0x0233 }
            if (r4 != 0) goto L_0x0271
            r0 = r20
            boolean r4 = r0 instanceof java.lang.Number     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x00f3
        L_0x0271:
            java.lang.String r8 = com.alibaba.fastjson.JSON.toJSONString(r20)     // Catch:{ all -> 0x0233 }
            r0 = r37
            r1 = r38
            r2 = r39
            boolean r4 = r0.applyName(r1, r2, r8)     // Catch:{ all -> 0x0233 }
            if (r4 != 0) goto L_0x00f3
            goto L_0x0091
        L_0x0283:
            java.lang.Class r4 = r20.getClass()     // Catch:{ all -> 0x0233 }
            boolean r4 = r4.isPrimitive()     // Catch:{ all -> 0x0233 }
            if (r4 != 0) goto L_0x0293
            r0 = r20
            boolean r4 = r0 instanceof java.lang.Number     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x011a
        L_0x0293:
            java.lang.String r8 = com.alibaba.fastjson.JSON.toJSONString(r20)     // Catch:{ all -> 0x0233 }
            r0 = r37
            r1 = r38
            r2 = r39
            boolean r4 = r0.apply(r1, r2, r8, r9)     // Catch:{ all -> 0x0233 }
            if (r4 != 0) goto L_0x011a
            goto L_0x0091
        L_0x02a5:
            java.lang.Class r4 = r20.getClass()     // Catch:{ all -> 0x0233 }
            boolean r4 = r4.isPrimitive()     // Catch:{ all -> 0x0233 }
            if (r4 != 0) goto L_0x02b5
            r0 = r20
            boolean r4 = r0 instanceof java.lang.Number     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x0141
        L_0x02b5:
            java.lang.String r8 = com.alibaba.fastjson.JSON.toJSONString(r20)     // Catch:{ all -> 0x0233 }
            r0 = r37
            r1 = r38
            r2 = r39
            boolean r4 = r0.apply(r1, r2, r8, r9)     // Catch:{ all -> 0x0233 }
            if (r4 != 0) goto L_0x0141
            goto L_0x0091
        L_0x02c7:
            java.lang.Class r4 = r20.getClass()     // Catch:{ all -> 0x0233 }
            boolean r4 = r4.isPrimitive()     // Catch:{ all -> 0x0233 }
            if (r4 != 0) goto L_0x02d7
            r0 = r20
            boolean r4 = r0 instanceof java.lang.Number     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x0165
        L_0x02d7:
            java.lang.String r8 = com.alibaba.fastjson.JSON.toJSONString(r20)     // Catch:{ all -> 0x0233 }
            r0 = r37
            r1 = r38
            r2 = r39
            java.lang.String r20 = r0.processKey(r1, r2, r8, r9)     // Catch:{ all -> 0x0233 }
            goto L_0x0165
        L_0x02e7:
            java.lang.Class r4 = r20.getClass()     // Catch:{ all -> 0x0233 }
            boolean r4 = r4.isPrimitive()     // Catch:{ all -> 0x0233 }
            if (r4 != 0) goto L_0x02f7
            r0 = r20
            boolean r4 = r0 instanceof java.lang.Number     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x03e4
        L_0x02f7:
            java.lang.String r8 = com.alibaba.fastjson.JSON.toJSONString(r20)     // Catch:{ all -> 0x0233 }
            r0 = r37
            r1 = r38
            r2 = r39
            java.lang.String r20 = r0.processKey(r1, r2, r8, r9)     // Catch:{ all -> 0x0233 }
            r35 = r20
            goto L_0x018b
        L_0x0309:
            java.lang.Class r4 = r35.getClass()     // Catch:{ all -> 0x0233 }
            boolean r4 = r4.isPrimitive()     // Catch:{ all -> 0x0233 }
            if (r4 != 0) goto L_0x0319
            r0 = r35
            boolean r4 = r0 instanceof java.lang.Number     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x01bf
        L_0x0319:
            java.lang.String r8 = com.alibaba.fastjson.JSON.toJSONString(r35)     // Catch:{ all -> 0x0233 }
            r6 = 0
            r4 = r37
            r5 = r38
            r7 = r39
            java.lang.Object r9 = r4.processValue(r5, r6, r7, r8, r9)     // Catch:{ all -> 0x0233 }
            goto L_0x01bf
        L_0x032a:
            java.lang.Class r4 = r35.getClass()     // Catch:{ all -> 0x0233 }
            boolean r4 = r4.isPrimitive()     // Catch:{ all -> 0x0233 }
            if (r4 != 0) goto L_0x033a
            r0 = r35
            boolean r4 = r0 instanceof java.lang.Number     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x01f4
        L_0x033a:
            java.lang.String r8 = com.alibaba.fastjson.JSON.toJSONString(r35)     // Catch:{ all -> 0x0233 }
            r6 = 0
            r4 = r37
            r5 = r38
            r7 = r39
            java.lang.Object r9 = r4.processValue(r5, r6, r7, r8, r9)     // Catch:{ all -> 0x0233 }
            goto L_0x01f4
        L_0x034b:
            if (r21 != 0) goto L_0x0354
            r4 = 44
            r0 = r26
            r0.write((int) r4)     // Catch:{ all -> 0x0233 }
        L_0x0354:
            com.alibaba.fastjson.serializer.SerializerFeature r4 = com.alibaba.fastjson.serializer.SerializerFeature.BrowserCompatible     // Catch:{ all -> 0x0233 }
            r0 = r26
            boolean r4 = r0.isEnabled((com.alibaba.fastjson.serializer.SerializerFeature) r4)     // Catch:{ all -> 0x0233 }
            if (r4 != 0) goto L_0x0372
            com.alibaba.fastjson.serializer.SerializerFeature r4 = com.alibaba.fastjson.serializer.SerializerFeature.WriteNonStringKeyAsString     // Catch:{ all -> 0x0233 }
            r0 = r26
            boolean r4 = r0.isEnabled((com.alibaba.fastjson.serializer.SerializerFeature) r4)     // Catch:{ all -> 0x0233 }
            if (r4 != 0) goto L_0x0372
            com.alibaba.fastjson.serializer.SerializerFeature r4 = com.alibaba.fastjson.serializer.SerializerFeature.BrowserSecure     // Catch:{ all -> 0x0233 }
            r0 = r26
            boolean r4 = r0.isEnabled((com.alibaba.fastjson.serializer.SerializerFeature) r4)     // Catch:{ all -> 0x0233 }
            if (r4 == 0) goto L_0x0386
        L_0x0372:
            java.lang.String r32 = com.alibaba.fastjson.JSON.toJSONString(r35)     // Catch:{ all -> 0x0233 }
            r0 = r38
            r1 = r32
            r0.write((java.lang.String) r1)     // Catch:{ all -> 0x0233 }
        L_0x037d:
            r4 = 58
            r0 = r26
            r0.write((int) r4)     // Catch:{ all -> 0x0233 }
            goto L_0x022a
        L_0x0386:
            r0 = r38
            r1 = r35
            r0.write((java.lang.Object) r1)     // Catch:{ all -> 0x0233 }
            goto L_0x037d
        L_0x038e:
            java.lang.Class r16 = r9.getClass()     // Catch:{ all -> 0x0233 }
            r0 = r16
            r1 = r28
            if (r0 != r1) goto L_0x03aa
            r14 = 0
            r15 = 0
            r10 = r30
            r11 = r38
            r12 = r9
            r13 = r35
            r10.write(r11, r12, r13, r14, r15)     // Catch:{ all -> 0x0233 }
            r10 = r30
        L_0x03a6:
            r30 = r10
            goto L_0x0091
        L_0x03aa:
            r28 = r16
            r0 = r38
            r1 = r16
            com.alibaba.fastjson.serializer.ObjectSerializer r10 = r0.getObjectWriter(r1)     // Catch:{ all -> 0x0233 }
            r14 = 0
            r15 = 0
            r11 = r38
            r12 = r9
            r13 = r35
            r10.write(r11, r12, r13, r14, r15)     // Catch:{ all -> 0x0233 }
            goto L_0x03a6
        L_0x03bf:
            r0 = r27
            r1 = r38
            r1.context = r0
            r38.decrementIdent()
            com.alibaba.fastjson.serializer.SerializerFeature r4 = com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat
            r0 = r26
            boolean r4 = r0.isEnabled((com.alibaba.fastjson.serializer.SerializerFeature) r4)
            if (r4 == 0) goto L_0x03db
            int r4 = r23.size()
            if (r4 <= 0) goto L_0x03db
            r38.println()
        L_0x03db:
            r4 = 125(0x7d, float:1.75E-43)
            r0 = r26
            r0.write((int) r4)
            goto L_0x000b
        L_0x03e4:
            r35 = r20
            goto L_0x018b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.MapSerializer.write(com.alibaba.fastjson.serializer.JSONSerializer, java.lang.Object, java.lang.Object, java.lang.reflect.Type, int):void");
    }
}
