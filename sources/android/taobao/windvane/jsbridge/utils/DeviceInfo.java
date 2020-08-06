package android.taobao.windvane.jsbridge.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.media.session.PlaybackStateCompat;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import mtopsdk.common.util.SymbolExpUtil;

public class DeviceInfo {
    private static final FileFilter CPU_FILTER = new FileFilter() {
        public boolean accept(File pathname) {
            String path = pathname.getName();
            if (!path.startsWith("cpu")) {
                return false;
            }
            for (int i = 3; i < path.length(); i++) {
                if (path.charAt(i) < '0' || path.charAt(i) > '9') {
                    return false;
                }
            }
            return true;
        }
    };
    public static final int DEVICEINFO_UNKNOWN = -1;

    public static int getNumberOfCPUCores() {
        if (Build.VERSION.SDK_INT <= 10) {
            return 1;
        }
        try {
            return new File("/sys/devices/system/cpu/").listFiles(CPU_FILTER).length;
        } catch (SecurityException e) {
            return -1;
        } catch (NullPointerException e2) {
            return -1;
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getCPUMaxFreqKHz() {
        /*
            r7 = -1
            r6 = 0
        L_0x0002:
            int r10 = getNumberOfCPUCores()     // Catch:{ IOException -> 0x0070 }
            if (r6 >= r10) goto L_0x0078
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0070 }
            r10.<init>()     // Catch:{ IOException -> 0x0070 }
            java.lang.String r11 = "/sys/devices/system/cpu/cpu"
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ IOException -> 0x0070 }
            java.lang.StringBuilder r10 = r10.append(r6)     // Catch:{ IOException -> 0x0070 }
            java.lang.String r11 = "/cpufreq/cpuinfo_max_freq"
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ IOException -> 0x0070 }
            java.lang.String r4 = r10.toString()     // Catch:{ IOException -> 0x0070 }
            java.io.File r1 = new java.io.File     // Catch:{ IOException -> 0x0070 }
            r1.<init>(r4)     // Catch:{ IOException -> 0x0070 }
            boolean r10 = r1.exists()     // Catch:{ IOException -> 0x0070 }
            if (r10 == 0) goto L_0x0068
            r10 = 128(0x80, float:1.794E-43)
            byte[] r0 = new byte[r10]     // Catch:{ IOException -> 0x0070 }
            java.io.FileInputStream r9 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0070 }
            r9.<init>(r1)     // Catch:{ IOException -> 0x0070 }
            r9.read(r0)     // Catch:{ NumberFormatException -> 0x006b, all -> 0x0073 }
            r3 = 0
        L_0x003b:
            byte r10 = r0[r3]     // Catch:{ NumberFormatException -> 0x006b, all -> 0x0073 }
            r11 = 48
            if (r10 < r11) goto L_0x004d
            byte r10 = r0[r3]     // Catch:{ NumberFormatException -> 0x006b, all -> 0x0073 }
            r11 = 57
            if (r10 > r11) goto L_0x004d
            int r10 = r0.length     // Catch:{ NumberFormatException -> 0x006b, all -> 0x0073 }
            if (r3 >= r10) goto L_0x004d
            int r3 = r3 + 1
            goto L_0x003b
        L_0x004d:
            java.lang.String r8 = new java.lang.String     // Catch:{ NumberFormatException -> 0x006b, all -> 0x0073 }
            r10 = 0
            r8.<init>(r0, r10, r3)     // Catch:{ NumberFormatException -> 0x006b, all -> 0x0073 }
            int r10 = java.lang.Integer.parseInt(r8)     // Catch:{ NumberFormatException -> 0x006b, all -> 0x0073 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r10)     // Catch:{ NumberFormatException -> 0x006b, all -> 0x0073 }
            int r10 = r5.intValue()     // Catch:{ NumberFormatException -> 0x006b, all -> 0x0073 }
            if (r10 <= r7) goto L_0x0065
            int r7 = r5.intValue()     // Catch:{ NumberFormatException -> 0x006b, all -> 0x0073 }
        L_0x0065:
            r9.close()     // Catch:{ IOException -> 0x0070 }
        L_0x0068:
            int r6 = r6 + 1
            goto L_0x0002
        L_0x006b:
            r10 = move-exception
            r9.close()     // Catch:{ IOException -> 0x0070 }
            goto L_0x0068
        L_0x0070:
            r2 = move-exception
            r7 = -1
        L_0x0072:
            return r7
        L_0x0073:
            r10 = move-exception
            r9.close()     // Catch:{ IOException -> 0x0070 }
            throw r10     // Catch:{ IOException -> 0x0070 }
        L_0x0078:
            r10 = -1
            if (r7 != r10) goto L_0x0072
            java.io.FileInputStream r9 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0070 }
            java.lang.String r10 = "/proc/cpuinfo"
            r9.<init>(r10)     // Catch:{ IOException -> 0x0070 }
            java.lang.String r10 = "cpu MHz"
            int r5 = parseFileForValue(r10, r9)     // Catch:{ all -> 0x0093 }
            int r5 = r5 * 1000
            if (r5 <= r7) goto L_0x008f
            r7 = r5
        L_0x008f:
            r9.close()     // Catch:{ IOException -> 0x0070 }
            goto L_0x0072
        L_0x0093:
            r10 = move-exception
            r9.close()     // Catch:{ IOException -> 0x0070 }
            throw r10     // Catch:{ IOException -> 0x0070 }
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.utils.DeviceInfo.getCPUMaxFreqKHz():int");
    }

    @TargetApi(16)
    public static long getTotalMemory(Context c) {
        FileInputStream stream;
        if (Build.VERSION.SDK_INT >= 16) {
            ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
            ((ActivityManager) c.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getMemoryInfo(memInfo);
            if (memInfo != null) {
                return memInfo.totalMem;
            }
            return -1;
        }
        long totalMem = -1;
        try {
            stream = new FileInputStream("/proc/meminfo");
            totalMem = ((long) parseFileForValue("MemTotal", stream)) * PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
            stream.close();
            return totalMem;
        } catch (IOException e) {
            return totalMem;
        } catch (Throwable th) {
            stream.close();
            throw th;
        }
    }

    private static int parseFileForValue(String textToMatch, FileInputStream stream) {
        byte[] buffer = new byte[1024];
        try {
            int length = stream.read(buffer);
            int i = 0;
            while (i < length) {
                if (buffer[i] == 10 || i == 0) {
                    if (buffer[i] == 10) {
                        i++;
                    }
                    int j = i;
                    while (j < length) {
                        int textIndex = j - i;
                        if (buffer[j] != textToMatch.charAt(textIndex)) {
                            continue;
                            break;
                        } else if (textIndex == textToMatch.length() - 1) {
                            return extractValue(buffer, j);
                        } else {
                            j++;
                        }
                    }
                    continue;
                }
                i++;
            }
        } catch (IOException | NumberFormatException e) {
        }
        return -1;
    }

    private static int extractValue(byte[] buffer, int index) {
        while (index < buffer.length && buffer[index] != 10) {
            if (buffer[index] < 48 || buffer[index] > 57) {
                index++;
            } else {
                int start = index;
                while (true) {
                    index++;
                    if (index >= buffer.length || buffer[index] < 48 || buffer[index] > 57) {
                    }
                }
                return Integer.parseInt(new String(buffer, 0, start, index - start));
            }
        }
        return -1;
    }

    public static float getProcessCpuRate() {
        ArrayList<Long> cpuTime = getCpuTime();
        if (cpuTime == null || cpuTime.size() < 2) {
            return 0.0f;
        }
        float totalCpuTime1 = (float) cpuTime.get(0).longValue();
        float idleCpuTime1 = (float) cpuTime.get(1).longValue();
        try {
            Thread.sleep(360);
        } catch (Exception e) {
        }
        ArrayList<Long> cpuTime2 = getCpuTime();
        if (cpuTime2 == null || cpuTime2.size() < 2) {
            return 0.0f;
        }
        float totalCpuTime2 = (float) cpuTime2.get(0).longValue();
        return ((totalCpuTime2 - ((float) cpuTime2.get(1).longValue())) - (totalCpuTime1 - idleCpuTime1)) / (totalCpuTime2 - totalCpuTime1);
    }

    private static ArrayList<Long> getCpuTime() {
        ArrayList<Long> cpuTime = new ArrayList<>();
        try {
            RandomAccessFile cpuInfo = new RandomAccessFile("/proc/stat", UploadQueueMgr.MSGTYPE_REALTIME);
            while (true) {
                String line = cpuInfo.readLine();
                if (line == null || !line.startsWith("cpu")) {
                    cpuInfo.close();
                } else {
                    String[] toks = line.split("\\s+");
                    long idleCpu = Long.parseLong(toks[4]);
                    cpuTime.add(Long.valueOf(Long.parseLong(toks[1]) + Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]) + Long.parseLong(toks[6]) + Long.parseLong(toks[5]) + Long.parseLong(toks[7])));
                    cpuTime.add(Long.valueOf(idleCpu));
                }
            }
            cpuInfo.close();
        } catch (Exception e) {
        }
        return cpuTime;
    }

    public long getTotalMemory() {
        String memTotal = "";
        try {
            BufferedReader localBufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            while (true) {
                String readTemp = localBufferedReader.readLine();
                if (readTemp == null) {
                    localBufferedReader.close();
                    return Long.parseLong(memTotal.split(" ")[0].trim());
                } else if (readTemp.contains("MemTotal")) {
                    memTotal = readTemp.split(SymbolExpUtil.SYMBOL_COLON)[1].trim();
                }
            }
        } catch (IOException e) {
            return 0;
        }
    }

    public static long getFreeMemorySize(Context context) {
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getMemoryInfo(outInfo);
        return outInfo.availMem;
    }
}
