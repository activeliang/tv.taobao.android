package android.taobao.windvane.jsbridge.utils;

import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;

public class YearClass {
    public static final int CLASS_2008 = 2008;
    public static final int CLASS_2009 = 2009;
    public static final int CLASS_2010 = 2010;
    public static final int CLASS_2011 = 2011;
    public static final int CLASS_2012 = 2012;
    public static final int CLASS_2013 = 2013;
    public static final int CLASS_2014 = 2014;
    public static final int CLASS_UNKNOWN = -1;
    private static final long MB = 1048576;
    private static final int MHZ_IN_KHZ = 1000;
    private static volatile Integer mYearCategory;

    public static int get(Context c) {
        if (mYearCategory == null) {
            synchronized (YearClass.class) {
                if (mYearCategory == null) {
                    mYearCategory = Integer.valueOf(categorizeByYear(c));
                }
            }
        }
        return mYearCategory.intValue();
    }

    private static void conditionallyAdd(ArrayList<Integer> list, int value) {
        if (value != -1) {
            list.add(Integer.valueOf(value));
        }
    }

    private static int categorizeByYear(Context c) {
        ArrayList<Integer> componentYears = new ArrayList<>();
        conditionallyAdd(componentYears, getNumCoresYear());
        conditionallyAdd(componentYears, getClockSpeedYear());
        conditionallyAdd(componentYears, getRamYear(c));
        if (componentYears.isEmpty()) {
            return -1;
        }
        Collections.sort(componentYears);
        if ((componentYears.size() & 1) == 1) {
            return componentYears.get(componentYears.size() / 2).intValue();
        }
        int baseIndex = (componentYears.size() / 2) - 1;
        return ((componentYears.get(baseIndex + 1).intValue() - componentYears.get(baseIndex).intValue()) / 2) + componentYears.get(baseIndex).intValue();
    }

    private static int getNumCoresYear() {
        int cores = DeviceInfo.getNumberOfCPUCores();
        if (cores < 1) {
            return -1;
        }
        if (cores == 1) {
            return 2008;
        }
        if (cores <= 3) {
            return 2011;
        }
        return CLASS_2012;
    }

    private static int getClockSpeedYear() {
        long clockSpeedKHz = (long) DeviceInfo.getCPUMaxFreqKHz();
        if (clockSpeedKHz == -1) {
            return -1;
        }
        if (clockSpeedKHz <= 528000) {
            return 2008;
        }
        if (clockSpeedKHz <= 620000) {
            return CLASS_2009;
        }
        if (clockSpeedKHz <= 1020000) {
            return CLASS_2010;
        }
        if (clockSpeedKHz <= 1220000) {
            return 2011;
        }
        if (clockSpeedKHz <= 1520000) {
            return CLASS_2012;
        }
        if (clockSpeedKHz <= 2020000) {
            return CLASS_2013;
        }
        return CLASS_2014;
    }

    private static int getRamYear(Context c) {
        long totalRam = DeviceInfo.getTotalMemory(c);
        if (totalRam <= 0) {
            return -1;
        }
        if (totalRam <= 201326592) {
            return 2008;
        }
        if (totalRam <= 304087040) {
            return CLASS_2009;
        }
        if (totalRam <= 536870912) {
            return CLASS_2010;
        }
        if (totalRam <= 1073741824) {
            return 2011;
        }
        if (totalRam <= 1610612736) {
            return CLASS_2012;
        }
        if (totalRam <= 2147483648L) {
            return CLASS_2013;
        }
        return CLASS_2014;
    }
}
