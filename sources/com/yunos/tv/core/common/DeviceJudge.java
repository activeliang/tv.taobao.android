package com.yunos.tv.core.common;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import java.io.File;
import java.io.FileFilter;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;

public class DeviceJudge {
    public static final String FAKE_HIGH = "fake_high";
    public static final String FAKE_LOW = "fake_low";
    public static final String FAKE_MEDIUM = "fake_medium";
    private static final int G_UNIT = 1073741824;
    private static final int HighMemorySize = 1024;
    private static final int K_UNIT = 1024;
    private static final int M_UNIT = 1048576;
    private static final int MediumMemorySize = 768;
    public static final String REAL = "real";
    private static final String TAG = "DevicePerformance";
    private static DeviceJudge instance = null;
    private CpuType cpuType = CpuType.cpuUNKNOWN;
    private String deviceFake = "real";
    private DeviceJudgeImpl deviceJudgeImpl = null;
    private DeviceType deviceType = DeviceType.dtUNKNOWN;
    private ScreenResolutionType mScreenResolutionType = ScreenResolutionType.UNKNOWN;
    private MemoryType memoryType = MemoryType.memUNKNOWN;
    private String userCfg;

    public enum CpuType {
        cpuLow,
        cpuMedium,
        cpuHigh,
        cpuUNKNOWN
    }

    public enum DeviceType {
        dtLOW,
        dtNORMAL,
        dtUNKNOWN
    }

    public enum MemoryType {
        memLow,
        memMedium,
        memHigh,
        memUNKNOWN
    }

    public enum ScreenResolutionType {
        srtSmall,
        srt720P,
        srtMedium,
        srt1080P,
        srtLarge,
        UNKNOWN
    }

    private DeviceJudge() {
    }

    private DeviceJudge(Context context) {
        this.deviceJudgeImpl = new DeviceJudgeImpl(context);
        judgeScreenResolutionType();
        judgeMemoryType();
        judgeCpuType();
        judgeDeviceType();
    }

    public static DeviceJudge getInstance(Context context) {
        if (instance == null) {
            synchronized (DeviceJudge.class) {
                if (instance == null) {
                    instance = new DeviceJudge(context);
                }
            }
        }
        return instance;
    }

    public String toString() {
        return "DeviceJudge{deviceJudgeImpl=" + this.deviceJudgeImpl + ", mScreenResolutionType=" + this.mScreenResolutionType + ", memoryType=" + this.memoryType + ", cpuType=" + this.cpuType + ", deviceType=" + this.deviceType + ", deviceFake='" + this.deviceFake + '\'' + '}';
    }

    public static String getDevicePerformanceString() {
        checkInstance();
        return ("DevicePerformance:" + instance.toString());
    }

    public static ScreenResolutionType getScreenResolutionType() {
        checkInstance();
        return instance.mScreenResolutionType;
    }

    public static MemoryType getMemoryType() {
        checkInstance();
        return instance.memoryType;
    }

    public static boolean isMemTypeHigh() {
        checkInstance();
        return instance.memoryType == MemoryType.memHigh;
    }

    public static boolean isMemTypeMedium() {
        checkInstance();
        return instance.memoryType == MemoryType.memMedium;
    }

    public static boolean isMemTypeLow() {
        checkInstance();
        return instance.memoryType == MemoryType.memLow;
    }

    public static CpuType getCpuType() {
        checkInstance();
        return instance.cpuType;
    }

    public static boolean isCpuTypeHigh() {
        checkInstance();
        return instance.cpuType == CpuType.cpuHigh;
    }

    public static boolean isCpuTypeMedium() {
        checkInstance();
        return instance.cpuType == CpuType.cpuMedium;
    }

    public static boolean isCpuTypeLow() {
        checkInstance();
        return instance.cpuType == CpuType.cpuLow;
    }

    public static DeviceType getDeviceType() {
        checkInstance();
        return instance.deviceType;
    }

    public static boolean isLowDevice() {
        checkInstance();
        return instance.deviceType == DeviceType.dtLOW;
    }

    public static boolean isNormalDevice() {
        checkInstance();
        return instance.deviceType == DeviceType.dtNORMAL;
    }

    public static int getTotalMemorySizeInMB() {
        checkInstance();
        return instance.deviceJudgeImpl.totalMemorySizeMB;
    }

    public static int getMaxCpuFreqInKHz() {
        checkInstance();
        return instance.deviceJudgeImpl.maxCpuFreqInKHz;
    }

    public static int getCoreCount() {
        checkInstance();
        return instance.deviceJudgeImpl.cpuCoreCount;
    }

    public static String getDeviceFake() {
        checkInstance();
        return instance.deviceFake;
    }

    public static String getUserCfg() {
        checkInstance();
        return instance.userCfg;
    }

    public static DeviceJudge updateCurrCpuFreqInKHz() {
        checkInstance();
        instance.deviceJudgeImpl.updateCurrCpuFreqInKHz();
        return instance;
    }

    public static String getCpuStat() {
        checkInstance();
        return instance.deviceJudgeImpl.getCpuStatImpl();
    }

    private static void checkInstance() {
        if (instance == null) {
            throw new RuntimeException("getInstance first !!");
        }
    }

    private void judgeScreenResolutionType() {
        if (this.deviceJudgeImpl.widthPixels == 1920 && this.deviceJudgeImpl.heightPixels == 1080) {
            this.mScreenResolutionType = ScreenResolutionType.srt1080P;
        } else if (this.deviceJudgeImpl.widthPixels == 1280 && this.deviceJudgeImpl.heightPixels == 720) {
            this.mScreenResolutionType = ScreenResolutionType.srt720P;
        } else {
            long pixels = (long) (this.deviceJudgeImpl.widthPixels * this.deviceJudgeImpl.heightPixels);
            if (pixels > 2073600) {
                this.mScreenResolutionType = ScreenResolutionType.srtLarge;
            } else if (pixels > 921600) {
                this.mScreenResolutionType = ScreenResolutionType.srtMedium;
            } else {
                this.mScreenResolutionType = ScreenResolutionType.srtSmall;
            }
        }
    }

    private void judgeMemoryType() {
        int memorySize = 0;
        this.userCfg = SharePreferences.getString("device_fake_by_user", (String) null);
        if (this.userCfg != null) {
            this.deviceFake = this.userCfg;
        }
        if (this.deviceFake.equals("real")) {
            memorySize = this.deviceJudgeImpl.totalMemorySizeMB;
        } else if (this.deviceFake.equals(FAKE_LOW)) {
            memorySize = 767;
        } else if (this.deviceFake.equals(FAKE_MEDIUM)) {
            memorySize = 1023;
        } else if (this.deviceFake.equals(FAKE_HIGH)) {
            memorySize = 1025;
        }
        if (memorySize <= 768) {
            this.memoryType = MemoryType.memLow;
        } else if (memorySize <= 1024) {
            this.memoryType = MemoryType.memMedium;
        } else {
            this.memoryType = MemoryType.memHigh;
        }
    }

    private void judgeCpuType() {
        float cpuValue = 1024.0f * ((float) this.deviceJudgeImpl.maxCpuFreqInKHz) * ((float) this.deviceJudgeImpl.cpuCoreCount);
        if (this.mScreenResolutionType == ScreenResolutionType.srt1080P || this.mScreenResolutionType == ScreenResolutionType.srtLarge) {
            if (cpuValue >= 7.7309409E9f) {
                this.cpuType = CpuType.cpuHigh;
            } else if (cpuValue >= 6.4424509E9f) {
                this.cpuType = CpuType.cpuMedium;
            } else {
                this.cpuType = CpuType.cpuLow;
            }
        } else if (this.mScreenResolutionType == ScreenResolutionType.srt720P || this.mScreenResolutionType == ScreenResolutionType.srtMedium) {
            if (cpuValue >= 6.4424509E9f) {
                this.cpuType = CpuType.cpuHigh;
            } else if (cpuValue > 5.153961E9f) {
                this.cpuType = CpuType.cpuMedium;
            } else {
                this.cpuType = CpuType.cpuLow;
            }
        } else if (cpuValue >= 3.86547046E9f) {
            this.cpuType = CpuType.cpuHigh;
        } else if (cpuValue >= 2.57698048E9f) {
            this.cpuType = CpuType.cpuMedium;
        } else {
            this.cpuType = CpuType.cpuLow;
        }
    }

    private void judgeDeviceType() {
        if (this.mScreenResolutionType == ScreenResolutionType.srt1080P || this.mScreenResolutionType == ScreenResolutionType.srtLarge) {
            if (this.memoryType == MemoryType.memLow) {
                this.deviceType = DeviceType.dtLOW;
            } else if (this.cpuType == CpuType.cpuLow) {
                this.deviceType = DeviceType.dtLOW;
            } else {
                this.deviceType = DeviceType.dtNORMAL;
            }
        } else if (this.mScreenResolutionType == ScreenResolutionType.srt720P || this.mScreenResolutionType == ScreenResolutionType.srtMedium) {
            if (this.memoryType == MemoryType.memLow) {
                this.deviceType = DeviceType.dtLOW;
            } else if (this.cpuType != CpuType.cpuHigh) {
                this.deviceType = DeviceType.dtLOW;
            } else {
                this.deviceType = DeviceType.dtNORMAL;
            }
        } else if (this.memoryType == MemoryType.memLow && this.cpuType == CpuType.cpuLow) {
            this.deviceType = DeviceType.dtLOW;
        } else {
            this.deviceType = DeviceType.dtNORMAL;
        }
    }

    private class DeviceJudgeImpl {
        int cpuCoreCount = -1;
        int currCpuFreqInKHz = -1;
        int heightPixels = -1;
        int maxCpuFreqInKHz = -1;
        int minCpuFreqInKHz = -1;
        int totalMemorySizeMB = -1;
        int widthPixels = -1;

        public String toString() {
            return "DeviceJudgeImpl{cpuCoreCount=" + this.cpuCoreCount + ", maxCpuFreqInKHz=" + this.maxCpuFreqInKHz + ", minCpuFreqInKHz=" + this.minCpuFreqInKHz + ", currCpuFreqInKHz=" + this.currCpuFreqInKHz + ", totalMemorySizeMB=" + this.totalMemorySizeMB + ", widthPixels=" + this.widthPixels + ", heightPixels=" + this.heightPixels + '}';
        }

        private DeviceJudgeImpl() {
        }

        DeviceJudgeImpl(Context context) {
            readFromCfgFile();
            DisplayMetrics metric = new DisplayMetrics();
            ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(metric);
            this.widthPixels = metric.widthPixels;
            this.heightPixels = metric.heightPixels;
        }

        private int getCoresCount() {
            try {
                return new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        if (Pattern.matches("cpu[0-9]+", pathname.getName())) {
                            return true;
                        }
                        return false;
                    }
                }).length;
            } catch (Exception e) {
                return Math.max(1, Runtime.getRuntime().availableProcessors());
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:28:0x0048 A[Catch:{ all -> 0x0097 }] */
        /* JADX WARNING: Removed duplicated region for block: B:30:0x004d A[SYNTHETIC, Splitter:B:30:0x004d] */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x0052 A[SYNTHETIC, Splitter:B:33:0x0052] */
        /* JADX WARNING: Removed duplicated region for block: B:47:0x0073 A[Catch:{ all -> 0x0097 }] */
        /* JADX WARNING: Removed duplicated region for block: B:49:0x0078 A[SYNTHETIC, Splitter:B:49:0x0078] */
        /* JADX WARNING: Removed duplicated region for block: B:52:0x007d A[SYNTHETIC, Splitter:B:52:0x007d] */
        /* JADX WARNING: Removed duplicated region for block: B:64:0x009a A[SYNTHETIC, Splitter:B:64:0x009a] */
        /* JADX WARNING: Removed duplicated region for block: B:67:0x009f A[SYNTHETIC, Splitter:B:67:0x009f] */
        /* JADX WARNING: Unknown top exception splitter block from list: {B:25:0x0042=Splitter:B:25:0x0042, B:44:0x006d=Splitter:B:44:0x006d} */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private int getFirstCoreMaxFreq() {
            /*
                r10 = this;
                java.lang.String r5 = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"
                r6 = -1
                r3 = 0
                r0 = 0
                java.io.FileReader r4 = new java.io.FileReader     // Catch:{ FileNotFoundException -> 0x0041, IOException -> 0x006c }
                r4.<init>(r5)     // Catch:{ FileNotFoundException -> 0x0041, IOException -> 0x006c }
                java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ FileNotFoundException -> 0x00c7, IOException -> 0x00c0, all -> 0x00b9 }
                r1.<init>(r4)     // Catch:{ FileNotFoundException -> 0x00c7, IOException -> 0x00c0, all -> 0x00b9 }
                java.lang.String r7 = r1.readLine()     // Catch:{ FileNotFoundException -> 0x00cb, IOException -> 0x00c3, all -> 0x00bc }
                java.lang.String r8 = r7.trim()     // Catch:{ FileNotFoundException -> 0x00cb, IOException -> 0x00c3, all -> 0x00bc }
                int r6 = java.lang.Integer.parseInt(r8)     // Catch:{ FileNotFoundException -> 0x00cb, IOException -> 0x00c3, all -> 0x00bc }
                if (r4 == 0) goto L_0x0021
                r4.close()     // Catch:{ IOException -> 0x0029 }
            L_0x0021:
                if (r1 == 0) goto L_0x00d0
                r1.close()     // Catch:{ IOException -> 0x0034 }
                r0 = r1
                r3 = r4
            L_0x0028:
                return r6
            L_0x0029:
                r2 = move-exception
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()
                if (r8 == 0) goto L_0x0021
                r2.printStackTrace()
                goto L_0x0021
            L_0x0034:
                r2 = move-exception
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()
                if (r8 == 0) goto L_0x003e
                r2.printStackTrace()
            L_0x003e:
                r0 = r1
                r3 = r4
                goto L_0x0028
            L_0x0041:
                r2 = move-exception
            L_0x0042:
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()     // Catch:{ all -> 0x0097 }
                if (r8 == 0) goto L_0x004b
                r2.printStackTrace()     // Catch:{ all -> 0x0097 }
            L_0x004b:
                if (r3 == 0) goto L_0x0050
                r3.close()     // Catch:{ IOException -> 0x0061 }
            L_0x0050:
                if (r0 == 0) goto L_0x0028
                r0.close()     // Catch:{ IOException -> 0x0056 }
                goto L_0x0028
            L_0x0056:
                r2 = move-exception
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()
                if (r8 == 0) goto L_0x0028
                r2.printStackTrace()
                goto L_0x0028
            L_0x0061:
                r2 = move-exception
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()
                if (r8 == 0) goto L_0x0050
                r2.printStackTrace()
                goto L_0x0050
            L_0x006c:
                r2 = move-exception
            L_0x006d:
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()     // Catch:{ all -> 0x0097 }
                if (r8 == 0) goto L_0x0076
                r2.printStackTrace()     // Catch:{ all -> 0x0097 }
            L_0x0076:
                if (r3 == 0) goto L_0x007b
                r3.close()     // Catch:{ IOException -> 0x008c }
            L_0x007b:
                if (r0 == 0) goto L_0x0028
                r0.close()     // Catch:{ IOException -> 0x0081 }
                goto L_0x0028
            L_0x0081:
                r2 = move-exception
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()
                if (r8 == 0) goto L_0x0028
                r2.printStackTrace()
                goto L_0x0028
            L_0x008c:
                r2 = move-exception
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()
                if (r8 == 0) goto L_0x007b
                r2.printStackTrace()
                goto L_0x007b
            L_0x0097:
                r8 = move-exception
            L_0x0098:
                if (r3 == 0) goto L_0x009d
                r3.close()     // Catch:{ IOException -> 0x00a3 }
            L_0x009d:
                if (r0 == 0) goto L_0x00a2
                r0.close()     // Catch:{ IOException -> 0x00ae }
            L_0x00a2:
                throw r8
            L_0x00a3:
                r2 = move-exception
                boolean r9 = com.yunos.tv.core.config.Config.isDebug()
                if (r9 == 0) goto L_0x009d
                r2.printStackTrace()
                goto L_0x009d
            L_0x00ae:
                r2 = move-exception
                boolean r9 = com.yunos.tv.core.config.Config.isDebug()
                if (r9 == 0) goto L_0x00a2
                r2.printStackTrace()
                goto L_0x00a2
            L_0x00b9:
                r8 = move-exception
                r3 = r4
                goto L_0x0098
            L_0x00bc:
                r8 = move-exception
                r0 = r1
                r3 = r4
                goto L_0x0098
            L_0x00c0:
                r2 = move-exception
                r3 = r4
                goto L_0x006d
            L_0x00c3:
                r2 = move-exception
                r0 = r1
                r3 = r4
                goto L_0x006d
            L_0x00c7:
                r2 = move-exception
                r3 = r4
                goto L_0x0042
            L_0x00cb:
                r2 = move-exception
                r0 = r1
                r3 = r4
                goto L_0x0042
            L_0x00d0:
                r0 = r1
                r3 = r4
                goto L_0x0028
            */
            throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.core.common.DeviceJudge.DeviceJudgeImpl.getFirstCoreMaxFreq():int");
        }

        /* JADX WARNING: Removed duplicated region for block: B:28:0x0048 A[Catch:{ all -> 0x0097 }] */
        /* JADX WARNING: Removed duplicated region for block: B:30:0x004d A[SYNTHETIC, Splitter:B:30:0x004d] */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x0052 A[SYNTHETIC, Splitter:B:33:0x0052] */
        /* JADX WARNING: Removed duplicated region for block: B:47:0x0073 A[Catch:{ all -> 0x0097 }] */
        /* JADX WARNING: Removed duplicated region for block: B:49:0x0078 A[SYNTHETIC, Splitter:B:49:0x0078] */
        /* JADX WARNING: Removed duplicated region for block: B:52:0x007d A[SYNTHETIC, Splitter:B:52:0x007d] */
        /* JADX WARNING: Removed duplicated region for block: B:64:0x009a A[SYNTHETIC, Splitter:B:64:0x009a] */
        /* JADX WARNING: Removed duplicated region for block: B:67:0x009f A[SYNTHETIC, Splitter:B:67:0x009f] */
        /* JADX WARNING: Unknown top exception splitter block from list: {B:25:0x0042=Splitter:B:25:0x0042, B:44:0x006d=Splitter:B:44:0x006d} */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private int getFirstCoreMinFreq() {
            /*
                r10 = this;
                java.lang.String r5 = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"
                r6 = -1
                r3 = 0
                r0 = 0
                java.io.FileReader r4 = new java.io.FileReader     // Catch:{ FileNotFoundException -> 0x0041, IOException -> 0x006c }
                r4.<init>(r5)     // Catch:{ FileNotFoundException -> 0x0041, IOException -> 0x006c }
                java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ FileNotFoundException -> 0x00c7, IOException -> 0x00c0, all -> 0x00b9 }
                r1.<init>(r4)     // Catch:{ FileNotFoundException -> 0x00c7, IOException -> 0x00c0, all -> 0x00b9 }
                java.lang.String r7 = r1.readLine()     // Catch:{ FileNotFoundException -> 0x00cb, IOException -> 0x00c3, all -> 0x00bc }
                java.lang.String r8 = r7.trim()     // Catch:{ FileNotFoundException -> 0x00cb, IOException -> 0x00c3, all -> 0x00bc }
                int r6 = java.lang.Integer.parseInt(r8)     // Catch:{ FileNotFoundException -> 0x00cb, IOException -> 0x00c3, all -> 0x00bc }
                if (r4 == 0) goto L_0x0021
                r4.close()     // Catch:{ IOException -> 0x0029 }
            L_0x0021:
                if (r1 == 0) goto L_0x00d0
                r1.close()     // Catch:{ IOException -> 0x0034 }
                r0 = r1
                r3 = r4
            L_0x0028:
                return r6
            L_0x0029:
                r2 = move-exception
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()
                if (r8 == 0) goto L_0x0021
                r2.printStackTrace()
                goto L_0x0021
            L_0x0034:
                r2 = move-exception
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()
                if (r8 == 0) goto L_0x003e
                r2.printStackTrace()
            L_0x003e:
                r0 = r1
                r3 = r4
                goto L_0x0028
            L_0x0041:
                r2 = move-exception
            L_0x0042:
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()     // Catch:{ all -> 0x0097 }
                if (r8 == 0) goto L_0x004b
                r2.printStackTrace()     // Catch:{ all -> 0x0097 }
            L_0x004b:
                if (r3 == 0) goto L_0x0050
                r3.close()     // Catch:{ IOException -> 0x0061 }
            L_0x0050:
                if (r0 == 0) goto L_0x0028
                r0.close()     // Catch:{ IOException -> 0x0056 }
                goto L_0x0028
            L_0x0056:
                r2 = move-exception
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()
                if (r8 == 0) goto L_0x0028
                r2.printStackTrace()
                goto L_0x0028
            L_0x0061:
                r2 = move-exception
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()
                if (r8 == 0) goto L_0x0050
                r2.printStackTrace()
                goto L_0x0050
            L_0x006c:
                r2 = move-exception
            L_0x006d:
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()     // Catch:{ all -> 0x0097 }
                if (r8 == 0) goto L_0x0076
                r2.printStackTrace()     // Catch:{ all -> 0x0097 }
            L_0x0076:
                if (r3 == 0) goto L_0x007b
                r3.close()     // Catch:{ IOException -> 0x008c }
            L_0x007b:
                if (r0 == 0) goto L_0x0028
                r0.close()     // Catch:{ IOException -> 0x0081 }
                goto L_0x0028
            L_0x0081:
                r2 = move-exception
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()
                if (r8 == 0) goto L_0x0028
                r2.printStackTrace()
                goto L_0x0028
            L_0x008c:
                r2 = move-exception
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()
                if (r8 == 0) goto L_0x007b
                r2.printStackTrace()
                goto L_0x007b
            L_0x0097:
                r8 = move-exception
            L_0x0098:
                if (r3 == 0) goto L_0x009d
                r3.close()     // Catch:{ IOException -> 0x00a3 }
            L_0x009d:
                if (r0 == 0) goto L_0x00a2
                r0.close()     // Catch:{ IOException -> 0x00ae }
            L_0x00a2:
                throw r8
            L_0x00a3:
                r2 = move-exception
                boolean r9 = com.yunos.tv.core.config.Config.isDebug()
                if (r9 == 0) goto L_0x009d
                r2.printStackTrace()
                goto L_0x009d
            L_0x00ae:
                r2 = move-exception
                boolean r9 = com.yunos.tv.core.config.Config.isDebug()
                if (r9 == 0) goto L_0x00a2
                r2.printStackTrace()
                goto L_0x00a2
            L_0x00b9:
                r8 = move-exception
                r3 = r4
                goto L_0x0098
            L_0x00bc:
                r8 = move-exception
                r0 = r1
                r3 = r4
                goto L_0x0098
            L_0x00c0:
                r2 = move-exception
                r3 = r4
                goto L_0x006d
            L_0x00c3:
                r2 = move-exception
                r0 = r1
                r3 = r4
                goto L_0x006d
            L_0x00c7:
                r2 = move-exception
                r3 = r4
                goto L_0x0042
            L_0x00cb:
                r2 = move-exception
                r0 = r1
                r3 = r4
                goto L_0x0042
            L_0x00d0:
                r0 = r1
                r3 = r4
                goto L_0x0028
            */
            throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.core.common.DeviceJudge.DeviceJudgeImpl.getFirstCoreMinFreq():int");
        }

        /* JADX WARNING: Removed duplicated region for block: B:28:0x0048 A[Catch:{ all -> 0x0097 }] */
        /* JADX WARNING: Removed duplicated region for block: B:30:0x004d A[SYNTHETIC, Splitter:B:30:0x004d] */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x0052 A[SYNTHETIC, Splitter:B:33:0x0052] */
        /* JADX WARNING: Removed duplicated region for block: B:47:0x0073 A[Catch:{ all -> 0x0097 }] */
        /* JADX WARNING: Removed duplicated region for block: B:49:0x0078 A[SYNTHETIC, Splitter:B:49:0x0078] */
        /* JADX WARNING: Removed duplicated region for block: B:52:0x007d A[SYNTHETIC, Splitter:B:52:0x007d] */
        /* JADX WARNING: Removed duplicated region for block: B:64:0x009a A[SYNTHETIC, Splitter:B:64:0x009a] */
        /* JADX WARNING: Removed duplicated region for block: B:67:0x009f A[SYNTHETIC, Splitter:B:67:0x009f] */
        /* JADX WARNING: Unknown top exception splitter block from list: {B:25:0x0042=Splitter:B:25:0x0042, B:44:0x006d=Splitter:B:44:0x006d} */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private int getFirstCoreCurrFreq() {
            /*
                r10 = this;
                java.lang.String r5 = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq"
                r6 = -1
                r3 = 0
                r0 = 0
                java.io.FileReader r4 = new java.io.FileReader     // Catch:{ FileNotFoundException -> 0x0041, IOException -> 0x006c }
                r4.<init>(r5)     // Catch:{ FileNotFoundException -> 0x0041, IOException -> 0x006c }
                java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ FileNotFoundException -> 0x00c7, IOException -> 0x00c0, all -> 0x00b9 }
                r1.<init>(r4)     // Catch:{ FileNotFoundException -> 0x00c7, IOException -> 0x00c0, all -> 0x00b9 }
                java.lang.String r7 = r1.readLine()     // Catch:{ FileNotFoundException -> 0x00cb, IOException -> 0x00c3, all -> 0x00bc }
                java.lang.String r8 = r7.trim()     // Catch:{ FileNotFoundException -> 0x00cb, IOException -> 0x00c3, all -> 0x00bc }
                int r6 = java.lang.Integer.parseInt(r8)     // Catch:{ FileNotFoundException -> 0x00cb, IOException -> 0x00c3, all -> 0x00bc }
                if (r4 == 0) goto L_0x0021
                r4.close()     // Catch:{ IOException -> 0x0029 }
            L_0x0021:
                if (r1 == 0) goto L_0x00d0
                r1.close()     // Catch:{ IOException -> 0x0034 }
                r0 = r1
                r3 = r4
            L_0x0028:
                return r6
            L_0x0029:
                r2 = move-exception
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()
                if (r8 == 0) goto L_0x0021
                r2.printStackTrace()
                goto L_0x0021
            L_0x0034:
                r2 = move-exception
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()
                if (r8 == 0) goto L_0x003e
                r2.printStackTrace()
            L_0x003e:
                r0 = r1
                r3 = r4
                goto L_0x0028
            L_0x0041:
                r2 = move-exception
            L_0x0042:
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()     // Catch:{ all -> 0x0097 }
                if (r8 == 0) goto L_0x004b
                r2.printStackTrace()     // Catch:{ all -> 0x0097 }
            L_0x004b:
                if (r3 == 0) goto L_0x0050
                r3.close()     // Catch:{ IOException -> 0x0061 }
            L_0x0050:
                if (r0 == 0) goto L_0x0028
                r0.close()     // Catch:{ IOException -> 0x0056 }
                goto L_0x0028
            L_0x0056:
                r2 = move-exception
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()
                if (r8 == 0) goto L_0x0028
                r2.printStackTrace()
                goto L_0x0028
            L_0x0061:
                r2 = move-exception
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()
                if (r8 == 0) goto L_0x0050
                r2.printStackTrace()
                goto L_0x0050
            L_0x006c:
                r2 = move-exception
            L_0x006d:
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()     // Catch:{ all -> 0x0097 }
                if (r8 == 0) goto L_0x0076
                r2.printStackTrace()     // Catch:{ all -> 0x0097 }
            L_0x0076:
                if (r3 == 0) goto L_0x007b
                r3.close()     // Catch:{ IOException -> 0x008c }
            L_0x007b:
                if (r0 == 0) goto L_0x0028
                r0.close()     // Catch:{ IOException -> 0x0081 }
                goto L_0x0028
            L_0x0081:
                r2 = move-exception
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()
                if (r8 == 0) goto L_0x0028
                r2.printStackTrace()
                goto L_0x0028
            L_0x008c:
                r2 = move-exception
                boolean r8 = com.yunos.tv.core.config.Config.isDebug()
                if (r8 == 0) goto L_0x007b
                r2.printStackTrace()
                goto L_0x007b
            L_0x0097:
                r8 = move-exception
            L_0x0098:
                if (r3 == 0) goto L_0x009d
                r3.close()     // Catch:{ IOException -> 0x00a3 }
            L_0x009d:
                if (r0 == 0) goto L_0x00a2
                r0.close()     // Catch:{ IOException -> 0x00ae }
            L_0x00a2:
                throw r8
            L_0x00a3:
                r2 = move-exception
                boolean r9 = com.yunos.tv.core.config.Config.isDebug()
                if (r9 == 0) goto L_0x009d
                r2.printStackTrace()
                goto L_0x009d
            L_0x00ae:
                r2 = move-exception
                boolean r9 = com.yunos.tv.core.config.Config.isDebug()
                if (r9 == 0) goto L_0x00a2
                r2.printStackTrace()
                goto L_0x00a2
            L_0x00b9:
                r8 = move-exception
                r3 = r4
                goto L_0x0098
            L_0x00bc:
                r8 = move-exception
                r0 = r1
                r3 = r4
                goto L_0x0098
            L_0x00c0:
                r2 = move-exception
                r3 = r4
                goto L_0x006d
            L_0x00c3:
                r2 = move-exception
                r0 = r1
                r3 = r4
                goto L_0x006d
            L_0x00c7:
                r2 = move-exception
                r3 = r4
                goto L_0x0042
            L_0x00cb:
                r2 = move-exception
                r0 = r1
                r3 = r4
                goto L_0x0042
            L_0x00d0:
                r0 = r1
                r3 = r4
                goto L_0x0028
            */
            throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.core.common.DeviceJudge.DeviceJudgeImpl.getFirstCoreCurrFreq():int");
        }

        /* JADX WARNING: Removed duplicated region for block: B:29:0x005b A[SYNTHETIC, Splitter:B:29:0x005b] */
        /* JADX WARNING: Removed duplicated region for block: B:35:0x0067 A[SYNTHETIC, Splitter:B:35:0x0067] */
        /* JADX WARNING: Unknown top exception splitter block from list: {B:26:0x0056=Splitter:B:26:0x0056, B:18:0x0047=Splitter:B:18:0x0047} */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private int getTotalMemorySizeInMB() {
            /*
                r14 = this;
                r10 = -1
                java.lang.String r7 = "/proc/meminfo"
                r3 = 0
                r1 = 0
                java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ FileNotFoundException -> 0x0046, IOException -> 0x0055 }
                java.io.FileReader r11 = new java.io.FileReader     // Catch:{ FileNotFoundException -> 0x0046, IOException -> 0x0055 }
                r11.<init>(r7)     // Catch:{ FileNotFoundException -> 0x0046, IOException -> 0x0055 }
                r12 = 8
                r2.<init>(r11, r12)     // Catch:{ FileNotFoundException -> 0x0046, IOException -> 0x0055 }
                java.lang.String r6 = r2.readLine()     // Catch:{ FileNotFoundException -> 0x007b, IOException -> 0x0078, all -> 0x0075 }
                if (r6 == 0) goto L_0x0019
                r3 = r6
            L_0x0019:
                if (r2 == 0) goto L_0x007e
                r2.close()     // Catch:{ IOException -> 0x0040 }
                r1 = r2
            L_0x001f:
                java.lang.String r11 = "MemTotal:"
                int r0 = r3.indexOf(r11)
                r11 = 107(0x6b, float:1.5E-43)
                int r5 = r3.indexOf(r11)
                int r11 = r0 + 9
                java.lang.String r11 = r3.substring(r11, r5)
                java.lang.String r3 = r11.trim()
                long r8 = java.lang.Long.parseLong(r3)     // Catch:{ Exception -> 0x0070 }
                r12 = 1024(0x400, double:5.06E-321)
                long r12 = r8 / r12
                int r10 = (int) r12
            L_0x003f:
                return r10
            L_0x0040:
                r4 = move-exception
                r4.printStackTrace()
                r1 = r2
                goto L_0x001f
            L_0x0046:
                r4 = move-exception
            L_0x0047:
                r4.printStackTrace()     // Catch:{ all -> 0x0064 }
                if (r1 == 0) goto L_0x001f
                r1.close()     // Catch:{ IOException -> 0x0050 }
                goto L_0x001f
            L_0x0050:
                r4 = move-exception
                r4.printStackTrace()
                goto L_0x001f
            L_0x0055:
                r4 = move-exception
            L_0x0056:
                r4.printStackTrace()     // Catch:{ all -> 0x0064 }
                if (r1 == 0) goto L_0x001f
                r1.close()     // Catch:{ IOException -> 0x005f }
                goto L_0x001f
            L_0x005f:
                r4 = move-exception
                r4.printStackTrace()
                goto L_0x001f
            L_0x0064:
                r11 = move-exception
            L_0x0065:
                if (r1 == 0) goto L_0x006a
                r1.close()     // Catch:{ IOException -> 0x006b }
            L_0x006a:
                throw r11
            L_0x006b:
                r4 = move-exception
                r4.printStackTrace()
                goto L_0x006a
            L_0x0070:
                r4 = move-exception
                r4.printStackTrace()
                goto L_0x003f
            L_0x0075:
                r11 = move-exception
                r1 = r2
                goto L_0x0065
            L_0x0078:
                r4 = move-exception
                r1 = r2
                goto L_0x0056
            L_0x007b:
                r4 = move-exception
                r1 = r2
                goto L_0x0047
            L_0x007e:
                r1 = r2
                goto L_0x001f
            */
            throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.core.common.DeviceJudge.DeviceJudgeImpl.getTotalMemorySizeInMB():int");
        }

        private void readFromCfgFile() {
            this.cpuCoreCount = getCoresCount();
            this.maxCpuFreqInKHz = getFirstCoreMaxFreq();
            this.minCpuFreqInKHz = getFirstCoreMinFreq();
            this.currCpuFreqInKHz = getFirstCoreCurrFreq();
            this.totalMemorySizeMB = getTotalMemorySizeInMB();
        }

        /* access modifiers changed from: private */
        public void updateCurrCpuFreqInKHz() {
            this.currCpuFreqInKHz = getFirstCoreCurrFreq();
        }

        /* access modifiers changed from: private */
        public String getCpuStatImpl() {
            RandomAccessFile reader;
            if (1 != 0) {
                try {
                    reader = new RandomAccessFile("/proc/stat", UploadQueueMgr.MSGTYPE_REALTIME);
                    try {
                        String load = reader.readLine();
                        StringBuffer sb = new StringBuffer();
                        int loop = 20;
                        while (!TextUtils.isEmpty(load) && loop - 1 >= 0) {
                            sb.append(load);
                        }
                        String rtn = sb.toString();
                        reader.close();
                        RandomAccessFile randomAccessFile = reader;
                        return rtn;
                    } catch (Throwable th) {
                        throwable = th;
                        RandomAccessFile randomAccessFile2 = reader;
                        throwable.printStackTrace();
                        return " error ";
                    }
                } catch (Throwable th2) {
                    throwable = th2;
                    throwable.printStackTrace();
                    return " error ";
                }
            } else {
                reader = new RandomAccessFile("/proc/stat", UploadQueueMgr.MSGTYPE_REALTIME);
                String load2 = reader.readLine();
                StringBuffer sb2 = new StringBuffer();
                int loop2 = 20;
                while (!TextUtils.isEmpty(load2) && loop2 - 1 >= 0) {
                    sb2.append(load2);
                }
                String rtn2 = sb2.toString();
                reader.close();
                RandomAccessFile randomAccessFile3 = reader;
                return rtn2;
            }
        }
    }
}
