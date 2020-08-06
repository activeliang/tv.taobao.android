package com.alibaba.appmonitor.sample;

import com.alibaba.analytics.core.db.annotation.Column;
import com.alibaba.analytics.core.db.annotation.TableName;
import com.alibaba.analytics.utils.Logger;
import java.util.ArrayList;
import java.util.Map;

@TableName("ap_alarm")
public class AlarmConfig extends AMConifg {
    @Column("fcp")
    protected int failSampling = 0;
    @Column("scp")
    protected int successSampling = 0;

    public boolean isSampled(int samplingSeed, String module, String monitorPoint, Boolean isSuccess, Map<String, String> map) {
        ArrayList<String> keys = new ArrayList<>(2);
        keys.add(module);
        keys.add(monitorPoint);
        return sampling(samplingSeed, keys, isSuccess.booleanValue());
    }

    @Deprecated
    public boolean isSampled(int samplingSeed, String module, String monitorPoint, Boolean isSuccess) {
        return isSampled(samplingSeed, module, monitorPoint, isSuccess, (Map<String, String>) null);
    }

    private boolean sampling(int samplingSeed, ArrayList<String> keys, boolean isSuccess) {
        if (keys == null || keys.size() == 0) {
            return checkSelfSampling(samplingSeed, isSuccess);
        }
        String nextkey = keys.remove(0);
        if (isContains(nextkey)) {
            return ((AlarmConfig) getNext(nextkey)).sampling(samplingSeed, keys, isSuccess);
        }
        return checkSelfSampling(samplingSeed, isSuccess);
    }

    private boolean checkSelfSampling(int samplingSeed, boolean isSuccess) {
        if (isSuccess) {
            Logger.d("", "samplingSeed", Integer.valueOf(samplingSeed), SampleConfigConstant.SAMPLING, Integer.valueOf(this.successSampling));
            if (samplingSeed < this.successSampling) {
                return true;
            }
            return false;
        }
        Logger.d("", "samplingSeed", Integer.valueOf(samplingSeed), SampleConfigConstant.SAMPLING, Integer.valueOf(this.failSampling));
        if (samplingSeed >= this.failSampling) {
            return false;
        }
        return true;
    }

    public void setSampling(int sampling) {
        this.successSampling = sampling;
        this.failSampling = sampling;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("AlarmConfig{");
        sb.append("module=").append(this.module);
        sb.append(", monitorPoint=").append(this.monitorPoint);
        sb.append(", offline=").append(this.offline);
        sb.append(", failSampling=").append(this.failSampling);
        sb.append(", successSampling=").append(this.successSampling);
        sb.append('}');
        return sb.toString();
    }
}
