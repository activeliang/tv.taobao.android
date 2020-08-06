package com.zhiping.dev.android.logger;

public class ZpLoggerConfig {
    private static Level l = Level.v;

    public static void setLevelCfg(Level level) {
        if (level != null) {
            l = level;
        }
    }

    static boolean approve(Level level) {
        if (level == null || level.val < l.val) {
            return false;
        }
        return true;
    }

    public enum Level {
        v(2),
        d(3),
        i(4),
        w(5),
        e(6),
        a(7),
        shutdown(8);
        
        /* access modifiers changed from: private */
        public int val;

        private Level(int val2) {
            this.val = val2;
        }

        public int getVal() {
            return this.val;
        }

        public static Level from(int val2) {
            if (val2 == v.val) {
                return v;
            }
            if (val2 == d.val) {
                return d;
            }
            if (val2 == i.val) {
                return i;
            }
            if (val2 == w.val) {
                return w;
            }
            if (val2 == e.val) {
                return e;
            }
            if (val2 == a.val) {
                return a;
            }
            if (val2 == shutdown.val) {
                return shutdown;
            }
            return null;
        }
    }
}
