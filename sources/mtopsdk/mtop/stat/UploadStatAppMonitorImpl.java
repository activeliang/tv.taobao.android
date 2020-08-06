package mtopsdk.mtop.stat;

import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.taobao.orange.OConstant;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import mtopsdk.common.util.TBSdkLog;

public class UploadStatAppMonitorImpl implements IUploadStats {
    private static final String TAG = "mtopsdk.UploadStatImpl";
    private static boolean mAppMonitorValid = false;

    public UploadStatAppMonitorImpl() {
        try {
            Class.forName(OConstant.REFLECT_APPMONITOR);
            mAppMonitorValid = true;
        } catch (Throwable th) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
                TBSdkLog.e(TAG, "didn't find app-monitor-sdk or ut-analytics sdk.");
            }
        }
    }

    public void onRegister(String module, String monitorPoint, Set<String> dimensions, Set<String> measures, boolean commitDetail) {
        DimensionSet dimensionSet;
        MeasureSet measureSet = null;
        if (mAppMonitorValid) {
            if (dimensions != null) {
                try {
                    dimensionSet = DimensionSet.create((Collection<String>) dimensions);
                } catch (Throwable e) {
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
                        TBSdkLog.e(TAG, "call AppMonitor.register error.", e);
                        return;
                    }
                    return;
                }
            } else {
                dimensionSet = null;
            }
            if (measures != null) {
                measureSet = MeasureSet.create((Collection<String>) measures);
            }
            AppMonitor.register(module, monitorPoint, measureSet, dimensionSet, commitDetail);
        }
    }

    public void onCommit(String module, String monitorPoint, Map<String, String> dimensions, Map<String, Double> measures) {
        if (mAppMonitorValid) {
            DimensionValueSet dimensionValues = null;
            MeasureValueSet measureValues = null;
            if (dimensions != null) {
                try {
                    dimensionValues = DimensionValueSet.create();
                    dimensionValues.setMap(dimensions);
                } catch (Throwable e) {
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
                        TBSdkLog.e(TAG, "call AppMonitor.onCommit error.", e);
                        return;
                    }
                    return;
                }
            }
            if (measures != null) {
                measureValues = MeasureValueSet.create();
                for (Map.Entry<String, Double> entry : measures.entrySet()) {
                    measureValues.setValue(entry.getKey(), entry.getValue().doubleValue());
                }
            }
            AppMonitor.Stat.commit(module, monitorPoint, dimensionValues, measureValues);
        }
    }
}
