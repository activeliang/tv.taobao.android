package com.alibaba.appmonitor.offline;

import android.text.TextUtils;
import com.alibaba.analytics.core.db.annotation.Column;
import com.alibaba.analytics.core.db.annotation.TableName;
import com.alibaba.fastjson.JSON;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;

@TableName("stat_temp")
public class TempStat extends TempEvent {
    @Column("dimension_values")
    private String dimension_values;
    @Column("measure_values")
    private String measure_values;

    public TempStat() {
    }

    public TempStat(String module, String monitorPoint, DimensionValueSet dimensionValues, MeasureValueSet measureValues, String access, String accsssSubType) {
        super(module, monitorPoint, access, accsssSubType);
        this.dimension_values = JSON.toJSONString(dimensionValues);
        this.measure_values = JSON.toJSONString(measureValues);
    }

    public MeasureValueSet getMeasureVauleSet() {
        if (!TextUtils.isEmpty(this.measure_values)) {
            return (MeasureValueSet) JSON.parseObject(this.measure_values, MeasureValueSet.class);
        }
        return null;
    }

    public DimensionValueSet getDimensionValue() {
        if (!TextUtils.isEmpty(this.dimension_values)) {
            return (DimensionValueSet) JSON.parseObject(this.dimension_values, DimensionValueSet.class);
        }
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("TempStat{");
        sb.append("module='").append(this.module).append('\'');
        sb.append("monitorPoint='").append(this.monitorPoint).append('\'');
        sb.append("dimension_values='").append(this.dimension_values).append('\'');
        sb.append(", measure_values='").append(this.measure_values).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
