package com.alibaba.motu.crashreporter;

public class ReporterConfigure {
    public boolean closeMainLooperMonitor = false;
    public int enabeANRTimeoutInterval = 5000;
    public long enabeMainLooperTimeoutInterval = 5000;
    public boolean enableANRMainThreadOnly = false;
    public boolean enableAbortCount = false;
    public boolean enableActivityMonitor = true;
    public boolean enableBreakPadDump = false;
    public boolean enableCatchANRException = true;
    public boolean enableCatchNativeException = true;
    public boolean enableCatchUncaughtException = true;
    public boolean enableDebug = false;
    public boolean enableDeduplication = false;
    public boolean enableDumpAllThread = false;
    public boolean enableDumpAppLog = false;
    public boolean enableDumpEventsLog = false;
    public boolean enableDumpRadioLog = false;
    public boolean enableDumpSysLog = false;
    public boolean enableExternalLinster = true;
    public boolean enableFinalizeFake = true;
    public int enableMaxThreadNumber = 200;
    public int enableMaxThreadStackTraceNumber = 64;
    public boolean enableStartCount = true;
    public int enableSysLogcatLinkMaxCount = 100;
    public int enableSysLogcatMaxCount = 100;
    public boolean enableUIProcessSafeGuard = false;
    public boolean enableUncaughtExceptionIgnore = true;
    public boolean isCloseMainLooperSampling = false;
    public int sendOnLaunchDelay = 0;

    public void setEnableCatchANRException(boolean enableCatchANRException2) {
        this.enableCatchANRException = enableCatchANRException2;
    }

    public void setEnableDumpSysLog(boolean enableDumpSysLog2) {
        this.enableDumpSysLog = enableDumpSysLog2;
    }

    public void setEnableDumpEventsLog(boolean enableDumpEventsLog2) {
        this.enableDumpEventsLog = enableDumpEventsLog2;
    }

    public void setEnableDumpRadioLog(boolean enableDumpRadioLog2) {
        this.enableDumpRadioLog = enableDumpRadioLog2;
    }

    public void setEnableDumpAppLog(boolean enableDumpAppLog2) {
        this.enableDumpAppLog = enableDumpAppLog2;
    }

    public void setEnableDumpAllThread(boolean enableDumpAllThread2) {
        this.enableDumpAllThread = enableDumpAllThread2;
    }

    public void setEnableDebug(boolean enableDebug2) {
        this.enableDebug = enableDebug2;
    }

    public void setEnableANRMainThreadOnly(boolean enableANRMainThreadOnly2) {
        this.enableANRMainThreadOnly = enableANRMainThreadOnly2;
    }
}
