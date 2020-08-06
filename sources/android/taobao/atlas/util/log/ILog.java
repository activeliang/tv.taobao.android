package android.taobao.atlas.util.log;

public interface ILog {
    void d(String str, String str2);

    void e(String str, String str2);

    void e(String str, String str2, Throwable th);

    void i(String str, String str2);

    void v(String str, String str2);

    void w(String str, String str2);
}
