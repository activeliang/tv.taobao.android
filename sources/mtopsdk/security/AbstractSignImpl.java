package mtopsdk.security;

import android.support.annotation.NonNull;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.global.MtopConfig;

public abstract class AbstractSignImpl implements ISign {
    EnvModeEnum envMode = null;
    MtopConfig mtopConfig = null;

    public void init(@NonNull MtopConfig mtopConfig2) {
        this.mtopConfig = mtopConfig2;
        if (this.mtopConfig != null) {
            this.envMode = this.mtopConfig.envMode;
        }
    }

    /* access modifiers changed from: package-private */
    public int getEnv() {
        if (this.envMode == null) {
            return 0;
        }
        switch (this.envMode) {
            case PREPARE:
                return 1;
            case TEST:
            case TEST_SANDBOX:
                return 2;
            default:
                return 0;
        }
    }

    /* access modifiers changed from: package-private */
    public String getAuthCode() {
        return this.mtopConfig != null ? this.mtopConfig.authCode : "";
    }

    /* access modifiers changed from: package-private */
    public String getInstanceId() {
        return this.mtopConfig != null ? this.mtopConfig.instanceId : "";
    }

    public String getAvmpSign(String input, String authCode, int flag) {
        return null;
    }
}
