package android.taobao.windvane.packageapp.zipapp.data;

import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;

public class AppResConfig {
    public ZipAppInfo mAppinfo;
    public Hashtable<String, FileInfo> mResfileMap = new Hashtable<>();
    public AtomicInteger resConut = new AtomicInteger(0);
    public String tk;
    public Hashtable<String, FileInfo> updateResfilesMap;

    public FileInfo getResfileInfo(String filename) {
        if (this.mResfileMap == null || !this.mResfileMap.containsKey(filename)) {
            return null;
        }
        return this.mResfileMap.get(filename);
    }

    public void setAppInfo(ZipAppInfo appinfo) {
        this.mAppinfo = appinfo;
    }

    public ZipAppInfo getAppInfo() {
        return this.mAppinfo;
    }

    public class FileInfo {
        public String path;
        public String url;
        public String v;

        public FileInfo() {
        }
    }
}
