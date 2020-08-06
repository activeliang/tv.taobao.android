package com.alibaba.motu.crashreporter;

import android.content.Context;
import android.os.Build;
import com.alibaba.motu.crashreporter.Utils;
import com.alibaba.motu.crashreporter.ignores.FakeFinallzeExceptionIgnore;
import com.alibaba.motu.crashreporter.lab.FinalizeFake;

class LabFeatureManager {
    CatcherManager mCatcherManager;
    Configuration mConfiguration;
    Context mContext;
    FinalizeFake mFinalizeFake;

    LabFeatureManager(Context context, Configuration configuration, CatcherManager catcherManager) {
        this.mContext = context;
        this.mConfiguration = configuration;
        this.mCatcherManager = catcherManager;
        if (this.mConfiguration.getBoolean(Configuration.enableFinalizeFake, true)) {
            this.mFinalizeFake = new FinalizeFake();
            this.mCatcherManager.addUncaughtExceptionIgnore(new FakeFinallzeExceptionIgnore());
        }
    }

    /* access modifiers changed from: package-private */
    public void enable() {
        if (this.mConfiguration.getBoolean(Configuration.disableJitCompilation, true) && Build.VERSION.SDK_INT < 21) {
            Utils.VMRuntimeUtils.disableJitCompilation();
        }
        if (this.mConfiguration.getBoolean(Configuration.enableFinalizeFake, true)) {
            this.mFinalizeFake.startFakeFinalizerDaemon();
        }
    }

    /* access modifiers changed from: package-private */
    public void disable() {
        if (this.mConfiguration.getBoolean(Configuration.disableJitCompilation, true) && Build.VERSION.SDK_INT < 21) {
            Utils.VMRuntimeUtils.startJitCompilation();
        }
        if (this.mConfiguration.getBoolean(Configuration.enableFinalizeFake, true)) {
            this.mFinalizeFake.resumeFinalizerDaemon();
        }
    }
}
