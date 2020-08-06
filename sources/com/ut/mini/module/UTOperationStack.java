package com.ut.mini.module;

import android.text.TextUtils;
import java.util.List;
import java.util.Vector;

public class UTOperationStack {
    private static final String DEFAULT_SEPARATOR = ",";
    private static final int MAX_HISTORY_AMOUNT = 1000;
    private static UTOperationStack s_instance = new UTOperationStack();
    private List<String> mActionHistory = new Vector();
    private int mHistoryAmount = 1000;

    private UTOperationStack() {
    }

    public static UTOperationStack getInstance() {
        return s_instance;
    }

    public String getOperationHistory(int aAmount, String aSeparator) {
        boolean lIsFirst = true;
        StringBuffer lResult = new StringBuffer(500);
        if (TextUtils.isEmpty(aSeparator)) {
            aSeparator = ",";
        }
        if (aAmount <= 0) {
            return null;
        }
        if (aAmount >= this.mActionHistory.size()) {
            for (String lAction : this.mActionHistory) {
                if (!lIsFirst) {
                    lResult.append(aSeparator);
                }
                lResult.append(lAction);
                lIsFirst = false;
            }
        } else {
            int lUpStart = (this.mActionHistory.size() - aAmount) - 1;
            int i = lUpStart;
            while (lUpStart < this.mActionHistory.size()) {
                String action = this.mActionHistory.get(i);
                if (!TextUtils.isEmpty(action)) {
                    if (!lIsFirst) {
                        lResult.append(aSeparator);
                    }
                    lResult.append(action);
                    lIsFirst = false;
                }
                i++;
            }
        }
        return lResult.toString();
    }

    public void addAction(String action) {
        if (!TextUtils.isEmpty(action)) {
            if (this.mActionHistory.size() >= this.mHistoryAmount) {
                this.mActionHistory.remove(0);
            }
            this.mActionHistory.add(action);
        }
    }

    public void clear() {
        if (this.mActionHistory != null) {
            this.mActionHistory.clear();
        }
    }
}
