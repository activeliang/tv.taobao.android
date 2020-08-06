package com.taobao.ju.track.impl.interfaces;

import java.util.Map;

public interface ITrack {
    Map<String, String> getDynamic(String str);

    Map<String, String> getParamMap(String str);

    String getParamValue(String str, String str2);

    String getParamValue(String str, String str2, String str3);

    Map<String, String> getRefer(String str);

    Map<String, String> getStatic(String str);

    boolean hasParam(String str, String str2);

    boolean hasPoint(String str);

    boolean isDynamic(String str, String str2);

    boolean isInternal(String str, String str2);

    boolean isRefer(String str, String str2);

    boolean isStatic(String str, String str2);
}
