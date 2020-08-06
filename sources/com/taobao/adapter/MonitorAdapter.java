package com.taobao.adapter;

import java.util.List;
import java.util.Map;

public interface MonitorAdapter {
    void alarmCommitFail(String str, String str2, String str3, String str4);

    void alarmCommitSuccess(String str, String str2, String str3);

    void register(String str, String str2, List<MonitorMeasure> list, List<MonitorDimension> list2);

    void statCommit(String str, String str2, Map<String, String> map, Map<String, Double> map2);
}
