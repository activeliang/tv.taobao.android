package com.edge.pcdn;

import android.content.Context;
import com.bftv.fui.constantplugin.Constant;
import java.io.File;
import java.io.Serializable;

public class PcdnManager implements Serializable {
    private static AccMgr accMgr = new AccMgr();
    private static int cacheDirType = 1;
    private static LiveMgr liveMgr = new LiveMgr();
    private static final long serialVersionUID = 1;

    public static int start(Context context, String type, String clientId, String cacheDir, String pid, String ext) {
        String typeparam;
        if (type == null) {
            typeparam = Constant.NULL;
        } else {
            typeparam = type;
        }
        PcdnLog.i(" pcdnManager pcdnStart invoked,type：" + typeparam);
        ConfigManager.init(context);
        int rs = -1;
        if (context == null || clientId == null || type == null) {
            try {
                PcdnLog.i(" pcdnManager pcdnStart invoke failed,context or id or type null");
                return -1;
            } catch (Throwable throwable) {
                PcdnLog.i(" pcdnManager pcdnStart error");
                PcdnLog.w(PcdnLog.toString(throwable));
            }
        } else {
            if (ext == null) {
                ext = "";
            }
            String ext2 = ext.trim();
            String ext3 = ext2 + ("".equals(ext2) ? "apppackage=" + context.getPackageName() : "&apppackage=" + context.getPackageName());
            if (cacheDir == null || "".equals(cacheDir)) {
                cacheDir = getCachePath(context, clientId);
            }
            String ext4 = ext3 + "&cachepath-type=" + cacheDirType;
            if (pid == null || "".equals(pid)) {
                pid = "1";
            }
            if (PcdnType.VOD.equals(type) || type.equals(PcdnType.DOWN)) {
                rs = accMgr.start(context, clientId, cacheDir, pid, ext4);
                return rs;
            }
            if (PcdnType.LIVE.equals(type)) {
                rs = liveMgr.start(context, clientId, cacheDir, pid, ext4);
            }
            return rs;
        }
    }

    public static String PCDNAddress(String type, String url) {
        if (type == null || url == null) {
            PcdnLog.e("PCDNAddress：params cannot be null");
            return "";
        }
        PcdnLog.i(" pcdnManager pcdnAddress invoked：" + type);
        return PCDNAddress(type, url, 0, "");
    }

    public static String PCDNAddress(String type, String url, int rank, String ext) {
        if (type == null || url == null) {
            PcdnLog.e("PCDNAddress：params cannot be null");
            return "";
        } else if (PcdnType.VOD.equals(type) || PcdnType.DOWN.equals(type)) {
            return accMgr.pcdnAddress(url, type, rank, ext);
        } else {
            if (PcdnType.LIVE.equals(type)) {
                return liveMgr.pcdnAddress(url, type, rank, ext);
            }
            return url;
        }
    }

    public static String getVersion(String type) {
        if (type == null) {
            PcdnLog.e("getVersion：params cannot be null");
            return "";
        }
        PcdnLog.i(" pcdnManager getVersion invoked：" + type);
        if (PcdnType.VOD.equals(type) || PcdnType.DOWN.equals(type)) {
            return accMgr.getVersion();
        }
        if (PcdnType.LIVE.equals(type)) {
            return liveMgr.getVersion();
        }
        return "";
    }

    public static String PCDNGet(String type, String key, String defaultValue) {
        if (type == null || key == null) {
            PcdnLog.e("PCDNGet：params cannot be null");
            return defaultValue;
        }
        PcdnLog.i(" pcdnManager pcdnGet invoked：" + type);
        String getValue = null;
        if (PcdnType.VOD.equals(type) || PcdnType.DOWN.equals(type)) {
            getValue = accMgr.pcdnGet(key);
        } else if (PcdnType.LIVE.equals(type)) {
            getValue = liveMgr.pcdnGet(key);
        }
        if (getValue == null || "".equals(getValue)) {
            return defaultValue;
        }
        return getValue;
    }

    public static int PCDNSet(String type, String keyValue) {
        if (type == null || keyValue == null) {
            PcdnLog.e("PCDNSet：params cannot be null");
            return -2;
        }
        PcdnLog.i(" pcdnManager pcdnSet invoked：" + type);
        if (PcdnType.VOD.equals(type) || PcdnType.DOWN.equals(type)) {
            return accMgr.pcdnSet(keyValue);
        }
        if (PcdnType.LIVE.equals(type)) {
            return liveMgr.pcdnSet(keyValue);
        }
        return -2;
    }

    public static int stop(String type) {
        if (type == null) {
            PcdnLog.e("stop：params cannot be null");
            return -1;
        }
        PcdnLog.i(" pcdnManager pcdnStop invoked：" + type);
        if (PcdnType.VOD.equals(type) || PcdnType.DOWN.equals(type)) {
            try {
                accMgr.pcdnStop();
            } catch (Error e) {
                return -1;
            } catch (Exception e2) {
                return -1;
            }
        } else if (PcdnType.LIVE.equals(type)) {
            try {
                liveMgr.pcdnStop();
            } catch (Error e3) {
                return -1;
            } catch (Exception e4) {
                return -1;
            }
        }
        return 0;
    }

    public static int exit(String type) {
        if (type == null) {
            PcdnLog.e("stop：params cannot be null");
            return -1;
        }
        PcdnLog.i(" pcdnManager pcdnExit invoked：" + type);
        if (PcdnType.VOD.equals(type) || PcdnType.DOWN.equals(type)) {
            try {
                accMgr.pcdnExit();
            } catch (Error e) {
                return -1;
            } catch (Exception e2) {
                return -1;
            }
        } else if (PcdnType.LIVE.equals(type)) {
            try {
                liveMgr.pcdnExit();
                return -1;
            } catch (Error e3) {
                return -1;
            } catch (Exception e4) {
                return -1;
            }
        }
        return 0;
    }

    private static String getCachePath(Context context, String cid) {
        File cachePath = context.getExternalCacheDir();
        String folder = "60000000";
        if (cid != null && cid.trim().length() >= 8) {
            folder = cid.substring(0, 8);
        }
        PcdnLog.d("cachePath:" + folder);
        if (cachePath == null) {
            cachePath = context.getCacheDir();
            cacheDirType = 0;
        }
        File cachePath2 = new File(cachePath.getAbsolutePath() + File.separator + folder + File.separator);
        if (!cachePath2.exists()) {
            cachePath2.mkdirs();
        }
        return cachePath2.getAbsolutePath();
    }
}
