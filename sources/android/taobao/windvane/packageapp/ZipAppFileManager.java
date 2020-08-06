package android.taobao.windvane.packageapp;

import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.file.FileAccesser;
import android.taobao.windvane.file.FileManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.util.TaoLog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ZipAppFileManager {
    private static ZipAppFileManager zipAppFileManager;
    private String TAG = "PackageApp-ZipAppFileManager";

    public static ZipAppFileManager getInstance() {
        if (zipAppFileManager == null) {
            synchronized (ZipAppFileManager.class) {
                if (zipAppFileManager == null) {
                    zipAppFileManager = new ZipAppFileManager();
                }
            }
        }
        return zipAppFileManager;
    }

    public boolean createZipAppInitDir() {
        if (GlobalConfig.context == null) {
            return false;
        }
        File fileDir = FileManager.createFolder(GlobalConfig.context, ZipAppConstants.ZIPAPP_ROOT_APPS_DIR);
        TaoLog.d(this.TAG, "createDir: dir[" + fileDir.getAbsolutePath() + "]:" + fileDir.exists());
        if (!fileDir.exists()) {
            return false;
        }
        File fileDir2 = FileManager.createFolder(GlobalConfig.context, ZipAppConstants.ZIPAPP_ROOT_TMP_DIR);
        TaoLog.d(this.TAG, "createDir: dir[" + fileDir2.getAbsolutePath() + "]:" + fileDir2.exists());
        return fileDir2.exists();
    }

    public String readZcacheConfig(boolean isTmp) {
        return readFile(getZcacheConfigPath(isTmp));
    }

    public String readGlobalConfig(boolean isTmp) {
        return readFile(getGlobalConfigPath(isTmp));
    }

    public boolean saveGlobalConfig(byte[] fileData, boolean isTmp) {
        return saveFile(getGlobalConfigPath(isTmp), fileData);
    }

    public boolean saveZcacheConfig(byte[] fileData, boolean isTmp) {
        return saveFile(getZcacheConfigPath(isTmp), fileData);
    }

    public String readZipAppRes(ZipAppInfo appInfo, String fileName, boolean isTmp) {
        return readFile(getZipResAbsolutePath(appInfo, fileName, isTmp));
    }

    public byte[] readZipAppResByte(ZipAppInfo appInfo, String fileName, boolean isTmp) {
        return FileAccesser.read(getZipResAbsolutePath(appInfo, fileName, isTmp));
    }

    public boolean saveZipAppRes(ZipAppInfo appInfo, String fileName, byte[] fileData, boolean isTmp) {
        return saveFile(getZipResAbsolutePath(appInfo, fileName, isTmp), fileData);
    }

    public boolean unZipToTmp(ZipAppInfo appInfo, String srcFilePath) {
        FileAccesser.deleteFile(getZipRootDir(appInfo, true));
        boolean isSuccess = FileManager.unZipByFilePath(srcFilePath, getZipRootDir(appInfo, true));
        try {
            File file = new File(srcFilePath);
            if (file != null && file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
        }
        return isSuccess;
    }

    public boolean deleteHisZipApp(ZipAppInfo appInfo) {
        boolean z;
        String str = appInfo.name;
        if (ZipAppTypeEnum.ZIP_APP_TYPE_PACKAGEAPP == appInfo.getAppType()) {
            z = true;
        } else {
            z = false;
        }
        return deleteDir(getFileAbsolutePath(str, false, z), appInfo.v);
    }

    public boolean deleteZipApp(ZipAppInfo appInfo, boolean isTmp) {
        File file = new File(getFileAbsolutePath(appInfo.name, isTmp, ZipAppTypeEnum.ZIP_APP_TYPE_PACKAGEAPP == appInfo.getAppType()));
        if (file.exists()) {
            return FileAccesser.deleteFile(file);
        }
        return true;
    }

    public InputStream getPreloadInputStream(String zipFileName) {
        try {
            return GlobalConfig.context.getResources().getAssets().open(zipFileName);
        } catch (FileNotFoundException e) {
            TaoLog.i(this.TAG, "preload package not exists");
        } catch (Exception e2) {
        }
        return null;
    }

    public boolean copyZipApp(ZipAppInfo appInfo) {
        return FileManager.copyDir(getZipRootDir(appInfo, true), getZipRootDir(appInfo, false));
    }

    public boolean clearTmpDir(String path, boolean isSelf) {
        return FileAccesser.deleteFile(new File(getFileAbsolutePath(path, true, true)), isSelf);
    }

    public boolean clearAppsDir() {
        return FileAccesser.deleteFile(new File(getFileAbsolutePath((String) null, false, true)), false);
    }

    public String getRootPath() {
        if (GlobalConfig.context == null) {
            return "";
        }
        return GlobalConfig.context.getFilesDir().getAbsolutePath() + File.separator + ZipAppConstants.ZIPAPP_ROOT_DIR;
    }

    public String getRootPathTmp() {
        if (GlobalConfig.context == null) {
            return "";
        }
        return GlobalConfig.context.getFilesDir().getAbsolutePath() + File.separator + ZipAppConstants.ZIPAPP_ROOT_TMP_DIR;
    }

    public String getDownLoadPath() {
        if (GlobalConfig.context == null) {
            return "";
        }
        return GlobalConfig.context.getFilesDir().getAbsolutePath() + File.separator + ZipAppConstants.ZIPAPP_DOWNLOAD__DIR;
    }

    public String getRootPathApps() {
        if (GlobalConfig.context == null) {
            return "";
        }
        return GlobalConfig.context.getFilesDir().getAbsolutePath() + File.separator + ZipAppConstants.ZIPAPP_ROOT_APPS_DIR;
    }

    public String getGlobalConfigPath(boolean isTmp) {
        return getFileAbsolutePath(ZipAppConstants.H5_APPS_NAME, isTmp, true);
    }

    public String getZcacheConfigPath(boolean isTmp) {
        return getFileAbsolutePath(ZipAppConstants.H5_ZCACHE_MAP, isTmp, false);
    }

    public String getZipRootDir(ZipAppInfo appInfo, boolean isTmp) {
        return getFileAbsolutePath(appInfo.genMidPath(), isTmp, ZipAppTypeEnum.ZIP_APP_TYPE_PACKAGEAPP == appInfo.getAppType());
    }

    public String getZipResAbsolutePath(ZipAppInfo appInfo, String fileName, boolean isTmp) {
        return getFileAbsolutePath(appInfo.genMidPath() + File.separator + fileName, isTmp, ZipAppTypeEnum.ZIP_APP_TYPE_PACKAGEAPP == appInfo.getAppType());
    }

    public String readFile(String path) {
        try {
            if (!FileAccesser.exists(path)) {
                TaoLog.w(this.TAG, "file[" + path + "] not found");
                return null;
            }
            byte[] data = FileAccesser.read(path);
            if (data != null && data.length >= 1) {
                return new String(data, ZipAppConstants.DEFAULT_ENCODING);
            }
            TaoLog.w(this.TAG, "readConfig:[" + path + "] data is null");
            return null;
        } catch (Exception e) {
            TaoLog.e(this.TAG, "readFile:[" + path + "] exception:" + e.getMessage());
            return null;
        }
    }

    private boolean saveFile(String path, byte[] fileData) {
        try {
            return FileAccesser.write(path, ByteBuffer.wrap(fileData));
        } catch (Exception e) {
            TaoLog.e(this.TAG, "write file:[" + path + "]  exception:" + e.getMessage());
            return false;
        }
    }

    private String getFileAbsolutePath(ZipAppInfo info, boolean isTmp) {
        if (GlobalConfig.context == null) {
            return "";
        }
        return GlobalConfig.context.getFilesDir().getAbsolutePath() + File.separator + (isTmp ? ZipAppConstants.ZIPAPP_ROOT_TMP_DIR : ZipAppConstants.ZIPAPP_ROOT_APPS_DIR) + (info.genMidPath() == null ? "" : File.separator + info.genMidPath());
    }

    private String getFileAbsolutePath(String filename, boolean isTmp) {
        if (GlobalConfig.context == null) {
            return "";
        }
        return GlobalConfig.context.getFilesDir().getAbsolutePath() + File.separator + (isTmp ? ZipAppConstants.ZIPAPP_ROOT_TMP_DIR : ZipAppConstants.ZIPAPP_ROOT_APPS_DIR) + (filename == null ? "" : File.separator + filename);
    }

    private String getFileAbsolutePath(String filename, boolean isTmp, boolean isPacakageApp) {
        if (GlobalConfig.context == null) {
            return "";
        }
        StringBuilder path = new StringBuilder(128);
        path.append(GlobalConfig.context.getFilesDir().getAbsolutePath());
        path.append(File.separator);
        path.append(isTmp ? ZipAppConstants.ZIPAPP_ROOT_TMP_DIR : isPacakageApp ? ZipAppConstants.ZIPAPP_ROOT_APPS_DIR : ZipAppConstants.ZIPAPP_ROOT_ZCACHE_DIR);
        path.append(filename == null ? "" : File.separator + filename);
        return path.toString();
    }

    private boolean deleteDir(String fileDir, String notDel) {
        try {
            File file = new File(fileDir);
            if (!file.exists() || !file.isDirectory()) {
                return false;
            }
            for (File f : file.listFiles()) {
                if (!f.isDirectory()) {
                    f.delete();
                } else if (!notDel.equals(f.getName())) {
                    FileAccesser.deleteFile(f);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
