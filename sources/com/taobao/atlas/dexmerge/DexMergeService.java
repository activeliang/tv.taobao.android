package com.taobao.atlas.dexmerge;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.taobao.atlas.dexmerge.IDexMergeBinder;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DexMergeService extends Service {
    private final IDexMergeBinder.Stub mBinder = new IDexMergeBinder.Stub() {
        public void dexMerge(String patchFilePath, List<MergeObject> list, boolean b) throws RemoteException {
            try {
                new MergeExcutorServices(DexMergeService.this.mCallback).excute(patchFilePath, list, b);
            } catch (ExecutionException e) {
                e.printStackTrace();
                if (DexMergeService.this.mCallback != null) {
                    DexMergeService.this.mCallback.onMergeAllFinish(false, "ExecutionException");
                }
            } catch (InterruptedException e2) {
                e2.printStackTrace();
                if (DexMergeService.this.mCallback != null) {
                    DexMergeService.this.mCallback.onMergeAllFinish(false, "InterruptedException");
                }
            }
        }

        public void registerListener(IDexMergeCallback cb) {
            if (cb != null) {
                DexMergeService.this.mCallback = cb;
            }
        }
    };
    IDexMergeCallback mCallback = null;

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }
}
