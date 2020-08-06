package com.alibaba.analytics.core.db;

import android.content.Context;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.model.Log;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.TaskExecutor;
import java.io.File;
import java.util.List;

public class OldDBTransferMgr {
    protected static final String TAG = "OldDBTransferMgr";
    /* access modifiers changed from: private */
    public static String usertrackDbName = "usertrack.db";

    public static void checkAndTransfer() {
        final Context context = Variables.getInstance().getContext();
        if (context != null) {
            final File dbfile = context.getDatabasePath(usertrackDbName);
            if (dbfile.exists()) {
                TaskExecutor.getInstance().submit(new Runnable() {
                    public void run() {
                        DBMgr usertrackDbMgr = new DBMgr(context, OldDBTransferMgr.usertrackDbName);
                        while (true) {
                            List<? extends Entity> logs = usertrackDbMgr.find(Log.class, (String) null, "time", 100);
                            if (logs.size() == 0) {
                                Logger.d(OldDBTransferMgr.TAG, "delete old db file:", dbfile.getAbsoluteFile());
                                dbfile.delete();
                                return;
                            }
                            usertrackDbMgr.delete(logs);
                            Variables.getInstance().getDbMgr().insert(logs);
                        }
                    }
                });
            }
        }
    }
}
