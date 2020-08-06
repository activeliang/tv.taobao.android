package com.yunos.tvtaobao.biz.model;

import android.taobao.windvane.util.WVNativeCallbackUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.atlas.update.model.UpdateInfo;

public class AppInfo {
    private static final String BASE_VERSION = "baseVersion";
    private static final String COLOR = "color";
    private static final String DATA = "data";
    private static final String DOWNLOAD_MD5 = "downloadMd5";
    private static final String DOWNLOAD_URL = "downloadUrl";
    private static final String EXTEND = "extend";
    private static final String ID = "id";
    private static final String IMAGE1 = "image1";
    private static final String IMAGE2 = "image2";
    private static final String IMAGE3 = "image3";
    private static final String IMAGE4 = "image4";
    private static final String IMAGE5 = "image5";
    private static final String IMAGE6 = "image6";
    private static final String LATER_ON = "laterOn";
    private static final String NEW_RELEASE_NOTE = "newReleaseNote";
    private static final String OSS_DOWNLOAD_URL = "ossDownloadUrl";
    private static final String RELEASE_AFTER_NOTE = "releaseAfterNote";
    private static final String RELEASE_NOTE = "releaseNote";
    private static final String RESOURCE_ID = "resourceId";
    private static final String RET = "ret";
    private static final String SIZE = "size";
    private static final String STATUS = "status";
    private static final String TAPTCH_DOWNLOAD_URL = "tpatchDownloadUrl";
    private static final String TIME_STAMP = "timeStamp";
    private static final String TYPE = "type";
    private static final String UPDATE_BUNDLES = "updateBundles";
    private static final String UPDATE_VERSION = "updateVersion";
    private static final String UPGRADE_MODE = "upgradeMode";
    private static final String UPGRADE_NOW = "upgradeNow";
    private static final String VERSION = "version";
    private static final String VERSION_NAME = "versionName";
    public String apkName;
    private String color;
    public String downloadMd5;
    public String downloadUrl;
    public String extend;
    public String id;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    private String image6;
    public boolean isForced;
    public boolean isLatest;
    public boolean isSuccess;
    private String laterOn;
    public UpdateInfo mUpdateInfo;
    private String newReleaseNote;
    public String ossDownloadUrl;
    public String releaseAfterNote;
    public String releaseNote;
    public String resourceId;
    private JSONArray retArray;
    public String returnText;
    public String size;
    public String status;
    public String timeStamp;
    public String tpatchDownloadUrl;
    public int type = 0;
    private String upgradeMode;
    private String upgradeNow;
    private JSONObject upgradeObject;
    public String version;
    public String versionName;

    public String getReleaseNote() {
        return this.releaseNote;
    }

    public void setReleaseNote(String releaseNote2) {
        this.releaseNote = releaseNote2;
    }

    public String getImage1() {
        return this.image1;
    }

    public String getImage2() {
        return this.image2;
    }

    public String getImage3() {
        return this.image3;
    }

    public String getImage4() {
        return this.image4;
    }

    public String getImage5() {
        return this.image5;
    }

    public String getImage6() {
        return this.image6;
    }

    public String getNewReleaseNote() {
        return this.newReleaseNote;
    }

    public String getUpgradeMode() {
        return this.upgradeMode;
    }

    public String getColor() {
        return this.color;
    }

    public String getLaterOn() {
        return this.laterOn;
    }

    public String getUpgradeNow() {
        return this.upgradeNow;
    }

    public AppInfo(JSONObject upgradeObject2) {
        if (upgradeObject2 == null) {
            this.isSuccess = false;
            return;
        }
        this.isSuccess = true;
        if (upgradeObject2 == null || upgradeObject2.size() <= 0) {
            this.isLatest = true;
            return;
        }
        this.id = upgradeObject2.getString(ID);
        this.resourceId = upgradeObject2.getString(RESOURCE_ID);
        this.version = upgradeObject2.getString("version");
        this.versionName = upgradeObject2.getString("versionName");
        this.releaseNote = upgradeObject2.getString("releaseNote");
        this.downloadUrl = upgradeObject2.getString(DOWNLOAD_URL);
        if (this.downloadUrl != null) {
            String[] tmp = this.downloadUrl.split(WVNativeCallbackUtil.SEPERATER);
            this.apkName = tmp[tmp.length - 1].split("[?]")[0];
        }
        this.ossDownloadUrl = upgradeObject2.getString(OSS_DOWNLOAD_URL);
        this.downloadMd5 = upgradeObject2.getString(DOWNLOAD_MD5);
        this.type = upgradeObject2.getInteger("type").intValue();
        if (this.type == 1) {
            this.mUpdateInfo = (UpdateInfo) JSON.parseObject(upgradeObject2.getString(UPDATE_BUNDLES), UpdateInfo.class);
        }
        this.size = upgradeObject2.getString("size");
        this.status = upgradeObject2.getString("status");
        this.extend = upgradeObject2.getString(EXTEND);
        if ("forceInstall".equalsIgnoreCase(this.extend)) {
            this.isForced = true;
        } else {
            this.isForced = false;
        }
        this.timeStamp = upgradeObject2.getString(TIME_STAMP);
        this.releaseAfterNote = upgradeObject2.getString(RELEASE_AFTER_NOTE);
        this.image1 = upgradeObject2.getString("image1");
        this.image2 = upgradeObject2.getString("image2");
        this.image3 = upgradeObject2.getString("image3");
        this.image4 = upgradeObject2.getString("image4");
        this.image5 = upgradeObject2.getString("image5");
        this.image6 = upgradeObject2.getString("image6");
        this.newReleaseNote = upgradeObject2.getString("newReleaseNote");
        this.upgradeMode = upgradeObject2.getString("upgradeMode");
        this.color = upgradeObject2.getString("color");
        this.laterOn = upgradeObject2.getString("laterOn");
        this.upgradeNow = upgradeObject2.getString("upgradeNow");
    }

    public String toString() {
        return "AppInfo{isSuccess=" + this.isSuccess + ", isForced=" + this.isForced + ", isLatest=" + this.isLatest + ", type=" + this.type + ", returnText='" + this.returnText + '\'' + ", versionName='" + this.versionName + '\'' + ", releaseNote='" + this.releaseNote + '\'' + ", downloadUrl='" + this.downloadUrl + '\'' + ", tpatchDownloadUrl='" + this.tpatchDownloadUrl + '\'' + ", ossDownloadUrl='" + this.ossDownloadUrl + '\'' + ", downloadMd5='" + this.downloadMd5 + '\'' + ", extend='" + this.extend + '\'' + ", id='" + this.id + '\'' + ", resourceId='" + this.resourceId + '\'' + ", version='" + this.version + '\'' + ", size='" + this.size + '\'' + ", status='" + this.status + '\'' + ", timeStamp='" + this.timeStamp + '\'' + ", apkName='" + this.apkName + '\'' + ", upgradeObject=" + this.upgradeObject + ", retArray=" + this.retArray + ", releaseAfterNote=" + this.releaseAfterNote + ", image1=" + this.image1 + ", image2=" + this.image2 + ", image3=" + this.image3 + ", image4=" + this.image4 + ", image5=" + this.image5 + ", image6=" + this.image6 + ", newReleaseNote=" + this.newReleaseNote + ", upgradeMode=" + this.upgradeMode + ", color=" + this.color + ", laterOn=" + this.laterOn + ", upgradeNow=" + this.upgradeNow + '}';
    }
}
