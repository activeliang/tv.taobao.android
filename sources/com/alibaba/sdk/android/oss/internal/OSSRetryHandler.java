package com.alibaba.sdk.android.oss.internal;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.OSSLog;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;

public class OSSRetryHandler {
    private int maxRetryCount = 2;

    public OSSRetryHandler(int maxRetryCount2) {
        setMaxRetryCount(maxRetryCount2);
    }

    public void setMaxRetryCount(int maxRetryCount2) {
        this.maxRetryCount = maxRetryCount2;
    }

    public OSSRetryType shouldRetry(Exception e, int currentRetryCount) {
        if (currentRetryCount >= this.maxRetryCount) {
            return OSSRetryType.OSSRetryTypeShouldNotRetry;
        }
        if (e instanceof ClientException) {
            if (((ClientException) e).isCanceledException().booleanValue()) {
                return OSSRetryType.OSSRetryTypeShouldNotRetry;
            }
            Exception localException = (Exception) e.getCause();
            if ((localException instanceof InterruptedIOException) && !(localException instanceof SocketTimeoutException)) {
                OSSLog.logError("[shouldRetry] - is interrupted!");
                return OSSRetryType.OSSRetryTypeShouldNotRetry;
            } else if (localException instanceof IllegalArgumentException) {
                return OSSRetryType.OSSRetryTypeShouldNotRetry;
            } else {
                OSSLog.logDebug("shouldRetry - " + e.toString());
                e.getCause().printStackTrace();
                return OSSRetryType.OSSRetryTypeShouldRetry;
            }
        } else if (!(e instanceof ServiceException)) {
            return OSSRetryType.OSSRetryTypeShouldNotRetry;
        } else {
            ServiceException serviceException = (ServiceException) e;
            if (serviceException.getErrorCode() != null && serviceException.getErrorCode().equalsIgnoreCase("RequestTimeTooSkewed")) {
                return OSSRetryType.OSSRetryTypeShouldFixedTimeSkewedAndRetry;
            }
            if (serviceException.getStatusCode() >= 500) {
                return OSSRetryType.OSSRetryTypeShouldRetry;
            }
            return OSSRetryType.OSSRetryTypeShouldNotRetry;
        }
    }

    public long timeInterval(int currentRetryCount, OSSRetryType retryType) {
        switch (retryType) {
            case OSSRetryTypeShouldRetry:
                return ((long) Math.pow(2.0d, (double) currentRetryCount)) * 200;
            default:
                return 0;
        }
    }
}
