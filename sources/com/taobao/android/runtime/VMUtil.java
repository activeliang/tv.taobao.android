package com.taobao.android.runtime;

import android.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class VMUtil {
    static final boolean IS_VM_ART = isVMART(System.getProperty("java.vm.version"));
    private static final String TAG = VMUtil.class.getSimpleName();
    private static final int VM_WITH_ART_VERSION_MAJOR = 2;
    private static final int VM_WITH_ART_VERSION_MINOR = 1;

    private VMUtil() {
    }

    static boolean isVMART(String versionString) {
        boolean isART = false;
        if (versionString != null) {
            Matcher matcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(versionString);
            if (matcher.matches()) {
                try {
                    int major = Integer.parseInt(matcher.group(1));
                    isART = major > 2 || (major == 2 && Integer.parseInt(matcher.group(2)) >= 1);
                } catch (NumberFormatException e) {
                }
            }
        }
        Log.i(TAG, "VM with version " + versionString + (isART ? " has ART support" : " does not have ART support"));
        return isART;
    }
}
