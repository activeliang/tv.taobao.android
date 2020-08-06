package com.alibaba.motu.videoplayermonitor;

import android.text.TextUtils;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValue;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import java.util.Map;

public class MotuTransaction {
    private MeasureValueSet mvs = MeasureValueSet.create();
    private String name = null;
    private MotuMediaInfo videoInfo = null;

    public MotuTransaction(String name2, MotuMediaInfo info) {
        this.name = name2;
        this.videoInfo = info;
    }

    public synchronized void addSegment(String segment, double duration) {
        if (segment != null) {
            this.mvs.setValue(segment, duration);
        }
    }

    public synchronized void commit() {
        if (this.mvs != null) {
            if (this.videoInfo != null && !TextUtils.isEmpty(this.name)) {
                MeasureSet ms = MeasureSet.create();
                Map<String, MeasureValue> map = this.mvs.getMap();
                if (map != null) {
                    for (String measure : map.keySet()) {
                        ms.addMeasure(measure);
                        Logger.d("register", "measure", measure);
                    }
                }
                DimensionSet ds = DimensionSet.create();
                ds.addDimension(VPMConstants.DIMENSION_MEDIATYPE);
                ds.addDimension(VPMConstants.DIMENSION_VIDEOWIDTH);
                ds.addDimension(VPMConstants.DIMENSION_VIDEOHEIGHT);
                ds.addDimension(VPMConstants.DIMENSION_VIDEOCODE);
                ds.addDimension(VPMConstants.DIMENSION_SCREENSIZE);
                AppMonitor.register(VPMConstants.VPM, this.name, ms, ds, true);
                DimensionValueSet dvs = DimensionValueSet.create();
                dvs.setMap(this.videoInfo.toMap());
                AppMonitor.Stat.commit(VPMConstants.VPM, this.name, dvs, this.mvs);
                this.mvs = null;
                this.videoInfo = null;
                this.name = null;
            }
        }
    }
}
