package com.alibaba.motu.crashreporter;

import com.alibaba.motu.crashreporter.Options;

public class Configuration extends Options<Options.Option> {
    public static final String disableJitCompilation = "Configuration.disableJitCompilation";
    public static final String enableANRCatch = "Configuration.enableANRCatch";
    public static final String enableAllThreadCollection = "Configuration.enableAllThreadCollection";
    public static final String enableDumpHprof = "Configuration.enableDumpHprof";
    public static final String enableEventsLogCollection = "Configuration.enableEventsLogCollection";
    public static final String enableExternalLinster = "Configuration.enableExternalLinster";
    public static final String enableFinalizeFake = "Configuration.enableFinalizeFake";
    public static final String enableLogcatCollection = "Configuration.enableLogcatCollection";
    public static final String enableMainLoopBlockCatch = "Configuration.enableMainLoopBlockCatch";
    public static final String enableNativeExceptionCatch = "Configuration.enableNativeExceptionCatch";
    public static final String enableReportContentCompress = "Configuration.enableReportContentCompress";
    public static final String enableSafeGuard = "Configuration.enableSafeGuard";
    public static final String enableSecuritySDK = "Configuration.enableSecuritySDK";
    public static final String enableUCNativeExceptionCatch = "Configuration.enableUCNativeExceptionCatch";
    public static final String enableUIProcessSafeGuard = "Configuration.enableUIProcessSafeGuard";
    public static final String enableUncaughtExceptionCatch = "Configuration.enableUncaughtExceptionCatch";
    public static final String enableUncaughtExceptionIgnore = "Configuration.enableUncaughtExceptionIgnore";
    public static final String eventsLogLineLimit = "Configuration.eventsLogLineLimit";
    public static final String fileDescriptorLimit = "Configuration.fileDescriptorLimit";
    public static final String mainLogLineLimit = "Configuration.mainLogLineLimit";

    public Configuration() {
        super(false);
        add(new Options.Option(enableUncaughtExceptionCatch, true));
        add(new Options.Option(enableUncaughtExceptionIgnore, true));
        add(new Options.Option(enableNativeExceptionCatch, true));
        add(new Options.Option(enableUCNativeExceptionCatch, true));
        add(new Options.Option(enableANRCatch, true));
        add(new Options.Option(enableMainLoopBlockCatch, true));
        add(new Options.Option(enableAllThreadCollection, true));
        add(new Options.Option(enableLogcatCollection, true));
        add(new Options.Option(enableEventsLogCollection, true));
        add(new Options.Option(enableDumpHprof, false));
        add(new Options.Option(enableExternalLinster, true));
        add(new Options.Option(enableSafeGuard, true));
        add(new Options.Option(enableUIProcessSafeGuard, false));
        add(new Options.Option(enableFinalizeFake, true));
        add(new Options.Option(disableJitCompilation, true));
        add(new Options.Option(fileDescriptorLimit, 900));
        add(new Options.Option(mainLogLineLimit, 2000));
        add(new Options.Option(eventsLogLineLimit, 200));
        add(new Options.Option(enableReportContentCompress, true));
        add(new Options.Option(enableSecuritySDK, true));
    }
}
