package com.taobao.android.runtime;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.taobao.android.dex.interpret.ARTUtils;
import dalvik.system.DexFile;
import java.util.ArrayList;
import java.util.List;

public class Dex2OatService extends IntentService {
    private static final String OUTPUT_PATH_NAME_EXTRA = "outputPathName";
    private static final String SOURCE_PATH_NAME_EXTRA = "sourcePathName";
    private static final String TAG = "Dex2OatService";
    private static List<Runnable> mPostInitRunnables = new ArrayList();
    public static boolean sBootCompleted = true;
    private final Boolean mSuccess = ARTUtils.setIsDex2oatEnabled(true);

    public Dex2OatService() {
        super(TAG);
        AndroidRuntime.getInstance().setVerificationEnabled(true);
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        if (this.mSuccess != null && this.mSuccess.booleanValue() && intent != null) {
            String sourcePathName = intent.getStringExtra(SOURCE_PATH_NAME_EXTRA);
            String outputPathName = intent.getStringExtra(OUTPUT_PATH_NAME_EXTRA);
            try {
                long start = System.currentTimeMillis();
                DexFile loadDex = DexFile.loadDex(sourcePathName, outputPathName, 0);
                long currentTimeMillis = System.currentTimeMillis() - start;
            } catch (Exception e) {
                Log.e(TAG, "- DexFile loadDex fail: sourcePathName=" + sourcePathName + ", outputPathName=" + outputPathName, e);
            }
        }
    }

    static synchronized void start(Context context, String sourcePathName, String outputPathName) {
        synchronized (Dex2OatService.class) {
            Log.d(TAG, "- Dex2OatService start: sourcePathName=" + sourcePathName + ", outputPathName=" + outputPathName);
            if (sBootCompleted) {
                AsyncTask.execute(new Dex2OatTask(context, sourcePathName, outputPathName));
            } else if (mPostInitRunnables != null) {
                mPostInitRunnables.add(new Dex2OatTask(context, sourcePathName, outputPathName));
            }
        }
    }

    public static synchronized void setBootCompleted(boolean bootCompleted) {
        synchronized (Dex2OatService.class) {
            if (!sBootCompleted) {
                sBootCompleted = bootCompleted;
                if (sBootCompleted) {
                    Log.d(TAG, "- Dex2OatService setBootCompleted: sBootCompleted=" + sBootCompleted);
                    for (Runnable postInitRunnable : mPostInitRunnables) {
                        postInitRunnable.run();
                    }
                    mPostInitRunnables = null;
                }
            }
        }
    }

    private static class Dex2OatTask implements Runnable {
        private final Context mContext;
        private final String mOutputPathName;
        private final String mSourcePathName;

        public Dex2OatTask(Context context, String sourcePathName, String outputPathName) {
            this.mContext = context;
            this.mSourcePathName = sourcePathName;
            this.mOutputPathName = outputPathName;
        }

        public void run() {
            try {
                Intent intent = new Intent(this.mContext, Dex2OatService.class);
                intent.putExtra(Dex2OatService.SOURCE_PATH_NAME_EXTRA, this.mSourcePathName);
                intent.putExtra(Dex2OatService.OUTPUT_PATH_NAME_EXTRA, this.mOutputPathName);
                this.mContext.startService(intent);
            } catch (Exception e) {
                Log.e(Dex2OatService.TAG, "- Dex2OatService start fail: sourcePathName=" + this.mSourcePathName + ", outputPathName=" + this.mOutputPathName, e);
            }
        }
    }
}
