package android.taobao.windvane.util.log;

public interface ILog {
    void d(String str, String str2);

    void d(String str, String str2, Throwable th);

    void e(String str, String str2);

    void e(String str, String str2, Throwable th);

    void i(String str, String str2);

    void i(String str, String str2, Throwable th);

    boolean isLogLevelEnabled(int i);

    void v(String str, String str2);

    void v(String str, String str2, Throwable th);

    void w(String str, String str2);

    void w(String str, String str2, Throwable th);

    public enum LogLevelEnum {
        VERBOSE(0, "V"),
        DEBUG(1, "D"),
        INFO(2, "I"),
        WARNING(3, "W"),
        ERROR(4, "E");
        
        private int LogLevel;
        private String LogLevelName;

        private LogLevelEnum(int level, String name) {
            this.LogLevelName = name;
            this.LogLevel = level;
        }

        public String getLogLevelName() {
            return this.LogLevelName;
        }

        public int getLogLevel() {
            return this.LogLevel;
        }
    }
}
