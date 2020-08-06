package com.alibaba.analytics.core.sync;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.timestamp.ConfigTimeStampMgr;
import com.alibaba.analytics.core.device.Constants;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.ByteUtils;
import com.alibaba.analytics.utils.DeviceUtil;
import com.alibaba.analytics.utils.GzipUtils;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.ReflectUtils;
import com.alibaba.analytics.utils.WuaHelper;
import com.alibaba.analytics.utils.ZipDictUtils;
import com.alibaba.analytics.version.UTBuildInfo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

public class BizRequest {
    private static final byte BYTE_ZERO = 0;
    private static final byte FLAGS_GET_CONFIG = 32;
    private static final byte FLAGS_GZIP = 1;
    private static final byte FLAGS_GZIP_FLUSH_DIC = 2;
    private static final byte FLAGS_KEEP_ALIVE = 8;
    private static final byte FLAGS_REAL_TIME_DEBUG = 16;
    private static final int HEAD_LENGTH = 8;
    private static final String LOG_SEPARATE = String.valueOf(1);
    private static final boolean NeedConfigByResponse = true;
    private static final boolean NeedMiniWua = true;
    private static final int PAYLOAD_MAX_LENGTH = 16777216;
    private static final int SplitNumber = 34;
    private static boolean bTestFlowGenterClsExist = false;
    private static Class flowClz = null;
    private static ByteArrayOutputStream mByteArrayOutputStream = null;
    private static GZIPOutputStream mGZIPOutputStream = null;
    static String mMiniWua = null;
    private static int mMiniWuaIndex = 0;
    public static final SelfMonitorEventDispather mMonitor = new SelfMonitorEventDispather();
    private static long mReceivedDataLen = 0;
    static String mResponseAdditionalData = null;

    public static byte[] getPackRequest(Map<String, String> eventMap) throws Exception {
        return getPackRequest(eventMap, 1);
    }

    static byte[] getPackRequestByRealtime(Map<String, String> eventMap) throws Exception {
        return getPackRequest(eventMap, 2);
    }

    static byte[] getPackRequest(Map<String, String> eventMap, int type) throws Exception {
        byte version;
        byte[] payload;
        byte flags;
        if (Variables.getInstance().isGzipUpload() || Variables.getInstance().isHttpService()) {
            payload = GzipUtils.gzip(getPayload(eventMap));
            flags = 1;
            version = 1;
        } else {
            TnetUtil.initTnetStream();
            if (mGZIPOutputStream != null) {
                mGZIPOutputStream.write(getPayloadByDictZip(eventMap));
                mGZIPOutputStream.flush();
                payload = mByteArrayOutputStream.toByteArray();
                mByteArrayOutputStream.reset();
                flags = 2;
            } else {
                payload = GzipUtils.gzip(getPayloadByDictZip(eventMap));
                flags = 1;
            }
            version = 2;
        }
        if (payload == null) {
            return null;
        }
        if (payload.length >= 16777216) {
            if (Variables.getInstance().isSelfMonitorTurnOn()) {
                mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.DATALEN_OVERFLOW, String.valueOf(payload.length), Double.valueOf(1.0d)));
            }
            return null;
        }
        ByteArrayOutputStream baosRequest = new ByteArrayOutputStream();
        baosRequest.write(version);
        baosRequest.write(ByteUtils.intToBytes3(payload.length));
        baosRequest.write(type);
        byte flags2 = (byte) (flags | 8);
        if (Variables.getInstance().isRealTimeDebug()) {
            flags2 = (byte) (flags2 | FLAGS_REAL_TIME_DEBUG);
        }
        baosRequest.write((byte) (flags2 | FLAGS_GET_CONFIG));
        baosRequest.write(0);
        baosRequest.write(0);
        baosRequest.write(payload);
        byte[] byteArray = baosRequest.toByteArray();
        try {
            baosRequest.close();
            return byteArray;
        } catch (IOException e) {
            Logger.e((String) null, e, new Object[0]);
            return byteArray;
        }
    }

    private static byte[] getPayload(Map<String, String> eventMap) throws Exception {
        ByteArrayOutputStream baosPayload = new ByteArrayOutputStream();
        String head = getHead();
        if (head == null || head.length() <= 0) {
            baosPayload.write(ByteUtils.intToBytes2(0));
        } else {
            baosPayload.write(ByteUtils.intToBytes2(head.getBytes().length));
            baosPayload.write(head.getBytes());
        }
        if (eventMap != null && eventMap.size() > 0) {
            for (String key : eventMap.keySet()) {
                baosPayload.write(ByteUtils.intToBytes4(Integer.valueOf(key).intValue()));
                String eventLogs = eventMap.get(key);
                if (eventLogs != null) {
                    baosPayload.write(ByteUtils.intToBytes4(eventLogs.getBytes().length));
                    baosPayload.write(eventLogs.getBytes());
                } else {
                    baosPayload.write(ByteUtils.intToBytes4(0));
                }
            }
        }
        byte[] buf = baosPayload.toByteArray();
        try {
            baosPayload.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf;
    }

    private static byte[] getPayloadByDictZip(Map<String, String> eventMap) throws Exception {
        String[] fields;
        ByteArrayOutputStream baosPayload = new ByteArrayOutputStream();
        baosPayload.write(ZipDictUtils.getHeadBytes(getHead()));
        if (eventMap != null && eventMap.size() > 0) {
            ByteArrayOutputStream baosLogs = new ByteArrayOutputStream();
            for (String key : eventMap.keySet()) {
                baosPayload.write(ZipDictUtils.getLengthBytes(Integer.valueOf(key).intValue()));
                String eventLogs = eventMap.get(key);
                if (eventLogs != null) {
                    String[] arr$ = eventLogs.split(LOG_SEPARATE);
                    int len$ = arr$.length;
                    int i$ = 0;
                    while (true) {
                        int i$2 = i$;
                        if (i$2 >= len$) {
                            break;
                        }
                        String log = arr$[i$2];
                        if (!TextUtils.isEmpty(log) && (fields = getSplitResult(log)) != null && 34 == fields.length) {
                            for (String field : fields) {
                                baosLogs.write(ZipDictUtils.getBytes(field));
                            }
                            baosLogs.write(0);
                        }
                        i$ = i$2 + 1;
                    }
                    baosPayload.write(ZipDictUtils.getLengthBytes(baosLogs.size()));
                    baosPayload.write(baosLogs.toByteArray());
                    baosLogs.reset();
                } else {
                    baosPayload.write(0);
                }
            }
            try {
                baosLogs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] buf = baosPayload.toByteArray();
        try {
            baosPayload.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return buf;
    }

    public static String getHead() {
        String head;
        String appkey = AppInfoUtil.getAppkey();
        Context context = Variables.getInstance().getContext();
        String appVersion = Variables.getInstance().getAppVersion();
        if (appVersion == null) {
            appVersion = "";
        }
        String appVersionSys = "";
        Map<String, String> deviceInfoMap = DeviceUtil.getDeviceInfo(context);
        if (deviceInfoMap != null && (appVersionSys = deviceInfoMap.get(LogField.APPVERSION.toString())) == null) {
            appVersionSys = "";
        }
        String channel = AppInfoUtil.getChannel();
        if (channel == null) {
            channel = "";
        }
        String utdid = "";
        if (deviceInfoMap != null) {
            utdid = deviceInfoMap.get(LogField.UTDID.toString());
        }
        String sdkVersion = UTBuildInfo.getInstance().getFullSDKVersion();
        if (Variables.getInstance().isRealTimeDebug()) {
            head = String.format("ak=%s&av=%s&avsys=%s&c=%s&d=%s&sv=%s&dk=%s", new Object[]{appkey, appVersion, appVersionSys, channel, utdid, sdkVersion, Variables.getInstance().getDebugKey()});
        } else {
            head = String.format("ak=%s&av=%s&avsys=%s&c=%s&d=%s&sv=%s", new Object[]{appkey, appVersion, appVersionSys, channel, utdid, sdkVersion});
        }
        StringBuilder builder = new StringBuilder(head);
        if (Variables.getInstance().isHttpService()) {
            if (mMiniWuaIndex == 0) {
                mMiniWua = WuaHelper.getMiniWua();
            }
            mMiniWuaIndex++;
            if (mMiniWuaIndex > 50) {
                mMiniWuaIndex = 0;
            }
        } else {
            TnetUtil.refreshMiniWua();
        }
        if (!TextUtils.isEmpty(mMiniWua)) {
            builder.append("&").append("wua=").append(mMiniWua);
        }
        builder.append("&").append("_").append("ut_sample").append("=").append(ConfigTimeStampMgr.getInstance().get("ut_sample"));
        builder.append("&").append("_").append("utap_system").append("=").append(ConfigTimeStampMgr.getInstance().get("utap_system"));
        builder.append("&").append("_").append("ap_stat").append("=").append(ConfigTimeStampMgr.getInstance().get("ap_stat"));
        builder.append("&").append("_").append("ap_alarm").append("=").append(ConfigTimeStampMgr.getInstance().get("ap_alarm"));
        builder.append("&").append("_").append("ap_counter").append("=").append(ConfigTimeStampMgr.getInstance().get("ap_counter"));
        builder.append("&").append("_").append("ut_bussiness").append("=").append(ConfigTimeStampMgr.getInstance().get("ut_bussiness"));
        builder.append("&").append("_").append("ut_realtime").append("=").append(ConfigTimeStampMgr.getInstance().get("ut_realtime"));
        String head2 = builder.toString();
        Logger.i("PostData", "send url :" + head2);
        return head2;
    }

    @TargetApi(19)
    static void initOutputStream() {
        if (Build.VERSION.SDK_INT >= 19) {
            closeOutputStream();
            mByteArrayOutputStream = new ByteArrayOutputStream();
            try {
                mGZIPOutputStream = new GZIPOutputStream(mByteArrayOutputStream, true);
            } catch (Exception e) {
            }
        }
    }

    static void closeOutputStream() {
        closeOutputStream(mGZIPOutputStream);
        closeOutputStream(mByteArrayOutputStream);
    }

    static void closeOutputStream(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String[] getSplitResult(String aLogContent) {
        String[] result = new String[34];
        int lastIndexPos = 0;
        int i = 0;
        while (true) {
            if (i >= result.length - 1) {
                break;
            }
            int findIndexPos = aLogContent.indexOf(Constants.SEPARATOR, lastIndexPos);
            if (findIndexPos == -1) {
                result[i] = aLogContent.substring(lastIndexPos);
                break;
            }
            result[i] = aLogContent.substring(lastIndexPos, findIndexPos);
            lastIndexPos = findIndexPos + 2;
            i++;
        }
        result[33] = aLogContent.substring(lastIndexPos);
        return result;
    }

    static int parseResult(byte[] result) {
        int errCode;
        int leftLen;
        if (result == null || result.length < 12) {
            errCode = -1;
            Logger.e("", "recv errCode UNKNOWN_ERROR");
        } else {
            mReceivedDataLen = (long) result.length;
            if (ByteUtils.bytesToInt(result, 1, 3) + 8 != result.length) {
                errCode = -1;
                Logger.e("", "recv len error");
            } else {
                boolean gzip = false;
                if (1 == (result[5] & 1)) {
                    gzip = true;
                }
                errCode = ByteUtils.bytesToInt(result, 8, 4);
                if (result.length - 12 >= 0) {
                    leftLen = result.length - 12;
                } else {
                    leftLen = 0;
                }
                if (leftLen <= 0) {
                    mResponseAdditionalData = null;
                } else if (gzip) {
                    byte[] rawData = new byte[leftLen];
                    System.arraycopy(result, 12, rawData, 0, leftLen);
                    byte[] unGzipData = GzipUtils.unGzip(rawData);
                    mResponseAdditionalData = new String(unGzipData, 0, unGzipData.length);
                } else {
                    mResponseAdditionalData = new String(result, 12, leftLen);
                }
            }
        }
        if (107 == errCode) {
            Variables.getInstance().setHttpService(true);
        }
        if (109 == errCode) {
            Variables.getInstance().setGzipUpload(true);
        }
        if (115 == errCode) {
            Variables.getInstance().setRealtimeServiceClosed(true);
        }
        if (116 == errCode) {
            Variables.getInstance().setAllServiceClosed(true);
        }
        Logger.d("", "errCode", Integer.valueOf(errCode));
        return errCode;
    }

    static void recordTraffic(long sendBytes) {
        Object object;
        try {
            Context context = Variables.getInstance().getContext();
            if (context != null) {
                if (!bTestFlowGenterClsExist && flowClz != null) {
                    flowClz = Class.forName("com.taobao.analysis.FlowCenter");
                    bTestFlowGenterClsExist = true;
                }
                if (!(flowClz == null || (object = ReflectUtils.invokeStaticMethod(flowClz, "getInstance")) == null)) {
                    Logger.d("", "sendBytes", Long.valueOf(sendBytes), "mReceivedDataLen", Long.valueOf(mReceivedDataLen));
                    ReflectUtils.invokeMethod(object, "commitFlow", new Object[]{context, "ut", true, "ut", Long.valueOf(sendBytes), Long.valueOf(mReceivedDataLen)}, Context.class, String.class, Boolean.TYPE, String.class, Long.TYPE, Long.TYPE);
                }
            }
        } catch (Throwable th) {
        } finally {
            mReceivedDataLen = 0;
        }
    }
}
