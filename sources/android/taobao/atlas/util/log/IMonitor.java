package android.taobao.atlas.util.log;

import java.util.Map;

public interface IMonitor {
    void report(String str, Map<String, Object> map, Throwable th);
}
