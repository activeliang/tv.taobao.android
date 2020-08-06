package mtopsdk.mtop.global.init;

import java.lang.reflect.Constructor;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.intf.Mtop;

public class MtopInitTaskFactory {
    private static final String INNER_MTOP_INIT_TASK = "mtopsdk.mtop.global.init.InnerMtopInitTask";
    private static final String OPEN_MTOP_INIT_TASK = "mtopsdk.mtop.global.init.OpenMtopInitTask";
    private static final String PRODUCT_MTOP_INIT_TASK = "mtopsdk.mtop.global.init.ProductMtopInitTask";
    private static final String TAG = "mtopsdk.MtopInitTaskFactory";

    public static IMtopInitTask getMtopInitTask(String instanceId) {
        if (instanceId == null) {
            return null;
        }
        char c = 65535;
        switch (instanceId.hashCode()) {
            case 2432586:
                if (instanceId.equals(Mtop.Id.OPEN)) {
                    c = 1;
                    break;
                }
                break;
            case 69817910:
                if (instanceId.equals(Mtop.Id.INNER)) {
                    c = 0;
                    break;
                }
                break;
            case 408508623:
                if (instanceId.equals(Mtop.Id.PRODUCT)) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return newInstance(INNER_MTOP_INIT_TASK);
            case 1:
                return newInstance(OPEN_MTOP_INIT_TASK);
            case 2:
                return newInstance(PRODUCT_MTOP_INIT_TASK);
            default:
                return newInstance(INNER_MTOP_INIT_TASK);
        }
    }

    private static IMtopInitTask newInstance(String clazzName) {
        try {
            Constructor<?> constructor = Class.forName(clazzName).getDeclaredConstructor(new Class[0]);
            constructor.setAccessible(true);
            return (IMtopInitTask) constructor.newInstance(new Object[0]);
        } catch (Throwable th) {
            TBSdkLog.e(TAG, "reflect IMtopInitTask instance error.clazzName=" + clazzName);
            return null;
        }
    }
}
