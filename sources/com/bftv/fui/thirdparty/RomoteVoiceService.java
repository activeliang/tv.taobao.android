package com.bftv.fui.thirdparty;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.bftv.fui.thirdparty.IAsynRomoteVoice;

public abstract class RomoteVoiceService extends Service {
    private final IAsynRomoteVoice.Stub mBinder = new IAsynRomoteVoice.Stub() {
        public void asynMessage(IRemoteVoice callBack, String userTxt, String nlpJson) throws RemoteException {
            VoiceThirdLog.l("mBinder-sendMessage-userTxt:" + userTxt + "|nlpJson:" + nlpJson);
            RomoteVoiceService.this.send(userTxt, nlpJson, callBack);
        }
    };

    public abstract void send(String str, String str2, IRemoteVoice iRemoteVoice);

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }
}
