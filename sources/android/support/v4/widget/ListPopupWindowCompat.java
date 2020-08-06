package android.support.v4.widget;

import android.os.Build;
import android.view.View;

public final class ListPopupWindowCompat {
    static final ListPopupWindowImpl IMPL;

    interface ListPopupWindowImpl {
        View.OnTouchListener createDragToOpenListener(Object obj, View view);
    }

    static class BaseListPopupWindowImpl implements ListPopupWindowImpl {
        BaseListPopupWindowImpl() {
        }

        public View.OnTouchListener createDragToOpenListener(Object listPopupWindow, View src) {
            return null;
        }
    }

    static class KitKatListPopupWindowImpl extends BaseListPopupWindowImpl {
        KitKatListPopupWindowImpl() {
        }

        public View.OnTouchListener createDragToOpenListener(Object listPopupWindow, View src) {
            return ListPopupWindowCompatKitKat.createDragToOpenListener(listPopupWindow, src);
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 19) {
            IMPL = new KitKatListPopupWindowImpl();
        } else {
            IMPL = new BaseListPopupWindowImpl();
        }
    }

    private ListPopupWindowCompat() {
    }

    public static View.OnTouchListener createDragToOpenListener(Object listPopupWindow, View src) {
        return IMPL.createDragToOpenListener(listPopupWindow, src);
    }
}
