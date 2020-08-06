package android.taobao.windvane.packageapp.zipapp.data;

import android.support.v4.media.session.PlaybackStateCompat;
import android.taobao.windvane.config.EnvEnum;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import com.taobao.ju.track.constants.Constants;
import java.util.ArrayList;

public class ZipAppInfo {
    private ZipAppTypeEnum appType;
    public long f = 5;
    public ArrayList<String> folders = new ArrayList<>();
    public long installedSeq = 0;
    public String installedVersion = Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE;
    public boolean isOptional = false;
    public boolean isPreViewApp = false;
    public String mappingUrl = "";
    public String name = "";
    public long s = 0;
    public int status = 0;
    public long t = 0;
    private ZipUpdateInfoEnum updateInfo;
    private ZipUpdateTypeEnum updateType;
    public String v = "";
    public String z = "";

    public int getPriority() {
        return (int) (this.f & 15);
    }

    public ZipAppTypeEnum getAppType() {
        for (ZipAppTypeEnum item : ZipAppTypeEnum.values()) {
            if (item.getValue() == (this.f & 240)) {
                this.appType = item;
                return this.appType;
            }
        }
        return ZipAppTypeEnum.ZIP_APP_TYPE_UNKNOWN;
    }

    public ZipUpdateTypeEnum getUpdateType() {
        for (ZipUpdateTypeEnum item : ZipUpdateTypeEnum.values()) {
            if (item.getValue() == (this.f & 3840)) {
                this.updateType = item;
                return this.updateType;
            }
        }
        return ZipUpdateTypeEnum.ZIP_UPDATE_TYPE_PASSIVE;
    }

    public ZipUpdateInfoEnum getInfo() {
        for (ZipUpdateInfoEnum item : ZipUpdateInfoEnum.values()) {
            if (item.getValue() == (this.f & 12288)) {
                this.updateInfo = item;
                return this.updateInfo;
            }
        }
        return ZipUpdateInfoEnum.ZIP_APP_TYPE_NORMAL;
    }

    public boolean getIs2GUpdate() {
        return (this.f & PlaybackStateCompat.ACTION_PREPARE) != 0;
    }

    public boolean getIs3GUpdate() {
        return (this.f & PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID) != 0;
    }

    public String genMidPath() {
        return this.name + WVNativeCallbackUtil.SEPERATER + this.v;
    }

    public String getNameandVersion() {
        return this.name + "_" + this.v;
    }

    public String getNameAndSeq() {
        return this.name + "-" + this.installedSeq;
    }

    public boolean isAppInstalled() {
        return 0 != this.installedSeq;
    }

    public boolean equals(ZipAppInfo object) {
        if (this.v != null && object != null && object.v != null && !this.v.equals(object.v)) {
            return false;
        }
        if (object == null || this.s == object.s) {
            return true;
        }
        return false;
    }

    public String getZipUrl() {
        if (TextUtils.isEmpty(this.z)) {
            if (TextUtils.isEmpty(WVCommonConfig.commonConfig.packageZipPrefix)) {
                switch (GlobalConfig.env) {
                    case ONLINE:
                        this.z = "http://gw.alicdn.com/L1/429/49823646/";
                        break;
                    case PRE:
                        this.z = "http://img1.tbcdn.cn/L1/429/49823646/";
                        break;
                    case DAILY:
                        this.z = "http://img1.daily.taobaocdn.net/L1/429/1286354249/";
                        break;
                    default:
                        this.z = "http://gw.alicdn.com/L1/429/49823646/";
                        break;
                }
            } else {
                this.z = WVCommonConfig.commonConfig.packageZipPrefix;
            }
        }
        StringBuilder builder = new StringBuilder(this.z);
        if ('/' != builder.charAt(builder.length() - 1)) {
            builder.append(WVNativeCallbackUtil.SEPERATER);
        }
        builder.append("app/");
        builder.append(this.name);
        builder.append("/app-");
        builder.append(this.s);
        if (GlobalConfig.env.equals(EnvEnum.PRE)) {
            builder.append(".zip.awppre.awpbak");
        } else if (this.isPreViewApp) {
            builder.append(".zip.awpbak");
        } else {
            if (this.v.equals(this.installedVersion)) {
                builder.append("-incr");
            }
            builder.append(".zip");
        }
        return builder.toString();
    }
}
