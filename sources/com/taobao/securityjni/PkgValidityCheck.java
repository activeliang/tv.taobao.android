package com.taobao.securityjni;

import android.content.Context;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.pkgvaliditycheck.IPkgValidityCheckComponent;

@Deprecated
public class PkgValidityCheck {
    public static int FLAG_DEX_FILE = 1;
    public static int FLAG_DEX_MANIFEST = 0;

    public PkgValidityCheck(Context context) {
    }

    public String getDexHash(String timestamp, String saltKey, int sourceFlag) {
        IPkgValidityCheckComponent com2 = SecurityGuardManager.getInstance(GlobalInit.getGlobalContext()).getPackageValidityCheckComp();
        if (com2 != null) {
            return com2.getDexHash(timestamp, saltKey, sourceFlag);
        }
        return null;
    }

    public boolean isPackageValid(String absolutePkgPath) {
        return false;
    }

    public int checkEnvAndFiles(String... filePath) {
        return 0;
    }
}
