package com.taobao.securityjni;

import android.content.ContextWrapper;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.rootdetect.IRootDetectComponent;
import com.taobao.wireless.security.sdk.simulatordetect.ISimulatorDetectComponent;

@Deprecated
public class EnvironmentDetector {
    private ContextWrapper context;

    public EnvironmentDetector(ContextWrapper context2) {
        this.context = context2;
    }

    public boolean isRoot() {
        IRootDetectComponent comRoot = SecurityGuardManager.getInstance(this.context).getRootDetectComp();
        if (comRoot != null) {
            return comRoot.isRoot();
        }
        return false;
    }

    public boolean isSimulator() {
        ISimulatorDetectComponent comSimulator = SecurityGuardManager.getInstance(this.context).getSimulatorDetectComp();
        if (comSimulator != null) {
            return comSimulator.isSimulator();
        }
        return false;
    }
}
