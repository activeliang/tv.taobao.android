package com.zhiping.dev.android.logcat;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.zhiping.dev.android.logcat.config.IExtCfg;
import com.zhiping.dev.android.logcat.config.IFileNameCfg;
import com.zhiping.dev.android.logcat.config.IOssClientCfg;
import com.zhiping.dev.android.logcat.config.IOssStorageCfg;
import com.zhiping.dev.android.logcat.config.ISaveCfg;
import com.zhiping.dev.android.oss.IClient;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.apache.commons.codec.binary.Base32;

class LogcatConfig implements ISaveCfg, IOssClientCfg, IOssStorageCfg, IFileNameCfg, IExtCfg {
    private static String TAG = LogcatConfig.class.getSimpleName();
    private Context context;
    private IExtCfg extCfg;
    private IFileNameCfg fileNameCfg;
    private IOssClientCfg ossCfg;
    private IOssStorageCfg ossStorageCfg;
    private ISaveCfg saveCfg;

    private LogcatConfig() {
    }

    public LogcatConfig(Context tmp) {
        this.context = tmp.getApplicationContext();
    }

    public void clearCfg() {
        this.context = null;
        this.saveCfg = null;
        this.extCfg = null;
        this.fileNameCfg = null;
        this.ossStorageCfg = null;
        this.ossCfg = null;
    }

    /* access modifiers changed from: package-private */
    public void appendExtInfo(File file, String... extParams) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(file, true));
            if (extParams != null && extParams.length > 0) {
                printWriter.println("--------------------------------------------");
                for (String println : extParams) {
                    printWriter.println(println);
                }
            }
            printWriter.println("");
            printWriter.println("--------------------------------------------");
            printWriter.println("deviceId:" + getDeviceId());
            printWriter.println("userId:" + getUserId());
            printWriter.println("userName:" + getUserName());
            printWriter.println("appVersionCode:" + getAppVersionCode());
            printWriter.println("appVersionName:" + getAppVersionName());
            printWriter.println("appBuildSeq:" + getAppBuildSeq());
            printWriter.flush();
            printWriter.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public List<File> listLogs() {
        try {
            File logDir = getLogDir();
            List<File> logFiles = Arrays.asList(logDir.listFiles());
            Collections.sort(logFiles, new Comparator<File>() {
                public int compare(File o1, File o2) {
                    return o2.getName().compareTo(o1.getName());
                }
            });
            for (int i = logFiles.size() - 1; i > 4; i--) {
                logFiles.get(i).delete();
            }
            return Arrays.asList(logDir.listFiles());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public LogcatConfig setSaveCfg(ISaveCfg saveCfg2) {
        this.saveCfg = saveCfg2;
        return this;
    }

    public LogcatConfig setExtCfg(IExtCfg extCfg2) {
        this.extCfg = extCfg2;
        return this;
    }

    public LogcatConfig setFileNameCfg(IFileNameCfg fileNameCfg2) {
        this.fileNameCfg = fileNameCfg2;
        return this;
    }

    public LogcatConfig setOssCfg(IOssClientCfg ossCfg2) {
        this.ossCfg = ossCfg2;
        return this;
    }

    public LogcatConfig setOssStorageCfg(IOssStorageCfg ossStorageCfg2) {
        this.ossStorageCfg = ossStorageCfg2;
        return this;
    }

    public static String getFitSize(long size) {
        float KB = ((float) size) / 1024.0f;
        float MB = ((float) size) / 1048576.0f;
        float GB = ((float) size) / 1.07374182E9f;
        if (GB > 1.0f) {
            return String.format("%.2fGB", new Object[]{Float.valueOf(GB)});
        } else if (MB > 1.0f) {
            return String.format("%.2fMB", new Object[]{Float.valueOf(MB)});
        } else {
            return String.format("%.2fKB", new Object[]{Float.valueOf(KB)});
        }
    }

    public String getBucketName() {
        if (this.ossStorageCfg != null) {
            try {
                String rtn = this.ossStorageCfg.getBucketName();
                Log.d(TAG, "use outer getBucketName :" + rtn);
                return rtn;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "use inner getBucketName :" + "alibaba-tv-log");
        return "alibaba-tv-log";
    }

    public String getObjectKey(File file) {
        if (this.ossStorageCfg != null) {
            try {
                String rtn = this.ossStorageCfg.getObjectKey(file);
                Log.d(TAG, "use outer getObjectKey :" + rtn);
                return rtn;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String rtn2 = "logcat" + File.separator + file.getName();
        Log.d(TAG, "use inner getObjectKey :" + rtn2);
        return rtn2;
    }

    public File getLogDir() {
        File dumpLogPath;
        if (this.saveCfg != null) {
            try {
                File rtn = this.saveCfg.getLogDir();
                Log.d(TAG, "use outer saveCfg getLogDir :" + rtn.getPath());
                return rtn;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (Environment.getExternalStorageState() == "mounted") {
            dumpLogPath = new File(Environment.getExternalStorageDirectory() + File.separator + "zplog");
        } else {
            dumpLogPath = new File(this.context.getFilesDir().getAbsolutePath() + File.separator + "zplog");
        }
        if (!dumpLogPath.exists()) {
            dumpLogPath.mkdirs();
        }
        Log.d(TAG, "use inner saveCfg getLogDir :" + dumpLogPath.getPath());
        return dumpLogPath;
    }

    public IClient getOssClient() {
        if (this.ossCfg != null) {
            try {
                IClient rtn = this.ossCfg.getOssClient();
                Log.d(TAG, "use outer ossCfg getOssClient :" + rtn.hashCode());
                return rtn;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "use inner ossCfg getOssClient :" + null);
        return null;
    }

    public String genLogFileName() {
        if (this.fileNameCfg != null) {
            try {
                String rtn = this.fileNameCfg.genLogFileName();
                Log.d(TAG, "use outer fileNameCfg genLogFileName :" + rtn);
                return rtn;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String userName = "" + getUserName();
        try {
            userName = new Base32().encodeAsString(userName.getBytes());
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        String rtn2 = date + "_" + userName;
        Log.d(TAG, "use inner fileNameCfg genLogFileName :" + rtn2);
        return rtn2;
    }

    public String getDeviceId() {
        if (this.extCfg != null) {
            try {
                String rtn = this.extCfg.getDeviceId();
                Log.d(TAG, "use outer extCfg getDeviceId :" + rtn);
                return rtn;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "use inner extCfg getDeviceId :" + null);
        return null;
    }

    public String getUserId() {
        if (this.extCfg != null) {
            try {
                String rtn = this.extCfg.getUserId();
                Log.d(TAG, "use outer extCfg getUserId :" + rtn);
                return rtn;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "use inner extCfg getUserId :" + null);
        return null;
    }

    public String getUserName() {
        if (this.extCfg != null) {
            try {
                String rtn = this.extCfg.getUserName();
                Log.d(TAG, "use outer extCfg getUserName :" + rtn);
                return rtn;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "use inner extCfg getUserName :" + null);
        return null;
    }

    public String getAppVersionName() {
        if (this.extCfg != null) {
            try {
                String rtn = this.extCfg.getAppVersionName();
                Log.d(TAG, "use outer extCfg getAppVersionName :" + rtn);
                return rtn;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "use inner extCfg getAppVersionName :" + null);
        return null;
    }

    public String getAppVersionCode() {
        if (this.extCfg != null) {
            try {
                String rtn = this.extCfg.getAppVersionCode();
                Log.d(TAG, "use outer extCfg getAppVersionCode :" + rtn);
                return rtn;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "use inner extCfg getAppVersionCode :" + null);
        return null;
    }

    public String getAppChannel() {
        if (this.extCfg != null) {
            try {
                String rtn = this.extCfg.getAppChannel();
                Log.d(TAG, "use outer extCfg getAppChannel :" + rtn);
                return rtn;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "use inner extCfg getAppChannel :" + null);
        return null;
    }

    public String getAppBuildSeq() {
        if (this.extCfg != null) {
            try {
                String rtn = this.extCfg.getAppBuildSeq();
                Log.d(TAG, "use outer extCfg getAppBuildSeq :" + rtn);
                return rtn;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "use inner extCfg getAppBuildSeq :" + null);
        return null;
    }
}
