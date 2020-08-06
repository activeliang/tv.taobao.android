package com.alibaba.motu.crashreporter.utrestapi;

import android.content.Context;
import android.os.Build;
import com.alibaba.motu.crashreporter.Constants;
import com.alibaba.motu.crashreporter.LogUtil;
import com.alibaba.motu.crashreporter.utils.StringUtils;
import java.util.HashMap;
import java.util.Map;

public class UTReqDataBuilder {
    private static long s_session_start_timestamp = System.currentTimeMillis();

    private static String _fixVariableValue(String value) {
        if (StringUtils.isBlank(value)) {
            return "-";
        }
        if (value == null || "".equals(value)) {
            return value;
        }
        StringBuilder strNoBlank = new StringBuilder(value.length());
        char[] str = value.toCharArray();
        for (int i = 0; i < str.length; i++) {
            if (!(str[i] == 10 || str[i] == 13 || str[i] == 9 || str[i] == '|')) {
                strNoBlank.append(str[i]);
            }
        }
        return strNoBlank.toString();
    }

    public static UTReqDataBuildResult buildTracePostReqDataObj(Context aContext, Map<String, String> aData, long aTimestamp, String aPage, int aEventId, Object aArg1, Object aArg2, Object aArg3, Map<String, String> extData) {
        if (aEventId == 0) {
            return null;
        }
        try {
            String lRecordTimestamp = "" + (aTimestamp > 0 ? aTimestamp : System.currentTimeMillis());
            String lPage = _fixVariableValue(aPage);
            String lEventId = _fixVariableValue(String.valueOf(aEventId));
            String lArg1 = _fixVariableValue(StringUtils.convertObjectToString(aArg1));
            String lArg2 = _fixVariableValue(StringUtils.convertObjectToString(aArg2));
            String lArg3 = _fixVariableValue(StringUtils.convertObjectToString(aArg3));
            String lArgs = _fixVariableValue(StringUtils.convertMapToString(extData));
            String imei = _fixVariableValue(aData.get("IMEI"));
            String imsi = _fixVariableValue(aData.get("IMSI"));
            String brand = _fixVariableValue(Build.BRAND);
            String _fixVariableValue = _fixVariableValue(aData.get(Constants.CPU));
            String _fixVariableValue2 = _fixVariableValue(imei);
            String deviceModel = _fixVariableValue(Build.MODEL);
            String resolution = _fixVariableValue(aData.get(Constants.RESOLUTION));
            String carrier = _fixVariableValue(aData.get(Constants.CARRIER));
            String access = _fixVariableValue(aData.get(Constants.ACCESS));
            String accessSubType = _fixVariableValue(aData.get(Constants.ACCESS_SUBTYPE));
            String appKey = _fixVariableValue(aData.get(Constants.APP_KEY));
            String appVersion = _fixVariableValue(aData.get(Constants.APP_VERSION));
            String channel = _fixVariableValue(aData.get(Constants.CHANNEL));
            String longLoginUserNick = _fixVariableValue(aData.get(Constants.USERNICK));
            String userNick = _fixVariableValue(aData.get(Constants.USERNICK));
            String _fixVariableValue3 = _fixVariableValue(aData.get(Constants.COUNTRY));
            String language = _fixVariableValue(aData.get(Constants.LANGUAGE));
            String appId = aData.get(Constants.APP_ID);
            String os = "a";
            String osVersion = _fixVariableValue(Build.VERSION.RELEASE);
            String str = "" + s_session_start_timestamp;
            String reserve2 = _fixVariableValue(aData.get(Constants.UTDID));
            if (!StringUtils.isBlank("")) {
                String reserve6 = "";
            }
            if (appId != null && appId.contains("aliyunos")) {
                os = "y";
            }
            Map<String, String> aLogMap = new HashMap<>();
            aLogMap.put(UTFieldsScheme.IMEI.toString(), imei);
            aLogMap.put(UTFieldsScheme.IMSI.toString(), imsi);
            aLogMap.put(UTFieldsScheme.BRAND.toString(), brand);
            aLogMap.put(UTFieldsScheme.DEVICE_MODEL.toString(), deviceModel);
            aLogMap.put(UTFieldsScheme.RESOLUTION.toString(), resolution);
            aLogMap.put(UTFieldsScheme.CARRIER.toString(), carrier);
            aLogMap.put(UTFieldsScheme.ACCESS.toString(), access);
            aLogMap.put(UTFieldsScheme.ACCESS_SUBTYPE.toString(), accessSubType);
            aLogMap.put(UTFieldsScheme.CHANNEL.toString(), channel);
            aLogMap.put(UTFieldsScheme.APPKEY.toString(), appKey);
            aLogMap.put(UTFieldsScheme.APPVERSION.toString(), appVersion);
            aLogMap.put(UTFieldsScheme.LL_USERNICK.toString(), longLoginUserNick);
            aLogMap.put(UTFieldsScheme.USERNICK.toString(), userNick);
            aLogMap.put(UTFieldsScheme.LL_USERID.toString(), "-");
            aLogMap.put(UTFieldsScheme.USERID.toString(), "-");
            aLogMap.put(UTFieldsScheme.LANGUAGE.toString(), language);
            aLogMap.put(UTFieldsScheme.OS.toString(), os);
            aLogMap.put(UTFieldsScheme.OSVERSION.toString(), osVersion);
            aLogMap.put(UTFieldsScheme.SDKVERSION.toString(), "1.0");
            aLogMap.put(UTFieldsScheme.START_SESSION_TIMESTAMP.toString(), "" + s_session_start_timestamp);
            aLogMap.put(UTFieldsScheme.UTDID.toString(), reserve2);
            aLogMap.put(UTFieldsScheme.SDKTYPE.toString(), com.alibaba.analytics.core.Constants.SDK_TYPE);
            aLogMap.put(UTFieldsScheme.RESERVE2.toString(), reserve2);
            aLogMap.put(UTFieldsScheme.RESERVE3.toString(), "-");
            aLogMap.put(UTFieldsScheme.RESERVE4.toString(), "-");
            aLogMap.put(UTFieldsScheme.RESERVE5.toString(), "-");
            aLogMap.put(UTFieldsScheme.RESERVES.toString(), "-");
            aLogMap.put(UTFieldsScheme.RECORD_TIMESTAMP.toString(), lRecordTimestamp);
            aLogMap.put(UTFieldsScheme.PAGE.toString(), lPage);
            aLogMap.put(UTFieldsScheme.EVENTID.toString(), lEventId);
            aLogMap.put(UTFieldsScheme.ARG1.toString(), lArg1);
            aLogMap.put(UTFieldsScheme.ARG2.toString(), lArg2);
            aLogMap.put(UTFieldsScheme.ARG3.toString(), lArg3);
            aLogMap.put(UTFieldsScheme.ARGS.toString(), lArgs);
            Map<String, Object> lBReqData = buildPostRequestMap(assembleWithFullFields(aLogMap));
            UTReqDataBuildResult lResult = new UTReqDataBuildResult();
            lResult.setReqUrl(UTRestUrlWrapper.getSignedTransferUrl(UTConstants.getTransferUrl(), (Map<String, Object>) null, lBReqData, aContext, appKey, channel, appVersion, os, "", reserve2));
            lResult.setPostReqData(lBReqData);
            return lResult;
        } catch (Exception e) {
            LogUtil.e("UTRestAPI buildTracePostReqDataObj catch!", e);
            return null;
        }
    }

    public static String assembleWithFullFields(Map<String, String> aLogMap) {
        Map<String, String> lLogMapNew = aLogMap;
        StringBuffer lSb = new StringBuffer();
        for (UTFieldsScheme lEnumKey : UTFieldsScheme.values()) {
            if (lEnumKey == UTFieldsScheme.ARGS) {
                break;
            }
            String lV = null;
            if (lLogMapNew.containsKey(lEnumKey.toString())) {
                lV = StringUtils.convertObjectToString(lLogMapNew.get(lEnumKey.toString()));
                lLogMapNew.remove(lEnumKey.toString());
            }
            lSb.append(_fixVariableValue(lV)).append(com.alibaba.analytics.core.device.Constants.SEPARATOR);
        }
        boolean lIsFirstArgFlag = true;
        if (lLogMapNew.containsKey(UTFieldsScheme.ARGS.toString())) {
            lSb.append(_fixVariableValue(StringUtils.convertObjectToString(lLogMapNew.get(UTFieldsScheme.ARGS.toString()))));
            lLogMapNew.remove(UTFieldsScheme.ARGS.toString());
            lIsFirstArgFlag = false;
        }
        for (String lKey : lLogMapNew.keySet()) {
            String lV2 = null;
            if (lLogMapNew.containsKey(lKey)) {
                lV2 = StringUtils.convertObjectToString(lLogMapNew.get(lKey));
            }
            if (lIsFirstArgFlag) {
                if ("StackTrace".equals(lKey)) {
                    lSb.append("StackTrace=====>").append(lV2);
                } else {
                    lSb.append(_fixVariableValue(lKey)).append("=").append(lV2);
                }
                lIsFirstArgFlag = false;
            } else if ("StackTrace".equals(lKey)) {
                lSb.append(",").append("StackTrace=====>").append(lV2);
            } else {
                lSb.append(",").append(_fixVariableValue(lKey)).append("=").append(lV2);
            }
        }
        String lLogResult = lSb.toString();
        if (StringUtils.isEmpty(lLogResult) || !lLogResult.endsWith(com.alibaba.analytics.core.device.Constants.SEPARATOR)) {
            return lLogResult;
        }
        return lLogResult + "-";
    }

    public static Map<String, Object> buildPostRequestMap(String lReqData) {
        if (StringUtils.isEmpty(lReqData)) {
            return null;
        }
        Map<String, String> newReqData = new HashMap<>();
        newReqData.put("stm_x", lReqData);
        return buildPostRequestMap(newReqData);
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0067 A[SYNTHETIC, Splitter:B:24:0x0067] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x006c A[Catch:{ Exception -> 0x0070 }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0018 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Map<java.lang.String, java.lang.Object> buildPostRequestMap(java.util.Map<java.lang.String, java.lang.String> r13) {
        /*
            r11 = 0
            if (r13 == 0) goto L_0x0009
            int r12 = r13.size()
            if (r12 > 0) goto L_0x000b
        L_0x0009:
            r3 = r11
        L_0x000a:
            return r3
        L_0x000b:
            java.util.HashMap r3 = new java.util.HashMap     // Catch:{ Exception -> 0x0070 }
            r3.<init>()     // Catch:{ Exception -> 0x0070 }
            java.util.Set r12 = r13.keySet()     // Catch:{ Exception -> 0x0070 }
            java.util.Iterator r1 = r12.iterator()     // Catch:{ Exception -> 0x0070 }
        L_0x0018:
            boolean r12 = r1.hasNext()     // Catch:{ Exception -> 0x0070 }
            if (r12 == 0) goto L_0x000a
            java.lang.Object r9 = r1.next()     // Catch:{ Exception -> 0x0070 }
            java.lang.String r9 = (java.lang.String) r9     // Catch:{ Exception -> 0x0070 }
            java.lang.Object r10 = r13.get(r9)     // Catch:{ Exception -> 0x0070 }
            java.lang.String r10 = (java.lang.String) r10     // Catch:{ Exception -> 0x0070 }
            boolean r12 = com.alibaba.motu.crashreporter.utils.StringUtils.isEmpty(r9)     // Catch:{ Exception -> 0x0070 }
            if (r12 != 0) goto L_0x0018
            boolean r12 = com.alibaba.motu.crashreporter.utils.StringUtils.isEmpty(r10)     // Catch:{ Exception -> 0x0070 }
            if (r12 != 0) goto L_0x0018
            r4 = 0
            r7 = 0
            java.io.ByteArrayOutputStream r5 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0079 }
            r5.<init>()     // Catch:{ IOException -> 0x0079 }
            java.util.zip.GZIPOutputStream r8 = new java.util.zip.GZIPOutputStream     // Catch:{ IOException -> 0x007b }
            r8.<init>(r5)     // Catch:{ IOException -> 0x007b }
            java.lang.String r12 = "UTF-8"
            byte[] r12 = r10.getBytes(r12)     // Catch:{ IOException -> 0x0062 }
            r8.write(r12)     // Catch:{ IOException -> 0x0062 }
            r8.flush()     // Catch:{ IOException -> 0x0062 }
            r8.close()     // Catch:{ IOException -> 0x0062 }
            byte[] r6 = r5.toByteArray()     // Catch:{ IOException -> 0x0062 }
            java.lang.String r12 = com.alibaba.motu.crashreporter.utrestapi.UTConstants.getRC4PrivateKey()     // Catch:{ IOException -> 0x0062 }
            byte[] r2 = com.alibaba.motu.crashreporter.utils.RC4.rc4(r6, r12)     // Catch:{ IOException -> 0x0062 }
            r3.put(r9, r2)     // Catch:{ IOException -> 0x0062 }
            goto L_0x0018
        L_0x0062:
            r0 = move-exception
            r7 = r8
            r4 = r5
        L_0x0065:
            if (r7 == 0) goto L_0x006a
            r7.close()     // Catch:{ Exception -> 0x0070 }
        L_0x006a:
            if (r4 == 0) goto L_0x0018
            r4.close()     // Catch:{ Exception -> 0x0070 }
            goto L_0x0018
        L_0x0070:
            r0 = move-exception
            java.lang.String r12 = "buildPostRequestMap"
            com.alibaba.motu.crashreporter.LogUtil.e(r12, r0)
            r3 = r11
            goto L_0x000a
        L_0x0079:
            r0 = move-exception
            goto L_0x0065
        L_0x007b:
            r0 = move-exception
            r4 = r5
            goto L_0x0065
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.crashreporter.utrestapi.UTReqDataBuilder.buildPostRequestMap(java.util.Map):java.util.Map");
    }
}
