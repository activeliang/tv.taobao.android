package com.tvtaobao.voicesdk.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;
import com.bftv.fui.tell.TTS;
import com.bftv.fui.tell.TellManager;
import com.bftv.fui.thirdparty.Feedback;
import com.bftv.fui.thirdparty.IRemoteFeedback;
import com.bftv.fui.thirdparty.IUserStatusNotice;
import com.bftv.fui.thirdparty.InterceptionData;
import com.bftv.fui.thirdparty.RecyclingData;
import com.tvtaobao.voicesdk.ASRInput;
import com.tvtaobao.voicesdk.ASRNotify;
import com.tvtaobao.voicesdk.bean.CommandReturn;
import com.tvtaobao.voicesdk.listener.VoiceListener;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.config.AppInfo;

public class BftvService extends Service {
    /* access modifiers changed from: private */
    public String TAG = "BftvService";
    /* access modifiers changed from: private */
    public ASRInput asrInput;
    /* access modifiers changed from: private */
    public ASRNotify asrNotify;
    /* access modifiers changed from: private */
    public Listener mListener;
    private IUserStatusNotice.Stub stub = new IUserStatusNotice.Stub() {
        public void onInterception(InterceptionData interceptionData) throws RemoteException {
            LogPrint.e(BftvService.this.TAG, "onInterception:" + interceptionData.toString());
            BftvService.this.asrInput.handleInput(interceptionData.needValue, (String) null, (VoiceListener) null);
        }

        public void onRecyclingNotice(RecyclingData recyclingData) throws RemoteException {
            LogPrint.e(BftvService.this.TAG, "onRecyclingNotice 回收数据通知" + recyclingData.toString());
        }

        public void onAsr(String s, int i, int i1, IRemoteFeedback iRemoteFeedback) throws RemoteException {
            LogPrint.e(BftvService.this.TAG, "onAsr s : " + s + " ,i : " + i + " ,i1 : " + i1);
            BftvService.this.asrNotify.setFeedBack(BftvService.this.mListener);
            BftvService.this.mListener.destroy();
            BftvService.this.mListener.setIRemoteVoice(iRemoteFeedback, s);
            BftvService.this.asrInput.handleInput(s, (String) null, BftvService.this.mListener);
        }

        public void onShow(boolean b) throws RemoteException {
            LogPrint.e(BftvService.this.TAG, "onShow b : " + b);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    Toast.makeText(BftvService.this, "onShow", 0).show();
                }
            });
        }
    };

    public void onCreate() {
        super.onCreate();
        this.asrNotify = ASRNotify.getInstance();
        this.asrInput = ASRInput.getInstance();
        this.asrInput.setContext(this);
        this.mListener = new Listener();
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return this.stub;
    }

    class Listener implements VoiceListener {
        private String asr_text;
        private IRemoteFeedback iRemoteVoice;

        Listener() {
        }

        /* access modifiers changed from: private */
        public void setIRemoteVoice(IRemoteFeedback iRemoteVoice2, String asr) {
            this.iRemoteVoice = iRemoteVoice2;
            this.asr_text = asr;
        }

        public void destroy() {
            this.iRemoteVoice = null;
        }

        public void callback(CommandReturn command) {
            LogPrint.e(BftvService.this.TAG, BftvService.this.TAG + "Listener callback mIsHandled : " + command.mIsHandled + " ,Action : " + command.mAction);
            try {
                Feedback feed = new Feedback();
                feed.isHasResult = command.mIsHandled;
                switch (command.mAction) {
                    case 1001:
                        if (!TextUtils.isEmpty(command.mMessage)) {
                            TTS tts = new TTS();
                            tts.pck = AppInfo.getPackageName();
                            tts.tts = command.mMessage;
                            tts.userTxt = this.asr_text;
                            tts.isDisplayLayout = command.showUI;
                            TellManager.getInstance().tts(CoreApplication.getApplication(), tts);
                            break;
                        }
                        break;
                }
                if (this.iRemoteVoice != null) {
                    this.iRemoteVoice.feedback(feed);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void searchResult(String data) {
            LogPrint.e(BftvService.this.TAG, BftvService.this.TAG + ".Listener.searchResult");
            try {
                Feedback feed = new Feedback();
                feed.isHasResult = false;
                if (this.iRemoteVoice != null) {
                    this.iRemoteVoice.feedback(feed);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
