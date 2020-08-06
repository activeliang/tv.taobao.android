package com.alibaba.appmonitor.event;

import com.alibaba.analytics.utils.StringUtils;
import com.alibaba.appmonitor.pool.BalancedPool;

@Deprecated
public class RawAlarmEvent extends Event implements IRawEvent {
    private static final String ERROR_CODE_KEY = "errorCode";
    private static final String ERROR_MSG_KEY = "errorMsg";
    private String errorCode;
    private String errorMsg;
    private int failCount = 0;
    private int successCount = 0;

    public void setSuccess() {
        this.successCount = 1;
    }

    public void setFail(String errorCode2, String errorMsg2) {
        this.failCount = 1;
        this.errorCode = errorCode2;
        this.errorMsg = errorMsg2;
    }

    public UTEvent dumpToUTEvent() {
        UTEvent event = (UTEvent) BalancedPool.getInstance().poll(UTEvent.class, new Object[0]);
        event.eventId = this.eventId;
        event.page = this.module;
        event.arg1 = this.monitorPoint;
        event.arg2 = String.valueOf(this.successCount);
        event.arg3 = String.valueOf(this.failCount);
        if (StringUtils.isNotBlank(this.errorCode)) {
            event.args.put("errorCode", this.errorCode);
        }
        if (StringUtils.isNotBlank(this.errorMsg)) {
            event.args.put(ERROR_MSG_KEY, this.errorMsg);
        }
        if (this.extraArg != null) {
            event.args.put("arg", this.extraArg);
        }
        return event;
    }

    public int getSuccessCount() {
        return this.successCount;
    }

    public int getFailCount() {
        return this.failCount;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void clean() {
        super.clean();
        this.successCount = 0;
        this.failCount = 0;
        this.errorCode = null;
        this.errorMsg = null;
    }
}
