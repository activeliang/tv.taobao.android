package com.alibaba.motu.videoplayermonitor.model;

import com.alibaba.motu.videoplayermonitor.VPMConstants;
import java.util.HashMap;
import java.util.Map;

public class MotuMediaBase {
    public Map<String, String> extInfoData = null;
    public MotuMediaType mediaType;
    public String playerCore;
    public String sourceIdentity;
    public String videoFormat;

    public Map<String, String> toBaseMap() {
        Map<String, String> map = new HashMap<>();
        if (this.mediaType != null) {
            map.put(VPMConstants.DIMENSION_MEDIATYPE, this.mediaType.getValue() + "");
        } else {
            map.put(VPMConstants.DIMENSION_MEDIATYPE, "-1");
        }
        if (this.videoFormat != null) {
            map.put(VPMConstants.DIMENSION_VIDEOFORMAT, this.videoFormat);
        } else {
            map.put(VPMConstants.DIMENSION_VIDEOFORMAT, "-1");
        }
        if (this.sourceIdentity != null) {
            map.put(VPMConstants.DIMENSION_SOURCEIDENTYTY, this.sourceIdentity);
        } else {
            map.put(VPMConstants.DIMENSION_SOURCEIDENTYTY, "-1");
        }
        if (this.playerCore != null) {
            map.put(VPMConstants.DIMENSION_PLAYERCORE, this.playerCore);
        } else {
            map.put(VPMConstants.DIMENSION_PLAYERCORE, "-1");
        }
        if (this.extInfoData != null && this.extInfoData.size() > 0) {
            map.putAll(this.extInfoData);
        }
        return map;
    }
}
