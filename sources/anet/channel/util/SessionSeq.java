package anet.channel.util;

import android.text.TextUtils;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionSeq {
    private static AtomicInteger mIndex = new AtomicInteger();

    public static String createSequenceNo(String prefix) {
        if (mIndex.get() == Integer.MAX_VALUE) {
            mIndex.set(0);
        }
        if (!TextUtils.isEmpty(prefix)) {
            return StringUtils.concatString(prefix, ".AWCN", String.valueOf(mIndex.incrementAndGet()));
        }
        return StringUtils.concatString("AWCN", String.valueOf(mIndex.incrementAndGet()));
    }
}
