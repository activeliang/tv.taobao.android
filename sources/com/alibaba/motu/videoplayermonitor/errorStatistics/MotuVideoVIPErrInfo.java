package com.alibaba.motu.videoplayermonitor.errorStatistics;

import com.alibaba.motu.videoplayermonitor.VPMConstants;
import com.alibaba.motu.videoplayermonitor.model.MotuErrorInfoBase;
import java.util.Map;

public class MotuVideoVIPErrInfo extends MotuErrorInfoBase {
    public Map<String, String> toMap() {
        Map<String, String> map = toBaseMap();
        Map<String, String> errInfoMap = toErrorInfoMap();
        if (errInfoMap != null) {
            map.putAll(errInfoMap);
        }
        if (this.errorCode != null) {
            map.put(VPMConstants.DIMENSION_VIPERRORCODE, this.errorCode);
        }
        if (this.errorMsg != null) {
            map.put(VPMConstants.DIMENSION_VIPERRORMSG, this.errorMsg);
        }
        return map;
    }
}
