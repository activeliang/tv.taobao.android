package com.taobao.securityjni;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SGJpgProcess {
    private static String FINISHED_FILE_NAME = "SGJpgProcessFinished";
    private static String JPG_DIR_PREFIX = "jpgs_";
    private static String JPG_PREFIX = "yw_1222";
    private static String ROOT_FOLDER = "SGLib";
    private Context ctx;

    public SGJpgProcess(Context context) {
        this.ctx = context;
    }

    private boolean isPathSecurityValid(String path) {
        if (Pattern.compile("\\S*(\\.\\.)+(%)*\\S*").matcher(path).find()) {
            return false;
        }
        return true;
    }

    private boolean unzipByPrefix(String zipFilePath, String targetPath, String prefix) {
        int f;
        ZipInputStream zin = null;
        BufferedInputStream bin = null;
        if (targetPath != null) {
            try {
                if (!"".equals(targetPath)) {
                    ZipInputStream zin2 = new ZipInputStream(new FileInputStream(zipFilePath));
                    try {
                        BufferedInputStream bin2 = new BufferedInputStream(zin2);
                        File file = null;
                        while (true) {
                            try {
                                ZipEntry entry = zin2.getNextEntry();
                                if (entry != null) {
                                    String path = entry.getName();
                                    if (isPathSecurityValid(path) && (f = path.indexOf(prefix)) >= 0) {
                                        FileOutputStream unzipout = null;
                                        BufferedOutputStream unzipbout = null;
                                        try {
                                            File file2 = new File(targetPath, path.substring(f));
                                            try {
                                                FileOutputStream unzipout2 = new FileOutputStream(file2);
                                                try {
                                                    BufferedOutputStream unzipbout2 = new BufferedOutputStream(unzipout2);
                                                    while (true) {
                                                        try {
                                                            int count = bin2.read();
                                                            if (count != -1) {
                                                                unzipbout2.write(count);
                                                            } else {
                                                                try {
                                                                    break;
                                                                } catch (Exception e) {
                                                                }
                                                            }
                                                        } catch (Exception e2) {
                                                            unzipbout = unzipbout2;
                                                            unzipout = unzipout2;
                                                            try {
                                                                unzipbout.close();
                                                                unzipout.close();
                                                            } catch (Exception e3) {
                                                            }
                                                            try {
                                                                zin2.close();
                                                                bin2.close();
                                                            } catch (Exception e4) {
                                                            }
                                                            BufferedInputStream bufferedInputStream = bin2;
                                                            ZipInputStream zipInputStream = zin2;
                                                            return false;
                                                        } catch (Throwable th) {
                                                            th = th;
                                                            unzipbout = unzipbout2;
                                                            unzipout = unzipout2;
                                                            try {
                                                                unzipbout.close();
                                                                unzipout.close();
                                                            } catch (Exception e5) {
                                                            }
                                                            throw th;
                                                        }
                                                    }
                                                    unzipbout2.close();
                                                    unzipout2.close();
                                                    file = file2;
                                                } catch (Exception e6) {
                                                    unzipout = unzipout2;
                                                    unzipbout.close();
                                                    unzipout.close();
                                                    zin2.close();
                                                    bin2.close();
                                                    BufferedInputStream bufferedInputStream2 = bin2;
                                                    ZipInputStream zipInputStream2 = zin2;
                                                    return false;
                                                } catch (Throwable th2) {
                                                    th = th2;
                                                    unzipout = unzipout2;
                                                    unzipbout.close();
                                                    unzipout.close();
                                                    throw th;
                                                }
                                            } catch (Exception e7) {
                                                unzipbout.close();
                                                unzipout.close();
                                                zin2.close();
                                                bin2.close();
                                                BufferedInputStream bufferedInputStream22 = bin2;
                                                ZipInputStream zipInputStream22 = zin2;
                                                return false;
                                            } catch (Throwable th3) {
                                                th = th3;
                                                unzipbout.close();
                                                unzipout.close();
                                                throw th;
                                            }
                                        } catch (Exception e8) {
                                            File file3 = file;
                                            unzipbout.close();
                                            unzipout.close();
                                            zin2.close();
                                            bin2.close();
                                            BufferedInputStream bufferedInputStream222 = bin2;
                                            ZipInputStream zipInputStream222 = zin2;
                                            return false;
                                        } catch (Throwable th4) {
                                            th = th4;
                                            File file4 = file;
                                            unzipbout.close();
                                            unzipout.close();
                                            throw th;
                                        }
                                    }
                                } else {
                                    try {
                                        break;
                                    } catch (Exception e9) {
                                    }
                                }
                            } catch (IOException e10) {
                                bin = bin2;
                                zin = zin2;
                            } catch (Throwable th5) {
                                th = th5;
                                bin = bin2;
                                zin = zin2;
                                try {
                                    zin.close();
                                    bin.close();
                                } catch (Exception e11) {
                                }
                                throw th;
                            }
                        }
                        zin2.close();
                        bin2.close();
                        BufferedInputStream bufferedInputStream3 = bin2;
                        ZipInputStream zipInputStream3 = zin2;
                        return true;
                    } catch (IOException e12) {
                        zin = zin2;
                        try {
                            zin.close();
                            bin.close();
                            return false;
                        } catch (Exception e13) {
                            return false;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        zin = zin2;
                        zin.close();
                        bin.close();
                        throw th;
                    }
                }
            } catch (IOException e14) {
            } catch (Throwable th7) {
                th = th7;
                zin.close();
                bin.close();
                throw th;
            }
        }
        try {
            zin.close();
            bin.close();
            return false;
        } catch (Exception e15) {
            return false;
        }
    }

    private boolean deleteDir(File dir) {
        if (dir == null) {
            return false;
        }
        if (dir.isDirectory()) {
            String[] children = dir.list();
            int i = 0;
            while (children != null && i < children.length) {
                if (!deleteDir(new File(dir, children[i]))) {
                    return false;
                }
                i++;
            }
        }
        return dir.delete();
    }

    private boolean writeEmptyFile(File file) {
        boolean z = false;
        FileOutputStream outData = null;
        if (file == null) {
            try {
                outData.close();
            } catch (IOException e) {
            }
        } else {
            try {
                FileOutputStream outData2 = new FileOutputStream(file.getAbsolutePath());
                try {
                    outData2.close();
                    z = true;
                    try {
                        outData2.close();
                    } catch (IOException e2) {
                    }
                    FileOutputStream fileOutputStream = outData2;
                } catch (IOException e3) {
                    e = e3;
                    outData = outData2;
                    try {
                        e.printStackTrace();
                        try {
                            outData.close();
                        } catch (IOException e4) {
                        }
                        return z;
                    } catch (Throwable th) {
                        th = th;
                        try {
                            outData.close();
                        } catch (IOException e5) {
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    outData = outData2;
                    outData.close();
                    throw th;
                }
            } catch (IOException e6) {
                e = e6;
                e.printStackTrace();
                outData.close();
                return z;
            }
        }
        return z;
    }

    private static String getProcessName(Context context) {
        try {
            int pid = Process.myPid();
            if (context == null) {
                return "";
            }
            for (ActivityManager.RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName != null ? appProcess.processName : "";
                }
            }
            return "";
        } catch (Throwable th) {
            return "";
        }
    }

    private boolean isMainProcess() {
        try {
            return getProcessName(this.ctx).equals(this.ctx.getPackageName());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean unzipFinished() {
        try {
            if (this.ctx == null || !isMainProcess()) {
                return false;
            }
            String rootDir = this.ctx.getDir(ROOT_FOLDER, 0).getAbsolutePath();
            String appTimeDirPath = null;
            String preUnzipJpgsPath = null;
            String apkPath = this.ctx.getApplicationInfo().sourceDir;
            if (apkPath != null) {
                File apkFile = new File(apkPath);
                if (apkFile.exists()) {
                    appTimeDirPath = rootDir + "/app_" + (apkFile.lastModified() / 1000);
                }
                File appTimeDir = new File(appTimeDirPath);
                if (!appTimeDir.exists()) {
                    appTimeDir.mkdir();
                }
                preUnzipJpgsPath = appTimeDirPath + "/pre_unzip_jpgs";
                File preUnzipDir = new File(preUnzipJpgsPath);
                if (!preUnzipDir.exists()) {
                    preUnzipDir.mkdir();
                }
            }
            File finishedFile = new File(preUnzipJpgsPath, FINISHED_FILE_NAME);
            if (finishedFile.exists()) {
                return true;
            }
            if (!unzipByPrefix(apkPath, preUnzipJpgsPath, JPG_PREFIX)) {
                return false;
            }
            return writeEmptyFile(finishedFile);
        } catch (Exception e) {
            return false;
        }
    }
}
