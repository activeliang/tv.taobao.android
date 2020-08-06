package mtopsdk.mtop.deviceid;

import android.content.Context;
import android.os.Build;
import com.ut.device.UTDevice;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import mtopsdk.common.util.ConfigStoreManager;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.deviceid.domain.MtopSysNewDeviceIdRequest;
import mtopsdk.mtop.deviceid.domain.MtopSysNewDeviceIdResponse;
import mtopsdk.mtop.deviceid.domain.MtopSysNewDeviceIdResponseData;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.util.MtopConvert;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import mtopsdk.xstate.XState;
import mtopsdk.xstate.util.PhoneInfo;

public class DeviceIDManager {
    private static final String CREATED_NO = "0";
    private static final String CREATED_YES = "1";
    private static final String DEVICEID_CREATED_KEY = "deviceId_created";
    private static final String DEVICEID_KEY = "deviceId";
    private static final String MTOPSDK_DEVICEID_STORE_PREFIX = "MTOPSDK_DEVICEID_STORE.";
    private static final String TAG = "mtopsdk.DeviceIDManager";
    private static Map<String, DeviceIdDomain> deviceIdMap = new HashMap();

    private DeviceIDManager() {
    }

    private static class InstanceHolder {
        /* access modifiers changed from: private */
        public static final DeviceIDManager INSTANCE = new DeviceIDManager();

        private InstanceHolder() {
        }
    }

    public static DeviceIDManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public Future<String> getDeviceID(final Context context, final String appKey) {
        Future<String> mLastFuture;
        if (StringUtils.isBlank(appKey)) {
            TBSdkLog.e(TAG, "appkey is null,get DeviceId error");
            return null;
        }
        DeviceIdDomain deviceIdDomain = deviceIdMap.get(appKey);
        if (deviceIdDomain == null || (mLastFuture = deviceIdDomain.mLastFuture) == null || mLastFuture.isDone()) {
            final FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
                public String call() throws Exception {
                    String mDeviceId = DeviceIDManager.this.getLocalDeviceID(context, appKey);
                    String mUtdid = DeviceIDManager.this.getLocalUtdid(context);
                    if (StringUtils.isBlank(mDeviceId) || StringUtils.isBlank(mUtdid)) {
                        mDeviceId = DeviceIDManager.this.getRemoteDeviceID(context, appKey);
                    }
                    if (StringUtils.isNotBlank(mDeviceId)) {
                        Mtop.instance(Mtop.Id.INNER, (Context) null).registerDeviceId(mDeviceId);
                    }
                    return mDeviceId;
                }
            });
            MtopSDKThreadPoolExecutorFactory.submit(new Runnable() {
                public void run() {
                    futureTask.run();
                }
            });
            deviceIdMap.put(appKey, new DeviceIdDomain(futureTask));
            return futureTask;
        } else if (!TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            return mLastFuture;
        } else {
            TBSdkLog.i(TAG, "[getDeviceID] appKey=" + appKey + " return mLastFuture");
            return mLastFuture;
        }
    }

    public String getLocalDeviceID(Context context, String appKey) {
        DeviceIdDomain deviceIdDomain = deviceIdMap.get(appKey);
        if (deviceIdDomain != null && StringUtils.isNotBlank(deviceIdDomain.mDeviceId)) {
            return deviceIdDomain.mDeviceId;
        }
        if (context == null) {
            return null;
        }
        String deviceIdStr = ConfigStoreManager.getInstance().getConfigItem(context, ConfigStoreManager.MTOP_CONFIG_STORE, MTOPSDK_DEVICEID_STORE_PREFIX + appKey, "deviceId");
        if ("1".equalsIgnoreCase(ConfigStoreManager.getInstance().getConfigItem(context, ConfigStoreManager.MTOP_CONFIG_STORE, MTOPSDK_DEVICEID_STORE_PREFIX + appKey, DEVICEID_CREATED_KEY))) {
            DeviceIdDomain deviceIdDomain2 = new DeviceIdDomain((Future<String>) null);
            deviceIdDomain2.mDeviceId = deviceIdStr;
            deviceIdDomain2.mCreated = true;
            deviceIdMap.put(appKey, deviceIdDomain2);
        }
        if (!TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            return deviceIdStr;
        }
        TBSdkLog.i(TAG, "[getLocalDeviceID]get DeviceId from store appkey=" + appKey + "; deviceId=" + deviceIdStr);
        return deviceIdStr;
    }

    public String getLocalUtdid(Context context) {
        String utdid = XState.getValue("utdid");
        if (StringUtils.isNotBlank(utdid)) {
            Mtop.instance(Mtop.Id.INNER, (Context) null).registerUtdid(utdid);
            return utdid;
        } else if (context != null) {
            String utdid2 = UTDevice.getUtdid(context);
            Mtop.instance(Mtop.Id.INNER, (Context) null).registerUtdid(utdid2);
            return utdid2;
        } else if (!TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
            return null;
        } else {
            TBSdkLog.w(TAG, "[getLocalUtdid] Context is null,get Utdid failed");
            return null;
        }
    }

    public void clear(Context context, String appKey) {
        deviceIdMap.remove(appKey);
        saveDeviceIdToStore(context, appKey, "", "0");
    }

    /* access modifiers changed from: package-private */
    public String getRemoteDeviceID(Context context, String appKey) {
        String deviceId = null;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "[getRemoteDeviceID] called!appkey=" + appKey);
        }
        String utdid = getLocalUtdid(context);
        String imei = PhoneInfo.getOriginalImei(context);
        String imsi = PhoneInfo.getOriginalImsi(context);
        StringBuilder device_global_id = new StringBuilder(64);
        if (StringUtils.isNotBlank(utdid)) {
            device_global_id.append(utdid);
        }
        if (StringUtils.isNotBlank(imei)) {
            device_global_id.append(imei);
        }
        if (StringUtils.isNotBlank(imsi)) {
            device_global_id.append(imsi);
        }
        if (StringUtils.isBlank(device_global_id.toString())) {
            TBSdkLog.e(TAG, "[getRemoteDeviceID]device_global_id is blank");
        } else {
            MtopSysNewDeviceIdRequest request = new MtopSysNewDeviceIdRequest();
            request.device_global_id = device_global_id.toString();
            request.new_device = true;
            request.c0 = Build.BRAND;
            request.c1 = Build.MODEL;
            request.c2 = imei;
            request.c3 = imsi;
            request.c4 = PhoneInfo.getLocalMacAddress(context);
            request.c5 = PhoneInfo.getSerialNum();
            request.c6 = PhoneInfo.getAndroidId(context);
            MtopResponse response = Mtop.instance(Mtop.Id.INNER, (Context) null).build((IMTOPDataObject) request, (String) null).setBizId(4099).syncRequest();
            deviceId = null;
            if (response.isApiSuccess()) {
                try {
                    BaseOutDo baseOutDo = (BaseOutDo) MtopConvert.convertJsonToOutputDO(response.getBytedata(), MtopSysNewDeviceIdResponse.class);
                    if (baseOutDo != null) {
                        deviceId = ((MtopSysNewDeviceIdResponseData) baseOutDo.getData()).device_id;
                        if (StringUtils.isNotBlank(deviceId)) {
                            saveDeviceIdToStore(context, appKey, deviceId, "1");
                        }
                    }
                } catch (Throwable e) {
                    TBSdkLog.e(TAG, "[getRemoteDeviceID] error ---" + e.toString());
                }
            }
        }
        return deviceId;
    }

    private void saveDeviceIdToStore(Context context, String appKey, String deviceId, String mCreated) {
        if (context != null) {
            ConfigStoreManager.getInstance().saveConfigItem(context, ConfigStoreManager.MTOP_CONFIG_STORE, MTOPSDK_DEVICEID_STORE_PREFIX + appKey, "deviceId", deviceId);
            ConfigStoreManager.getInstance().saveConfigItem(context, ConfigStoreManager.MTOP_CONFIG_STORE, MTOPSDK_DEVICEID_STORE_PREFIX + appKey, DEVICEID_CREATED_KEY, mCreated);
            DeviceIdDomain deviceIdDomain = deviceIdMap.get(appKey);
            if (deviceIdDomain == null) {
                deviceIdDomain = new DeviceIdDomain((Future<String>) null);
            }
            deviceIdDomain.mDeviceId = deviceId;
            deviceIdDomain.mCreated = true;
            deviceIdMap.put(appKey, deviceIdDomain);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                StringBuilder builder = new StringBuilder(32);
                builder.append("[saveDeviceIdToStore] appkey=").append(appKey);
                builder.append("; deviceId=").append(deviceId);
                builder.append("; mCreated=").append(mCreated);
                TBSdkLog.i(TAG, builder.toString());
            }
        }
    }

    class DeviceIdDomain {
        public boolean mCreated;
        public String mDeviceId;
        public Future<String> mLastFuture;

        public DeviceIdDomain(Future<String> mLastFuture2) {
            this.mLastFuture = mLastFuture2;
        }
    }
}
