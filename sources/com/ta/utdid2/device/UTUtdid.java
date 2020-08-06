package com.ta.utdid2.device;

import android.content.Context;
import android.os.Binder;
import android.provider.Settings;
import android.text.TextUtils;
import com.alibaba.analytics.core.device.Constants;
import com.ta.audid.utils.RC4;
import com.ta.utdid2.android.utils.Base64;
import com.ta.utdid2.android.utils.IntUtils;
import com.ta.utdid2.android.utils.PhoneInfoUtils;
import com.ta.utdid2.android.utils.StringUtils;
import com.ta.utdid2.core.persistent.PersistentConfiguration;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Random;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class UTUtdid {
    private static final Object CREATE_LOCK = new Object();
    private static final String S_GLOBAL_PERSISTENT_CONFIG_DIR = (Constants.PERSISTENT_CONFIG_DIR + File.separator + "Global");
    private static final String S_GLOBAL_PERSISTENT_CONFIG_KEY = "Alvin2";
    private static final String S_LOCAL_STORAGE_KEY = "ContextData";
    private static final String S_LOCAL_STORAGE_NAME = ".DataStorage";
    private static final String UM_SETTINGS_STORAGE = "dxCRMxhQkdGePGnp";
    private static final String UM_SETTINGS_STORAGE_NEW = "mqBRboGZkQPcAkyk";
    private static UTUtdid s_umutdid = null;
    private String mCBDomain = "xx_utdid_domain";
    private String mCBKey = "xx_utdid_key";
    private Context mContext = null;
    private PersistentConfiguration mPC = null;
    private Pattern mPattern = Pattern.compile("[^0-9a-zA-Z=/+]+");
    private PersistentConfiguration mTaoPC = null;
    private String mUtdid = null;
    private UTUtdidHelper mUtdidHelper = null;

    private UTUtdid(Context context) {
        this.mContext = context;
        this.mTaoPC = new PersistentConfiguration(context, S_GLOBAL_PERSISTENT_CONFIG_DIR, S_GLOBAL_PERSISTENT_CONFIG_KEY, false, true);
        this.mPC = new PersistentConfiguration(context, S_LOCAL_STORAGE_NAME, S_LOCAL_STORAGE_KEY, false, true);
        this.mUtdidHelper = new UTUtdidHelper();
        this.mCBKey = String.format("K_%d", new Object[]{Integer.valueOf(StringUtils.hashCode(this.mCBKey))});
        this.mCBDomain = String.format("D_%d", new Object[]{Integer.valueOf(StringUtils.hashCode(this.mCBDomain))});
    }

    private void removeIllegalKeys() {
        if (this.mTaoPC != null) {
            if (StringUtils.isEmpty(this.mTaoPC.getString("UTDID2"))) {
                String lUtdid = this.mTaoPC.getString(com.alibaba.motu.crashreporter.Constants.UTDID);
                if (!StringUtils.isEmpty(lUtdid)) {
                    saveUtdidToTaoPPC(lUtdid);
                }
            }
            boolean lNeedSync = false;
            if (!StringUtils.isEmpty(this.mTaoPC.getString("DID"))) {
                this.mTaoPC.remove("DID");
                lNeedSync = true;
            }
            if (!StringUtils.isEmpty(this.mTaoPC.getString("EI"))) {
                this.mTaoPC.remove("EI");
                lNeedSync = true;
            }
            if (!StringUtils.isEmpty(this.mTaoPC.getString("SI"))) {
                this.mTaoPC.remove("SI");
                lNeedSync = true;
            }
            if (lNeedSync) {
                this.mTaoPC.commit();
            }
        }
    }

    public static UTUtdid instance(Context context) {
        if (context != null && s_umutdid == null) {
            synchronized (CREATE_LOCK) {
                if (s_umutdid == null) {
                    s_umutdid = new UTUtdid(context);
                    s_umutdid.removeIllegalKeys();
                }
            }
        }
        return s_umutdid;
    }

    private void saveUtdidToTaoPPC(String pUtdid) {
        if (isValidUtdid(pUtdid)) {
            if (pUtdid.endsWith("\n")) {
                pUtdid = pUtdid.substring(0, pUtdid.length() - 1);
            }
            if (pUtdid.length() == 24 && this.mTaoPC != null) {
                this.mTaoPC.putString("UTDID2", pUtdid);
                this.mTaoPC.commit();
            }
        }
    }

    private void saveUtdidToLocalStorage(String pPackedUtdid) {
        if (pPackedUtdid != null && this.mPC != null && !pPackedUtdid.equals(this.mPC.getString(this.mCBKey))) {
            this.mPC.putString(this.mCBKey, pPackedUtdid);
            this.mPC.commit();
        }
    }

    private void saveUtdidToNewSettings(String lUtdid) {
        if (checkSettingsPermission() && isValidUtdid(lUtdid)) {
            if (lUtdid.endsWith("\n")) {
                lUtdid = lUtdid.substring(0, lUtdid.length() - 1);
            }
            if (24 == lUtdid.length()) {
                String data = null;
                try {
                    data = Settings.System.getString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE_NEW);
                } catch (Exception e) {
                }
                if (!isValidUtdid(data)) {
                    try {
                        Settings.System.putString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE_NEW, lUtdid);
                    } catch (Exception e2) {
                    }
                }
            }
        }
    }

    private void syncUtdidToSettings(String pPackedUtdid) {
        String data = null;
        try {
            data = Settings.System.getString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE);
        } catch (Exception e) {
        }
        if (!pPackedUtdid.equals(data)) {
            try {
                Settings.System.putString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE, pPackedUtdid);
            } catch (Exception e2) {
            }
        }
    }

    private void saveUtdidToSettings(String lPackedUtdid) {
        if (checkSettingsPermission() && lPackedUtdid != null) {
            syncUtdidToSettings(lPackedUtdid);
        }
    }

    private String getUtdidFromTaoPPC() {
        if (this.mTaoPC != null) {
            String lUtdid = this.mTaoPC.getString("UTDID2");
            if (StringUtils.isEmpty(lUtdid) || this.mUtdidHelper.packUtdidStr(lUtdid) == null) {
                return null;
            }
            return lUtdid;
        }
        return null;
    }

    private boolean isValidUtdid(String pUtdid) {
        if (pUtdid == null) {
            return false;
        }
        if (pUtdid.endsWith("\n")) {
            pUtdid = pUtdid.substring(0, pUtdid.length() - 1);
        }
        if (24 != pUtdid.length() || this.mPattern.matcher(pUtdid).find()) {
            return false;
        }
        return true;
    }

    public synchronized String getValue() {
        String valueForUpdate;
        if (this.mUtdid != null) {
            valueForUpdate = this.mUtdid;
        } else {
            valueForUpdate = getValueForUpdate();
        }
        return valueForUpdate;
    }

    public synchronized String getValueForUpdate() {
        String str;
        this.mUtdid = readUtdid();
        if (!TextUtils.isEmpty(this.mUtdid)) {
            str = this.mUtdid;
        } else {
            try {
                byte[] lUtdid = generateUtdid();
                if (lUtdid != null) {
                    this.mUtdid = Base64.encodeToString(lUtdid, 2);
                    saveUtdidToTaoPPC(this.mUtdid);
                    String lPackedUtdid = this.mUtdidHelper.pack(lUtdid);
                    if (lPackedUtdid != null) {
                        saveUtdidToSettings(lPackedUtdid);
                        saveUtdidToLocalStorage(lPackedUtdid);
                    }
                    str = this.mUtdid;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            str = null;
        }
        return str;
    }

    public synchronized String readUtdid() {
        String lNewSettingsUtdid;
        lNewSettingsUtdid = "";
        try {
            lNewSettingsUtdid = Settings.System.getString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE_NEW);
        } catch (Exception e) {
        }
        if (!isValidUtdid(lNewSettingsUtdid)) {
            UTUtdidHelper2 lHelper2 = new UTUtdidHelper2();
            boolean lNeedUpdateSettings = false;
            String data = null;
            try {
                data = Settings.System.getString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE);
            } catch (Exception e2) {
            }
            if (!StringUtils.isEmpty(data)) {
                String lTmpUtdidBase64 = lHelper2.dePackWithBase64(data);
                if (isValidUtdid(lTmpUtdidBase64)) {
                    saveUtdidToNewSettings(lTmpUtdidBase64);
                    lNewSettingsUtdid = lTmpUtdidBase64;
                } else {
                    String lTmpUtdid = lHelper2.dePack(data);
                    if (isValidUtdid(lTmpUtdid)) {
                        String lPTmpUtdid = this.mUtdidHelper.packUtdidStr(lTmpUtdid);
                        if (!StringUtils.isEmpty(lPTmpUtdid)) {
                            saveUtdidToSettings(lPTmpUtdid);
                            try {
                                data = Settings.System.getString(this.mContext.getContentResolver(), UM_SETTINGS_STORAGE);
                            } catch (Exception e3) {
                            }
                        }
                    }
                    String lDePackedUtdid = this.mUtdidHelper.dePack(data);
                    if (isValidUtdid(lDePackedUtdid)) {
                        this.mUtdid = lDePackedUtdid;
                        saveUtdidToTaoPPC(lDePackedUtdid);
                        saveUtdidToLocalStorage(data);
                        saveUtdidToNewSettings(this.mUtdid);
                        lNewSettingsUtdid = this.mUtdid;
                    }
                }
            } else {
                lNeedUpdateSettings = true;
            }
            String lSUtdid = getUtdidFromTaoPPC();
            if (isValidUtdid(lSUtdid)) {
                String lPackedUtdid = this.mUtdidHelper.packUtdidStr(lSUtdid);
                if (lNeedUpdateSettings) {
                    saveUtdidToSettings(lPackedUtdid);
                }
                saveUtdidToNewSettings(lSUtdid);
                saveUtdidToLocalStorage(lPackedUtdid);
                this.mUtdid = lSUtdid;
                lNewSettingsUtdid = lSUtdid;
            } else {
                String lContent = this.mPC.getString(this.mCBKey);
                if (!StringUtils.isEmpty(lContent)) {
                    String lUtdid = lHelper2.dePack(lContent);
                    if (!isValidUtdid(lUtdid)) {
                        lUtdid = this.mUtdidHelper.dePack(lContent);
                    }
                    if (isValidUtdid(lUtdid)) {
                        String lBUtdid = this.mUtdidHelper.packUtdidStr(lUtdid);
                        if (!StringUtils.isEmpty(lUtdid)) {
                            this.mUtdid = lUtdid;
                            if (lNeedUpdateSettings) {
                                saveUtdidToSettings(lBUtdid);
                            }
                            saveUtdidToTaoPPC(this.mUtdid);
                            lNewSettingsUtdid = this.mUtdid;
                        }
                    }
                }
                lNewSettingsUtdid = null;
            }
        }
        return lNewSettingsUtdid;
    }

    private byte[] generateUtdid() throws Exception {
        String imei;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int uniqueID = new Random().nextInt();
        byte[] bTimestamp = IntUtils.getBytes((int) (System.currentTimeMillis() / 1000));
        byte[] bUniqueID = IntUtils.getBytes(uniqueID);
        baos.write(bTimestamp, 0, 4);
        baos.write(bUniqueID, 0, 4);
        baos.write(3);
        baos.write(0);
        try {
            imei = PhoneInfoUtils.getImei(this.mContext);
        } catch (Exception e) {
            imei = "" + new Random().nextInt();
        }
        baos.write(IntUtils.getBytes(StringUtils.hashCode(imei)), 0, 4);
        baos.write(IntUtils.getBytes(StringUtils.hashCode(calcHmac(baos.toByteArray()))));
        return baos.toByteArray();
    }

    public static String calcHmac(byte[] src) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(RC4.rc4(new byte[]{69, 114, 116, -33, 125, -54, -31, 86, -11, 11, -78, -96, -17, -99, 64, 23, -95, -126, -82, -64, 113, 116, -16, -103, 49, -30, 9, -39, 33, -80, -68, -78, -117, 53, 30, -122, 64, -104, 74, -49, 106, 85, -38, -93}), mac.getAlgorithm()));
        return Base64.encodeToString(mac.doFinal(src), 2);
    }

    private boolean checkSettingsPermission() {
        return this.mContext.checkPermission("android.permission.WRITE_SETTINGS", Binder.getCallingPid(), Binder.getCallingUid()) == 0;
    }
}
