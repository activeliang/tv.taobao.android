package com.taobao.detail.toolkit;

import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Profiler {
    public static boolean SWITCHER = true;
    private static final ThreadLocal entryStack = new ThreadLocal();

    public interface Message {
        String getBriefMessage();

        String getDetailedMessage();
    }

    public static void start() {
        if (SWITCHER) {
            start((String) null);
        }
    }

    public static void start(String message) {
        if (SWITCHER) {
            entryStack.set(new Entry(message, (Entry) null, (Entry) null));
        }
    }

    public static void start(Message message) {
        if (SWITCHER) {
            entryStack.set(new Entry(message, (Entry) null, (Entry) null));
        }
    }

    public static void reset() {
        if (SWITCHER) {
            entryStack.set((Object) null);
        }
    }

    public static void enter(String message) {
        Entry currentEntry;
        if (SWITCHER && (currentEntry = getCurrentEntry()) != null) {
            currentEntry.enterSubEntry(message);
        }
    }

    public static void enter(Message message) {
        Entry currentEntry;
        if (SWITCHER && (currentEntry = getCurrentEntry()) != null) {
            currentEntry.enterSubEntry(message);
        }
    }

    public static void release() {
        Entry currentEntry;
        if (SWITCHER && (currentEntry = getCurrentEntry()) != null) {
            currentEntry.release();
        }
    }

    public static long getDuration() {
        if (!SWITCHER) {
            return 0;
        }
        Entry entry = (Entry) entryStack.get();
        if (entry != null) {
            return entry.getDuration();
        }
        return -1;
    }

    public static String dump() {
        if (!SWITCHER) {
            return "";
        }
        return dump("", "");
    }

    public static String dump(String prefix) {
        return dump(prefix, prefix);
    }

    public static String dump(String prefix1, String prefix2) {
        Entry entry = (Entry) entryStack.get();
        if (entry != null) {
            return entry.toString(prefix1, prefix2);
        }
        return "";
    }

    public static Entry getEntry() {
        return (Entry) entryStack.get();
    }

    private static Entry getCurrentEntry() {
        Entry subEntry = (Entry) entryStack.get();
        Entry entry = null;
        if (subEntry != null) {
            do {
                entry = subEntry;
                subEntry = entry.getUnreleasedEntry();
            } while (subEntry != null);
        }
        return entry;
    }

    public static final class Entry {
        private final long baseTime;
        private long endTime;
        private final Entry firstEntry;
        private final Object message;
        private final Entry parentEntry;
        private final long startTime;
        private final List subEntries;

        private Entry(Object message2, Entry parentEntry2, Entry firstEntry2) {
            Entry entry;
            this.subEntries = new ArrayList(4);
            this.message = message2;
            this.startTime = System.currentTimeMillis();
            this.parentEntry = parentEntry2;
            if (firstEntry2 == null) {
                entry = this;
            } else {
                entry = firstEntry2;
            }
            this.firstEntry = entry;
            this.baseTime = firstEntry2 == null ? 0 : firstEntry2.startTime;
        }

        public String getMessage() {
            if (this.message instanceof String) {
                return (String) this.message;
            }
            if (this.message instanceof Message) {
                return ((Message) this.message).getDetailedMessage();
            }
            return null;
        }

        public long getStartTime() {
            if (this.baseTime > 0) {
                return this.startTime - this.baseTime;
            }
            return 0;
        }

        public long getEndTime() {
            if (this.endTime < this.baseTime) {
                return -1;
            }
            return this.endTime - this.baseTime;
        }

        public long getDuration() {
            if (this.endTime < this.startTime) {
                return -1;
            }
            return this.endTime - this.startTime;
        }

        public long getDurationOfSelf() {
            long duration = getDuration();
            if (duration < 0) {
                return -1;
            }
            if (this.subEntries.isEmpty()) {
                return duration;
            }
            for (int i = 0; i < this.subEntries.size(); i++) {
                duration -= ((Entry) this.subEntries.get(i)).getDuration();
            }
            if (duration >= 0) {
                return duration;
            }
            return -1;
        }

        public double getPecentage() {
            double parentDuration = ClientTraceData.b.f47a;
            double duration = (double) getDuration();
            if (this.parentEntry != null && this.parentEntry.isReleased()) {
                parentDuration = (double) this.parentEntry.getDuration();
            }
            if (duration <= ClientTraceData.b.f47a || parentDuration <= ClientTraceData.b.f47a) {
                return ClientTraceData.b.f47a;
            }
            return duration / parentDuration;
        }

        public double getPecentageOfAll() {
            double firstDuration = ClientTraceData.b.f47a;
            double duration = (double) getDuration();
            if (this.firstEntry != null && this.firstEntry.isReleased()) {
                firstDuration = (double) this.firstEntry.getDuration();
            }
            if (duration <= ClientTraceData.b.f47a || firstDuration <= ClientTraceData.b.f47a) {
                return ClientTraceData.b.f47a;
            }
            return duration / firstDuration;
        }

        public List getSubEntries() {
            return Collections.unmodifiableList(this.subEntries);
        }

        /* access modifiers changed from: private */
        public void release() {
            if (Profiler.SWITCHER) {
                this.endTime = System.currentTimeMillis();
            }
        }

        private boolean isReleased() {
            return this.endTime > 0;
        }

        /* access modifiers changed from: private */
        public void enterSubEntry(Object message2) {
            this.subEntries.add(new Entry(message2, this, this.firstEntry));
        }

        /* access modifiers changed from: private */
        public Entry getUnreleasedEntry() {
            if (this.subEntries.isEmpty()) {
                return null;
            }
            Entry subEntry = (Entry) this.subEntries.get(this.subEntries.size() - 1);
            if (subEntry.isReleased()) {
                return null;
            }
            return subEntry;
        }

        public String toString() {
            return toString("", "");
        }

        /* access modifiers changed from: private */
        public String toString(String prefix1, String prefix2) {
            StringBuffer buffer = new StringBuffer();
            toString(buffer, prefix1, prefix2);
            return buffer.toString();
        }

        private void toString(StringBuffer buffer, String prefix1, String prefix2) {
            buffer.append(prefix1);
            String message2 = getMessage();
            long startTime2 = getStartTime();
            long duration = getDuration();
            long durationOfSelf = getDurationOfSelf();
            double percent = getPecentage();
            double percentOfAll = getPecentageOfAll();
            Object[] params = {message2, new Long(startTime2), new Long(duration), new Long(durationOfSelf), new Double(percent), new Double(percentOfAll)};
            StringBuffer pattern = new StringBuffer("{1,number} ");
            if (isReleased()) {
                pattern.append("[{2,number}ms");
                if (durationOfSelf > 0 && durationOfSelf != duration) {
                    pattern.append(" ({3,number}ms)");
                }
                if (percent > ClientTraceData.b.f47a) {
                    pattern.append(", {4,number,##%}");
                }
                if (percentOfAll > ClientTraceData.b.f47a) {
                    pattern.append(", {5,number,##%}");
                }
                pattern.append("]");
            } else {
                pattern.append("[UNRELEASED]");
            }
            if (message2 != null) {
                pattern.append(" - {0}");
            }
            buffer.append(MessageFormat.format(pattern.toString(), params));
            for (int i = 0; i < this.subEntries.size(); i++) {
                Entry subEntry = (Entry) this.subEntries.get(i);
                buffer.append(10);
                if (i == this.subEntries.size() - 1) {
                    subEntry.toString(buffer, prefix2 + "`---", prefix2 + "    ");
                } else if (i == 0) {
                    subEntry.toString(buffer, prefix2 + "+---", prefix2 + "|   ");
                } else {
                    subEntry.toString(buffer, prefix2 + "+---", prefix2 + "|   ");
                }
            }
        }
    }
}
