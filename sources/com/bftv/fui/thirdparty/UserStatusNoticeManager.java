package com.bftv.fui.thirdparty;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.bftv.fui.thirdparty.IRemoteFeedback;
import com.bftv.fui.thirdparty.IUserStatusNotice;
import java.util.HashMap;

public class UserStatusNoticeManager {
    private static final UserStatusNoticeManager ourInstance = new UserStatusNoticeManager();
    /* access modifiers changed from: private */
    public HashMap<String, IUserStatusNotice> mCacheMap = new HashMap<>(10);

    public interface OnCallBackListener {
        void onFeedback(Feedback feedback);
    }

    public static UserStatusNoticeManager getInstance() {
        return ourInstance;
    }

    private UserStatusNoticeManager() {
    }

    public void onInterception(Application app, String pck, InterceptionData interceptionData) {
        if (this.mCacheMap.containsKey(pck)) {
            Log.e("Less", "onInterception-mIUserStatusNotice-containsKey:" + pck);
            try {
                this.mCacheMap.get(pck).onInterception(interceptionData);
            } catch (Throwable e) {
                e.printStackTrace();
                Log.e("Less", "other app error:" + pck + "|error message :" + e.getMessage());
            }
        } else {
            Log.e("Less", "onInterception-new Intent");
            Intent intent = new Intent("intent.action.user." + pck);
            intent.setPackage(pck);
            app.bindService(intent, new OnInterceptionConnection(interceptionData), 1);
        }
    }

    public void onRecycling(Application app, String pck, RecyclingData recyclingData) {
        if (this.mCacheMap.containsKey(pck)) {
            Log.e("Less", "onRecycling-mIUserStatusNotice-containsKey:" + pck);
            try {
                this.mCacheMap.get(pck).onRecyclingNotice(recyclingData);
            } catch (Throwable e) {
                e.printStackTrace();
                Log.e("Less", "other app error:" + pck + "|error message :" + e.getMessage());
            }
        } else {
            Log.e("Less", "onRecycling-new Intent");
            Intent intent = new Intent("intent.action.user." + pck);
            intent.setPackage(pck);
            app.bindService(intent, new OnRecyclingConnection(recyclingData), 1);
        }
    }

    public void onAsr(Application app, String pck, String asr, int age, int sex, final OnCallBackListener onCallBackListener) {
        IRemoteFeedback.Stub mStub = new IRemoteFeedback.Stub() {
            public void feedback(Feedback feedback) throws RemoteException {
                if (onCallBackListener != null) {
                    onCallBackListener.onFeedback(feedback);
                }
            }
        };
        if (this.mCacheMap.containsKey(pck)) {
            Log.e("Less", "onAsr-mIUserStatusNotice-containsKey:" + pck);
            try {
                this.mCacheMap.get(pck).onAsr(asr, age, sex, mStub);
            } catch (Throwable e) {
                e.printStackTrace();
                Log.e("Less", "other app error:" + pck + "|error message :" + e.getMessage());
            }
        } else {
            Log.e("Less", "onAsr-new Intent");
            Intent intent = new Intent("intent.action.user." + pck);
            intent.setPackage(pck);
            app.bindService(intent, new OnAsrConnection(mStub, asr, age, sex), 1);
        }
    }

    public void onShow(Application app, String pck) {
        if (this.mCacheMap.containsKey(pck)) {
            Log.e("Less", "onAsr-mIUserStatusNotice-containsKey:" + pck);
            try {
                this.mCacheMap.get(pck).onShow(false);
            } catch (Throwable e) {
                e.printStackTrace();
                Log.e("Less", "other app error:" + pck + "|error message :" + e.getMessage());
            }
        } else {
            Log.e("Less", "onAsr-new Intent");
            Intent intent = new Intent("intent.action.user." + pck);
            intent.setPackage(pck);
            app.bindService(intent, new OnShowConnection(), 1);
        }
    }

    private class OnAsrConnection extends BaseConnection {
        private int mAge;
        private String mAsr;
        private IRemoteFeedback mIRemoteFeedback;
        private int mSex;

        public OnAsrConnection(IRemoteFeedback iRemoteFeedback, String asr, int age, int sex) {
            super();
            this.mAsr = asr;
            this.mAge = age;
            this.mSex = sex;
            this.mIRemoteFeedback = iRemoteFeedback;
        }

        /* access modifiers changed from: protected */
        public void onAchieve(IUserStatusNotice iUserStatusNotice) throws RemoteException {
            iUserStatusNotice.onAsr(this.mAsr, this.mAge, this.mSex, this.mIRemoteFeedback);
        }
    }

    private class OnShowConnection extends BaseConnection {
        public OnShowConnection() {
            super();
        }

        /* access modifiers changed from: protected */
        public void onAchieve(IUserStatusNotice iUserStatusNotice) throws RemoteException {
            iUserStatusNotice.onShow(false);
        }
    }

    private class OnInterceptionConnection extends BaseConnection {
        protected InterceptionData mInterceptionData;

        public OnInterceptionConnection(InterceptionData interceptionData) {
            super();
            this.mInterceptionData = interceptionData;
        }

        /* access modifiers changed from: protected */
        public void onAchieve(IUserStatusNotice iUserStatusNotice) throws RemoteException {
            iUserStatusNotice.onInterception(this.mInterceptionData);
        }
    }

    private class OnRecyclingConnection extends BaseConnection {
        protected RecyclingData mRecyclingData;

        public OnRecyclingConnection(RecyclingData recyclingData) {
            super();
            this.mRecyclingData = recyclingData;
        }

        /* access modifiers changed from: protected */
        public void onAchieve(IUserStatusNotice iUserStatusNotice) throws RemoteException {
            iUserStatusNotice.onRecyclingNotice(this.mRecyclingData);
        }
    }

    private abstract class BaseConnection implements ServiceConnection {
        /* access modifiers changed from: protected */
        public abstract void onAchieve(IUserStatusNotice iUserStatusNotice) throws RemoteException;

        private BaseConnection() {
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            String pck = name.getPackageName();
            Log.e("Less", "UserStatusNoticeManager-onServiceConnected:" + pck);
            IUserStatusNotice iUserStatusNotice = IUserStatusNotice.Stub.asInterface(service);
            UserStatusNoticeManager.this.mCacheMap.put(name.getPackageName(), iUserStatusNotice);
            try {
                onAchieve(iUserStatusNotice);
            } catch (Throwable e) {
                e.printStackTrace();
                Log.e("Less", "other app error:" + pck + "|error message :" + e.getMessage());
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.e("Less", "UserStatusNoticeManager-onServiceDisconnected:" + name.getPackageName());
            UserStatusNoticeManager.this.mCacheMap.remove(name.getPackageName());
        }
    }
}
