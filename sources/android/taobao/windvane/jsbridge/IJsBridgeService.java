package android.taobao.windvane.jsbridge;

public interface IJsBridgeService {
    Class<? extends WVApiPlugin> getBridgeClass(String str);
}
