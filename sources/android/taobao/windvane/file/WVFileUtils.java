package android.taobao.windvane.file;

import android.taobao.windvane.util.TaoLog;
import java.io.File;
import java.util.ArrayList;

public class WVFileUtils {
    private static final String TAG = "WVFileUtils";

    public static ArrayList<String> getFileListbyDir(File file) {
        TaoLog.i(TAG, file.getPath());
        ArrayList<String> fileList = new ArrayList<>();
        getFileList(file, fileList);
        return fileList;
    }

    private static void getFileList(File path, ArrayList<String> fileList) {
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File fileList2 : files) {
                    getFileList(fileList2, fileList);
                }
                return;
            }
            return;
        }
        fileList.add(path.getAbsolutePath());
    }
}
