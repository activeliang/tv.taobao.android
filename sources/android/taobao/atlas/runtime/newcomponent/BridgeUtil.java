package android.taobao.atlas.runtime.newcomponent;

import android.taobao.atlas.runtime.RuntimeVariables;
import android.text.TextUtils;
import mtopsdk.common.util.SymbolExpUtil;

public class BridgeUtil {
    public static final String COMPONENT_PACKAGE = "android.taobao.atlas.runtime.newcomponent.";
    public static final String PROXY_PREFIX = "ATLASPROXY";
    public static final int TYPE_ACTIVITYBRIDGE = 1;
    public static final int TYPE_PROVIDERBRIDGE = 3;
    public static final int TYPE_SERVICEBRIDGE = 2;

    public static String getBridgeName(int type, String process) {
        switch (type) {
            case 1:
                return String.format("%s%s_%s_%s", new Object[]{COMPONENT_PACKAGE, PROXY_PREFIX, fixProcess(process), "Activity"});
            case 2:
                return String.format("%s%s_%s_%s", new Object[]{COMPONENT_PACKAGE, PROXY_PREFIX, fixProcess(process), "Service"});
            case 3:
                return String.format("%s%s_%s_%s", new Object[]{COMPONENT_PACKAGE, PROXY_PREFIX, fixProcess(process), "Provider"});
            default:
                throw new RuntimeException("wrong type");
        }
    }

    public static String fixProcess(String processName) {
        if (TextUtils.isEmpty(processName)) {
            processName = RuntimeVariables.androidApplication.getPackageName();
        }
        String prefix = RuntimeVariables.androidApplication.getPackageName() + SymbolExpUtil.SYMBOL_COLON;
        if (processName.equals(RuntimeVariables.androidApplication.getPackageName())) {
            return processName.replace(".", "_");
        }
        String childProcessPrefix = RuntimeVariables.androidApplication.getPackageName().replace(".", "_") + "_";
        if (processName.startsWith(prefix)) {
            return childProcessPrefix + processName.substring(prefix.length(), processName.length());
        }
        return childProcessPrefix + processName.replace(".", "_");
    }
}
