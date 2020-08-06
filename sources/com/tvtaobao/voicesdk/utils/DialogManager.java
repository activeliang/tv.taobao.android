package com.tvtaobao.voicesdk.utils;

import android.app.Dialog;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DialogManager {
    private static DialogManager manager;
    private List<WeakReference<Dialog>> mDialogList = new ArrayList();

    private DialogManager() {
        this.mDialogList.clear();
    }

    public static DialogManager getManager() {
        if (manager == null) {
            synchronized (DialogManager.class) {
                if (manager == null) {
                    manager = new DialogManager();
                }
            }
        }
        return manager;
    }

    public void onClearAllDialogList() {
        this.mDialogList.clear();
    }

    public void dismissAllDialog() {
        int size = this.mDialogList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                if (!(this.mDialogList.get(i) == null || this.mDialogList.get(i).get() == null || !((Dialog) this.mDialogList.get(i).get()).isShowing())) {
                    ((Dialog) this.mDialogList.get(i).get()).dismiss();
                    this.mDialogList.remove(i);
                }
            }
            onClearAllDialogList();
        }
    }

    public void dismissBeforeDialog() {
        int size = this.mDialogList.size() - 1;
        for (int i = 0; i < size; i++) {
            if (!(this.mDialogList.get(i) == null || this.mDialogList.get(i).get() == null || !((Dialog) this.mDialogList.get(i).get()).isShowing())) {
                ((Dialog) this.mDialogList.get(i).get()).dismiss();
                this.mDialogList.remove(i);
            }
        }
    }

    public void pushDialog(Dialog dialog) {
        this.mDialogList.add(new WeakReference(dialog));
    }
}
