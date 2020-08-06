package android.taobao.windvane.extra.config;

import android.content.Context;
import com.taobao.orange.OrangeConfig;
import com.taobao.orange.OrangeConfigListenerV1;

public class TBConfigManager {
    public static final String ANDROID_WINDVANE_CONFIG = "android_windvane_config";
    private static volatile TBConfigManager instance = null;
    private OrangeConfigListenerV1 mConfigListenerV1 = null;

    public static TBConfigManager getInstance() {
        if (instance == null) {
            synchronized (TBConfigManager.class) {
                if (instance == null) {
                    instance = new TBConfigManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        if (this.mConfigListenerV1 == null) {
            try {
                String[] mGroupNames = {ANDROID_WINDVANE_CONFIG};
                this.mConfigListenerV1 = new TBConfigListenerV1();
                OrangeConfig.getInstance().registerListener(mGroupNames, this.mConfigListenerV1);
            } catch (Throwable th) {
                this.mConfigListenerV1 = null;
            }
        }
    }
}
