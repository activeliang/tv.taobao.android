package android.taobao.windvane.packageapp;

import android.taobao.windvane.WindvaneException;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.webkit.ValueCallback;

public interface WVPackageAppConfigInterface {
    ZipGlobalConfig getGlobalConfig();

    boolean saveLocalConfig(ZipGlobalConfig zipGlobalConfig);

    void updateGlobalConfig(boolean z, ValueCallback<ZipGlobalConfig> valueCallback, ValueCallback<WindvaneException> valueCallback2, String str, String str2);
}
