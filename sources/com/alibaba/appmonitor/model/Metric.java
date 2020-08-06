package com.alibaba.appmonitor.model;

import android.text.TextUtils;
import com.alibaba.analytics.core.db.Entity;
import com.alibaba.analytics.core.db.annotation.Column;
import com.alibaba.analytics.core.db.annotation.Ingore;
import com.alibaba.analytics.core.db.annotation.TableName;
import com.alibaba.appmonitor.pool.Reusable;
import com.alibaba.appmonitor.sample.AMSamplingMgr;
import com.alibaba.fastjson.JSON;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.Measure;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import java.util.List;
import java.util.UUID;

@TableName("stat_register_temp")
public class Metric extends Entity implements Reusable {
    @Ingore
    private static final String SEPERATOR = "$";
    @Ingore
    private DimensionSet dimensionSet;
    @Column("dimensions")
    private String dimensions;
    @Ingore
    private String extraArg;
    @Column("is_commit_detail")
    private boolean isCommitDetail;
    @Ingore
    private MeasureSet measureSet;
    @Column("measures")
    private String measures;
    @Column("module")
    private String module;
    @Column("monitor_point")
    private String monitorPoint;
    @Ingore
    private String transactionId;

    @Deprecated
    public Metric() {
    }

    public Metric(String module2, String monitorPoint2, MeasureSet measureSet2, DimensionSet dimensionSet2, boolean isCommitDetail2) {
        this.module = module2;
        this.monitorPoint = monitorPoint2;
        this.dimensionSet = dimensionSet2;
        this.measureSet = measureSet2;
        this.extraArg = null;
        this.isCommitDetail = isCommitDetail2;
        if (dimensionSet2 != null) {
            this.dimensions = JSON.toJSONString(dimensionSet2);
        }
        this.measures = JSON.toJSONString(measureSet2);
    }

    @Deprecated
    protected Metric(String module2, String monitorPoint2, String measures2, String dimensions2, boolean isCommitDetail2) {
        this.module = module2;
        this.monitorPoint = monitorPoint2;
        this.dimensionSet = (DimensionSet) JSON.parseObject(dimensions2, DimensionSet.class);
        this.measureSet = (MeasureSet) JSON.parseObject(measures2, MeasureSet.class);
        this.extraArg = null;
        this.isCommitDetail = isCommitDetail2;
        this.dimensions = dimensions2;
        this.measures = measures2;
    }

    public synchronized String getTransactionId() {
        if (this.transactionId == null) {
            this.transactionId = UUID.randomUUID().toString() + "$" + this.module + "$" + this.monitorPoint;
        }
        return this.transactionId;
    }

    public void resetTransactionId() {
        this.transactionId = null;
    }

    public boolean valid(DimensionValueSet dimensionValues, MeasureValueSet measureValues) {
        boolean valid = true;
        if (this.dimensionSet != null) {
            valid = this.dimensionSet.valid(dimensionValues);
        }
        if (this.measureSet != null) {
            return valid && this.measureSet.valid(measureValues);
        }
        return valid;
    }

    @Deprecated
    private Measure getMeasureByName(String name, List<Measure> measures2) {
        if (measures2 != null) {
            for (Measure measure : measures2) {
                if (TextUtils.equals(name, measure.name)) {
                    return measure;
                }
            }
        }
        return null;
    }

    public String getModule() {
        return this.module;
    }

    public String getMonitorPoint() {
        return this.monitorPoint;
    }

    public DimensionSet getDimensionSet() {
        if (this.dimensionSet == null && !TextUtils.isEmpty(this.dimensions)) {
            this.dimensionSet = (DimensionSet) JSON.parseObject(this.dimensions, DimensionSet.class);
        }
        return this.dimensionSet;
    }

    public MeasureSet getMeasureSet() {
        if (this.measureSet == null && !TextUtils.isEmpty(this.measures)) {
            this.measureSet = (MeasureSet) JSON.parseObject(this.measures, MeasureSet.class);
        }
        return this.measureSet;
    }

    public synchronized boolean isCommitDetail() {
        return this.isCommitDetail || AMSamplingMgr.getInstance().isDetail(this.module, this.monitorPoint);
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((((this.extraArg == null ? 0 : this.extraArg.hashCode()) + 31) * 31) + (this.module == null ? 0 : this.module.hashCode())) * 31;
        if (this.monitorPoint != null) {
            i = this.monitorPoint.hashCode();
        }
        return hashCode + i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Metric other = (Metric) obj;
        if (this.extraArg == null) {
            if (other.extraArg != null) {
                return false;
            }
        } else if (!this.extraArg.equals(other.extraArg)) {
            return false;
        }
        if (this.module == null) {
            if (other.module != null) {
                return false;
            }
        } else if (!this.module.equals(other.module)) {
            return false;
        }
        if (this.monitorPoint == null) {
            if (other.monitorPoint != null) {
                return false;
            }
            return true;
        } else if (!this.monitorPoint.equals(other.monitorPoint)) {
            return false;
        } else {
            return true;
        }
    }

    public void clean() {
        this.module = null;
        this.monitorPoint = null;
        this.extraArg = null;
        this.isCommitDetail = false;
        this.dimensionSet = null;
        this.measureSet = null;
        this.transactionId = null;
    }

    public void fill(Object... params) {
        this.module = params[0];
        this.monitorPoint = params[1];
        if (params.length > 2) {
            this.extraArg = params[2];
        }
    }
}
