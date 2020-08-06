package anetwork.channel.util;

import java.util.concurrent.atomic.AtomicInteger;

public class SeqGen {
    private static final int mask = Integer.MAX_VALUE;
    private static AtomicInteger seq = new AtomicInteger(0);

    public static String createSeqNo(String prefix, String taskType) {
        StringBuilder builder = new StringBuilder(16);
        if (prefix != null) {
            builder.append(prefix).append('.');
        }
        if (taskType != null) {
            builder.append(taskType).append(seq.incrementAndGet() & Integer.MAX_VALUE);
        }
        return builder.toString();
    }
}
