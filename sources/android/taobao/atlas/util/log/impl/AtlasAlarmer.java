package android.taobao.atlas.util.log.impl;

import android.taobao.atlas.util.log.IAlarmer;

public class AtlasAlarmer implements IAlarmer {
    private static IAlarmer externalMonitor;
    private static AtlasAlarmer singleton;

    public static synchronized AtlasAlarmer getInstance() {
        AtlasAlarmer atlasAlarmer;
        synchronized (AtlasAlarmer.class) {
            if (singleton == null) {
                singleton = new AtlasAlarmer();
            }
            atlasAlarmer = singleton;
        }
        return atlasAlarmer;
    }

    public static void setExternalAlarmer(IAlarmer alarmer) {
        externalMonitor = alarmer;
    }

    public void commitFail(String module, String monitorPoint, String errorCode, String errorMsg) {
    }

    public void commitSuccess(String module, String monitorPoint) {
    }
}
