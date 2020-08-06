package org.greenrobot.eventbus;

import android.util.Log;
import java.util.logging.Level;

public interface Logger {
    void log(Level level, String str);

    void log(Level level, String str, Throwable th);

    public static class AndroidLogger implements Logger {
        static final boolean ANDROID_LOG_AVAILABLE;
        private final String tag;

        static {
            boolean android2 = false;
            try {
                android2 = Class.forName("android.util.Log") != null;
            } catch (ClassNotFoundException e) {
            }
            ANDROID_LOG_AVAILABLE = android2;
        }

        public static boolean isAndroidLogAvailable() {
            return ANDROID_LOG_AVAILABLE;
        }

        public AndroidLogger(String tag2) {
            this.tag = tag2;
        }

        public void log(Level level, String msg) {
            if (level != Level.OFF) {
                Log.println(mapLevel(level), this.tag, msg);
            }
        }

        public void log(Level level, String msg, Throwable th) {
            if (level != Level.OFF) {
                Log.println(mapLevel(level), this.tag, msg + "\n" + Log.getStackTraceString(th));
            }
        }

        /* access modifiers changed from: protected */
        public int mapLevel(Level level) {
            int value = level.intValue();
            if (value < 800) {
                if (value < 500) {
                    return 2;
                }
                return 3;
            } else if (value < 900) {
                return 4;
            } else {
                if (value < 1000) {
                    return 5;
                }
                return 6;
            }
        }
    }

    public static class JavaLogger implements Logger {
        protected final java.util.logging.Logger logger;

        public JavaLogger(String tag) {
            this.logger = java.util.logging.Logger.getLogger(tag);
        }

        public void log(Level level, String msg) {
            this.logger.log(level, msg);
        }

        public void log(Level level, String msg, Throwable th) {
            this.logger.log(level, msg, th);
        }
    }

    public static class SystemOutLogger implements Logger {
        public void log(Level level, String msg) {
            System.out.println("[" + level + "] " + msg);
        }

        public void log(Level level, String msg, Throwable th) {
            System.out.println("[" + level + "] " + msg);
            th.printStackTrace(System.out);
        }
    }
}
