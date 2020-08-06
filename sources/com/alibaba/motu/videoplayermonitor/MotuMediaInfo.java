package com.alibaba.motu.videoplayermonitor;

import com.alibaba.motu.videoplayermonitor.model.MotuMediaBase;
import com.alibaba.motu.videoplayermonitor.model.MotuVideoCode;
import java.util.Map;

public class MotuMediaInfo extends MotuMediaBase {
    public String beforeDurationAdtype;
    public String playType;
    public String playWay;
    public double screenSize;
    public MotuVideoCode videoCode;
    public int videoHeight;
    public String videoProtocol;
    public int videoWidth;

    public Map<String, String> toMap() {
        Map<String, String> map = toBaseMap();
        map.put(VPMConstants.DIMENSION_VIDEOWIDTH, this.videoWidth + "");
        map.put(VPMConstants.DIMENSION_VIDEOHEIGHT, this.videoHeight + "");
        if (this.videoCode != null) {
            map.put(VPMConstants.DIMENSION_VIDEOCODE, this.videoCode.getValue() + "");
        } else {
            map.put(VPMConstants.DIMENSION_VIDEOCODE, "-1");
        }
        map.put(VPMConstants.DIMENSION_SCREENSIZE, this.screenSize + "");
        if (this.beforeDurationAdtype != null) {
            map.put(VPMConstants.DIMENSION_BEFOREDURATIONADTYPE, this.beforeDurationAdtype);
        } else {
            map.put(VPMConstants.DIMENSION_BEFOREDURATIONADTYPE, "-1");
        }
        if (this.playType != null) {
            map.put("playType", this.playType);
        } else {
            map.put("playType", "-1");
        }
        if (this.playWay != null) {
            map.put(VPMConstants.DIMENSION_PLAYWAY, this.playWay);
        } else {
            map.put(VPMConstants.DIMENSION_PLAYWAY, "-1");
        }
        if (this.videoProtocol != null) {
            map.put(VPMConstants.DIMENSION_VIDEOPROTOCOL, this.videoProtocol);
        } else {
            map.put(VPMConstants.DIMENSION_VIDEOPROTOCOL, "-1");
        }
        return map;
    }
}
