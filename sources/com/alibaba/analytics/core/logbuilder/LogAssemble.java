package com.alibaba.analytics.core.logbuilder;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import com.alibaba.analytics.AnalyticsImp;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.UTSampleConfBiz;
import com.alibaba.analytics.core.device.Device;
import com.alibaba.analytics.core.device.DeviceInfo;
import com.alibaba.analytics.core.model.Log;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.core.model.UTMCLogFields;
import com.alibaba.analytics.core.network.NetworkUtil;
import com.alibaba.analytics.core.store.LogStoreMgr;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.MapUtils;
import com.alibaba.analytics.utils.PhoneInfoUtils;
import com.alibaba.analytics.utils.StringUtils;
import com.alibaba.analytics.utils.UTMCDevice;
import com.alibaba.analytics.version.UTBuildInfo;
import com.taobao.alimama.global.Constants;
import java.util.HashMap;
import java.util.Map;

public class LogAssemble {
    private static final int LOG_MAX_LENGHTH = 40960;
    private static final int LOG_TRUNC_SEND_LENGHTH = 1024;
    private static volatile String s_bssid = null;
    private static volatile String s_mac_str = null;

    private static String checkField(String pField) {
        if (StringUtils.isEmpty(pField)) {
            return "-";
        }
        return pField;
    }

    private static void copyIfLogMapDoesNotExist(String aKey, Map<String, String> aLogMap, Map<String, String> aDeviceInfoMap) {
        if (!aLogMap.containsKey(aKey) && aDeviceInfoMap.get(aKey) != null) {
            aLogMap.put(aKey, aDeviceInfoMap.get(aKey));
        }
    }

    private static void mergeLogMapAndDeviceInfoMap(Map<String, String> aLogMap, Map<String, String> aDeviceInfoMap) {
        aLogMap.put(LogField.BRAND.toString(), aDeviceInfoMap.get(LogField.BRAND.toString()));
        aLogMap.put(LogField.DEVICE_MODEL.toString(), aDeviceInfoMap.get(LogField.DEVICE_MODEL.toString()));
        aLogMap.put(LogField.RESOLUTION.toString(), aDeviceInfoMap.get(LogField.RESOLUTION.toString()));
        aLogMap.put(LogField.OS.toString(), aDeviceInfoMap.get(LogField.OS.toString()));
        aLogMap.put(LogField.OSVERSION.toString(), aDeviceInfoMap.get(LogField.OSVERSION.toString()));
        aLogMap.put(LogField.UTDID.toString(), aDeviceInfoMap.get(LogField.UTDID.toString()));
        copyIfLogMapDoesNotExist(LogField.IMEI.toString(), aLogMap, aDeviceInfoMap);
        copyIfLogMapDoesNotExist(LogField.IMSI.toString(), aLogMap, aDeviceInfoMap);
        copyIfLogMapDoesNotExist(LogField.APPVERSION.toString(), aLogMap, aDeviceInfoMap);
        copyIfLogMapDoesNotExist(UTMCLogFields.DEVICE_ID.toString(), aLogMap, aDeviceInfoMap);
        copyIfLogMapDoesNotExist(LogField.LANGUAGE.toString(), aLogMap, aDeviceInfoMap);
        copyIfLogMapDoesNotExist(LogField.ACCESS.toString(), aLogMap, aDeviceInfoMap);
        copyIfLogMapDoesNotExist(LogField.ACCESS_SUBTYPE.toString(), aLogMap, aDeviceInfoMap);
        copyIfLogMapDoesNotExist(LogField.CARRIER.toString(), aLogMap, aDeviceInfoMap);
    }

    public static String assemble(Map<String, String> aLogMap) {
        StringBuilder lRerservesBuilder;
        if (aLogMap != null && aLogMap.size() > 0) {
            Context context = Variables.getInstance().getContext();
            if (context == null && AnalyticsImp.getApplication() != null) {
                context = AnalyticsImp.getApplication().getApplicationContext();
            }
            String lHostPackageImei = null;
            String lHostPackageImsi = null;
            DeviceInfo device = Device.getDevice(context);
            if (device != null) {
                lHostPackageImei = device.getImei();
                lHostPackageImsi = device.getImsi();
            }
            if (lHostPackageImei != null && lHostPackageImsi != null && aLogMap.get(LogField.IMEI.toString()) == null && aLogMap.get(LogField.IMSI.toString()) == null) {
                aLogMap.put(LogField.IMEI.toString(), lHostPackageImei);
                aLogMap.put(LogField.IMSI.toString(), lHostPackageImsi);
            }
            if (!StringUtils.isEmpty(Variables.getInstance().getUsernick())) {
                aLogMap.put(LogField.USERNICK.toString(), Variables.getInstance().getUsernick());
            }
            if (!StringUtils.isEmpty(Variables.getInstance().getLongLoginUsernick())) {
                aLogMap.put(LogField.LL_USERNICK.toString(), Variables.getInstance().getLongLoginUsernick());
            }
            if (!StringUtils.isEmpty(Variables.getInstance().getUserid())) {
                aLogMap.put(LogField.USERID.toString(), Variables.getInstance().getUserid());
            }
            if (!StringUtils.isEmpty(Variables.getInstance().getLongLoingUserid())) {
                aLogMap.put(LogField.LL_USERID.toString(), Variables.getInstance().getLongLoingUserid());
            }
            if (!aLogMap.containsKey(LogField.SDKVERSION.toString())) {
                aLogMap.put(LogField.SDKVERSION.toString(), UTBuildInfo.getInstance().getFullSDKVersion());
            }
            if (!aLogMap.containsKey(LogField.APPKEY.toString())) {
                aLogMap.put(LogField.APPKEY.toString(), Variables.getInstance().getAppkey());
            }
            if (!StringUtils.isEmpty(Variables.getInstance().getChannel())) {
                aLogMap.put(LogField.CHANNEL.toString(), Variables.getInstance().getChannel());
            }
            if (!StringUtils.isEmpty(Variables.getInstance().getAppVersion())) {
                aLogMap.put(LogField.APPVERSION.toString(), Variables.getInstance().getAppVersion());
            }
            if (aLogMap.containsKey(LogField.RECORD_TIMESTAMP.toString())) {
                aLogMap.put(LogField.RECORD_TIMESTAMP.toString(), "" + TimeStampAdjustMgr.getInstance().getCurrentMils(aLogMap.get(LogField.RECORD_TIMESTAMP.toString())));
            } else {
                aLogMap.put(LogField.RECORD_TIMESTAMP.toString(), "" + TimeStampAdjustMgr.getInstance().getCurrentMils());
            }
            if (!aLogMap.containsKey(LogField.START_SESSION_TIMESTAMP.toString())) {
                aLogMap.put(LogField.START_SESSION_TIMESTAMP.toString(), "" + SessionTimeAndIndexMgr.getInstance().getSessionTimestamp());
            }
            if (!aLogMap.containsKey(LogField.SDKTYPE.toString())) {
                aLogMap.put(LogField.SDKTYPE.toString(), Constants.getSdkType());
            }
            Map<String, String> lDeviceInfoMap = UTMCDevice.getDeviceInfo(context);
            if (lDeviceInfoMap != null) {
                Map<String, String> lLogMapNew = aLogMap;
                mergeLogMapAndDeviceInfoMap(lLogMapNew, lDeviceInfoMap);
                if (aLogMap.containsKey(UTMCLogFields.ALIYUN_PLATFORM_FLAG.toString())) {
                    lLogMapNew.put(LogField.OS.toString(), "y");
                }
                String lReserves = lLogMapNew.get(LogField.RESERVES.toString());
                if (!StringUtils.isEmpty(lReserves)) {
                    lRerservesBuilder = new StringBuilder(lReserves);
                } else {
                    lRerservesBuilder = new StringBuilder(100);
                }
                if (StringUtils.isEmpty(s_mac_str) && context != null) {
                    try {
                        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") == 0) {
                            s_mac_str = NetworkUtil.getWifiAddress(context);
                        }
                    } catch (Exception e) {
                    }
                }
                if (s_mac_str != null) {
                    if (lRerservesBuilder.length() > 0) {
                        lRerservesBuilder.append(",_mac=").append(s_mac_str);
                    } else {
                        lRerservesBuilder.append("_mac=").append(s_mac_str);
                    }
                    lLogMapNew.remove("_mac");
                }
                String lDeviceId = lLogMapNew.get(UTMCLogFields.DEVICE_ID.toString());
                if (lDeviceId != null) {
                    if (lRerservesBuilder.length() > 0) {
                        lRerservesBuilder.append(",_did=").append(lDeviceId);
                    } else {
                        lRerservesBuilder.append("_did=").append(lDeviceId);
                    }
                    lLogMapNew.remove(UTMCLogFields.DEVICE_ID.toString());
                }
                String umidStr = LogAssembleHelper.getSecurityToken(context);
                if (umidStr != null) {
                    if (lLogMapNew.containsKey(LogField.UTDID.toString()) && umidStr.equals(lLogMapNew.get(LogField.UTDID.toString()))) {
                        umidStr = "utdid";
                    }
                    if (lRerservesBuilder.length() > 0) {
                        lRerservesBuilder.append(",_umid=").append(umidStr);
                    } else {
                        lRerservesBuilder.append("_umid=").append(umidStr);
                    }
                }
                if (s_bssid == null) {
                    String bssID = getBssID(context);
                    if (bssID == null) {
                        s_bssid = "";
                    } else {
                        s_bssid = bssID;
                    }
                }
                if (!StringUtils.isEmpty(s_bssid)) {
                    if (lRerservesBuilder.length() > 0) {
                        lRerservesBuilder.append(",_bssid=").append(s_bssid);
                    } else {
                        lRerservesBuilder.append("_bssid=").append(s_bssid);
                    }
                }
                if (Variables.getInstance().isOldDevice()) {
                    if (lRerservesBuilder.length() > 0) {
                        lRerservesBuilder.append(",_io=").append("1");
                    } else {
                        lRerservesBuilder.append("_io=1");
                    }
                }
                if (Variables.getInstance().isDebugPackage()) {
                    String buildid = Variables.getInstance().getPackageBuildId();
                    if (!TextUtils.isEmpty(buildid)) {
                        if (lRerservesBuilder.length() > 0) {
                            lRerservesBuilder.append(",_buildid=").append(buildid);
                        } else {
                            lRerservesBuilder.append("_buildid=").append(buildid);
                        }
                    }
                }
                if (lRerservesBuilder.length() > 0) {
                    lRerservesBuilder.append(",_timeAdjust=").append(TimeStampAdjustMgr.getInstance().getAdjustFlag() ? "1" : "0");
                } else {
                    lRerservesBuilder.append("_timeAdjust=").append(TimeStampAdjustMgr.getInstance().getAdjustFlag() ? "1" : "0");
                }
                String logAppkey = aLogMap.get(LogField.APPKEY.toString());
                String mainAppkey = Variables.getInstance().getAppkey();
                if (!TextUtils.isEmpty(logAppkey) && !TextUtils.isEmpty(mainAppkey) && !mainAppkey.equalsIgnoreCase(logAppkey)) {
                    if (lRerservesBuilder.length() > 0) {
                        lRerservesBuilder.append(",_mak=").append(mainAppkey);
                    } else {
                        lRerservesBuilder.append("_mak=").append(mainAppkey);
                    }
                }
                String phoneOrPad = UTMCDevice.isPad(Variables.getInstance().getContext()) ? "1" : "0";
                if (lRerservesBuilder.length() > 0) {
                    lRerservesBuilder.append(",_pad=").append(phoneOrPad);
                } else {
                    lRerservesBuilder.append("_pad=").append(phoneOrPad);
                }
                String channel2 = AppInfoUtil.getChannle2ForPreLoadApk(context);
                if (!TextUtils.isEmpty(channel2)) {
                    if (lRerservesBuilder.length() > 0) {
                        lRerservesBuilder.append(",_channel2=").append(channel2);
                    } else {
                        lRerservesBuilder.append("_channel2=").append(channel2);
                    }
                }
                String openid = Variables.getInstance().getOpenid();
                if (!StringUtils.isEmpty(openid)) {
                    if (lRerservesBuilder.length() > 0) {
                        lRerservesBuilder.append(",_openid=").append(openid);
                    } else {
                        lRerservesBuilder.append("_openid=").append(openid);
                    }
                }
                String imeiBySystem = PhoneInfoUtils.getImeiBySystem(context);
                if (!TextUtils.isEmpty(imeiBySystem)) {
                    if (lRerservesBuilder.length() > 0) {
                        lRerservesBuilder.append(",_ie=").append(imeiBySystem);
                    } else {
                        lRerservesBuilder.append("_ie=").append(imeiBySystem);
                    }
                }
                String imsiBySystem = PhoneInfoUtils.getImsiBySystem(context);
                if (!TextUtils.isEmpty(imsiBySystem)) {
                    if (lRerservesBuilder.length() > 0) {
                        lRerservesBuilder.append(",_is=").append(imsiBySystem);
                    } else {
                        lRerservesBuilder.append("_is=").append(imsiBySystem);
                    }
                }
                Map<String, String> lSessionProperties = Variables.getInstance().getSessionProperties();
                if (lSessionProperties != null && lSessionProperties.size() > 0) {
                    String lSessionPropertiesStr = StringUtils.convertMapToStringWithUrlEncoder(lSessionProperties);
                    if (!StringUtils.isEmpty(lSessionPropertiesStr)) {
                        if (lRerservesBuilder.length() > 0) {
                            lRerservesBuilder.append(",").append(lSessionPropertiesStr);
                        } else {
                            lRerservesBuilder.append(lSessionPropertiesStr);
                        }
                    }
                }
                lLogMapNew.put(LogField.RESERVES.toString(), lRerservesBuilder.toString());
                return assembleWithFullFields(lLogMapNew);
            }
        }
        return null;
    }

    public static String assembleWithFullFields(Map<String, String> aLogMap) {
        String logArgs;
        Map<String, String> lLogMapNew = FieldCheck.checkMapFields(aLogMap);
        boolean isTrunc = truncLog(lLogMapNew, LogField.ARG3.toString()) || (truncLog(lLogMapNew, LogField.ARG2.toString()) || truncLog(lLogMapNew, LogField.ARG1.toString()));
        StringBuffer stringBufferLog = new StringBuffer();
        for (LogField lEnumKey : LogField.values()) {
            if (lEnumKey == LogField.ARGS) {
                break;
            }
            String lV = null;
            if (lLogMapNew.containsKey(lEnumKey.toString())) {
                lV = StringUtils.convertObjectToString(lLogMapNew.get(lEnumKey.toString()));
                lLogMapNew.remove(lEnumKey.toString());
            }
            stringBufferLog.append(checkField(lV)).append(com.alibaba.analytics.core.device.Constants.SEPARATOR);
        }
        StringBuffer stringBufferArgs = new StringBuffer();
        boolean lIsFirstArgFlag = true;
        if (lLogMapNew.containsKey(LogField.ARGS.toString())) {
            stringBufferArgs.append(checkField(StringUtils.convertObjectToString(lLogMapNew.get(LogField.ARGS.toString()))));
            lLogMapNew.remove(LogField.ARGS.toString());
            lIsFirstArgFlag = false;
        }
        for (String lKey : lLogMapNew.keySet()) {
            String lV2 = null;
            if (lLogMapNew.containsKey(lKey)) {
                lV2 = StringUtils.convertObjectToString(lLogMapNew.get(lKey));
            }
            if (lIsFirstArgFlag) {
                if ("StackTrace".equals(lKey)) {
                    stringBufferArgs.append("StackTrace=====>").append(lV2);
                } else {
                    stringBufferArgs.append(checkField(lKey)).append("=").append(lV2);
                }
                lIsFirstArgFlag = false;
            } else if ("StackTrace".equals(lKey)) {
                stringBufferArgs.append(",").append("StackTrace=====>").append(lV2);
            } else {
                stringBufferArgs.append(",").append(checkField(lKey)).append("=").append(lV2);
            }
        }
        int stringBufferArgsLength = stringBufferArgs.length();
        if (stringBufferArgsLength < 1) {
            logArgs = "-";
        } else if (stringBufferArgsLength > LOG_MAX_LENGHTH) {
            Logger.e("LogAssemble truncLog", "field", LogField.ARGS.toString(), "length", Integer.valueOf(stringBufferArgsLength));
            logArgs = stringBufferArgs.substring(0, LOG_MAX_LENGHTH);
            isTrunc = true;
        } else {
            logArgs = stringBufferArgs.toString();
        }
        stringBufferLog.append(logArgs);
        String logResult = stringBufferLog.toString();
        if (isTrunc) {
            sendTruncLogEvent(logResult);
        }
        return logResult;
    }

    public static Map<String, String> disassemble(String pLogContent) {
        if (StringUtils.isEmpty(pLogContent)) {
            return null;
        }
        Map<String, String> lDResult = new HashMap<>();
        String[] lSplitResult = getLSplitResult(pLogContent, 34);
        if (lSplitResult == null || lSplitResult.length <= 0) {
            return lDResult;
        }
        int lLIndex = 0;
        for (LogField lEnumKey : LogField.values()) {
            if (lLIndex < lSplitResult.length && lSplitResult[lLIndex] != null) {
                lDResult.put(lEnumKey.toString(), lSplitResult[lLIndex]);
            }
            lLIndex++;
        }
        return lDResult;
    }

    private static String[] getLSplitResult(String aLogContent, int aSplitNumber) {
        String[] result = new String[aSplitNumber];
        int lastIndexPos = 0;
        int i = 0;
        while (true) {
            if (i >= result.length - 1) {
                break;
            }
            int findIndexPos = aLogContent.indexOf(com.alibaba.analytics.core.device.Constants.SEPARATOR, lastIndexPos);
            if (findIndexPos == -1) {
                result[i] = aLogContent.substring(lastIndexPos);
                break;
            }
            result[i] = aLogContent.substring(lastIndexPos, findIndexPos);
            lastIndexPos = findIndexPos + 2;
            i++;
        }
        result[aSplitNumber - 1] = aLogContent.substring(lastIndexPos);
        return result;
    }

    private static String getBssID(Context aContext) {
        try {
            return ((WifiManager) aContext.getSystemService("wifi")).getConnectionInfo().getBSSID();
        } catch (Throwable th) {
            return null;
        }
    }

    public static int getEventId(Map<String, String> lLogMap) {
        try {
            return Integer.parseInt(lLogMap.get(LogField.EVENTID.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String assemble(String page, String eventId, String arg1, String arg2, String arg3, Map<String, String> args, String logIndex, String time) {
        HashMap<String, String> map = new HashMap<>();
        if (args != null) {
            map.putAll(args);
        }
        if (!TextUtils.isEmpty(page)) {
            map.put(LogField.PAGE.toString(), page);
        }
        map.put(LogField.EVENTID.toString(), eventId);
        if (!TextUtils.isEmpty(arg1)) {
            map.put(LogField.ARG1.toString(), arg1);
        }
        if (!TextUtils.isEmpty(arg2)) {
            map.put(LogField.ARG2.toString(), arg2);
        }
        if (!TextUtils.isEmpty(arg3)) {
            map.put(LogField.ARG3.toString(), arg3);
        }
        if (!TextUtils.isEmpty(time)) {
            map.put(LogField.RECORD_TIMESTAMP.toString(), time);
        }
        if (!TextUtils.isEmpty(logIndex)) {
            map.put(LogField.RESERVE3.toString(), logIndex);
        }
        return assemble(map);
    }

    private static boolean truncLog(Map<String, String> lLogMapNew, String field) {
        String logField = lLogMapNew.get(field);
        if (TextUtils.isEmpty(logField) || logField.length() <= LOG_MAX_LENGHTH) {
            return false;
        }
        Logger.e("LogAssemble truncLog", "field", field, "length", Integer.valueOf(logField.length()));
        lLogMapNew.put(field, logField.substring(0, LOG_MAX_LENGHTH));
        return true;
    }

    private static void sendTruncLogEvent(String truncLogString) {
        if (!UTSampleConfBiz.getInstance().isSampleSuccess(Constants.UtEventId.CUSTOM, "TRUNC_LOG")) {
            Logger.d("sendTruncLogEvent", "TRUNC_LOG is discarded!");
            return;
        }
        Map<String, String> logMap = disassemble(truncLogString);
        if (logMap != null) {
            HashMap<String, String> argsMap = new HashMap<>();
            argsMap.put("PN", logMap.get(LogField.PAGE.toString()));
            argsMap.put("EID", logMap.get(LogField.EVENTID.toString()));
            argsMap.put("A1", getSubString(logMap.get(LogField.ARG1.toString())));
            argsMap.put("A2", getSubString(logMap.get(LogField.ARG2.toString())));
            argsMap.put("A3", getSubString(logMap.get(LogField.ARG3.toString())));
            argsMap.put("AS", getSubString(logMap.get(LogField.ARGS.toString())));
            argsMap.put("R3", logMap.get(LogField.RESERVE3.toString()));
            LogStoreMgr.getInstance().add(new Log("UT_ANALYTICS", "19999", "TRUNC_LOG", "", "", MapUtils.convertToUrlEncodedMap(argsMap)));
        }
    }

    private static String getSubString(String truncString) {
        return (TextUtils.isEmpty(truncString) || truncString.length() <= 1024) ? truncString : truncString.substring(0, 1024);
    }
}
