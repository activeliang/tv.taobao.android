package com.alibaba.motu.videoplayermonitor.model;

import com.alibaba.motu.videoplayermonitor.VPMConstants;
import java.util.HashMap;
import java.util.Map;

public class MotuErrorInfoBase extends MotuMediaBase {
    public String errorCode;
    public String errorMsg;
    public Boolean isSuccess;

    public Map<String, String> toErrorInfoMap() {
        Map<String, String> map = new HashMap<>();
        if (this.isSuccess != null) {
            if (this.isSuccess.booleanValue()) {
                map.put(VPMConstants.DIMENSION_ISSUCCESS, "1");
            } else {
                map.put(VPMConstants.DIMENSION_ISSUCCESS, "0");
            }
        }
        return map;
    }
}
