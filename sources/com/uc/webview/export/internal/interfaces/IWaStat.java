package com.uc.webview.export.internal.interfaces;

import com.ali.auth.third.core.model.KernelMessageConstants;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.internal.c.a.a;
import com.uc.webview.export.internal.c.a.b;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.utility.Utils;
import java.util.Date;
import java.util.HashMap;

@Api
/* compiled from: ProGuard */
public interface IWaStat {
    public static final String ACTIVATE_PUSH_PROCESS = "sdk_act_pp";
    public static final String CORE_DOWNLOAD = "sdk_cdl";
    public static final String CORE_ERROR_CODE_DOWNLOAD = "sdk_ecdl0";
    public static final String CORE_ERROR_CODE_UNZIP = "sdk_ecuz0";
    public static final String CORE_ERROR_CODE_UPDATE_CHECK_REQUEST = "sdk_ecur0";
    public static final String CORE_ERROR_CODE_VERIFY = "sdk_ecv0";
    public static final String DEC = "sdk_dec";
    public static final String DEC_EXCEPTION = "sdk_dec_e";
    public static final String DEC_SUCCESS = "sdk_dec_s";
    public static final String DEC_ZIP = "sdk_decz";
    public static final String DEC_ZIP_SUCCESS = "sdk_decz_s";
    public static final String DOWNLOAD = "sdk_dl";
    public static final String DOWNLOAD_EXCEPTION = "sdk_dl_e";
    public static final String DOWNLOAD_EXISTS = "sdk_dl_x";
    public static final String DOWNLOAD_FAILED = "sdk_dl_f";
    public static final String DOWNLOAD_RECOVERED = "sdk_dl_r";
    public static final String DOWNLOAD_SUCCESS = "sdk_dl_s";
    public static final String ERROR_CODE_INIT = "sdk_eci";
    public static final String KEY_ART = "art";
    public static final String KEY_CLASS = "cls";
    public static final String KEY_CNT = "cnt";
    public static final String KEY_CODE = "code";
    public static final String KEY_COST = "cost";
    public static final String KEY_COST_CPU = "cost_cpu";
    public static final String KEY_CPU_CNT = "cpu_cnt";
    public static final String KEY_CPU_FREQ = "cpu_freq";
    public static final String KEY_CRASH = "crash";
    public static final String KEY_DATA = "data";
    public static final String KEY_DIR = "dir";
    public static final String KEY_DVM = "dvm";
    public static final String KEY_DVM2 = "dvm2";
    public static final String KEY_ENABLE = "enable";
    public static final String KEY_ERRNO = "err";
    public static final String KEY_FIRST_RUN = "frun";
    public static final String KEY_HOOK_RUN_AS_EXPECTED = "run_expected";
    public static final String KEY_HOOK_SUCCESS = "hook_succ";
    public static final String KEY_MESSAGE = "msg";
    public static final String KEY_MULTI_CORE = "multi_core";
    public static final String KEY_OLD = "old";
    public static final String KEY_PRIORITY = "pri";
    public static final String KEY_SDK_INT = "sdk_int";
    public static final String KEY_SUCCESS = "succ";
    public static final String KEY_TASK = "task";
    public static final String KEY_WIFI = "wifi";
    public static final String SETUP_DEFAULT_EXCEPTION = "sdk_stp_def_exc";
    public static final String SETUP_DELETE_CORE_COUNT = "sdk_stp_dcc";
    public static final String SETUP_EXTRA_EXCEPTION = "sdk_stp_ext_exc";
    public static final String SETUP_REPAIR_EXCEPTION = "sdk_stp_rep_exc";
    public static final String SETUP_START = "sdk_stp";
    public static final String SETUP_START_FINISH = "sdk_stp_fi";
    public static final String SETUP_SUCCESS = "sdk_stp_suc";
    public static final String SETUP_SUCCESS_INITED = "sdk_stp_i";
    public static final String SETUP_SUCCESS_LOADED = "sdk_stp_l";
    public static final String SETUP_SUCCESS_SETUPED = "sdk_stp_s";
    public static final String SETUP_TASK_DEXOPT = "sdk_opt";
    public static final String SETUP_TASK_HOOKDEX = "sdk_hookdex";
    public static final String SETUP_TASK_INIT = "sdk_ini";
    public static final String SETUP_TASK_LIBARY = "sdk_lib";
    public static final String SETUP_TASK_UCDEXOPT = "sdk_ucdexopt";
    public static final String SETUP_TASK_VERIFY = "sdk_vrf";
    public static final String SETUP_TOTAL_EXCEPTION = "sdk_stp_exc";
    public static final String SEVENZIP = "sdk_7z";
    public static final String SEVENZIP_EXCEPTION_CRC = "sdk_7z_e3";
    public static final String SEVENZIP_EXCEPTION_FAILED = "sdk_7z_e1";
    public static final String SEVENZIP_EXCEPTION_MEM = "sdk_7z_e2";
    public static final String SEVENZIP_FILE = "sdk_7z_f";
    public static final String SEVENZIP_FILE_SUCCESS = "sdk_7z_fs";
    public static final String SEVENZIP_LIB = "sdk_7z_l";
    public static final String SEVENZIP_LIB_SUCCESS = "sdk_7z_ls";
    public static final String SEVENZIP_SUCCESS = "sdk_7z_s";
    public static final String UCM = "sdk_ucm";
    public static final String UCM_DISK_MB = "sdk_ucm_dm";
    public static final String UCM_DISK_PERCENT = "sdk_ucm_dp";
    public static final String UCM_EXCEPTION_CHECK = "sdk_ucm_en";
    public static final String UCM_EXCEPTION_DOWNLOAD = "sdk_ucm_e1";
    public static final String UCM_EXCEPTION_UPDATE = "sdk_dec7z";
    public static final String UCM_EXISTS = "sdk_ucm_e";
    public static final String UCM_FAILED_DOWNLOAD = "sdk_dec7z_s";
    public static final String UCM_FAILED_VERIFY = "sdk_dec7z_ls";
    public static final String UCM_LAST_EXCEPTION = "sdk_ucm_le";
    public static final String UCM_PERCENT = "sdk_ucm_p";
    public static final String UCM_RECOVERED = "sdk_ucm_f";
    public static final String UCM_SUCCESS = "sdk_ucm_s";
    public static final String UCM_WIFI = "sdk_ucm_wifi";
    public static final String VIDEO_AC = "sdk_vac";
    public static final String VIDEO_DOWNLOAD = "sdk_vdl";
    public static final String VIDEO_DOWNLOAD_SUCCESS = "sdk_vdls";
    public static final String VIDEO_ERROR_CODE_DOWNLOAD = "sdk_ecdl1";
    public static final String VIDEO_ERROR_CODE_UNZIP = "sdk_ecuz1";
    public static final String VIDEO_ERROR_CODE_UPDATE_CHECK_REQUEST = "sdk_ecur1";
    public static final String VIDEO_ERROR_CODE_VERIFY = "sdk_ecv1";
    public static final String VIDEO_UNZIP = "sdk_vz";
    public static final String VIDEO_UNZIP_SUCCESS = "sdk_vzs";
    public static final String WV_NEW_AFTER = "sdk_wv_a";
    public static final String WV_NEW_BEFORE = "sdk_wv_b";

    @Api
    /* compiled from: ProGuard */
    public static class WaStat {
        public static void statPV(String str) {
            boolean z;
            a.C0009a aVar;
            if (a.a == null && d.e != null) {
                a.a(d.e);
            }
            if (a.a != null) {
                if (a.a == null && d.e != null) {
                    a.a(d.e);
                }
                a aVar2 = a.a;
                if (((Boolean) d.a(10006, "stat", true)).booleanValue() && !d.f) {
                    if (str == null || str.trim().length() == 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (!z) {
                        String lowerCase = str.toLowerCase();
                        if (lowerCase.startsWith("http://") || lowerCase.startsWith("https://")) {
                            if (Utils.sWAPrintLog) {
                                Log.d("SDKWaStat", "statPV:" + lowerCase);
                            }
                            if (((Boolean) d.a(10006, "stat", true)).booleanValue() && !d.f) {
                                Date date = new Date(System.currentTimeMillis());
                                int intValue = ((Boolean) d.a(10010, new Object[0])).booleanValue() ? ((Integer) d.a((int) KernelMessageConstants.PARAM_ERROR, new Object[0])).intValue() : 0;
                                if (!(intValue == 2 || intValue == 0)) {
                                    intValue = (intValue * 10) + d.l;
                                }
                                String str2 = aVar2.f.format(date) + "~" + intValue;
                                synchronized (aVar2.h) {
                                    if (aVar2.d == null) {
                                        aVar2.d = new HashMap();
                                    }
                                    a.C0009a aVar3 = aVar2.d.get(str2);
                                    if (aVar3 == null) {
                                        a.C0009a aVar4 = new a.C0009a(aVar2, (byte) 0);
                                        aVar2.d.put(str2, aVar4);
                                        aVar = aVar4;
                                    } else {
                                        aVar = aVar3;
                                    }
                                    aVar.b.put("tm", aVar2.g.format(date));
                                    Integer num = aVar.a.get("sum_pv");
                                    if (num == null) {
                                        aVar.a.put("sum_pv", 1);
                                    } else {
                                        aVar.a.put("sum_pv", Integer.valueOf(num.intValue() + 1));
                                    }
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            Log.w("SDKWaStat", "statPV>>WaStatImp not inited");
        }

        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static void stat(java.lang.String r7) {
            /*
                r6 = 2
                r1 = 0
                com.uc.webview.export.internal.c.a.a r0 = com.uc.webview.export.internal.c.a.a.a     // Catch:{ Throwable -> 0x00e2 }
                if (r0 != 0) goto L_0x000f
                android.content.Context r0 = com.uc.webview.export.internal.d.e     // Catch:{ Throwable -> 0x00e2 }
                if (r0 == 0) goto L_0x000f
                android.content.Context r0 = com.uc.webview.export.internal.d.e     // Catch:{ Throwable -> 0x00e2 }
                com.uc.webview.export.internal.c.a.a.a((android.content.Context) r0)     // Catch:{ Throwable -> 0x00e2 }
            L_0x000f:
                com.uc.webview.export.internal.c.a.a r0 = com.uc.webview.export.internal.c.a.a.a     // Catch:{ Throwable -> 0x00e2 }
                if (r0 == 0) goto L_0x00f7
                com.uc.webview.export.internal.c.a.a r0 = com.uc.webview.export.internal.c.a.a.a     // Catch:{ Throwable -> 0x00e2 }
                if (r0 != 0) goto L_0x0020
                android.content.Context r0 = com.uc.webview.export.internal.d.e     // Catch:{ Throwable -> 0x00e2 }
                if (r0 == 0) goto L_0x0020
                android.content.Context r0 = com.uc.webview.export.internal.d.e     // Catch:{ Throwable -> 0x00e2 }
                com.uc.webview.export.internal.c.a.a.a((android.content.Context) r0)     // Catch:{ Throwable -> 0x00e2 }
            L_0x0020:
                com.uc.webview.export.internal.c.a.a r2 = com.uc.webview.export.internal.c.a.a.a     // Catch:{ Throwable -> 0x00e2 }
                r0 = 10006(0x2716, float:1.4021E-41)
                r3 = 2
                java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x00e2 }
                r4 = 0
                java.lang.String r5 = "stat"
                r3[r4] = r5     // Catch:{ Throwable -> 0x00e2 }
                r4 = 1
                r5 = 1
                java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)     // Catch:{ Throwable -> 0x00e2 }
                r3[r4] = r5     // Catch:{ Throwable -> 0x00e2 }
                java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r0, (java.lang.Object[]) r3)     // Catch:{ Throwable -> 0x00e2 }
                java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Throwable -> 0x00e2 }
                boolean r0 = r0.booleanValue()     // Catch:{ Throwable -> 0x00e2 }
                if (r0 == 0) goto L_0x0045
                boolean r0 = com.uc.webview.export.internal.d.f     // Catch:{ Throwable -> 0x00e2 }
                if (r0 == 0) goto L_0x0046
            L_0x0045:
                return
            L_0x0046:
                java.util.Date r3 = new java.util.Date     // Catch:{ Throwable -> 0x00e2 }
                long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00e2 }
                r3.<init>(r4)     // Catch:{ Throwable -> 0x00e2 }
                r0 = 10010(0x271a, float:1.4027E-41)
                r4 = 0
                java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x00e2 }
                java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r0, (java.lang.Object[]) r4)     // Catch:{ Throwable -> 0x00e2 }
                java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Throwable -> 0x00e2 }
                boolean r0 = r0.booleanValue()     // Catch:{ Throwable -> 0x00e2 }
                if (r0 == 0) goto L_0x00e5
                r0 = 10020(0x2724, float:1.4041E-41)
                r1 = 0
                java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x00e2 }
                java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r0, (java.lang.Object[]) r1)     // Catch:{ Throwable -> 0x00e2 }
                java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Throwable -> 0x00e2 }
                int r0 = r0.intValue()     // Catch:{ Throwable -> 0x00e2 }
            L_0x006f:
                if (r0 == r6) goto L_0x0078
                if (r0 == 0) goto L_0x0078
                int r0 = r0 * 10
                int r1 = com.uc.webview.export.internal.d.l     // Catch:{ Throwable -> 0x00e2 }
                int r0 = r0 + r1
            L_0x0078:
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00e2 }
                r1.<init>()     // Catch:{ Throwable -> 0x00e2 }
                java.text.SimpleDateFormat r4 = r2.f     // Catch:{ Throwable -> 0x00e2 }
                java.lang.String r4 = r4.format(r3)     // Catch:{ Throwable -> 0x00e2 }
                java.lang.StringBuilder r1 = r1.append(r4)     // Catch:{ Throwable -> 0x00e2 }
                java.lang.String r4 = "~"
                java.lang.StringBuilder r1 = r1.append(r4)     // Catch:{ Throwable -> 0x00e2 }
                java.lang.StringBuilder r0 = r1.append(r0)     // Catch:{ Throwable -> 0x00e2 }
                java.lang.String r1 = r0.toString()     // Catch:{ Throwable -> 0x00e2 }
                java.lang.Object r4 = r2.h     // Catch:{ Throwable -> 0x00e2 }
                monitor-enter(r4)     // Catch:{ Throwable -> 0x00e2 }
                java.util.Map<java.lang.String, com.uc.webview.export.internal.c.a.a$a> r0 = r2.d     // Catch:{ all -> 0x00df }
                if (r0 != 0) goto L_0x00a4
                java.util.HashMap r0 = new java.util.HashMap     // Catch:{ all -> 0x00df }
                r0.<init>()     // Catch:{ all -> 0x00df }
                r2.d = r0     // Catch:{ all -> 0x00df }
            L_0x00a4:
                java.util.Map<java.lang.String, com.uc.webview.export.internal.c.a.a$a> r0 = r2.d     // Catch:{ all -> 0x00df }
                java.lang.Object r0 = r0.get(r1)     // Catch:{ all -> 0x00df }
                com.uc.webview.export.internal.c.a.a$a r0 = (com.uc.webview.export.internal.c.a.a.C0009a) r0     // Catch:{ all -> 0x00df }
                if (r0 != 0) goto L_0x0102
                com.uc.webview.export.internal.c.a.a$a r0 = new com.uc.webview.export.internal.c.a.a$a     // Catch:{ all -> 0x00df }
                r5 = 0
                r0.<init>(r2, r5)     // Catch:{ all -> 0x00df }
                java.util.Map<java.lang.String, com.uc.webview.export.internal.c.a.a$a> r5 = r2.d     // Catch:{ all -> 0x00df }
                r5.put(r1, r0)     // Catch:{ all -> 0x00df }
                r1 = r0
            L_0x00ba:
                java.util.Map<java.lang.String, java.lang.String> r0 = r1.b     // Catch:{ all -> 0x00df }
                java.lang.String r5 = "tm"
                java.text.SimpleDateFormat r2 = r2.g     // Catch:{ all -> 0x00df }
                java.lang.String r2 = r2.format(r3)     // Catch:{ all -> 0x00df }
                r0.put(r5, r2)     // Catch:{ all -> 0x00df }
                java.util.Map<java.lang.String, java.lang.Integer> r0 = r1.a     // Catch:{ all -> 0x00df }
                java.lang.Object r0 = r0.get(r7)     // Catch:{ all -> 0x00df }
                java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ all -> 0x00df }
                if (r0 != 0) goto L_0x00e7
                java.util.Map<java.lang.String, java.lang.Integer> r0 = r1.a     // Catch:{ all -> 0x00df }
                r1 = 1
                java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x00df }
                r0.put(r7, r1)     // Catch:{ all -> 0x00df }
            L_0x00dc:
                monitor-exit(r4)     // Catch:{ all -> 0x00df }
                goto L_0x0045
            L_0x00df:
                r0 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x00df }
                throw r0     // Catch:{ Throwable -> 0x00e2 }
            L_0x00e2:
                r0 = move-exception
                goto L_0x0045
            L_0x00e5:
                r0 = r1
                goto L_0x006f
            L_0x00e7:
                java.util.Map<java.lang.String, java.lang.Integer> r1 = r1.a     // Catch:{ all -> 0x00df }
                int r0 = r0.intValue()     // Catch:{ all -> 0x00df }
                int r0 = r0 + 1
                java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x00df }
                r1.put(r7, r0)     // Catch:{ all -> 0x00df }
                goto L_0x00dc
            L_0x00f7:
                java.lang.String r0 = "SDKWaStat"
                java.lang.String r1 = "stat>>WaStatImp not inited"
                com.uc.webview.export.internal.utility.Log.w(r0, r1)     // Catch:{ Throwable -> 0x00e2 }
                goto L_0x0045
            L_0x0102:
                r1 = r0
                goto L_0x00ba
            */
            throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(java.lang.String):void");
        }

        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static void statAdd(java.lang.String r7, int r8) {
            /*
                r6 = 2
                r1 = 0
                com.uc.webview.export.internal.c.a.a r0 = com.uc.webview.export.internal.c.a.a.a     // Catch:{ Throwable -> 0x00e1 }
                if (r0 != 0) goto L_0x000f
                android.content.Context r0 = com.uc.webview.export.internal.d.e     // Catch:{ Throwable -> 0x00e1 }
                if (r0 == 0) goto L_0x000f
                android.content.Context r0 = com.uc.webview.export.internal.d.e     // Catch:{ Throwable -> 0x00e1 }
                com.uc.webview.export.internal.c.a.a.a((android.content.Context) r0)     // Catch:{ Throwable -> 0x00e1 }
            L_0x000f:
                com.uc.webview.export.internal.c.a.a r0 = com.uc.webview.export.internal.c.a.a.a     // Catch:{ Throwable -> 0x00e1 }
                if (r0 == 0) goto L_0x00f5
                com.uc.webview.export.internal.c.a.a r0 = com.uc.webview.export.internal.c.a.a.a     // Catch:{ Throwable -> 0x00e1 }
                if (r0 != 0) goto L_0x0020
                android.content.Context r0 = com.uc.webview.export.internal.d.e     // Catch:{ Throwable -> 0x00e1 }
                if (r0 == 0) goto L_0x0020
                android.content.Context r0 = com.uc.webview.export.internal.d.e     // Catch:{ Throwable -> 0x00e1 }
                com.uc.webview.export.internal.c.a.a.a((android.content.Context) r0)     // Catch:{ Throwable -> 0x00e1 }
            L_0x0020:
                com.uc.webview.export.internal.c.a.a r2 = com.uc.webview.export.internal.c.a.a.a     // Catch:{ Throwable -> 0x00e1 }
                r0 = 10006(0x2716, float:1.4021E-41)
                r3 = 2
                java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x00e1 }
                r4 = 0
                java.lang.String r5 = "stat"
                r3[r4] = r5     // Catch:{ Throwable -> 0x00e1 }
                r4 = 1
                r5 = 1
                java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)     // Catch:{ Throwable -> 0x00e1 }
                r3[r4] = r5     // Catch:{ Throwable -> 0x00e1 }
                java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r0, (java.lang.Object[]) r3)     // Catch:{ Throwable -> 0x00e1 }
                java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Throwable -> 0x00e1 }
                boolean r0 = r0.booleanValue()     // Catch:{ Throwable -> 0x00e1 }
                if (r0 == 0) goto L_0x0045
                boolean r0 = com.uc.webview.export.internal.d.f     // Catch:{ Throwable -> 0x00e1 }
                if (r0 == 0) goto L_0x0046
            L_0x0045:
                return
            L_0x0046:
                java.util.Date r3 = new java.util.Date     // Catch:{ Throwable -> 0x00e1 }
                long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00e1 }
                r3.<init>(r4)     // Catch:{ Throwable -> 0x00e1 }
                r0 = 10010(0x271a, float:1.4027E-41)
                r4 = 0
                java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x00e1 }
                java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r0, (java.lang.Object[]) r4)     // Catch:{ Throwable -> 0x00e1 }
                java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Throwable -> 0x00e1 }
                boolean r0 = r0.booleanValue()     // Catch:{ Throwable -> 0x00e1 }
                if (r0 == 0) goto L_0x00e4
                r0 = 10020(0x2724, float:1.4041E-41)
                r1 = 0
                java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x00e1 }
                java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r0, (java.lang.Object[]) r1)     // Catch:{ Throwable -> 0x00e1 }
                java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Throwable -> 0x00e1 }
                int r0 = r0.intValue()     // Catch:{ Throwable -> 0x00e1 }
            L_0x006f:
                if (r0 == r6) goto L_0x0078
                if (r0 == 0) goto L_0x0078
                int r0 = r0 * 10
                int r1 = com.uc.webview.export.internal.d.l     // Catch:{ Throwable -> 0x00e1 }
                int r0 = r0 + r1
            L_0x0078:
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00e1 }
                r1.<init>()     // Catch:{ Throwable -> 0x00e1 }
                java.text.SimpleDateFormat r4 = r2.f     // Catch:{ Throwable -> 0x00e1 }
                java.lang.String r4 = r4.format(r3)     // Catch:{ Throwable -> 0x00e1 }
                java.lang.StringBuilder r1 = r1.append(r4)     // Catch:{ Throwable -> 0x00e1 }
                java.lang.String r4 = "~"
                java.lang.StringBuilder r1 = r1.append(r4)     // Catch:{ Throwable -> 0x00e1 }
                java.lang.StringBuilder r0 = r1.append(r0)     // Catch:{ Throwable -> 0x00e1 }
                java.lang.String r1 = r0.toString()     // Catch:{ Throwable -> 0x00e1 }
                java.lang.Object r4 = r2.h     // Catch:{ Throwable -> 0x00e1 }
                monitor-enter(r4)     // Catch:{ Throwable -> 0x00e1 }
                java.util.Map<java.lang.String, com.uc.webview.export.internal.c.a.a$a> r0 = r2.d     // Catch:{ all -> 0x00de }
                if (r0 != 0) goto L_0x00a4
                java.util.HashMap r0 = new java.util.HashMap     // Catch:{ all -> 0x00de }
                r0.<init>()     // Catch:{ all -> 0x00de }
                r2.d = r0     // Catch:{ all -> 0x00de }
            L_0x00a4:
                java.util.Map<java.lang.String, com.uc.webview.export.internal.c.a.a$a> r0 = r2.d     // Catch:{ all -> 0x00de }
                java.lang.Object r0 = r0.get(r1)     // Catch:{ all -> 0x00de }
                com.uc.webview.export.internal.c.a.a$a r0 = (com.uc.webview.export.internal.c.a.a.C0009a) r0     // Catch:{ all -> 0x00de }
                if (r0 != 0) goto L_0x0100
                com.uc.webview.export.internal.c.a.a$a r0 = new com.uc.webview.export.internal.c.a.a$a     // Catch:{ all -> 0x00de }
                r5 = 0
                r0.<init>(r2, r5)     // Catch:{ all -> 0x00de }
                java.util.Map<java.lang.String, com.uc.webview.export.internal.c.a.a$a> r5 = r2.d     // Catch:{ all -> 0x00de }
                r5.put(r1, r0)     // Catch:{ all -> 0x00de }
                r1 = r0
            L_0x00ba:
                java.util.Map<java.lang.String, java.lang.String> r0 = r1.b     // Catch:{ all -> 0x00de }
                java.lang.String r5 = "tm"
                java.text.SimpleDateFormat r2 = r2.g     // Catch:{ all -> 0x00de }
                java.lang.String r2 = r2.format(r3)     // Catch:{ all -> 0x00de }
                r0.put(r5, r2)     // Catch:{ all -> 0x00de }
                java.util.Map<java.lang.String, java.lang.Integer> r0 = r1.a     // Catch:{ all -> 0x00de }
                java.lang.Object r0 = r0.get(r7)     // Catch:{ all -> 0x00de }
                java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ all -> 0x00de }
                if (r0 != 0) goto L_0x00e6
                java.util.Map<java.lang.String, java.lang.Integer> r0 = r1.a     // Catch:{ all -> 0x00de }
                java.lang.Integer r1 = java.lang.Integer.valueOf(r8)     // Catch:{ all -> 0x00de }
                r0.put(r7, r1)     // Catch:{ all -> 0x00de }
            L_0x00db:
                monitor-exit(r4)     // Catch:{ all -> 0x00de }
                goto L_0x0045
            L_0x00de:
                r0 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x00de }
                throw r0     // Catch:{ Throwable -> 0x00e1 }
            L_0x00e1:
                r0 = move-exception
                goto L_0x0045
            L_0x00e4:
                r0 = r1
                goto L_0x006f
            L_0x00e6:
                java.util.Map<java.lang.String, java.lang.Integer> r1 = r1.a     // Catch:{ all -> 0x00de }
                int r0 = r0.intValue()     // Catch:{ all -> 0x00de }
                int r0 = r0 + r8
                java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x00de }
                r1.put(r7, r0)     // Catch:{ all -> 0x00de }
                goto L_0x00db
            L_0x00f5:
                java.lang.String r0 = "SDKWaStat"
                java.lang.String r1 = "stat>>WaStatImp not inited"
                com.uc.webview.export.internal.utility.Log.w(r0, r1)     // Catch:{ Throwable -> 0x00e1 }
                goto L_0x0045
            L_0x0100:
                r1 = r0
                goto L_0x00ba
            */
            throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.interfaces.IWaStat.WaStat.statAdd(java.lang.String, int):void");
        }

        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static void stat(java.lang.String r7, java.lang.String r8) {
            /*
                r6 = 2
                r1 = 0
                com.uc.webview.export.internal.c.a.a r0 = com.uc.webview.export.internal.c.a.a.a     // Catch:{ Throwable -> 0x00d2 }
                if (r0 != 0) goto L_0x000f
                android.content.Context r0 = com.uc.webview.export.internal.d.e     // Catch:{ Throwable -> 0x00d2 }
                if (r0 == 0) goto L_0x000f
                android.content.Context r0 = com.uc.webview.export.internal.d.e     // Catch:{ Throwable -> 0x00d2 }
                com.uc.webview.export.internal.c.a.a.a((android.content.Context) r0)     // Catch:{ Throwable -> 0x00d2 }
            L_0x000f:
                com.uc.webview.export.internal.c.a.a r0 = com.uc.webview.export.internal.c.a.a.a     // Catch:{ Throwable -> 0x00d2 }
                if (r0 == 0) goto L_0x00d7
                com.uc.webview.export.internal.c.a.a r0 = com.uc.webview.export.internal.c.a.a.a     // Catch:{ Throwable -> 0x00d2 }
                if (r0 != 0) goto L_0x0020
                android.content.Context r0 = com.uc.webview.export.internal.d.e     // Catch:{ Throwable -> 0x00d2 }
                if (r0 == 0) goto L_0x0020
                android.content.Context r0 = com.uc.webview.export.internal.d.e     // Catch:{ Throwable -> 0x00d2 }
                com.uc.webview.export.internal.c.a.a.a((android.content.Context) r0)     // Catch:{ Throwable -> 0x00d2 }
            L_0x0020:
                com.uc.webview.export.internal.c.a.a r2 = com.uc.webview.export.internal.c.a.a.a     // Catch:{ Throwable -> 0x00d2 }
                r0 = 10006(0x2716, float:1.4021E-41)
                r3 = 2
                java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x00d2 }
                r4 = 0
                java.lang.String r5 = "stat"
                r3[r4] = r5     // Catch:{ Throwable -> 0x00d2 }
                r4 = 1
                r5 = 1
                java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)     // Catch:{ Throwable -> 0x00d2 }
                r3[r4] = r5     // Catch:{ Throwable -> 0x00d2 }
                java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r0, (java.lang.Object[]) r3)     // Catch:{ Throwable -> 0x00d2 }
                java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Throwable -> 0x00d2 }
                boolean r0 = r0.booleanValue()     // Catch:{ Throwable -> 0x00d2 }
                if (r0 == 0) goto L_0x0045
                boolean r0 = com.uc.webview.export.internal.d.f     // Catch:{ Throwable -> 0x00d2 }
                if (r0 == 0) goto L_0x0046
            L_0x0045:
                return
            L_0x0046:
                java.util.Date r3 = new java.util.Date     // Catch:{ Throwable -> 0x00d2 }
                long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00d2 }
                r3.<init>(r4)     // Catch:{ Throwable -> 0x00d2 }
                r0 = 10010(0x271a, float:1.4027E-41)
                r4 = 0
                java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x00d2 }
                java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r0, (java.lang.Object[]) r4)     // Catch:{ Throwable -> 0x00d2 }
                java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Throwable -> 0x00d2 }
                boolean r0 = r0.booleanValue()     // Catch:{ Throwable -> 0x00d2 }
                if (r0 == 0) goto L_0x00d5
                r0 = 10020(0x2724, float:1.4041E-41)
                r1 = 0
                java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x00d2 }
                java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r0, (java.lang.Object[]) r1)     // Catch:{ Throwable -> 0x00d2 }
                java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Throwable -> 0x00d2 }
                int r0 = r0.intValue()     // Catch:{ Throwable -> 0x00d2 }
            L_0x006f:
                if (r0 == r6) goto L_0x0078
                if (r0 == 0) goto L_0x0078
                int r0 = r0 * 10
                int r1 = com.uc.webview.export.internal.d.l     // Catch:{ Throwable -> 0x00d2 }
                int r0 = r0 + r1
            L_0x0078:
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00d2 }
                r1.<init>()     // Catch:{ Throwable -> 0x00d2 }
                java.text.SimpleDateFormat r4 = r2.f     // Catch:{ Throwable -> 0x00d2 }
                java.lang.String r4 = r4.format(r3)     // Catch:{ Throwable -> 0x00d2 }
                java.lang.StringBuilder r1 = r1.append(r4)     // Catch:{ Throwable -> 0x00d2 }
                java.lang.String r4 = "~"
                java.lang.StringBuilder r1 = r1.append(r4)     // Catch:{ Throwable -> 0x00d2 }
                java.lang.StringBuilder r0 = r1.append(r0)     // Catch:{ Throwable -> 0x00d2 }
                java.lang.String r1 = r0.toString()     // Catch:{ Throwable -> 0x00d2 }
                java.lang.Object r4 = r2.h     // Catch:{ Throwable -> 0x00d2 }
                monitor-enter(r4)     // Catch:{ Throwable -> 0x00d2 }
                java.util.Map<java.lang.String, com.uc.webview.export.internal.c.a.a$a> r0 = r2.d     // Catch:{ all -> 0x00cf }
                if (r0 != 0) goto L_0x00a4
                java.util.HashMap r0 = new java.util.HashMap     // Catch:{ all -> 0x00cf }
                r0.<init>()     // Catch:{ all -> 0x00cf }
                r2.d = r0     // Catch:{ all -> 0x00cf }
            L_0x00a4:
                java.util.Map<java.lang.String, com.uc.webview.export.internal.c.a.a$a> r0 = r2.d     // Catch:{ all -> 0x00cf }
                java.lang.Object r0 = r0.get(r1)     // Catch:{ all -> 0x00cf }
                com.uc.webview.export.internal.c.a.a$a r0 = (com.uc.webview.export.internal.c.a.a.C0009a) r0     // Catch:{ all -> 0x00cf }
                if (r0 != 0) goto L_0x00b9
                com.uc.webview.export.internal.c.a.a$a r0 = new com.uc.webview.export.internal.c.a.a$a     // Catch:{ all -> 0x00cf }
                r5 = 0
                r0.<init>(r2, r5)     // Catch:{ all -> 0x00cf }
                java.util.Map<java.lang.String, com.uc.webview.export.internal.c.a.a$a> r5 = r2.d     // Catch:{ all -> 0x00cf }
                r5.put(r1, r0)     // Catch:{ all -> 0x00cf }
            L_0x00b9:
                java.util.Map<java.lang.String, java.lang.String> r1 = r0.b     // Catch:{ all -> 0x00cf }
                java.lang.String r5 = "tm"
                java.text.SimpleDateFormat r2 = r2.g     // Catch:{ all -> 0x00cf }
                java.lang.String r2 = r2.format(r3)     // Catch:{ all -> 0x00cf }
                r1.put(r5, r2)     // Catch:{ all -> 0x00cf }
                java.util.Map<java.lang.String, java.lang.String> r0 = r0.b     // Catch:{ all -> 0x00cf }
                r0.put(r7, r8)     // Catch:{ all -> 0x00cf }
                monitor-exit(r4)     // Catch:{ all -> 0x00cf }
                goto L_0x0045
            L_0x00cf:
                r0 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x00cf }
                throw r0     // Catch:{ Throwable -> 0x00d2 }
            L_0x00d2:
                r0 = move-exception
                goto L_0x0045
            L_0x00d5:
                r0 = r1
                goto L_0x006f
            L_0x00d7:
                java.lang.String r0 = "SDKWaStat"
                java.lang.String r1 = "stat>>WaStatImp not inited"
                com.uc.webview.export.internal.utility.Log.w(r0, r1)     // Catch:{ Throwable -> 0x00d2 }
                goto L_0x0045
            */
            throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(java.lang.String, java.lang.String):void");
        }

        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static void stat(android.util.Pair<java.lang.String, java.util.HashMap<java.lang.String, java.lang.String>> r6) {
            /*
                com.uc.webview.export.internal.c.a.a r0 = com.uc.webview.export.internal.c.a.a.a     // Catch:{ Throwable -> 0x0071 }
                if (r0 != 0) goto L_0x000d
                android.content.Context r0 = com.uc.webview.export.internal.d.e     // Catch:{ Throwable -> 0x0071 }
                if (r0 == 0) goto L_0x000d
                android.content.Context r0 = com.uc.webview.export.internal.d.e     // Catch:{ Throwable -> 0x0071 }
                com.uc.webview.export.internal.c.a.a.a((android.content.Context) r0)     // Catch:{ Throwable -> 0x0071 }
            L_0x000d:
                com.uc.webview.export.internal.c.a.a r0 = com.uc.webview.export.internal.c.a.a.a     // Catch:{ Throwable -> 0x0071 }
                if (r0 == 0) goto L_0x00de
                com.uc.webview.export.internal.c.a.a r0 = com.uc.webview.export.internal.c.a.a.a     // Catch:{ Throwable -> 0x0071 }
                if (r0 != 0) goto L_0x001e
                android.content.Context r0 = com.uc.webview.export.internal.d.e     // Catch:{ Throwable -> 0x0071 }
                if (r0 == 0) goto L_0x001e
                android.content.Context r0 = com.uc.webview.export.internal.d.e     // Catch:{ Throwable -> 0x0071 }
                com.uc.webview.export.internal.c.a.a.a((android.content.Context) r0)     // Catch:{ Throwable -> 0x0071 }
            L_0x001e:
                com.uc.webview.export.internal.c.a.a r2 = com.uc.webview.export.internal.c.a.a.a     // Catch:{ Throwable -> 0x0071 }
                boolean r0 = com.uc.webview.export.internal.utility.Log.sPrintLog     // Catch:{ Throwable -> 0x0071 }
                if (r0 == 0) goto L_0x007d
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0071 }
                r3.<init>()     // Catch:{ Throwable -> 0x0071 }
                java.lang.Object r0 = r6.first     // Catch:{ Throwable -> 0x0071 }
                java.lang.String r0 = (java.lang.String) r0     // Catch:{ Throwable -> 0x0071 }
                java.lang.String r1 = "ev_ac="
                java.lang.StringBuilder r1 = r3.append(r1)     // Catch:{ Throwable -> 0x0071 }
                r1.append(r0)     // Catch:{ Throwable -> 0x0071 }
                java.lang.Object r0 = r6.second     // Catch:{ Throwable -> 0x0071 }
                java.util.HashMap r0 = (java.util.HashMap) r0     // Catch:{ Throwable -> 0x0071 }
                java.util.Set r0 = r0.entrySet()     // Catch:{ Throwable -> 0x0071 }
                java.util.Iterator r4 = r0.iterator()     // Catch:{ Throwable -> 0x0071 }
            L_0x0043:
                boolean r0 = r4.hasNext()     // Catch:{ Throwable -> 0x0071 }
                if (r0 == 0) goto L_0x0073
                java.lang.Object r0 = r4.next()     // Catch:{ Throwable -> 0x0071 }
                java.util.Map$Entry r0 = (java.util.Map.Entry) r0     // Catch:{ Throwable -> 0x0071 }
                java.lang.Object r1 = r0.getKey()     // Catch:{ Throwable -> 0x0071 }
                java.lang.String r1 = (java.lang.String) r1     // Catch:{ Throwable -> 0x0071 }
                java.lang.Object r0 = r0.getValue()     // Catch:{ Throwable -> 0x0071 }
                java.lang.String r0 = (java.lang.String) r0     // Catch:{ Throwable -> 0x0071 }
                java.lang.String r5 = "`"
                java.lang.StringBuilder r5 = r3.append(r5)     // Catch:{ Throwable -> 0x0071 }
                java.lang.StringBuilder r1 = r5.append(r1)     // Catch:{ Throwable -> 0x0071 }
                java.lang.String r5 = "="
                java.lang.StringBuilder r1 = r1.append(r5)     // Catch:{ Throwable -> 0x0071 }
                r1.append(r0)     // Catch:{ Throwable -> 0x0071 }
                goto L_0x0043
            L_0x0071:
                r0 = move-exception
            L_0x0072:
                return
            L_0x0073:
                java.lang.String r0 = "SDKWaStat"
                java.lang.String r1 = r3.toString()     // Catch:{ Throwable -> 0x0071 }
                com.uc.webview.export.internal.utility.Log.d(r0, r1)     // Catch:{ Throwable -> 0x0071 }
            L_0x007d:
                r0 = 10006(0x2716, float:1.4021E-41)
                r1 = 2
                java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x0071 }
                r3 = 0
                java.lang.String r4 = "stat"
                r1[r3] = r4     // Catch:{ Throwable -> 0x0071 }
                r3 = 1
                r4 = 1
                java.lang.Boolean r4 = java.lang.Boolean.valueOf(r4)     // Catch:{ Throwable -> 0x0071 }
                r1[r3] = r4     // Catch:{ Throwable -> 0x0071 }
                java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r0, (java.lang.Object[]) r1)     // Catch:{ Throwable -> 0x0071 }
                java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Throwable -> 0x0071 }
                boolean r0 = r0.booleanValue()     // Catch:{ Throwable -> 0x0071 }
                if (r0 == 0) goto L_0x0072
                boolean r0 = com.uc.webview.export.internal.d.f     // Catch:{ Throwable -> 0x0071 }
                if (r0 != 0) goto L_0x0072
                java.lang.Object r3 = r2.h     // Catch:{ Throwable -> 0x0071 }
                monitor-enter(r3)     // Catch:{ Throwable -> 0x0071 }
                java.util.List<com.uc.webview.export.internal.c.a.a$b> r0 = r2.e     // Catch:{ all -> 0x00db }
                if (r0 != 0) goto L_0x00ae
                java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ all -> 0x00db }
                r0.<init>()     // Catch:{ all -> 0x00db }
                r2.e = r0     // Catch:{ all -> 0x00db }
            L_0x00ae:
                java.util.Date r1 = new java.util.Date     // Catch:{ all -> 0x00db }
                long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00db }
                r1.<init>(r4)     // Catch:{ all -> 0x00db }
                java.lang.Object r0 = r6.second     // Catch:{ all -> 0x00db }
                java.util.HashMap r0 = (java.util.HashMap) r0     // Catch:{ all -> 0x00db }
                java.lang.String r4 = "tm"
                java.text.SimpleDateFormat r5 = r2.g     // Catch:{ all -> 0x00db }
                java.lang.String r1 = r5.format(r1)     // Catch:{ all -> 0x00db }
                r0.put(r4, r1)     // Catch:{ all -> 0x00db }
                java.util.List<com.uc.webview.export.internal.c.a.a$b> r4 = r2.e     // Catch:{ all -> 0x00db }
                com.uc.webview.export.internal.c.a.a$b r5 = new com.uc.webview.export.internal.c.a.a$b     // Catch:{ all -> 0x00db }
                java.lang.Object r0 = r6.first     // Catch:{ all -> 0x00db }
                java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x00db }
                java.lang.Object r1 = r6.second     // Catch:{ all -> 0x00db }
                java.util.Map r1 = (java.util.Map) r1     // Catch:{ all -> 0x00db }
                r5.<init>(r0, r1)     // Catch:{ all -> 0x00db }
                r4.add(r5)     // Catch:{ all -> 0x00db }
                monitor-exit(r3)     // Catch:{ all -> 0x00db }
                goto L_0x0072
            L_0x00db:
                r0 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x00db }
                throw r0     // Catch:{ Throwable -> 0x0071 }
            L_0x00de:
                java.lang.String r0 = "SDKWaStat"
                java.lang.String r1 = "stat>>WaStatImp not inited"
                com.uc.webview.export.internal.utility.Log.w(r0, r1)     // Catch:{ Throwable -> 0x0071 }
                goto L_0x0072
            */
            throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(android.util.Pair):void");
        }

        public static void saveData() {
            if (a.a == null && d.e != null) {
                a.a(d.e);
            }
            if (a.a != null) {
                if (a.a == null && d.e != null) {
                    a.a(d.e);
                }
                a aVar = a.a;
                if (!d.f) {
                    if (((Boolean) d.a(10006, "stat", true)).booleanValue()) {
                        try {
                            new b(aVar).start();
                        } catch (Exception e) {
                            Log.e("SDKWaStat", "saveData", e);
                        }
                    }
                }
            }
        }
    }
}
