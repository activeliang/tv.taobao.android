package com.alibaba.motu.crashreporter.ignores;

import com.alibaba.motu.crashreporter.utils.StringUtils;
import java.util.regex.Pattern;

public class NonSystemThreadIgnore implements UncaughtExceptionIgnore {
    Pattern NON_NAME_THREAD = Pattern.compile("Thread-\\d+");

    public String getName() {
        return "NonSystemThreadIgnore";
    }

    public boolean uncaughtExceptionIgnore(Thread thread, Throwable throwable) {
        String threadName = thread.getName();
        if (!StringUtils.isBlank(threadName) && !this.NON_NAME_THREAD.matcher(threadName).find() && !thread.isDaemon()) {
            return false;
        }
        return true;
    }
}
