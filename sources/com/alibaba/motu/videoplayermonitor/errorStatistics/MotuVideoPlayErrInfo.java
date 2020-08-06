package com.alibaba.motu.videoplayermonitor.errorStatistics;

import com.alibaba.motu.videoplayermonitor.VPMConstants;
import com.alibaba.motu.videoplayermonitor.model.MotuErrorInfoBase;
import java.util.Map;

public class MotuVideoPlayErrInfo extends MotuErrorInfoBase {
    public String bussinessType;
    public String cdnIP;
    public String playWay;
    public String videoPlayType;

    public Map<String, String> toMap() {
        Map<String, String> map = toBaseMap();
        Map<String, String> errInfoMap = toErrorInfoMap();
        if (errInfoMap != null) {
            map.putAll(errInfoMap);
        }
        if (this.errorCode != null) {
            map.put(VPMConstants.DIMENSION_VIDEOERRORCODE, this.errorCode);
        }
        if (this.errorMsg != null) {
            map.put(VPMConstants.DIMENSION_VIDEOERRMSA, this.errorMsg);
        }
        if (this.bussinessType != null) {
            map.put(VPMConstants.DIMENSION_BUSINESSTYPE, this.bussinessType);
        }
        if (this.playWay != null) {
            map.put(VPMConstants.DIMENSION_PLAYWAY, this.playWay);
        } else {
            map.put(VPMConstants.DIMENSION_PLAYWAY, "-1");
        }
        if (this.videoPlayType != null) {
            map.put(VPMConstants.DIMENSION_VIDEOPLAYTYPE, this.videoPlayType);
        } else {
            map.put(VPMConstants.DIMENSION_VIDEOPLAYTYPE, "-1");
        }
        if (this.cdnIP != null) {
            map.put(VPMConstants.DIMENSION_CDNIP, this.cdnIP);
        } else {
            map.put(VPMConstants.DIMENSION_CDNIP, "-1");
        }
        return map;
    }
}
