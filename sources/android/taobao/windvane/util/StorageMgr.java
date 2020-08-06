package android.taobao.windvane.util;

import android.os.Environment;

public class StorageMgr {
    public static final int ERROR = -1;

    public static boolean checkSDCard() {
        try {
            String state = Environment.getExternalStorageState();
            if (state == null || !state.equals("mounted")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
