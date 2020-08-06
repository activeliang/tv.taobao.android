package com.tvtaobao.voicesdk.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.tvtaobao.voicesdk.ASRInput;
import com.tvtaobao.voicesdk.ASRNotify;
import com.tvtaobao.voicesdk.bean.CommandReturn;
import com.tvtaobao.voicesdk.bean.ConfigVO;
import com.tvtaobao.voicesdk.bean.SearchObject;
import com.tvtaobao.voicesdk.listener.VoiceListener;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.tv.alitvasrsdk.ASRCommandReturn;
import com.yunos.tv.alitvasrsdk.AliTVASRManager;
import com.yunos.tv.alitvasrsdk.AppContextData;
import com.yunos.tv.alitvasrsdk.CommonData;
import com.yunos.tv.alitvasrsdk.OnASRCommandListener;

public class YunosASRService extends Service {
    private static final String TAG = "YunosASRService";
    private AliTVASRManager aliTVASRManager;
    /* access modifiers changed from: private */
    public ASRInput asrInput;
    /* access modifiers changed from: private */
    public boolean isAction = false;
    /* access modifiers changed from: private */
    public Listener listener;
    /* access modifiers changed from: private */
    public OnASRCommandListener onASRCommandListener = new OnASRCommandListener() {
        public void onASRStatusUpdated(OnASRCommandListener.ASRStatus asrStatus, Bundle bundle) {
        }

        public void onASRServiceStatusUpdated(OnASRCommandListener.ASRServiceStatus asrServiceStatus) {
            String msg;
            LogPrint.e(YunosASRService.TAG, "YunosASRService.onASRServiceStatusUpdated status : " + asrServiceStatus);
            if (asrServiceStatus == OnASRCommandListener.ASRServiceStatus.ASR_SERVICE_STATUS_CONNECTED) {
                msg = "语音服务注册成功...(" + Thread.currentThread().getId() + ")";
            } else {
                msg = "语音服务注册失败...(" + Thread.currentThread().getId() + ")";
            }
            LogPrint.e(YunosASRService.TAG, "onASRServiceStatusUpdated : " + msg);
        }

        public ASRCommandReturn onASRResult(String s, boolean b) {
            return null;
        }

        public ASRCommandReturn onNLUResult(String s, String s1, String s2, Bundle bundle) {
            LogPrint.e(YunosASRService.TAG, "onNLUResult s: " + s + " ,s1 : " + s1 + " ,s2 : " + s2 + " ,bundle : " + bundle);
            String mAsr = bundle.getString(CommonData.TYPE_ASR);
            ASRCommandReturn result = new ASRCommandReturn();
            if (!TextUtils.isEmpty(mAsr)) {
                boolean unused = YunosASRService.this.isAction = false;
                synchronized (YunosASRService.this.onASRCommandListener) {
                    try {
                        YunosASRService.this.asrInput.handleInput(mAsr, (String) null, YunosASRService.this.listener);
                        LogPrint.e(YunosASRService.TAG, "onNLUResult isAction : " + YunosASRService.this.isAction + "  ,time : " + System.currentTimeMillis());
                        YunosASRService.this.onASRCommandListener.wait();
                        LogPrint.e(YunosASRService.TAG, "onNLUResult isAction : " + YunosASRService.this.isAction + "  ,time : " + System.currentTimeMillis());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                result.mIsHandled = YunosASRService.this.isAction;
            }
            return result;
        }

        public void getAppContextData(AppContextData appContextData) {
        }

        public Bundle getSceneInfo(Bundle bundle) {
            return null;
        }

        public Bundle asrToClient(Bundle bundle) {
            return null;
        }
    };

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        try {
            this.asrInput = ASRInput.getInstance();
            this.asrInput.setContext(this);
            this.listener = new Listener();
            SearchObject searchObject = new SearchObject();
            searchObject.showUI = true;
            ConfigVO.searchConfig = searchObject;
            ASRNotify.getInstance().setFeedBack(this.listener);
            initAliTvASR(getBaseContext(), false, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        this.asrInput.destroy();
        this.aliTVASRManager.release();
        super.onDestroy();
    }

    public void initAliTvASR(Context context, boolean showUI, int serverMode, int ResultMode) {
        this.aliTVASRManager = new AliTVASRManager();
        this.aliTVASRManager.init(context, showUI);
        this.aliTVASRManager.setOnASRCommandListener(this.onASRCommandListener).setASRListenerType(OnASRCommandListener.ASRListenerType.DEFAULT_LISTENER);
        this.aliTVASRManager.setASRServerMode(serverMode);
        this.aliTVASRManager.setASRResultMode(ResultMode);
        this.aliTVASRManager.showASRUI(true);
    }

    class Listener implements VoiceListener {
        Listener() {
        }

        public void callback(CommandReturn command) {
            LogPrint.i(YunosASRService.TAG, "VoiceListener callback action : " + command.mAction);
            boolean unused = YunosASRService.this.isAction = command.mIsHandled;
            synchronized (YunosASRService.this.onASRCommandListener) {
                try {
                    YunosASRService.this.onASRCommandListener.notify();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            switch (command.mAction) {
                case 1001:
                    LogPrint.i(YunosASRService.TAG, "VoiceListener callback Message : " + command.mMessage);
                    if (!TextUtils.isEmpty(command.mMessage)) {
                        boolean unused2 = YunosASRService.this.playTTS(command.mMessage);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        public void searchResult(String data) {
            LogPrint.i(YunosASRService.TAG, "VoiceListener searchResult");
            boolean unused = YunosASRService.this.isAction = true;
            synchronized (YunosASRService.this.onASRCommandListener) {
                try {
                    YunosASRService.this.onASRCommandListener.notify();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean playTTS(String s) {
        boolean b = false;
        try {
            b = this.aliTVASRManager.playTTS(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogPrint.i(TAG, "playTTS b : " + b);
        return b;
    }

    private void stopTTS() {
        try {
            this.aliTVASRManager.stopTTS();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
