package android.taobao.windvane.packageapp.zipapp;

public class ZipDownloaderComparable implements Comparable<ZipDownloaderComparable> {
    private String name = "";
    private int priority = 0;

    ZipDownloaderComparable(String name2, int priority2) {
        this.name = name2;
        this.priority = priority2;
    }

    public String getAppName() {
        return this.name;
    }

    public int compareTo(ZipDownloaderComparable o) {
        return o.priority - this.priority;
    }
}
