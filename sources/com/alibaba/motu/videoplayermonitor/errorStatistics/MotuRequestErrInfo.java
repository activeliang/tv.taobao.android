package com.alibaba.motu.videoplayermonitor.errorStatistics;

import com.alibaba.motu.videoplayermonitor.VPMConstants;
import com.alibaba.motu.videoplayermonitor.model.MotuErrorInfoBase;
import java.util.Map;

public class MotuRequestErrInfo extends MotuErrorInfoBase {
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
            map.put(VPMConstants.DIMENSION_REQUESTERRCODE, this.errorCode);
        }
        if (this.errorMsg != null) {
            map.put(VPMConstants.DIMENSION_REQUESTERRMSG, this.errorMsg);
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
        if (this.playWay != null) {
            map.put(VPMConstants.DIMENSION_PLAYWAY, this.playWay);
        } else {
            map.put(VPMConstants.DIMENSION_PLAYWAY, "-1");
        }
        return map;
    }
}
