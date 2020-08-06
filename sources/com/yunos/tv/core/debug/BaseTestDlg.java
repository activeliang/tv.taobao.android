package com.yunos.tv.core.debug;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public abstract class BaseTestDlg extends Dialog {
    private static ArrayList<WeakReference<BaseTestDlg>> dlgList = new ArrayList<>();

    public BaseTestDlg(@NonNull Context context) {
        super(context);
        requestWindowFeature(1);
        dlgList.add(new WeakReference(this));
    }

    public static void onDestroy() {
        if (dlgList != null) {
            for (int i = dlgList.size() - 1; i >= 0; i--) {
                WeakReference<BaseTestDlg> item = dlgList.get(i);
                if (!(item == null || item.get() == null)) {
                    try {
                        ((BaseTestDlg) item.get()).dismiss();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
            dlgList.clear();
        }
    }
}
