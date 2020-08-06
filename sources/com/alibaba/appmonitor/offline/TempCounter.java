package com.alibaba.appmonitor.offline;

import com.alibaba.analytics.core.db.annotation.Column;
import com.alibaba.analytics.core.db.annotation.TableName;

@TableName("counter_temp")
public class TempCounter extends TempEvent {
    @Column("arg")
    public String arg;
    @Column("value")
    public double value;

    public TempCounter(String module, String monitorPoint, String arg2, double value2, String access, String accsssSubType) {
        super(module, monitorPoint, access, accsssSubType);
        this.arg = arg2;
        this.value = value2;
    }

    public TempCounter() {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("TempCounter{");
        sb.append("arg='").append(this.arg).append('\'');
        sb.append(", value=").append(this.value);
        sb.append('}');
        return sb.toString();
    }
}
