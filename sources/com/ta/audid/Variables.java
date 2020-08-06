package com.ta.audid;

import android.content.Context;
import android.text.TextUtils;
import com.ta.audid.db.DBMgr;
import com.ta.audid.filesync.UtdidBroadcastMgr;
import com.ta.audid.permission.PermissionUtils;
import com.ta.audid.upload.UploadMgr;
import com.ta.audid.upload.UtdidKeyFile;
import com.ta.audid.utils.UtdidLogger;
import java.io.File;

public class Variables {
    private static final String DATABASE_NAME = "utdid.db";
    private static final Variables mInstance = new Variables();
    private boolean bGetModeState = false;
    private volatile boolean bInit = false;
    private boolean bOldMode = false;
    private String mAppChannel = "";
    private String mAppkey = "testKey";
    private Context mContext = null;
    private DBMgr mDbMgr = null;
    private long mDeltaTime = 0;
    private File mOldModeFile = null;
    private boolean mPhoneStatePermission = false;
    private boolean mStoragePermission = false;

    private Variables() {
    }

    public static Variables getInstance() {
        return mInstance;
    }

    public synchronized void init() {
        if (!this.bInit) {
            UtdidBroadcastMgr.getInstance().startBroadCastReceiver(this.mContext);
            this.mDbMgr = new DBMgr(this.mContext, DATABASE_NAME);
            UploadMgr.getInstance(this.mContext).start();
            this.mStoragePermission = PermissionUtils.checkStoragePermissionGranted(this.mContext);
            this.mPhoneStatePermission = PermissionUtils.checkReadPhoneStatePermissionGranted(this.mContext);
            this.bInit = true;
        }
    }

    public synchronized void initContext(Context context) {
        if (this.mContext == null && context != null) {
            if (context.getApplicationContext() != null) {
                this.mContext = context.getApplicationContext();
            } else {
                this.mContext = context;
            }
        }
    }

    @Deprecated
    public synchronized void setOldMode(boolean oldMode) {
        try {
            this.bOldMode = oldMode;
            UtdidLogger.d("", Boolean.valueOf(this.bOldMode));
            if (oldMode) {
                UploadMgr.getInstance(this.mContext).stop();
                UtdidBroadcastMgr.getInstance().stopBroadCastReceiver(this.mContext);
            } else {
                UtdidBroadcastMgr.getInstance().startBroadCastReceiver(this.mContext);
                UploadMgr.getInstance(this.mContext).start();
            }
            if (this.mOldModeFile == null) {
                this.mOldModeFile = new File(UtdidKeyFile.getOldModeFilePath());
            }
            boolean exists = this.mOldModeFile.exists();
            if (oldMode && !exists) {
                this.mOldModeFile.createNewFile();
            } else if (!oldMode && exists) {
                this.mOldModeFile.delete();
            }
        } catch (Exception e) {
            UtdidLogger.d("", e);
        }
        return;
    }

    public synchronized boolean getOldMode() {
        boolean z;
        if (this.bGetModeState) {
            UtdidLogger.d("", Boolean.valueOf(this.bOldMode));
            z = this.bOldMode;
        } else {
            try {
                if (this.mOldModeFile == null) {
                    this.mOldModeFile = new File(UtdidKeyFile.getOldModeFilePath());
                }
                if (this.mOldModeFile.exists()) {
                    this.bOldMode = true;
                    UtdidLogger.d("", "old mode file");
                    z = this.bOldMode;
                    this.bGetModeState = true;
                } else {
                    this.bGetModeState = true;
                    this.bOldMode = false;
                    UtdidLogger.d("", "new mode file");
                    z = this.bOldMode;
                }
            } catch (Exception e) {
                UtdidLogger.d("", e);
                this.bGetModeState = true;
            } catch (Throwable th) {
                this.bGetModeState = true;
                throw th;
            }
        }
        return z;
    }

    public void setDebug(boolean isDebug) {
        UtdidLogger.setDebug(isDebug);
    }

    public Context getContext() {
        return this.mContext;
    }

    public DBMgr getDbMgr() {
        return this.mDbMgr;
    }

    public boolean userGrantStoragePermission() {
        boolean currentPermission = PermissionUtils.checkStoragePermissionGranted(this.mContext);
        if (this.mStoragePermission || !currentPermission) {
            this.mStoragePermission = currentPermission;
            return false;
        }
        this.mStoragePermission = true;
        return true;
    }

    public boolean userGrantPhoneStatePermission() {
        boolean currentPermission = PermissionUtils.checkReadPhoneStatePermissionGranted(this.mContext);
        if (this.mPhoneStatePermission || !currentPermission) {
            this.mStoragePermission = currentPermission;
            return false;
        }
        this.mStoragePermission = true;
        return true;
    }

    public void setAppkey(String appKey) {
        if (!TextUtils.isEmpty(appKey)) {
            this.mAppkey = appKey;
        }
    }

    public String getAppkey() {
        return this.mAppkey;
    }

    public void setAppChannel(String appChannel) {
        this.mAppChannel = appChannel;
    }

    public String getAppChannel() {
        return this.mAppChannel;
    }

    public void setSystemTime(long time) {
        this.mDeltaTime = time - System.currentTimeMillis();
    }

    public long getCurrentTimeMillis() {
        return System.currentTimeMillis() + this.mDeltaTime;
    }

    public String getCurrentTimeMillisString() {
        return "" + getCurrentTimeMillis();
    }
}
