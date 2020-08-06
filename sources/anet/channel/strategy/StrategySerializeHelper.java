package anet.channel.strategy;

import android.content.Context;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.statist.StrategyStatObject;
import anet.channel.util.ALog;
import anet.channel.util.SerializeHelper;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.uc.webview.export.internal.interfaces.IWaStat;
import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

class StrategySerializeHelper {
    private static final String DEFAULT_STRATEGY_FOLDER_NAME = "awcn_strategy";
    private static final long MAX_AVAILABLE_PERIOD = 259200000;
    private static final long MAX_FILE_NUM = 10;
    private static final String TAG = "awcn.StrategySerializeHelper";
    private static Comparator<File> comparator = new Comparator<File>() {
        public int compare(File file, File file2) {
            return (int) (file2.lastModified() - file.lastModified());
        }
    };
    private static File strategyFolder = null;
    private static volatile boolean toDelete = false;

    StrategySerializeHelper() {
    }

    public static void initialize(Context context) {
        if (context != null) {
            try {
                strategyFolder = new File(context.getFilesDir(), DEFAULT_STRATEGY_FOLDER_NAME);
                if (!checkFolderExistOrCreate(strategyFolder)) {
                    ALog.e(TAG, "create directory failed!!!", (String) null, IWaStat.KEY_DIR, strategyFolder.getAbsolutePath());
                }
                if (!GlobalAppRuntimeInfo.isTargetProcess()) {
                    String process = GlobalAppRuntimeInfo.getCurrentProcess();
                    strategyFolder = new File(strategyFolder, process.substring(process.indexOf(58) + 1));
                    if (!checkFolderExistOrCreate(strategyFolder)) {
                        ALog.e(TAG, "create directory failed!!!", (String) null, IWaStat.KEY_DIR, strategyFolder.getAbsolutePath());
                    }
                }
                ALog.i(TAG, "StrateyFolder", (String) null, TuwenConstants.PARAMS.SKU_PATH, strategyFolder.getAbsolutePath());
                if (toDelete) {
                    clearStrategyFolder();
                    toDelete = false;
                    return;
                }
                removeInvalidFile();
            } catch (Throwable e) {
                ALog.e(TAG, "StrategySerializeHelper initialize failed!!!", (String) null, e, new Object[0]);
            }
        }
    }

    private static boolean checkFolderExistOrCreate(File file) {
        if (file == null || file.exists()) {
            return true;
        }
        return file.mkdir();
    }

    public static File getStrategyFile(String filename) {
        checkFolderExistOrCreate(strategyFolder);
        return new File(strategyFolder, filename);
    }

    static synchronized void clearStrategyFolder() {
        synchronized (StrategySerializeHelper.class) {
            ALog.i(TAG, "clear start.", (String) null, new Object[0]);
            if (strategyFolder == null) {
                ALog.w(TAG, "folder path not initialized, wait to clear", (String) null, new Object[0]);
                toDelete = true;
            } else {
                File[] files = strategyFolder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile()) {
                            file.delete();
                        }
                    }
                    ALog.i(TAG, "clear end.", (String) null, new Object[0]);
                }
            }
        }
    }

    static synchronized File[] getSortedFiles() {
        File[] files;
        synchronized (StrategySerializeHelper.class) {
            if (strategyFolder == null) {
                files = null;
            } else {
                files = strategyFolder.listFiles();
                if (files != null) {
                    Arrays.sort(files, comparator);
                }
            }
        }
        return files;
    }

    static synchronized void removeInvalidFile() {
        int numOfTables;
        synchronized (StrategySerializeHelper.class) {
            File[] files = getSortedFiles();
            if (files != null) {
                int i = 0;
                int numOfTables2 = 0;
                while (i < files.length) {
                    File file = files[i];
                    if (file.isDirectory()) {
                        numOfTables = numOfTables2;
                    } else if (System.currentTimeMillis() - file.lastModified() >= MAX_AVAILABLE_PERIOD) {
                        file.delete();
                        numOfTables = numOfTables2;
                    } else if (file.getName().startsWith("WIFI")) {
                        numOfTables = numOfTables2 + 1;
                        if (((long) numOfTables2) > MAX_FILE_NUM) {
                            file.delete();
                        }
                    } else {
                        numOfTables = numOfTables2;
                    }
                    i++;
                    numOfTables2 = numOfTables;
                }
            }
        }
    }

    static synchronized void persist(Serializable serializable, String filename, StrategyStatObject sso) {
        synchronized (StrategySerializeHelper.class) {
            SerializeHelper.persist(serializable, getStrategyFile(filename), sso);
        }
    }

    static synchronized <T> T restore(String filename, StrategyStatObject sso) {
        T restore;
        synchronized (StrategySerializeHelper.class) {
            restore = SerializeHelper.restore(getStrategyFile(filename), sso);
        }
        return restore;
    }
}
