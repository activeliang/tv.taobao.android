package com.yunos.tvtaobao.biz.focus_impl;

import android.view.KeyEvent;

public interface FocusFinder {
    FocusNode findFittestLeaf();

    FocusNode findNext(Routine routine, int i, KeyEvent keyEvent);

    public static class Routine {
        public long id = -1;

        private Routine() {
        }

        public Routine(long id2) {
            this.id = id2;
        }

        public static Routine getNew() {
            return new Routine(System.currentTimeMillis());
        }
    }
}
