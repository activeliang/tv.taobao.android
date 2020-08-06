package android.taobao.windvane.packageapp.cleanup;

public class InfoSnippet {
    public long count = 0;
    public long lastAccessTime = 0;
    public String name = "";

    public InfoSnippet() {
    }

    public InfoSnippet(String name2, long count2, long lastAccessTime2) {
        this.name = name2;
        this.count = count2;
        this.lastAccessTime = lastAccessTime2;
    }

    public String toString() {
        return "InfoSnippet{name='" + this.name + '\'' + ", lastAccessTime=" + this.lastAccessTime + ", count=" + this.count + '}';
    }
}
