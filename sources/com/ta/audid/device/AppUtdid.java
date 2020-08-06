package com.ta.audid.device;

import android.content.Context;
import android.text.TextUtils;
import com.ta.audid.Constants;
import com.ta.audid.Variables;
import com.ta.audid.collect.DeviceInfo2;
import com.ta.audid.filesync.UtdidBroadcastMgr;
import com.ta.audid.store.SdcardDeviceModle;
import com.ta.audid.store.UtdidContentBuilder;
import com.ta.audid.store.UtdidContentSqliteStore;
import com.ta.audid.upload.AppsResponse;
import com.ta.audid.upload.UtdidKeyFile;
import com.ta.audid.utils.AppInfoUtils;
import com.ta.audid.utils.MutiProcessLock;
import com.ta.audid.utils.TaskExecutor;
import com.ta.audid.utils.UtdidLogger;
import com.ta.utdid2.device.UTUtdid;
import java.util.ArrayList;
import java.util.List;

public class AppUtdid {
    private static final int V5 = 5;
    private static final AppUtdid mInstance = new AppUtdid();
    /* access modifiers changed from: private */
    public String mAppUtdid = "";
    private String mUtdid = "";

    private AppUtdid() {
    }

    public static AppUtdid getInstance() {
        return mInstance;
    }

    public synchronized String getUtdid() {
        String str;
        if (!TextUtils.isEmpty(this.mUtdid)) {
            str = this.mUtdid;
        } else if (!TextUtils.isEmpty(getUtdidFromFile())) {
            uploadAppUtdid();
            str = this.mUtdid;
        } else {
            str = Constants.UTDID_INVALID;
        }
        return str;
    }

    public String getUtdidFromFile() {
        try {
            MutiProcessLock.lockUtdidFile();
            String utdid = getV5Utdid();
            if (!TextUtils.isEmpty(utdid)) {
                UtdidLogger.d("", "read from NewFile:" + utdid);
                this.mUtdid = utdid;
                this.mAppUtdid = utdid;
                return this.mUtdid;
            }
            String utdid2 = UTUtdid.instance(Variables.getInstance().getContext()).getValueForUpdate();
            if (!TextUtils.isEmpty(utdid2)) {
                UtdidLogger.d("", "read from OldFile:" + utdid2);
                this.mUtdid = utdid2;
                this.mAppUtdid = utdid2;
                String str = this.mUtdid;
                MutiProcessLock.releaseUtdidFile();
                return str;
            }
            MutiProcessLock.releaseUtdidFile();
            return "";
        } catch (Throwable t) {
            UtdidLogger.e("", t, new Object[0]);
        } finally {
            MutiProcessLock.releaseUtdidFile();
        }
    }

    private String getV5Utdid() {
        UtdidObj utdidObjFromApp;
        Context context = Variables.getInstance().getContext();
        if (context == null) {
            return "";
        }
        if (AppInfoUtils.isBelowMVersion().booleanValue()) {
            String utdidFromSettings = UtdidKeyFile.getUtdidFromSettings(context);
            if (!TextUtils.isEmpty(utdidFromSettings)) {
                UtdidObj utdidObj = AppUtdidDecoder.decode(utdidFromSettings);
                if (utdidObj.isValid() && utdidObj.getVersion() == 5) {
                    UtdidKeyFile.writeAppUtdidFile(utdidFromSettings);
                    UtdidKeyFile.writeSdcardUtdidFile(utdidFromSettings);
                    return utdidFromSettings;
                }
            }
        }
        String utdidFromSdcard = UtdidKeyFile.readSdcardUtdidFile();
        String utdidFromApp = UtdidKeyFile.readAppUtdidFile();
        UtdidObj utdidObjFromSdcard = null;
        long utdidObjFromSdcardTimestamp = 0;
        long utdidObjFromAppTimestamp = 0;
        if (!TextUtils.isEmpty(utdidFromSdcard) && (utdidObjFromSdcard = AppUtdidDecoder.decode(utdidFromSdcard)) != null) {
            if (utdidObjFromSdcard.getVersion() != 5) {
                utdidFromSdcard = "";
                UtdidKeyFile.writeSdcardUtdidFile("");
            } else {
                utdidObjFromSdcardTimestamp = utdidObjFromSdcard.getTimestamp();
            }
        }
        if (!TextUtils.isEmpty(utdidFromApp)) {
            if (utdidFromApp.equals(utdidFromSdcard)) {
                utdidObjFromApp = utdidObjFromSdcard;
            } else {
                utdidObjFromApp = AppUtdidDecoder.decode(utdidFromApp);
            }
            if (utdidObjFromApp != null) {
                if (utdidObjFromApp.getVersion() != 5) {
                    utdidFromApp = "";
                    UtdidKeyFile.writeAppUtdidFile("");
                } else {
                    utdidObjFromAppTimestamp = utdidObjFromApp.getTimestamp();
                }
            }
        }
        if (TextUtils.isEmpty(utdidFromSdcard) || TextUtils.isEmpty(utdidFromApp)) {
            if (!TextUtils.isEmpty(utdidFromSdcard) && TextUtils.isEmpty(utdidFromApp)) {
                UtdidKeyFile.writeAppUtdidFile(utdidFromSdcard);
                UtdidKeyFile.writeUtdidToSettings(context, utdidFromSdcard);
                return utdidFromSdcard;
            } else if (!TextUtils.isEmpty(utdidFromSdcard) || TextUtils.isEmpty(utdidFromApp)) {
                return "";
            } else {
                UtdidKeyFile.writeSdcardUtdidFile(utdidFromApp);
                UtdidKeyFile.writeUtdidToSettings(context, utdidFromApp);
                return utdidFromApp;
            }
        } else if (utdidFromSdcard.equals(utdidFromApp)) {
            return utdidFromSdcard;
        } else {
            if (utdidObjFromSdcardTimestamp >= utdidObjFromAppTimestamp) {
                UtdidKeyFile.writeAppUtdidFile(utdidFromSdcard);
                UtdidKeyFile.writeUtdidToSettings(context, utdidFromSdcard);
                return utdidFromSdcard;
            }
            UtdidKeyFile.writeSdcardUtdidFile(utdidFromApp);
            UtdidKeyFile.writeUtdidToSettings(context, utdidFromApp);
            return utdidFromApp;
        }
    }

    private void uploadAppUtdid() {
        UtdidLogger.d();
        if (!TextUtils.isEmpty(this.mAppUtdid)) {
            try {
                TaskExecutor.getInstance().submit(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(30000);
                            UtdidBroadcastMgr.getInstance().sendBroadCast(AppUtdid.this.mAppUtdid);
                            AppUtdid.this.writeSdcardDevice();
                            AppsResponse.getInstance().requestAppList();
                            List<String> mUploadStringList = new ArrayList<>();
                            mUploadStringList.add(UtdidContentBuilder.buildUtdidFp(AppUtdid.this.mAppUtdid));
                            UtdidContentSqliteStore.getInstance().insertStringList(mUploadStringList);
                        } catch (Throwable e) {
                            UtdidLogger.d("", e);
                        }
                    }
                });
            } catch (Throwable e) {
                UtdidLogger.d("", e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void writeSdcardDevice() {
        Context context = Variables.getInstance().getContext();
        if (context != null) {
            SdcardDeviceModle.writeSdcardDeviceModle(DeviceInfo2.getIMEI(context), DeviceInfo2.getIMSI(context));
        }
    }

    public synchronized void setAppUtdid(String appUtdid) {
        this.mAppUtdid = appUtdid;
    }

    public synchronized String getCurrentAppUtdid() {
        return this.mAppUtdid;
    }
}
