package com.tvtao.user.dclib.impl;

import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import mtopsdk.common.util.SymbolExpUtil;

public class CPUUtil {
    public static String getSerial(Context context) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("cat /proc/cpuinfo").getInputStream()));
            StringBuffer sb = new StringBuffer();
            while (true) {
                String info = br.readLine();
                if (info == null) {
                    return sb.toString();
                }
                String[] kv = info.split(SymbolExpUtil.SYMBOL_COLON);
                if (kv.length == 2) {
                    if ("hardware".equalsIgnoreCase(kv[0].trim())) {
                        sb.append(kv[1].trim());
                    } else if ("serial".equalsIgnoreCase(kv[0].trim())) {
                        sb.append(SymbolExpUtil.SYMBOL_SEMICOLON).append(kv[1].trim());
                    }
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static String getMaxFreq(Context context) {
        try {
            return new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("cat sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq").getInputStream())).readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCPUCount(Context context) {
        try {
            return "" + new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    if (Pattern.matches("cpu[0-9]+", pathname.getName())) {
                        return true;
                    }
                    return false;
                }
            }).length;
        } catch (Exception e) {
            e.printStackTrace();
            return "1";
        }
    }
}
