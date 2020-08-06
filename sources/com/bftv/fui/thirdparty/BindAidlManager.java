package com.bftv.fui.thirdparty;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.bftv.fui.thirdparty.IAsynRomoteVoice;
import com.bftv.fui.thirdparty.IRemoteVoice;
import java.util.LinkedList;
import java.util.Queue;
import mtopsdk.common.util.SymbolExpUtil;

public class BindAidlManager {
    private static final String TAG = BindAidlManager.class.getSimpleName();
    private static BindAidlManager ourInstance = new BindAidlManager();
    /* access modifiers changed from: private */
    public static Handler sHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            long diff = System.currentTimeMillis() - BindAidlManager.this.mDealMessageStartTime;
            if (BindAidlManager.this.mOnBindTimeListener != null) {
                BindAidlManager.this.mOnBindTimeListener.onBindSuccessTime(diff);
            }
            VoiceThirdLog.l("onServiceConnected:" + className.getPackageName() + "|bing耗时:" + diff);
            IAsynRomoteVoice unused = BindAidlManager.this.mIAsynRomoteVoice = IAsynRomoteVoice.Stub.asInterface(service);
            String msg = (String) BindAidlManager.this.mQueue.poll();
            if (!TextUtils.isEmpty(msg)) {
                String[] str = msg.split(SymbolExpUtil.SYMBOL_VERTICALBAR);
                VoiceThirdLog.l("usetTxt:" + str[0] + "|nlpJson:" + str[1]);
                long key = System.currentTimeMillis();
                BindAidlManager.this.mQueueKey.add(Long.valueOf(key));
                BindAidlManager.this.send(str[0], str[1], key, false);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            IAsynRomoteVoice unused = BindAidlManager.this.mIAsynRomoteVoice = null;
            VoiceThirdLog.l("onServiceDisconnected:" + className.toString());
        }
    };
    /* access modifiers changed from: private */
    public long mDealMessageStartTime;
    /* access modifiers changed from: private */
    public IAsynRomoteVoice mIAsynRomoteVoice;
    private long mMaxEndureTime = 3000;
    /* access modifiers changed from: private */
    public OnBindAidlListener mOnBindAidlListener;
    /* access modifiers changed from: private */
    public OnBindTimeListener mOnBindTimeListener;
    /* access modifiers changed from: private */
    public Queue<String> mQueue = new LinkedList();
    /* access modifiers changed from: private */
    public Queue<Long> mQueueKey = new LinkedList();
    /* access modifiers changed from: private */
    public Runnable mRunable = new Runnable() {
        public void run() {
            BindAidlManager.this.mQueueKey.clear();
            if (BindAidlManager.this.mOnBindAidlListener != null) {
                BindAidlManager.this.mOnBindAidlListener.onTimeOut();
            }
        }
    };

    public interface OnBindAidlListener {
        boolean isVerification();

        void onSuccess(VoiceFeedback voiceFeedback);

        void onTimeOut();

        void onTimeOut(VoiceFeedback voiceFeedback);

        void onVerificationFail();
    }

    public interface OnBindTimeListener {
        void onBindOtherDealTime(long j);

        void onBindSuccessTime(long j);
    }

    public static BindAidlManager getInstance() {
        return ourInstance;
    }

    private BindAidlManager() {
    }

    public void setOnBindTimeListener(OnBindTimeListener onBindTimeListener) {
        this.mOnBindTimeListener = onBindTimeListener;
    }

    public String dealResult(VoiceFeedback voiceFeedback) {
        if (voiceFeedback == null || !voiceFeedback.isHasResult) {
            return null;
        }
        return voiceFeedback.feedback;
    }

    public void dealMessage(Context context, String pck, String userTxt, String nlpJson, OnBindAidlListener onBindAidlListener) {
        if (!onBindAidlListener.isVerification()) {
            Log.e("Less", "isVerification fail return");
            onBindAidlListener.onVerificationFail();
            return;
        }
        this.mDealMessageStartTime = System.currentTimeMillis();
        this.mOnBindAidlListener = onBindAidlListener;
        if (this.mIAsynRomoteVoice == null) {
            Intent intent = new Intent("intent.action.remotevoice." + pck);
            intent.setPackage(pck);
            context.bindService(intent, this.mConnection, 1);
            VoiceThirdLog.l("bindService");
        }
        long key = System.currentTimeMillis();
        this.mQueueKey.add(Long.valueOf(key));
        if (this.mIAsynRomoteVoice == null) {
            this.mQueue.add(userTxt + "|" + nlpJson);
        } else {
            send(userTxt, nlpJson, key, true);
        }
    }

    public void setMaxEndureTime(long time) {
        this.mMaxEndureTime = time;
    }

    /* access modifiers changed from: private */
    public void send(String userTxt, String nlpJson, final long key, boolean isCalculating) {
        VoiceThirdLog.l("send-start");
        if (isCalculating) {
            sHandler.postDelayed(this.mRunable, this.mMaxEndureTime);
        }
        try {
            this.mIAsynRomoteVoice.asynMessage(new IRemoteVoice.Stub() {
                public void sendMessage(VoiceFeedback voiceFeedback) throws RemoteException {
                    if (BindAidlManager.this.mOnBindTimeListener != null) {
                        BindAidlManager.this.mOnBindTimeListener.onBindOtherDealTime(System.currentTimeMillis() - BindAidlManager.this.mDealMessageStartTime);
                    }
                    BindAidlManager.sHandler.removeCallbacks(BindAidlManager.this.mRunable);
                    if (BindAidlManager.this.mQueueKey.contains(Long.valueOf(key))) {
                        if (BindAidlManager.this.mOnBindAidlListener != null) {
                            BindAidlManager.this.mOnBindAidlListener.onSuccess(voiceFeedback);
                        }
                    } else if (BindAidlManager.this.mOnBindAidlListener != null) {
                        BindAidlManager.this.mOnBindAidlListener.onTimeOut(voiceFeedback);
                    }
                }
            }, userTxt, nlpJson);
        } catch (RemoteException e) {
            VoiceThirdLog.l("send-err:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
