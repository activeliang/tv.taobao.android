package com.alibaba.motu.crashreporter.utrestapi;

public class UTRestReqSend {
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean sendLog(android.content.Context r10, java.util.Map<java.lang.String, java.lang.String> r11, long r12, java.lang.String r14, int r15, java.lang.Object r16, java.lang.Object r17, java.lang.Object r18, java.util.Map<java.lang.String, java.lang.String> r19) {
        /*
            java.lang.String r8 = "UTRestAPI start send log!"
            com.alibaba.motu.crashreporter.LogUtil.i(r8)     // Catch:{ Throwable -> 0x00a5 }
            if (r11 != 0) goto L_0x0010
            java.lang.String r8 = "environment data is null and send failed!"
            com.alibaba.motu.crashreporter.LogUtil.i(r8)     // Catch:{ Throwable -> 0x00a5 }
            r8 = 0
        L_0x000f:
            return r8
        L_0x0010:
            com.alibaba.motu.crashreporter.utrestapi.UTReqDataBuildResult r4 = com.alibaba.motu.crashreporter.utrestapi.UTReqDataBuilder.buildTracePostReqDataObj(r10, r11, r12, r14, r15, r16, r17, r18, r19)     // Catch:{ Throwable -> 0x00a5 }
            if (r4 == 0) goto L_0x009e
            java.lang.String r8 = "UTRestAPI build data succ!"
            com.alibaba.motu.crashreporter.LogUtil.i(r8)     // Catch:{ Throwable -> 0x00a5 }
            java.util.Map r2 = r4.getPostReqData()     // Catch:{ Throwable -> 0x00a5 }
            if (r2 != 0) goto L_0x002a
            java.lang.String r8 = "postReqData is null!"
            com.alibaba.motu.crashreporter.LogUtil.i(r8)     // Catch:{ Throwable -> 0x00a5 }
            r8 = 0
            goto L_0x000f
        L_0x002a:
            java.lang.String r5 = r4.getReqUrl()     // Catch:{ Throwable -> 0x00a5 }
            boolean r8 = com.alibaba.motu.crashreporter.utils.StringUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x00a5 }
            if (r8 == 0) goto L_0x003c
            java.lang.String r8 = "reqUrl is null!"
            com.alibaba.motu.crashreporter.LogUtil.i(r8)     // Catch:{ Throwable -> 0x00a5 }
            r8 = 0
            goto L_0x000f
        L_0x003c:
            r8 = 2
            r9 = 1
            byte[] r3 = com.alibaba.motu.crashreporter.utrestapi.UTRestHttpUtils.sendRequest(r8, r5, r2, r9)     // Catch:{ Throwable -> 0x00a5 }
            if (r3 == 0) goto L_0x009b
            java.lang.String r6 = new java.lang.String     // Catch:{ UnsupportedEncodingException -> 0x0094 }
            java.lang.String r8 = "UTF-8"
            r6.<init>(r3, r8)     // Catch:{ UnsupportedEncodingException -> 0x0094 }
            boolean r8 = com.alibaba.motu.crashreporter.utils.StringUtils.isEmpty(r6)     // Catch:{ UnsupportedEncodingException -> 0x0094 }
            if (r8 != 0) goto L_0x009b
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0078 }
            r1.<init>(r6)     // Catch:{ JSONException -> 0x0078 }
            java.lang.String r8 = "success"
            boolean r8 = r1.has(r8)     // Catch:{ JSONException -> 0x0078 }
            if (r8 == 0) goto L_0x007f
            java.lang.String r8 = "success"
            java.lang.String r7 = r1.getString(r8)     // Catch:{ JSONException -> 0x0078 }
            boolean r8 = com.alibaba.motu.crashreporter.utils.StringUtils.isBlank(r7)     // Catch:{ JSONException -> 0x0078 }
            if (r8 != 0) goto L_0x007f
            java.lang.String r8 = "success"
            boolean r8 = r7.equals(r8)     // Catch:{ JSONException -> 0x0078 }
            if (r8 == 0) goto L_0x007f
            r8 = 1
            goto L_0x000f
        L_0x0078:
            r0 = move-exception
            java.lang.String r8 = "result to json error!"
            com.alibaba.motu.crashreporter.LogUtil.e(r8, r0)     // Catch:{ UnsupportedEncodingException -> 0x0094 }
        L_0x007f:
            java.lang.String r8 = "success"
            boolean r8 = r6.contains(r8)     // Catch:{ UnsupportedEncodingException -> 0x0094 }
            if (r8 != 0) goto L_0x0091
            java.lang.String r8 = "SUCCESS"
            boolean r8 = r6.contains(r8)     // Catch:{ UnsupportedEncodingException -> 0x0094 }
            if (r8 == 0) goto L_0x009b
        L_0x0091:
            r8 = 1
            goto L_0x000f
        L_0x0094:
            r0 = move-exception
            java.lang.String r8 = "result encoding UTF-8 error!"
            com.alibaba.motu.crashreporter.LogUtil.e(r8, r0)     // Catch:{ Throwable -> 0x00a5 }
        L_0x009b:
            r8 = 0
            goto L_0x000f
        L_0x009e:
            java.lang.String r8 = "UTRestAPI build data failure!"
            com.alibaba.motu.crashreporter.LogUtil.i(r8)     // Catch:{ Throwable -> 0x00a5 }
            goto L_0x009b
        L_0x00a5:
            r0 = move-exception
            java.lang.String r8 = "system error!"
            com.alibaba.motu.crashreporter.LogUtil.e(r8, r0)
            goto L_0x009b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.crashreporter.utrestapi.UTRestReqSend.sendLog(android.content.Context, java.util.Map, long, java.lang.String, int, java.lang.Object, java.lang.Object, java.lang.Object, java.util.Map):boolean");
    }
}
