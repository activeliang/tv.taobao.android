package com.yunos.tvtaobao.payment.request;

public class TvTaoBaoSwitchBean {
    private static final String COMMONT_SWITCH = "scoreSwitch";
    private static final String GLOBALJSONCONFIG = "globalJsonConfig";
    private static final String IS_SAFE = "safe";
    private static final String LOGIN2_3 = "login2_3";
    private static final String TAG = "TvTaoBaoSwitchBean";
    public GlobalConfig globalJsonConfig;
    public boolean is_safe = true;
    public boolean is_show_commont = true;
    public boolean login2_3;

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0059, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x005e, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x005f, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x005e A[ExcHandler: Exception (r0v0 'e' java.lang.Exception A[CUSTOM_DECLARE]), Splitter:B:3:0x000e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public TvTaoBaoSwitchBean(org.json.JSONObject r5) {
        /*
            r4 = this;
            r1 = 1
            r4.<init>()
            r4.is_safe = r1
            r4.is_show_commont = r1
            if (r5 != 0) goto L_0x000b
        L_0x000a:
            return
        L_0x000b:
            java.lang.String r1 = "login2_3"
            boolean r1 = r5.getBoolean(r1)     // Catch:{ JSONException -> 0x0059, Exception -> 0x005e }
            r4.login2_3 = r1     // Catch:{ JSONException -> 0x0059, Exception -> 0x005e }
            java.lang.String r1 = "safe"
            boolean r1 = r5.getBoolean(r1)     // Catch:{ JSONException -> 0x0059, Exception -> 0x005e }
            r4.is_safe = r1     // Catch:{ JSONException -> 0x0059, Exception -> 0x005e }
            java.lang.String r1 = "scoreSwitch"
            boolean r1 = r5.getBoolean(r1)     // Catch:{ JSONException -> 0x0059, Exception -> 0x005e }
            r4.is_show_commont = r1     // Catch:{ JSONException -> 0x0059, Exception -> 0x005e }
        L_0x0026:
            java.lang.String r1 = "globalJsonConfig"
            java.lang.String r1 = r5.getString(r1)     // Catch:{ JSONException -> 0x0054, Exception -> 0x005e }
            java.lang.Class<com.yunos.tvtaobao.payment.request.GlobalConfig> r2 = com.yunos.tvtaobao.payment.request.GlobalConfig.class
            java.lang.Object r1 = com.alibaba.fastjson.JSON.parseObject((java.lang.String) r1, r2)     // Catch:{ JSONException -> 0x0054, Exception -> 0x005e }
            com.yunos.tvtaobao.payment.request.GlobalConfig r1 = (com.yunos.tvtaobao.payment.request.GlobalConfig) r1     // Catch:{ JSONException -> 0x0054, Exception -> 0x005e }
            r4.globalJsonConfig = r1     // Catch:{ JSONException -> 0x0054, Exception -> 0x005e }
            java.lang.String r1 = "TvTaoBaoSwitchBean"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x0054, Exception -> 0x005e }
            r2.<init>()     // Catch:{ JSONException -> 0x0054, Exception -> 0x005e }
            java.lang.String r3 = "ljyljyljy= "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ JSONException -> 0x0054, Exception -> 0x005e }
            com.yunos.tvtaobao.payment.request.GlobalConfig r3 = r4.globalJsonConfig     // Catch:{ JSONException -> 0x0054, Exception -> 0x005e }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ JSONException -> 0x0054, Exception -> 0x005e }
            java.lang.String r2 = r2.toString()     // Catch:{ JSONException -> 0x0054, Exception -> 0x005e }
            com.zhiping.dev.android.logger.ZpLogger.v(r1, r2)     // Catch:{ JSONException -> 0x0054, Exception -> 0x005e }
            goto L_0x000a
        L_0x0054:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x000a
        L_0x0059:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ JSONException -> 0x0054, Exception -> 0x005e }
            goto L_0x0026
        L_0x005e:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x000a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.payment.request.TvTaoBaoSwitchBean.<init>(org.json.JSONObject):void");
    }

    public String toString() {
        return "TvTaoBaoSwitchBean{login2_3=" + this.login2_3 + "is_safe=" + this.is_safe + "is_show_commont=" + this.is_show_commont + "globalJsonConfig=" + this.globalJsonConfig + '}';
    }
}
