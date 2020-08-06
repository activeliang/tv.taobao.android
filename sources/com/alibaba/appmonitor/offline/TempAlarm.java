package com.alibaba.appmonitor.offline;

import com.alibaba.analytics.core.db.annotation.Column;
import com.alibaba.analytics.core.db.annotation.TableName;

@TableName("alarm_temp")
public class TempAlarm extends TempEvent {
    @Column("arg")
    public String arg;
    @Column("err_code")
    public String errCode;
    @Column("err_msg")
    public String errMsg;
    @Column("success")
    public String success;

    public TempAlarm() {
    }

    public TempAlarm(String module, String monitorPoint, String arg2, String errCode2, String errMsg2, boolean success2, String access, String accessSubType) {
        super(module, monitorPoint, access, accessSubType);
        this.arg = arg2;
        this.errCode = errCode2;
        this.errMsg = errMsg2;
        this.success = success2 ? "1" : "0";
    }

    public boolean isSuccessEvent() {
        return "1".equalsIgnoreCase(this.success);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("TempAlarm{");
        sb.append(" module='").append(this.module).append('\'');
        sb.append(", monitorPoint='").append(this.monitorPoint).append('\'');
        sb.append(", commitTime=").append(this.commitTime);
        sb.append(", access='").append(this.access).append('\'');
        sb.append(", accessSubType='").append(this.accessSubType).append('\'');
        sb.append(", arg='").append(this.arg).append('\'');
        sb.append(", errCode='").append(this.errCode).append('\'');
        sb.append(", errMsg='").append(this.errMsg).append('\'');
        sb.append(", success='").append(this.success).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
