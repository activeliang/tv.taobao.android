package com.yunos.tvtaobao.uuid;

import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tvtaobao.uuid.client.Client;
import com.yunos.tvtaobao.uuid.client.exception.GenerateUUIDException;
import com.yunos.tvtaobao.uuid.client.exception.InfosInCompleteException;
import com.yunos.tvtaobao.uuid.infos.InfosManager;
import com.yunos.tvtaobao.uuid.security.SecurityInfosManager;
import com.yunos.tvtaobao.uuid.utils.Logger;
import com.yunos.tvtaobao.uuid.utils.SGMWrapper;
import com.yunos.tvtaobao.uuid.utils.SystemProperties;
import java.util.regex.Pattern;

public class TVAppUUIDImpl {
    public static int FAILED = 1;
    private static final int ITEM_UUID_ENCRYPTED_ID = 2;
    private static final int ITEM_UUID_ID = 1;
    public static int NOERROR = 0;
    private static String PROVIDER_AUTHORITY = null;
    private static final String PROVIDER_AUTHORITY_SUFFIX = ".yunosuuid.provider";
    public static String UMID = null;
    public static int UMID_ret = 0;
    private static boolean androidOnly = false;
    private static boolean isSystemUUID = false;
    /* access modifiers changed from: private */
    public static Context mContext;
    private static Client.StatusListener mListener = new Client.StatusListener() {
        public void onRegister(String uuid) {
            if (new SGMWrapper(TVAppUUIDImpl.mContext, TVAppUUIDImpl.mSMGAuthcode).saveUUID(uuid)) {
                Logger.log_d("uuid saved.");
            } else {
                Logger.loge("uuid save failed");
            }
        }

        public void onActivate() {
            if (new SGMWrapper(TVAppUUIDImpl.mContext, TVAppUUIDImpl.mSMGAuthcode).saveActMsg()) {
                Logger.log_d("act-msg saved.");
            } else {
                Logger.loge("act-msg save failed");
            }
        }
    };
    public static String mSMGAuthcode = "uuid";
    private static UriMatcher mUriMatcher = new UriMatcher(-1);
    public static boolean usingAndroidID = false;
    private static String uuidCached = null;

    public enum UUID_FORM_WHERE {
        NONE,
        SYSTEM,
        LOCAL,
        NETWORK,
        NEIGHBOR
    }

    public static void _init(Context context) {
        mContext = context;
        if (!isSystemUUIDExist(context)) {
            Logger.log("did't need got uuid from provider");
        }
    }

    public static UUID_FORM_WHERE _isUUIDExist() {
        if (isSystemUUID) {
            uuidCached = getSystemUUID();
            if (TextUtils.isEmpty(uuidCached)) {
                return UUID_FORM_WHERE.NONE;
            }
            return UUID_FORM_WHERE.SYSTEM;
        }
        SGMWrapper sgm = new SGMWrapper(mContext, mSMGAuthcode);
        String uuid = sgm.getUUID();
        uuidCached = uuid;
        if (!(uuid != null && sgm.isActivated())) {
            return UUID_FORM_WHERE.NONE;
        }
        Logger.log_d("_isUUIDExist LOCAL");
        return UUID_FORM_WHERE.LOCAL;
    }

    public static String _getCloudUUID() {
        if (uuidCached != null) {
            Logger.log("got uuid Cached:" + uuidCached);
            return uuidCached;
        } else if (UUID_FORM_WHERE.NONE == _isUUIDExist()) {
            return null;
        } else {
            Logger.log("got uuid Cached:" + uuidCached);
            return uuidCached;
        }
    }

    private static String getSystemUUID() {
        String resultUUID = null;
        try {
            String result = (String) Class.forName("com.yunos.baseservice.clouduuid.CloudUUID").getMethod("getCloudUUID", new Class[0]).invoke((Object) null, new Object[0]);
            if (!"false".equalsIgnoreCase(result)) {
                resultUUID = result;
            }
        } catch (Exception e) {
            Logger.loge("getSystemDeviceID error:" + e.getMessage());
        }
        Logger.log_d("getSystemDeviceID uuid:" + resultUUID);
        if (TextUtils.isEmpty(resultUUID)) {
            return getYunOSUUID();
        }
        return resultUUID;
    }

    private static boolean isSystemUUIDExist(Context context) {
        boolean z = true;
        if (hasYunOSUUID()) {
            Logger.log("has yunos uuid");
            isSystemUUID = true;
        } else if (isYunOS()) {
            Logger.log("runging on yunos env");
            isSystemUUID = true;
        } else if (context != null) {
            try {
                if (context.getPackageManager().getPackageInfo("com.yunos.alifunui.uuid", 8192) == null) {
                    z = false;
                }
                isSystemUUID = z;
            } catch (Exception e) {
                Logger.log_d("alifun uuid is didt exist=" + isSystemUUID);
                return false;
            }
        }
        Logger.log_d("isSystemUUIDExist=" + isSystemUUID);
        return isSystemUUID;
    }

    public static boolean _isSystemUUID() {
        return isSystemUUID;
    }

    public static void setAndroidOnly(boolean only) {
        androidOnly = only;
    }

    public static void setUseAndroidID(boolean use) {
        usingAndroidID = use;
    }

    public static void setAUTHCODE(String newAuthcode) {
        mSMGAuthcode = newAuthcode;
    }

    public static void _generateUUIDAsyn(IUUIDListener listener, String productName, String ttid, int licenseType) {
        String uuid;
        long beginTime = System.currentTimeMillis();
        Logger.log("generate syn alifun UUID:" + isSystemUUID + " androidOnly:" + androidOnly);
        if (isSystemUUID) {
            Logger.log("_generateUUIDAsyn is alifun SystemUUID");
            notify(listener, beginTime, FAILED);
            return;
        }
        if (!androidOnly) {
            if (!isYunOS() || (uuid = getYunOSUUID()) == null) {
                Logger.log("This not YunOS, gen uuid");
            } else {
                Logger.log("This is YunOS, got uuid.");
                SGMWrapper sgm = new SGMWrapper(mContext, mSMGAuthcode);
                sgm.saveUUID(uuid);
                sgm.saveActMsg();
                notify(listener, beginTime, NOERROR);
                return;
            }
        }
        try {
            InfosManager infosManager = new InfosManager(mContext, productName, ttid);
            if (InfosManager.default_mac.equals(infosManager.getWifiMac())) {
                notify(listener, beginTime, NOERROR);
                uuidCached = InfosManager.default_uuid_FORD;
                return;
            }
            SecurityInfosManager sInfosManager = new SecurityInfosManager(infosManager, mContext);
            int error = NOERROR;
            try {
                new Client(mContext, infosManager, sInfosManager, mListener, licenseType).generate();
            } catch (GenerateUUIDException e) {
                Logger.loge("GenerateUUIDException: errorcode = " + e.getErrorCode() + " errormsg: " + e.getMessage());
                error = e.getErrorCode();
            }
            notify(listener, beginTime, error);
        } catch (InfosInCompleteException e2) {
            e2.print();
            e2.printStackTrace();
            notify(listener, beginTime, FAILED);
        }
    }

    public static Cursor queryProvider(Uri uri, String[] arg1, String arg2, String[] arg3, String arg4) {
        String uuid = Constant.NULL;
        boolean uuid_no_enc = false;
        MatrixCursor cursor = null;
        try {
            switch (mUriMatcher.match(uri)) {
                case 1:
                    uuid_no_enc = true;
                    break;
                case 2:
                    break;
            }
            uuid = _getCloudUUID();
            MatrixCursor cursor2 = new MatrixCursor(new String[]{"uuid"});
            try {
                Object[] object = new Object[1];
                if (uuid_no_enc) {
                    object[0] = uuid;
                } else {
                    object[0] = new SGMWrapper(mContext, mSMGAuthcode).encryptUUID(uuid);
                }
                cursor2.addRow(object);
                Logger.log("query uuid Provider :" + object[0].toString());
                return cursor2;
            } catch (Exception e) {
                e = e;
                cursor = cursor2;
            }
        } catch (Exception e2) {
            e = e2;
        }
        Logger.log("query Provider exception:" + e.toString());
        return cursor;
    }

    private static boolean isYunOS() {
        return "1".equals(SystemProperties.get("persist.sys.yunosflag", "")) || "yunos".equals(SystemProperties.get("ro.yunos.hardware", "")) || !"tvtaobaonochip".equals(SystemProperties.get("ro.yunos.product.chip", "tvtaobaonochip"));
    }

    private static boolean hasYunOSUUID() {
        return !TextUtils.isEmpty(getYunOSUUID());
    }

    private static String getYunOSUUID() {
        String uuid = SystemProperties.get("ro.aliyun.clouduuid", "");
        Logger.log_d("getYunOSUUID uuid:" + uuid);
        if (uuid.length() != 32) {
            return null;
        }
        byte[] bytes = uuid.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] < 48 || bytes[i] > 57) && (bytes[i] < 65 || bytes[i] > 70)) {
                return null;
            }
        }
        return uuid;
    }

    private static void notify(IUUIDListener listener, long begin, int error) {
        float time = ((float) (System.currentTimeMillis() - begin)) / 1000.0f;
        Logger.log("time cost : " + time);
        listener.onCompleted(error, time);
    }

    private static boolean isValidUUID(String uuid) {
        if (Pattern.compile("[A-Z0-9]{32}").matcher(uuid).matches()) {
            return true;
        }
        Logger.log_d("invalid uuid:" + uuid);
        return false;
    }
}
