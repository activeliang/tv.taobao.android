package com.tvtaobao.voicesdk.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import com.tvtao.sdk.voice.ITVTaoCallBack;
import com.tvtao.sdk.voice.ITVTaoInterface;
import com.tvtaobao.voicesdk.ASRInput;
import com.tvtaobao.voicesdk.ASRNotify;
import com.tvtaobao.voicesdk.bean.CommandReturn;
import com.tvtaobao.voicesdk.listener.VoiceListener;
import com.tvtaobao.voicesdk.utils.LogPrint;

public class TVTaoASRService extends Service {
    /* access modifiers changed from: private */
    public String TAG = "TVTaoASRService";
    /* access modifiers changed from: private */
    public ASRInput asrInput;
    private IBinder mBinder = new ITVTaoInterface.Stub() {
        public void nlpRequest(String asr, String searchConfig, String json, ITVTaoCallBack callback) throws RemoteException {
            LogPrint.i(TVTaoASRService.this.TAG, "nlpRequest asr : " + asr + " ,searchConfig : " + searchConfig + " ,json : " + json);
            TVTaoASRService.this.mListener.destroy();
            TVTaoASRService.this.mListener.setITVTaoCallBack(asr, callback);
            TVTaoASRService.this.asrInput.setMessage(asr, searchConfig, json, TVTaoASRService.this.mListener);
        }
    };
    /* access modifiers changed from: private */
    public Listener mListener;

    public void onCreate() {
        super.onCreate();
        LogPrint.i(this.TAG, "onCreate");
        this.asrInput = ASRInput.getInstance();
        this.asrInput.setContext(this);
        this.mListener = new Listener();
        ASRNotify.getInstance().setFeedBack(this.mListener);
        if (Build.VERSION.SDK_INT >= 26) {
            LogPrint.i(this.TAG, "Android Version -> " + Build.VERSION.SDK_INT);
            ((NotificationManager) getSystemService("notification")).createNotificationChannel(new NotificationChannel("tvtaobao", "TVTAOBAO", 4));
            startForeground(1, new Notification.Builder(getApplicationContext(), "tvtaobao").build());
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        LogPrint.i(this.TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        this.asrInput.destroy();
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    class Listener implements VoiceListener {
        private String _asr;
        private ITVTaoCallBack mCallBack;

        Listener() {
        }

        public void setITVTaoCallBack(String asr, ITVTaoCallBack callBack) {
            this._asr = asr;
            this.mCallBack = callBack;
        }

        public void destroy() {
            this._asr = null;
            this.mCallBack = null;
        }

        public void callback(CommandReturn command) {
            LogPrint.d(TVTaoASRService.this.TAG, "VoiceListener.callback");
            try {
                LogPrint.d(TVTaoASRService.this.TAG, "VoiceListener.callback : " + this.mCallBack);
                command.mASRMessage = this._asr;
                if (this.mCallBack != null) {
                    this.mCallBack.callback(command.toString());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void searchResult(String data) {
            LogPrint.d(TVTaoASRService.this.TAG, "VoiceListener.searchResult");
            try {
                if (this.mCallBack != null) {
                    this.mCallBack.searchResult(this._asr, data);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
