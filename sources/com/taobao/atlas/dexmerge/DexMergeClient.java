package com.taobao.atlas.dexmerge;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.util.Log;
import com.taobao.atlas.dexmerge.IDexMergeBinder;
import com.taobao.atlas.dexmerge.IDexMergeCallback;
import java.util.List;

public class DexMergeClient {
    public static final int REMOTE_TIMEOUT = 60000;
    private static final String TAG = "DexMergeClient";
    private static final int numBinderDieTries = 3;
    ServiceConnection conn = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(DexMergeClient.TAG, "Get binder" + (System.currentTimeMillis() - DexMergeClient.this.mStartTime) + " ms");
            DexMergeClient.this.dexMergeBinder = IDexMergeBinder.Stub.asInterface(service);
            boolean unused = DexMergeClient.this.isBinded = true;
            try {
                DexMergeClient.this.dexMergeBinder.registerListener(new IDexMergeCallback.Stub() {
                    public void onMergeFinish(String filePath, boolean result, String reason) {
                        if (!result) {
                            if (DexMergeClient.this.mergeCallBack != null) {
                                DexMergeClient.this.mergeCallBack.onMergeResult(false, filePath);
                            }
                            Log.e(DexMergeClient.TAG, "merge Failed:" + filePath);
                        } else if (DexMergeClient.this.mergeCallBack != null) {
                            DexMergeClient.this.mergeCallBack.onMergeResult(true, filePath);
                        }
                    }

                    public void onMergeAllFinish(boolean result, String reason) {
                        boolean unused = DexMergeClient.this.isFinished = result;
                        synchronized (DexMergeClient.this.lock) {
                            boolean unused2 = DexMergeClient.this.isTimeout = false;
                            DexMergeClient.this.lock.notifyAll();
                        }
                        Log.d(DexMergeClient.TAG, "dexMerge  " + result + (System.currentTimeMillis() - DexMergeClient.this.mStartTime) + " ms");
                    }
                });
                synchronized (DexMergeClient.this.lock) {
                    DexMergeClient.this.lock.notifyAll();
                }
                try {
                    service.linkToDeath(DexMergeClient.this.mDeathRecipient, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } catch (RemoteException e2) {
                boolean unused2 = DexMergeClient.this.isTimeout = false;
                Log.d(DexMergeClient.TAG, "dexMerge registerListener RemoteException" + (System.currentTimeMillis() - DexMergeClient.this.mStartTime) + " ms");
                synchronized (DexMergeClient.this.lock) {
                    DexMergeClient.this.lock.notifyAll();
                }
            } catch (Throwable th) {
                synchronized (DexMergeClient.this.lock) {
                    DexMergeClient.this.lock.notifyAll();
                    throw th;
                }
            }
        }
    };
    IDexMergeBinder dexMergeBinder;
    /* access modifiers changed from: private */
    public boolean isBinded;
    /* access modifiers changed from: private */
    public boolean isBinderDied = false;
    /* access modifiers changed from: private */
    public boolean isFinished;
    /* access modifiers changed from: private */
    public boolean isTimeout = true;
    /* access modifiers changed from: private */
    public Object lock = new Object();
    /* access modifiers changed from: private */
    public IBinder.DeathRecipient mDeathRecipient = new MyServiceDeathHandler();
    /* access modifiers changed from: private */
    public long mStartTime;
    /* access modifiers changed from: private */
    public MergeCallback mergeCallBack;

    public DexMergeClient(MergeCallback mergeCallBack2) {
        this.mergeCallBack = mergeCallBack2;
        RuntimeVariables.androidApplication.registerReceiver(new PatchVersionReceiver(), new IntentFilter("com.taobao.atlas.intent.PATCH_VERSION"));
    }

    public boolean prepare() {
        Intent intent = new Intent();
        intent.setClassName(RuntimeVariables.androidApplication, "com.taobao.atlas.dexmerge.DexMergeService");
        this.mStartTime = System.currentTimeMillis();
        if (!RuntimeVariables.androidApplication.bindService(intent, this.conn, 65)) {
            return false;
        }
        if (!this.isBinded) {
            try {
                synchronized (this.lock) {
                    this.lock.wait(60000);
                }
            } catch (InterruptedException e) {
            }
        }
        if (!this.isBinded) {
            RuntimeVariables.androidApplication.unbindService(this.conn);
        }
        return this.isBinded;
    }

    public void unPrepare() {
        RuntimeVariables.androidApplication.unbindService(this.conn);
    }

    public boolean dexMerge(String patchFilePath, List toMergeList, boolean diffBundleDex) {
        if (toMergeList.size() == 0) {
            return true;
        }
        this.mStartTime = System.currentTimeMillis();
        if (!dexMergeInternal(patchFilePath, toMergeList, diffBundleDex) && this.isBinderDied) {
            for (int i = 0; i < 3 && this.isBinderDied; i++) {
                this.isBinderDied = false;
                if (!prepare()) {
                    return this.isFinished;
                }
                if (dexMergeInternal(patchFilePath, toMergeList, diffBundleDex)) {
                    break;
                }
            }
        }
        return this.isFinished;
    }

    private boolean dexMergeInternal(String patchFilePath, List toMergeList, boolean diffBundleDex) {
        this.isFinished = false;
        try {
            this.dexMergeBinder.dexMerge(patchFilePath, toMergeList, diffBundleDex);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return this.isFinished;
    }

    private class MyServiceDeathHandler implements IBinder.DeathRecipient {
        public MyServiceDeathHandler() {
        }

        public void binderDied() {
            synchronized (DexMergeClient.this.lock) {
                boolean unused = DexMergeClient.this.isTimeout = false;
                boolean unused2 = DexMergeClient.this.isBinderDied = true;
                DexMergeClient.this.lock.notifyAll();
            }
            Log.e(DexMergeClient.TAG, "dexMerge service died");
        }
    }
}
